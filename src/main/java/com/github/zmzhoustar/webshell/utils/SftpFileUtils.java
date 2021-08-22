/*
 * Copyright © 2020-present zmzhou-star. All Rights Reserved.
 */

package com.github.zmzhoustar.webshell.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.zmzhoustar.webshell.Constants;
import com.github.zmzhoustar.webshell.vo.SftpFileTreeVo;
import com.jcraft.jsch.ChannelSftp;

/**
 * sftp文件工具类
 * @title SftpFileUtils
 * @author zmzhou
 * @version 1.0
 * @date 2021/2/28 21:52
 */
public final class SftpFileUtils {
	/** 长文件名正则表达式 */
	private static final Pattern FILE_PATTERN = Pattern.compile(
			"^([-dlpbcsrwx]{10})\\s+([0-9]+)\\s+([0-9a-zA-Z]+)\\s+([0-9a-zA-Z]+)\\s+([0-9]+)\\s+([0-9a-zA-Z:\\s]+)\\s+");
	/**
	 * 获取文件列表
	 * @param sftpUtils SftpUtils
	 * @param path 路径
	 * @return 文件列表
	 * @author zmzhou
	 * @date 2021/2/28 22:04
	 */
	public static List<SftpFileTreeVo> getFileTree(SftpUtils sftpUtils, String path) {
		List<SftpFileTreeVo> fileTree = new ArrayList<>();
		String parentPath = path;
		if (!parentPath.endsWith(Constants.SEPARATOR)) {
			parentPath = path + Constants.SEPARATOR;
		}
		Vector<?> files = sftpUtils.listFiles(parentPath);
		String finalParentPath = parentPath;
		files.forEach(file -> {
			ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) file;
			String fileType = lsEntry.getLongname().substring(0, 1);
			// 文件类型图标
			String icon = FileType.getFileTypeIcon(fileType);
			SftpFileTreeVo vo = SftpFileTreeVo.builder()
					.id(finalParentPath + lsEntry.getFilename())
					.parent(finalParentPath)
					.text(lsEntry.getFilename())
					.icon(icon)
					.build();
			// 匹配文件详情
			Matcher m = FILE_PATTERN.matcher(lsEntry.getLongname());
			if (m.find()) {
				vo.setFileType(FileType.getZhName(m.group(1).substring(0, 1)));
				vo.setFileAttr(m.group(1).substring(1));
				vo.setNumberOfDir(m.group(2));
				vo.setOwner(m.group(3));
				vo.setGroup(m.group(4));
				vo.setSize(WebShellUtils.convertFileSize(Long.parseLong(m.group(5))));
				vo.setModifiedDate(m.group(6));
			}
			fileTree.add(vo);
		});
		// 排序
		Collections.sort(fileTree);
		return fileTree;
	}
	/**
	 * 获取文件所属用户
	 * @param longName 文件详情
	 * @return 文件所属用户
	 * @author zmzhou
	 * @date 2021/3/7 19:37
	 */
	public static String getOwner(String longName){
		// 正则匹配长文件详情
		Matcher m = FILE_PATTERN.matcher(longName);
		if (m.find()) {
			return m.group(3);
		}
		return "";
	}
	/**
	 * 私有构造器
	 * @author zmzhou
	 * @date 2021/2/28 21:53
	 */
	private SftpFileUtils() {
	}
}
