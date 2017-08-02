package com.tp.service.cms;

import java.util.List;

import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.query.CmsPageQuery;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.Page;
import com.tp.service.IBaseService;
 /**
 * 页面管理 Service
 * @author szy
 */
public interface IPageService extends IBaseService<Page>{

	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	int deletePageByIds(List<Long> ids) throws Exception;

	PageInfo<Page> queryPageList(CmsPageQuery query) throws CmsServiceException;

}
