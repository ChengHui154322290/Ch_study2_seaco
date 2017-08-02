/**
 * 
 */
package com.tp.dto.mmp.enums;

/**
 * @author szy
 *
 */
public enum SalesPartten {
	NORMAL(1), // 不限
	FLAGSHIP_STORE(2),//旗舰店
	XG_STORE(3),//西客商城商城
	YTP(4),//西客商城
	FLASH(5),//闪购
	TIME_LIMIT(6),//秒杀
	GROUP_BUY(7),//团购
	DISTRIBUTION(8),//分销
	OFF_LINE_GROUP_BUY(9);//线下团购券
	private Integer value;

	private SalesPartten(Integer value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}

	public static SalesPartten parse(int open) {
		for(SalesPartten partten: SalesPartten.values()){
			if((byte)partten.ordinal()==open){
				return partten;
			}
		}
		return null;
	}
}
