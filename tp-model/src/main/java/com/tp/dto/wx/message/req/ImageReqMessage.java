package com.tp.dto.wx.message.req;

/**
 * 
 * @ClassName: ImageMessage 
 * @description: 图片消息实体类
 * @author: zhuss 
 * @date: 2015年7月3日 下午1:54:41 
 * @version: V1.0
 *
 */
public class ImageReqMessage extends BaseReqMessage {
	
	public ImageReqMessage(BaseReqMessage base) {
		super(base.getToUserName(), base.getFromUserName(), base.getCreateTime(), base.getMsgType(),base.getMsgId());
	}
	// 图片链接
	private String PicUrl;
	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
}
