package com.tp.service.cms;

import java.util.Map;

import com.tp.dto.cms.temple.HomePageAdData;
import com.tp.exception.CmsServiceException;


/**
* CMS图片模板管理
* @author szy
*/
public interface ISinglePictureTemService {

	/**
     * 首页-最上面-轮播图
     * @return 广告字符串
     * @throws Exception
     */
	String loadHomePageAdInfo() throws Exception;
	
	/**
     * 首页-右边-最优惠的加载
     * @return
     * @throws Exception
     */
    public String loadPreferentialHtml() throws Exception;
    
    /**
	 * 最后疯抢-右边-最优惠加载
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * @author szy
	 */
    public String loadRushedPreferentialHtml() throws Exception;
    
    /**
	 * 首页-刚进去的弹出层
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * @author szy
	 */
    public String loadIndexPicLinkHtml() throws Exception;
    
    /**
	 * 用户管理-用户登录-图片(一张)
	 * @return 
	 * @throws CmsServiceException
	 * @author szy
	 */
    public HomePageAdData loadUserIndexLogin() throws Exception;
    
	/**
     * 海淘首页-特卖精选图片
     * @return 海淘特卖精选的html字符串
     * @throws Exception
     */
	String haiTaoLoadSpecialSellAdInfo() throws Exception;
	
	/**
     * 首页-顶端-大图  和 首页-顶端-小图的map集合
     * @return html字符串
     * @throws Exception
     */
	Map<String, HomePageAdData> indexLoadTopicBigSmalAdInfo() throws Exception;
	
	/**
     * 首页-预留广告位
     * @return html字符串
     * @throws Exception
     */
	String indexLoadAllowAdInfo() throws Exception;
	
	/**
     * 首页-底部广告位
     * @return html字符串
     * @throws Exception
     */
	String indexLoadFootAdInfo() throws Exception;
	
	/**
     * 首页-右边-广告位
     * @return html字符串
     * @throws Exception
     */
	Map<String,String> indexLoadRingAdInfo() throws Exception;
	
}
