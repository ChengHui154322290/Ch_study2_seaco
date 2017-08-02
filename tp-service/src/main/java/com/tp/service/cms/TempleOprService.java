package com.tp.service.cms;

import java.io.StringWriter;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tp.common.vo.cms.TempleConstant;
import com.tp.exception.CmsServiceException;
import com.tp.service.cms.ITempleOprService;

/**
 * 模板组装数据service
 */
@Service(value="templeOprService")
public class TempleOprService  implements ITempleOprService{

	/**
	 * 组装促销首页专题模板
	 * @param templateData 已经组装好的数据，是一个map集合
	 * @return 组装好的代码片段
	 * @throws CmsServiceException
	 * @author szy
	 */
	@Override
	public String indexLoading(Map<String, Object> templateData) throws CmsServiceException {
		String tempStr = "";
		tempStr = FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/index03.flt",new StringWriter());
		
		return tempStr;
	}
	
	/**
	 * 组装首页模板
	 * @param templateData  模板数据
	 * @param template  具体模板
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * @author szy
	 */
	@Override
	public String getAdDetailHtml(Map<String, Object> templateData, String template) throws CmsServiceException {
		String tempStr = "";
		tempStr = FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,template,new StringWriter());
		
		return tempStr;
	}
	
}
