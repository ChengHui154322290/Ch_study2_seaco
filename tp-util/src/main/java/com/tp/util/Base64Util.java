package com.tp.util;

import org.apache.commons.codec.binary.Base64;

/**
 * Base64工具类
 * @author zhuss
 * @2016年1月13日 下午7:56:13
 */
public class Base64Util {

	/**
	 * 加密
	 * @param bytes
	 * @return
	 */
	public static String encrypt(byte[] bytes) {
		return new String(Base64.encodeBase64(bytes, true));
	}


	/**
	 * 解密
	 * @param encryptString
	 * @return
	 */
	public static byte[] decrypt(String encryptString) {
		return Base64.decodeBase64(encryptString.getBytes());
	}
}