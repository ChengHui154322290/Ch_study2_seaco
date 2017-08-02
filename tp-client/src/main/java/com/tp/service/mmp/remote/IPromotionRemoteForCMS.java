/**
 * 
 */
package com.tp.service.mmp.remote;


import com.tp.common.vo.PageInfo;
import com.tp.dto.mmp.TopicDetailDTO;
import com.tp.dto.mmp.TopicItemBrandCategoryDTO;
import com.tp.exception.ServiceException;
import com.tp.query.mmp.CmsTopicQuery;
import com.tp.result.mmp.TopicItemInfoResult;

/**
 * 
 **
 */
public interface IPromotionRemoteForCMS {

	/**
	 * 根据传入条件，返回指定类型的西客商城商城活动 只显示审批通过&正在进行的活动
	 * 
	 * @param query
	 * @return
	 * @throws ServiceException
	 */
	PageInfo<TopicDetailDTO> getXGMallTopicList(CmsTopicQuery query)
			throws ServiceException;

	/**
	 * 根据传入条件，返回指定类型的商城商品 只显示审批通过&正在进行的活动
	 * 
	 * @param query
	 * @return
	 * @throws ServiceException
	 */
	PageInfo<TopicItemInfoResult> getXGMallItemList(CmsTopicQuery query)
			throws ServiceException;

	/**
	 * 根据传入条件，获取商城下品牌 只显示审批通过&正在进行的活动 只返回品牌Id列表
	 * 
	 * @param query
	 * @return
	 * @throws ServiceException
	 */
	TopicItemBrandCategoryDTO getXGMallBrandList(CmsTopicQuery query)
			throws ServiceException;
}
