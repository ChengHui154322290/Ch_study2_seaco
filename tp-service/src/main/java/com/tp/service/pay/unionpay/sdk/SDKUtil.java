/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 * 
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * 
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *   xshu       2014-05-28      MPI工具类
 * =============================================================================
 */
package com.tp.service.pay.unionpay.sdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class SDKUtil {

	// protected static SimpleDateFormat dateTime14Fmt = new
	// SimpleDateFormat("yyyyMMddHHmmss");

	// protected static SimpleDateFormat GetDateFormat14Fmt(){
	// return new SimpleDateFormat("yyyyMMddHHmmss");
	// }

	protected static char[] letter = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
			'y', 'z' };

	protected static final Random random = new Random();

	/**
	 * 发送后台交易
	 * 
	 * @param url
	 *            请求的URL
	 * @param data
	 *            发送的数据
	 * @param httpHeads
	 *            HTTP头信息
	 * @param encoding
	 *            字符集编码
	 * @return 结果
	 */
	public static String send(String url, Map<String, String> data, String encoding, int connectionTimeout, int readTimeout) {
		HttpClient hc = new HttpClient(url, connectionTimeout, readTimeout);
		String res = "";
		try {
			int status = hc.send(data, encoding);
			if (200 == status) {
				res = hc.getResult();
			}
		} catch (Exception e) {
			LogUtil.writeErrorLog("通信异常", e);
		}
		return res;
	}

	/**
	 * 生成签名值(MD5摘要算法)
	 * 
	 * @param data
	 *            待签名数据Map键值对形式
	 * @param encoding
	 *            编码
	 * @return 签名值
	 */
	public static String signByMd5(Map<String, String> data, String encoding, boolean forApp) {
		/**
		 * 将map信息拼接成key=value&key=value的形式
		 */
		String dataString = coverMap2String(data);
		if (null == encoding || "".equals(encoding)) {
			encoding = "UTF-8";
		}
		LogUtil.writeLog("key=value字符串=[" + dataString + "]");
		/**
		 * 签名\base64编码
		 */
		byte[] signbyte = null;
		try {
			byte[] signD = SecureUtil.md5X16(dataString, encoding);
			LogUtil.writeLog("md5->16进制转换后的摘要=[" + new String(signD) + "]");
			signbyte = SecureUtil.base64Encode(SecureUtil.signBySoft(CertUtil.getSignCertPrivateKey(forApp), signD));
			return new String(signbyte);
		} catch (Exception e) {
			LogUtil.writeErrorLog("签名异常", e);
			return null;
		}
	}

	/**
	 * 生成签名值(SHA1摘要算法),供商户调用
	 * 
	 * @param data
	 *            待签名数据Map键值对形式
	 * @param encoding
	 *            编码
	 * @param forApp 是否使用app证书
	 * @return 签名是否成功
	 */
	public static boolean sign(Map<String, String> data, String encoding, boolean forApp) {
		LogUtil.writeLog("签名处理开始.");
		if (isEmpty(encoding)) {
			encoding = "UTF-8";
		}
		// 设置签名证书序列号
		data.put(SDKConstants.param_certId, CertUtil.getSignCertId(forApp));

		// 将Map信息转换成key1=value1&key2=value2的形式
		String stringData = coverMap2String(data);

		LogUtil.writeLog("报文签名之前的字符串(不含signature域)=[" + stringData + "]");
		/**
		 * 签名\base64编码
		 */
		byte[] byteSign = null;
		String stringSign = null;
		try {
			// 通过SHA1进行摘要并转16进制
			byte[] signDigest = SecureUtil.sha1X16(stringData, encoding);
			LogUtil.writeLog("SHA1->16进制转换后的摘要=[" + new String(signDigest) + "]");
			byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft(CertUtil.getSignCertPrivateKey(forApp), signDigest));
			stringSign = new String(byteSign);
			LogUtil.writeLog("报文签名之后的字符串=[" + stringSign + "]");
			// 设置签名域值
			data.put(SDKConstants.param_signature, stringSign);
			LogUtil.writeLog("签名处理结束.");
			return true;
		} catch (Exception e) {
			LogUtil.writeErrorLog("签名异常", e);
			return false;
		}
	}

	/**
	 * 通过传入的证书绝对路径和证书密码读取签名证书进行签名并返回签名值<br>
	 * 此为信用卡应用提供的特殊化处理方法
	 * 
	 * @param data
	 *            待签名数据Map键值对形式
	 * @param encoding
	 *            编码
	 * @param certPath
	 *            证书绝对路径
	 * @param certPwd
	 *            证书密码
	 * @return 签名值
	 */
	public static boolean signByCertInfo(Map<String, String> data, String encoding, String certPath, String certPwd) {
		LogUtil.writeLog("签名处理开始.");
		if (isEmpty(encoding)) {
			encoding = "UTF-8";
		}
		if (isEmpty(certPath) || isEmpty(certPwd)) {
			LogUtil.writeLog("传入参数不合法,签名失败");
			return false;
		}
		// 设置签名证书序列号
		data.put(SDKConstants.param_certId, CertUtil.getCertIdByCertPath(certPath, certPwd, "PKCS12"));
		// 将Map信息转换成key1=value1&key2=value2的形式
		String stringData = coverMap2String(data);
		LogUtil.writeLog("报文签名之前的字符串(不含signature域)=[" + stringData + "]");
		/**
		 * 签名\base64编码
		 */
		byte[] byteSign = null;
		String stringSign = null;
		try {
			byte[] signDigest = SecureUtil.sha1X16(stringData, encoding);
			LogUtil.writeLog("SHA1->16进制转换后的摘要=[" + new String(signDigest) + "]");
			byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft(CertUtil.getSignCertPrivateKey(certPath, certPwd), signDigest));
			stringSign = new String(byteSign);
			LogUtil.writeLog("报文签名之后的字符串=[" + stringSign + "]");
			// 设置签名域值
			data.put(SDKConstants.param_signature, stringSign);
			LogUtil.writeLog("签名处理结束.");
			return true;
		} catch (Exception e) {
			LogUtil.writeErrorLog("签名异常", e);
			return false;
		}
	}

	/**
	 * 验证签名(MD5摘要算法)
	 * 
	 * @param resData
	 *            结果数据
	 * @param signString
	 *            签名信息
	 * @param encoding
	 *            编码
	 * @return 验证结果
	 */
	// public static boolean validate(Map<String, String> resData, String
	// encoding) {
	//
	// LogUtil.writeLog("MpiUtil.validate验证签名开始");
	//
	// String signString = resData.get(MpiConstants.param_signature);
	// LogUtil.writeLog("返回报文中signature=[" + signString + "]");
	//
	// // 此处新增方法：从返回报文中获取certId ，然后去证书静态Map中查询对应证书对象
	// String certId = resData.get(MpiConstants.param_certId);
	// LogUtil.writeLog("返回报文中certId=[" + certId + "]");
	//
	// /**
	// * 将map信息拼接成key=value&key=value的形式
	// */
	// String dataString = coverMap2String(resData);
	// LogUtil.writeLog("返回报文coverMap2String得到的不包含签名域signature的dataString=[" +
	// dataString + "]");
	// if (null == encoding || "".equals(encoding)) {
	// encoding = "UTF-8";
	// }
	//
	// try {
	// // 验证签名需要用银联发给商户的公钥证书.
	// return SecureUtil.validateSignBySoft(CertUtil.getValidateKey(certId),
	// SecureUtil.base64Decode(signString.getBytes(encoding)),
	// SecureUtil.md5X16(dataString, encoding));
	// } catch (UnsupportedEncodingException e) {
	// LogUtil.writeErrorLog(e.getMessage(), e);
	// } catch (Exception e) {
	// LogUtil.writeErrorLog(e.getMessage(), e);
	// }
	// return false;
	// }

	/**
	 * 验证签名(SHA-1摘要算法)
	 * 
	 * @param resData
	 *            返回报文数据
	 * @param encoding
	 *            编码格式
	 * @return
	 */
	public static boolean validate(Map<String, String> resData, String encoding) {
		LogUtil.writeLog("验签处理开始.");
		if (isEmpty(encoding)) {
			encoding = "UTF-8";
		}
		String stringSign = resData.get(SDKConstants.param_signature);
		LogUtil.writeLog("返回报文中signature=[" + stringSign + "]");

		// 从返回报文中获取certId ，然后去证书静态Map中查询对应验签证书对象
		String certId = resData.get(SDKConstants.param_certId);
		LogUtil.writeLog("返回报文中certId=[" + certId + "]");

		// 将Map信息转换成key1=value1&key2=value2的形式
		String stringData = coverMap2String(resData);
		LogUtil.writeLog("返回报文中(不含signature域)的stringData=[" + stringData + "]");

		try {
			// 验证签名需要用银联发给商户的公钥证书.
			return SecureUtil.validateSignBySoft(CertUtil.getValidateKey(certId), SecureUtil.base64Decode(stringSign.getBytes(encoding)),
					SecureUtil.sha1X16(stringData, encoding));
		} catch (UnsupportedEncodingException e) {
			LogUtil.writeErrorLog(e.getMessage(), e);
		} catch (Exception e) {
			LogUtil.writeErrorLog(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 将Map中的数据转换成key1=value1&key2=value2的形式 不包含签名域signature
	 * 
	 * @param data
	 *            待拼接的Map数据
	 * @return 拼接好后的字符串
	 */
	public static String coverMap2String(Map<String, String> data) {
		TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			if (SDKConstants.param_signature.equals(en.getKey().trim())) {
				continue;
			}
			tree.put(en.getKey(), en.getValue());
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			sf.append(en.getKey() + SDKConstants.EQUAL + en.getValue() + SDKConstants.AMPERSAND);
		}
		return sf.substring(0, sf.length() - 1);
	}

	/**
	 * 将形如key=value&key=value的字符串转换为相应的Map对象
	 * 
	 * @param res
	 *            形如key=value&key=value的字符串
	 * @return 转换后的Map对象
	 */
	private static Map<String, String> convertResultString2Map(String res) {
		Map<String, String> map = null;
		if (null != res && !"".equals(res.trim())) {
			String[] resArray = res.split(SDKConstants.AMPERSAND);
			if (0 != resArray.length) {
				map = new HashMap<String, String>(resArray.length);
				for (String arrayStr : resArray) {
					if (null == arrayStr || "".equals(arrayStr.trim())) {
						continue;
					}
					int index = arrayStr.indexOf(SDKConstants.EQUAL);
					if (-1 == index) {
						continue;
					}
					map.put(arrayStr.substring(0, index), arrayStr.substring(index + 1));
				}
			}
		}
		return map;
	}

	/**
	 * 将key=value形式的字符串加入到指定Map中
	 * 
	 * @param res
	 * @param map
	 */
	private static void convertResultStringJoinMap(String res, Map<String, String> map) {
		if (null != res && !"".equals(res.trim())) {
			String[] resArray = res.split(SDKConstants.AMPERSAND);
			if (0 != resArray.length) {
				for (String arrayStr : resArray) {
					if (null == arrayStr || "".equals(arrayStr.trim())) {
						continue;
					}
					int index = arrayStr.indexOf(SDKConstants.EQUAL);
					if (-1 == index) {
						continue;
					}
					map.put(arrayStr.substring(0, index), arrayStr.substring(index + 1));
				}
			}
		}
	}

	/**
	 * 兼容老方法 将形如key=value&key=value的字符串转换为相应的Map对象
	 * 
	 * @param result
	 * @return
	 */
	public static Map<String, String> coverResultString2Map(String result) {
		return convertResultStringToMap(result);
	}

	/**
	 * 将形如key=value&key=value的字符串转换为相应的Map对象
	 * 
	 * @param result
	 * @return
	 */
	public static Map<String, String> convertResultStringToMap(String result) {

		if (result.contains(SDKConstants.LEFT_BRACE)) {
			// 返回报文含有{}特殊字符,特殊处理

			// 指定分隔符
			String separator = "\\{";
			String[] res = result.split(separator);

			Map<String, String> map = new HashMap<String, String>();

			convertResultStringJoinMap(res[0], map);// 将正常报文字符串转成map

			// res[0]是切割之后的正常报文部分，所以从res[1]开始特殊处理
			for (int i = 1; i < res.length; i++) {

				// 1.找到报文中出现}的地方
				int index = res[i].indexOf(SDKConstants.RIGHT_BRACE);
				// 2.将....}截取出来保存到临时value
				String specialValue = SDKConstants.LEFT_BRACE + res[i].substring(0, index) + SDKConstants.RIGHT_BRACE;// 得到{}里面的key=value&....再组合成{key=value&...}
				// 3.找到{}所对应的key
				int indexKey = res[i - 1].lastIndexOf(SDKConstants.AMPERSAND);
				String specialKey = res[i - 1].substring(indexKey + 1, res[i - 1].length() - 1);// 得到key={}里面的key

				map.put(specialKey, specialValue);// k3={k4=v4&k5=v5}

				// 4.获取剩余部分的正常报文
				String normalResult = res[i].substring(index + 2, res[i].length());
				// 5.正常报文转换成map并加入到全局map中
				convertResultStringJoinMap(normalResult, map);// 将正常报文字符串转成map

			}

			return map;

		} else {
			// 返回报文没有{}特殊字符,默认处理
			return convertResultString2Map(result);
		}

	}

	/**
	 * 密码加密.
	 * 
	 * @param card
	 *            卡号
	 * @param pwd
	 *            密码
	 * @param encoding
	 *            字符集编码
	 * @return 加密后的字符串
	 */
	public static String encryptPin(String card, String pwd, String encoding, boolean forApp) {
		return SecureUtil.EncryptPin(pwd, card, encoding, CertUtil.getEncryptCertPublicKey(forApp));
	}

	/**
	 * CVN2加密.
	 * 
	 * @param cvn2
	 *            CVN2值
	 * @param encoding
	 *            字符编码
	 * @return 加密后的数据
	 */
	public static String encryptCvn2(String cvn2, String encoding, boolean forApp) {
		return SecureUtil.EncryptData(cvn2, encoding, CertUtil.getEncryptCertPublicKey(forApp));
	}

	/**
	 * CVN2解密
	 * 
	 * @param base64cvn2
	 *            加密后的CVN2值
	 * @param encoding
	 *            字符编码
	 * @return 解密后的数据
	 */
	public static String decryptCvn2(String base64cvn2, String encoding, boolean forApp) {
		return SecureUtil.DecryptedData(base64cvn2, encoding, CertUtil.getSignCertPrivateKey(forApp));
	}

	/**
	 * 有效期加密.
	 * 
	 * @param date
	 *            有效期
	 * @param encoding
	 *            字符编码
	 * @return 加密后的数据
	 */
	public static String encryptAvailable(String date, String encoding, boolean forApp) {
		return SecureUtil.EncryptData(date, encoding, CertUtil.getEncryptCertPublicKey(forApp));
	}

	/**
	 * 有效期解密.
	 * 
	 * @param base64Date
	 *            有效期值
	 * @param encoding
	 *            字符编码
	 * @return 加密后的数据
	 */
	public static String decryptAvailable(String base64Date, String encoding, boolean forApp) {
		return SecureUtil.DecryptedData(base64Date, encoding, CertUtil.getSignCertPrivateKey(forApp));
	}

	/**
	 * 卡号加密.
	 * 
	 * @param pan
	 *            卡号
	 * @param encoding
	 *            字符编码
	 * @return 加密后的卡号值
	 */
	public static String encryptPan(String pan, String encoding, boolean forApp) {
		return SecureUtil.EncryptData(pan, encoding, CertUtil.getEncryptCertPublicKey(forApp));
	}

	/**
	 * 卡号解密.
	 * 
	 * @param base64Pan
	 *            如果卡号加密，传入返回报文中的卡号字段
	 * @param encoding
	 * @return
	 */
	public static String decryptPan(String base64Pan, String encoding, boolean forApp) {
		return SecureUtil.DecryptedData(base64Pan, encoding, CertUtil.getSignCertPrivateKey(forApp));
	}

	/**
	 * 组装银行卡验证信息及身份信息(customerInfo):
	 * <p>
	 * 银行卡验证信息及身份信息 customerInfo VAR1 填写对以下内容做Base64后的密文：
	 * {证件类型|证件号码|姓名|手机号|短信验证码|持卡人密码|CVN2|有效期} 当子域不出现时，可为空，并保留|分割。各字段取值为：
	 * <p>
	 * 1、证件类型：N2
	 * <p>
	 * 01：身份证 02：军官证 03：护照 04：回乡证 05：台胞证 06：警官证 07：士兵证 99：其它证件 2、证件号码：ANS1..20
	 * <p>
	 * 3、姓名：ANS1..32，支持汉字
	 * <p>
	 * 4、手机号：N1..20
	 * <p>
	 * 5、短信验证码：N6
	 * <p>
	 * 6、持卡人密码：ANS1..256，使用RSA证书对 ANSI X9.8格式的PIN加密，并Base64
	 * <p>
	 * 7、CVN2：N3
	 * <p>
	 * 8、有效期：YYMM
	 * <p>
	 * 
	 * @param customerInfo01
	 *            证件类型
	 * @param customerInfo02
	 *            证件号码
	 * @param customerInfo03
	 *            姓名
	 * @param customerInfo04
	 *            手机号
	 * @param customerInfo05
	 *            短信验证码
	 * @param customerInfo06
	 *            持卡人密码
	 * @param customerInfo07
	 *            CVN2
	 * @param customerInfo08
	 *            有效期
	 * @param pan
	 *            卡号
	 * @param encoding
	 *            编码格式
	 * @param isEncrypt
	 *            敏感信息是否加密
	 * @return 组装后的customerInfo域值
	 */
	public static String generateCustomerInfo(String customerInfo01, String customerInfo02, String customerInfo03, String customerInfo04,
			String customerInfo05, String customerInfo06, String customerInfo07, String customerInfo08, String pan, String encoding, boolean isEncrypt, boolean forApp) {

		// 设置编码方式
		if (isEmpty(encoding)) {
			encoding = SDKConstants.UTF_8_ENCODING;
		}
		// 持卡人身份信息 --证件类型|证件号码|姓名|手机号|短信验证码|持卡人密码|CVN2|有效期
		StringBuffer sf = new StringBuffer("{");

		sf.append((isEmpty(customerInfo01) ? "" : customerInfo01) + SDKConstants.COLON);
		sf.append((isEmpty(customerInfo02) ? "" : customerInfo02) + SDKConstants.COLON);
		sf.append((isEmpty(customerInfo03) ? "" : customerInfo03) + SDKConstants.COLON);
		sf.append((isEmpty(customerInfo04) ? "" : customerInfo04) + SDKConstants.COLON);
		sf.append((isEmpty(customerInfo05) ? "" : customerInfo05) + SDKConstants.COLON);
		// 持卡人密码处理
		if (!isEmpty(customerInfo06)) {
			if (!isEmpty(pan)) {
				sf.append(SDKUtil.encryptPin(pan.trim(), customerInfo06, encoding, forApp) + SDKConstants.COLON);
			} else {
				sf.append(customerInfo06 + SDKConstants.COLON);
			}
		} else {
			sf.append((isEmpty(customerInfo06) ? "" : customerInfo06) + SDKConstants.COLON);
		}
		// CVN2处理
		if (!isEmpty(customerInfo07)) {
			if (isEncrypt) {
				sf.append(SDKUtil.encryptCvn2(customerInfo07, encoding, forApp) + SDKConstants.COLON);
			} else {
				sf.append(customerInfo07 + SDKConstants.COLON);
			}
		} else {
			sf.append((isEmpty(customerInfo07) ? "" : customerInfo07) + SDKConstants.COLON);
		}
		// 有效期处理
		if (!isEmpty(customerInfo08)) {
			if (isEncrypt) {
				sf.append(SDKUtil.encryptAvailable(customerInfo08, encoding, forApp));
			} else {
				sf.append(customerInfo08);
			}
		}
		sf.append("}");
		try {
			return new String(SecureUtil.base64Encode(sf.toString().getBytes(encoding)));
		} catch (UnsupportedEncodingException e) {
			LogUtil.writeErrorLog(e.getMessage(), e);
			return "";
		} catch (IOException e) {
			LogUtil.writeErrorLog(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * 判断字符串是否为NULL或空
	 * 
	 * @param s
	 *            待判断的字符串数据
	 * @return 判断结果 true-是 false-否
	 */
	public static boolean isEmpty(String s) {
		return null == s || "".equals(s.trim());
	}

	/**
	 * 生成订单发送时间YYYYMMDDhhmmss
	 * 
	 * @return 返回YYYYMMDDhhmmss格式的时间戳
	 */
	public static String generateTxnTime() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	/**
	 * 生产商户订单号AN12..32
	 * 
	 * @return
	 */
	public static String generateOrderId() {
		StringBuilder sb = new StringBuilder();
		int len = random.nextInt(18);
		for (int i = 0; i < len; i++) {
			sb.append(letter[i]);
		}
		return generateTxnTime() + sb.toString();
	}

	/**
	 * 构建HTML页面,包含一个form表单(自动提交)
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	public static String createAutoSubmitForm(String url, Map<String, String> data) {
		StringBuffer sf = new StringBuffer();
		sf.append("<form id = \"sform\" action=\"" + url + "\" method=\"post\">");
		if (null != data && 0 != data.size()) {
			Set<Entry<String, String>> set = data.entrySet();
			Iterator<Entry<String, String>> it = set.iterator();
			while (it.hasNext()) {
				Entry<String, String> ey = it.next();
				String key = ey.getKey();
				String value = ey.getValue();
				sf.append("<input type=\"hidden\" name=\"" + key + "\" id=\"" + key + "\" value=\"" + value + "\"/>");
			}
		}
		sf.append("</form>");
		sf.append("</body>");
		sf.append("<script type=\"text/javascript\">");
		sf.append("document.getElementById(\"sform\").submit();\n");
		sf.append("</script>");
		return sf.toString();
	}

	public static String createCombDomain(Map<String, String> data) {
		return "";
	}

}
