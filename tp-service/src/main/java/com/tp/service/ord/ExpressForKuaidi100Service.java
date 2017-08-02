/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.service.ord;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.ord.OrderErrorCodes;
import com.tp.dto.ord.kuaidi100.PushExpressInfoRequest;
import com.tp.dto.ord.kuaidi100.SubscribeResult;
import com.tp.service.ord.IExpressForKuaidi100Service;
import com.tp.util.HttpClientUtil;

/**
 * <pre>
 * 快递信息处理 - 对接快递100实现类
 * </pre>
 * 
 * @author szy
 * @time 2015-2-2 上午10:26:12
 */
@Service
public class ExpressForKuaidi100Service implements IExpressForKuaidi100Service {
	private static final Logger logger = LoggerFactory.getLogger(ExpressForKuaidi100Service.class);
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final String DEFAULT_DATA_FORMAT = "json";
	private static final String DEFAULT_POST_URL = "http://www.kuaidi100.com/poll";

	@Value("#{meta['kuaidi100.poll.domain']}")
	public String kuaidi100PollUrl;

	@Override
	public SubscribeResult pushExpressInfoToKuaidi100(PushExpressInfoRequest pushReq) {
		SubscribeResult pushRet = null;
		String reqJsonStr = JSONObject.toJSONString(pushReq);
		HashMap<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("schema", DEFAULT_DATA_FORMAT);
		requestMap.put("param", reqJsonStr);
		try {
			kuaidi100PollUrl = StringUtils.isBlank(kuaidi100PollUrl) ? DEFAULT_POST_URL : kuaidi100PollUrl;
			
			String ret = HttpClientUtil.postData(kuaidi100PollUrl, requestMap, DEFAULT_CHARSET);
			pushRet = JSONObject.parseObject(ret, SubscribeResult.class);
			if (pushRet == null) {
				pushRet = new SubscribeResult(false, String.valueOf(OrderErrorCodes.SYSTEM_ERROR), "推送返回结果为空");
			}
			return pushRet;
		} catch (Exception e) {
			logger.error("HTTP请求快递100接口异常", e);
			pushRet = new SubscribeResult(false, String.valueOf(OrderErrorCodes.SYSTEM_ERROR), "调用HTTP请求快递100接口异常");
		}
		return pushRet;
	}
}
