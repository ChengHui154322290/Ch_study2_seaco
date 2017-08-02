package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.PointSignConfig;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IPointSignConfigService;
/**
 * 签到积分奖励配置表代理层
 * @author szy
 *
 */
@Service
public class PointSignConfigProxy extends BaseProxy<PointSignConfig>{

	@Autowired
	private IPointSignConfigService pointSignConfigService;

	@Override
	public IBaseService<PointSignConfig> getService() {
		return pointSignConfigService;
	}
}
