package com.tp.dao;

import java.util.List;
import java.util.Map;

import com.tp.model.Column;

public interface ColumnDao {

	/**
	 * 传入参数 tableSchema,tableName;
	 * @param params
	 * @return
	 */
	public List<Column> queryListByParams(Map<String,Object> params);
}
