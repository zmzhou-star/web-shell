package com.github.zmzhoustar.webshell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 程序入口
 * @title WebShellApplication
 * @author zmzhou
 * @version 1.0
 * @date 2021/1/30 23:00
 */
@SpringBootApplication
@EnableCaching
public class WebShellApplication {

	public static void main(String[] args) {
		// log4j2全局异步日志配置 http://logging.apache.org/log4j/2.x/manual/async.html#AllAsync
		System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
		SpringApplication.run(WebShellApplication.class, args);
	}

}
