package com.tp.m.geetest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * GeetestWeb配置文件
 * 
 *
 */

@Service
public class GeetestConfig {
	// 填入自己的captcha_id和private_key
	@Value("#{meta['geetest.id']}")
	private String captcha_id = "26c9d7cbb9755c145b8ec8e280819659";
	@Value("#{meta['geetest.key']}")
	private String private_key = "1624065006ee645119deac43d2c8db0a";

	public String getCaptcha_id() {
		return captcha_id;
	}
	
	public String getPrivate_key() {
		return private_key;
	}

}
