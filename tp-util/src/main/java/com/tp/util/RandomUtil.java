package com.tp.util;

import java.util.Random;

/**
 * 随机数工具类
 * @author zhuss
 * @2016年1月2日 下午4:36:11
 */
public class RandomUtil {

	/**
	 * 生成随机数
	 * @param iLen
	 * @param iType：随机数的类型： '0':表示仅获得数字随机数；'1'：表示仅获得字符随机数； '2'：表示获得数字字符混合随机数
	 * @return
	 */
	public static String createRadom(int iLen, int iType) {
		String strRandom = " ";// 随机字符串
		Random rnd = new Random();
		if (iLen < 0) {
			iLen = 5;
		}
		if ((iType > 2) || (iType < 0)) {
			iType = 2;
		}
		switch (iType) {
		case 0:
			for (int iLoop = 0; iLoop < iLen; iLoop++) {
				strRandom += Integer.toString(rnd.nextInt(10));
			}
			break;
		case 1:
			for (int iLoop = 0; iLoop < iLen; iLoop++) {
				strRandom += Integer.toString((35 - rnd.nextInt(10)), 36);
			}
			break;
		case 2:
			for (int iLoop = 0; iLoop < iLen; iLoop++) {
				strRandom += Integer.toString(rnd.nextInt(36), 36);
			}
			break;
		}
		return strRandom.trim();
	}
}
