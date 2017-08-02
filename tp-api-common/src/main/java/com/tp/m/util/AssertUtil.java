package com.tp.m.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.tp.m.enums.MResultInfo;
import com.tp.m.enums.ValidFieldType;
import com.tp.m.exception.MobileException;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 入参验证
 * @author zhuss
 * @2016年1月2日 下午3:58:06
 */
public class AssertUtil {
	
    public static void notNull(Object o, MResultInfo errorCode) {
        if (o == null) {
        	throw new MobileException(errorCode);
        }
    }

    public static void notEmpty(String str, MResultInfo errorCode) {
        if (StringUtils.isEmpty(str)) {
            throw new MobileException(errorCode);
        }
    }
    
    public static <T> void notEmpty(Collection<T> collection, MResultInfo errorCode) {
        if (CollectionUtils.isEmpty(collection)) {
        	throw new MobileException(errorCode);
        }
    }
    
    /**
     * 验证字段属性是否为空
     * @param str
     * @param errorCode
     */
    public static void notBlank(String str,MResultInfo errorCode) {
        if (StringUtil.isBlank(str)) {
            throw new MobileException(errorCode);
        }
    }
    
    /**
     * 验证字段属性是否合法
     * @param str
     * @param fieldType
     */
    public static void notValid(String str,ValidFieldType fieldType) {
    	if(fieldType.equals(ValidFieldType.LOGONNAME)){//验证登录用户名:有可能是手机也可能是邮箱,第一步支持手机号
        	if(!VerifyUtil.verifyTelephone(str)) throw new MobileException(MResultInfo.LOGONNAME_NO_VALID);
        }else if(fieldType.equals(ValidFieldType.TELEPHONE)){//验证手机号
        	if(!VerifyUtil.verifyTelephone(str)) throw new MobileException(MResultInfo.TELEPHONE_NO_VALID);
        }else if(fieldType.equals(ValidFieldType.EMAIL)){//验证邮箱
        	if(!VerifyUtil.verifyEmail(str)) throw new MobileException(MResultInfo.EMAIL_NO_VALID);
        }else if(fieldType.equals(ValidFieldType.PASSWORD)){//验证密码
        	if(!VerifyUtil.verifyPassword(str)) throw new MobileException(MResultInfo.PASSWORD_NO_VALID);
        }else if(fieldType.equals(ValidFieldType.CAPTCHA)){//验证手机验证码
        	if(!VerifyUtil.verifyCaptcha(str)) throw new MobileException(MResultInfo.CAPTCHA_NO_VALID);
        }else if(fieldType.equals(ValidFieldType.ID)){//验证身份证号
        	if(!VerifyUtil.verifyCardID(str)) throw new MobileException(MResultInfo.ID_NO_VALID);
        }else{
        	throw new MobileException(MResultInfo.TYPE_NOT_IN_SCOPE);
        }
    }
    
    /**
     * 验证是否在规定类型范围内
     * @param str
     * @param c
     */
    public static void notScope(String str,Class<?> c,MResultInfo info){
    	boolean r = true;
    	try{
    		Method[] ms = c.getMethods();
    		for(int i = 0;i< ms.length;i++){
    			if(ms[i].getName().equals("check"))r = (boolean) ms[i].invoke(c,str);
    		}
    	}catch(Exception e){
    		throw new MobileException(MResultInfo.PARAM_ERROR);
    	}
    	if(!r) throw new MobileException(info);
    }
}
