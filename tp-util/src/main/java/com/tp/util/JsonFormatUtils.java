package com.tp.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

/**
 * json格式工具
 * 
 * @author szy
 * @version 0.0.1
 */
public class JsonFormatUtils {

	/**
	 * 将obj格式化JSON字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String format(Object obj) {
		if (null != obj) {
			JsonConfig conf = new JsonConfig();
			conf.setAllowNonStringKeys(true);	// 允许key为非String类型
			
			if (JSONUtils.isArray(obj.getClass()) || obj instanceof Enum) {
				return JSONArray.fromObject(obj, conf).toString();
			}
			
			return JSONObject.fromObject(obj, conf).toString();
		}
		return null;
	}
	public static JSONArray stringToJsonArray(String json){
		if (null != json) {
			JsonConfig conf = new JsonConfig();
			conf.setAllowNonStringKeys(true);	// 允许key为非String类型
			return JSONArray.fromObject(json, conf);
		}
		return null;
	}
}
