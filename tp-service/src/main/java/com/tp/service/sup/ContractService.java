package com.tp.service.sup;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.supplier.CommonUtil;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.dao.sup.AuditRecordsDao;
import com.tp.dao.sup.ContractAttachDao;
import com.tp.dao.sup.ContractCostDao;
import com.tp.dao.sup.ContractDao;
import com.tp.dao.sup.ContractProductDao;
import com.tp.dao.sup.ContractPropertiesDao;
import com.tp.dao.sup.ContractQualificationsDao;
import com.tp.dao.sup.ContractSettlementRuleDao;
import com.tp.dao.sup.QuotationInfoDao;
import com.tp.dao.sup.SupplierInfoDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.Contract;
import com.tp.model.sup.ContractAttach;
import com.tp.model.sup.ContractCost;
import com.tp.model.sup.ContractProduct;
import com.tp.model.sup.ContractProperties;
import com.tp.model.sup.ContractQualifications;
import com.tp.model.sup.ContractSettlementRule;
import com.tp.model.sup.QuotationInfo;
import com.tp.model.sup.SupplierInfo;
import com.tp.result.sup.ContractDTO;
import com.tp.service.BaseService;
import com.tp.service.sup.IContractService;
import com.tp.service.sup.ISequenceCodeService;

@Service
public class ContractService extends BaseService<Contract> implements IContractService {

	@Autowired
	private ContractDao contractDao;
    @Autowired
    private AuditRecordsDao auditRecordsDao;
    @Autowired
    private QuotationInfoDao quotationInfoDao;
    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ContractAttachDao contractAttachDao;
    @Autowired
    private ContractCostDao contractCostDao;
    @Autowired
    private ContractSettlementRuleDao contractSettlementRuleDao;
    @Autowired
    private ContractQualificationsDao contractQualificationsDao;    
    @Autowired
    private ContractPropertiesDao contractPropertiesDao;    
    @Autowired
    private SupplierInfoDao supplierInfoDao;
    
    @Autowired
    private ISequenceCodeService sequenceCodeService;
    
	@Override
	public BaseDao<Contract> getDao() {
		return contractDao;
	}
	@Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo<Boolean> auditContract(final Contract contract, final Integer auditStatus, final AuditRecords record) throws Throwable {
        if (null != contract && null != contract.getId()) {
            final Contract oldContract = queryById(contract.getId());
            if (null == oldContract || !SupplierConstant.PREVIOUS_AUDIT_STATUS.get(auditStatus).contains(oldContract.getAuditStatus())) {
                return new ResultInfo<>(new FailInfo("合同审核状态异常， 审核失败。"));
            }
            final Contract updateConstant = new Contract();
            
            if(AuditStatus.THROUGH.getStatus().equals(auditStatus)){
            	//审核通过生成快照
            	Map<String,Object> params = new HashMap<String,Object>();
            	params.put("contractId", contract.getId());
            	params.put("status", Constant.DEFAULTED.YES);
            	List<ContractProperties> contractPropResList = contractPropertiesDao.queryByParam(params);
            	if(CollectionUtils.isNotEmpty(contractPropResList)){
            		ContractProperties contractPropeties = contractPropResList.get(0);
            		contractPropeties = supplierSnapshot(contractPropeties, oldContract.getSupplierId());
					contractPropertiesDao.updateNotNullById(contractPropeties);
            	} else {
            		return new ResultInfo<>(new FailInfo("合同属性更新异常， 审核失败。"));
            	}
            	updateConstant.setContractorEmail(contract.getContractorEmail());
            	updateConstant.setContractorPhone(contract.getContractorPhone());
            }
            updateConstant.setAuditStatus(auditStatus);
            updateConstant.setUpdateTime(new Date());
            updateConstant.setId(contract.getId());
            final int num = contractDao.updateNotNullById(updateConstant);
            if (num < 1) {
            	ExceptionUtils.printAndThrow(new FailInfo(new Exception("合同审核异常。")), logger, updateConstant);
            }
            auditRecordsDao.insert(record);
        } else {
        	return new ResultInfo<>(new FailInfo("合同审核异常。"));
        }
        return new ResultInfo<>(Boolean.TRUE);
    }

