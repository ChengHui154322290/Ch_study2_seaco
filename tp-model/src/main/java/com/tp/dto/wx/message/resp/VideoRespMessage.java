package com.tp.dto.wx.message.resp;
/**
 * 
 * @ClassName: VideoMessage 
 * @description: 视频消息实体类
 * @author: zhuss 
 * @date: 2015年7月3日 下午2:38:26 
 * @version: V1.0
 *
 */
public class VideoRespMessage extends BaseRespMessage {
	public VideoRespMessage(BaseRespMessage base) {
		super(base.getToUserName(), base.getFromUserName(),base.getCreateTime());
	}
	// 视频
	private Video Video;

	public Video getVideo() {
		return Video;
	}

	public void setVideo(Video video) {
		Video = video;
	}
}
