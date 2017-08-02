package com.tp.world.ao.GroupCoupon;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.GroupCouponExchangeDto;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.util.DateUtil;
import com.tp.model.ord.OrderRedeemItem;
import com.tp.proxy.ord.OrderRedeemItemProxy;
import com.tp.util.Base64Util;

/**
 * 团购券
 * 
 */
@Service
public class GroupCouponAO {

	private static final Logger log = LoggerFactory.getLogger(GroupCouponAO.class);
	
	@Autowired
	private OrderRedeemItemProxy orderRedeemItemProxy;
	
	/**
	 * 1.获取兑换码对应的信息、订单信息,
	 * 2.验证:是否存在、是否已兑换、是否已过期、操作人是否有权限(是否在同一个店铺下、是否已禁用)
	 * 3.兑换:修改兑换信息、写日志
	 * @param groupCouponExchange
	 * @return
	 */
	public MResultVO<OrderRedeemItem> exchange(GroupCouponExchangeDto groupCouponExchange){
		try {
			ResultInfo<OrderRedeemItem> result = orderRedeemItemProxy.exchangeCode(groupCouponExchange);
			if(result.isSuccess())return new MResultVO<>(MResultInfo.EXCHANGE_SUCCESS,result.getData());
			return new MResultVO<>(result.getMsg().getMessage());
		}catch (MobileException e) {
			log.error("[API接口 - 兑换团购券  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		} catch (Exception e) {
			log.error("[API接口 - 兑换团购券 Exception]={}", e);
			return new MResultVO<>(MResultInfo.EXCHANGE_FAILED);
		}
	}
	
	public MResultVO<String> enCode(String code){
		try {
			String codeStr = new String(Base64Util.decrypt(code),"UTF-8");
			String[] codeArray = codeStr.split("\\|\\|");
			if(codeArray.length!=2){
				return new MResultVO<>("解析二维码时格式出错");
			}
			if(!codeArray[1].matches("\\d{14}")){
				return new MResultVO<>("二维码时间格式出错");
			}
			Date currentDate = DateUtil.getDate(codeArray[1], "yyyyMMddHHmmss");
			if(currentDate.after(new Date())){
				return new MResultVO<>("二维码时间校验失败");
			}
			currentDate = DateUtil.addMinute2Date(5, currentDate);
			if(currentDate.before(new Date())){
				return new MResultVO<>("二维码已过期");
			}
			return new MResultVO<>(MResultInfo.EXCHANGE_SUCCESS,codeArray[0]);
		} catch (UnsupportedEncodingException e) {
			return new MResultVO<>("解析二维码出错");
		}
	}
}
