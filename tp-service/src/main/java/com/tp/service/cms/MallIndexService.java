package com.tp.service.cms;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.cms.ElementEnum;
import com.tp.common.vo.cms.PageTempletConstant;
import com.tp.common.vo.cms.PictureSizeConstant;
import com.tp.common.vo.cms.TempleConstant;
import com.tp.dao.cms.PositionDao;
import com.tp.dao.cms.TempleDao;
import com.tp.dto.cms.temple.HomePageAdData;
import com.tp.dto.cms.temple.Mall;
import com.tp.dto.cms.temple.MallReturnData;
import com.tp.dto.cms.temple.MallReturnListData;
import com.tp.dto.cms.temple.Products;
import com.tp.dto.mmp.TopicDetailDTO;
import com.tp.dto.mmp.TopicItemBrandCategoryDTO;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.dto.mmp.enums.TopicType;
import com.tp.model.bse.Brand;
import com.tp.model.cms.ActivityElement;
import com.tp.model.cms.DefinedElement;
import com.tp.model.cms.PictureElement;
import com.tp.model.cms.Temple;
import com.tp.model.cms.WrittenElement;
import com.tp.model.mmp.Topic;
import com.tp.query.mmp.CmsTopicQuery;
import com.tp.result.mmp.TopicItemInfoResult;
import com.tp.service.bse.IBrandService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.cms.IMallIndexService;
import com.tp.service.mmp.ITopicService;
import com.tp.service.mmp.remote.PromotionRemoteForCMS;


@Service(value="mallIndexService")
public class MallIndexService<T> implements IMallIndexService{

	@Autowired
	private PromotionRemoteForCMS promotionRemoteForCMS;
	
	@Value("${cms_topic_adress}")
	private String cmsTopicAdress;
	
	@Value("${cms_chaimed_adress}")
	private String cmsChaimedAdress;
	
	/*@Autowired
	DfsDomainUtil dfsDomainUtil;*/
	
	@Autowired
	SwitchBussiesConfigDao switchBussiesConfigDao;
	
	@Autowired
	TempleDao templeDao;
	
	@Autowired
	PositionDao positionDao;
	
	@Autowired
    private IBrandService brandService;
	
	@Autowired
    private ICategoryService categoryService;
	
	@Autowired
	private ITopicService topicService;
	
	private final static Log logger = LogFactory.getLog(MallIndexService.class);
	
