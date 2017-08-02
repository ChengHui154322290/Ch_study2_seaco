package com.tp.model.cms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 广告管理表
  */
public class AdvertiseInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451847L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**图片名称 数据类型varchar(50)*/
	private String advertname;
	
	/**类别 数据类型varchar(128)*/
	private String type;
	
	/**接口标识 数据类型varchar(50)*/
	private String ident;
	
	/** 数据类型varchar(128)*/
	private String link;
	
	/**图片路径 数据类型varchar(128)*/
	private String path;
	
	/**位置 数据类型int(2)*/
	private Integer position;
	
	/** 数据类型int(10)*/
	private Integer sort;
	
	/**启动时间,单位是秒 数据类型int(4)*/
	private Integer time;
	
	/**sku编号 数据类型varchar(128)*/
	private String sku;
	
	/**商品id 数据类型bigint(11)*/
	private Long productid;
	
	/**活动id 数据类型bigint(11)*/
	private Long activityid;
	
	/**活动类型（0表示网址链接，1表示商品id） 数据类型varchar(128)*/
	private String acttype;
	
	/**平台标识 数据类型int(4)*/
	private Integer platformType;
	
	/** 数据类型varchar(10)*/
	private String status;
	
	/**开始时间 数据类型datetime*/
	private Date startdate;
	
	/**结束时间 数据类型datetime*/
	private Date enddate;
	
	/**创建人 数据类型int(10)*/
	private Integer creater;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改人 数据类型int(10)*/
	private Integer modifier;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	/** 开始时间字符串 */
	@Virtual
	private String startdateStr;
	
	/** 结束时间字符串 */
	@Virtual
	private String enddateStr;
	
	
	public Long getId(){
		return id;
	}
	public String getAdvertname(){
		return advertname;
	}
	public String getType(){
		return type;
	}
	public String getIdent(){
		return ident;
	}
	public String getLink(){
		return link;
	}
	public String getPath(){
		return path;
	}
	public Integer getPosition(){
		return position;
	}
	public Integer getSort(){
		return sort;
	}
	public Integer getTime(){
		return time;
	}
	public String getSku(){
		return sku;
	}
	public Long getProductid(){
		return productid;
	}
	public Long getActivityid(){
		return activityid;
	}
	public String getActtype(){
		return acttype;
	}
	public Integer getPlatformType(){
		return platformType;
	}
	public String getStatus(){
		return status;
	}
	public Date getStartdate(){
		return startdate;
	}
	public Date getEnddate(){
		return enddate;
	}
	public Integer getCreater(){
		return creater;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Integer getModifier(){
		return modifier;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setAdvertname(String advertname){
		this.advertname=advertname;
	}
	public void setType(String type){
		this.type=type;
	}
	public void setIdent(String ident){
		this.ident=ident;
	}
	public void setLink(String link){
		this.link=link;
	}
	public void setPath(String path){
		this.path=path;
	}
	public void setPosition(Integer position){
		this.position=position;
	}
	public void setSort(Integer sort){
		this.sort=sort;
	}
	public void setTime(Integer time){
		this.time=time;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setProductid(Long productid){
		this.productid=productid;
	}
	public void setActivityid(Long activityid){
		this.activityid=activityid;
	}
	public void setActtype(String acttype){
		this.acttype=acttype;
	}
	public void setPlatformType(Integer platformType){
		this.platformType=platformType;
	}
	public void setStatus(String status){
		this.status=status;
	}
	public void setStartdate(Date startdate){
		this.startdate=startdate;
	}
	public void setEnddate(Date enddate){
		this.enddate=enddate;
	}
	public void setCreater(Integer creater){
		this.creater=creater;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifier(Integer modifier){
		this.modifier=modifier;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public String getStartdateStr() {
		return startdateStr;
	}
	public void setStartdateStr(String startdateStr) {
		this.startdateStr = startdateStr;
	}
	public String getEnddateStr() {
		return enddateStr;
	}
	public void setEnddateStr(String enddateStr) {
		this.enddateStr = enddateStr;
	}
}
