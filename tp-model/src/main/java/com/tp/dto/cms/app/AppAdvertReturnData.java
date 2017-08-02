/**
 * 
 */
package com.tp.dto.cms.app;

import java.io.Serializable;
import java.util.List;

/**
 * app-广告位信息实体类
 * 
 * @author szy
 * @version 0.0.1
 * 
 */
public class AppAdvertReturnData implements Serializable {

	private static final long serialVersionUID = -1620952831373453144L;

	/** 广告数量 */
	private Integer count;
	
	/** 广告图片数组 */
	private List<AppAdvertDTO> urls;
	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<AppAdvertDTO> getUrls() {
		return urls;
	}

	public void setUrls(List<AppAdvertDTO> urls) {
		this.urls = urls;
	}

}
