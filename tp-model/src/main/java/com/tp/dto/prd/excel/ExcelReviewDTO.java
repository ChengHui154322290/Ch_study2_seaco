package com.tp.dto.prd.excel;

import java.io.Serializable;

import com.tp.common.annotation.excel.poi.ExcelEntity;
import com.tp.common.annotation.excel.poi.ExcelProperty;

/**
 * 
 * 商品评论导入
 * @author szy
 * 
 */
@ExcelEntity
public class ExcelReviewDTO extends ExcelBaseDTO  implements Serializable {

	private static final long serialVersionUID = 5350689447621545702L;

	/** 主键 */
	private Long id;

	/** item_import_log的主键 */
	private Long logId;

	/** PRD **/
	@ExcelProperty(value="PRD")
	private String prd;

	/** 注册手机号 **/
	@ExcelProperty(value="用户名(手机号)")
	private String username;

	/** 订单号 **/
	@ExcelProperty(value="订单号")
	private String orderNo;

	/** 分值 **/
	@ExcelProperty(value="分值")
	private Integer score;

	/** 是否置顶  **/
	@ExcelProperty(value="置顶")
	private Integer isTop;

	/** 是否隐藏  **/
	@ExcelProperty(value="隐藏 ")
	private Integer isHide;

	/** 评论内容 **/
	@ExcelProperty(value="评论内容")
	private String content;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public String getPrd() {
		return prd;
	}

	public void setPrd(String prd) {
		this.prd = prd;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	public Integer getIsHide() {
		return isHide;
	}

	public void setIsHide(Integer isHide) {
		this.isHide = isHide;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ExcelReviewDomain [id=" + id + ", logId=" + logId + ", prd="
				+ prd + ", username=" + username + ", order no=" + orderNo
				+ ", score=" + score + ", is_top=" + isTop
				+ ", is_hide=" + isHide + "]";
	}

	

}