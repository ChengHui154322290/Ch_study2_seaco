package com.tp.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.tp.datasource.ContextHolder;


public class XgDataSource extends AbstractRoutingDataSource {  
    @Override  
    protected synchronized Object determineCurrentLookupKey() {  
        String customerKey = ContextHolder.getCustomerKey();
		return customerKey;  
    }  
}  
