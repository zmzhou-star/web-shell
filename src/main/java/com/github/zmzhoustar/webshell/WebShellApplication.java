package com.github.zmzhoustar.webshell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序入口 
 * @title WebShellApplication
 * @author zmzhou
 * @version 1.0
 * @date 2021/1/30 23:00
 */
@SpringBootApplication
public class WebShellApplication {

	public static void main(String[] args) {
		// log4j2全局异步日志配置 http://logging.apache.org/log4j/2.x/manual/async.html#AllAsync
		System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
		SpringApplication.run(WebShellApplication.class, args);
		System.out.println("\u9759\u6001\u8D44\u6E90\u7684\u914D\u7F6E\u4F4D\u7F6E\uFF0C\u53EF\u4EE5" +
				"\u5199\u6210\u4E00\u4E2A\u6570\u7EC4\u914D\u7F6E\u591A\u4E2A\u76EE\u5F55");
	}

}
