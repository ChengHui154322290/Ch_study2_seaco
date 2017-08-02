package com.tp.service.cms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.dao.cms.SeoElementDao;
import com.tp.model.cms.SeoElement;
import com.tp.service.BaseService;
import com.tp.service.cms.ISeoElementService;

@Service(value="seoElementService")
public class SeoElementService extends BaseService<SeoElement> implements ISeoElementService{

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private SeoElementDao seoElementDao;

//	private List<SeoElement> selectDynamicPageQuery(SeoElement cmsSeoElementDO) throws CmsServiceException {
//		try {
//			PageInfo<SeoElement> pageInfo = new PageInfo<>(cmsSeoElementDO.getStartPage(), cmsSeoElementDO.getPageSize());
//			return queryPageByObject(cmsSeoElementDO, pageInfo).getRows();
//		}catch(Exception e){
//			logger.error(e);
//            throw new CmsServiceException(e);
//		}
//	}

	@Override
	public int deleteByIds(List<Long> ids) {
		int count = 0;
		try {
			count = seoElementDao.deleteByIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("批量删除报错", e);
		}
		return count;
	}

	@Override
	public PageInfo<SeoElement> queryPageListByCmsSeoElement(SeoElement cmsSeoElementDO) {
		if (cmsSeoElementDO != null) {
			PageInfo<SeoElement> pageInfo = new PageInfo<>(cmsSeoElementDO.getStartPage(), cmsSeoElementDO.getPageSize());
			pageInfo = queryPageByObject(cmsSeoElementDO, pageInfo);
			return pageInfo;
		}
		return new PageInfo<SeoElement>();
	}

	@Override
	public PageInfo<SeoElement> queryPageListByCmsSeoElementAndStartPageSize(SeoElement cmsSeoElementDO, int startPage, int pageSize) {
		if (cmsSeoElementDO != null && startPage>0 && pageSize>0) {
			cmsSeoElementDO.setStartPage(startPage);
			cmsSeoElementDO.setPageSize(pageSize);
			return this.queryPageListByCmsSeoElement(cmsSeoElementDO);
		}
		return new PageInfo<SeoElement>();
	}

	@Override
	public BaseDao<SeoElement> getDao() {
		return seoElementDao;
	}

	@Override
	public List<SeoElement> getDefinedElement(Long positionId) {
		SeoElement seoDO = new SeoElement();
		seoDO.setPositionId(positionId);
		return queryByObject(seoDO);
	}

}
