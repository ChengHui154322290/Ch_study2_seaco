package com.tp.model.cms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 公告资讯表
  */
public class AnnounceInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**标题 数据类型varchar(50)*/
	private String title;
	
	/**内容 数据类型varchar(3000)*/
	private String content;
	
	/**状态 数据类型int(10)*/
	private Integer status;
	
	/**创建人 数据类型int(10)*/
	private Integer creater;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改人 数据类型int(10)*/
	private Integer modifier;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	/** 数据类型int(10)*/
	private Integer sort;
	
	/**页面位置 数据类型varchar(10)*/
	private String type;
	
	/** 数据类型varchar(128)*/
	private String link;
	
	
	public Long getId(){
		return id;
	}
	public String getTitle(){
		return title;
	}
	public String getContent(){
		return content;
	}
	public Integer getStatus(){
		return status;
	}
	public Integer getCreater(){
		return creater;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Integer getModifier(){
		return modifier;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public Integer getSort(){
		return sort;
	}
	public String getType(){
		return type;
	}
	public String getLink(){
		return link;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setTitle(String title){
		this.title=title;
	}
	public void setContent(String content){
		this.content=content;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreater(Integer creater){
		this.creater=creater;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifier(Integer modifier){
		this.modifier=modifier;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setSort(Integer sort){
		this.sort=sort;
	}
	public void setType(String type){
		this.type=type;
	}
	public void setLink(String link){
		this.link=link;
	}
}
