package com.tp.m.util;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * 字段验证工具类
 * @author zhuss
 * @2016年1月2日 下午4:34:46
 */
public class VerifyUtil {
	
	/**
	 * 验证手机短信验证码
	 * @param captcha
	 * @return
	 */
	public static boolean verifyCaptcha(String captcha) {
		return match(captcha, "\\d{6}");
	}
	
	/**
	 * 验证手机号
	 * @param telephone
	 * @return
	 */
	public static boolean verifyTelephone(String telephone) {
		return match(telephone, "^1[3|4|5|7|8][0-9]\\d{8}$");
	}

	/**
	 * 验证密码格式：6到12位的数字字母
	 * @param password
	 * @return
	 */
	public static boolean verifyPassword(String password){
		return match(password,"^[A-Za-z0-9]{6,12}$" );
	}
	
	/**
	 * 验证邮箱
	 * @param email
	 * @return
	 */
	public static boolean verifyEmail(String email) {
		return match(email, "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$");
	}
	
	/**
	 * 验证IP
	 * @param ip
	 * @return
	 */
	public static boolean verifyIP(String ip) {
		return match(ip, "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$");
	}
	
	/**
     * 功能：判断字符串是否为数字
     * @param str
     * @return
     */
	public static boolean isNumeric(String str) {
        return match(str,"^[0-9]*$");
    }

    /**
     * 功能：判断字符串是否为日期格式
     * @param str
     * @return
     */
	public static boolean isDate(String strDate) {
        return match(strDate,"^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
    }

    /**
	 * 验证身份证号
	 * @param ip
	 * @return
	 */
	public static boolean verifyCardID(String id) {
		return match(id,"(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
	}

	/**
	 * 正则表达式匹配
	 * @param text
	 * @param reg
	 * @return
	 */
	private final static boolean match(String text, String reg) {
		if (StringUtils.isBlank(text) || StringUtils.isBlank(reg))
			return false;
		return Pattern.compile(reg).matcher(text).matches();
	}
	
	/** 
     * 过滤emoji
     * @param source 
     * @return 过滤后的字符串 
     */  
    public static String filterEmoji(String source) {  
        if(StringUtils.isNotBlank(source)){  
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", StringUtils.EMPTY);  
        }else{  
            return source;  
        }  
    }  
    
    /**
     * 转义js 过滤emoji
     * @param source
     * @return
     */
    public static String escapeJSAndEmoji(String source){
    	return filterEmoji(escapeJS(source));
    }
    
    /**
	 * 转义js
	 * @param content
	 * @return
	 */
	public static String escapeJS(String content){
		return StringEscapeUtils.escapeHtml4(content);
	}
}
