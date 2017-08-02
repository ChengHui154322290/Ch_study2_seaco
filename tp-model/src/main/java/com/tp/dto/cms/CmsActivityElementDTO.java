package com.tp.dto.cms;

import java.io.Serializable;
import java.util.Date;

public class CmsActivityElementDTO implements Serializable/*,JSONAware*/ {

	private static final long serialVersionUID = 1L;
	
	/** 主键 */
	private Long id;
	
	/** 位置表主键 */
	private Long positionId;

	/** 活动id */
	private Long activityId;

	/** 专题编号 */
	private String activityCode;

	/** 专题名称 */
	private String activityName;
	
	/** SKU编号 */
	private String skuCode;
	
	/** 商品名称 */
	private String goodsName;
	
	/** 商家 */
	private String seller;
	
	/** 规格参数 */
	private String standardParams;
	
	/** 限购总量 */
	private Integer limitTotal;
	
	/** 限购数量 */
	private Integer limitNumber;
	
	/** 活动价 */
	private double sellingPrice;
	
	/** 活动开始时间 */
	private Date actStartdate;
	
	/** 活动结束时间 */
	private Date actEnddate;
	
	/** 创建人 */
	private Integer creater;

	/** 创建时间 */
	private Date create_time;
	
	/** 分页：开始 */
	private Integer start;
	
	/** 分页：大小 */
	private Integer pageSize;
	
	/** 状态 */
	private String flagStr;
	
	/** 按状态排序值 */
	private Integer flagStrNum;
	
	/** 启用时间 */
	private Date startdate;

	/** 失效时间 */
	private Date enddate;

	/** 展示图片(图片地址src) */
	private String picture;
	
	/** 展示图片(有前缀拼接) */
	private String pictureStr;
	
	/** 跳转链接 */
	private String link;

	/** 当前状态(0正常，1停用，2删除) */
	private Integer status;
	
	/** 顺序 **/
	private Integer seq;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getStandardParams() {
		return standardParams;
	}

	public void setStandardParams(String standardParams) {
		this.standardParams = standardParams;
	}

	public Integer getLimitTotal() {
		return limitTotal;
	}

	public void setLimitTotal(Integer limitTotal) {
		this.limitTotal = limitTotal;
	}

	public Integer getLimitNumber() {
		return limitNumber;
	}

	public void setLimitNumber(Integer limitNumber) {
		this.limitNumber = limitNumber;
	}

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Integer getCreater() {
		return creater;
	}

	public void setCreater(Integer creater) {
		this.creater = creater;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getFlagStr() {
		return flagStr;
	}

	public void setFlagStr(String flagStr) {
		this.flagStr = flagStr;
	}

	public Integer getFlagStrNum() {
		return flagStrNum;
	}

	public void setFlagStrNum(Integer flagStrNum) {
		this.flagStrNum = flagStrNum;
	}

	public Date getActStartdate() {
		return actStartdate;
	}

	public void setActStartdate(Date actStartdate) {
		this.actStartdate = actStartdate;
	}

	public Date getActEnddate() {
		return actEnddate;
	}

	public void setActEnddate(Date actEnddate) {
		this.actEnddate = actEnddate;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPictureStr() {
		return pictureStr;
	}

	public void setPictureStr(String pictureStr) {
		this.pictureStr = pictureStr;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
}
