package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.SequenceDao;
import com.tp.model.sup.Sequence;
import com.tp.service.BaseService;
import com.tp.service.sup.ISequenceService;

@Service
public class SequenceService extends BaseService<Sequence> implements ISequenceService {

	@Autowired
	private SequenceDao sequenceDao;
	
	@Override
	public BaseDao<Sequence> getDao() {
		return sequenceDao;
	}
}
