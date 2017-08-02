package com.tp.util;

import java.util.TimeZone;

import com.thoughtworks.xstream.converters.basic.DateConverter;

/**
 * XStream EnhanceDateConverter
 * 
 * @author szy
 *
 */
public class EnhanceDateConverter extends DateConverter {

	public EnhanceDateConverter(String dateFormat) {
		
		super(dateFormat, null, TimeZone.getTimeZone("GMT+8"));
		
	}

}
