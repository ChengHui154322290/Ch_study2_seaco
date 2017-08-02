package com.tp.service.sup;

import com.tp.model.sup.SupplierAttach;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 供应商-证件信息表接口
  */
public interface ISupplierAttachService extends IBaseService<SupplierAttach>{

    /**
     * <pre>
     * 根据供应商id获取供应商的附件信息
     * </pre>
     *
     * @param spId
     * @return
     */
	SupplierAttach getSupplierBySupplierId(final Long supplierId);
    /**
     * <pre>
     * 更新供应商附件信息
     * </pre>
     */
    void updateAttachInfoBySupplierId(SupplierAttach aupplierAttach);
}
