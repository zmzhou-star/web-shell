/*
 * Copyright © 2020-present zmzhou-star. All Rights Reserved.
 */

package com.github.zmzhoustar.webshell.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.zmzhoustar.webshell.Constants;
import com.github.zmzhoustar.webshell.utils.EhCacheUtils;
import com.github.zmzhoustar.webshell.utils.SftpFileUtils;
import com.github.zmzhoustar.webshell.utils.SftpUtils;
import com.github.zmzhoustar.webshell.utils.WebShellUtils;
import com.github.zmzhoustar.webshell.vo.ApiResult;
import com.github.zmzhoustar.webshell.vo.SftpFileTreeVo;
import com.github.zmzhoustar.webshell.vo.WebShellData;
import com.jcraft.jsch.SftpException;

import lombok.extern.slf4j.Slf4j;

/**
 * SFTP控制层
 * SFTP是Secure File Transfer Protocol的缩写，安全文件传送协议
 * @title SftpController
 * @author zmzhou
 * @version 1.0
 * @date 2021/3/1 13:54
 */
@Slf4j
@RequestMapping("/sftp")
@RestController
public class SftpController {
	/**
	 * 获取文件列表
	 * @param path 路径
	 * @return 文件列表
	 * @author zmzhou
	 * @date 2021/3/1 14:10
	 */
	@GetMapping("getFileTree")
	public ApiResult<List<SftpFileTreeVo>> getFileTree(String path) {
		String sessionId = WebShellUtils.getSessionId();
		log.info("sessionId：{}", sessionId);
		// 存放ssh连接信息
		WebShellData sshData = EhCacheUtils.get(sessionId);
		ApiResult<List<SftpFileTreeVo>> result = new ApiResult<>();
		if (sshData != null) {
			SftpUtils sftpUtils = new SftpUtils(sshData);
			if (sftpUtils.login()) {
				List<SftpFileTreeVo> fileTree = SftpFileUtils.getFileTree(sftpUtils, path);
				result.setData(fileTree);
				sftpUtils.logout();
			}
		}
		return result;
	}

	/**
	 * 上传文件到服务器
	 * @param request HttpServletRequest
	 * @return
	 * @author zmzhou
	 * @date 2021/3/2 23:07
	 */
	@PostMapping("/upload")
	public ApiResult<String> upload(HttpServletRequest request) {
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		String sessionId = WebShellUtils.getSessionId();
		log.info("sessionId：{}", sessionId);
		// 存放ssh连接信息
		WebShellData sshData = EhCacheUtils.get(sessionId);
		// 上传目标文件夹
		String directory = request.getParameter("path");
		// 返回值
		AtomicReference<String> res = new AtomicReference<>("上传成功！");
		if (sshData != null) {
			SftpUtils sftpUtils = new SftpUtils(sshData);
			if (sftpUtils.login()) {
				files.forEach(file -> {
					String fileName = file.getOriginalFilename();
					try {
						sftpUtils.upload(directory, fileName, file.getInputStream());
					} catch (SftpException | IOException e) {
						log.error("上传文件失败：{}", fileName, e);
						res.set("上传失败！");
					}
				});
				sftpUtils.logout();
			}
		}
		ApiResult<String> result = ApiResult.builder();
		return result.data(res.get());
	}

	/**
	 * 从服务器下载文件
	 * @param path 服务器文件路径
	 * @author zmzhou
	 * @date 2021/3/2 20:46
	 */
	@GetMapping("/download")
	public void download(String path, HttpServletResponse response) {
		if (StringUtils.isBlank(path)) {
			return;
		}
		String sessionId = WebShellUtils.getSessionId();
		log.info("sessionId：{}", sessionId);
		// 文件名
		String fileName = path.substring(path.lastIndexOf(Constants.SEPARATOR) + 1);
		// 存放ssh连接信息
		WebShellData sshData = EhCacheUtils.get(sessionId);
		if (sshData != null) {
			SftpUtils sftpUtils = new SftpUtils(sshData);
			if (sftpUtils.login()) {
				// 设置信息给客户端不解析
				String type = new MimetypesFileTypeMap().getContentType(path);
				// 设置content-type，即告诉客户端所发送的数据属于什么类型
				response.setHeader("Content-type", type);
				// 设置强制下载不打开
				response.setContentType("application/force-download");
				// 设置文件名
				response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
				byte[] buffer = new byte[Constants.BUFFER_SIZE];
				try (InputStream fis = sftpUtils.download(path);
					 BufferedInputStream bis = new BufferedInputStream(fis)) {
					OutputStream os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
				} catch (Exception e) {
					log.error("下载文件:{}失败", path, e);
				}
				sftpUtils.logout();
			}
		}
	}

	/**
	 * 删除文件
	 * @param path 文件路径
	 * @return 删除结果
	 * @author zmzhou
	 * @date 2021/3/4 21:05
	 */
	@DeleteMapping
	public ApiResult<String> deleteFile(String path){
		ApiResult<String> result = ApiResult.builder();
		if (StringUtils.isBlank(path)) {
			return result.error(404, "文件路径为空！");
		}
		String sessionId = WebShellUtils.getSessionId();
		log.info("sessionId：{}，删除文件path：{}", sessionId, path);
		// 存放ssh连接信息
		WebShellData sshData = EhCacheUtils.get(sessionId);
		if (sshData != null) {
			SftpUtils sftpUtils = new SftpUtils(sshData);
			if (sftpUtils.login()) {
				// 删除文件
				if (!sftpUtils.delete(path)) {
					return result.error(500, "删除文件失败！");
				}
				sftpUtils.logout();
			}
		}
		return result.data("删除成功!");
	}
}
