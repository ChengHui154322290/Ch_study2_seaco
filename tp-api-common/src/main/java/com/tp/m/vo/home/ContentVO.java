package com.tp.m.vo.home;


import com.tp.m.base.BaseVO;

/**
 * 首页- 广告位对象中的内容对象
 * @author zhuss
 * @2016年1月2日 下午2:39:54
 */
public class ContentVO implements BaseVO{
	private static final long serialVersionUID = 2329596560078206309L;
	private String text;//用于超链接
	private String sku;//商品的sku
	private String tid;//专场id

	
	public ContentVO() {
		super();
	}

	public ContentVO(String text,String sku,String tid) {
		this.text = text;
		this.sku = sku;
		this.tid = tid;
	}

	public String getText() {
		return text;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
}
