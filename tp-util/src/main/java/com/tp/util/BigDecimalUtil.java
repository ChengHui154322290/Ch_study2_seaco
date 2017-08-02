package com.tp.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtil{

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
		return toBigDecimal(num1).divide(toBigDecimal(num2), 2, RoundingMode.HALF_UP);
	}
	public static <M extends Number, N extends Number> BigDecimal divide(M num1, N num2,Integer num) {
		return toBigDecimal(num1).divide(toBigDecimal(num2), num, RoundingMode.HALF_UP);
	}
	
	/**
	 * 格式化成价格格式（保留2位小数，四舍五入），例：1.99 、 2.00
	 * 
	 * @return
	 */
	public static BigDecimal formatToPrice(BigDecimal num) {
		return null == num ? BigDecimal.ZERO : num.setScale(2, RoundingMode.HALF_UP);
	}
	
	public static Double toPrice(BigDecimal num){
		return formatToPrice(num).doubleValue();
	}
	private static <N extends Number> BigDecimal toBigDecimal(N num) {
		return null == num ? BigDecimal.ZERO : new BigDecimal(num.toString());
	}
}
