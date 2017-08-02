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
import com.tp.dto.prd.SkuImportLogDto;
import com.tp.model.prd.ItemImportList;
import com.tp.proxy.prd.ItemDetailProxy;
import com.tp.proxy.prd.ItemImportProxy;
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
public class SellerItemImportAO extends SellerBaseAO {
	@Autowired
	private ItemDetailProxy itemDetailProxy;
	@Autowired
	ItemImportProxy itemImportProxy;

	/**
	 * 根据条件查询订单信息
	 *
	 * @param queryMap
	 * @return
	 */

	public PageInfo<ItemImportList> queryItemByCondition(final HttpServletRequest request, Model model) {
		ItemQuery query = new ItemQuery();
		generateSearchCondition(request, query);
		String userName = (String) request.getSession().getAttribute(SellerConstant.USER_NAME_KEY);
		Integer status = getIntValue(request, "status");
		Integer startPage = getIntValue(request, "start");
		Integer pageSize = getIntValue(request, "pageSize");

		if (null == startPage) {
			startPage = 1;
		}
		if (null == pageSize) {
			pageSize = 20;
		}
		if (null == status) {
			status = 0;
		}
		SkuImportLogDto skuImportLogDto = itemImportProxy.queryImportLogDto(userName, status, startPage, pageSize);
		PageInfo<ItemImportList> pageInfo = itemImportProxy.queryImportList(skuImportLogDto, startPage, pageSize);
		return pageInfo;

	}

	/**
	 * 生成查询条件
	 *
	 * @param request
	 * @param qo
	 * @return
	 */
	private void generateSearchCondition(final HttpServletRequest request, final ItemQuery qo) {
		final Long supplierId = SessionUtils.getSupplierId(request);
		final Long orderCode = getLongValue(request, "orderCode");
		final Date startTime = getDate(request, "startTime", null);
		final Date endTime = getDate(request, "endTime", null);
		final Integer orderStatus = getIntValue(request, "orderStatus");
		final Integer orderType = getIntValue(request, "orderType");
		final Long deliveryWay = getLongValue(request, "deliveryWay");

		Integer startPage = getIntValue(request, "start");
		Integer pageSize = getIntValue(request, "pageSize");

		if (null == startPage) {
			startPage = 1;
		}
		if (null == pageSize) {
			pageSize = 20;
		}

		qo.setStartPage(startPage);
		qo.setPageSize(pageSize);
	}
}
