/*
 * Copyright © 2020-present zmzhou-star. All Rights Reserved.
 */

package com.github.zmzhoustar.webshell.interceptor;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.github.zmzhoustar.webshell.Constants;

import lombok.extern.slf4j.Slf4j;

/**
 * WebSocket拦截器
 *
 * @author zmzhou
 * @version 1.0
 * @title WebSocketInterceptor
 * @date 2021/1/31 13:18
 */
@Slf4j
public class WebSocketInterceptor implements HandshakeInterceptor {
	/**
	 * 处理前beforeHandshake
	 * @author zmzhou
	 * @date 2021/1/31 13:21
	 */
	@Override
	public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
								   WebSocketHandler webSocketHandler, Map<String, Object> map) {
		if (serverHttpRequest instanceof ServletServerHttpRequest) {
			//生成一个UUID
			String uuid = UUID.randomUUID().toString().replace("-", "");
			//将uuid放到websocketsession中
			map.put(Constants.USER_UUID_KEY, uuid);
			return true;
		}
		return false;
	}

	@Override
	public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
							   WebSocketHandler webSocketHandler, Exception e) {
		log.info("afterHandshake");
	}
}
