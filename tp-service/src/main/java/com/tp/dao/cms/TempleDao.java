package com.tp.dao.cms;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.cms.Temple;

public interface TempleDao extends BaseDao<Temple> {

	int deleteByIds(List<Long> ids);

	long selectIsExists(List<Long> ids, boolean b);

}
