package com.tp.service.sup;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.supplier.CommonUtil;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.PurcharseType;
import com.tp.dao.sup.AuditRecordsDao;
import com.tp.dao.sup.PurchaseInfoDao;
import com.tp.dao.sup.PurchaseProductDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.PurchaseInfo;
import com.tp.model.sup.PurchaseProduct;
import com.tp.model.sup.SupplierInfo;
import com.tp.service.BaseService;
import com.tp.service.sup.IPurchaseInfoService;
import com.tp.service.sup.ISupplierInfoService;

@Service
public class PurchaseInfoService extends BaseService<PurchaseInfo> implements IPurchaseInfoService {

	@Autowired
	private PurchaseInfoDao purchaseInfoDao;
	@Autowired
	private PurchaseProductDao purchaseProductDao;
	@Autowired
	private AuditRecordsDao auditRecordsDao;
	
	@Autowired
	private ISupplierInfoService supplierInfoService;
	
	@Override
	public BaseDao<PurchaseInfo> getDao() {
		return purchaseInfoDao;
	}
	/**
     * 审核订单
     *
     * @param purchaseDO
     * @param setStatus
     * @param record
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo<Boolean> auditOrder(final PurchaseInfo purchaseInfo, final Integer auditStatus, final AuditRecords record){
        if (null != purchaseInfo && null != purchaseInfo.getId()) {
            final PurchaseInfo oldPurchaseInfo = this.queryById(purchaseInfo.getId());
            if (null == oldPurchaseInfo || !SupplierConstant.O_PREVIOUS_AUDIT_STATUS.get(auditStatus).contains(oldPurchaseInfo.getAuditStatus())) {
            	return new ResultInfo<>(new FailInfo("订单审核状态异常！ 审核失败。"));
            }
            // by zhs 01171613 设置 purchaseInfo的 auditStatus
            purchaseInfo.setAuditStatus(auditStatus);
            final int num = purchaseInfoDao.updateNotNullById(purchaseInfo);
            purchaseProductDao.updateAuditStatus(purchaseInfo.getId(), purchaseInfo.getAuditStatus(), purchaseInfo.getUpdateUser());
            if (num < 1) {
            	return new ResultInfo<>(new FailInfo("审核异常。"));
            }

            auditRecordsDao.insert(record);
        } else {
        	return new ResultInfo<>(new FailInfo("审核异常。"));
        }
        return new ResultInfo<>(Boolean.TRUE);
    }

    /**
     * <pre>
     * 校验订单信息
     * </pre>
     *
     * @param purchaseInfo
     * @return
     * @throws DAOException
     */
    private ResultInfo<Boolean> checkSaveInfo(final PurchaseInfo purchaseInfo) {
        final Long supplierId = purchaseInfo.getSupplierId();
        final Long warehouseId = purchaseInfo.getWarehouseId();
        if (null == supplierId) {
        	return new ResultInfo<>(new FailInfo("供应商id不能为空"));
        }
        if (null == warehouseId) {
        	return new ResultInfo<>(new FailInfo("仓库id不能为空"));
        }
        SupplierInfo supplierInfo = supplierInfoService.queryById(supplierId);
        if (null == supplierInfo) {
        	return new ResultInfo<>(new FailInfo("供应商找不到。"));
        }
        return new ResultInfo<>(Boolean.TRUE);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo<PurchaseInfo> saveOrderInfo(final PurchaseInfo purchaseInfo){
        final List<PurchaseProduct> productList = purchaseInfo.getPurchaseProductList();
        if (CollectionUtils.isEmpty(productList)) {
        	return new ResultInfo<>(new FailInfo("订单商品不能为空。"));
        }

        final ResultInfo<Boolean> msg = checkSaveInfo(purchaseInfo);
        if (!msg.success) {
            return new ResultInfo<>(msg.msg);
        }
        // by zhs 设置isconfirm默认值为0
        if (purchaseInfo.getIsConfirm() == null) {
			purchaseInfo.setIsConfirm(0);
		}
        purchaseInfoDao.insert(purchaseInfo);
        saveProducts(purchaseInfo, productList);
        return new ResultInfo<>(purchaseInfo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo<Boolean> updateOrderInfo(final PurchaseInfo purchaseInfo, final Integer auditStatus){
        if (null == purchaseInfo) {
        	return new ResultInfo<>(new FailInfo("参数异常！实体为空。"));
        }
        // by zhs 01171626 当auditStatus为null是，不更新auditStatus字段
        if (auditStatus != null) {
            purchaseInfo.setAuditStatus(auditStatus);			
		}
        final Long id = purchaseInfo.getId();
        if (id == null) {
        	return new ResultInfo<>(new FailInfo("更新id非空。"));
        }

        if (null == purchaseInfo.getPurchaseType()) {
        	return new ResultInfo<>(new FailInfo("订单的类型不能为空。"));
        }

        final List<PurchaseProduct> productList = purchaseInfo.getPurchaseProductList();
        updateProductList(productList, purchaseInfo);
        purchaseInfoDao.updateNotNullById(purchaseInfo);
        return new ResultInfo<>(Boolean.TRUE);
    }

    /**
     * <pre>
     * 保存商品信息
     * </pre>
     *
     * @param id
     * @param productList
     * @throws DAOException
     */
    private void saveProducts(final PurchaseInfo purchaseInfo, final List<PurchaseProduct> productList) {
        String batchNumber = null;
        if (PurcharseType.PURCHARSE.getValue().equals(purchaseInfo.getPurchaseType()) || PurcharseType.SELL.getValue().equals(purchaseInfo.getPurchaseType())) {
            batchNumber = CommonUtil.formatDate(new Date(), "yyyyMMdd") + purchaseInfo.getId();
            purchaseInfo.setBatchNumber(batchNumber);
            super.updateNotNullById(purchaseInfo);
        }
        for (final PurchaseProduct purchaseProduct : productList) {
            purchaseProduct.setPurchaseId(purchaseInfo.getId());
            purchaseProduct.setAuditStatus(purchaseInfo.getAuditStatus());
            purchaseProduct.setBatchNumber(batchNumber);
            purchaseProductDao.insert(purchaseProduct);;
        }
    }

    /**
     * <pre>
     * 更新商品信息
     * </pre>
     *
     * @param productList
     * @param id
     * @throws DAOException
     */
    private void updateProductList(final List<PurchaseProduct> productList, final PurchaseInfo purchasInfo){
    	// by zhs 01171815 原来修改过的purchaseProduct数据不需要重置
//        if (null != purchasInfo.getAuditStatus() && null != purchasInfo.getId()) {
//            purchaseProductDao.updateAuditStatus(purchasInfo.getId(), purchasInfo.getAuditStatus(), purchasInfo.getUpdateUser());
//        }
        if (null != productList && productList.size() > 0) {
            final String updateUser = productList.get(0).getUpdateUser();
            purchaseProductDao.updateStatus(purchasInfo.getId(), Constant.DEFAULTED.NO, updateUser);
            final PurchaseInfo purchaseOld = purchaseInfoDao.queryById(purchasInfo.getId());
            boolean needUpdate = true;
            if (PurcharseType.PURCHARSE_RETURN.getValue().equals(purchasInfo.getPurchaseType()) || PurcharseType.SELL_RETURN.getValue().equals(purchasInfo.getPurchaseType())) {
                needUpdate = false;
            }
            final String batchNumber = purchaseOld.getBatchNumber();
            for (final PurchaseProduct purchaseProduct : productList) {
                purchaseProduct.setAuditStatus(purchasInfo.getAuditStatus());
                purchaseProduct.setPurchaseId(purchasInfo.getId());
                purchaseProduct.setUpdateUser(purchasInfo.getUpdateUser());
                purchaseProduct.setCreateUser(purchasInfo.getUpdateUser());
                if (needUpdate) {
                	purchaseProduct.setBatchNumber(batchNumber);
                }
                purchaseProductDao.insert(purchaseProduct);
            }
        }
    }
}
