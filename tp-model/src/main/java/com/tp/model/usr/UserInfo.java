package com.tp.model.usr;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class UserInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451892616621L;

	/** 数据类型bigint(14)*/
	@Id
	private Long id;
	
	/**登录名 数据类型varchar(50)*/
	private String loginName;
	
	/**用户名 数据类型varchar(50)*/
	private String userName;
	
	/**密码 数据类型varchar(100)*/
	private String password;
	
	/**登录盐 数据类型varchar(100)*/
	private String salt;
	
	/**部门id 数据类型bigint(20)*/
	private Long departmentId;
	
	/**角色id 数据类型bigint(20)*/
	private Long roleId;
	
	/**用户手机号 数据类型varchar(20)*/
	private String mobile;
	
	/**用户email 数据类型varchar(100)*/
	private String email;
	
	/**创建人id 数据类型varchar(32)*/
	private String createUser;
	
	/**修改人id 数据类型varchar(32)*/
	private String updateUser;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**最后一次登录时间 数据类型datetime*/
	private Date lastLoginTime;
	
	/**最后一次登录ip 数据类型varchar(50)*/
	private String lastLoginIp;
	
	/**用户状态 数据类型tinyint(1)*/
	private Integer status;
	
	@Virtual
	private String departmentName;
	/**
	 * 冗余数据，角色名称
	 */
	@Virtual
	private String roleName;
	@Virtual
	/** 用户扩展信息 */
	private UserDetail userDetail;
	@Virtual
	public List<SysMenu> sysMenuList;
	
	public String getCredentialsSalt() {
        return loginName + salt;
    }
	
	public Long getId(){
		return id;
	}
	public String getLoginName(){
		return loginName;
	}
	public String getUserName(){
		return userName;
	}
	public String getPassword(){
		return password;
	}
	public String getSalt(){
		return salt;
	}
	public Long getDepartmentId(){
		return departmentId;
	}
	public Long getRoleId(){
		return roleId;
	}
	public String getMobile(){
		return mobile;
	}
	public String getEmail(){
		return email;
	}
	public String getCreateUser(){
		return createUser;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public Date getLastLoginTime(){
		return lastLoginTime;
	}
	public String getLastLoginIp(){
		return lastLoginIp;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setLoginName(String loginName){
		this.loginName=loginName;
	}
	public void setUserName(String userName){
		this.userName=userName;
	}
	public void setPassword(String password){
		this.password=password;
	}
	public void setSalt(String salt){
		this.salt=salt;
	}
	public void setDepartmentId(Long departmentId){
		this.departmentId=departmentId;
	}
	public void setRoleId(Long roleId){
		this.roleId=roleId;
	}
	public void setMobile(String mobile){
		this.mobile=mobile;
	}
	public void setEmail(String email){
		this.email=email;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setLastLoginTime(Date lastLoginTime){
		this.lastLoginTime=lastLoginTime;
	}
	public void setLastLoginIp(String lastLoginIp){
		this.lastLoginIp=lastLoginIp;
	}
	public UserDetail getUserDetail() {
		return userDetail;
	}
	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public List<SysMenu> getSysMenuList() {
		return sysMenuList;
	}

	public void setSysMenuList(List<SysMenu> sysMenuList) {
		this.sysMenuList = sysMenuList;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
