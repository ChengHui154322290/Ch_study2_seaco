package com.tp.service.cms;

import java.util.List;
import java.util.Map;

import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.app.AppSingleCotAllInfoDTO;
import com.tp.dto.cms.app.AppSingleInfoDTO;
import com.tp.dto.cms.app.query.AppTopItemPageQuery;
import com.tp.dto.cms.query.HiTaoParamSingleBusQuery;
import com.tp.dto.cms.query.ParamSingleBusTemQuery;
import com.tp.dto.cms.temple.Topic;
import com.tp.dto.mmp.TopicItemBrandCategoryDTO;
import com.tp.enums.common.PlatformEnum;
import com.tp.exception.CmsServiceException;
import com.tp.model.bse.DistrictInfo;
import com.tp.query.mmp.TopicItemPageQuery;
import com.tp.result.bse.ChinaRegionInformation;


/**
* 单品团模板管理（业务） Service
*/
public interface ISingleBusTemService {
	
	
	/**
	 * 首页-单品团管理(PC端)
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * 
	 */
	String  singleProduct(ParamSingleBusTemQuery paramSingleBusTemQuery) throws Exception;
	
	
	/**
	 * 首页-单品团管理(APP端)
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * 
	 */
	PageInfo<AppSingleCotAllInfoDTO>  singleProductAPP (int pagestart,int pagesize) throws Exception;
	
	/**
	 * 秒杀单品团(APP端) ：在cms中进行配置，读取都是和app单品团一致
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * 
	 */
	PageInfo<AppSingleCotAllInfoDTO>  querySeckillSingleInfo (int pagestart,int pagesize) throws Exception;
	
	/**
	 * WAP-今日精选(APP端) ：在cms中进行配置，读取都是和app单品团一致
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * 
	 */
	Map<String,Object> queryWAPChosenSingleInfo(int pagestart,int pagesize) throws Exception;
	
	/**
	 * 首页-今日你特卖管理  即活动信息
	 * 注意：每次鼠标滑动，会调此方法，并加载3个数据过去，直到没有数据加载
	 * 		需要有标识去记录页数，前台有记录，每次结束后会自动加1，然后从数据库中查询
	 * pagestart:起始页      pageSize：每页数量
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * 
	 */
	String  singleIndexDiscountInfo (ParamSingleBusTemQuery paramSingleBusTemQuery) throws Exception;
	
	/**
	 * 首页-今日必海淘管理
	 * 描述：把今天所要卖的全部加载出来,但是首页加载的时候会有四个，如果大于四的话就要另起一行重新排序
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * 
	 */
	String  singleloadHoardHtml (ParamSingleBusTemQuery paramSingleBusTemQuery) throws Exception;
	
	/**
	 * 最后疯抢页面-今日特卖管理(PC)
	 * 注意：后台没有模板配置，直接取促销数据
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * 
	 */
	String  singleRushedDiscountHtml (ParamSingleBusTemQuery paramSingleBusTemQuery) throws Exception;
	
	/**
	 * 最后疯抢页面-今日特卖管理(APP)
	 * 注意：后台没有模板配置，直接取促销数据
	 * 
	 * @param pageStart 起始页
	 * @param pageSize  页大小
	 * @param platformType 平台标识
	 * 
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * 
	 */
	PageInfo<AppSingleInfoDTO>  singleRushedDiscountHtmlAPP (int pagestart, int pageSize, PlatformEnum platformType) throws Exception;
	
	/**
	 * 明日预告管理(PC)
	 * 注意：后台没有模板配置，直接取促销数据
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * 
	 */
	Map<String,String>  singleAdvanceHtml (ParamSingleBusTemQuery paramSingleBusTemQuery) throws Exception;
	
	/**
	 * 明日预告管理(APP)
	 * 注意：后台没有模板配置，直接取促销数据
	 * 
	 * @param pageStart 起始页
	 * @param pageSize  页大小
	 * @param dateCounts  日期编号，明天是1，后天是2
	 * @param platformType 平台标识
	 * 
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * 
	 */
	PageInfo<AppSingleInfoDTO>  singleAdvanceHtmlAPP (int pagestart,int pageSize,int dateCounts,PlatformEnum platformType,long userId) throws Exception;
	
