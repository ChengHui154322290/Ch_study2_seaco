package com.tp.dao.cms;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.cms.SeoElement;

public interface SeoElementDao extends BaseDao<SeoElement> {

	int deleteByIds(List<Long> ids);

}
