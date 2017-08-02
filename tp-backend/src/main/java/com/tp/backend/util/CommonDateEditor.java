package com.tp.backend.util;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

public class CommonDateEditor extends PropertyEditorSupport{
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (!StringUtils.hasText(text)) {
			setValue(null);
		}
		else{
			String source = text.trim();
			 if(source.matches("^\\d{4}-\\d{1,2}$")){
				 setValue(parseDate(source, "yyyy-MM"));
			 }
			 else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
				 setValue(parseDate(source, "yyyy-MM-dd"));
			 }
			 else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")){
				 setValue(parseDate(source, "yyyy-MM-dd HH:mm"));
			 }
			 else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")){
				 setValue(parseDate(source, "yyyy-MM-dd HH:mm:ss"));
			 }
			 else{
				 throw new IllegalArgumentException("Could not parse date: '" + source + "'");
			 }
		}
	}
	
	private Date parseDate(String text, String pattern) throws IllegalArgumentException {
		Date date = null;
		try{
			date = new SimpleDateFormat(pattern).parse(text);
		}catch(ParseException ex){
			throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
		}
		return date;
	}
}
