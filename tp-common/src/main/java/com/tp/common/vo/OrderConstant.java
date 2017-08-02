package com.tp.common.vo;

import org.apache.commons.lang.ArrayUtils;

import com.tp.common.vo.StorageConstant.StorageType;

/**
 * 订单常量定义
 * @author szy
 *
 */
public final class OrderConstant {
	
	/**速购订单类型*/
	public static final Integer FAST_ORDER_TYPE = OrderType.FAST.code;
	
	/** 是否需要发票：0-手机，1-地址 */
	public enum ORDER_RECIEVE_TYPE{
		TEL(0,"手机"),
		ADDRESS(1,"地址");
		public int code;
		public String cnName;
		
		ORDER_RECIEVE_TYPE(int code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
	}
	
	
	
	/**
	 * 订单状态
	 * @author szy
	 *
	 */
	public enum ORDER_STATUS{
		
		/**新建-1，保留*/
		CREATE(1,"新建","新建"),
		/**待支付-2*/
		PAYMENT(2,"待支付","待支付"),
		/**待转移-3，保留*/
		TRANSFER(3,"待备货","待接单"),	
		/**待发货-4*/
		DELIVERY(4,"待发货","已接单"),
		/**待收货-5*/
		RECEIPT(5,"待收货","配送中"),
		/**已收货-6*/
		FINISH(6,"已收货","已送达"),
		UNUSE(7,"待使用","待使用"),//数据中状态同RECEIPT 5
		USED(8,"已使用","已使用"),//数据中状态同FINISH 6
		/**已取消-0*/
		CANCEL(0,"已取消","已取消");

		public Integer code;
		public String cnName;
		public String alias;//类型别称
		
		public static String getCnName(Integer code){
			if(code!=null){
				for(ORDER_STATUS entry:ORDER_STATUS.values()){
					if(entry.code.intValue()==code){
						return entry.cnName;
					}
				}
			}
			return null;
		}
		
		public static String getAlias(Integer code){
			if(code!=null){
				for(ORDER_STATUS entry:ORDER_STATUS.values()){
					if(entry.code.intValue()==code){
						return entry.alias;
					}
				}
			}
			return null;
		}
		
		public static Integer getCode(String cnName){
			for(ORDER_STATUS entry:ORDER_STATUS.values()){
				if(entry.cnName.equals(cnName) || entry.alias.equals(cnName)){
					return entry.code;
				}
			}
			return null;
		}
		ORDER_STATUS(Integer code,String cnName,String alias){
			this.code = code;
			this.cnName = cnName;
			this.alias = alias;
		}
		
