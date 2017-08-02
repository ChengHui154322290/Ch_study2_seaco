package com.tp.service.wms;

import java.util.List;

import com.tp.model.wms.StockasnDetailFact;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 接口
  */
public interface IStockasnDetailFactService extends IBaseService<StockasnDetailFact>{
	
	/**
	 * 批量保存
	 */
	void batchInsert(List<StockasnDetailFact> list);
}
