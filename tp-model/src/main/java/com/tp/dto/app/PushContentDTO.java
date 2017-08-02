package com.tp.dto.app;

import java.io.Serializable;

public class PushContentDTO implements Serializable{

	private static final long serialVersionUID = 8430548379804281520L;

	private String text;
	private String sku;//商品的skucode
	private String tid;//专场id,topic
	private String link;
	private String type;//推送类型
	private String title;
	private String pageTitle;//页面显示标题
	
	private Content content = new Content();
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
		content.text =text;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
		content.sku=sku;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
		content.setTid(tid);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
		content.pageTitle = pageTitle;
	}

	public Content getContent() {
		return content;
	}
	public void setContent(Content content) {
		this.content = content;
	}
	
	class Content implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4468381775748254210L;
		
		private String tid;
		private String sku;
		private String text;
		private String pageTitle;
		public String getTid() {
			return tid;
		}
		public void setTid(String tid) {
			this.tid = tid;
		}
		public String getSku() {
			return sku;
		}
		public void setSku(String sku) {
			this.sku = sku;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getPageTitle() {
			return pageTitle;
		}
		public void setPageTitle(String pageTitle) {
			this.pageTitle = pageTitle;
		}
	}

}
