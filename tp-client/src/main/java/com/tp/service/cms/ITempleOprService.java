package com.tp.service.cms;

import java.util.Map;

import com.tp.exception.CmsServiceException;
 /**
 * 模板管理 Service
 */
public interface ITempleOprService {

	/**
	 * 主页模板管理
	 * @param str
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 */
	String indexLoading(Map<String, Object> templateData) throws CmsServiceException;
	
	/**
	 * 主页模板管理
	 * @param templateData  模板数据
	 * @param template  具体模板
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 */
	String getAdDetailHtml(Map<String, Object> templateData, String template) throws CmsServiceException;

}
