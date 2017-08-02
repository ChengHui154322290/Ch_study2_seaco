/**
 * 
 */
package com.tp.common.util.ptm;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;

/**
 * 加解密工具类
 * 
 * @author 项硕
 * @version 2014年12月4日
 * 
 */
public class EncryptionUtil {
    private static String DEFULT_ENCODESTRING="UTF-8";
    private static final Logger logger = LoggerFactory.getLogger(EncryptionUtil.class);
	/**
	 * base64加密
	 * 
	 * @param source
	 * @return
	 */
	public static String encrptBase64(String source) {
		Assert.notNull(source);
		try {
			return new String(Base64.encodeBase64(source.getBytes(DEFULT_ENCODESTRING)), DEFULT_ENCODESTRING);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * base64解密
	 * 
	 * @param source
	 * @return
	 */
	public static String decryptBase64(String source) {
		Assert.notNull(source);
		try {
            return new String(Base64.decodeBase64(source.getBytes(DEFULT_ENCODESTRING)), DEFULT_ENCODESTRING);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 
	 * <pre>
	 * 32位MD5加密
	 * </pre>
	 * 
	 * @param source
	 * @return
	 */
	public static String encrptMD5(String source) {
		Assert.notNull(source);
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			int len = b.length;
			for (int offset = 0; offset < len; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
}
