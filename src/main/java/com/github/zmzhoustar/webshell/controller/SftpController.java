package com.github.zmzhoustar.webshell.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.zmzhoustar.webshell.utils.EhCacheUtils;
import com.github.zmzhoustar.webshell.utils.SecretUtils;
import com.github.zmzhoustar.webshell.utils.SftpFileUtils;
import com.github.zmzhoustar.webshell.utils.SftpUtils;
import com.github.zmzhoustar.webshell.utils.WebShellUtils;
import com.github.zmzhoustar.webshell.vo.ApiResult;
import com.github.zmzhoustar.webshell.vo.SftpFileTreeVo;
import com.github.zmzhoustar.webshell.vo.WebShellData;

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
		WebShellData sshData = EhCacheUtils.get(sessionId);
		ApiResult<List<SftpFileTreeVo>> result = new ApiResult<>();
		// 存放ssh连接信息
		if (sshData != null) {
			SftpUtils sftpUtils = new SftpUtils(sshData.getUsername(),
					SecretUtils.decrypt(sshData.getPassword(), SecretUtils.AES_KEY),
					sshData.getHost(), sshData.getPort());
			if (sftpUtils.login()) {
				List<SftpFileTreeVo> fileTree = SftpFileUtils.getFileTree(sftpUtils, path);
				result.setData(fileTree);
				sftpUtils.logout();
			}
		}
		return result;
	}
}
