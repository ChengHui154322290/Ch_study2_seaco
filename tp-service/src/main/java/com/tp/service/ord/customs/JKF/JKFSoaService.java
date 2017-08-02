package com.tp.service.ord.customs.JKF;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.customs.JKFConstant;
import com.tp.common.vo.sys.CommonLogConstant.RequestMethod;
import com.tp.common.vo.sys.CommonLogConstant.RestLogType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.JKF.JkfCancelOrderRequest;
import com.tp.model.ord.JKF.JkfGoodsDeclareRequest;
import com.tp.model.ord.JKF.JkfImportOrderRequest;
import com.tp.model.ord.JKF.JkfReceiptResult;
import com.tp.model.sys.RestLog;
import com.tp.service.ord.IJKFSoaService;
import com.tp.service.ord.webservice.client.JKFSoapUtil;
import com.tp.service.ord.webservice.client.JkfSoapClient;
import com.tp.service.sys.IRestLogService;

@Service
public class JKFSoaService implements IJKFSoaService{

	private static final Logger LOGGER = LoggerFactory.getLogger(JKFSoaService.class);
	
	private static final String DEFAULT_ENCODE = "UTF-8";
	
	@Value("#{meta['JKF.self.privatekey']}")
	private String privateKey;
	
	@Value("#{meta['JKF.self.AESKey']}")
	private String aesKey;
	
//	@Value("#{meta['JKF.XG.companyCode']}")
//	private String companyCode;
	
	@Autowired
	private JkfSoapClient jkfSoapClient;
	
	@Autowired
	private IRestLogService restLogService;
//	@Autowired
//	private JkfCheckGoodsSoapClient jkfCheckGoodsSoapClient;
	
	/**
	 * 订单信息申报 
	 */
	@Override
	public ResultInfo<JkfReceiptResult> orderDeclare(JkfImportOrderRequest declareData, boolean isEncrypt) {
		try {
			String data = JKFSoapUtil.XmlUtil.beanToXML(declareData);
			String result = "";
			String requestData = data;
			String signature = "";
			String url = "";
			String companyCode = declareData.getBody().getOrderInfoList().get(0).getJkfSign().getCompanyCode();
			if (isEncrypt) { //加密
				byte[] aes_key = Base64.decodeBase64(aesKey.getBytes(DEFAULT_ENCODE));
				byte[] encryptData = JKFSoapUtil.AESUtil.encrypt(data.getBytes(DEFAULT_ENCODE), aes_key);
				requestData = Base64.encodeBase64String(encryptData);
				byte[] private_key = Base64.decodeBase64(privateKey.getBytes(DEFAULT_ENCODE));
				byte[] signData = JKFSoapUtil.RSAUtil.sign(data.getBytes(DEFAULT_ENCODE), private_key);
				signature = Base64.encodeBase64String(signData);
				result = jkfSoapClient.declareData(requestData, JKFConstant.JKFBusinessType.IMPORTORDER.type, signature, companyCode);
				url = jkfSoapClient.getEncryptUrl();
			}else{
				result = jkfSoapClient.declareData(data, JKFConstant.JKFBusinessType.IMPORTORDER.type, "1");
				url = jkfSoapClient.getUrl();
			}
			addLog(url, RestLogType.D_HZ_ORDER.code, requestData, signature, result);
			return new ResultInfo<>(JKFSoapUtil.XmlUtil.xmlToBean(result, JkfReceiptResult.class));
		} catch (Exception e) {
			LOGGER.error("[电子口岸申报数据 - 申报订单数据异常]", e);
			return new ResultInfo<>(new FailInfo("订单信息申报异常：" + e.getMessage()));
		}
	}

