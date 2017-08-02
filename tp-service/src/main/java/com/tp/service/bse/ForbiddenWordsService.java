package com.tp.service.bse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.dao.bse.ForbiddenWordsDao;
import com.tp.model.bse.ForbiddenWords;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.BaseService;
import com.tp.service.bse.IForbiddenWordsService;

@Service
public class ForbiddenWordsService extends BaseService<ForbiddenWords> implements IForbiddenWordsService {

	@Autowired
	private ForbiddenWordsDao forbiddenWordsDao;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Override
	public BaseDao<ForbiddenWords> getDao() {
		return forbiddenWordsDao;
	}
	@Override
	public String checkForbiddenWordsField(String sourceField) {
		if(StringUtils.isBlank(sourceField)){
			return "";
		}
		ForbiddenWords queryForbiddenWords  = new ForbiddenWords();
		List<ForbiddenWords> fobiddenWords=super.queryByObject(queryForbiddenWords);
		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		sb.append("中有");
		if(CollectionUtils.isNotEmpty(fobiddenWords)){
			for(ForbiddenWords forbiddenWordsDO :fobiddenWords ){
				String forbiddenWords = forbiddenWordsDO.getWords();
				if(StringUtils.isNotBlank(forbiddenWords)){
					int total = checkStrCount(sourceField,forbiddenWords);
					if(total>0){
						flag = true;
						sb.append(": 违禁词[").append(forbiddenWords)
						  .append("],总共出现").append(total).append("次。");
					}
				}
			}
		}
		if(flag){
			return sb.toString();
		}else{
			return "";
		}
	}
	
	/**
	 * 
	 * <pre>
	 *  判断一个字符串出现另一个字符串的次数
	 * </pre>
	 *
	 * @param str
	 * @param checkStr
	 * @return
	 */
	private static  int checkStrCount(String str, String checkStr){
		int total = 0;
		for (String tmp = str; 	tmp!= null &&tmp.length()>=checkStr.length();){
		  if(tmp.indexOf(checkStr) == 0){
		    total ++;
		  }
		  tmp = tmp.substring(1);
		}
		return total;
	}
	@Override
	public List<ForbiddenWords> getAllEffectiveForbiddenWords() {
		@SuppressWarnings("unchecked")
		List<ForbiddenWords> fobiddenWords= (List<ForbiddenWords>) jedisCacheUtil.getCache("ALL_EFFECTIVE_FORBIDDEN_WORDS");
		if(CollectionUtils.isNotEmpty(fobiddenWords)){
			return fobiddenWords;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", Constant.ENABLED.YES);
		List<ForbiddenWords> list = forbiddenWordsDao.queryByParam(params);
		jedisCacheUtil.setCache("ALL_EFFECTIVE_FORBIDDEN_WORDS", list, Integer.MAX_VALUE);
		return list;
	}
}
