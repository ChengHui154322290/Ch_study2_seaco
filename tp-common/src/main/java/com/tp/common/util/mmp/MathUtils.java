package com.tp.common.util.mmp;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 数学运算
 * 
 * @author szy
 * @version 0.0.1
 */
public class MathUtils {

	/**
	 * 加法
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static <M extends Number, N extends Number> BigDecimal add(M num1, N num2) {
		return toBigDecimal(num1).add(toBigDecimal(num2));
	}

	/**
	 * 减法
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static <M extends Number, N extends Number> BigDecimal subtract(M num1, N num2) {
		return toBigDecimal(num1).subtract(toBigDecimal(num2));
	}

	/**
	 * 乘法
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static <M extends Number, N extends Number> BigDecimal multiply(M num1, N num2) {
		return toBigDecimal(num1).multiply(toBigDecimal(num2));
	}

	/**
	 * 除法
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static <M extends Number, N extends Number> BigDecimal divide(M num1, N num2) {
		return toBigDecimal(num1).divide(toBigDecimal(num2), 3, RoundingMode.HALF_UP);
	}
	
	/**
	 * 格式化成价格格式（保留2位小数，四舍五入），例：1.99 、 2.00
	 * 
	 * @return
	 */
	public static BigDecimal formatToPrice(BigDecimal num) {
		return num.setScale(2, RoundingMode.HALF_UP);
	}

	private static <N extends Number> BigDecimal toBigDecimal(N num) {
		if (num instanceof Integer) {
			return new BigDecimal((Integer) num);
		} else if (num instanceof Double) {
			return new BigDecimal((Double) num);
		} else if (num instanceof Float) {
			return new BigDecimal((Float) num);
		} else if (num instanceof Long) {
			return new BigDecimal((Long) num);
		} else if (num instanceof Short) {
			return new BigDecimal((Short) num);
		} else {
			return new BigDecimal(num.doubleValue());
		}
	}
}
