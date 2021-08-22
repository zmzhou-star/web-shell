/*
 * Copyright © 2020-present zmzhou-star. All Rights Reserved.
 */

package com.github.zmzhoustar.webshell.vo;

import java.io.Serializable;

import org.springframework.web.socket.WebSocketSession;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;

import lombok.Data;

/**
 * ssh连接信息
 * @title SSHConnectInfo
 * @author zmzhou
 * @version 1.0
 * @date 2021/2/23 21:05
 */
@Data
public class ShellConnectInfo implements Serializable {
	/** serialVersionUID */
	private static final long serialVersionUID = 1555506471798748444L;
	/** WebSocketSession */
	private WebSocketSession webSocketSession;
	/** JSch是SSH2的一个纯Java实现 */
	private JSch jsch;
	/** shell通道 */
	private Channel channel;
}
