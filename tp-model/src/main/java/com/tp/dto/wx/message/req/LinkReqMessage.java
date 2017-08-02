package com.tp.dto.wx.message.req;

/**
 * 
 * @ClassName: LinkMessage 
 * @description: 链接消息实体类
 * @author: zhuss 
 * @date: 2015年7月3日 下午11:56:43 
 * @version: V1.0
 *
 */
public class LinkReqMessage extends BaseReqMessage{
	
	public LinkReqMessage(BaseReqMessage base) {
		super(base.getToUserName(), base.getFromUserName(), base.getCreateTime(), base.getMsgType(),base.getMsgId());
	}
	//链接消息名称
	private String Title;
	//链接消息描述
	private String Description;
	//消息链接
	private String Url;

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return null == Description ? "" : Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getUrl() {
		return null == Url ? "" : Url;
	}

	public void setUrl(String url) {
		Url = url;
	}
}
