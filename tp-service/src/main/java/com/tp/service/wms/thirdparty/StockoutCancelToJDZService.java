/**
 * 
 */
package com.tp.service.wms.thirdparty;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.stg.WarehouseConstant.WMSCode;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.Stockout;
import com.tp.model.wms.StockoutCancel;
import com.tp.model.wms.jdz.JdzRequestUser;
import com.tp.model.wms.jdz.JdzStockoutCancelOrderRequest;
import com.tp.result.wms.ResultMessage;
import com.tp.service.wms.thirdparty.IStockoutCancelToTPService;
import com.tp.util.HttpClientUtil;

/**
 * @author Administrator
 *
 */
@Service
public class StockoutCancelToJDZService implements IStockoutCancelToTPService{

	private static final Logger logger = LoggerFactory.getLogger(StockoutCancelToJDZService.class);
	
	@Value("#{meta['wms.jdz.saleOrderCancelurl']}")
	private String serviceUrl;
	
	@Value("#{meta['wms.jdz.appKey']}")
	private String jdzAppKey;
	
	@Value("#{meta['wms.jdz.password']}")
	private String jdzSecret;
	
	//账册编号
	@Value("#{meta['wms.jdz.manualNo']}")
	private String manualNo;
	
	@Value("#{meta['wms.jdz.goodsOwner']}")
	private String goodsOwner;
	
	@Override
	public boolean check(Stockout stockout) {
		if (StringUtils.isNotEmpty(stockout.getWmsCode()) && 
				WMSCode.JDZ_HZ.code.equals(stockout.getWmsCode())) {
			return true;
		}
		return false;
	}
	
	/** 
	 * 取消订单（只用于清关未通过且已经推送给仓库的订单，需要再次请求仓库取消配货）
	 */
	@Override
	public ResultInfo<StockoutCancel> cancelOrder(String orderCode) {
		StockoutCancel cancel = new StockoutCancel();
		cancel.setOrderCode(orderCode);
		cancel.setCancelTime(new Date());
		cancel.setCreateTime(new Date());
		//请求数据
		JdzStockoutCancelOrderRequest request = new JdzStockoutCancelOrderRequest();
		request.setOrderNo(orderCode);
		request.setGoodsOwner(goodsOwner);
		request.setRemark("取消订单");
		try {			
			String serviceId = getServiceId();
			String content = JDZHelper.AESEncrypt.encrytor(JSONArray.toJSONString(request));	
			Map<String, String> headers = new HashMap<>();
			headers.put("serviceId", serviceId);
			String response = HttpClientUtil.postData(serviceUrl, content, "application/json", headers);
			ResultMessage result = (ResultMessage)JSONObject.parseObject(response, ResultMessage.class);
			cancel.setResMsg(response);
			cancel.setReqMsg(JSONArray.toJSONString(request));
			if (result.isSuccess()) {
				cancel.setSuccess(1);
			}else{
				cancel.setSuccess(0);
				cancel.setError(result.getError());
			}
			return new ResultInfo<>(cancel);
		} catch (Exception e) {
			logger.error("向公共仓取消订单异常", e);
			return new ResultInfo<>(new FailInfo("取消订单异常"));
		}
	}
	
	private String getServiceId(){
		JdzRequestUser user = new JdzRequestUser();
		user.setAppkey(jdzAppKey);
		user.setSecret(JdzRequestUser.encryptSecret(jdzSecret));
		return JSONObject.toJSONString(user);
	}

}
