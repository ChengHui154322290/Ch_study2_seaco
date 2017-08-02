package com.tp.util;

import org.apache.commons.lang3.StringUtils;


public final class StringUtil extends StringUtils{
	private static final String REX = "\"";
    /**
     * <p>
     * 如果给定的对象为<code>null</code>或者字符串形式为空字符串，则返回<code>true</code>。
     * </p> .
     * 
     * @param o 要验证的对象。
     * @return 返回true，如果给定的对象为<code>null</code>或者字符串形式为空字符串
     */
    public static boolean isNullOrEmpty(Object o) {
        if (o == null) {
            return true;
        } else if (EMPTY.equals(o.toString().trim())
                || "null".equals(o.toString().trim().toLowerCase())) {
            return true;
        }
        return false;
    }
	public static boolean isNull (Object src) {
		return src == null;
	}
	
	public static Boolean isNotBlank(Object obj){
		return obj!=null && isNotBlank(obj.toString());
	}
	public static String l(String rex){
		return REX+rex+REX;
	}
	
}
