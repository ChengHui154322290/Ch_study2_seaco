package com.tp.dto.ptm.salesorder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.common.util.mmp.MathUtils;
import com.tp.dto.ord.remote.SubOrder4BackendDTO;


/**
 * 开放平台对外订单DTO
 * 
 * @author 项硕
 * @version 2015年5月15日 下午3:23:00
 */
public class Order4PlatformDTO implements Serializable {

	private static final long serialVersionUID = 823478536517129535L;

	private Long code;
	private Date createTime;
	private Date payTime;
	private String account;
	private String payWay;
	private String payCode;
	private Double total;
	private Double freight;
	private Double discount;
	private String seaChannel;
	private String consigneeName;
	private String consigneeMobile;
	private String postcode;
	private String province;
	private String city;
	private String county;
	private String town;
	private String address;
	private String idcode; // 身份证号
	private String realname;
	private String receiptTitle;
	private String source;
	private String remarks;
	private String customsCode; // 海关备案号
	private String customsName; // 海关备案名称
	private String type; // 订单类型

	List<OrderLine4PlatformDTO> itemList;

	public Order4PlatformDTO() {
	}

	public Order4PlatformDTO(SubOrder4BackendDTO sobDTO) {
		if (null != sobDTO) {
			/* 子单信息 */
			if (null != sobDTO.getSubOrder()) {
				code = sobDTO.getSubOrder().getOrderCode();
				createTime = sobDTO.getSubOrder().getCreateTime();
				payTime = sobDTO.getSubOrder().getPayTime();
				account = sobDTO.getSubOrder().getAccountName();
				payWay = sobDTO.getSubOrder().getPayWayStr();
				payCode = sobDTO.getSubOrder().getPayCode();
				total = MathUtils.add(sobDTO.getSubOrder().getTotal(), sobDTO.getSubOrder().getFreight()).doubleValue();
				discount = sobDTO.getSubOrder().getDiscount();
				seaChannel = sobDTO.getSubOrder().getSeaChannelName();
				customsCode = sobDTO.getSubOrder().getCustomCode();
				customsName = sobDTO.getSubOrder().getOrgName();
				type = sobDTO.getSubOrder().getTypeStr();
				freight = sobDTO.getSubOrder().getFreight();
			}

			/* 父单信息 */
			if (null != sobDTO.getOrder()) {
				source = sobDTO.getOrder().getSourceStr();
				remarks = sobDTO.getOrder().getRemark();
			}

			/* 收货人信息 */
			if (null != sobDTO.getOrderConsignee()) {
				consigneeName = sobDTO.getOrderConsignee().getName();
				consigneeMobile = sobDTO.getOrderConsignee().getMobile();
				postcode = sobDTO.getOrderConsignee().getPostcode();
				province = sobDTO.getOrderConsignee().getProvinceName();
				city = sobDTO.getOrderConsignee().getCityName();
				county = sobDTO.getOrderConsignee().getCountyName();
				town = sobDTO.getOrderConsignee().getTownName();
				address = sobDTO.getOrderConsignee().getAddress();
			}

			/* 实名认证 */
			if (null != sobDTO.getMemRealinfo()) {
				idcode = sobDTO.getMemRealinfo().getIdentityCode();
				realname = sobDTO.getMemRealinfo().getRealName();
			}

			/* 发票信息 */
			if (null != sobDTO.getOrderReceipt()) {
				receiptTitle = sobDTO.getOrderReceipt().getTitle();
			}
		}
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getSeaChannel() {
		return seaChannel;
	}

	public void setSeaChannel(String seaChannel) {
		this.seaChannel = seaChannel;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneeMobile() {
		return consigneeMobile;
	}

	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIdcode() {
		return idcode;
	}

	public void setIdcode(String idcode) {
		this.idcode = idcode;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getReceiptTitle() {
		return receiptTitle;
	}

	public void setReceiptTitle(String receiptTitle) {
		this.receiptTitle = receiptTitle;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCustomsCode() {
		return customsCode;
	}

	public void setCustomsCode(String customsCode) {
		this.customsCode = customsCode;
	}

	public String getCustomsName() {
		return customsName;
	}

	public void setCustomsName(String customsName) {
		this.customsName = customsName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<OrderLine4PlatformDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<OrderLine4PlatformDTO> itemList) {
		this.itemList = itemList;
	}

    public Double getFreight() {
        return freight;
    }

    public void setFreight(Double freight) {
        this.freight = freight;
    }

}
