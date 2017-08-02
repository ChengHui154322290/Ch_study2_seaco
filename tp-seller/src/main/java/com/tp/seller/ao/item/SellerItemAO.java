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
import com.tp.dto.prd.ItemResultDto;
import com.tp.proxy.prd.ItemDetailProxy;
import com.tp.query.prd.ItemQuery;
import com.tp.seller.ao.base.SellerBaseAO;
import com.tp.seller.util.SessionUtils;

/**  
 * ClassName:SellerItemAO <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2016年9月8日 上午10:24:59 <br/>  
 * @author   zhouguofeng  
 * @version    
 * @since   JDK 1.8  
 * @see        
 */
@Service
public class SellerItemAO extends SellerBaseAO {
	@Autowired
	private ItemDetailProxy itemDetailProxy;
	 /**
     * 根据条件查询商品信息	
     *
     * @param queryMap
     * @return
     */

    public PageInfo<ItemResultDto> queryItemByCondition(final HttpServletRequest request,Model model) {
        ItemQuery query=new ItemQuery();
        generateSearchCondition(request, query);
        final PageInfo<ItemResultDto> sellerOrderPageInfo = itemDetailProxy.listItem(query, query.getStartPage(), model, query.getPageSize());
        return sellerOrderPageInfo;

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
        final String spuId=getStringValue(request,"spuId");//供应商ID
        final Long largeId=getLongValue(request, "largeId");//大类
        final Long mediumId=getLongValue(request, "mediumId");//中类
        final Long smallId=getLongValue(request, "smallId");//小类
        final String spuName=getStringValue(request,"spuName");//spu名称
        final String  spu=getStringValue(request,"spu");//spu名称
        final String  name=getStringValue(request,"name");//网络显示名称
        final String  sku=getStringValue(request,"sku");//sku
        final String  barcode=getStringValue(request,"barcode");//条形码
        final Integer  status=getIntValue(request,"status");//发布状态
        final Date  createBeginTime=getDate(request,"createBeginTime","yyyy-MM-dd");//创建开始日期
        final Date  createEndTime=getDate(request,"createEndTime","yyyy-MM-dd");//创建截止日期
        final Integer itemType=getIntValue(request,"itemType");//商品类型
        final Long unitId=getLongValue(request,"unitId");//单位
        final Long brandId=getLongValue(request,"brandId");//品牌
        final Integer wavesSign=getIntValue(request,"wavesSign");//商品类型
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
        qo.setSupplierId(supplierId);
        qo.setSpu(spuId);
        qo.setLargeId(largeId);
        qo.setMediumId(mediumId);
        qo.setSmallId(smallId);
        qo.setSpuName(spuName);
        qo.setName(name);
        qo.setSpu(spu);
        qo.setBarcode(barcode);
        qo.setSku(sku);
        qo.setStatus(status);
        qo.setCreateBeginTime(createBeginTime);
        qo.setCreateEndTime(createEndTime);
        qo.setItemType(itemType);
        qo.setUnitId(unitId);
        qo.setWavesSign(wavesSign);
        qo.setBrandId(brandId);
        qo.setSearchFrom("1");//来源于供应商系统
    }
}
  
