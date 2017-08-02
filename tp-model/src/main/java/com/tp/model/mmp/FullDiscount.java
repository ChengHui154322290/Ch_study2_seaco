package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 优惠政策信息
  */
public class FullDiscount extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579107L;

	/**序号 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**优惠政策名称 数据类型varchar(50)*/
	private String name;
	
	/**优惠政策类型 1-满减 数据类型smallint(1)*/
	private Integer type;
	
	/**状态 1 - 编辑中 2 - 待审核  3 - 审核通过 4 - 终止  5-取消 6-驳回 数据类型smallint(1)*/
	private Integer status;
	
	/**优惠开始时间 数据类型datetime*/
	private Date startTime;
	
	/**优惠结束时间 数据类型datetime*/
	private Date endTime;
	
	/**使用范围 1 - 全网商品  2 - 自营和代销商品 3 - 代发商品 数据类型smallint(1)*/
	private Integer useRange;
	
	/**适用海淘 1-全网商品    2-海淘商品   3-非海淘商品   数据类型smallint(6)*/
	private Integer hitaoSymbol;
	
	/**费用承担方 数据类型bigint(18)*/
	private Long supplierId;
	
	/**是否叠加使用  1-全网 2-满减券 4-现金券 8-满减活动 数据类型smallint(1)*/
	private Integer overlay;
	
	/**使用平台 1-全平台 2-PC 4-IOS 8-安卓 16-WAP 32-西客商城孕育 64-微信商城 128-APP 数据类型smallint(2)*/
	private Integer platform;
	
	/**使用区域 1-全网 2-华东 4-华北 8-华中 16-华南 32-东北 64-西北 128-西南 数据类型smallint(2)*/
	private Integer area;
	
	/**全品类商品 数据类型tinyint(1)*/
	private Integer useAllItem;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**修改人 数据类型varchar(32)*/
	private String updateUser;
	
	
	public Long getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public Integer getType(){
		return type;
	}
	public Integer getStatus(){
		return status;
	}
	public Date getStartTime(){
		return startTime;
	}
	public Date getEndTime(){
		return endTime;
	}
	public Integer getUseRange(){
		return useRange;
	}
	public Integer getHitaoSymbol(){
		return hitaoSymbol;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public Integer getOverlay(){
		return overlay;
	}
	public Integer getPlatform(){
		return platform;
	}
	public Integer getArea(){
		return area;
	}
	public Integer getUseAllItem(){
		return useAllItem;
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
	public void setId(Long id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setStartTime(Date startTime){
		this.startTime=startTime;
	}
	public void setEndTime(Date endTime){
		this.endTime=endTime;
	}
	public void setUseRange(Integer useRange){
		this.useRange=useRange;
	}
	public void setHitaoSymbol(Integer hitaoSymbol){
		this.hitaoSymbol=hitaoSymbol;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setOverlay(Integer overlay){
		this.overlay=overlay;
	}
	public void setPlatform(Integer platform){
		this.platform=platform;
	}
	public void setArea(Integer area){
		this.area=area;
	}
	public void setUseAllItem(Integer useAllItem){
		this.useAllItem=useAllItem;
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
}
