package com.tp.m.vo.home;

import com.tp.m.base.BaseVO;

/**
 * 标签VO
 * @author zhuss
 *
 */
public class LabVO implements BaseVO{

	private static final long serialVersionUID = 9199809617704926839L;

	private String imageurl;
	private String linkurl;
	private String title;
	private String type;
	private ContentVO content;
	
	
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ContentVO getContent() {
		return content;
	}
	public void setContent(ContentVO content) {
		this.content = content;
	}
}
