package com.tp.m.query.order;

import com.tp.m.base.BaseQuery;

/**
 * 售后入参
 * @author zhuss
 * @2016年2月26日 下午2:16:23
 */
public class QueryAfterSales extends BaseQuery{

	private static final long serialVersionUID = -1741953400126705149L;

	//private String sku;
	private String returnreason;//退货原因：有七种情况让用户选择，如下1.无理由退货2.商品质量问题 3.商品错发/漏发 4.收到商品破损 5.拍错了6.不想要了 7.已重新下单
	private String returninfo;//退货说明
	private String returncount;//退货数量
	
	private String lineid;//订单子项ID
	private String linkname;//联系人
	private String linktel;//联系手机号
	
	private String ordercode;//订单号
	
	
	
	private String asid;//售后ID
	private String company;//物流公司
	private String companycode;//物流公司编号
	private String logisticcode;//运单号
	
	private String imageone;
	private String imagetwo;
	private String imagethree;
	private String imagefour;
	private String imagefive;
	
	private String loginname;

	public String getReturnreason() {
		return returnreason;
	}
	public void setReturnreason(String returnreason) {
		this.returnreason = returnreason;
	}
	public String getReturninfo() {
		return returninfo;
	}
	public void setReturninfo(String returninfo) {
		this.returninfo = returninfo;
	}
	public String getReturncount() {
		return returncount;
	}
	public void setReturncount(String returncount) {
		this.returncount = returncount;
	}
	public String getLinkname() {
		return linkname;
	}
	public void setLinkname(String linkname) {
		this.linkname = linkname;
	}
	public String getLinktel() {
		return linktel;
	}
	public void setLinktel(String linktel) {
		this.linktel = linktel;
	}
	public String getImageone() {
		return imageone;
	}
	public void setImageone(String imageone) {
		this.imageone = imageone;
	}
	public String getImagetwo() {
		return imagetwo;
	}
	public void setImagetwo(String imagetwo) {
		this.imagetwo = imagetwo;
	}
	public String getImagethree() {
		return imagethree;
	}
	public void setImagethree(String imagethree) {
		this.imagethree = imagethree;
	}
	public String getImagefour() {
		return imagefour;
	}
	public void setImagefour(String imagefour) {
		this.imagefour = imagefour;
	}
	public String getImagefive() {
		return imagefive;
	}
	public void setImagefive(String imagefive) {
		this.imagefive = imagefive;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getOrdercode() {
		return ordercode;
	}
	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}
	public String getLineid() {
		return lineid;
	}
	public void setLineid(String lineid) {
		this.lineid = lineid;
	}
	public String getAsid() {
		return asid;
	}
	public void setAsid(String asid) {
		this.asid = asid;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompanycode() {
		return companycode;
	}
	public void setCompanycode(String companycode) {
		this.companycode = companycode;
	}
	public String getLogisticcode() {
		return logisticcode;
	}
	public void setLogisticcode(String logisticcode) {
		this.logisticcode = logisticcode;
	}
}
