/**
 * 
 */
package com.tp.service.ord.customs;

import java.util.List;

import com.tp.common.vo.wms.WmsConstant.ExpressCompany;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.SubOrder;
import com.tp.model.wms.WaybillDetail;

/**
 * @author Administrator
 * 运单信息处理
 */
public interface ISeaWaybillInfoService {

	/**
	 * 推送申报运单
	 * @param subOrders
	 * @return 
	 */
	boolean pushWaybillInfos(List<SubOrder> subOrders, ExpressCompany expressCompany);
	
	/**
	 * 	推送申报运单
	 *  @param suborder 子订单信息
	 *  @param expressCompany 快递公司信息
	 *  @param expressNo 快递单号
	 */
	ResultInfo<Boolean> pushWaybillInfo(SubOrder subOrder, ExpressCompany expressCompany, String expressNo);
	
	/**
	 * 申请运单号
	 */
	ResultInfo<WaybillDetail> applyWaybillNoForOrder(String OrderCode, ExpressCompany expressCompany);

}
