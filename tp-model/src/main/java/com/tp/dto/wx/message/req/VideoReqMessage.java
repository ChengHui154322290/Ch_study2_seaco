package com.tp.dto.wx.message.req;

/**
 * 
 * @ClassName: VideoMessage 
 * @description: 视频消息实体类
 * @author: zhuss 
 * @date: 2015年7月3日 下午12:02:56 
 * @version: V1.0
 *
 */
public class VideoReqMessage extends BaseReqMessage {
	
	public VideoReqMessage(BaseReqMessage base) {
		super(base.getToUserName(), base.getFromUserName(), base.getCreateTime(), base.getMsgType(),base.getMsgId());
	}
	
	// 视频消息媒体id
	private String MediaId;
	// 视频消息缩略图的媒体id
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
