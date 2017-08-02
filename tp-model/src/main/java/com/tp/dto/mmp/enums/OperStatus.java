/**
 * 
 */
package com.tp.dto.mmp.enums;

/**
 * @author szy
 *
 */
public enum OperStatus {
	MODIFY, NEW, DELETE, NEW_DELETE, RECOVER, TERMINATE;

	public static OperStatus parse(Integer open) {
		if (open == null) return null;
		for (OperStatus status : OperStatus.values()) {
			if ((byte) status.ordinal() == open) {
				return status;
			}
		}
		return null;
	}
}
