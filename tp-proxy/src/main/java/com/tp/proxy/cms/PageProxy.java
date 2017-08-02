package com.tp.proxy.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.query.CmsPageQuery;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.Page;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cms.IPageService;
/**
 * 页面管理表代理层
 * @author szy
 *
 */
@Service
public class PageProxy extends BaseProxy<Page>{

	@Autowired
	private IPageService pageService;

	@Override
	public IBaseService<Page> getService() {
		return pageService;
	}
	
	/**
	 * 模板管理列表的查询
	 * @param jSONObject  即前台传值的查询条件
	 * @return
	 * @throws Exception
	 */
	public PageInfo<Page> getPageList(CmsPageQuery query) throws CmsServiceException{
		if (null == query) {
			return null;
		}
		return pageService.queryPageList(query);
	}
	
	public Long addSubmit(Page cmsPageDO) throws CmsServiceException{
		cmsPageDO = pageService.insert(cmsPageDO);
		if(cmsPageDO.getId() == null)
			return 0L;
		return cmsPageDO.getId();
	}
	
	public Integer updateSubmit(Page cmsPageDO) throws CmsServiceException{
		return pageService.updateById(cmsPageDO);
	}
	
	public Integer delPageByIds(List<Long> ids) throws Exception{
		return pageService.deletePageByIds(ids);
	}
	
	public Page getPageById(Long id) throws Exception{
		return pageService.queryById(id);
	}
}
