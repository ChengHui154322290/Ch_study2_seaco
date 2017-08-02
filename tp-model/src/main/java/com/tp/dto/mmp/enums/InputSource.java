/**
 * 
 */
package com.tp.dto.mmp.enums;

/**
 * @author szy
 *
 */
public enum InputSource {
	MANUAL, PASTE, COPY;

	public static InputSource parse(int open) {
		for (InputSource inputSource : InputSource.values()) {
			if ((byte) inputSource.ordinal() == open) {
				return inputSource;
			}
		}
		return null;
	}
}
