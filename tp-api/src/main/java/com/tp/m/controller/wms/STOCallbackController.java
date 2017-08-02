package com.tp.m.controller.wms;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tp.dto.wms.sto.STOStockoutBackDto;
import com.tp.dto.wms.sto.STOStocksyncInfoDto;
import com.tp.m.ao.wms.STOCallbackAO;
import com.tp.m.base.BaseQuery;
import com.tp.m.helper.RequestHelper;

/**
 * 申通WMS 
 */

@Controller
@RequestMapping(value = "/sto")
public class STOCallbackController{
	
	private static final Logger logger = LoggerFactory.getLogger(STOCallbackController.class);
	
	@Autowired
	private STOCallbackAO stoCallbackAO;
	
	/** 订单状态 */
	@RequestMapping(value = "/order_status", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public String callbackOrderStatus(HttpServletRequest request){
		boolean success = false;
		try {	
			String content = RequestHelper.getJsonStrByIO(request);
			String decodeContent = URLDecoder.decode(content, "UTF-8");
			String prefix = "JSON_OBJ=";
			String jsonStr = decodeContent.substring(prefix.length());
			logger.info("[STOCKOUT_BACK]出库单回执数据：{}", jsonStr);
			assertNull(jsonStr, "请求数据为空");
			String jsonData = jsonStr;
			STOStockoutBackDto dto = JSONObject.parseObject(jsonData, STOStockoutBackDto.class);
			success = stoCallbackAO.stockoutCallback(dto);
		} catch (Exception e) {
			logger.error("[STOCKOUT_BACK][申通仓回调接口 -异常]", e);
		}
		return success ? "success":"fail";
	}

	/** 库存回调 */
	@RequestMapping(value = "/sku_inventory", method = RequestMethod.POST)
	@ResponseBody
	public String callbackSkuInventory(HttpServletRequest request){
		boolean success = false;
		try {	
			String content = RequestHelper.getJsonStrByIO(request);
			String decodeContent = URLDecoder.decode(content, "UTF-8");
			String prefix = "JSON_OBJ=";
			String jsonStr = decodeContent.substring(prefix.length());
			assertNull(jsonStr, "请求数据为空");
			logger.info("[STOCKOUT_BACK]库存回执数据：" + jsonStr);
			String jsonData = jsonStr;
			STOStocksyncInfoDto dto = JSONObject.parseObject(jsonData, STOStocksyncInfoDto.class);
			success = stoCallbackAO.skuInventoryCallback(dto);
		} catch (Exception e) {
			logger.error("[STOCKOUT_BACK][申通仓回调接口 -异常]", e);
		}
		return success ? "success":"fail";
	}
	
	private void assertNull(String value, String error) throws Exception{
		if (StringUtils.isEmpty(value)) {
			throw new Exception(error);
		}
	}
}
