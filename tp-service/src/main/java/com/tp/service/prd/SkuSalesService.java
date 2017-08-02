package com.tp.service.prd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.model.prd.ItemPictures;
import com.tp.model.prd.ItemSku;
import com.tp.query.prd.SkuSalesQuery;
import com.tp.result.prd.SkuSalesResult;
import com.tp.service.prd.IItemDetailService;
import com.tp.service.prd.IItemPicturesService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.prd.ISkuSalesService;
import com.tp.util.StringUtil;

@Service
public class SkuSalesService implements ISkuSalesService{

	@Autowired
	private IItemSkuService itemSkuService;
	
	@Autowired
	private IItemDetailService itemDetailService;
	
	@Autowired
	private IItemPicturesService itemPicturesService;
	
	
	@Override
	public PageInfo<SkuSalesResult> selectSkuSalesByPage(SkuSalesQuery query, PageInfo<SkuSalesResult> page) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sku", query.getSku());
		params.put("spu", query.getSpu());
		params.put("spName", query.getSpName());
		params.put("prdId", query.getPrdid());
		params.put("detailName", query.getDetailName());
		//查询sku商品信息
		PageInfo<ItemSku> itemSkuPageInfo = new PageInfo<ItemSku>(page.getPage(),page.getSize());
		itemSkuPageInfo = itemSkuService.queryPageByParamNotEmpty(params, itemSkuPageInfo);
		if(CollectionUtils.isNotEmpty(itemSkuPageInfo.getRows())){
			List<SkuSalesResult> resultList = new ArrayList<SkuSalesResult>();
			List<Long> detailIds = new ArrayList<Long>();
			for (ItemSku item : itemSkuPageInfo.getRows()) {
				detailIds.add(item.getDetailId());
				SkuSalesResult result = new SkuSalesResult();
				result.setDetailName(item.getDetailName());
				result.setId(item.getId());
				result.setSku(item.getSku());
				result.setDetailId(item.getDetailId());
				resultList.add(result);
			}
			params.clear();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "detail_id in("+StringUtil.join(detailIds, Constant.SPLIT_SIGN.COMMA)+")");
			params.put("main", Constant.DEFAULTED.YES);
			List<ItemPictures> pictureList = itemPicturesService.queryByParam(params);
			if(CollectionUtils.isNotEmpty(pictureList)){
				for (SkuSalesResult item : resultList) {
					for(ItemPictures itemPictures:pictureList){
						if(item.getDetailId().equals(itemPictures.getDetailId())){
							item.setPicture(itemPictures.getPicture());
						}
					}
				}
			}
			page.setRows(resultList);
			return page;
		}
		return null;
	}

}
