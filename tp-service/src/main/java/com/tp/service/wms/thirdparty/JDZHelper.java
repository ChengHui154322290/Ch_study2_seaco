/**
 * 
 */
package com.tp.service.wms.thirdparty;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.sun.crypto.provider.SunJCE;

/**
 * @author Administrator
 *
 */
public class JDZHelper {
	public static class AESEncrypt {

		/** KeyGenerator 提供对称密钥生成器的功能，支持各种算法 */
		private static KeyGenerator keygen;
		/** SecretKey 负责保存对称密钥 */
		private static SecretKey deskey;
		/** Cipher负责完成加密或解密工作 */
		private static Cipher c;
		/** 该字节数组负责保存加密的结果 */
		private static byte[] cipherByte;
		/** 加密密码 */
		private static final String PASSWORD = "zjport_tropjdz";

		static {
			Security.addProvider(new SunJCE());
			// 实例化支持DES算法的密钥生成器(算法名称命名需按规定，否则抛出异常)
			try {
				keygen = KeyGenerator.getInstance("AES");
				keygen.init(128, new SecureRandom(PASSWORD.getBytes()));
				// 生成密钥
				deskey = keygen.generateKey();
				// 生成Cipher对象,指定其支持的DES算法
				c = Cipher.getInstance("AES");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			}

		}

		/**
		 * 
		 * 方法说明：对字符串加密
		 * 
		 * @param str  String
		 * @return byte[]
		 * @throws InvalidKeyException  e1
		 * @throws IllegalBlockSizeException e2
		 * @throws BadPaddingException e3
		 * @throws UnsupportedEncodingException e4
		 */
		public static String encrytor(String str) throws InvalidKeyException,
				IllegalBlockSizeException, BadPaddingException,
				UnsupportedEncodingException {
			// 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式
			c.init(Cipher.ENCRYPT_MODE, deskey);
			byte[] src = str.getBytes("utf-8");
			// 加密，结果保存进cipherByte
			cipherByte = c.doFinal(src);
			return ByteHexHelper.bytesToHexString(cipherByte);
		}


		/**
		 * 
		 * 方法说明：对字符串解密
		 * 
		 * @param buff byte[]
		 * @return byte[]
		 * @throws InvalidKeyException e1
		 * @throws IllegalBlockSizeException  e2
		 * @throws BadPaddingException e3
		 */
		public static byte[] decryptor(byte[] buff) throws InvalidKeyException,
				IllegalBlockSizeException, BadPaddingException {
			// 根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示加密模式
			c.init(Cipher.DECRYPT_MODE, deskey);
			cipherByte = c.doFinal(buff);
			return cipherByte;
		}

		/**
		 * 
		 * 方法说明：对字符串解密
		 * 
		 * @param hexString String
		 * @return String
		 * @throws InvalidKeyException e1
		 * @throws IllegalBlockSizeException  e2
		 * @throws BadPaddingException  e3
		 */
		public static String decryptor(String hexString)
				throws InvalidKeyException, IllegalBlockSizeException,
				BadPaddingException {
			byte[] hexArray = decryptor(ByteHexHelper.hexStr2ByteArray(hexString));
			return new String(hexArray);
		}

		/**
		 * 
		 * 方法说明：
		 * 
		 * @param args String[]
		 * @throws Exception e
		 */
		public static void main(String[] args) throws Exception {
			String msg = "你好!@#$%^&*";
			String encontent = AESEncrypt.encrytor(msg);
			String decontent = AESEncrypt.decryptor(encontent);
			System.out.println("=====\u539F\u6587\uFF1A" + msg);
			System.out.println("=====\u52A0\u5BC6\u540E\uFF1A" + encontent);
			System.out.println("=====\u89e3\u5BC6\u540E\uFF1A" + decontent);
		}

	}
	
	public static class ByteHexHelper {

		/**
		 * 
		 * 构造方法说明：
		 */
		public ByteHexHelper() {
		}

		/**
		 * 
		 * 方法说明：
		 * @param src byte
		 * @return String
		 */
		public static String bytesToHexString(byte src[]) {
			StringBuilder stringBuilder = new StringBuilder("");
			if (src == null || src.length <= 0){
				return null;
			}
			for (int i = 0; i < src.length; i++) {
				int v = src[i] & 255;
				String hv = Integer.toHexString(v);
				if (hv.length() < 2){
					stringBuilder.append(0);	
				}
				stringBuilder.append(hv);
			}

			return stringBuilder.toString();
		}

		/**
		 * 
		 * 方法说明：
		 * @param hexString String
		 * @return byte[] 
		 */
		public static byte[] hexStr2ByteArray(String hexString) {
			if (hexString == null || "".equals(hexString)){
				throw new IllegalArgumentException(
						"this hexString must not be empty");
			}
			hexString = hexString.toLowerCase();
			byte byteArray[] = new byte[hexString.length() / 2];
			int k = 0;
			for (int i = 0; i < byteArray.length; i++) {
				byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 255);
				byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 255);
				byteArray[i] = (byte) (high << 4 | low);
				k += 2;
			}

			return byteArray;
		}

		/**
		 * 
		 * 方法说明：
		 * @param hex String
		 * @return  byte[]
		 */
		public static byte[] hex2Byte(String hex) {
			String digital = "0123456789ABCDEF";
			char hex2char[] = hex.toCharArray();
			byte bytes[] = new byte[hex.length() / 2];
			for (int i = 0; i < bytes.length; i++) {
				int temp = digital.indexOf(hex2char[2 * i]) * 16;
				temp += digital.indexOf(hex2char[2 * i + 1]);
				bytes[i] = (byte) (temp & 255);
			}

			return bytes;
		}

	}

}
