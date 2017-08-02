package com.tp.proxy.sup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.supplier.ConstractConstant;
import com.tp.common.vo.supplier.ContractTemplateConstant;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.common.vo.supplier.entry.BillType;
import com.tp.common.vo.supplier.entry.ContractTemplate;
import com.tp.common.vo.supplier.entry.SalesChannels;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.DictionaryInfo;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.Contract;
import com.tp.model.sup.ContractProduct;
import com.tp.model.sup.ContractProperties;
import com.tp.model.sup.ContractQualifications;
import com.tp.model.sup.ContractSettlementRule;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.usr.UserDetail;
import com.tp.proxy.BaseProxy;
import com.tp.result.sup.ContractDTO;
import com.tp.service.IBaseService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.sup.IContractService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.service.usr.IUserDetailService;
import com.tp.util.NumToWordUtil;
/**
 * 合同主表代理层
 * @author szy
 *
 */
@Service
public class ContractProxy extends BaseProxy<Contract>{

	@Autowired
	private IContractService contractService;
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private ISupplierInfoService supplierInfoService;
	@Autowired
	private SupplierInfoProxy supplierInfoProxy;
	@Autowired
	private IUserDetailService userDetailService;

	@Override
	public IBaseService<Contract> getService() {
		return contractService;
	}

	public ContractDTO getContractById(Long contractId) {
		ContractDTO contractDTO = contractService.getContractByIdAll(contractId);
		setContractLicenceInfo(contractDTO);
		groupContractProductByBigcidAndBrand(contractDTO);
		return contractDTO;
	}

