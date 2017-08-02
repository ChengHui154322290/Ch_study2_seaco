package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.SequenceCode;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.ISequenceCodeService;
/**
 * 采购管理-编号表代理层
 * @author szy
 *
 */
@Service
public class SequenceCodeProxy extends BaseProxy<SequenceCode>{

	@Autowired
	private ISequenceCodeService sequenceCodeService;

	@Override
	public IBaseService<SequenceCode> getService() {
		return sequenceCodeService;
	}
}
