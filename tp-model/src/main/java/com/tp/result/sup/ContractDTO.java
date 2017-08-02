package com.tp.result.sup;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tp.model.sup.Contract;
import com.tp.model.sup.ContractAttach;
import com.tp.model.sup.ContractCost;
import com.tp.model.sup.ContractProduct;
import com.tp.model.sup.ContractProperties;
import com.tp.model.sup.ContractQualifications;
import com.tp.model.sup.ContractSettlementRule;
import com.tp.model.sup.QuotationInfo;

public class ContractDTO extends Contract implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8830780726132573226L;

	private Map<String, Boolean> salesChannelsMap = new HashMap<String, Boolean>();

	/**
	 * 根据品牌id和大类id进行分组
	 */
	private Map<String, List<ContractProduct>> contractProductMap;

	/**
	 * 合同商品
	 */
	private List<ContractProduct> contractProductList;

	/**
	 * 费用明细
	 */
	private List<ContractCost> contractCostList;

	/**
	 * 结算规则
	 */
	private List<ContractSettlementRule> contractSettlementRuleList;

	/**
	 * 上传合同附件
	 */
	private List<ContractAttach> contractAttachList;

	/**
	 * 上传资质证明
	 */
	private List<ContractQualifications> contractQualificationsList;

	/**
	 * 合同资质证明map
	 */
	private Map<String, List<ContractQualifications>> contractQualificationsMap;

	/**
	 * 报价单
	 */
	private List<QuotationInfo> quotationInfoList;

	/**
	 * 合同属性
	 */
	private ContractProperties contractProperties;

	public Map<String, Boolean> getSalesChannelsMap() {
		return salesChannelsMap;
	}

	public void setSalesChannelsMap(Map<String, Boolean> salesChannelsMap) {
		this.salesChannelsMap = salesChannelsMap;
	}

	public Map<String, List<ContractProduct>> getContractProductMap() {
		return contractProductMap;
	}

	public void setContractProductMap(
			Map<String, List<ContractProduct>> contractProductMap) {
		this.contractProductMap = contractProductMap;
	}

	public List<ContractProduct> getContractProductList() {
		return contractProductList;
	}

	public void setContractProductList(List<ContractProduct> contractProductList) {
		this.contractProductList = contractProductList;
	}

	public List<ContractCost> getContractCostList() {
		return contractCostList;
	}

	public void setContractCostList(List<ContractCost> contractCostList) {
		this.contractCostList = contractCostList;
	}

	public List<ContractSettlementRule> getContractSettlementRuleList() {
		return contractSettlementRuleList;
	}

	public void setContractSettlementRuleList(
			List<ContractSettlementRule> contractSettlementRuleList) {
		this.contractSettlementRuleList = contractSettlementRuleList;
	}

	public List<ContractAttach> getContractAttachList() {
		return contractAttachList;
	}

	public void setContractAttachList(List<ContractAttach> contractAttachList) {
		this.contractAttachList = contractAttachList;
	}

	public List<ContractQualifications> getContractQualificationsList() {
		return contractQualificationsList;
	}

	public void setContractQualificationsList(
			List<ContractQualifications> contractQualificationsList) {
		this.contractQualificationsList = contractQualificationsList;
	}

	public Map<String, List<ContractQualifications>> getContractQualificationsMap() {
		return contractQualificationsMap;
	}

	public void setContractQualificationsMap(
			Map<String, List<ContractQualifications>> contractQualificationsMap) {
		this.contractQualificationsMap = contractQualificationsMap;
	}

	public List<QuotationInfo> getQuotationInfoList() {
		return quotationInfoList;
	}

	public void setQuotationInfoList(List<QuotationInfo> quotationInfoList) {
		this.quotationInfoList = quotationInfoList;
	}

	public ContractProperties getContractProperties() {
		return contractProperties;
	}

	public void setContractProperties(ContractProperties contractProperties) {
		this.contractProperties = contractProperties;
	}
}
