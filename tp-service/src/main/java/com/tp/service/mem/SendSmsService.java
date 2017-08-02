package com.tp.service.mem;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.common.util.mem.EmaySms;
import com.tp.common.util.mem.ISms;
import com.tp.common.util.mem.Sms;
import com.tp.common.util.mem.SmsException;
import com.tp.common.vo.mem.MemberInfoConstant;
import com.tp.common.vo.mem.PassPortErrorCode;
import com.tp.common.vo.mem.SessionKey;
import com.tp.common.vo.mem.SmsUtil;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.bse.SmsForbiddenWordsService;
import com.tp.service.mem.ISendSmsService;
import com.tp.service.mem.ISmsStatisticsService;
import com.tp.service.mem.ISmsWhiteInfoService;
import com.tp.util.StringUtil;

/**
 * 
 * 发生送短信
 *
 * @author szy
 */
@Service(value = "sendSmsService")
public class SendSmsService implements ISendSmsService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//@Autowired
	private SmsForbiddenWordsService smsForbiddenWordsService;
	
	@Autowired
	private List<ISms> smsList;
	
	@Autowired
	private EmaySms emaySms;
	
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	@Value("#{settings['sendsms.sendLimitByMobile']}")
	private Integer sendLimitByMobile;
	@Value("#{settings['sendsms.sendLimitByIp']}")
	private Integer sendLimitByIp;
	@Value("#{settings['sendsms.switch']}")
	private Boolean smswitch;
	
	@Autowired
	private ISmsWhiteInfoService smsWhiteInfoService;
	
	@Autowired
	private ISmsStatisticsService smsStatisticsService;
	
	@Override
	public void sendSmsForUserSelf(String mobile,String content,String ip) throws SmsException{
		logger.info("开始发送短信");
		String redisKey = getRedisKey(MemberInfoConstant.RedisKey.SEND_SMS_STATE,mobile);
		logger.info("校验手机号码"+mobile+"是否在一分钟内发送过短信");
		checkMobile(redisKey,mobile,content,ip);
		logger.info("允许手机号码"+mobile+"发送短信");
		ISms sms = buildSms(mobile,ip);
		try {
			if(!StringUtil.isNullOrEmpty(content)&&!content.matches("^【.{2,8}】.*$")) content = "【西客商城】" + content;
			logger.info("短信开始发送");
			if(smswitch){
				sms.sendSms(mobile,content.toString());
			}else{
				logger.info("短信开关关闭,发送内容:{}",content);
			}
			logger.info("短信发送结束");
		} catch (Exception e) {
			logger.error("发送短信异常:"+e.getMessage(), e);
			throw new SmsException(PassPortErrorCode.SEND_SMS_FAIL.value);
		}
		jedisCacheUtil.setCache(redisKey, MemberInfoConstant.IsSuccess.Success, MemberInfoConstant.SMS_CODE_RETRY_TIME);
	}
	
	
	private void checkMobile(String redisKey,String mobile,String content,String ip) throws SmsException{
		Object o = jedisCacheUtil.getCache(redisKey);
		if(null != o) {
			Long lastTime = jedisCacheUtil.getKeyExpire(redisKey);
			if(lastTime > 0) throw new SmsException(PassPortErrorCode.SEND_MSG_UN_ONE_SECOND.code,lastTime+PassPortErrorCode.SEND_MSG_UN_ONE_SECOND.value);
		}
		sendValidate(mobile,content,ip);
	}
	private void checkMobileNew(String redisKey,String mobile,String content,String ip) throws SmsException{
		Object o = jedisCacheUtil.getCache(redisKey);
		if(null != o) {
			Long lastTime = jedisCacheUtil.getKeyExpire(redisKey);
			if(lastTime > 0) throw new SmsException(PassPortErrorCode.SEND_MSG_UN_ONE_SECOND.code,lastTime+PassPortErrorCode.SEND_MSG_UN_ONE_SECOND.value);
		}
		sendValidateNew(mobile,content,ip);
	}
	
	private ISms buildSms(String mobile,String ip){
		logger.info("开始初始化短信平台");
		ISms sms = emaySms;
		/*Integer count = (Integer) userCache.getCache(getRedisKey(MemberInfoConstant.RedisKey.SEND_SMS_COUNT,mobile));
		if(null == count) count = 0;
		if(null == smsList || smsList.isEmpty()){
			switch (count%2) {
				case 0:
					sms = emaySms;
					break;
				case 1:
					sms = emaySms;
					break;
				default:
					sms = emaySms;
					break;
			}
			
		}else{
			sms = smsList.get(count%smsList.size());
			if(null == sms) sms = srSms;
		}
		
		count++;
		userCache.setCache(getRedisKey(MemberInfoConstant.RedisKey.SEND_SMS_COUNT,mobile), count);
		logger.info("初始化短信平台成功:"+sms.getClass().toString());*/
		return sms;
	}
	
	private boolean isMobile(String mobile) {
		Pattern p = Pattern.compile("1[0-9]{10}$");
		Matcher m = p.matcher(mobile);
		return m.find();
	}
	
	@Override
	public void sendSms(String mobile,String content,String ip) throws SmsException{
		logger.info("开始发送短信");
		if(!isMobile(mobile)) {
			logger.warn("{} is not a mobiel ");
		}
		
		if(!StringUtil.isNullOrEmpty(content)){
			logger.info("--------------敏感词过滤 begin---------------");
			logger.info("原始内容:"+content);
//			content = smsForbiddenWordsService.haveSmsForbiddenWordsButNotFixed(content);
			logger.info("过滤后内容:"+content);
			logger.info("--------------敏感词过滤 end---------------");
		}
		String redisKey = getRedisKey(MemberInfoConstant.RedisKey.SEND_SMS_STATE,mobile);
		ISms sms = buildSms(mobile,ip);
		try {
			if(!StringUtil.isNullOrEmpty(content)&&!content.matches("^【.{2,8}】.*$")) content = "【西客商城】" + content;
			logger.info("短信开始发送");
			if(smswitch){
				sms.sendSms(mobile,content.toString());
			}else{
				logger.info("短信开关关闭,发送内容:{}",content);
			}
			logger.info("短信发送结束");
		} catch (Exception e) {
			logger.error("发送短信异常:"+e.getMessage(), e);
			throw new SmsException(PassPortErrorCode.SEND_SMS_FAIL.value);
		}
		jedisCacheUtil.setCache(redisKey, MemberInfoConstant.IsSuccess.Success, MemberInfoConstant.SMS_CODE_RETRY_TIME);
	}
	
	@Override
	public Integer sendSms4App(String mobile,Integer type,String ip) throws SmsException{
		return sendSms4AppByChannelCode(mobile,type,ip,null);
	}
	@Override
	public Integer sendSms4AppNew(String mobile,Integer type,String ip) throws SmsException{
		return sendSms4AppByChannelCodeNew(mobile,type,ip,null);
	}
	
	@Override
	public Integer sendSms4AppByChannelCode(String mobile,Integer type,String ip,String shortName) throws SmsException{
		//检查白名单
		if (!smsWhiteInfoService.checkSendSms(mobile)) {
			logger.error("检查白名单不通过：{}", mobile);
			return null;
		}
		logger.info("开始发送短信");
		if(StringUtil.isNullOrEmpty(mobile)) throw new SmsException(PassPortErrorCode.SMS_MOBILE_IS_NULL.code,PassPortErrorCode.SMS_MOBILE_IS_NULL.value);
		
		if(StringUtil.isNull(type)) throw new SmsException(PassPortErrorCode.SMS_MOBILE_TYPE_IS_NULL.code,PassPortErrorCode.SMS_MOBILE_TYPE_IS_NULL.value);
		String redisKey = getRedisKey(MemberInfoConstant.RedisKey.SEND_SMS_STATE,mobile);
		logger.info("校验手机号码"+mobile+"是否在一分钟内发送过短信");
		checkMobile(redisKey,mobile,"",ip);
		logger.info("允许手机号码"+mobile+"发送短信");
		ISms sms = buildSms(mobile,ip);
		Integer random;
		try {
			random = SmsUtil.getRandomNumber();
			
			StringBuffer smsCodeKey = new StringBuffer();
			StringBuffer content = new StringBuffer();
			if(type == SessionKey.APP_REGISTER.key){
				smsCodeKey.append(mobile).append(":").append(SessionKey.APP_REGISTER.value);
				content.append(SmsUtil.getRegSmsContent(random));
				logger.info(mobile+"["+SessionKey.APP_REGISTER.value+"]smsCode:"+random);
			}else if(type == SessionKey.APP_UPDATE_PASSWORD.key){
				smsCodeKey.append(mobile).append(":").append(SessionKey.APP_UPDATE_PASSWORD.value);
				content.append(SmsUtil.getResetPwdSmsContent(random));
				logger.info(mobile+"["+SessionKey.APP_UPDATE_PASSWORD.value+"]smsCode:"+random);
			}else if(type == SessionKey.APP_BINDPHONE.key){
				smsCodeKey.append(mobile).append(":").append(SessionKey.APP_BINDPHONE.value);
				content.append(SmsUtil.getBindMobileSmsContent(random));
				logger.info(mobile+"["+SessionKey.APP_BINDPHONE.value+"]smsCode:"+random);
			}else if(type == SessionKey.RECEIVE_COUPON.key){
				smsCodeKey.append(mobile).append(":").append(SessionKey.RECEIVE_COUPON.value);
				content.append(SmsUtil.getReceiveCouponSmsContent(random));
				logger.info(mobile+"["+SessionKey.RECEIVE_COUPON.value+"]smsCode:"+random);
			}else if(type == SessionKey.REGISTER_DSS.key){
				smsCodeKey.append(mobile).append(":").append(SessionKey.REGISTER_DSS.value);
				content.append(SmsUtil.getRegDSSSmsContent(random));
				logger.info(mobile+"["+SessionKey.REGISTER_DSS.value+"]smsCode:"+random);
			}else if(type == SessionKey.UNION_BINDMOBILE.key){
				smsCodeKey.append(mobile).append(":").append(SessionKey.UNION_BINDMOBILE.value);
				content.append(SmsUtil.getUnionBindmblContent(random));
				logger.info(mobile+"["+SessionKey.UNION_BINDMOBILE.value+"]smsCode:"+random);
			}else if(type == SessionKey.MODIFY_MOBILE.key){
				smsCodeKey.append(mobile).append(":").append(SessionKey.MODIFY_MOBILE.value);
				content.append(SmsUtil.getUnionBindmblContent(random));
				logger.info(mobile+"["+SessionKey.MODIFY_MOBILE.value+"]smsCode:"+random);
			}
			if(StringUtil.isNotBlank(shortName) && StringUtil.isNotBlank(content)){
				content = content.replace(1, 5, shortName);	
			}
			logger.info("短信开始发送");
			logger.info("smsCodeKey:"+smsCodeKey);
			if(smswitch){
				sms.sendSms(mobile,content.toString());
			}else{
				logger.info("短信开关关闭,发送内容:{}",content);
			}
			jedisCacheUtil.setCache(smsCodeKey.toString(), random, MemberInfoConstant.SMS_CODE_EXPIRE_TIME);
			logger.info("短信发送结束");
			smsStatisticsService.statisticsSmsSend(mobile, true);
		} catch (Exception e) {
			logger.error("发送短信异常:"+e.getMessage(), e);
			smsStatisticsService.statisticsSmsSend(mobile, false);
			throw new SmsException(e.getMessage());
		}
		jedisCacheUtil.setCache(redisKey, MemberInfoConstant.IsSuccess.Success, MemberInfoConstant.SMS_CODE_RETRY_TIME);
		return random;
	}
	public Integer sendSms4AppByChannelCodeNew(String mobile,Integer type,String ip,String shortName) throws SmsException{
		//检查白名单
		if (!smsWhiteInfoService.checkSendSms(mobile)) {
			logger.error("检查白名单不通过：{}", mobile);
			return null;
		}
		logger.info("开始发送短信");
		if(StringUtil.isNullOrEmpty(mobile)) throw new SmsException(PassPortErrorCode.SMS_MOBILE_IS_NULL.code,PassPortErrorCode.SMS_MOBILE_IS_NULL.value);
		
		if(StringUtil.isNull(type)) throw new SmsException(PassPortErrorCode.SMS_MOBILE_TYPE_IS_NULL.code,PassPortErrorCode.SMS_MOBILE_TYPE_IS_NULL.value);
		String redisKey = getRedisKey(MemberInfoConstant.RedisKey.SEND_SMS_STATE,mobile);
		logger.info("校验手机号码"+mobile+"是否在一分钟内发送过短信");
		checkMobileNew(redisKey,mobile,"",ip);
		logger.info("允许手机号码"+mobile+"发送短信");
		ISms sms = buildSms(mobile,ip);
		Integer random;
		try {
			random = SmsUtil.getRandomNumber();
			
			StringBuffer smsCodeKey = new StringBuffer();
			StringBuffer content = new StringBuffer();
			if(type == SessionKey.APP_REGISTER.key){
				smsCodeKey.append(mobile).append(":").append(SessionKey.APP_REGISTER.value);
				content.append(SmsUtil.getRegSmsContent(random));
				logger.info(mobile+"["+SessionKey.APP_REGISTER.value+"]smsCode:"+random);
			}else if(type == SessionKey.APP_UPDATE_PASSWORD.key){
				smsCodeKey.append(mobile).append(":").append(SessionKey.APP_UPDATE_PASSWORD.value);
				content.append(SmsUtil.getResetPwdSmsContent(random));
				logger.info(mobile+"["+SessionKey.APP_UPDATE_PASSWORD.value+"]smsCode:"+random);
			}else if(type == SessionKey.APP_BINDPHONE.key){
				smsCodeKey.append(mobile).append(":").append(SessionKey.APP_BINDPHONE.value);
				content.append(SmsUtil.getBindMobileSmsContent(random));
				logger.info(mobile+"["+SessionKey.APP_BINDPHONE.value+"]smsCode:"+random);
			}else if(type == SessionKey.RECEIVE_COUPON.key){
				smsCodeKey.append(mobile).append(":").append(SessionKey.RECEIVE_COUPON.value);
				content.append(SmsUtil.getReceiveCouponSmsContent(random));
				logger.info(mobile+"["+SessionKey.RECEIVE_COUPON.value+"]smsCode:"+random);
			}else if(type == SessionKey.REGISTER_DSS.key){
				smsCodeKey.append(mobile).append(":").append(SessionKey.REGISTER_DSS.value);
				content.append(SmsUtil.getRegDSSSmsContent(random));
				logger.info(mobile+"["+SessionKey.REGISTER_DSS.value+"]smsCode:"+random);
			}else if(type == SessionKey.UNION_BINDMOBILE.key){
				smsCodeKey.append(mobile).append(":").append(SessionKey.UNION_BINDMOBILE.value);
				content.append(SmsUtil.getUnionBindmblContent(random));
				logger.info(mobile+"["+SessionKey.UNION_BINDMOBILE.value+"]smsCode:"+random);
			}else if(type == SessionKey.MODIFY_MOBILE.key){
				smsCodeKey.append(mobile).append(":").append(SessionKey.MODIFY_MOBILE.value);
				content.append(SmsUtil.getUnionBindmblContent(random));
				logger.info(mobile+"["+SessionKey.MODIFY_MOBILE.value+"]smsCode:"+random);
			}
			if(StringUtil.isNotBlank(shortName) && StringUtil.isNotBlank(content)){
				content = content.replace(1, 5, shortName);	
			}
			logger.info("短信开始发送");
			logger.info("smsCodeKey:"+smsCodeKey);
			if(smswitch){
				sms.sendSms(mobile,content.toString());
			}else{
				logger.info("短信开关关闭,发送内容:{}",content);
			}
			jedisCacheUtil.setCache(smsCodeKey.toString(), random, MemberInfoConstant.SMS_CODE_EXPIRE_TIME);
			logger.info("短信发送结束");
			smsStatisticsService.statisticsSmsSend(mobile, true);
		} catch (Exception e) {
			logger.error("发送短信异常:"+e.getMessage(), e);
			smsStatisticsService.statisticsSmsSend(mobile, false);
			throw new SmsException(e.getMessage());
		}
		jedisCacheUtil.setCache(redisKey, MemberInfoConstant.IsSuccess.Success, MemberInfoConstant.SMS_CODE_RETRY_TIME);
		return random;
	}

	@Override
	public void sendSmsForTime(String mobile,String content,Date sendTime,String ip) throws SmsException{
		logger.info("定时发送短信---------------------------------------->");
		if(!StringUtil.isNullOrEmpty(content)){
			logger.info("--------------敏感词过滤 begin---------------");
			logger.info("原始内容:"+content);
//			content = smsForbiddenWordsService.haveSmsForbiddenWordsButNotFixed(content);
			logger.info("过滤后内容:"+content);
			logger.info("--------------敏感词过滤 end---------------");
		}
		ISms sms = buildSms(mobile,ip);
		try {
			if(!StringUtil.isNullOrEmpty(content)&&!content.matches("^【.{2,8}】.*$")) content = "【西客商城】" + content;
			logger.info("定时短信开始发送");
			sms.sendSmsForTime(mobile,content,sendTime);
			logger.info("定时短信发送结束");
		} catch (Exception e) {
			logger.error("定时发送短信失败:"+e.getMessage());
			throw new SmsException(PassPortErrorCode.SEND_SMS_FAIL.value);
		}
		logger.info("定时发送短信结束---------------------------------------->");
	}
	
	@Override
	public void batchSendSms(List<Sms> smsList){
		if(null == smsList || smsList.isEmpty()) return;
		logger.info("批量发送短信---------------------------------------->");
		for (Sms sms : smsList) {
			try {
				String content=sms.getContent();
				if(!StringUtil.isNullOrEmpty(content)){
					logger.info("--------------敏感词过滤 begin---------------");
					logger.info("原始内容:"+content);
//					content = smsForbiddenWordsService.haveSmsForbiddenWordsButNotFixed(content);
					logger.info("过滤后内容:"+content);
					logger.info("--------------敏感词过滤 end---------------");
				}
				
				this.sendSms(sms.getMobile(),content,sms.getIp());
			} catch (Exception e) {
				logger.error("为手机号码:"+sms.getMobile()+"发送短信失败.原因:"+e.getMessage());
				continue;
			}
		}
		logger.info("批量发送短信---------------------------------------->");
	}
	
	
	private String getRedisKey(String topic,String mobile){
		return topic+":"+mobile;
	}


	public void setSmsList(List<ISms> smsList) {
		this.smsList = smsList;
	}

	public Boolean sendValidate(String mobile,String content,String ip) throws SmsException{
		Boolean validateSign = sendTimeLimitByMobile(mobile);
		if(validateSign){
			validateSign = sendTimeLimitByIp(ip);
		}
		return validateSign;
	}
	
	public Boolean sendTimeLimitByMobile(String mobile) throws SmsException{
		if(null==sendLimitByMobile){
			return Boolean.TRUE;
		}
		if(jedisCacheUtil.keyExists("sendsms:limit:"+mobile)){
			logger.info("【{}】手机发送时间限制：{}秒,",mobile,sendLimitByMobile);
			 throw new SmsException(PassPortErrorCode.SEND_MSG_UN_ONE_SECOND.code,sendLimitByMobile+PassPortErrorCode.SEND_MSG_UN_ONE_SECOND.value);
		}
		jedisCacheUtil.setCache("sendsms:limit:"+mobile, mobile, sendLimitByMobile);
		return Boolean.TRUE;
	}
	public Boolean sendTimeLimitByIp(String ip) throws SmsException{
		if(null==sendLimitByIp){
			return Boolean.TRUE;
		}
		if(StringUtil.isNotBlank(ip) && jedisCacheUtil.keyExists("sendsms:limit:"+ip)){
			logger.info("【{}】IP发送时间限制：{}秒,",ip,sendLimitByIp);
			 throw new SmsException(PassPortErrorCode.SEND_MSG_UN_ONE_SECOND.code,sendLimitByIp+PassPortErrorCode.SEND_MSG_UN_ONE_SECOND.value);
		}
		jedisCacheUtil.setCache("sendsms:limit:"+ip, ip, sendLimitByIp);
		return Boolean.TRUE;
	}
	/** 判断是否同一个电话号码和同一个IP地址重复获取短信验证码 */
	public Boolean sendValidateNew(String mobile,String content,String ip) throws SmsException{
		Boolean validateSign = sendTimeLimitByMobile(mobile);
		if(validateSign){
			validateSign = sendTimeLimitByIpNew(ip);
		}
		return validateSign;
	}
	public Boolean sendTimeLimitByIpNew(String ip) throws SmsException{
		if(null==sendLimitByIp){
			return Boolean.TRUE;
		}
		if(StringUtil.isNotBlank(ip) && jedisCacheUtil.keyExists("changeTelsendsms:limit:"+ip)){
			logger.info("【{}】IP发送时间限制：{}秒,",ip,sendLimitByIp);
			 throw new SmsException(PassPortErrorCode.SEND_MSG_UN_ONE_SECOND.code,sendLimitByIp+PassPortErrorCode.SEND_MSG_UN_ONE_SECOND.value);
		}
		jedisCacheUtil.setCache("changeTelsendsms:limit:"+ip, ip, sendLimitByIp);
		return Boolean.TRUE;
	}
}
