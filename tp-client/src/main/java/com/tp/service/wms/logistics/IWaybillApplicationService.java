package com.tp.service.wms.logistics;

import com.tp.common.vo.wms.WmsConstant.ExpressCompany;
import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.WaybillApplication;
import com.tp.model.wms.WaybillDetail;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 运单号申请表接口
  */
public interface IWaybillApplicationService extends IBaseService<WaybillApplication>{
	
	/**
	 *  批量申请运单号
	 *  @param count 数量
	 *  @return
	 */
	ResultInfo<Boolean> applyWaybills(ExpressCompany expressCompany, int count);
	
	/**
	 * 为订单分配运单号
	 * @param orderCode
	 * @return 
	 */
	ResultInfo<WaybillDetail> applyWaybillNoForOrder(String orderCode, ExpressCompany company);
	
	/**
	 * 查询未使用订单编号
	 * @param company
	 * @return 
	 */
	ResultInfo<Integer> queryUnUsedWaybillNoCount(ExpressCompany company);
	
	/**
	 * 归还未使用的运单号 
	 * @param waybillNo 运单号
	 * @return
	 */
	ResultInfo<Boolean> waybillNoBack(String waybillNo);
}
