package com.tp.backend.controller.supplier.ao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.supplier.PurchaseConstant;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.common.vo.supplier.entry.PurcharseType;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.PurchaseInfo;
import com.tp.model.sup.SupplierInfo;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.sup.IPurchaseProductService;
import com.tp.service.sup.ISupplierInfoService;

/**
 * <pre>
 * 采购订单AO
 * </pre>
 *
 * @author Administrator
 * @version $Id: PurchaseOrderAO.java, v 0.1 2015年1月8日 下午7:36:10 Administrator Exp $
 */
@Service
public class PurchaseOrderAO extends SupplierOrderBaseAO {

    @Autowired
    private ISupplierInfoService supplierInfoService;

    @Autowired
    private IPurchaseProductService purchaseProductService;

    @Autowired
    private IWarehouseService warehouseService;

    /**
     * <pre>
     * 生成页面消息
     * </pre>
     *
     * @param request
     * @param b
     * @return
     */
    public Map<String, Object> genPurchaseOrderInfo(final HttpServletRequest request, final boolean needCheck) {
        final Map<String, Object> retMap = new HashMap<String, Object>();
        final PurchaseInfo purchaseInfo = new PurchaseInfo();
        final Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("createTime", new Date());
        resultMap.put("createUser", SupplierUtilAO.getCurrentUserName());
        final Long supplierId = getLongValue(request, "supplierId");
        final Long warehouseId = getLongValue(request, "warehouseId");
        if (null == supplierId && needCheck) {
            retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "供应商不能为空。");
            return retMap;
        }
        if (null == warehouseId && needCheck) {
            retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "仓库不能为空。");
            return retMap;
        }
        final SupplierInfo supplierVO = supplierInfoService.queryById(supplierId);
        final Warehouse warehouseDO = warehouseService.queryById(warehouseId);
        if (null == warehouseDO) {
            retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "仓库信息 不存在。");
            return retMap;
        }
        if (null == supplierVO && needCheck) {
            retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "供应商不存在。");
            return retMap;
        }

        if (!checkWarehoueBelongsToSupplier(warehouseDO, supplierVO) && needCheck) {
            retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "供应商不存在此仓库信息。");
            return retMap;
        }

        resultMap.put("supplier", supplierVO);
        resultMap.put("warehouseDO", warehouseDO);
        // 生成报价单基础信息
        generateBaseInfo(purchaseInfo, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        // 生成报价单商品信息
        try {
        	generateProductBaseInfo(purchaseInfo, resultMap, request, needCheck);
        } catch(Exception e) {
        	LOGGER.error("（采购/代销）订单生成商品异常----------------");
        	LOGGER.error(e.getMessage(),e);
        	retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "生成商品信息失败。");
            return retMap;
        }
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        caculateOrderItemSum(purchaseInfo);
        retMap.put(SupplierConstant.DATA_KEY, purchaseInfo);
        retMap.put(SupplierConstant.SUCCESS_KEY, true);
        return retMap;
    }

    /**
     * <pre>
     * 生成订单基础信息
     * </pre>
     *
     * @param purchaseInfo
     * @param resultMap
     * @param request
     * @param needCheck
     */
    private void generateBaseInfo(final PurchaseInfo purchaseInfo, final Map<String, Object> resultMap,
        final HttpServletRequest request, final boolean needCheck) {
        final SupplierInfo supplier = (SupplierInfo) resultMap.get("supplier");
        final Warehouse warehouseDO = (Warehouse) resultMap.get("warehouseDO");
        final Date createTime = (Date) resultMap.get("createTime");
        /** 审核状态 */
        final Integer auditStatus = AuditStatus.EDITING.getStatus();
        /** 状体（1：启用 0：禁用） */
        final Integer status = Constant.ENABLED.YES;
        /** 订[退]单日期 */
        final Date orderDate = getDate(request, "orderDate", "yyyy-MM-dd");
        /** 供应商id */
        final Long supplierId = supplier.getId();
        /** 供应商名称 */
        final String supplierName = supplier.getName();
        /** 税率(%) */
        final Double rate = getRateInfo(request, "rate");
        /** 币别 */
        final String currency = getStringValue(request, "currency");
        /** 仓库id */
        final Long warehouseId = warehouseDO.getId();
        /** 仓库名称 */
        final String warehouseName = warehouseDO.getName();
        /** 期望日期 */
        final Date expectDate = getDate(request, "expectDate", "yyyy-MM-dd");
        /** 汇率(CNY) */
        final Double exchangeRate = getRateInfo(request, "exchangeRate");
        /** 订单是否确认(0:未确认,1:已确认) */
        final Integer isConfirm = getIntValue(request, "isConfirm");
        /** 订单类型(采购订单,采购退货单,代销订单,代销退货单) */
        final String orderType = PurcharseType.PURCHARSE.getValue();
        /** 订单类型：一般订单、紧急订单 */
        final String orderTypeLevel = getStringValue(request, "orderTypeLevel");
        /** 备注 */
        final String orderDesc = getStringValue(request, "orderDesc");
        
        purchaseInfo.setPurchaseDate(orderDate);
        purchaseInfo.setAuditStatus(auditStatus);
        purchaseInfo.setStatus(status);
        purchaseInfo.setReceiveStatus(PurchaseConstant.BILL_UNCONFIRM);
        purchaseInfo.setSupplierId(supplierId);
        purchaseInfo.setSupplierName(supplierName);
        purchaseInfo.setCurrency(currency);
        purchaseInfo.setWarehouseId(warehouseId);
        purchaseInfo.setWarehouseName(warehouseName);
        purchaseInfo.setExpectDate(expectDate);
        purchaseInfo.setExchangeRate(exchangeRate);
        purchaseInfo.setRate(rate);
        purchaseInfo.setIsConfirm(isConfirm);
        purchaseInfo.setPurchaseType(orderType);
        purchaseInfo.setPurchaseTypeLevel(orderTypeLevel);
        purchaseInfo.setPurchaseDesc(orderDesc);
        purchaseInfo.setCreateUser(SupplierUtilAO.getCurrentUserName());
        purchaseInfo.setUpdateUser(SupplierUtilAO.getCurrentUserName());
        purchaseInfo.setCreateTime(createTime);
        resultMap.put(SupplierConstant.SUCCESS_KEY, true);
    }
    /**
     * 校验仓库师傅属于该供应商
     */
    public boolean checkWarehoueBelongsToSupplier(final Warehouse warehouseDO, final SupplierInfo supplierVO) {
        if (null == warehouseDO || null == supplierVO) {
            return false;
        }
        if ((SupplierType.PURCHASE.getValue().equals(supplierVO.getSupplierType())|| SupplierType.SELL.getValue().equals(supplierVO.getSupplierType()))
            && new Long("0").equals(warehouseDO.getSpId())) {
            return true;
        } else if (null != supplierVO.getId() && supplierVO.getId().equals(warehouseDO.getSpId())) {
            return true;
        }
        return false;
    }
}
