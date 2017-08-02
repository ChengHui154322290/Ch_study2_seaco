package com.tp.service.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.dao.cms.IndexFeedbackDao;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.IndexFeedback;
import com.tp.model.mem.MemberInfo;
import com.tp.service.BaseService;
import com.tp.service.cms.IIndexFeedbackService;
import com.tp.service.mem.IMemberInfoService;

@Service(value="indexFeedbackService")
public class IndexFeedbackService extends BaseService<IndexFeedback> implements IIndexFeedbackService{

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IndexFeedbackDao indexFeedbackDao;
	
	@Autowired
	IMemberInfoService memberInfoService;

	@Override
	public IndexFeedback insert(IndexFeedback cmsIndexFeedbackDO) throws CmsServiceException {
		try {
			
			if(cmsIndexFeedbackDO.getUserId() != null 
					&& cmsIndexFeedbackDO.getUserName() == null){
				MemberInfo user = memberInfoService.queryById(cmsIndexFeedbackDO.getUserId());
				cmsIndexFeedbackDO.setUserName(user.getNickName());
				if(cmsIndexFeedbackDO.getMobile() == null ||
						cmsIndexFeedbackDO.getMobile() == ""){
					cmsIndexFeedbackDO.setMobile(user.getMobile());
				}
				if(cmsIndexFeedbackDO.getEmail() != null ||
						cmsIndexFeedbackDO.getEmail() != ""){
					cmsIndexFeedbackDO.setEmail(user.getEmail());
				}
			}
			
			indexFeedbackDao.insert(cmsIndexFeedbackDO);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
		return cmsIndexFeedbackDO;
	}

	@Override
	public IndexFeedback selectById(Long id) throws CmsServiceException {
		try {
			return indexFeedbackDao.selectById(id,true);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}

	@Override
	public Long selectCountDynamic(IndexFeedback cmsIndexFeedbackDO) throws CmsServiceException {
		try {
			return indexFeedbackDao.selectCountDynamic(cmsIndexFeedbackDO);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}

	@Override
	public List<IndexFeedback> selectDynamic(IndexFeedback cmsIndexFeedbackDO) throws CmsServiceException {
		try {
			return indexFeedbackDao.selectDynamic(cmsIndexFeedbackDO);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}
	

	private List<IndexFeedback> selectDynamicPageQuery(IndexFeedback cmsIndexFeedbackDO) throws CmsServiceException {
		try {
			return indexFeedbackDao.selectDynamicPageQuery(cmsIndexFeedbackDO);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}

	public PageInfo<IndexFeedback> queryPageListByIndexFeedback(IndexFeedback cmsIndexFeedbackDO) {
		if (cmsIndexFeedbackDO != null) {
			Long totalCount = this.selectCountDynamic(cmsIndexFeedbackDO);
			List<IndexFeedback> resultList = this.selectDynamicPageQuery(cmsIndexFeedbackDO);

			PageInfo<IndexFeedback> page = new PageInfo<IndexFeedback>();
			page.setPage(cmsIndexFeedbackDO.getStartPage());
			page.setSize(cmsIndexFeedbackDO.getPageSize());
			page.setRecords(totalCount.intValue());
			page.setRows(resultList);
			return page;
		}
		return new PageInfo<IndexFeedback>();
	}

	public PageInfo<IndexFeedback> queryPageListByIndexFeedbackAndStartPageSize(IndexFeedback cmsIndexFeedbackDO,int startPage,int pageSize){
		if (cmsIndexFeedbackDO != null && startPage>0 && pageSize>0) {
			cmsIndexFeedbackDO.setStartPage(startPage);
			cmsIndexFeedbackDO.setPageSize(pageSize);
			return this.queryPageListByIndexFeedback(cmsIndexFeedbackDO);
		}
		return new PageInfo<IndexFeedback>();
	}

	@Override
	public Map<String, Object> selectFeedbackPageQuery(
			Map<String, Object> paramMap, IndexFeedback cmsIndexFeedbackDO)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("counts", indexFeedbackDao.selectCountDynamic(cmsIndexFeedbackDO));
		map.put("list", indexFeedbackDao.selectFeedbackPageQuery(paramMap));
		return map;
	}

	@Override
	public int deleteByIds(List<Long> ids) throws CmsServiceException {
		try {
			return indexFeedbackDao.deleteByIds(ids);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}



	@Override
	public BaseDao<IndexFeedback> getDao() {
		return indexFeedbackDao;
	}

}
