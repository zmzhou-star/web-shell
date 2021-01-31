package com.github.zmzhoustar.webshell.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
	@RequestMapping({"/", "/index"})
	public String index() {
		return "index";
	}
}
