package com.tp.m.enums;

/**
 * 商品枚举
 * @author zhuss
 * @2016年1月2日 下午4:10:32
 */
public interface ProductEnum {

	public enum Status{
		NORMAL("1","正常"),
		ITEM_OUT_OF_STOCK("2","已抢光"),//库存为0
		ITEM_UNDERCARRIAGE("3","已下架"),//有商品详情
		ITEM_NO_USE("4","已作废"),//没有商品详情
		TOPIC_NO_START("5","活动未开始"),//有商品详情
		TOPIC_NO_END("6","活动已结束"),
		TOPIC_BACKORDERED("7","备货中");//有商品详情
		public String code;
		
	    public String desc;

		private Status(String code, String desc) {
			this.code = code;
			this.desc = desc;
		} 
		
	}
}
