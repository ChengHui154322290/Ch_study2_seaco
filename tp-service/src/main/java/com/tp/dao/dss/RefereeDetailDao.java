package com.tp.dao.dss;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.dss.RefereeDetail;

public interface RefereeDetailDao extends BaseDao<RefereeDetail> {
	public List<RefereeDetail> queryRefereeByDetailCode(@Param("detailCodeList")List<Long> detaiCodeList);
}
