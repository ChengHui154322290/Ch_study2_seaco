package com.tp.common.vo.bse;

/**
 * 
 * <pre>
 *   通关渠道枚举类
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public enum ClearanceChannelsEnum {
	
	NBK("宁波保税区",1l),
	SHKJT("上海保税区",2l),
	HWZY("海外直邮",3l),
	GZBSQ("广州白云机场保税区",4l),
	ZHENGZHOUBSQ("郑州保税区",5l),
	HANGZHOU("杭州保税区",6l),
	ZONGSHU("海关总署", 10l);
	 
	//通关渠道名称
	private String clearanceName;
	
	public Long id;
	
	private ClearanceChannelsEnum(String clearanceName,Long id) {
		this.clearanceName = clearanceName;
		this.id = id;
	}

	public String getClearanceName() {
		return clearanceName;
	}
}
