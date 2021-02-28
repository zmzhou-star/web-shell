package com.github.zmzhoustar.webshell.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

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
		Optional.ofNullable(files).orElse(new Vector<>()).forEach(file -> {
			ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) file;
			// 文件夹图标
			String icon = "jstree-folder";
			if (lsEntry.getLongname().startsWith(Constants.MINUS)) {
				// 文件图标
				icon = "jstree-file";
			}
			SftpFileTreeVo vo = SftpFileTreeVo.builder()
					.id(finalParentPath + lsEntry.getFilename())
					.parent(finalParentPath)
					.text(lsEntry.getFilename())
					.icon(icon)
					.build();
			fileTree.add(vo);
		});
		return fileTree;
	}
	/**
	 * 私有构造器
	 * @author zmzhou
	 * @date 2021/2/28 21:53
	 */
	private SftpFileUtils() {
	}
}
