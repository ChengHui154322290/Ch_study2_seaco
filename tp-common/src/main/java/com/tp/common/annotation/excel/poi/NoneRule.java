package com.tp.common.annotation.excel.poi;

/**
 * 
 * <pre>
 *  默认字段处理规则
 *  不对字段值进行任何处理
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class NoneRule implements ExcelRule<Object> {

	public void check(Object value, String columnName, String fieldName) {
	}

	public Object filter(Object value, String columnName, String fieldName) {
		return value;
	}

}