    /**
     * 查询合同信息
     *
     * @throws DaoException
     */
    @Override
    public ContractDTO getContractByIdAll(final Long contractId) {
        final ContractDTO contractDTO = new ContractDTO();
        Contract contract = contractDao.queryById(contractId);
        if(null!=contract){
        	BeanUtils.copyProperties(contract, contractDTO);
        	Map<String,Object> params = new HashMap<String,Object>();
        	params.put("contractId", contractId);
        	params.put("status", Constant.DEFAULTED.YES);
        	/** 合同商品 */
        	List<ContractProduct> contractProductList = contractProductDao.queryByParam(params);
        	/** 费用明细 */
        	List<ContractCost> contractCostList = contractCostDao.queryByParam(params);
        	/** 结算规则 */
        	List<ContractSettlementRule> contractSettlementRuleList = contractSettlementRuleDao.queryByParam(params);
        	/** 上传合同附件 */
        	List<ContractAttach> contractAttachList = contractAttachDao.queryByParam(params);
        	/** 上传资质证明 */
        	List<ContractQualifications> contractQualificationsList = contractQualificationsDao.queryByParam(params);
        	/** 设置合同报价单信息 */
        	List<QuotationInfo> quotationInfoList = quotationInfoDao.queryByParam(params);
        	/**上传合同属性*/
        	List<ContractProperties> contractPropertiesList = contractPropertiesDao.queryByParam(params); 
        	ContractProperties contractProperties = contractPropertiesList.size() > 0 ? contractPropertiesList.get(0):null; 
        	// by zhs 0130 生成SaleChannelMap
        	Map<String, Boolean> mapSaleChannel = toSalesChannelMap(contract.getSalesChannels());
        	
        	/**合同商品*/
        	contractDTO.setContractProductList(contractProductList);
        	contractDTO.setContractCostList(contractCostList);
        	contractDTO.setContractSettlementRuleList(contractSettlementRuleList);
        	contractDTO.setContractAttachList(contractAttachList);
        	contractDTO.setContractQualificationsList(contractQualificationsList);
        	contractDTO.setQuotationInfoList(quotationInfoList);
        	contractDTO.setContractProperties(contractProperties);
        	contractDTO.setSalesChannelsMap(mapSaleChannel);
        }
        return contractDTO;
    }
    
    /**
     * 
     * 
     * @param channel
     * @return
     */
    private Map<String, Boolean> toSalesChannelMap(String channel) {
    	Long lChannel = CommonUtil.getLongVal(channel);
    	Map<String, Boolean> mapChannel = new HashMap<String, Boolean>();
    	int i = 1;    	
    	while( lChannel > 0 ){    		
    		if( lChannel % 10 == 1){
    			mapChannel.put(String.valueOf(i), true);
    		}
    		else {
				mapChannel.put(String.valueOf(i), false);
			}
    		lChannel = lChannel / 10;
    		i += 1;
    	}
    	return mapChannel;
	}
    
