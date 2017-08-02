package com.tp.service.cms.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.cms.temple.Topic;
import com.tp.model.cms.RedisIndexRule;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.cms.CmsIndexRedisPromotionService;
import com.tp.service.cms.CmsRedisDAO;

/**
 * @author szy
 *
 */
@Service(value = "cmsIndexRedisPromotionService")
public class CmsIndexRedisPromotionServiceImpl implements CmsIndexRedisPromotionService{

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Autowired
	CmsRedisDAO cmsRedisDAO;
	
	/*private static final String INDEX_REDIS_COLLECT = "runCmsIndexRedis";*/

	
	/**
	 * a.{"CMS_ALL_NEW","下一级的所有key的数组['JR_PC','JR_APP']"};  
	 * 	{"CMS_ALL_OLDER","下一级的所有key的数组['JR_PC','JR_APP']"},作用是更新前需要清除下缓存。
	 * b.{"JR_PC","具体活动的key的数组"},{"JR_APP","具体活动的key的数组"} ; 
	 * c.迭代生成后{"JR_PC_ALL","下一级的所有key的数组['JR_PC_SH','JR_PC_BJ']"},
	 * 		{"JR_APP_ALL","具体活动的key的数组['JR_PC_SH','JR_PC_BJ']"}
	 * d.修改的时候先清缓存中"JR_PC_ALL"下面的key的缓存，然后重新根据规则表迭代生成JR_PC_SH，JR_PC_BJ，
	 * 		然后再生成一个JR_PC_ALL。
	 * e.然后迭代找出JR_PC_ALL下面的所有key，并对每个key进行分页迭代排序，存放为：{"JR_PC_SH_1","value"},
	 * 		{"JR_PC_SH_2","value"},分页是找到JR_PC，在表中查询，找到分页数，并按此分页排序。
	 *      {"JR_PC_SH_ALL",["JR_PC_SH_1","JR_PC_SH_2"]}这个是供清除用
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void updateALLIndexRedis() {
		Object firstObj_older = jedisCacheUtil.getCache("CMS_ALL_OLDER");
		if(firstObj_older != null){
			logger.error("查询缓存的ALL_OLDER的key值不为空,则需要删除老的缓存值");
			//先取出CMS_ALL_OLDER下面的所有key值的数组
			ArrayList<String> firstArg = (ArrayList<String>)firstObj_older;
			for(String firstStr : firstArg){
				String first_key = firstStr+"_ALL";//如JR_PC_ALL
				
				Object secondObj = jedisCacheUtil.getCache(first_key);
				ArrayList<String> secondArg = (ArrayList<String>)secondObj;
				for(String secondStr : secondArg){
					
					//如JR_PC_SH_ALL，里面包含分页信息
					String second_key = secondStr+"_ALL";
					
					Object thirdObj = jedisCacheUtil.getCache(second_key);
					ArrayList<String> thirdArg = (ArrayList<String>)thirdObj;
					for(String thirdStr : thirdArg){
						
						//如JR_PC_SH_1，JR_PC_SH_2（删除分页信息）
						jedisCacheUtil.deleteCacheKey(thirdStr);
					}
					//如JR_PC_SH，这个是不存在的，是中间的一个过渡
					//jedisCacheUtil.deleteCacheKey(secondArg[si]);
					
					//如JR_PC_SH_ALL，JR_PC_SZ_ALL（删除all项）
					jedisCacheUtil.deleteCacheKey(second_key);
				}
				jedisCacheUtil.deleteCacheKey(first_key);//如JR_PC_ALL，JR_APP_ALL（删除all项）
			}
		}
		
		/**
		 * 更新缓存
		 */
		Object firstObj = jedisCacheUtil.getCache("CMS_ALL_NEW");
		ArrayList<String> firstList = (ArrayList<String>)firstObj;
		for(String firstStr : firstList){
			ArrayList<String> firstKeyList = (ArrayList<String>)jedisCacheUtil.getCache(firstStr);
			
			//把firstStr当做参数，在规则表中进行查询，查找到规则表中以firstStr为一级别所包含的所有二级别
			List<RedisIndexRule> secondKeyALLList = new ArrayList<RedisIndexRule>();//Do.select(firstStr) ;
			
			/**
			 * 遍历firstKeyList里面的key，再通过key去找值，依据secondKeyList里面的规则一次存放到新的map中，
			 * map的key值就是JR_PC_SH，JR_PC_SZ这种。
			 */
			Map secondMap = new HashMap();
			if(CollectionUtils.isNotEmpty(secondKeyALLList)){
				for(RedisIndexRule cmsRedisIndexRuleDO:secondKeyALLList){
					String secondKey = cmsRedisIndexRuleDO.getSecondKey()+"_ALL";
					ArrayList<String> secondKeyList = new ArrayList<String>();
					
					//比较，进行归类
					for(String str:firstKeyList){
						Topic strObj = (Topic)jedisCacheUtil.getCache(str);
						
						/**此处需要strObj那边加二级菜单，方便规则表的二级菜单与促销存放的数据是否匹配一致，如果一致则把key值放入list中**/
						/*if(strObj.getName()){
							secondKeyList.add(strObj.getName());
						}*/
						
					}
					
					secondMap.put(secondKey, secondKeyList);
					jedisCacheUtil.setCache(secondKey, secondKeyList);
					
					/**把secondKeyList分页存放到map中**/
					//页数
					int pageSize = cmsRedisIndexRuleDO.getPage();
					
					/*jedisCacheUtil.setCache(cmsRedisIndexRuleDO.getSecondKey()+"_1", secondKeyList{"取前pageSize条"});
					jedisCacheUtil.setCache(cmsRedisIndexRuleDO.getSecondKey()+"_2", 
							secondKeyList{"取pageSize+1开始，后面取前pageSize条"});*/
					
				}
			}
		}
		
	}

	@Override
	public void updateSingleIndexRedis() {
		// TODO Auto-generated method stub
		
	}
	
	

	

}
