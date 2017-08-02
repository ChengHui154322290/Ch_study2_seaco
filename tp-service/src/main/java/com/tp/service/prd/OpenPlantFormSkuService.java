package com.tp.service.prd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.prd.ItemSkuDao;
import com.tp.dao.prd.SkuOpenPlantFormDao;
import com.tp.dto.prd.OpenPlantFormItemDto;
import com.tp.dto.prd.SkuOpenDto;
import com.tp.model.prd.ItemPictures;
import com.tp.query.prd.OpenPlantFormItemQuery;
import com.tp.service.prd.IItemPicturesService;
import com.tp.service.prd.IOpenPlantFormSkuService;
import com.tp.util.StringUtil;


/***
 *  商品对外平台查询sku信息类
 * @author szy
 *
 */

@Service
public class OpenPlantFormSkuService implements IOpenPlantFormSkuService{
	
	
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IItemPicturesService itemPicturesService;
	
	
	@Autowired
	private SkuOpenPlantFormDao skuOpenPlantFormDao;
	
	@Autowired
	private ItemSkuDao itemSkuDao;
	
	@Override
	public SkuOpenDto getSkuInfoForSellerValidationWithBarCode(Map<String,Object> dataMap) {
		Assert.hasLength(dataMap.get("barCode").toString(), "获取sku信息传入barcode为空，调用方法：IOpenPlatFormSkuService.getSkuInfoForSellerValidationWithBarCode()");
		Assert.hasLength(dataMap.get("spId").toString(), "获取sku信息传入spID为空，调用方法：IOpenPlatFormSkuService.getSkuInfoForSellerValidationWithBarCode()");
		//第一层级查询sku
		SkuOpenDto returnInfo = skuOpenPlantFormDao.getSkuInfoForSellerValidationWithBarCode(dataMap);
		//第二层级查询detailId
		if(returnInfo ==null){
			returnInfo = skuOpenPlantFormDao.getProductDetailInfoForSellerValidationWithBarCode(dataMap);
		}
		if(returnInfo != null){
			List<Long> detailIds = new ArrayList<>();
			if(returnInfo.getDetailId() !=null){
				detailIds.add(returnInfo.getDetailId());
			}
			if(CollectionUtils.isNotEmpty(detailIds)){
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("main", Constant.DEFAULTED.YES);
				params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " detail_id in ("+StringUtil.join(detailIds,Constant.SPLIT_SIGN.COMMA)+")");
				List<ItemPictures> mainPictureInfo = itemPicturesService.queryByParam(params);
				if(CollectionUtils.isNotEmpty(mainPictureInfo)){
					returnInfo.setMainPicture(mainPictureInfo.get(0).getPicture());
				}
			}
		}
		return returnInfo;
	}

	@Override
	public PageInfo<OpenPlantFormItemDto> queryPageListByOpenPlantFormItemQuery(OpenPlantFormItemQuery openPlantFormItemQuery, int startPage,int pageSize) {
		if (openPlantFormItemQuery != null && startPage>0 && pageSize>0) {
			openPlantFormItemQuery.setStartPage(startPage);
			openPlantFormItemQuery.setPageSize(pageSize);
			Long totalCount = this.selectCountByLikeOfopenPlantFormItemQuery(openPlantFormItemQuery);
			List<OpenPlantFormItemDto> resultList;
			resultList = itemSkuDao.selectListOfByOpenPlantFormItemQuery(openPlantFormItemQuery);
			if(CollectionUtils.isNotEmpty(resultList)){
				for (OpenPlantFormItemDto openPlantFormItemDto : resultList) {
					openPlantFormItemDto.setItemMark(true);
				}
			}
			PageInfo<OpenPlantFormItemDto> page = new PageInfo<OpenPlantFormItemDto>();
			page.setPage(openPlantFormItemQuery.getStartPage());
			page.setSize(openPlantFormItemQuery.getPageSize());
			page.setRecords(totalCount.intValue());
			page.setRows(resultList);
			return page;		
		}
		return new PageInfo<OpenPlantFormItemDto>();
	}

	private Long selectCountByLikeOfopenPlantFormItemQuery(OpenPlantFormItemQuery openPlantFormItemQuery) {
		return itemSkuDao.selectCountByLikeOfopenPlantFormItemQuery(openPlantFormItemQuery);
	}

}
