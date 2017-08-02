package com.tp.dao.sup;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.sup.ContractProperties;

public interface ContractPropertiesDao extends BaseDao<ContractProperties> {
	/**
	 * 根据合同ID更新状态及更新人
	 * @param contractId
	 * @param status
	 * @param updateUser
	 * @return
	 */
	Integer updateStatusByContractId(@Param("contractId")Long contractId,@Param("status")Integer status,@Param("updateUser")String updateUser);
	
	/**
	 * 批量更新
	 * @param contractAttachList
	 */
	void batchInsert(List<ContractProperties> contractPropertiesList);
}
