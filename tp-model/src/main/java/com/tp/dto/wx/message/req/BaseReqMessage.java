package com.tp.dto.wx.message.req;

/**
 * 
* @ClassName: BaseMessage 
* @description: 消息基类（普通用户 ->公众帐号）
* @author: zhuss 
* @date: 2015年7月3日 下午1:52:21 
* @version: V1.0
*
 */
public class BaseReqMessage {
	// 开发者微信号
	private String ToUserName;
	//发送方帐号（收到的OpenID）
	private String FromUserName;
	// 消息创建时间 （整型）
	private long CreateTime;
	// 消息类型
	private String MsgType;
	
	private long MsgId; //消息id，64位整型
	
	public BaseReqMessage() {
		super();
	}

	public BaseReqMessage(String toUserName, String fromUserName, long createTime,
			String msgType, long msgId) {
		super();
		ToUserName = toUserName;
		FromUserName = fromUserName;
		CreateTime = createTime;
		MsgType = msgType;
		MsgId = msgId;
	}

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public long getMsgId() {
		return MsgId;
	}

	public void setMsgId(long msgId) {
		MsgId = msgId;
	}
}
