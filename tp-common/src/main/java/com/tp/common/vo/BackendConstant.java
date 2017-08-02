/**
 * 
 */
package com.tp.common.vo;

/**
 * @author szy
 *
 */
public final class BackendConstant {
	public static final String BACKEND_SPACE = "/backend";
	public interface SpConstant {
		/*** 西客 spid ***/
		final static long SP_XIGOU = 0L;
	}
	
	public enum SessionKey {
		LOGIN(0,"loginSecurityCode"),
		REGISTER(1,"registerSecurityCode"),
		VALIDATECODE(2,"validateCode"),
		SMSCODE(3,"smsCode"),
		SMSPWDCODE(5,"smsPwdCode"),
		USER(6,"user"),
		OTHER(999,"other.unSelect");
		 
		// 成员变量
	     public Integer key;
	     public String value;

	     // 构造方法
	     private SessionKey(Integer key,String value) {
	         this.key = key;
	         this.value = value;
	     }

	     // 普通方法
	     public static String getValue(int key) {
	         for (SessionKey c : SessionKey.values()) {
	             if (c.getKey() == key) {
	                 return c.getValue();
	             }
	         }
	         return null;
	     }

		public Integer getKey() {
			return key;
		}

		public void setKey(Integer key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
	
	public interface Boolean {
		boolean True = true;
		boolean False = false;
	}
	
	public interface IsSuccess {
		Integer Success = 0;
		Integer Fail = 1;
	}
}
