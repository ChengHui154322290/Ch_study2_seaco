package com.tp.proxy.mem;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.mem.SmsException;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.mem.ISendSmsService;
import com.tp.util.StringUtil;

@Service
public class SendSmsWrapProxy extends SendSmsProxy {
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
	private IPromoterInfoService promoterInfoService;


	/**
	 * 
	 * <pre>
	 * 发送短信
	 * </pre>
	 *
	 * @param mobile
	 * @param content
	 * @throws Exception
	 */
	public void sendSms(String mobile, String content,String ip,String channelCode) throws SmsException{
		String promoterName = promoterInfoService.queryShortNameByChannelCode(channelCode);
		if(StringUtils.isNotBlank(content) && StringUtil.isNotBlank(promoterName)){
			content = content.replaceFirst("西客商城", promoterName);
		}
		sendSmsService.sendSms(mobile, content,ip);
	}

	/**
	 * 
	 * <pre>
	 *  手机端发短信
	 * </pre>
	 *
	 * @param mobile
	 * @param type
	 * @throws SmsException
	 */
	public Integer sendSms4App(String mobile, Integer type,String ip,String channelCode) throws SmsException{
		return sendSmsService.sendSms4AppByChannelCode(mobile, type,ip,promoterInfoService.queryShortNameByChannelCode(channelCode));
	}
	
}
