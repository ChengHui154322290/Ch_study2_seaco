package com.tp.m.util;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;

/**
 * JSON工具类 - 阿里JSON
 * @author zhuss
 * @2016年1月3日 下午2:57:32
 */
public class JsonUtil {
	/**
	*  DisableCheckSpecialChar：一个对象的字符串属性中如果有特殊字符如双引号，将会在转成json时带有反斜杠转移符。如果不需要转义，可以使用这个属性。默认为false
	*  QuoteFieldNames———-输出key时是否使用双引号,默认为true
	*  WriteMapNullValue——–是否输出值为null的字段,默认为false
	*  WriteNullNumberAsZero—-数值字段如果为null,输出为0,而非null
	*  WriteNullListAsEmpty—–List字段如果为null,输出为[],而非null
	*  WriteNullStringAsEmpty—字符类型字段如果为null,输出为"",而非null
	*  WriteNullBooleanAsFalse–Boolean字段如果为null,输出为false,而非null
	*/
	private static SerializerFeature[] features = {SerializerFeature.WriteNullNumberAsZero,
			SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty };

	private static Logger log = LoggerFactory.getLogger(JsonUtil.class);

	

	/**
	 * JSON字符串转换成Obj
	 * @param key
	 * @param reader
	 * @return
	 * @throws Exception
	 */
	public static Object getObjectByJsonStr(String jsonStr, Class<?> c) {
		return getFastJsonObject(jsonStr, c);
	}

	/**
	 * 使用FastJSON处理JSON对象
	 * @param jsonStr
	 * @param c
	 * @return
	 */
	public static Object getFastJsonObject(String jsonStr, Class<?> c) {
		try {
			Object jsonValue = JSONObject.parseObject(jsonStr, c);
			if(null == jsonValue) throw new MobileException(MResultInfo.PARAM_ERROR);
			return jsonValue;
		} catch (Exception e) {
			log.error("Json参数解析错误", e);
			throw new MobileException(MResultInfo.PARAM_ERROR);
		}
	}

	/**
	 * 根据KEY获取VALUE的值
	 * @param key
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public static Object getJsonObjectByKey(String key, String jsonStr){
		try {
			JSONObject jsonvalue = JSONObject.parseObject(jsonStr);
		    return jsonvalue.get(key);
		} catch (Exception e) {
			log.error("KEY值无法找到");
			throw new MobileException(MResultInfo.PARAM_ERROR);
		}
	}
	
	public static List<?> convListByJsonStr(String jsonStr, Class<?> c){
		if(StringUtil.isBlank(jsonStr)) return null;
		return JSON.parseArray(jsonStr,c);
	}

	/***
	 * json字符串转java List
	 * @param rsContent
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> jsonStringToList(String rsContent) {
		return convertJsonStringToList(rsContent);
	}

	/**
	 * 使用FastJSON处理List转Map
	 * @param rsContent
	 * @return
	 */
	public static List<Map<String, String>> convertJsonStringToList(String rsContent) {
		return JSON.parseObject(rsContent, new TypeReference<List<Map<String, String>>>() {
		});

	}

	public static <T> T parse(String s,Class<T> clazz){
		return  JSONObject.parseObject(s,clazz);
	}

	/**
	 * 对象String输出
	 * @param obj
	 * @return
	 */
	public static String printStrFromObj(Object obj) {
		return JSONArray.toJSONString(obj, features);
	}

	/**
	 * 把Java对象转换成Json格式的
	 * @param obj
	 * @return
	 */
	public static String convertObjToStr(Object obj) {
		return JSONArray.toJSONString(obj, features);
	}
	
}