	/**
	 * 顶部广告位
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String mallTopAdHtml() {
		Map<String, Object> templateData = new HashMap<String, Object>();
		HomePageAdData ad = new HomePageAdData();
		
		try {
			List<PictureElement> lst = (List<PictureElement>) queryPageTempletElementInfo(
					PageTempletConstant.PG_XGSC,PageTempletConstant.XGSC_TOP_ADVERT);
			if(lst==null || lst.size()==0){
				return "";
			}
			PictureElement cmsPictureElementDO = lst.get(0);
			//ad.setImageSrc(dfsDomainUtil.getFileFullUrl(cmsPictureElementDO.getPicSrc()));
			ad.setImageSrc(switchBussiesConfigDao.getFullPictureSrc_PC(cmsPictureElementDO.getPicSrc()));
			ad.setLink(contLink(cmsPictureElementDO.getLink()));
		} catch (Exception e) {
			logger.error("西客商城商城-顶部广告位查询报错", e);
			e.printStackTrace();
		}
		
		templateData.put("ad", ad);
		String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/malltopad.flt",new StringWriter());
	    return str;
	}
	
	/**
	 * 商城首页：导航栏
	 * @return html字符串的代码
	 * @throws Exception
	 * @author szy
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String mallNavigationHtml() {
		Map<String, Object> templateData = new HashMap<String, Object>();
		List<MallReturnData> templateList = new ArrayList<MallReturnData>();
		
		try {
			List<WrittenElement> lst = (List<WrittenElement>) queryPageTempletElementInfo(
					PageTempletConstant.PG_XGSC,PageTempletConstant.XGSC_NAVIGAT);
			if(lst==null || lst.size()==0){
				return "";
			}
			for(WrittenElement cmsWrittenElementDO:lst){
				MallReturnData mallReturnData = new MallReturnData();
				mallReturnData.setLink(contLink(cmsWrittenElementDO.getLink()));
				mallReturnData.setName(cmsWrittenElementDO.getName());
				templateList.add(mallReturnData);
			}
		} catch (Exception e) {
			logger.error("西客商城商城-导航栏查询报错", e);
			e.printStackTrace();
		}
	   	
	   	templateData.put("navigationList", templateList);
	   	
	   	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/mallnavigation.flt",new StringWriter());
	    return str;
	}
	
	/**
	 * 商城详情页：导航栏
	 * @return html字符串的代码
	 * @throws Exception
	 * @author szy
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String mallCategoryNavigationHtml(String tId) {
		Map<String, Object> templateData = new HashMap<String, Object>();
		List<MallReturnData> templateList = new ArrayList<MallReturnData>();
		
		try {
			List<WrittenElement> lst = (List<WrittenElement>) queryPageTempletElementInfo(
					PageTempletConstant.PG_XGSC,PageTempletConstant.XGSC_NAVIGAT);
			if(lst==null || lst.size()==0){
				return "";
			}
			for(WrittenElement cmsWrittenElementDO:lst){
				MallReturnData mallReturnData = new MallReturnData();
				mallReturnData.setLink(contLink(cmsWrittenElementDO.getLink()));
				mallReturnData.setName(cmsWrittenElementDO.getName());
				templateList.add(mallReturnData);
			}
			StringBuffer url =  new StringBuffer();
			url.append("category-");
			url.append(tId);
			url.append(".htm");
			templateData.put("navigationList", templateList);
			templateData.put("url", url.toString());
		} catch (Exception e) {
			logger.error("西客商城商城-商城详情页：导航栏的查询报错", e);
			e.printStackTrace();
		}
	   	
	   	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/categorynavigation.flt",new StringWriter());
	    return str;
	}
	

	/**
	 * 商城首页：目录栏
	 * @return html字符串的代码
	 * @throws Exception
	 * @author szy
	 */
	@Override
	public String mallMenuHtml() {
		Map<String, Object> templateData = new HashMap<String, Object>();
		List<MallReturnListData> templateList = new ArrayList<MallReturnListData>();
		
		try {
			Temple cmsTempleDO = new Temple();
			cmsTempleDO.setTempleCode(PageTempletConstant.XGSC_CATALOG);
			List<Temple> lst = positionDao.selectTempletByTempletCode(cmsTempleDO);
			if(lst!=null && lst.size()>0){
				for(Temple mode:lst){
					List<WrittenElement> cmsTempleDOList = positionDao.selectWrittenByTempletId(mode);
					if(cmsTempleDOList!=null && cmsTempleDOList.size()>0){
						MallReturnListData mallReturnListData = new MallReturnListData();
						List<MallReturnData> MallReturnList =  new ArrayList<MallReturnData>();
						for(WrittenElement cmsWrittenElementDO:cmsTempleDOList){
							MallReturnData mallReturnData = new MallReturnData();
							mallReturnData.setLink(contLink(cmsWrittenElementDO.getLink()));
							mallReturnData.setName(cmsWrittenElementDO.getName());
							MallReturnList.add(mallReturnData);
						}
						mallReturnListData.setMallReturnList(MallReturnList);
						templateList.add(mallReturnListData);
					}
				}
			}
			
			templateData.put("menuList", templateList);
		} catch (Exception e) {
			logger.error("西客商城商城-目录栏的查询报错", e);
			e.printStackTrace();
		}
	   	
	   	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/mallmenu.flt",new StringWriter());
	    return str;
	}

