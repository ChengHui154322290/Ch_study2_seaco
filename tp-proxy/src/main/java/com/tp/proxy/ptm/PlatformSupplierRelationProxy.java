package com.tp.proxy.ptm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ptm.PlatformSupplierRelation;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ptm.IPlatformSupplierRelationService;
/**
 * 开放平台供应商关联表代理层
 * @author szy
 *
 */
@Service
public class PlatformSupplierRelationProxy extends BaseProxy<PlatformSupplierRelation>{

	@Autowired
	private IPlatformSupplierRelationService platformSupplierRelationService;

	@Override
	public IBaseService<PlatformSupplierRelation> getService() {
		return platformSupplierRelationService;
	}
}