    /**
     * 保存合同的基本信息
     *
     * @throws DaoException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo<Long> saveContractBaseInfo(final ContractDTO contractDTO){
        if (null == contractDTO.getSupplierName() || contractDTO.getSupplierName().length() > SupplierConstant.LENGTH_60) {
        	return new ResultInfo<>(new FailInfo("供应商名称非空且不能超过60个字符"));
        }
        if (null == contractDTO.getContractName() || contractDTO.getContractName().length() > SupplierConstant.LENGTH_60) {
        	return new ResultInfo<>(new FailInfo("合同名称非空且不能超过60个字符"));
        }
        if (null == contractDTO.getContractType()) {
        	return new ResultInfo<>(new FailInfo("合同类型不能为空"));
        }

        final Contract contract = new Contract();
        BeanUtils.copyProperties(contractDTO, contract);
        
        String template = sequenceCodeService.CONTRACT_TEMPLATE_MAP.get(contract.getTemplateType());
        if(null != template){
        	contract.setContractCode(generateContractCode(contract.getTemplateType()));
        	sequenceCodeService.nextCode(template);
        } else {
        	return new ResultInfo<>(new FailInfo("找不到对应的合同模板信息"));
        }
        contract.setIsSea(Constant.DEFAULTED.YES);
        
        contractDao.insert(contract);
        final Long id = contract.getId();
        saveContractProduct(contractDTO.getContractProductList(), id);
        saveContractAttach(contractDTO.getContractAttachList(), id);
        saveContractCost(contractDTO.getContractCostList(), id);
        saveContractSettlementRule(contractDTO.getContractSettlementRuleList(), id);
        saveContractQualifications(contractDTO.getContractQualificationsList(), id);
        saveContractProperties(contractDTO.getContractProperties(),id);
        return new ResultInfo<>(id);
    }
    /**
     * 更新合同基本信息
     *
     * @throws DaoException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo<Long> updateContractBaseInfo(final ContractDTO contractDTO){
        if (contractDTO == null) {
        	return new ResultInfo<>(new FailInfo("更新信息为空。"));
        }
        final Long contractId = contractDTO.getId();
        if (contractId == null) {
        	return new ResultInfo<>(new FailInfo("找不到相关合同信息。"));
        }

        final Contract contract = new Contract();
        BeanUtils.copyProperties(contractDTO, contract);
        contractDao.updateNotNullById(contract);
        //如果是非协议合同  协议附件非空
        if(!Constant.DEFAULTED.YES.equals(contract.getIsAgreementContract())){
        	Contract contractOld = contractDao.queryById(contractId);
        	contractOld.setAgreementContractUrl(null);
        	contractDao.updateById(contractOld);
        }
        
        updateContractProduct(contractDTO.getContractProductList(), contractId);
        updateContractAttach(contractDTO.getContractAttachList(), contractId);
        updateContractCost(contractDTO.getContractCostList(), contractId);
        updateContractSettlementRule(contractDTO.getContractSettlementRuleList(), contractId);
        updateContractQualifications(contractDTO.getContractQualificationsList(), contractId);
        updateContractProperties(contractDTO.getContractProperties(),contractId);
        return new ResultInfo<>(contractId);
    }


    
    
    /**
	 * 生成合同类型
	 * 
	 * @param contractTemplateType
	 * @return
	 */
	public String generateContractCode(String contractTemplateType) {
    	Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String template = SequenceCodeService.CONTRACT_TEMPLATE_MAP.get(contractTemplateType);
        if(null == template){
        	return "";
        }
        Long num = sequenceCodeService.currentCode(template);
        return year + contractTemplateType + CommonUtil.getMinIntegerDigits(num,5);
    }
	
	/**
	 * 供应商基本信息快照
	 * @throws DaoException 
	 */
	private ContractProperties supplierSnapshot(ContractProperties contractPropeties,Long supplierId){
		SupplierInfo supplierInfo = supplierInfoDao.queryById(supplierId);
		if(null != supplierInfo){
			contractPropeties.setBaseEmail(supplierInfo.getEmail());
			contractPropeties.setBaseFax(supplierInfo.getFax());
			contractPropeties.setBaseLegalPerson(supplierInfo.getLegalPerson());
			contractPropeties.setBaseLinkAddress(supplierInfo.getAddress());
			contractPropeties.setBaseLinkName(supplierInfo.getLinkName());
			contractPropeties.setBaseLinkPhone(supplierInfo.getPhone());
		}
		return contractPropeties;
	}
	
    /**
     * 保存合同属性
     * 
     * @param contractPropertiesDTO
     * @param id
     * @throws DaoException 
     */
    private void saveContractProperties(ContractProperties contractProperties, Long contractId){
		if(null != contractProperties){
			contractProperties.setContractId(contractId);
			contractPropertiesDao.insert(contractProperties);
		}
	}
    private void saveContractAttach(final List<ContractAttach> contractAttachList, final Long contractId) {
    	if (CollectionUtils.isNotEmpty(contractAttachList)) {
            for (ContractAttach contractAttach : contractAttachList) {
            	contractAttach.setContractId(contractId);
            	contractAttachDao.insert(contractAttach);
            }
        }
    }
	private void saveContractCost(final List<ContractCost> contractCostList, final Long contractId) {
        if (CollectionUtils.isNotEmpty(contractCostList)) {
            for (ContractCost contractCost : contractCostList) {
            	contractCost.setContractId(contractId);
            	contractCostDao.insert(contractCost);
            }
        }
    }

    private void saveContractProduct(final List<ContractProduct> contractProductList, final Long contractId) {
    	if (CollectionUtils.isNotEmpty(contractProductList)) {
            for (ContractProduct contractProduct : contractProductList) {
            	contractProduct.setContractId(contractId);
            	contractProductDao.insert(contractProduct);
            }
        }
    }

