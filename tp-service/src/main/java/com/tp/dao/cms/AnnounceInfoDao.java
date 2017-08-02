package com.tp.dao.cms;

import java.util.List;
import java.util.Map;

import com.tp.common.dao.BaseDao;
import com.tp.dto.cms.CmsAnnounceInfoDTO;
import com.tp.model.cms.AnnounceInfo;

public interface AnnounceInfoDao extends BaseDao<AnnounceInfo> {
	/**
	 * 插入  
	 * @param AnnounceInfo
	 * @return 主键
	 * @throws DAOException
	 */
	void insert(AnnounceInfo cmsAnnounceInfo);

	/**
	 * 根据ID更新 全部属性
	 * @param AnnounceInfo
	 * @return 更新行数
	 * @throws DAOException
	 */
	Integer update(AnnounceInfo cmsAnnounceInfo);

	/**
	 * 根据ID删除 
	 * @param id
	 * @return 删除行数
	 * @throws DAOException
	 */
	Integer deleteById(Long id);

	/**
	 * 动态更新 部分属性，包括全部
	 * @param AnnounceInfo
	 * @return 更新行数
	 * @throws DAOException
	 */
	Integer updateDynamic(AnnounceInfo cmsAnnounceInfo);

	/**
	 * 根据ID查询 一个 
	 * @param id
	 * @return CmsAnnounceInfo
	 * @throws DAOException
	 */
	AnnounceInfo selectById(Long id);

	/**
	 * 根据   动态返回记录数
	 * @param AnnounceInfo
	 * @return 记录条数
	 * @throws DAOException
	 */
	Long selectCountDynamic(AnnounceInfo cmsAnnounceInfo);

	/**
	 * 根据   动态返回  列表
	 * @param AnnounceInfo
	 * @return List<CmsAnnounceInfo>
	 * @throws DAOException
	 */
	List<AnnounceInfo> selectDynamic(AnnounceInfo cmsAnnounceInfo);

	/**
	 * 根据   动态返回  Limit 列表
	 * @param AnnounceInfo start,pageSize属性必须指定
	 * @return List<CmsAnnounceInfo>
	 * @throws DAOException
	 */
	List<AnnounceInfo> selectDynamicPageQuery(AnnounceInfo cmsAnnounceInfo);
	
	
	/**
	 * 
	 * <pre>
	 * 根据id集合得到用户集合
	 * </pre>
	 *
	 * @param ids id集合
	 * @return 用户集合
	 */
	public List<AnnounceInfo> selectByIds(List<Long> ids);
	
	/**
	 * 根据ID集合删除 
	 * @param ids
	 * @return 删除行数
	 * @throws DAOException
	 */
	Integer deleteByIds(List<Long> ids);
	
	/**
	 * 传入map对象，查询分页集合
	 * @param ids
	 * @return 广告资讯集合
	 * @throws DAOException
	 */
	List<CmsAnnounceInfoDTO> selectAnnouncePageQuery(Map<String, Object> paramMap);
}
