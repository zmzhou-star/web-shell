package com.github.zmzhoustar.webshell.utils;

import org.springframework.web.socket.WebSocketSession;

import com.github.zmzhoustar.webshell.Constants;

/**
 * 常用工具类 
 * @title WebShellUtils
 * @author zmzhou
 * @version 1.0
 * @date 2021/2/23 20:45
 */
public final class WebShellUtils {
	/**
	 * 从WebSocketSession获取用户名 
	 * @param webSocketSession WebSocketSession
	 * @author zmzhou
	 * @date 2021/2/23 20:47
	 */
	public static String getUserName(WebSocketSession webSocketSession){
		return String.valueOf(webSocketSession.getAttributes().get(Constants.USER_UUID_KEY));
	}
}
