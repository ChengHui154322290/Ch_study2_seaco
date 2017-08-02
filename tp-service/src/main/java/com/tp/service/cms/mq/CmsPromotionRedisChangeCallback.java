/**
 * 
 */
package com.tp.service.cms.mq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.mq.MqMessageCallBack;
import com.tp.service.cms.CmsRedisDAO;

/**
 * @author szy
 *
 */
@Service("cmsPromotionRedisChangeCallback")
public class CmsPromotionRedisChangeCallback implements MqMessageCallBack {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	CmsRedisDAO cmsRedisDAO;
	
	/*@Autowired
	CmsIndexRedisPromotionService cmsIndexRedisService;*/
	
	@Override
	public boolean execute(Object o) {
		logger.info("start mq excute.......");
		String keyStr = (String) o;
		logger.info("cms更新首页缓存数据，更新的key值为:" + keyStr);
		
		if("CMS_ALL".equals(keyStr)){
			//全量更新
//			cmsIndexRedisService.updateALLIndexRedis();
		}else{
			//单量更新
//			cmsIndexRedisService.updateSingleIndexRedis();
		}
		
		return true;
	}

}
