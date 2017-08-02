package com.tp.common.util.mmp;

import java.util.Random;
import java.util.UUID;


public class DataUtil {

	/**判空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNvl(Object str) {
		if(str instanceof String){
			//字符串出来
			if(str == null || "".equals(str)){
				return true;
			}else{
				return false;
			}
		}else{
			//除字符串之外的值
			if(str == null){
				return true;
			}else{
				return false;
			}
		}
		
	}
	
	/**随机生成16位的数字
	 * 
	 * @param str
	 * @return
	 */
	public static String radomCode() {
		UUID uuid = UUID.randomUUID();
	    String uuidStr = uuid.toString().replace("-", "").toUpperCase();
	    Random random = new Random();
	    int num = random.nextInt(16);
	    uuidStr = uuidStr.substring(num, 16+num);
	    return uuidStr;
	}
	
}
