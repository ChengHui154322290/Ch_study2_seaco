package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.SupplierUploadLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.ISupplierUploadLogService;
/**
 * 供应商-大文件上传记录表(如产品质量检验合格证明或质量检测报告)代理层
 * @author szy
 *
 */
@Service
public class SupplierUploadLogProxy extends BaseProxy<SupplierUploadLog>{

	@Autowired
	private ISupplierUploadLogService supplierUploadLogService;

	@Override
	public IBaseService<SupplierUploadLog> getService() {
		return supplierUploadLogService;
	}
}
