package com.tp.seller.util;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;


/**
 * 
 * <pre>
 * 资源工具类
 * </pre>
 *
 * @author Administrator
 * @version $Id: ResourcesUtil.java, v 0.1 2014年12月29日 上午11:01:41 liuchunhua Exp $
 */
public class ResourcesUtil implements Serializable {


	/**
	 * <pre>
	 * 资源读取
	 * </pre>
	 */
	private static final long serialVersionUID = 1824234628530004178L;

	/**
	 * 系统语言环境，默认为中文zh
	 */
	public static final String LANGUAGE = "zh";

	/**
	 * 系统国家环境，默认为中国CN
	 */
	public static final String COUNTRY = "CN";
	private static Locale getLocale() {
		Locale locale = new Locale(LANGUAGE, COUNTRY);
		return locale;
	}


	private static String getProperties(String baseName, String section) {
		String retValue = "";
		try {
			Locale locale = getLocale();
			ResourceBundle rb = ResourceBundle.getBundle(baseName, locale);
			retValue = (String) rb.getObject(section);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO 添加处理
		}
		return retValue;
	}

	public static String getValue(String fileName, String key) {
		String value = getProperties(fileName,key);
		return value;
	}

	public static List<String> gekeyList(String baseName) {
		Locale locale = getLocale();
		ResourceBundle rb = ResourceBundle.getBundle(baseName, locale);

		List<String> reslist = new ArrayList<String>();

		Set<String> keyset = rb.keySet();
		for (Iterator<String> it = keyset.iterator(); it.hasNext();) {
			String lkey = (String)it.next();
			reslist.add(lkey);
		}

		return reslist;

	}

	
	public static String getValue(String fileName, String key, Object[] objs) {
		String pattern = getValue(fileName, key);
		String value = MessageFormat.format(pattern, objs);
		return value;
	}
}
