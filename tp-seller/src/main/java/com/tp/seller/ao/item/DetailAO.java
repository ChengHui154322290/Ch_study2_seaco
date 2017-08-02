package com.tp.seller.ao.item;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.BseConstant.DictionaryCode;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.prd.enums.ItemSaleTypeEnum;
import com.tp.dto.prd.enums.ItemStatusEnum;
import com.tp.dto.prd.enums.ItemTypesEnum;
import com.tp.model.bse.Category;
import com.tp.model.bse.DictionaryInfo;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemInfo;
import com.tp.query.prd.DetailQuery;
import com.tp.query.prd.ItemQuery;
import com.tp.seller.ao.base.CategoryAO;
import com.tp.seller.ao.base.DictionaryInfoAO;
import com.tp.service.bse.ICategoryService;
import com.tp.service.prd.IItemDetailService;
import com.tp.service.prd.IItemManageService;

@Service
public class DetailAO {

	@Autowired
	private IItemDetailService itemDetailService;
	@Autowired
	private ICategoryService categoryService;
	
	@Autowired
	private IItemManageService itemManageService;
	
	@Autowired
	private CategoryAO categoryServiceAO;
	
	@Autowired
	private DictionaryInfoAO dictionaryInfoAO;
	
	@Autowired
	private SkuAO skuAO;
	public PageInfo<ItemResultDto> searchItemByQuery(ItemQuery query) {
		DetailQuery detailQuery = new DetailQuery();
		detailQuery.setBrandName(query.getBrandName());
		detailQuery.setCreateBeginTime(query.getCreateBeginTime());
		detailQuery.setCreateEndTime(query.getCreateEndTime());
		detailQuery.setItemType(query.getItemType());
		detailQuery.setName(query.getName());
		detailQuery.setPageSize(query.getPageSize());
		detailQuery.setPrdid(query.getPrdid());
		detailQuery.setSpu(query.getSpu());
		detailQuery.setStartPage(query.getStartPage());
		detailQuery.setUnitId(query.getUnitId());
		detailQuery.setBarcode(query.getBarcode());
		detailQuery.setCategoryCode(query.getCategoryCode());
		detailQuery.setStatus(query.getStatus());
		detailQuery.setBrandId(query.getBrandId());
		detailQuery.setSpuName(query.getSpuName());
		detailQuery.setWavesSign(query.getWavesSign());
		if(query.getCreateEndTime()!=null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(query.getCreateEndTime());
			calendar.add(Calendar.DATE, 1);
			detailQuery.setCreateEndTime(calendar.getTime());
		}
		PageInfo<ItemResultDto> itemPageInfo = itemDetailService.queryPageByQuery(detailQuery);
		
		//处理itemReusltDto里面的基础服务的数据
		bindBaseData(itemPageInfo.getRows());
		return itemPageInfo;
	}
	
	public PageInfo<ItemResultDto> listItem(ItemQuery query, Integer pageNo,
			Model model, Integer pageSize) {
		// 获得商品分类信息 大类
		List<Category> categoryList = categoryServiceAO.getFirstCategoryList();
		model.addAttribute("categoryList", categoryList);
		query.setStartPage(pageNo==null?1:pageNo);
		query.setPageSize(pageSize);
		PageInfo<ItemResultDto> page = null;
		initItemQuery(query);
		//查询sku列表
		if(StringUtils.isNotBlank(query.getSku())||null!=query.getSupplierId()){
			page = skuAO.searchItemByQuery(query);
			//获取sku对应的商品类型
			bindSkuWavesSign(page.getRows());
			//处理itemReusltDto里面的基础服务的数据
			bindBaseData(page.getRows());
			//main_title
			//bindMainTitle(page.getList());
		}else{
			page = searchItemByQuery(query);
		}
	
		DictionaryInfo dictionaryItemInfo = new DictionaryInfo();
		dictionaryItemInfo.setCode(DictionaryCode.c1001.getCode());
		List<DictionaryInfo> unitList = dictionaryInfoAO.queryByObject(dictionaryItemInfo);
		model.addAttribute("unitList", unitList);
		model.addAttribute("itemStatus", ItemStatusEnum.values());
		model.addAttribute("itemTypes", ItemTypesEnum.values());
		model.addAttribute("itemSaleTypes", ItemSaleTypeEnum.values());
		model.addAttribute("query", query);
		//查询商品分类
		model.addAttribute("largeId", query.getLargeId());
		model.addAttribute("mediumId", query.getMediumId());
		model.addAttribute("smallId", query.getSmallId());
		model.addAttribute("status", query.getStatus());
		model.addAttribute("supplierId", query.getSupplierId());
		model.addAttribute("spuName", query.getSpuName());
		return page;
	}
	
