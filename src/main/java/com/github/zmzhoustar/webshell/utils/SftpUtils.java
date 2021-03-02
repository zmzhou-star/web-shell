package com.github.zmzhoustar.webshell.utils;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;

import com.github.zmzhoustar.webshell.Constants;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

import lombok.extern.slf4j.Slf4j;

/**
 * SFTP服务器工具类
 * SftpUtils
 *
 * @author zmzhou
 * @version 1.0
 * @date 2021/2/25 11:46
 */
@Slf4j
public final class SftpUtils {

	private ChannelSftp channelSftp;
	private Session session;

	/**
	 * 用户名
	 */
	private final String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 秘钥
	 */
	private String privateKey;

	/**
	 * FTP服务器Ip
	 */
	private final String host;

	/**
	 * FTP服务器端口号
	 */
	private int port = 22;

	/**
	 * 构造器：基于密码认证sftp对象
	 *
	 * @param username 用户名
	 * @param password 密码
	 * @param host     服务器ip
	 * @param port     服务器端口号
	 */
	public SftpUtils(String username, String password, String host, int port) {
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
	}

	/**
	 * 构造器：基于秘钥认证sftp对象
	 *
	 * @param username   用户名
	 * @param privateKey 秘钥
	 * @param host       服务器ip
	 * @param port       服务器端口号
	 */
	public SftpUtils(String username, String privateKey, int port, String host) {
		this.username = username;
		this.privateKey = privateKey;
		this.host = host;
		this.port = port;
	}

	/**
	 * 连接SFTP服务器
	 *
	 * @return 连接成功
	 * @author zmzhou
	 * @date 2021/3/1 14:01
	 */
	public boolean login() {
		JSch jsch = new JSch();
		try {
			if (StringUtils.isNotBlank(privateKey)) {
				//设置登陆主机的秘钥
				jsch.addIdentity(privateKey);
			}
			//采用指定的端口连接服务器
			session = jsch.getSession(username, host, port);
			if (StringUtils.isNotBlank(password)) {
				//设置登陆主机的密码
				session.setPassword(password);
			}
			//优先使用 password 验证   注：session.connect()性能低，使用password验证可跳过gssapi认证，提升连接服务器速度
			session.setConfig("PreferredAuthentications", "password");
			//设置第一次登陆的时候提示，可选值：(ask | yes | no)
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			//创建sftp通信通道
			Channel channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			log.info("sftp server connect success !!");
		} catch (JSchException e) {
			log.error("SFTP服务器连接异常！！", e);
			return false;
		}
		return true;
	}

	/**
	 * 关闭SFTP连接
	 */
	public void logout() {
		if (channelSftp != null) {
			if (channelSftp.isConnected()) {
				channelSftp.disconnect();
				log.debug("sftp closed");
			}
		}
		if (session != null) {
			if (session.isConnected()) {
				session.disconnect();
				log.debug("session closed");
			}
		}
	}

	/**
	 * 将输入流上传到SFTP服务器，作为文件
	 *
	 * @param directory    上传到SFTP服务器的路径
	 * @param sftpFileName 上传到SFTP服务器后的文件名
	 * @param input        输入流
	 * @throws SftpException SftpException
	 * @author zmzhou
	 * @date 2021/3/2 23:36
	 */
	public void upload(String directory, String sftpFileName, InputStream input) throws SftpException {
		long start = System.currentTimeMillis();
		try {
			//如果文件夹不存在，则创建文件夹
			if (channelSftp.ls(directory) == null) {
				channelSftp.mkdir(directory);
			}
			//切换到指定文件夹
			channelSftp.cd(directory);
		} catch (SftpException e) {
			//创建不存在的文件夹，并切换到文件夹
			channelSftp.mkdir(directory);
			channelSftp.cd(directory);
		}
		// 上传文件
		channelSftp.put(input, sftpFileName);
		log.info("文件上传成功！！ 耗时：{}ms", (System.currentTimeMillis() - start));
	}

	/**
	 * 下载文件
	 *
	 * @param path SFTP服务器的文件路径
	 * @return 输入流
	 * @author zmzhou
	 * @date 2021/3/2 21:20
	 */
	public InputStream download(String path) {
		// 文件所在目录
		String directory = path.substring(0, path.lastIndexOf(Constants.SEPARATOR));
		// 文件名
		String fileName = path.substring(path.lastIndexOf(Constants.SEPARATOR) + 1);
		return download(directory, fileName);
	}

