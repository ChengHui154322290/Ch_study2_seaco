/**
 * 
 */
package com.tp.backend.controller.sys;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.backend.util.CryptUtil.AESUtil;
import com.tp.backend.util.CryptUtil.RSAUtil;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.util.StringUtil;

/**
 * @author Administrator
 * 仅用于部分对接平台调试数据及问题追踪使用
 */
@Controller
@RequestMapping("/sys/util/")
public class UtilController extends AbstractBaseController{

	private static final String ENCODING_CHARSET = "UTF-8";
	
	/*----------------------------------AES-------------------------------*/
	@RequestMapping("aes")
	public String aesUtil(Model model){		
		return "/sys/util/AES";
	}
	
	@RequestMapping("aesEncrypt")
	@ResponseBody
	public ResultInfo<String> aesEncrypt(Model model, String content, String key){
		FailInfo failInfo = validateData(content, key);
		if (null != failInfo) return new ResultInfo<>(failInfo);
		try {
			byte[] bContent = Base64.decodeBase64(content.getBytes(ENCODING_CHARSET));
			byte[] bKey = Base64.decodeBase64(key.getBytes(ENCODING_CHARSET));
			byte[] bNormalResult = Base64.encodeBase64(AESUtil.encrypt(bContent, bKey));
			return new ResultInfo<>(new String(bNormalResult));
		} catch (Exception e) {
			return new ResultInfo<>(new FailInfo("异常"));
		}
	}
	@RequestMapping("aesDecrypt")
	@ResponseBody
	public ResultInfo<String> aesDecrypt(Model model, String content, String key){
		FailInfo failInfo = validateData(content, key);
		if (null != failInfo) return new ResultInfo<>(failInfo);
		try {
			byte[] bContent = Base64.decodeBase64(content.getBytes(ENCODING_CHARSET));
			byte[] bKey = Base64.decodeBase64(key.getBytes(ENCODING_CHARSET));
			byte[] bNormalResult = AESUtil.decrypt(bContent, bKey);
			String base64Result = new String(Base64.encodeBase64(bNormalResult));
			String normalResult = new String(bNormalResult);
			StringBuffer sbBuffer = new StringBuffer();
			sbBuffer.append("BASE64加密结果:").append(base64Result).append("\r\n");
			sbBuffer.append("正常结果:").append(normalResult);
			return new ResultInfo<>(sbBuffer.toString());
		} catch (Exception e) {
			return new ResultInfo<>(new FailInfo("异常"));
		}	
	}
	
	/*-----------------------------------------RSA----------------------------------------------*/
	@RequestMapping("rsa")
	public String rsa(Model model){
		return "/sys/util/RSA";
	}
	
	//签名
	@RequestMapping("rsaSign")
	@ResponseBody
	public ResultInfo<String> rsaSign(Model model, String content, String key){
		FailInfo failInfo = validateData(content, key);
		if (failInfo != null) return new ResultInfo<>(failInfo);
		try {
			byte[] bContent = Base64.decodeBase64(content.getBytes(ENCODING_CHARSET));
			byte[] bKey = Base64.decodeBase64(key.getBytes(ENCODING_CHARSET));
			byte[] bSign = RSAUtil.sign(bContent, bKey);
			return new ResultInfo<>(new String(Base64.encodeBase64(bSign)));
		} catch (Exception e) {
			return new ResultInfo<>(new FailInfo("异常"));
		}
	}
	
	//验签
	@RequestMapping("rsaVerify")
	@ResponseBody
	public ResultInfo<String> rsaVerify(Model model, String content, String key, String sign){
		FailInfo failInfo = validateData(content, key);
		if (null != failInfo) return new ResultInfo<>(failInfo);
		if (StringUtil.isEmpty(sign) || !Base64.isBase64(sign)) {
			return new ResultInfo<>(new FailInfo("签名错误(为空或不为base64加密)"));
		}
		try {
			byte[] bContent = Base64.decodeBase64(content.getBytes(ENCODING_CHARSET));
			byte[] bKey = Base64.decodeBase64(key.getBytes(ENCODING_CHARSET));
			byte[] bSign = Base64.decodeBase64(sign.getBytes(ENCODING_CHARSET));
			boolean bSuccess = RSAUtil.verify(bContent, bKey, bSign);
			return new ResultInfo<>(bSuccess ? "验签通过":"验签失败");
		} catch (Exception e) {
			return new ResultInfo<>(new FailInfo("异常"));
		}
	}
	
	private FailInfo validateData(String content, String key){
		if (StringUtil.isEmpty(content)) {
			return new FailInfo("加解密内容为空");
		}
		if (StringUtil.isEmpty(key)) {
			return new FailInfo("加解密KEY为空");
		}
		if (!Base64.isBase64(content) || !Base64.isBase64(key)) {
			return new FailInfo("数据非法,不为Base64加密内容");
		}
		return null;
	}
}
