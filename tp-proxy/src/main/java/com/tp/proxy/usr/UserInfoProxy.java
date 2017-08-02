package com.tp.proxy.usr;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.model.usr.Department;
import com.tp.model.usr.QueryUserDto;
import com.tp.model.usr.RoleInfo;
import com.tp.model.usr.UserDetail;
import com.tp.model.usr.UserInfo;
import com.tp.model.usr.UserRole;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.usr.IDepartmentService;
import com.tp.service.usr.IRoleInfoService;
import com.tp.service.usr.IUserDetailService;
import com.tp.service.usr.IUserInfoService;
import com.tp.service.usr.IUserRoleService;
import com.tp.util.StringUtil;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class UserInfoProxy extends BaseProxy<UserInfo>{

	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
    private PasswordHelper passwordHelper;
    
    @Autowired
    private IUserRoleService urService;
    
    @Autowired
    private IUserDetailService bUserDetailService;
    
    @Autowired
    private IDepartmentService deptService;
    
    @Autowired
    private IRoleInfoService roleInfoService;
    
	@Override
	public IBaseService<UserInfo> getService() {
		return userInfoService;
	}
    
    public void refreshUser(UserInfo user){
    	UserInfo u = new UserInfo();
    	u.setId(user.getId());
    	u.setLastLoginTime(new Date());
    	u.setLastLoginIp("localhost");
    	userInfoService.save(u);
    }
    
	/**
	 * 
	 * <pre>
	 * 根据登录名查找用户
	 * </pre>
	 *
	 * @param loginName
	 * @return
	 */
	public UserInfo findByLoginName(String loginName) throws Exception{
		return userInfoService.findByLoginName(loginName);
	}
	
	/**
	 * 
	 * <pre>
	 * 保存用户
	 * </pre>
	 *
	 * @param user
	 * @throws Exception 
	 */
	public Long save(UserInfo user) throws Exception{
		UserInfo nowUser = UserHandler.getUser();
		if(null == user.getId()){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("loginName", user.getLoginName());
			List<UserInfo> list = userInfoService.queryByParam(params);
			if(null != list && !list.isEmpty())return null;
			user.setCreateUser(null!=nowUser?nowUser.getUserName():null);
			user.setStatus(Constant.DEFAULTED.YES);
		}
		if(user.getPassword()!=null && StringUtil.isBlank(user.getPassword())){
			user.setPassword(null);
		}
		if(!StringUtil.isNullOrEmpty(user.getPassword())) passwordHelper.encryptPassword(user);
		user.setUpdateUser(null!=nowUser?nowUser.getUserName():null);
		Long userId = userInfoService.save(user);
		UserDetail userDetail = new UserDetail();
		userDetail.setUserId(userId);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		List<UserDetail> list = bUserDetailService.queryByParam(params);
		if(null != list && !list.isEmpty())userDetail = list.get(0); 
		
		userDetail.setEmail(user.getEmail());
		userDetail.setMobile(user.getMobile());

		userDetail = bUserDetailService.save(userDetail);
		
		if(nowUser!=null && null!=user.getId()&&nowUser.getId().intValue() == user.getId()){
			nowUser.setUserDetail(userDetail);
		}
		
		return userId;
	}
	
	/**
	 * 
	 * <pre>
	 * 修改密码
	 * </pre>
	 *
	 * @param user
	 * @throws Exception 
	 */
	public Long updatePassword(UserInfo user,String password) throws Exception{
		if(user.getPassword()!=null && StringUtil.isBlank(user.getPassword())){
			user.setPassword(null);
		}
		UserInfo nowUser = UserHandler.getUser();
		if(!StringUtil.isNullOrEmpty(user.getPassword())){
			String newPwd = passwordHelper.getPass(user.getPassword(),nowUser);
			
			if(!newPwd.equals(nowUser.getPassword())) throw new Exception("原密码错误");
			nowUser.setPassword(password);
			passwordHelper.encryptPassword(nowUser);
			
		}
		return userInfoService.save(nowUser);
	}
	
	
	public void updateUserInfo(UserDetail userDetail) throws Exception{
		UserInfo nowUser = UserHandler.getUser();
		userDetail.setUserId(nowUser.getId());
		userDetail = bUserDetailService.save(userDetail);
		nowUser.setUserDetail(userDetail);
		
		
		nowUser.setMobile(userDetail.getMobile());
		nowUser.setEmail(userDetail.getEmail());
		userInfoService.save(nowUser);
		
	}
	
	public PageInfo<UserInfo> getAllPageInfo(Integer pageNo,Integer pageSize,QueryUserDto query){
		if(pageNo==null){
			pageNo = DAOConstant.DEFUALT_PAGE;
		}
		if(pageSize==null){
			pageSize = DAOConstant.DEFUALT_SIZE;
		}
		UserInfo userDO = new UserInfo();
		userDO.setLoginName(query.getLoginName());
		userDO.setUserName(query.getUserName());
		userDO.setDepartmentId(query.getDepartmentId());
		userDO.setRoleId(query.getRoleId());
		
		PageInfo<UserInfo> page = userInfoService.findListPageInfoByParamsForFazzy(userDO, pageNo, pageSize);
		
		List<UserInfo> list = page.getRows();
		
		for (UserInfo user : list) {
			Long departmentId = user.getDepartmentId();
			Department dept = deptService.queryById(departmentId);
			if(null != dept)user.setDepartmentName(dept.getName());
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userId", user.getId());
			List<UserRole> urList = urService.queryByParam(params);
			if(null != urList && !urList.isEmpty()){
				 Long roleId = urList.get(0).getRoleId();
				 if(null != roleId){
					 RoleInfo role = roleInfoService.queryById(roleId);
					 if(null!=role) user.setRoleName(role.getName());
				 }
			}
		}
		
		return page;
	}
	
	
	public void remove(Long userId){
		UserRole urDo = new UserRole();
		urDo.setUserId(userId);
		UserInfo userDO = new UserInfo();
		userDO.setId(userId);
		userDO.setStatus(Constant.DEFAULTED.YES);
		userDO.setUpdateUser(UserHandler.getUser().getUserName());
		userInfoService.save(userDO);
		
	}
	
	public UserInfo findById(Long userId){
		return userInfoService.queryById(userId);
	}
}
