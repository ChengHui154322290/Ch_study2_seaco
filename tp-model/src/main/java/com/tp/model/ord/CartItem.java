package com.tp.model.ord;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.vo.Constant;
import com.tp.model.BaseDO;
public class CartItem extends BaseDO {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5231450373619284353L;
	/**购买车商品编号 数据类型bigint(14)*/
	@Id
	private Long cartItemId;
	/**会员ID 数据类型bigint(14)*/
	private Long memberId;
	
	/**店铺ID,0或空为西客*/
	private Long shopId;
	/**商品SKU 数据类型varchar(20)*/
	private String skuCode;
	/**活动编号 数据类型bigint(14)*/
	private Long topicId;
	/**购买商品数量 数据类型int(5)*/
	private Integer quantity;
	/**是否选中 数据类型tinyint(1)*/
	private Integer selected;
	/**添加时间 数据类型datetime*/
	private Date createTime;
	/**创建时间 数据类型datetime*/
	private Date updateTime;
	
	public Boolean getSelectedBoolean(){
		return Constant.SELECTED.YES.equals(selected);
	}
	public Long getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(Long cartItemId) {
		this.cartItemId = cartItemId;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getSelected() {
		return selected;
	}
	public void setSelected(Integer selected) {
		this.selected = selected;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
}
