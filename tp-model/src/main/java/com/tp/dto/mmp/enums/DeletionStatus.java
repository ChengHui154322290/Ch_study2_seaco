package com.tp.dto.mmp.enums;

/**
 * 专题功能删除状态
 * @author szy
 *
 */
public enum DeletionStatus {

	NORMAL,// 正常
	DELETED; // 删除
	
	public static DeletionStatus parse(int open) {
		for(DeletionStatus status: DeletionStatus.values()){
			if((byte)status.ordinal()==open){
				return status;
			}
		}
		return null;
	}

}
