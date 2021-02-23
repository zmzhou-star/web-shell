package com.github.zmzhoustar.webshell.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.github.zmzhoustar.webshell.handler.WebShellWebSocketHandler;
import com.github.zmzhoustar.webshell.interceptor.WebSocketInterceptor;

/**
 * websocket配置 
 * @title WebSocketConfig
 * @author zmzhou
 * @version 1.0
 * @date 2021/1/31 13:13
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	@Resource
	private WebShellWebSocketHandler webSocketHandler;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
		//socket通道
		//指定处理器和路径
		webSocketHandlerRegistry.addHandler(webSocketHandler, "/shell")
				.addInterceptors(new WebSocketInterceptor())
				.setAllowedOrigins("*");
	}
}
