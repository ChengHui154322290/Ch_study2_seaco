/**  
 * Project Name:xg-seller  
 * File Name:SupplierShopAo.java  
 * Package Name:com.tp.seller.ao.shop  
 * Date:2016年9月22日下午2:58:17  
 * Copyright (c) 2016, seagoor All Rights Reserved.  
 *  
*/  
   
package com.tp.seller.ao.shop;

import org.springframework.beans.factory.annotation.Autowired;

import com.tp.model.sup.SupplierShop;
import com.tp.proxy.sup.SupplierShopProxy;

/**  
 * ClassName:SupplierShopAo <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2016年9月22日 下午2:58:17 <br/>  
 * @author   zhouguofeng  
 * @version    
 * @since   JDK 1.8  
 * @see        
 */
public class SupplierShopAo {
	@Autowired
	private SupplierShopProxy  supplierShopProxy;
	
	/**
	 * 
	 * getSupplierShopInfo:(根据供应商ID 获取供应商店铺信息). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param supplierId
	 * @return  
	 * @sinceJDK 1.8
	 */
	public  SupplierShop   getSupplierShopInfo(Long supplierId){
		return	supplierShopProxy.getSupplierShopInfo(supplierId);
	}
	

}
  
