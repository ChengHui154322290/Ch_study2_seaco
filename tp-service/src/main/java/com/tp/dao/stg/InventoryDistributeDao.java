package com.tp.dao.stg;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.stg.InventoryDistribute;

public interface InventoryDistributeDao extends BaseDao<InventoryDistribute> {


	
	/**
	 * 增加分配库存数量
	 * @param id
	 * @param inventory
	 */
	void increaseInventoryDistributeById(@Param("id")Long id, @Param("inventory")int inventory);
	/**
	 * 更新backed状态
	 * @param id
	 * 		主键id
	 * @param backed
	 * 		是否已归还申请库存 0-否（未归还，即活动还在进行） 1-是(已归还，则该活动的订单在取消的时候，不再操作分配库存信息)
	 */
	void updateBackedStatus(@Param("id")Long id, @Param("backed")int backed);
	/**
	 * 减少分配库存，减少库存数量必须小于现有库存
	 * 
	 * @param id
	 * @param invetory
	 * 		减少库存数量
	 */
	void reduceInventoryDistributeById(@Param("id")Long id, @Param("inventory")int inventory);
	/**
	 * 下单时 冻结库存，减少可销售库存，增加冻结库存数量
	 * @param id
	 * 		分配库存记录
	 * @param inventory
	 * 		冻结库存数量
	 * @return 
	 * 		返回更新影响行数
	 */
	int forzenDistInventory(@Param("id")Long id, @Param("inventory")int inventory);
	/**
	 * 取消订单时，解冻库存，减少商品活动库存的冻结库存数量，增加可销售库存数量
	 * @param id
	 * 		活动库存记录id
	 * @param inventory
	 * 		大于0，增加可销售库存数量，减少占用库存数量
	 */
	void thawDistInventory(@Param("id")Long id, @Param("inventory")Integer inventory);
	/**
	 * 批量查询活动商品库存信息
	 * @param apps
	 * @param bizs
	 * @param skus
	 * @return
	 */
	List<InventoryDistribute> queryInventoryDistributes(@Param("queries") List<InventoryDistribute> queries);
	/**
	 * 减少冻结库存
	 * @param app
	 * @param bizId
	 * @param sku
	 * @param inventory
	 */
	Integer reduceOccupyDistributeById(@Param("id") Long distributeId, @Param("inventory")Integer inventory);
//	
//	/**
//	 * 增加分配库存数量
//	 * @param id
//	 * @param inventory
//	 */
//	void increaseInventoryDistributeById(@Param("id")Long id, @Param("inventory")int inventory);
//	/**
//	 * 更新backed状态
//	 * @param id
//	 * 		主键id
//	 * @param backed
//	 * 		是否已归还申请库存 0-否（未归还，即活动还在进行） 1-是(已归还，则该活动的订单在取消的时候，不再操作分配库存信息)
//	 */
//	void updateBackedStatus(@Param("id")Long id, @Param("backed")int backed);
//	/**
//	 * 减少分配库存，减少库存数量必须小于现有库存
//	 * 
//	 * @param id
//	 * @param invetory
//	 * 		减少库存数量
//	 */
//	void reduceInventoryDistributeById(@Param("id")Long id, @Param("inventory")int inventory);
//	/**
//	 * 下单时 冻结库存，减少可销售库存，增加冻结库存数量
//	 * @param id
//	 * 		分配库存记录
//	 * @param inventory
//	 * 		冻结库存数量
//	 * @return 
//	 * 		返回更新影响行数
//	 */
//	int forzenDistInventory(@Param("id")Long id, @Param("inventory")int inventory);
//	/**
//	 * 取消订单时，解冻库存，减少商品活动库存的冻结库存数量，增加可销售库存数量
//	 * @param id
//	 * 		活动库存记录id
//	 * @param inventory
//	 * 		大于0，增加可销售库存数量，减少占用库存数量
//	 */
//	void thawDistInventory(@Param("id")Long id, @Param("inventory")Integer inventory);
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
//	void reduceOccupyDistribute(@Param("app")String app, @Param("bizId")String bizId, 
//			@Param("sku")String sku, @Param("inventory")Integer inventory);
}
