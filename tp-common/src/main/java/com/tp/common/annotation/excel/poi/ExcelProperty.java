package com.tp.common.annotation.excel.poi;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * <pre>
 *  标记字段为Excel填充字段
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelProperty {
	
	/**
	 * 
	 * <pre>
	 *   采用的列名， 该列名对应Excel中第一列的内容
	 * </pre>
	 *
	 * @return
	 */
	String value() default "";

	/**
	 * 
	 * <pre>
	 * 该列是否必须
	 * </pre>
	 *
	 * @return
	 */
	boolean required() default false;
	
	/**
	 * 
	 * <pre>
	 * 	 商品纬度 1-spu，2-prdid，3-sku
	 * </pre>
	 *
	 * @return
	 */
	int itemLatitude()  default 1;
	
	
	/**
	 * 长度
	 * @return
	 */
	int columnLength()  default -1;
	
	/**
	 * 
	 * <pre>
	 * 校验规则类
	 * </pre>
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends ExcelRule> rule() default NoneRule.class;

	/**
	 * 
	 * <pre>
	 * 正则表达式校验规则
	 * </pre>
	 *
	 * @return
	 */
	String regexp() default "";
	
	/**
	 * 
	 * <pre>
	 * 正则规则校验失败错误提示信息
	 * </pre>
	 *
	 * @return
	 */
	String regexpErrorMessage() default "";
	
	/**
	 * 
	 * <pre>
	 *  默认值
	 * 	默认值均采用String类型，系统将会自动进行类型转换，不支持对象类型!
	 * </pre>
	 *
	 * @return
	 */
	String defaultValue() default "";
	
	/**
	 * 
	 * <pre>
	 *  是否采用默认值
	 *  如果为true，默认值为空的时候会使用空字符串，否则使用null
	 * </pre>
	 *
	 * @return
	 */
	boolean hasDefaultValue() default false;
}