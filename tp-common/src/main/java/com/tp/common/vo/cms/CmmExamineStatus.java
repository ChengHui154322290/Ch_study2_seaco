/**
 * 
 */
package com.tp.common.vo.cms;

/**
 * @author szy
 *
 */
public enum CmmExamineStatus {
	WAIT_EXAMINE(1, "待审核"),
	APPROVE(2, "审核通过"),
	REFUSE(3, "审核不通过"),
	TERMINATE(4, "终止");
	
	private int type;
	private String description;
	
	private CmmExamineStatus(int type, String description) {
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
