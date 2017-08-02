package com.tp.model.cmt;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 评论回复信息表
  */
public class ReviewReply extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450404446076L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**评论id 数据类型bigint(20)*/
	private Long reviewId;
	
	/**回复内容100个汉字或200个英文 数据类型varchar(200)*/
	private String content;
	
	/**回复类型 0-管理员（西客） 1-普通会员 数据类型tinyint(4)*/
	private Integer replyType;
	
	/**是否被删除 0-否 1-是 数据类型tinyint(4)*/
	private Integer isDel;
	
	/**回复用户id，type为0时表示管理员ID，为1时表示普通用户id 数据类型bigint(20)*/
	private Long createUserId;
	
	/**回复时间 数据类型datetime*/
	private Date createTime;
	
	/**最后修改人id 数据类型bigint(20)*/
	private Long modifyUserId;
	
	/**最后修改时间 数据类型datetime*/
	private Date modifyTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getReviewId(){
		return reviewId;
	}
	public String getContent(){
		return content;
	}
	public Integer getReplyType(){
		return replyType;
	}
	public Integer getIsDel(){
		return isDel;
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
	public void setReviewId(Long reviewId){
		this.reviewId=reviewId;
	}
	public void setContent(String content){
		this.content=content;
	}
	public void setReplyType(Integer replyType){
		this.replyType=replyType;
	}
	public void setIsDel(Integer isDel){
		this.isDel=isDel;
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
