package com.tp.dto.wx.message.req;


/**
 * 
 * @ClassName: TextMessage 
 * @description: 文本消息实体类
 * @author: zhuss 
 * @date: 2015年7月3日 下午12:01:42 
 * @version: V1.0
 *
 */
public class TextReqMessage extends BaseReqMessage {
	
	public TextReqMessage(BaseReqMessage base) {
		super(base.getToUserName(), base.getFromUserName(), base.getCreateTime(), base.getMsgType(),base.getMsgId());
	}
	
	// 消息内容
	private String Content;
	
	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
