package com.tp.proxy.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.stg.OutputBackSku;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.stg.IOutputBackSkuService;
/**
 * 出库订单反馈sku信息列表
代理层
 * @author szy
 *
 */
@Service
public class OutputBackSkuProxy extends BaseProxy<OutputBackSku>{

	@Autowired
	private IOutputBackSkuService outputBackSkuService;

	@Override
	public IBaseService<OutputBackSku> getService() {
		return outputBackSkuService;
	}
}
