package com.github.zmzhoustar.webshell.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

/**
 * 线程工具类
 *
 * @author zmzhou
 * @version 1.0
 * @title ThreadPoolUtils
 * @date 2020/9/13 18:08
 */
@Slf4j
public final class ThreadPoolUtils {
	/**
	 * ThreadPoolTaskExecutor
	 */
	private static ThreadPoolTaskExecutor threadPool = null;

	/**
	 * 工具类私有化构造器
	 * @author zmzhou
	 * @date 2020/9/13 18:20
	 */
	private ThreadPoolUtils() {
	}

	/**
	 * 无返回值直接执行
	 *
	 * @param runnable Runnable
	 * @author zmzhou
	 * @date 2020/9/13 18:09
	 */
	public static void execute(Runnable runnable) {
		get().execute(runnable);
	}

	/**
	 * 有返回值直接执行
	 *
	 * @param callable Callable
	 * @return Future<T> future.get()获取返回值
	 * @author zmzhou
	 * @date 2020/9/13 18:10
	 */
	public static <T> Future<T> submit(Callable<T> callable) {
		return get().submit(callable);
	}

	/**
	 * 获取线程池对象
	 * @return ThreadPoolTaskExecutor
	 * @author zmzhou
	 * @date 2020/9/13 18:21
	 */
	private static ThreadPoolTaskExecutor get() {
		if (threadPool != null) {
			log.debug("线程池已创建");
			return threadPool;
		} else {
			synchronized (ThreadPoolUtils.class) {
				//二次检查
				if (threadPool == null) {
					// 获取处理器数量 Ncpu = CPU核心数
					int cpuNum = Runtime.getRuntime().availableProcessors();
					// 根据cpu数量,计算出合理的线程并发数
					// Nthreads = Ncpu x Ucpu x (1 + W/C)，其中 Ucpu = CPU使用率，0~1；W/C = 等待时间与计算时间的比率
					int threadNum = cpuNum * 2;
					// 创建线程池
					threadPool = new ThreadPoolTaskExecutor();
					threadPool.setThreadNamePrefix("web-shell-thread");
					// 核心线程数
					threadPool.setCorePoolSize(threadNum);
					// 最大线程数
					threadPool.setMaxPoolSize(threadNum + 1);
					// 队列已满,而且当前线程数已经超过最大线程数时的异常处理策略 来电运行政策
					threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
					// 初始化
					threadPool.initialize();
					log.info("创建线程池完成:{}", threadPool.toString());
				}
			}
		}
		return threadPool;
	}
}
