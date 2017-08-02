package com.tp.model.cmt;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 评论图片信息
  */
public class ReviewImg extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450404446076L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**评论ID 数据类型bigint(20)*/
	private Long reviewId;
	
	/**评论图片路径 数据类型varchar(255)*/
	private String path;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getReviewId(){
		return reviewId;
	}
	public String getPath(){
		return path;
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
	public void setPath(String path){
		this.path=path;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
