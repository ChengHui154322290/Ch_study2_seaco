package com.tp.model.cmt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品评论信息表
  */
public class ItemReview extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450404446075L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**订单ID 数据类型varchar(32)*/
	private String orderCode;
	
	/**商品prdid 数据类型varchar(20)*/
	private String pid;
	
	/**spu 数据类型varchar(20)*/
	private String spu;
	
	/**商品规格信息 数据类型varchar(100)*/
	private String sku;
	
	/**商品 skuCode 数据类型varchar(20)*/
	private String skuCode;
	
	/**用户id 数据类型bigint(20)*/
	private Long uid;
	
	/** 数据类型varchar(50)*/
	private String userName;
	
	/**评论主题 数据类型varchar(100)*/
	private String subject;
	
	/**评论内容 数据类型varchar(500)*/
	private String content;
	
	/**0 待审核 1. 审核中  2. 审核通过  3. 已驳回 数据类型tinyint(1)*/
	private Integer isCheck;
	
	/**是否匿名 数据类型tinyint(1)*/
	private Integer isAnonymous;
	
	/**是否置顶   0.置底  1.不限 2.置顶 数据类型tinyint(1)*/
	private Integer isTop;
	
	/**1-隐藏 0-仅自己可见 2-显示 数据类型tinyint(1)*/
	private Integer isHide;
	
	/** 0.用户评论产生  1.导入产生     2 后台新增产生 数据类型tinyint(1)*/
	private Integer isImport;
	
	/**是否精华 0-否 1-是 数据类型tinyint(1)*/
	private Integer isEssence;
	
	/**是否有图片 0-否 1-是 数据类型tinyint(1)*/
	private Integer hasImg;
	
	/**是否有西客回复 0-否 1-是 数据类型tinyint(1)*/
	private Integer hasReply;
	
	/**评论是否包含差评词 0-否 1-是 数据类型tinyint(1)*/
	private Integer hasNegative;
	
	/**数据导入失败原因 数据类型varchar(500)*/
	private String failReason;
	
	/**打分 数据类型tinyint(4)*/
	private Integer mark;
	
	/**服务评分 数据类型tinyint(4)*/
	private Integer serviceMark;
	
	/**宝宝年龄，单位月 数据类型int(5)*/
	private Integer babyAge;
	
	/**创建人 用户评论的是用户id 后台增加的为管理员id 数据类型bigint(20)*/
	private Long createUserId;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型datetime*/
	private Date modifyTime;
	
	/** 数据类型datetime*/
	private Date deleteTime;
	
	/**是否删除 数据类型tinyint(1)*/
	private Integer isDelete;
	
	/**用户头像 数据类型varchar(100)*/
	private String userImgUrl;
	
	/**0:来自西客商城 1:来自seagoor 数据类型tinyint(1)*/
	private Integer platForm;
	
	/**seagoor商品code 数据类型varchar(30)*/
	private String btCode;
	
	/**seagoor userId 数据类型varchar(20)*/
	private String encUserId;
	
	/**seagoor评论id 数据类型bigint(20)*/
	private Long itemReviewBbtId;
	
	/**平台来源  0:pc 1:app 2:wap 3:ios 4:BTM  5:wx 数据类型tinyint(1)*/
	private Integer source;
	
	/** 保留字段,必要时用于屏蔽用户评论 */
	@Virtual
	private Integer status;
	
	/**商品名称 数据类型varchar(60)*/
	@Virtual
	private String itemTitle;
	
	/** 用于提取多少条记录,只针对findTopReview接口有用  */
	@Virtual
	private Integer count = 1;
	@Virtual
	private Date createBeginTime;
	@Virtual
	private Date createEndTime;
	@Virtual
	private List<String> prds = new ArrayList<String>();
	
	public Long getId(){
		return id;
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
	public Integer getIsImport(){
		return isImport;
	}
	public String getFailReason(){
		return failReason;
	}
	public Integer getMark(){
		return mark;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public Date getDeleteTime(){
		return deleteTime;
	}
	public Integer getIsDelete(){
		return isDelete;
	}
	public void setId(Long id){
		this.id=id;
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
	public void setIsImport(Integer isImport){
		this.isImport=isImport;
	}
	public void setFailReason(String failReason){
		this.failReason=failReason;
	}
	public void setMark(Integer mark){
		this.mark=mark;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setDeleteTime(Date deleteTime){
		this.deleteTime=deleteTime;
	}
	public void setIsDelete(Integer isDelete){
		this.isDelete=isDelete;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Date getCreateBeginTime() {
		return createBeginTime;
	}
	public void setCreateBeginTime(Date createBeginTime) {
		this.createBeginTime = createBeginTime;
	}
	public Date getCreateEndTime() {
		return createEndTime;
	}
	public void setCreateEndTime(Date createEndTime) {
		this.createEndTime = createEndTime;
	}
	public List<String> getPrds() {
		return prds;
	}
	public void setPrds(List<String> prds) {
		this.prds = prds;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getSpu() {
		return spu;
	}
	public void setSpu(String spu) {
		this.spu = spu;
	}
	public Integer getIsEssence() {
		return isEssence;
	}
	public void setIsEssence(Integer isEssence) {
		this.isEssence = isEssence;
	}
	public Integer getHasImg() {
		return hasImg;
	}
	public void setHasImg(Integer hasImg) {
		this.hasImg = hasImg;
	}
	public Integer getHasReply() {
		return hasReply;
	}
	public void setHasReply(Integer hasReply) {
		this.hasReply = hasReply;
	}
	public Integer getHasNegative() {
		return hasNegative;
	}
	public void setHasNegative(Integer hasNegative) {
		this.hasNegative = hasNegative;
	}
	public Integer getServiceMark() {
		return serviceMark;
	}
	public void setServiceMark(Integer serviceMark) {
		this.serviceMark = serviceMark;
	}
	public Integer getBabyAge() {
		return babyAge;
	}
	public void setBabyAge(Integer babyAge) {
		this.babyAge = babyAge;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public String getUserImgUrl() {
		return userImgUrl;
	}
	public void setUserImgUrl(String userImgUrl) {
		this.userImgUrl = userImgUrl;
	}
	public Integer getPlatForm() {
		return platForm;
	}
	public void setPlatForm(Integer platForm) {
		this.platForm = platForm;
	}
	public String getBtCode() {
		return btCode;
	}
	public void setBtCode(String btCode) {
		this.btCode = btCode;
	}
	public String getEncUserId() {
		return encUserId;
	}
	public void setEncUserId(String encUserId) {
		this.encUserId = encUserId;
	}
	public Long getItemReviewBbtId() {
		return itemReviewBbtId;
	}
	public void setItemReviewBbtId(Long itemReviewBbtId) {
		this.itemReviewBbtId = itemReviewBbtId;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public String getItemTitle() {
		return itemTitle;
	}
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}	
}
