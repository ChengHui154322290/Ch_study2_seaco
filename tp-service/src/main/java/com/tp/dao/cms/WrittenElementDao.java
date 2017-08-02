package com.tp.dao.cms;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.cms.WrittenElement;

public interface WrittenElementDao extends BaseDao<WrittenElement> {

	int deleteByIds(List<Long> ids);

}
