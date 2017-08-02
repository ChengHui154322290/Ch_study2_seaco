package com.tp.service.mmp;

import java.util.List;

import com.tp.common.vo.PageInfo;
import com.tp.model.mmp.Platform;
import com.tp.service.IBaseService;

/**
 * 促销适用平台 Service
 */
public interface IPlatformService  extends IBaseService<Platform> {

	/**
	 * 插入  促销适用平台
	 * @param platformDO
	 * @return 主键
	 * @throws ServiceException
	 * @author szy
	 */
	Platform insert(Platform platform);

	/**
	 * 根据Platform对象更新 促销适用平台
	 * @param platformDO
	 * @param isAllField 是否更新所有字段
	 * @return 更新行数
	 * @throws ServiceException
	 * @author szy
	 */
	int update(Platform platformDO,boolean isAllField);

//	/**
//	 * 根据ID更新 促销适用平台全部字段
//	 * @param platformDO
//	 * @return 更新行数
//	 * @throws ServiceException
//	 * @author szy
//	 */
//	int updateById(Platform platform);

	/**
	 * 根据ID删除 促销适用平台
	 * @param id
	 * @return 物理删除行
	 * @throws ServiceException
	 * @author szy
	 */
	int deleteById(Long id);

//	/**
//	 * 动态更新 促销适用平台部分字段
//	 * @param platformDO
//	 * @return 更新行数
//	 * @throws ServiceException
//	 * @author szy
//	 */
//	int updateDynamic(Platform platform);

	/**
	 * 根据ID查询 一个 促销适用平台
	 * @param id
	 * @return Platform
	 * @throws ServiceException
	 * @author szy
	 */
	Platform selectById(Long id);

	/**
	 * 根据  促销适用平台 动态返回记录数
	 * @param platformDO
	 * @return 记录数
	 * @throws ServiceException
	 * @author szy
	 */
	Long selectCountDynamic(Platform platform);

	/**
	 * 动态返回 促销适用平台 列表
	 * @param platformDO
	 * @return List<Platform>
	 * @throws ServiceException
	 * @author szy
	 */
	List<Platform> selectDynamic(Platform platform);

	/**
	 * 动态返回 促销适用平台 分页列表
	 * @param platformDO
	 * @return PageInfo<Platform>
	 * @throws ServiceException
	 * @author szy
	 */
	PageInfo<Platform> queryPageInfoListByPlatform(Platform platform);

	/**
	 * 动态返回 促销适用平台 分页列表
	 * @param platformDO
	 * @param startPageInfo 起始页
	 * @param pageSize 每页记录数
	 * @return PageInfo<Platform>
	 * @throws ServiceException
	 * @author szy
	 */
	PageInfo<Platform> queryPageInfoListByPlatformAndStartPageInfoSize(Platform platformDO,int startPageInfo,int pageSize);

}
