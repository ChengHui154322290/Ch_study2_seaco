package com.tp.common.util.mem;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.common.util.mem.emay.EmayClient;
import com.tp.common.vo.mem.PassPortErrorCode;
import com.tp.util.DateUtil;

public class EmaySms extends ISms {

	
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = -1602254407505500862L;
	
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void sendSms(String mobile,String content) throws SmsException {
		try {
			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>【忆美短信】开始发送短信:"+mobile);
			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>【忆美短信】短信内容:"+content);
			
			Long smsId = Calendar.getInstance().getTimeInMillis();
			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>【忆美短信】短信ID:"+smsId);
			
			String [] mobiles = null;
			if(mobile.contains(",")) mobiles = mobile.split(",");
			else mobiles = new String[]{mobile};
			EmayClient sdkclient = new EmayClient(sendUrl, name, password, smsPriority);
			for(String phoneNo : mobiles) {
				int state = sdkclient.sendSms(phoneNo, content);
				logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>【忆美短信】发送短信结束");
				logger.info("忆美短信:"+EmayErrorCode.getValue(state));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			throw new SmsException(e.getMessage());
		}
	}

	@Override
	public void sendSmsForTime(String mobile, String content, Date sendTime) throws SmsException {
		try {
			if(null == sendTime) throw new SmsException(PassPortErrorCode.SEND_SMS_SEND_TIME_IS_NULL.value);
			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>【忆美短信】【"+sendTime+"】开始发送定时短信短信:"+mobile);
			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>【忆美短信】短信内容:"+content);
			
			String [] mobiles = null;
			if(mobile.contains(",")) mobiles = mobile.split(",");
			else mobiles = new String[]{mobile};
			EmayClient sdkclient = new EmayClient(sendUrl,name, password, smsPriority);
			for(String phoneNo : mobiles) {
				int state =  sdkclient.sendScheduledSMS(phoneNo, content, DateUtil.formatDate(sendTime, "yyyyMMddHHmmss"));
				logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>【忆美短信】发送定时短信结束");
				logger.info("忆美短信:"+EmayErrorCode.getValue(state));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new SmsException(e.getMessage());
		}
	};
	
	@Override
	public int sendMultixSms(StringBuffer strPtMsgId, String strUserId,
			String strPwd, List<MULTIX_XG> MultixXg) {
		return 0;
	}

	@Override
	public List<MO_PACK> getMo(String strUserId, String strPwd) {
		return null;
	}

	@Override
	public List<RPT_PACK> getRpt(String strUserId, String strPwd) {
		return null;
	}

	
	public enum EmayErrorCode {
		
		/** 发送信息失败（短信内容长度越界）*/
		SMS_OTHER(-999, "系统异常"),
		
		/** 发送信息失败（短信内容长度越界）*/
		SMS_CONTENT_SO_LONG(-1, "发送信息失败（短信内容长度越界）"),
		
		/** 短信发送成功*/
		SMS_SUCCESS(0, "短信发送成功"),
		
		/** 发送信息失败（未激活序列号或序列号和KEY值不对，或账户没有余额等）.*/
		SMS_FAIL_UN_KEY(17, "发送信息失败（未激活序列号或序列号和KEY值不对，或账户没有余额等）"),
		
		/** 发送定时信息失败，一般用户是定时格式不规范所致.*/
		SMS_FAIL_SEND_TIME_ERROR(18, "发送定时信息失败，一般用户是定时格式不规范所致"),
		
		/** 服务器端返回错误，错误的返回值（返回值不是数字字符串）.*/
		SMS_SERVER_RETURN_VALUE_ERROR(305, "服务器端返回错误，错误的返回值（返回值不是数字字符串）"),
		
		/** 目标电话号码不符合规则，电话号码必须是以0、1开头.*/
		SMS_MOBILE_ERROR(307, "目标电话号码不符合规则，电话号码必须是以0、1开头"),
		
		/** 发送消息时短信序列号错误 即smsid错误.*/
		SMS_SMSID_ERROR(996, "发送消息时短信序列号错误 即smsid错误"),
		
		/** 平台返回找不到超时的短信，该信息是否成功无法确定.*/
		SMS_SEND_STATE_UN_SURE(997, "平台返回找不到超时的短信，该信息是否成功无法确定"),
		
		/** 由于客户端网络问题导致信息发送超时，该信息是否成功下发无法确定 */
		SMS_SEND_STATE_UN_SURE_FOR_CLIENT(303, "由于客户端网络问题导致信息发送超时，该信息是否成功下发无法确定"),
		
		/** 客户端网络故障.*/
		SMS_CLIENT_ERROR(101, "客户端网络故障");
		
		public Integer code;
		
		public String value;
		
		private EmayErrorCode(Integer code, String value) {
			this.code = code;
			this.value = value;
		}
		
		public static String getValue(Integer code){
			for (EmayErrorCode c : EmayErrorCode.values()) {
	            if (c.code.intValue() == code.intValue()) {
	                return c.value;
	            }
	        }
			return SMS_OTHER.value;
		}
	}
	
	public static void main(String[] args) throws Exception {
//		Client sdkclient=new Client("9SDK-EMY-0999-JETRR","497750"); 
//		sdkclient.registEx("497750");
	}
}
