package com.tp.common.vo.wms;

import java.util.HashMap;
import java.util.Map;

public enum StockOutColsEnum {
	STORER("storer", "货主编码"),
	WMWHSEID("wmwhseid", "仓库代码"),
	EXTERNAL_NO("externalNo", "订单号（电子口岸/电商）"),
	EXTERNAL_NO2("externalNo2", "订单号（电商）"),
	SHIP_TO_NAME("shipToName", "收货人姓名"),
	SHIP_TO_PHONE("shipToPhone", "收货人联系电话"),
	USER_NAME("userName", "买家用户名"),
	BILL_DATE("billDate", "下单时间"),
	PAYMENT_DATE_TIME("paymentDateTime", "付款时间"),
	RECEIP_TYPE("receipType", "订单分类"),
	PROVINCE_NAME("provinceName", "省名"),
	CITY_NAME("cityName", "市名"),
	REGION_NAME("regionName", "区/县名"),
	SHIP_TO_ADDR("shipToAddr", "收货详细地址"),
	SHIP_TO_PASS_CODE("shipToPassCode", "物流公司代码"),
	ZIP_CODE("zipCode", "邮编"),
	DS_PLATFORM("dsPlatform", "电商平台"),
	DS_STORER("dsStorer", "店铺代码"),
	CARRIER_KEY("carrierKey", "承运商代码"),
	EXPRESS_ID("expressId", "快递单号"),
	EXPRESS_TYPE("expressType", "快递类型 "),
	PAYMENT("payment", "实际支付金额"),
	POST_FEE("postFee", "运费"),
	BIZ_TYPE("bizType", "集备货模式标记字段"),
	TDQ("tdq", "明细条数"),
	CREATE_TIME("createTime", "创建时间"),
	STATUS("status", "状态");
	
	private String beanCode;
	private String beanName;
	
    public static final Map<String, String> stockOutFieldMap = new HashMap<String, String>();

    static {
        /**
         * 字段和名称map
         */
        for (final StockOutColsEnum field : StockOutColsEnum.values()) {
        	stockOutFieldMap.put(field.getBeanCode(), field.getBeanName());
        }
    }

	public String getBeanCode() {
		return beanCode;
	}

	public void setBeanCode(String beanCode) {
		this.beanCode = beanCode;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	StockOutColsEnum(String beanCode, String beanName) {
		this.beanCode = beanCode;
		this.beanName = beanName;
	}

	
}
