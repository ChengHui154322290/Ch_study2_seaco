package com.tp.dao.stg;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.stg.OutputBackSku;

public interface OutputBackSkuDao extends BaseDao<OutputBackSku> {
	/**
	 * 插入  出库订单反馈sku信息列(批量)

	 * @param outputBackSkuList
	 * @return 主键
	 * @throws DAOException
	 * @author szy
	 */
	Long insertBatch(List<OutputBackSku> outputBackSkuList);
}
