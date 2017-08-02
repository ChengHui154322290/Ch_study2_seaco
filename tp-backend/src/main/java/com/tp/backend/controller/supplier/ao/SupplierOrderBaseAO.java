package com.tp.backend.controller.supplier.ao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.thoughtworks.xstream.mapper.Mapper.Null;
import com.tp.common.vo.Constant;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.BillType;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.dto.sup.SkuInfoVO;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.PurchaseInfo;
import com.tp.model.sup.PurchaseProduct;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.sup.SupplierWarehouse;
import com.tp.service.sup.IAuditRecordsService;
import com.tp.service.sup.IPurchaseInfoService;
import com.tp.service.sup.IPurchaseProductService;
import com.tp.service.sup.ISequenceService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.util.BigDecimalUtil;

public class SupplierOrderBaseAO extends SupplierBaseAO {

    @Autowired
    private ISupplierInfoService supplierInfoService;

    @Autowired
    private IPurchaseInfoService purchaseInfoService;

    @Autowired
    private IPurchaseProductService purchaseProductService;

    @Autowired
    private IAuditRecordsService auditRecordsService;

    @Autowired
    private SupplierItemAO supplierItemAO;

    @Autowired
    private ISequenceService sequenceService;

    /**
     * <pre>
     * 生成商品信息
     * </pre>
     *
     * @param purchaseInfo
     * @param resultMap
     * @param request
     * @param needCheck
     */
    public void generateProductBaseInfo(final PurchaseInfo purchaseInfo, final Map<String, Object> resultMap,
        final HttpServletRequest request, final boolean needCheck) {

        final Date createTime = (Date) resultMap.get("createTime");
        final String skuCodes[] = getStringValues(request, "skuCode");
        final Long[] counts = getLongValues(request, "count");

        final BigDecimal[] standardPrices = getBigDecimals(request, "standardPrice");
        final BigDecimal[] discounts = getBigDecimals(request, "discount");
        final BigDecimal[] orderPrices = getBigDecimals(request, "orderPrice");
        final BigDecimal[] subtotals = getBigDecimals(request, "subtotal");
        final BigDecimal[] purchaseRates = getRateInfos(request, "purchaseRate");
        final BigDecimal[] noRateMoney = getBigDecimals(request, "noTaxRate");
        final BigDecimal[] tariffRates = getRateInfos(request, "tariffRate");
        final String[] productDescs = getStringValues(request, "productDesc");
        final String[] batchNumbers = getStringValues(request, "batchNumber");
        boolean isReturnOrder = false;
        final Long[] originIds = getLongValues(request, "originId");
        final Long[] originProductIds = getLongValues(request, "originProductId");
        if (null != originIds && originIds.length > 0) {
            isReturnOrder = true;
        }
        final String[] unitNames = getStringValues(request, "unitName");
        final String[] prop1s = getStringValues(request, "prop1");
        final String[] prop2s = getStringValues(request, "prop2");
        final String[] prop3s = getStringValues(request, "prop3");
        // by zhs 0125 保存storageCount 和 numberReturns 
       final Long[] storageCounts = getLongValues(request, "storageCount");
       final Long[] numberReturnss = getLongValues(request, "numberReturns");

        // 生成商品和sku的map
        final Map<String, SkuInfoVO> skuInfoMap = generateSkuMap(skuCodes);
        if (null == skuCodes || skuCodes.length == 0) {
            resultMap.put(SupplierConstant.SUCCESS_KEY, false);
            resultMap.put(SupplierConstant.MESSAGE_KEY, "报价单商品非空。");
            return;
        }
        final List<PurchaseProduct> productList = new ArrayList<PurchaseProduct>();
        for (int i = 0; i < skuCodes.length; i++) {
            final String skuCode = skuCodes[i];
            final PurchaseProduct purchaseProduct = new PurchaseProduct();
            purchaseProduct.setSku(skuCode);
            final SkuInfoVO skuInfo = skuInfoMap.get(skuCode);
            if (null != skuInfo) {
                purchaseProduct.setBigName(skuInfo.getBigCatName());
                purchaseProduct.setBigId(skuInfo.getBigCatId() + "");
                purchaseProduct.setMidName(skuInfo.getMidCatName());
                purchaseProduct.setMidId(skuInfo.getMidCatId() + "");
                purchaseProduct.setSmallId(skuInfo.getSmallCatId() + "");
                purchaseProduct.setSmallName(skuInfo.getSmallCatName());
                purchaseProduct.setBrandName(skuInfo.getBrandName());
                purchaseProduct.setBrandId(skuInfo.getBrandId());
                purchaseProduct.setBarcode(skuInfo.getBarcode());
                purchaseProduct.setProductName(skuInfo.getSkuName());
            }

            // by zhs 01161615 增加null判断
            if (unitNames !=null && unitNames[i] != null) {
                purchaseProduct.setProductUnit(unitNames[i]);				
			}
            if (prop1s != null && prop1s[i] != null) {
                purchaseProduct.setProp1(prop1s[i]);				
			}
            if (prop2s != null && prop2s[i] != null) {
                purchaseProduct.setProp2(prop2s[i]);				
			}
            if (prop3s !=null && prop3s[i] != null) {
                purchaseProduct.setProp3(prop3s[i]);				
			}
            if (isReturnOrder) {
            	if (originIds != null && originIds[i] !=null) {
                    purchaseProduct.setOriginId(originIds[i]);					
				}
            	if (originProductIds != null && originProductIds[i] != null) {
                    purchaseProduct.setOriginProductId(originProductIds[i]);					
				}
            }
            
            if (counts !=null && counts[i] != null) {
                purchaseProduct.setCount(counts[i]);				
			}
            if (standardPrices!=null && standardPrices[i] != null) {
                purchaseProduct.setStandardPrice(standardPrices[i].doubleValue());				
			}
            if (discounts!=null && discounts[i]!=null) {
                purchaseProduct.setDiscount(discounts[i].doubleValue());				
			}
            if (orderPrices!=null && orderPrices[i]!=null) {
                purchaseProduct.setOrderPrice(orderPrices[i].doubleValue());				
			}
            if (subtotals!=null && subtotals[i]!=null) {
                purchaseProduct.setSubtotal(subtotals[i].doubleValue());				
			}            
            if (purchaseRates != null && purchaseRates[i] != null) {
                purchaseProduct.setPurchaseRate(purchaseRates[i].doubleValue());				
			}
            if (tariffRates != null && tariffRates[i] != null) {
                purchaseProduct.setTariffRate(tariffRates[i].doubleValue());				
			}
            if (noRateMoney != null && noRateMoney[i] != null) {
                purchaseProduct.setNoTaxAccount(noRateMoney[i].doubleValue());               				
			}
            if (productDescs!=null && productDescs[i]!=null) {
                purchaseProduct.setProductDesc(productDescs[i]);				
			}
            if (storageCounts !=null && standardPrices[i]!=null) {
				purchaseProduct.setStorageCount(storageCounts[i]);
			}
            if (numberReturnss!=null && numberReturnss[i]!=null) {
				purchaseProduct.setNumberReturns(numberReturnss[i]);
			}

            purchaseProduct.setPurchaseType(purchaseInfo.getPurchaseType());
            if (isReturnOrder) {
            	if (batchNumbers!=null && batchNumbers[i]!=null) {
                    purchaseProduct.setBatchNumber(batchNumbers[i]);					
				}
            } else {
            }
            //////////////////////
            
            purchaseProduct.setSupplierId(purchaseInfo.getSupplierId());
            purchaseProduct.setStatus(Constant.ENABLED.YES);
            purchaseProduct.setCreateTime(createTime);
            purchaseProduct.setCreateUser(SupplierUtilAO.getCurrentUserName());
            purchaseProduct.setCreateTime(createTime);
            productList.add(purchaseProduct);
        }

        purchaseInfo.setPurchaseProductList(productList);
    }


