package com.tp.m.vo.search;

import java.util.List;

import com.tp.m.base.BaseVO;

public class NavBrandVO implements BaseVO{

	private static final long serialVersionUID = -5604404551128300164L;
	
	private String brandid;
	private String imgurl;
	private String name;
	private List<NavBrandVO> child;
	
	
	public NavBrandVO() {
		super();
	}
	
	public NavBrandVO(String brandid,String name, String imgurl) {
		super();
		this.brandid = brandid;
		this.name = name;
		this.imgurl = imgurl;
	}


	public String getBrandid() {
		return brandid;
	}
	public void setBrandid(String brandid) {
		this.brandid = brandid;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public List<NavBrandVO> getChild() {
		return child;
	}
	public void setChild(List<NavBrandVO> child) {
		this.child = child;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
