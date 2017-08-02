package com.tp.service.pay.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.util.Base64;


public class KqPaySecurityUtil {
	static Logger logger = LoggerFactory.getLogger(KqPaySecurityUtil.class);
	
	public static String signMsg(String signMsg, String password) {

		String base64 = "";
		try {
			// 密钥仓库
			KeyStore ks = KeyStore.getInstance("PKCS12");

			// 读取密钥仓库
//			FileInputStream ksfis = new FileInputStream("e:/tester-rsa.pfx");
			
			// 读取密钥仓库（相对路径）
			String file = KqPaySecurityUtil.class.getResource("/certs/99bill-rsa.pfx").getPath().replaceAll("%20", " ");
			logger.info("/99bill-rsa.pfx={}", file);
			
			FileInputStream ksfis = new FileInputStream(file);
			
			BufferedInputStream ksbufin = new BufferedInputStream(ksfis);

			if (password==null) {
				password="123456";
			}
			char[] keyPwd = password.toCharArray();
			//char[] keyPwd = "YaoJiaNiLOVE999Year".toCharArray();
			ks.load(ksbufin, keyPwd);
			// 从密钥仓库得到私钥
			PrivateKey priK = (PrivateKey) ks.getKey("test-alias", keyPwd);
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(priK);
			signature.update(signMsg.getBytes("utf-8"));
//			sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			base64 = Base64.encode(signature.sign());			
		} catch(FileNotFoundException e){
			logger.error("文件找不到", e);
		}catch (Exception ex) {
			logger.error("", ex);
		}
		return base64;
	}
	public static boolean verifyMsg( String val, String msg) {
		boolean flag = false;
		try {
			//获得文件(绝对路径)
			//InputStream inStream = new FileInputStream("e:/99bill[1].cert.rsa.20140803.cer");
			
			//获得文件(相对路径)
			String file = KqPaySecurityUtil.class.getResource("/certs/99bill.cer").toURI().getPath();
			logger.info("get cert file: {}",  file);
			FileInputStream inStream = new FileInputStream(file);
			
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
			//获得公钥
			PublicKey pk = cert.getPublicKey();
			//签名
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initVerify(pk);
			signature.update(val.getBytes());
			//解码
//			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			flag = signature.verify(Base64.decode(msg));
			logger.info("verifyMsg({}, {})={}", val, msg, flag);
		} catch (Exception e) {
			logger.error("error happens when verify", e);;
		} 
		return flag;
	}
	