    /**
     * <pre>
     * 计算订单中商品信息
     * </pre>
     *
     * @param purchaseInfo
     */
    public void caculateOrderItemSum(final PurchaseInfo purchaseInfo) {
        /** 可用数量 */
        final Long totalAvailable = 0L;
        /** 数量合计 */
        Long totalCount = 0L;
        /** 金额总计 */
        BigDecimal totalMoney = new BigDecimal("0");
        /** 入[出]库总数 */
        final Long totalStorage = 0L;

        final List<PurchaseProduct> productList = purchaseInfo.getPurchaseProductList();
        if (null != productList && productList.size() > 0) {
            for (final PurchaseProduct product : productList) {
                totalCount = totalCount + product.getCount();
                totalMoney = totalMoney.add(new BigDecimal(product.getSubtotal()));
            }
        }
        purchaseInfo.setTotalAvailable(totalAvailable);
        purchaseInfo.setTotalCount(totalCount);
        purchaseInfo.setTotalMoney(BigDecimalUtil.formatToPrice(totalMoney).doubleValue());
        purchaseInfo.setTotalStorage(totalStorage);
    }

    /**
     * <pre>
     * 生成商品map
     * </pre>
     *
     * @param skuCodes
     * @return
     */
    private Map<String, SkuInfoVO> generateSkuMap(final String skuCodes[]) {
        Map<String, SkuInfoVO> retMap = new HashMap<String, SkuInfoVO>();
        if (null != skuCodes && skuCodes.length > 0) {
            retMap = supplierItemAO.getSkuInfoBySkus(Arrays.asList(skuCodes));
        }
        return retMap;
    }