	/**
	 * 商城首页：左侧广告位
	 * @return html字符串的代码
	 * @throws Exception
	 * @author szy
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String mallLeftAdHtml() {
		Map<String, Object> templateData = new HashMap<String, Object>();
		HomePageAdData ad = new HomePageAdData();
		
		try {
			List<PictureElement> lst = (List<PictureElement>) queryPageTempletElementInfo(
					PageTempletConstant.PG_XGSC,PageTempletConstant.XGSC_LEFT_ADVERT);
			if(lst==null || lst.size()==0){
				return "";
			}
			PictureElement cmsPictureElementDO = lst.get(0);
			//ad.setImageSrc(dfsDomainUtil.getFileFullUrl(cmsPictureElementDO.getPicSrc()));
			//ad.setImageSrc(dfsDomainUtil.getSnapshotUrl(cmsPictureElementDO.getPicSrc(),218));
			ad.setImageSrc(switchBussiesConfigDao.getFullPictureSrc_PC(cmsPictureElementDO.getPicSrc()));
			ad.setLink(contLink(cmsPictureElementDO.getLink()));
			
			templateData.put("ad", ad);
		} catch (Exception e) {
			logger.error("西客商城商城-左侧广告位的查询报错", e);
			e.printStackTrace();
		}
		String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/mallleftad.flt",new StringWriter());
	    return str;
	}

	/**
	 * 商城首页：右侧广告位 220*213
	 * @return html字符串的代码
	 * @throws Exception
	 * @author szy
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String mallRightAdHtml() {
		Map<String, Object> templateData = new HashMap<String, Object>();
		HomePageAdData ad = new HomePageAdData();
		
		try {
			List<PictureElement> lst = (List<PictureElement>) queryPageTempletElementInfo(
					PageTempletConstant.PG_XGSC,PageTempletConstant.XGSC_RIGHT_ADVERT);
			if(lst==null || lst.size()==0){
				return "";
			}
			PictureElement cmsPictureElementDO = lst.get(0);
			//ad.setImageSrc(dfsDomainUtil.getFileFullUrl(cmsPictureElementDO.getPicSrc()));
			//ad.setImageSrc(dfsDomainUtil.getSnapshotUrl(cmsPictureElementDO.getPicSrc(),220));
			ad.setImageSrc(switchBussiesConfigDao.getFullPictureSrc_PC(cmsPictureElementDO.getPicSrc()));
			ad.setLink(contLink(cmsPictureElementDO.getLink()));
			
			templateData.put("ad", ad);
		} catch (Exception e) {
			logger.error("西客商城商城-右侧广告位 220*213的查询报错", e);
			e.printStackTrace();
		}
		String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/mallrightad.flt",new StringWriter());
	    return str;
	}
	/**
	 * 商城首页：右侧广告位  上边
	 * @return html字符串的代码
	 * @throws Exception
	 * @author szy
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String mallRightTopAdHtml() {
		Map<String, Object> templateData = new HashMap<String, Object>();
		List<HomePageAdData> homePageAdList = new  ArrayList<HomePageAdData>();
		
		try {
			List<PictureElement> lst = (List<PictureElement>) queryPageTempletElementInfo(
					PageTempletConstant.PG_XGSC,PageTempletConstant.XGSC_RIGHT_TOP_ADVERT);
			if(lst==null || lst.size()==0){
				return "";
			}
			for(PictureElement cmsPictureElementDO:lst){
				HomePageAdData ad = new HomePageAdData();
				//ad.setImageSrc(dfsDomainUtil.getFileFullUrl(cmsPictureElementDO.getPicSrc()));
				//ad.setImageSrc(dfsDomainUtil.getSnapshotUrl(cmsPictureElementDO.getPicSrc(),220));
				ad.setImageSrc(switchBussiesConfigDao.getFullPictureSrc_PC(cmsPictureElementDO.getPicSrc()));
				ad.setLink(contLink(cmsPictureElementDO.getLink()));
				homePageAdList.add(ad);
			}
			
			templateData.put("adList", homePageAdList);
		} catch (Exception e) {
			logger.error("西客商城商城-右侧广告位  上边的查询报错", e);
			e.printStackTrace();
		}
		String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/mallrighttopad.flt",new StringWriter());
	    return str;
	}


	/**
	 * 商城首页：中间广告位 下边（配置的是品牌信息）
	 * @return html字符串的代码
	 * @throws Exception
	 * @author szy
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String mallCenterBottomAdHtml() {
		Map<String, Object> templateData = new HashMap<String, Object>();
		List<HomePageAdData> homePageAdList = new  ArrayList<HomePageAdData>();
		
		List<ActivityElement> lst = new ArrayList<ActivityElement>();
		try {
			lst = (List<ActivityElement>) queryPageTempletElementInfo(
					PageTempletConstant.PG_XGSC,PageTempletConstant.XGSC_MIDDLE_UND_ADVERT);
		} catch (Exception e) {
			logger.error("西客商城商城-中间广告位下边的品牌团信息，取cms后台品牌活动id报错", e);
			e.printStackTrace();
		}
		if(lst==null || lst.size()==0){
			return "";
		}
		
		List<Long> ldList = new ArrayList<Long>();
		for (ActivityElement cmsActivityElementDO:lst) {
			ldList.add(cmsActivityElementDO.getActivityId());
		}
		//先从促销那边取数据，传入id列表，查到具体bean
		List<TopicDetailDTO> topicDetailDTO = new ArrayList<TopicDetailDTO>();
		try {
			topicDetailDTO = topicService.getTopicDetailByIdsForCms(null, ldList);
		} catch (Exception e) {
			logger.error("西客商城商城-顶部广告位查询，通过品牌活动id列表从促销那边取数据报错", e);
			e.printStackTrace();
		}
		List<Long> ldBrandList = new ArrayList<Long>();
		if(topicDetailDTO != null && topicDetailDTO.size()>0 ){
			
       		//循环促销数据，找到id值，组装成ids列表去品牌那边查询数据
       		for(TopicDetailDTO dtomd:topicDetailDTO){
       			Topic topicDO = dtomd.getTopic();
       			if(topicDO != null){
           			ldBrandList.add(topicDO.getBrandId());
       			}
       		}
       		
       		//从基础库中查询品牌logo
		    List<Brand> brandDOList = brandService.selectListBrand(ldBrandList, 2);
		    
			for (ActivityElement cmsActivityElementDO:lst) {
				String pic = cmsActivityElementDO.getPicture();
				String link = cmsActivityElementDO.getLink();
				
				//对应每个活动主键，找到每个品牌图片logo地址
			    for(TopicDetailDTO dtomd:topicDetailDTO){
			    	Topic topicDO = dtomd.getTopic();
			    	
			    	if(cmsActivityElementDO.getActivityId().equals(topicDO.getId()) 
			    			&& TopicStatus.PASSED.ordinal() == topicDO.getStatus().intValue()){
			    		
			    		HomePageAdData homePageAdData = new HomePageAdData();
				    	
				    	if(link == null || "".equals(link)){
				    		homePageAdData.setLink(splitJoinTopicAdress(topicDO.getId()));
				    	}else{
				    		homePageAdData.setLink(contLink(link));
				    	}
				    	
				    	if(pic == null || "".equals(pic)){
				    		for(Brand brandDO:brandDOList){
				    			
					    		try {
									if(topicDO.getBrandId().longValue() == brandDO.getId().longValue()){
										//homePageAdData.setImageSrc(dfsDomainUtil.getSnapshotUrl(brandDO.getLogo(),142));
										homePageAdData.setImageSrc(
												switchBussiesConfigDao.getFullPictureSrc_PC(brandDO.getLogo()));
										break;
									}
								} catch (Exception e) {
									homePageAdData.setImageSrc("");
									logger.error("西客商城商城，品牌团logo查询，活动id："+topicDO.getId()+"没有品牌id", e);
									e.printStackTrace();
									break;
								}
					    		
					    	}
				    	}else{
				    		//homePageAdData.setImageSrc(dfsDomainUtil.getSnapshotUrl(pic,142));
				    		homePageAdData.setImageSrc(
				    				switchBussiesConfigDao.getFullPictureSrc_PC(pic));
				    	}
				    	
				    	homePageAdList.add(homePageAdData);
			    		break;
			    	}
			    	
				}
				
			}
		    
       	}
		
		templateData.put("adList", homePageAdList);
		String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/mallcenterbottomad.flt",new StringWriter());
	    return str;
	}

	/**
	 * 商城首页：中间广告位 上边
	 * @return html字符串的代码
	 * @throws Exception
	 * @author szy
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String mallCenterTopAdHtml() {
		Map<String, Object> templateData = new HashMap<String, Object>();
		List<HomePageAdData> homePageAdList = new  ArrayList<HomePageAdData>();
		
		List<PictureElement> lst = new ArrayList<PictureElement>();
		try {
			lst = (List<PictureElement>) queryPageTempletElementInfo(
					PageTempletConstant.PG_XGSC,PageTempletConstant.XGSC_MIDDLE_TOP_ADVERT);
		} catch (Exception e) {
			logger.error("类型转换错误，数据库数据有误", e);
			e.printStackTrace();
		}
		if(lst==null || lst.size()==0){
			return "";
		}
		for(PictureElement cmsPictureElementDO:lst){
			HomePageAdData ad = new HomePageAdData();
			//ad.setImageSrc(dfsDomainUtil.getFileFullUrl(cmsPictureElementDO.getPicSrc()));
			ad.setImageSrc(switchBussiesConfigDao.getFullPictureSrc_PC(cmsPictureElementDO.getPicSrc()));
			ad.setLink(contLink(cmsPictureElementDO.getLink()));
			homePageAdList.add(ad);
		}
		
		templateData.put("adList", homePageAdList);
		String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/mallcentertopad.flt",new StringWriter());
	    return str;
	}

	/**
	 * 商城首页：大牌精选,取的是  品牌团  的数据
	 * @return html字符串的代码
	 * @throws Exception
	 * @author szy
	 */
	@Override
	public String mallBigHtml(CmsTopicQuery query) {
		Map<String, Object> templateData = new HashMap<String, Object>();
		List<Mall> homePageAdList = new  ArrayList<Mall>();
		
		//取品牌团
		query.setTopicType(TopicType.BRAND.ordinal());
		PageInfo<TopicDetailDTO> page = promotionRemoteForCMS.getXGMallTopicList(query);
		List<TopicDetailDTO> lst = page.getRows();
		if(lst == null || lst.size()==0){
			return "";
		}
		for(TopicDetailDTO mode:lst){
			Mall ad = new Mall();
			
			Topic topicDO = mode.getTopic();
			//ad.setDetail(topicDO.getIntro());
			ad.setImgLink(splitJoinTopicAdress(topicDO.getId()));
			//注意：此处取图片需要取海淘图片
			//ad.setImgsrc(dfsDomainUtil.getFileFullUrl(topicDO.getImage()));
			//ad.setImgsrc(dfsDomainUtil.getSnapshotUrl(topicDO.getImageHitao(),576));
			ad.setImgsrc(switchBussiesConfigDao.getPictureSrc_PC(topicDO.getMallImage(), PictureSizeConstant.MALL_BIG_CARE_SELECT));
			ad.setName(topicDO.getName());
			ad.setRate(topicDO.getDiscount());
			ad.setTopicPoint(topicDO.getTopicPoint());
			
			homePageAdList.add(ad);
		}
		templateData.put("mallList", homePageAdList);
		String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/mallbigad.flt",new StringWriter());
	    return str;
	}
	