	public static void main(String[] args) {
		//人民币网关账号，该账号为11位人民币网关商户编号+01,该参数必填。
		String merchantAcctId = "1001213884201";
		//编码方式，1代表 UTF-8; 2 代表 GBK; 3代表 GB2312 默认为1,该参数必填。
		String inputCharset = "1";
		//接收支付结果的页面地址，该参数一般置为空即可。
		String pageUrl = "";
		//服务器接收支付结果的后台地址，该参数务必填写，不能为空。
		String bgUrl = "http://222.72.249.242:9080/RMBPORT/receive.jsp";
		//网关版本，固定值：v2.0,该参数必填。
		String version =  "v2.0";
		//语言种类，1代表中文显示，2代表英文显示。默认为1,该参数必填。
		String language =  "1";
		//签名类型,该值为4，代表PKI加密方式,该参数必填。
		String signType =  "4";
		//支付人姓名,可以为空。
		String payerName= ""; 
		//支付人联系类型，1 代表电子邮件方式；2 代表手机联系方式。可以为空。
		String payerContactType =  "1";
		//支付人联系方式，与payerContactType设置对应，payerContactType为1，则填写邮箱地址；payerContactType为2，则填写手机号码。可以为空。
		String payerContact =  "2532987@qq.com";
		//商户订单号，以下采用时间来定义订单号，商户可以根据自己订单号的定义规则来定义该值，不能为空。
		String orderId = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
		//订单金额，金额以“分”为单位，商户测试以1分测试即可，切勿以大金额测试。该参数必填。
		String orderAmount = "1";
		//订单提交时间，格式：yyyyMMddHHmmss，如：20071117020101，不能为空。
		String orderTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
		//商品名称，可以为空。
		String productName= "苹果"; 
		//商品数量，可以为空。
		String productNum = "5";
		//商品代码，可以为空。
		String productId = "55558888";
		//商品描述，可以为空。
		String productDesc = "";
		//扩展字段1，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
		String ext1 = "";
		//扩展自段2，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
		String ext2 = "";
		//支付方式，一般为00，代表所有的支付方式。如果是银行直连商户，该值为10，必填。
		String payType = "00";
		//银行代码，如果payType为00，该值可以为空；如果payType为10，该值必须填写，具体请参考银行列表。
		String bankId = "";
		//同一订单禁止重复提交标志，实物购物车填1，虚拟产品用0。1代表只能提交一次，0代表在支付不成功情况下可以再提交。可为空。
		String redoFlag = "";
		//快钱合作伙伴的帐户号，即商户编号，可为空。
		String pid = "";
		// signMsg 签名字符串 不可空，生成加密签名串
		String signMsgVal = "";
		signMsgVal = appendParam(signMsgVal, "inputCharset", inputCharset);
		signMsgVal = appendParam(signMsgVal, "pageUrl", pageUrl);
		signMsgVal = appendParam(signMsgVal, "bgUrl", bgUrl);
		signMsgVal = appendParam(signMsgVal, "version", version);
		signMsgVal = appendParam(signMsgVal, "language", language);
		signMsgVal = appendParam(signMsgVal, "signType", signType);
		signMsgVal = appendParam(signMsgVal, "merchantAcctId",merchantAcctId);
		signMsgVal = appendParam(signMsgVal, "payerName", payerName);
		signMsgVal = appendParam(signMsgVal, "payerContactType",payerContactType);
		signMsgVal = appendParam(signMsgVal, "payerContact", payerContact);
		signMsgVal = appendParam(signMsgVal, "orderId", orderId);
		signMsgVal = appendParam(signMsgVal, "orderAmount", orderAmount);
		signMsgVal = appendParam(signMsgVal, "orderTime", orderTime);
		signMsgVal = appendParam(signMsgVal, "productName", productName);
		signMsgVal = appendParam(signMsgVal, "productNum", productNum);
		signMsgVal = appendParam(signMsgVal, "productId", productId);
		signMsgVal = appendParam(signMsgVal, "productDesc", productDesc);
		signMsgVal = appendParam(signMsgVal, "ext1", ext1);
		signMsgVal = appendParam(signMsgVal, "ext2", ext2);
		signMsgVal = appendParam(signMsgVal, "payType", payType);
		signMsgVal = appendParam(signMsgVal, "bankId", bankId);
		signMsgVal = appendParam(signMsgVal, "redoFlag", redoFlag);
		signMsgVal = appendParam(signMsgVal, "pid", pid);
//		Bill99SecurityUtil pki = new Bill99SecurityUtil();
//		String signMsg = pki.signMsg(signMsgVal);
//		logger.info(signMsg);
//		
//		String sm = pki.signMsg("hello");
//		logger.info(sm);
//		logger.info("pki.verifyMsgByCer(signMsgVal, signMsg)="+pki.verifyMsg(signMsgVal, signMsg));

		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "ABC");
		map.put("key", "value");
		logger.error("map={}", map);
	}
	
	public static String appendParam(String returns, String paramId, String paramValue) {
//		logger.info("if (StringUtils.isNotBlank("+paramId+")) {");
//		logger.info("	result.append('&').append(\""+paramId+"\").append('=').append("+paramId+");");
//		logger.info("}");
		if (!"".equals(returns)) {
			if (!"".equals(paramValue)) {

				returns += "&" + paramId + "=" + paramValue;
			}

		} else {

			if (!"".equals(paramValue)) {
				returns = paramId + "=" + paramValue;
			}
		}

		return returns;
	}
}
