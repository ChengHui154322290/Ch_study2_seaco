package com.tp.service.mem;

import java.util.Date;
import java.util.List;

import com.tp.common.util.mem.Sms;
import com.tp.common.util.mem.SmsException;

public interface ISendSmsService {

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
	void sendSms(String mobile, String content,String ip) throws SmsException;

	/**
	 * 
	 * <pre>
	 *  手机端发短信
	 * </pre>
	 *
	 * @param mobile
	 * @param type
	 * 
	 * @return 验证码
	 * @throws SmsException
	 */
	Integer sendSms4App(String mobile, Integer type,String ip) throws SmsException;

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
	void sendSmsForTime(String mobile, String content, Date sendTime,String ip)
			throws SmsException;

	/**
	 * 
	 * <pre>
	 * 批量发送短信
	 * </pre>
	 *
	 * @param smsList
	 */
	void batchSendSms(List<Sms> smsList);

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
	void sendSmsForUserSelf(String mobile, String content,String ip)
			throws SmsException;

	/**
	 * 第三方商城发送短信
	 * @param mobile
	 * @param type
	 * @param ip
	 * @param shortName
	 * @return
	 * @throws SmsException 
	 */
	Integer sendSms4AppByChannelCode(String mobile, Integer type, String ip, String shortName) throws SmsException;

	Integer sendSms4AppNew(String mobile, Integer type, String ip) throws SmsException;

	

}
