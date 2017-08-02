package com.tp.dao.mmp;

import com.tp.common.dao.BaseDao;
import com.tp.model.mmp.OlgbHsConfig;

import java.util.List;

public interface OlgbHsConfigDao extends BaseDao<OlgbHsConfig> {


    int updateStatusByIds(List<Long> ids);

}
