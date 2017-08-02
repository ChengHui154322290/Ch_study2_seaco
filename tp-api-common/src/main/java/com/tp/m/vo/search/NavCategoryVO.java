package com.tp.m.vo.search;

import java.util.List;

import com.tp.m.base.BaseVO;

/**
 * 搜索导航-分类
 * @author zhuss
 * @2016年3月2日 下午12:13:19
 */
public class NavCategoryVO implements BaseVO{

	private static final long serialVersionUID = 1781438109560813774L;
	private String categoryid;
	private String imgurl;
	private String name;
	private String ishighlight;//是否高亮 0否 1是
	
	private List<NavCategoryVO> child; //二级分类

	public String getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIshighlight() {
		return ishighlight;
	}
	public void setIshighlight(String ishighlight) {
		this.ishighlight = ishighlight;
	}
	public List<NavCategoryVO> getChild() {
		return child;
	}
	public void setChild(List<NavCategoryVO> child) {
		this.child = child;
	}
}
