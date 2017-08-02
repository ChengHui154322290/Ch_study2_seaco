package com.tp.common.vo;

/**
 * 常用工具常量
 * @author szy
 *
 */
public class CmmConstant {

	/**
	 * 短信通道
	 * @author szy
	 *
	 */
	public interface MessageChannelType{
		/**容联-云通讯*/
		Integer YUN_TONG_XUN = 1;
	}
	
	/**
	 * 短信模块
	 * @author szy
	 *
	 */
	public interface SHORT_MESSAGE_TEMPLATE_TYPE{
		/**注册验证模板*/
		Integer register_verify=1;
		/**登录验证模板*/
		/**找回密码模板*/
		/**支付成功后通知模板*/
		/**发货成功后通知模板*/
		/**退款成功通知模板*/
	}
	
	public static final String OPTION_CACHE_KEY="OPTION_CACHE_KEY";
	
	/**
	 * 后台管理系统模块
	 * @author szy
	 *
	 */
	public enum RESOURCE_MODEL_TYPE{
		prd(1,"商品"),
		ord(2,"订单"),
		mmp(3,"营销"),
		cms(4,"内容"),
		usr(5,"用户"),
		sys(6,"系统"),
		rpt(7,"数据"),
		oth(8,"其他");
	    
	    public Integer code;
	    public String cnName;
	    
	    RESOURCE_MODEL_TYPE(Integer code,String cnName){
	    	this.code = code;
	    	this.cnName = cnName;
	    }
	    public static String getName(Integer code){
	    	for(RESOURCE_MODEL_TYPE entry:RESOURCE_MODEL_TYPE.values()){
	    		if(entry.code.equals(code)){
	    			return entry.cnName;
	    		}
	    	}
	    	return null;
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
	 * 权限类型
	 * @author szy
	 *
	 */
	public enum RESOURCE_TYPE{
		group(1,"分组"),
		menu(2,"菜单"),
		element(3,"元素");
		public Integer code;
	    public String cnName;
	    
	    RESOURCE_TYPE(Integer code,String cnName){
	    	this.code = code;
	    	this.cnName = cnName;
	    }
	    public static String getName(Integer code){
	    	for(RESOURCE_TYPE entry:RESOURCE_TYPE.values()){
	    		if(entry.code.equals(code)){
	    			return entry.cnName;
	    		}
	    	}
	    	return null;
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
}
