package com.tp.dao.ord;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.Kuaidi100Express;

public interface Kuaidi100ExpressDao extends BaseDao<Kuaidi100Express> {

	Integer batchInsert(List<Kuaidi100Express> kuaidi100ExpressDOList);

	List<Kuaidi100Express> selectListBySubOrderCodeAndPackageNo(
			List<Kuaidi100Express> kuaidi100ExpressDOList);

	Integer deleteOldExpressInfo(@Param("code")Long code, @Param("packageNo")String packageNo);

	List<Kuaidi100Express> selectListByRejectNoAndPackageNo(
			List<Kuaidi100Express> kuaidi100ExpressDOList);

}
