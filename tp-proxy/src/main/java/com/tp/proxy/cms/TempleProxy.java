package com.tp.proxy.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.Temple;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cms.ITempleService;
/**
 * 模板管理表代理层
 * @author szy
 *
 */
@Service
public class TempleProxy extends BaseProxy<Temple>{

	@Autowired
	private ITempleService templeService;

	@Override
	public IBaseService<Temple> getService() {
		return templeService;
	}
	/**
	 * 模板管理列表的查询
	 * @param jSONObject  即前台传值的查询条件
	 * @return
	 * @throws Exception
	 */
	public PageInfo<Temple> getTempletList(Temple query) throws CmsServiceException{
		if (null == query) {
			return null;
		}
		
		return templeService.queryPageListByTemple(query);
	}
	
	public Long addSubmit(Temple temple) throws CmsServiceException{
		temple = templeService.insert(temple);
		return temple.getId();
	}
	
	public Integer updateSubmit(Temple cmsTemple) throws CmsServiceException{
		return templeService.updateById(cmsTemple);
	}
	
	public Integer delByIds(List<Long> ids) throws Exception{
		return templeService.deleteByIds(ids);
	}
	
	public Temple getById(Long id) throws Exception{
		return templeService.queryById(id);
	}
}