	/**
	 * 商城首页：每日专场，取的是  主题团  的数据
	 * @return html字符串的代码
	 * @throws Exception
	 * @author szy
	 */
	@Override
	public String mallTopicHtml(CmsTopicQuery query) {
		Map<String, Object> templateData = new HashMap<String, Object>();
		List<Mall> homePageAdList = new  ArrayList<Mall>();
		
		//取主题团
		query.setTopicType(TopicType.THEME.ordinal());
		PageInfo<TopicDetailDTO> page = promotionRemoteForCMS.getXGMallTopicList(query);
		List<TopicDetailDTO> lst = page.getRows();
		if(lst == null || lst.size()==0){
			return "";
		}
		for(TopicDetailDTO mode:lst){
			Mall ad = new Mall();
			
			Topic topicDO = mode.getTopic();
			//ad.setDetail(topicDO.getIntro());
			ad.setImgLink(splitJoinTopicAdress(topicDO.getId()));
			//ad.setImgsrc(dfsDomainUtil.getFileFullUrl(topicDO.getImage()));
			//ad.setImgsrc(dfsDomainUtil.getFileFullUrl(topicDO.getImageHitao()));
			//ad.setImgsrc(dfsDomainUtil.getSnapshotUrl(topicDO.getImageHitao(),576));
			ad.setImgsrc(switchBussiesConfigDao.getPictureSrc_PC(topicDO.getMallImage(),
					PictureSizeConstant.MALL_TODAY_TOPIC_LIST));
			ad.setName(topicDO.getName());
			ad.setRate(topicDO.getDiscount());
			ad.setTopicPoint(topicDO.getTopicPoint());
			
			homePageAdList.add(ad);
		}
		templateData.put("mallList", homePageAdList);
		String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/malltopicad.flt",new StringWriter());
		return str;
	}
	
