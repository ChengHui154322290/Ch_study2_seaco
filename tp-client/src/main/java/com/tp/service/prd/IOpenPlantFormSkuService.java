package com.tp.service.prd;

import java.util.Map;

import com.tp.common.vo.PageInfo;
import com.tp.dto.prd.OpenPlantFormItemDto;
import com.tp.dto.prd.SkuOpenDto;
import com.tp.query.prd.OpenPlantFormItemQuery;


/***
 *  sku 对外平台访问接口
 * @author szy
 *
 */
public interface IOpenPlantFormSkuService {
	
	/***
	 * 商家平台  通过条形码 获取商品的sku信息
	 * @param dataMap
	 * @return
	 */
	SkuOpenDto getSkuInfoForSellerValidationWithBarCode(Map<String,Object> dataMap);
	
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
}
