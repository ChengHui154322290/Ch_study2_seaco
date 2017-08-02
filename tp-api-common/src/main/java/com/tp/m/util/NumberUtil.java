package com.tp.m.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 数字运算工具类
 * @author zhuss
 * @2016年1月4日 下午8:17:56
 */
public class NumberUtil {
	public static final Double DOUBLE_ZERO = 0d;  
	private static final int DEF_DIV_SCALE = 2;  // 默认除法运算精度
	private static final DecimalFormat df = new DecimalFormat("0.0");

	private NumberUtil() {
	}

	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(Double v1, Double v2) {
		if(null == v1) v1 = 0d ;
		if(null == v2) v2 = 0d ;
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(Double v1, Double v2) {
		if(null == v1) v1 = 0d ;
		if(null == v2) v2 = 0d ;
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 格式化为一位小数
	 * @param decimal
	 * @return
	 */
	public static String decimalFormat(Double decimal){
		if(decimal == null){
			return null;
		}
		return df.format(decimal);
	}
	/**
	 * 格式化为一位小数
	 * @param decimal
	 * @return
	 */
	public static String decimalFormat(Double decimal,RoundingMode roundingMode){
		if(decimal == null){
			return "";
		}
		if(roundingMode == null){
			roundingMode = RoundingMode.HALF_UP;
		}
		df.setRoundingMode(roundingMode);
		return df.format(decimal);
	}

	/**
	 * 计算商品折扣
	 * roundingMode 采用 RoundingMode.DOWN
	 * 折扣小于0.1时返回0.1
	 * @param topicPrice
	 * @param salePrice
	 * @return
	 */
	public static String  calDiscount(double topicPrice,double salePrice){
		if(salePrice == 0){
			return "";
		}
		double disCount = div(NumberUtil.mul(topicPrice, 10), salePrice, 6, RoundingMode.DOWN);
		if( 0 < disCount && disCount < 0.1){
			disCount = 0.1;
		}
		return decimalFormat(disCount, RoundingMode.DOWN);
	}
	
	/**
	 * 计算商品价格
	 * @param unitPrice:商品单价
	 * @param count:商品数量
	 * @return
	 */
	public static String calPrice(String unitPrice,String count){
		if(StringUtil.isBlank(unitPrice) || StringUtil.isBlank(count)) return null;
		return sfwr(Double.parseDouble(unitPrice)*Integer.parseInt(count));
	}
	
	/**
	 * 四舍五入保留2位小数
	 * @param d
	 * @return
	 */
	public static String sfwr(Double d){
		if(null == d) return null;
		DecimalFormat df = new DecimalFormat("######0.00");
		return df.format(d);
	}

	/**
	 * 提供（相对）精确的除法运算。
	 * scale参数指定精度
	 * roundingMode 指定舍入方式
	 *
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @param roundingMode  为null时则为RoundingMode.HALF_UP
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale,RoundingMode roundingMode) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		if(roundingMode == null){
			roundingMode = RoundingMode.HALF_UP;
		}
		return b1.divide(b2, scale, roundingMode).doubleValue();
	}
	
}
