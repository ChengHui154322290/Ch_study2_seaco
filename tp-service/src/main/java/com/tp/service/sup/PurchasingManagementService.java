package com.tp.service.sup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.supplier.PurchaseConstant;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.common.vo.supplier.entry.OrderAuditStatus;
import com.tp.common.vo.supplier.entry.PurcharseType;
import com.tp.common.vo.supplier.entry.SupplierBusinessType;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.dao.sup.PurchaseInfoDao;
import com.tp.dao.sup.PurchaseProductDao;
import com.tp.dao.sup.QuotationProductDao;
import com.tp.dao.sup.SupplierCategoryDao;
import com.tp.dao.sup.SupplierCustomsRecordationDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.WarehouseOrderProductRewriteDTO;
import com.tp.dto.stg.WarehouseOrderRewriteDTO;
import com.tp.model.sup.PurchaseInfo;
import com.tp.model.sup.PurchaseProduct;
import com.tp.model.sup.QuotationProduct;
import com.tp.model.sup.SupplierCategory;
import com.tp.model.sup.SupplierCustomsRecordation;
import com.tp.model.sup.SupplierInfo;
import com.tp.query.sup.SupplierQuery;
import com.tp.result.sup.SupplierCustomsRecordationResult;
import com.tp.result.sup.SupplierResult;
import com.tp.service.sup.IPurchasingManagementService;
import com.tp.service.sup.ISupplierInfoService;

/**
 * {采购管理模块对外接口实现} <br>
 * Create on : 2015年1月9日 下午4:59:50<br>
 *
 * @author szy
 * @version 0.0.1
 */
@Service(value = "purchasingManagementService")
public class PurchasingManagementService implements IPurchasingManagementService {

