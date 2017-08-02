package com.tp.seller.ao.item;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.dto.prd.ItemResultDto;
import com.tp.query.prd.ItemQuery;
import com.tp.service.prd.IItemSkuService;

/**
 * 基于sku纬度的商品信息查询
 * @author 付磊
 * 2015年1月3日 下午3:49:15
 *
 */
@Service
public class SkuAO {
	@Autowired
	private IItemSkuService itemSkuService;
	
	
	public PageInfo<ItemResultDto> searchItemByQuery(ItemQuery query) {
		if(query.getCreateEndTime()!=null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(query.getCreateEndTime());
			calendar.add(Calendar.DATE, 1);
			query.setCreateEndTime(calendar.getTime());
		}
		PageInfo<ItemResultDto> itemPageInfo = itemSkuService.searchItemByQuery(query);
		return itemPageInfo;
	}
}
