package com.tp.common.vo.cms;


/**
 * 达人计划进度
 * @author szy
 *
 */
public enum ProgessEnum {

	NOSTART(1, "未开始"),  			//1未开始
	RECRUITING(2, "招募中"), 		//2招募中 
	PASSED(3,"进行中"),   			//3进行中 
	END(4,"已结束"),     			//4已结束
	STOP(5,"已终止"); 				//5已终止
	
	private int type;
	private String description;
	
	private ProgessEnum(int type, String description) {
		this.type = type;
		this.description = description;
	}
	
	public int getValue() {
		return this.type;
	}
	
	public String getDescription() {
		return this.description;
	}
	
}
