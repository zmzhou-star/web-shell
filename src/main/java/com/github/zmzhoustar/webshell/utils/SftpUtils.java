package com.github.zmzhoustar.webshell.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.apache.commons.io.IOUtils;

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

	private ChannelSftp sftp;
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
	 */
	public void login() {
		JSch jsch = new JSch();
		try {
			if (privateKey != null) {
				//设置登陆主机的秘钥
				jsch.addIdentity(privateKey);
			}
			//采用指定的端口连接服务器
			session = jsch.getSession(username, host, port);
			if (password != null) {
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
			sftp = (ChannelSftp) channel;
			log.info("sftp server connect success !!");
		} catch (JSchException e) {
			log.error("SFTP服务器连接异常！！", e);
		}
	}

	/**
	 * 关闭SFTP连接
	 */
	public void logout() {
		if (sftp != null) {
			if (sftp.isConnected()) {
				sftp.disconnect();
				log.info("sftp is close already");
			}
		}
		if (session != null) {
			if (session.isConnected()) {
				session.disconnect();
				log.info("session is close already");
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
	 */
	public void upload(String directory, String sftpFileName, InputStream input) throws SftpException {
		long start = System.currentTimeMillis();
		try {
			//如果文件夹不存在，则创建文件夹
			if (sftp.ls(directory) == null) {
				sftp.mkdir(directory);
			}
			//切换到指定文件夹
			sftp.cd(directory);
		} catch (SftpException e) {
			//创建不存在的文件夹，并切换到文件夹
			sftp.mkdir(directory);
			sftp.cd(directory);
		}
		sftp.put(input, sftpFileName);
		log.info("文件上传成功！！ 耗时：{}ms", (System.currentTimeMillis() - start));
	}

	/**
	 * 上传单个文件
	 *
	 * @param directory     上传到SFTP服务器的路径
	 * @param uploadFileUrl 文件路径
	 */
	public void upload(String directory, String uploadFileUrl) {
		File file = new File(uploadFileUrl);
		try {
			upload(directory, file.getName(), new FileInputStream(file));
		} catch (FileNotFoundException | SftpException e) {
			log.error("上传文件异常！", e);
		}
	}

	/**
	 * 将byte[] 上传到SFTP服务器，作为文件
	 * 注： 从String转换成byte[] 需要指定字符集
	 *
	 * @param directory    上传到SFTP服务器的路径
	 * @param sftpFileName 上传SFTP服务器后的文件名
	 * @param bytes        字节数组
	 */
	public void upload(String directory, String sftpFileName, byte[] bytes) {
		try {
			upload(directory, sftpFileName, new ByteArrayInputStream(bytes));
		} catch (SftpException e) {
			log.error("上传文件异常！", e);
		}
	}

	/**
	 * 将字符串按照指定编码格式上传到SFTP服务器
	 *
	 * @param directory    上传到SFTP服务器的路径
	 * @param sftpFileName 上传SFTP服务器后的文件名
	 * @param dataStr      字符串
	 * @param charsetName  字符串的编码格式
	 */
	public void upload(String directory, String sftpFileName, String dataStr, String charsetName) {
		try {
			upload(directory, sftpFileName, new ByteArrayInputStream(dataStr.getBytes(charsetName)));
		} catch (UnsupportedEncodingException | SftpException e) {
			log.error("上传文件异常！", e);
		}
	}

	/**
	 * 下载文件
	 *
	 * @param directory    SFTP服务器的文件路径
	 * @param downloadFile SFTP服务器上的文件名
	 * @param saveFile     保存到本地路径
	 */
	public void download(String directory, String downloadFile, String saveFile) {
		try {
			if (directory != null && !"".equals(directory)) {
				sftp.cd(directory);
			}
			File file = new File(saveFile);
			sftp.get(downloadFile, new FileOutputStream(file));
		} catch (SftpException | FileNotFoundException e) {
			log.error("文件下载异常！", e);
		}
	}

	/**
	 * 下载文件
	 *
	 * @param directory    SFTP服务器的文件路径
	 * @param downloadFile SFTP服务器上的文件名
	 * @return 字节数组
	 */
	public byte[] download(String directory, String downloadFile) {
		try {
			if (directory != null && !"".equals(directory)) {
				sftp.cd(directory);
			}
			InputStream inputStream = sftp.get(downloadFile);
			return IOUtils.toByteArray(inputStream);
		} catch (SftpException | IOException e) {
			log.error("文件下载异常！", e);
		}
		return null;
	}

	/**
	 * 下载文件
	 *
	 * @param directory    SFTP服务器的文件路径
	 * @param downloadFile SFTP服务器上的文件名
	 * @return 输入流
	 */
	public InputStream downloadStream(String directory, String downloadFile) {
		try {
			if (directory != null && !"".equals(directory)) {
				sftp.cd(directory);
			}
			return sftp.get(downloadFile);
		} catch (SftpException e) {
			log.error("文件下载异常！", e);
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
			sftp.cd(directory);
			sftp.rm(deleteFileName);
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
		Vector vector = listFiles(directory);
		vector.remove(0);
		vector.remove(0);
		for (Object v : vector) {
			ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) v;
			try {
				sftp.cd(directory);
				sftp.rm(lsEntry.getFilename());
			} catch (SftpException e) {
				log.error("文件删除异常！", e);
			}
		}
	}

	/**
	 * 获取文件夹下的文件
	 *
	 * @param directory 路径
	 * @return
	 */
	public Vector<?> listFiles(String directory) {
		try {
			if (isDirExist(directory)) {
				Vector<?> vector = sftp.ls(directory);
				//移除上级目录和根目录："." ".."
				vector.remove(0);
				vector.remove(0);
				return vector;
			}
		} catch (SftpException e) {
			log.error("获取文件夹信息异常！", e);
		}
		return null;
	}

	/**
	 * 检测文件夹是否存在
	 *
	 * @param directory 路径
	 * @return
	 */
	public boolean booleanUrl(String directory) {
		try {
			if (sftp.ls(directory) == null) {
				return false;
			}
		} catch (Exception e) {
			log.error("检测文件夹异常！", e);
		}
		return true;
	}

	/**
	 * 创建一个文件目录
	 *
	 * @param createpath 路径
	 * @return
	 */
	public boolean createDir(String createpath) {
		try {
			if (isDirExist(createpath)) {
				this.sftp.cd(createpath);
				return true;
			}
			String[] pathArry = createpath.split("/");
			StringBuilder filePath = new StringBuilder("/");
			for (String path : pathArry) {
				if ("".equals(path)) {
					continue;
				}
				filePath.append(path + "/");
				if (isDirExist(filePath.toString())) {
					sftp.cd(filePath.toString());
				} else {
					// 建立目录
					sftp.mkdir(filePath.toString());
					// 进入并设置为当前目录
					sftp.cd(filePath.toString());
				}
			}
			this.sftp.cd(createpath);
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
			SftpATTRS attrs = this.sftp.lstat(directory);
			return attrs.isDir();
		} catch (Exception e) {
			log.error("判断目录是否存在:", e);
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
	public boolean isExist(String path, ChannelSftp sftp) {
		boolean isExist = false;
		try {
			sftp.lstat(path);
			isExist = true;
		} catch (Exception e) {
			log.error("判断文件或目录是否存在:", e);
		}
		return isExist;
	}
}
