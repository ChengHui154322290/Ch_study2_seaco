package com.tp.backend.controller.permission;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.usr.Department;
import com.tp.proxy.usr.DepartmentProxy;
import com.tp.util.StringUtil;

@Controller
@RequestMapping("/permission/department")
public class DepartmentManageController extends AbstractBaseController{
	
	@Autowired
	private DepartmentProxy departmentProxy;
	
	@RequestMapping("/departmentManage")
	public String departmentManage(HttpServletRequest request){
		return "department/departmentManage";
	}
	
	@RequestMapping("/departmentList")
	public String loadDepartmentList(HttpServletRequest request,Integer pageNo,Integer pageSize){
		PageInfo<Department> page = departmentProxy.queryPageByParam(new HashMap<String,Object>(),new PageInfo<Department>(pageNo,pageSize)).getData();
		request.setAttribute("page", page);
		return "department/departmentList";
	}
	
	
	@RequestMapping("/save")
	@ResponseBody
	public ResultInfo<Boolean> save(HttpServletRequest request,Department departmentDO){
		if(null==departmentDO){
			return new ResultInfo<Boolean>(new FailInfo("数据异常,user为空"));
		}
		if(StringUtil.isBlank(departmentDO.getName())){
			return new ResultInfo<Boolean>(new FailInfo("请输入部门名称"));
		}
		if(null!=departmentDO.getId()){
			ResultInfo<Integer> updateResult = departmentProxy.updateNotNullById(departmentDO);
			if(!updateResult.success){
				return new ResultInfo<Boolean>(updateResult.msg);
			}
		}else{
			ResultInfo<Department> updateResult = departmentProxy.insert(departmentDO);
			if(!updateResult.success){
				return new ResultInfo<Boolean>(updateResult.msg);
			}
		}
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping("/remove")
	@ResponseBody
	public ResultInfo<Boolean> remove(HttpServletRequest request,Long departmentId){
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}
	
}
