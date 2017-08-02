package com.tp.dao.cms;

import java.util.List;
import java.util.Map;

import com.tp.common.dao.BaseDao;
import com.tp.dto.cms.CmsAdvertiseInfoDTO;
import com.tp.model.cms.AdvertiseInfo;

public interface AdvertiseInfoDao extends BaseDao<AdvertiseInfo> {

	/**
	 * 根据ID更新 全部属性
	 * @param AdvertiseInfo
	 * @return 更新行数
	 * @throws DAOException
	 */
	Integer update(AdvertiseInfo cmsAdvertiseInfoDO);

	/**
	 * 根据ID删除 
	 * @param id
	 * @return 删除行数
	 * @throws DAOException
	 */
	Integer deleteById(Long id);

	/**
	 * 动态更新 部分属性，包括全部
	 * @param AdvertiseInfo
	 * @return 更新行数
	 * @throws DAOException
	 */
	Integer updateDynamic(AdvertiseInfo cmsAdvertiseInfoDO);

	/**
	 * 根据ID查询 一个 
	 * @param id
	 * @return AdvertiseInfo
	 * @throws DAOException
	 */
	AdvertiseInfo selectById(Long id);

	/**
	 * 根据   动态返回记录数
	 * @param CmsAnnounceInfoDO
	 * @return 记录条数
	 * @throws DAOException
	 */
	Long selectCountDynamic(AdvertiseInfo cmsAdvertiseInfoDO);

	/**
	 * 根据   动态返回  列表
	 * @param AdvertiseInfo
	 * @return List<AdvertiseInfo>
	 * @throws DAOException
	 */
	List<AdvertiseInfo> selectDynamic(AdvertiseInfo cmsAnnounceInfo);

	/**
	 * 根据   动态返回  Limit 列表
	 * @param AdvertiseInfo start,pageSize属性必须指定
	 * @return List<AdvertiseInfo>
	 * @throws DAOException
	 */
	List<AdvertiseInfo> selectDynamicPageQuery(AdvertiseInfo cmsAdvertiseInfoDO);
	
	
	/**
	 * 
	 * <pre>
	 * 根据id集合得到用户集合
	 * </pre>
	 *
	 * @param ids id集合
	 * @return 用户集合
	 */
	public List<AdvertiseInfo> selectByIds(List<Long> ids);
	
	/**
	 * 根据ID集合删除 
	 * @param ids
	 * @return 删除行数
	 * @throws DAOException
	 */
	Integer deleteByIds(List<Long> ids);
	
	/**
	 * 根据ID集合启用 
	 * @param ids
	 * @return 行数
	 * @throws DAOException
	 */
	int openByIds(List<Long> ids);
	
	/**
	 * 根据ID集合禁用
	 * @param ids
	 * @return 行数
	 * @throws DAOException
	 */
	int noOpenByIds(List<Long> ids);
	
	/**
	 * 传入map对象，查询分页集合
	 * @param ids
	 * @return 图片资讯集合
	 * @throws DAOException
	 */
	List<CmsAdvertiseInfoDTO> selectAdvertPageQuery(Map<String, Object> paramMap);
}
