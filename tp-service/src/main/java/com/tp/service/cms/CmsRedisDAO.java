package com.tp.service.cms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tp.common.vo.cms.CmsRedisKeyConstant;
import com.tp.redis.util.JedisCacheUtil;

/**
 * @author szy
 *
 */
@Component(value = "cmsRedisDAO")
public class CmsRedisDAO {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private JedisCacheUtil jedisCacheUtil;

	public void releaseKeyList() throws Exception {
		if (jedisCacheUtil.keyExists(CmsRedisKeyConstant.CMS_ADVERT_CAROUSEL)) {
			jedisCacheUtil
					.deleteCacheKey(CmsRedisKeyConstant.CMS_ADVERT_CAROUSEL);
		}
		if (jedisCacheUtil
				.keyExists(CmsRedisKeyConstant.CMS_ADVERT_INDEX_PREFERENT)) {
			jedisCacheUtil
					.deleteCacheKey(CmsRedisKeyConstant.CMS_ADVERT_INDEX_PREFERENT);
		}
		if (jedisCacheUtil
				.keyExists(CmsRedisKeyConstant.CMS_ADVERT_LASTRUSHPRE)) {
			jedisCacheUtil
					.deleteCacheKey(CmsRedisKeyConstant.CMS_ADVERT_LASTRUSHPRE);
		}
		if (jedisCacheUtil.keyExists(CmsRedisKeyConstant.CMS_ADVERT_INDEX_POPLAYER)) {
			jedisCacheUtil
					.deleteCacheKey(CmsRedisKeyConstant.CMS_ADVERT_INDEX_POPLAYER);
		}
		if (jedisCacheUtil.keyExists(CmsRedisKeyConstant.CMS_ADVERT_USER_LOGINPIC)) {
			jedisCacheUtil
					.deleteCacheKey(CmsRedisKeyConstant.CMS_ADVERT_USER_LOGINPIC);
		}
		if (jedisCacheUtil.keyExists(CmsRedisKeyConstant.CMS_INDEX_ANNOUNCE)) {
			jedisCacheUtil
					.deleteCacheKey(CmsRedisKeyConstant.CMS_INDEX_ANNOUNCE);
		}
	}
	
	public void insertObject(String key,Object value) throws Exception {
		jedisCacheUtil.setCache(key,
				value, CmsRedisKeyConstant.DAYS_EXPIRE);
	}
	
	public Object getValueObject(String key) throws Exception {
		return jedisCacheUtil.getCache(key);
	}
	
	public void insertString(String key,String value) throws Exception {
		jedisCacheUtil.setCacheString(key,
				value, CmsRedisKeyConstant.DAYS_EXPIRE);
	}
	
	public String getValueString(String key) throws Exception {
		return jedisCacheUtil.getCacheString(key);
	}

}
