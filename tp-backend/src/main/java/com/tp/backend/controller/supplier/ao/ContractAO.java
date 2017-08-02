package com.tp.backend.controller.supplier.ao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.supplier.CommonUtil;
import com.tp.common.vo.supplier.ConstractConstant;
import com.tp.common.vo.supplier.ContractTemplateConstant;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.common.vo.supplier.entry.ContractTemplate;
import com.tp.common.vo.supplier.entry.SalesChannels;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.model.sup.ContractAttach;
import com.tp.model.sup.ContractCost;
import com.tp.model.sup.ContractProduct;
import com.tp.model.sup.ContractProperties;
import com.tp.model.sup.ContractQualifications;
import com.tp.model.sup.ContractSettlementRule;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.usr.UserDetail;
import com.tp.proxy.usr.UserDetailProxy;
import com.tp.result.sup.ContractDTO;
import com.tp.service.sup.IAuditRecordsService;
import com.tp.service.sup.IContractProductService;
import com.tp.service.sup.IContractService;
import com.tp.service.sup.ISupplierBrandService;
import com.tp.service.sup.ISupplierCategoryService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.util.NumToWordUtil;

import freemarker.core._RegexBuiltins.replace_reBI;

/**
 * 合同业务逻辑相关 {class_description} <br>
 * Create on : 2014年12月29日 上午11:15:05<br>
 *
 * @author Administrator<br>
 * @version backend-front v0.0.1
 */
@Service
public class ContractAO extends SupplierBaseAO {

    private static final String DATE_FORMATE_8 = "yyyy-MM-dd";

    @Autowired
    private IContractService contractService;

    @Autowired
    private IContractProductService contractProductService;

    @Autowired
    private ISupplierBrandService supplierBrandService;
    
    @Autowired
    private ISupplierCategoryService supplierCategoryService;

    @Autowired
    private IAuditRecordsService auditRecordsService;

    @Autowired
    private UserDetailProxy userDetailProxy;
    
    @Autowired
    private SupplierItemAO supplierItemAO;
    
    @Autowired
    private ISupplierInfoService supplierInfoService;

