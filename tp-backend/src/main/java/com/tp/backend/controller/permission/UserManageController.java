package com.tp.backend.controller.permission;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.usr.Department;
import com.tp.model.usr.QueryUserDto;
import com.tp.model.usr.RoleInfo;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.usr.DepartmentProxy;
import com.tp.proxy.usr.PasswordHelper;
import com.tp.proxy.usr.RoleInfoProxy;
import com.tp.proxy.usr.UserInfoProxy;
import com.tp.proxy.usr.UserRoleProxy;
import com.tp.util.StringUtil;

@Controller
@RequestMapping("/permission/user")
public class UserManageController extends AbstractBaseController{

	@Autowired
	private UserInfoProxy userInfoProxy;
	@Autowired
	private DepartmentProxy departmentProxy;
	@Autowired
	private RoleInfoProxy roleInfoProxy;
	@RequestMapping("/userManage")
	public String userManage(HttpServletRequest request){
		return "backendUser/userManage";
	}
	
	@RequestMapping("/toUpdateUserInfo")
	public String toUpdateUserInfo(HttpServletRequest request){
		return "common/updateUserInfo";
	}
	
	@RequestMapping("/toUpdatePassword")
	public String toUpdatePassword(HttpServletRequest request){
		return "common/updatePassword";
	}
	
	@RequestMapping("/toResetPassword")
	public String toResetPassword(HttpServletRequest request){
		
		return "common/resetPassword";
	}
	
	
	@RequestMapping("/userList")
	public String loadUserList(Model model,Integer pageNo,Integer pageSize,QueryUserDto query){
		PageInfo<UserInfo> page = this.userInfoProxy.getAllPageInfo(pageNo, pageSize,query);
		model.addAttribute("page", page);
		List<Department> departmentList = departmentProxy.getAll();
		model.addAttribute("deList", departmentList);
		List<RoleInfo> roleList = roleInfoProxy.getAll();
		model.addAttribute("roList", roleList);
		model.addAttribute("query", query);
		return "backendUser/userList";
	}
	
	@RequestMapping("/editUserManage")
	public String editUserManage(Model model,Long userId){
		if(null != userId){
			UserInfo userInfo = this.userInfoProxy.findById(userId);
			model.addAttribute("userInfo", userInfo);
		}
		List<Department> departmentList = departmentProxy.getAll();
		model.addAttribute("departmentList", departmentList);
		List<RoleInfo> roleList = roleInfoProxy.getAll();
		model.addAttribute("roleList", roleList);
		return "backendUser/editUserManage";
	}
	
	
	@RequestMapping("/updatePassword")
	@ResponseBody
	public ResultInfo<Boolean> updatePassword(Model model,UserInfo user,String password1,String password2){
		if(null==user){
			return new ResultInfo<Boolean>(new FailInfo("数据异常,user为空"));
		}
		if(!StringUtil.isNullOrEmpty(user.getPassword())
				&&!StringUtil.isNullOrEmpty(password1)
				&&!StringUtil.isNullOrEmpty(password2)){
			if(!password1.equals(password2)){
				return new ResultInfo<Boolean>(new FailInfo("密码不一致"));
			}
		}
		if(StringUtil.isNullOrEmpty(user.getId())){
			return new ResultInfo<Boolean>(new FailInfo("数据异常,id为空"));
		}
		Boolean passIsSafe = PasswordHelper.checkPassWord(password1);
		if(!passIsSafe){
			return new ResultInfo<Boolean>(new FailInfo("密码必须包含字母数字以及特殊字符(10-20位),特殊字符包含~!@#$%^&*()_+=-"));
		}
		try{
			userInfoProxy.updatePassword(user,password1);
			return new ResultInfo<Boolean>(Boolean.TRUE);
		}catch(Exception e){
			return new ResultInfo<Boolean>(new FailInfo("系统出错，修改失败"));
		}
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public ResultInfo<Boolean> save(Model model,UserInfo user,Long roleId,String ckPass){
		if(null==user){
			return new ResultInfo<Boolean>(new FailInfo("数据异常,user为空"));
		}
		if(StringUtil.isNullOrEmpty(user.getLoginName())){
			return new ResultInfo<Boolean>(new FailInfo("数据异常,user为空"));
		}
		if(StringUtil.isNullOrEmpty(user.getUserName())){
			return new ResultInfo<Boolean>(new FailInfo("数据异常,user为空"));
		}
		if(StringUtil.isNullOrEmpty(user.getDepartmentId())){
			return new ResultInfo<Boolean>(new FailInfo("数据异常,user为空"));
		}
		if(StringUtil.isNullOrEmpty(roleId)){
			return new ResultInfo<Boolean>(new FailInfo("数据异常,user为空"));
		}
		if(null == user.getId()){
			if(StringUtil.isNullOrEmpty(user.getPassword())){
				return new ResultInfo<Boolean>(new FailInfo("请输入密码"));
			}
			if(StringUtil.isNullOrEmpty(ckPass)){
				return new ResultInfo<Boolean>(new FailInfo("请输入确认密码"));
			}
			if(!user.getPassword().equals(ckPass)){
				return new ResultInfo<Boolean>(new FailInfo("密码不一致"));
			}
		}
		if(!StringUtil.isNullOrEmpty(user.getPassword())){
			 Boolean passIsSafe = PasswordHelper.checkPassWord(user.getPassword());
//			 RequestContextHolder.getRequestAttributes().setAttribute("passIsSafe", passIsSafe, RequestAttributes.SCOPE_SESSION);
			 if(!passIsSafe){
				 return new ResultInfo<Boolean>(new FailInfo("密码必须包含字母数字以及特殊字符(10-20位),特殊字符包含~!@#$%^&*()_+=-"));
			 }
		}
		try{
			 userInfoProxy.save(user);
			return new ResultInfo<Boolean>(Boolean.TRUE);
		}catch(Exception e){
			return new ResultInfo<Boolean>(new FailInfo("系统出错，修改失败"));
		}
	}
	
	@RequestMapping("/clockUser")
	@ResponseBody
	public ResultInfo<Boolean> clockUser(Model model,Long userId,Integer status){
		if(null==userId){
			return new ResultInfo<Boolean>(new FailInfo("数据异常,user为空"));
		}
		UserInfo userInfo = userInfoProxy.queryById(userId).getData();
		if(null==userInfo){
			return new ResultInfo<Boolean>(new FailInfo("用户不存在"));
		}
		userInfo.setStatus(Constant.DEFAULTED.YES.equals(status)?Constant.DEFAULTED.YES:Constant.DEFAULTED.NO);
		try{
			 userInfoProxy.save(userInfo);
			return new ResultInfo<Boolean>(Boolean.TRUE);
		}catch(Exception e){
			return new ResultInfo<Boolean>(new FailInfo("系统出错，修改失败"));
		}
	}
	
}
