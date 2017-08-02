package com.tp.service.cms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.dao.cms.AnnounceElementDao;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.AnnounceElement;
import com.tp.service.BaseService;
import com.tp.service.cms.IAnnounceElementService;

@Service(value="cmsAnnounceElementService")
public class AnnounceElementService extends BaseService<AnnounceElement> implements IAnnounceElementService{

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private AnnounceElementDao announceElementDao;



	@Override
	public AnnounceElement selectById(Long id) throws CmsServiceException {
		try {
			return announceElementDao.selectById(id,true);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}

	@Override
	public Long selectCountDynamic(AnnounceElement cmsAnnounceElementDO) throws CmsServiceException {
		try {
			return announceElementDao.selectCountDynamic(cmsAnnounceElementDO);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}

	@Override
	public List<AnnounceElement> selectDynamic(AnnounceElement cmsAnnounceElementDO) throws CmsServiceException {
		try {
			return announceElementDao.selectDynamic(cmsAnnounceElementDO);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}
	

	private List<AnnounceElement> selectDynamicPageQuery(AnnounceElement cmsAnnounceElementDO) throws CmsServiceException {
		try {
			return announceElementDao.selectDynamicPageQuery(cmsAnnounceElementDO);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}
	
	public PageInfo<AnnounceElement> queryPageListByAnnounceElement(AnnounceElement cmsAnnounceElementDO) {
		if (cmsAnnounceElementDO != null) {
			Long totalCount = this.selectCountDynamic(cmsAnnounceElementDO);
			List<AnnounceElement> resultList = this.selectDynamicPageQuery(cmsAnnounceElementDO);

			PageInfo<AnnounceElement> page = new PageInfo<AnnounceElement>();
			page.setPage(cmsAnnounceElementDO.getStartPage());
			page.setSize(cmsAnnounceElementDO.getPageSize());
			page.setRecords(totalCount.intValue());
			page.setRows(resultList);
			return page;
		}
		return new PageInfo<AnnounceElement>();
	}
	@Override
	public PageInfo<AnnounceElement> queryPageListByAnnounceElementAndStartPageSize(AnnounceElement cmsAnnounceElementDO,int startPage,int pageSize){
		if (cmsAnnounceElementDO != null && startPage>0 && pageSize>0) {
			cmsAnnounceElementDO.setStartPage(startPage);
			cmsAnnounceElementDO.setPageSize(pageSize);
			return this.queryPageListByAnnounceElement(cmsAnnounceElementDO);
		}
		return new PageInfo<AnnounceElement>();
	}

	@Override
	public int deleteByIds(List<Long> ids) throws Exception {
		int count = 0;
		try {
			count = announceElementDao.deleteByIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("批量删除报错", e);
		}
		return count;
	}

	@Override
	public BaseDao<AnnounceElement> getDao() {
		return announceElementDao;
	}
}
