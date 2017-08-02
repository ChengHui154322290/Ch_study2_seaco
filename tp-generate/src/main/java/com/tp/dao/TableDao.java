package com.tp.dao;

import java.util.List;
import java.util.Map;

import com.tp.model.Table;

public interface TableDao {

	/**
	 * 传入参数 schema,notInTable List,inTable List;
	 * @param params
	 * @return
	 */
	public List<Table> queryListBySchema(Map<String,Object> params);
}
