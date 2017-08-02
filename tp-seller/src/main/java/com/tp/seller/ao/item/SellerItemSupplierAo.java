/**  
 * Project Name:xg-seller  
 * File Name:SellerItemAO.java  
 * Package Name:com.tp.seller.ao.item  
 * Date:2016年9月8日上午10:24:59  
 * Copyright (c) 2016, seagoor All Rights Reserved.  
 *  
*/

package com.tp.seller.ao.item;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tp.common.vo.PageInfo;
import com.tp.model.prd.ItemImportList;
import com.tp.model.sup.SupplierInfo;
import com.tp.proxy.prd.ItemProxy;
import com.tp.query.prd.ItemQuery;
import com.tp.seller.ao.base.SellerBaseAO;
import com.tp.seller.constant.SellerConstant;
import com.tp.seller.util.SessionUtils;

/**
 * ClassName:SellerItemAO <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年9月8日 上午10:24:59 <br/>
 * 
 * @author zhouguofeng
 * @version
 * @since JDK 1.8
 * @see
 */
@Service
public class SellerItemSupplierAo extends SellerBaseAO {
	@Autowired
	private ItemProxy itemProxy;

	/**
	 * 根据条件查询订单信息
	 *
	 * @param queryMap
	 * @return
	 */

	public PageInfo<SupplierInfo> querySupplierInfoByCondition(final HttpServletRequest request, Model model) {
		ItemQuery query = new ItemQuery();
		String userName = (String) request.getSession().getAttribute(SellerConstant.USER_NAME_KEY);
		Integer status = getIntValue(request, "status");
		Integer startPage = getIntValue(request, "start");
		Integer pageSize = getIntValue(request, "pageSize");
		String supplierNameQuery=getStringValue(request,"supplierNameQuery"); 
		String supplierTypeQuery=getStringValue(request,"supplierTypeQuery"); 
		Integer hasXgSeller=getIntValue(request,"hasXgSeller");
		Integer addNewSupplierFlag=getIntValue(request,"addNewSupplierFlag");
		Long supplierIdQuery=getLongValue(request,"supplierIdQuery");
		Long skuId=getLongValue(request,"skuId");
		
		if (null == startPage) {
			startPage = 1;
		}
		if (null == pageSize) {
			pageSize = 20;
		}
		if (null == status) {
			status = 0;
		}
		 itemProxy.selectSupplier(model, startPage, pageSize,
					supplierNameQuery, supplierTypeQuery, hasXgSeller,
					addNewSupplierFlag, supplierIdQuery, skuId);
		 PageInfo<SupplierInfo> pageInfo=(PageInfo<SupplierInfo>) model.asMap().get("page");
		 return pageInfo;

	}

}
