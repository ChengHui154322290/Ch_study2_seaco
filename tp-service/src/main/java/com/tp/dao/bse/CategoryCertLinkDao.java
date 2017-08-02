package com.tp.dao.bse;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.bse.CategoryCertLink;

public interface CategoryCertLinkDao extends BaseDao<CategoryCertLink> {

	void batchInsert(List<CategoryCertLink> baseCategoryCertLinks);

}