	/**
	 * 申报个人物品信息
	 */
	@Override
	public ResultInfo<JkfReceiptResult> personalGoodsDeclare(JkfGoodsDeclareRequest declareData, boolean isEncrypt) {
		try {
			String data = JKFSoapUtil.XmlUtil.beanToXML(declareData);
			String result = "";
			String requestData = data;
			String signature = "";
			String url = "";
			String companyCode = declareData.getBody().getGoodsDeclareModuleList().get(0).getJkfSign().getCompanyCode();
			if (isEncrypt) {
				byte[] aes_key = Base64.decodeBase64(aesKey.getBytes(DEFAULT_ENCODE));
				byte[] encryptData = JKFSoapUtil.AESUtil.encrypt(data.getBytes(DEFAULT_ENCODE), aes_key);
				requestData = Base64.encodeBase64String(encryptData);
				byte[] private_key = Base64.decodeBase64(privateKey.getBytes(DEFAULT_ENCODE));
				byte[] signData = JKFSoapUtil.RSAUtil.sign(data.getBytes(DEFAULT_ENCODE), private_key);
				signature = Base64.encodeBase64String(signData);
				result = jkfSoapClient.declareData(requestData, JKFConstant.JKFBusinessType.PERSONAL_GOODS_DECLAR.type, signature, companyCode);
				url = jkfSoapClient.getEncryptUrl();
			}else {
				result = jkfSoapClient.declareData(data, JKFConstant.JKFBusinessType.PERSONAL_GOODS_DECLAR.type, "1");
				url = jkfSoapClient.getUrl();
			}
			addLog(url, RestLogType.D_HZ_CLEARANCE.code, requestData, signature, result);
			return new ResultInfo<>(JKFSoapUtil.XmlUtil.xmlToBean(result, JkfReceiptResult.class));
		} catch (Exception e) {
			LOGGER.error("[电子口岸申报数据 - 申报个人物品信息数据异常]", e);
			return new ResultInfo<>(new FailInfo("个人物品信息申报异常：" + e.getMessage()));
		}
	}

	/**
	 * 删除订单
	 */
	@Override
	public ResultInfo<JkfReceiptResult> cancelOrderDeclare(JkfCancelOrderRequest cancelOrderRequest, boolean isEncrypt) {
		try {
			String data = JKFSoapUtil.XmlUtil.beanToXML(cancelOrderRequest);
			String result = "";
			String requestData = data;
			String signature = "";
			String url = "";
			String companyCode = cancelOrderRequest.getBody().getJkfSign().getCompanyCode();
			if (isEncrypt) {
				byte[] aes_key = Base64.decodeBase64(aesKey.getBytes(DEFAULT_ENCODE));
				byte[] encryptData = JKFSoapUtil.AESUtil.encrypt(data.getBytes(DEFAULT_ENCODE), aes_key);
				requestData = Base64.encodeBase64String(encryptData);
				byte[] private_key = Base64.decodeBase64(privateKey.getBytes(DEFAULT_ENCODE));
				byte[] signData = JKFSoapUtil.RSAUtil.sign(data.getBytes(DEFAULT_ENCODE), private_key);
				signature = Base64.encodeBase64String(signData);
				result = jkfSoapClient.declareData(requestData, JKFConstant.JKFBusinessType.MODIFY_CANCEL.type, signature, companyCode);
				url = jkfSoapClient.getEncryptUrl();
			}else {
				result = jkfSoapClient.declareData(data, JKFConstant.JKFBusinessType.MODIFY_CANCEL.type, "1");
				url = jkfSoapClient.getUrl();
			}
			addLog(url, RestLogType.D_HZ_CANCELORDER.code, requestData, signature, result);
			return new ResultInfo<>(JKFSoapUtil.XmlUtil.xmlToBean(result, JkfReceiptResult.class));
		} catch (Exception e) {
			LOGGER.error("[电子口岸申报数据 - 删除订单异常]", e);
			return new ResultInfo<>(new FailInfo("删除订单异常：" + e.getMessage()));
		}
	}	
	//增加推送日志
	private void addLog(String url, String type, String requestData, String sign, String result){
		try {
			Map<String, String> mapData = new HashMap<>();
			mapData.put("data", requestData);
			mapData.put("signature", sign);
			String content = JSONObject.toJSONString(mapData);
			RestLog log = new RestLog(url, type, RequestMethod.WEBSERVICE.code, content, new String(Base64.encodeBase64(result.getBytes())));
			restLogService.insert(log);
		} catch (Exception e) {
			LOGGER.error("写入日志异常", e);
		}
	}
}
