package com.tp.dto.wx.message.resp;


/**
 * 
 * @ClassName: ImageMessage 
 * @description:  图片消息实体类
 * @author: zhuss 
 * @date: 2015年7月3日 下午2:34:41 
 * @version: V1.0
 *
 */
public class ImageRespMessage extends BaseRespMessage {
	public ImageRespMessage(BaseRespMessage base) {
		super(base.getToUserName(), base.getFromUserName(),base.getCreateTime());
	}
	// 图片
	private Image Image;

	public Image getImage() {
		return Image;
	}

	public void setImage(Image image) {
		Image = image;
	}
}
