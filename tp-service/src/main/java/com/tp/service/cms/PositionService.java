package com.tp.service.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.cms.PositionDao;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.Position;
import com.tp.model.cms.Temple;
import com.tp.service.BaseService;
import com.tp.service.cms.IPositionService;
import com.tp.util.StringUtil;

@Service(value="cmsPositionService")
public class PositionService extends BaseService<Position> implements IPositionService{

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private PositionDao positionDao;

	@Override
	public Position insert(Position cmsPositionDO) throws CmsServiceException {
		try {
			Temple cmsTempleDO = new Temple();
			cmsTempleDO.setPageId(cmsPositionDO.getTempleId());//设置模板id
			List<Temple> lst = positionDao.selectTempletByCounts(cmsTempleDO);
			positionDao.insert(cmsPositionDO);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
		return cmsPositionDO;
	}


	@Override
	public int update(Position cmsPositionDO) throws CmsServiceException {
		try {
			if(0 == cmsPositionDO.getStatus()){
				Temple cmsTempleDO = new Temple();
				cmsTempleDO.setPageId(cmsPositionDO.getTempleId());//设置模板id
				cmsTempleDO.setId(cmsPositionDO.getId());//设置位置id
				List<Temple> lst = positionDao.selectTempletByCounts(cmsTempleDO);
				if(lst != null && lst.size()>0
						&& lst.get(0).getElementNum() < 1){//判断限制个数是否大于等于已存在的个数
					return -1;
				}
			}
			return (Integer) positionDao.updateById(cmsPositionDO);
		}catch(Exception e){
			logger.error(e);
            throw new CmsServiceException(e);
		}
	}

	@Override
	public int deleteByIds(List<Long> ids) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(ids, Constant.SPLIT_SIGN.COMMA)+")");
		return positionDao.deleteByParam(params);
	}


//	@Override
//	public PageInfo<Position> queryPageListByCmsPositionDO(Position cmsPositionDO) {
//		if (cmsPositionDO != null) {
//			Map<String,Object> params = BeanUtil.beanMap(cmsPositionDO);
//			Integer totalCount = this.queryByParamCount(params);
//
//			PageInfo<Position> page = new PageInfo<Position>();
//			page.setPage(cmsPositionDO.getStartPage());
//			page.setSize(cmsPositionDO.getPageSize());
//			page.setRecords(totalCount.intValue());
//			page = queryPageByObject(cmsPositionDO, page);
//			return page;
//		}
//		return new PageInfo<Position>();
//	}


//	@Override
//	public PageInfo<Position> queryPageListByCmsPositionDOAndStartPageSize(Position cmsPositionDO, int startPage,
//			int pageSize) {
//		if (cmsPositionDO != null && startPage>0 && pageSize>0) {
//			cmsPositionDO.setStartPage(startPage);
//			cmsPositionDO.setPageSize(pageSize);
//			return this.queryPageListByCmsPositionDO(cmsPositionDO);
//		}
//		return new PageInfo<Position>();
//	}


	@Override
	public BaseDao<Position> getDao() {
		return positionDao;
	}

}
