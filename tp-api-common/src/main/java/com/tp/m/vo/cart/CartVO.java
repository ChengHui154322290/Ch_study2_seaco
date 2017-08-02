package com.tp.m.vo.cart;

import java.util.List;

import com.tp.m.base.BaseVO;
import com.tp.m.vo.product.ProductVO;
import com.tp.m.vo.product.ProductWithWarehouseVO;

/**
 * 购物车检查获取金额
 * @author zhuss
 * @2016年1月7日 下午12:06:18
 */
public class CartVO implements BaseVO{

	private static final long serialVersionUID = 234561691138742362L;

	private String totalprice;//总金额
	private String disprice;//优惠的金额
	private List<ProductVO> products;//旧版本商品列表
	private List<ProductWithWarehouseVO> mergeitems;//正常商品列表
	private List<ProductVO> invaliditems;//失效商品列表
	public String getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(String totalprice) {
		this.totalprice = totalprice;
	}
	public String getDisprice() {
		return disprice;
	}
	public void setDisprice(String disprice) {
		this.disprice = disprice;
	}
	public List<ProductVO> getProducts() {
		return products;
	}
	public void setProducts(List<ProductVO> products) {
		this.products = products;
	}
	public List<ProductWithWarehouseVO> getMergeitems() {
		return mergeitems;
	}
	public void setMergeitems(List<ProductWithWarehouseVO> mergeitems) {
		this.mergeitems = mergeitems;
	}
	public List<ProductVO> getInvaliditems() {
		return invaliditems;
	}
	public void setInvaliditems(List<ProductVO> invaliditems) {
		this.invaliditems = invaliditems;
	}
}
