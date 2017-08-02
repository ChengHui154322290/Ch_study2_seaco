package com.tp.proxy.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.stg.InputBack;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.stg.IInputBackService;
/**
 * 标杆系统入库单反馈
代理层
 * @author szy
 *
 */
@Service
public class InputBackProxy extends BaseProxy<InputBack>{

	@Autowired
	private IInputBackService inputBackService;

	@Override
	public IBaseService<InputBack> getService() {
		return inputBackService;
	}
}