    /**
     * 校验供应商仓库是否存在
     *
     * @param warehouseList
     * @param warehouseDO
     * @return
     */
    public boolean checkStorageInList(final List<SupplierWarehouse> warehouseList, final Warehouse warehouseDO) {
        if (null == warehouseList || warehouseList.size() == 0) {
            return false;
        }
        for (final SupplierWarehouse warehouseVO : warehouseList) {
            if (warehouseDO.getId().equals(warehouseVO.getWarehouseId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * <pre>
     * 保存审核记录
     * </pre>
     *
     * @param purchaseInfo
     * @param auditStatus
     * @param auditContent
     * @param request
     */
    public void saveAuditRecords(final PurchaseInfo purchaseInfo, final Integer auditStatus, final String auditContent,
        final HttpServletRequest request, final BillType billtype) {
        final AuditRecords record = new AuditRecords();
        final Long userId = SupplierUtilAO.currentUserId();
        final Long roleId = SupplierUtilAO.currentRoleId();
        record.setAuditId(purchaseInfo.getId());
        record.setContent(auditContent);
        record.setAuditStatus(auditStatus);
        record.setUserId(userId);
        record.setUserName(SupplierUtilAO.currentUserName());
        if (billtype.getValue().equals(BillType.SELL.getValue())
            || billtype.getValue().equals(BillType.PURCHARSE.getValue())) {
            record.setOperate(SupplierConstant.O_AUDIT_RESULT.get(auditStatus));
        } else if (billtype.getValue().equals(BillType.PURCHARSE_RETURN.getValue())
            || billtype.getValue().equals(BillType.SELL_RETURN.getValue())) {
            record.setOperate(SupplierConstant.REFUND_O_AUDIT_RESULT.get(auditStatus));
        }
        record.setRoleId(roleId);
        record.setRoleName(SupplierUtilAO.getRoleName());
        record.setBillType(billtype.getValue());
        record.setStatus(Constant.ENABLED.YES);
        record.setCreateTime(new Date());
        auditRecordsService.insert(record);
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