	/**
     * 首页-公告资讯的加载
     * @return
     * @throws Exception
     */
    public String loadIndexAnnouncementHtml() throws Exception;
    	
	/**
     * 最后疯抢-公告资讯的加载
     * @return
     * @throws Exception
     */
    public String loadRushedAnnouncementHtml() throws Exception;
    
    /**
     * 首页-自定义编辑区域
     * @return
     * @throws Exception
     */
    public String loadIndexDefinedHtml() throws Exception;
    
    /**
     * 西客商城商城品牌专题-过滤条件的加载：分类和品牌加载
     * @return
     * @throws Exception
     */
    public String loadFilterclassInfoHtml(long topicId) throws Exception;
    
    /**
     * 品牌专题-通过过滤条件去查品牌明细
     * @param 
     * @des 
     * @return
     * @throws Exception
     */
    public String loadTopiInfocHtml(TopicItemPageQuery query) throws Exception;
    
    /**
     * 品牌专题-过滤条件的加载：分类和品牌加载(APP)
     * @return
     * @throws Exception
     */
    public Map<String, Object> loadFilterclassInfoHtmlApp(List<Long> brandids,List<Long> kinds) throws Exception;
    
    /**
     * 品牌专题-通过过滤条件去查品牌明细(APP)
     * @param 
     * @des 
     * @return
     * @throws Exception
     */
    public Topic loadTopiInfocHtmlApp( AppTopItemPageQuery query) throws Exception;

    
    /**
     * 品牌专题-通过过滤条件去查品牌明细(APP)
     * @param 
     * @des 
     * @return
     * @throws Exception
     */
    public Topic loadTopiInfocHtmlAppForDss( AppTopItemPageQuery query) throws Exception;

    /**
     * 专场详情-通过专场id查询专场详情(APP)
     * @param 
     * @des 
     * @return
     * @throws Exception
     */
    public TopicItemBrandCategoryDTO loadTopiInHtmlApp(long topicId) throws Exception;
    
    /**
     * 品牌专题最下方的-品牌兴趣加载
     * @return
     * @throws Exception
     */
    public String loadInterestHtml(long topicId,int size) throws Exception;
    
    /**
     * 品牌Banner加载，即品牌专题最上面的一张图的加载，后来确认是从促销那边拿的一段html代码
     * @return
     * @throws Exception
     */
    public String loadBannerHtml(Long topicId) throws Exception;
    
    /**
     * 海淘首页：顶上的自定义编辑区的html代码片
     * @return
     * @throws Exception
     */
    public String haiTaoloadDefinedBannerHtml() throws Exception;
    
    /**
	 * 海淘首页：今日精选，是在cms后台配置出来
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * 
	 */
	public String loadHaitaoDiscount(HiTaoParamSingleBusQuery params) throws Exception;
	
	/**
	 * 西客商城首页：热销榜单
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * 
	 */
	public String loadIndexHotSell(ParamSingleBusTemQuery params) throws Exception;
	
	/**
	 * 西客商城首页：品牌精选
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * 
	 */
	public String loadIndexTopicDiscount(ParamSingleBusTemQuery params) throws Exception;
	
	/**
	 * 明日预告-首页新版本：没有日期包装，一次性把今天以后的活动全部显示出来，加上分页（PC）
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String loadIndextrailerHtml(ParamSingleBusTemQuery params) throws Exception;
	
	/**
	 * 明日预告-首页：没有日期包装，一次性把今天以后的活动全部显示出来，加上分页（APP）
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public PageInfo<AppSingleInfoDTO> loadAPPIndextrailerHtml(ParamSingleBusTemQuery params) throws Exception;
	
	/**
	 * 取地址信息-首页新版本
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> loadAddrMessageHtml(List<DistrictInfo> addr,List<ChinaRegionInformation> addrFloat) throws Exception;
	
	
	/**
	 * 取购物车信息-首页新版本
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> loadShoppingHtml(Long uid,String salesorderDomain,String addr,String itemurl, String countdown,String countdownDatte) throws Exception;
	
	
    
}
