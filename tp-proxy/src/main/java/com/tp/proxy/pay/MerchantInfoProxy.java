package com.tp.proxy.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.pay.MerchantInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.pay.IMerchantInfoService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class MerchantInfoProxy extends BaseProxy<MerchantInfo>{

	@Autowired
	private IMerchantInfoService merchantInfoService;

	@Override
	public IBaseService<MerchantInfo> getService() {
		return merchantInfoService;
	}
}
