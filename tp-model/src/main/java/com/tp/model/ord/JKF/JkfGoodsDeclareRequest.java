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
 *	个人物品申报单写入跨境电商通关服务平台 报文结构
 */
@XmlRootElement(name="mo")
@XmlAccessorType(XmlAccessType.FIELD)
public class JkfGoodsDeclareRequest extends JkfBaseDO{

	private static final long serialVersionUID = -984616695288247010L;
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
		private static final long serialVersionUID = 5924357993900358015L;
		private String businessType;

		public String getBusinessType() {
			return businessType;
		}

		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}	
	}
	
	public static class Body implements Serializable{
		private static final long serialVersionUID = -2355421341369775072L;
		private List<GoodsDeclareModule> goodsDeclareModuleList = new ArrayList<JkfGoodsDeclareRequest.GoodsDeclareModule>();

		@XmlElementWrapper(name="goodsDeclareModuleList")
		@XmlElement(name="goodsDeclareModule")
		public List<GoodsDeclareModule> getGoodsDeclareModuleList() {
			return goodsDeclareModuleList;
		}

		public void setGoodsDeclareModuleList(List<GoodsDeclareModule> goodsDeclareModuleList) {
			this.goodsDeclareModuleList = goodsDeclareModuleList;
		}		
	}	
	
	public static class GoodsDeclareModule implements Serializable{
		private static final long serialVersionUID = -2535986346264118760L;
		private JkfSign jkfSign = new JkfSign();
		private GoodsDeclare goodsDeclare = new GoodsDeclare();
		private List<GoodsDeclareDetail> goodsDeclareDetails = new ArrayList<JkfGoodsDeclareRequest.GoodsDeclareDetail>();
		public JkfSign getJkfSign() {
			return jkfSign;
		}
		public void setJkfSign(JkfSign jkfSign) {
			this.jkfSign = jkfSign;
		}
		public GoodsDeclare getGoodsDeclare() {
			return goodsDeclare;
		}
		public void setGoodsDeclare(GoodsDeclare goodsDeclare) {
			this.goodsDeclare = goodsDeclare;
		}
		
		@XmlElementWrapper(name="goodsDeclareDetails")
		@XmlElement(name="goodsDeclareDetail")
		public List<GoodsDeclareDetail> getGoodsDeclareDetails() {
			return goodsDeclareDetails;
		}
		public void setGoodsDeclareDetails(List<GoodsDeclareDetail> goodsDeclareDetails) {
			this.goodsDeclareDetails = goodsDeclareDetails;
		}		
	}
	
	public static class JkfSign implements Serializable{
		private static final long serialVersionUID = 539425007944404569L;

		/** 发送方备案编号	VARCHAR2(20)	必填	发送方备案编号,不可随意填写 **/
		private String companyCode;
		
		/** 业务编码	VARCHAR2(100)	必填	可以是个人物品申报单预录入号，字段作用是回执给到企业的时候通过这个编号企业能认出对应之前发送的哪个单子 **/
		private String businessNo;
		
		/** 业务类型	VARCHAR2(30)	必填	业务类型 PERSONAL_GOODS_DECLAR **/
		private String businessType;
		
		/** 申报类型	CHAR(1)	必填	固定填写1 **/
		private String declareType;
		
		/** 对接海关总署 可空	对接总署版企业必填；不填或者填写或01表示杭州版报文， 02 表示企业自行生成总署报文， 03委托电子口岸生成总署报文 */
		private String cebFlag;
		
		/** 备注	VARCHAR2(256)	可空 **/
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
	
	/** 个人物品申报单表头信息 **/
	public static class GoodsDeclare implements Serializable{
		private static final long serialVersionUID = -4906124709673513366L;
		
		/** 电商平台名称	VARCHAR2(200)	是	电商平台在跨境电商通关服务平台的备案名称 **/
		private String companyName;
		
		/** 电商平台代码	VARCHAR(20)	是	电商平台在跨境电商通关服务的备案编号 **/
		private String companyCode;
		
		/** 物流企业名称	VARCHAR2(200)	是	物流企业在跨境平台备案的企业名称 **/
		private String logisCompanyName;
		
		/** 物流企业代码	VARCHAR2(20)	是	物流企业在跨境平台备案编码 **/
		private String logisCompanyCode;
		
		/** 收件人地址	VARCHAR2(255)	是	对应订单中的收件人地址 **/
		private String consigneeAddress;
		
		/** 购买人电话	VARCHAR2(30)	是	海关监管对象的电话,对应订单中的购买人联系电话 **/
		private String purchaserTelNumber;
		
		/** 订购人证件类型	VARCHAR2(1)	是	1-身份证；2-其它 **/
		private String buyerIdType;
		
		/** 订购人证件号码	VARCHAR2(60)	是	海关监控对象的身份证号,对应订单购买人证件号码 **/
		private String buyerIdNumber;
		
		/** 订购人姓名	VARCHAR2(60)	是	海关监控对象的姓名,对应订单购买人人姓名 **/
		private String buyerName;
		
		/** 保费	NUMBER(12,4)	是	商家向用户征收的保价费用，无保费可填写0 **/
		private String insureAmount;
		
		/** 运费	NUMBER(12,4)	是	交易过程中商家向用户征收的运费，免邮模式填写0 **/
		private String feeAmount;
		
		/** 担保企业编号	VARCHAR2(50)	否 **/
		private String assureCode;
		
		/** 许可证号		否 **/
		private String licenseNo;
		
		/** 账册编号	VARCHAR2(50)	否	可以数字和字母（转出的手册、转入、转出的报关单） **/
		private String accountBookNo;
		
		/** 进出口标志	CHAR(1)	是	必须为I **/
		private String ieFlag;
		
		/** 预录入号码	CHAR(18)	是	4位电商编号+14位企业流水，电商平台/物流企业生成后发送服务平台，与运单号一一对应，同个运单重新申报时，保持不变 **/
		private String preEntryNumber;
		
		/** 进口类型	CHAR(1)	是	0：直邮进口 1：保税进口 **/
		private String importType;
		
		/** 进出口日期	DATE	否	格式：2014-02-18 20:33:33 **/
		private String inOutDateStr;
		
		/** 进出口岸代码	VARCHAR2(5)	是	口岸代码表 **/
		private String iePort;
		
		/** 指运港(抵运港)	VARCHAR2(5)	是	对应参数表 **/
		private String destinationPort;
		
		/** 运输工具名称	NVARCHAR2(50)	否	包括字母和数字可以填写中文转关时填写@+16位转关单号 **/
		private String trafName;
		
		/** 运输工具航次(班)号	VARCHAR2(50)	否	新增，包括字母和数字，可以有中文 **/
		private String voyageNo;
		
		/** 运输方式代码	VARCHAR2(30)	是	参照运输方式代码表(TRANSF) **/
		private String trafMode;
		
		/** 运输工具编号	VARCHAR2(100)	否	直邮必填 */
		private String trafNo;
		
		/** 申报单位类别	VARCHAR2(30)	是	个人委托电商企业申报 |个人委托物流企业申报 |个人委托第三方申报 **/
		private String declareCompanyType;
		
		/** 申报单位代码	VARCHAR2(20)	是	指委托申报单位代码，需在海关注册，具有报关资质 **/
		private String declareCompanyCode;
		
		/** 申报单位名称	NVARCHAR2(200)	是	指委托申报单位名称，需在海关注册，具有报关资质 **/
		private String declareCompanyName;
		
		/** 电商企业代码	VARCHAR2(20)	是	电商企业在跨境平台备案编码 **/
		private String eCommerceCode;
		
		/** 电商企业名称	NVARCHAR2(200)	是	电商企业在跨境平台备案的企业名称 **/
		private String eCommerceName;
		
		/** 订单编号	VARCHAR2(50)	是 **/
		private String orderNo;
		
		/** 分运单号	VARCHAR2(50)	是 **/
		private String wayBill;
		
		/** 提运单号	VARCHAR2(37)	否	直邮必填 **/
		private String billNo;
		
		/** 贸易国别（起运地）	NVARCHAR2(20)	是	参照国别代码表(COUNTRY) **/
		private String tradeCountry;
		
		/** 件数	NUMBER(14,4)	是	表体独立包装的物品总数，不考虑物品计量单位，大于0 **/
		private Integer packNo;
		
		/** 毛重（公斤）	NUMBER(14,4)	是	大于0 **/
		private Double grossWeight;
		
		/** 净重（公斤）	NUMBER(14,4)	否	大于0 **/
		private Double netWeight;
		
		/** 包装种类	VARCHAR2(20)	否	参照包装种类代码表 **/
		private String warpType;
		
		/** 备注	NVARCHAR2(200)	否	可以数字和字母或者中文 **/
		private String remark;
		
		/** 申报口岸代码	VARCHAR2(5)	是	对应参数表 **/
		private String declPort;
		
		/** 录入人	NVARCHAR2(20)	是	默认9999 **/
		private String enteringPerson;
		
		/** 录入单位名称	NVARCHAR2(30)	是	默认9999 **/
		private String enteringCompanyName;
		
		/** 报关员代码	VARCHAR2(20)	否	 **/
		private String declarantNo;
		
		/** 码头/货场代码	VARCHAR2(20)	是	对应参数表 292801下城园区;299102下沙园区; 299201萧山园区 **/
		private String customsField;
		
		/** 发件人	NVARCHAR2(20)	是	可以数字和字母，可以有中文和英文 **/
		private String senderName;
		
		/** 收件人	NVARCHAR2(20)	 是	可以数字和字母，可以有中文和英文 **/
		private String consignee;
		
		/** 发件人国别	NVARCHAR2(20)	是	参数表 **/
		private String senderCountry;
		
		/** 发件人城市	NVARCHAR2(20)	否	可以数字和字母，可以有中文和英文 **/
		private String senderCity;
		
		/** 收件人证件类型	CHAR(1)	否	1-身份证（试点期间只能是身份证）2-护照3-其他 **/
		private String paperType;
		
		/** 收件人证件号	VARCHAR2(50)	否	可以有数字和字母 **/
		private String paperNumber;
		
		/** 价值	NUMBER(14,4)	是	表体所有商品总价的和+运费+保费 **/
		private Double worth;
		
		/** 币制	VARCHAR2(18)	是	对应参数表 **/
		private String currCode;
		
		/** 主要货物名称	NVARCHAR2(255)	是	可以数字和字母或者中文 **/
		private String mainGName;
		
		/** 区内企业编码	VARCHAR2(50)	否	保税进口必填，填报仓储企业编码 **/
		private String internalAreaCompanyNo;
		
		/** 区内企业名称	NVARCHAR2(200)	否	保税进口必填，填报仓储企业名称 **/
		private String internalAreaCompanyName;
		
		/** 申请单编号	VARCHAR2(30)	否	保税进口必填，指仓储企业事先在辅助系统申请的分送集报申请单编号 **/
		private String applicationFormNo;
		
		/** 是否授权	CHAR(1)	是	 代表是否个人买家授权电商申报数据，填写0或1，0代表否，1代表是 **/
		private String isAuthorize;
	
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

		public String getConsigneeAddress() {
			return consigneeAddress;
		}

		public void setConsigneeAddress(String consigneeAddress) {
			this.consigneeAddress = consigneeAddress;
		}

		public String getPurchaserTelNumber() {
			return purchaserTelNumber;
		}

		public void setPurchaserTelNumber(String purchaserTelNumber) {
			this.purchaserTelNumber = purchaserTelNumber;
		}

		public String getInsureAmount() {
			return insureAmount;
		}

		public void setInsureAmount(String insureAmount) {
			this.insureAmount = insureAmount;
		}

		public String getFeeAmount() {
			return feeAmount;
		}

		public void setFeeAmount(String feeAmount) {
			this.feeAmount = feeAmount;
		}

		public String getAssureCode() {
			return assureCode;
		}

		public void setAssureCode(String assureCode) {
			this.assureCode = assureCode;
		}

		public String getAccountBookNo() {
			return accountBookNo;
		}

		public void setAccountBookNo(String accountBookNo) {
			this.accountBookNo = accountBookNo;
		}

		public String getIeFlag() {
			return ieFlag;
		}

		public void setIeFlag(String ieFlag) {
			this.ieFlag = ieFlag;
		}

		public String getPreEntryNumber() {
			return preEntryNumber;
		}

		public void setPreEntryNumber(String preEntryNumber) {
			this.preEntryNumber = preEntryNumber;
		}

		public String getImportType() {
			return importType;
		}

		public void setImportType(String importType) {
			this.importType = importType;
		}

		public String getInOutDateStr() {
			return inOutDateStr;
		}

		public void setInOutDateStr(String inOutDateStr) {
			this.inOutDateStr = inOutDateStr;
		}

		public String getIePort() {
			return iePort;
		}

		public void setIePort(String iePort) {
			this.iePort = iePort;
		}

		public String getDestinationPort() {
			return destinationPort;
		}

		public void setDestinationPort(String destinationPort) {
			this.destinationPort = destinationPort;
		}

		public String getTrafName() {
			return trafName;
		}

		public void setTrafName(String trafName) {
			this.trafName = trafName;
		}

		public String getVoyageNo() {
			return voyageNo;
		}

		public void setVoyageNo(String voyageNo) {
			this.voyageNo = voyageNo;
		}

		public String getTrafMode() {
			return trafMode;
		}

		public void setTrafMode(String trafMode) {
			this.trafMode = trafMode;
		}

		public String getDeclareCompanyType() {
			return declareCompanyType;
		}

		public void setDeclareCompanyType(String declareCompanyType) {
			this.declareCompanyType = declareCompanyType;
		}

		public String getDeclareCompanyCode() {
			return declareCompanyCode;
		}

		public void setDeclareCompanyCode(String declareCompanyCode) {
			this.declareCompanyCode = declareCompanyCode;
		}

		public String getDeclareCompanyName() {
			return declareCompanyName;
		}

		public void setDeclareCompanyName(String declareCompanyName) {
			this.declareCompanyName = declareCompanyName;
		}

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

		public String getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}

		public String getWayBill() {
			return wayBill;
		}

		public void setWayBill(String wayBill) {
			this.wayBill = wayBill;
		}

		public String getTradeCountry() {
			return tradeCountry;
		}

		public void setTradeCountry(String tradeCountry) {
			this.tradeCountry = tradeCountry;
		}

		public Integer getPackNo() {
			return packNo;
		}

		public void setPackNo(Integer packNo) {
			this.packNo = packNo;
		}

		public Double getGrossWeight() {
			return grossWeight;
		}

		public void setGrossWeight(Double grossWeight) {
			this.grossWeight = grossWeight;
		}

		public Double getNetWeight() {
			return netWeight;
		}

		public void setNetWeight(Double netWeight) {
			this.netWeight = netWeight;
		}

		public String getWarpType() {
			return warpType;
		}

		public void setWarpType(String warpType) {
			this.warpType = warpType;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getDeclPort() {
			return declPort;
		}

		public void setDeclPort(String declPort) {
			this.declPort = declPort;
		}

		public String getEnteringPerson() {
			return enteringPerson;
		}

		public void setEnteringPerson(String enteringPerson) {
			this.enteringPerson = enteringPerson;
		}

		public String getEnteringCompanyName() {
			return enteringCompanyName;
		}

		public void setEnteringCompanyName(String enteringCompanyName) {
			this.enteringCompanyName = enteringCompanyName;
		}

		public String getDeclarantNo() {
			return declarantNo;
		}

		public void setDeclarantNo(String declarantNo) {
			this.declarantNo = declarantNo;
		}

		public String getCustomsField() {
			return customsField;
		}

		public void setCustomsField(String customsField) {
			this.customsField = customsField;
		}

		public String getSenderName() {
			return senderName;
		}

		public void setSenderName(String senderName) {
			this.senderName = senderName;
		}

		public String getConsignee() {
			return consignee;
		}

		public void setConsignee(String consignee) {
			this.consignee = consignee;
		}

		public String getSenderCountry() {
			return senderCountry;
		}

		public void setSenderCountry(String senderCountry) {
			this.senderCountry = senderCountry;
		}

		public String getSenderCity() {
			return senderCity;
		}

		public void setSenderCity(String senderCity) {
			this.senderCity = senderCity;
		}

		public String getPaperType() {
			return paperType;
		}

		public void setPaperType(String paperType) {
			this.paperType = paperType;
		}

		public String getPaperNumber() {
			return paperNumber;
		}

		public void setPaperNumber(String paperNumber) {
			this.paperNumber = paperNumber;
		}

		public Double getWorth() {
			return worth;
		}

		public void setWorth(Double worth) {
			this.worth = worth;
		}

		public String getCurrCode() {
			return currCode;
		}

		public void setCurrCode(String currCode) {
			this.currCode = currCode;
		}

		public String getMainGName() {
			return mainGName;
		}

		public void setMainGName(String mainGName) {
			this.mainGName = mainGName;
		}

		public String getInternalAreaCompanyNo() {
			return internalAreaCompanyNo;
		}

		public void setInternalAreaCompanyNo(String internalAreaCompanyNo) {
			this.internalAreaCompanyNo = internalAreaCompanyNo;
		}

		public String getInternalAreaCompanyName() {
			return internalAreaCompanyName;
		}

		public void setInternalAreaCompanyName(String internalAreaCompanyName) {
			this.internalAreaCompanyName = internalAreaCompanyName;
		}

		public String getApplicationFormNo() {
			return applicationFormNo;
		}

		public void setApplicationFormNo(String applicationFormNo) {
			this.applicationFormNo = applicationFormNo;
		}

		public String getIsAuthorize() {
			return isAuthorize;
		}

		public void setIsAuthorize(String isAuthorize) {
			this.isAuthorize = isAuthorize;
		}

		public String getBuyerIdType() {
			return buyerIdType;
		}

		public void setBuyerIdType(String buyerIdType) {
			this.buyerIdType = buyerIdType;
		}

		public String getBuyerIdNumber() {
			return buyerIdNumber;
		}

		public void setBuyerIdNumber(String buyerIdNumber) {
			this.buyerIdNumber = buyerIdNumber;
		}

		public String getBuyerName() {
			return buyerName;
		}

		public void setBuyerName(String buyerName) {
			this.buyerName = buyerName;
		}

		public String getLicenseNo() {
			return licenseNo;
		}

		public void setLicenseNo(String licenseNo) {
			this.licenseNo = licenseNo;
		}

		public String getBillNo() {
			return billNo;
		}

		public void setBillNo(String billNo) {
			this.billNo = billNo;
		}

		public String getTrafNo() {
			return trafNo;
		}

		public void setTrafNo(String trafNo) {
			this.trafNo = trafNo;
		}
	}
	
	/** 个人物品申报单表体信息 **/
	public static class GoodsDeclareDetail implements Serializable{
		private static final long serialVersionUID = -8438862548011692754L;

		/** 商品序号	NUMBER	是	只能有数字，外网自动生成大于0小于50 **/
		private Integer goodsOrder;
		
		/** 商品HScode	VARCHAR2(50)	是	填写商品对应的HS编码 **/
		private String codeTs;
		
		/** 企业商品货号	VARCHAR2(30)	否 **/
		private String goodsItemNo;
		
		/** 账册备案料号	VARCHAR2(30)	否	保税必填，与仓储企业备案的电子账册中料号数据一致 **/
		private String itemRecordNo;
		
		/** 企业商品品名	NVARCHAR2(250)	否 **/
		private String itemName;
		
		/** 物品名称	NVARCHAR2(255)	是 **/
		private String goodsName;
		
		/** 商品规格、型号	NVARCHAR2(255)	是	有数字和字母或者中文 **/
		private String goodsModel;
		
		/** 产销国	NVARCHAR2(30)	是	参照国别代码表(COUNTRY) **/
		private String originCountry;
		
		/** 成交币制	NVARCHAR2(20)	是	参照币制代码表(CURR) **/
		private String tradeCurr;
		
		/** 成交总价	NUMBER(14,4)	否	与申报总价一致 **/
		private Double tradeTotal;
		
		/** 申报单价	NUMBER(14,4)	是	只能是数字 **/
		private Double declPrice;
		
		/** 申报总价	NUMBER(14,4)	是	物品实际申报的价值。（实际销售价格） **/
		private Double declTotalPrice;
		
		/** 用途	NVARCHAR2(200)	否	08-赠品、10-试用装 **/
		private String useTo;
		
		/** 申报数量	NUMBER(14,4)	是	只能是数字，大于0 **/
		private Integer declareCount;
		
		/** 申报计量单位	NVARCHAR2(20)	是	参照计量单位代码表(UNIT) **/
		private String goodsUnit;
		
		/** 商品毛重	NUMBER(14,4)	否	只能是数字 **/
		private Double goodsGrossWeight;
		
		/** 第一单位	NVARCHAR2(15)	是	填写商品HS编码对应的第一单位 **/
		private String firstUnit;
		
		/** 第一数量	NUMBER(14,4)	是	根据第一单位填写对应数量 **/
		private Double firstCount;
		
		/** 第二单位	NVARCHAR2(15)		填写商品HS编码对应的第二单位  **/
		private String secondUnit;
		
		/** 第二数量	NUMBER(14,4)		根据第二单位填写对应数量 **/
		private Double secondCount;
		
		/** 产品国检备案编号	VARCHAR2 (18)	是	通过向国检备案获取 **/
		private String productRecordNo;
		
		/** 商品网址	VARCHAR2 (100)	否	商品网址 **/
		private String webSite;
		
		/** 条形码	VARCHAR2 (50)	否	条形码 **/
		private String barCode;
		
		/** 备注	VARCHAR2 (1000)	否	备注 **/
		private String note;		

		public Integer getGoodsOrder() {
			return goodsOrder;
		}

		public void setGoodsOrder(Integer goodsOrder) {
			this.goodsOrder = goodsOrder;
		}

		public String getCodeTs() {
			return codeTs;
		}

		public void setCodeTs(String codeTs) {
			this.codeTs = codeTs;
		}

		public String getGoodsItemNo() {
			return goodsItemNo;
		}

		public void setGoodsItemNo(String goodsItemNo) {
			this.goodsItemNo = goodsItemNo;
		}

		public String getGoodsName() {
			return goodsName;
		}

		public void setGoodsName(String goodsName) {
			this.goodsName = goodsName;
		}

		public String getGoodsModel() {
			return goodsModel;
		}

		public void setGoodsModel(String goodsModel) {
			this.goodsModel = goodsModel;
		}

		public String getOriginCountry() {
			return originCountry;
		}

		public void setOriginCountry(String originCountry) {
			this.originCountry = originCountry;
		}

		public String getTradeCurr() {
			return tradeCurr;
		}

		public void setTradeCurr(String tradeCurr) {
			this.tradeCurr = tradeCurr;
		}

		public Double getTradeTotal() {
			return tradeTotal;
		}

		public void setTradeTotal(Double tradeTotal) {
			this.tradeTotal = tradeTotal;
		}

		public Double getDeclPrice() {
			return declPrice;
		}

		public void setDeclPrice(Double declPrice) {
			this.declPrice = declPrice;
		}

		public Double getDeclTotalPrice() {
			return declTotalPrice;
		}

		public void setDeclTotalPrice(Double declTotalPrice) {
			this.declTotalPrice = declTotalPrice;
		}

		public String getUseTo() {
			return useTo;
		}

		public void setUseTo(String useTo) {
			this.useTo = useTo;
		}

		public Integer getDeclareCount() {
			return declareCount;
		}

		public void setDeclareCount(Integer declareCount) {
			this.declareCount = declareCount;
		}

		public String getGoodsUnit() {
			return goodsUnit;
		}

		public void setGoodsUnit(String goodsUnit) {
			this.goodsUnit = goodsUnit;
		}

		public Double getGoodsGrossWeight() {
			return goodsGrossWeight;
		}

		public void setGoodsGrossWeight(Double goodsGrossWeight) {
			this.goodsGrossWeight = goodsGrossWeight;
		}

		public String getFirstUnit() {
			return firstUnit;
		}

		public void setFirstUnit(String firstUnit) {
			this.firstUnit = firstUnit;
		}

		public Double getFirstCount() {
			return firstCount;
		}

		public void setFirstCount(Double firstCount) {
			this.firstCount = firstCount;
		}

		public String getSecondUnit() {
			return secondUnit;
		}

		public void setSecondUnit(String secondUnit) {
			this.secondUnit = secondUnit;
		}

		public Double getSecondCount() {
			return secondCount;
		}

		public void setSecondCount(Double secondCount) {
			this.secondCount = secondCount;
		}

		public String getProductRecordNo() {
			return productRecordNo;
		}

		public void setProductRecordNo(String productRecordNo) {
			this.productRecordNo = productRecordNo;
		}

		public String getWebSite() {
			return webSite;
		}

		public void setWebSite(String webSite) {
			this.webSite = webSite;
		}

		public String getItemRecordNo() {
			return itemRecordNo;
		}

		public void setItemRecordNo(String itemRecordNo) {
			this.itemRecordNo = itemRecordNo;
		}

		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
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
}
