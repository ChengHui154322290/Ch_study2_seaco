package com.tp.common.vo;

import com.tp.common.vo.Constant.SPLIT_SIGN;

/**
 * redis 常量
 * @author szy
 *
 */
public class RedisConstant {

	/**
	 * redis key 前缀
	 * @author szy
	 *
	 */
	public enum REDIS_KEY{
		basedata,
		item,
		order,
		member,
		promotion,
		lock;
		public String toString(){
			return name()+SPLIT_SIGN.COLON;
		}
	}
}
