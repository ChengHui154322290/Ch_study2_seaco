package com.tp.service.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.cmt.BeViewpointDao;
import com.tp.model.cmt.BeViewpoint;
import com.tp.service.BaseService;
import com.tp.service.cmt.IBeViewpointService;

@Service
public class BeViewpointService extends BaseService<BeViewpoint> implements IBeViewpointService {

	@Autowired
	private BeViewpointDao beViewpointDao;
	
	@Override
	public BaseDao<BeViewpoint> getDao() {
		return beViewpointDao;
	}

}
