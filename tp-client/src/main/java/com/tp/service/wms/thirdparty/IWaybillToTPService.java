/**
 * 
 */
package com.tp.service.wms.thirdparty;

import com.tp.common.vo.wms.WmsConstant.ExpressCompany;
import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.WaybillApplication;
import com.tp.model.wms.WaybillInfo;

/**
 * @author Administrator
 * 第三方快递运单服务
 */
public interface IWaybillToTPService {

	/**
	 * 批量申请运单号 
	 * @param company
	 * @param count
	 * @return
	 */
	WaybillApplication applyWaybill(ExpressCompany company, int count);
	
	/**
	 * 推送运单信息
	 * @param waybillInfo
	 * @return
	 */
	ResultInfo<Boolean> pushWaybillInfo(WaybillInfo waybillInfo);
	
	/**
	 * 校验申请
	 */
	boolean checkApplyWaybill(ExpressCompany company, int count);
	
	/**
	 * 校验推单
	 */
	boolean checkPushWaybillInfo(WaybillInfo waybillInfo);
	
}
