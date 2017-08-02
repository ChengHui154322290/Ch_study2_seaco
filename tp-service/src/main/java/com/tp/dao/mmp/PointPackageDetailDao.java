package com.tp.dao.mmp;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.mmp.PointPackageDetail;

public interface PointPackageDetailDao extends BaseDao<PointPackageDetail> {

	void batchInsert(@Param("pointPackageDetailList") List<PointPackageDetail> pointPackageDetailList);
	
	List<PointPackageDetail> queryListByPointDetailId(Long pointDetailId);
}
