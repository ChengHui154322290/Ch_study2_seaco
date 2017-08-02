package com.tp.model.cmt;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 评论差评词
  */
public class NegativeCommentWords extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450404446075L;

	/**主键ID 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**差评词 数据类型varchar(50)*/
	private String negativeWord;
	
	/**备注 数据类型varchar(200)*/
	private String remark;
	
	/**状态0：无效1有效 数据类型tinyint(1)*/
	private Integer status;
	
	/**创建人ID 数据类型bigint(18)*/
	private Long createUserId;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改人ID 数据类型bigint(18)*/
	private Long updateUserId;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	
	public Long getId(){
		return id;
	}
	public String getNegativeWord(){
		return negativeWord;
	}
	public String getRemark(){
		return remark;
	}
	public Integer getStatus(){
		return status;
	}
	public Long getCreateUserId(){
		return createUserId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getUpdateUserId(){
		return updateUserId;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setNegativeWord(String negativeWord){
		this.negativeWord=negativeWord;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateUserId(Long updateUserId){
		this.updateUserId=updateUserId;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
}
