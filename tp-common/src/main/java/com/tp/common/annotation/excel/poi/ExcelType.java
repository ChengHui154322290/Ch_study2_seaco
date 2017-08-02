package com.tp.common.annotation.excel.poi;

/**
 * 
 * <pre>
 *   功能说明： 扩展字段类型抽象父类
 *   参数说明：
 * 	      泛型量必须与子类类名相同！！！！
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public abstract class ExcelType<T> {
	public abstract T parseValue(String value);

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}