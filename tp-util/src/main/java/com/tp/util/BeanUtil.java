package com.tp.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;

public final class BeanUtil extends BeanUtils{

private static 	Logger logger = org.slf4j.LoggerFactory.getLogger(BeanUtil.class);

	public static Map<String,Object> beanMap(Object obj){
		Field[] fields = obj.getClass().getDeclaredFields();
		Field[] superFields = obj.getClass().getSuperclass().getDeclaredFields();
		fields = ArrayUtils.addAll(fields, superFields);
		Map<String,Object> map = new HashMap<String,Object>();
		for (Field f : fields) {
			if (Modifier.isStatic(f.getModifiers())
					|| Modifier.isFinal(f.getModifiers())
				|| Modifier.isTransient(f.getModifiers()))
				continue;
			Object value = null;
			try {
				f.setAccessible(true);
				value = f.get(obj);
				if(value!=null && value instanceof String && StringUtils.isBlank(value.toString())){
					value = null;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (value != null)
				map.put(f.getName(), value);
		}
		map.remove("startPage");
		map.remove("pageSize");
		return map;
	}

	public static <T> T clone(Object o){
		try {
			return (T)BeanUtils.cloneBean(o);
		} catch (IllegalAccessException |InstantiationException |InvocationTargetException |NoSuchMethodException e) {
			logger.error("CLONE_BEAN_ERROR", e);
		}
return null;
	}

}
