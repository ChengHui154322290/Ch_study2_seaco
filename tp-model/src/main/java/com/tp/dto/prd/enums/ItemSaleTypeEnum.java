package com.tp.dto.prd.enums;

/**
 * 
 * <pre>
 * 商品销售类型枚举
 * </pre>
 *
 * @author szy
 */
public enum ItemSaleTypeEnum {
	
	SEAGOOR("西客商城" ,0),
	SELLER("商家",1);
	
	
	private String key;

	private Integer value;

	private ItemSaleTypeEnum(String key, Integer value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}


	public void setValue(Integer value) {
		this.value = value;
	}


	public Integer getValue() {
		return value;
	}

	public static ItemSaleTypeEnum getByValue(Integer value) {
		if(null==value){
			return null;
		}
		ItemSaleTypeEnum[] values = ItemSaleTypeEnum.values();
		for (ItemSaleTypeEnum item : values) {
			if(item.getValue().intValue()==value.intValue()){
				return item;
			}
		}
		return null;
	}
	
}
