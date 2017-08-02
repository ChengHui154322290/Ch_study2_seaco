package com.tp.dao.cms;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.cms.DefinedElement;

public interface DefinedElementDao extends BaseDao<DefinedElement> {
	/**
	 * 根据ID集合删除 
	 * @param ids
	 * @return 删除行数
	 * @throws DAOException
	 */
	Integer deleteByIds(List<Long> ids);

	/**
	 * 动态更新 自定义元素部分属性，包括全部
	 * @param cmsDefinedElementDO
	 * @return 更新行数
	 * @throws DAOException
	 * @author szy
	 */
	Integer updateDynamic(DefinedElement cmsDefinedElementDO);

	/**
	 * 根据ID查询 一个 自定义元素
	 * @param id
	 * @return DefinedElement
	 * @throws DAOException
	 * @author szy
	 */
	DefinedElement selectById(Long id, boolean isReadSalve);

	/**
	 * 根据  自定义元素 动态返回记录数
	 * @param cmsDefinedElementDO
	 * @return 记录条数
	 * @throws DAOException
	 * @author szy
	 */
	Long selectCountDynamic(DefinedElement cmsDefinedElementDO);

	/**
	 * 根据  自定义元素 动态返回 自定义元素 列表
	 * @param cmsDefinedElementDO
	 * @return List<DefinedElement>
	 * @throws DAOException
	 * @author szy
	 */
	List<DefinedElement> selectDynamic(DefinedElement cmsDefinedElementDO);

	/**
	 * 根据  自定义元素 动态返回 自定义元素 Limit 列表
	 * @param cmsDefinedElementDO start,pageSize属性必须指定
	 * @return List<DefinedElement>
	 * @throws DAOException
	 * @author szy
	 */
	List<DefinedElement> selectDynamicPageQuery(DefinedElement cmsDefinedElementDO);
}
