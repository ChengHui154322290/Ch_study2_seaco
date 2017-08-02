package com.tp.common.vo;

/**
 * 产品
 * @author szy
 *
 */
public final class ProductConstant {

	public static String defaultSuffix=".html";
	
	public enum RATE_TYPE{
		POSTAL(1,"行邮税"),
		SYNTHESIS(2,"跨境综合税"),
		TAXFREE(3, "包税");
		public Integer code;
		public String cnName;
		
		RATE_TYPE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
	}

}
