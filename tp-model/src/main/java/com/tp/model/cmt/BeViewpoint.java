package com.tp.model.cmt;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 西客观点
  */
public class BeViewpoint extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450404446073L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**条码 数据类型varchar(50)*/
	private String barcode;
	
	/**spu编码 数据类型varchar(50)*/
	private String spu;
	
	/**商品名称 数据类型varchar(100)*/
	private String itemTitle;
	
	/**内容 数据类型text*/
	private String content;
	
	/**分值 数据类型varchar(10)*/
	private String score;
	
	/**排序 数据类型bigint(20)*/
	private Long sort;
	
	/**评论时间 数据类型datetime*/
	private Date viewpointTime;
	
	/**置顶/置底设置： 默认1，置顶为2，置底为0 数据类型tinyint(4)*/
	private Integer stickSign;
	
	/**是否隐藏：默认为0，0：不隐藏；1：隐藏 数据类型tinyint(4)*/
	private Integer hideSign;
	
	/**会员Id 数据类型bigint(20)*/
	private Long userId;
	
	/**创建人 数据类型bigint(20)*/
	private Long createUserId;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改人 数据类型bigint(20)*/
	private Long modifyUserId;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	/** 会员账号 */
	@Virtual
	private String memLoginName;
	/** 会员昵称 */
	@Virtual String memNickName;
	
	@Virtual
	private Date createStartDate;
	
	@Virtual
	private Date createEndDate;
	
	public Long getId(){
		return id;
	}
	public String getBarcode(){
		return barcode;
	}
	public String getSpu(){
		return spu;
	}
	public String getItemTitle(){
		return itemTitle;
	}
	public String getContent(){
		return content;
	}
	public String getScore(){
		return score;
	}
	public Long getSort(){
		return sort;
	}
	public Date getViewpointTime(){
		return viewpointTime;
	}
	public Integer getStickSign(){
		return stickSign;
	}
	public Integer getHideSign(){
		return hideSign;
	}
	public Long getUserId(){
		return userId;
	}
	public Long getCreateUserId(){
		return createUserId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getModifyUserId(){
		return modifyUserId;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setBarcode(String barcode){
		this.barcode=barcode;
	}
	public void setSpu(String spu){
		this.spu=spu;
	}
	public void setItemTitle(String itemTitle){
		this.itemTitle=itemTitle;
	}
	public void setContent(String content){
		this.content=content;
	}
	public void setScore(String score){
		this.score=score;
	}
	public void setSort(Long sort){
		this.sort=sort;
	}
	public void setViewpointTime(Date viewpointTime){
		this.viewpointTime=viewpointTime;
	}
	public void setStickSign(Integer stickSign){
		this.stickSign=stickSign;
	}
	public void setHideSign(Integer hideSign){
		this.hideSign=hideSign;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyUserId(Long modifyUserId){
		this.modifyUserId=modifyUserId;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public Date getCreateStartDate() {
		return createStartDate;
	}
	public void setCreateStartDate(Date createStartDate) {
		this.createStartDate = createStartDate;
	}
	public Date getCreateEndDate() {
		return createEndDate;
	}
	public void setCreateEndDate(Date createEndDate) {
		this.createEndDate = createEndDate;
	}
	public String getMemLoginName() {
		return memLoginName;
	}
	public void setMemLoginName(String memLoginName) {
		this.memLoginName = memLoginName;
	}
	public String getMemNickName() {
		return memNickName;
	}
	public void setMemNickName(String memNickName) {
		this.memNickName = memNickName;
	}	
	
}
