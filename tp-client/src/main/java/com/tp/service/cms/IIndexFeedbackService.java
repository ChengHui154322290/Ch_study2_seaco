package com.tp.service.cms;

import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import com.tp.common.vo.PageInfo;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.IndexFeedback;
import com.tp.service.IBaseService;
 /**
 * 页面反馈信息 Service
 */
public interface IIndexFeedbackService extends IBaseService<IndexFeedback>{


	
	/**
	 * 根据ID删除 页面反馈信息
	 * @param id
	 * @return 物理删除行
	 * @throws ServiceException
	 * 
	 */
	int deleteByIds(List<Long> id) throws ServiceException;

//	/**
//	 * 动态更新 页面反馈信息部分字段
//	 * @param cmsIndexFeedback
//	 * @return 更新行数
//	 * @throws ServiceException
//	 * 
//	 */
//	int updateDynamic(IndexFeedback cmsIndexFeedback) throws ServiceException;

	/**
	 * 根据ID查询 一个 页面反馈信息
	 * @param id
	 * @return IndexFeedback
	 * @throws ServiceException
	 * 
	 */
	IndexFeedback selectById(Long id) throws ServiceException;

	/**
	 * 根据  页面反馈信息 动态返回记录数
	 * @param cmsIndexFeedback
	 * @return 记录数
	 * @throws ServiceException
	 * 
	 */
	Long selectCountDynamic(IndexFeedback cmsIndexFeedback) throws ServiceException;

	/**
	 * 动态返回 页面反馈信息 列表
	 * @param cmsIndexFeedback
	 * @return List<IndexFeedback>
	 * @throws ServiceException
	 * 
	 */
	List<IndexFeedback> selectDynamic(IndexFeedback cmsIndexFeedback) throws ServiceException;

	/**
	 * 动态返回 页面反馈信息 分页列表
	 * @param cmsIndexFeedback
	 * @return Page<IndexFeedback>
	 * @throws ServiceException
	 * 
	 */
	PageInfo<IndexFeedback> queryPageListByIndexFeedback(IndexFeedback cmsIndexFeedback);

	/**
	 * 动态返回 页面反馈信息 分页列表
	 * @param cmsIndexFeedback
	 * @param startPage 起始页
	 * @param pageSize 每页记录数
	 * @return Page<IndexFeedback>
	 * @throws ServiceException
	 * 
	 */
	PageInfo<IndexFeedback> queryPageListByIndexFeedbackAndStartPageSize(IndexFeedback cmsIndexFeedback,int startPage,int pageSize);

	/**
	 * 分页查询
	 * @param paramMap
	 * @param cmsAdvertiseInfo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectFeedbackPageQuery(
			Map<String, Object> paramMap, IndexFeedback cmsIndexFeedback)throws Exception ;

}
