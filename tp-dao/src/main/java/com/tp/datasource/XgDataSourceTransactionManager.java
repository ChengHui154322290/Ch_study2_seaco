package com.tp.datasource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;


public class XgDataSourceTransactionManager extends DataSourceTransactionManager{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2671937755696901181L;

	@Override

	   protected void doCleanupAfterCompletion(Object transaction) {

	      super.doCleanupAfterCompletion(transaction);

	      ContextHolder.clearCustomerKey();

	   }

	}

