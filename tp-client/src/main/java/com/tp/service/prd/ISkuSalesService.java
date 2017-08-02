package com.tp.service.prd;

import com.tp.common.vo.PageInfo;
import com.tp.query.prd.SkuSalesQuery;
import com.tp.result.prd.SkuSalesResult;


/**
 * 发布促销活动时查询商品信息接口
 * @author szy
 * 2014年12月25日 下午7:31:00
 *
 */
public interface ISkuSalesService {
	/**
	 * 发布促销活动时查询商品信息 分页查询
	 * @param query
	 * 		查询条件 
	 * @param page
	 * @return
	 */
	public PageInfo<SkuSalesResult> selectSkuSalesByPage(SkuSalesQuery query, PageInfo<SkuSalesResult> page);
}
