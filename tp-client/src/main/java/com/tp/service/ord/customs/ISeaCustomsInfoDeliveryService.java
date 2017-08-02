/**
 * 
 */
package com.tp.service.ord.customs;

import com.tp.common.vo.OrderConstant.PutCustomsType;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.SubOrder;

/**
 * @author Administrator
 * 
 * 报关申报通用接口
 */
public interface ISeaCustomsInfoDeliveryService {
	
	/** 
	 * @doc 申报接口 
	 * @param subOrder
	 * @return  
	 */
	ResultInfo<Boolean> delivery(SubOrder subOrder);
	
	/** 
	 * @doc 申报前校验,校验数据是否需要申报
	 * @param subOrder
	 * @return 
	 * 		false：不需要申报
	 * 		true： 需要申报
	 */
	boolean checkDelivery(SubOrder subOrder);
	
	/** 
	 * @doc 申报前准备工作,数据准备 
	 * @param subOrder
	 * @return 
	 * 		Boolean.FLASE： 准备推送失败,不改变报关状态的情况下返回通知用户
	 * 		Boolean.TRUE ： 准备推送工作完成,可以进行推送
	 */
	ResultInfo<Boolean> prepareDelivery(SubOrder subOrder);
	
	/**
	 * @doc 校验推送类型 
	 * @param PutCustomsType
	 * @return 
	 * 		true: 对应的报关类型
	 * 		false:不是对应的报关类型
	 */
	boolean checkPutCustomsType(PutCustomsType type);
}
