/*
 * Copyright © 2020-present zmzhou-star. All Rights Reserved.
 */

package com.github.zmzhoustar.webshell.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决前后端分离跨域请求
 *
 * @author zmzhou
 * @version 1.0
 * @title WebMvcConfig
 * @date 2021/3/6 19:49
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Autowired
	private CorsConfig corsConfig;

	/**
	 * 跨站资源共享配置
	 * Cross Origin Resourse-Sharing - 跨站资源共享
	 *
	 * @param registry CorsRegistry
	 * @author zmzhou
	 * @since 2021/8/22 18:46
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// Add more mappings... 可以添加多个mapping
		registry.addMapping("/**")
				// 服务器支持的所有头信息字段
				.allowedHeaders(corsConfig.getAllowedHeaders())
				// 服务器支持的所有跨域请求的方法
				.allowedMethods(corsConfig.getAllowedMethods())
				// 是否允许发送Cookie
				.allowCredentials(corsConfig.isAllowCredentials())
				// 指定本次请求的有效期
				.maxAge(corsConfig.getMaxAge())
				// 设置允许跨域请求的域名
				.allowedOriginPatterns(corsConfig.getAllowedOriginPatterns());
	}
}
