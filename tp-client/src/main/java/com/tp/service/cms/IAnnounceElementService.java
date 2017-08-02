package com.tp.service.cms;

import java.util.List;

import javax.xml.rpc.ServiceException;

import com.tp.common.vo.PageInfo;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.AnnounceElement;
import com.tp.service.IBaseService;
 /**
 * 公告元素 Service
 * @author szy
 */
public interface IAnnounceElementService extends IBaseService<AnnounceElement>{


	/**
	 * 批量删除
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	int deleteByIds(List<Long> ids) throws Exception;

//	/**
//	 * 动态更新 公告元素部分字段
//	 * @param cmsAnnounceElementDO
//	 * @return 更新行数
//	 * @throws ServiceException
//	 * @author szy
//	 */
//	int updateDynamic(AnnounceElement cmsAnnounceElementDO) throws CmsServiceException;

	/**
	 * 根据ID查询 一个 公告元素
	 * @param id
	 * @return AnnounceElement
	 * @throws ServiceException
	 * @author szy
	 */
	AnnounceElement selectById(Long id) throws CmsServiceException;

	/**
	 * 根据  公告元素 动态返回记录数
	 * @param cmsAnnounceElementDO
	 * @return 记录数
	 * @throws ServiceException
	 * @author szy
	 */
	Long selectCountDynamic(AnnounceElement cmsAnnounceElementDO) throws CmsServiceException;

	/**
	 * 动态返回 公告元素 列表
	 * @param cmsAnnounceElementDO
	 * @return List<AnnounceElement>
	 * @throws ServiceException
	 * @author szy
	 */
	List<AnnounceElement> selectDynamic(AnnounceElement cmsAnnounceElementDO) throws CmsServiceException;

	/**
	 * 动态返回 公告元素 分页列表
	 * @param cmsAnnounceElementDO
	 * @return PageInfo<AnnounceElement>
	 * @throws ServiceException
	 * @author szy
	 */
	PageInfo<AnnounceElement> queryPageListByAnnounceElement(AnnounceElement cmsAnnounceElementDO);

	/**
	 * 动态返回 公告元素 分页列表
	 * @param cmsAnnounceElementDO
	 * @param startPage 起始页
	 * @param pageSize 每页记录数
	 * @return PageInfo<AnnounceElement>
	 * @throws ServiceException
	 * @author szy
	 */
	PageInfo<AnnounceElement> queryPageListByAnnounceElementAndStartPageSize(AnnounceElement cmsAnnounceElementDO,int startPage,int pageSize);
	
}
