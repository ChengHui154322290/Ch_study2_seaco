package com.tp.proxy.cms;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.Position;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cms.IPositionService;
import com.tp.util.BeanUtil;
/**
 * 位置管理表代理层
 * @author szy
 *
 */
@Service
public class PositionProxy extends BaseProxy<Position>{

	@Autowired
	private IPositionService positionService;

	@Override
	public IBaseService<Position> getService() {
		return positionService;
	}

	/**
	 * 模板管理列表的查询
	 * @param jSONObject  即前台传值的查询条件
	 * @return
	 * @throws Exception
	 */
	public PageInfo<Position> getPositionList(Position query) throws CmsServiceException{
		if (null == query) {
			return null;
		}
		PageInfo<Position> pageInfo = new PageInfo<Position>();
		pageInfo.setPage(query.getStartPage());
		pageInfo.setSize(query.getPageSize());
		
		Map<String, Object> params = BeanUtil.beanMap(query);
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "seq asc ");
		return positionService.queryPageByParam(params, new PageInfo<>(query.getStartPage(), query.getPageSize()));
	}
	
	public Long addSubmit(Position position) throws CmsServiceException{
		position = positionService.insert(position);
		return position.getId();
	}
	
	public Integer updateSubmit(Position cmsPositionDO) throws CmsServiceException{
		return positionService.updateById(cmsPositionDO);
	}
	
	public Integer delByIds(List<Long> ids) throws Exception{
		return positionService.deleteByIds(ids);
	}
	
	public Position getById(Long id) throws Exception{
		return positionService.queryById(id);
	}
}
