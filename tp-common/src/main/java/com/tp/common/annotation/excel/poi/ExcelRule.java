package com.tp.common.annotation.excel.poi;


/**
 * 
 * <pre>
 *  校验规则接口
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public interface ExcelRule<T> {
	
	/**
	 * 
	 * <pre>
	 *  实现对单元格内容的检查
	 *  如果内容不合法，则抛出ExcelContentInvalidException
	 * </pre>
	 *
	 * @param value
	 * @param columnName
	 * @param fieldName
	 * @throws ExcelContentInvalidException
	 */
	public void check(Object value, String columnName, String fieldName);
	
	/**
	 * 
	 * <pre>
	 *  内容过滤规则
	 *  在该方法中对内容进行修改，并返回修改后的对象
	 * </pre>
	 *
	 * @param value
	 * @param columnName
	 * @param fieldName
	 * @return
	 */
	public T filter(Object value, String columnName, String fieldName);
}