package com.tp.common.util.mem;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract  class ISms implements Serializable{
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = -1322287062577810372L;


	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	

	/* 企业id */
	protected String companyId;
	
	/* 短信平台 用户账号 */
	protected String name;

	/* web平台：基本资料中的接口密码 */
	protected String password;

	/* 短信平台发送短信接口URL */
	protected String sendUrl;

	/* 发送短信类型 固定值 pt */
	protected String type;

	/* 用户签名 */
	protected String sign;

	/* 扩展码，用户定义扩展码，只能为数字 */
	protected String extNo;

	/* 目标用户手机号码 */
	protected String mobile;

	/* 目标用户手机号码组 */
	protected String[] mobiles;

	/* 短信内容 */
	protected String content;

	/* 发送时间（格式为yyyy-mm-dd hi24:mi:ss） */
	protected String sentTime;

	/* 短信内容的编码方式 只能填GBK */
	protected String srcCharset;

	/*
	 * 短信ID，自定义唯一的消息ID，数字位数最大19位，与状态报告ID一一对应，需用户自定义ID规则确保ID的唯一性。
	 * 如果smsID为0将获取不到相应的状态报告信息
	 */
	protected Long smsID;
	
	/* 一次发送短信最大数量 */
	protected Long mobileCount = 100L;

	/* 优先级(级别从1到5的正整数，数字越大优先级越高，越先被发送) */
	protected Integer smsPriority;
	
	/** 营销内容短信账号 **/
	protected String marketName;
	/** 营销内容短信密码  **/
	protected String marketPassword;
	
	public String getCompanyId() {
		return companyId;
	}


	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getSendUrl() {
		return sendUrl;
	}


	public void setSendUrl(String sendUrl) {
		this.sendUrl = sendUrl;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		this.sign = sign;
	}


	public String getExtNo() {
		return extNo;
	}


	public void setExtNo(String extNo) {
		this.extNo = extNo;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String[] getMobiles() {
		return mobiles;
	}


	public void setMobiles(String[] mobiles) {
		this.mobiles = mobiles;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getSentTime() {
		return sentTime;
	}


	public void setSentTime(String sentTime) {
		this.sentTime = sentTime;
	}


	public String getSrcCharset() {
		return srcCharset;
	}


	public void setSrcCharset(String srcCharset) {
		this.srcCharset = srcCharset;
	}


	public Long getSmsID() {
		return smsID;
	}


	public void setSmsID(Long smsID) {
		this.smsID = smsID;
	}


	public Long getMobileCount() {
		return mobileCount;
	}


	public void setMobileCount(Long mobileCount) {
		this.mobileCount = mobileCount;
	}


	public Integer getSmsPriority() {
		return smsPriority;
	}


	public void setSmsPriority(Integer smsPriority) {
		this.smsPriority = smsPriority;
	}


	/**
	 * 1.短信息发送接口（相同内容群发，可自定义流水号）
	 * @param strPtMsgId 平台返回的流水号
	 * @param strUserId  帐号
	 * @param strPwd 密码
	 * @param strMobiles 手机号
	 * @param strMessage 短信内容
	 * @param strSubPort 扩展子号
	 * @param strUserMsgId 用户自编流水号
	 * @return 状态
	 * @throws Exception 
	 */
	public abstract void sendSms(String mobile,String content) throws SmsException;
	
	
	/**
	 * 2.短信息发送接口（不同内容群发，可自定义不同流水号，自定义不同扩展号）
	 * @param strPtMsgId 平台返回的流水号
	 * @param strUserId 帐号
	 * @param strPwd 密码
	 * @param MultixXg 批量请求包
	 * @return 状态
	 */
	public abstract int sendMultixSms(StringBuffer strPtMsgId,String strUserId, String strPwd, List<MULTIX_XG> MultixXg) throws SmsException;
	
	
	/**
	 * 4.获取上行
	 * @param strUserId 帐号
	 * @param strPwd 密码
	 * @return 返回上行集合
	 */
	public abstract List<MO_PACK> getMo(String strUserId, String strPwd) throws SmsException;
	
	
	/**
	 * 5.状态报告
	 * @param strUserId 帐号
	 * @param strPwd 密码
	 * @return 返回状态报告集合
	 */
	public abstract List<RPT_PACK> getRpt(String strUserId, String strPwd) throws SmsException;
	
	/**
	 * 
	 * <pre>
	 *  执行方法
	 * </pre>
	 *
	 * @param post post对象
	 * @param data 参数列表
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public String executeMethod(PostMethod post, NameValuePair[] data)
			throws HttpException, IOException {
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");
		post.setRequestBody(data);
		HttpClient client = new HttpClient();
		client.executeMethod(post);
//		Header[] headers = post.getResponseHeaders();
		int statusCode = post.getStatusCode();
		logger.info("statusCode:" + statusCode);
//		for (Header h : headers) {
//			logger.info(h.toString());
//		}
		return new String(post.getResponseBodyAsString().getBytes("utf-8"));
	}

	/**
	 * 
	 * <pre>
	 *  定时发送短信
	 * </pre>
	 *
	 * @param mobile
	 * @param content
	 * @param sendTime 发送时间 时间格式 
	 * @throws SmsException
	 */
	public void sendSmsForTime(String mobile, String content, Date sendTime)
			throws SmsException {
	}


	public String getMarketName() {
		return marketName;
	}


	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}


	public String getMarketPassword() {
		return marketPassword;
	}


	public void setMarketPassword(String marketPassword) {
		this.marketPassword = marketPassword;
	}
}
