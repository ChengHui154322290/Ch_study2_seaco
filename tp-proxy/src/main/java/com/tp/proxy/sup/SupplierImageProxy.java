package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.SupplierImage;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.ISupplierImageService;
/**
 * 供应商-(证件)图片信息表代理层
 * @author szy
 *
 */
@Service
public class SupplierImageProxy extends BaseProxy<SupplierImage>{

	@Autowired
	private ISupplierImageService supplierImageService;

	@Override
	public IBaseService<SupplierImage> getService() {
		return supplierImageService;
	}
}
