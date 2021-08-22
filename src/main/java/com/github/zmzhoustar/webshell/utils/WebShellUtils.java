/*
 * Copyright © 2020-present zmzhou-star. All Rights Reserved.
 */

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
    private WebShellUtils() {
    }

    /**
	 * 从WebSocketSession获取用户名
	 * @param webSocketSession WebSocketSession
	 * @author zmzhou
	 * @date 2021/2/23 20:47
	 */
	public static String getUuid(WebSocketSession webSocketSession){
		return String.valueOf(webSocketSession.getAttributes().get(Constants.USER_UUID_KEY));
	}

	/**
	 * Gets session id.
	 *
	 * @return the session id
	 * @author zmzhou
	 * @date 2021/3/1 14:36
	 */
	public static String getSessionId() {
		return SpringUtils.getSession().getId();
	}

	/**
	 * 文件大小转换单位
	 * @param size 文件大小
	 * @author zmzhou
	 * @date 2021/3/1 17:53
	 */
	public static String convertFileSize(long size) {
		long kb = Constants.KB;
		long mb = kb * Constants.KB;
		long gb = mb * Constants.KB;
		String fileSize;
		if (size >= gb) {
			fileSize = String.format("%.1fGB", (float)size / (float)gb);
		} else {
			float f;
			if (size >= mb) {
				f = (float)size / (float)mb;
				fileSize = String.format(f > 100.0F ? "%.0fMB" : "%.1fMB", f);
			} else if (size >= kb) {
				f = (float)size / (float)kb;
				fileSize = String.format(f > 100.0F ? "%.0fKB" : "%.1fKB", f);
			} else {
				fileSize = String.format("%dB", size);
			}
		}
		return fileSize;
	}
}
