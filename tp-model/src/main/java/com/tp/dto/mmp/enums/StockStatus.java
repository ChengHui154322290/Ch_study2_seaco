/**
 * 
 */
package com.tp.dto.mmp.enums;

/**
 * @author szy
 *
 */
public enum StockStatus {
	EMPTY, HAS_STOCK;

	public static StockStatus parse(int open) {
		for (StockStatus status : StockStatus.values()) {
			if ((byte) status.ordinal() == open) {
				return status;
			}
		}
		return null;
	}
}
