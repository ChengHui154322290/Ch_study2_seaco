/**
 * 
 */
package com.tp.m.vo.promoter;

import com.tp.m.base.BaseVO;

/**
 * @author Administrator
 *
 */
public class TopicInfoVO implements BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6201035260552044889L;
		
	/** 主键 */
	private Long topicid;

	/** 专题名称 */
	private String name;

	/** 类型 0- 全部 1-单品团 2-品牌团 3-主题团 */
	private Integer type;
	
	/** 专题图片 */
	private String image;
	
	/** 专题上下架情况 0:下架 1:上架 */
	private Integer onshelves;
	
	/**调用方式 0:调用getopics 1:调用getopicItems*/
	private Integer callmode;
		
	/** 主题分享地址 */
	private String shareurl;
	
	
	
	
	public String getShareurl() {
		return shareurl;
	}

	public void setShareurl(String shareurl) {
		this.shareurl = shareurl;
	}

	public Integer getCallmode() {
		return callmode;
	}

	public void setCallmode(Integer callmode) {
		this.callmode = callmode;
	}

	public Long getTopicid() {
		return topicid;
	}

	public void setTopicid(Long topicid) {
		this.topicid = topicid;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getOnshelves() {
		return onshelves;
	}

	public void setOnshelves(Integer onshelves) {
		this.onshelves = onshelves;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
					
}
