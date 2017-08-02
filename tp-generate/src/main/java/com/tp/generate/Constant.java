package com.tp.generate;

import java.util.HashMap;
import java.util.Map;

public final class Constant {
	public final static Map<String,String> COLUMN_PROPERTY_TYPE_MAP = new HashMap<String,String>();
	
	static{
		COLUMN_PROPERTY_TYPE_MAP.put("date","Date");
		COLUMN_PROPERTY_TYPE_MAP.put("time","Date");
		COLUMN_PROPERTY_TYPE_MAP.put("datetime","Date");
		COLUMN_PROPERTY_TYPE_MAP.put("timestamp","Date");
		COLUMN_PROPERTY_TYPE_MAP.put("tinyint","Integer");
		COLUMN_PROPERTY_TYPE_MAP.put("smallint","Integer");
		COLUMN_PROPERTY_TYPE_MAP.put("mediumint","Integer");
		COLUMN_PROPERTY_TYPE_MAP.put("int","Integer");
		COLUMN_PROPERTY_TYPE_MAP.put("bigint","Long");
		COLUMN_PROPERTY_TYPE_MAP.put("float","Float");
		COLUMN_PROPERTY_TYPE_MAP.put("double","Double");
		COLUMN_PROPERTY_TYPE_MAP.put("decimal","Double");
		COLUMN_PROPERTY_TYPE_MAP.put("char","String");
		COLUMN_PROPERTY_TYPE_MAP.put("varchar","String");
		COLUMN_PROPERTY_TYPE_MAP.put("tinytext","String");
		COLUMN_PROPERTY_TYPE_MAP.put("text","String");
		COLUMN_PROPERTY_TYPE_MAP.put("mediumtext","String");
		COLUMN_PROPERTY_TYPE_MAP.put("longtext","String");
	}
}
