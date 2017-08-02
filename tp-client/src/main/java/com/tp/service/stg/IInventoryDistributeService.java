package com.tp.service.stg;

import java.util.List;
import java.util.Map;

import com.tp.model.stg.InventoryDistribute;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 库存分配记录表 可分配库存数量=真实库存数量-各业务系统分配的库存数量-占用库存数量，可销售库存=分配库存-已售出数量接口
  */
public interface IInventoryDistributeService extends IBaseService<InventoryDistribute>{

	

	/**
	 * 增加分配库存数量
	 * @param id
	 * @param inventory
	 */
	void increaseInventoryDistributeById(Long id, int inventory);
	InventoryDistribute increaseInventoryDistribute(InventoryDistribute distribute, int inventory);
	/**
	 * 更新backed状态
	 * @param id
	 * 		主键id
	 * @param backed
	 * 		是否已归还申请库存 0-否（未归还，即活动还在进行） 1-是(已归还，则该活动的订单在取消的时候，不再操作分配库存信息)
	 */
	void updateBackedStatus(Long id, int backed);
	/**
	 * 减少分配库存，减少库存数量必须小于现有库存
	 * 
	 * @param id
	 * @param invetory
	 * 		减少库存数量
	 */
	void reduceInventoryDistributeById(Long id, int inventory);
	InventoryDistribute reduceInventoryDistribute(InventoryDistribute distribute, int inventory);
	/**
	 * 下单时 冻结库存，减少可销售库存，增加冻结库存数量
	 * @param id
	 * 		分配库存记录
	 * @param inventory
	 * 		冻结库存数量
	 * @return 
	 * 		返回更新影响行数
	 */
	int forzenDistInventory(Long id, int inventory);
	/**
	 * 取消订单时，解冻库存，减少商品活动库存的冻结库存数量，增加可销售库存数量
	 * @param id
	 * 		活动库存记录id
	 * @param inventory
	 * 		大于0，增加可销售库存数量，减少占用库存数量
	 */
	void thawDistInventory(Long id, Integer inventory);
	
	Integer reduceOccupyDistributeById(Long distributeId, Integer inventory);
	
	/**
	 * 库存归还
	 * @param id 主键
	 * @return
	 */
	void backDistributeInventoryById(Long id);
	InventoryDistribute backDistributeInventory(InventoryDistribute inventoryDistribute);
	/**
	 * 批量查询活动商品库存分配数据
	 * @param apps
	 * @param bizs
	 * @param skus
	 * @return
	 */
	List<InventoryDistribute> queryInventoryDistributes(List<InventoryDistribute> queries);
	
	/** 查询库存分配记录 */
	List<InventoryDistribute> queryInventoryDistributesByInventoryId(Long inventoryId);
	
	
//	/**
//	 * 增加分配库存数量
//	 * @param id
//	 * @param inventory
//	 */
//	void increaseInventoryDistributeById(Long id, int inventory);
//	/**
//	 * 更新backed状态
//	 * @param id
//	 * 		主键id
//	 * @param backed
//	 * 		是否已归还申请库存 0-否（未归还，即活动还在进行） 1-是(已归还，则该活动的订单在取消的时候，不再操作分配库存信息)
//	 */
//	void updateBackedStatus(Long id, int backed);
//	/**
//	 * 减少分配库存，减少库存数量必须小于现有库存
//	 * 
//	 * @param id
//	 * @param invetory
//	 * 		减少库存数量
//	 */
//	void reduceInventoryDistributeById(Long id, int inventory);
//	/**
//	 * 下单时 冻结库存，减少可销售库存，增加冻结库存数量
//	 * @param id
//	 * 		分配库存记录
//	 * @param inventory
//	 * 		冻结库存数量
//	 * @return 
//	 * 		返回更新影响行数
//	 */
//	int forzenDistInventory(Long id, int inventory);
//	/**
//	 * 取消订单时，解冻库存，减少商品活动库存的冻结库存数量，增加可销售库存数量
//	 * @param id
//	 * 		活动库存记录id
//	 * @param inventory
//	 * 		大于0，增加可销售库存数量，减少占用库存数量
//	 */
//	void thawDistInventory(Long id, Integer inventory);
//	/**
//	 * 批量查询活动商品库存信息
//	 * @param apps
//	 * @param bizs
//	 * @param skus
//	 * @return
//	 */
//	List<InventoryDistribute> selectByAppAndSkuAndBizIds(List<Map<String, String>> params);
//	/**
//	 * 减少冻结库存
//	 * @param app
//	 * @param bizId
//	 * @param sku
//	 * @param inventory
//	 */
//	void reduceOccupyDistribute(String app, String bizId, String sku, Integer inventory);
}
