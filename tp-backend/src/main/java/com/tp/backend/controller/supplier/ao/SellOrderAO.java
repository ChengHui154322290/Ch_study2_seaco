package com.tp.backend.controller.supplier.ao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.common.vo.supplier.entry.PurcharseType;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.PurchaseInfo;
import com.tp.model.sup.SupplierInfo;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.sup.ISupplierInfoService;

@Service
public class SellOrderAO extends SupplierOrderBaseAO {

    @Autowired
    private ISupplierInfoService supplierInfoService;
    @Autowired
    private IWarehouseService warehouseService;
    @Autowired
    private SupplierUtilAO supplierUtilAO;

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
        final PurchaseInfo purchaseDTO = new PurchaseInfo();
        final Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("createTime", new Date());
        resultMap.put("createUserId", SupplierUtilAO.currentUserId());
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
        final SupplierInfo supplierVO = supplierInfoService.queryAllInfoBySupplierId(supplierId);
        final Warehouse warehouseDO = warehouseService.queryById(warehouseId);
        if (null == supplierVO && needCheck) {
            retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "供应商不存在。");
            return retMap;
        }
        /*
         * if (needCheck) { if (!checkStorageInList(supplierVO.getSupplierWarehouseList(), warehouseDO)) {
         * retMap.put(SupplierConstant.SUCCESS_KEY, false); retMap.put(SupplierConstant.MESSAGE_KEY, "供应商不存在此仓库信息。");
         * return retMap; } }
         */
        if (!checkWarehoueBelongsToSupplier(warehouseDO, supplierVO) && needCheck) {
            retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "供应商不存在此仓库信息。");
            return retMap;
        }
        resultMap.put("supplier", supplierVO);
        resultMap.put("warehouseDO", warehouseDO);
        // 生成报价单基础信息
        generateBaseInfo(purchaseDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        // 生成报价单商品信息
        generateProductBaseInfo(purchaseDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        caculateOrderItemSum(purchaseDTO);
        retMap.put(SupplierConstant.DATA_KEY, purchaseDTO);
        retMap.put(SupplierConstant.SUCCESS_KEY, true);
        return retMap;
    }

    /**
     * <pre>
     * 生成订单基础信息
     * </pre>
     *
     * @param purchaseDTO
     * @param resultMap
     * @param request
     * @param needCheck
     */
    private void generateBaseInfo(final PurchaseInfo purchaseDTO, final Map<String, Object> resultMap,
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
        final String orderType = PurcharseType.SELL.getValue();
        /** 订单类型：一般订单、紧急订单 */
        final String orderTypeLevel = getStringValue(request, "orderTypeLevel");
        /** 备注 */
        final String orderDesc = getStringValue(request, "orderDesc");

        purchaseDTO.setAuditStatus(auditStatus);
        purchaseDTO.setStatus(status);
        purchaseDTO.setPurchaseDate(orderDate);
        purchaseDTO.setSupplierId(supplierId);
        purchaseDTO.setSupplierName(supplierName);
        purchaseDTO.setCurrency(currency);
        purchaseDTO.setWarehouseId(warehouseId);
        purchaseDTO.setWarehouseName(warehouseName);
        purchaseDTO.setExpectDate(expectDate);
        purchaseDTO.setExchangeRate(exchangeRate);
        purchaseDTO.setRate(rate);
        purchaseDTO.setIsConfirm(isConfirm);
        purchaseDTO.setPurchaseType(orderType);
        purchaseDTO.setPurchaseTypeLevel(orderTypeLevel);
        purchaseDTO.setPurchaseDesc(orderDesc);
        purchaseDTO.setCreateUser(supplierUtilAO.currentUserName());
        purchaseDTO.setUpdateUser(purchaseDTO.getCreateUser());
        purchaseDTO.setCreateTime(createTime);
        resultMap.put(SupplierConstant.SUCCESS_KEY, true);
    }

}
