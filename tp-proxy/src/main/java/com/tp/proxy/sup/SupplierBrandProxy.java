package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.SupplierBrand;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.ISupplierBrandService;
/**
 * 供应商-品牌表代理层
 * @author szy
 *
 */
@Service
public class SupplierBrandProxy extends BaseProxy<SupplierBrand>{

	@Autowired
	private ISupplierBrandService supplierBrandService;

	@Override
	public IBaseService<SupplierBrand> getService() {
		return supplierBrandService;
	}
}
