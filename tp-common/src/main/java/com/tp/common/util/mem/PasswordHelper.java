package com.tp.common.util.mem;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.common.vo.mem.MemberInfoConstant;
import com.tp.common.vo.mem.MemberInfoConstant.PASSWORD;
import com.tp.util.StringUtil;

/**
 * 
 * <pre>
 * 
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class PasswordHelper implements Serializable{
	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = -5559517826198874303L;

	public static Logger logger = LoggerFactory.getLogger(PasswordHelper.class);
	
	private static MessageDigest SHA = null;
	
	private static MessageDigest MD5 = null;
	
	public static String $secretkey = "oidjfajsflkjsafoifiqii987321987432jsaijfoijsalkkz,mxv,mxznv"; 
	
	static{
		try {
			SHA = MessageDigest.getInstance(PASSWORD.SHA);
			
			logger.info("------------------SHA----------------------");
			logger.info("SHA:"+SHA.getClass());
			logger.info("------------------SHA----------------------");
			
			MD5 = MessageDigest.getInstance(PASSWORD.MD5);
			
			logger.info("------------------MD5----------------------");
			logger.info("MD5:"+MD5.getClass());
			logger.info("------------------MD5----------------------");
		} catch (NoSuchAlgorithmException e) {
			logger.error("初始化加密错误:"+e.getMessage());
		}
	}
	
	public static String md5(String password) throws UnsupportedEncodingException{
		logger.info("------------------MD5----------------------");
		logger.info("MD5CLASS:"+MD5.getClass());
		if(StringUtil.isNullOrEmpty(password)){
			logger.info("--------------------------------------------------");
			logger.info("MD5 password is null");
			logger.info("--------------------------------------------------");
			return null;
		}
		String md5Str = new String(Hex.encodeHex(MD5.digest(password.getBytes(MemberInfoConstant.UTF8))));
		logger.info("MD5:"+md5Str);
		logger.info("------------------MD5----------------------");
		return md5Str;
	}
	
	public static String sha(String password) throws UnsupportedEncodingException{
		logger.info("------------------SHA----------------------");
		logger.info("SHACLASS:"+SHA.getClass());
		if(StringUtil.isNullOrEmpty(password)){
			logger.info("--------------------------------------------------");
			logger.info("SHA password is null");
			logger.info("--------------------------------------------------");
			return null;
		}
		String shaStr = new String(Hex.encodeHex(SHA.digest(password.getBytes(MemberInfoConstant.UTF8))));
		logger.info("SHA:"+shaStr);
		logger.info("------------------SHA----------------------");
		return shaStr;
	}
	
	
	public static boolean checkCookieSig(String userId,String cookieTime,String password,String cookieSig) throws UnsupportedEncodingException{
		StringBuffer sig = new StringBuffer();
		sig.append(userId).append(cookieTime).append(password).append($secretkey);
		String newSig = md5(sig.toString());
		logger.info("生成sig:"+newSig+"，原始sig:"+cookieSig);
		if(!newSig.equals(cookieSig)) return false;
		return true;
	}
	
	public static boolean checkCookieUed(String userId,String cookieTime,String cookieUed) throws UnsupportedEncodingException{
		StringBuffer ued = new StringBuffer();
		ued.append(userId).append(cookieTime).append($secretkey);
		String newUed = md5(ued.toString()).substring(0,16);
		logger.info("生成ued:"+newUed+"，原始ued:"+cookieUed);
		if(!newUed.equals(cookieUed)) return false;
		return true;
	}
	/*public static String getCookieSig(String userId,String cookieTime) throws UnsupportedEncodingException{
		StringBuffer sig = new StringBuffer();
		sig.append(userId).append(cookieTime).append(updatePasswordTime).append($secretkey);
		String newSig = md5(sig.toString());
		logger.info("生成sig:"+newSig);
		return newSig;
	}*/
	
	/**
	 * 
	 * <pre>
	 * 获取盐
	 * </pre>
	 *
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private static SecureRandom secureRandom = new SecureRandom();
	public static String getSalt() {
		byte[] bytes = new byte[16];
        secureRandom.nextBytes(bytes);
        String hexString = Hex.encodeHexString(bytes);
		try {
			return PasswordHelper.sha(hexString);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}
	
	public static String getCookieUed(String userId,String cookieTime) throws UnsupportedEncodingException{
		StringBuffer ued = new StringBuffer();
		ued.append(userId).append(cookieTime).append($secretkey);
		String newUed = md5(ued.toString()).substring(0,16);
		logger.info("生成ued:"+newUed);
		return newUed;
	}
	
	public static void main(String[] args) {
		System.out.println(getSalt());
		System.out.println(getSalt());
		System.out.println(getSalt());
		System.out.println(getSalt());
		System.out.println(getSalt());
	}
}
