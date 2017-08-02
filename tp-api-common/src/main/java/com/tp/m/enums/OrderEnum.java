package com.tp.m.enums;

import com.tp.m.util.StringUtil;

/**
 * 订单枚举
 * @author zhuss
 * @2016年1月2日 下午4:10:32
 */
public interface OrderEnum {

	public enum QueryType{
		ALL("1","全选"),
		UNPAY("2","待付款"),
		UNRECEIPT("3","待收货"),
		UNUSE("7","待使用");
		public String code;
	    public String desc;

		private QueryType(String code, String desc) {
			this.code = code;
			this.desc = desc;
		} 
		
		
		public static boolean check(String code){
			if(StringUtil.isNotBlank(code)){
				for(QueryType c : QueryType.values()){
					if(code.equals(c.code)) return true;
				}
			}
			return false;
		}
	}
	
	//区分取消和删除
	public enum CalOrDelType{
		CANCEL("1","取消"),
		DEL("2","删除");
		public String code;
	    public String desc;

		private CalOrDelType(String code, String desc) {
			this.code = code;
			this.desc = desc;
		} 
		public static boolean check(String code){
			if(StringUtil.isBlank(code)) return false;
			for(CalOrDelType c :CalOrDelType.values()){
				if(code.equals(c.code)) return true;
			}
			return false;
		}
	}
}
