package com.tp.dto.mmp.enums;

/**
 * 促销专题进度
 * @author szy
 *
 */
public enum ProgressStatus {

	NotStarted,// 未开始NotStarted
	DOING,// 进行中
	FINISHED; //已结束
	
	public static ProgressStatus parse(int open) {
		for(ProgressStatus status: ProgressStatus.values()){
			if((byte)status.ordinal()==open){
				return status;
			}
		}
		return null;
	}

}
