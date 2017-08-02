package com.tp.dao.prd;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.prd.ItemCode;

public interface ItemCodeDao extends BaseDao<ItemCode> {

	Integer updateCode(@Param("code")String code);

}
