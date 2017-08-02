package com.tp.proxy.sup;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.formula.functions.Quotient;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.common.vo.supplier.entry.BillType;
import com.tp.common.vo.supplier.entry.QuotationType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.sup.SkuInfoVO;
import com.tp.dto.sup.excel.ExcelQuotationInfoDTO;
import com.tp.dto.sup.excel.ExcelQuotationProductDTO;
import com.tp.model.prd.ItemSku;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.Contract;
import com.tp.model.sup.QuotationInfo;
import com.tp.model.sup.QuotationProduct;
import com.tp.model.sup.SupplierInfo;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.usr.UserHandler;
import com.tp.result.prd.SkuInfoResult;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.sup.IContractService;
import com.tp.service.sup.IQuotationInfoService;
import com.tp.service.sup.IQuotationProductService;
import com.tp.service.sup.ISupplierInfoService;
/**
 * 报价单主表代理层
 * @author szy
 *
 */
@Service
public class QuotationInfoProxy extends BaseProxy<QuotationInfo>{

	@Autowired
	private IQuotationInfoService quotationInfoService;
	@Autowired
	private IItemSkuService itemSkuService;
	
	@Autowired
	private IQuotationProductService quotationProductService;
	
    @Autowired
    private ISupplierInfoService supplierInfoService;
	
    @Autowired
    private IContractService contractService;

    @Autowired
    SupplierItemProxy supplierItemProxy;
    
    @Autowired
    private QuotationImportProxy quotationImportProxy;

    /**
     * 默认时间格式
     */
    public final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
	public IBaseService<QuotationInfo> getService() {
		return quotationInfoService;
	}

