package com.tp.model.cmt;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 评论标签信息
  */
public class ReviewTags extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450404446077L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**评论ID 数据类型bigint(20)*/
	private Long reviewId;
	
	/**标签 数据类型varchar(16)*/
	private String name;
	
	/**标签来源0-分类 1-商品 2-自定义 数据类型tinyint(4)*/
	private Integer type;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getReviewId(){
		return reviewId;
	}
	public String getName(){
		return name;
	}
	public Integer getType(){
		return type;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setReviewId(Long reviewId){
		this.reviewId=reviewId;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
