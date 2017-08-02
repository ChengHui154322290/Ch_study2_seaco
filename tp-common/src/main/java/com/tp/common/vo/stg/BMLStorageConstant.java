/**
 * 
 */
package com.tp.common.vo.stg;

import org.apache.commons.lang3.StringUtils;


/**
 * @author Administrator
 *
 */
public class BMLStorageConstant {
	
	/**
	 * 仓库编号
	 */
	public static final String WAREHOUSE_CODE = "BML_KSWH";

	/**
	 * 标杆仓库系统反馈接入秘钥
	 */
	public static final String WAREHOUSE_SECRETKEY = "BML@Seagoor";
	
	
	/**
	 * 入库单类型编码表
	 *
	 */
	public enum InputOrderType {
		
		IR("IR","调拨入库"),
		FG("FG","正常入库"),
		CF("CF","拆分入库"),
		OT("OT","加工入库"),
		RR("RR","退货入库"),
		MF("MF","特殊入库（免费）");
		
		private String code;
		
		private String desc;
		
		InputOrderType(String code,String desc){
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
			InputOrderType[] values = InputOrderType.values();
			for (InputOrderType item : values) {
				if(item.getCode().equals(code)){
					return item.getDesc();
				}
			}
			return null;
		}
	}
	
	public enum CancelOrderCode {

		E000("000","取消成功"),
		E002("002","取消失败，订单已发运或者订单不存在"),
		E003("003","取消失败，订单进入仓内作业（此时订单可拦截，可根据客户预设，视为取消成功或者失败）"),
		E009("009","取消失败，取消时发生异常");
		
		private String code;
		
		private String desc;
		
		CancelOrderCode(String code,String desc){
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
			CancelOrderCode[] values = CancelOrderCode.values();
			for (CancelOrderCode item : values) {
				if(item.getCode().equals(code)){
					return item.getDesc();
				}
			}
			return null;
		}
	}
	
	/**
	 * 标杆系统错误码
	 * @author szy
	 * 2015年1月11日 下午1:22:48
	 *
	 */
	public enum WHErrorCode {

		E001("001","密钥检查未通过"),
		E002("002","解析xml内容失败"),
		E003("003","没有读取到跟节点"),
		E004("004","没有解析到节点信息"),
		E005("005","数据重复"),
		E006("006","必填项为空"),
		E007("007","sku不存在"),
		E008("008","保存失败"),
		E009("009","订单不存在");
		
		private String code;
		
		private String desc;
		
		WHErrorCode(String code,String desc){
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
			WHErrorCode[] values = WHErrorCode.values();
			for (WHErrorCode item : values) {
				if(item.getCode().equals(code)){
					return item.getDesc();
				}
			}
			return null;
		}
	}
	
	/**
	 * 标杆快递商码表
	 * @author szy
	 * 2015年1月11日 上午11:09:46
	 *
	 */
	public enum ExpressType {
		SF("SF","顺丰快运"),
		YTO("YTO","圆通速递"),
		STO("STO","申通E物流"),
		YUNDA("YUNDA","韵达快运"),
		EMS("EMS","EMS"),
		YCT("YCT","宅急送"),
		ZJS("ZJS","黑猫宅急便");
		
		private String code;
		
		private String desc;
		
		ExpressType(String code,String desc){
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
			ExpressType[] values = ExpressType.values();
			for (ExpressType item : values) {
				if(item.getCode().equals(code)){
					return item.getDesc();
				}
			}
			return null;
		}
	}
	
	/**
	 * 入库单状态码表
	 * @author szy
	 * 2015年1月11日 下午1:27:33
	 *
	 */
	public enum InputOrderStatus {

		S00("00","订单创建"),
		S10("10","已码盘"),
		S30("20","部分收货"),
		S40("30","完全收货"),
		S90("40","收货取消"),
		S98("98","等待释放"),
		S99("99","ASN关闭");
		
		private String code;
		
		private String desc;
		
		InputOrderStatus(String code,String desc){
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
			InputOrderStatus[] values = InputOrderStatus.values();
			for (InputOrderStatus item : values) {
				if(item.getCode().equals(code)){
					return item.getDesc();
				}
			}
			return null;
		}
	}
	
	/**
	 * 出库订单状态码表
	 *
	 */
	public enum OutputOrderStatus {
		OP00("00","创建订单"),
		OP01("01","地址待审"),
		OP02("02","重复待审"),
		OP10("10","部分预配"),
		OP20("20","预配完成"),
		OP30("30","部分分配"),
		OP40("40","分配完成"),
		OP50("50","部分拣货"),
		OP60("60","拣货完成"),
		OP62("62","部分装箱"),
		OP63("63","完全装箱"),
		OP65("65","部分装车"),
		OP66("66","装车完成"),
		OP70("70","部分发运"),
		OP80("80","完全发运"),
		OP90("90","订单取消"),
		OP98("98","等待释放"),
		OP99("99","订单完成");
		
		private String code;
		
		private String desc;
		
		OutputOrderStatus(String code,String desc){
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
			OutputOrderStatus[] values = OutputOrderStatus.values();
			for (OutputOrderStatus item : values) {
				if(item.getCode().equals(code)){
					return item.getDesc();
				}
			}
			return null;
		}	
	}
}
