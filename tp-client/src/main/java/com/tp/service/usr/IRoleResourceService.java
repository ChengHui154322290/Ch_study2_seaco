package com.tp.service.usr;

import java.util.List;

import com.tp.model.usr.RoleResource;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 角色权限表接口
  */
public interface IRoleResourceService extends IBaseService<RoleResource>{

	/**
	 * 批量插入
	 * @param roleResourceList
	 */
	public void insertBatchRoleResourceList(List<RoleResource> roleResourceList);
}
