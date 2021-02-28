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
public class SftpFileTreeVo {
	/** id,文件路径 */
	private String id;
	/** 父路径 */
	private String parent;
	/** 文件名 */
	private String text;
	/** jstree图标 */
	private String icon;
}
