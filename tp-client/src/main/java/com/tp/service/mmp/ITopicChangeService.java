package com.tp.service.mmp;

import java.util.List;

import com.tp.common.vo.PageInfo;
import com.tp.dto.mmp.TopicChangeDetailDTO;
import com.tp.model.mmp.TopicChange;
import com.tp.service.IBaseService;

/**
 * 促销活动 Service
 * @author szy
 */
public interface ITopicChangeService extends IBaseService<TopicChange> {

	/**
	 * 获取模糊查询后的数量
	 * @param
	 * @return
	 * @throws
	 */
	Long selectCountDynamicWithLike(TopicChange topicChange);

	/**
	 * 获取模糊查询和分页的数据
	 * @param
	 * @return
	 * @throws
	 */
	List<TopicChange> selectDynamicPageQueryWithLike(TopicChange topicChange);

	/**
	 * 根据专题id，获得专题变更单的详细信息
	 * 
	 * @param tid
	 * @return
	 */
	TopicChangeDetailDTO getTopicChangeDetailById(Long tid);

	 /**
	  * 支持模糊查询和分页
	  * @param topicChangeDO
	  * @param currentPage
	  * @param pageSize
	  * @return
	  */
	 PageInfo<TopicChange> queryPageListByTopicChangeDOAndStartPageSizeWithLike(TopicChange topicChangeDO, int currentPage, int pageSize);

	 /**
	  * 支持模糊查询和分页
	  * @param topicChangeDO
	  * @return
	  */
	 PageInfo<TopicChange> queryPageListByTopicChangeDOWithLike(TopicChange topicChangeDO);
}
