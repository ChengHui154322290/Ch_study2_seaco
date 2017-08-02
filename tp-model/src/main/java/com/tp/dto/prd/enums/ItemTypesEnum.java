package com.tp.dto.prd.enums;

/**
 * 
 * <pre>
 * 商品类型枚举
 * </pre>
 *
 * @author szy
 */
public enum ItemTypesEnum {

	NORMAL("正常商品", 1),

	DUMMY("服务商品", 2),

	TWOHAND("二手商品", 3),

	ANNOUNCE("报废商品", 4);
	
	private String key;

	private Integer value;

	private ItemTypesEnum(String key, Integer value) {
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


	public static ItemTypesEnum getByValue(Integer itemType) {
		if(null==itemType){
			return null;
		}
		ItemTypesEnum[] values = ItemTypesEnum.values();
		for (ItemTypesEnum itemTypesEnum : values) {
			if(itemTypesEnum.getValue().intValue()==itemType.intValue()){
				return itemTypesEnum;
			}
		}
		return null;
	}

}
