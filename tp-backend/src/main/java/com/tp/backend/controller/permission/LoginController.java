package com.tp.backend.controller.permission;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.backend.shiro.filter.ShiroUPToken;
import com.tp.common.vo.BackendConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.usr.Department;
import com.tp.model.usr.RoleInfo;
import com.tp.model.usr.RoleMenu;
import com.tp.model.usr.SysMenu;
import com.tp.model.usr.UserInfo;
import com.tp.model.usr.UserRole;
import com.tp.proxy.usr.DepartmentProxy;
import com.tp.proxy.usr.PasswordHelper;
import com.tp.proxy.usr.RoleInfoProxy;
import com.tp.proxy.usr.UserRoleProxy;
import com.tp.service.usr.IRoleMenuService;
import com.tp.service.usr.ISysMenuService;
import com.tp.util.StringUtil;


@Controller
public class LoginController extends AbstractBaseController{
	private final static Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private UserRoleProxy userRoleProxy;
	@Autowired
	private RoleInfoProxy roleInfoProxy;
	@Autowired
	private DepartmentProxy departmentProxy;
	@Autowired
	private ISysMenuService sysMenuService;
	@Autowired
	private IRoleMenuService roleMenuService;
	
	@RequestMapping("/login")
	public String login() throws Exception{		
		return "login";
	}
	
	/**
	 * 
	 * <pre>
	 *  登录
	 * </pre>
	 *
	 * @param request 
	 * @param loginName 登录名
	 * @param password 密码
	 * @param rememberMe 是否记住我
	 * @param securityCode 验证码
	 * @return
	 */
	@RequestMapping(value = "doLogin")
	@ResponseBody
	public ResultInfo<Boolean> doLogin(Model model,String loginName,String password,Boolean rememberMe) {
		if (StringUtil.isNullOrEmpty(loginName)){
			return new ResultInfo<Boolean>(new FailInfo("请输入用户名"));
		}
		if(StringUtil.isNullOrEmpty(password)){
			return new ResultInfo<Boolean>(new FailInfo("请输入密码"));
		}
		ResultInfo<Boolean> resultInfo = loginIn(loginName, password,false);
		if(!resultInfo.success){
			return resultInfo;
		}
		Boolean passIsSafe = PasswordHelper.checkPassWord(password);
		RequestContextHolder.getRequestAttributes().setAttribute("passIsSafe",passIsSafe, RequestAttributes.SCOPE_SESSION);
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}
	
	 /**
		 * 登出
		 * 
		 * @return
		 * @throws Exception 
		 */
		@RequestMapping(value = "logout")
		public String logout(HttpServletRequest request) throws Exception {
			
			request.removeAttribute(BackendConstant.SessionKey.USER.getValue());
			Subject sub = SecurityUtils.getSubject();
			sub.logout();
			return "redirect:login";
		}
		
	public ResultInfo<Boolean> loginIn(String loginName, String password, boolean rememberMe) {
		Subject subject = SecurityUtils.getSubject();
		//创建用户名和密码的令牌
		try{
		ShiroUPToken token = new ShiroUPToken(loginName,password);
		token.setRememberMe(rememberMe);
		logger.info("登录:{},{}",loginName,loginName);
		subject.login(token);
		logger.info("登录结束:{},{}",loginName,loginName);
		}catch (ExcessiveAttemptsException e) {
			return new ResultInfo<Boolean>(new FailInfo(e));
		}catch (LockedAccountException ex){
			return new ResultInfo<Boolean>(new FailInfo("账号已被锁定"));
		}catch (DisabledAccountException ex){
			return new ResultInfo<Boolean>(new FailInfo("账号已被禁用"));
		}catch (UnknownAccountException ex) {	
			return new ResultInfo<Boolean>(new FailInfo("用户名或密码错误"));
		} catch (IncorrectCredentialsException ex) {
			return new ResultInfo<Boolean>(new FailInfo("用户名或密码错误"));
		}catch (AuthenticationException e) {
			return new ResultInfo<Boolean>(new FailInfo(e));
		}catch(Exception e){
			return new ResultInfo<Boolean>(new FailInfo(e));
		}
		UserInfo user = (UserInfo) subject.getPrincipal(); 
		
		Department dept = departmentProxy.queryById(user.getDepartmentId()).getData();
		if(null != dept)user.setDepartmentName(dept.getName());
		
		UserRole urDo = new UserRole();
		if(null != urDo)urDo.setUserId(user.getId());
		
		List<UserRole> urList = userRoleProxy.queryByObject(urDo).getData();
		if(null != urList && !urList.isEmpty()){
			 Long roleId = urList.get(0).getRoleId();
			 if(null != roleId){
				 RoleInfo role = roleInfoProxy.queryById(roleId).getData();
				 if(null!=role) user.setRoleName(role.getName());
				 
				 List<Long> sysMenuIds = selectSysMenuIds(role.getId());
		        	
		    		if(null != sysMenuIds && !sysMenuIds.isEmpty()){
		    			List<SysMenu> list = findByIds(sysMenuIds);
		    			user.setSysMenuList(list);
		    		}
		    		
			 }
		}
		
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}
	public List<SysMenu> findByIds(List<Long> ids){
		return sysMenuService.selectByIds(ids);
	}
	
	public List<Long> selectSysMenuIds(Long roleId){
		RoleMenu rDo = new RoleMenu();
		rDo.setRoleId(roleId);
		return roleMenuService.selectSysMenuIds(rDo);
		
	}
}
