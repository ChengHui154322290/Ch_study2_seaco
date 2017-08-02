package com.tp.m.vo.order;

import java.util.List;

import com.tp.m.base.BaseVO;

/**
 * 售后出参
 * @author zhuss
 * @2016年2月26日 下午6:05:18
 */
public class AfterSalesVO implements BaseVO{

	private static final long serialVersionUID = -4425855905392750653L;

	private String asid; //售后单ID
	private String ascode;//售后单编号
	private String applydate;//申请时间
	
	private String lineprice;//实付行价格
	private String returnprice;//退货价格
	private String returninfo;//退货说明
	private String returnreason;//退货原因
	private String returnreasondesc;//退货原因描述
	private String returnaddress;//退货地址
	private String sellerinfo;//商家说明
	private String kfinfo;//客服说明
	private List<String> returnimg;//退货凭证
	
	private String status;
	private String statusdesc;
	
	private String lineid; 
	private String itemname;
	private String itemimg;
	private String itemprice;//商品单价
	private String returncount;//退货数量
	private String buycount;//购买数量
	//private List<String> itemspecs;
	
	private String linkname;//联系人
	private String linktel;//联系手机号
	
	private String ordercode;
	private String logisticcode;//运单号
	private String company;//物流公司名称
	private String companycode;//物流公司编号
	
	private String historycount; //历史次数
	public String getAsid() {
		return asid;
	}
	public void setAsid(String asid) {
		this.asid = asid;
	}
	public String getAscode() {
		return ascode;
	}
	public void setAscode(String ascode) {
		this.ascode = ascode;
	}
	public String getReturncount() {
		return returncount;
	}
	public void setReturncount(String returncount) {
		this.returncount = returncount;
	}
	public String getReturnprice() {
		return returnprice;
	}
	public void setReturnprice(String returnprice) {
		this.returnprice = returnprice;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusdesc() {
		return statusdesc;
	}
	public void setStatusdesc(String statusdesc) {
		this.statusdesc = statusdesc;
	}
	public String getLineid() {
		return lineid;
	}
	public void setLineid(String lineid) {
		this.lineid = lineid;
	}
	public String getBuycount() {
		return buycount;
	}
	public void setBuycount(String buycount) {
		this.buycount = buycount;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public String getItemimg() {
		return itemimg;
	}
	public void setItemimg(String itemimg) {
		this.itemimg = itemimg;
	}
	public String getApplydate() {
		return applydate;
	}
	public void setApplydate(String applydate) {
		this.applydate = applydate;
	}
	public String getReturninfo() {
		return returninfo;
	}
	public void setReturninfo(String returninfo) {
		this.returninfo = returninfo;
	}
	public String getReturnreason() {
		return returnreason;
	}
	public void setReturnreason(String returnreason) {
		this.returnreason = returnreason;
	}
	public String getSellerinfo() {
		return sellerinfo;
	}
	public void setSellerinfo(String sellerinfo) {
		this.sellerinfo = sellerinfo;
	}
	public List<String> getReturnimg() {
		return returnimg;
	}
	public void setReturnimg(List<String> returnimg) {
		this.returnimg = returnimg;
	}
	public String getReturnreasondesc() {
		return returnreasondesc;
	}
	public void setReturnreasondesc(String returnreasondesc) {
		this.returnreasondesc = returnreasondesc;
	}
	public String getReturnaddress() {
		return returnaddress;
	}
	public void setReturnaddress(String returnaddress) {
		this.returnaddress = returnaddress;
	}
	public String getOrdercode() {
		return ordercode;
	}
	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}
	public String getLogisticcode() {
		return logisticcode;
	}
	public void setLogisticcode(String logisticcode) {
		this.logisticcode = logisticcode;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getItemprice() {
		return itemprice;
	}
	public void setItemprice(String itemprice) {
		this.itemprice = itemprice;
	}
	public String getCompanycode() {
		return companycode;
	}
	public void setCompanycode(String companycode) {
		this.companycode = companycode;
	}
	public String getKfinfo() {
		return kfinfo;
	}
	public void setKfinfo(String kfinfo) {
		this.kfinfo = kfinfo;
	}
	public String getLineprice() {
		return lineprice;
	}
	public void setLineprice(String lineprice) {
		this.lineprice = lineprice;
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
	public String getHistorycount() {
		return historycount;
	}
	public void setHistorycount(String historycount) {
		this.historycount = historycount;
	}
}
