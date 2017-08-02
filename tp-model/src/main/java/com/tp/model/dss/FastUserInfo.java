package com.tp.model.dss;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.FastConstant;
import com.tp.common.vo.Constant.ENABLED;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 速购人员信息表
  */
public class FastUserInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1475202399618L;

	/**速购用户编号 数据类型bigint(10)*/
	@Id
	private Long fastUserId;
	
	/**用户姓名 数据类型varchar(16)*/
	private String userName;
	
	/**用户手机 数据类型varchar(16)*/
	private String mobile;
	
	/**关联仓库（店铺） 数据类型bigint(14)*/
	private Long warehouseId;
	
	/**用户类型:1-店铺管理人员，2-店铺员工，3-关联的速购人员 数据类型tinyint(1)*/
	private Integer userType;
	
	/**店铺类型:1-速购,2-团购券*/
	private Integer shopType;
	/**是否可使用：1-可使用，0-不可使用 数据类型tinyint(1)*/
	private Integer enabled;
	
	/**备注 数据类型varchar(128)*/
	private String remark;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**更新人 数据类型varchar(32)*/
	private String updateUser;
	
	/**店铺名称*/
	@Virtual
	private String warehouseName;
	@Virtual
	public transient String parentUserMobile;
	
	public String getShopTypeCn(){
		return FastConstant.SHOP_TYPE.getCnName(shopType);
	}
	public String getUserTypeCn(){
		return FastConstant.USER_TYPE.getCnName(userType);
	}
	
	public String getEnabledCn(){
		return ENABLED.YES.equals(enabled)?"可用":"禁用";
	}
	public Long getFastUserId(){
		return fastUserId;
	}
	public String getUserName(){
		return userName;
	}
	public String getMobile(){
		return mobile;
	}
	public Long getWarehouseId(){
		return warehouseId;
	}
	public Integer getUserType(){
		return userType;
	}
	public Integer getEnabled(){
		return enabled;
	}
	public String getRemark(){
		return remark;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setFastUserId(Long fastUserId){
		this.fastUserId=fastUserId;
	}
	public void setUserName(String userName){
		this.userName=userName;
	}
	public void setMobile(String mobile){
		this.mobile=mobile;
	}
	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}
	public void setUserType(Integer userType){
		this.userType=userType;
	}
	public void setEnabled(Integer enabled){
		this.enabled=enabled;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public Integer getShopType() {
		return shopType;
	}

	public void setShopType(Integer shopType) {
		this.shopType = shopType;
	}
}
