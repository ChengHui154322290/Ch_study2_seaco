package com.tp.dao.stg;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.dto.stg.InventoryManageDto;
import com.tp.model.stg.Inventory;

public interface InventoryDao extends BaseDao<Inventory> {
	


	/**
	 * 增加现货库存
	 * @param sku
	 * @param spId
	 * @param warehouseId
	 * @param quantity
	 */
	Integer increaseRealInventory(@Param("sku")String sku, @Param("spId")Long spId, 
			@Param("warehouseId")Long warehouseId, @Param("quantity")Integer quantity);
	
	/**
	 * 增加现货库存
	 * @param id
	 * @param inventory
	 */
	void increaseRealInventoryById(@Param("id")Long id, @Param("quantity")int inventory);
	/**
	 * 减少现货库存
	 * @param sku
	 * @param spId
	 * @param warehouseId
	 * @param quantity
	 */
	Integer reduceRealInventory(@Param("sku")String sku, @Param("spId")Long spId, @Param("warehouseId")Long warehouseId,
			@Param("quantity")Integer quantity);
	
	/**
	 * 冻结库存
	 * @param inventoryId
	 * 		库存记录id
	 * @param occupyInventory
	 * 		冻结库存数量
	 */
	Integer frozenOccupyInventoryById(@Param("id")Long inventoryId, @Param("occupyInventory")int occupyInventory);
	
	/**
	 * 解冻库存，根据库存记录id，更新占用库存数量
	 * @param inventoryId
	 * 		库存记录id
	 * @param occupyInventory
	 * 		解冻库存数量
	 */
	void thawOccupyInventoryById(@Param("id")Long inventoryId, @Param("occupyInventory")Integer occupyInventory);
	/**
	 * @param queries : spId, warehouseId, sku 
	 */
	List<Inventory> queryPageInventoryInfo(@Param("list")List<Inventory> queries, @Param("start")Integer start, @Param("pageSize")Integer pageSize);
	Long queryCountInventoryInfo(@Param("list")List<Inventory> queries);
	List<Inventory> queryInventoryInfo(@Param("list")List<Inventory> queries);
	/**
	 * @param queries : spId, warehouseId, sku, maxUsableInventory, minUsableInventory
	 */
	Long queryCountInventoryInfoByParam(Map<String, Object> params);
	List<Inventory> queryPageInventoryInfoByParam(Map<String, Object> params);
	List<Inventory> queryInventoryInfoByParam(Map<String, Object> params);
	/**
	 * 减少占用库存，减少现货库存
	 * @param inventoryId
	 * @param inventory
	 */
//	void reduceInventoryAndOccupy(@Param("inventoryId")Long inventoryId, @Param("inventory")Integer inventory);

	void reduceOccupyInventoryById(@Param("inventoryId")Long inventoryId, @Param("inventory")Integer inventory);
	
	Integer batchInsert(List<Inventory> list);


	List<InventoryManageDto> getInventoryList(Map<String, Object> params);

	int getInventoryListCount(Map<String, Object> params);
	public InventoryManageDto getInventoryById(Map<String, Object> params);
//
//
//	/**
//	 * 增加现货库存
//	 * @param sku
//	 * @param spId
//	 * @param warehouseId
//	 * @param quantity
//	 */
//	Integer increaseRealInventory(@Param("sku")String sku, @Param("spId")Long spId, 
//			@Param("warehouseId")Long warehouseId, @Param("quantity")Integer quantity);
//	
//	/**
//	 * 增加现货库存
//	 * @param id
//	 * @param inventory
//	 */
//	void increaseRealInventoryById(@Param("id")Long id, @Param("quantity")int inventory);
//	/**
//	 * 减少现货库存
//	 * @param sku
//	 * @param spId
//	 * @param warehouseId
//	 * @param quantity
//	 */
//	Integer reduceRealInventory(@Param("sku")String sku, @Param("spId")Long spId, @Param("warehouseId")Long warehouseId,
//			@Param("quantity")Integer quantity);
//	
//	/**
//	 * 冻结库存
//	 * @param inventoryId
//	 * 		库存记录id
//	 * @param occupyInventory
//	 * 		冻结库存数量
//	 */
//	void frozenOccupyInventoryById(@Param("id")Long inventoryId, @Param("occupyInventory")int occupyInventory);
//	
//	/**
//	 * 解冻库存，根据库存记录id，更新占用库存数量
//	 * @param inventoryId
//	 * 		库存记录id
//	 * @param occupyInventory
//	 * 		解冻库存数量
//	 */
//	void thawOccupyInventoryById(@Param("id")Long inventoryId, @Param("occupyInventory")Integer occupyInventory);
//	/**
//	 * 根据sku和供应商列表查询库存信息
//	 * @param inventoryQueries
//	 * @param pageObj
//	 * @param slaveStorageDatasource
//	 * @return
//	 */
//	List<Inventory> selectPageBySkuAndSpIdList(@Param("queries")List<InventoryQuery> inventoryQueries, 
//			@Param("start")Integer start, @Param("pageSize")Integer pageSize);
//	
//	/**
//	 * 根据sku和供应商列表查询库存数量
//	 * @param inventoryQueries
//	 * @param pageObj
//	 * @param slaveStorageDatasource
//	 * @return
//	 */
//	Long selectCountBySkuAndSpIdList(@Param("queries")List<InventoryQuery> inventoryQueries);
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
//	void reduceInventoryAndOccupy(@Param("inventoryId")Long inventoryId, @Param("inventory")Integer inventory);
//
//
//	List<Inventory> queryByInventoriesForStockasn(List<Inventory> list);
//
//	Integer batchInsert(List<Inventory> list);
}
