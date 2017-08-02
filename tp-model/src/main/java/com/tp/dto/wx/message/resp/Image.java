package com.tp.dto.wx.message.resp;

/**
 * 
 * @ClassName: Image 
 * @description: 图片实体类 
 * @author: zhuss 
 * @date: 2015年7月3日 下午2:14:16 
 * @version: V1.0
 *
 */
public class Image {
	// 媒体文件id
	private String MediaId;

	
	public Image() {
		super();
	}

	public Image(String mediaId) {
		super();
		MediaId = mediaId;
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
}
