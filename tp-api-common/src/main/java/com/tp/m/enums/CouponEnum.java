package com.tp.m.enums;

import com.tp.m.util.StringUtil;

/**
 * 优惠券枚举
 * @author zhuss
 * @2016年1月2日 下午4:10:32
 */
public interface CouponEnum {

	public enum ListFromType{
		LIST_FROM_USER("1","个人中心"),
		LIST_FROM_CAN_RECEIVE("2","可领取"),
		LIST_FROM_ORDER("3","结算");
		public String code;
		
	    public String desc;

		private ListFromType(String code, String desc) {
			this.code = code;
			this.desc = desc;
		} 
		
		public static String getDescByCode(String code){
			if(StringUtil.isNotBlank(code)){
				for(ListFromType c : ListFromType.values()){
					if(code.equals(c.code)) return c.desc;
				}
			} 
			return "未知";
		}
		
		public static boolean check(String code){
			if(StringUtil.isNotBlank(code)){
				for(ListFromType c : ListFromType.values()){
					if(code.equals(c.code)) return true;
				}
			}
			return false;
		}
	}
	
	public enum Status{
		NORMAL("0","可用"),
		USED("1","已使用"),
		OVERDUE("2","已过期");
		public String code;
		
	    public String desc;

		private Status(String code, String desc) {
			this.code = code;
			this.desc = desc;
		} 
		public static boolean check(String code){
			if(StringUtil.isBlank(code)) return false;
			for(Status c : Status.values()){
				if(code.equals(c.code)) return true;
			}
			return false;
		}
	}
}
