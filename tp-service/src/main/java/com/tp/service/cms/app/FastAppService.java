package com.tp.service.cms.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.common.util.ImageDownUtil;
import com.tp.common.util.ImageUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.cms.AdvertTypeAPPEnum;
import com.tp.common.vo.cms.ElementEnum;
import com.tp.common.vo.cms.FastAppTempletConstant;
import com.tp.common.vo.mmp.TopicConstant;
import com.tp.dao.cms.PositionDao;
import com.tp.dao.cms.TempleDao;
import com.tp.dto.cms.app.AppAdvertiseInfoDTO;
import com.tp.dto.cms.app.AppIndexAdvertReturnData;
import com.tp.dto.cms.app.AppSingleAllInfoDTO;
import com.tp.dto.cms.app.AppSingleCotAllInfoDTO;
import com.tp.dto.cms.app.AppSingleInfoDTO;
import com.tp.dto.cms.app.query.AppTopItemPageQuery;
import com.tp.dto.cms.temple.Products;
import com.tp.dto.mmp.TopicDetailDTO;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.dto.prd.SkuDetailInfoDto;
import com.tp.dto.prd.enums.ItemStatusEnum;
import com.tp.dto.promoter.PromoterTopicItemDTO;
import com.tp.model.bse.ClearanceChannels;
import com.tp.model.bse.DistrictInfo;
import com.tp.model.bse.NationalIcon;
import com.tp.model.cms.ActivityElement;
import com.tp.model.cms.PictureElement;
import com.tp.model.cms.Temple;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.dss.PromoterTopic;
import com.tp.model.mmp.ItemOtherInfo;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicInfo;
import com.tp.model.mmp.TopicItem;
import com.tp.model.mmp.TopicItemDss;
import com.tp.model.prd.ItemDetail;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.bse.IClearanceChannelsService;
import com.tp.service.bse.IDistrictInfoService;
import com.tp.service.bse.INationalIconService;
import com.tp.service.cms.CmsTempletUtil;
import com.tp.service.cms.ISingleBusTemService;
import com.tp.service.cms.app.IFastAppService;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.dss.IPromoterTopicService;
import com.tp.service.mmp.ITopicItemService;
import com.tp.service.mmp.ITopicService;
import com.tp.service.prd.IItemDetailService;
import com.tp.service.prd.IItemSkuService;
import com.tp.util.DateUtil;
import com.tp.util.StringUtil;


@Service
public class FastAppService<T> implements IFastAppService{

	private final static Logger logger = LoggerFactory.getLogger(FastAppService.class);
	
	@Value("${cms_topic_adress}")
	private String cmsTopicAdress;
	
	@Value("${cms_chaimed_adress}")
	private String cmsChaimedAdress;
	
	@Autowired
	TempleDao templeDao;
	
	@Autowired
	PositionDao positionDao;
	
	@Autowired
	private ITopicService topicService;
	@Autowired
	private IItemDetailService itemDetailService;
	
	@Autowired
	private INationalIconService nationalIconService;
	@Autowired
	private IClearanceChannelsService clearanceChannelsService;
	@Autowired
	private IDistrictInfoService districtInfoService;
	@Autowired
	private ITopicItemService topicItemService;
	@Autowired
	private ISingleBusTemService singleBusTemService;	
	@Autowired
	private IPromoterTopicService promoterTopicService;	
	@Autowired
	IPromoterInfoService promoterInfoService;	
	@Autowired
	private IItemSkuService  itemSkuService;	
	
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
		
	private final String DSSRedis = "DSS_REDIS_";
	
	
	/**
	 * APP海淘首页-轮播图
	 */
	@SuppressWarnings("unchecked")
	@Override
	public AppIndexAdvertReturnData carouseAdvert() {
		AppIndexAdvertReturnData returnDate = new AppIndexAdvertReturnData();
		
		List<PictureElement> lst = new ArrayList<PictureElement>();
		try {
			lst = (List<PictureElement>) queryPageTempletElementInfo(FastAppTempletConstant.FAST_APP,FastAppTempletConstant.FAST_APP_CAROUSE_ADVERT);
		} catch (Exception e) {
			logger.error("APP海淘首页-轮播图查询报错", e);
			e.printStackTrace();
		}
		if(lst==null || lst.size()==0){
			return null;
		}

		//封装数据
    	List<AppAdvertiseInfoDTO<Object>> appAdvertiseInfoDTO = new ArrayList<AppAdvertiseInfoDTO<Object>>();
    	for(int i=0;i<lst.size();i++){
    		PictureElement ssmode = lst.get(i);
    		AppAdvertiseInfoDTO<Object> csmode = new AppAdvertiseInfoDTO<Object>();
    		csmode.setImageurl(Constant.IMAGE_URL_TYPE.cmsimg.url+ssmode.getPicSrc());
    		//链接
    		csmode.setLinkurl(ssmode.getLink());
    		//sku
    		csmode.setSku(ssmode.getSku());
    		//活动类型
    		csmode.setType(ssmode.getActtype());

    		if(AdvertTypeAPPEnum.ACT_ITEMSKU.getValue().equals(ssmode.getActtype())){
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setSku(ssmode.getSku());
    			appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
    			csmode.setInfo(appSingleInfoDTO);
    		}else if(AdvertTypeAPPEnum.ACTIVITYID.getValue().equals(ssmode.getActtype())){
    			if(ssmode.getActivityid() != null){
    				AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
        			appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
        			csmode.setInfo(appSingleInfoDTO);
    			}
    		}else{
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setText(ssmode.getLink());
    			csmode.setInfo(appSingleInfoDTO);
    		}

    		appAdvertiseInfoDTO.add(csmode);
    	}

    	returnDate.setCount(lst.size());
    	returnDate.setUrls(appAdvertiseInfoDTO);
		
		return returnDate ;
	}

	/**
	 * APP海淘首页-功能标签
	 */
	@SuppressWarnings("unchecked")
	@Override
	public AppIndexAdvertReturnData funLab() {
		AppIndexAdvertReturnData returnDate = new AppIndexAdvertReturnData();
		
		List<PictureElement> lst = new ArrayList<PictureElement>();
		try {
			lst = (List<PictureElement>) queryPageTempletElementInfo(FastAppTempletConstant.FAST_APP,FastAppTempletConstant.FAST_APP_FUNLAB);
		} catch (Exception e) {
			logger.error("APP海淘首页-功能标签查询报错", e);
			e.printStackTrace();
		}
		if(lst==null || lst.size()==0){
			return null;
		}
		Collections.sort(lst, new Comparator<PictureElement>() {
			@Override
			public int compare(PictureElement o1, PictureElement o2) {
				if(StringUtils.isEmpty(o1.getAttr()) || StringUtils.isEmpty(o2.getAttr())) {
					return 0;
				}
				return o1.getAttr().compareTo(o2.getAttr());
			}
		});
		
		//封装数据
    	List<AppAdvertiseInfoDTO<Object>> appAdvertiseInfoDTO = new ArrayList<AppAdvertiseInfoDTO<Object>>();
    	for(int i=0;i<lst.size();i++){
    		PictureElement ssmode = lst.get(i);
    		AppAdvertiseInfoDTO<Object> csmode = new AppAdvertiseInfoDTO<Object>();
    		csmode.setImageurl(Constant.IMAGE_URL_TYPE.cmsimg.url+ssmode.getPicSrc());
    		//链接
    		csmode.setLinkurl(ssmode.getLink());
    		//sku
    		csmode.setSku(ssmode.getSku());
    		//活动类型
    		csmode.setType(ssmode.getActtype());
    		csmode.setName(ssmode.getName());
    		
    		if(AdvertTypeAPPEnum.ACT_ITEMSKU.getValue().equals(ssmode.getActtype())){
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setSku(ssmode.getSku());
    			appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
    			csmode.setInfo(appSingleInfoDTO);
    		}else if(AdvertTypeAPPEnum.ACTIVITYID.getValue().equals(ssmode.getActtype())){
    			if(ssmode.getActivityid() != null){
    				AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
        			appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
        			csmode.setInfo(appSingleInfoDTO);
    			}
    		}else{
    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    			appSingleInfoDTO.setText(ssmode.getLink());
    			csmode.setInfo(appSingleInfoDTO);
    		}
    		
    		appAdvertiseInfoDTO.add(csmode);
    	}
    	
    	returnDate.setCount(lst.size());
    	
    	returnDate.setTables(appAdvertiseInfoDTO);
		
		return returnDate ;
	}

	@Override
	public AppIndexAdvertReturnData funLabForWorld() {
		return null;
	}

