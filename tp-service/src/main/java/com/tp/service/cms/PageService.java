package com.tp.service.cms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.dao.cms.PageDao;
import com.tp.dao.cms.TempleDao;
import com.tp.dto.cms.query.CmsPageQuery;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.Page;
import com.tp.service.BaseService;
import com.tp.service.cms.IPageService;

@Service(value="pageService")
public class PageService extends BaseService<Page>  implements IPageService{

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private PageDao pageDao;
	
	@Autowired
	private TempleDao templeDao;

	private List<Page> selectDynamicPageQuery(CmsPageQuery query) throws CmsServiceException {
		try {
			return pageDao.selectDynamicPageQuery(query);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}


	@Override
	public PageInfo<Page> queryPageList(CmsPageQuery query) throws CmsServiceException {
		if (query != null) {
			Long totalCount;
			List<Page> resultList;
			try {
				totalCount = pageDao.selectCountDynamic(query);
				resultList = pageDao.selectDynamicPageQuery(query);
				PageInfo<Page> page = new PageInfo<Page>();
				page.setPage(query.getStartPage());
				page.setSize(query.getPageSize());
				page.setRecords((int)Math.ceil(totalCount.intValue()/(float)query.getPageSize()));
				page.setRows(resultList);
				return page;
			} catch (Exception e) {
				logger.error("查询报错", e);
				e.printStackTrace();
			}
		}
		return new PageInfo<Page>();
	}

	@Override
	public int deletePageByIds(List<Long> ids) {
		int count = 0;
		try {
			
			//先判断页面下面是否有启用的模块，有的话是不能删除页面
			long fgcount = templeDao.selectIsExists(ids, true);
			if(fgcount > 0){
				return -1;
			}
			
			count = pageDao.deleteByIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("批量删除页面模板报错", e);
		}
		return count;
	}


	@Override
	public BaseDao<Page> getDao() {
		return pageDao;
	}

}
