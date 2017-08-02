/**
 * 
 */
package com.tp.dto.cms.app;

import java.io.Serializable;
import java.util.List;

/**
 * 首页-广告位信息实体类
 * 
 * @author szy
 * @version 0.0.1
 * 
 */
public class AppIndexAdvertReturnData implements Serializable {

	private static final long serialVersionUID = -1620952831373453144L;

	/** 广告数量 */
	private Integer count;
	/** 广告图片数组 */
	private List<AppAdvertiseInfoDTO<Object>> urls;
	
	/** 广告图片数组,给功能标签使用 */
	private List<AppAdvertiseInfoDTO<Object>> tables;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<AppAdvertiseInfoDTO<Object>> getUrls() {
		return urls;
	}

	public void setUrls(List<AppAdvertiseInfoDTO<Object>> urls) {
		this.urls = urls;
	}

	public List<AppAdvertiseInfoDTO<Object>> getTables() {
		return tables;
	}

	public void setTables(List<AppAdvertiseInfoDTO<Object>> tables) {
		this.tables = tables;
	}

}
