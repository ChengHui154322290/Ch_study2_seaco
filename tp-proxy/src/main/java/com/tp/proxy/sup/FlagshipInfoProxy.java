package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.FlagshipInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.IFlagshipInfoService;
/**
 * 供应商-旗舰店设置代理层
 * @author szy
 *
 */
@Service
public class FlagshipInfoProxy extends BaseProxy<FlagshipInfo>{

	@Autowired
	private IFlagshipInfoService flagshipInfoService;

	@Override
	public IBaseService<FlagshipInfo> getService() {
		return flagshipInfoService;
	}
}
