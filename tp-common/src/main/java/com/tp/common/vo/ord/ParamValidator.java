package com.tp.common.vo.ord;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.exception.OrderServiceException;

/**
 * 入参校验工具
 * 
 * @author szy
 * @version 0.0.1
 */
public class ParamValidator {
	
	private static final Logger log = LoggerFactory.getLogger(ParamValidator.class);
	
	private static final String NOT_NULL = "%s-参数[%s]不能为空";
	private static final String NOT_BLANK = "%s-参数[%s]不能为空字符串";
	private static final String NOT_EMPTY = "%s-参数[%s]不能为空集合";
	private static final String RANGE = "%s-参数[%s：'%d']不在范围内";
	
	/** 业务名称 */
	private String business;
	
	/**
	 * 
	 * @param business	业务名称(eg. "下单"、"加入购物车")
	 */
	public ParamValidator(String business) {
		super();
		this.business = business;
	}

	/**
	 * 不为空
	 * 
	 * @param obj
	 * @param paramName	对象名称
	 */
	public void notNull(Object obj, String objName) {
		if (null == obj) {
			throwEx(String.format(NOT_NULL, business, objName));
		}
	}

	/**
	 * 不能空字符串
	 * 
	 * @param text
	 * @param objName
	 */
	public void notBlank(String text, String objName) {
		notNull(text, objName);
		
		if (StringUtils.isBlank(text)) {
			throwEx(String.format(NOT_BLANK, business, objName));
		}
	}

	/**
	 * 不能为空集合
	 * 
	 * @param coll
	 * @param objName
	 */
	public void notEmpty(Collection<?> coll, String objName) {
		notNull(coll, objName);
		
		if (CollectionUtils.isEmpty(coll)) {
			throwEx(String.format(NOT_EMPTY, business, objName));
		}
	}

	/**
	 * 不能为空集合
	 * 
	 * @param coll
	 * @param objName
	 */
	public void notEmpty(Map<?, ?> map, String objName) {
		notNull(map, objName);
		
		if (map.size() == 0) {
			throwEx(String.format(NOT_EMPTY, business, objName));
		}
	}
	
	/**
	 * 限制范围
	 * 
	 * @param num
	 * @param constant
	 * @param objName
	 */
	public void range(Integer num, BaseEnum constant, String objName) {
		notNull(num, objName);
		
		if (! constant.contains(num)) {
			throwEx(String.format(RANGE, business, objName, num));
		}
	}
	
	// 纪录并抛出异常
	private void throwEx(String errMsg) {
		log.error(errMsg);
		throw new OrderServiceException(OrderErrorCodes.PARAM_ERROR, errMsg);
	}
}
