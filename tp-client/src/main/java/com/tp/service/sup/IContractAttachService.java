package com.tp.service.sup;

import com.tp.model.sup.ContractAttach;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 合同-附件表接口
  */
public interface IContractAttachService extends IBaseService<ContractAttach>{

    /**
     * 根据contractId获取合同附件信息
     */
	public ContractAttach getContractAttachByContractId(final Long contractId);
}
