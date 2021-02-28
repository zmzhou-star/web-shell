package com.github.zmzhoustar.webshell;

/**
 * 常量
 * @title ConstantPool
 * @author zmzhou
 * @version 1.0
 * @date 2021/2/23 20:39
 */
public final class Constants {
    /**
     * 分隔符
     */
    public static final String SEPARATOR = "/";
    /**
     * 减号
     */
    public static final String MINUS = "-";
    /**
     * 缓存字符长度
     */
    public static final int BUFFER_SIZE = 2048;
    /**
     * 随机生成uuid的key名
     */
    public static final String USER_UUID_KEY = "user_uuid";
    /**
     * 发送指令：连接
     */
    public static final String OPERATE_CONNECT = "connect";
    /**
     * 发送指令：命令
     */
    public static final String OPERATE_COMMAND = "command";
    /**
     * 发送指令：安全文件传送
     */
    public static final String OPERATE_SFTP = "sftp";

    private Constants() {
    }
}
