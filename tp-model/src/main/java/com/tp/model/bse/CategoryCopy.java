package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品类别
  */
public class CategoryCopy extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786416L;

	/**id 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**父类别 数据类型bigint(11)*/
	private Long parentId;
	
	/**类别名称 数据类型varchar(64)*/
	private String name;
	
	/**类别编号 数据类型varchar(255)*/
	private String code;
	
	/**级别(1:大类、2:中类、3:小类) 数据类型int(1)*/
	private Integer level;
	
	/**状态 0-无效 1-有效 数据类型tinyint(4)*/
	private Integer status;
	
	/**路径(冗余)，以-连接 数据类型varchar(255)*/
	private String path;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	/**是否颜色管理：0-否、1-是，默认1 数据类型int(1)*/
	private Integer colorAttributeSign;
	
	/**是否尺码管理：0-否、1-是、默认1 数据类型int(1)*/
	private Integer sizeAbbtributeSign;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型bigint(20)*/
	private Long createUserId;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	/**修改人 数据类型bigint(20)*/
	private Long modifyUserId;
	
	
	public Long getId(){
		return id;
	}
	public Long getParentId(){
		return parentId;
	}
	public String getName(){
		return name;
	}
	public String getCode(){
		return code;
	}
	public Integer getLevel(){
		return level;
	}
	public Integer getStatus(){
		return status;
	}
	public String getPath(){
		return path;
	}
	public String getRemark(){
		return remark;
	}
	public Integer getColorAttributeSign(){
		return colorAttributeSign;
	}
	public Integer getSizeAbbtributeSign(){
		return sizeAbbtributeSign;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getCreateUserId(){
		return createUserId;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public Long getModifyUserId(){
		return modifyUserId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setParentId(Long parentId){
		this.parentId=parentId;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setCode(String code){
		this.code=code;
	}
	public void setLevel(Integer level){
		this.level=level;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setPath(String path){
		this.path=path;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setColorAttributeSign(Integer colorAttributeSign){
		this.colorAttributeSign=colorAttributeSign;
	}
	public void setSizeAbbtributeSign(Integer sizeAbbtributeSign){
		this.sizeAbbtributeSign=sizeAbbtributeSign;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setModifyUserId(Long modifyUserId){
		this.modifyUserId=modifyUserId;
	}
}
