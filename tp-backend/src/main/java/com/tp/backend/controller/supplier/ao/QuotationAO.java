package com.tp.backend.controller.supplier.ao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.supplier.CommonUtil;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.common.vo.supplier.entry.QuotationType;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.dto.sup.SkuInfoVO;
import com.tp.model.sup.Contract;
import com.tp.model.sup.QuotationInfo;
import com.tp.model.sup.QuotationProduct;
import com.tp.model.sup.SupplierInfo;
import com.tp.service.sup.IContractService;
import com.tp.service.sup.IQuotationInfoService;
import com.tp.service.sup.IQuotationProductService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.util.StringUtil;

@Service
public class QuotationAO extends SupplierBaseAO {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IQuotationInfoService quotationInfoService;

    @Autowired
    private IQuotationProductService quotationProductService;

    @Autowired
    private ISupplierInfoService supplierInfoService;

    @Autowired
    private IContractService contractService;

    @Autowired
    private SupplierItemAO supplierItemAO;

    /**
     * <pre>
     * 从页面获取报价单信息
     * </pre>
     *
     * @param request
     * @param b
     * @return
     */
    public Map<String, Object> genQuotationInfo(final HttpServletRequest request, final boolean needCheck) {
        final Map<String, Object> retMap = new HashMap<String, Object>();
        final QuotationInfo quotationInfo = new QuotationInfo();
        final Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("createTime", new Date());
        resultMap.put("createUser", SupplierUtilAO.getCurrentUserName());
        final Long supplierId = getLongValue(request, "supplierId");
        final Long contractId = getLongValue(request, "contractId");
        if (null == supplierId && needCheck) {
            retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "供应商不能为空。");
            return retMap;
        }
        if (null == contractId && needCheck) {
            retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "合同不能为空。");
            return retMap;
        }
        final SupplierInfo supplierDO = supplierInfoService.queryById(supplierId);
        final Contract contractDO = contractService.queryById(contractId);
        if (null == supplierDO && needCheck) {
            retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "供应商不存在。");
            return retMap;
        }
        if (null == contractDO || !supplierId.equals(contractDO.getSupplierId()) && needCheck) {
            retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "该供应商合同找不到。");
            return retMap;
        }
        resultMap.put("supplier", supplierDO);
        resultMap.put("contract", contractDO);
        // 生成报价单基础信息
        generateQuotationBaseInfo(quotationInfo, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        // 生成报价单商品信息
        generateQuotationProductBaseInfo(quotationInfo, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        retMap.put(SupplierConstant.DATA_KEY, quotationInfo);
        retMap.put(SupplierConstant.SUCCESS_KEY, true);
        return retMap;
    }

    /**
     * <pre>
     * 生成报价单商品信息
     * </pre>
     *
     * @param quotationInfo
     * @param resultMap
     * @param request
     * @param needCheck
     */
    private void generateQuotationProductBaseInfo(final QuotationInfo quotationInfo,
        final Map<String, Object> resultMap, final HttpServletRequest request, final boolean needCheck) {
        final Date createTime = (Date) resultMap.get("createTime");
        final String skuCodes[] = getStringValues(request, "skuCode");
        final BigDecimal marketPrice[] = getBigDecimalValues(request, "marketPrice");
      //  final BigDecimal salePrice[] = getBigDecimalValues(request, "salePrice");
       // final BigDecimal supplyPrice[] = getBigDecimalValues(request, "supplyPrice");
        final BigDecimal commissionPercent[] = getBigDecimalValues(request, "commissionPercent");
        final String productUnits[] = getStringValues(request, "unitName");
        BigDecimal basePrice []= getBigDecimalValues(request,"basePrice");
        BigDecimal freight []= getBigDecimalValues(request,"freight");
        BigDecimal mulTaxRate[] = getBigDecimalValues(request,"mulTaxRate");
        BigDecimal tarrifTaxRate[] = getBigDecimalValues(request,"tarrifTaxRate");
        BigDecimal sumPrice[] = getBigDecimalValues(request,"sumPrice");
        // 生成商品和sku的map
        final Map<String, SkuInfoVO> skuInfoMap = generateSkuMap(skuCodes);
        if (null == skuCodes || skuCodes.length == 0) {
            resultMap.put(SupplierConstant.SUCCESS_KEY, false);
            resultMap.put(SupplierConstant.MESSAGE_KEY, "报价单商品非空。");
            return;
        }
        final List<QuotationProduct> quotationProductList = new ArrayList<QuotationProduct>();
        for (int i = 0; i < skuCodes.length; i++) {
            final String skuCode = skuCodes[i];
            final QuotationProduct product = new QuotationProduct();
            final SkuInfoVO skuInfoVO = skuInfoMap.get(skuCode);
            if (null != skuInfoVO) {
            	product.setSpu(skuInfoVO.getSpu());
            	product.setPrdid(skuInfoVO.getPrdid());
                product.setBigName(skuInfoVO.getBigCatName());
                product.setBigId(skuInfoVO.getBigCatId() + "");
                product.setMidName(skuInfoVO.getMidCatName());
                product.setMidId(skuInfoVO.getMidCatId() + "");
                product.setSmallId(skuInfoVO.getSmallCatId() + "");
                product.setSmallName(skuInfoVO.getSmallCatName());
                product.setBrandName(skuInfoVO.getBrandName());
                product.setBrandId(skuInfoVO.getBrandId());
                product.setProductName(skuInfoVO.getSkuName());
                product.setProductProp(skuInfoVO.getSpecifications());
                product.setBoxProp(skuInfoVO.getCartonSpec());
                product.setBarCode(skuInfoVO.getBarcode());
                product.setProductUnit(skuInfoVO.getUnitName());
            }
            product.setProductUnit(productUnits[i]);
            product.setSku(skuCode);
            product.setStandardPrice(marketPrice[i].doubleValue());
            product.setSupplierId(quotationInfo.getSupplierId());
           // quotationProductDTO.setSalePrice(salePrice[i].doubleValue());
           // product.setSupplyPrice(supplyPrice[i].doubleValue());
            product.setCommissionPercent(null!=commissionPercent[i]?commissionPercent[i].doubleValue():0);
            product.setStatus(Constant.ENABLED.YES);
            product.setCreateTime(createTime);
            product.setCreateUser(SupplierUtilAO.getCurrentUserName());
            product.setUpdateUser(product.getCreateUser());
            product.setUpdateTime(createTime);

            product.setBasePrice(basePrice[i].doubleValue());
            product.setFreight(freight[i].doubleValue());
            product.setMulTaxRate(mulTaxRate[i].doubleValue());
            product.setTarrifTaxRate(tarrifTaxRate[i].doubleValue());
            product.setSumPrice(sumPrice[i].doubleValue());

            quotationProductList.add(product);
        }
        quotationInfo.setQuotationProductList(quotationProductList);
    }

    /**
     * <pre>
     *
     * </pre>
     *
     * @param skuCodes
     * @return
     */
    private Map<String, SkuInfoVO> generateSkuMap(final String skuCodes[]) {
        Map<String, SkuInfoVO> skuInfoMap = null;
        if (null != skuCodes && skuCodes.length > 0) {
            skuInfoMap = supplierItemAO.getSkuInfoBySkus(Arrays.asList(skuCodes));
        }
        if (null == skuInfoMap) {
            skuInfoMap = new HashMap<String, SkuInfoVO>();
        }
        return skuInfoMap;
    }

    /**
     * <pre>
     * 生成报价单基本信息
     * </pre>
     *
     * @param quotationInfo
     * @param resultMap
     * @param request
     * @param needCheck
     */
    private void generateQuotationBaseInfo(final QuotationInfo quotationInfo, final Map<String, Object> resultMap,
        final HttpServletRequest request, final boolean needCheck) {
        final SupplierInfo supplier = (SupplierInfo) resultMap.get("supplier");
        final Contract contract = (Contract) resultMap.get("contract");
        final Date createTime = (Date) resultMap.get("createTime");
        /** 报价单名称 */
        final String quotationName = getStringValue(request, "quotationName");
        /** 供应商名称 */
        final String supplierName = supplier.getName();
        /** 合同编号 */
        final String contractCode = getStringValue(request, "contractCode");
        /** 合同编号 */
        final Long contractId = contract.getId();
        /** 合同名称 */
        final String contractName = contract.getContractName();
        /** 合同类型 */
        final String contractType = contract.getContractType();
        /** 有效期-开始日期 */
        final Date startDate = getDate(request, "startTime", "yyyy-MM-dd");
        /** 有效期-结束日期 */
        final Date endDate = getDate(request, "endTime", "yyyy-MM-dd");
        /** 报价单备注 */
        final String quotationDesc = getStringValue(request, "quotationDesc");
        /** 审核状态 */
        final Integer auditStatus = AuditStatus.EDITING.getStatus();
        /** 状体（1：启用 0：禁用） */
        final Integer status = Constant.ENABLED.YES;
        String quotationType = getStringValue(request, "quotationType");
        if (null == quotationType) {
            quotationType = QuotationType.COMMON_TYPE.getValue();
        } else if (quotationType.equals(QuotationType.CONTRACT_TYPE.getValue())) {
            quotationType = QuotationType.CONTRACT_TYPE.getValue();
        } else {
            quotationType = QuotationType.COMMON_TYPE.getValue();
        }
        quotationInfo.setQuotationType(quotationType);
        quotationInfo.setSupplierId(supplier.getId());
        quotationInfo.setContractId(contractId);
        quotationInfo.setContractCode(contractCode);
        quotationInfo.setContractName(contractName);
        quotationInfo.setAuditStatus(auditStatus);
        quotationInfo.setContractType(contractType);
        quotationInfo.setQuotationName(quotationName);
        quotationInfo.setSupplierName(supplierName);
        quotationInfo.setStartDate(startDate);
        quotationInfo.setStatus(status);
        quotationInfo.setEndDate(endDate);
        quotationInfo.setQuotationDesc(quotationDesc);
        quotationInfo.setCreateUser(SupplierUtilAO.getCurrentUserName());
        quotationInfo.setUpdateUser(quotationInfo.getCreateUser());
        quotationInfo.setCreateTime(createTime);
        quotationInfo.setUpdateTime(createTime);
        resultMap.put(SupplierConstant.SUCCESS_KEY, true);
    }


    /**
     * 分装索引和sku的map
     * 
     * @param skuInfoFromPage
     * @param skuInfoMap
     * @param barCodeIndexMap
     * @param skuCodeIndexMap
     */
    private void generateIndexSKuMap(final List<String[]> skuInfoFromPage, final Map<Integer, SkuInfoVO> skuInfoMap,
        final Map<String, Integer> skuCodeIndexMap, final Map<String, Integer> barCodeIndexMap) {
        for (int i = 0; i < skuInfoFromPage.size(); i++) {

            final String[] oneLine = skuInfoFromPage.get(i);
            final String barcodeStr = oneLine[0].trim();
            final String skuStr = oneLine[1].trim();
            final String marketPriceStr = oneLine[2].trim();
            final String salesPriceStr = oneLine[3].trim();
            final String supplierPriceStr = oneLine[4].trim();
            final String commonssionStr = oneLine[5].trim();
            final BigDecimal marketPrice = CommonUtil.getBigDecimalVal(marketPriceStr);
            final BigDecimal salesPrice = CommonUtil.getBigDecimalVal(salesPriceStr);
            final BigDecimal supplierPrice = CommonUtil.getBigDecimalVal(supplierPriceStr);
            final BigDecimal commonssion = CommonUtil.getBigDecimalVal(commonssionStr);
            // if (null == salesPrice || null == supplierPrice || null == commonssionStr) {
            // continue;
            // }
            if (!StringUtil.isBlank(skuStr)) {
                skuCodeIndexMap.put(skuStr, new Integer(i));
            } else if (!StringUtil.isBlank(barcodeStr)) {
                barCodeIndexMap.put(barcodeStr, new Integer(i));
            } else {
                continue;
            }
            final SkuInfoVO skuInfo = new SkuInfoVO();
            skuInfo.setBarcode(barcodeStr);
            skuInfo.setSku(skuStr);
            skuInfo.setMarketPrice(marketPrice);
            skuInfo.setStarndardPrice(salesPrice);
            skuInfo.setSupplyPrice(supplierPrice);
            skuInfo.setCommissionPer(commonssion);
            skuInfoMap.put(new Integer(i), skuInfo);
        }
    }

    /**
     * 解析页面信息
     * 
     * @param pageInfo
     * @return
     */
    private List<String[]> analyzePageInfo(String pageInfo, final String supplierType) {
        final List<String[]> retList = new ArrayList<String[]>();
        if (null == pageInfo) {
            return retList;
        }
        pageInfo = pageInfo.replaceAll("(\\n){2,}", "\n");
        if (pageInfo.length() > 1 && "\n".equals(pageInfo.substring(pageInfo.length() - 1, pageInfo.length()))) {
            pageInfo = pageInfo.substring(0, pageInfo.length() - 1);
        }
        final String[] lineArr = pageInfo.split("\n");
        for (int i = 0; i < lineArr.length; i++) {
            final String[] oneLine = (lineArr[i] + " ").split("\t");
            final String[] dest = new String[6];
            if (oneLine.length == 5 && SupplierType.PURCHASE.getValue().equals(supplierType)) {
                System.arraycopy(oneLine, 0, dest, 0, 5);
                dest[5] = "";
                retList.add(dest);
            } else if (oneLine.length == 6) {
                retList.add(oneLine);
            }
        }
        return retList;
    }


}
