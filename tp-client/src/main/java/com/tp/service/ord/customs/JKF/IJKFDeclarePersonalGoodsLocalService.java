/**
 * 
 */
package com.tp.service.ord.customs.JKF;

import com.tp.common.vo.wms.WmsConstant.ExpressCompany;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.PersonalgoodsDeclareInfo;
import com.tp.model.ord.SubOrder;

/**
 * @author Administrator
 *
 */
public interface IJKFDeclarePersonalGoodsLocalService {

	
	/**
	 * 推送个人物品申报单
	 * @param pDecl
	 * @param subOrder
	 * @return
	 */
	ResultInfo<Boolean> pushPersonalGoodsInfoToJKF(PersonalgoodsDeclareInfo pDecl, SubOrder subOrder);
	
	/**
	 * 生成个人物品申报单
	 * @param subOrder
	 * @param expressCompany
	 * @param waybillNo
	 * @return 
	 */
	PersonalgoodsDeclareInfo createPersonalgoodsDeclareInfo(SubOrder subOrder,
			ExpressCompany expressCompany, String waybillNo);
	
	/** 
	 * 生成浙江电子口岸版本的清关单流水号 
	 */
	String getPreEntryNo();
}
