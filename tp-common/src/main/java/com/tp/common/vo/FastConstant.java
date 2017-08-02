package com.tp.common.vo;

import com.tp.common.vo.StorageConstant.StorageType;

public final class FastConstant {

	/**
	 * 用户类型
	 * @author szy
	 *
	 */
	public enum USER_TYPE{
		MANAGER(1,"管理员"),
		EMPLOYEE(2,"员工"),
		COURIER(3,"速递员");
		public Integer code;
		public String cnName;
		
		USER_TYPE(Integer code,String cnName){
			this.code=code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code){
			if(code!=null){
				for(USER_TYPE entry:USER_TYPE.values()){
					if(entry.code.intValue()==code){
						return entry.cnName;
					}
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
	 * 店铺类型
	 * @author szy
	 *
	 */
	public enum SHOP_TYPE{
		FAST_BUY(1,StorageType.FAST.value,"速购"),
		GROUP_COUPON(2,StorageType.BUY_COUPONS.value,"团购券");
		public Integer code;
		public Integer whType;
		public String cnName;
		
		SHOP_TYPE(Integer code,Integer whType,String cnName){
			this.code=code;
			this.whType = whType;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code){
			if(code!=null){
				for(SHOP_TYPE entry:SHOP_TYPE.values()){
					if(entry.code.intValue()==code){
						return entry.cnName;
					}
				}
			}
			return null;
		}
		
		public static Integer getWhType(Integer code){
			if(code!=null){
				for(SHOP_TYPE entry:SHOP_TYPE.values()){
					if(entry.code.intValue()==code){
						return entry.whType;
					}
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
