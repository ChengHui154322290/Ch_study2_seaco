package com.tp.service.cms;

import java.util.List;

import javax.xml.rpc.ServiceException;

import com.tp.common.vo.PageInfo;
import com.tp.model.cms.DefinedElement;
import com.tp.service.IBaseService;
 /**
 * 自定义元素 Service
 * 
 */
public interface IDefinedElementService extends IBaseService<DefinedElement>{

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	int deleteByIds(List<Long> ids) throws Exception;

//	/**
//	 * 动态更新 自定义元素部分字段
//	 * @param cmsDefinedElement
//	 * @return 更新行数
//	 * @throws ServiceException
//	 * 
//	 */
//	int updateDynamic(DefinedElement cmsDefinedElement) throws ServiceException;

	/**
	 * 根据ID查询 一个 自定义元素
	 * @param id
	 * @return DefinedElement
	 * @throws ServiceException
	 * 
	 */
	DefinedElement selectById(Long id) throws ServiceException;

	/**
	 * 根据  自定义元素 动态返回记录数
	 * @param cmsDefinedElement
	 * @return 记录数
	 * @throws ServiceException
	 * 
	 */
	Long selectCountDynamic(DefinedElement cmsDefinedElement) throws ServiceException;

	/**
	 * 动态返回 自定义元素 列表
	 * @param cmsDefinedElement
	 * @return List<DefinedElement>
	 * @throws ServiceException
	 * 
	 */
	List<DefinedElement> selectDynamic(DefinedElement cmsDefinedElement) throws ServiceException;

	/**
	 * 动态返回 自定义元素 分页列表
	 * @param cmsDefinedElement
	 * @return Page<DefinedElement>
	 * @throws ServiceException
	 * 
	 */
	PageInfo<DefinedElement> queryPageListByDefinedElement(DefinedElement cmsDefinedElement);

	/**
	 * 动态返回 自定义元素 分页列表
	 * @param cmsDefinedElement
	 * @param startPage 起始页
	 * @param pageSize 每页记录数
	 * @return Page<DefinedElement>
	 * @throws ServiceException
	 * 
	 */
	PageInfo<DefinedElement> queryPageListByDefinedElementAndStartPageSize(DefinedElement cmsDefinedElement,int startPage,int pageSize);

}
