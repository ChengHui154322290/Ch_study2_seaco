package com.tp.dto.wx.message.resp;

/**
 * 
 * @ClassName: Video 
 * @description: 视频实体类
 * @author: zhuss 
 * @date: 2015年7月3日 下午2:18:06 
 * @version: V1.0
 *
 */
public class Video {
	// 媒体文件id
	private String MediaId;
	// 缩略图的媒体id
	private String ThumbMediaId;

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getThumbMediaId() {
		return ThumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}
}
