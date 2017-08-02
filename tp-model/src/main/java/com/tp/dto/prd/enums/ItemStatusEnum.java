package com.tp.dto.prd.enums;

/**
 * 
 * <pre>
 * 商品状态枚举类
 * </pre>
 *
 * @author szy
 */
public enum ItemStatusEnum {

	OFFLINE("未上架", 0),

	ONLINE("已上架", 1),

	CANCEL("作废", 2);

	private String key;

	private Integer value;

	private ItemStatusEnum(String key, Integer value) {
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

	public static ItemStatusEnum getByValue(Integer value) {
		if(null==value){
			return null;
		}
		ItemStatusEnum[] values = ItemStatusEnum.values();
		for (ItemStatusEnum item : values) {
			if(item.getValue().intValue()==value.intValue()){
				return item;
			}
		}
		return null;
	}
}
