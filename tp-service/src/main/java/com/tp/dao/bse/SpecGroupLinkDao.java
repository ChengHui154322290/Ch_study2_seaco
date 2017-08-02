package com.tp.dao.bse;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.bse.SpecGroupLink;

public interface SpecGroupLinkDao extends BaseDao<SpecGroupLink> {

	void insertByList(List<SpecGroupLink> specGroupLinks);

}
