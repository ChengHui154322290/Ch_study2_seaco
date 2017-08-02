package com.tp.dao.stg;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.stg.OutputOrder;

public interface OutputOrderDao extends BaseDao<OutputOrder> {

	/**
	 * 查询失败的指令
	 * @param maxTime
	 * 		最大失败次数
	 * @return
	 */
	List<OutputOrder> selectFailOutputOrder(@Param("maxTime")Integer maxTime,@Param("limitSize")Integer limitSize);
}
