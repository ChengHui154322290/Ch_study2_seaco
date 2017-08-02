/**
 * 
 */
package com.tp.common.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * @author szy
 *
 */
public class StorageConstant {

	
	public static final Integer SEND_ORDER_WMS_MAX_FAIL_TIMES=3;
	
	public interface MailProperties {
		String MAIL_HOST = "mail.smtp.host";
		String MAIL_AUTH = "mail.smtp.auth";
		String MAIL_TIMEOUT = "mail.smtp.timeout";
		String MAIL_NOTIFY_TITLE_FG="标杆采购入库服务接口调用失败";
	}
	
	public static final String straight = "-";
	

	
	/**
	 * 定义可以从仓储系统中获得库存分配的app
	 *
	 */
	public enum App {
		/** 活动 */
		PROMOTION("PROMOTION"),
		PURCHASE("PURCHASE");
		
		private String name;
		App(String name){
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}	
	}
	
	/**
	 * 库存归还状态
	 * @author szy
	 *
	 */
	public enum DistributeBackedStatus {
		
		backed(1,"已还库"),
		unbacked(0,"未还库");
		
		private Integer status;
		
		private String desc;
		
		DistributeBackedStatus(Integer status,String desc){
			this.status = status;
			this.desc = desc;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
	/**
	 * 现货库存变化流水类型
	 *
	 */
	public enum InputAndOutputType {

		SO("SO","销售出库"),
		RO("RO","退货出库"),
	    CO("CO","采购入库"),
	    /*适用于活动直接存入虚拟库存*/
	    PO("PO","活动入库"),
	    PH("PH","活动还库"),
	    PD("PD", "活动预扣"),
	    PXS("PXS","活动销售"),
	    QX("QX","取消订单"),
	    PC("PC","初始化库存"),
	    PA("PA","盘盈"),
	    PR("PR","盘亏");
		private String code;
		
		private String desc;
		
		InputAndOutputType(String code,String desc){  
			this.code=code;
			this.desc=desc;
		}
		static Map<String, String> addInventoryTypes = new HashMap<String, String>();
		
		static{
			addInventoryTypes.put(InputAndOutputType.CO.getCode(), "add");
			addInventoryTypes.put(InputAndOutputType.PO.getCode(), "add");
			addInventoryTypes.put(InputAndOutputType.QX.getCode(), "add");
			addInventoryTypes.put(InputAndOutputType.PC.getCode(), "add");
			addInventoryTypes.put(InputAndOutputType.PA.getCode(), "add");
		}
		
		public static boolean isOrderCancel(InputAndOutputType type){
			if(InputAndOutputType.QX == type){
				return true;
			}
			return false;
		}
		
		public static boolean isOrderDelivery(InputAndOutputType type){
			if(InputAndOutputType.SO == type || InputAndOutputType.PXS == type){
				return true;
			}
			return false;
		}
		
		public boolean isAddInventory(){
			String add = addInventoryTypes.get(this.code);
			if(null==add){
				return false;
			}
			return true;
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
		
		public static String getDescByCode(String code){
			if(StringUtils.isBlank(code)){
				return null;
			}
			InputAndOutputType[] values = InputAndOutputType.values();
			for (InputAndOutputType item : values) {
				if(item.getCode().equals(code)){
					return item.getDesc();
				}
			}
			return null;
		}
	}
	/**
	 * 出库单类型编码表
	 *
	 */
	public enum OutputOrderType {

		TT("TT","退货出库"),
		CM("CM","销售出库");
		
		private String code;
		
		private String desc;
		
		OutputOrderType(String code,String desc){
			this.code=code;
			this.desc=desc;
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
		
		public static String getDescByCode(String code){
			if(StringUtils.isBlank(code)){
				return null;
			}
			OutputOrderType[] values = OutputOrderType.values();
			for (OutputOrderType item : values) {
				if(item.getCode().equals(code)){
					return item.getDesc();
				}
			}
			return null;
		}
	}
	
	/**
	 * 仓库类型
	 * 1-普通(自营) 2-平台  3-国内直发  4-保税区 5-境外直发
	 * @author szy
	 *
	 */
	public enum StorageType {

		
		COMMON("普通(自营)",1),
		PLATFORM ("平台",2),
		DOMESTIC ("国内直发",3),
		BONDEDAREA  ("保税区",4),
		OVERSEASMAIL ("境外直发",5),
		COMMON_SEA("海淘(自营)", 6),/**7-速购*/
		FAST("速购",7),
		BUY_COUPONS("团购券",8),
		WORLD("微店",9);
		
		public String name;
		public Integer value;
		
		
		StorageType(String name, Integer value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getValue() {
			return value;
		}
		public void setValue(Integer value) {
			this.value = value;
		}



		public static String getValueByName(String name){
			if(StringUtils.isBlank(name)){
				return null;
			}
			StorageType[] values = StorageType.values();
			for (StorageType item : values) {
				if(item.getName().equals(name)){
					return item.getName();
				}
			}
			return null;
		}
	}

	
	 /**
	 * 下单完成后向仓库发送发货出库订单 常量
	 */
	/**加急*/
	public static final String PRIORITY_URGENT="加急";
	/**普通*/
	public static final String PRIORITY_NORMAL="普通";
	/**无需开票*/
	public static final String INVOICE_NO="无需开票";
	/**需要开票*/
	public static final String INVOICE_REQ="需要开票";
	/**需开专票*/
	public static final String INVOICE_REQSPEC="需开专票";
	/**在线支付*/
	public static final String PAYMENT_ONLINE="在线支付";
	/** 货到付款*/
	public static final String PAYMENT_OFFONLINE="货到付款";
	
	/**
	 * MQ
	 */
	/** 销售订单出库消息队列 */
	public static final String STORAGE_SALESORDER_OUTPUT_TASK_QUEUE_P2P="storage_salesorder_output_task_queue_p2p";
	
	/** 采购入库消息队列 */
	public static final String STORAGE_SUPPLIER_INPUT_QUEUE_P2P="storage_supplier_input_queue_p2p";
	
	/** 采购退货出库消息队列 */
	public static final String STORAGE_SUPPLIER_OUTPUT_QUEUE_P2P="storage_supplier_output_queue_p2p";
	
	/** 发票信息队列 */
	public static final String STORAGE_SALESORDER_INVOICE_TASK_QUEUE_P2P="storage_salesorder_invoice_task_queue_p2p";
	
	/**
	 * 缓存
	 */
	/**仓库缓存key**/
	public static final String STORAGE_WAREHOUSE_PRE_="warehouse-";
	

	
	/**
	 * 
	 * @author szy
	 * 发票类型
	 *
	 */
	public enum InvoiceType {

		PT("PT","普通发票"),
		ZZ("ZZ","增值税专用发票");
		private String code;
		
		private String desc;
		

		InvoiceType(String code,String desc){  
			this.code=code;
			this.desc=desc;
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
		
		public static String getDescByCode(String code){
			if(StringUtils.isBlank(code)){
				return null;
			}
			InvoiceType[] values = InvoiceType.values();
			for (InvoiceType item : values) {
				if(item.getCode().equals(code)){
					return item.getDesc();
				}
			}
			return null;
		}

	}
		
	/****
	 * 
	 * 库存盘点常量类
	 * @author szy
	 *
	 */
	public interface AdjustInventoryConstant{
		/***盘点增加库存***/
		static final Integer INCREASE_TYPE = 1;
		
		/***盘点减少库存***/
		static final Integer DECREASE_TYPE = 2;
		
		/***库存初始化***/
		static final Integer INIT = 3;
	}

}
