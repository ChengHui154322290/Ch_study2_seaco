package com.tp.service.sup;

import com.tp.dto.common.ResultInfo;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.PurchaseInfo;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 采购[代销]订单[退货单]主表接口
  */
public interface IPurchaseInfoService extends IBaseService<PurchaseInfo>{
	  /**
     * <pre>
     * 订单审核
     * </pre>
     *
     * @param purchaseDO
     * @param setStatus
     * @param record
     * @return
     * @throws Exception
     */
	ResultInfo<Boolean> auditOrder(PurchaseInfo purchaseInfo, Integer auditStatus, AuditRecords record);
    /**
     * <pre>
     * 保存订单信息
     * </pre>
     *
     * @param purchaseDTO
     */
	ResultInfo<PurchaseInfo> saveOrderInfo(PurchaseInfo purchaseInfo);

    /**
     * <pre>
     * 更新订单信息
     * </pre>
     *
     * @param purchaseDTO
     * @param auditStatus
     */
	ResultInfo<Boolean> updateOrderInfo(PurchaseInfo purchaseInfo, Integer auditStatus);
}
