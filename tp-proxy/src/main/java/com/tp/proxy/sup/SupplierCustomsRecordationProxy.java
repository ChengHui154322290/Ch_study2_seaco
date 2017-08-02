package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.SupplierCustomsRecordation;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.ISupplierCustomsRecordationService;
/**
 * 供应商海关备案表代理层
 * @author szy
 *
 */
@Service
public class SupplierCustomsRecordationProxy extends BaseProxy<SupplierCustomsRecordation>{

	@Autowired
	private ISupplierCustomsRecordationService supplierCustomsRecordationService;

	@Override
	public IBaseService<SupplierCustomsRecordation> getService() {
		return supplierCustomsRecordationService;
	}
}
