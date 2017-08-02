package com.tp.m.vo.product;

import java.util.List;

import com.tp.m.base.BaseVO;

/**
 * 按照仓库区分的商品
 * @author zhuss
 * @2016年1月14日 下午2:13:48
 */
public class ProductWithWarehouseVO implements BaseVO{

	private static final long serialVersionUID = -2497064328510267335L;
	
	private String warehouseid;// 仓库id
	private String warehousename;//仓库名称
	private String channelcode;//海淘渠道编码
	private String channel;//海淘渠道名称
	private String storagetype;
	private String freight;//运费
	private String taxes;//进口税
	private String price;//小计
	private String itemsprice;//商品总金额
	private String isfreetax;//是否免税 0否 1是
	private List<ProductVO> products;//商品列表
	private String lnglat;//仓库经纬度
	public String getWarehouseid() {
		return warehouseid;
	}
	public void setWarehouseid(String warehouseid) {
		this.warehouseid = warehouseid;
	}
	public String getWarehousename() {
		return warehousename;
	}
	public void setWarehousename(String warehousename) {
		this.warehousename = warehousename;
	}
	public String getChannelcode() {
		return channelcode;
	}
	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getStoragetype() {
		return storagetype;
	}
	public void setStoragetype(String storagetype) {
		this.storagetype = storagetype;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	public String getTaxes() {
		return taxes;
	}
	public void setTaxes(String taxes) {
		this.taxes = taxes;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public List<ProductVO> getProducts() {
		return products;
	}
	public void setProducts(List<ProductVO> products) {
		this.products = products;
	}
	public String getItemsprice() {
		return itemsprice;
	}
	public void setItemsprice(String itemsprice) {
		this.itemsprice = itemsprice;
	}
	public String getIsfreetax() {
		return isfreetax;
	}
	public void setIsfreetax(String isfreetax) {
		this.isfreetax = isfreetax;
	}
	public String getLnglat() {
		return lnglat;
	}
	public void setLnglat(String lnglat) {
		this.lnglat = lnglat;
	}
}
