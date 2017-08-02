package com.tp.dto.mmp.enums;

/**
 * 专题时间，有效期
 * @author szy
 *
 */
public enum LastingType {

	ALLTIME,// 长期有效
	SOMETIME; // 固定时间
	
	public static LastingType parse(int open) {
		for(LastingType status: LastingType.values()){
			if((byte)status.ordinal()==open){
				return status;
			}
		}
		return null;
	}

}
