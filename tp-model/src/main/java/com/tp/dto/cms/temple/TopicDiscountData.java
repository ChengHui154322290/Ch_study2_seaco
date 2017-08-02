/**
 * 
 */
package com.tp.dto.cms.temple;

import java.io.Serializable;
import java.util.List;

import com.tp.dto.cms.CmsSingleTempleDTO;

/**
 * 首页促销专题模板的dto实体类
 * 
 * @author szy
 * @version 0.0.1
 * 
 */
public class TopicDiscountData implements Serializable {

	private static final long serialVersionUID = -1620952831373453144L;

	/**日期**/
	private String toipcSdate;
	
	/**星期数**/
	private String weekName;
	
	/**图片集合**/
	List<HomePageAdData> HomePageAdDataList;
	
	/*********************************一下是准备组装数据用的字段********************************************************/
	
	/**图片集合**/
	List<CmsSingleTempleDTO> singleTempletList;
	

	public List<HomePageAdData> getHomePageAdDataList() {
		return HomePageAdDataList;
	}

	public void setHomePageAdDataList(List<HomePageAdData> homePageAdDataList) {
		HomePageAdDataList = homePageAdDataList;
	}

	public String getToipcSdate() {
		return toipcSdate;
	}

	public void setToipcSdate(String toipcSdate) {
		this.toipcSdate = toipcSdate;
	}

	public String getWeekName() {
		return weekName;
	}

	public void setWeekName(String weekName) {
		this.weekName = weekName;
	}

	public List<CmsSingleTempleDTO> getSingleTempletList() {
		return singleTempletList;
	}

	public void setSingleTempletList(List<CmsSingleTempleDTO> singleTempletList) {
		this.singleTempletList = singleTempletList;
	}

	
}