    // 从页面获取合同的信息
    public Map<String, Object> genContractBaseInfo(final HttpServletRequest request, final Boolean needCheck) {
        final Map<String, Object> retMap = new HashMap<String, Object>();
        final Map<String, Object> resultMap = new HashMap<String, Object>();
        final ContractDTO contractDTO = new ContractDTO();

        /**
         * 生成合同的基本信息
         */
        generateContractBaseInfo(contractDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }

        /**
         * 合同商品的信息
         */
        generateContractProduct(contractDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        /**
         * 结算规则的信息
         */
        generateContractSettlementRule(contractDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }

        /**
         * 生成费用明细的信息
         */
        generateContractCost(contractDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        /**
         * 上传合同附件
         */
        generateContractAttachInfo(contractDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        /**
         * 上传资质证明
         */
        generateContractQualification(contractDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        
        /**
         * 生成合同属性信息
         */
        generateContractProperties(contractDTO, resultMap, request, needCheck);
        
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        
        retMap.put(SupplierConstant.DATA_KEY, contractDTO);
        retMap.put(SupplierConstant.SUCCESS_KEY, true);
        return retMap;
    }

    /**
     * 生成合同属性信息
     * 
     * @param contractDTO
     * @param resultMap
     * @param request
     * @param needCheck
     */
    private void generateContractProperties(ContractDTO contractDTO,
			Map<String, Object> resultMap, HttpServletRequest request,
			Boolean needCheck) {
    	
    	ContractProperties contractProp = new ContractProperties();
    	/**
         * 经办、采购及其联系方式  甲方、乙方
         */
    	//乙方
    	contractProp.setSpLinkType(getStringValue(request, "suppLinkType"));
    	contractProp.setSpLinkName(getStringValue(request, "suppLinkName"));
    	contractProp.setSpMobilePhone(getStringValue(request, "supplierLinkMobile"));
    	contractProp.setSpEmail(getStringValue(request, "supplierLinkEmail"));
    	contractProp.setSpTelephone(getStringValue(request, "supplierLinkTel"));
    	contractProp.setSpFax(getStringValue(request, "supplierLinkFaq"));
    	contractProp.setSpLinkAddress(getStringValue(request, "supplierLinkAddr"));
    	contractProp.setSpQq(getStringValue(request, "supplierLinkQQ"));
    	//甲方
    	contractProp.setXgDeptId(getStringValue(request, "xgLinkType"));
    	contractProp.setXgDeptName(getStringValue(request, "xgLinktypeValue"));
    	contractProp.setXgUserName(getStringValue(request, "xgLinkNameValue"));
    	contractProp.setXgUserId(getStringValue(request, "xgLinkName"));
    	contractProp.setXgEmail(getStringValue(request, "xgLinkEmail"));
    	contractProp.setXgFax(getStringValue(request, "xgLinkFaq"));
    	contractProp.setXgMobilePhone(getStringValue(request, "xgLinkMobile"));
    	contractProp.setXgQq(getStringValue(request, "xgLinkQQ"));
    	contractProp.setXgLinkAddress(getStringValue(request, "xgLinkAddr"));
    	contractProp.setXgTelephone(getStringValue(request, "xgLinkTel"));
    	/**
         * 接收及汇出款项的银行信息
         */
    	contractProp.setBankName(getStringValue(request, "contractBankName"));
    	contractProp.setBankAccount(getStringValue(request, "contractBankAccount"));
    	contractProp.setBankAccName(getStringValue(request, "contractAccountName"));
    	contractProp.setBankCurrency(getStringValue(request, "receBankCurrency"));
        /**
         * 开票信息
         */
    	contractProp.setSpInvoiceName(getStringValue(request, "contracInvoiceName"));
    	contractProp.setSpBankName(getStringValue(request, "kpBank"));
    	contractProp.setSpBankAccount(getStringValue(request, "kpAccount"));
    	contractProp.setSpBankAccName(getStringValue(request, "kpAccountName"));
    	contractProp.setSpTaxpayerCode(getStringValue(request, "taxpayerCode"));
    	contractProp.setSpLinkPhone(getStringValue(request, "kpTel"));
    	contractProp.setSpInvoiceLinkAddress(getStringValue(request, "kpAddress"));
    	
    	contractProp.setCreateTime(new Date());
    	contractProp.setUpdateTime(new Date());
    	contractProp.setCreateUser(SupplierUtilAO.getCurrentUserName());
    	contractProp.setUpdateUser(contractProp.getCreateUser());
    	contractProp.setStatus(Constant.ENABLED.YES);
    	contractDTO.setContractProperties(contractProp);
    	
	}

	/**
     * 生成合同的基本信息
     */
    public void generateContractBaseInfo(final ContractDTO contractDTO, final Map<String, Object> resultMap,
        final HttpServletRequest request, final Boolean needCheck) {

        /** 签约日期 开始日期 结束日期 */
        final Date signingDate = getDate(request, "signingDate", DATE_FORMATE_8);
        final Date startDate = getDate(request, "startDate", DATE_FORMATE_8);
        final Date endDate = getDate(request, "endDate", DATE_FORMATE_8);
        Long cId = getLongValue(request, "cid");
        Long supplierId = getLongValue(request, "supplierId");
        SupplierInfo supplier = supplierInfoService.queryById(supplierId);
        if(null == supplier){
        	resultMap.put(SupplierConstant.SUCCESS_KEY, false);
        	resultMap.put(SupplierConstant.MESSAGE_KEY, "找不到供应商信息。");
        	return;
        }
        
        if (cId != null) {
            contractDTO.setId(cId);		
		}
        contractDTO.setSupplierId(getLongValue(request, "supplierId"));
        contractDTO.setSupplierName(getStringValue(request, "supplierName"));
        contractDTO.setContractType(getStringValue(request, "contractType"));
        contractDTO.setContractName(getStringValue(request, "contractName"));
        contractDTO.setContractDesc(getStringValue(request, "contractDesc"));
        contractDTO.setSigningDate(signingDate);
        contractDTO.setStartDate(startDate);
        contractDTO.setEndDate(endDate);

        contractDTO.setContractorId(getLongValue(request, "contractorId"));
        contractDTO.setContractorName(getStringValue(request, "contractorName"));
        contractDTO.setContractorDeptId(getStringValue(request, "contractorDeptId"));
        contractDTO.setContractorDeptName(getStringValue(request, "contractorDeptName"));
        contractDTO.setAuditStatus(AuditStatus.EDITING.getStatus());
        //保证金  币种
        contractDTO.setCash(getDoubleValue(request, "contractDeposit"));
        contractDTO.setCurrency(getStringValue(request, "currency"));
        //合同主体
        contractDTO.setContractXg(getStringValue(request, "contractPartyA"));
        //合同模板
        String contractTemplate = getContractTemplate(supplier.getSupplierType(), supplier.getIsSea());
        contractDTO.setTemplateType(contractTemplate);
        contractDTO.setTemplateName(ConstractConstant.CONTRACT_TEMPLATE_MAP.get(contractTemplate));
        //销售渠道
        contractDTO.setSalesChannels(getSalesChannel(request));
        //是否协议合同
        if("1".equals(getStringValue(request, "isAgreementContract"))){
        	//是协议合同
        	contractDTO.setIsAgreementContract(Constant.ENABLED.YES);
        	contractDTO.setAgreementContractUrl(getStringValue(request, "agreementContract"));
        } else {
        	contractDTO.setIsAgreementContract(Constant.ENABLED.NO);
        }
        
        contractDTO.setStatus(Constant.ENABLED.YES);
        final Date createTime = new Date();
        contractDTO.setCreateTime(createTime);
        contractDTO.setUpdateTime(createTime);

        contractDTO.setCreateUser(SupplierUtilAO.getCurrentUserName());
        contractDTO.setUpdateUser(SupplierUtilAO.getCurrentUserName());
        resultMap.put(SupplierConstant.SUCCESS_KEY, true);
    }
    
    /**
     * 获取销售渠道
     * 
     * @param request
     * @return
     */
    private String getSalesChannel(HttpServletRequest request) {
    	Long channel = 0L;
    	Long[] salesChannels = getLongValues(request, "salesWay");
    	if(null != salesChannels && salesChannels.length>0){
    		for(Long salesChannel : salesChannels) {
    			if(null != salesChannel){
    				channel = channel + salesChannel;
    			}
    		}
    	}
		return CommonUtil.getMinIntegerDigits(channel, 6);
	}
   
    
	/**
     * 根据供应商类型获取合同模板
     * 
     * @param supplierType
     * @param isHaotao
     * @return
     */
    public String getContractTemplate(String supplierType,Integer isHaotao){
    	if(Constant.DEFAULTED.YES.equals(isHaotao)){
    		if(SupplierType.PURCHASE.getValue().equals(supplierType) || SupplierType.SELL.getValue().equals(supplierType)){
    			return ContractTemplate.HTZY.getKey();
    		} else if(SupplierType.ASSOCIATE.getValue().equals(supplierType)) {
    			return ContractTemplate.HTBS.getKey();
    		} else {
    			return null;
    		}
    	} else {
    		if(SupplierType.PURCHASE.getValue().equals(supplierType) || SupplierType.SELL.getValue().equals(supplierType)){
    			return ContractTemplate.PLRC.getKey();
    		} else if(SupplierType.ASSOCIATE.getValue().equals(supplierType)) {
    			return ContractTemplate.PLDF.getKey();
    		} else {
    			return null;
    		}
    	}
    }

    /**
     * 生成合同商品信息
     */
    private void generateContractProduct(final ContractDTO contractDTO, final Map<String, Object> resultMap,
        final HttpServletRequest request, final Boolean needCheck) {
        final List<ContractProduct> cList = new ArrayList<ContractProduct>();

        final Long[] brandIds = getLongValues(request, "brandId");
        final String[] brandNames = getStringValues(request, "brandName");
        final String[] bigNames = getStringValues(request, "bigName");
        final Long[] bigId = getLongValues(request, "bigId");
        final String[] midNames = getStringValues(request, "midName");
        final Long[] minId = getLongValues(request, "midId");
        final String[] smallNames = getStringValues(request, "smallName");
        final Long[] smallId = getLongValues(request, "smallId");
        final BigDecimal[] commissions = getBigDecimalValues(request, "commission");
        if (null != brandIds && brandIds.length > 0) {
            for (int i = 0; i < brandIds.length; i++) {
                final ContractProduct contractProduct = new ContractProduct();
                contractProduct.setBrandId(brandIds[i]);
                contractProduct.setBrandName(brandNames[i]);
                if (bigNames[i]==null) {
                    contractProduct.setBigName("");
				}else {
	                contractProduct.setBigName(bigNames[i]);					
				}
                if (midNames[i]==null) {
                    contractProduct.setMidName("");
				}else {
	                contractProduct.setMidName(midNames[i]);					
				}
                if (smallNames[i]==null) {
                    contractProduct.setSmallName("");
				}else {
	                contractProduct.setSmallName(smallNames[i]);					
				}
                contractProduct.setCommission(commissions[i].doubleValue());
                if (bigId[i] == null) {
	                contractProduct.setBigId( 0L );										
				}else {
	                contractProduct.setBigId(bigId[i]);					
				}
                if (minId[i]==null) {
                    contractProduct.setMidId( 0L );					
				}else {
	                contractProduct.setMidId(minId[i]);
				}
                if (smallId[i]==null) {
                    contractProduct.setSmallId(0L);
				}else {
	                contractProduct.setSmallId(smallId[i]);	
				}
                contractProduct.setCreateTime(new Date());
                contractProduct.setUpdateTime(new Date());
                contractProduct.setCreateUser(SupplierUtilAO.getCurrentUserName());
                contractProduct.setUpdateUser(SupplierUtilAO.getCurrentUserName());
                contractProduct.setStatus(Constant.ENABLED.YES);
                cList.add(contractProduct);
            }
        }
        contractDTO.setContractProductList(cList);
    }
    /**
     * 生成资质证明的信息
     */
    public void generateContractQualification(final ContractDTO contractDTO, final Map<String, Object> resultMap,
        final HttpServletRequest request, final Boolean needCheck) {
        final List<ContractQualifications> contractQualificationsDTOList = new ArrayList<ContractQualifications>();
        final Long[] brandId = getLongValues(request, "brandId");
        final String[] brandName = getStringValues(request, "brandName");
        final String[] bigNames = getStringValues(request, "bigName");
        final Long[] bigId = getLongValues(request, "bigId");
        final String[] midNames = getStringValues(request, "midName");
        final Long[] minId = getLongValues(request, "midId");
        final String[] smallName = getStringValues(request, "smallName");
        final Long[] smallId = getLongValues(request, "smallId");

        if (null != brandId && brandId.length > 0) {
        	Set<String> brandCategorySet = new HashSet<String>();
            for (int i = 0; i < brandId.length; i++) {
                final Long brandIdSel = brandId[i];
                final Long bigIdSel = bigId[i];
                final String checkboxName = "quaitionAttach_" + bigIdSel + "_" + brandIdSel;
                if(brandCategorySet.contains(checkboxName)){
                	continue;
                }
                brandCategorySet.add(checkboxName);
                final Long paperSels[] = getLongValues(request, checkboxName);
                if (null != paperSels && paperSels.length > 0) {
                    for (final Long paperSel : paperSels) {
                        final String endTag = bigIdSel + "_" + brandIdSel + "_" + paperSel;
                        final String fileName = "qualityFile" + endTag;
                        final String quaName = "quaName" + endTag;
                        final String oldUrl = "beforeUrl" + endTag;
                        final String quanameSel = getStringValue(request, quaName);
                        String quaUrl = fileName;
                        final String oldUrlSub = getStringValue(request, oldUrl);
                        if ((null == quaUrl || "".equals(quaUrl)) && null != oldUrlSub && !"".equals(oldUrlSub)) {
                            quaUrl = oldUrlSub;
                        }
                        final ContractQualifications contractQualificationsDTO = new ContractQualifications();
                        // contractQualificationsDTO.setProductId(productId[i]);
                        contractQualificationsDTO.setBrandId(brandIdSel);
                        contractQualificationsDTO.setBrandName(brandName[i]);
                        
                        contractQualificationsDTO.setBigId(bigId[i]);
                        contractQualificationsDTO.setBigName(bigNames[i]);
                        
                        contractQualificationsDTO.setMidId(minId[i]);
                        contractQualificationsDTO.setMidName(midNames[i]);
                        
                        contractQualificationsDTO.setSmallId(smallId[i]);
                        contractQualificationsDTO.setSmallName(smallName[i]);

                        contractQualificationsDTO.setPapersId("" + paperSel);
                        contractQualificationsDTO.setPapersName(quanameSel);
                        contractQualificationsDTO.setUrl(quaUrl);

                        contractQualificationsDTO.setStatus(Constant.ENABLED.YES);
                        contractQualificationsDTO.setCreateTime(new Date());
                        contractQualificationsDTO.setUpdateTime(new Date());
                        contractQualificationsDTO.setCreateUser(SupplierUtilAO.getCurrentUserName());
                        contractQualificationsDTO.setUpdateUser(SupplierUtilAO.getCurrentUserName());
                        contractQualificationsDTOList.add(contractQualificationsDTO);
                    }
                }
            }
        }
        contractDTO.setContractQualificationsList(contractQualificationsDTOList);
    }

    /**
     * 生成结算规则的信息
     */
    public void generateContractSettlementRule(final ContractDTO contractDTO, final Map<String, Object> resultMap,
        final HttpServletRequest request, final Boolean needCheck) {
        final List<ContractSettlementRule> contractSettlementRuleList = new ArrayList<ContractSettlementRule>();
        final Integer[] dayBefore = getIntegerVals(request, "day");
        final BigDecimal[] percents = getBigDecimals(request, "percent");
        final String[] dayTypes = getStringValues(request, "dayType");
        final String ruleType = getStringValue(request, "ruleType");
        final String frequence = getStringValue(request, "frequence");
        if (null != dayBefore && dayBefore.length > 0) {
            for (int i = 0; i < dayBefore.length; i++) {
                final ContractSettlementRule contractSettlementRuleDTO = new ContractSettlementRule();
                contractSettlementRuleDTO.setStatus(Constant.ENABLED.YES);
                contractSettlementRuleDTO.setDay(dayBefore[i] + "");
                contractSettlementRuleDTO.setPercent(percents[i] + "");
                contractSettlementRuleDTO.setDayType(dayTypes[i]);
                contractSettlementRuleDTO.setRuleType(ruleType);
                contractSettlementRuleDTO.setFrequence(frequence);

                contractSettlementRuleDTO.setCreateTime(new Date());
                contractSettlementRuleDTO.setUpdateTime(new Date());
                contractSettlementRuleDTO.setCreateUser(SupplierUtilAO.getCurrentUserName());
                contractSettlementRuleDTO.setUpdateUser(SupplierUtilAO.getCurrentUserName());
                contractSettlementRuleList.add(contractSettlementRuleDTO);
            }
        }
        contractDTO.setContractSettlementRuleList(contractSettlementRuleList);
    }

    /**
     * 生成费用明细的信息
     */
    public void generateContractCost(final ContractDTO contractDTO, final Map<String, Object> resultMap,
        final HttpServletRequest request, final Boolean needCheck) {
        final List<ContractCost> contractCostDTOList = new ArrayList<ContractCost>();
        /** 获取类型 */
        final String[] type = getStringValues(request, "costType");
        /** 获取费用 */
        final BigDecimal[] values = getBigDecimals(request, "value");
        /** 获取币种 */
        final String[] currency = getStringValues(request, "currency");
        /** 获取增减值 */
        /*
         * BigDecimal changevalue = getRateInfo(request, "changeValue");
         */

        if (null != type && type.length > 0) {
            for (int i = 0; i < type.length; i++) {
                final ContractCost contractCostDTO = new ContractCost();
                /*
                 * if (type[i].equals("Deposit")) { contractCostDTO.setChangeValue(changevalue); } if
                 * (type[i].equals("Royalties")) { contractCostDTO.setChangeValue(null); }
                 */
                contractCostDTO.setType(type[i]);
                contractCostDTO.setValue(values[i].doubleValue());
                contractCostDTO.setStatus(Constant.ENABLED.YES);
                contractCostDTO.setCurrency(currency[i]);
                contractCostDTO.setCreateTime(new Date());
                contractCostDTO.setUpdateTime(new Date());
                contractCostDTO.setCreateUser(SupplierUtilAO.getCurrentUserName());
                contractCostDTO.setUpdateUser(SupplierUtilAO.getCurrentUserName());
                contractCostDTOList.add(contractCostDTO);
            }
        }
        contractDTO.setContractCostList(contractCostDTOList);
    }

    /**
     * 生成合同附件信息
     */
    public void generateContractAttachInfo(final ContractDTO contractDTO, final Map<String, Object> resultMap,
        final HttpServletRequest request, final Boolean needCheck) {
        final List<ContractAttach> contractAttachDTOList = new ArrayList<ContractAttach>();
        /** 获取合同附件的名称 */
        final String name = getStringValue(request, "name");
        /** 获取合同附件的路径 */
        final String url = getStringValue(request, "url");

        final ContractAttach contractAttachDTO = new ContractAttach();
        contractAttachDTO.setName(name);
        contractAttachDTO.setUrl(url);
        contractAttachDTO.setStatus(Constant.ENABLED.YES);
        contractAttachDTO.setCreateTime(new Date());
        contractAttachDTO.setUpdateTime(new Date());
        contractAttachDTO.setCreateUser(SupplierUtilAO.getCurrentUserName());
        contractAttachDTO.setUpdateUser(SupplierUtilAO.getCurrentUserName());
        contractAttachDTOList.add(contractAttachDTO);
        contractDTO.setContractAttachList(contractAttachDTOList);
    }

	
	/**
	 * 生成合同code
	 * 
	 * @param supplierType
	 * @param isHaitao
	 * @return
	 */
	public String generateContractCode(String supplierType,Integer isHaitao) {
		String templateType = getContractTemplate(supplierType, isHaitao);
		return contractService.generateContractCode(templateType);
	}

	/**
	 * 生成合同模板设置的map
	 * @param contractVO 
	 * @param isDownload
	 * 
	 * @return
	 */
	private Map<String, String> generatContractMap(ContractDTO contractVO, boolean isDownload) {
		
		Map<String,String> contractMap = new HashMap<String,String>();
		ContractProperties contractProp = contractVO.getContractProperties();
		SupplierInfo supplier = supplierInfoService.queryById(contractVO.getSupplierId());
		if(null == supplier || null == contractProp){
			return contractMap;
		}
		
		List<ContractProduct> contractProducts = contractVO.getContractProductList();
		
		Calendar calendar = Calendar.getInstance();
		/**合同名称*/
		contractMap.put(ContractTemplateConstant.CONTRACT_TITLE , contractVO.getContractName());
		contractMap.put(ContractTemplateConstant.CONTRACT_CODE , contractVO.getContractCode());
		
		if(null != contractVO.getSigningDate()){
			calendar.setTime(contractVO.getSigningDate());
			/**合同签约年份*/
			contractMap.put(ContractTemplateConstant.CONTRACT_SIGN_YEAR,calendar.get(Calendar.YEAR)+"");
			/**合同签约月份*/
			contractMap.put(ContractTemplateConstant.CONTRACT_SIGN_MONTH,calendar.get(Calendar.MONTH)+1+"");
			/**签约天*/
			contractMap.put(ContractTemplateConstant.CONTRACT_SIGN_DAY,calendar.get(Calendar.DAY_OF_MONTH)+"");
		} else {
			/**合同签约年份*/
			contractMap.put(ContractTemplateConstant.CONTRACT_SIGN_YEAR,"");
			/**合同签约月份*/
			contractMap.put(ContractTemplateConstant.CONTRACT_SIGN_MONTH,"");
			contractMap.put(ContractTemplateConstant.CONTRACT_SIGN_DAY,"");
		}
		
		/**乙方名称*/
		contractMap.put(ContractTemplateConstant.PARTY_B_NAME,contractVO.getSupplierName());
		/**乙方地址*/
		contractMap.put(ContractTemplateConstant.PARTY_B_ADDRESS,contractProp.getSpLinkAddress());
		/**乙方保证金*/
		if(null != contractVO.getCash()){
			String moneyCash = NumToWordUtil.number2CNMontrayUnit(new BigDecimal(contractVO.getCash()));
			if(null != moneyCash && moneyCash.endsWith("元")){
				moneyCash = moneyCash.substring(0,moneyCash.length()-1);
			}
			contractMap.put(ContractTemplateConstant.DEPOSIT_MONEY,moneyCash);
		} else {
			contractMap.put(ContractTemplateConstant.DEPOSIT_MONEY,"");
		}
		/**结算规则*/
		String settleRule = generateSettleRule(contractVO.getContractSettlementRuleList());
		int wordCountPreLine = 48;
		int totalSize = settleRule.length();
		int lineSize = totalSize % wordCountPreLine == 0 ? totalSize / wordCountPreLine : totalSize / wordCountPreLine + 1;
		for(int i=0;i<lineSize;i++){
			int endIndex = (i + 1) * wordCountPreLine < totalSize ? (i + 1) * wordCountPreLine : totalSize;
			contractMap.put(ContractTemplateConstant.SETTLE_RULE+i,settleRule.substring(i*wordCountPreLine,endIndex));
		}
		
		if(null != contractVO.getStartDate()){
			calendar.setTime(contractVO.getStartDate());
			/**合同开始年份*/
			contractMap.put(ContractTemplateConstant.CONTRACT_START_YEAR,calendar.get(Calendar.YEAR)+"");
			/**合同开始月份*/
			contractMap.put(ContractTemplateConstant.CONTRACT_START_MONTH,calendar.get(Calendar.MONTH)+1+"");
			/**合同开始天*/
			contractMap.put(ContractTemplateConstant.CONTRACT_START_DAY,calendar.get(Calendar.DAY_OF_MONTH)+"");
		} else {
			/**合同开始年份*/
			contractMap.put(ContractTemplateConstant.CONTRACT_START_YEAR,"");
			/**合同开始月份*/
			contractMap.put(ContractTemplateConstant.CONTRACT_START_MONTH,"");
			/**合同开始天*/
			contractMap.put(ContractTemplateConstant.CONTRACT_START_DAY,"");
		}
		
		if(null != contractVO.getEndDate()){
			calendar.setTime(contractVO.getEndDate());
			/**合同结束年份*/
			contractMap.put(ContractTemplateConstant.CONTRACT_END_YEAR,calendar.get(Calendar.YEAR)+"");
			/**合同结束月份*/
			contractMap.put(ContractTemplateConstant.CONTRACT_END_MONTH,calendar.get(Calendar.MONTH)+1+"");
			/**合同结束天*/
			contractMap.put(ContractTemplateConstant.CONTRACT_END_DAY,calendar.get(Calendar.DAY_OF_MONTH)+"");
		} else {
			/**合同结束年份*/
			contractMap.put(ContractTemplateConstant.CONTRACT_END_YEAR,"");
			/**合同结束月份*/
			contractMap.put(ContractTemplateConstant.CONTRACT_END_MONTH,"");
			/**合同结束天*/
			contractMap.put(ContractTemplateConstant.CONTRACT_END_DAY,"");
		}
		
		/**公司名称--附件*/
		contractMap.put(ContractTemplateConstant.COMPANY_NAME,contractVO.getSupplierName());
		/**供应商联系地址--附件*/
		contractMap.put(ContractTemplateConstant.SUPPLIER_LINK_ADDRESS,contractProp.getSpLinkAddress());
		/**供应商法人--附件*/
		contractMap.put(ContractTemplateConstant.LAWER_PEOPLE,supplier.getLegalPerson());
		/**供应商联系人名称--附件*/
		contractMap.put(ContractTemplateConstant.SUPPLIER_LINK_NAME,contractProp.getSpLinkName());
		/**供应商联系人电话--附件*/
		contractMap.put(ContractTemplateConstant.SUPPLIER_LINK_TEL,contractProp.getSpTelephone());
		/**供应商联系人email--附件*/
		contractMap.put(ContractTemplateConstant.SUPPLIER_LINK_EMAIL,contractProp.getSpEmail());
		/**供应商联系人传真--附件*/
		contractMap.put(ContractTemplateConstant.SUPPLIER_LINK_FAX,contractProp.getSpFax());
		/**开户名称--附件收款*/
		contractMap.put(ContractTemplateConstant.SP_BANK_ACCOUNT_NAME,contractProp.getBankAccName());
		/**供应商名称--附件收款*/
		contractMap.put(ContractTemplateConstant.SP_BANK_NAME,contractProp.getBankName());
		/**银行账号--附件收款*/
		contractMap.put(ContractTemplateConstant.SP_BANK_ACCOUNT,contractProp.getBankAccount());
		/**开票名称--附件开票*/
		contractMap.put(ContractTemplateConstant.SP_KP_NAME,contractProp.getSpInvoiceName());
		/**纳税人识别码--附件开票*/
		contractMap.put(ContractTemplateConstant.SP_KP_TAXIDENTIFY,contractProp.getSpTaxpayerCode());
		/**地址和电话--附件开票*/
		contractMap.put(ContractTemplateConstant.SP_KP_ADDANDTEL,contractProp.getSpInvoiceLinkAddress()+" "+contractProp.getSpLinkPhone());
		/**开户银行以及账户--附件开票*/
		contractMap.put(ContractTemplateConstant.SP_KP_BANDANDACCOUNT,contractProp.getSpBankName()+" "+contractProp.getSpBankAccount());
		/**经营品牌--附件开票*/
		contractMap.put(ContractTemplateConstant.SP_ITEM_BRAND,generateContractBrandNames(contractProducts));
		/**经营分类--附件开票*/
		contractMap.put(ContractTemplateConstant.SP_ITEM_CATEGORY,generateContractCategoryNames(contractProducts));
		/**经营品牌分类--附件3*/
		contractMap.put(ContractTemplateConstant.SP_ITEM_CATEGORY_BRAND,generateContractBrandCategoryNames(contractProducts));
		
		/**西客商城联系人*/
		contractMap.put(ContractTemplateConstant.XG_LINK_NAME , contractProp.getXgUserName());
		/**西客商城联系手机*/
		contractMap.put(ContractTemplateConstant.XG_LINK_MOBILE , contractProp.getXgMobilePhone());
		/**西客商城联系email*/
		contractMap.put(ContractTemplateConstant.XG_LINK_EMAIL , contractProp.getXgEmail());
		/**西客商城联系电话*/
		contractMap.put(ContractTemplateConstant.XG_LINK_TEL , contractProp.getXgTelephone());
		/**西客商城联系传真*/
		contractMap.put(ContractTemplateConstant.XG_FAX , contractProp.getXgFax());
		/**西客商城联系QQ*/
		contractMap.put(ContractTemplateConstant.XG_LINK_QQ , contractProp.getXgQq());
		/**西客商城联系地址*/
		contractMap.put(ContractTemplateConstant.XG_LINK_ADDRESS , contractProp.getXgLinkAddress());
		/**供应商联系电话*/
		contractMap.put(ContractTemplateConstant.SUPPLIER_LINK_MOBILE , contractProp.getSpMobilePhone());
		/**供应商联系QQ*/
		contractMap.put(ContractTemplateConstant.SUPPLIER_LINK_QQ , contractProp.getSpQq());
		
		/**合同主体*/
		contractMap.put(ContractTemplateConstant.PARTY_A_TITLE , ConstractConstant.CONTRACT_PARTY_A_MAP.get(contractVO.getContractXg()));
		
		
		/**西客商城银行开户名称*/
		contractMap.put(ContractTemplateConstant.XG_BANK_ACCOUNT_NAME , ConstractConstant.BANK_ACCOUNT_NAME_PARTY_A_MAP.get(contractVO.getContractXg()));
		/**西客商城银行名称*/
		contractMap.put(ContractTemplateConstant.XG_BANK_NAME , ConstractConstant.BANK_NAME_PARTY_A_MAP.get(contractVO.getContractXg()));
		/**西客商城银行账号*/
		contractMap.put(ContractTemplateConstant.XG_BANK_ACCOUNT , ConstractConstant.BANK_ACCOUNT__PARTY_A_MAP.get(contractVO.getContractXg()));
		String salesChannel = generateSalesChannels(contractVO.getSalesChannels());
		if(salesChannel.length()>5){
			contractMap.put(ContractTemplateConstant.SALES_CHANNEL+0, salesChannel.substring(0,5));
			contractMap.put(ContractTemplateConstant.SALES_CHANNEL+1, salesChannel.substring(5,salesChannel.length()));
		}else {
			contractMap.put(ContractTemplateConstant.SALES_CHANNEL+0, salesChannel);
		}
		
		if(isDownload) {
			contractMap.put(ContractTemplateConstant.XG_SIGN_NAME, contractVO.getContractorName());
    		contractMap.put(ContractTemplateConstant.XG_SIGN_EMAIL, contractVO.getContractorEmail());
    		contractMap.put(ContractTemplateConstant.XG_SIGN_TEL, contractVO.getContractorPhone());
			
			contractMap.put(ContractTemplateConstant.BASE_ADDRESS , contractProp.getBaseLinkAddress());
			contractMap.put(ContractTemplateConstant.BASE_LINK_NAME , contractProp.getBaseLinkName());
			contractMap.put(ContractTemplateConstant.BASE_LINK_EMAIL , contractProp.getBaseEmail());
			contractMap.put(ContractTemplateConstant.BASE_LINK_FAX , contractProp.getBaseFax());
			contractMap.put(ContractTemplateConstant.BASE_LAWER, contractProp.getBaseLegalPerson());
			contractMap.put(ContractTemplateConstant.BASE_LINK_TEL, contractProp.getBaseLinkPhone());
		} else {
    		try {
    			Map<String,Object> params = new HashMap<String,Object>();
    			params.put("userId", contractVO.getContractorId());
				UserDetail userDetail = userDetailProxy.queryUniqueByParams(params).data;
	        	if(null != userDetail){
	        		contractMap.put(ContractTemplateConstant.XG_SIGN_NAME, contractVO.getContractorName());
	        		contractMap.put(ContractTemplateConstant.XG_SIGN_EMAIL, userDetail.getEmail());
	        		contractMap.put(ContractTemplateConstant.XG_SIGN_TEL, userDetail.getFixedPhone());
	        	}
	    	} catch(Exception e){
	    		LOGGER.error(e.getMessage(),e);
	    	}
    		
			contractMap.put(ContractTemplateConstant.BASE_ADDRESS , supplier.getAddress());
			contractMap.put(ContractTemplateConstant.BASE_LINK_NAME , supplier.getLinkName());
			contractMap.put(ContractTemplateConstant.BASE_LINK_EMAIL , supplier.getEmail());
			contractMap.put(ContractTemplateConstant.BASE_LINK_FAX , supplier.getFax());
			contractMap.put(ContractTemplateConstant.BASE_LAWER,supplier.getLegalPerson());
			contractMap.put(ContractTemplateConstant.BASE_LINK_TEL,supplier.getPhone());
		}
		
		return contractMap;
	}
	
	/**
	 * 生成销售渠道
	 * 
	 * @param salesChannels
	 * @return
	 */
	private String generateSalesChannels(String salesChannels){
		String retStr = "";
		try {
			StringBuffer sb = new StringBuffer("");
			Integer salesChannelInt = Integer.parseInt(salesChannels);
			int currentVal = salesChannelInt;
			
			for(SalesChannels salesChannel : SalesChannels.values()){
				if((currentVal & salesChannel.getValue()) != 0){
					sb.append("“");
					sb.append(salesChannel.getName());
					sb.append("”");
					sb.append("、");
				}
			}
			retStr = sb.toString();
			if(retStr.endsWith("、")){
				retStr = retStr.substring(0,retStr.length()-1);
			}
		} catch(Exception e){
			LOGGER.error(e.getMessage(),e);
		}
		return retStr + "等渠道。";
		
	}
	
	/**
	 * 生成品牌以及分类
	 * 
	 * @param contractProducts
	 * @return
	 */
	private String generateContractBrandCategoryNames(
			List<ContractProduct> contractProducts) {
		String retStr = null;
		StringBuffer sb = new StringBuffer("");
		if(CollectionUtils.isNotEmpty(contractProducts)){
			for (int i = 0; i < contractProducts.size(); i++) {
				ContractProduct productVO = contractProducts.get(i);
				sb.append(productVO.getBrandName());
				sb.append(getLeafCategoryName(productVO));
				sb.append(" ");
			}
		}
		retStr = sb.toString();
		if(retStr.endsWith(" ")){
			return retStr.substring(0,retStr.length()-1);
		} else {
			return retStr;
		}
	}

	/**
	 * 生成结算规则文本
	 * 
	 * @param contractSettlementRuleVOList
	 * @return
	 */
	private String generateSettleRule(
			List<ContractSettlementRule> contractSettlementRuleVOList) {
		StringBuilder sb = new StringBuilder("     3.3.3 结算节点如下：");
		String appendBeforeHalf = "销售活动档期结束后";
		String unitTag = "个";
		
		if(CollectionUtils.isNotEmpty(contractSettlementRuleVOList)){
			int len = contractSettlementRuleVOList.size();
			for (int i = 0; i < len; i++) {
				ContractSettlementRule ruleVO = contractSettlementRuleVOList.get(i);
				if(i == len -1){
					sb.append(ruleVO.getDay())
					  .append(unitTag)
					  .append(SupplierConstant.SETTLEMENTRULEDAYTYPE_MAP.get(ruleVO.getDayType()))
					  .append("结算剩余的").append(ruleVO.getPercent()).append("%。");
				} else {
					sb.append(appendBeforeHalf)
					  .append(ruleVO.getDay())
					  .append(unitTag)
					  .append(SupplierConstant.SETTLEMENTRULEDAYTYPE_MAP.get(ruleVO.getDayType()))
					  .append("结算当期商品的").append(ruleVO.getPercent()).append("%；");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 合同的商品品牌
	 * 
	 * @param contractProducts
	 * @return
	 */
	private String generateContractBrandNames(List<ContractProduct> contractProducts) {
		StringBuffer sb = new StringBuffer("");
		if (CollectionUtils.isNotEmpty(contractProducts)) {
			Set<String> brandSet = new HashSet<String>();
			for (ContractProduct contractProductVO : contractProducts) {
				if(!brandSet.contains(contractProductVO.getBrandName())){
					brandSet.add(contractProductVO.getBrandName());
					sb.append(contractProductVO.getBrandName()).append(" ");
				}
			}
		}
		String retStr = sb.toString();
		if(retStr.endsWith(" ")){
			retStr = retStr.substring(0,retStr.length()-1);
		}
		return retStr;
	}
	

	/**
	 * 合同的商品分类
	 * 
	 * @param contractProducts
	 * @return
	 */
	private String generateContractCategoryNames(List<ContractProduct> contractProducts) {
		StringBuffer sb = new StringBuffer("");
		if (CollectionUtils.isNotEmpty(contractProducts)) {
			Set<String> brandSet = new HashSet<String>();
			for (ContractProduct contractProductVO : contractProducts) {
				String cName = getLeafCategoryName(contractProductVO);
				if(StringUtils.isNotBlank(cName) && !brandSet.contains(cName)){
					brandSet.add(cName);
					sb.append(cName).append(" ");
				}
			}
		}
		String retStr = sb.toString();
		if(retStr.endsWith(" ")){
			retStr = retStr.substring(0,retStr.length()-1);
		}
		return retStr;
	}

	/**
	 * 获取叶子分类
	 * 
	 * @param contractProductVO
	 * @return
	 */
	private String getLeafCategoryName(ContractProduct contractProductVO) {
		String cName = null;
		if(StringUtils.isNotBlank(contractProductVO.getSmallName())){
			cName = contractProductVO.getSmallName();
		} else if(StringUtils.isNotBlank(contractProductVO.getMidName())){
			cName = contractProductVO.getMidName();
		} else if(StringUtils.isNotBlank(contractProductVO.getBigName())){
			cName = contractProductVO.getBigName();
		}
		return cName;
	}

}
