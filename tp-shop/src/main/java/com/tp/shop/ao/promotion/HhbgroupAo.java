/**  
 * Project Name:xg-shop  
 * File Name:HhbgroupAo.java  
 * Package Name:com.tp.shop.ao.promotion  
 * Date:2016年11月18日下午4:26:52  
 * Copyright (c) 2016, seagoor All Rights Reserved.  
 *  
*/

package com.tp.shop.ao.promotion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tp.model.mem.HhShopMemberInfo;
import com.tp.service.mem.IHhbgroupService;

import org.springframework.stereotype.Service;

/**
 * ClassName:HhbgroupAo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年11月18日 下午4:26:52 <br/>
 * 
 * @author zhouguofeng
 * @version
 * @since JDK 1.8
 * @see
 */
@Service
public class HhbgroupAo {
	@Autowired
	IHhbgroupService  hhbgroupService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 
	 * getHhShopMemberInfoInfo:(根据openId获取惠惠商城的用户信息). <br/>
	 * 
	 * @author zhouguofeng
	 * @param openId
	 * @return
	 * @sinceJDK 1.8
	 */
	public HhShopMemberInfo getHhShopMemberInfo(String openId) {
		HhShopMemberInfo  hhShopMemberInfo=new HhShopMemberInfo();
		HhShopMemberInfo  hhShopMemberAddressInfo=new HhShopMemberInfo();
		hhShopMemberInfo= hhbgroupService.getHhShopMemberInfoInfo(openId);
		if(hhShopMemberInfo!=null){
			hhShopMemberAddressInfo=hhbgroupService.getAddress(openId);
			if(hhShopMemberAddressInfo!=null){
				hhShopMemberInfo.setAddress(hhShopMemberAddressInfo.getAddress());
				hhShopMemberInfo.setContact(hhShopMemberAddressInfo.getContact());
			}
			
		}
		return  hhShopMemberInfo;
	}
	
//	@SuppressWarnings("unchecked")
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
//   public boolean  costMoney(String openId,Double  costMoney){
//		return hhbgroupService.costMoney(openId, costMoney);
//	}
	
	
	
	
   public static void main(String args[]){
	   String openId="o1_XewthXUggrRExkpyqdBzk1wtE";
	   Double costMoney=1D;
	   HhbgroupAo  hhbgroupAo=new HhbgroupAo();
	   
	   hhbgroupAo.getHhShopMemberInfo(openId);
	 //  hhbgroupAo.costMoney(openId, costMoney);
   }
}
