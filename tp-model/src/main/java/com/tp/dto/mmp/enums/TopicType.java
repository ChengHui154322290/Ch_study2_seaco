package com.tp.dto.mmp.enums;

/**
 * 专题类型 
 * @author szy
 *
 */
public enum TopicType {

	ALL,    // 全部 0 
	SINGLE, // 单品团  1
	BRAND,  //品牌团  2
	THEME,  //主题团 3
	TUN, //海淘 4
	SUPPLIER_SHOP; //商家店铺 5
	
	public static TopicType parse(int open) {
		for (TopicType status : TopicType.values()) {
			if ((byte) status.ordinal() == open) {
				return status;
			}
		}
		return null;
	}

}
