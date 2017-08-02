package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.FullDiscountRule;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IFullDiscountRuleService;
/**
 * 优惠政策规则代理层
 * @author szy
 *
 */
@Service
public class FullDiscountRuleProxy extends BaseProxy<FullDiscountRule>{

	@Autowired
	private IFullDiscountRuleService fullDiscountRuleService;

	@Override
	public IBaseService<FullDiscountRule> getService() {
		return fullDiscountRuleService;
	}
}
