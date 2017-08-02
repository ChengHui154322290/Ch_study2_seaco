package com.tp.world.geetest;

/**
 * GeetestWeb配置文件
 * 
 *
 */

public class GeetestConfig {

	// 填入自己的captcha_id和private_key
	private static final String captcha_id = "26c9d7cbb9755c145b8ec8e280819659";
	private static final String private_key = "1624065006ee645119deac43d2c8db0a";

	public static final String getCaptcha_id() {
		return captcha_id;
	}

	public static final String getPrivate_key() {
		return private_key;
	}

}
