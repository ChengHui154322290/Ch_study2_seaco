package com.tp.service.sup;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.ContractAttachDao;
import com.tp.model.sup.ContractAttach;
import com.tp.service.BaseService;
import com.tp.service.sup.IContractAttachService;

@Service
public class ContractAttachService extends BaseService<ContractAttach> implements IContractAttachService {

	@Autowired
	private ContractAttachDao contractAttachDao;
	
	@Override
	public BaseDao<ContractAttach> getDao() {
		return contractAttachDao;
	}

    @Override
    public ContractAttach getContractAttachByContractId(final Long contractId) {
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("contractId", contractId);
    	return super.queryUniqueByParams(params);
    }
}
