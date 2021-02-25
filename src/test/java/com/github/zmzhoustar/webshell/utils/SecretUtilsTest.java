package com.github.zmzhoustar.webshell.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * SecretUtilsTest
 *
 * @author zmzhou
 * @version 1.0
 * @date 2021/2/24 16:15
 */
@Slf4j
class SecretUtilsTest {

	private static final String a = "12345_67890-abc";

	/**
	 * Encrypt.
	 */
	@Test
	void encrypt() {
		String res = SecretUtils.encrypt(a, SecretUtils.AES_KEY);
		log.info("加密结果：{}", res);
		Assertions.assertNotNull(res);
		res = SecretUtils.decrypt(res, SecretUtils.AES_KEY);
		log.info("解密结果：{}", res);
		Assertions.assertNotNull(res);
	}
}
