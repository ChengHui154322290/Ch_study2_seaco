package com.tp.common.vo;

import org.apache.commons.lang3.StringUtils;


/**
 * 全网站常量
 * @author szy
 *
 */
public final class Constant {
	
	
	/**
	 * 大版本号
	 * @author szy
	 *
	 */
	public enum VERSION{
		/**盘古*/
		PANGEA,
		/**女娲*/
		NUWA
	}

	public interface SPLIT_SIGN {
		/**COMMA(",")*/
		String COMMA = ",";/**COLON(":")*/
		String COLON = ":";/**UNDERLINE("_")*/
		String UNDERLINE = "_";/**POUND("#")*/
		String POUND = "#";/**SPACEBAR("1")*/
		String SPACEBAR="|";
	}
	
	/**
	 * 模块
	 * @author szy
	 *
	 */
	public static enum EXCEPTION_MODEL_TYPE{
		user,
		base,
		member,
		common,
		cms,
		crm,
		product,
		order,
		payment,
		comment,
		finance,
		supplier,
		system,
		error;
	}
	
	public interface DATE_TIME_FORMAT{
		String DATE_FORMAT = "yyyy-MM-dd";
		String TIME_FORMAT = "HH:mm:ss";
		String ALL_FORMAT = "yyyy-MM-dd HH:mm:ss";
	}
	/**
	 * 是否默认
	 * @author szy
	 *
	 */
	public interface DEFAULTED{
		Integer YES = 1;
		Integer NO = 0;
	}
	
	/**
	 * 是否失效
	 * @author szy
	 *
	 */
	public interface DISABLED extends DEFAULTED{
	}
	
	public interface ENABLED extends DEFAULTED{
	} 
	
	/**
	 * 是否删除 
	 * @author szy
	 *
	 */
	public interface DELECTED extends DEFAULTED{
	}
	/**
	 * 是否选择
	 * @author szy
	 *
	 */
	public interface SELECTED extends DEFAULTED{
	}
	
	public interface TF extends DEFAULTED{
	}
	
	/**
	 * 所属者类型
	 * @author szy
	 *
	 */
	public interface AUTHOR_TYPE{
		/**[系统]*/
		String SYSTEM ="[系统]";
		/**[调度]*/
		String JOB ="[调度]";
		/**[会员]*/
		String MEMBER = "[会员]";
		/**[客服]*/
		String USER_CALL = "[客服]";
		/**[财务]*/
		String USER_FINANCE ="[财务]";
		/**[运营]*/
		String USER_OPERATER = "[运营]";
	}
	
	public enum LOG_AUTHOR_TYPE{
		SYSTEM(1),
		JOB(2),
		MEMBER(3),
		USER_CALL(4),
		USER_FINANCE(5),
		USER_OPERATER(6),
		SELLER(7);
		public Integer code;
		LOG_AUTHOR_TYPE(Integer code){
			this.code = code;
		}
	}
	
	/**
	 * 失败重试时间间隔,单位分,数组下标是第几次 
	 * 8<<(n-1)
	 */
	public static final Integer RETRY_TIME_INTERVAL_ARRAY[]={8<<0,8<<1,8<<2,8<<3,8<<4,8<<5};
	public enum IMAGE_URL_TYPE{
		cmsimg("http://cmsimg.qn.seagoor.com/","https://cmsimg.qn.seagoor.com/","配置管理"),
		member("","",""),
		basedata("http://basedata.qn.seagoor.com/","https://basedata.qn.seagoor.com/","基础数据"),
		supplier("http://supplier.qn.seagoor.com/","https://supplier.qn.seagoor.com/","供应商"),
		item("http://item.qn.seagoor.com/","https://item.qn.seagoor.com/","商品");
		public String url;
		public String sslUrl;
		public String cnName;
		IMAGE_URL_TYPE(String url,String sslUrl,String cnName){
			this.url = url;
			this.sslUrl = sslUrl;
			this.cnName = cnName;
		}
		
		public static String getUrl(String name){
			for(IMAGE_URL_TYPE entry:IMAGE_URL_TYPE.values()){
				if(entry.name().equals(name)){
					return entry.url;
				}
			}
			return "";
		}
		
