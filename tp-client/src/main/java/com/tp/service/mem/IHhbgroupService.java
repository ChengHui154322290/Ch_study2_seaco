/**  
 * Project Name:xg-service  
 * File Name:IHhbgroupService.java  
 * Package Name:com.tp.service.mem  
 * Date:2016年11月21日上午11:05:23  
 * Copyright (c) 2016, seagoor All Rights Reserved.  
 *  
*/

package com.tp.service.mem;

import java.util.List;
import java.util.Map;

import com.tp.dto.ord.HhbShopOrderInfoDTO;
import com.tp.model.mem.HhShopMemberInfo;

/**  
 * ClassName:IHhbgroupService <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2016年11月21日 上午11:05:23 <br/>  
 * @author   zhouguofeng  
 * @version    
 * @since   JDK 1.8  
 * @see        
 */
public interface IHhbgroupService {

	/**
	 * 
	 * getHhShopMemberInfoInfo:(根据openId获取惠惠商城的用户信息). <br/>
	 * 
	 * @author zhouguofeng
	 * @param openId
	 * @return
	 * @sinceJDK 1.8
	 */
	HhShopMemberInfo getHhShopMemberInfoInfo(String openId);

//	/**
//	 * 
//	 * costMoney:(扣除惠币). <br/>  
//	 *  
//	 * @author zhouguofeng  
//	 * @param openId   
//	 * @param costMoney
//	 * @return  
//	 * @sinceJDK 1.8
//	 */
//	boolean costMoney(String openId, Double costMoney);
	/**
	 * 
	 * getAddress:(获取惠惠城用户地址信息). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param openId
	 * @return  
	 * @sinceJDK 1.8
	 */
	HhShopMemberInfo getAddress(String openId);
	
//	/**
//	 * 
//	 * costMoney:(扣除惠币). <br/>  
//	 *  
//	 * @author zhouguofeng  
//	 * @param openId   
//	 * @param costMoney
//	 * @return  
//	 * @sinceJDK 1.8
//	 */
//	boolean backMoney(String openId, Double backMoney);
	/**
	 * 
	 * sendOrder:(发送订单信息). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param orderList
	 * @return  
	 * @sinceJDK 1.8
	 */
	Map<String,String> sendOrder(HhbShopOrderInfoDTO orderInfo);
}
