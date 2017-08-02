package com.tp.service.cms;

import com.tp.query.mmp.CmsTopicQuery;

/**
* 新板西客商城商城模板管理 Service
* @author szy
*/
public interface IMallIndexService {

	/**
	 * 西客商城商城顶部广告
	 */
	public String mallTopAdHtml();
	
	/**
	 * 西客商城商城导航模板
	 */
	public String mallNavigationHtml();
	
	/**
	 * 西客商城商城详情页面导航模板
	 */
	public String mallCategoryNavigationHtml(String tId);
	
	/**
	 * 西客商城商城目录模板
	 */
	public String mallMenuHtml();
	
	/**
	 * 西客商城商城左侧广告
	 */
	public String mallLeftAdHtml();
	
	/**
	 * 西客商城商城中间上边广告
	 */
	public String mallCenterTopAdHtml();
	
	/**
	 * 西客商城商城中间底部广告
	 */
	public String mallCenterBottomAdHtml();
	
	/**
	 * 西客商城商城右边上侧广告
	 */
	public String mallRightTopAdHtml();
	
	/**
	 * 西客商城商城右侧广告220*213
	 */
	public String mallRightAdHtml();
	
	/**
	 * 西客商城商城大牌精选
	 */
	public String mallBigHtml(CmsTopicQuery query);
	
	/**
	 * 西客商城商城每日专场
	 */
	public String mallTopicHtml(CmsTopicQuery query);
	
	/**
	 * 西客商城商城最下面广告位
	 */
	public String mallBottomHtml();
	
	/**
	 * 西客商城商城详细页-品牌信息
	 */
	public String mallBranchHtml(CmsTopicQuery query);
	
	/**
	 * 西客商城商城详细页-通过类目id查询其及其上级名称和id
	 */
	public String mallCagetoryByIdHtml(Long categoryId);
	
	/**
	 * 西客商城商城详细页-通过筛选条件，查询具体商品信息
	 */
	public String mallCagetoryItemInfoHtml(CmsTopicQuery query);
	
	/**
	 * 西客商城商城首页-自定义编辑区
	 */
	public String mallDefinedHtml();
}
