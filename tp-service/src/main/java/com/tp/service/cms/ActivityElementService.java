package com.tp.service.cms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.dao.cms.ActivityElementDao;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.ActivityElement;
import com.tp.service.BaseService;
import com.tp.service.cms.IActivityElementService;

@Service(value="activityElementService")
public class ActivityElementService extends BaseService<ActivityElement> implements IActivityElementService{

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private ActivityElementDao activityElementDao;

	public ActivityElement insert(ActivityElement cmsActivityElementDO) throws CmsServiceException {
		try {
			
			Long ct = activityElementDao.selectIsExistid(cmsActivityElementDO);
			if(ct <= 0){
				activityElementDao.insert(cmsActivityElementDO);
			}
			
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
		return cmsActivityElementDO;
	}


	@Override
	public int update(ActivityElement cmsActivityElementDO,boolean isAllField) throws CmsServiceException {
		try {
			if(isAllField){
				return (Integer) activityElementDao.update(cmsActivityElementDO);
			}else{
				return (Integer) activityElementDao.updateDynamic(cmsActivityElementDO);
			}
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}

	@Override
	public int deleteById(Long id) throws CmsServiceException {
		try {
			return (Integer) activityElementDao.deleteById(id);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}

//	@Override
//	public int updateDynamic(ActivityElement cmsActivityElementDO) throws CmsServiceException {
//		try {
//			return (Integer) activityElementDao.updateDynamic(cmsActivityElementDO);
//		}catch(DAOException e){
//			logger.error(e);
//            throw new CmsServiceException(e);
//		}
//	}

	@Override
	public ActivityElement selectById(Long id) throws CmsServiceException {
		try {
			return activityElementDao.selectById(id,true);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}

	@Override
	public Long selectCountDynamic(ActivityElement cmsActivityElementDO) throws CmsServiceException {
		try {
			return Long.valueOf(activityElementDao.queryByObjectCount(cmsActivityElementDO));
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}

	@Override
	public List<ActivityElement> selectDynamic(ActivityElement cmsActivityElementDO) throws CmsServiceException {
		try {
			return activityElementDao.queryByObject(cmsActivityElementDO);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}
	

	private List<ActivityElement> selectDynamicPageQuery(ActivityElement cmsActivityElementDO) throws CmsServiceException {
		try {
			return activityElementDao.selectDynamicPageQuery(cmsActivityElementDO);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}

	public PageInfo<ActivityElement> queryPageListByActivityElement(ActivityElement cmsActivityElementDO) {
		if (cmsActivityElementDO != null) {
			Long totalCount = Long.valueOf(queryByObjectCount(cmsActivityElementDO));
			List<ActivityElement> resultList = this.selectDynamicPageQuery(cmsActivityElementDO);

			PageInfo<ActivityElement> page = new PageInfo<ActivityElement>();
			page.setPage(cmsActivityElementDO.getStartPage());
			page.setSize(cmsActivityElementDO.getPageSize());
			page.setRecords(totalCount.intValue());
			page.setRows(resultList);
			return page;
		}
		return new PageInfo<ActivityElement>();
	}

	public PageInfo<ActivityElement> queryPageListByActivityElementAndStartPageSize(ActivityElement cmsActivityElementDO,int startPage,int pageSize){
		if (cmsActivityElementDO != null && startPage>0 && pageSize>0) {
			cmsActivityElementDO.setStartPage(startPage);
			cmsActivityElementDO.setPageSize(pageSize);
			return this.queryPageListByActivityElement(cmsActivityElementDO);
		}
		return new PageInfo<ActivityElement>();
	}

	@Override
	public int deleteByIds(List<Long> ids) throws Exception {
		int count = 0;
		try {
			count = activityElementDao.deleteByIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("批量删除报错", e);
		}
		return count;
	}


	public PageInfo<ActivityElement> queryPageListByCmsActivityElementDO(ActivityElement cmsActivityElementDO) {
		if (cmsActivityElementDO != null) {
			Long totalCount = this.selectCountDynamic(cmsActivityElementDO);
			List<ActivityElement> resultList = this.selectDynamicPageQuery(cmsActivityElementDO);

			PageInfo<ActivityElement> page = new PageInfo<ActivityElement>();
			page.setPage(cmsActivityElementDO.getStartPage());
			page.setSize(cmsActivityElementDO.getPageSize());
			page.setRecords(totalCount.intValue());
			page.setRows(resultList);
			return page;
		}
		return new PageInfo<ActivityElement>();
	}

	public PageInfo<ActivityElement> queryPageListByCmsActivityElementDOAndStartPageSize(ActivityElement cmsActivityElementDO,int startPage,int pageSize){
		if (cmsActivityElementDO != null && startPage>0 && pageSize>0) {
			cmsActivityElementDO.setStartPage(startPage);
			cmsActivityElementDO.setPageSize(pageSize);
			return this.queryPageListByCmsActivityElementDO(cmsActivityElementDO);
		}
		return new PageInfo<ActivityElement>();
	}


	@Override
	public BaseDao<ActivityElement> getDao() {
		return activityElementDao;
	}
}
