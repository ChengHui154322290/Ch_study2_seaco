package com.tp.model.ord.JKF;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *	电商平台发送商品订单数据到通关服务平台 报文结构
 */
@XmlRootElement(name="mo")
@XmlAccessorType(XmlAccessType.FIELD)
public class JkfImportOrderRequest extends JkfBaseDO{
	
	private static final long serialVersionUID = 6108927010433179192L;
	
	private Head head = new Head();
	private Body body = new Body();
	
	@XmlAttribute
	private String version = "1.0.0";
	
	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}
	
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
	
	public static class Head implements Serializable{
		private static final long serialVersionUID = 7102315445110506288L;
		private String businessType;

		public String getBusinessType() {
			return businessType;
		}

		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}	
	}
	
	public static class Body implements Serializable{
		private static final long serialVersionUID = 6294428287922172277L;
		private List<OrderInfo> orderInfoList = new ArrayList<JkfImportOrderRequest.OrderInfo>();

		@XmlElementWrapper(name="orderInfoList")
		@XmlElement(name="orderInfo")
		public List<OrderInfo> getOrderInfoList() {
			return orderInfoList;
		}

		public void setOrderInfoList(List<OrderInfo> orderInfoList) {
			this.orderInfoList = orderInfoList;
		}			
	}	
	
	/** 订单申报信息  **/
	public static class OrderInfo implements Serializable{
		private static final long serialVersionUID = -5414207226994567063L;
		private JkfSign jkfSign = new JkfSign();
		private JkfOrderImportHead jkfOrderImportHead = new JkfOrderImportHead();
		private List<JkfOrderDetail> jkfOrderDetailList = new ArrayList<JkfImportOrderRequest.JkfOrderDetail>();
		private JkfGoodsPurchaser jkfGoodsPurchaser = new JkfGoodsPurchaser();
		
		public JkfSign getJkfSign() {
			return jkfSign;
		}
		public void setJkfSign(JkfSign jkfSign) {
			this.jkfSign = jkfSign;
		}

		public JkfOrderImportHead getJkfOrderImportHead() {
			return jkfOrderImportHead;
		}
		public void setJkfOrderImportHead(JkfOrderImportHead jkfOrderImportHead) {
			this.jkfOrderImportHead = jkfOrderImportHead;
		}

		@XmlElementWrapper(name="jkfOrderDetailList")
		@XmlElement(name="jkfOrderDetail")
		public List<JkfOrderDetail> getJkfOrderDetailList() {
			return jkfOrderDetailList;
		}
		public void setJkfOrderDetailList(List<JkfOrderDetail> jkfOrderDetailList) {
			this.jkfOrderDetailList = jkfOrderDetailList;
		}

		public JkfGoodsPurchaser getJkfGoodsPurchaser() {
			return jkfGoodsPurchaser;
		}
		public void setJkfGoodsPurchaser(JkfGoodsPurchaser jkfGoodsPurchaser) {
			this.jkfGoodsPurchaser = jkfGoodsPurchaser;
		}	
	}
	
	/**签名信息**/
	public static class JkfSign implements Serializable{
		private static final long serialVersionUID = -7935395216899959156L;

		/** 
		 * 发送方备案编号	
		 * VARCHAR2(20)	
		 * 必填	
		 * 发送方备案编号,不可随意填写 
		 */
		private String companyCode;
		
		/** 
		 * 业务编号	
		 * VARCHAR2(100)	
		 * 必填	
		 * 主要作用是回执给到企业的时候通过这个编号企业能认出对应之前发送的哪个单子 
		 */
		private String businessNo;
		
		/** 
		 * 业务类型	
		 * VARCHAR2(30)	
		 * 必填	
		 * 业务类型 IMPORTORDER
		 */
		private String businessType;
		
		/** 申报类型	
		 * CHAR(1)	
		 * 必填	
		 * 固定填写1 *
		 */
		private String declareType;
		
		/** 		可空	对接总署版企业必填；不填或者填写或01表示杭州版报文， 02 表示企业自行生成总署报文， 03委托电子口岸生成总署报文 */
		private String cebFlag;
		
		/** 
		 * 备注	
		 * NVARCHAR2(256)	
		 * 选填 
		 */
		private String note;
		
		public String getCompanyCode() {
			return companyCode;
		}
		public void setCompanyCode(String companyCode) {
			this.companyCode = companyCode;
		}
		public String getBusinessNo() {
			return businessNo;
		}
		public void setBusinessNo(String businessNo) {
			this.businessNo = businessNo;
		}
		public String getBusinessType() {
			return businessType;
		}
		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}
		public String getDeclareType() {
			return declareType;
		}
		public void setDeclareType(String declareType) {
			this.declareType = declareType;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		public String getCebFlag() {
			return cebFlag;
		}
		public void setCebFlag(String cebFlag) {
			this.cebFlag = cebFlag;
		}	
	}
	
	/** 订单表头信息  **/
	public static class JkfOrderImportHead implements Serializable{
		private static final long serialVersionUID = 8370625659705876961L;

		public static final String USER_PROCOTOL = "本人承诺所购买商品系个人合理自用，现委托商家代理申报、代缴税款等通关事宜，本人保证遵守《海关法》和国家相关法律法规，保证所提供的身份信息和收货信息真实完整，无侵犯他人权益的行为，以上委托关系系如实填写，本人愿意接受海关、检验检疫机构及其他监管部门的监管，并承担相应法律责任。";
		
		/** 电商企业编码	VARCHAR2(60)	必填	电商平台下的商家备案编号 **/	
		private String eCommerceCode;
		
		/** 电商企业名称	VARCHAR2(200)	必填	电商平台下的商家备案名称**/
		private String eCommerceName;
		
		/** 进出口标志	CHAR(1)	必填	I:进口E:出口 **/
		private String ieFlag;
		/** 支付类型	VARCHAR2(60)	必填	01:银行卡支付   02:余额支付   03:其他  **/
		private String payType;
		
		/** 支付公司编码	VARCHAR2(50)	必填	支付平台在跨境平台备案编号 **/
		private String payCompanyCode;
		
		/** 支付公司名称		非必填	对接总署版必填；支付平台在跨境平台备案名称 */
		private String payCompanyName;
		
		/** 支付单号	VARCHAR2(60)	必填	支付成功后，支付平台反馈给电商平台的支付单号 **/
		private String payNumber;
		
		/** 订单总金额	NUMBER(12,4)	必填	货款+订单税款+运费+保费 **/
		private Double orderTotalAmount;
		
		/** 订单编号	VARCHAR2(60)	必填	电商平台订单号，注意：一个订单只能对应一个运单(包裹)**/
		private String orderNo;
		
		/** 订单税款	NUMBER(12,4)	必填	交易过程中商家向用户征收的税款，按缴税新政计算填写**/
		private Double orderTaxAmount;
		
		/** 订单货款	NUMBER(12,4)	必填	与成交总价一致，按以电子订单的实际销售价格作为完税价格 **/
		private Double orderGoodsAmount;
		
		/** 非现金抵扣金额	NUMBER(12,4)	必填	使用积分、虚拟货币、代金券等非现金支付金额，无则填写"0" */
		private Double discount;
		
		/** 运费	NUMBER(12,4)	非必填	交易过程中商家向用户征收的运费，免邮模式填写0 **/
		private Double feeAmount;
		
		/** 保费	NUMBER(12,4)	必填	商家向用户征收的保价费用，无保费可填写0 **/
		private Double insureAmount;
		
		/** 企业备案名称	VARCHAR2(200)	必填	企业在跨境电商通关服务平台的备案名称 **/
		private String companyName;
		
		/** 企业备案编号	VARCHAR(20)	必填	企业在跨境电商通关服务的备案编号 **/
		private String companyCode;
		
		/** 成交时间	VARCHAR2(25)	必填	格式：2014-02-18 15:58:11 **/
		private String tradeTime;
		
		/** 成交币制	VARCHAR2(3)	必填	见参数表 **/
		private String currCode;
		
		/** 成交总价	NUMBER(14,4)	必填	与订单货款一致 **/
		private Double totalAmount;
				
		/** 收件人Email	VARCHAR2(60)	非必填 **/
		private String consigneeEmail;
		
		/** 收件人联系方式	VARCHAR2(60)	必填 **/
		private String consigneeTel;
		
		/** 收件人姓名	NVARCHAR2(60)	必填 **/
		private String consignee;
		
		/** 收件人地址	VARCHAR2(255)	必填 **/
		private String consigneeAddress;
		
		/** 总件数	NUMBER	必填	包裹中独立包装的物品总数，不考虑物品计量单位 **/
		private Integer totalCount;
		
		/** 商品批次号	VARCHAR(100)	非必填	商品批次号 */
		private String batchNumbers;
		
		/** 收货地址行政区划代码	VARCHAR(6)	非必填	参照国家统计局公布的国家行政区划标准填制 */
		private String consigneeDitrict;
		
		/** 发货方式（物流方式）	VARCHAR2(20)	非必填	见参数表 **/
		private String postMode;
		
		/** 发件人国别	VARCHAR2(3)	必填	见参数表 **/
		private String senderCountry;
		
		/** 发件人姓名	VARCHAR2(200)	必填 **/
		private String senderName;
		
		/** 购买人ID	VARCHAR2(100)	必填	购买人在电商平台的注册ID **/
		private String purchaserId;
		
		/** 物流企业名称	VARCHAR2(200)	必填 **/
		private String logisCompanyName;
		
		/** 物流企业编号	VARCHAR2(20)	必填 **/
		private String logisCompanyCode;
		
		/** 邮编	VARCHAR2(20)	非必填 **/
		private String zipCode;
		
		/** 备注	VARCHAR2(255)	非必填 **/
		private String note;
		
		/** 运单号列表	VARCHAR2(255)	非必填	运单之间以分号隔开 **/
		private String wayBills;
		
		/** 汇率	VARCHAR2(10)	非必填	如果是人民币，统一填写1 **/
		private String rate;
		/** 
		 * 个人委托申报协议	
		 * VARCHAR2(255)	
		 * 必填	
		 * 填写个人在电商平台上所同意的委托协议。文本可参考：本人承诺所购买商品系个人合理自用，现委托商家代理申报、代缴税款等通关事宜，本人保证遵守《海关法》和国家相关法律法规，保证所提供的身份信息和收货信息真实完整，无侵犯他人权益的行为，以上委托关系系如实填写，本人愿意接受海关、检验检疫机构及其他监管部门的监管，并承担相应法律责任.**/
		private String userProcotol;
		
		
		public String geteCommerceCode() {
			return eCommerceCode;
		}
		public void seteCommerceCode(String eCommerceCode) {
			this.eCommerceCode = eCommerceCode;
		}
		public String geteCommerceName() {
			return eCommerceName;
		}
		public void seteCommerceName(String eCommerceName) {
			this.eCommerceName = eCommerceName;
		}
		public String getIeFlag() {
			return ieFlag;
		}
		public void setIeFlag(String ieFlag) {
			this.ieFlag = ieFlag;
		}
		public String getPayType() {
			return payType;
		}
		public void setPayType(String payType) {
			this.payType = payType;
		}
		public String getPayCompanyCode() {
			return payCompanyCode;
		}
		public void setPayCompanyCode(String payCompanyCode) {
			this.payCompanyCode = payCompanyCode;
		}
		public String getPayCompanyName() {
			return payCompanyName;
		}
		public void setPayCompanyName(String payCompanyName) {
			this.payCompanyName = payCompanyName;
		}
		public String getPayNumber() {
			return payNumber;
		}
		public void setPayNumber(String payNumber) {
			this.payNumber = payNumber;
		}
		public Double getOrderTotalAmount() {
			return orderTotalAmount;
		}
		public void setOrderTotalAmount(Double orderTotalAmount) {
			this.orderTotalAmount = orderTotalAmount;
		}
		public String getOrderNo() {
			return orderNo;
		}
		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}
		public Double getOrderTaxAmount() {
			return orderTaxAmount;
		}
		public void setOrderTaxAmount(Double orderTaxAmount) {
			this.orderTaxAmount = orderTaxAmount;
		}
		public Double getOrderGoodsAmount() {
			return orderGoodsAmount;
		}
		public void setOrderGoodsAmount(Double orderGoodsAmount) {
			this.orderGoodsAmount = orderGoodsAmount;
		}
		public Double getFeeAmount() {
			return feeAmount;
		}
		public void setFeeAmount(Double feeAmount) {
			this.feeAmount = feeAmount;
		}
		public Double getInsureAmount() {
			return insureAmount;
		}
		public void setInsureAmount(Double insureAmount) {
			this.insureAmount = insureAmount;
		}
		public String getCompanyName() {
			return companyName;
		}
		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}
		public String getCompanyCode() {
			return companyCode;
		}
		public void setCompanyCode(String companyCode) {
			this.companyCode = companyCode;
		}
		public String getTradeTime() {
			return tradeTime;
		}
		public void setTradeTime(String tradeTime) {
			this.tradeTime = tradeTime;
		}
		public String getCurrCode() {
			return currCode;
		}
		public void setCurrCode(String currCode) {
			this.currCode = currCode;
		}
		public Double getTotalAmount() {
			return totalAmount;
		}
		public void setTotalAmount(Double totalAmount) {
			this.totalAmount = totalAmount;
		}
		public String getConsigneeEmail() {
			return consigneeEmail;
		}
		public void setConsigneeEmail(String consigneeEmail) {
			this.consigneeEmail = consigneeEmail;
		}
		public String getConsigneeTel() {
			return consigneeTel;
		}
		public void setConsigneeTel(String consigneeTel) {
			this.consigneeTel = consigneeTel;
		}
		public String getConsignee() {
			return consignee;
		}
		public void setConsignee(String consignee) {
			this.consignee = consignee;
		}
		public String getConsigneeAddress() {
			return consigneeAddress;
		}
		public void setConsigneeAddress(String consigneeAddress) {
			this.consigneeAddress = consigneeAddress;
		}
		public Integer getTotalCount() {
			return totalCount;
		}
		public void setTotalCount(Integer totalCount) {
			this.totalCount = totalCount;
		}
		public String getPostMode() {
			return postMode;
		}
		public void setPostMode(String postMode) {
			this.postMode = postMode;
		}
		public String getSenderCountry() {
			return senderCountry;
		}
		public void setSenderCountry(String senderCountry) {
			this.senderCountry = senderCountry;
		}
		public String getSenderName() {
			return senderName;
		}
		public void setSenderName(String senderName) {
			this.senderName = senderName;
		}
		public String getPurchaserId() {
			return purchaserId;
		}
		public void setPurchaserId(String purchaserId) {
			this.purchaserId = purchaserId;
		}
		public String getLogisCompanyName() {
			return logisCompanyName;
		}
		public void setLogisCompanyName(String logisCompanyName) {
			this.logisCompanyName = logisCompanyName;
		}
		public String getLogisCompanyCode() {
			return logisCompanyCode;
		}
		public void setLogisCompanyCode(String logisCompanyCode) {
			this.logisCompanyCode = logisCompanyCode;
		}
		public String getZipCode() {
			return zipCode;
		}
		public void setZipCode(String zipCode) {
			this.zipCode = zipCode;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		public String getWayBills() {
			return wayBills;
		}
		public void setWayBills(String wayBills) {
			this.wayBills = wayBills;
		}
		public String getRate() {
			return rate;
		}
		public void setRate(String rate) {
			this.rate = rate;
		}
		public String getUserProcotol() {
			return userProcotol;
		}
		public void setUserProcotol(String userProcotol) {
			this.userProcotol = userProcotol;
		}
		public Double getDiscount() {
			return discount;
		}
		public void setDiscount(Double discount) {
			this.discount = discount;
		}
		public String getBatchNumbers() {
			return batchNumbers;
		}
		public void setBatchNumbers(String batchNumbers) {
			this.batchNumbers = batchNumbers;
		}
		public String getConsigneeDitrict() {
			return consigneeDitrict;
		}
		public void setConsigneeDitrict(String consigneeDitrict) {
			this.consigneeDitrict = consigneeDitrict;
		}	
	}
	
	/** 订单表体信息  	**/
	public static class JkfOrderDetail implements Serializable{
		private static final long serialVersionUID = 458850801753863494L;

		/** 商品序号	INTEGER	必填	商品序号,序号不大于50 **/
		private Integer goodsOrder;
		
		/** 物品名称	NVARCHAR2(255)	必填  **/
		private String goodsName;
		
		/** 商品HS编码	VARCHAR2(10)	必填	填写商品对应的HS编码 **/
		private String codeTs;
		
		/** 商品毛重	NUMBER(15,4)	非必填 **/
		private Double grossWeight;
		
		/** 成交单价	NUMBER(15,4)	必填	表体单价*数量总和应等于表体的货款，即成交总价 **/
		private Double unitPrice;
		
		/** 成交计量单位	VARCHAR2(3)	必填	见参数表 **/
		private String goodsUnit;
		
		/** 产销国	VARCHAR2(5)	必填	见参数表 **/
		private String originCountry;
		
		/** 成交数量	NUMBER(15,4)	必填 **/
		private Integer goodsCount;
		
		/** 币制	VARCHAR (3)	必填	限定为人民币，填写“142” */
		private String currency;
		
		/** 商品规格、型号	NVARCHAR2(255)	非必填 **/
		private String goodsModel;
		
		/** 条形码	VARCHAR2(50)	非必填	国际通用的商品条形码，一般由前缀部分、制造厂商代码、商品代码和校验码组成。 */
		private String barCode;
		
		/** 备注	VARCHAR2(1000)	非必填	促销活动，商品单价偏离市场价格的，可以在此说明。 */
		private String note;
		
		public Integer getGoodsOrder() {
			return goodsOrder;
		}
		public void setGoodsOrder(Integer goodsOrder) {
			this.goodsOrder = goodsOrder;
		}
		public String getGoodsName() {
			return goodsName;
		}
		public void setGoodsName(String goodsName) {
			this.goodsName = goodsName;
		}
		public String getCodeTs() {
			return codeTs;
		}
		public void setCodeTs(String codeTs) {
			this.codeTs = codeTs;
		}
		public Double getGrossWeight() {
			return grossWeight;
		}
		public void setGrossWeight(Double grossWeight) {
			this.grossWeight = grossWeight;
		}
		public Double getUnitPrice() {
			return unitPrice;
		}
		public void setUnitPrice(Double unitPrice) {
			this.unitPrice = unitPrice;
		}
		public String getGoodsUnit() {
			return goodsUnit;
		}
		public void setGoodsUnit(String goodsUnit) {
			this.goodsUnit = goodsUnit;
		}
		public String getOriginCountry() {
			return originCountry;
		}
		public void setOriginCountry(String originCountry) {
			this.originCountry = originCountry;
		}
		public Integer getGoodsCount() {
			return goodsCount;
		}
		public void setGoodsCount(Integer goodsCount) {
			this.goodsCount = goodsCount;
		}
		public String getGoodsModel() {
			return goodsModel;
		}
		public void setGoodsModel(String goodsModel) {
			this.goodsModel = goodsModel;
		}
		public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public String getBarCode() {
			return barCode;
		}
		public void setBarCode(String barCode) {
			this.barCode = barCode;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}	
	}

	/**	购买人信息	**/
	public static class JkfGoodsPurchaser implements Serializable{
		private static final long serialVersionUID = -7591357901751517663L;

		/** 购买人ID	VARCHAR2(100)	必填 **/
		private String id;	
		
		/** 姓名	NVARCHAR2(100)	必填 **/
		private String name;
		
		/** 注册邮箱	VARCHAR2(140)	非必填 **/
		private String email;
		
		/** 联系电话	VARCHAR2(30)	必填 **/
		private String telNumber;
		
		/** 证件类型代码	VARCHAR2(20)	必填	01:身份证（试点期间只能是身份证）02:护照  03:其他 **/
		private String paperType;
		
		/** 地址	NVARCHAR2(200)	非必填 **/
		private String address;
		
		/** 证件号码	VARCHAR2(100)	必填 **/
		private String paperNumber;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getTelNumber() {
			return telNumber;
		}
		public void setTelNumber(String telNumber) {
			this.telNumber = telNumber;
		}
		public String getPaperType() {
			return paperType;
		}
		public void setPaperType(String paperType) {
			this.paperType = paperType;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getPaperNumber() {
			return paperNumber;
		}
		public void setPaperNumber(String paperNumber) {
			this.paperNumber = paperNumber;
		}	
	}
}
