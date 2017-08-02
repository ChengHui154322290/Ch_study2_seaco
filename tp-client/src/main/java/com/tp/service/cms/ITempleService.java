package com.tp.service.cms;

import java.util.List;

import javax.xml.rpc.ServiceException;

import com.tp.common.vo.PageInfo;
import com.tp.model.cms.Temple;
import com.tp.service.IBaseService;
 /**
 * 模板管理 Service
 * @author szy
 */
public interface ITempleService extends IBaseService<Temple>{

	/**
	 * 动态返回 模板管理 分页列表
	 * @param cmsTempleDO
	 * @return Page<Temple>
	 * @throws ServiceException
	 * @author szy
	 */
	PageInfo<Temple> queryPageListByTemple(Temple cmsTempleDO);

	/**
	 * 动态返回 模板管理 分页列表
	 * @param cmsTempleDO
	 * @param startPage 起始页
	 * @param pageSize 每页记录数
	 * @return Page<Temple>
	 * @throws ServiceException
	 * @author szy
	 */
	PageInfo<Temple> queryPageListByTempleAndStartPageSize(Temple cmsTempleDO,int startPage,int pageSize);

	int deleteByIds(List<Long> ids) throws Exception;

}
