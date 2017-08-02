package com.tp.common.vo.wms;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 出库订单状态码表
 * @author beck
 *
 */
public enum StockOutStatus {
	/** 创建订单*/
	SO00("00","创建订单"),
	/** 地址待审*/
	SO10("10","地址待审"),
	/** 国检-待申报*/
	SO11("11","国检-待申报"),
	/** 国检-抽检中*/
	SO12("12","国检-抽检中"),
	/** 国检-抽检未通过 */
	SO13("13","国检-抽检未通过"),
	/** 海关-已申报*/
	SO14("14","国检-已放行/海关-已申报"),
	/** 海关-单证放行*/
	SO15("15","海关-单证放行"),
	/** 海关-审核未通过*/
	SO16("16","海关-审核未通过"),
	/** 海关-货物放行*/
	SO17("17","海关-货物放行"),
	/** 海关-查验未通过*/
	SO18("18","海关-查验未通过"),
	/** 分配完成*/
	SO20("20","分配完成"),
	/** 拣货完成*/
	SO30("30","拣货完成"),
	/** 完全装箱*/
	SO40("40","完全装箱"),
	/** 装车完成*/
	SO50("50","装车完成"),
	/** 完全发运*/
	SO60("60","完全发运"),
	/** 订单取消*/
	SO90("90","订单取消"),
	/** 订单关闭*/
	SO91("91","订单关闭");
	
	private String code;
	
	private String desc;
	/** 以下状态可以取消订单 */
	private static StockOutStatus[] cancelStatus ={
			StockOutStatus.SO00,StockOutStatus.SO10,StockOutStatus.SO11,StockOutStatus.SO12,StockOutStatus.SO13,StockOutStatus.SO14,
			StockOutStatus.SO15,StockOutStatus.SO16,StockOutStatus.SO18
	};
	/**
	 * 验证当前的订单状态是否可以取消
	 * @param code
	 * @return
	 */
	public static boolean cancelSoOrder(StockOutStatus status){
		if(null==status){
			return false;
		}
		return ArrayUtils.contains(cancelStatus, status);
	}
	
	StockOutStatus(String code,String desc){
		this.code=code;
		this.desc=desc;
	}
	
	public static boolean hasCode(String code){
		if(StringUtils.isBlank(code)){
			return false;
		}
		StockOutStatus[] values = StockOutStatus.values();
		for (StockOutStatus item : values) {
			if(item.getCode().equals(code)){
				return true;
			}
		}
		return false;
	}
	
	public static StockOutStatus selectByCode(String code){
		if(StringUtils.isBlank(code)){
			return null;
		}
		StockOutStatus[] values = StockOutStatus.values();
		for (StockOutStatus item : values) {
			if(item.getCode().equals(code)){
				return item;
			}
		}
		return null;
	}

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
		StockOutStatus[] values = StockOutStatus.values();
		for (StockOutStatus item : values) {
			if(item.getCode().equals(code)){
				return item.getDesc();
			}
		}
		return null;
	}
	
}
