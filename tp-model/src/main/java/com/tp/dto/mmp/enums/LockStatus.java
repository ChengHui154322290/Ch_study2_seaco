/**
 * 
 */
package com.tp.dto.mmp.enums;

/**
 * @author szy
 *
 */
public enum LockStatus {
	UNLOCK,LOCK;
	
	public static LockStatus parse(int open) {
		for (LockStatus status : LockStatus.values()) {
			if ((byte) status.ordinal() == open) {
				return status;
			}
		}
		return null;
	}
}
