package com.tp.proxy.mem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mem.PointRuleConfig;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mem.IPointRuleConfigService;
/**
 * 积分规则配置表 代理层
 * @author szy
 *
 */
@Service
public class PointRuleConfigProxy extends BaseProxy<PointRuleConfig>{

	@Autowired
	private IPointRuleConfigService pointRuleConfigService;

	@Override
	public IBaseService<PointRuleConfig> getService() {
		return pointRuleConfigService;
	}
}
