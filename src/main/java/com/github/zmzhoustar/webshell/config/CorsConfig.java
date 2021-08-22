/*
 * Copyright © 2020-present zmzhou-star. All Rights Reserved.
 */

package com.github.zmzhoustar.webshell.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * CORS(Cross Origin Resourse-Sharing) - 跨站资源共享
 * CSRF(Cross-Site Request Forgery) - 跨站请求伪造
 *
 * @author zmzhou
 * @version 1.0
 * @since 2021/5/11 16:28
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cors")
public class CorsConfig {
	/**
	 * 支持跨域请求的请求头信息字段
	 */
	private String[] allowedHeaders = {"*"};
	/**
	 * 支持跨域请求的方法
	 */
	private String[] allowedMethods = {"POST", "GET", "PUT", "DELETE", "OPTIONS", "HEAD"};
	/**
	 * 允许跨域请求的域名
	 */
	private String[] allowedOriginPatterns = {"*"};
	/**
	 * 是否允许发送Cookie
	 */
	private boolean allowCredentials = true;
	/**
	 * 本次请求的有效期
	 */
	private long maxAge = 1800L;
}
