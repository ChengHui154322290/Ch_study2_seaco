package com.tp.dao.cms;

import java.util.List;
import java.util.Map;

import com.tp.common.dao.BaseDao;
import com.tp.model.cms.IndexFeedback;

public interface IndexFeedbackDao extends BaseDao<IndexFeedback> {
	/**
	 * 动态更新 页面反馈信息部分属性，包括全部
	 * @param cmsIndexFeedbackDO
	 * @return 更新行数
	 * @throws DAOException
	 * @author szy
	 */
	Integer updateDynamic(IndexFeedback cmsIndexFeedbackDO);

	/**
	 * 根据ID查询 一个 页面反馈信息
	 * @param id
	 * @return IndexFeedback
	 * @throws DAOException
	 * @author szy
	 */
	IndexFeedback selectById(Long id, boolean isReadSalve);

	/**
	 * 根据  页面反馈信息 动态返回记录数
	 * @param cmsIndexFeedbackDO
	 * @return 记录条数
	 * @throws DAOException
	 * @author szy
	 */
	Long selectCountDynamic(IndexFeedback cmsIndexFeedbackDO);

	/**
	 * 根据  页面反馈信息 动态返回 页面反馈信息 列表
	 * @param cmsIndexFeedbackDO
	 * @return List<IndexFeedback>
	 * @throws DAOException
	 * @author szy
	 */
	List<IndexFeedback> selectDynamic(IndexFeedback cmsIndexFeedbackDO);

	/**
	 * 根据  页面反馈信息 动态返回 页面反馈信息 Limit 列表
	 * @param cmsIndexFeedbackDO start,pageSize属性必须指定
	 * @return List<IndexFeedback>
	 * @throws DAOException
	 * @author szy
	 */
	List<IndexFeedback> selectDynamicPageQuery(IndexFeedback cmsIndexFeedbackDO);
	
	/**
	 * 分页查询
	 * @param cmsIndexFeedbackDO
	 * @return
	 * @throws DAOException
	 */
	List<IndexFeedback> selectFeedbackPageQuery(Map<String, Object> map);
	
	int deleteByIds(List<Long> ids);
}
