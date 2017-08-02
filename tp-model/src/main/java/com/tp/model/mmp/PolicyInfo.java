package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 限购政策
  */
public class PolicyInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579108L;

	/**id 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**根据注册时间 数据类型int(1)*/
	private Integer byRegisterTime;
	
	/**根据uid限制 数据类型int(1)*/
	private Integer byUid;
	
	/**根据ip限制 数据类型int(1)*/
	private Integer byIp;
	
	/**根据手机号限制 数据类型int(1)*/
	private Integer byMobile;
	
	private Integer byTopic;
	
	/**注册时间早于 数据类型date*/
	private Date earlyThanTime;
	
	/**注册时间晚于 数据类型date*/
	private Date lateThanTime;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	
	public Long getId(){
		return id;
	}
	public Integer getByRegisterTime(){
		return byRegisterTime;
	}
	public Integer getByUid(){
		return byUid;
	}
	public Integer getByIp(){
		return byIp;
	}
	public Integer getByMobile(){
		return byMobile;
	}
	public Date getEarlyThanTime(){
		return earlyThanTime;
	}
	public Date getLateThanTime(){
		return lateThanTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setByRegisterTime(Integer byRegisterTime){
		this.byRegisterTime=byRegisterTime;
	}
	public void setByUid(Integer byUid){
		this.byUid=byUid;
	}
	public void setByIp(Integer byIp){
		this.byIp=byIp;
	}
	public void setByMobile(Integer byMobile){
		this.byMobile=byMobile;
	}
	public void setEarlyThanTime(Date earlyThanTime){
		this.earlyThanTime=earlyThanTime;
	}
	public void setLateThanTime(Date lateThanTime){
		this.lateThanTime=lateThanTime;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public Integer getByTopic() {
		return byTopic;
	}
	public void setByTopic(Integer byTopic) {
		this.byTopic = byTopic;
	}
}
