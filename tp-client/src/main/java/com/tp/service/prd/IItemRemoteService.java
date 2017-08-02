package com.tp.service.prd;

import java.util.List;
import java.util.Map;

import com.tp.common.vo.PageInfo;
import com.tp.dto.prd.DetailMainInfoDto;
import com.tp.dto.prd.ItemDetailSalesCountDto;
import com.tp.dto.prd.SkuDto;
import com.tp.exception.ItemServiceException;
import com.tp.model.prd.ItemDetailSalesCount;

public interface IItemRemoteService {


	/***
	 * 跟新商品(销售数量)
	 * @return
	 */
	int updateItemSalesCount(Map<String,Integer> map)throws ItemServiceException;
	
	
	/***
	 * 调整销售数量 基数
	 * @param dto
	 * @return
	 * @throws ItemServiceException
	 */
	int   updateDetailSalesCountDefaultCount(ItemDetailSalesCountDto dto)throws ItemServiceException;
	
	/***
	 * 更具条形码批量插入
	 * @param insertList
	 * @return
	 * @throws ItemServiceException
	 */
	String   insertWithPRDIDS(List<ItemDetailSalesCount> insertList)throws ItemServiceException;
	
	/***
	 * 更具条形码批量插入
	 * @param insertList
	 * @return
	 * @throws ItemServiceException
	 */
	String   insertWithBarcodes(List<ItemDetailSalesCount> insertList)throws ItemServiceException;
	
	/***
	 * 更具
	 * @param sku
	 * @return
	 * @throws ItemServiceException
	 */
	int  getSalesCountBySku(String sku) throws ItemServiceException;
	
	
	/***
	 * 列表查询sku销售数量
	 * @param skuList
	 * @return
	 * @throws ItemServiceException
	 */
	Map<String,Integer> getSalesCountBySkuList(List<String> skuList)throws ItemServiceException;
	
	/***
	 * 获取商品的运费信息和商品信息
	 * @param skuCode  
	 * @return
	 * 
	 * spuId itemId ,spuCode spu ,spuName spuName,brandId brandId,brandName brandName,skuID id,skuCode sku,barCode barCode 
	 * 
	 * @throws ItemServiceException
	 */
	SkuDto getPostageInfoAndItemInfo(String skuCode)throws ItemServiceException;


	/**
	 * @param dto
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	PageInfo<ItemDetailSalesCountDto> queryDetailSalesPageListByStartPageSize(ItemDetailSalesCountDto dto, int startPage, int pageSize);


	/**
	 * @param detailIds
	 * @return
	 */
	List<DetailMainInfoDto> getMaintitlesByDetailIds(List<Long> detailIds);
			
}
