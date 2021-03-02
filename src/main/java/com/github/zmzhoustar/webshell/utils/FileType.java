package com.github.zmzhoustar.webshell.utils;

/**
 * 文件类型枚举
 * @title FileType
 * @author zmzhou
 * @version 1.0
 * @date 2021/3/2 10:14
 */
public enum FileType {
	/** 普通文件 */
	NORMAL_FILE("-", "普通文件"),
	/** 目录 */
	DIRECTORY("d", "目录"),
	/** 链接文件 */
	LINK_FILE("l", "链接文件"),
	/** 管理文件 */
	MANAGE_FILE("p", "管理文件"),
	/** 块设备文件 */
	BLOCK_DEVICE_FILE("b", "块设备文件"),
	/** 字符设备文件 */
	CHARACTER_DEVICE_FILE("c", "字符设备文件"),
	/** 套接字文件 */
	SOCKET_FILE("s", "套接字文件");

	/** 文件类型字符 */
	private final String sign;
	/** 文件类型中文名 */
	private final String zhName;

	FileType(String sign, String zhName) {
		this.sign = sign;
		this.zhName = zhName;
	}

	/**
	 * Gets zh name.
	 *
	 * @param sign the sign
	 * @return the zh name
	 */
	public static String getZhName(String sign) {
		FileType[] types = FileType.values();
		for (FileType type : types) {
			if (type.getSign().equals(sign)) {
				return type.getZhName();
			}
		}
		return NORMAL_FILE.zhName;
	}

	/**
	 * 获取文件类型图标
	 * @param fileType 文件类型
	 * @return 图片地址或css class Name
	 * @author zmzhou
	 * @date 2021/3/2 11:29
	 */
	public static String getFileTypeIcon(String fileType) {
		String icon;
		if (fileType.equals(NORMAL_FILE.getSign())) {
			// 文件图标
			icon = "jstree-file";
		} else if (fileType.equals(DIRECTORY.getSign())) {
			// 文件夹图标
			icon = "jstree-folder";
		} else {
			icon = "/static/img/" + fileType + ".png";
		}
		return icon;
	}

	/**
	 * Gets sign.
	 *
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * Gets zh name.
	 *
	 * @return the zh name
	 */
	public String getZhName() {
		return zhName;
	}

}
