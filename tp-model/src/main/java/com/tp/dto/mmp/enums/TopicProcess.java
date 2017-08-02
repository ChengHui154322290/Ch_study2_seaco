/**
 * 
 */
package com.tp.dto.mmp.enums;

/**
 * @author szy
 *
 */
public enum TopicProcess {
	NOTSTART, // 未开始
	PROCESSING,  // 进行中
	ENDING; //已结束

	public static ProgressStatus parse(int open) {
		for (ProgressStatus status : ProgressStatus.values()) {
			if ((byte) status.ordinal() == open) {
				return status;
			}
		}
		return null;
	}
}
