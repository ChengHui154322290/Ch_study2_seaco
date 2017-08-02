package com.tp.common.vo.wms.kjb2c;

import org.apache.commons.lang.StringUtils;

/**
 * 订单分类
 * <pre>
 * 01=订单出库
 * 02=菜鸟模式单子
 * 99=换货出库
 * </pre>
 * @author beck
 *
 */
public enum KJSoOrderReceipType {
	SO("01","订单出库"),
	CNT("02","菜鸟模式单子"),
	CSO("99","换货出库");
	
	KJSoOrderReceipType(String code,String desc){
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
		KJSoOrderReceipType[] values = KJSoOrderReceipType.values();
		for (KJSoOrderReceipType item : values) {
			if(item.getCode().equals(code)){
				return item.getDesc();
			}
		}
		return null;
	}

}
