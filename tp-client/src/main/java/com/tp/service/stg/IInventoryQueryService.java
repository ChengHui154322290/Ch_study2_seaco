/**
 * 
 */
package com.tp.service.stg;

import java.util.List;
import java.util.Map;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.StorageConstant.App;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.InventoryDto;
import com.tp.dto.stg.query.InventoryDtoQuery;
import com.tp.dto.stg.query.InventoryQuery;
import com.tp.dto.stg.query.SkuInventoryQuery;
import com.tp.model.stg.Inventory;
import com.tp.model.stg.InventoryDistribute;
import com.tp.model.stg.InventoryOccupy;

/**
 * @author szy
 * 库存查询服务类
 */
public interface IInventoryQueryService {
	

	
	

	/**
	 * 查询可用于发布新活动销售的库存数量
	 * @param sku
	 * @param warehouseId
	 * @return
	 **/
	List<InventoryDto> queryAvailableInventory(String sku, Long warehouseId);
	/**
	 * 查询可用于发布新活动销售的库存数量
	 * @param InventoryQuery
	 * @return
	 */
	List<InventoryDto> queryAvailableInventory(InventoryQuery query);
	/**
	 * 查询可用于发布新活动销售的库存数量
	 * @param List<InventoryQuery>
	 * @return
	 */
	List<InventoryDto> queryAvailableInventory(List<InventoryQuery> queries);
	/**
	 * 分页查询可用于发布新活动销售的库存数量
	 * @param List<InventoryQuery>
	 * @return
	 */
	PageInfo<InventoryDto> queryPageAvailableInventory(InventoryQuery inventoryQuery, PageInfo<InventoryDto> pageInfo);
	/**
	 * 查询可销售库存
	 * @param List<SkuInventoryQuery>
	 * @return
	 */
	Map<String, Integer> querySalableInventory(List<SkuInventoryQuery> skuInventoryQueries);
	
	/**
	 * 校验可销售库存是否充足
	 * @param List<SkuInventoryQuery>
	 * @return
	 */
	Map<String, Boolean> checkSalableInventory(List<SkuInventoryQuery> skuInventoryQueries);
	
