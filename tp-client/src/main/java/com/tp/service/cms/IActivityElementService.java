package com.tp.service.cms;

import java.util.List;

import javax.xml.rpc.ServiceException;

import com.tp.common.vo.PageInfo;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.ActivityElement;
import com.tp.service.IBaseService;
 /**
 * 活动元素 Service
 * @author szy
 */
public interface IActivityElementService extends IBaseService<ActivityElement>{

	/**
	 * 根据CmsActivityElementDO对象更新 活动元素
	 * @param cmsActivityElementDO
	 * @param isAllField 是否更新所有字段(false 动态更新字段，true 更新所有字段,传null或false将动态更新，建议动态更新)
	 * @return 更新行数
	 * @throws ServiceException
	 */
	int update(ActivityElement cmsActivityElementDO,boolean isAllField) throws CmsServiceException;

//	/**
//	 * 根据ID更新 活动元素全部字段
//	 * @param cmsActivityElementDO
//	 * @return 更新行数
//	 * @throws ServiceException
//	 * @author szy
//	 */
//	int updateById(CmsActivityElementDO cmsActivityElementDO) throws ServiceException;

	/**
	 * 根据ID删除 活动元素
	 * @param id
	 * @return 物理删除行
	 * @throws ServiceException
	 */
	int deleteById(Long id) throws CmsServiceException;
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	int deleteByIds(List<Long> ids) throws Exception;

//	/**
//	 * 动态更新 活动元素部分字段
//	 * @param cmsActivityElementDO
//	 * @return 更新行数
//	 * @throws ServiceException
//	 * @author szy
//	 */
//	int updateDynamic(CmsActivityElementDO cmsActivityElementDO) throws ServiceException;

	/**
	 * 根据ID查询 一个 活动元素
	 * @param id
	 * @return CmsActivityElementDO
	 * @throws ServiceException
	 */
	ActivityElement selectById(Long id) throws CmsServiceException;

	/**
	 * 根据  活动元素 动态返回记录数
	 * @param cmsActivityElementDO
	 * @return 记录数
	 * @throws ServiceException
	 */
	Long selectCountDynamic(ActivityElement cmsActivityElementDO) throws CmsServiceException;

	/**
	 * 动态返回 活动元素 列表
	 * @param cmsActivityElementDO
	 * @return List<CmsActivityElementDO>
	 * @throws ServiceException
	 */
	List<ActivityElement> selectDynamic(ActivityElement cmsActivityElementDO) throws CmsServiceException;

	/**
	 * 动态返回 活动元素 分页列表
	 * @param cmsActivityElementDO
	 * @return Page<CmsActivityElementDO>
	 * @throws ServiceException
	 */
	PageInfo<ActivityElement> queryPageListByCmsActivityElementDO(ActivityElement cmsActivityElementDO);

	/**
	 * 动态返回 活动元素 分页列表
	 * @param cmsActivityElementDO
	 * @param startPage 起始页
	 * @param pageSize 每页记录数
	 * @return Page<CmsActivityElementDO>
	 * @throws ServiceException
	 */
	PageInfo<ActivityElement> queryPageListByCmsActivityElementDOAndStartPageSize(ActivityElement cmsActivityElementDO,int startPage,int pageSize);

}
