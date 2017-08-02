package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.SupplierWarehouse;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.ISupplierWarehouseService;
/**
 * 供应商-仓库及配送信息表代理层
 * @author szy
 *
 */
@Service
public class SupplierWarehouseProxy extends BaseProxy<SupplierWarehouse>{

	@Autowired
	private ISupplierWarehouseService supplierWarehouseService;

	@Override
	public IBaseService<SupplierWarehouse> getService() {
		return supplierWarehouseService;
	}
}
