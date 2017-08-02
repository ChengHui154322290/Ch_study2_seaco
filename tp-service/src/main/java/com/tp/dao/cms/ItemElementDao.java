package com.tp.dao.cms;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.cms.ItemElement;

public interface ItemElementDao extends BaseDao<ItemElement> {

	int deleteByIds(List<Long> ids);

}