	/**
	 * 商城首页：最下面广告位
	 * @return html字符串的代码
	 * @throws Exception
	 * @author szy
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String mallBottomHtml() {
		Map<String, Object> templateData = new HashMap<String, Object>();
		HomePageAdData ad = new HomePageAdData();
		
		try {
			List<PictureElement> lst = (List<PictureElement>) queryPageTempletElementInfo(PageTempletConstant.PG_XGSC,PageTempletConstant.XGSC_UND_ADVERT);
			if(lst==null || lst.size()==0){
				return "";
			}
			PictureElement cmsPictureElementDO = lst.get(0);
			//ad.setImageSrc(dfsDomainUtil.getFileFullUrl(cmsPictureElementDO.getPicSrc()));
			ad.setImageSrc(switchBussiesConfigDao.getFullPictureSrc_PC(cmsPictureElementDO.getPicSrc()));
			ad.setLink(contLink(cmsPictureElementDO.getLink()));
			
			templateData.put("ad", ad);
		} catch (Exception e) {
			logger.error("西客商城商城-最下面广告位的查询报错", e);
			e.printStackTrace();
		}
		String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/mallbottomad.flt",new StringWriter());
		return str;
	}
	
	/**
	 * 调用查询元素信息，参数为页面编码和模块编码
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked" })
	private List<T> queryPageTempletElementInfo(String pageCode,
			String templetCode) throws Exception {
		/*Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pageCode", pageCode);
		paramMap.put("templetCode", templetCode);*/
		
