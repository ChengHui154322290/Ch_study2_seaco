package com.tp.model.usr;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.CmmConstant;
import com.tp.common.vo.Constant;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 权限信息表
  */
public class ResourceInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1441690271330L;

	 /**权限id 数据类型int(8)*/
	@Id
	private Integer resourceId;
	
	 /**权限名称 数据类型varchar(32)*/
	private String resourceName;
	
	 /**权限地址 数据类型varchar(128)*/
	private String resourceUrl;
	
	 /**备注 数据类型varchar(256)*/
	private String remark;
	
	 /**排序 数据类型int(8)*/
	private Integer sortNo;
	
	 /**组别ID 数据类型int(8)*/
	private Integer modelType;
	
	 /**权限类型(0-菜单，1-元素） 数据类型tinyint(1)*/
	private Integer resourceType;
	
	 /**上级菜单权限ID 数据类型int(8)*/
	private Integer parentId;
	
	 /**创建时间 数据类型datetime*/
	private Date createTime;
	
	 /**创建者 数据类型varchar(32)*/
	private String createUser;
	
	 /**更新时间 数据类型datetime*/
	private Date updateTime;
	
	 /**更新者 数据类型varchar(32)*/
	private String updateUser;
	
	@Virtual
	private String parentName;
	@Virtual
	private Integer selected=Constant.SELECTED.NO;
	
	public String getSelectedCn(){
		if(Constant.SELECTED.YES.equals(selected)){
			return "是";
		}
		return "否";
	}
	
	public String getModelTypeCn(){
		return CmmConstant.RESOURCE_MODEL_TYPE.getName(modelType);
	}
	
	public String getResourceTypeCn(){
		return CmmConstant.RESOURCE_TYPE.getName(resourceType);
	}
	public Integer getResourceId(){
		return resourceId;
	}
	public String getResourceName(){
		return resourceName;
	}
	public String getResourceUrl(){
		return resourceUrl;
	}
	public String getRemark(){
		return remark;
	}
	public Integer getSortNo(){
		return sortNo;
	}
	public Integer getModelType(){
		return modelType;
	}
	public Integer getResourceType(){
		return resourceType;
	}
	public Integer getParentId(){
		return parentId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setResourceId(Integer resourceId){
		this.resourceId=resourceId;
	}
	public void setResourceName(String resourceName){
		this.resourceName=resourceName;
	}
	public void setResourceUrl(String resourceUrl){
		this.resourceUrl=resourceUrl;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setSortNo(Integer sortNo){
		this.sortNo=sortNo;
	}
	public void setModelType(Integer modelType){
		this.modelType=modelType;
	}
	public void setResourceType(Integer resourceType){
		this.resourceType=resourceType;
	}
	public void setParentId(Integer parentId){
		this.parentId=parentId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Integer getSelected() {
		return selected;
	}

	public void setSelected(Integer selected) {
		this.selected = selected;
	}
}
