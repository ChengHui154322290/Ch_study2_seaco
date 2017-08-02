/**
 * 
 */
package com.tp.model.mem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tp.model.BaseDO;

/**
 * @author Administrator
 * 短信白名单
 */
public class SmsWhiteInfo extends BaseDO{
	private static final long serialVersionUID = 1L;

	/** 状态：0关闭1开启 */
	private Integer status;
	
	/** 白名单列表 */
	private List<String> mobileList;
	
	/** 创建时间 */
	private Date createTime;
	
	/** 更新时间 */
	private Date updateTime;
	
	public SmsWhiteInfo() {
		status = 0;
		mobileList = new ArrayList<>();
		createTime = new Date();
		updateTime = new Date();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<String> getMobileList() {
		return mobileList;
	}

	public void setMobileList(List<String> mobileList) {
		this.mobileList = mobileList;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
