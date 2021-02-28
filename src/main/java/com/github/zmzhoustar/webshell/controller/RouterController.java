package com.github.zmzhoustar.webshell.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.alibaba.fastjson.JSON;
import com.github.zmzhoustar.webshell.utils.SecretUtils;
import com.github.zmzhoustar.webshell.utils.SftpFileUtils;
import com.github.zmzhoustar.webshell.utils.SftpUtils;
import com.github.zmzhoustar.webshell.utils.SpringUtils;
import com.github.zmzhoustar.webshell.vo.SftpFileTreeVo;
import com.github.zmzhoustar.webshell.vo.WebShellData;

import lombok.extern.slf4j.Slf4j;

/**
 * 路由控制类
 *
 * @author zmzhou
 * @version 1.0
 * @title RouterController
 * @date 2021/1/30 23:32
 */
@Slf4j
@Controller
public class RouterController {
	/** 存放ssh连接信息的map */
	private static final Map<String, WebShellData> SFTP_MAP = new ConcurrentHashMap<>();
	
	/**
	 * index
	 * @author zmzhou
	 * @date 2021/1/30 23:33
	 */
	@GetMapping({"/", "/index"})
	public String index() {
		return "index";
	}

	/**
	 * sftp
	 * @author zmzhou
	 * @date 2021/2/26 16:40
	 */
	@GetMapping("/sftp")
	public String sftp(String params, Model model) {
		String sessionId = SpringUtils.getSession().getId();
		log.info("sessionId：{}", sessionId);
		WebShellData sshData = JSON.parseObject(params, WebShellData.class);
		if (sshData != null) {
			SFTP_MAP.put(sessionId, sshData);
		} else {
			sshData = SFTP_MAP.get(sessionId);
		}
		if (sshData != null) {
			SftpUtils sftpUtils = new SftpUtils(sshData.getUsername(),
					SecretUtils.decrypt(sshData.getPassword(), SecretUtils.AES_KEY),
					sshData.getHost(), sshData.getPort());
			boolean login = sftpUtils.login();
			// 登录成功状态
			model.addAttribute("login", login);
			List<SftpFileTreeVo> fileTree = SftpFileUtils.getFileTree(sftpUtils, "/");
			model.addAttribute("fileTree", fileTree);
		}
		return "sftp";
	}
}
