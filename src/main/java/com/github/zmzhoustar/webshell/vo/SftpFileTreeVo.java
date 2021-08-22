/*
 * Copyright © 2020-present zmzhou-star. All Rights Reserved.
 */

package com.github.zmzhoustar.webshell.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 文件树图vo
 * @title SftpFileTreeVo
 * @author zmzhou
 * @version 1.0
 * @date 2021/2/28 21:49
 */
@Data
@Builder
public class SftpFileTreeVo implements Comparable<SftpFileTreeVo> {
	/** id,文件路径 */
	private String id;
	/** 父路径 */
	private String parent;
	/** 文件名 */
	private String text;
	/** jstree图标 */
	private String icon;

	/** 文件详情：文件类型 */
	private String fileType;
	/** 文件详情：文件属性 */
	private String fileAttr;
	/** 文件详情：目录/链接个数 */
	private String numberOfDir;
	/** 文件详情：所有者 */
	private String owner;
	/** 文件详情：组 */
	private String group;
	/** 文件详情：文件大小 */
	private String size;
	/** 文件详情：修改日期 */
	private String modifiedDate;

	/**
	 * Compares this object with the specified object for order.  Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.
	 */
	@Override
	public int compareTo(SftpFileTreeVo vo) {
		// 先根据文件类型排序，再根据文件名排序
		int ic = vo.getIcon().compareTo(icon);
		if (ic == 0) {
			return text.compareTo(vo.getText());
		}
		return ic;
	}
}
