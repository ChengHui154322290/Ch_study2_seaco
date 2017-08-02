package com.tp.proxy.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.stg.OutputBack;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.stg.IOutputBackService;
/**
 * 出库订单反馈代理层
 * @author szy
 *
 */
@Service
public class OutputBackProxy extends BaseProxy<OutputBack>{

	@Autowired
	private IOutputBackService outputBackService;

	@Override
	public IBaseService<OutputBack> getService() {
		return outputBackService;
	}
}
