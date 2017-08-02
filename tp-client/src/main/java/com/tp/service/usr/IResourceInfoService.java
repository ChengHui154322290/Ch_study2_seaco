package com.tp.service.usr;

import java.util.List;

import com.tp.model.usr.ResourceInfo;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 权限信息表接口
  */
public interface IResourceInfoService extends IBaseService<ResourceInfo>{

	/**
	 * 根据ID列表查询权限
	 * @param ids
	 * @return
	 */
	public List<ResourceInfo> queryResourceInfoListByIds(List<Long> ids);
	
	/**
	 * 查询全部权限
	 * @return
	 */
	public List<ResourceInfo> queryAllList();
}
