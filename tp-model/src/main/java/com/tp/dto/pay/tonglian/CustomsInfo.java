package com.tp.dto.pay.tonglian;

import java.io.Serializable;

public class CustomsInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String CUSTOMS_TYPE="";//海关类别
	private String ESHOP_ENT_CODE="";//电商企业类别
	private String ESHOP_ENT_NAME="";//电商企业名称
	private String BIZ_TYPE_CODE="";//空  业务类型
	private String APP_UID="";//空  用户编号
	private String APP_UNAME="";//空  用户名称
	private String TOTAL_FEE="";//支付总额
	private String GOODS_FEE="";//商品货款金额
	private String TAX_FEE="";//税款金额
	private String FREIGHT_FEE="";//运费
	private String OTHER_FEE="";//空  其他费用
	private String IETYPE="";//空  进出口标识
	private String ORIGINAL_ORDER_NO="";//原始订单编号
	private String PAY_TIME="";//付款时间
	private String CURRENCY="";//支付币制
	private String PAYER_NAME="";//支付人姓名
	private String PAPER_TYPE="";//支付人证件类型
	private String PAPER_NUMBER="";//支付人证件号码
	private String PAPER_PHONE="";//为空
	private String PAPER_EMAIL="";//为空
	private String PAY_BANK_NAME="";//为空
	private String PAY_BANK_CODE="";//为空
	private String PAY_BANK_SERIALNO="";//为空
	private String PAYER_COUNTRY_CODE="";//为空
	private String PAYER_ADDRESS="";//为空
	private String PAYER_SEX="";//为空
	private String PAYER_BIRTHDAY="";//为空
	private String CHECK_ECP_CODE="";//为空
	private String CHECK_ECP_NAME="";//为空
	private String ORG_CODE="";//为空
	private String PAY_CARD_NO="";//为空
	private String SHIPPER_NAME="";//为空
	private String IS_CHECK="";//为空
	private String MEMO="";//（非必输）备注（支付信息的备注）
	
	public String getCUSTOMS_TYPE() {
		return CUSTOMS_TYPE;
	}
	public void setCUSTOMS_TYPE(String cUSTOMS_TYPE) {
		CUSTOMS_TYPE = cUSTOMS_TYPE;
	}
	public String getESHOP_ENT_CODE() {
		return ESHOP_ENT_CODE;
	}
	public void setESHOP_ENT_CODE(String eSHOP_ENT_CODE) {
		ESHOP_ENT_CODE = eSHOP_ENT_CODE;
	}
	public String getESHOP_ENT_NAME() {
		return ESHOP_ENT_NAME;
	}
	public void setESHOP_ENT_NAME(String eSHOP_ENT_NAME) {
		ESHOP_ENT_NAME = eSHOP_ENT_NAME;
	}
	public String getBIZ_TYPE_CODE() {
		return BIZ_TYPE_CODE;
	}
	public void setBIZ_TYPE_CODE(String bIZ_TYPE_CODE) {
		BIZ_TYPE_CODE = bIZ_TYPE_CODE;
	}
	public String getAPP_UID() {
		return APP_UID;
	}
	public void setAPP_UID(String aPP_UID) {
		APP_UID = aPP_UID;
	}
	public String getAPP_UNAME() {
		return APP_UNAME;
	}
	public void setAPP_UNAME(String aPP_UNAME) {
		APP_UNAME = aPP_UNAME;
	}
	public String getTOTAL_FEE() {
		return TOTAL_FEE;
	}
	public void setTOTAL_FEE(String tOTAL_FEE) {
		TOTAL_FEE = tOTAL_FEE;
	}
	public String getGOODS_FEE() {
		return GOODS_FEE;
	}
	public void setGOODS_FEE(String gOODS_FEE) {
		GOODS_FEE = gOODS_FEE;
	}
	public String getTAX_FEE() {
		return TAX_FEE;
	}
	public void setTAX_FEE(String tAX_FEE) {
		TAX_FEE = tAX_FEE;
	}
	public String getFREIGHT_FEE() {
		return FREIGHT_FEE;
	}
	public void setFREIGHT_FEE(String fREIGHT_FEE) {
		FREIGHT_FEE = fREIGHT_FEE;
	}
	public String getOTHER_FEE() {
		return OTHER_FEE;
	}
	public void setOTHER_FEE(String oTHER_FEE) {
		OTHER_FEE = oTHER_FEE;
	}
	public String getIETYPE() {
		return IETYPE;
	}
	public void setIETYPE(String iETYPE) {
		IETYPE = iETYPE;
	}
	public String getORIGINAL_ORDER_NO() {
		return ORIGINAL_ORDER_NO;
	}
	public void setORIGINAL_ORDER_NO(String oRIGINAL_ORDER_NO) {
		ORIGINAL_ORDER_NO = oRIGINAL_ORDER_NO;
	}
	public String getPAY_TIME() {
		return PAY_TIME;
	}
	public void setPAY_TIME(String pAY_TIME) {
		PAY_TIME = pAY_TIME;
	}
	public String getCURRENCY() {
		return CURRENCY;
	}
	public void setCURRENCY(String cURRENCY) {
		CURRENCY = cURRENCY;
	}
	public String getPAYER_NAME() {
		return PAYER_NAME;
	}
	public void setPAYER_NAME(String pAYER_NAME) {
		PAYER_NAME = pAYER_NAME;
	}
	public String getPAPER_TYPE() {
		return PAPER_TYPE;
	}
	public void setPAPER_TYPE(String pAPER_TYPE) {
		PAPER_TYPE = pAPER_TYPE;
	}
	public String getPAPER_NUMBER() {
		return PAPER_NUMBER;
	}
	public void setPAPER_NUMBER(String pAPER_NUMBER) {
		PAPER_NUMBER = pAPER_NUMBER;
	}
	public String getPAPER_PHONE() {
		return PAPER_PHONE;
	}
	public void setPAPER_PHONE(String pAPER_PHONE) {
		PAPER_PHONE = pAPER_PHONE;
	}
	public String getPAPER_EMAIL() {
		return PAPER_EMAIL;
	}
	public void setPAPER_EMAIL(String pAPER_EMAIL) {
		PAPER_EMAIL = pAPER_EMAIL;
	}
	public String getPAY_BANK_NAME() {
		return PAY_BANK_NAME;
	}
	public void setPAY_BANK_NAME(String pAY_BANK_NAME) {
		PAY_BANK_NAME = pAY_BANK_NAME;
	}
	public String getPAY_BANK_CODE() {
		return PAY_BANK_CODE;
	}
	public void setPAY_BANK_CODE(String pAY_BANK_CODE) {
		PAY_BANK_CODE = pAY_BANK_CODE;
	}
	public String getPAY_BANK_SERIALNO() {
		return PAY_BANK_SERIALNO;
	}
	public void setPAY_BANK_SERIALNO(String pAY_BANK_SERIALNO) {
		PAY_BANK_SERIALNO = pAY_BANK_SERIALNO;
	}
	public String getPAYER_COUNTRY_CODE() {
		return PAYER_COUNTRY_CODE;
	}
	public void setPAYER_COUNTRY_CODE(String pAYER_COUNTRY_CODE) {
		PAYER_COUNTRY_CODE = pAYER_COUNTRY_CODE;
	}
	public String getPAYER_ADDRESS() {
		return PAYER_ADDRESS;
	}
	public void setPAYER_ADDRESS(String pAYER_ADDRESS) {
		PAYER_ADDRESS = pAYER_ADDRESS;
	}
	public String getPAYER_SEX() {
		return PAYER_SEX;
	}
	public void setPAYER_SEX(String pAYER_SEX) {
		PAYER_SEX = pAYER_SEX;
	}
	public String getPAYER_BIRTHDAY() {
		return PAYER_BIRTHDAY;
	}
	public void setPAYER_BIRTHDAY(String pAYER_BIRTHDAY) {
		PAYER_BIRTHDAY = pAYER_BIRTHDAY;
	}
	public String getCHECK_ECP_CODE() {
		return CHECK_ECP_CODE;
	}
	public void setCHECK_ECP_CODE(String cHECK_ECP_CODE) {
		CHECK_ECP_CODE = cHECK_ECP_CODE;
	}
	public String getCHECK_ECP_NAME() {
		return CHECK_ECP_NAME;
	}
	public void setCHECK_ECP_NAME(String cHECK_ECP_NAME) {
		CHECK_ECP_NAME = cHECK_ECP_NAME;
	}
	public String getORG_CODE() {
		return ORG_CODE;
	}
	public void setORG_CODE(String oRG_CODE) {
		ORG_CODE = oRG_CODE;
	}
	public String getPAY_CARD_NO() {
		return PAY_CARD_NO;
	}
	public void setPAY_CARD_NO(String pAY_CARD_NO) {
		PAY_CARD_NO = pAY_CARD_NO;
	}
	public String getSHIPPER_NAME() {
		return SHIPPER_NAME;
	}
	public void setSHIPPER_NAME(String sHIPPER_NAME) {
		SHIPPER_NAME = sHIPPER_NAME;
	}
	public String getIS_CHECK() {
		return IS_CHECK;
	}
	public void setIS_CHECK(String iS_CHECK) {
		IS_CHECK = iS_CHECK;
	}
	public String getMEMO() {
		return MEMO;
	}
	public void setMEMO(String mEMO) {
		MEMO = mEMO;
	}
	
	


}
