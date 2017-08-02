package com.tp.dto.mem;

import java.io.Serializable;
import java.util.Date;

import com.tp.util.DateUtil;
import com.tp.util.StringUtil;


public class ItemReviewDto implements Serializable {

	private static final long serialVersionUID = -4326543040936575699L;
	
	/** ID **/
	private Long id;

	/** 订单ID **/
	private String orderCode;

	/** 商品 prdid**/
	private String prd;
	
	private String barcode;
	
	private String itemName;

	/** 用户名 冗余字段 **/
	private String userName;
	
	/** 评论星数 **/
	private Integer star;
	
	/** 评论内容 **/
	private String content;
	
	/** 是否隐藏 2-不限 1-隐藏 0-仅自己可见 **/
	private Integer isHide;
	
	/** 是否置顶 2-不限 1-置顶 0-置底 **/
	private Integer isTop;
	
	/** 是否由批量导入产生 1-是 0-不是 **/
	private Boolean isImport;
	
	/** 导入失败原因 **/
	private String failReason;
	
	private Long excelIndex;
	
	/**导入是否成功: 1-成功，2-失败**/
	private int importStatus;
	
	/**导入操作产生的信息**/
	private String msg;
	
	/**  **/
	private Date createTime;

	public String getOrderCode() {
		if(StringUtil.isNullOrEmpty(orderCode))
			orderCode = "-";
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getPrd() {
		if(StringUtil.isNullOrEmpty(prd))
			prd = "-";
		return prd;
	}

	public void setPrd(String prd) {
		this.prd = prd;
	}

	public String getBarcode() {
		if(StringUtil.isNullOrEmpty(barcode))
			barcode = "-";
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getItemName() {
		if(StringUtil.isNullOrEmpty(itemName))
			itemName = "-";
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getUserName() {	
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getStar() {
		if(StringUtil.isNull(star))
			star = 0;
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public String getContent() {
		if(StringUtil.isNullOrEmpty(content))
			content = "-";
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() throws Exception {
		if(null == createTime)
			return "-";
		else {
			String str = DateUtil.formatDate(createTime, "yyyy-MM-dd HH:mm:ss");
			return str;
		}
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getIsHide() {
		return isHide;
	}

	public void setIsHide(Integer isHide) {
		this.isHide = isHide;
	}

	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	public Boolean getIsImport() {
		return isImport;
	}

	public void setIsImport(Boolean isImport) {
		this.isImport = isImport;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public Long getExcelIndex() {
		return excelIndex;
	}

	public void setExcelIndex(Long excelIndex) {
		this.excelIndex = excelIndex;
	}

	public int getImportStatus() {
		return importStatus;
	}

	public void setImportStatus(int importStatus) {
		this.importStatus = importStatus;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}