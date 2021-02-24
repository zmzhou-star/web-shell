package com.github.zmzhoustar.webshell.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zmzhoustar.webshell.Constants;
import com.github.zmzhoustar.webshell.utils.SecretUtils;
import com.github.zmzhoustar.webshell.utils.ThreadPoolUtils;
import com.github.zmzhoustar.webshell.utils.WebShellUtils;
import com.github.zmzhoustar.webshell.vo.ShellConnectInfo;
import com.github.zmzhoustar.webshell.vo.WebShellData;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import lombok.extern.slf4j.Slf4j;

/**
 * Web Shell业务逻辑实现
 * @title WebShellService
 * @author zmzhou
 * @version 1.0
 * @date 2021/2/23 21:58
 */
@Slf4j
@Service
public class WebShellService {
	/** 存放ssh连接信息的map */
	private static final Map<String, Object> SSH_MAP = new ConcurrentHashMap<>();

	/**
	 * 初始化连接
	 * @param session WebSocketSession
	 * @author zmzhou
	 * @date 2021/2/23 21:22
	 */
	public void initConnection(WebSocketSession session) {
		JSch jSch = new JSch();
		ShellConnectInfo shellConnectInfo = new ShellConnectInfo();
		shellConnectInfo.setJsch(jSch);
		shellConnectInfo.setWebSocketSession(session);
		String uuid = WebShellUtils.getUserName(session);
		//将这个ssh连接信息放入map中
		SSH_MAP.put(uuid, shellConnectInfo);
	}

	/**
	 * 处理客户端发送的数据
	 * @author zmzhou
	 * @date 2021/2/23 21:21
	 */
	public void recvHandle(String buffer, WebSocketSession session) {
		ObjectMapper objectMapper = new ObjectMapper();
		WebShellData shellData;
		try {
			shellData = objectMapper.readValue(buffer, WebShellData.class);
		} catch (IOException e) {
			log.error("Json转换异常:{}", e.getMessage());
			return;
		}
		String userId = WebShellUtils.getUserName(session);
		//找到刚才存储的ssh连接对象
		ShellConnectInfo shellConnectInfo = (ShellConnectInfo) SSH_MAP.get(userId);
		if (shellConnectInfo != null) {
			if (Constants.OPERATE_CONNECT.equals(shellData.getOperate())) {
				//启动线程异步处理
				ThreadPoolUtils.execute(() -> {
					try {
						connectToSsh(shellConnectInfo, shellData, session);
					} catch (JSchException | IOException e) {
						log.error("web shell连接异常:{}", e.getMessage());
						sendMessage(session, e.getMessage().getBytes());
						close(session);
					}
				});
			} else if (Constants.OPERATE_COMMAND.equals(shellData.getOperate())) {
				String command = shellData.getCommand();
				transToTerminal(shellConnectInfo.getChannel(), command);
			} else {
				log.error("不支持的操作");
				close(session);
			}
		}
	}

	/**
	 * 关闭连接
	 * @param session WebSocketSession
	 * @author zmzhou
	 * @date 2021/2/23 21:16
	 */
	public void close(WebSocketSession session) {
		String userId = WebShellUtils.getUserName(session);
		ShellConnectInfo shellConnectInfo = (ShellConnectInfo) SSH_MAP.get(userId);
		if (shellConnectInfo != null) {
			//断开连接
			if (shellConnectInfo.getChannel() != null) {
				shellConnectInfo.getChannel().disconnect();
			}
			//map中移除
			SSH_MAP.remove(userId);
		}
	}

	/**
	 * 使用jsch连接终端
	 * @param shellConnectInfo ShellConnectInfo
	 * @param sshData WebShellData
	 * @param webSocketSession WebSocketSession
	 * @author zmzhou
	 * @date 2021/2/23 21:15
	 */
	private void connectToSsh(ShellConnectInfo shellConnectInfo, WebShellData sshData, WebSocketSession webSocketSession)
			throws JSchException, IOException {
		Properties config = new Properties();
		// SSH 连接远程主机时，会检查主机的公钥。如果是第一次该主机，会显示该主机的公钥摘要，提示用户是否信任该主机
		config.put("StrictHostKeyChecking", "no");
		//获取jsch的会话
		Session session = shellConnectInfo.getJsch().getSession(sshData.getUsername(), sshData.getHost(),
				sshData.getPort());
		session.setConfig(config);
		//设置密码
		session.setPassword(SecretUtils.decrypt(sshData.getPassword(), SecretUtils.AES_KEY));
		//连接超时时间30s
		session.connect(30000);

		//开启shell通道
		Channel channel = session.openChannel("shell");
		//通道连接超时时间3s
		channel.connect(3000);
		//设置channel
		shellConnectInfo.setChannel(channel);

		//转发消息
		transToTerminal(channel, "lastlog -u " + sshData.getUsername() + "\r");

		//读取终端返回的信息流
		try (InputStream inputStream = channel.getInputStream()) {
			//循环读取
			byte[] buffer = new byte[1024];
			int i;
			//如果没有数据来，线程会一直阻塞在这个地方等待数据。
			while ((i = inputStream.read(buffer)) != -1) {
				sendMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i));
			}
		} finally {
			//断开连接后关闭会话
			session.disconnect();
			channel.disconnect();
		}
	}

	/**
	 * 数据写回前端
	 * @param session WebSocketSession
	 * @author zmzhou
	 * @date 2021/2/23 21:18
	 */
	public void sendMessage(WebSocketSession session, byte[] buffer) {
		try {
			session.sendMessage(new TextMessage(buffer));
		} catch (IOException e) {
			log.error("数据写回前端异常：", e);
		}
	}
	/**
	 * 将消息转发到终端
	 * @param channel Channel
	 * @author zmzhou
	 * @date 2021/2/23 21:13
	 */
	private void transToTerminal(Channel channel, String command) {
		if (channel != null) {
			try {
				OutputStream outputStream = channel.getOutputStream();
				outputStream.write(command.getBytes());
				outputStream.flush();
			} catch (IOException e) {
				log.error("web shell将消息转发到终端异常:{}", e.getMessage());
			}
		}
	}
}
