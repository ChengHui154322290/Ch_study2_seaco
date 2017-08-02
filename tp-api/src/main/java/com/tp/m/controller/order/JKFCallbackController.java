package com.tp.m.controller.order;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.customs.JKFConstant.JKFResultError;
import com.tp.m.ao.order.JKFCallbackAO;
import com.tp.m.helper.JKFHelper.XmlUtil;
import com.tp.model.ord.JKF.JkfCallbackResponse;

@Controller
@RequestMapping(value = "/jkf")
public class JKFCallbackController {

	private static final Logger logger = LoggerFactory.getLogger(JKFCallbackController.class);
	
	@Autowired
	private JKFCallbackAO jkfCallbackAO;
	
	//订单回执
	@RequestMapping("/pac_message_receiver")
	@ResponseBody
	public String callbackOfJKFOrder(HttpServletRequest request, String content, String msg_type, String data_digest){	
		try {	
			logger.error("[JKF_CALLBACK][content={}][msg_type={}][data_digest={}]回执数据", content, msg_type, data_digest);
			assertNull(content, JKFResultError.INVALID_REQUEST_PARAM);
			assertNull(msg_type, JKFResultError.INVALID_REQUEST_PARAM);
			if (!jkfCallbackAO.isTest()) {
				assertNull(data_digest, JKFResultError.INVALID_REQUEST_PARAM);	
			}
			jkfCallbackAO.clearanceCallback(content, data_digest, StringUtils.upperCase(msg_type), 1);
		} catch (JkfException e) {
			logger.error("[JKF_CALLBACK]回执处理异常", e);
		} catch (Exception e){
			logger.error("[JKF_CALLBACK]回执处理异常", e);
		}
		return XmlUtil.beanToXML(new JkfCallbackResponse());
	}
	
	//清单报关回执
	@RequestMapping("/goods_receiver")
	@ResponseBody
	public String callbackOfJKFGoods(HttpServletRequest request, String content, String msg_type, String data_digest){	
		try {	
			logger.error("[JKF_CALLBACK][content={}][msg_type={}][data_digest={}]回执数据", content, msg_type, data_digest);
			assertNull(content, JKFResultError.INVALID_REQUEST_PARAM);
			assertNull(msg_type, JKFResultError.INVALID_REQUEST_PARAM);
			if (!jkfCallbackAO.isTest()) {
				assertNull(data_digest, JKFResultError.INVALID_REQUEST_PARAM);	
			}
			jkfCallbackAO.clearanceCallback(content, data_digest, StringUtils.upperCase(msg_type), 2);
		} catch (JkfException e) {
			logger.error("[JKF_CALLBACK]回执处理异常", e);
		} catch (Exception e){
			logger.error("[JKF_CALLBACK]回执处理异常", e);
		}
		return XmlUtil.beanToXML(new JkfCallbackResponse());
	}
	
	private void assertNull(String value, JKFResultError error){
		if (StringUtils.isEmpty(value)) {
			throw new JkfException(error);
		}
	}

	/**
	 * 电子口岸对接自定义异常 
	 */
	public static class JkfException extends RuntimeException{
		private static final long serialVersionUID = 4652735540968935889L;
		private String errorCode;
		
		public JkfException(){
			super();
		}
		
		public JkfException(JKFResultError error){
			this(error.code, error.name);
		}
		
		public JkfException(String errorCode, String errorMsg){
			super(errorMsg);
			this.errorCode = errorCode;
		}

		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}		
	}
}


