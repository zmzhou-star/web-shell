/*
 * Copyright © 2020-present zmzhou-star. All Rights Reserved.
 */

package com.github.zmzhoustar.webshell.listener;

import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * springboot生命周期
 * @author zmzhou
 * @version 1.0
 * date 2021/4/20 13:54
 */
@Slf4j
public class ApplicationEventListener implements ApplicationListener<ApplicationEvent> {

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		// 在这里可以监听到Spring Boot的生命周期
		if (event instanceof ApplicationStartingEvent) {
			log.info("应用程序启动中");
		} else if (event instanceof ApplicationEnvironmentPreparedEvent) {
			log.info("初始化环境变量");
		} else if (event instanceof ApplicationContextInitializedEvent) {
			log.info("ApplicationContext初始化完成");
		} else if (event instanceof ApplicationPreparedEvent) {
			log.info("Spring容器执行refresh前触发");
		} else if (event instanceof ServletWebServerInitializedEvent) {
			log.info("Servlet Web服务器初始化");
		} else if (event instanceof ContextRefreshedEvent) {
			log.info("应用Context刷新");
		} else if (event instanceof ApplicationStartedEvent) {
			log.info("应用程序启动好了");
		} else if (event instanceof ApplicationReadyEvent) {
			log.info("应用已准备完成");
		} else if (event instanceof AvailabilityChangeEvent) {
			log.info("应用已处于活动状态");
		} else if (event instanceof ApplicationFailedEvent) {
			log.info("应用启动失败");
		} else if (event instanceof ContextStoppedEvent) {
			log.info("应用停止");
		} else if (event instanceof ContextClosedEvent) {
			log.info("应用关闭");
		} else {
			log.info("其他事件:{}", event);
		}
	}
}
