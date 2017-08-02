package com.tp.m.vo.home;

import com.tp.m.base.BaseVO;

/**
 * 首页 - 广告位对象
 * @author zhuss
 * @2016年1月2日 下午2:43:51
 */
public class BannerVO implements BaseVO {

	private static final long serialVersionUID = 3420461880012024801L;

	private String imageurl;
	
	/**“0”超链接，“1”专场id，“2”专场id和商品sku等等*/
	private String type;
	
	/**内容*/
	private ContentVO content;

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
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