		//通过模板编码获取模板id，注意：模板编码且启用的模板是唯一的，这需要后台管理去控制
		Temple cmsTempleDO = new Temple();
		cmsTempleDO.setTempleCode(templetCode);
		cmsTempleDO.setStatus(0);
		List<Temple> cmsPositionDOList = templeDao.queryByObject(cmsTempleDO);
		
		if(cmsPositionDOList == null || cmsPositionDOList.size()==0){
			return null;
		}
		
		int elementType = cmsPositionDOList.get(0).getElementType();
		cmsTempleDO.setId(cmsPositionDOList.get(0).getId());
		if(ElementEnum.ACTIVITY.getValue() == elementType){
			return (List<T>) positionDao.selectActivityByTempletId(cmsTempleDO);
		}else if(ElementEnum.WRITTEN.getValue() == elementType){
			return (List<T>) positionDao.selectWrittenByTempletId(cmsTempleDO);
		}
		else if(ElementEnum.PICTURE.getValue() == elementType){
			return (List<T>) positionDao.selectPicByTempletId(cmsTempleDO);
		}else if(ElementEnum.DEFINED.getValue() == elementType){
			return (List<T>) positionDao.selectDefinedByTempletId(cmsTempleDO);
		}
		return null;
	}
	
	/**
     * 建立活动topic的链接地址:活动地址
     * @param activityId 活动id
     * @param skuId	skuid
     * @return
     */
    public String splitJoinTopicAdress(Long activityId){
    	return cmsTopicAdress.replaceAll("parames", activityId.toString());
    }

    public String contLink(String linkStr){
    	if(linkStr==null || "".equals(linkStr)){
    		return "";
    	}
    	if(!linkStr.contains("http://") && !linkStr.contains("https://")){
			return "http://"+linkStr;
		}else{
			return linkStr;
		}
    }

    /**
	 * 西客商城商城详细页-品牌信息
	 * 注意：传入类目categoryId即可，查询该类目categoryId下面的所有品牌信息
	 */
	@Override
	public String mallBranchHtml(CmsTopicQuery query) {
		TopicItemBrandCategoryDTO page = promotionRemoteForCMS.getXGMallBrandList(query);
		List<Long> brandIds = page.getBrandIdList();
		
		List<Brand> brands = brandService.selectListBrand(brandIds,2);
		
		if(brands != null){
			for(Brand brandDO:brands){
				//brandDO.setLogo(dfsDomainUtil.getSnapshotUrl(brandDO.getLogo(),100));
				brandDO.setLogo(switchBussiesConfigDao.getFullPictureSrc_PC(brandDO.getLogo()));
			}
		}
		Map<String, Object> templateData = new HashMap<String, Object>();
    	templateData.put("brands", brands);
    	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/categoryproductsfilter.flt",new StringWriter());
    	return str;
	}

	/**
	 * 西客商城商城详细页-通过类目id查询其及其上级名称和id
	 * desc:返回的list，第一个值是最顶级的目录，下面依次类推
	 */
	@Override
	public String mallCagetoryByIdHtml(Long categoryId) {
		/*List<Category> lst = categoryService.getParentCategoryList(categoryId);
		Map<String, Object> templateData = new HashMap<String, Object>();
		templateData.put("lst", lst);
    	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/categorymenu.flt",new StringWriter());
		
		return str;*/
		return null;
	}

	/**
	 * 西客商城商城详细页-通过筛选条件，查询具体商品信息
	 * 注意：1.传入参数：类目id，必传的;
	 * 	   2.传入参数：List<Long> brandIds ->设置品牌id的集合;
	 * 	   3.传入参数：List<String> orderSortColumns ->设置排序字段，直接往list集合设值，
	 * 			DEFAULT表示默认排序，PRICE表示金额排序。
	 */
	@Override
	public String mallCagetoryItemInfoHtml(CmsTopicQuery query) {
		Map<String, Object> templateData = new HashMap<String, Object>();
		query.setPageSize(100);
		if(query.getPageSize()==0){
			//暂时设置页数为8
	    	query.setPageSize(100);
		}
		if(query.getPageId()==0){
			query.setPageId(1);
		}
		
		PageInfo<TopicItemInfoResult> page = promotionRemoteForCMS.getXGMallItemList(query);
		List<TopicItemInfoResult> topicDetailDTOList = page.getRows();
		if(topicDetailDTOList == null || topicDetailDTOList.size()==0){
			//如果活动不存在，抛出异常
			logger.info("获取专题活动有错误，该专题活动id不存在");
			return null;
		}
    	
    	com.tp.dto.cms.temple.Topic topic = new com.tp.dto.cms.temple.Topic();
    	//设置总页码 Math.ceil(1/Double.parseDouble(8+""))
    	topic.setAllPages((int)Math.ceil(page.getTotal()/Double.parseDouble(query.getPageSize()+"")));
    	//设置当前页码
    	topic.setCurrentPages(query.getPageId());
    	/*//设置专题名称
    	if(TopicItemInfoPage.getList() != null){
    		topic.setName(((TopicItemInfoResult)TopicItemInfoPage.getList()).getTopicName());
    	}*/
    	for(int i=0;i<topicDetailDTOList.size();i++){
    		TopicItemInfoResult topicItemInfoResult = topicDetailDTOList.get(i);
//    		if(TopicItemInfoResult.getTopicStatus() != TopicStatus.PASSED.ordinal()){
//    			//如果活动状态不是审核通过的，抛出异常
//    			logger.info("获取专题状态有错误，不是编辑中的活动");
//    			throw new NullPointerException();
//    		}
    		
    		Products productNormal = new Products();
        	productNormal.setSrclink("/detail");
        	
        	//图片地址：注意此处需要改尺寸，dfs工具代码为：getSnapshotUrl
        	//productNormal.setImgsrc(dfsDomainUtil.getSnapshotUrl(topicItemInfoResult.getTopicImage(),275));
        	productNormal.setImgsrc(switchBussiesConfigDao.getPictureSrc_PC(
        			topicItemInfoResult.getTopicImage(),PictureSizeConstant.MALL_ITEM_PICTURE));
        	
        	//图片链接:注意此处需要改尺寸
        	productNormal.setNamelink(CmsTempletUtil.splitJoinLinkAdress(cmsChaimedAdress,topicItemInfoResult.getTopicId(),topicItemInfoResult.getSku()));
        	//商品名称
        	productNormal.setName(topicItemInfoResult.getItemName());
        	
        	//字符，特定设置
        	productNormal.setMoney("&yen;");
        	//活动价格
        	productNormal.setNowValue(topicItemInfoResult.getTopicPrice());
        	//原价格
        	productNormal.setLastValue(topicItemInfoResult.getSalePrice());
        	
        	//折扣价格
        	productNormal.setDiscountPrice(CmsTempletUtil.getPriceDiscount(topicItemInfoResult.getTopicPrice(), topicItemInfoResult.getSalePrice()));
        	
        	//已卖出数量
        	productNormal.setNowHoard(topicItemInfoResult.getSaledAmount()+"人已海淘");
        	//品牌链接
        	productNormal.setSeelink(CmsTempletUtil.splitJoinLinkAdress(cmsChaimedAdress,topicItemInfoResult.getTopicId(),topicItemInfoResult.getSku()));
        	//开始时间
        	productNormal.setStartDate(topicItemInfoResult.getStartTime());
        	//结束时间
        	productNormal.setEndDate(topicItemInfoResult.getEndTime());
        	//sku
        	productNormal.setSku(topicItemInfoResult.getSku());
        	
        	//活动Id
        	productNormal.setTopicid(String.valueOf(topicItemInfoResult.getTopicId()));
        	//开始时间：秒
        	if(topicItemInfoResult.getStartTime() != null){
        		productNormal.setStartDateSeckend(topicItemInfoResult.getStartTime().getTime());
        	}
        	
        	//结束时间：秒
        	if(topicItemInfoResult.getEndTime() != null){
        		productNormal.setEndDateSeckend(topicItemInfoResult.getEndTime().getTime());
        	}
        	
        	productNormal.setSee("去看看");
        	
        	if(topicItemInfoResult.getStartTime() != null){
        		if(new Date().getTime() < topicItemInfoResult.getStartTime().getTime() ){
        			productNormal.setType("noStart");
        		}
        	}else{
        		productNormal.setType("normal");
        	}
        	
        	if(topicItemInfoResult.getTopicStatus() != TopicStatus.PASSED.ordinal() && topicItemInfoResult.getTopicStatus() != TopicStatus.STOP.ordinal() ){
        		productNormal.setType("editing");
        	}
        	
        	if(!topicItemInfoResult.isHasStock()){
        		productNormal.setType("ruball");//已抢光
        	}
        	
        	topic.getProductsList().add(productNormal);
    	}
		
    	templateData.put("topic", topic);
		/*templateData.put("pic", dfsDomainUtil.getSnapshotUrl(topicDO.getImageInterested(),200));
		templateData.put("name", topicDO.getName());*/
    	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/categoryproducts.flt",new StringWriter());
		return str;
	}

	/**
	 * 西客商城商城首页-自定义编辑区
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String mallDefinedHtml() {
		List<DefinedElement> lst = new ArrayList<DefinedElement>();
		try {
			lst = (List<DefinedElement>) queryPageTempletElementInfo(
					PageTempletConstant.PG_XGSC,PageTempletConstant.XGSC_DEFINED);
		} catch (Exception e) {
			logger.error("西客商城商城-自定义编辑区的查询报错", e);
			e.printStackTrace();
		}
		if(lst==null || lst.size()==0){
			return "";
		}
		DefinedElement cmsDefinedElementDO = lst.get(0);
	    return cmsDefinedElementDO.getContent();
	}


}
