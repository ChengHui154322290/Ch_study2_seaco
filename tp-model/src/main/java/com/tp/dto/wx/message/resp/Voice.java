package com.tp.dto.wx.message.resp;
/**
 * 
 * @ClassName: Voice 
 * @description: 语音实体类
 * @author: zhuss 
 * @date: 2015年7月3日 下午2:08:44 
 * @version: V1.0
 *
 */
public class Voice {
	// 媒体文件id
	private String MediaId;
	
	public Voice() {
		super();
	}

	public Voice(String mediaId) {
		MediaId = mediaId;
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
}
