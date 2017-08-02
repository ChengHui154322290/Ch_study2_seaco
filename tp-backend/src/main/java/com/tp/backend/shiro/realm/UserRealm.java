package com.tp.backend.shiro.realm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tp.dto.common.ResultInfo;
import com.tp.model.usr.Department;
import com.tp.model.usr.RoleInfo;
import com.tp.model.usr.RoleMenu;
import com.tp.model.usr.SysMenu;
import com.tp.model.usr.SysMenuLimit;
import com.tp.model.usr.UserInfo;
import com.tp.model.usr.UserRole;
import com.tp.proxy.usr.DepartmentProxy;
import com.tp.proxy.usr.RoleInfoProxy;
import com.tp.proxy.usr.RoleMenuLimitProxy;
import com.tp.proxy.usr.RoleMenuProxy;
import com.tp.proxy.usr.SysMenuLimitProxy;
import com.tp.proxy.usr.SysMenuProxy;
import com.tp.proxy.usr.UserInfoProxy;
import com.tp.proxy.usr.UserRoleProxy;
import com.tp.service.usr.IRoleMenuService;
import com.tp.service.usr.ISysMenuService;
import com.tp.util.StringUtil;

/**
 * <p>User: szy
 */
public class UserRealm extends AuthorizingRealm {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	 @Autowired
    private UserInfoProxy userInfoProxy;

    @Autowired
    private UserRoleProxy userRoleProxy;
    
    @Autowired
    private RoleInfoProxy roleInfoProxy;
    
    @Autowired
    private RoleMenuProxy roleMenuProxy;
    
    @Autowired
    private SysMenuProxy sysMenuProxy;
    @Autowired
    private RoleMenuLimitProxy roleMenuLimitProxy;
    
    @Autowired
    private SysMenuLimitProxy sysMenuLimitProxy;
    @Autowired
    private DepartmentProxy departmentProxy;
    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private IRoleMenuService roleMenuService;
    
    /**
     *  @principals 用户凭证 ,登录成功后的用户信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserInfo user = (UserInfo)principals.getPrimaryPrincipal();
        
    	Long roleId = userRoleProxy.getRoleIdByUserId(user.getId());
    	
    	SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
    	if(null != roleId){
    		List<Long> sysMenuIdsList = roleMenuProxy.selectSysMenuIds(roleId);
    		logger.info(sysMenuIdsList.toString());
    		if(null != sysMenuIdsList && !sysMenuIdsList.isEmpty()){
    			List<SysMenuLimit> list = null;
    			if(null!=sysMenuIdsList&&!sysMenuIdsList.isEmpty()){
    				list = new ArrayList<SysMenuLimit>();
    				for (Long menuId : sysMenuIdsList) {
        				List<SysMenuLimit> smList = sysMenuLimitProxy.findBySysMenuId(menuId);
        				list.addAll(smList);
    				}
    			}
    			
    			Set<String> perSet = new HashSet<String>();
    			for (SysMenuLimit menu : list) {
    				if(StringUtil.isNullOrEmpty(menu.getPermission())) continue;
    				perSet.add(menu.getPermission());
    				logger.info("授权:"+menu.getPermission());
				}
    			authorizationInfo.setStringPermissions(perSet);
    		}
    	}
        return authorizationInfo;
    }

    /**
     * @token 用户登录信息(令牌)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String loginName = (String)token.getPrincipal();
        logger.info("[BEGIN]用户登录:"+loginName);

        UserInfo user = null;
		try {
			user = userInfoProxy.findByLoginName(loginName);
			logger.info("[SUCCESS]登录时获取用户信息:"+user.toString());
			Department dept = departmentProxy.queryById(user.getDepartmentId()).getData();
			if(null != dept)user.setDepartmentName(dept.getName());
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userId", user.getId());
			ResultInfo<UserRole> roleResultInfo = userRoleProxy.queryUniqueByParams(params);
			if(roleResultInfo.success){
				UserRole userRole = roleResultInfo.getData(); 
				if(userRole!=null){
					 RoleInfo role = roleInfoProxy.queryById(userRole.getRoleId()).getData();
					 if(null!=role){
						 user.setRoleName(role.getName());
					 }
					 List<Long> sysMenuIds = selectSysMenuIds(role.getId());
		    		 if(CollectionUtils.isNotEmpty(sysMenuIds)){
		    			List<SysMenu> list = findByIds(sysMenuIds);
		    			user.setSysMenuList(list);
		    		 }
				}
			}
			logger.info("[SUCCESS]登录时获取用户信息菜单信息:{}",user.getUserName());
		} catch (Exception e) {
			logger.error("[ERROR]登录时获取用户信息错误:{}",e.getMessage());
		}
        if(null == user) {
            throw new UnknownAccountException();//没找到帐号
        }
        logger.info("[END]获取用户信息:"+user.toString());

        if(Boolean.FALSE.equals(user.getStatus())) {
            throw new LockedAccountException(); //帐号锁定
        }
    	
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user, //用户名
                user.getPassword(), //密码
                ByteSource.Util.bytes(user.getCredentialsSalt()),//salt=loginName+salt
                getName()  //realm name
        );
        
        return authenticationInfo;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
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
