/*
 * Copyright © 2020-present zmzhou-star. All Rights Reserved.
 */

package com.github.zmzhoustar.webshell.utils;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import lombok.extern.slf4j.Slf4j;

/**
 * ehcache缓存操作工具类
 *
 * @author zmzhou
 * @version 1.0
 * @title EhCacheUtils
 * @date 2021/3/1 11:56
 */
@Slf4j
public final class EhCacheUtils {
	/** CacheManager */
	private static final CacheManager CACHE_MANAGER = SpringUtils.getBean(CacheManager.class);

	/**
	 * 获取Cache
	 *
	 * @author zmzhou
	 * @date 2021/3/1 12:49
	 */
	public static Cache getCache() {
		return CACHE_MANAGER.getCache("myCache");
	}

	/**
	 * 添加缓存数据
	 *
	 * @param key   键
	 * @param value 值
	 * @author zmzhou
	 * @date 2021/3/1 12:50
	 */
	public static void put(String key, Object value) {
		try {
			Cache cache = getCache();
			cache.put(key, value);
		} catch (Exception e) {
			log.error("添加缓存失败：{}", e.getMessage());
		}
	}

	/**
	 * 获取缓存数据
	 *
	 * @param key 键
	 * @return 缓存数据
	 * @author zmzhou
	 * @date 2021/3/1 12:53
	 */
	public static <T> T get(String key) {
		try {
			Cache cache = getCache();
			return (T) cache.get(key).get();
		} catch (Exception e) {
			log.error("获取缓存数据失败：", e);
			return null;
		}
	}

	/**
	 * 删除缓存数据
	 *
	 * @param key 键
	 * @author zmzhou
	 * @date 2021/3/1 12:53
	 */
	public static void delete(String key) {
		try {
			Cache cache = getCache();
			cache.evict(key);
		} catch (Exception e) {
			log.error("删除缓存数据失败：", e);
		}
	}

	/**
	 * @author zmzhou
	 * @date 2021/3/1 12:02
	 */
	private EhCacheUtils() {
	}
}
