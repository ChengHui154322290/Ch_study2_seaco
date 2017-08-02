package com.tp.service.stg;

import java.util.List;

import com.tp.model.stg.OutputBackSku;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 出库订单反馈sku信息列表
接口
  */
public interface IOutputBackSkuService extends IBaseService<OutputBackSku>{
	/**
	 * 插入  出库订单反馈sku信息列(批量)

	 * @param outputBackSkuList
	 * @return 主键
	 * @throws DAOException
	 * @author szy
	 */
	Long insertBatch(List<OutputBackSku> outputBackSkuList);
}
