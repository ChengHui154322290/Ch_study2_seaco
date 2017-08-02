package com.tp.dao.stg;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.stg.InputBackSku;

public interface InputBackSkuDao extends BaseDao<InputBackSku> {

	/**
	 * 插入  入库单反馈(批量)

	 * @param inputBackSkuList
	 * @return 主键
	 * @throws DAOException
	 * @author szy
	 */
	public Long insertBatch(List<InputBackSku> inputBackSkuList);
}