	/**
	 * APP海淘首页-单品团
	 */
	@Override
	public PageInfo<AppSingleCotAllInfoDTO> singleTemplet(int pagestart,int pagesize) {
		List<AppSingleCotAllInfoDTO> retLst = new ArrayList<AppSingleCotAllInfoDTO>();
		
		List<ActivityElement> lst = new ArrayList<ActivityElement>();
		try {
			lst = (List<ActivityElement>) queryPageTempletElementPage(FastAppTempletConstant.FAST_APP,FastAppTempletConstant.FAST_APP_SINGLETEMP, pagestart,pagesize);
		} catch (Exception e) {
			logger.error("APP海淘首页-单品团查询报错", e);
			e.printStackTrace();
		}
		if(lst==null || lst.size()==0){
			return null;
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
			logger.error("APP海淘首页-单品团，从促销那边取数据报错", e);
			e.printStackTrace();
		}
		if(topicDetailDTO != null && topicDetailDTO.size()>0 ){
			for(int i=0; i<lst.size(); i++){//排序
				ActivityElement cmsActivityElementDO = lst.get(i);
				
				for(TopicDetailDTO dtomd:topicDetailDTO){
					AppSingleCotAllInfoDTO appSingleCotAllInfoDTO = new AppSingleCotAllInfoDTO();
	    			Topic topic = dtomd.getTopic();
	    			List<TopicItem> promotionItemList = dtomd.getPromotionItemList();
	    			
					if(cmsActivityElementDO.getActivityId().longValue() == topic.getId().longValue() 
							&& TopicStatus.PASSED.ordinal() == topic.getStatus().intValue() ){//找到值，即两个活动id一致情况下
						TopicItem promotionItemDO = new TopicItem();
			       		if(promotionItemList != null&&promotionItemList.size()>0){
			       			promotionItemDO = promotionItemList.get(0);
			       			
			       			//优惠后的价格
			    			appSingleCotAllInfoDTO.setPrice(promotionItemDO.getTopicPrice());
			    			//原价
			    			appSingleCotAllInfoDTO.setOldprice(promotionItemDO.getSalePrice());
			    			//商品名称
			    			appSingleCotAllInfoDTO.setGoodsName(promotionItemDO.getName());
			    			//商品id(productid)
			    			appSingleCotAllInfoDTO.setProductid(promotionItemDO.getId());
			    			//sku编码
			    			appSingleCotAllInfoDTO.setSku(promotionItemDO.getSku());
			    			
			    	       	appSingleCotAllInfoDTO.setDiscount(
			    	       			CmsTempletUtil.getPriceDiscount(promotionItemDO.getTopicPrice(), promotionItemDO.getSalePrice()));
			       		}
		    			//卖点
		        		appSingleCotAllInfoDTO.setTopicPoint(topic.getTopicPoint());
		    			
		    			//活动详情detail
		    			appSingleCotAllInfoDTO.setDetail(topic.getIntroMobile());
		    			
		    			//详情图片(取得是活动图片)
		    			List<String> lstStr = new ArrayList<String>();
		    			lstStr.add(topic.getImage());
		    			//lstStr.add(topic.getMobileImage());
		    			appSingleCotAllInfoDTO.setImageurl(lstStr);
		    			
		    			//专场id(specialid)
		    			appSingleCotAllInfoDTO.setSpecialid(topic.getId());
		    			//专场名称
		        		appSingleCotAllInfoDTO.setSpecialName(topic.getName());
		    			
		    			//活动开始时间
		    			if(topic.getStartTime() != null){
		    				appSingleCotAllInfoDTO.setStartTime(topic.getStartTime().getTime());
		    			}
		    			//活动结束时间
		    			if(topic.getEndTime() != null){
		    				appSingleCotAllInfoDTO.setEndTime(topic.getEndTime().getTime());
		    			}
		    			//活动状态
		    			appSingleCotAllInfoDTO.setStatus(topic.getStatus());
		    			retLst.add(appSingleCotAllInfoDTO);
					}
	    			
	    		}
			}
			
       	}
		
		PageInfo<AppSingleCotAllInfoDTO>  page = new PageInfo<AppSingleCotAllInfoDTO>();
    	page.setRecords(retLst.size());//此处暂时不需要总页数,只返回此次的总条数
    	page.setRows(retLst);
    	page.setPage(pagestart);
    	page.setSize(pagesize);
		return page;
	}

