package com.tp.common.vo.ord;

public class LogTypeConstant {
	
	/**
	 * 日志记录用户类型
	 * @author szy
	 */
	public enum LOG_TYPE{
		
		/**会员 -1*/
		MEMBER(1,"会员"),
		/**西客商城人员-2*/
		USER(2,"西客"),
		/**商家-3*/
		SELLER(3,"商家"),
		/**供应商-4*/
		SUPPLIER(4,"供应商"),
		/**系统-5*/
		SYSTEM(5,"系统"),
		OUT_APP(6,"外部系统");

		public Integer code;
		public String cnName;
		
		LOG_TYPE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code){
			if(null!=code){
				for(LOG_TYPE entry:LOG_TYPE.values()){
					if(entry.code.intValue()==code){
						return entry.cnName;
					}
				}
			}
			return null;
		}
	}
	
	public enum LOG_LEVEL{
		INFO(1),
		DEBUG(2),
		WARN(3),
		ERROR(4),
		FAIL(5);
		public Integer code;
		LOG_LEVEL(Integer code){
			this.code = code;
		}
	}
}