    private void saveContractQualifications(final List<ContractQualifications> contractQualificationsOList, final Long contractId) {
    	if (CollectionUtils.isNotEmpty(contractQualificationsOList)) {
            for (ContractQualifications contractQualifications : contractQualificationsOList) {
            	contractQualifications.setContractId(contractId);
            	contractQualificationsDao.insert(contractQualifications);
            }
        }
    }

    private void saveContractSettlementRule(final List<ContractSettlementRule> contractSettlementRuleList, final Long contractId) {
    	if (CollectionUtils.isNotEmpty(contractSettlementRuleList)) {
            for (ContractSettlementRule contractSettlementRule : contractSettlementRuleList) {
            	contractSettlementRule.setContractId(contractId);
            	contractSettlementRuleDao.insert(contractSettlementRule);
            }
        }
    }

    
    /**
     * @throws DaoException
     */
    public void updateContractAttach(final List<ContractAttach> contractAttachList, final Long contractId) {
        if (CollectionUtils.isNotEmpty(contractAttachList)) {
        	contractAttachDao.updateStatusByContractId(contractId,Constant.DEFAULTED.NO,contractAttachList.get(0).getUpdateUser());
            for (final ContractAttach contractAttach : contractAttachList) {
                contractAttach.setContractId(contractId);
            }
            contractAttachDao.batchInsert(contractAttachList);
        }
    }

    /**
     * 更新合同属性信息
     * 
     * @param contractPropertiesDTO
     * @param id
     * @throws DaoException 
     */
    private void updateContractProperties(ContractProperties contractProperties, Long contractId) {
    	if (null!=contractProperties) {
    		contractPropertiesDao.updateStatusByContractId(contractId,Constant.DEFAULTED.NO,contractProperties.getUpdateUser());
    		contractProperties.setContractId(contractId);
    		contractPropertiesDao.insert(contractProperties);
        }
	}

	public void updateContractCost(final List<ContractCost> contractCostList, final Long contractId) {
		if (CollectionUtils.isNotEmpty(contractCostList)) {
			contractCostDao.updateStatusByContractId(contractId,Constant.DEFAULTED.NO,contractCostList.get(0).getUpdateUser());
            for (final ContractCost contractCost : contractCostList) {
            	contractCost.setContractId(contractId);
            }
            contractCostDao.batchInsert(contractCostList);
        }
    }

    /**
     * 更新合同对应的商品信息
     *
     * @throws DaoException
     */
    public void updateContractProduct(final List<ContractProduct> contractProductList, final Long contractId) {
    	if (CollectionUtils.isNotEmpty(contractProductList)) {
    		contractProductDao.updateStatusByContractId(contractId,Constant.DEFAULTED.NO,contractProductList.get(0).getUpdateUser());
            for (final ContractProduct contractProduct : contractProductList) {
            	contractProduct.setContractId(contractId);
            }
            contractProductDao.batchInsert(contractProductList);
        }
    }

    public void updateContractQualifications(final List<ContractQualifications> contractQualificationsList, final Long contractId) {
    	if (CollectionUtils.isNotEmpty(contractQualificationsList)) {
    		contractQualificationsDao.updateStatusByContractId(contractId,Constant.DEFAULTED.NO,contractQualificationsList.get(0).getUpdateUser());
            for (final ContractQualifications contractQualifications : contractQualificationsList) {
            	contractQualifications.setContractId(contractId);
            }
            contractQualificationsDao.batchInsert(contractQualificationsList);
        }
    }

    public void updateContractSettlementRule(final List<ContractSettlementRule> contractSettlementRuleList, final Long contractId) {
    	if (CollectionUtils.isNotEmpty(contractSettlementRuleList)) {
    		contractSettlementRuleDao.updateStatusByContractId(contractId,Constant.DEFAULTED.NO,contractSettlementRuleList.get(0).getUpdateUser());
            for (final ContractSettlementRule contractSettlementRule : contractSettlementRuleList) {
            	contractSettlementRule.setContractId(contractId);
            }
            contractSettlementRuleDao.batchInsert(contractSettlementRuleList);
        }
    }
}