	@Override
	public PageInfo<AppSingleAllInfoDTO> htTopicAllList(Long promoterid, int pagestart,int pagesize,String ...templates) {

		
		List<ActivityElement> lst = new ArrayList<ActivityElement>();
		try {
			List<String> templeCodes = new ArrayList<>();
			if(promoterid == null) {
				templeCodes.add(FastAppTempletConstant.FAST_APP_SINGLETEMP);
				templeCodes.add(FastAppTempletConstant.FAST_APP_TOPICLIST);
				lst = (List<ActivityElement>) queryActivityElementsByTempletCodes(null, FastAppTempletConstant.FAST_APP,templeCodes, pagestart,pagesize);
			}
			else {
				templeCodes.add(FastAppTempletConstant.FAST_APP_DSS_TOPICLIST);
				lst = (List<ActivityElement>) queryActivityElementsByTempletCodes(promoterid, FastAppTempletConstant.FAST_APP,templeCodes, pagestart,pagesize);
			}
		} catch (Exception e) {
			logger.error("APP海淘首页-专场列表查询报错", e);
			e.printStackTrace();
		}
		if(lst==null || lst.size()==0){
			return null;
		}
		List<AppSingleAllInfoDTO> retLst = new ArrayList<AppSingleAllInfoDTO>();
		List<Long> ldList = new ArrayList<Long>();
		for (ActivityElement cmsActivityElementDO:lst) {
				ldList.add(cmsActivityElementDO.getActivityId());
		}

		//先从促销那边取数据，传入id列表，查到具体bean
		List<TopicDetailDTO> topicDetailDTO = new ArrayList<TopicDetailDTO>();
		try {
			topicDetailDTO = topicService.getTopicDetailByIdsForCms(promoterid, ldList);
		} catch (Exception e) {
			logger.error("APP海淘首页-专场列表，从促销那边取数据报错", e);
			e.printStackTrace();
		}

		if(topicDetailDTO != null && topicDetailDTO.size()>0 ){

			for(int i=0,j=lst.size();i<j;i++){//排序

				for(TopicDetailDTO dtomd:topicDetailDTO){
					ActivityElement cmsActivityElementDO = lst.get(i);


					if(dtomd.getTopic().getType().intValue() == TopicConstant.TOPIC_TYPE_SINGLE){
						Topic topic = dtomd.getTopic();
						if(cmsActivityElementDO.getActivityId().longValue() == topic.getId().longValue()
								&& TopicStatus.PASSED.ordinal() == topic.getStatus().intValue() ){//找到值，即两个活动id一致情况下


							AppSingleAllInfoDTO appSingleCotAllInfoDTO = new AppSingleAllInfoDTO();
							appSingleCotAllInfoDTO.setSingle(true);

			    			List<TopicItem> promotionItemList = dtomd.getPromotionItemList();

							TopicItem promotionItemDO = new TopicItem();
				       		if(promotionItemList != null&&promotionItemList.size()>0){
				       			ItemDetail itemQuery = new ItemDetail();
								itemQuery.setItemId(dtomd.getPromotionItemList().get(0).getItemId());
								ItemDetail itemDetail = itemDetailService.queryUniqueByObject(itemQuery);


								ClearanceChannels channel = clearanceChannelsService.queryById(dtomd.getPromotionItemList().get(0).getBondedArea());
								DistrictInfo districtInfo = districtInfoService.queryById(dtomd.getPromotionItemList().get(0).getCountryId());

								NationalIcon iconQuery = new NationalIcon();
								NationalIcon nationalIcon = null;
								if(dtomd.getPromotionItemList().get(0).getCountryId() != null){
									iconQuery.setCountryId(dtomd.getPromotionItemList().get(0).getCountryId().intValue());
									nationalIcon = nationalIconService.queryUniqueByObject(iconQuery);
								}

				       			promotionItemDO = promotionItemList.get(0);

				       			if(nationalIcon != null)
				       				appSingleCotAllInfoDTO.setCountryImageUrl(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.basedata.url,nationalIcon.getPicPath()));

				       			appSingleCotAllInfoDTO.setCountryName(districtInfo.getName());

				       			if(channel != null){
				       				appSingleCotAllInfoDTO.setChannelName(channel.getName());
				       			}

				       			//优惠后的价格
				    			appSingleCotAllInfoDTO.setPrice(promotionItemDO.getTopicPrice());
				    			//原价
				    			appSingleCotAllInfoDTO.setOldprice(promotionItemDO.getSalePrice());
				    			//商品名称
				    			appSingleCotAllInfoDTO.setGoodsName(promotionItemDO.getName());
				    			//商品id(productid)
				    			appSingleCotAllInfoDTO.setProductid(promotionItemDO.getId());
				    			//sku编码
				    			appSingleCotAllInfoDTO.setSku(promotionItemDO.getSku());
				    			//卖点
				        		appSingleCotAllInfoDTO.setTopicPoint(itemDetail.getSubTitle());

				    	       	appSingleCotAllInfoDTO.setDiscount(
				    	       			CmsTempletUtil.getPriceDiscount(promotionItemDO.getTopicPrice(), promotionItemDO.getSalePrice()));
				       		}

			    			//活动详情detail
			    			appSingleCotAllInfoDTO.setDetail(topic.getIntroMobile());

			    			//详情图片(取得是活动图片)
			    			List<String> lstStr = new ArrayList<String>();
			    			lstStr.add(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.cmsimg.url,topic.getImage()));
			    			//lstStr.add(topic.getMobileImage());
			    			appSingleCotAllInfoDTO.setImageurl(lstStr);

			    			//appSingleCotAllInfoDTO.setMobileImage(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.cmsimg.url,topic.getMobileImage()));
			    			appSingleCotAllInfoDTO.setMobileImage(topic.getImageMobile());

			    			//专场id(specialid)
			    			appSingleCotAllInfoDTO.setSpecialid(topic.getId());
			    			//专场名称
			        		appSingleCotAllInfoDTO.setSpecialName(topic.getName());

			    			//活动开始时间
			    			if(topic.getStartTime() != null){
			    				appSingleCotAllInfoDTO.setStartTime(topic.getStartTime().getTime());
			    			}
			    			//活动结束时间
			    			if(topic.getEndTime() != null){
			    				appSingleCotAllInfoDTO.setEndTime(topic.getEndTime().getTime());
			    			}
			    			//活动状态
			    			appSingleCotAllInfoDTO.setStatus(topic.getStatus());
			    			retLst.add(appSingleCotAllInfoDTO);
						}

		    		}
					if(dtomd.getTopic().getType().intValue() == TopicConstant.TOPIC_TYPE_TOPIC
							|| dtomd.getTopic().getType().intValue() == TopicConstant.TOPIC_TYPE_BRAND ){
						AppSingleAllInfoDTO appSingleInfoDTO = new AppSingleAllInfoDTO();
						appSingleInfoDTO.setSingle(false);

						if( promoterid != null){
							if(dtomd.getTopic().getType().intValue() == TopicConstant.TOPIC_TYPE_TOPIC  ){
								appSingleInfoDTO.setShopShowMode(TopicConstant.SHOPSHOWMODE_PRODUCT);
							}else if( dtomd.getTopic().getType().intValue() == TopicConstant.TOPIC_TYPE_BRAND ){
								if( cmsActivityElementDO.getShowTopic().equals(TopicConstant.SHOWTOPIC_HIDE) ){
									appSingleInfoDTO.setShopShowMode(TopicConstant.SHOPSHOWMODE_PRODUCT);
								}else if(  cmsActivityElementDO.getShowTopic().equals(TopicConstant.SHOWTOPIC_SHOW) ){
									appSingleInfoDTO.setShopShowMode(TopicConstant.SHOPSHOWMODE_PRODUCT_TOPIC);
								}
							}
						}

		    			Topic topic = dtomd.getTopic();
		    			if(cmsActivityElementDO.getActivityId().longValue() == topic.getId().longValue()){//找到值，即两个活动id一致情况下
		    				//折扣：需呀自己算出折扣吗
			    			appSingleInfoDTO.setDiscount(topic.getDiscount());

			    			//获取活动的结束时间
			       	    	Date endTime = topic.getEndTime();
			       	    	//此处需要把活动结束时间返回给前端，然后前端要实时计算，每秒种的刷时间
			       	    	appSingleInfoDTO.setSurplusTime(DateUtil.getSurplusDate(endTime));

			    			//活动图片
			       	    	List<String> imageUrl = new ArrayList<String>();
			       	    	imageUrl.add(topic.getImageMobile());
			    			appSingleInfoDTO.setImageurl(imageUrl);//旧
			    			appSingleInfoDTO.setMobileImage(topic.getMobileImage());//新

			    			//专场id(specialid)
			    			appSingleInfoDTO.setSpecialid(topic.getId());

			    			//专场名称
			    			appSingleInfoDTO.setSpecialName(topic.getName());

			    			//专场状态
			    			appSingleInfoDTO.setStatus(topic.getStatus());

			    			//活动开始时间
			    			if(topic.getStartTime() != null){
			    				appSingleInfoDTO.setStartTime(topic.getStartTime().getTime());
			    			}
			    			//活动结束时间
			    			if(topic.getEndTime() != null){
			    				appSingleInfoDTO.setEndTime(topic.getEndTime().getTime());
			    			}

//			    			Map<String, Object> params = new HashMap<String, Object>();
//			    			params.put("topic_id", topic.getId());
//			    			params.put("deletion", 0);
//			    			params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), "10");
//			    			List<TopicItem> topicItemList = topicItemService.queryByParam(params);
			    			// modify by zhs
			    		   // Long promoterid, Long topicid, Integer deletion, Integer lockstatus, Integer start, Integer pagesize
			    			List<TopicItem> topicItemList = topicItemService.getTopicItemFileterByDSS(promoterid, topic.getId(),
			    																																	DeletionStatus.NORMAL.ordinal(), null, 0, 10);
			    			if(CollectionUtils.isNotEmpty(topicItemList)) {
			    				for(TopicItem item : topicItemList) {
			    					item.setTopicImage(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.item.url,item.getTopicImage()));
			    				}
			    			}
			    			AppTopItemPageQuery query = new AppTopItemPageQuery();

			    			try {
			    				query.setSpecialid(Long.valueOf(topic.getId()));// 专场id
			    				query.setCurpage(1);
			    				query.setPageSize(10);
			    				query.setIsascending("0"); // 按默认顺序显示
			    				query.setPromoterId(promoterid);
								com.tp.dto.cms.temple.Topic t = singleBusTemService.loadTopiInfocHtmlApp( query);
								appSingleInfoDTO.setTopicItemList(t.getProductsList());
							} catch (Exception e) {
								e.printStackTrace();
							}

			    			retLst.add(appSingleInfoDTO);
		    			}
					}
	    		}
			}

       	}
		
		PageInfo<AppSingleAllInfoDTO>  page = new PageInfo<AppSingleAllInfoDTO>();
    	page.setRecords(retLst.size());//此处暂时不需要总页数,只返回此次的总条数
    	page.setRows(retLst);
    	page.setPage(pagestart);
    	page.setSize(pagesize);
		return page;
	}

	@Override
	public List<AppSingleAllInfoDTO> getAppSingleAllInfoDTOs(Long promoterid, List<ActivityElement> lst,String...arg) {
		return null;
	}


	private List<TopicInfo> selectTopicsDss(Long promoterid, Integer topicType, Long topicid){
		List<String> list = new ArrayList<String>();
		list.add(FastAppTempletConstant.FAST_APP_DSS_TOPICLIST);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("templecodelist", list);
		params.put("elementype", ElementEnum.ACTIVITY.getValue());
		params.put("promoterid", promoterid);
		params.put("topictype", topicType);
		if (topicid != null) {
			params.put("topicid", topicid);
		}
		return promoterTopicService.selectTopics(params);
	}
	

	private List<Long> GetNoTopics(Long promoterid){				
		// 主题
		Map<Long, Integer> mapTopicId = new HashMap<Long, Integer>();
		PromoterTopic pTopic = new PromoterTopic();
		pTopic.setPromoterId(promoterid);
		pTopic.setPageSize(null);
		pTopic.setTopicType(TopicConstant.TOPIC_TYPE_TOPIC);
		List<TopicInfo> topiclist = selectTopicsDss( promoterid, TopicConstant.TOPIC_TYPE_TOPIC, null);				
		for(TopicInfo tmp : topiclist){
			pTopic.getListTopic().add(tmp.getId());
		}
		if( pTopic.getListTopic().isEmpty() ){
			pTopic.getListTopic().add(-1L);
		}
		List<PromoterTopicItemDTO> listTopicItems = promoterTopicService.selectTopicItems(pTopic);	
				
		for(TopicInfo topic : topiclist ){
			mapTopicId.put(topic.getId(), DssConstant.PROMOTERTOPIC_STATUS.OFFSHELF.code);
		}		
		for(PromoterTopicItemDTO dto :  listTopicItems){
			if(dto.getOnShelves().equals( DssConstant.PROMOTERTOPIC_STATUS.ONSHELF.code)){
				mapTopicId.put( dto.getTopicId(), DssConstant.PROMOTERTOPIC_STATUS.ONSHELF.code);
			}
		}		
		List<Long> noList = new ArrayList<Long>();
		for (Map.Entry<Long, Integer> entry : mapTopicId.entrySet()) {  
				if( entry.getValue().equals( DssConstant.PROMOTERTOPIC_STATUS.OFFSHELF.code ) ){
					noList.add( entry.getKey() );
				}		
		}		
		
		
		// 品牌
		mapTopicId.clear();
		pTopic.getListTopic().clear();
		pTopic.setListTopic( new ArrayList<Long>());		
		pTopic.setTopicType(TopicConstant.TOPIC_TYPE_BRAND);
		List<TopicInfo> brandlist = selectTopicsDss( promoterid, TopicConstant.TOPIC_TYPE_BRAND, null);				
		for(TopicInfo tmp : brandlist){
			if( tmp.getOnShelves().equals(DssConstant.PROMOTERTOPIC_STATUS.OFFSHELF.code) ){
				pTopic.getListTopic().add(tmp.getId());	
			}
		}		
		if(pTopic.getListTopic().isEmpty() ){
			pTopic.getListTopic().add(-1L);
		}
		List<PromoterTopicItemDTO> listBrandsItems = promoterTopicService.selectTopicItems(pTopic);	
		for(Long id : pTopic.getListTopic() ){
			mapTopicId.put(id, DssConstant.PROMOTERTOPIC_STATUS.OFFSHELF.code);
		}		
		for(PromoterTopicItemDTO dto :  listBrandsItems){
			if(dto.getOnShelves().equals( DssConstant.PROMOTERTOPIC_STATUS.ONSHELF.code)){
				mapTopicId.put( dto.getTopicId(), DssConstant.PROMOTERTOPIC_STATUS.ONSHELF.code);
			}
		}		
		for (Map.Entry<Long, Integer> entry : mapTopicId.entrySet()) {  
				if( entry.getValue().equals( DssConstant.PROMOTERTOPIC_STATUS.OFFSHELF.code ) ){
					noList.add( entry.getKey() );
				}		
		}		
		
		return noList;
	}
	
	
	
	@Override
	public PageInfo<AppSingleAllInfoDTO> htTopicAllListForDss(Long promoterid, int pagestart,int pagesize) {
//		logger.info("[ $$$$$$$$$$^^^^^^htTopicAllListForDss start time ]" +  System.currentTimeMillis()   );
			

		if(promoterid ==null){
			return null;
		}		
		
//		if(pagestart == 2){
//			System.out.println("11111");
//		}
		
//			List<AppSingleAllInfoDTO> list = 	(List<AppSingleAllInfoDTO>)jedisCacheUtil.getCache(DSSRedis + promoterid);
//			if(list !=null && !list.isEmpty()){
//				
//				Integer start = (pagestart-1)*10;
//				if(start<0){
//					start = 0;
//				}		
//				Integer end = start + 10;
//								
//				PageInfo<AppSingleAllInfoDTO>  page2 = new PageInfo<AppSingleAllInfoDTO>();		
//
//				if(start >=list.size() ){
//					page2.setRecords( 0 );//此处暂时不需要总页数,只返回此次的总条数
//					page2.setRows(new ArrayList<AppSingleAllInfoDTO>() );	    	
//					page2.setPage(pagestart);
//					page2.setSize(pagesize);
//					return page2;								
//				}else{
//					if( list.size() < end ){
//						end = list.size();
//					}
//					List<AppSingleAllInfoDTO> sublist = list.subList( start, end );										
//					page2.setRecords(sublist.size());//此处暂时不需要总页数,只返回此次的总条数
//					page2.setRows(sublist);	    	
//					page2.setPage(pagestart);
//					page2.setSize(pagesize);
//					return page2;								
//				}
//			}


		
		
		// 获取无数据的Topics
		List<Long> noList = GetNoTopics(promoterid);
						
		List<AppSingleAllInfoDTO> retLst = new ArrayList<AppSingleAllInfoDTO>();
		
		List<ActivityElement> lst = new ArrayList<ActivityElement>();
		try {
			List<String> templeCodes = new ArrayList<>();
			templeCodes.add(FastAppTempletConstant.FAST_APP_DSS_TOPICLIST);
			lst = (List<ActivityElement>) queryActivityElementsByTempletCodesForDss(promoterid, FastAppTempletConstant.FAST_APP,templeCodes, noList, 1,pagesize);
		} catch (Exception e) {
			logger.error("APP海淘首页-专场列表查询报错", e);
			e.printStackTrace();
		}
		if(lst==null || lst.size()==0){
			return null;
		}
		
		List<Long> ldList = new ArrayList<Long>();
		for (ActivityElement cmsActivityElementDO:lst) {	
				ldList.add(cmsActivityElementDO.getActivityId());				
		}
				
				
		//先从促销那边取数据，传入id列表，查到具体bean
		List<TopicDetailDTO> topicDetailDTO = new ArrayList<TopicDetailDTO>();
		try {
			topicDetailDTO = topicService.getTopicDetailByIdsForCms(promoterid, ldList);
		} catch (Exception e) {
			logger.error("APP海淘首页-专场列表，从促销那边取数据报错", e);
			e.printStackTrace();
		}
		
//		logger.info("[ $$$$$$$$$$^^^^^^brandBeforeItem start time ]" +  System.currentTimeMillis()   );
		brandBeforeItem( retLst, promoterid, topicDetailDTO, lst);
//		logger.info("[ $$$$$$$$$$^^^^^^brandBeforeItem end time ]" +  System.currentTimeMillis()   );
			
//		PageInfo<AppSingleAllInfoDTO>  page = new PageInfo<AppSingleAllInfoDTO>();		
		
		

//			jedisCacheUtil.setCache(DSSRedis + promoterid, retLst);
				
			Integer start = (pagestart-1)*10;
			if(start<0){
				start = 0;
			}		
			Integer end = start + 10;
							
			PageInfo<AppSingleAllInfoDTO>  page = new PageInfo<AppSingleAllInfoDTO>();		
			if(start >=retLst.size() ){
		    	page.setRecords( 0 );//此处暂时不需要总页数,只返回此次的总条数
		    	page.setRows(new ArrayList<AppSingleAllInfoDTO>() );	    	
		    	page.setPage(pagestart);
		    	page.setSize(pagesize);
				return page;								
			}else{
				if( retLst.size() < end ){
					end = retLst.size();					
				}
			}
			List<AppSingleAllInfoDTO> sublist = retLst.subList( start, end );										
			
			
//			logger.info("[ $$$$$$$$$$^^^^^^AppSingleAllInfoDTO dto : sublist start time ]" +  System.currentTimeMillis()   );

			for(AppSingleAllInfoDTO dto : sublist){				
				if( false == dto.isSingle() )
					continue;
				
       			ItemDetail itemQuery = new ItemDetail();
				itemQuery.setItemId( dto.getItemid() );
				ItemDetail itemDetail = itemDetailService.queryUniqueByObject(itemQuery);
								
				ClearanceChannels channel = clearanceChannelsService.queryById( dto.getBondedArea() );
				DistrictInfo districtInfo = districtInfoService.queryById(dto.getCountryId() );
       			
				NationalIcon iconQuery = new NationalIcon();
				NationalIcon nationalIcon = null;
				if(dto.getCountryId() != null){
					iconQuery.setCountryId(dto.getCountryId().intValue());
					nationalIcon = nationalIconService.queryUniqueByObject(iconQuery);
				}       				       							
       			if(nationalIcon != null)
       				dto.setCountryImageUrl(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.basedata.url,nationalIcon.getPicPath()));
       			
       			if(districtInfo != null){
           			dto.setCountryName(districtInfo.getName());       				
       			}
       		
       			if(channel != null){
       				dto.setChannelName(channel.getName());
       			}	       			
    			//卖点
       			dto.setTopicPoint(itemDetail.getSubTitle());    			
       			
				Double commision = promoterInfoService.getCurrentCommision(promoterid, dto.getSku(), dto.getPrice() );
				dto.setCommission( commision );

			}