	/**
	 * 
	 * <pre>
	 *   初始化商品查询接口
	 * </pre>
	 *
	 * @param query
	 */
	private void  initItemQuery (ItemQuery query){
		String code = "";
		if(null!=query.getLargeId()){
			Category categoryDO = categoryService.queryById(query.getLargeId());
			code = categoryDO.getCode();
		}
		if(null!=query.getMediumId()){
			Category categoryDO = categoryService.queryById(query.getMediumId());
			code = categoryDO.getCode();
			
		}
		if(null!=query.getSmallId()){
			Category categoryDO = categoryService.queryById(query.getSmallId());
			code = categoryDO.getCode();
		}
		query.setCategoryCode(code);
	}
	
	/**
	 * 
	 * <pre>
	 * 	绑定base数据
	 * </pre>
	 *
	 * @param list
	 */
	private void bindBaseData(List<ItemResultDto> list){
		
		List<Long> itemIds = new ArrayList<Long>();
		if(CollectionUtils.isNotEmpty(list)){
			for(ItemResultDto itemResult : list){
				if(!itemIds.contains(itemResult.getItemId())){
					itemIds.add(itemResult.getItemId());
				}
			}
			List<ItemInfo> infoList = itemManageService.getInfoListByIds(itemIds);
			List<Long> categoryIds = new ArrayList<Long>();
			if(CollectionUtils.isNotEmpty(infoList)){
				for(ItemInfo infoDO : infoList){
					if(!categoryIds.contains(infoDO.getLargeId())){
						categoryIds.add(infoDO.getLargeId());
					}
					if(!categoryIds.contains(infoDO.getMediumId())){
						categoryIds.add(infoDO.getMediumId());
					}
					if(!categoryIds.contains(infoDO.getSmallId())){
						categoryIds.add(infoDO.getSmallId());
					}
				}
				List<Category> categoryList = categoryService.selectByIdsAndStatus(categoryIds, ItemConstant.ALL_DATAS);
				for(ItemInfo infoDO : infoList){
					for(Category categoryDO : categoryList){
						if(infoDO.getLargeId().equals(categoryDO.getId())){
							infoDO.setLargeName(categoryDO.getName());
						}
						if(infoDO.getMediumId().equals(categoryDO.getId())){
							infoDO.setMediumName(categoryDO.getName());
						}
						if(infoDO.getSmallId().equals(categoryDO.getId())){
							infoDO.setSmallName(categoryDO.getName());
						}
					}
					
				}
				for(ItemResultDto itemResult : list){
					for(ItemInfo infoDO : infoList){
						if(infoDO.getId().equals(itemResult.getItemId())){
							itemResult.setLargeName(infoDO.getLargeName());
							itemResult.setMediumName(infoDO.getMediumName());
							itemResult.setCategoryName(infoDO.getSmallName());
						}
					}
				}
			}
		}
	}
	
	
	
	/***
	 * 绑定sku商品类型
	 * @param list
	 */
	private void bindSkuWavesSign(List<ItemResultDto> list){
		List<Long> detailIds = new ArrayList<Long>();
		if(CollectionUtils.isNotEmpty(list)){
			for(ItemResultDto itemResult : list){
				if(!detailIds.contains(itemResult.getDetailId())){
					detailIds.add(itemResult.getDetailId());
				}
			}
		}
		if(CollectionUtils.isNotEmpty(detailIds)){
			 List<ItemDetail> listDetails = 	itemDetailService.selectByDetailIds(detailIds);
			 if(CollectionUtils.isNotEmpty(listDetails)){
					for(ItemResultDto itemResult : list){
						for(ItemDetail ddo : listDetails){
							if(ddo.getId().equals(itemResult.getDetailId())){
								itemResult.setWavesSign(ddo.getWavesSign());
							}
						}
					}
				 
			 }
		}
	}
	
	
	
	
	
}
