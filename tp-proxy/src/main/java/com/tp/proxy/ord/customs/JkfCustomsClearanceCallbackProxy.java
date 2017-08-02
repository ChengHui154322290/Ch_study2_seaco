/**
 * 
 */
package com.tp.proxy.ord.customs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.JKF.JkfBaseDO;
import com.tp.model.ord.JKF.JkfCallbackResponse;
import com.tp.service.ord.customs.JKF.callback.IJkfClearanceCallbackService;

/**
 * @author Administrator
 * 报关接口回执代理类
 */
@Service
public class JkfCustomsClearanceCallbackProxy {
	
	private static final Logger logger = LoggerFactory.getLogger(JkfCustomsClearanceCallbackProxy.class);
	
	@Autowired
	private IJkfClearanceCallbackService jkfClearanceCallbackService;
	
	/** 清关回执 */
	public ResultInfo<JkfCallbackResponse> clearanceCallback(JkfBaseDO receiptResult){
		try{
			return new ResultInfo<>(jkfClearanceCallbackService.asyncClearanceCallback(receiptResult));
		}catch(Throwable e){
			FailInfo failInfo = ExceptionUtils.println(new FailInfo(e), logger, receiptResult);
			return new ResultInfo<>(failInfo);
		}
	}
	
}
