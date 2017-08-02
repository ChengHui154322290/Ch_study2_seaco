/**  
 * Project Name:xg-service  
 * File Name:MemberPointService.java  
 * Package Name:com.tp.service.mem  
 * Date:2016年11月21日上午10:32:58  
 * Copyright (c) 2016, seagoor All Rights Reserved.  
 *  
*/  
  
package com.tp.service.mem;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.ord.HhbShopOrderInfoDTO;
import com.tp.service.mem.IHhbgroupService;
import com.tp.service.mem.IMemberPointService;

/**  
 * ClassName:MemberPointService <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2016年11月21日 上午10:32:58 <br/>  
 * @author   zhouguofeng  
 * @version    
 * @since   JDK 1.8  
 * @see        
 */
@Service("hhbMemberPointService")
public class HhbMemberPointService implements IMemberPointService {
	@Autowired
	IHhbgroupService  hhbgroupService;
	
	@Override
	public Double getMemberPorit(String openId) {
		return hhbgroupService.getHhShopMemberInfoInfo(openId).gethMoney();
	}

//	@Override
//	public Boolean costMemberPoint(String openId, Double costMoney) {
//		return hhbgroupService.costMoney(openId, costMoney);
//	}
//
//	@Override
//	public Boolean backMemberPoint(String openId, Double backMoney) {
//		return hhbgroupService.backMoney(openId, backMoney);
//	}

	@Override
	public Map<String,String> sendOrderInfo(HhbShopOrderInfoDTO  orderInfo) {
		 
		return hhbgroupService.sendOrder(orderInfo);
	}

}
  
