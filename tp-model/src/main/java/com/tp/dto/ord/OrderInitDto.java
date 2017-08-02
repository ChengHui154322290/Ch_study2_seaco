package com.tp.dto.ord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.tp.common.vo.Constant;
import com.tp.dto.mmp.OrderCouponDTO;
import com.tp.model.mem.ConsigneeAddress;
import com.tp.model.mem.MemberInfo;
import com.tp.model.mmp.Coupon;
import com.tp.model.ord.OrderChannelTrack;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPoint;
import com.tp.model.ord.SubOrder;

/**
 * 生成订单参数
 * @author szy
 *
 */
public class OrderInitDto extends ShoppingCartDto {

	private static final long serialVersionUID = 4464002801148960805L;
	/** 收货地址ID */
	private Long consigneeId;
	/**接收手机号码*/
	private String receiveTel;
	/** 支付方式（支付网关） */
	private Long payWay;
	/** 使用优惠券ID */
	private List<Long> couponIds;
	/** 订单备注 */
	private String orderRemark;
	/**订单来源*/
	private Integer orderSource;
	/**会员账号*/
	public String memberAccount;
	/** 是否需要发票：0-不需要发票，1-需要发票 */
	private Integer isNeedInvoice = Constant.TF.NO;
	/** 发票载体：1.纸质发票，2.电子发票 */
	private Integer invoiceCarrier;
	/** 发票类型 ：1,个人 2,企业 */
	private Integer invoiceType;
	/** 发票抬头 **/
	private String invoiceTitle;
	/** 积分类型  （存放商城域名代表该商城对应的积分）**/ 
	private String pointType;
	
	private ConsigneeAddress consigneeAddress;
	private MemberInfo memberInfo;
	/**使用的优惠券*/
	private List<OrderCouponDTO> orderCouponList;
	
	/**是否是首单*/
	private Boolean firstMinus = Boolean.FALSE;
	private Map<Coupon,List<OrderItem>> couponItemMap = new HashMap<Coupon,List<OrderItem>>();
	
	private List<OrderPoint> orderPointList ;
	/**是否需要校验实名认证信息*/
	private boolean checkAuth = true;

	/**渠道跟踪*/
	private OrderChannelTrack orderChannelTrack;
	
	public boolean isCheckAuth() {
		return checkAuth;
	}

	public void setCheckAuth(boolean checkAuth) {
		this.checkAuth = checkAuth;
	}

	public List<OrderItem> getOrderItemList(){
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		if(CollectionUtils.isNotEmpty(getPreSubOrderList())){
			for(SubOrder subOrder:getPreSubOrderList()){
				orderItemList.addAll(subOrder.getOrderItemList());
			}
		}
		return orderItemList;
	}
	
	public Long getConsigneeId() {
		return consigneeId;
	}
	public void setConsigneeId(Long consigneeId) {
		this.consigneeId = consigneeId;
	}
	public Long getPayWay() {
		return payWay;
	}
	public void setPayWay(Long payWay) {
		this.payWay = payWay;
	}
	public List<Long> getCouponIds() {
		return couponIds;
	}
	public void setCouponIds(List<Long> couponIds) {
		this.couponIds = couponIds;
	}
	public String getOrderRemark() {
		return orderRemark;
	}
	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}
	public Integer getIsNeedInvoice() {
		return isNeedInvoice;
	}
	public void setIsNeedInvoice(Integer isNeedInvoice) {
		this.isNeedInvoice = isNeedInvoice;
	}
	public Integer getInvoiceCarrier() {
		return invoiceCarrier;
	}
	public void setInvoiceCarrier(Integer invoiceCarrier) {
		this.invoiceCarrier = invoiceCarrier;
	}
	public Integer getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getInvoiceTitle() {
		return invoiceTitle;
	}
	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public ConsigneeAddress getConsigneeAddress() {
		return consigneeAddress;
	}
	public void setConsigneeAddress(ConsigneeAddress consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}
	public String getMemberAccount() {
		return memberAccount;
	}
	public void setMemberAccount(String memberAccount) {
		this.memberAccount = memberAccount;
	}
	public List<OrderCouponDTO> getOrderCouponList() {
		return orderCouponList;
	}
	public void setOrderCouponList(List<OrderCouponDTO> orderCouponList) {
		this.orderCouponList = orderCouponList;
	}
	public Integer getOrderSource() {
		return orderSource;
	}
	public void setOrderSource(Integer orderSource) {
		this.orderSource = orderSource;
	}
	public MemberInfo getMemberInfo() {
		return memberInfo;
	}
	public void setMemberInfo(MemberInfo memberInfo) {
		this.memberInfo = memberInfo;
	}
	public Boolean getFirstMinus() {
		return firstMinus;
	}
	public void setFirstMinus(Boolean firstMinus) {
		this.firstMinus = firstMinus;
	}
	public Map<Coupon, List<OrderItem>> getCouponItemMap() {
		return couponItemMap;
	}
	public void setCouponItemMap(Map<Coupon, List<OrderItem>> couponItemMap) {
		this.couponItemMap = couponItemMap;
	}

	public List<OrderPoint> getOrderPointList() {
		return orderPointList;
	}

	public void setOrderPointList(List<OrderPoint> orderPointList) {
		this.orderPointList = orderPointList;
	}

	public OrderChannelTrack getOrderChannelTrack() {
		return orderChannelTrack;
	}

	public void setOrderChannelTrack(OrderChannelTrack orderChannelTrack) {
		this.orderChannelTrack = orderChannelTrack;
	}

	public String getReceiveTel() {
		return receiveTel;
	}

	public void setReceiveTel(String receiveTel) {
		this.receiveTel = receiveTel;
	}

	public String getPointType() {
		return pointType;
	}

	public void setPointType(String pointType) {
		this.pointType = pointType;
	}

   
    
}
