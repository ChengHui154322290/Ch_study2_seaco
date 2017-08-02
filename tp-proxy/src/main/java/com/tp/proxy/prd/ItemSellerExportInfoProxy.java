package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemSellerExportInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemSellerExportInfoService;
/**
 * 商家平台-商家导出信息表代理层
 * @author szy
 *
 */
@Service
public class ItemSellerExportInfoProxy extends BaseProxy<ItemSellerExportInfo>{

	@Autowired
	private IItemSellerExportInfoService itemSellerExportInfoService;

	@Override
	public IBaseService<ItemSellerExportInfo> getService() {
		return itemSellerExportInfoService;
	}
}
