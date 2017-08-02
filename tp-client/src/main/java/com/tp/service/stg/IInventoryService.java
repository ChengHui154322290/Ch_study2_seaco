package com.tp.service.stg;

import java.util.List;


import com.tp.dto.stg.query.InventoryDtoQuery;
import com.tp.dto.stg.query.InventoryQuery;
import com.tp.dto.wms.StockasnFactWithDetail;
import com.tp.model.stg.Inventory;
import com.tp.service.IBaseService;
import java.util.List;
import java.util.Map;

import com.tp.common.vo.PageInfo;
import com.tp.dto.stg.InventoryDto;
import com.tp.dto.stg.InventoryManageDto;
import com.tp.dto.wms.StockasnFactWithDetail;
import com.tp.model.stg.Inventory;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 库存信息表 记录sku的总库存、总销售占用库存信息接口
  */
public interface IInventoryService extends IBaseService<Inventory>{
	

	
	
	/**
	 * 增加现货库存
	 * @param sku
	 * @param spId
	 * @param warehouseId
	 * @param quantity
	 */
	Integer increaseRealInventory(String sku, Long spId, Long warehouseId, Integer quantity);
	
	/**
	 * 增加现货库存
	 * @param id
	 * @param inventory
	 */
	void increaseRealInventoryById(Long id, int inventory);
	/**
	 * 减少现货库存
	 * @param sku
	 * @param spId
	 * @param warehouseId
	 * @param quantity
	 */
	Integer reduceRealInventory(String sku, Long spId, Long warehouseId,Integer quantity);
	
	/**
	 * 冻结库存
	 * @param inventoryId
	 * 		库存记录id
	 * @param occupyInventory
	 * 		冻结库存数量
	 */
	Integer frozenOccupyInventoryById(Long inventoryId, int occupyInventory);
	
	/**
	 * 解冻库存，根据库存记录id，更新占用库存数量
	 * @param inventoryId
	 * 		库存记录id
	 * @param occupyInventory
	 * 		解冻库存数量
	 */
	void thawOccupyInventoryById(Long inventoryId, Integer occupyInventory);

	/**
	 * 批量查询库存信息
	 * @param queries: sku, spId, warehouseId 
	 */
	List<Inventory> queryInventoryInfo(List<Inventory> queries);
	PageInfo<Inventory> queryPageInventoryInfo(List<Inventory> queries, PageInfo<Inventory> pageInfo);
    PageInfo<Inventory> queryPageInventoryInfoByParam(Map<String,Object> param, PageInfo<Inventory> pageInfo);
    List<Inventory> queryInventoryInfoByParam(Map<String, Object> params);
	/**
	 * 减少占用库存，减少现货库存
	 * @param inventoryId
	 * @param inventory
	 */
	void reduceOccupyInventoryById(Long inventoryId, Integer inventory);

	/**
	 * 根据仓库预约单的回执增加库存
	 * @param stockasnFactWithDetail
	 * @return
     */
	Integer increaseInventoryForStockasnFact(StockasnFactWithDetail stockasnFactWithDetail);

    /** 获取库存列表 */
	PageInfo<InventoryManageDto> getPageByParamNotEmpty(Map<String, Object> params,
			PageInfo<InventoryManageDto> pageInfo);

	InventoryManageDto getInventoryById(Map<String, Object> params);
	
	/** 查询库存数据 */
	Inventory getInventoryById(Long id);
	/** 查询库存详细数据 */
	InventoryDto getInventoryDetailInfo(Inventory inventory);
	InventoryDto getInventoryDetailInfoById(Long inventoryId);
//
//	/**
//	 * 增加现货库存
//	 * @param sku
//	 * @param spId
//	 * @param warehouseId
//	 * @param quantity
//	 */
//	Integer increaseRealInventory(String sku, Long spId, Long warehouseId, Integer quantity);
//	
//	/**
//	 * 增加现货库存
//	 * @param id
//	 * @param inventory
//	 */
//	void increaseRealInventoryById(Long id, int inventory);
//	/**
//	 * 减少现货库存
//	 * @param sku
//	 * @param spId
//	 * @param warehouseId
//	 * @param quantity
//	 */
//	Integer reduceRealInventory(String sku, Long spId, Long warehouseId,Integer quantity);
//	
//	/**
//	 * 冻结库存
//	 * @param inventoryId
//	 * 		库存记录id
//	 * @param occupyInventory
//	 * 		冻结库存数量
//	 */
//	void frozenOccupyInventoryById(Long inventoryId, int occupyInventory);
//	
//	/**
//	 * 解冻库存，根据库存记录id，更新占用库存数量
//	 * @param inventoryId
//	 * 		库存记录id
//	 * @param occupyInventory
//	 * 		解冻库存数量
//	 */
//	void thawOccupyInventoryById(Long inventoryId, Integer occupyInventory);
//	/**
//	 * 根据sku和供应商列表查询库存信息
//	 * @param inventoryQueries
//	 * @param pageObj
//	 * @param slaveStorageDatasource
//	 * @return
//	 */
//	List<Inventory> selectPageBySkuAndSpIdList(List<InventoryQuery> inventoryQueries, Integer page, Integer pageSize);
//	
//	/**
//	 * 根据sku和供应商列表查询库存数量
//	 * @param inventoryQueries
//	 * @param pageObj
//	 * @param slaveStorageDatasource
//	 * @return
//	 */
//	Long selectCountBySkuAndSpIdList(List<InventoryQuery> inventoryQueries);
//	/**
//	 * 根据sku和仓库id批量查询库存信息
//	 * @param cleanQuery
//	 * @return
//	 */
//	List<Inventory> selectBySkuAndWhIdList(List<InventoryDtoQuery> cleanQuery);
//	/**
//	 * 减少占用库存，减少现货库存
//	 * @param inventoryId
//	 * @param inventory
//	 */
//	void reduceInventoryAndOccupy(Long inventoryId, Integer inventory);
//
//	/**
//	 * 根据仓库预约单的回执增加库存
//	 * @param stockasnFactWithDetail
//	 * @return
//     */
//	Integer increaseInventoryForStockasnFact(StockasnFactWithDetail stockasnFactWithDetail);
}
