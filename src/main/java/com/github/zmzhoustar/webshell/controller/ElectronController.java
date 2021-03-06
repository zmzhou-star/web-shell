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
 * @title ElectronController
 * @author zmzhou
 * @version 1.0
 * @date 2021/3/6 19:41
 */
@Slf4j
@RequestMapping("/")
@RestController
public class ElectronController {
	/**
	 * TODO 
	 * @param 
	 * @author zmzhou
	 * @date 2021/3/6 19:45
	 */
	@PostMapping("/user/login")
	public ApiResult<Object> login(String username, String password){
		String token = WebShellUtils.getSessionId();
		return ApiResult.builder().data(token);
	}
	
	@GetMapping("/user/info")
	public ApiResult<JSONObject> userInfo(String token){
		ApiResult<JSONObject> res = ApiResult.builder();
		JSONObject json = new JSONObject();
		json.put("roles", "admin");
		json.put("name", "zmzhou");
		return res.data(json);
	}
}
