package com.tp.world.ao.payment;

import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.pay.cbdata.CallbackResultDto;
import com.tp.proxy.pay.PayServiceProxy;


/**
 * 支付回调
 * @author zhuss
 * @2016年1月13日 下午5:58:03
 */
@Service
public class PayCallbackAO {

	private Logger log = LoggerFactory.getLogger(PayCallbackAO.class);
	
	@Autowired
	private PayServiceProxy payServiceProxy;
	
	public CallbackResultDto callback(String gateway,
			Map<String, String> parameterMap, boolean isAsyn) {
		try {
			return payServiceProxy.callback(gateway, parameterMap,isAsyn);
		} catch (Exception e) {
			log.error("[API接口 - 支付回调 Exception] = {}",e);
			return null;
		}
	}
}
