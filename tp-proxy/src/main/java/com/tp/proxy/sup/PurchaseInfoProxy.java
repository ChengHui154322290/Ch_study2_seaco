package com.tp.proxy.sup;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.BillType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.PurchaseInfo;
import com.tp.model.sup.PurchaseProduct;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.IPurchaseInfoService;
import com.tp.service.sup.IPurchaseProductService;
import com.tp.util.CodeCreateUtil;
/**
 * 采购[代销]订单[退货单]主表代理层
 * @author szy
 *
 */
@Service
public class PurchaseInfoProxy extends BaseProxy<PurchaseInfo>{

	@Autowired
	private IPurchaseInfoService purchaseInfoService;
	@Autowired
	private IPurchaseProductService purchaseProductService;

	@Override
	public IBaseService<PurchaseInfo> getService() {
		return purchaseInfoService;
	}

	public ResultInfo<Boolean> updateOrderInfo(PurchaseInfo purchaseInfo, Integer auditStatus) {
		try{
			return purchaseInfoService.updateOrderInfo(purchaseInfo, auditStatus);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,purchaseInfo,auditStatus);
			return new ResultInfo<>(failInfo);
		}
	}

	public ResultInfo<PurchaseInfo> savePurchaseOrderInfo(PurchaseInfo purchaseInfo) {
		try{
			purchaseInfo.setPurchaseCode(CodeCreateUtil.initCodeValue());
			List<PurchaseProduct> purchaseProductList = purchaseInfo.getPurchaseProductList();
			if(CollectionUtils.isNotEmpty(purchaseProductList)){
				for(PurchaseProduct purchaseProduct:purchaseProductList){
					purchaseProduct.setCreateUser(purchaseInfo.getUpdateUser());
					purchaseProduct.setUpdateUser(purchaseInfo.getUpdateUser());
				}
			}
			return purchaseInfoService.saveOrderInfo(purchaseInfo);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,purchaseInfo);
			return new ResultInfo<>(failInfo);
		}
	}

	public ResultInfo<Boolean> auditOrder(PurchaseInfo purchaseInfo, Integer auditStatus,
			AuditRecords record) {
		try{
			return purchaseInfoService.auditOrder(purchaseInfo, auditStatus, record);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,purchaseInfo,auditStatus,record);
			return new ResultInfo<>(failInfo);
		}
	}

	/**
     * <pre>
     *
     * </pre>
     *
     * @param purId
     * @return
     */
    public PurchaseInfo getPurchaseOrderById(final Long purchaseId) {
        if (null == purchaseId) {
            return null;
        }
        final PurchaseInfo purchaseInfo = purchaseInfoService.queryById(purchaseId);
        if (null == purchaseInfo) {
            return null;
        }
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("purchaseId", purchaseId);
        params.put("status", Constant.ENABLED.YES);
        final List<PurchaseProduct> purchaseProductList = purchaseProductService.queryByParam(params);
        purchaseInfo.setPurchaseProductList(purchaseProductList);
        return purchaseInfo;
    }
    
    /**
     * <pre>
     * 订单审核
     * </pre>
     *
     * @param purchaseInfo
     * @param auditStatus
     * @param auditContent
     * @param request
     */
    public ResultInfo<Boolean> auditOrder(final PurchaseInfo purchaseInfo, final Integer auditStatus,
        final String auditContent, final BillType billtype,String userName,Long userId) {
        final AuditRecords record = new AuditRecords();
        record.setAuditId(purchaseInfo.getId());
        record.setContent(auditContent);
        record.setAuditStatus(auditStatus);
        record.setUserId(userId);
        record.setUserName(userName);
        record.setUpdateUser(userName);
        record.setCreateUser(userName);
        record.setTitle("审核报价单");
        if (billtype.getValue().equals(BillType.SELL.getValue())
            || billtype.getValue().equals(BillType.PURCHARSE.getValue())) {
            record.setOperate(SupplierConstant.O_AUDIT_RESULT.get(auditStatus));
        } else if (billtype.getValue().equals(BillType.PURCHARSE_RETURN.getValue())
            || billtype.getValue().equals(BillType.SELL_RETURN.getValue())) {
            record.setOperate(SupplierConstant.REFUND_O_AUDIT_RESULT.get(auditStatus));
        }
        record.setBillType(billtype.getValue());
        record.setStatus(Constant.ENABLED.YES);
        record.setCreateTime(new Date());
        record.setUserName(userName);
        return auditOrder(purchaseInfo, auditStatus, record);
    }
    
}
