/*
 * Copyright © 2020-present zmzhou-star. All Rights Reserved.
 */

package com.github.zmzhoustar.webshell.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.zmzhoustar.webshell.utils.WebShellUtils;
import com.github.zmzhoustar.webshell.vo.ApiResult;

import lombok.extern.slf4j.Slf4j;

/**
 * 桌面版控制层
 *
 * @author zmzhou
 * @version 1.0
 * @date 2021/3/6 19:41
 */
@Slf4j
@RequestMapping("/")
@RestController
public class ElectronController {
	/**
	 * 登录
	 *
	 * @param username 用户名
	 * @param password 密码
	 * @return com.github.zmzhoustar.webshell.vo.ApiResult<java.lang.Object>
	 * @author zmzhou
	 * @since 2021/8/22 19:06
	 */
	@PostMapping("/user/login")
	public ApiResult<Object> login(String username, String password){
		String token = WebShellUtils.getSessionId();
		return ApiResult.builder().data(token);
	}

	/**
	 * 获取用户信息
	 *
	 * @param token token
	 * @return com.github.zmzhoustar.webshell.vo.ApiResult<com.alibaba.fastjson.JSONObject>
	 * @author zmzhou
	 * @since 2021/8/22 19:07
	 */
	@GetMapping("/user/info")
	public ApiResult<JSONObject> userInfo(String token){
		ApiResult<JSONObject> res = ApiResult.builder();
		JSONObject json = new JSONObject();
		json.put("roles", "admin");
		json.put("name", "zmzhou");
		return res.data(json);
	}
}
