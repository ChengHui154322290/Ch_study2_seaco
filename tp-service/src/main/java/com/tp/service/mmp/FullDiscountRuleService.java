package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.FullDiscountRuleDao;
import com.tp.model.mmp.FullDiscountRule;
import com.tp.service.BaseService;
import com.tp.service.mmp.IFullDiscountRuleService;

@Service
public class FullDiscountRuleService extends BaseService<FullDiscountRule> implements IFullDiscountRuleService {

	@Autowired
	private FullDiscountRuleDao fullDiscountRuleDao;
	
	@Override
	public BaseDao<FullDiscountRule> getDao() {
		return fullDiscountRuleDao;
	}

}
