package com.tp.model.usr;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 权限表
  */
public class SysMenuLimit extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451892616621L;

	/**权限d 数据类型bigint(14)*/
	@Id
	private Long id;
	
	/**菜单id 数据类型bigint(15)*/
	private Long sysMenuId;
	
	/**权限描述 数据类型varchar(100)*/
	private String permission;
	
	/**状态 数据类型tinyint(4)*/
	private Integer status;
	
	@Virtual
	private String desc;
	
	public SysMenuLimit() {
	}
	
	public SysMenuLimit(long sysMenuId, String permission, Integer status) {
		this.sysMenuId = sysMenuId;
		this.permission =permission;
		this.status = status;
	}
	public Long getId(){
		return id;
	}
	public Long getSysMenuId(){
		return sysMenuId;
	}
	public String getPermission(){
		return permission;
	}
	public Integer getStatus(){
		return status;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSysMenuId(Long sysMenuId){
		this.sysMenuId=sysMenuId;
	}
	public void setPermission(String permission){
		this.permission=permission;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
