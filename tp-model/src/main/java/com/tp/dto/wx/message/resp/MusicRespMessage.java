package com.tp.dto.wx.message.resp;
/**
 * 
 * @ClassName: MusicMessage 
 * @description: 音乐消息
 * @author: zhuss 
 * @date: 2015年7月3日 下午2:36:17 
 * @version: V1.0
 *
 */
public class MusicRespMessage extends BaseRespMessage {
	
	public MusicRespMessage(BaseRespMessage base) {
		super(base.getToUserName(), base.getFromUserName(),base.getCreateTime());
	}
	// 音乐
	private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}
}
