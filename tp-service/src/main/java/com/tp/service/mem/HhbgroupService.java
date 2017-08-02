/**  
 * Project Name:xg-shop  
 * File Name:HhbgroupAo.java  
 * Package Name:com.tp.shop.ao.promotion  
 * Date:2016年11月18日下午4:26:52  
 * Copyright (c) 2016, seagoor All Rights Reserved.  
 *  
*/

package com.tp.service.mem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.tp.dto.ord.HhbShopOrderInfoDTO;
import com.tp.model.mem.HhShopMemberInfo;
import com.tp.service.mem.IHhbgroupService;
import com.tp.util.HttpClientUtil;

/**
 * ClassName:HhbgroupAo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年11月18日 下午4:26:52 <br/>
 * 
 * @author zhouguofeng
 * @version
 * @since JDK 1.8
 * @see
 */
@Service
public class HhbgroupService implements IHhbgroupService {
	@Value("#{meta['hhb.userinfo.url']}")
	private String userinfourl ;

	@Value("#{meta['hhb.cost.money.url']}")
	private String costMoneyUrl ;
	
	@Value("#{meta['hhb.back.money.url']}")
	private String backMoneyUrl ;

	@Value("#{meta['hhb.userInfo.address.url']}")
	private String addressUrl ;
	@Value("#{meta['hhb.order.url']}")
	private String orderUrl;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * 
	 * @see com.tp.service.mem.IHhbgroupService#getHhShopMemberInfoInfo(java.lang.String)
	 */
	@Override
	public HhShopMemberInfo getHhShopMemberInfoInfo(String openId) {
		HhShopMemberInfo hhShopMemberInfo = new HhShopMemberInfo();
		if(openId==null){
			return hhShopMemberInfo;
		}
		String getUserinfourl = userinfourl + "?openId=" + openId;
		try {
			String reslut = HttpClientUtil.getData(getUserinfourl, "UTF-8");
			Gson gson = new Gson();
			hhShopMemberInfo = gson.fromJson(reslut, HhShopMemberInfo.class);
			logger.info("惠惠商城的用户信息----------------" + reslut);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return hhShopMemberInfo;
	}

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * 
	 * @see com.tp.service.mem.IHhbgroupService#costMoney(java.lang.String,
	 *      java.lang.Double)
	 */
	@Override
	@SuppressWarnings("unchecked")
//	/**
//	 * 
//	 * costMoney:(扣除惠币). <br/>
//	 * 
//	 * @author zhouguofeng
//	 * @param openId
//	 * @param costMoney
//	 * @return
//	 * @sinceJDK 1.8
//	 */
//	public boolean costMoney(String openId, Double costMoney) {
//		costMoneyUrl = costMoneyUrl + "?openId=" + openId + "&costMoney=" + costMoney;
//		try {
//			logger.info("调用惠惠商城接口开始传入参数---------------" + openId+"---:"+openId+"-----------costMoney---:"+costMoney);
//			String reslut = HttpClientUtil.getData(costMoneyUrl, "UTF-8");
//			Gson gson = new Gson();
//			HashMap<String, String> resultInfo = gson.fromJson(reslut, HashMap.class);
//			if ("true".endsWith(MapUtils.getString(resultInfo, "success"))) {// 调用成功
//				logger.info("调用惠惠商城接口成功----------------" + reslut);
//				return true;
//			} else {// 调用失败
//				logger.info("调用惠惠商城接口失败----------------" + reslut);
//				return false;
//			}
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//
//		}
//		return true;
//	}
   /**
    * 
    * getAddress:(根据openId获取用户信息). <br/>  
    *  
    * @author zhouguofeng  
    * @param openId
    * @return  
    * @sinceJDK 1.8
    */
	public HhShopMemberInfo getAddress(String openId) {
		HhShopMemberInfo hhShopMemberInfo = new HhShopMemberInfo();
		String getAddressUrl = addressUrl + "?openId=" + openId;
		try {
			String reslut = HttpClientUtil.getData(getAddressUrl, "UTF-8");
			Gson gson = new Gson();
			hhShopMemberInfo = gson.fromJson(reslut, HhShopMemberInfo.class);
			logger.info("惠惠商城的用户信息----------------" + reslut);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return hhShopMemberInfo;

	}
	/**
	 * 
	 * 退还积分 
	 * @see com.tp.service.mem.IHhbgroupService#backMoney(java.lang.String, java.lang.Double)
	 */
	public boolean backMoney(String openId, Double backMoney) {
		costMoneyUrl = costMoneyUrl + "?openId=" + openId + "&costMoney=" + backMoney;
		try {
			logger.info("调用惠惠商城接口开始传入参数---------------" + openId+"---:"+openId+"-----------costMoney---:"+backMoney);
			String reslut = HttpClientUtil.getData(costMoneyUrl, "UTF-8");
			Gson gson = new Gson();
			HashMap<String, String> resultInfo = gson.fromJson(reslut, HashMap.class);
			if ("true".endsWith(MapUtils.getString(resultInfo, "success"))) {// 调用成功
				logger.info("调用惠惠商城接口成功----------------" + reslut);
				return true;
			} else {// 调用失败
				logger.info("调用惠惠商城接口失败----------------" + reslut);
				return false;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return true;
	}
	public static void main(String args[]) {
		String openId = "o1_XewthXUggrRExkpyqdBzk1wtE";
		IHhbgroupService hhbgroupAo = new HhbgroupService();

		hhbgroupAo.getAddress(openId);
	}

	@Override
	public Map<String,String> sendOrder(HhbShopOrderInfoDTO orderInfo) {
		HashMap<String, String> resultInfo =new HashMap<String,String>();
		String openId=orderInfo.getOpenId();
		String sendOrderUrl="";
		sendOrderUrl = orderUrl + "?openId=" + openId + "&type="+orderInfo.getType()+"&code="+orderInfo.getCode()+"&totalMoney="+orderInfo.getTotalMoney()+"&balance="+orderInfo.getBalance()+"&cash="+orderInfo.getCash()+"&returnMoney="+orderInfo.getReturnMoney();
		try {
			logger.info("调用惠惠商城接口开始传入参数---------------" +sendOrderUrl);
			String reslut = HttpClientUtil.getData(sendOrderUrl, "UTF-8");
			Gson gson = new Gson();
			resultInfo = gson.fromJson(reslut, HashMap.class);
			if ("true".endsWith(MapUtils.getString(resultInfo, "success"))) {// 调用成功
				logger.info("调用惠惠商城接口成功----------------" + reslut);
			} else {// 调用失败
				logger.info("调用惠惠商城接口失败----------------" + reslut);
			}
			return resultInfo;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultInfo.put("success", "false");
			resultInfo.put("message", e.getMessage());
			return resultInfo;
		}
	}

	
}
