package com.tp.dao.cms;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.cms.AdvertiseType;

public interface AdvertiseTypeDao extends BaseDao<AdvertiseType> {

	/**
	 * 根据ID更新 全部属性
	 * @param CmsAdvertiseInfoDO
	 * @return 更新行数
	 * @throws DAOException
	 */
	Integer update(AdvertiseType cmsAdvertTypeDO);

	/**
	 * 根据ID删除 
	 * @param id
	 * @return 删除行数
	 * @throws DAOException
	 */
	Integer deleteById(Long id);

	/**
	 * 动态更新 部分属性，包括全部
	 * @param CmsAdvertiseInfoDO
	 * @return 更新行数
	 * @throws DAOException
	 */
	Integer updateDynamic(AdvertiseType cmsAdvertTypeDO);

	/**
	 * 根据ID查询 一个 
	 * @param id
	 * @return CmsAdvertiseInfoDO
	 * @throws DAOException
	 */
	AdvertiseType selectById(Long id);

	/**
	 * 根据   动态返回记录数
	 * @param CmsAnnounceInfoDO
	 * @return 记录条数
	 * @throws DAOException
	 */
	Long selectCountDynamic(AdvertiseType cmsAdvertTypeDO);

	/**
	 * 根据   动态返回  列表
	 * @param CmsAdvertiseInfoDO
	 * @return List<CmsAdvertiseInfoDO>
	 * @throws DAOException
	 */
	List<AdvertiseType> selectDynamic(AdvertiseType cmsAdvertTypeDO);

	/**
	 * 根据   动态返回  Limit 列表
	 * @param CmsAdvertiseInfoDO start,pageSize属性必须指定
	 * @return List<CmsAdvertiseInfoDO>
	 * @throws DAOException
	 */
	List<AdvertiseType> selectDynamicPageQuery(AdvertiseType cmsAdvertTypeDO);
	
	/**
	 * 
	 * <pre>
	 * 根据id集合得到用户集合
	 * </pre>
	 *
	 * @param ids id集合
	 * @return 用户集合
	 */
	public List<AdvertiseType> selectByIds(List<Long> ids);
	
	/**
	 * 根据ID集合删除 
	 * @param ids
	 * @return 删除行数
	 * @throws DAOException
	 */
	Integer deleteByIds(List<Long> ids);
	
	/**
	 * 查询已存在的接口标识数 
	 * @param ids
	 * @return 行数
	 * @throws DAOException
	 */
	Long selectIsIdentExist(AdvertiseType cmsAdvertTypeDO);
}
