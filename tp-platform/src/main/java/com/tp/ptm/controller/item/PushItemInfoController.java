package com.tp.ptm.controller.item;


import java.io.BufferedReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.ptm.ErrorCodes.SystemError;
import com.tp.dto.prd.PushItemCostpriceDto;
import com.tp.dto.prd.PushItemInfoAndDetailDto;
import com.tp.dto.ptm.ReturnData;
import com.tp.model.prd.ItemSku;
import com.tp.ptm.annotation.Authority;
import com.tp.ptm.ao.item.PushItemInfoServiceAO;

@RequestMapping("/item")
@Controller
public class PushItemInfoController {
	
	private static final Logger logger = LoggerFactory.getLogger(PushItemInfoController.class);

	@Autowired
	private PushItemInfoServiceAO pushItemInfoServiceAO;
	
	@RequestMapping(value="/pushItemInfo",method = RequestMethod.POST)
	@ResponseBody
	@Authority
	public ReturnData pushItemInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody PushItemInfoAndDetailDto pushItemInfoAndDetailDto){
		String appKey = request.getParameter("appkey");
		Long currentUserId= pushItemInfoServiceAO.getCurrentUserIdByAppKey(appKey);
		if(null ==currentUserId){
			logger.info("未获取到用户信息");
		} else {
			logger.info(currentUserId.toString());
		}
		if(currentUserId == null){
			logger.error("未获取到banckend user 关联的当前用户id");
			return new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code, SystemError.PARAM_ERROR.cnName);
		}
		ReturnData rtData = new ReturnData(Boolean.TRUE);
	
		
			if (pushItemInfoAndDetailDto != null) {
				rtData	=pushItemInfoServiceAO.pushItemInfo(pushItemInfoAndDetailDto,request,currentUserId,appKey);
			}
			else {
				rtData = new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code,"未获取到json数据");
			}
		
		if(!rtData.getIsSuccess()){
			logger.info("商品插入不成功,返回错误信息为:"+rtData.getData().toString());
		}
		return rtData;
		
	}
	
	protected String getRequestContent(HttpServletRequest request) {
		try {
			BufferedReader reader = request.getReader();
			StringBuilder content = new StringBuilder();
			String line = null;

			do {
				line = reader.readLine();
				if (line != null) {
					content.append(line);
				}
			} while (line != null);

			return content.toString();
		} catch (Exception e) {
			logger.error("获取请求数据异常", e);
		}
		return null;
	}
	
//	public static void main(String []args){
//		String hehe="{'sex':'nv','name':'heheda','detail':[{'naintitle':'hehead','storge':'hehewa'}]}";
//		User parseObject = JSONObject.parseObject(hehe,User.class);
//		String detail = parseObject.getDetail();
//		JSONArray parseArray = JSONObject.parseArray(detail);
////		JSONObject object = (JSONObject)parseArray.get(0);
//	//	JSONObject.pa
//	//	JSONObject parseObject2 = JSONObject.parseObject(detail);
//		JSONObject object = (JSONObject)parseArray.get(0);
//		Object object2 = (String)object.get("naintitle");
//		Object object3 = (String)object.get("storge");
//		System.out.println(parseArray);
//	}
//	public static void main(String []args){
//		String ssf="{'brandId': 1,'spuName':'\u8BF7\u9009\u62E9'}";
//	// PushItemInfoAndDetailDto parseObject = JSONObject.parseObject(ssf,PushItemInfoAndDetailDto.class);
//	
//	 System.out.println(unescapeJson);
//	}
	
	@RequestMapping(value="/pushItemCostprice",method = RequestMethod.POST)
	@ResponseBody
	@Authority
	public ReturnData pushItemCostpriceApi(HttpServletRequest request, HttpServletResponse response, @RequestBody PushItemCostpriceDto pushItemCostpriceDto){
		String appKey = request.getParameter("appkey");
		Long currentUserId= pushItemInfoServiceAO.getCurrentUserIdByAppKey(appKey);
		if(null ==currentUserId){
			logger.info("未获取到用户信息");
		} else {
			logger.info(currentUserId.toString());
		}
		if(currentUserId == null){
			logger.error("未获取到banckend user 关联的当前用户id");
			return new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code, SystemError.PARAM_ERROR.cnName);
		}
		ReturnData rtData = new ReturnData(Boolean.TRUE);
		if (pushItemCostpriceDto != null ) {
			rtData	=pushItemInfoServiceAO.setItemCostprice(pushItemCostpriceDto,request,currentUserId,appKey);
		}
		else {
			rtData = new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code,"未获取到json数据");
		}
		
		if(!rtData.getIsSuccess()){
			logger.info("商品插入不成功,返回错误信息为:"+rtData.getData().toString());
		}
		return rtData;
		
	}
	
}
