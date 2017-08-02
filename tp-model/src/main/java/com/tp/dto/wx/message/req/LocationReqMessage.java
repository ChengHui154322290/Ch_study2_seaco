package com.tp.dto.wx.message.req;

/**
 * 
 * @ClassName: LocationMessage 
 * @description: 地理位置消息实体类  
 * @author: zhuss 
 * @date: 2015年7月3日 下午11:27:01 
 * @version: V1.0
 *
 */
public class LocationReqMessage extends BaseReqMessage {
	
	public LocationReqMessage(BaseReqMessage base) {
		super(base.getToUserName(), base.getFromUserName(), base.getCreateTime(), base.getMsgType(),base.getMsgId());
	}
	//纬度
	private String Location_X;
	//经度
	private String Location_Y;
	//地图缩放比例
	private String Scale;
	//地理位置信息
	private String Label;
	
	public String getLocation_X() {
		return Location_X;
	}
	public void setLocation_X(String location_X) {
		Location_X = location_X;
	}
	public String getLocation_Y() {
		return Location_Y;
	}
	public void setLocation_Y(String location_Y) {
		Location_Y = location_Y;
	}
	public String getScale() {
		return Scale;
	}
	public void setScale(String scale) {
		Scale = scale;
	}
	public String getLabel() {
		return Label;
	}
	public void setLabel(String label) {
		Label = label;
	}
}
