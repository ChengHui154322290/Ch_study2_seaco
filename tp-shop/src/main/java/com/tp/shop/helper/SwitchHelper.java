package com.tp.shop.helper;

import com.tp.m.util.StringUtil;

/**
 * 开关帮助类
 * @author zhuss
 */
public class SwitchHelper {

	public static final String KG_RATE = "KG_TAXRATE";
	
	public static boolean isShowRate(){
		String isShow = PropertiesHelper.readValue(KG_RATE);
		if(StringUtil.isBlank(isShow))return false;
		if(StringUtil.equals(isShow, StringUtil.ONE))return true;
		return false;
	}
	
	public static String taxDesc(){
		String taxDesc = PropertiesHelper.readValue("TAX_DESC");
		if(StringUtil.isBlank(taxDesc))return "";
		return taxDesc;
	}
}
