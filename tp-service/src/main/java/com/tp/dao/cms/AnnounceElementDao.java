package com.tp.dao.cms;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.cms.AnnounceElement;

public interface AnnounceElementDao extends BaseDao<AnnounceElement> {
	
	/**
	 * 根据ID集合删除 
	 * @param ids
	 * @return 删除行数
	 * @throws DAOException
	 */
	Integer deleteByIds(List<Long> ids);

	/**
	 * 动态更新 公告元素部分属性，包括全部
	 * @param cmsAnnounceElementDO
	 * @return 更新行数
	 * @throws DAOException
	 * @author szy
	 */
	Integer updateDynamic(AnnounceElement cmsAnnounceElementDO);

	/**
	 * 根据ID查询 一个 公告元素
	 * @param id
	 * @return AnnounceElement
	 * @throws DAOException
	 * @author szy
	 */
	AnnounceElement selectById(Long id, boolean isReadSalve);

	/**
	 * 根据  公告元素 动态返回记录数
	 * @param cmsAnnounceElementDO
	 * @return 记录条数
	 * @throws DAOException
	 * @author szy
	 */
	Long selectCountDynamic(AnnounceElement cmsAnnounceElementDO);

	/**
	 * 根据  公告元素 动态返回 公告元素 列表
	 * @param cmsAnnounceElementDO
	 * @return List<AnnounceElement>
	 * @throws DAOException
	 * @author szy
	 */
	List<AnnounceElement> selectDynamic(AnnounceElement cmsAnnounceElementDO);

	/**
	 * 根据  公告元素 动态返回 公告元素 Limit 列表
	 * @param cmsAnnounceElementDO start,pageSize属性必须指定
	 * @return List<AnnounceElement>
	 * @throws DAOException
	 * @author szy
	 */
	List<AnnounceElement> selectDynamicPageQuery(AnnounceElement cmsAnnounceElementDO);
}
