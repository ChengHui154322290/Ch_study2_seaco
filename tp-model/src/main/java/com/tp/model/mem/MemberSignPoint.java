package com.tp.model.mem;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
import com.tp.util.DateUtil;
/**
  * @author szy
  * 会员签到获取积分日志表
  */
public class MemberSignPoint extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1473303924127L;

	/**会员签到ID 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**会员ID 数据类型bigint(11)*/
	private Long memberId;
	
	/**签到日期 数据类型datetime*/
	private Date signDate;
	
	/**签到连续天数 数据类型 int(6)*/
	private Integer days;
	
	/**获得到的积分 数据类型int(4)*/
	private Integer point;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**是否已签到*/
	@Virtual
	private Boolean signed;
	@Virtual
	private Boolean enabled=Boolean.FALSE;//是否可签到
	
	public String getSignDateStr(){
		if(signDate!=null){
			return DateUtil.formatDate(signDate,"yyyyMMdd");
		}
		return "";
	}
	
	public Long getId(){
		return id;
	}
	public Long getMemberId(){
		return memberId;
	}
	public Date getSignDate(){
		return signDate;
	}
	public Integer getPoint(){
		return point;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setMemberId(Long memberId){
		this.memberId=memberId;
	}
	public void setSignDate(Date signDate){
		this.signDate=signDate;
	}
	public void setPoint(Integer point){
		this.point=point;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public Boolean getSigned() {
		return signed;
	}
	public void setSigned(Boolean signed) {
		this.signed = signed;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
