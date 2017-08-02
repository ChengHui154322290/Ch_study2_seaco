package com.tp.dto.wx.message.resp;

/**
 * 
 * @ClassName: VoiceMessage 
 * @description: 语音消息实体类
 * @author: zhuss 
 * @date: 2015年7月3日 下午2:28:57 
 * @version: V1.0
 *
 */
public class VoiceRespMessage extends BaseRespMessage {
	public VoiceRespMessage(BaseRespMessage base) {
		super(base.getToUserName(), base.getFromUserName(),base.getCreateTime());
	}
	// 语音
	private Voice Voice;

	public Voice getVoice() {
		return Voice;
	}

	public void setVoice(Voice voice) {
		Voice = voice;
	}
}