	/**
	 * 下载文件
	 *
	 * @param directory SFTP服务器的文件路径
	 * @param fileName  SFTP服务器上的文件名
	 * @return 输入流
	 * @author zmzhou
	 * @date 2021/3/2 21:20
	 */
	public InputStream download(String directory, String fileName) {
		try {
			if (StringUtils.isNotBlank(directory)) {
				channelSftp.cd(directory);
			}
			log.info("下载文件:{}/{}", directory, fileName);
			return channelSftp.get(fileName);
		} catch (SftpException e) {
			log.error("下载文件:{}/{}异常！", directory, fileName, e);
		}
		return null;
	}

	/**
	 * 删除文件
	 *
	 * @param directory      SFTP服务器的文件路径
	 * @param deleteFileName 删除的文件名称
	 */
	public void delete(String directory, String deleteFileName) {
		try {
			channelSftp.cd(directory);
			channelSftp.rm(deleteFileName);
		} catch (SftpException e) {
			log.error("文件删除异常！", e);
		}
	}

	/**
	 * 删除文件夹
	 *
	 * @param directory SFTP服务器的文件路径
	 */
	public void delete(String directory) {
		Vector<?> vector = listFiles(directory);
		vector.forEach(v -> {
			ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) v;
			try {
				channelSftp.cd(directory);
				channelSftp.rm(lsEntry.getFilename());
			} catch (SftpException e) {
				log.error("文件删除异常！", e);
			}
		});
	}

	/**
	 * 获取文件夹下的文件列表
	 *
	 * @param directory 路径
	 * @return 文件列表
	 * @author zmzhou
	 * @date 2021/3/1 9:56
	 */
	public Vector<?> listFiles(String directory) {
		try {
			if (isDirExist(directory)) {
				Vector<?> vector = channelSftp.ls(directory);
				//移除上级目录和根目录："." ".."
				Iterator<?> it = vector.iterator();
				while (it.hasNext()) {
					ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) it.next();
					if (Constants.DOT.equals(lsEntry.getFilename())
							|| Constants.PARENT_DIRECTORY.equals(lsEntry.getFilename())) {
						it.remove();
					}
				}
				return vector;
			}
		} catch (SftpException e) {
			log.error("获取文件夹信息异常！", e);
		}
		return new Vector<>();
	}

	/**
	 * 检测文件夹是否存在
	 *
	 * @param directory 路径
	 * @author zmzhou
	 * @date 2021/2/26 15:31
	 */
	public boolean exists(String directory) {
		try {
			if (channelSftp.ls(directory) != null) {
				return true;
			}
		} catch (Exception e) {
			log.error("检测文件夹异常！", e);
		}
		return false;
	}

	/**
	 * 创建一个文件目录
	 *
	 * @param createPath 路径
	 * @return
	 */
	public boolean createDir(String createPath) {
		try {
			if (isDirExist(createPath)) {
				this.channelSftp.cd(createPath);
				return true;
			}
			String[] pathArray = createPath.split(Constants.SEPARATOR);
			StringBuilder filePath = new StringBuilder(Constants.SEPARATOR);
			for (String path : pathArray) {
				if ("".equals(path)) {
					continue;
				}
				filePath.append(path).append(Constants.SEPARATOR);
				if (!isDirExist(filePath.toString())) {
					// 建立目录
					channelSftp.mkdir(filePath.toString());
					// 进入并设置为当前目录
				}
				channelSftp.cd(filePath.toString());
			}
			this.channelSftp.cd(createPath);
		} catch (SftpException e) {
			log.error("目录创建异常！", e);
			return false;
		}
		return true;
	}

	/**
	 * 判断目录是否存在
	 *
	 * @param directory 路径
	 * @return 目录是否存在
	 */
	public boolean isDirExist(String directory) {
		try {
			SftpATTRS attrs = this.channelSftp.lstat(directory);
			return attrs.isDir();
		} catch (Exception e) {
			log.error("判断目录是否存在异常：{}", directory, e);
		}
		return false;
	}

	/**
	 * 方法功能说明：目录不存在时创建目录
	 *
	 * @return void
	 * @throws
	 * @参数： @param path
	 */
	public void mkdirs(String path) {
		File file = new File(path);
		String fs = file.getParent();
		file = new File(fs);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 判断文件或目录是否存在
	 *
	 * @param path 路径
	 * @return 文件或目录是否存在
	 */
	public boolean isExist(String path) {
		boolean isExist = false;
		try {
			this.channelSftp.lstat(path);
			isExist = true;
		} catch (Exception e) {
			log.error("判断文件或目录是否存在:", e);
		}
		return isExist;
	}

	/**
	 * Gets channel sftp.
	 *
	 * @return the channel sftp
	 */
	public ChannelSftp getChannelSftp() {
		return channelSftp;
	}

	/**
	 * Gets session.
	 *
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}
}
