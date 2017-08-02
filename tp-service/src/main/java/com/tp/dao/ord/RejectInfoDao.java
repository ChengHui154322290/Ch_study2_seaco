package com.tp.dao.ord;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.RejectInfo;
import com.tp.query.ord.RejectQuery;

public interface RejectInfoDao extends BaseDao<RejectInfo> {

	List<RejectInfo> selectListByRejectNoAndPackageNo(@Param("rejectCode")Long rejectCode,@Param("packageNo")String packageNo);

	Integer updatePostKuaidi100(RejectInfo rejectInfo);

	Integer batchUpdatePostKuaidi100(List<RejectInfo> rejectInfoDOList);

	List<RejectInfo> queryNotSuccessPostKuaidi100List(RejectInfo rejectInfo);

	Integer queryPageListByRejectQueryCount(Map<String, Object> params);

	List<RejectInfo> queryPageListByRejectQuery(RejectQuery rejectQuery);

	Integer updateForAudit(RejectInfo rejectInfo);

	List<RejectInfo> selectByRejectNo(List<Long> rejectNos);
	
	// by zhs 	
	Integer queryPageListByRejectQueryCount_DistinctByOrdercode(Map<String, Object> params);
	
	List<RejectInfo> queryPageListByRejectQuery_DistinctByOrdercode(RejectQuery rejectQuery);
}
