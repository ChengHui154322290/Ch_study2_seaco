package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.SupplierAttach;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.ISupplierAttachService;
/**
 * 供应商-证件信息表代理层
 * @author szy
 *
 */
@Service
public class SupplierAttachProxy extends BaseProxy<SupplierAttach>{

	@Autowired
	private ISupplierAttachService supplierAttachService;

	@Override
	public IBaseService<SupplierAttach> getService() {
		return supplierAttachService;
	}
	
	/**
     * 
     * <pre>
     *  根据供应商id获取供应商的附件信息
     * </pre>
     *
     * @param spId
     * @return
     */
    public SupplierAttach getSupplierBySupplierId(Long spId) {
        return supplierAttachService.getSupplierBySupplierId(spId);
    }
}
