package com.tp.dto.wx.message.req;

/**
 * 
 * @ClassName: VoiceMessage 
 * @description: 语音消息实体类
 * @author: zhuss 
 * @date: 2015年7月3日 下午2:02:41 
 * @version: V1.0
 *
 */
public class VoiceReqMessage extends BaseReqMessage {
	
	public VoiceReqMessage(BaseReqMessage base) {
		super(base.getToUserName(), base.getFromUserName(), base.getCreateTime(), base.getMsgType(),base.getMsgId());
	}
	// 媒体ID
	private String MediaId;
	// 语音格式
	private String Format;
	// 语音识别结果，UTF8编码
	private String Recognition;
	
	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(String format) {
		Format = format;
	}

	public String getRecognition() {
		return Recognition;
	}

	public void setRecognition(String recognition) {
		Recognition = recognition;
	}
}
