package com.tp.world.ao.order;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.common.vo.customs.JKFConstant.JKFFeedbackType;
import com.tp.common.vo.customs.JKFConstant.JKFResultError;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.JKF.JkfBaseDO;
import com.tp.model.ord.JKF.JkfCallbackResponse;
import com.tp.model.ord.JKF.JkfCancelOrderResult;
import com.tp.model.ord.JKF.JkfGoodsDeclarResult;
import com.tp.model.ord.JKF.JkfImportBillResult;
import com.tp.model.ord.JKF.JkfReceiptResult;
import com.tp.model.ord.JKF.JkfTaxIsNeedResult;
import com.tp.proxy.ord.JKFServiceProxy;
import com.tp.proxy.ord.customs.JkfCustomsClearanceCallbackProxy;
import com.tp.world.helper.JKFHelper;

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
	}
	
	public JkfCallbackResponse clearanceCallback(String content, String signature, String messageType) throws Exception{		
		String decryptContent = decryptContent(content);
		if(false == verifyContent(decryptContent, signature)){
			logger.error("[JKF_CALLBACK]回执数据验签失败:{},{}", decryptContent, signature);
			return new JkfCallbackResponse(JKFResultError.INVALID_SIGN);
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
	
	public JkfCallbackResponse callback(String content, String signature, String messageType){
		JkfCallbackResponse callbackResponse = null;	
		try {
			String data = content;
			if (false == isTest) {
				data = decryptContent(content);
				boolean bVerify = verifyContent(data, signature); 
				if(!bVerify){ 		//验签失败
					logger.error("[JKF_CALLBACK]回执数据验签失败:{},{}", data, signature);
					return new JkfCallbackResponse(JKFResultError.INVALID_SIGN);
				}	
			}	
			if (JKFFeedbackType.CUSTOMS_DECLARE_RESULT_CALLBACK.type.equals(messageType)) {				
				logger.info("[JKF_CALLBACK][CUSTOMS_CALLBACK]口岸回执：messageType={}", messageType);
				callbackResponse = customsDeclCallback(data);
			}else if(JKFFeedbackType.CUSTOMS_DECLARE_GOODS_CALLBACK.type.equals(messageType)){			
				logger.info("[JKF_CALLBACK][CLEARANCE_CALLBACK]海关回执：messageType={}", messageType);
				callbackResponse = customsGoodsDeclCallback(data);
			}else if(JKFFeedbackType.CUSTOMS_BILL_CALLBACK.type.equals(messageType)){				
				logger.info("[JKF_CALLBACK][BILL_CALLBACK]进出口出区回执：messageType={}", messageType);
				callbackResponse = customsBillCallback(data);
			}else if(JKFFeedbackType.CUSTOMS_TAX_CALLBACK.type.equals(messageType)){
				
				logger.info("[JKF_CALLBACK][TAX_CALLBACK]税款回执：messageType={}", messageType);
				callbackResponse = customsTaxCallback(data);
			}else{
				return new JkfCallbackResponse(JKFResultError.INVALID_BUSINESS_TYPE);
			}	
		} catch (Exception e) {
			logger.error("[JKF_CALLBACK]回执数据异常", e);
			return new JkfCallbackResponse(JKFResultError.SYSTEM_EXCEPTION);
		}
		return callbackResponse;
	}
	
	/*-------------------------------------------------------------------------------*/
	/**
	 * 处理跨境电商平台回执报文
	 */
	private JkfCallbackResponse customsDeclCallback(String message){
		JkfCallbackResponse jkfCallbackResponse = null;
		logger.info("[JKF_CALLBACK][CUSTOMS_CALLBACK]口岸回执：message={}", message);
		try {
			JkfReceiptResult receiptResult = JKFHelper.XmlUtil.xmlToBean(message, JkfReceiptResult.class);
			if (CollectionUtils.isEmpty(receiptResult.getBody().getList())) {
				logger.error("[JKF_CALLBACK][CUSTOMS_CALLBACK]口岸回执数据错误");
				return new JkfCallbackResponse(JKFResultError.INVALID_CONTENT);
			}
			ResultInfo<JkfCallbackResponse> resultInfo = jkfServiceProxy.customsDeclareCallback(receiptResult);
			if (!resultInfo.isSuccess()) {				
				logger.error("[JKF_CALLBACK][CUSTOMS_CALLBACK]回执处理失败：{}", resultInfo.getMsg().getMessage());
				jkfCallbackResponse = new JkfCallbackResponse(JKFResultError.SYSTEM_EXCEPTION);
			}
			jkfCallbackResponse = new JkfCallbackResponse();			
		} catch (Exception e) {
			logger.error("[JKF_CALLBACK][CUSTOMS_CALLBACK]回执处理异常", e);
			jkfCallbackResponse = new JkfCallbackResponse(JKFResultError.SYSTEM_EXCEPTION);
		}
		return jkfCallbackResponse;
	}
	
	/**
	 * 处理个人物品申报单审单结果（海关审单结果）(包括删单)
	 */
	private JkfCallbackResponse customsGoodsDeclCallback(String message){
		JkfCallbackResponse jkfCallbackResponse = null;
		logger.info("[JKF_CALLBACK][CLEARANCE_CALLBACK]海关回执,清单审批结果：{}", message);
		try {
			JkfGoodsDeclarResult declResult = JKFHelper.XmlUtil.xmlToBean(message, JkfGoodsDeclarResult.class);
			if (null == declResult.getBody() || null == declResult.getBody().getJkfSign() 
					|| null == declResult.getBody().getJkfGoodsDeclar()) {
				logger.error("[JKF_CALLBACK][CLEARANCE_CALLBACK]清单回执数据异常");
				return new JkfCallbackResponse(JKFResultError.INVALID_CONTENT);
			}
			ResultInfo<JkfCallbackResponse> resultInfo = jkfServiceProxy.goodsDeclCallback(declResult);
			if (!resultInfo.isSuccess()) {
				logger.error("[JKF_CALLBACK][CLEARANCE_CALLBACK]清单回执处理失败：{}", resultInfo.getMsg().getMessage());
				jkfCallbackResponse = new JkfCallbackResponse(JKFResultError.SYSTEM_EXCEPTION);
			}
			return resultInfo.getData();			
		} catch (Exception e) {
			logger.error("[API接口 - 电子口岸回调 - 个人物品申报单回执异常]", e);
			jkfCallbackResponse = new JkfCallbackResponse(JKFResultError.SYSTEM_EXCEPTION);
		}
		return jkfCallbackResponse;
	}
	
	/**
	 * 处理进口运单出区回执
	 */
	private JkfCallbackResponse customsBillCallback(String message){
		logger.info("[API接口 - 电子口岸回调 - 进口运单出区回执]={}", message);
		return new JkfCallbackResponse();
	}
	
	/**
	 * 处理税款回传
	 * 记录个人物品申报单的交税额作为海关税款流水记录
	 */
	private JkfCallbackResponse customsTaxCallback(String message){
		JkfCallbackResponse jkfCallbackResponse = null;
		logger.info("[API接口 - 电子口岸回调 - 处理税款回传]={}", message);
		try {
			JkfTaxIsNeedResult result = JKFHelper.XmlUtil.xmlToBean(message, JkfTaxIsNeedResult.class);
			if (null == result.getBody() || null == result.getBody().getJkfSign() 
					|| null == result.getBody().getJkfTaxIsNeedDto()) {
				return new JkfCallbackResponse(JKFResultError.INVALID_CONTENT);
			}
			ResultInfo<JkfCallbackResponse> resultInfo = jkfServiceProxy.taxIsNeedCallback(result);
			if (!resultInfo.isSuccess()) {
				logger.error("[API接口 - 电子口岸回调 - 处理税款回传异常]={}", resultInfo.getMsg().getMessage());
				jkfCallbackResponse = new JkfCallbackResponse(JKFResultError.SYSTEM_EXCEPTION);
			}
			return resultInfo.getData();			
		} catch (Exception e) {
			logger.error("[API接口 - 电子口岸回调 - 处理税款回传异常]", e);
			jkfCallbackResponse = new JkfCallbackResponse(JKFResultError.SYSTEM_EXCEPTION);
		}
		return jkfCallbackResponse;
	}
	
	/**
	 * 处理税款回传
	 * 记录个人物品申报单的交税额作为海关税款流水记录
	 */
	private JkfCallbackResponse customsCancelOrderCallback(String message){
		JkfCallbackResponse jkfCallbackResponse = null;
		logger.info("[API接口 - 电子口岸回调 - 删单回执]={}", message);
		try {
			JkfCancelOrderResult result = JKFHelper.XmlUtil.xmlToBean(message, JkfCancelOrderResult.class);
			if (null == result.getBody() || null == result.getBody().getModifyCancelResult()) {
				return new JkfCallbackResponse(JKFResultError.INVALID_CONTENT);
			}
			ResultInfo<JkfCallbackResponse> resultInfo = jkfServiceProxy.cancelOrderCallback(result);
			if (!resultInfo.isSuccess()) {
				logger.error("[API接口 - 电子口岸回调 - 删单回执异常]={}", resultInfo.getMsg().getMessage());
				jkfCallbackResponse = new JkfCallbackResponse(JKFResultError.SYSTEM_EXCEPTION);
			}
			return resultInfo.getData();			
		} catch (Exception e) {
			logger.error("[API接口 - 电子口岸回调 - 删单回执异常]", e);
			jkfCallbackResponse = new JkfCallbackResponse(JKFResultError.SYSTEM_EXCEPTION);
		}
		return jkfCallbackResponse;
	}
	
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
		return new String(JKFHelper.AESUtil.decrypt(input_content, aes_key), COMMON_ENCODE);
	}
	/**
	 *	验签 
	 */
	private boolean verifyContent(String originalContent, String signature) throws Exception{
		if(isTest()) return true;
		byte[] input = originalContent.getBytes(COMMON_ENCODE);
		byte[] publicKey = Base64.decodeBase64(zjPublicKey);
		byte[] sign = Base64.decodeBase64(signature);
		return JKFHelper.RSAUtil.verify(input, publicKey, sign);
	}

	public boolean isTest() {
		return isTest;
	}	
}
