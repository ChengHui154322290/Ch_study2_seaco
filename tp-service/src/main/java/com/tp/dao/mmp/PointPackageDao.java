package com.tp.dao.mmp;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.mmp.PointPackage;

public interface PointPackageDao extends BaseDao<PointPackage> {

	public PointPackage queryPointPackageByMemberIdAndPackageTime(@Param("memberId") Long memberId,@Param("packageTime") Integer packageTime);

	public Integer updateSubPointById(PointPackage pointPackage);
	
}
