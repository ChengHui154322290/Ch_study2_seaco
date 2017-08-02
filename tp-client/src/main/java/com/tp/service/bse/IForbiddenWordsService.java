package com.tp.service.bse;

import java.util.List;

import com.tp.model.bse.ForbiddenWords;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 违禁词接口
  */
public interface IForbiddenWordsService extends IBaseService<ForbiddenWords>{
	/**
	 * 
	 * <pre>
	 *  分析sourceField中存在的违禁词,返回分析结果,没有违禁词返回空字符串
	 * </pre>
	 *
	 * @param sourceField
	 * @return 如果返回空字符串("")则表示不存在违禁词,如果存在则返回相应的信息
	 */
	
	String  checkForbiddenWordsField (String sourceField );

	List<ForbiddenWords> getAllEffectiveForbiddenWords();
}
