package com.tp.model.cmt;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 评论标签统计信息表
  */
public class ReviewTagsStatis extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450404446078L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**商品spu 数据类型varchar(15)*/
	private String spu;
	
	/**商品pridid 数据类型varchar(20)*/
	private String prdid;
	
	/**标签 数据类型varchar(20)*/
	private String tagName;
	
	/**标签被使用次数 数据类型int(11)*/
	private Integer times;
	
	
	public Long getId(){
		return id;
	}
	public String getSpu(){
		return spu;
	}
	public String getPrdid(){
		return prdid;
	}
	public String getTagName(){
		return tagName;
	}
	public Integer getTimes(){
		return times;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSpu(String spu){
		this.spu=spu;
	}
	public void setPrdid(String prdid){
		this.prdid=prdid;
	}
	public void setTagName(String tagName){
		this.tagName=tagName;
	}
	public void setTimes(Integer times){
		this.times=times;
	}
}
