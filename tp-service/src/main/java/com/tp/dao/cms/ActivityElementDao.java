package com.tp.dao.cms;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.cms.ActivityElement;

public interface ActivityElementDao extends BaseDao<ActivityElement> {

	/**
	 * 根据ID更新 活动元素全部属性
	 * @param cmsActivityElementDO
	 * @return 更新行数
	 * @throws DAOException
	 * @author szy
	 */
	Integer update(ActivityElement cmsActivityElementDO);

	/**
	 * 根据ID删除 活动元素
	 * @param id
	 * @return 删除行数
	 * @throws DAOException
	 * @author szy
	 */
	Integer deleteById(Long id);
	
	/**
	 * 根据ID集合删除 
	 * @param ids
	 * @return 删除行数
	 * @throws DAOException
	 */
	Integer deleteByIds(List<Long> ids);

	/**
	 * 动态更新 活动元素部分属性，包括全部
	 * @param cmsActivityElementDO
	 * @return 更新行数
	 * @throws DAOException
	 * @author szy
	 */
	Integer updateDynamic(ActivityElement cmsActivityElementDO);

	/**
	 * 根据ID查询 一个 活动元素
	 * @param id
	 * @return ActivityElement
	 * @throws DAOException
	 * @author szy
	 */
	ActivityElement selectById(Long id, boolean isReadSalve);

	/**
	 * 根据  活动元素 动态返回记录数
	 * @param cmsActivityElementDO
	 * @return 记录条数
	 * @throws DAOException
	 * @author szy
	 */
	Long selectCountDynamic(ActivityElement cmsActivityElementDO);

	/**
	 * 根据  活动元素 动态返回 活动元素 列表
	 * @param cmsActivityElementDO
	 * @return List<ActivityElement>
	 * @throws DAOException
	 * @author szy
	 */
	List<ActivityElement> selectDynamic(ActivityElement cmsActivityElementDO);

	/**
	 * 根据  活动元素 动态返回 活动元素 Limit 列表
	 * @param cmsActivityElementDO start,pageSize属性必须指定
	 * @return List<ActivityElement>
	 * @throws DAOException
	 * @author szy
	 */
	List<ActivityElement> selectDynamicPageQuery(ActivityElement cmsActivityElementDO);
	
	Long selectIsExistid(ActivityElement cmsActivityElementDO);
}
