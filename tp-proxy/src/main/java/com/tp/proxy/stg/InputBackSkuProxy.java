package com.tp.proxy.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.stg.InputBackSku;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.stg.IInputBackSkuService;
/**
 * 入库单反馈
代理层
 * @author szy
 *
 */
@Service
public class InputBackSkuProxy extends BaseProxy<InputBackSku>{

	@Autowired
	private IInputBackSkuService inputBackSkuService;

	@Override
	public IBaseService<InputBackSku> getService() {
		return inputBackSkuService;
	}
}
