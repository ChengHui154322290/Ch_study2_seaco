package com.tp.dto.ptm;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.common.vo.ord.SystemConstants;


/**
 * 
 * 
 * @author 项硕
 * @version 2015年6月1日 下午7:01:18
 */
public class DateTimeDeserializer extends JsonDeserializer<Date> {
	
	private static final Logger log = LoggerFactory.getLogger(DateTimeDeserializer.class);

	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String date = null;
		try {
			date = jp.getText().trim();
			return new SimpleDateFormat(SystemConstants.DEFAULT_DATE_PATTERN)
					.parse(date);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return ctxt.parseDate(date);
	}
	
}
