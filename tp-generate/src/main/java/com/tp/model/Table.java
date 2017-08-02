package com.tp.model;

import java.io.Serializable;

public class Table implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7576500849057690028L;
	
	private String tableName;
	private String tableComment;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableComment() {
		return tableComment;
	}
	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

}