		public static String getSslUrl(String name){
			for(IMAGE_URL_TYPE entry:IMAGE_URL_TYPE.values()){
				if(entry.name().equals(name)){
					return entry.sslUrl;
				}
			}
			return "";
		}
	}
	
	/**
	 * 获取下一次重试时间
	 * @param retryTime
	 * @return
	 */
	public static final Integer getNextRetryTime(Integer retryTime){
		if(null==retryTime){
			return RETRY_TIME_INTERVAL_ARRAY[0];
		}
		if(retryTime<RETRY_TIME_INTERVAL_ARRAY[0]){
			return RETRY_TIME_INTERVAL_ARRAY[0];
		}
		if(retryTime>RETRY_TIME_INTERVAL_ARRAY[5]){
			return RETRY_TIME_INTERVAL_ARRAY[5];
		}
		for(int i=0;i<6;i++){
			Integer time = RETRY_TIME_INTERVAL_ARRAY[i];
			if(retryTime.equals(time)){
				return RETRY_TIME_INTERVAL_ARRAY[i+1];
			}
			if(i<5 && retryTime>time && retryTime<RETRY_TIME_INTERVAL_ARRAY[i+1]){
				return RETRY_TIME_INTERVAL_ARRAY[i+1];
			}
		}
		return RETRY_TIME_INTERVAL_ARRAY[5];
	}
	
	/**
	 * 平台类型 
	 * @author szy
	 *
	 */
	public enum PLATFORM_TYPE{
		IOS(1,"IOS"),
		ANDROID(2,"ANDROID"),
		WAP(3,"WAP"),
		PC(4,"PC");
		public Integer code;
		public String cnName;
		PLATFORM_TYPE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code){
			for(PLATFORM_TYPE entry:PLATFORM_TYPE.values()){
				if(entry.code.equals(code)){
					return entry.cnName;
				}
			}
			return null;
		}
		
		public static int isValid(String name){
			if(StringUtils.isBlank(name)) return 0;
			for(PLATFORM_TYPE entry:PLATFORM_TYPE.values()){
				if(entry.toString().equalsIgnoreCase(name))return entry.code;
			}
			return 0;
		}
		
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
	
    /**
     * 缓存时长
     * @author szy
     *
     */
    public enum CACHE_STEP{
    	MINUTE(60*1000),
    	TEN_MINUTE(10*60*1000),
    	HALF_HOUR(30*60*1000),
    	HOUR(3600*1000),
    	TWO_HOUR(2*3600*1000),
    	DAY(24*3600*1000);
    	
    	public Integer code;
    	CACHE_STEP(Integer code){
    		this.code = code;
    	}
    }
    
    /**
     * 图片尺寸
     * @author szy
     *
     */
    public enum IMAGE_SIZE{
    	model40_40(40,40),
    	model60_60(60,60),
		model80_80(80,80),
		model100_100(100,100),
    	model120_120(120,120),
    	model150_150(120,120),
    	model200_200(200,200),
    	model300_300(300,300),
    	model400_400(400,400),
    	model600_600(600,600),
    	model750_750(750,750),
    	model800_800(800,800);
		public Integer width;
		public Integer height;
		IMAGE_SIZE(Integer width,Integer height){
			this.width = width;
			this.height = height;
		}
    }
    public static String getTempDir() {
		return System.getProperty("java.io.tmpdir");
	}
    
    public static final Double ZERO = 0.00D;
	
	/***
	 * 单据类型代码
	 * @author szy
	 *
	 */
	public enum DOCUMENT_TYPE{
		/**订单-10*/
		SO_ORDER(10),
		/**子订单*/
		SO_SUB_ORDER(11),
		/**退款-20*/
		REFUND(20),
		/**赔偿-21*/
		OFFSET(21),
		/**取消-22*/
		CANCEL(22),
		/**拒收-23*/
		REJECT(23),
		/**退货-24*/
		RETURNED(24),
		
		/**支付 -30*/
		PAYMENT(30),
		/**拉新提佣*/
		REFERRAL_FEES(40),
		WITHDRAW(41),

		DSS_PAY(50),

		SEAGOOR_PAY(60),

		SEAGOOR_PAY_REFUND(62)
		;
		
		public Integer code;
		
		DOCUMENT_TYPE(Integer code){
			this.code = code;
		}
		
	}
}
