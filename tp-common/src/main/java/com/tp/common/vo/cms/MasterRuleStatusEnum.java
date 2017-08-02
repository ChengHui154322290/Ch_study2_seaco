package com.tp.common.vo.cms;


/**
 * 达人计划规则状态
 * @author szy
 *
 */
public enum MasterRuleStatusEnum {

	EDIT(1, "编辑中"),  			//1编辑中
	EXAMINEING(2, "审核中"), 	//2审核中 
	CANCEL(3,"已取消"),   		//3已取消 
	EXAMINEEND(4,"审核通过"),     //4审核通过
	REJECT(5,"已驳回"),  		//5已驳回
	STOP(6,"已终止");  			//6已终止
	private int type;
	private String description;
	
	private MasterRuleStatusEnum(int type, String description) {
		this.type = type;
		this.description = description;
	}
	
	public int getValue() {
		return this.type;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public static MasterRuleStatusEnum parse(int open) {
		for (MasterRuleStatusEnum status : MasterRuleStatusEnum.values()) {
			if ((byte) status.ordinal()+1 == open) {
				return status;
			}
		} 
		return null;
	}
	
}
