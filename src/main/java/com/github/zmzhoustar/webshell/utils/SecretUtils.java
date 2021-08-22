/*
 * Copyright © 2020-present zmzhou-star. All Rights Reserved.
 */

package com.github.zmzhoustar.webshell.utils;

import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import lombok.extern.slf4j.Slf4j;

/**
 * AES对称加密算法
 * - 算法种类
 * 	- 对称算法（分组密码算法）：AES/DES/SM4
 * 	- 非对称算法（公钥密码算法）：RSA/SM2
 * 	- 摘要算法（杂凑算法）：MD5/SHA-I/SM3
 * 	- 国密算法 SMx (SM2/SM3/SM4/SM9/ZUC等国密(国家商用密码))
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
	/** 算法 "算法/模式/补码方式" */
	private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	private static final String KEY_ALGORITHM = "AES";
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
			return Base64.getEncoder().encodeToString(encryptResult);
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
			byte[] contentByte = Base64.getDecoder().decode(content);
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
		Security.addProvider(new BouncyCastleProvider());
		SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), KEY_ALGORITHM);
		// 创建密码器
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		// 初始化
		cipher.init(decryptMode, keySpec);
		return cipher;
	}
	/**
	 * 微信小程序加密数据解密算法
	 * @param data 对称解密的目标密文
	 * @param sessionKey session_key login接口获取
	 * @param iv 对称解密算法初始向量
	 * @return 明文
	 * @author zmzhou
	 * @date 2021/2/25 15:51
	 */
	public static String decrypt(String data, String sessionKey, String iv) {
		byte[] dataByte = Base64.getDecoder().decode(data);
		byte[] keyByte = Base64.getDecoder().decode(sessionKey);
		byte[] ivByte = Base64.getDecoder().decode(iv);
		int base = 16;
		if (keyByte.length % base != 0) {
			int groups = keyByte.length / base + 1;
			byte[] temp = new byte[groups * base];
			Arrays.fill(temp, (byte)0);
			System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
			keyByte = temp;
		}
		try {
			Security.addProvider(new BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			SecretKeySpec spec = new SecretKeySpec(keyByte, KEY_ALGORITHM);
			AlgorithmParameters parameters = AlgorithmParameters.getInstance(KEY_ALGORITHM);
			parameters.init(new IvParameterSpec(ivByte));
			cipher.init(2, spec, parameters);
			byte[] resultByte = cipher.doFinal(dataByte);
			if (null != resultByte && resultByte.length > 0) {
				return new String(resultByte, StandardCharsets.UTF_8);
			}
		} catch (Exception e) {
			log.error("对称解密错误：", e);
		}
		return null;
	}

	private SecretUtils() {
	}

}
