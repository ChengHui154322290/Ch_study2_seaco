package com.tp.datasource;

public class ContextHolder {
	
	public enum DATA_SOURCE_KEY{
		SLAVE_SALE_ORDER_DATA_SOURCE,
		MASTER_SALE_ORDER_DATA_SOURCE
	}
	
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setCustomerKey(DATA_SOURCE_KEY data_source_key) {
		contextHolder.set(data_source_key.name());
	}

	public static String getCustomerKey() {
		String string = contextHolder.get();
		ContextHolder.clearCustomerKey();
		return string;
	}

	public static void clearCustomerKey() {
		contextHolder.remove();
	}

}