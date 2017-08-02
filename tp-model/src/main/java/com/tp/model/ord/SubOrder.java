package com.tp.model.ord;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.OrderConstant.ClearanceStatus;
import com.tp.common.vo.OrderConstant.ORDER_STATUS;
import com.tp.common.vo.OrderConstant.OrderType;
import com.tp.common.vo.OrderConstant.PutCustomsStatus;
import com.tp.common.vo.OrderConstant.PutCustomsType;
import com.tp.common.vo.ord.SalesOrderConstant.OrderIsDeleted;
import com.tp.common.vo.ord.SalesOrderConstant.OrderPayType;
import com.tp.common.vo.ord.SubOrderConstant.PutStatus;
import com.tp.enums.common.PlatformEnum;
import com.tp.model.BaseDO;
import com.tp.util.BigDecimalUtil;
/**
  * @author szy
  * 子订单表
  */
public class SubOrder extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597559L;

	/**PK 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**子订单编号(11+yymmdd+LENGTH(0,id)) 数据类型varchar(50)*/
	private Long orderCode;
	
	/**订单PK 数据类型bigint(20)*/
	private Long parentOrderId;
	
	/**父订单编号 数据类型bigint(20)*/
	private Long parentOrderCode;
	
	/**订单状态（新订单-1,待付款-2,待转移-3,待发货-4,待收货-5,已收货-6,已取消-0,退款完成-100） 数据类型tinyint(3)*/
	private Integer orderStatus;
	
	/**供应商ID 数据类型bigint(20)*/
	private Long supplierId;
	
	/**仓库ID 数据类型bigint(20)*/
	private Long warehouseId;
	
	/**会员ID 数据类型bigint(20)*/
	private Long memberId;
	
	/**收货人名称 数据类型varchar(30)*/
	private String consigneeName;
	
	/**收货人手机 数据类型varchar(20)*/
	private String consigneeMobile;
	
	/**支付号 数据类型varchar(50)*/
	private String payCode;
	
	/**海淘渠道ID 数据类型bigint(20)*/
	private Long seaChannel;
	
	/**海淘电商企业code 数据类型varchar(45)*/
	private String customCode;
	
	/**订单类型（1：一般订单（入仓）。2：平台（不入仓）） 数据类型tinyint(3)*/
	private Integer type;
	
	/**子单商品总数量 数据类型int(11)*/
	private Integer quantity;
	
	/**子单实付总价（打折后，但不含运费） 数据类型double(10,2)*/
	private Double total;
	/**子单实付商品总价  数据类型double(10,2)*/
	private Double itemTotal;
	/**子单应付总价（打折前，但不含运费） 数据类型double(10,2)*/
	private Double originalTotal;
	
	/**优惠金额 数据类型double(10,2)*/
	private Double discount;
	
	/**子单运费 数据类型double(10,2)*/
	private Double freight;
	
	/**使用余额 数据类型double(10,2)*/
	private Double balance;
	
	/**使用积分 数据类型int(11)*/
	private Integer points;
	
	/**供应商名称 数据类型varchar(30)*/
	private String supplierName;
	
	/**供应商别名 数据类型varchar(30)*/
	private String supplierAlias;
	
	/**仓库名称 数据类型varchar(20)*/
	private String warehouseName;
	
	/**是否删除（1.删除 2. 不删除） 数据类型tinyint(1)*/
	private Integer deleted;
	
	/**订单完成时间 数据类型datetime*/
	private Date doneTime;
	
	/**用户账户名 数据类型varchar(50)*/
	private String accountName;
	
	/**订单备注 数据类型varchar(255)*/
	private String remark;
	
	/**取消原因 数据类型varchar(255)*/
	private String cancelReason;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**子单修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**发货时间 数据类型datetime*/
	private Date deliveredTime;
	
	/**税费 数据类型double*/
	private Double taxFee;
	
	/**支付方式 数据类型tinyint(3)*/
	private Integer payType;
	
	/**支付途径 数据类型tinyint(3)*/
	private Long payWay;
	
	/**支付时间 数据类型datetime*/
	private Date payTime;
	
	/**支付公司编码 数据类型varchar(50)*/
	private String payCompanyCode;
	
	/**海淘电商企业名字 数据类型varchar(45)*/
	private String orgName;
	
	/**推送到仓库的状态（0:等待推送，1：已推送，2：推送失败） 数据类型tinyint(2)*/
	private Integer putStatus;
	
	/**海淘渠道名称 数据类型varchar(45)*/
	private String seaChannelName;
	
	/**订单来源 数据类型tinyint(3)*/
	private Integer source;
	
	/**订单来源（用于数据统计） 数据类型tinyint(2)*/
	private Integer trackSource;
	
	/**推广员ID 数据类型bigint(14)*/
	private Long promoterId;
	
	/**分销员ID 数据类型bigint(14)*/
	private Long shopPromoterId;

	/**扫码关注推广员ID 数据类型bigint(14)*/
	private Long scanPromoterId;

	/**团Id*/
	private Long groupId;
	
	/**订单推送海关状态**/
	private Integer putOrderStatus;
	
	/**清关单推送海关状态**/
	private Integer putPersonalgoodsStatus;
	
	/**运单推送海关状态**/
	private Integer putWaybillStatus;
	
	/**支付单推送海关状态**/
	private Integer putPayStatus;
	
	/**订单推送次数**/
	private Integer putCustomsTimes;
	
	/** 清关状态 */
	private Integer clearanceStatus;
	
	/**速递人员ID*/
	private Long fastUserId;
	
	/**接收手机号码*/
	private String receiveTel;
	
	/**海外直邮订单状态*/
	private Integer directOrderStatus;
	@Virtual
	private String payWayStr;
	/**返佣（惠惠保商城用）*/
	@Virtual
	private Double  returnMoney;
	/**商品项列表*/
	@Virtual
	private List<OrderItem> orderItemList = new ArrayList<OrderItem>();
	@Virtual
	private Double commisionAmoutCoupon;
	@Virtual
	private Double commisionAmoutShop;
	@Virtual
	private Double commisionAmoutScan;	// 扫码推广
	
	@Virtual
	private Integer storageType;

	private String channelCode;	
	@Virtual
	private String uuid;

	@Virtual
	private String tpin;
	
	/**推送标签*/
	private Integer putSign;
	
	/**是否推送清关单*/
	@Virtual
	private String putCleanOrder;
	/**是否推送运单*/
	@Virtual
	private String putWaybill;
	/**是否推送订单*/
	@Virtual
	private String putOrder;
	/**是否推送支付单*/
	@Virtual
	private String putPayOrder;
	/**是否推送仓库*/
	@Virtual
	private String putStorage;
	/**推送标识字符*/
	@Virtual
	private transient StringBuffer putSignStr=new StringBuffer("00000");
	
	@Virtual
	private List<Integer> putSignList = new ArrayList<Integer>();
	/**订单超时时间*/
	@Virtual
	private Long overTime;
	/**配送地址*/
	@Virtual
	private List<Long> deliveryAddressList;
	@Virtual
	private String payWayCodeListString;
	
	public Double getOrgTaxFee(){
		BigDecimal orgTaxFee = BigDecimal.ZERO;
		if(CollectionUtils.isNotEmpty(orderItemList)){
			for(OrderItem orderItem:orderItemList){
				orgTaxFee.add(
						new BigDecimal(orderItem.getSalesPrice()).multiply(new BigDecimal(orderItem.getQuantity())
						 .multiply(new BigDecimal(orderItem.getTaxRate()).divide(new BigDecimal(100), 6))));
			}
		}
		return orgTaxFee.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
	
	public Double getCommisionAmount(){
		BigDecimal commisionAmount = BigDecimal.ZERO;
		if(CollectionUtils.isNotEmpty(orderItemList) && shopPromoterId!=null && shopPromoterId!=0){
			for(OrderItem orderItem:orderItemList){
				if(orderItem.getCommisionRate()!=null){
					commisionAmount = commisionAmount.add(
							new BigDecimal(orderItem.getItemAmount())
							   .add(new BigDecimal(orderItem.getCouponAmount()))
							   .multiply(new BigDecimal(orderItem.getCommisionRate()))
									);
				}
			}
		}else{
			return null;
		}
		return BigDecimalUtil.toPrice(commisionAmount);
	}
	
	public Long getScanPromoterId() {
		return scanPromoterId;
	}

	public void setScanPromoterId(Long scanPromoterId) {
		this.scanPromoterId = scanPromoterId;
	}
	
	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getSourceStr(){
		return PlatformEnum.getDescByCode(source);
	}
	
	public String getStatusStr() {
		if(OrderConstant.FAST_ORDER_TYPE.equals(type)){
			return ORDER_STATUS.getAlias(orderStatus);
		}else if(OrderConstant.OrderType.BUY_COUPONS.code.equals(type)  &&  ORDER_STATUS.RECEIPT.code.equals(orderStatus)){//团购券  待使用
			return  ORDER_STATUS.UNUSE.getCnName();
		}else if(OrderConstant.OrderType.BUY_COUPONS.code.equals(type)  &&  ORDER_STATUS.FINISH.code.equals(orderStatus)){//团购券  待使用
			return  ORDER_STATUS.USED.getCnName();
		}else{
			return ORDER_STATUS.getCnName(orderStatus);
		}
		
	}

	public String getOrderStatusAliasStr(){
		return ORDER_STATUS.getAlias(orderStatus);
	}
	
	public String getTypeStr() {
		if (OrderType.DOMESTIC.code.equals(getType()) || OrderType.BONDEDAREA.code.equals(getType()) || OrderType.OVERSEASMAIL.code.equals(getType())) { // 海淘订单
			return OrderType.getCnName(getType()) + " - " + getSeaChannelName();
		}
		return OrderType.getCnName(getType());
	}

	public String getDeletedStr() {
		return OrderIsDeleted.getCnName(getDeleted());
	}
	
	public String getClearanceStatusStr(){
		return ClearanceStatus.getClearanceDescByCode(clearanceStatus);
	}
	
	public Double getPayTotal() {
		return getTotal();
	}

	public String getZhOrderStatus() {
		return ORDER_STATUS.getCnName(getOrderStatus());
	}

	public String getZhType() {
		return OrderConstant.OrderType.getCnName(getType());
	}

	public String getSeaChannelStr() {
		return getSeaChannelName();
	}

	public void resetPutCustomsStatus(){
		this.clearanceStatus = ClearanceStatus.NEW.code;
		this.putOrderStatus = PutCustomsStatus.INIT.code;
		this.putPersonalgoodsStatus = PutCustomsStatus.INIT.code;
		this.putWaybillStatus = PutCustomsStatus.INIT.code;
		this.putPayStatus = PutCustomsStatus.INIT.code;
		this.putCustomsTimes = 0;
	}
	
	public void resetPutCustomsStatus(PutCustomsType type){
		if(PutCustomsType.ORDER_DECLARE == type){
			setPutOrderStatus(PutCustomsStatus.INIT.code);
		}else if(PutCustomsType.PERSONALGOODS_DECLARE == type){
			setPutPersonalgoodsStatus(PutCustomsStatus.INIT.code);
		}else if(PutCustomsType.PAY_DECLARE == type){
			setPutPayStatus(PutCustomsStatus.INIT.code);
		}else if(PutCustomsType.WAYBILL_DECLARE == type){
			setPutWaybillStatus(PutCustomsStatus.INIT.code);
		}else if(PutCustomsType.CLEARANCE == type){
			setClearanceStatus(ClearanceStatus.NEW.code);
		}
	}
	
	public String getPutStatusStr() {
		return PutStatus.getCnName(getPutStatus());
	}
	public String getPayTypeStr() {
		if (null == getPayType()) {
			return StringUtils.EMPTY;
		}
		return OrderPayType.getCnName(getPayType());
	}
	public Long getId(){
		return id;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public Long getParentOrderId(){
		return parentOrderId;
	}
	public Long getParentOrderCode(){
		return parentOrderCode;
	}
	public Integer getOrderStatus(){
		return orderStatus;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public Long getWarehouseId(){
		return warehouseId;
	}
	public Long getMemberId(){
		return memberId;
	}
	public String getConsigneeName(){
		return consigneeName;
	}
	public String getConsigneeMobile(){
		return consigneeMobile;
	}
	public String getPayCode(){
		return payCode;
	}
	public Long getSeaChannel(){
		return seaChannel;
	}
	public String getCustomCode(){
		return customCode;
	}
	public Integer getType(){
		return type;
	}
	public Integer getQuantity(){
		return quantity;
	}
	public Double getTotal(){
		if(total==null){
			return Constant.ZERO;
		}
		return total;
	}
	public Double getOriginalTotal(){
		if(originalTotal==null){
			return Constant.ZERO;
		}
		return originalTotal;
	}
	public Double getDiscount(){
		if(discount==null){
			return Constant.ZERO;
		}
		return discount;
	}
	public Double getFreight(){
		if(freight==null){
			return Constant.ZERO;
		}
		return freight;
	}
	public Double getBalance(){
		return balance;
	}
	public Integer getPoints(){
		return points==null?0:points;
	}
	public String getSupplierName(){
		return supplierName;
	}
	public String getSupplierAlias(){
		return supplierAlias;
	}
	public String getWarehouseName(){
		return warehouseName;
	}
	public Integer getDeleted(){
		return deleted;
	}
	public Date getDoneTime(){
		return doneTime;
	}
	public String getAccountName(){
		return accountName;
	}
	public String getRemark(){
		return remark;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public Date getDeliveredTime(){
		return deliveredTime;
	}
	public Double getTaxFee(){
		if(taxFee==null){
			return Constant.ZERO;
		}
		return taxFee;
	}
	public Integer getPayType(){
		return payType;
	}
	public Long getPayWay(){
		return payWay;
	}
	public Date getPayTime(){
		return payTime;
	}
	public String getPayCompanyCode(){
		return payCompanyCode;
	}
	public String getOrgName(){
		return orgName;
	}
	public Integer getPutStatus(){
		return putStatus;
	}
	public String getSeaChannelName(){
		return seaChannelName;
	}
	public Integer getSource(){
		return source;
	}
	public Integer getTrackSource(){
		return trackSource;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setParentOrderId(Long parentOrderId){
		this.parentOrderId=parentOrderId;
	}
	public void setParentOrderCode(Long parentOrderCode){
		this.parentOrderCode=parentOrderCode;
	}
	public void setOrderStatus(Integer orderStatus){
		this.orderStatus=orderStatus;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}
	public void setMemberId(Long memberId){
		this.memberId=memberId;
	}
	public void setConsigneeName(String consigneeName){
		this.consigneeName=consigneeName;
	}
	public void setConsigneeMobile(String consigneeMobile){
		this.consigneeMobile=consigneeMobile;
	}
	public void setPayCode(String payCode){
		this.payCode=payCode;
	}
	public void setSeaChannel(Long seaChannel){
		this.seaChannel=seaChannel;
	}
	public void setCustomCode(String customCode){
		this.customCode=customCode;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setQuantity(Integer quantity){
		this.quantity=quantity;
	}
	public void setTotal(Double total){
		this.total=total;
	}
	public void setOriginalTotal(Double originalTotal){
		this.originalTotal=originalTotal;
	}
	public void setDiscount(Double discount){
		this.discount=discount;
	}
	public void setFreight(Double freight){
		this.freight=freight;
	}
	public void setBalance(Double balance){
		this.balance=balance;
	}
	public void setPoints(Integer points){
		this.points=points;
	}
	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}
	public void setSupplierAlias(String supplierAlias){
		this.supplierAlias=supplierAlias;
	}
	public void setWarehouseName(String warehouseName){
		this.warehouseName=warehouseName;
	}
	public void setDeleted(Integer deleted){
		this.deleted=deleted;
	}
	public void setDoneTime(Date doneTime){
		this.doneTime=doneTime;
	}
	public void setAccountName(String accountName){
		this.accountName=accountName;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setDeliveredTime(Date deliveredTime){
		this.deliveredTime=deliveredTime;
	}
	public void setTaxFee(Double taxFee){
		this.taxFee=taxFee;
	}
	public void setPayType(Integer payType){
		this.payType=payType;
	}
	public void setPayWay(Long payWay){
		this.payWay=payWay;
	}
	public void setPayTime(Date payTime){
		this.payTime=payTime;
	}
	public void setPayCompanyCode(String payCompanyCode){
		this.payCompanyCode=payCompanyCode;
	}
	public void setOrgName(String orgName){
		this.orgName=orgName;
	}
	public void setPutStatus(Integer putStatus){
		this.putStatus=putStatus;
	}
	public void setSeaChannelName(String seaChannelName){
		this.seaChannelName=seaChannelName;
	}
	public void setSource(Integer source){
		this.source=source;
	}
	public void setTrackSource(Integer trackSource){
		this.trackSource=trackSource;
	}

	public String getPayWayStr() {
		return payWayStr;
	}

	public void setPayWayStr(String payWayStr) {
		this.payWayStr = payWayStr;
	}

	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	public Double getItemTotal() {
		if(itemTotal==null){
			return Constant.ZERO;
		}
		return itemTotal;
	}

	public void setItemTotal(Double itemTotal) {
		this.itemTotal = itemTotal;
	}

	public Long getShopPromoterId() {
		return shopPromoterId;
	}

	public void setShopPromoterId(Long shopPromoterId) {
		this.shopPromoterId = shopPromoterId;
	}

	public Long getPromoterId() {
		return promoterId;
	}

	public void setPromoterId(Long promoterId) {
		this.promoterId = promoterId;
	}

	public Double getCommisionAmoutCoupon() {
		return commisionAmoutCoupon;
	}

	public void setCommisionAmoutCoupon(Double commisionAmoutCoupon) {
		this.commisionAmoutCoupon = commisionAmoutCoupon;
	}

	public Double getCommisionAmoutShop() {
		return commisionAmoutShop;
	}

	public void setCommisionAmoutShop(Double commisionAmoutShop) {
		this.commisionAmoutShop = commisionAmoutShop;
	}
	
	public Double getCommisionAmoutScan() {
		return commisionAmoutScan;
	}

	public void setCommisionAmoutScan(Double commisionAmoutScan) {
		this.commisionAmoutScan = commisionAmoutScan;
	}

	public Integer getStorageType() {
		return storageType;
	}

	public void setStorageType(Integer storageType) {
		this.storageType = storageType;
	}
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	
	public String getTpin() {
		return tpin;
	}

	public void setTpin(String tpin) {
		this.tpin = tpin;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public Integer getPutOrderStatus() {
		return putOrderStatus;
	}

	public void setPutOrderStatus(Integer putOrderStatus) {
		this.putOrderStatus = putOrderStatus;
	}

	public Integer getPutPersonalgoodsStatus() {
		return putPersonalgoodsStatus;
	}

	public void setPutPersonalgoodsStatus(Integer putPersonalgoodsStatus) {
		this.putPersonalgoodsStatus = putPersonalgoodsStatus;
	}

	public Integer getPutWaybillStatus() {
		return putWaybillStatus;
	}

	public void setPutWaybillStatus(Integer putWaybillStatus) {
		this.putWaybillStatus = putWaybillStatus;
	}

	public Integer getPutPayStatus() {
		return putPayStatus;
	}

	public void setPutPayStatus(Integer putPayStatus) {
		this.putPayStatus = putPayStatus;
	}

	public Integer getPutCustomsTimes() {
		return putCustomsTimes;
	}

	public void setPutCustomsTimes(Integer putCustomsTimes) {
		this.putCustomsTimes = putCustomsTimes;
	}

	public Integer getClearanceStatus() {
		return clearanceStatus;
	}

	public void setClearanceStatus(Integer clearanceStatus) {
		this.clearanceStatus = clearanceStatus;
	}

	public Integer getPutSign() {
		return putSign;
	}

	public void setPutSign(Integer putSign) {
		this.putSign = putSign;
		if(null!=this.putSign && this.putSign>0){
			String putSignStrNew = Integer.toBinaryString(putSign);
			Integer length = putSignStr.length()-putSignStrNew.length();
			if(length<0){
				length=0;
			}
			putSignStr.replace(length, putSignStr.length(), putSignStrNew);
			putCleanOrder=putSignStr.substring(0, 1);
			putWaybill=putSignStr.substring(1, 2);
			putOrder=putSignStr.substring(2, 3);
			putPayOrder=putSignStr.substring(3, 4);
			putStorage=putSignStr.substring(4, 5);
		}
	}

	public boolean getPutCleanOrder() {
		return "1".equals(putCleanOrder);
	}
	public void setPutCleanOrder(String putCleanOrder) {
		if("1".equals(putCleanOrder)){
			this.putCleanOrder = "1";
			putSignStr.setCharAt(0, '1');
		}
		initPutSign();
	}
	public boolean getPutWaybill() {
		return "1".equals(putWaybill);
	}
	public void setPutWaybill(String putWaybill) {
		if("1".equals(putWaybill)){
			this.putWaybill = "1";
			putSignStr.setCharAt(1, '1');
		}
		initPutSign();
	}
	public boolean getPutOrder() {
		return "1".equals(putOrder);
	}
	public void setPutOrder(String putOrder) {
		if("1".equals(putOrder)){
			this.putOrder = "1";
			putSignStr.setCharAt(2, '1');
		}
		initPutSign();
	}
	public boolean getPutPayOrder() {
		return "1".equals(putPayOrder);
	}
	public void setPutPayOrder(String putPayOrder) {
		if("1".equals(putPayOrder)){
			this.putPayOrder = "1";
			putSignStr.setCharAt(3, '1');
		}
		initPutSign();
	}
	public boolean getPutStorage() {
		return "1".equals(putStorage);
	}
	public void setPutStorage(String putStorage) {
		if("1".equals(putStorage)){
			this.putStorage = "1";
			putSignStr.setCharAt(4, '1');
		}
		initPutSign();
	}
	
	private void initPutSign(){
		this.putSign = Integer.parseInt(putSignStr.toString(), 2);
	}
	
	public List<Integer> getPutSignList() {
		return initPutSignList(putCleanOrder, putWaybill, putOrder, putPayOrder, putStorage);
	}

	public void setPutSignList(List<Integer> putSignList) {
		this.putSignList = putSignList;
	}
	public static List<Integer> initPutSignList(String putCleanOrder,String putWaybill,String putOrder,
			String putPayOrder,String putStorage){
		if(putCleanOrder==null && putWaybill==null && putOrder==null && putPayOrder==null && putStorage==null){
			return null;
		}
		Integer max = Integer.parseInt("11111", 2);
		List<Integer> searchList = new ArrayList<Integer>(); 
		for(int i=0;i<=max;i++){
			searchList.add(i);
		}
		searchList.removeIf(new Predicate<Integer>(){
			public boolean test(Integer i) {
				String str = Integer.toBinaryString(i);
				str = "00000".replaceFirst("[0]{"+str.length()+"}$", str);
				if(putCleanOrder!=null && !str.matches(putCleanOrder+"[01]{4}$")){
					return Boolean.TRUE;
				}
				if(null!=putWaybill && !str.matches("^[01]"+putWaybill+"[01]{3}$")){
					return Boolean.TRUE;
				}
				if(null!=putOrder && !str.matches("^[01]{2}"+putOrder+"[01]{2}$")){
					return Boolean.TRUE;
				}
				if(null!=putPayOrder && !str.matches("^[01]{3}"+putPayOrder+"[01]$")){
					return Boolean.TRUE;
				}
				if(null!=putStorage && !str.matches("^[01]{4}"+putStorage+"$")){
					return Boolean.TRUE;
				}
				return false;
			}
		});
		if(searchList.size()==0){
			searchList = null;
		}
		return searchList;
	}

	public Long getOverTime() {
		return overTime;
	}

	public void setOverTime(Long overTime) {
		this.overTime = overTime;
	}

	public List<Long> getDeliveryAddressList() {
		return deliveryAddressList;
	}

	public void setDeliveryAddressList(List<Long> deliveryAddressList) {
		this.deliveryAddressList = deliveryAddressList;
	}

	public StringBuffer getPutSignStr() {
		return putSignStr;
	}

	public void setPutSignStr(StringBuffer putSignStr) {
		this.putSignStr = putSignStr;
	}

	public String getPayWayCodeListString() {
		return payWayCodeListString;
	}

	public void setPayWayCodeListString(String payWayCodeListString) {
		this.payWayCodeListString = payWayCodeListString;
	}

	public Long getFastUserId() {
		return fastUserId;
	}

	public void setFastUserId(Long fastUserId) {
		this.fastUserId = fastUserId;
	}

	public String getReceiveTel() {
		return receiveTel;
	}

	public void setReceiveTel(String receiveTel) {
		this.receiveTel = receiveTel;
	}

	public Double getReturnMoney() {
		return returnMoney==null?0:returnMoney;
	}

	public void setReturnMoney(Double returnMoney) {
		this.returnMoney = returnMoney;
	}

	public Integer getDirectOrderStatus() {
		return directOrderStatus;
	}

	public void setDirectOrderStatus(Integer directOrderStatus) {
		this.directOrderStatus = directOrderStatus;
	}
	public String getDirectOrderStatusStr(){
		if(0 == getDirectOrderStatus()){
			return "等待推送";
		}else if(1 == getDirectOrderStatus()){
			return "推送成功";
		}else if(2 == getDirectOrderStatus()){
			return "推送失败";
		}
		return null;
	}
}
