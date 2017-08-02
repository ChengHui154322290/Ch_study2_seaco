package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.vo.app.VersionConstant;
import com.tp.exception.AppServiceException;
import com.tp.model.BaseDO;
import com.tp.util.StringUtil;
/**
  * @author szy
  * 
  */
public class VersionInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450432186418L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**版本编码 数据类型varcher(20)*/
	private String code;
	
	/**版本名称 数据类型varcher(20)*/
	private String name;
	
	/**版本号 数据类型varcher(10)*/
	private String version;
	
	/**平台 数据类型tinyint(2)*/
	private Integer platform;
	
	/**下载地址 数据类型varcher(100)*/
	private String downUrl;
	
	/**发布时间 数据类型datetime*/
	private Date pushTime;
	
	/**更新内容 数据类型varcher(1000)*/
	private String remark;
	
	/**是否最新0否 1是 数据类型tinyint(1)*/
	private Integer isNew;
	
	/**状态0冻结 1发布 数据类型tinyint(1)*/
	private Integer status;
	
	/**是否删除0否1是 数据类型tinyint(1)*/
	private Integer isDel;
	
	/**创建人 数据类型varcher(20)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改人 数据类型varcher(20)*/
	private String modifyUser;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	public String getPlatformDesc(){
		return VersionConstant.VERSION_PLATFORM.getDesc(platform);
	}
	
	public String getStatusDesc(){
		return VersionConstant.VERSION_STATUS.getDesc(status);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	public Integer getIsNew() {
		return isNew;
	}

	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}

	public String getDownUrl() {
		return downUrl;
	}

	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}

	public Date getPushTime() {
		return pushTime;
	}

	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public void validate(VersionInfo versionInfo){
		if(StringUtil.isEmpty(versionInfo.getVersion())) throw new AppServiceException("版本号不能为空");
		if(StringUtil.isNull(versionInfo.getPlatform())) throw new AppServiceException("平台不能为空");
	}
}
