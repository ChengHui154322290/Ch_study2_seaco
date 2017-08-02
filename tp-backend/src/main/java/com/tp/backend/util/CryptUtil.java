/******************************************************************************
  *Copyright (c) 2006-2013 ZheJiang Electronic Port, Inc.
  * All rights reserved.
  * 
  * 项目名称：EPLINK
  * 版权说明：本软件属浙江电子口岸有限公司所有，在未获得浙江电子口岸有限公司正式授权
  *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
  *           识产权保护的内容。
  *****************************************************************************/
package com.tp.backend.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;

import com.tp.model.ord.JKF.JkfBaseDO;


public class CryptUtil {
	
	/**
	 * AES加密工具类
	 */
	public static class AESUtil{
		/**
		 * 密钥算法
		 */
		public static final String KEY_ALGORITHM = "AES";

		/**
		 * 加密/解密算法 / 工作模式 / 填充方式 
		 * Java 6支持PKCS5Padding填充方式 
		 * Bouncy Castle支持PKCS7Padding填充方式
		 */
		public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

		/**
		 * 转换密钥
		 * 
		 * @param key 二进制密钥
		 * @return Key 密钥
		 * @throws Exception
		 */
		private static Key toKey(byte[] key) throws Exception {

			// 实例化AES密钥材料
			SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);

			return secretKey;
		}

		/**
		 * 解密
		 * 
		 * @param data 待解密数据
		 * @param key 密钥
		 * @return byte[] 解密数据
		 * @throws Exception
		 */
		public static byte[] decrypt(byte[] data, byte[] key) throws Exception {

			// 还原密钥
			Key k = toKey(key);

			/*
			 * 实例化 
			 * 使用PKCS7Padding填充方式，按如下方式实现 
			 * Cipher.getInstance(CIPHER_ALGORITHM, "BC");
			 */
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

			// 初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, k);

			// 执行操作
			return cipher.doFinal(data);
		}

		/**
		 * 加密
		 * 
		 * @param data 待加密数据
		 * @param key 密钥
		 * @return byte[] 加密数据
		 * @throws Exception
		 */
		public static byte[] encrypt(byte[] data, byte[] key) throws Exception {

			// 还原密钥
			Key k = toKey(key);

			/*
			 * 实例化 
			 * 使用PKCS7Padding填充方式，按如下方式实现
			 * Cipher.getInstance(CIPHER_ALGORITHM, "BC");
			 */
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

			// 初始化，设置为加密模式
			cipher.init(Cipher.ENCRYPT_MODE, k);

			// 执行操作
			return cipher.doFinal(data);
		}

		/**
		 * 生成密钥 <br>
		 * 
		 * @return byte[] 二进制密钥
		 * @throws Exception
		 */
		public static byte[] initKey() throws Exception {

			// 实例化
			KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);

			/*
			 * AES 要求密钥长度为 128位、192位或 256位
			 */
			kg.init(128);

			// 生成秘密密钥
			SecretKey secretKey = kg.generateKey();

			// 获得密钥的二进制编码形式
			return secretKey.getEncoded();
		}
	}
	
	/**
	 * RSA加密工具类
	 */
	public static class RSAUtil{
		/**
		 * 数字签名
		 * 密钥算法
		 */
		public static final String KEY_ALGORITHM = "RSA";

		/**
		 * 数字签名
		 * 签名/验证算法
		 */
		public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

		/**
		 * 公钥
		 */
		private static final String PUBLIC_KEY = "RSAPublicKey";

		/**
		 * 私钥
		 */
		private static final String PRIVATE_KEY = "RSAPrivateKey";

		/**
		 * RSA密钥长度 默认1024位，
		 *  密钥长度必须是64的倍数， 
		 *  范围在512至65536位之间。
		 */
		private static final int KEY_SIZE = 512;

		/**
		 * 签名
		 * 
		 * @param data
		 *            待签名数据
		 * @param privateKey
		 *            私钥
		 * @return byte[] 数字签名
		 * @throws Exception
		 */
		public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {

			// 转换私钥材料
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);

			// 实例化密钥工厂
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

			// 取私钥匙对象
			PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

			// 实例化Signature
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

			// 初始化Signature
			signature.initSign(priKey);

			// 更新
			signature.update(data);

			// 签名
			return signature.sign();
		}

		/**
		 * 校验
		 * 
		 * @param data
		 *            待校验数据
		 * @param publicKey
		 *            公钥
		 * @param sign
		 *            数字签名
		 * 
		 * @return boolean 校验成功返回true 失败返回false
		 * @throws Exception
		 * 
		 */
		public static boolean verify(byte[] data, byte[] publicKey, byte[] sign)
				throws Exception {

			// 转换公钥材料
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);

			// 实例化密钥工厂
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

			// 生成公钥
			PublicKey pubKey = keyFactory.generatePublic(keySpec);

			// 实例化Signature
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

			// 初始化Signature
			signature.initVerify(pubKey);

			// 更新
			signature.update(data);

			// 验证
			return signature.verify(sign);
		}

		/**
		 * 取得私钥
		 * 
		 * @param keyMap
		 * @return
		 * @throws Exception
		 */
		public static byte[] getPrivateKey(Map<String, Object> keyMap)
				throws Exception {

			Key key = (Key) keyMap.get(PRIVATE_KEY);

			return key.getEncoded();
		}

		/**
		 * 取得公钥
		 * 
		 * @param keyMap
		 * @return
		 * @throws Exception
		 */
		public static byte[] getPublicKey(Map<String, Object> keyMap)
				throws Exception {

			Key key = (Key) keyMap.get(PUBLIC_KEY);

			return key.getEncoded();
		}

		/**
		 * 初始化密钥
		 * 
		 * @return Map 密钥对儿 Map
		 * @throws Exception
		 */
		public static Map<String, Object> initKey() throws Exception {

			// 实例化密钥对儿生成器
			KeyPairGenerator keyPairGen = KeyPairGenerator
					.getInstance(KEY_ALGORITHM);

			// 初始化密钥对儿生成器
			keyPairGen.initialize(KEY_SIZE);

			// 生成密钥对儿
			KeyPair keyPair = keyPairGen.generateKeyPair();

			// 公钥
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

			// 私钥
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

			// 封装密钥
			Map<String, Object> keyMap = new HashMap<String, Object>(2);

			keyMap.put(PUBLIC_KEY, publicKey);
			keyMap.put(PRIVATE_KEY, privateKey);

			return keyMap;
		}
	}
	
	/**
	 * JKF专用xml处理类
	 */
	public static class XmlUtil{
		/**
		 * POJO转换成XML
		 */
		public static String beanToXML(JkfBaseDO bean) {
			if (null == bean) return null;
			try {
				JAXBContext ctx = JAXBContext.newInstance(bean.getClass());
				Marshaller marshaller = ctx.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				StringWriter sw = new StringWriter();
				marshaller.marshal(bean, sw);
				return sw.toString();
			} catch (Exception e) {
				e.printStackTrace();	
			}
			return "";
		}
		/**
		 * XML转换成POJO
		 */
		@SuppressWarnings("unchecked")
		public static <T extends JkfBaseDO> T xmlToBean(String xml, Class<?> clazz){
			if(StringUtils.isEmpty(xml)) return null;
			try {
				JAXBContext ctx = JAXBContext.newInstance(clazz);
				Unmarshaller unmarshaller = ctx.createUnmarshaller();
				return (T) unmarshaller.unmarshal(new StringReader(xml));
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
