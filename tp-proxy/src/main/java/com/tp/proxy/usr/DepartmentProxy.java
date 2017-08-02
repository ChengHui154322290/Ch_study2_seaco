package com.tp.proxy.usr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.model.usr.Department;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.usr.IDepartmentService;
import com.tp.service.usr.IUserInfoService;
/**
 * 部门表代理层
 * @author szy
 *
 */
@Service
public class DepartmentProxy extends BaseProxy<Department>{

	@Autowired
	private IDepartmentService departmentService;
	@Autowired
	private IUserInfoService userInfoService;
	@Override
	public IBaseService<Department> getService() {
		return departmentService;
	}
	
	public void save(Department department) throws Exception{
		UserInfo user = UserHandler.getUser();
		if(null == department.getId()){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("name", department.getName());
			List<Department> list = this.departmentService.queryByParam(params);
			if(null!=list&&!list.isEmpty()) throw new Exception("部门名称已经存在");
			department.setCreateUser(user.getUserName());
		}
		if(null == department.getId())
		department.setUpdateUser(user.getUserName());
		department.setStatus(Constant.DEFAULTED.YES);
		departmentService.save(department);
	}
	
	public PageInfo<Department> getAll(Integer pageNo,Integer pageSize){
		Department department = new Department();
		department.setStatus(Constant.DEFAULTED.YES);
		PageInfo<Department> page = departmentService.queryPageByObject(department, new PageInfo<Department>(pageNo,pageSize));
		return page;
	}
	
	public List<Department> getAll(){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", Constant.DEFAULTED.YES);
		List<Department> list = departmentService.queryByParam(params);
		return list;
	}
	
	public void remove(Long departmentId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("departmentId",departmentId);
		List<UserInfo> userList = userInfoService.queryByParam(params);
		if(null != userList && !userList.isEmpty())return;
		Department department = new Department();
		department.setId(departmentId);
		department.setStatus(Constant.DEFAULTED.NO);
		this.departmentService.updateNotNullById(department);
	}
}
