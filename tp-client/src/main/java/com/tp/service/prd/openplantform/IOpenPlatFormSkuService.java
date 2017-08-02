package com.tp.service.prd.openplantform;


import java.util.Map;

import com.tp.common.vo.PageInfo;
import com.tp.dto.prd.OpenPlantFormItemDto;
import com.tp.dto.prd.SkuOpenDto;
import com.tp.model.cms.Page;
import com.tp.query.prd.OpenPlantFormItemQuery;


/***
 *  sku 对外平台访问接口
 * @author sailing
 *
 */
public interface IOpenPlatFormSkuService {
	
	/***
	 * 商家平台  通过条形码 获取商品的sku信息
	 * @param dataMap
	 * @return
	 */
	SkuOpenDto getSkuInfoForSellerValidationWithBarCode(Map dataMap);
	
	/**
	 * 
	 * <pre>
	 *  商家平台商品列表查询结果(已经分页)
	 * </pre>
	 *
	 * @param openPlantFormItemQuery
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	 PageInfo<OpenPlantFormItemDto> queryPageListByOpenPlantFormItemQuery( OpenPlantFormItemQuery openPlantFormItemQuery,int startPage,int pageSize);
     
	 /**
	  * 
	  * <pre>
	  * 获取主库供应商的所有商品列表
	  * </pre>
	  *
	  * @param query
	  * @param startPage
	  * @param pageSize
	  * @return
	  */
	 PageInfo<OpenPlantFormItemDto> querySpItemListByOpenPlantFormItemQuery(OpenPlantFormItemQuery query, int startPage, int pageSize);
}