	/**
	 * 查询可销售库存
	 * @param SkuInventoryQuery
	 * @return
	 */
	Integer querySalableInventory(App app, String bizId, String sku, Long warehouseId, boolean isPreOccupyInventory);
	/**
	 * 查询可销售库存
	 * @param SkuInventoryQuery
	 * @return
	 */
	Integer querySalableInventory(SkuInventoryQuery skuInventoryQuery);
	/**
	 * 校验可销售库存是否充足
	 * @param skuInventoryQuery
	 * @return
	 */
	boolean checkInventoryQuantity(App app, String bizId, String sku, Long warehouseId, boolean isPreOccupyInventory, Integer quantity);
	/**
	 * 校验可销售库存是否充足
	 * @param skuInventoryQuery
	 * @return
	 */
	boolean checkInventoryQuantity(SkuInventoryQuery skuInventoryQuery);
	/**
	 * 根据sku查询占用库存信息
	 * @param sku
	 * @return
	 */
	public PageInfo<InventoryOccupy> queryPageOccupyInfoBySku(String sku,Integer pageNo,Integer pageSize);
	/**
	 * 根据仓库ID查询库存
	 * @param warehouseId
	 * 		仓库id
	 * @return
	 */
	public List<Inventory> queryInventoryByWarehouseId(Long warehouseId);
	
//
//	/**
//	 * 根据sku编号获得sku在各仓库中可用于发布新活动销售的库存数量
//	 * @param sku
//	 * 		商品sku 必传
//	 * @return
//	 */
//	public List<InventoryDto> selectAvailableForSaleBySku(String sku);
//	
//	/**
//	 * 根据sku编号获得sku在各仓库中可用于发布新活动销售的库存数量
//	 * @param sku
//	 * 		商品sku 必传
//	 * @param warehouseId
//	 * 		仓库id 
//	 * @return
//	 */
//	public List<InventoryDto> selectAvailableForSaleBySkuAndWhId(String sku,Long warehouseId);
//	/**
//	 * 根据sku编号获得sku在各仓库中可用于发布新活动销售的库存数量
//	 * @param sku
//	 * 		商品sku 必传
//	 * @param warehouseId
//	 * @param spId
//	 * @return
//	 */
//	public List<InventoryDto> selectAvailableForSaleBySkuSpIdAndWId(InventoryDtoQuery dtoQuery);
//	/**
//	 * 根据sku编号获得sku在各仓库中可用于发布新活动销售的库存数量
//	 * @param sku
//	 * @param warehouseId
//	 * @param spId
//	 * @return
//	 */
//	public List<InventoryDto> selectAvailableForSaleBySkuAndWhIdList(List<InventoryDtoQuery> dtoQueries);
//	/**
//	 * 查询可用（可用于发布新的活动）库存信息
//	 * @param dtoQuery
//	 * 		查询条件
//	 * @param page
//	 * 		页码
//	 * @param pageSize
//	 * 		页数 最大50
//	 * @return
//	 */
//	public PageInfo<InventoryDto> queryAvailableForSaleBySkuSpIdAndWId(InventoryQuery dtoQuery, int page, int pageSize);
//	
//	/**
//	 * 查询可用（可用于发布新的活动）库存信息
//	 * @param inventoryQueries
//	 * 		查询条件
//	 * @param page
//	 * 		页码
//	 * @param pageSize
//	 * 		页数 最大50
//	 * @return
//	 */
//	public PageInfo<InventoryDto> queryAvailableForSaleBySkuSpIdAndWIdList(List<InventoryQuery> inventoryQueries, int page, int pageSize);
//
//	
//	/**
//	 * 根据sku列表查询库存信息
//	 * 
//	 * @param skuList
//	 * @return
//	 */
//	public List<Inventory> queryBySkuList(List<String> skuList);
//	/**
//	 * 根据单个sku获得库存信息
//	 * @param sku
//	 * @return
//	 */
//	public List<Inventory> queryBySku(String sku);
//	
//	
//	/**
//	 * 根据应用、业务id、sku获得商品当前业务活动中剩余的库存数量
//	 * @param app
//	 * 		应用
//	 * @param bizId
//	 * 		业务id
//	 * @param sku
//	 * 		商品sku编号
//	 * @return
//	 * 		剩余库存数量
//	 */
//	public int selectInvetory(App app,String bizId,String sku);
//	/**
//	 * 批量获得活动下商品的剩余库存信息
//	 * 
//	 * @param queries
//	 * @return
//	 */
//	public Map<String, Integer> batchSelectInventory(List<SkuInventoryQuery> queries);
//	
//	/**
//	 * 下单或加入购物车验证库存
//	 * @param app
//	 * 		应用
//	 * @param bizId
//	 * 		业务id
//	 * @param sku
//	 * 		商品sku编号
//	 * @param quantity
//	 * 		库存信息 
//	 * @return
//	 * 		true 可以购买 false 库存不足
//
//	 */
//	public boolean checkInventoryQuantity(App app,String bizId,String sku,Integer quantity);
//	
//	
//	/**
//	 * 批量验证库存
//	 * @param queries
//	 * @return
//	 * <pre>
//	 * 		反馈每一个商品的库存信息 key为sku，
//	 * 		resultMessage -true有库存 false库存不足，code为库存数量
//	 * </pre>
//	 */
//	public Map<String, ResultInfo<String>> checkAllInventory(List<SkuInventoryQuery> queries);
//	/**
//	 * 根据sku 查询有库存的业务活动信息
//	 * 
//	 * @param sku
//	 * @return
//	 */
//	public List<InventoryDistribute> queryHasInventoryBizInfo(App app, String sku);
//	
//	/**
//	 * 根据sku查询占用库存信息
//	 * @param sku
//	 * @return
//	 */
//	public PageInfo<InventoryOccupy> queryPageOccupyInfoBySku(String sku,Integer pageNo,Integer pageSize);
//	
//	
//	/**
//	 * 根据仓库ID查询库存
//	 * @param warehouseId
//	 * 		仓库id
//	 * @return
//	 */
//	public List<Inventory> queryInventoryByWarehouseId(Long warehouseId);
	
}
