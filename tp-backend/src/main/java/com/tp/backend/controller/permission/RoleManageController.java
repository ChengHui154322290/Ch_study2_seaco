package com.tp.backend.controller.permission;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.usr.RoleInfo;
import com.tp.proxy.usr.RoleInfoProxy;
import com.tp.proxy.usr.RoleMenuLimitProxy;
import com.tp.proxy.usr.RoleMenuProxy;
import com.tp.proxy.usr.SysMenuProxy;
import com.tp.util.StringUtil;

@Controller
@RequestMapping("/permission/role")
public class RoleManageController extends AbstractBaseController{

	private final static Log logger = LogFactory.getLog(RoleManageController.class);
	
	@Autowired
	private RoleInfoProxy roleInfoProxy;
	@Autowired
	private SysMenuProxy sysMenuProxy;
	@Autowired
	private RoleMenuProxy roleMenuProxy;
	@Autowired
	private RoleMenuLimitProxy roleMenuLimitProxy;
	
	@RequestMapping("/roleManage")
	public String roleManage(HttpServletRequest request){
		return "role/roleManage";
	}
	
	@RequestMapping("/roleList")
	public String loadRoleList(Model model,Integer pageNo,Integer pageSize){
		model.addAttribute("page", roleInfoProxy.queryPageByParam(new HashMap<String,Object>(), new PageInfo<RoleInfo>(pageNo,pageSize)).getData());
		return "role/roleList";
	}
	
	@RequestMapping("/showMenuTree")
	public String showMenuTree(HttpServletRequest request,Long roleId){
		return "role/showMenuTree";
	}
	
	@RequestMapping("/editRoleManage")
	public String roleManage(Model model,Long roleId){
		if(null!=roleId){
			RoleInfo role = this.roleInfoProxy.findById(roleId);
			model.addAttribute("role", role);
		}
		String sysMenuIds = this.roleMenuProxy.getRoleMenus(roleId);
		String sysMenuLimitIds = roleMenuLimitProxy.getRoleMenuLimits(roleId);
		model.addAttribute("sysMenuLimitIds", sysMenuLimitIds);
		model.addAttribute("sysMenuIds", sysMenuIds);
		return "role/editRoleManage";
	}
	
	@RequestMapping("/loadSysMenuTree")
	public void showMenuTree(HttpServletResponse response,Long parentId) throws IOException{
		String sysMenuJson =	"[" +sysMenuProxy.sysMenuTree(parentId)+"]";
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.write(sysMenuJson);
	}
	
	
	@RequestMapping("/save")
	@ResponseBody
	public ResultInfo<RoleInfo> save(RoleInfo role,String sysMenuIds,String sysMenuLimitIds){
		if(null==role){
			return new ResultInfo<RoleInfo>(new FailInfo("数据异常,role为空"));
		}
		if(StringUtil.isNullOrEmpty(role.getName())){
			return new ResultInfo<RoleInfo>(new FailInfo("请输入角色名"));
		}
		Long roleId = this.roleInfoProxy.save(role);
		roleMenuProxy.setRoleMenu(roleId, sysMenuIds);
		roleMenuLimitProxy.setRoleMenuLimit(roleId, sysMenuLimitIds);
		return new ResultInfo<RoleInfo>(role);
	}
}