	public ResultInfo<Long> saveContractBaseInfo(ContractDTO contractDTO) {
		try{
			ResultInfo<Long> resultInfo = contractService.saveContractBaseInfo(contractDTO);
			return resultInfo;
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,contractDTO);
			return new ResultInfo<>(failInfo);
		}
	}

	public ResultInfo<Long> updateContractBaseInfo(ContractDTO contractDTO) {
		try{
			ResultInfo<Long> resultInfo = contractService.updateContractBaseInfo(contractDTO);
			return resultInfo;
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,contractDTO);
			return new ResultInfo<>(failInfo);
		}
	}

	public void previewConract(Long contractId) {
	    /**询对应的合同*/
		ContractDTO contractDTO = getContractByIdAll(contractId);
		if(null == contractDTO) {
			logger.info("Can not find contract info with id : {}",contractId);
			return;
		}
		Map<String,String> contractMap = generatContractMap(contractDTO, false);
		
		//List<PageTemplate> templateList = null;
		
		if(ContractTemplate.PLRC.getKey().equals(contractDTO.getTemplateType())){
			//templateList = generateTemplate(contractDTO.getContractProductList(),contractDTO.getSupplierName(),contractDTO.getStartDate());
		}
		
		/**合同预览
		String pdfTemplateFilePath = ConstractConstant.CONTRACT_TEMPLATE_FILE_MAP.get(contractDTO.getTemplateType());
		try {
			PdfStamperUtil.renderCopyMergedOutputModel(contractMap, templateList, pdfTemplateFilePath);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);
		}
		*/
	}
	
	 /**
     * 查询全部的合同信息
     *
     * @param cId
     * @return
     */
    public ContractDTO getContractByIdAll(final Long cid) {
        ContractDTO contractDTO = contractService.getContractByIdAll(cid);
        setContractLicenceInfo(contractDTO);
        groupContractProductByBigcidAndBrand(contractDTO);
        return contractDTO;
    }
    /**
     * 设置合同的资质信息
     */
    private void setContractLicenceInfo(final ContractDTO contractDTO) {
        final List<ContractProduct> contractProducts = contractDTO.getContractProductList();
        setQuolificationMap(contractDTO, contractDTO.getContractQualificationsList());
        final Map<String, List<ContractQualifications>> quaMap = contractDTO.getContractQualificationsMap();
        if (null != contractProducts && contractProducts.size() > 0) {
            final List<Long> cidList = new ArrayList<Long>();
            for (final ContractProduct quaVO : contractProducts) {
            	if(null == quaVO.getBigId()){
            		continue;
            	}
                final String key = quaVO.getBigId() + "_" + quaVO.getBrandId();
                if (quaMap.containsKey(key)) {
                    cidList.add(quaVO.getBigId());
                }
            }
            setQualificationHasChecked(cidList, quaMap);
        }
    }
    /**
     * 设置map的值
     *
     * @param contractDTO
     * @param contractQualificationsVOList
     */
    private void setQuolificationMap(final ContractDTO contractDTO,
        final List<ContractQualifications> contractQualificationsList) {
        final Map<String, List<ContractQualifications>> contractQualificationsVOMap = new HashMap<String, List<ContractQualifications>>();
        if (null != contractQualificationsList && contractQualificationsList.size() > 0) {
            for (final ContractQualifications quaVO : contractQualificationsList) {
                final String key = quaVO.getBigId() + "_" + quaVO.getBrandId();
                List<ContractQualifications> quoList = null;
                if (contractQualificationsVOMap.containsKey(key)) {
                    quoList = contractQualificationsVOMap.get(key);
                } else {
                    quoList = new ArrayList<ContractQualifications>();
                }
                quoList.add(quaVO);
                contractQualificationsVOMap.put(key, quoList);
            }
        }
        contractDTO.setContractQualificationsMap(contractQualificationsVOMap);
    }
    
    /**
     * 设置资质是否被选中
     *
     * @param quaMap
     * @param cidList
     */
    private void setQualificationHasChecked(final List<Long> cidList,
        final Map<String, List<ContractQualifications>> quaMap) {
        if (null != cidList && cidList.size() > 0) {
            final Map<Long, List<DictionaryInfo>> cidPaperMap = new HashMap<Long, List<DictionaryInfo>>();
            final Map<String, DictionaryInfo> paperIdMap = new HashMap<String, DictionaryInfo>();
            for (final Long cid : cidList) {
                final List<DictionaryInfo> papers = categoryService.selectCategoryCertsByCategoryId(cid);
                if(null != papers){
                	cidPaperMap.put(cid, papers);
                    for (final DictionaryInfo pDO : papers) {
                        paperIdMap.put(pDO.getId() + "", pDO);
                    }
                }
            }
            for (final Map.Entry<String, List<ContractQualifications>> quaEntry : quaMap.entrySet()) {
                final String key = quaEntry.getKey();
                final List<ContractQualifications> contractQuaList = quaEntry.getValue();
                if (null == contractQuaList || contractQuaList.size() == 0) {
                    continue;
                }
                final String[] keySp = key.split("_");
                final Long cId = Long.parseLong(keySp[0]);
                final Long bid = Long.parseLong(keySp[1]);
                final Set<String> checkedSet = new HashSet<String>();
                for (final ContractQualifications qufMapChecked : contractQuaList) {
                    qufMapChecked.setHasChecked(Boolean.TRUE);
                    checkedSet.add(qufMapChecked.getPapersId());
                }
                final List<DictionaryInfo> quaInfoAll = cidPaperMap.get(cId);
                if (null != quaInfoAll && quaInfoAll.size() > 0) {
                    for (final DictionaryInfo sourceInfo : quaInfoAll) {
                        if (checkedSet.contains(sourceInfo.getId() + "")) {
                            continue;
                        }
                        final ContractQualifications quaNewAdd = new ContractQualifications();
                        quaNewAdd.setBigId(cId);
                        quaNewAdd.setBrandId(bid);
                        quaNewAdd.setPapersId(sourceInfo.getId() + "");
                        quaNewAdd.setPapersName(sourceInfo.getName());
                        quaNewAdd.setHasChecked(false);
                        contractQuaList.add(quaNewAdd);
                    }
                }
            }
        }
    }

    /**
     * 根据大类和品牌对合同的商品进行分组
     * 
     * @return
     */
    private void groupContractProductByBigcidAndBrand(ContractDTO contractDTO){
    	if(null == contractDTO){
    		return;
    	}
    	List<ContractProduct> contractProducts = contractDTO.getContractProductList();
    	if(null == contractProducts){
    		return;
    	}
        Map<String,List<ContractProduct>> productVOListMap = new HashMap<String,List<ContractProduct>>();
        for(ContractProduct product : contractProducts){
        	String key = product.getBrandId() + "_";
        	if(null != product.getBigId()){
        		key = key + product.getBigId();
        	}
        	List<ContractProduct> productVOs = null;
        	if(productVOListMap.containsKey(key)){
        		productVOs = productVOListMap.get(key);
        	} else {
        		productVOs = new ArrayList<ContractProduct>();
        	}
        	productVOs.add(product);
        	productVOListMap.put(key, productVOs);
        }
        contractDTO.setContractProductMap(productVOListMap);
    }
    /**
	 * 生成合同模板设置的map
	 * @param contractDTO 
	 * @param isDownload
	 * 
	 * @return
	 */
	private Map<String, String> generatContractMap(ContractDTO contractDTO, boolean isDownload) {
		
		Map<String,String> contractMap = new HashMap<String,String>();
		ContractProperties contractProp = contractDTO.getContractProperties();
		SupplierInfo supplier = supplierInfoService.queryById(contractDTO.getSupplierId());
		if(null == supplier || null == contractProp){
			return contractMap;
		}
		
		List<ContractProduct> contractProducts = contractDTO.getContractProductList();
		
		Calendar calendar = Calendar.getInstance();
		/**合同名称*/
		contractMap.put(ContractTemplateConstant.CONTRACT_TITLE , contractDTO.getContractName());
		contractMap.put(ContractTemplateConstant.CONTRACT_CODE , contractDTO.getContractCode());
		
		if(null != contractDTO.getSigningDate()){
			calendar.setTime(contractDTO.getSigningDate());
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
		contractMap.put(ContractTemplateConstant.PARTY_B_NAME,contractDTO.getSupplierName());
		/**乙方地址*/
		contractMap.put(ContractTemplateConstant.PARTY_B_ADDRESS,contractProp.getSpLinkAddress());
		/**乙方保证金*/
		if(null != contractDTO.getCash()){
			String moneyCash = NumToWordUtil.number2CNMontrayUnit(new BigDecimal(contractDTO.getCash()));
			if(null != moneyCash && moneyCash.endsWith("元")){
				moneyCash = moneyCash.substring(0,moneyCash.length()-1);
			}
			contractMap.put(ContractTemplateConstant.DEPOSIT_MONEY,moneyCash);
		} else {
			contractMap.put(ContractTemplateConstant.DEPOSIT_MONEY,"");
		}
		/**结算规则*/
		String settleRule = generateSettleRule(contractDTO.getContractSettlementRuleList());
		int wordCountPreLine = 48;
		int totalSize = settleRule.length();
		int lineSize = totalSize % wordCountPreLine == 0 ? totalSize / wordCountPreLine : totalSize / wordCountPreLine + 1;
		for(int i=0;i<lineSize;i++){
			int endIndex = (i + 1) * wordCountPreLine < totalSize ? (i + 1) * wordCountPreLine : totalSize;
			contractMap.put(ContractTemplateConstant.SETTLE_RULE+i,settleRule.substring(i*wordCountPreLine,endIndex));
		}
		
		if(null != contractDTO.getStartDate()){
			calendar.setTime(contractDTO.getStartDate());
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
		
		if(null != contractDTO.getEndDate()){
			calendar.setTime(contractDTO.getEndDate());
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
		contractMap.put(ContractTemplateConstant.COMPANY_NAME,contractDTO.getSupplierName());
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
		contractMap.put(ContractTemplateConstant.PARTY_A_TITLE , ConstractConstant.CONTRACT_PARTY_A_MAP.get(contractDTO.getContractXg()));
		
		
		/**西客商城银行开户名称*/
		contractMap.put(ContractTemplateConstant.XG_BANK_ACCOUNT_NAME , ConstractConstant.BANK_ACCOUNT_NAME_PARTY_A_MAP.get(contractDTO.getContractXg()));
		/**西客商城银行名称*/
		contractMap.put(ContractTemplateConstant.XG_BANK_NAME , ConstractConstant.BANK_NAME_PARTY_A_MAP.get(contractDTO.getContractXg()));
		/**西客商城银行账号*/
		contractMap.put(ContractTemplateConstant.XG_BANK_ACCOUNT , ConstractConstant.BANK_ACCOUNT__PARTY_A_MAP.get(contractDTO.getContractXg()));
		String salesChannel = generateSalesChannels(contractDTO.getSalesChannels());
		if(salesChannel.length()>5){
			contractMap.put(ContractTemplateConstant.SALES_CHANNEL+0, salesChannel.substring(0,5));
			contractMap.put(ContractTemplateConstant.SALES_CHANNEL+1, salesChannel.substring(5,salesChannel.length()));
		}else {
			contractMap.put(ContractTemplateConstant.SALES_CHANNEL+0, salesChannel);
		}
		
		if(isDownload) {
			contractMap.put(ContractTemplateConstant.XG_SIGN_NAME, contractDTO.getContractorName());
    		contractMap.put(ContractTemplateConstant.XG_SIGN_EMAIL, contractDTO.getContractorEmail());
    		contractMap.put(ContractTemplateConstant.XG_SIGN_TEL, contractDTO.getContractorPhone());
			
			contractMap.put(ContractTemplateConstant.BASE_ADDRESS , contractProp.getBaseLinkAddress());
			contractMap.put(ContractTemplateConstant.BASE_LINK_NAME , contractProp.getBaseLinkName());
			contractMap.put(ContractTemplateConstant.BASE_LINK_EMAIL , contractProp.getBaseEmail());
			contractMap.put(ContractTemplateConstant.BASE_LINK_FAX , contractProp.getBaseFax());
			contractMap.put(ContractTemplateConstant.BASE_LAWER, contractProp.getBaseLegalPerson());
			contractMap.put(ContractTemplateConstant.BASE_LINK_TEL, contractProp.getBaseLinkPhone());
		} else {
    		try {
				Long userId = contractDTO.getContractorId();
				ResultInfo<UserDetail> userDetail = supplierInfoProxy.getDetailByUserId(userId);
	        	if(userDetail.success){
	        		contractMap.put(ContractTemplateConstant.XG_SIGN_NAME, contractDTO.getContractorName());
	        		contractMap.put(ContractTemplateConstant.XG_SIGN_EMAIL, userDetail.getData().getEmail());
	        		contractMap.put(ContractTemplateConstant.XG_SIGN_TEL, userDetail.getData().getFixedPhone());
	        	}
	    	} catch(Exception e){
	    		logger.error(e.getMessage(),e);
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
			logger.error(e.getMessage(),e);
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
	private String generateSettleRule(List<ContractSettlementRule> contractSettlementRuleVOList) {
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

	public String generateContractCode(String supplierType, Integer isSea) {
		String templateType = getContractTemplate(supplierType, isSea);
		return contractService.generateContractCode(templateType);
	}
	
	/**
     * 根据供应商类型获取合同模板
     * 
     * @param supplierType
     * @param isHaotao
     * @return
     */
    public String getContractTemplate(String supplierType,Integer isHaotao){
    	if(Constant.SELECTED.YES==isHaotao){
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

	public ResultInfo<Boolean> auditContract(Contract contract,
			Integer auditStatus, String auditContent, Long userId,
			String userName) {
        Integer setStatus = null;
        final AuditRecords record = new AuditRecords();
        record.setAuditId(contract.getId());
        // by zhs 0130 
//        if ("pass".equals(auditStatus)) {
//            setStatus = AuditStatus.THROUGH.getStatus();
//        } else if ("refuse".equals(auditStatus)) {
//            setStatus = AuditStatus.REFUSED.getStatus();
//        }
        if (AuditStatus.THROUGH.getStatus() == auditStatus ) {
            setStatus = AuditStatus.THROUGH.getStatus();
        } else if(AuditStatus.REFUSED.getStatus() == auditStatus){
            setStatus = AuditStatus.REFUSED.getStatus();
        }else if(AuditStatus.STOPED.getStatus() == auditStatus){
            setStatus = AuditStatus.STOPED.getStatus();	
		}
        record.setContent(auditContent);
        record.setAuditStatus(setStatus);
        record.setUserId(userId);
        record.setUserName(userName);
        record.setOperate(SupplierConstant.AUDIT_RESULT.get(setStatus));
        record.setBillType(BillType.CONSTRACT.getValue());
        record.setStatus(Constant.ENABLED.YES);
        record.setCreateTime(new Date());
        record.setCreateUser(userName);
        record.setUpdateUser(userName);
        try {
        	if(AuditStatus.THROUGH.getStatus().equals(setStatus)){
        		setSignPeopleInfo(contract, contract.getContractorId());
        	}
            return contractService.auditContract(contract, setStatus, record);
        } catch (final Throwable exception) {
        	FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,contract, setStatus, record);
			return new ResultInfo<>(failInfo);
        }
    }
	
	 /**
     * 设置签约人信息
     */
    private void setSignPeopleInfo(Contract contract,Long userId){
    	Long userIdLong = null;
    	try {
    		UserDetail userDetail = userDetailService.findByUserId(userIdLong);
        	if(null != userDetail){
        		contract.setContractorEmail(userDetail.getEmail());
        		contract.setContractorPhone(userDetail.getFixedPhone());
        	}
    	} catch(Exception e){
    		logger.error(e.getMessage(),e);
    	}
    }
}
