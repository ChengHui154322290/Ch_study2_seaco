package com.tp.dto.mmp.enums;

public enum ErrorCodeType {

	SUCCESS,// 0 成功
	LIMIT_AMOUNT,//1 限购数量出错
	STOCK_AMOUNT,//2 库存数量出错
	AREA,// 3 地区出错
	PLATFORM, //4 平台出错
	POLICY, //5 限购政策
	OVERDUE,//6 已经过期
	NOTSTART,//7 还没开始
	OTHER,//8 其他错误
	REGISTER_TIME,//9 注册时间不符合
	UID,// 10 限制相同用户购买
	UIP,// 11 限制相同uip购买
	MOBILE,//12 限制相同收货手机的购买
	LOCKED ,//13 被锁定
	AUTOCOUPON,//14自动发券
	GET_TOPIC_BY_ID,//15查询活动信息出错
	TOPIC;// 专场限制每个用户只能购买一次
	
	public static ErrorCodeType parse(int open) {
		for (ErrorCodeType status : ErrorCodeType.values()) {
			if ((byte) status.ordinal() == open) {
				return status;
			}
		}
		return null;
	}
}
