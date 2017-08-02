package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.SupplierLink;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.ISupplierLinkService;
/**
 * 供应商-联系人表代理层
 * @author szy
 *
 */
@Service
public class SupplierLinkProxy extends BaseProxy<SupplierLink>{

	@Autowired
	private ISupplierLinkService supplierLinkService;

	@Override
	public IBaseService<SupplierLink> getService() {
		return supplierLinkService;
	}
}
