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
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.cms.PositionDao;
import com.tp.dao.cms.TempleDao;
import com.tp.model.cms.Temple;
import com.tp.service.BaseService;
import com.tp.service.cms.ITempleService;
import com.tp.util.BeanUtil;

@Service(value="templeService")
public class TempleService extends BaseService<Temple> implements ITempleService{

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private TempleDao templeDao;
	
	@Autowired
	private PositionDao positionDao;

	public PageInfo<Temple> queryPageListByTempleAndStartPageSize(Temple cmsTempleDO,int startPage,int pageSize){
		if (cmsTempleDO != null && startPage>0 && pageSize>0) {
			cmsTempleDO.setStartPage(startPage);
			cmsTempleDO.setPageSize(pageSize);
			return this.queryPageListByTemple(cmsTempleDO);
		}
		return new PageInfo<Temple>();
	}

	@Override
	public int deleteByIds(List<Long> ids) throws Exception {
		int count = 0;
		try {
			//先判断页面下面是否有启用的模块，有的话是不能删除页面
			long fgcount = positionDao.selectIsExists(ids, true);
			if(fgcount > 0){
				return -1;
			}
			
			count = templeDao.deleteByIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("批量删除报错", e);
		}
		return count;
	}

	@Override
	public BaseDao<Temple> getDao() {
		return templeDao;
	}

	public PageInfo<Temple> queryPageListByTemple(Temple cmsTempleDO) {
		if (cmsTempleDO != null) {
			PageInfo<Temple> page = new PageInfo<Temple>();
			page.setPage(cmsTempleDO.getStartPage());
			page.setSize(cmsTempleDO.getPageSize());
			Map<String, Object> params = BeanUtil.beanMap(cmsTempleDO);
			params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " id desc ");
			return queryPageByParam(params, page);
		}
		return new PageInfo<Temple>();
	}
	
}
