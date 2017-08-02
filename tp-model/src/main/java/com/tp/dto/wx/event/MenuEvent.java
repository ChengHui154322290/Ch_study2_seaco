package com.tp.dto.wx.event;

/**
 * 
 * @ClassName: MenuEvent 
 * @description: 自定义菜单事件 
 * @author: zhuss 
 * @date: 2015年8月3日 下午2:18:33 
 * @version: V1.0
 *
 */
public class MenuEvent extends BaseEvent {
	// 事件KEY值，与自定义菜单接口中KEY值对应
	private String EventKey;

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}
}
