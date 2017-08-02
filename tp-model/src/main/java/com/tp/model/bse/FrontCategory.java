package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 前台类目表
  */
public class FrontCategory extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786419L;

	/**类目id 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**类目名称 数据类型varchar(20)*/
	private String name;
	
	/**类目编码 数据类型varchar(50)*/
	private String code;
	
	/**类目级别:1-一级类目,2-二级类目,3-三级类目 数据类型tinyint(1)*/
	private Integer level;
	
	/**状态:0-有效，1-无效 数据类型tinyint(2)*/
	private Integer status;
	
	/**是否突出展示：0-是，1-否 数据类型tinyint(1)*/
	private Integer isHighlight;
	
	/** 数据类型varchar(1000)*/
	private String logoUrl;
	
	/**父类目ID 数据类型bigint(11)*/
	private Long parentId;
	
	/**顺序 数据类型tinyint(4)*/
	private Integer seq;
	
	/**是否发布:0-发布，1-否 数据类型tinyint(1)*/
	private Integer isPublish;
	
	/**创建人 数据类型bigint(11)*/
	private Long createUserId;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改人 数据类型bigint(11)*/
	private Long modifyUserId;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	
	public Long getId(){
		return id;
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
	public Integer getIsHighlight(){
		return isHighlight;
	}
	public String getLogoUrl(){
		return logoUrl;
	}
	public Long getParentId(){
		return parentId;
	}
	public Integer getSeq(){
		return seq;
	}
	public Integer getIsPublish(){
		return isPublish;
	}
	public Long getCreateUserId(){
		return createUserId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getModifyUserId(){
		return modifyUserId;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public void setId(Long id){
		this.id=id;
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
	public void setIsHighlight(Integer isHighlight){
		this.isHighlight=isHighlight;
	}
	public void setLogoUrl(String logoUrl){
		this.logoUrl=logoUrl;
	}
	public void setParentId(Long parentId){
		this.parentId=parentId;
	}
	public void setSeq(Integer seq){
		this.seq=seq;
	}
	public void setIsPublish(Integer isPublish){
		this.isPublish=isPublish;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyUserId(Long modifyUserId){
		this.modifyUserId=modifyUserId;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
}
