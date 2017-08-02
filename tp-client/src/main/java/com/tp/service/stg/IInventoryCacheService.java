/**
 * 
 */
package com.tp.service.stg;

import java.util.List;

import com.tp.common.vo.StorageConstant.App;

/**
 * @author szy
 * 库存缓存接口
 */
public interface IInventoryCacheService {
	/**
	 * 设置库存的缓存
	 * @param app
	 * @param bizId
	 * @param sku
	 * @param inventory
	 * 		当前查询到的库存
	 */
	public void setInventoryCache(App app,String bizId,String sku,Integer inventory);

	/**
	 * 从缓存中获得缓存的库存
	 * @param app
	 * @param bizId
	 * @param sku
	 * @return
	 */
	public Integer selectInevntoryFromCache(App app,String bizId,String sku);
	/**
	 * 清除库存缓存
	 * @param app
	 * @param sku
	 * @param bizId
	 */
	public void removeInventoryCache(App app, String sku,String bizId);
	/**
	 * 批量清除库存缓存
	 * @param skus
	 * @param app
	 * @param bizId
	 */
	public void batchRemoveInventoryCache(List<String> skus,App app,String bizId);
}