	public ResultInfo<QuotationInfo> saveQuotationInfo(QuotationInfo quotationInfo) {
		try{
			if(quotationInfo!=null && CollectionUtils.isNotEmpty(quotationInfo.getQuotationProductList())){
				for(QuotationProduct product:quotationInfo.getQuotationProductList()){
					ItemSku itemSku = itemSkuService.selectSkuInfoBySkuCode(product.getSku());
					product.setBarCode(itemSku.getBarcode());
					product.setSpu(itemSku.getSpu());
					product.setProductCode(itemSku.getSpCode());
					// by zhs 01151530 原来将product name设为供应商name
					//product.setProductName(itemSku.getSpName());
					product.setPrdid(itemSku.getPrdid());
				}
			}
			return quotationInfoService.saveQuotationInfo(quotationInfo);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,quotationInfo);
			return new ResultInfo<>(failInfo);
		}
	}
	
	// 向同一报价单下面加product 行, 批量导入使用
	public ResultInfo<QuotationInfo> addQuotationProductForImport(QuotationInfo quotationInfo, ExcelQuotationInfoDTO quoDTO) {
		try{
			if(quotationInfo!=null && CollectionUtils.isNotEmpty(quotationInfo.getQuotationProductList())){								
		        final List<QuotationProduct> productList = quotationInfo.getQuotationProductList();
		        if (null == productList || productList.size() == 0) {
		        	return new ResultInfo<>(new FailInfo("报价单商品不能为空。"));
		        }		        		        
				QuotationInfo queryQua = new QuotationInfo();
				queryQua.setSupplierId(quotationInfo.getSupplierId());	    		
				queryQua.setQuotationName(quotationInfo.getQuotationName() );
				queryQua.setStartDate(quotationInfo.getStartDate());
				queryQua.setEndDate(quotationInfo.getEndDate());
                queryQua.setId(quotationInfo.getId());
				List<QuotationInfo> quaList = quotationInfoService.queryByObject(queryQua);	    		
				if (quaList == null || quaList.isEmpty() ) {
		        	return new ResultInfo<>(new FailInfo("数据库内不存在此报价单。供应商id("+quotationInfo.getSupplierId()+") 报价单名称("+quotationInfo.getQuotationName()+")" ));
				}
				List<QuotationProduct> newprdlist = new ArrayList<QuotationProduct>();
				
				QuotationInfo quainfo = quaList.get(0);
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("quotation_id",  quainfo.getId() );
				params.put("status", 1);
			//	List<QuotationProduct> oldprdlist = quotationProductService.queryByParamNotEmpty(params);
				for (QuotationProduct newprd : productList) {					

//					for(QuotationProduct oldprd : oldprdlist){
//						if( newprd.getSku().equals( oldprd.getSku() ) )
//				            quotationImportProxy.setQuoErrorMsg(quoDTO, "sku("+oldprd.getSku() +")已存在; \n");
//						    break;
//
//					}
						ItemSku itemSku = itemSkuService.selectSkuInfoBySkuCode(newprd.getSku());
						newprd.setBarCode(itemSku.getBarcode());
						newprd.setSpu(itemSku.getSpu());
						newprd.setProductCode(itemSku.getSpCode());
						newprd.setPrdid(itemSku.getPrdid());

						newprdlist.add(newprd);

				}

				return quotationInfoService.addQuotationProducts(quainfo,newprdlist);
			}else {
	        	return new ResultInfo<>(new FailInfo("报价单为null 或 报价单商品为空。"));
			}
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,quotationInfo);
			return new ResultInfo<>(failInfo);
		}
	}

	public ResultInfo<QuotationInfo> updateQuotationInfo(QuotationInfo quotationInfo, Integer setStatus) {
		try{
			ResultInfo<Boolean> resultInfo = quotationInfoService.updateQuotationInfo(quotationInfo,setStatus);
			if(resultInfo.success){
				return new ResultInfo<>(quotationInfo);
			}
			return new ResultInfo<>(resultInfo.msg);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,quotationInfo);
			return new ResultInfo<>(failInfo);
		}
	}

	public ResultInfo<Boolean> auditQuotation(QuotationInfo quotation,Integer auditStatus, String auditContent, Long userId,String userName) {
        Integer setStatus = null;
        final AuditRecords record = new AuditRecords();
        record.setAuditId(quotation.getId());
        // by zhs 0120 修改判断条件
//        if (Constant.ENABLED.YES.equals(auditStatus)) {
        if (AuditStatus.THROUGH.getStatus() == auditStatus ) {
            setStatus = AuditStatus.THROUGH.getStatus();
        } else{
            setStatus = AuditStatus.REFUSED.getStatus();
        }
        record.setContent(auditContent);
        record.setAuditStatus(setStatus);
        record.setUserId(userId);
        record.setUserName(userName);
        record.setOperate(SupplierConstant.AUDIT_RESULT.get(setStatus));
        record.setBillType(BillType.PRICE.getValue());
        record.setStatus(Constant.ENABLED.YES);
        record.setCreateTime(new Date());
        record.setCreateUser(userName);
        record.setUpdateUser(userName);
        return quotationInfoService.auditQuotation(quotation, setStatus, record);
    }
		
	
	
    /**
     * <pre>
     * 从页面获取报价单信息
     * </pre>
     *
     * @param request
     * @param b
     * @return
     */
    public Map<String, Object> genQuotationInfo( ExcelQuotationInfoDTO quoInfo) {
        final Map<String, Object> retMap = new HashMap<String, Object>();
        final QuotationInfo quotationInfo = new QuotationInfo();
        final Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("createTime", new Date());
        resultMap.put("createUser", UserHandler.getUser().getLoginName() );
        final Long supplierId = quoInfo.getSupplierId();  //getLongValue(request, "supplierId");
        final Long contractId = quoInfo.getContractId();  //getLongValue(request, "contractId");
        if (null == supplierId ) {
            retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "供应商不能为空。");
            quotationImportProxy.setQuoErrorMsg(quoInfo, "供应商不能为空。\n");
            return retMap;
        }
        if (null == contractId ) {
            retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "合同不能为空。");
            quotationImportProxy.setQuoErrorMsg(quoInfo, "合同不能为空。\n");
            return retMap;
        }
        final SupplierInfo supplierDO = supplierInfoService.queryById(supplierId);
        final Contract contractDO = contractService.queryById(contractId);
        if (null == supplierDO  ) {
            retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "供应商不存在。");
            quotationImportProxy.setQuoErrorMsg(quoInfo, "供应商不存在。\n");
            return retMap;
        }
        if (null == contractDO || !supplierId.equals(contractDO.getSupplierId())  ) {
            retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "该供应商合同找不到。");
            quotationImportProxy.setQuoErrorMsg(quoInfo, "供应商ID("+supplierId+")该供应商合同找不到。\n");
            return retMap;
        }
        resultMap.put("supplier", supplierDO);
        resultMap.put("contract", contractDO);
        
        boolean needCheck = true;
        // 生成报价单基础信息
        generateQuotationBaseInfo(quotationInfo, resultMap, quoInfo, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        // 生成报价单商品信息
        generateQuotationProductBaseInfo(quotationInfo, resultMap, quoInfo, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        retMap.put(SupplierConstant.DATA_KEY, quotationInfo);
        retMap.put(SupplierConstant.SUCCESS_KEY, true);
        return retMap;
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
        final ExcelQuotationInfoDTO quoInfoDTO, final boolean needCheck) {
        final SupplierInfo supplier = (SupplierInfo) resultMap.get("supplier");
        final Contract contract = (Contract) resultMap.get("contract");
        final Date createTime = (Date) resultMap.get("createTime");
        /** 报价单名称 */
        final String quotationName = quoInfoDTO.getQuotationName(); //getStringValue(request, "quotationName");
        /** 供应商名称 */
        final String supplierName = supplier.getName();
        /** 合同编号 */
        final String contractCode = contract.getContractCode(); //getStringValue(request, "contractCode");
        /** 合同ID*/
        final Long contractId = contract.getId();
        /** 合同名称 */
        final String contractName = contract.getContractName();
        /** 合同类型 */
        final String contractType = contract.getContractType();
        /** 有效期-开始日期 */
        final Date startDate = getDate(quoInfoDTO.getStartDate(), "yyyy/MM/dd");
        /** 有效期-结束日期 */
        final Date endDate = getDate(quoInfoDTO.getEndDate(), "yyyy/MM/dd");
        /** 报价单备注 */
        final String quotationDesc = quoInfoDTO.getQuotationDesc(); //getStringValue(request, "quotationDesc");
        /** 审核状态 */
        final Integer auditStatus = AuditStatus.EDITING.getStatus();
        /** 状体（1：启用 0：禁用） */
        final Integer status = Constant.ENABLED.YES;
//        String quotationType = getStringValue(request, "quotationType");
//        if (null == quotationType) {
        String  quotationType = QuotationType.COMMON_TYPE.getValue();
//        } else if (quotationType.equals(QuotationType.CONTRACT_TYPE.getValue())) {
//            quotationType = QuotationType.CONTRACT_TYPE.getValue();
//        } else {
//            quotationType = QuotationType.COMMON_TYPE.getValue();
//        }
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
        quotationInfo.setCreateUser( UserHandler.getUser().getLoginName() );
        quotationInfo.setUpdateUser(quotationInfo.getCreateUser());
        quotationInfo.setCreateTime(createTime);
        quotationInfo.setId(quoInfoDTO.getQuotationInfoIndex());
        resultMap.put(SupplierConstant.SUCCESS_KEY, true);
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
        final Map<String, Object> resultMap, final ExcelQuotationInfoDTO quoInfoDTO, final boolean needCheck) {
        final Date createTime = (Date) resultMap.get("createTime");
        
        List<ExcelQuotationProductDTO>quoPrdList = quoInfoDTO.getQuotationProductList();                
        List<String> skuCodes = new ArrayList<String>();
        for(ExcelQuotationProductDTO quoPrd : quoPrdList){
        	skuCodes.add( quoPrd.getPrdSku() );
        }
        
        // 生成商品和sku的map
        final Map<String, SkuInfoResult> skuInfoMap = generateSkuMap(skuCodes, quoInfoDTO.getSupplierId() );
        if (null == skuCodes || skuCodes.isEmpty() ) {
            resultMap.put(SupplierConstant.SUCCESS_KEY, false);
            resultMap.put(SupplierConstant.MESSAGE_KEY, "报价单商品非空。");
            return;
        }
        final List<QuotationProduct> quotationProductList = new ArrayList<QuotationProduct>();
        for (int i = 0; i < skuCodes.size(); i++) {
            final String skuCode = skuCodes.get(i);
            final QuotationProduct quotationProductDTO = new QuotationProduct();
            final SkuInfoResult skuInfoVO = skuInfoMap.get(skuCode);
            if (null != skuInfoVO) {
            	quotationProductDTO.setSpu(skuInfoVO.getSpu());
            	quotationProductDTO.setPrdid(skuInfoVO.getPrdid());
                quotationProductDTO.setBigName(skuInfoVO.getBigCatName());
                quotationProductDTO.setBigId(skuInfoVO.getBigCatId() + "");
                quotationProductDTO.setMidName(skuInfoVO.getMidCatName());
                quotationProductDTO.setMidId(skuInfoVO.getMidCatId() + "");
                quotationProductDTO.setSmallId(skuInfoVO.getSmallCatId() + "");
                quotationProductDTO.setSmallName(skuInfoVO.getSmallCatName());
                quotationProductDTO.setBrandName(skuInfoVO.getBrandName());
                quotationProductDTO.setBrandId(skuInfoVO.getBrandId());
                quotationProductDTO.setProductName(skuInfoVO.getSkuName());
                quotationProductDTO.setProductProp(skuInfoVO.getSpecifications());
                quotationProductDTO.setBoxProp(skuInfoVO.getCartonSpec());
                quotationProductDTO.setBarCode(skuInfoVO.getBarcode());
                quotationProductDTO.setProductUnit(skuInfoVO.getUnitName());
                quotationProductDTO.setStandardPrice( skuInfoVO.getBasicPrice() );
            }
            // quotationProductDTO.setProductUnit( );	// productUnits[i]
            quotationProductDTO.setSku(skuCode);
//            quotationProductDTO.setStandardPrice(marketPrice[i].doubleValue());
            quotationProductDTO.setSupplierId(quotationInfo.getSupplierId());
            quotationProductDTO.setSalePrice( quoPrdList.get(i).getSalePrice() );		// salePrice[i].doubleValue()
            quotationProductDTO.setSupplyPrice( quoPrdList.get(i).getSupplyPrice() );	// supplyPrice[i].doubleValue()
            quotationProductDTO.setCommissionPercent( quoPrdList.get(i).getCommissionPercent() );		// null!=commissionPercent[i]?commissionPercent[i].doubleValue():0
            quotationProductDTO.setStatus(Constant.ENABLED.YES);
            quotationProductDTO.setCreateTime(createTime);
            quotationProductDTO.setCreateUser( UserHandler.getUser().getLoginName() );
            quotationProductDTO.setUpdateUser(quotationProductDTO.getCreateUser());
            quotationProductDTO.setCreateTime(createTime);

            ExcelQuotationProductDTO dto = getExcelProduct(quoInfoDTO.getQuotationProductList(),skuCode);
            if(dto !=null){
                quotationProductDTO.setBasePrice(dto.getBasePrice());
                quotationProductDTO.setFreight(dto.getFreight());
                quotationProductDTO.setMulTaxRate(dto.getMulTaxRate());
                quotationProductDTO.setTarrifTaxRate(dto.getTarrifTaxRate());
                quotationProductDTO.setSumPrice(dto.getSumPrice());
            }


            quotationProductList.add(quotationProductDTO);
        }
        quotationInfo.setQuotationProductList(quotationProductList);
    }

    private ExcelQuotationProductDTO getExcelProduct(List<ExcelQuotationProductDTO> list,String skuCode){
        for(ExcelQuotationProductDTO dto :list){
            if(dto.getPrdSku().equals(skuCode)){
                return dto;
            }
        }
        return null;
    }
    
    /**
     * <pre>
     * 获取时间
     * </pre>
     *
     * @param request
     * @param name
     * @param format
     * @return
     */
    public Date getDate(final String dateStr, final String format) {
        Date date = null;
        String dateFormat = DEFAULT_TIME_FORMAT;

        if (null == dateStr) {
			return null;
		}                
        if (null != format) {
            dateFormat = format;
        }
        final String dateVal = dateStr;
        if (null == dateVal || "".equals(dateVal.trim())) {
            return null;
        }
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            date = sdf.parse(dateVal);
        } catch (final Exception e) {
        }
        return date;
    }
    
    
    /**
     * <pre>
     * 校验结果
     * </pre>
     *
     * @param resultMap
     * @return
     */
    public boolean checkResult(final Map<String, Object> resultMap) {
        try {
            return null != resultMap && null != resultMap.get(SupplierConstant.SUCCESS_KEY) && (Boolean) resultMap.get(SupplierConstant.SUCCESS_KEY);
        } catch (final Exception e) {
            return false;
        }
    }

    
    /**
     * <pre>
     * 从返回结果中获取message
     * </pre>
     *
     * @param resultMap
     * @return
     */
    public String getMessage(final Map<String, Object> resultMap) {
        final String result = "";
        if (null != resultMap && null != resultMap.get(SupplierConstant.MESSAGE_KEY)) {
            return (String) resultMap.get(SupplierConstant.MESSAGE_KEY);
        }
        return result;
    }

    /**
     * <pre>
     * 获取返回的result
     * </pre>
     *
     * @return
     */
    public Map<String, Object> setResult(final Map<String, Object> resultMap) {
        final Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put(SupplierConstant.SUCCESS_KEY, false);
        retMap.put(SupplierConstant.MESSAGE_KEY, getMessage(resultMap));
        return retMap;
    }
 
    /**
     * <pre>
     *
     * </pre>
     *
     * @param skuCodes
     * @return
     */
    private Map<String, SkuInfoResult> generateSkuMap(final List<String> skuCodes, Long suplierId) {
        Map<String, SkuInfoResult> skuInfoMap = null;
        if (null != skuCodes && !skuCodes.isEmpty() ) {
            skuInfoMap = supplierItemProxy.getSkuInfoBySkus( skuCodes, suplierId);
        }
        if (null == skuInfoMap) {
            skuInfoMap = new HashMap<String, SkuInfoResult>();
        }
        return skuInfoMap;
    }
    
        
}
