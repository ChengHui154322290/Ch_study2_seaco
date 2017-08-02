package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.Sequence;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.ISequenceService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class SequenceProxy extends BaseProxy<Sequence>{

	@Autowired
	private ISequenceService sequenceService;

	@Override
	public IBaseService<Sequence> getService() {
		return sequenceService;
	}
}
