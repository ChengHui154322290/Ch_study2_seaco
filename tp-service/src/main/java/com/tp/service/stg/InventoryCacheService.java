/**
 * 
 */
package com.tp.service.stg;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.StorageConstant.App;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.stg.IInventoryCacheService;

/**
 * @author szy
 *
 */
@Service
public class InventoryCacheService implements IInventoryCacheService {
	Logger logger = LoggerFactory.getLogger(InventoryCacheService.class);
	
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	public static final Integer EXPIRESECONDS=2;
	

	public Integer selectInevntoryFromCache(App app,String bizId,String sku){
		Integer inventory = null;
		if(null!=app&&StringUtils.isNotBlank(bizId)&&StringUtils.isNotBlank(sku)){
			String key =  buildCachekey(app, bizId, sku);
			try {
				inventory =(Integer) jedisCacheUtil.getCache(key);
			} catch (Exception e) {
				logger.error("获得库存缓存失败 key = {}  error = {}",key,e.getMessage());
			}
		}
		return inventory;
	}
	
	/**
	 * 清除库存缓存
	 * @param sku
	 * @param bizId
	 */
	public void removeInventoryCache(App app, String sku,String bizId){
		if(null!=app&&StringUtils.isNotBlank(sku)&&StringUtils.isNotBlank(bizId)){
			String key = buildCachekey(app, bizId, sku);
			try {
				//设置key两秒后过期
				jedisCacheUtil.setKeyExpire(key, EXPIRESECONDS);
			} catch (Exception e) {
				logger.error("清除库存缓存失败 key = {}  error = {}",key,e.getMessage());
			}
		}
	}
	
	/**
	 * 批量清除库存缓存
	 * @param skus
	 * @param bizId
	 */
	public void batchRemoveInventoryCache(List<String> skus,App app,String bizId){
		if(null!=app&&CollectionUtils.isNotEmpty(skus)&&StringUtils.isNotBlank(bizId)){
			for (String sku : skus) {
				removeInventoryCache(app,sku,bizId);
			}
		}
	}

	@Override
	public void setInventoryCache(App app, String bizId, String sku,
			Integer inventory) {
		if(null!=app&&StringUtils.isNotBlank(bizId)&&StringUtils.isNotBlank(sku)&&null!=inventory&&inventory.intValue()>=0){
			String key =  buildCachekey(app, bizId, sku);
			try {
				jedisCacheUtil.setCache(key, inventory);
			} catch (Exception e) {
				logger.error("设置库存缓存失败 key = {}  error = {}",key,e.getMessage());
			}
		}
	}
	/**
	 * 拼接库存缓存的key
	 * @param app
	 * @param bizId
	 * @param sku
	 * @return
	 */
	private String buildCachekey(App app,String bizId,String sku){
		String key = app.getName().concat(StorageConstant.straight).concat(bizId).concat(StorageConstant.straight).concat(sku);
		return key;
	}
}
