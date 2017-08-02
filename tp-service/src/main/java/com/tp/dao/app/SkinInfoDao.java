package com.tp.dao.app;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.dto.prd.InfoDetailDto;
import com.tp.model.app.SkinInfo;

public interface SkinInfoDao extends BaseDao<SkinInfo> {
	List<SkinInfo> selectSkinNew(@Param("id")Long id,@Param("nowTime")Date nowTime,@Param("status")int status);
}
