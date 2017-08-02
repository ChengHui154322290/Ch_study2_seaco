package com.tp.service.prd;

import java.util.Date;
import java.util.List;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.prd.SkuDetailInfoDto;
import com.tp.dto.prd.SkuDto;
import com.tp.exception.ItemServiceException;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemSku;
import com.tp.query.prd.ItemQuery;
import com.tp.query.prd.SellerSkuQuery;
import com.tp.service.IBaseService;
/**
  * @author szy
  * SPU对应SKU维度信息接口
  */
public interface IItemSkuService extends IBaseService<ItemSku>{
	
	/**
	 * 
	 * <pre>
	 *  更具 itemId 和 supplierId 查询productDetailIds 
	 * </pre>
	 *
	 * @param InfoDetailDto
	 * @return List<ItemSku>
	 * @throws DAOException
	 */
	List<SkuDto>  selectProductDeatilsByItemIdAndSpId(ItemSku itemSku);
	
	/***
	 * 获取 info_detail 数据
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	List<SkuDto> selectSkuDetailInfo(List<Long> skuIds);  
	
	/**
	 * 根据sku列表查询sku级别的信息
	 * @param skuList
	 * @return
	 */
	List<ItemSku> selectSkuListBySkuCode(List<String> skuList);
	
	/***
	 * 
	 * <pre>
	 *  更具 detailId 查询规格信息
	 * </pre>
	 * @param detailId
	 * @return
	 * @throws DAOException
	 */
	List<ItemDetailSpec> selectSkuDetailSpecInfos(Long detailId);  
	
	/***
	 * 
	 * <pre>
	 *  更具 detailIds 查询规格信息
	 * </pre>
	 * @param detailId
	 * @return
	 * @throws DAOException
	 */
	List<ItemDetailSpec> selectSpecByDetailIds(List<Long> skuIds);

	/**
	 * 根据query当中的条件查询商品信息
	 * @param query
	 * @return
	 */
	List<ItemSku> selectSkuListByBarcode(String barcode);
	/**
	 * 根据sku获得sku级别信息
	 * @param sku
	 * @return
	 */
	ItemSku selectSkuInfoBySkuCode(String sku);  
	
	/***
	 * 主库查询同detailId下spID 相同的记录
	 */
	
	ItemSku checQniqueSkuWithDetailIdAndSpId(ItemSku check);
	
	/***
	 * 主库查询sku信息
	 * @param id
	 * @return
	 * @throws ItemServiceException
	 */
	ItemSku selectByIdFromMaster(Long id);

	PageInfo<ItemResultDto> searchItemByQuery(ItemQuery query);

	Integer updateSkuInfoWithItemSpuInfo(ItemSku updateLast);

	PageInfo<ItemSku> queryAllLikedSkuByBySellerSkuQueryPage(
			SellerSkuQuery sellerSkuQuery, PageInfo<ItemSku> pageInfo);

	List<ItemQuery> checkSellerSkuWithAuditStatusAndLevel(ItemQuery searchQuery);

	/**
	 * 根据sku集合查询itemsku对象集合
	 * @param skus
	 * @return
	 */
	ResultInfo<List<ItemSku>> queryItemListBySkus(List<String> skus);
	
	
	List<SkuDetailInfoDto> selectCommisionRateByListSkus(List<String> skuList);

	/**
	 * 更新促销价格
	 * @param itemSku
	 * @return
	 */
	public Integer updateTopicPrice(ItemSku itemSku);
	/**
	 * 根据折扣批量更新促销价格
	 * @param idList
	 * @param discount
	 * @param userName
	 * @return
	 */
	Integer updateBatchPrice(List<String> skuList, Float discount, String userName);

	List<ItemSku> queryByUpdateTime(Date updateTime);
}


