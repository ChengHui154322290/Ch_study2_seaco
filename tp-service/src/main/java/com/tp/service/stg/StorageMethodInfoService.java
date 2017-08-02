/**
 * 
 */
package com.tp.service.stg;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.stg.IStorageMethodInfoService;
import com.tp.service.stg.mtdinvoke.MethodInvokeReportService;

/**
 * @author szy
 *
 */
@Service
public class StorageMethodInfoService implements IStorageMethodInfoService {

	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Override
	public Map<String, Integer> queryMethodInfo() {
		String projectPrefixKey = "storage";
		jedisCacheUtil.setProjectPrefixKey(projectPrefixKey);
		@SuppressWarnings("unchecked")
		Map<String, Integer> map =(Map<String, Integer>) jedisCacheUtil.getCache(MethodInvokeReportService.STORAGEINVOKEKEY);
		return map;
	}

}