		public static ORDER_STATUS[] getCurrentStatus(){
			ORDER_STATUS[] array= ORDER_STATUS.values();
			array = (ORDER_STATUS[]) ArrayUtils.remove(array, 0);
			return array;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
	
	/** 是否需要发票：0-不需要发票，1-需要发票 */
	public enum NEED_INVOICE{
		FALSE(0,"不需要"),
		TRUE(1,"需要");
		
		public int code;
		public String cnName;
		
		NEED_INVOICE(int code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
	}
	/**
	 * 订单详情中是否已退款
	 * @author szy
	 *
	 */
	public enum REFUND_STATUS{
		STATUS_0(0,"未退款"),
		STATUS_1(1,"已退款");
		
		private Integer code;
		private String cnName;
		
		private REFUND_STATUS(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
	
	/**
	 * 订单详情中是否已点评
	 * @author szy
	 *
	 */
	public enum COMMENT_STATUS{
		STATUS_0(0,"未点评"),
		STATUS_1(1,"已点评");
		
		private Integer code;
		private String cnName;
		
		private COMMENT_STATUS(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
	
	public enum GROUP_SALE_STATUS{
		GROUP_SALE_STATUS_0(0,"初始"),
		GROUP_SALE_STATUS_1(1,"正在进行"),
		GROUP_SALE_STATUS_2(2,"已成功"),
		GROUP_SALE_STATUS_3(3,"以失败"),
		GROUP_SALE_STATUS_4(4,"已结束");
		
		private Integer code;
		private String cnName;
		
		private GROUP_SALE_STATUS(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code){
			if(code!=null){
				for(GROUP_SALE_STATUS entry:GROUP_SALE_STATUS.values()){
					if(entry.code.intValue()==code){
						return entry.cnName;
					}
				}
			}
			return null;
		}
		
	}
	
	/**
	 * 订单详情中商品版本号
	 */
public static String ITEMVERSION="1.0";

// /** 订单类型（1：一般订单（入仓）。2：平台（不入仓））。3：海淘*/
	public final static int GENERAL_ORDER = 1;
	public final static int PLATFORM_ORDER = 2;
	public final static int SEA_ORDER = 3;

	/** 逻辑删除 - 是 */
	public final static Integer DELETED_TRUE = 1;
	/** 逻辑删除 - 否 */
	public final static Integer DELETED_FALSE = 2;
	
	/**
	 * 子订单类型
	 * 
	 * @author szy
	 * @version 0.0.1
	 */
	public enum OrderType {
		/**1-国内自营*/
		COMMON(StorageType.COMMON.value, StorageType.COMMON.getName()),/**2-国内平台*/
		PLATFORM (StorageType.PLATFORM.value, StorageType.PLATFORM.getName()),/**3-国内直发*/
		DOMESTIC (StorageType.DOMESTIC.value, StorageType.DOMESTIC.getName()),/**4-保税区*/
		BONDEDAREA  (StorageType.BONDEDAREA.value, StorageType.BONDEDAREA.getName()),/**5-境外直发*/
		OVERSEASMAIL (StorageType.OVERSEASMAIL.value, StorageType.OVERSEASMAIL.getName()),/**6-海淘自营*/
		COMMON_SEA(StorageType.COMMON_SEA.value, StorageType.COMMON_SEA.getName()),/**7-速购*/
		FAST(StorageType.FAST.value, StorageType.FAST.getName()),
		BUY_COUPONS(StorageType.BUY_COUPONS.value, StorageType.BUY_COUPONS.getName()),//团购券
		WORLD(StorageType.WORLD.value,StorageType.WORLD.getName());//微店
		
		public Integer code;
		public String cnName;

		OrderType(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static OrderType getOrderTypeByCode(int code) {
			for (OrderType entry : OrderType.values()) {
				if (entry.code.equals(code)) {
					return entry;
				}
			}
			
			return null;
		}

		public static String getCnName(Integer code) {
			for (OrderType entry : OrderType.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getCnName() {
			return cnName;
		}

		public void setCnName(String cnName) {
			this.cnName = cnName;
		}

	}

		/**
		 * 海关审核状态
		 * 
		 * @author szy
		 * @version 0.0.1
		 */
	public enum PutStatus {
		
		/** 0 - 等待推送 */
		NEW(0, "等待推送"),
		/** 1 - 已推送 */
		PUT(1, "已推送"),
		/** 2 - 推送失败 */
		PUT_FAIL(2, "推送失败"),
		/** 3 - 审核成功 */
		VERIFY_SUCCUESS(3, "审核成功"),
		/** 4 - 审核失败 */
		VERIFY_FAIL(4, "审核失败");
		
		public Integer code;
		public String cnName;
		
		PutStatus(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code) {
			for (PutStatus entry : PutStatus.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getCnName() {
			return cnName;
		}

		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
		
	}
	
	/**
	 * 订单支付方式
	 * 
	 * @author szy
	 * @version 0.0.1
	 */
	public enum OrderPayType{
		
		/** 1 - 在线支付 */
		ONLINE(1, "在线支付"),
		/** 2 - 货到付款 */
		COD(2, "货到付款");
		
		public Integer code;
		public String cnName;
		
		OrderPayType(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code) {
			for (OrderPayType entry : OrderPayType.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}
	}

	/**
	 * 订单是否开发票
	 * 
	 * @author szy
	 * @version 0.0.1
	 */
	public enum OrderIsReceipt {
		
		/** 1 - 开票 */
		YES(1, "开票"),
		/** 0 - 不开票 */
		NO(0, "不开票");
		
		public Integer code;
		public String cnName;
		
		OrderIsReceipt(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code) {
			for (OrderIsReceipt entry : OrderIsReceipt.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}
	}

	/**
	 * 订单是否逻辑删除
	 * 
	 * @author szy
	 * @version 0.0.1
	 */
	public enum OrderIsDeleted {
		
		/** 1 - 删除 */
		YES(1, "删除"),
		/** 2 - 不删除 */
		NO(2, "不删除");
		
		public Integer code;
		public String cnName;
		
		OrderIsDeleted(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code) {
			for (OrderIsDeleted entry : OrderIsDeleted.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}
	}
	
	/**
	 * 订单支付途径
	 * 
	 * @author szy
	 * @version 0.0.1
	 */
	public enum OrderPayWay{
		
		/** 1 - 支付宝支付 */
		ALIPAY(1, "支付宝支付"),
		/** 2 - 银联支付 */
		UNIONPAY(2, "银联支付"),
		/** 3 - 快钱 */
		BILL99(3, "快钱"),
		/** 4 - 微信支付 */
		WEIXIN(4, "微信支付"),
		/** 5 - 招商银行 */
		CMB(5, "招商银行"),
		/** 6 - 支付宝国际 */
		ALIINTERNATIONAL(6, "支付宝国际"),		
		/** 7 - 支付宝合并支付 */
		MERGEALIPAY(7, "支付宝合并支付"),
		/** 8 - 微信境外支付 */
		WEIXININTERNATIONAL(8, "微信境外支付");
		
		public Integer code;
		public String cnName;
		
		OrderPayWay(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code) {
			for (OrderPayWay entry : OrderPayWay.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}
	}
	
	/**
	 * 商品销售类型（商品、赠品）
	 * @author szy
	 *
	 */
	public enum ITEM_SALES_TYPE{
		NO_GIFT(1,"商品"),
		GIFT(2,"赠品");
		
		public Integer code;
		public String cnName;
		
		ITEM_SALES_TYPE(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code) {
			for (ITEM_SALES_TYPE entry : ITEM_SALES_TYPE.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}
	}
	
	public enum CHANNEL_CODE{
		// 西客商城
		xg,
		// 民生银行公众号
		cmbc
	}
	
	/***
	 * 清关状态审核
	 */
	public enum ClearanceStatus{
		NEW(0, "等待推送"),
		PUT_SUCCESS(1, "推送成功"),
		PUT_FAILED(2, "推送失败"),
		AUDIT_SUCCESS(3, "审核成功（清关通过）"),
		AUDIT_FAILED(4, "审核失败（清关失败）");
		
		public Integer code;
		public String desc;
		
		ClearanceStatus(Integer code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public static boolean isNew(Integer status){
			if (NEW.code.equals(status)) return true;
			return false;
		}
		
		public static boolean hasAudit(Integer status){
			if (AUDIT_SUCCESS.code.equals(status) || AUDIT_FAILED.code.equals(status)){
				return true;
			}
			return false;
		}
		
		public static String getClearanceDescByCode(Integer code){
			for (ClearanceStatus status : ClearanceStatus.values()) {
				if(status.getCode().equals(code)){
					return status.desc;
				}
			}
			return null;	
		}
		
		public static ClearanceStatus getClearanceStatusByCode(Integer code){
			for (ClearanceStatus status : ClearanceStatus.values()) {
				if(status.getCode().equals(code)){
					return status;
				}
			}
			return null;
		}
		
		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
	public enum PutCustomsType{
		NEW(0,  "新建"),
		ORDER_DECLARE(1, "订单申报"),
		WAYBILL_DECLARE(2, "运单申报"),
		PERSONALGOODS_DECLARE(3, "个人物品申报"),
		PAY_DECLARE(4, "支付单申报"),
		CLEARANCE(5, "清关状态");

		
		public int index;
		public String desc;
		
		public static PutCustomsType getPutCustomsTypeByIndex(Integer idx){
			if(idx == null) return null;
			for(PutCustomsType type : PutCustomsType.values()){
				if(type.getIndex() == idx){
					return type;
				}
			}
			return null;
		}
		
		private PutCustomsType(int index, String desc) {
			this.index = index;
			this.desc = desc;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
	//单据申报状态
	public enum PutCustomsStatus{	
		INIT(0, "初始状态"),
		SUCCESS(1, "推送成功"),
		FAIL(2, "推送失败"),
		AUDIT_SUCCESS(3, "审核通过"),
		AUDIT_FAIL(4, "审核不通过");
		
		public Integer code;
		public String 	desc;
		
		private PutCustomsStatus(Integer code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		
		public static boolean isAuditSuccess(Integer status){
			if (AUDIT_SUCCESS.code.equals(status)) return true;
			return false;
		}
		
		public static boolean isSuccess(Integer status){
			if (SUCCESS.code.equals(status)) return true;
			return false;
		}
		
		public static boolean isInit(Integer status){
			if (INIT.code.equals(status)) return true;
			return false;
		}
		
		public static boolean hasAudit(Integer status){
			if (AUDIT_SUCCESS.code.equals(status) || AUDIT_FAIL.code.equals(status)){
				return true;
			}
			return false;
		}
		public static PutCustomsStatus getCustomsStatusByCode(Integer code){
			for (PutCustomsStatus status : PutCustomsStatus.values()) {
				if (status.getCode().equals(code)) {
					return status;
				}
			}
			return null;
		}
		public static String getCustomsStatusDescByCode(Integer code){
			for (PutCustomsStatus status : PutCustomsStatus.values()) {
				if (status.getCode().equals(code)) {
					return status.desc;
				}
			}
			return null;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		
	}
	
	
	//对接海关
	public enum DeclareCustoms{
		JKF("JKF", 6l, "浙江杭州电子口岸"),
		CEB("CEB", 10l, "海关总署");
		
		public String code;
		public Long channelid;
		public String desc;
		
		public static String getCodeByChannelId(Long channelId){
			for(DeclareCustoms customs : DeclareCustoms.values()){
				if (customs.getChannelid().equals(channelId))
					return customs.getCode();
			}
			return null;
		}
		
		private DeclareCustoms(String code, Long channelid, String desc) {
			this.code = code;
			this.channelid = channelid;
			this.desc = desc;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public Long getChannelid() {
			return channelid;
		}

		public void setChannelid(Long channelid) {
			this.channelid = channelid;
		}
	}
	
	//订单海关取消状态
	public enum CancelCustomsOrderStatus{
		PUSH_SUCCESS(1, "推送成功"),
		PUSH_FAILED(2, "推送失败"),
		AUDIT_SUCCESS(3, "审核通过"),
		AUDIT_FAILED(4, "审核失败");
		
		public Integer code;
		public String desc;
		
		private CancelCustomsOrderStatus(Integer code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}		
	}
	//快递公司对应
	public enum directOrderLogistics{
		YTO("YTO","yuantong"),
		ZTO("ZTO","zhongtong"),
		STO("STO","shentong"),
		YUNDA("YUNDA","yunda"),
		SF("SF","shunfeng"),
		EMS("EMS","ems");
		
		public String code;
		public String desc;
		
		private directOrderLogistics(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}		
	}
}
