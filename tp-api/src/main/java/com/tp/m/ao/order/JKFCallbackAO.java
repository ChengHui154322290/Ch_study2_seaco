package com.tp.m.ao.order;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.common.vo.customs.JKFConstant.JKFFeedbackType;
import com.tp.common.vo.customs.JKFConstant.JKFResultError;
import com.tp.dto.common.ResultInfo;
import com.tp.m.helper.JKFHelper;
import com.tp.m.helper.JKFHelper.AESUtil;
import com.tp.m.helper.JKFHelper.RSAUtil;
import com.tp.model.ord.JKF.JkfBaseDO;
import com.tp.model.ord.JKF.JkfCallbackResponse;
import com.tp.model.ord.JKF.JkfGoodsDeclarResult;
import com.tp.model.ord.JKF.JkfImportBillResult;
import com.tp.model.ord.JKF.JkfReceiptResult;
import com.tp.model.ord.JKF.JkfTaxIsNeedResult;
import com.tp.model.ord.ceb.CebGoodsResponse;
import com.tp.model.ord.ceb.CebOrderResponse;
import com.tp.proxy.ord.JKFServiceProxy;
import com.tp.proxy.ord.customs.JkfCustomsClearanceCallbackProxy;
import com.tp.util.StringUtil;

@Service
public class JKFCallbackAO {
	
	private static final Logger logger = LoggerFactory.getLogger(JKFCallbackAO.class);
	
	private static final String COMMON_ENCODE = "utf-8";
	
	@Autowired
	private JKFServiceProxy jkfServiceProxy;
	
	@Autowired
	private JkfCustomsClearanceCallbackProxy jkfCustomsClearanceCallbackProxy;
	
	@Value("#{meta['JKF.ZJ.AESKey']}")
	private String aesKey;
	@Value("#{meta['JKF.ZJ.publickey']}")
	private String zjPublicKey;
	@Value("#{meta['JKF.ZJ.isTest']}")
	private boolean isTest;

	public static final Map<JKFFeedbackType, Class> CALLBACK_TYPE_MAP = new HashMap<>();
	static{
		//通关平台回执：可能包括订单申报和个人物品申报的回执
		CALLBACK_TYPE_MAP.put(JKFFeedbackType.CUSTOMS_DECLARE_RESULT_CALLBACK, JkfReceiptResult.class);
		//个人物品申报海关审单回执：包括进度信息
		CALLBACK_TYPE_MAP.put(JKFFeedbackType.CUSTOMS_DECLARE_GOODS_CALLBACK, JkfGoodsDeclarResult.class);
		//进口运单出区回执
		CALLBACK_TYPE_MAP.put(JKFFeedbackType.CUSTOMS_BILL_CALLBACK, JkfImportBillResult.class);
		//税款回传
		CALLBACK_TYPE_MAP.put(JKFFeedbackType.CUSTOMS_TAX_CALLBACK, JkfTaxIsNeedResult.class);
		//总署订单回执
		CALLBACK_TYPE_MAP.put(JKFFeedbackType.CUSTOMS_CEB_CALLBACK_ORDER, CebOrderResponse.class);
		//总署版清关单回执
		CALLBACK_TYPE_MAP.put(JKFFeedbackType.CUSTOMS_CEB_CALLBACK_GOODS, CebGoodsResponse.class);
	}
		
	//type： 1订单回执2清单回执
	public JkfCallbackResponse clearanceCallback(String content, String signature, String messageType, Integer type) throws Exception{		
		String decryptContent = decryptContent(content);
		if(false == verifyContent(decryptContent, signature)){
			logger.error("[JKF_CALLBACK]回执数据验签失败:{},{}", decryptContent, signature);
			return new JkfCallbackResponse(JKFResultError.INVALID_SIGN);
		}
		messageType = convertClearanceCallbackMessageType(messageType, type);
		if (StringUtil.isEmpty(messageType)) {
			logger.error("[JKF_CALLBACK]回执类型错误:{},{}", messageType, type);
			return new JkfCallbackResponse(JKFResultError.INVALID_BUSINESS_TYPE);
		}
		JkfBaseDO receiptResult = getReceiptResultByMessageType(decryptContent, messageType);
		if (receiptResult == null){
			logger.error("[JKF_CALLBACK]回执数据异常, content = {}, type = {}", decryptContent, messageType);
			return new JkfCallbackResponse(JKFResultError.INVALID_CONTENT);
		}
		ResultInfo<JkfCallbackResponse> resultInfo = jkfCustomsClearanceCallbackProxy.clearanceCallback(receiptResult);
		if(Boolean.FALSE == resultInfo.isSuccess()){
			logger.error("[JKF_CALLBACK]回执数据处理异常: {}", resultInfo.getMsg().getMessage());
			return new JkfCallbackResponse(JKFResultError.SYSTEM_EXCEPTION);
		}
		return resultInfo.getData();
	}

	private String convertClearanceCallbackMessageType(String messageType, Integer type){
		if (JKFFeedbackType.CUSTOMS_CEB_CALLBACK.type.equals(messageType)){
			if (type == 1) {
				messageType = JKFFeedbackType.CUSTOMS_CEB_CALLBACK_ORDER.type;
			}else if(type == 2) {
				messageType = JKFFeedbackType.CUSTOMS_CEB_CALLBACK_GOODS.type;
			}else{
				return null;
			}
		}
		return messageType;
	}
	
	//type 1订单2清单
	private JkfBaseDO getReceiptResultByMessageType(String content, String messageType){
		JKFFeedbackType feedbackType = JKFFeedbackType.getTypeByType(messageType);
		if (feedbackType == null) return null;
		
		Class receiptClass = CALLBACK_TYPE_MAP.get(feedbackType);
		if (receiptClass == null) return null;
		JkfBaseDO receiptResult = JKFHelper.XmlUtil.xmlToBean(content, receiptClass);
		if (receiptResult != null) receiptResult.setReceiptType(feedbackType);
		return receiptResult;
	}
	
	/**
	 * 解密
	 */
	private String decryptContent(String content) throws Exception{
		if(isTest()) return content;
		byte[] input_content = Base64.decodeBase64(content.getBytes(COMMON_ENCODE));
		byte[] aes_key = Base64.decodeBase64(aesKey.getBytes(COMMON_ENCODE));
		return new String(AESUtil.decrypt(input_content, aes_key), COMMON_ENCODE);
	}
	/**
	 *	验签 
	 */
	private boolean verifyContent(String originalContent, String signature) throws Exception{
		if(isTest()) return true;
		byte[] input = originalContent.getBytes(COMMON_ENCODE);
		byte[] publicKey = Base64.decodeBase64(zjPublicKey);
		byte[] sign = Base64.decodeBase64(signature);
		return RSAUtil.verify(input, publicKey, sign);
	}

	public boolean isTest() {
		return isTest;
	}	
}
