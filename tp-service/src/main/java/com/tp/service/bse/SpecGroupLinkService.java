package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.SpecGroupLinkDao;
import com.tp.model.bse.SpecGroupLink;
import com.tp.service.BaseService;
import com.tp.service.bse.ISpecGroupLinkService;

@Service
public class SpecGroupLinkService extends BaseService<SpecGroupLink> implements ISpecGroupLinkService {

	@Autowired
	private SpecGroupLinkDao specGroupLinkDao;
	
	@Override
	public BaseDao<SpecGroupLink> getDao() {
		return specGroupLinkDao;
	}

}
