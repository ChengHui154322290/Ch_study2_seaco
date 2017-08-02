package com.tp.service.cms;

import java.util.List;

import javax.xml.rpc.ServiceException;

import com.tp.common.vo.PageInfo;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.SeoElement;
import com.tp.service.IBaseService;
 /**
 * SEO元素 Service
 */
public interface ISeoElementService extends IBaseService<SeoElement>{

//	/**
//	 * 根据ID更新 SEO元素全部字段
//	 * @param cmsSeoElementDO
//	 * @return 更新行数
//	 * @throws ServiceException
//	 * 
//	 */
//	int updateById(CmsSeoElementDO cmsSeoElementDO) throws CmsServiceException;

	/**
	 * 根据ID删除 SEO元素
	 * @param id
	 * @return 物理删除行
	 * @throws ServiceException
	 * 
	 */
	int deleteByIds(List<Long> ids) throws CmsServiceException;
	
	/**
	 * 动态返回 SEO元素 分页列表
	 * @param cmsSeoElementDO
	 * @return Page<CmsSeoElementDO>
	 * @throws ServiceException
	 * 
	 */
	PageInfo<SeoElement> queryPageListByCmsSeoElement(SeoElement cmsSeoElementDO);

	/**
	 * 动态返回 SEO元素 分页列表
	 * @param cmsSeoElementDO
	 * @param startPage 起始页
	 * @param pageSize 每页记录数
	 * @return Page<CmsSeoElementDO>
	 * @throws ServiceException
	 * 
	 */
	PageInfo<SeoElement> queryPageListByCmsSeoElementAndStartPageSize(SeoElement cmsSeoElementDO,int startPage,int pageSize);

	List<SeoElement> getDefinedElement(Long positionId);

}
