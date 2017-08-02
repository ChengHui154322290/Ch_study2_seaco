package com.tp.dto.wx.message.resp;

/**
 * 
 * @ClassName: TextMessage 
 * @description: 文本消息实体类 
 * @author: zhuss 
 * @date: 2015年7月3日 下午2:46:33 
 * @version: V1.0
 *
 */
public class TextRespMessage extends BaseRespMessage {
	
	public TextRespMessage(BaseRespMessage base) {
		super(base.getToUserName(), base.getFromUserName(),base.getCreateTime());
	}
	// 回复消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
