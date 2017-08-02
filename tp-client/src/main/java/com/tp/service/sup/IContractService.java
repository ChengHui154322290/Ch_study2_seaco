package com.tp.service.sup;

import com.tp.dto.common.ResultInfo;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.Contract;
import com.tp.result.sup.ContractDTO;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 合同主表接口
  */
public interface IContractService extends IBaseService<Contract>{

    /**
     * 执行审核操作
     *
     * @param contractDO
     * @param setStatus
     * @param record
     * @return
     * @throws Exception
     */
	ResultInfo<Boolean> auditContract(Contract contractDO, Integer setStatus, AuditRecords record) throws Throwable;

    /**
     * 根据合同id查询所欲合同信息
     *
     * @param cid
     * @return
     */
	ContractDTO getContractByIdAll(Long contractId);

    /**
     * 保存合同的基本信息
     */
	ResultInfo<Long> saveContractBaseInfo(final ContractDTO contractDTO);

    /**
     * 更新合同的基本信息
     *
     * @param contractDTO
     * @return
     */
	ResultInfo<Long> updateContractBaseInfo(final ContractDTO contractDTO);
    
    /**
     * 生成合同编码
     * 
     * @param contractTemplateType
     * @return
     */
    public String generateContractCode(String contractTemplateType);
}
