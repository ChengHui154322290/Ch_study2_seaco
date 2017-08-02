/**  
 * Project Name:xg-client  
 * File Name:IMemberPoritService.java  
 * Package Name:com.tp.service.mem  
 * Date:2016年11月21日上午10:28:56  
 * Copyright (c) 2016, seagoor All Rights Reserved.  
 *  
*/  
  
package com.tp.service.mem;

import java.util.Map;

import com.tp.dto.ord.HhbShopOrderInfoDTO;

/**  
 * ClassName:IMemberPoritService <br/>  
 * Function: 各个商城的积分获取 <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2016年11月21日 上午10:28:56 <br/>  
 * @author   zhouguofeng  
 * @version    
 * @since   JDK 1.8  
 * @see        
 */
public interface IMemberPointService {
	/**
	 * 
	 * getMemberPorit:(根据用户openid和chanelCode获取对应商城的积分). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param openid
	 * @param chanelCode
	 * @return  
	 * @sinceJDK 1.8
	 */
	public Double getMemberPorit(String openid);
	
	
	
//	/**
//	 * 
//	 * costMemberPoint:(扣除消费的积分). <br/>  
//	 *  
//	 * @author zhouguofeng  
//	 * @param openId
//	 * @param costMoney
//	 * @return  
//	 * @sinceJDK 1.8
//	 */
//	public Boolean costMemberPoint(String openId,Double costMoney);
//	
//	
//	/**
//	 * 
//	 * costMemberPoint:(退还消费的积分). <br/>  
//	 *  
//	 * @author zhouguofeng  
//	 * @param openId
//	 * @param costMoney
//	 * @return  
//	 * @sinceJDK 1.8
//	 */
//	public Boolean backMemberPoint(String openId,Double backMoney);
	
	/**
	 * 
	 * sendOrderInfo:(惠惠宝订单发送). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>   
	 *  
	 * @author zhouguofeng  
	 * @param orderList
	 * @return  
	 * @sinceJDK 1.8
	 */
	public Map<String,String> sendOrderInfo(HhbShopOrderInfoDTO  orderInfo);

}
  
