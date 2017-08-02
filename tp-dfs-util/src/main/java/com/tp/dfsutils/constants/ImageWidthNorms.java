package com.tp.dfsutils.constants;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * 
 * @describe
 * @author 叶礼锋
 * 
 *         2014-12-18 下午1:48:11
 */
@SuppressWarnings(value = { "unused", "deprecation", "serial", "unchecked", "static-access", "rawtypes" })
public enum ImageWidthNorms {

	INSTANCE;

	/** 图片大小规格 暂定11个级别（注意这里的命名必须是WIDTH开头，会涉及到反射） */
	public static final int WIDTHD720 = 720;

	public static final int WIDTHDEFAULT = 640;

	public static final int WIDTHD640 = WIDTHDEFAULT;

	public static final int WIDTH0 = 620;

	public static final int WIDTH620 = WIDTH0;

	public static final int WIDTH610 = 610;

	public static final int WIDTH576 = 576;

	public static final int WIDTH520 = 520;

	public static final int WIDTH450 = 450;

	public static final int WIDTH1 = 420;

	public static final int WIDTH420 = WIDTH1;

	public static final int WIDTH378 = 378;

	public static final int WIDTH375 = 375;

	public static final int WIDTH2 = 310;

	public static final int WIDTH310 = WIDTH2;

	public static final int WIDTH278 = 278;

	public static final int WIDTH275 = 275;

	public static final int WIDTH264 = 264;

	public static final int WIDTH220 = 220;

	public static final int WIDTH3 = 218;

	public static final int WIDTH218 = WIDTH3;

	public static final int WIDTH4 = 200;

	public static final int WIDTH200 = WIDTH4;

	public static final int WIDTH5 = 160;

	public static final int WIDTH160 = WIDTH5;

	public static final int WIDTH142 = 142;

	public static final int WIDTH140 = 140;

	public static final int WIDTH123 = 123;

	public static final int WIDTH6 = 120;

	public static final int WIDTH120 = WIDTH6;

	public static final int WIDTH100 = 100;

	public static final int WIDTH7 = 80;
	
	public static final int WIDTH80 = WIDTH7;

	public static final int WIDTH65 = 65;

	public static final int WIDTH8 = 60;
	
	public static final int WIDTH60 = WIDTH8;

	public static final int WIDTH9 = 40;
	
	public static final int WIDTH40 = WIDTH9;

	public static final int WIDTH10 = 30;
	
	public static final int WIDTH30 = WIDTH10;

	public int[] widths = null;

	ImageWidthNorms() {
		if (widths == null) {
			Field[] field = ImageWidthNorms.class.getDeclaredFields();
			if (field != null) {
				widths = new int[field.length];
				for (int i = 0; i < field.length; i++) {
					if (field[i].getType() == int.class && field[i].getName().matches("^WIDTH.+")) {
						try {
							widths[i] = (Integer) field[i].get(null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				Arrays.sort(widths);
			}
		}
	}

	/**
	 * 判断key是否存在于标准中
	 * 
	 * @param key
	 * @return
	 */
	public boolean contains(int key) {
		return Arrays.binarySearch(widths, key) >= 0;
	}
}
