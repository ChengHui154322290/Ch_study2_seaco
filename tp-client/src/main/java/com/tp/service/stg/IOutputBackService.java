package com.tp.service.stg;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.OutputBackDto;
import com.tp.model.stg.OutputBack;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 出库订单反馈接口
  */
public interface IOutputBackService extends IBaseService<OutputBack>{
	/**
	 * 根据订单号获得运单号
	 * @param orderCode
	 * 		订单编号，整个系统中应该唯一
	 * @return
	 */
	public String selectShipNoByOrderCode(String orderCode);
	/**
	 * 根据订单号获得发货信息
	 * @param orderCode
	 * @return
	 * 		返回发货运单相关信息
	 */
	public OutputBack selectOutputBackInfoByOrderCode(String orderCode);
	
	/**
	 * 增加订单发运信息，主要用于商家平台增加发运信息
	 */
	public ResultInfo<String> addOutputBackInfo(OutputBackDto back)throws Exception;
}
