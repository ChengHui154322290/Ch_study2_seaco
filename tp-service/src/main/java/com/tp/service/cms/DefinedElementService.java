package com.tp.service.cms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.dao.cms.DefinedElementDao;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.DefinedElement;
import com.tp.service.BaseService;
import com.tp.service.cms.IDefinedElementService;

@Service(value="definedElementService")
public class DefinedElementService extends BaseService<DefinedElement> implements IDefinedElementService{

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private DefinedElementDao definedElementDao;



	@Override
	public DefinedElement selectById(Long id) throws CmsServiceException {
		try {
			return definedElementDao.selectById(id,true);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}

	@Override
	public Long selectCountDynamic(DefinedElement cmsDefinedElementDO) throws CmsServiceException {
		try {
			return definedElementDao.selectCountDynamic(cmsDefinedElementDO);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}

	@Override
	public List<DefinedElement> selectDynamic(DefinedElement cmsDefinedElementDO) throws CmsServiceException {
		try {
			return definedElementDao.selectDynamic(cmsDefinedElementDO);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}
	

	private List<DefinedElement> selectDynamicPageQuery(DefinedElement cmsDefinedElementDO) throws CmsServiceException {
		try {
			return definedElementDao.selectDynamicPageQuery(cmsDefinedElementDO);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}

	public PageInfo<DefinedElement> queryPageListByDefinedElement(DefinedElement cmsDefinedElementDO) {
		if (cmsDefinedElementDO != null) {
			Long totalCount = this.selectCountDynamic(cmsDefinedElementDO);
			List<DefinedElement> resultList = this.selectDynamicPageQuery(cmsDefinedElementDO);
			
			/*for(int i=0,j=resultList.size();i<j;i++){
				String countStr = resultList.get(i).getContent();
				if(countStr != null && countStr.length()>50){
	    			StringBuffer sb = new StringBuffer();
	    			sb.append(countStr.substring(0, 45));
	    			sb.append("......");
	    			resultList.get(i).setContent(sb.toString());
				}
			}*/
			
			PageInfo<DefinedElement> page = new PageInfo<DefinedElement>();
			page.setPage(cmsDefinedElementDO.getStartPage());
			page.setSize(cmsDefinedElementDO.getPageSize());
			page.setRecords(totalCount.intValue());
			page.setRows(resultList);
			return page;
		}
		return new PageInfo<DefinedElement>();
	}

	public PageInfo<DefinedElement> queryPageListByDefinedElementAndStartPageSize(DefinedElement cmsDefinedElementDO,int startPage,int pageSize){
		if (cmsDefinedElementDO != null && startPage>0 && pageSize>0) {
			cmsDefinedElementDO.setStartPage(startPage);
			cmsDefinedElementDO.setPageSize(pageSize);
			return this.queryPageListByDefinedElement(cmsDefinedElementDO);
		}
		return new PageInfo<DefinedElement>();
	}

	@Override
	public int deleteByIds(List<Long> ids) throws Exception {
		int count = 0;
		try {
			count = definedElementDao.deleteByIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("批量删除报错", e);
		}
		return count;
	}

	@Override
	public BaseDao<DefinedElement> getDao() {
		return definedElementDao;
	}
	
}
