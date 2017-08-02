package com.tp.common.vo.wms.kjb2c;

import org.apache.commons.lang.StringUtils;

/**
 * 快递类型
 * EMS:(01=标准快递，02=经济快递),SF:(03=电商特惠,07=电商速配,26=全球顺保税)
 * @author beck
 *
 */
public enum KJExpressType {
	EMS01("01","EMS标准快递"),
	EMS02("02","EMS经济快递"),
	SF03("03","电商特惠"),
	SF07("07","电商速配"),
	SF26("26","全球顺保税");
	
	KJExpressType(String code,String desc){
		this.code = code;
		this.desc = desc;
	}
	
	private String code;
	
	private String desc;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static String getDescByCode(String code){
		if(StringUtils.isBlank(code)){
			return null;
		}
		KJExpressType[] values = KJExpressType.values();
		for (KJExpressType item : values) {
			if(item.getCode().equals(code)){
				return item.getDesc();
			}
		}
		return null;
	}

}