//			logger.info("[ $$$$$$$$$$^^^^^^AppSingleAllInfoDTO dto : sublist end time ]" +  System.currentTimeMillis()   );
//
//			logger.info("[ $$$$$$$$$$^^^^^^htTopicAllListForDss end time ]" +  System.currentTimeMillis()   );
			
		    page.setRecords(sublist.size());//此处暂时不需要总页数,只返回此次的总条数
		    page.setRows(sublist);	    	
	    	page.setPage(pagestart);
	    	page.setSize(pagesize);
			return page;

//		}
	}

	
	
	public void getBrandProducts(PromoterInfo promoter, List<TopicItem> itemList, List<AppSingleAllInfoDTO> brandList) {

		List<String> skuList = new  ArrayList<String>();
		for(TopicItem item : itemList ){
			if( item.getSku() !=null){
				skuList.add( item.getSku());
			}
		}
		List<SkuDetailInfoDto> skudtoList = itemSkuService.selectCommisionRateByListSkus(skuList);
		for(TopicItem item : itemList ){
			for(SkuDetailInfoDto dto : skudtoList){
				if(item.getSku().equals( dto.getSku() )){
					if(dto.getCommisionRate() != null)
						item.setCommisionRate( dto.getCommisionRate() );
					else
						item.setCommisionRate(0.0d);
					break;
				}
			}
		}					
		
		for(AppSingleAllInfoDTO topicInfo :  brandList){
			List<Products> productsList = new ArrayList<Products>();
			for(TopicItem item : itemList){
				if( !topicInfo.isSingle() &&  topicInfo.getSpecialid().equals(item.getTopicId()) ){					
		    		Products productNormal = new Products();
		    		productNormal.setTopicid(String.valueOf( topicInfo.getSpecialid() ));
		        	productNormal.setSrclink("/detail");		        	
	        		productNormal.setImgsrc( ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item, item.getTopicImage()) );	        

	            	//图片链接:注意此处需要改尺寸
	            	productNormal.setNamelink( splitJoinLinkAdress(topicInfo.getSpecialid(),item.getSku()));
	            	//品牌名称
	            	productNormal.setName(item.getName());	            	
	            	//字符，特定设置
	            	productNormal.setMoney("&yen;");
	            	//活动价格
	            	productNormal.setNowValue(item.getTopicPrice());
	            	//原价格
	            	productNormal.setLastValue(item.getSalePrice());
	            	//折扣价格
	            	productNormal.setDiscountPrice(getPriceDiscount(item.getTopicPrice(), item.getSalePrice()));	            	
	            	//已卖出数量
	            	productNormal.setNowHoard(item.getSaledAmount()+"人已海淘");
	            	//品牌链接
	            	productNormal.setSeelink(splitJoinLinkAdress(topicInfo.getSpecialid(),item.getSku()));
	            	//开始时间
	            	productNormal.setStartDate(topicInfo.getStartTimeDate());
	            	//结束时间
	            	productNormal.setEndDate(topicInfo.getEndTimeDate());
	            	//sku
	            	productNormal.setSku(item.getSku());	            	
	            	//开始时间：秒
	            	if(topicInfo.getStartTime() != null){
	            		productNormal.setStartDateSeckend(topicInfo.getStartTimeDate().getTime());
	            	}
	            	//结束时间：秒
	            	if(topicInfo.getEndTime() != null){
	            		productNormal.setEndDateSeckend(topicInfo.getStartTimeDate().getTime());
	            	}
	            		            	
	            	if(topicInfo.getStartTimeDate() != null){
	            		if(new Date().getTime() < topicInfo.getStartTimeDate().getTime() ){
	            			productNormal.setType("noStart");
	            		}
	            	}else{
	            		productNormal.setType("normal");
	            	}
	            	
	            	if(topicInfo.getStatus() != TopicStatus.PASSED.ordinal() && topicInfo.getStatus() != TopicStatus.STOP.ordinal() ){
	            		productNormal.setType("editing");
	            	}	            	
//	            	if(!topicItemInfoResult.isHasStock()){
//	            		productNormal.setType("ruball");//已抢光
//	            	}	            
    				Double commision = promoterInfoService.getCurrentCommision2(promoter,  item.getTopicPrice(), item.getCommisionRate());
    				productNormal.setCommision(commision);    				
	            	productsList.add(productNormal);
	            	if(productsList.size() == 10){
	            		break;		// 取前10个
	            	}	            
				}				
			}			
			topicInfo.setTopicItemList(productsList);				
		}				
	}
	
    /**
     * 建立item的链接地址:商品地址
     * @param activityId 活动id
     * @param skuId	skuid
     * @return
     */
    public String splitJoinLinkAdress(Long activityId,String skuId){
    	String str = activityId+"-"+skuId;
    	return cmsChaimedAdress.replaceAll("parames", str);
    }
    
    
    /**
     * 根据价格计算折扣信息
     * @param priceNow 现价
     * @param priceOld 原价
     * @return
     */
    public String getPriceDiscount(double priceNow, double priceOld){
    	return Math.round(priceNow/priceOld*100)/10.0 == 10 ?"":Math.round(priceNow/priceOld*100)/10.0+"折";
    }


	
	@Override
	public PageInfo<AppSingleAllInfoDTO> htTopicAllListForDss2(Long promoterid,String channelCode, int pagestart,int pagesize) {


		
		if( promoterid == null){
			return null;
		}
		PromoterInfo promoter =  promoterInfoService.queryById(promoterid);
				
		Integer start = (pagestart-1)*10;
		if(start<0){
			start = 0;
		}		
		Integer end = start + 10;

		List<AppSingleAllInfoDTO> retLst = new ArrayList<AppSingleAllInfoDTO>();



		List<Topic> topicList = promoterTopicService.geBrandTopics(promoterid,null);		
		xToBrands(retLst, topicList);
		
		List<TopicItemDss> topicitemList = promoterTopicService.getBrandTopicItems(promoterid,null);	
		topicitemList.removeIf(new Predicate<TopicItemDss>(){
			public boolean test(TopicItemDss topicItemDss) {
				return null!=topicItemDss.getItemStatus() && !ItemStatusEnum.ONLINE.getValue().equals(topicItemDss.getItemStatus()) && -1!=topicItemDss.getItemStatus();
			}
		});
		xtoTopicItems( promoterid,  retLst,  topicitemList);

		List<TopicItemDss> otheritemList = promoterTopicService.getOtherTopicItems(promoterid,null);
		otheritemList.removeIf(new Predicate<TopicItemDss>(){
			public boolean test(TopicItemDss topicItemDss) {
				return null!=topicItemDss.getItemStatus() && !ItemStatusEnum.ONLINE.getValue().equals(topicItemDss.getItemStatus()) && -1!=topicItemDss.getItemStatus();
			}
		});
		xtoTopicItems( promoterid,  retLst,  otheritemList);
		
											
			PageInfo<AppSingleAllInfoDTO>  page = new PageInfo<AppSingleAllInfoDTO>();		
			if(start >=retLst.size() ){
		    	page.setRecords( 0 );//此处暂时不需要总页数,只返回此次的总条数
		    	page.setRows(new ArrayList<AppSingleAllInfoDTO>() );	    	
		    	page.setPage(pagestart);
		    	page.setSize(pagesize);
				return page;								
			}else{
				if( retLst.size() < end ){
					end = retLst.size();					
				}
			}
			List<AppSingleAllInfoDTO> sublist = retLst.subList( start, end );										
			
			List<Long> ids = new ArrayList<Long>();
			List<Long> brandids = new ArrayList<Long>();

			for(AppSingleAllInfoDTO dto : sublist){
				if( dto.isSingle() ){
					ids.add(dto.getItemid());					
				}
				else{
					brandids.add( dto.getSpecialid() );
				}					
			}
			if(!brandids.isEmpty()){
//				List<TopicItem> itemList = topicItemService.getTopicItemByTopicId_Top10(brandids);
				List<TopicItem> itemList = topicItemService.getTopicItemByTopicId(brandids);
				itemList.removeIf(new Predicate<TopicItem>(){
					public boolean test(TopicItem topicItem) {
						return null!=topicItem.getItemStatus() && !ItemStatusEnum.ONLINE.getValue().equals(topicItem.getItemStatus()) && -1!=topicItem.getItemStatus();
					}
				});		
				getBrandProducts(promoter, itemList, sublist);				
			}
			
			// 获取单品信息
			if(!ids.isEmpty()){
				List<ItemOtherInfo> otherinfoList = promoterTopicService.getItemOtherInfo(ids);
				if(otherinfoList!=null && !otherinfoList.isEmpty()){
					for(AppSingleAllInfoDTO dto : sublist){
						for(ItemOtherInfo otherinfo : otherinfoList){
							if( dto.isSingle() && dto.getItemid()!=null){
								if(dto.getItemid().equals(otherinfo.getItemId())){
					    			//卖点
					       			dto.setTopicPoint(otherinfo.getSubTitle());    			
					       			
				       				dto.setChannelName(otherinfo.getClearName());
				       				
				           			dto.setCountryName(otherinfo.getDirctName());       				
				           			dto.setCountryImageUrl(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.basedata.url,otherinfo.getPicPath()));
				           			
				           			if(otherinfo.getCommisionRate() == null){
				           				otherinfo.setCommisionRate(0.0d);
				           			}
				    				Double commision = promoterInfoService.getCurrentCommision2(promoter,  dto.getPrice(), otherinfo.getCommisionRate() );
				    				dto.setCommission( commision );
				           							           			
				           			break;
								}								
							}
						}
					}
				}
			}
			
		    page.setRecords(sublist.size());//此处暂时不需要总页数,只返回此次的总条数
		    page.setRows(sublist);	    	
	    	page.setPage(pagestart);
	    	page.setSize(pagesize);
			return page;
	}

	@Override
	public PageInfo<AppSingleAllInfoDTO> htTopicAllListForDssWithMS(Long promoterid,String channelCode,int pagestart, int pagesize) {
		return null;
	}


	private void xToBrands(List<AppSingleAllInfoDTO> retLst,  List<Topic> topicList){
		for (Topic topic : topicList ) {// 找到值，即两个活动id一致情况下
			AppSingleAllInfoDTO appSingleInfoDTO = new AppSingleAllInfoDTO();
			appSingleInfoDTO.setSingle(false);

			appSingleInfoDTO.setDiscount(topic.getDiscount());
			// 获取活动的结束时间
			Date endTime = topic.getEndTime();
			// 此处需要把活动结束时间返回给前端，然后前端要实时计算，每秒种的刷时间
			appSingleInfoDTO.setSurplusTime(DateUtil.getSurplusDate(endTime));

			// 活动图片
			List<String> imageUrl = new ArrayList<String>();
//			imageUrl.add(topic.getImageMobile());
			imageUrl.add(ImageUtil.getCMSImgFullUrl(topic.getImageMobile()));
			appSingleInfoDTO.setImageurl(imageUrl);// 旧
			appSingleInfoDTO.setMobileImage(ImageUtil.getCMSImgFullUrl(topic.getMobileImage()));// 新
			
			// 专场id(specialid)
			appSingleInfoDTO.setSpecialid(topic.getId());

			// 专场名称
			appSingleInfoDTO.setSpecialName(topic.getName());

			// 专场状态
			appSingleInfoDTO.setStatus(topic.getStatus());

			// 活动开始时间
			if (topic.getStartTime() != null) {
				appSingleInfoDTO.setStartTime(topic.getStartTime().getTime());
				appSingleInfoDTO.setStartTimeDate(topic.getStartTime());
			}
			// 活动结束时间
			if (topic.getEndTime() != null) {
				appSingleInfoDTO.setEndTime(topic.getEndTime().getTime());
				appSingleInfoDTO.setEndTimeDate(topic.getEndTime());
			}
//			AppTopItemPageQuery query = new AppTopItemPageQuery();
//			try {
//				query.setSpecialid(Long.valueOf(topic.getId()));// 专场id
//				query.setCurpage(1);
//				query.setPageSize(10);
//				query.setIsascending("0"); // 按默认顺序显示
//				query.setPromoterId(promoterid);
//				com.tp.dto.cms.temple.Topic t = singleBusTemService.loadTopiInfocHtmlAppForDss(query);
//				appSingleInfoDTO.setTopicItemList(t.getProductsList());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			retLst.add(appSingleInfoDTO);
		}
	}
	
	
	
	private void xtoTopicItems(Long promoterid, List<AppSingleAllInfoDTO> retLst, List<TopicItemDss> topicitemList){
//			Topic topic = dtomd.getTopic();
									
			for(TopicItemDss promotionItemDO : topicitemList){				
				AppSingleAllInfoDTO appSingleCotAllInfoDTO = new AppSingleAllInfoDTO();
				appSingleCotAllInfoDTO.setSingle(true);
								
	       		if(topicitemList != null&&topicitemList.size()>0){
	       				       			
	       			
//	       			ItemDetail itemQuery = new ItemDetail();
//					itemQuery.setItemId(promotionItemDO.getItemId());
	       			appSingleCotAllInfoDTO.setItemid(promotionItemDO.getItemId());
//					ItemDetail itemDetail = itemDetailService.queryUniqueByObject(itemQuery);
									
	       			appSingleCotAllInfoDTO.setBondedArea(promotionItemDO.getBondedArea());
//					ClearanceChannels channel = clearanceChannelsService.queryById(promotionItemDO.getBondedArea());
	       			
	       			appSingleCotAllInfoDTO.setCountryId(promotionItemDO.getCountryId());
//					DistrictInfo districtInfo = districtInfoService.queryById(promotionItemDO.getCountryId());
	       			
					NationalIcon iconQuery = new NationalIcon();
//					NationalIcon nationalIcon = null;
//					if(promotionItemDO.getCountryId() != null){
//						iconQuery.setCountryId(promotionItemDO.getCountryId().intValue());
//						nationalIcon = nationalIconService.queryUniqueByObject(iconQuery);
//					}
	       				       			
					
//	       			if(nationalIcon != null)
//	       				appSingleCotAllInfoDTO.setCountryImageUrl(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.basedata.url,nationalIcon.getPicPath()));
	       			
//	       			appSingleCotAllInfoDTO.setCountryName(districtInfo.getName());
//	       		
//	       			if(channel != null){
//	       				appSingleCotAllInfoDTO.setChannelName(channel.getName());
//	       			}	       			
	       			//优惠后的价格
	    			appSingleCotAllInfoDTO.setPrice(promotionItemDO.getTopicPrice());
	    			//原价
	    			appSingleCotAllInfoDTO.setOldprice(promotionItemDO.getSalePrice());
	    			//商品名称
	    			appSingleCotAllInfoDTO.setGoodsName(promotionItemDO.getName());
	    			//商品id(productid)
	    			appSingleCotAllInfoDTO.setProductid(promotionItemDO.getId());
	    			//sku编码
	    			appSingleCotAllInfoDTO.setSku(promotionItemDO.getSku());
	    			// 商品名称
	    			appSingleCotAllInfoDTO.setSpecialName(promotionItemDO.getName());	    			
	    			//卖点
//	        		appSingleCotAllInfoDTO.setTopicPoint(itemDetail.getSubTitle());
	    			
	    	       	appSingleCotAllInfoDTO.setDiscount(
	    	       			CmsTempletUtil.getPriceDiscount(promotionItemDO.getTopicPrice(), promotionItemDO.getSalePrice()));
	       		}
				
				//活动详情detail
//				appSingleCotAllInfoDTO.setDetail(topic.getIntroMobile());
				appSingleCotAllInfoDTO.setDetail(promotionItemDO.getIntroMobile());
				
				//详情图片(取得是活动图片)
				List<String> lstStr = new ArrayList<String>();
//				lstStr.add(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.cmsimg.url,topic.getImage()));
				lstStr.add(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.item.url,promotionItemDO.getTopicImage() ));
				//lstStr.add(topic.getMobileImage());
				appSingleCotAllInfoDTO.setImageurl(lstStr);
				
				//appSingleCotAllInfoDTO.setMobileImage(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.cmsimg.url,topic.getMobileImage()));
//				appSingleCotAllInfoDTO.setMobileImage(topic.getImageMobile());
				appSingleCotAllInfoDTO.setMobileImage(promotionItemDO.getImageMobile());
				
				//专场id(specialid)
//				appSingleCotAllInfoDTO.setSpecialid(topic.getId());
				appSingleCotAllInfoDTO.setSpecialid(promotionItemDO.getTopicId());
				//专场名称
//	    		appSingleCotAllInfoDTO.setSpecialName(topic.getName());
				
				//活动开始时间
//				if(topic.getStartTime() != null){
//					appSingleCotAllInfoDTO.setStartTime(topic.getStartTime().getTime());
//				}
				if(promotionItemDO.getStartTime() != null){
					appSingleCotAllInfoDTO.setStartTime(promotionItemDO.getStartTime().getTime());
				}
				//活动结束时间
//				if(topic.getEndTime() != null){
//					appSingleCotAllInfoDTO.setEndTime(topic.getEndTime().getTime());
//				}
				if(promotionItemDO.getEndTime() != null){
					appSingleCotAllInfoDTO.setEndTime(promotionItemDO.getEndTime().getTime());
				}
				//活动状态
//				appSingleCotAllInfoDTO.setStatus(topic.getStatus());
				appSingleCotAllInfoDTO.setStatus(promotionItemDO.getTopicStatus());
				
				// 佣金计算						
//				Double commision = promoterInfoService.getCurrentCommision(promoterid, promotionItemDO.getSku(), promotionItemDO.getTopicPrice() );
//				appSingleCotAllInfoDTO.setCommission( commision );								
								
				retLst.add(appSingleCotAllInfoDTO);
		}		
	}
	
	
	private void brandBeforeItem(List<AppSingleAllInfoDTO> retLst, Long promoterid, List<TopicDetailDTO> topicDetailDTO, List<ActivityElement> lst){
		
		// 封闭成品牌
		
//		logger.info("[ $$$$$$$$$$^^^^^^t封闭成品牌 start time ]" +  System.currentTimeMillis()   );

		if(topicDetailDTO != null && topicDetailDTO.size()>0 ){			
			for(int i=0,j=lst.size();i<j;i++){//排序				
				for(TopicDetailDTO dtomd:topicDetailDTO){
					ActivityElement cmsActivityElementDO = lst.get(i);
																					
					if (promoterid != null && dtomd.getTopic().getType().intValue() == TopicConstant.TOPIC_TYPE_BRAND
							&& cmsActivityElementDO.getShowTopic().equals(TopicConstant.SHOWTOPIC_SHOW)) { // 品牌团需要显示

						AppSingleAllInfoDTO appSingleInfoDTO = new AppSingleAllInfoDTO();
						appSingleInfoDTO.setSingle(false);
						Topic topic = dtomd.getTopic();
						if (cmsActivityElementDO.getActivityId().longValue() == topic.getId().longValue()) {// 找到值，即两个活动id一致情况下
							appSingleInfoDTO.setDiscount(topic.getDiscount());
							// 获取活动的结束时间
							Date endTime = topic.getEndTime();
							// 此处需要把活动结束时间返回给前端，然后前端要实时计算，每秒种的刷时间
							appSingleInfoDTO.setSurplusTime(DateUtil.getSurplusDate(endTime));

							// 活动图片
							List<String> imageUrl = new ArrayList<String>();
							imageUrl.add(topic.getImageMobile());
							appSingleInfoDTO.setImageurl(imageUrl);// 旧
							appSingleInfoDTO.setMobileImage(topic.getMobileImage());// 新

							// 专场id(specialid)
							appSingleInfoDTO.setSpecialid(topic.getId());

							// 专场名称
							appSingleInfoDTO.setSpecialName(topic.getName());

							// 专场状态
							appSingleInfoDTO.setStatus(topic.getStatus());

							// 活动开始时间
							if (topic.getStartTime() != null) {
								appSingleInfoDTO.setStartTime(topic.getStartTime().getTime());
							}
							// 活动结束时间
							if (topic.getEndTime() != null) {
								appSingleInfoDTO.setEndTime(topic.getEndTime().getTime());
							}
							AppTopItemPageQuery query = new AppTopItemPageQuery();
							try {
								query.setSpecialid(Long.valueOf(topic.getId()));// 专场id
								query.setCurpage(1);
								query.setPageSize(10);
								query.setIsascending("0"); // 按默认顺序显示
								query.setPromoterId(promoterid);
								com.tp.dto.cms.temple.Topic t = singleBusTemService.loadTopiInfocHtmlAppForDss(query);
								appSingleInfoDTO.setTopicItemList(t.getProductsList());
							} catch (Exception e) {
								e.printStackTrace();
							}
							retLst.add(appSingleInfoDTO);
						}
					}
				}
			}
//			logger.info("[ $$$$$$$$$$^^^^^^t封闭成品牌 end time ]" +  System.currentTimeMillis()   );

			// 单品封装
			
//			logger.info("[ $$$$$$$$$$^^^^^^toXSingleTopic start time ]" +  System.currentTimeMillis()   );

			for(int i=0,j=lst.size();i<j;i++){//排序				
				for(TopicDetailDTO dtomd:topicDetailDTO){
					ActivityElement cmsActivityElementDO = lst.get(i);
															
					if(dtomd.getTopic().getType().intValue() == TopicConstant.TOPIC_TYPE_TOPIC  
							|| dtomd.getTopic().getType().intValue() == TopicConstant.TOPIC_TYPE_BRAND ){
						
						toXSingleTopic(promoterid, retLst, cmsActivityElementDO, dtomd);  // 封装成单品形式	
						
					}
				}
			}
			
//			logger.info("[ $$$$$$$$$$^^^^^^toXSingleTopic end time ]" +  System.currentTimeMillis()   );

       	}		


	}
	
	
	private void itemAndBrand(List<AppSingleAllInfoDTO> retLst, Long promoterid, List<TopicDetailDTO> topicDetailDTO, List<ActivityElement> lst){
		
		if(topicDetailDTO != null && topicDetailDTO.size()>0 ){			
			for(int i=0,j=lst.size();i<j;i++){//排序				
				for(TopicDetailDTO dtomd:topicDetailDTO){
					ActivityElement cmsActivityElementDO = lst.get(i);
															
					if(dtomd.getTopic().getType().intValue() == TopicConstant.TOPIC_TYPE_TOPIC  
							|| dtomd.getTopic().getType().intValue() == TopicConstant.TOPIC_TYPE_BRAND ){
						
						toXSingleTopic(promoterid, retLst, cmsActivityElementDO, dtomd);  // 封闭成单品形式
						
						if( promoterid != null 
								&& dtomd.getTopic().getType().intValue() == TopicConstant.TOPIC_TYPE_BRAND							
								&& cmsActivityElementDO.getShowTopic().equals(TopicConstant.SHOWTOPIC_SHOW) ){		// 品牌团需要显示
							
							AppSingleAllInfoDTO appSingleInfoDTO = new AppSingleAllInfoDTO();
							appSingleInfoDTO.setSingle(false);								
							Topic topic = dtomd.getTopic();
							if(cmsActivityElementDO.getActivityId().longValue() == topic.getId().longValue()){//找到值，即两个活动id一致情况下		    			    						    				
								appSingleInfoDTO.setDiscount(topic.getDiscount());
			    			
								//获取活动的结束时间
								Date endTime = topic.getEndTime();
								//此处需要把活动结束时间返回给前端，然后前端要实时计算，每秒种的刷时间
								appSingleInfoDTO.setSurplusTime(DateUtil.getSurplusDate(endTime));
			    			
								//活动图片
								List<String> imageUrl = new ArrayList<String>();
								imageUrl.add(topic.getImageMobile());
								appSingleInfoDTO.setImageurl(imageUrl);//旧
								appSingleInfoDTO.setMobileImage(topic.getMobileImage());//新
			    			
								//专场id(specialid)
								appSingleInfoDTO.setSpecialid(topic.getId());
			    			
								//专场名称
								appSingleInfoDTO.setSpecialName(topic.getName());
			    			
								//专场状态
								appSingleInfoDTO.setStatus(topic.getStatus());
			    			
								//活动开始时间
								if(topic.getStartTime() != null){
									appSingleInfoDTO.setStartTime(topic.getStartTime().getTime());
								}
								//活动结束时间
								if(topic.getEndTime() != null){
									appSingleInfoDTO.setEndTime(topic.getEndTime().getTime());
								}			    						    			
								AppTopItemPageQuery query = new AppTopItemPageQuery();			    			
								try {
									query.setSpecialid(Long.valueOf(topic.getId()));// 专场id
									query.setCurpage(1);
									query.setPageSize(10);
									query.setIsascending("0"); // 按默认顺序显示
									query.setPromoterId(promoterid);
									com.tp.dto.cms.temple.Topic t = singleBusTemService.loadTopiInfocHtmlAppForDss( query);
									appSingleInfoDTO.setTopicItemList(t.getProductsList());
								} catch (Exception e) {
									e.printStackTrace();
								}			        		
								retLst.add(appSingleInfoDTO);
							}
						}										
					}
				}
			}
       	}		

	}
	
	
	
	private void toXSingleTopic(Long promoterid, List<AppSingleAllInfoDTO> retLst, ActivityElement cmsActivityElementDO, TopicDetailDTO dtomd){

		Topic topic = dtomd.getTopic();
		if(cmsActivityElementDO.getActivityId().longValue() == topic.getId().longValue() 
				&& TopicStatus.PASSED.ordinal() == topic.getStatus().intValue() ){//找到值，即两个活动id一致情况下
						
			List<TopicItem> promotionItemList = dtomd.getPromotionItemList();			
			
			for(TopicItem promotionItemDO : promotionItemList){				
				AppSingleAllInfoDTO appSingleCotAllInfoDTO = new AppSingleAllInfoDTO();
				appSingleCotAllInfoDTO.setSingle(true);
								
	       		if(promotionItemList != null&&promotionItemList.size()>0){
	       				       			
	       			
//	       			ItemDetail itemQuery = new ItemDetail();
//					itemQuery.setItemId(promotionItemDO.getItemId());
	       			appSingleCotAllInfoDTO.setItemid(promotionItemDO.getItemId());
//					ItemDetail itemDetail = itemDetailService.queryUniqueByObject(itemQuery);
									
	       			appSingleCotAllInfoDTO.setBondedArea(promotionItemDO.getBondedArea());
//					ClearanceChannels channel = clearanceChannelsService.queryById(promotionItemDO.getBondedArea());
	       			
	       			appSingleCotAllInfoDTO.setCountryId(promotionItemDO.getCountryId());
//					DistrictInfo districtInfo = districtInfoService.queryById(promotionItemDO.getCountryId());
	       			
					NationalIcon iconQuery = new NationalIcon();
//					NationalIcon nationalIcon = null;
//					if(promotionItemDO.getCountryId() != null){
//						iconQuery.setCountryId(promotionItemDO.getCountryId().intValue());
//						nationalIcon = nationalIconService.queryUniqueByObject(iconQuery);
//					}
	       				       			
					
//	       			if(nationalIcon != null)
//	       				appSingleCotAllInfoDTO.setCountryImageUrl(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.basedata.url,nationalIcon.getPicPath()));
	       			
//	       			appSingleCotAllInfoDTO.setCountryName(districtInfo.getName());
//	       		
//	       			if(channel != null){
//	       				appSingleCotAllInfoDTO.setChannelName(channel.getName());
//	       			}	       			
	       			//优惠后的价格
	    			appSingleCotAllInfoDTO.setPrice(promotionItemDO.getTopicPrice());
	    			//原价
	    			appSingleCotAllInfoDTO.setOldprice(promotionItemDO.getSalePrice());
	    			//商品名称
	    			appSingleCotAllInfoDTO.setGoodsName(promotionItemDO.getName());
	    			//商品id(productid)
	    			appSingleCotAllInfoDTO.setProductid(promotionItemDO.getId());
	    			//sku编码
	    			appSingleCotAllInfoDTO.setSku(promotionItemDO.getSku());
	    			// 商品名称
	    			appSingleCotAllInfoDTO.setSpecialName(promotionItemDO.getName());	    			
	    			//卖点
//	        		appSingleCotAllInfoDTO.setTopicPoint(itemDetail.getSubTitle());
	    			
	    	       	appSingleCotAllInfoDTO.setDiscount(
	    	       			CmsTempletUtil.getPriceDiscount(promotionItemDO.getTopicPrice(), promotionItemDO.getSalePrice()));
	       		}
				
				//活动详情detail
				appSingleCotAllInfoDTO.setDetail(topic.getIntroMobile());
				
				//详情图片(取得是活动图片)
				List<String> lstStr = new ArrayList<String>();
//				lstStr.add(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.cmsimg.url,topic.getImage()));
				lstStr.add(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.item.url,promotionItemDO.getTopicImage() ));
				//lstStr.add(topic.getMobileImage());
				appSingleCotAllInfoDTO.setImageurl(lstStr);
				
				//appSingleCotAllInfoDTO.setMobileImage(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.cmsimg.url,topic.getMobileImage()));
				appSingleCotAllInfoDTO.setMobileImage(topic.getImageMobile());
				
				//专场id(specialid)
				appSingleCotAllInfoDTO.setSpecialid(topic.getId());
				//专场名称
//	    		appSingleCotAllInfoDTO.setSpecialName(topic.getName());
				
				//活动开始时间
				if(topic.getStartTime() != null){
					appSingleCotAllInfoDTO.setStartTime(topic.getStartTime().getTime());
				}
				//活动结束时间
				if(topic.getEndTime() != null){
					appSingleCotAllInfoDTO.setEndTime(topic.getEndTime().getTime());
				}
				//活动状态
				appSingleCotAllInfoDTO.setStatus(topic.getStatus());
				
				// 佣金计算						
//				Double commision = promoterInfoService.getCurrentCommision(promoterid, promotionItemDO.getSku(), promotionItemDO.getTopicPrice() );
//				appSingleCotAllInfoDTO.setCommission( commision );								
								
				retLst.add(appSingleCotAllInfoDTO);
			}			
		}			
}
	
	
	
	
	
	/**
	 * APP海淘首页-专场列表
	 */
	@Override
	public PageInfo<AppSingleInfoDTO> htTopicList(int pagestart,int pagesize) {
		List<AppSingleInfoDTO> retLst = new ArrayList<AppSingleInfoDTO>();
		
		List<ActivityElement> lst = new ArrayList<ActivityElement>();
		try {
			lst = (List<ActivityElement>) queryPageTempletElementPage(FastAppTempletConstant.FAST_APP,FastAppTempletConstant.FAST_APP_TOPICLIST, pagestart,pagesize);
		} catch (Exception e) {
			logger.error("APP海淘首页-专场列表查询报错", e);
			e.printStackTrace();
		}
		if(lst==null || lst.size()==0){
			return null;
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
			logger.error("APP海淘首页-专场列表，从促销那边取数据报错", e);
			e.printStackTrace();
		}
		if(topicDetailDTO != null && topicDetailDTO.size()>0 ){
			
			for(int i=0,j=lst.size();i<j;i++){//排序
				ActivityElement cmsActivityElementDO = lst.get(i);
				
				for(TopicDetailDTO dtomd:topicDetailDTO){
	    			AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
	    			Topic topic = dtomd.getTopic();
	    			
	    			if(cmsActivityElementDO.getActivityId().longValue() == topic.getId().longValue()){//找到值，即两个活动id一致情况下
	    				//折扣：需呀自己算出折扣吗
		    			appSingleInfoDTO.setDiscount(topic.getDiscount());
		    			
		    			//获取活动的结束时间
		       	    	Date endTime = topic.getEndTime();
		       	    	//此处需要把活动结束时间返回给前端，然后前端要实时计算，每秒种的刷时间
		       	    	appSingleInfoDTO.setSurplusTime(DateUtil.getSurplusDate(endTime));
		    			
		    			//活动图片
		    			appSingleInfoDTO.setImageurl(topic.getImageMobile());//旧
		    			appSingleInfoDTO.setMobileImage(topic.getMobileImage());//新
		    			
		    			//专场id(specialid)
		    			appSingleInfoDTO.setSpecialid(topic.getId());
		    			
		    			//专场名称
		    			appSingleInfoDTO.setName(topic.getName());
		    			
		    			//专场状态
		    			appSingleInfoDTO.setStatus(topic.getStatus());
		    			
		    			//活动开始时间
		    			if(topic.getStartTime() != null){
		    				appSingleInfoDTO.setStartTime(topic.getStartTime().getTime());
		    			}
		    			//活动结束时间
		    			if(topic.getEndTime() != null){
		    				appSingleInfoDTO.setEndTime(topic.getEndTime().getTime());
		    			}
		        		
		    			retLst.add(appSingleInfoDTO);
	    			}
	    			
	    		}
			}
    		
       	}
		
		PageInfo<AppSingleInfoDTO>  page = new PageInfo<AppSingleInfoDTO>();
    	page.setRecords(retLst.size());//此处暂时不需要总页数,只返回此次的总条数
    	page.setRows(retLst);
    	page.setPage(pagestart);
    	page.setSize(pagesize);
		return page;
	}

	/**
	 * 调用查询元素信息，参数为页面编码和模块编码
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked" })
	public List<T> queryPageTempletElementInfo(String pageCode, String templetCode) throws Exception {
		logger.info("pageCode:{}, templetCode:{}", pageCode, templetCode);
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
	 * 调用查询元素信息，参数为页面编码和模块编码，分页查询
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private List<ActivityElement> queryPageTempletElementPage(String pageCode, String templetCode,int pagestart,int pagesize) throws Exception {
		
		//通过模板编码获取模板id，注意：模板编码且启用的模板是唯一的，这需要后台管理去控制
		Temple cmsTempleDO = new Temple();
		cmsTempleDO.setTempleCode(templetCode);
		cmsTempleDO.setStatus(0);
		cmsTempleDO.setStartPage(pagestart);
		cmsTempleDO.setPageSize(pagesize);
		
		List<Temple> cmsPositionDOList = templeDao.queryByObject(cmsTempleDO);
		
		if(cmsPositionDOList == null || cmsPositionDOList.size()==0){
			return null;
		}
		
		
		Set<Long> ids = new HashSet<>();
		Set<Integer> elementTypes = new HashSet<>();
		for(Temple t : cmsPositionDOList){
			elementTypes.add(t.getElementType());
			if(ElementEnum.ACTIVITY.getValue() == t.getElementType()){
				ids.add(t.getId());
			}
		}
		List<ActivityElement> result = new ArrayList<ActivityElement>();
		for(Integer elementType : elementTypes){
			//暂时只有活动是分页的
			if(ElementEnum.ACTIVITY.getValue() == elementType){
				Map<String, Object> params = new HashMap<>();
				params.put("start", cmsTempleDO.getStart());
				params.put("pageSize", pagesize);
				params.put("list", new ArrayList<Long>(ids));
				result.addAll(positionDao.selectActivityByTempletsPage(params));
			}/*else if(ElementEnum.WRITTEN.getValue() == elementType){
				return (List<T>) positionDao.selectWrittenByTempletId(cmsTempleDO);
			}
			else if(ElementEnum.PICTURE.getValue() == elementType){
				return (List<T>) positionDao.selectPicByTempletId(cmsTempleDO);
			}else if(ElementEnum.DEFINED.getValue() == elementType){
				return (List<T>) positionDao.selectDefinedByTempletId(cmsTempleDO);
			}*/
		}
		
		return result;
	}

	/**
	 * 调用查询元素信息，参数为页面编码和模块编码数组，分页查询
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<ActivityElement> queryActivityElementsByTempletCodes(Long promoterId, String pageCode, List<String> templetCode,int pagestart,int pagesize) throws Exception {
		
		//通过模板编码获取模板id，注意：模板编码且启用的模板是唯一的，这需要后台管理去控制
		Temple cmsTemple = new Temple();
		cmsTemple.setStatus(0);
		cmsTemple.setStartPage(pagestart);
		cmsTemple.setPageSize(pagesize);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", 0);
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " temple_code in( '" + StringUtil.join(templetCode, "'"+Constant.SPLIT_SIGN.COMMA+"'") + "') ");
		
		List<Temple> cmsPositionDOList = templeDao.queryByParam(params);
		
		if(cmsPositionDOList == null || cmsPositionDOList.size()==0){
			return null;
		}		
		
		Set<Long> ids = new HashSet<>();
		Set<Integer> elementTypes = new HashSet<>();
		for(Temple t : cmsPositionDOList){
			elementTypes.add(t.getElementType());
			if(ElementEnum.ACTIVITY.getValue() == t.getElementType()){
				ids.add(t.getId());
			}
		}
		List<ActivityElement> result = new ArrayList<ActivityElement>();
		for(Integer elementType : elementTypes){
			//暂时只有活动是分页的
			if(ElementEnum.ACTIVITY.getValue() == elementType){
				Map<String, Object> params1 = new HashMap<>();
				params1.put("start", cmsTemple.getStart());
				params1.put("pageSize", pagesize);
				params1.put("list", new ArrayList<Long>(ids));
				params1.put("promoterid", promoterId);
				if(promoterId !=null ){
					result.addAll(positionDao.selectActivityByTempletsPageForDSS(params1));										
				}else{
					result.addAll(positionDao.selectActivityByTempletsPage(params1));					
				}
			}/*else if(ElementEnum.WRITTEN.getValue() == elementType){
				return (List<T>) positionDao.selectWrittenByTempletId(cmsTempleDO);
			}
			else if(ElementEnum.PICTURE.getValue() == elementType){
				return (List<T>) positionDao.selectPicByTempletId(cmsTempleDO);
			}else if(ElementEnum.DEFINED.getValue() == elementType){
				return (List<T>) positionDao.selectDefinedByTempletId(cmsTempleDO);
			}*/
		}
		
		return result;
	}
	
	/**
	 * 调用查询元素信息，参数为页面编码和模块编码数组，分页查询
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private List<ActivityElement> queryActivityElementsByTempletCodesForDss(Long promoterId, String pageCode, List<String> templetCode, List<Long> notlist, int pagestart,int pagesize) throws Exception {		
		//通过模板编码获取模板id，注意：模板编码且启用的模板是唯一的，这需要后台管理去控制
		Temple cmsTemple = new Temple();
		cmsTemple.setStatus(0);
		cmsTemple.setStartPage(pagestart);
		cmsTemple.setPageSize(pagesize);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", 0);
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " temple_code in( '" + StringUtil.join(templetCode, "'"+Constant.SPLIT_SIGN.COMMA+"'") + "') ");
		
		List<Temple> cmsPositionDOList = templeDao.queryByParam(params);
		
		if(cmsPositionDOList == null || cmsPositionDOList.size()==0){
			return null;
		}		
		
		Set<Long> ids = new HashSet<>();
		Set<Integer> elementTypes = new HashSet<>();
		for(Temple t : cmsPositionDOList){
			elementTypes.add(t.getElementType());
			if(ElementEnum.ACTIVITY.getValue() == t.getElementType()){
				ids.add(t.getId());
			}
		}
		List<ActivityElement> result = new ArrayList<ActivityElement>();
		for(Integer elementType : elementTypes){
			//暂时只有活动是分页的
			if(ElementEnum.ACTIVITY.getValue() == elementType){
				Map<String, Object> params1 = new HashMap<>();
				params1.put("start", cmsTemple.getStart());
				params1.put("pageSize", pagesize);
				params1.put("list", new ArrayList<Long>(ids));
				params1.put("promoterid", promoterId);
				if(notlist!=null && !notlist.isEmpty() ){
					params1.put("nolist", notlist);					
				}
				if(promoterId !=null ){
					result.addAll(positionDao.selectActivityByTempletsPageForDSS(params1));										
				}else{
					result.addAll(positionDao.selectActivityByTempletsPage(params1));					
				}
			}
		}
		
		return result;
	}

	@Override
	public AppIndexAdvertReturnData carouseDssAdvert() {
		return null;
	}

	@Override
	public AppIndexAdvertReturnData dssFunLab(String channelCode) {
		// TODO Auto-generated method stub
		return null;
	}
}





