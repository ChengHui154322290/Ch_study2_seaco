package com.tp.dao.bse;

import com.tp.common.dao.BaseDao;
import com.tp.model.bse.ImageInfo;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImageInfoDao extends BaseDao<ImageInfo> {

    List<ImageInfo> queryListWithId(@Param("id") Long id);

}
