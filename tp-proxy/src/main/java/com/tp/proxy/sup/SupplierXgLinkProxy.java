package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.SupplierXgLink;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.ISupplierXgLinkService;
/**
 * 供应商-西客对接人信息表代理层
 * @author szy
 *
 */
@Service
public class SupplierXgLinkProxy extends BaseProxy<SupplierXgLink>{

	@Autowired
	private ISupplierXgLinkService supplierXgLinkService;

	@Override
	public IBaseService<SupplierXgLink> getService() {
		return supplierXgLinkService;
	}
}
