package com.tp.service.cms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.dao.cms.WrittenElementDao;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.WrittenElement;
import com.tp.service.BaseService;
import com.tp.service.cms.IWrittenElementService;

@Service(value="writtenElementService")
public class WrittenElementService extends BaseService<WrittenElement> implements IWrittenElementService{

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private WrittenElementDao writtenElementDao;


	private List<WrittenElement> selectDynamicPageQuery(WrittenElement cmsWrittenElementDO) throws CmsServiceException {
		try {
			PageInfo<WrittenElement> pageInfo = new PageInfo<>(cmsWrittenElementDO.getStartPage(), cmsWrittenElementDO.getPageSize());
			return queryPageByObject(cmsWrittenElementDO, pageInfo).getRows();
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}

	public PageInfo<WrittenElement> queryPageListByWrittenElement(WrittenElement cmsWrittenElementDO) {
		if (cmsWrittenElementDO != null) {
			Integer totalCount = this.queryByObjectCount(cmsWrittenElementDO);
			List<WrittenElement> resultList = this.selectDynamicPageQuery(cmsWrittenElementDO);

			PageInfo<WrittenElement> page = new PageInfo<WrittenElement>();
			page.setPage(cmsWrittenElementDO.getStartPage());
			page.setSize(cmsWrittenElementDO.getPageSize());
			page.setRecords(totalCount.intValue());
			page.setRows(resultList);
			return page;
		}
		return new PageInfo<WrittenElement>();
	}

	public PageInfo<WrittenElement> queryPageListByWrittenElementAndStartPageSize(WrittenElement cmsWrittenElementDO,int startPage,int pageSize){
		if (cmsWrittenElementDO != null && startPage>0 && pageSize>0) {
			cmsWrittenElementDO.setStartPage(startPage);
			cmsWrittenElementDO.setPageSize(pageSize);
			return this.queryPageListByWrittenElement(cmsWrittenElementDO);
		}
		return new PageInfo<WrittenElement>();
	}

	@Override
	public int deleteByIds(List<Long> ids) throws Exception {
		int count = 0;
		try {
			count = writtenElementDao.deleteByIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("批量删除报错", e);
		}
		return count;
	}

	@Override
	public BaseDao<WrittenElement> getDao() {
		return writtenElementDao;
	}
	
}
