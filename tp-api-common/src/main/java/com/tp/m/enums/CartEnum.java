package com.tp.m.enums;

import com.tp.m.util.StringUtil;


/**
 * 购物车枚举
 * @author zhuss
 * @2016年1月2日 下午4:10:32
 */
public interface CartEnum {

	public enum CheckType{
		CHECK_ALL("1","全选"),
		CHECK_CANCEL_ALL("2","取消全选"),
		CHECK_RADIO("3","单选"),
		CHECK_CANCEL_RADIO("4","取消单选"),
		CHECK_MODIFY("5","更新"),
		CHECK_DEL("6","删除"),
		CHECK_SUBMIT("7","提交");
		public String code;
		
	    public String desc;

		private CheckType(String code, String desc) {
			this.code = code;
			this.desc = desc;
		} 
		
		public static String getDescByCode(String code){
			if(StringUtil.isNotBlank(code)){
				for(CheckType c : CheckType.values()){
					if(code.equals(c.code)) return c.desc;
				}
			} 
			return "未知";
		}
		
		public static boolean check(String code){
			if(StringUtil.isNotBlank(code)){
				for(CheckType c : CheckType.values()){
					if(code.equals(c.code)) return true;
				}
			}
			return false;
		}
	}
	
	// 购物车行状态
	public enum CartLineStatus {
		CARTLINE_VALID("1", "有效", 0), 
		CARTLINE_INVALID("2", "已下架", 2), 
		CARTLINE_STOCK_LACK("3", "库存不足", 1), 
		CARTLINE_QUATA_OVER("4", "超过限额", 3);

		public String appcode;
		public String desc;
		public int pccode;

		private CartLineStatus(String appcode, String desc, int pccode) {
			this.appcode = appcode;
			this.desc = desc;
			this.pccode = pccode;
		}

		public static CartLineStatus getStatusByPC(int pccode) {
			for (CartLineStatus c : CartLineStatus.values()) {
				if (c.pccode == pccode) {
					return c;
				}
			}
			return null;
		}
	}
}
