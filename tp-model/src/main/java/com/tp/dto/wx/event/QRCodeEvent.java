package com.tp.dto.wx.event;

/**
 * 
 * @ClassName: QRCodeEvent 
 * @description: 扫描带参数二维码事件
 * @author: zhuss 
 * @date: 2015年7月3日 下午2:22:49 
 * @version: V1.0
 *
 */
public class QRCodeEvent extends BaseEvent {
	// 事件KEY值
	private String EventKey;
	// 用于换取二维码图片
	private String Ticket;

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}

	public String getTicket() {
		return Ticket;
	}

	public void setTicket(String ticket) {
		Ticket = ticket;
	}
}
