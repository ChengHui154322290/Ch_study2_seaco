package com.tp.common.vo;

/**
 * 会员常量
 * @author szy
 *
 */
public final class MemberConstant {
	public static final String MEMBER_SESSION_KEY = "MEMBER_SESSION_KEY";
	public static final String JHJ_JSESSION_ID = "jhjJessionId";
	public static final String MEMBER_VISIT_IP = "MEMBER_VISIT_IP";
	
	public static final String WEIXIN_OPENID = "WEIXIN_OPENID";
	
	/**配送时间选项字典编码*/
	public static final String DELIVERY_TIME_TYPE="DELIVERY_TIME_TYPE";
	
	/**
	 * 会员性别
	 * @author szy
	 *
	 */
	public enum GENDER{
		GENDER_0(0,"女"),
		GENDER_1(1,"男");
		
		
		private Integer code;
		private String cnName;
		
		private GENDER(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code){
			for(GENDER entry:GENDER.values()){
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
	 * 会员状态
	 * @author szy
	 *
	 */
	public enum STATUS{
		STATUS_0(0,"禁用"),
		STATUS_1(1,"启用");
		
		private Integer code;
		private String cnName;
		
		private STATUS(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
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
	 * 点评状态
	 * 1-创建、2-审核中、3-已审核、4-审核失败
	 * @author szy
	 *
	 */
	public enum COMMENT_STATUS{
		CREATE(1,"创建"),
		AUDIT(2,"审核中"),
		PASS(3,"已审核"),
		FAIL(4,"审核失败");
		public Integer code;
		public String cnName;
		COMMENT_STATUS(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
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
