package com.tp.service.wms;

import java.util.List;

import com.tp.model.wms.StockinImportDetail;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 入库单导入明细表接口
  */
public interface IStockinImportDetailService extends IBaseService<StockinImportDetail>{

	//批量插入导入入库的数据
	void batchInsert(List<StockinImportDetail> list);

}
