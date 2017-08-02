package com.tp.proxy.mem;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.mem.Sms;
import com.tp.common.util.mem.SmsException;
import com.tp.service.mem.ISendSmsService;

/**
 * 用户收货地址表代理层
 * 
 * @author szy
 *
 */
@Service
public class SendSmsProxy{

	@Autowired
	private ISendSmsService sendSmsService;


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
	public void sendSms(String mobile, String content,String ip) throws SmsException{
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
	public Integer sendSms4App(String mobile, Integer type,String ip) throws SmsException{
		return sendSmsService.sendSms4App(mobile, type,ip);
	}
	public Integer sendSms4AppNew(String mobile, Integer type,String ip) throws SmsException{
		return sendSmsService.sendSms4AppNew(mobile, type,ip);
	}

	/**
	 * 
	 * <pre>
	 *  定时发送短信
	 * </pre>
	 *
	 * @param mobile
	 * @param content
	 * @param sendTime
	 * @throws SmsException
	 */
	public void sendSmsForTime(String mobile, String content, Date sendTime,String ip) throws SmsException{
		sendSmsService.sendSmsForTime(mobile, content, sendTime,ip);
	}

	/**
	 * 
	 * <pre>
	 * 批量发送短信
	 * </pre>
	 *
	 * @param smsList
	 */
	public void batchSendSms(List<Sms> smsList){
		sendSmsService.batchSendSms(smsList);
	}

	/**
	 * 
	 * <pre>
	 *  用户模块专用的短信发送接口
	 * </pre>
	 *
	 * @param mobile
	 * @param content
	 * @throws SmsException
	 */
	public void sendSmsForUserSelf(String mobile, String content,String ip) throws SmsException{
		sendSmsService.sendSmsForUserSelf(mobile, content,ip);
	}
	
}
