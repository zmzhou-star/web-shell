package com.github.zmzhoustar.webshell.utils;


import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * AES对称加密算法
 *
 * @author zmzhou
 * @version 1.0
 * @date 2021/2/24 15:46
 */
@Slf4j
public final class SecretUtils {

	/**
	 * 默认加密密钥
	 */
	public static final String AES_KEY = "ws9ybUMn4F81t5oPKqJrqLKxERaYAS12";
	/** 算法 */
	private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

	/**
	 * AES加密
	 *
	 * @param content 需要加密的内容
	 * @param aesKey  加密密钥
	 * @return 密文
	 * @author zmzhou
	 * @date 2021/2/24 15:47
	 */
	public static String encrypt(String content, String aesKey) {
		try {
			// 创建密码器
			Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, aesKey);
			byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
			//加密
			byte[] encryptResult = cipher.doFinal(byteContent);
			// 将加密后的数据转为BASE64字符串
			return new BASE64Encoder().encode(encryptResult);
		} catch (Exception e) {
			log.error("AES加密时出现异常：[content：{};AESPwd：{}]", content, aesKey, e);
		}
		return null;
	}

	/**
	 * AES解密
	 *
	 * @param content 待解密内容
	 * @param aesKey  解密密钥
	 * @return 明文
	 * @author zmzhou
	 * @date 2021/2/24 15:57
	 */
	public static String decrypt(String content, String aesKey) {
		try {
			// 先用base64解密
			byte[] contentByte = new BASE64Decoder().decodeBuffer(content);
			// 创建密码器
			Cipher cipher = getCipher(Cipher.DECRYPT_MODE, aesKey);
			byte[] result = cipher.doFinal(contentByte);
			// 解密
			return new String(result, StandardCharsets.UTF_8);
		} catch (Exception e) {
			log.error("AES解密时出现异常：[content：{};AESPwd：{}]", content, aesKey, e);
		}
		return null;
	}

	/**
	 * 初始化密码器
	 *
	 * @param decryptMode 模式
	 * @param aesKey      加密密钥
	 * @return 密码器
	 * @author zmzhou
	 * @date 2021/2/24 16:04
	 */
	private static Cipher getCipher(int decryptMode, String aesKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(128);
		SecretKeySpec key = new SecretKeySpec(aesKey.getBytes(), "AES");
		// 创建密码器
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		// 初始化
		cipher.init(decryptMode, key);
		return cipher;
	}

	private SecretUtils() {
	}

}
