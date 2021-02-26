package com.github.zmzhoustar.webshell.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.alibaba.fastjson.JSON;

/**
 * 路由控制类
 *
 * @author zmzhou
 * @version 1.0
 * @title RouterController
 * @date 2021/1/30 23:32
 */
@Controller
public class RouterController {
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
		model.addAttribute("params", JSON.parseObject(params));
		return "sftp";
	}
}
