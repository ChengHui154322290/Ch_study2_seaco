package com.tp.model.cmt;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品评论导入明细表
  */
public class ReviewImportList extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450404446076L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**item_import_log的主键  数据类型bigint(10)*/
	private Long logId;
	
	/**订单ID 数据类型varchar(32)*/
	private String orderCode;
	
	/**商品id 数据类型varchar(20)*/
	private String pid;
	
	/** 数据类型varchar(100)*/
	private String sku;
	
	/**商品 skuCode 数据类型varchar(20)*/
	private String skuCode;
	
	/**用户id 数据类型bigint(20)*/
	private Long uid;
	
	/**用户名 冗余字段 数据类型varchar(20)*/
	private String userName;
	
	/** 数据类型int(5)*/
	private Integer mark;
	
	/**评论主题 数据类型varchar(100)*/
	private String subject;
	
	/**评论内容 数据类型varchar(500)*/
	private String content;
	
	/**是否审核通过 数据类型tinyint(1)*/
	private Integer isCheck;
	
	/**是否匿名 数据类型tinyint(1)*/
	private Integer isAnonymous;
	
	/**是否置顶 1-置顶 0-置底 数据类型tinyint(1)*/
	private Integer isTop;
	
	/**1-隐藏 0-仅自己可见 数据类型tinyint(1)*/
	private Integer isHide;
	
	/**导入的excel行号 数据类型bigint(8)*/
	private Long excelIndex;
	
	/**状态:1-为导入成功，2-为导入失败，默认1 数据类型int(1)*/
	private Integer status;
	
	/** 数据类型varchar(100)*/
	private String opMessage;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getLogId(){
		return logId;
	}
	public String getOrderCode(){
		return orderCode;
	}
	public String getPid(){
		return pid;
	}
	public String getSku(){
		return sku;
	}
	public String getSkuCode(){
		return skuCode;
	}
	public Long getUid(){
		return uid;
	}
	public String getUserName(){
		return userName;
	}
	public Integer getMark(){
		return mark;
	}
	public String getSubject(){
		return subject;
	}
	public String getContent(){
		return content;
	}
	public Integer getIsCheck(){
		return isCheck;
	}
	public Integer getIsAnonymous(){
		return isAnonymous;
	}
	public Integer getIsTop(){
		return isTop;
	}
	public Integer getIsHide(){
		return isHide;
	}
	public Long getExcelIndex(){
		return excelIndex;
	}
	public Integer getStatus(){
		return status;
	}
	public String getOpMessage(){
		return opMessage;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setLogId(Long logId){
		this.logId=logId;
	}
	public void setOrderCode(String orderCode){
		this.orderCode=orderCode;
	}
	public void setPid(String pid){
		this.pid=pid;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setSkuCode(String skuCode){
		this.skuCode=skuCode;
	}
	public void setUid(Long uid){
		this.uid=uid;
	}
	public void setUserName(String userName){
		this.userName=userName;
	}
	public void setMark(Integer mark){
		this.mark=mark;
	}
	public void setSubject(String subject){
		this.subject=subject;
	}
	public void setContent(String content){
		this.content=content;
	}
	public void setIsCheck(Integer isCheck){
		this.isCheck=isCheck;
	}
	public void setIsAnonymous(Integer isAnonymous){
		this.isAnonymous=isAnonymous;
	}
	public void setIsTop(Integer isTop){
		this.isTop=isTop;
	}
	public void setIsHide(Integer isHide){
		this.isHide=isHide;
	}
	public void setExcelIndex(Long excelIndex){
		this.excelIndex=excelIndex;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setOpMessage(String opMessage){
		this.opMessage=opMessage;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
