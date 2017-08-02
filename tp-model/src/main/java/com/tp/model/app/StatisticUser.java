package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 在会员登录时投递，主要统计登录的会员数和登录结果
  */
public class StatisticUser extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450431557816L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**会员（账号）名称	该参数值投递之前要转换成UTF-8编码字串 数据类型varchar(45)*/
	private String userName;
	
	/**创建记录的时间,  登录的时间. 数据类型datetime*/
	private Date createTime;
	
	/**ret 会员登录状态，登录接口返回的值  0,‘登录成功’;101, '用户不存在';102, '密码错误',
103, '重复登陆', 等 数据类型varchar(100)*/
	private String loginResult;
	
	/**“-1”，是自动登录；“0”,使用户手动输入账号名和密码登录的
 数据类型int(11)*/
	private Integer isAutoLogin;
	
	/**公共参数表 id 数据类型bigint(20)*/
	private Long staBaseId;
	
	
	public Long getId(){
		return id;
	}
	public String getUserName(){
		return userName;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getLoginResult(){
		return loginResult;
	}
	public Integer getIsAutoLogin(){
		return isAutoLogin;
	}
	public Long getStaBaseId(){
		return staBaseId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setUserName(String userName){
		this.userName=userName;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setLoginResult(String loginResult){
		this.loginResult=loginResult;
	}
	public void setIsAutoLogin(Integer isAutoLogin){
		this.isAutoLogin=isAutoLogin;
	}
	public void setStaBaseId(Long staBaseId){
		this.staBaseId=staBaseId;
	}
}