    private static final String DEFAULT_UPDATE_OPERATOR_ID = "-2";

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchasingManagementService.class);

    @Autowired
    private QuotationProductDao quotationProductDao;

    @Autowired
    private ISupplierInfoService supplierInfoService;

    @Autowired
    private PurchaseInfoDao purchaseInfoDao;

    @Autowired
    private PurchaseProductDao purchaseProductDao;

    @Autowired
    private SupplierCategoryDao supplierCategoryDao;

    @Autowired
    private SupplierCustomsRecordationDao supplierCustomsRecordationDao;

    /**
     * 校验返回值的信息
     *
     * @param warehouseOrder
     * @param message
     */
    private ResultInfo<Boolean> checkBackOrderInfo(final WarehouseOrderRewriteDTO warehouseOrder) {
        final Long warehouseOrderId = warehouseOrder.getWarehouseOrderId();
        if (null == warehouseOrderId) {
        	return new ResultInfo<>(new FailInfo("预约单id为空。"));
        }

        final List<WarehouseOrderProductRewriteDTO> productList = warehouseOrder.getProductList();
        if (null == productList || productList.size() == 0) {
        	return new ResultInfo<>(new FailInfo("商品为空。"));
        }

        for (final WarehouseOrderProductRewriteDTO product : productList) {
            if (null == product.getStorageCount()) {
            	return new ResultInfo<>(new FailInfo("商品数量为空。"));
            }

            if (null == product.getSkuCode()) {
            	return new ResultInfo<>(new FailInfo("skuCode为空。"));
            }
        }
        return new ResultInfo<Boolean>(Boolean.TRUE);
    }

    @Override
    public BigDecimal getProductSalesPrice(final String sku, final long supplierId) {
        if (StringUtils.isEmpty(sku)) {
            LOGGER.error("SKU is null.");
            return null;
        }
        final QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setSku(sku);
        quotationProduct.setSupplierId(supplierId);
        quotationProduct.setStartPage(1);
        quotationProduct.setPageSize(1);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("sku", sku);
        params.put("supplierId", supplierId);
        params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), " 0,1");
        List<QuotationProduct> QuotationProductList = quotationProductDao.queryByParam(params);
        if (CollectionUtils.isNotEmpty(QuotationProductList)) {
            final QuotationProduct product = QuotationProductList.get(0);
            if (product != null) {
                return new BigDecimal(product.getSalePrice());
            }
        }
        return null;
    }

    /**
     * 获取采购商品和sku对应map
     *
     * @param purchaseInfo
     */
    private Map<String, PurchaseProduct> getPurchaseProductMap(final Long purchaseId) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("puchaseId", purchaseId);
        params.put("status", Constant.TF.YES);
        List<PurchaseProduct> purProducts = purchaseProductDao.queryByParam(params);
        final Map<String, PurchaseProduct> skuPurProductMap = new HashMap<String, PurchaseProduct>();
        if (CollectionUtils.isNotEmpty(purProducts)) {
            for (final PurchaseProduct pruProduct : purProducts) {
                skuPurProductMap.put(pruProduct.getSupplierId() + pruProduct.getBatchNumber() + pruProduct.getSku(), pruProduct);
            }
        }
        return skuPurProductMap;
    }

    @Override
    public SupplierResult getSpecialSupplier() {
        final SupplierResult result = new SupplierResult();
        final List<SupplierInfo> supplierInfos = new ArrayList<SupplierInfo>();
        supplierInfos.add(supplierInfoService.queryById(0L));
        result.setSupplierInfoList(supplierInfos);
        return result;
    }

    /**
     * 根据条件获取供应商信息(默认审核通过状态)
     *
     * @param supplierId
     * @param supplierName
     * @param startPage
     * @param pageSize
     * @return
     */
    @Override
    public SupplierResult getSupplierListWithCondition(final Long supplierId, final List<SupplierType> supplierTypes, final String supplierName, int startPage,
        int pageSize) {

        final SupplierResult result = new SupplierResult();
        final SupplierInfo supplierInfo = new SupplierInfo();
        if (startPage < 1) {
            startPage = 1;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id", supplierId);
        if (StringUtils.isNotEmpty(supplierName)) {
            params.put("name", supplierName);
		}
        params.put("status", Constant.TF.YES);
        params.put("auditStatus", AuditStatus.THROUGH.getStatus());
        if (null != supplierTypes && supplierTypes.size() > 0) {
            final List<String> suppTypes = new ArrayList<String>();
            for (final SupplierType supType : supplierTypes) {
                suppTypes.add(supType.getValue());
            }
            supplierInfo.setSupplierTypesQuery(suppTypes);
        }
        PageInfo<SupplierInfo> supplierInfoPage = supplierInfoService.queryPageByParamNotEmpty(params, new PageInfo<SupplierInfo>(startPage,pageSize));
        result.setTotalCount(supplierInfoPage.getRecords().longValue());
        result.setStartPage(startPage);
        result.setPageSize(pageSize);
        result.setSupplierInfoList(supplierInfoPage.getRows());
        return result;
    }

    /**
     * 根据条件获取供应商信息(默认审核通过状态)
     *
     * @param supplierId
     * @param supplierName
     * @param startPage
     * @param pageSize
     * @return
     */
    @Override
    public SupplierResult getSuppliersByTypes(final List<SupplierType> supplierTypes, final int startPage, final int pageSize) {
    	final SupplierResult result = new SupplierResult();
        final SupplierInfo supplierInfo = new SupplierInfo();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("status", Constant.TF.YES);
        params.put("auditStatus", AuditStatus.THROUGH.getStatus());
        params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), startPage+","+pageSize);
        if (null != supplierTypes && supplierTypes.size() > 0) {
            final List<String> suppTypes = new ArrayList<String>();
            for (final SupplierType supType : supplierTypes) {
                suppTypes.add(supType.getValue());
            }
            supplierInfo.setSupplierTypesQuery(suppTypes);
        }
        PageInfo<SupplierInfo> supplierInfoPage = supplierInfoService.queryPageByParam(params, new PageInfo<SupplierInfo>(startPage,pageSize));
        result.setTotalCount(supplierInfoPage.getRecords().longValue());
        result.setStartPage(startPage);
        result.setPageSize(pageSize);
        result.setSupplierInfoList(supplierInfoPage.getRows());
        return result;
    }

    /**
     * 处理单个源退货订单
     *
     * @param oriPurchaseInfo
     * @param warehouseBackProductMap
     */
    private void handleOneRefundOriOrder(final PurchaseInfo purchaseOrder, final Map<String, WarehouseOrderProductRewriteDTO> warehouseBackProductMap) {
        final PurchaseProduct productPruduct = new PurchaseProduct();
        productPruduct.setPurchaseId(purchaseOrder.getId());
        productPruduct.setStatus(Constant.TF.YES);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("puchaseId", purchaseOrder.getId());
        params.put("status", Constant.TF.YES);
        List<PurchaseProduct> purProducts = purchaseProductDao.queryByParam(params);
        if (CollectionUtils.isNotEmpty(purProducts)) {
            final Long supplierId = purchaseOrder.getSupplierId();
            Long totalRefundNum = 0L;
            for (final PurchaseProduct purchaseProduct : purProducts) {
                final String key = supplierId + purchaseProduct.getBatchNumber() + purchaseProduct.getSku();
                if (warehouseBackProductMap.containsKey(key)) {
                    final WarehouseOrderProductRewriteDTO pro = warehouseBackProductMap.get(key);
                    Long returnNum = purchaseProduct.getNumberReturns();
                    if (null == returnNum) {
                        returnNum = 0L;
                    }
                    purchaseProduct.setNumberReturns(returnNum + pro.getStorageCount());
                    purchaseProduct.setUpdateUser(DEFAULT_UPDATE_OPERATOR_ID);
                    purchaseProduct.setUpdateTime(new Date());
                    purchaseProductDao.updateNotNullById(purchaseProduct);

                }
                Long numReturns = purchaseProduct.getNumberReturns();
                if (null == numReturns) {
                    numReturns = 0L;
                }
                totalRefundNum = totalRefundNum + numReturns;
            }
            purchaseOrder.setTotalReturn(totalRefundNum);
            purchaseOrder.setUpdateUser(DEFAULT_UPDATE_OPERATOR_ID);
            purchaseOrder.setUpdateTime(new Date());
            purchaseInfoDao.updateNotNullById(purchaseOrder);
        }
    }

    /**
     * 处理退货源订单信息
     *
     * @param originalIds
     * @param warehouseBackProductMap
     */
    private void handleRefundOrignalOrder(final List<Long> originalIds, final Map<String, WarehouseOrderProductRewriteDTO> warehouseBackProductMap) {
        if (CollectionUtils.isNotEmpty(originalIds)) {
            Map<String,Object> params = new HashMap<String,Object>();
            params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtils.join(originalIds, Constant.SPLIT_SIGN.COMMA)+")");
            List<PurchaseInfo> resultPurDOs =  purchaseInfoDao.queryByParam(params);
            if (null != resultPurDOs && resultPurDOs.size() > 0) {
                for (final PurchaseInfo oriPurchaseInfo : resultPurDOs) {
                    handleOneRefundOriOrder(oriPurchaseInfo, warehouseBackProductMap);
                }
            }
        }
    }

    /**
     * 设置仓库预约单的回写信息. 将订单信息设置到源信息当中 1. 处理分两种类型 订单和退货单。 2. 订单： 回写出库数量、订单状态 ; 退货单：回写入库数量，源订单确认退货数量。 3. 回写仓库预约单状态 为完成。
     *
     * @param warehouseOrder
     */
    public ResultInfo<Boolean> setOrderInfoToOriginal(final WarehouseOrderRewriteDTO warehouseOrder){
        /*
         * 1. 处理分两种类型 订单和退货单 2. 订单： 回写出库数量 订单状态 退货单：回写入库数量，源订单确认退货数量 3. 回写仓库预约单状态 为完成
         */
        final Long purchaseOrderId = warehouseOrder.getWarehouseOrderId();
        PurchaseInfo purchaseInfo = purchaseInfoDao.queryById(purchaseOrderId);
        // 采购或者仓库订单
        if (null != purchaseInfo) {
            // 普通订单 和 退货单 两种类型
            purchaseInfo.setAuditStatus(OrderAuditStatus.PURCHARSE_FINISHED.getStatus());
            writeBackOrderInfo(purchaseInfo, warehouseOrder);
            return new ResultInfo<Boolean>(Boolean.TRUE);
        } else {
            LOGGER.error("Warehouse booking order {} write back，can't find order info.", purchaseOrderId);
            return new ResultInfo<Boolean>(new FailInfo("不能找到信息："+purchaseOrderId));
        }
    }

    /**
     * 设置退货信息
     *
     * @param purchaseInfo
     * @param warehouseOrder
     */
    private void setRefundOrderDetailInfo(final PurchaseInfo purchaseInfo, final WarehouseOrderRewriteDTO warehouseOrder) {
        // 退货总数
        final Map<String, WarehouseOrderProductRewriteDTO> warehouseBackProductMap = new HashMap<String, WarehouseOrderProductRewriteDTO>();
        final List<WarehouseOrderProductRewriteDTO> productList = warehouseOrder.getProductList();
        if (null != productList && productList.size() > 0) {
            for (final WarehouseOrderProductRewriteDTO orderProduct : productList) {
                final String batchNumber = orderProduct.getBatchNumber();
                final String key = purchaseInfo.getSupplierId() + batchNumber + orderProduct.getSkuCode();
                warehouseBackProductMap.put(key, orderProduct);
            }
        }

        final Map<String, PurchaseProduct> purPruductSkuMap = getPurchaseProductMap(purchaseInfo.getId());
        if (null != purPruductSkuMap && purPruductSkuMap.size() > 0) {
            final List<Long> originalIds = new ArrayList<Long>();
            for (final Map.Entry<String, PurchaseProduct> purMap : purPruductSkuMap.entrySet()) {
                final PurchaseProduct product = purMap.getValue();
                originalIds.add(product.getOriginId());
            }
            /**
             * 处理退货源订单信息
             */
            handleRefundOrignalOrder(originalIds, warehouseBackProductMap);
        }
    }

    /**
     * 更新订单商品信息
     *
     * @param productDO
     * @param storageCount 出库或者入库数量
     * @param returnCount 退货数量
     */
    private void updatePurProductInfo(final PurchaseProduct productDO, final Long storageCount, final Long numberReturns) {
        productDO.setUpdateUser(DEFAULT_UPDATE_OPERATOR_ID);
        productDO.setUpdateTime(new Date());
        productDO.setStorageCount(storageCount);
        productDO.setNumberReturns(numberReturns);
        purchaseProductDao.updateNotNullById(productDO);
    }

    /**
     * 回写订单信息
     *
     * @param purchaseInfo
     * @param warehouseOrder
     */
    private void writeBackOrderInfo(final PurchaseInfo purchaseInfo, final WarehouseOrderRewriteDTO warehouseOrder) {
        // 1. 设置订单中的商品
        // 2. 设置订单信息
        final List<WarehouseOrderProductRewriteDTO> productList = warehouseOrder.getProductList();
        final Map<String, PurchaseProduct> purPruductSkuMap = getPurchaseProductMap(purchaseInfo.getId());
        final int applyProductCount = purPruductSkuMap.size();
        // 入库总数
        Long totalStorage = 0L;
        int receiveProductCount = 0;
        if (null != productList && (receiveProductCount = productList.size()) > 0) {
            // 标记是否完全发货
            boolean isComplete = true;
            if (applyProductCount > receiveProductCount) {
                // 如果回写的商品行和原来的商品行数量不相等 肯定不是完全发货
                isComplete = false;
            }
            for (final WarehouseOrderProductRewriteDTO orderProduct : productList) {
                final Long storeCount = orderProduct.getStorageCount();
                totalStorage = totalStorage + storeCount;
                final String batchNumber = orderProduct.getBatchNumber();
                final PurchaseProduct purchaseProduct = purPruductSkuMap.get(purchaseInfo.getSupplierId() + batchNumber + orderProduct.getSkuCode());
                if (null != purchaseProduct) {
                    if (!storeCount.equals(purchaseProduct.getCount())) {
                        // 如果回写的商数量和原来的商品数量不相等 肯定不是完全发货
                        isComplete = false;
                    }
                    updatePurProductInfo(purchaseProduct, storeCount, null);
                }
            }
            // 如果是退货订单
            if (PurcharseType.PURCHARSE_RETURN.getValue().equals(warehouseOrder.getOrderType())
                || PurcharseType.SELL_RETURN.getValue().equals(warehouseOrder.getOrderType())) {
                setRefundOrderDetailInfo(purchaseInfo, warehouseOrder);
            } else {
                // 设置采购和分销订单的状态
                if (isComplete) {
                    purchaseInfo.setReceiveStatus(PurchaseConstant.BILL_CONFIRM);
                }
            }
        }
        purchaseInfo.setTotalStorage(totalStorage);
        purchaseInfo.setUpdateTime(new Date());
        purchaseInfo.setUpdateUser(DEFAULT_UPDATE_OPERATOR_ID);
        purchaseInfo.setHasStorage(Constant.TF.YES);
        purchaseInfo.setAuditStatus(OrderAuditStatus.PURCHARSE_FINISHED.getStatus());
        purchaseInfoDao.updateNotNullById(purchaseInfo);
        purchaseProductDao.updateAuditStatus(purchaseInfo.getId(), OrderAuditStatus.PURCHARSE_FINISHED.getStatus(), "-2");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo<Boolean> writeWarehoseInfoToOrder(final WarehouseOrderRewriteDTO warehouseOrder) {
        // 1. 校验信息
        ResultInfo<Boolean> message = checkBackOrderInfo(warehouseOrder);
        if (!message.success) {
            LOGGER.error("writeWarehoseInfoToOrder message : {}", message);
            return message;
        }
        warehouseOrder.setOrderType(PurcharseType.PURCHARSE.getValue());
        message = setOrderInfoToOriginal(warehouseOrder);

        return message;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo<Boolean> writeWarehoseRefundInfoToOrder(final WarehouseOrderRewriteDTO warehouseOrder) {
        // 1. 校验信息
        ResultInfo<Boolean> message = checkBackOrderInfo(warehouseOrder);
        if (!message.success) {
            LOGGER.error("writeWarehoseRefundInfoToOrder message : {}", message);
            return message;
        }
        warehouseOrder.setOrderType(PurcharseType.PURCHARSE_RETURN.getValue());
        return setOrderInfoToOriginal(warehouseOrder);
    }

    @Override
    public SupplierResult getSuppliersByIds(final List<Long> idList){
        final SupplierResult result = new SupplierResult();
        List<SupplierInfo> suppliers = null;
        if (CollectionUtils.isEmpty(idList)) {
            result.setTotalCount(0L);
            return result;
        }
        Map<String,Object> params = new HashMap<String,Object>();
        final int totalSize = idList.size();
        final int handleSize = 800;
        if (handleSize >= totalSize) {
        	params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtils.join(idList, Constant.SPLIT_SIGN.COMMA)+")");
            // 总数小于于每次的最大处理数据量
            suppliers = supplierInfoService.queryByParam(params);
        } else {
            suppliers = new ArrayList<SupplierInfo>();
            final int subSize = totalSize % handleSize == 0 ? totalSize / handleSize : totalSize / handleSize + 1;
            for (int i = 0; i < subSize; i++) {
                int endIndex = (i + 1) * handleSize;
                if (endIndex > totalSize) {
                    endIndex = totalSize;
                }
                params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtils.join(idList.subList(i * handleSize, endIndex), Constant.SPLIT_SIGN.COMMA)+")");
                final List<SupplierInfo> subList = supplierInfoService.queryByParam(params);
                if (CollectionUtils.isNotEmpty(subList)) {
                    suppliers.addAll(subList);
                }
            }
        }
        if (null == suppliers) {
            result.setTotalCount(0L);
            return result;
        }
        result.setSupplierInfoList(suppliers);
        result.setTotalCount(new Long(suppliers.size()));
        return result;
    }

    @Override
    public SupplierResult getUsedSuppliersByIds(final List<Long> idList,AuditStatus auditStatus,Integer status){
        final SupplierResult result = new SupplierResult();
        List<SupplierInfo> suppliers = null;
        if (CollectionUtils.isEmpty(idList)) {
            result.setTotalCount(0L);
            return result;
        }
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("auditStatus", auditStatus.getStatus());
        params.put("status", status);
        final int totalSize = idList.size();
        final int handleSize = 800;
        if (handleSize >= totalSize) {
        	params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtils.join(idList, Constant.SPLIT_SIGN.COMMA)+")");
            // 总数小于于每次的最大处理数据量
            suppliers = supplierInfoService.queryByParam(params);
        } else {
            suppliers = new ArrayList<SupplierInfo>();
            final int subSize = totalSize % handleSize == 0 ? totalSize / handleSize : totalSize / handleSize + 1;
            for (int i = 0; i < subSize; i++) {
                int endIndex = (i + 1) * handleSize;
                if (endIndex > totalSize) {
                    endIndex = totalSize;
                }
                params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtils.join(idList.subList(i * handleSize, endIndex), Constant.SPLIT_SIGN.COMMA)+")");
                final List<SupplierInfo> subList = supplierInfoService.queryByParam(params);
                if (CollectionUtils.isNotEmpty(subList)) {
                    suppliers.addAll(subList);
                }
            }
        }
        if (null == suppliers) {
            result.setTotalCount(0L);
            return result;
        }
        result.setSupplierInfoList(suppliers);
        result.setTotalCount(new Long(suppliers.size()));
        return result;
    }

    @Override
    public SupplierCustomsRecordationResult getSupplierCustomsRecordation(final List<SupplierQuery> queryList) {
        final SupplierCustomsRecordationResult recordationRes = new SupplierCustomsRecordationResult();
        List<SupplierCustomsRecordation> supplierCustomsRecordationList = null;
        int nullParamsCount = 0;
        int retTotalCount = 0;
        final List<SupplierQuery> queryParamList = new ArrayList<SupplierQuery>();
        if (null == queryList || queryList.size() == 0) {
            return recordationRes;
        }

        for (int i = 0; i < queryList.size(); i++) {
            final SupplierQuery query = queryList.get(i);
            if (null != query) {
                queryParamList.add(query);
            } else {
                nullParamsCount++;
            }
        }

        final int totalSize = queryParamList.size();
        final int handleSize = 100;
        try {
            if (handleSize >= totalSize) {
                // 总数小于于每次的最大处理数据量
            	supplierCustomsRecordationList = supplierCustomsRecordationDao.queryCustomsRecordations(queryParamList);
            } else {
            	supplierCustomsRecordationList = new ArrayList<SupplierCustomsRecordation>();
                final int subSize = totalSize % handleSize == 0 ? totalSize / handleSize : totalSize / handleSize + 1;
                for (int i = 0; i < subSize; i++) {
                    int endIndex = (i + 1) * handleSize;
                    if (endIndex > totalSize) {
                        endIndex = totalSize;
                    }
                    final List<SupplierCustomsRecordation> subList = supplierCustomsRecordationDao.queryCustomsRecordations(queryParamList.subList(i
                        * handleSize, endIndex));
                    if (CollectionUtils.isNotEmpty(subList)) {
                    	supplierCustomsRecordationList.addAll(subList);
                    }
                }
            }
        } catch (final Exception e) {
            LOGGER.info(e.getMessage(), e);
        }
        retTotalCount = supplierCustomsRecordationList.size();
        LOGGER.info("getSupplierCustomsRecordation：-- execute success total results {}", retTotalCount);
        setErrorRecords(queryList, supplierCustomsRecordationList, recordationRes);
        recordationRes.setSupplierCustomsRecordationList(supplierCustomsRecordationList);
        recordationRes.setTotalCount(retTotalCount);
        recordationRes.setNullParamsCount(nullParamsCount);
        return recordationRes;
    }

    /**
     * 设置错误的返回记录
     *
     * @param queryList
     * @param retDOList
     * @param recordationRes
     */
    private void setErrorRecords(final List<SupplierQuery> queryList, final List<SupplierCustomsRecordation> supplierCustomsRecordationList,
        final SupplierCustomsRecordationResult recordationRes) {
        if (null == supplierCustomsRecordationList || null == recordationRes) {
            return;
        }
        final List<SupplierQuery> resList = new ArrayList<SupplierQuery>();
        final Map<String, SupplierCustomsRecordation> handleMap = new HashMap<String, SupplierCustomsRecordation>();
        for (int i = 0; i < supplierCustomsRecordationList.size(); i++) {
            final SupplierCustomsRecordation supplierCustomsRecordation = supplierCustomsRecordationList.get(i);
            handleMap.put(supplierCustomsRecordation.getSupplierId() + "_" + supplierCustomsRecordation.getCustomsChannelId(), supplierCustomsRecordation);
        }
        for (int i = 0; i < queryList.size(); i++) {
            final SupplierQuery supplierQuery = queryList.get(i);
            if (null == supplierQuery) {
                continue;
            }
            if (!handleMap.containsKey(supplierQuery.getSupplierId() + "_" + supplierQuery.getCustomsChannelId())) {
                resList.add(supplierQuery);
            }
        }
        recordationRes.setSupplierQueryList(resList);
    }

    @Override
    public List<SupplierType> getSupplierTypes(final SupplierBusinessType supplierBusinessType) {
        final List<SupplierType> supplierTypes = new ArrayList<SupplierType>();
        if (supplierBusinessType == null) {
            LOGGER.error("supplierBusinessType is null.");
            return supplierTypes;
        }

        if (SupplierBusinessType.SEAGOOR.equals(supplierBusinessType)) {
            supplierTypes.add(SupplierType.PURCHASE);
            supplierTypes.add(SupplierType.SELL);
        } else if (SupplierBusinessType.SELLER.equals(supplierBusinessType)) {
            supplierTypes.add(SupplierType.ASSOCIATE);
        }

        return supplierTypes;
    }

    @Override
    public List<Long> getSupplierBrandIds(Long supplierId){
    	List<Long> retList = new ArrayList<Long>();
    	if(null == supplierId){
    		return retList;
    	}
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("status", Constant.TF.YES);
    	params.put("supplierId", supplierId);
    	List<SupplierCategory> supplierBrands = supplierCategoryDao.queryByParam(params);
        if(CollectionUtils.isNotEmpty(supplierBrands)){
        	Set<Long> idSet = new HashSet<Long>();
        	for (SupplierCategory supplierBrand : supplierBrands) {
        		idSet.add(supplierBrand.getBrandId());
			}
        	retList = new ArrayList<Long>(idSet);
        }
        return retList;
    }

	@Override
	public SupplierResult fuzzyQuerySupplierByName(String supplierName,
			int startPage, int pageSize) {
		final SupplierResult result = new SupplierResult();
        if (startPage < 1) {
            startPage = 1;
        }
        if(StringUtils.isNotBlank(supplierName)){
        	Map<String,Object> params = new HashMap<String,Object>();
        	params.put("status", Constant.TF.YES);
        	params.put("auditStatus", AuditStatus.THROUGH.getStatus());
        	params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " name like concat('%','"+supplierName+"','%')");
        	PageInfo<SupplierInfo> pageInfo = supplierInfoService.queryPageByParam(params, new PageInfo<SupplierInfo>(startPage,pageSize));
            result.setTotalCount(pageInfo.getRecords().longValue());
            result.setStartPage(startPage);
            result.setPageSize(pageSize);
            result.setSupplierInfoList(pageInfo.getRows());
        	return result;
        } else {
        	result.setTotalCount(0L);
        	result.setMessage("传入的查询参数为空。");
        	return result;
        }
	}


	@Override
	public SupplierResult fuzzyQueryAllSupplierByName(String supplierName,
			int startPage, int pageSize) {
		final SupplierResult result = new SupplierResult();
        if (startPage < 1) {
            startPage = 1;
        }
        if(StringUtils.isNotBlank(supplierName)){
        	Map<String,Object> params = new HashMap<String,Object>();
        	params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " name like concat('%','"+supplierName+"','%')");
        	PageInfo<SupplierInfo> pageInfo = supplierInfoService.queryPageByParam(params, new PageInfo<SupplierInfo>(startPage,pageSize));
            result.setTotalCount(pageInfo.getRecords().longValue());
            result.setStartPage(startPage);
            result.setPageSize(pageSize);
            result.setSupplierInfoList(pageInfo.getRows());
        	return result;
        } else {
        	result.setTotalCount(0L);
        	result.setMessage("传入的查询参数为空。");
        	return result;
        }
	}

	@Override
	public SupplierResult fuzzyQuerySupplier(Long supplierId,
			List<SupplierType> supplierTypes, String supplierName,
			int startPage, int pageSize) {
	Map<String,Object> params = new HashMap<String,Object>();
	final SupplierResult result = new SupplierResult();
    if (startPage < 1) {
        startPage = 1;
    }
    if (null != supplierTypes && supplierTypes.size() > 0) {
        final List<String> suppTypes = new ArrayList<String>();
        for (final SupplierType supType : supplierTypes) {
            suppTypes.add(supType.getValue());
        }
        params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "supplier_type in (\'"+StringUtils.join(suppTypes, "\'"+Constant.SPLIT_SIGN.COMMA+"\'")+"\')");
    }
    if(StringUtils.isNotBlank(supplierName)){
    	params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " name like concat('%','"+supplierName+"','%')");
    }

    if(supplierId != null) params.put("id", supplierId);
	params.put("status", Constant.TF.YES);
	params.put("auditStatus", AuditStatus.THROUGH.getStatus());

	PageInfo<SupplierInfo> pageInfo = supplierInfoService.queryPageByParam(params, new PageInfo<SupplierInfo>(startPage,pageSize));
    result.setTotalCount(pageInfo.getRecords().longValue());
    result.setStartPage(startPage);
    result.setPageSize(pageSize);
    result.setSupplierInfoList(pageInfo.getRows());
	return result;
	}
}
