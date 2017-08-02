package com.tp.service.ord;

import com.tp.dto.prd.SkuImportLogDto;
import com.tp.exception.ItemServiceException;
import com.tp.model.ord.OrderImportInfo;
import com.tp.model.ord.OrderImportLog;
import com.tp.service.IBaseService;
/**
  * @author zhouguofeng 
  * 订单导入明细接口
  */
public interface IOrderImportInfoService extends IBaseService<OrderImportInfo>{
	public OrderImportLog saveImportLogDto(OrderImportLog orderImportLog);
	/**
	 * 
	 * <pre>
	 *  查询excel导入日志信息
	 * </pre>
	 * @param userId
	 * @param status
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws ItemServiceException
	 */
	SkuImportLogDto queryOrderImport(String createUser,int status,int pageNo, int pageSize);
}
