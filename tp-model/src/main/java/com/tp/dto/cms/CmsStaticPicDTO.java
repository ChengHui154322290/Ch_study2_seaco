package com.tp.dto.cms;

import java.io.Serializable;
import java.util.List;

import com.tp.model.BaseDO;
import com.tp.model.cms.StaticPic;

/**
 * 活动图片模板
 * 
 * @author szy
 */

public class CmsStaticPicDTO extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1780471457199866515L;

	/** 图片名称 */
	private String name;

	/** 图片地址url */
	private String url;
	
	/** 图片地址url的全写 */
	private String spellUrl;

	/** 图片变量表id */
	private Long picId;
	
	/** 变量的集合 */
	private List<StaticPic> list = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSpellUrl() {
		return spellUrl;
	}

	public void setSpellUrl(String spellUrl) {
		this.spellUrl = spellUrl;
	}

	public Long getPicId() {
		return picId;
	}

	public void setPicId(Long picId) {
		this.picId = picId;
	}

	public List<StaticPic> getList() {
		return list;
	}

	public void setList(List<StaticPic> list) {
		this.list = list;
	}


}