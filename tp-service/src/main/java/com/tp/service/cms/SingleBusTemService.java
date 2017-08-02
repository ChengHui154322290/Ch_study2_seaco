package com.tp.service.cms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.cms.PictureSizeConstant;
import com.tp.common.vo.cms.TempleConstant;
import com.tp.common.vo.mmp.TopicConstant;
import com.tp.dao.cms.AdvertiseInfoDao;
import com.tp.dao.cms.AnnounceInfoDao;
import com.tp.dao.cms.SingleTepactivityDao;
import com.tp.dao.cms.SingleTepnodeDao;
import com.tp.dao.cms.SingleproTempleDao;
import com.tp.dfsutils.service.DfsService;
import com.tp.dto.cms.CmsAnnounceInfoDTO;
import com.tp.dto.cms.CmsSingleTempleDTO;
import com.tp.dto.cms.TempleReturnData;
import com.tp.dto.cms.app.AppSingleCotAllInfoDTO;
import com.tp.dto.cms.app.AppSingleInfoDTO;
import com.tp.dto.cms.app.query.AppTopItemPageQuery;
import com.tp.dto.cms.query.HiTaoParamSingleBusQuery;
import com.tp.dto.cms.query.ParamSingleBusTemQuery;
import com.tp.dto.cms.temple.AdvanceData;
import com.tp.dto.cms.temple.AdvancePicData;
import com.tp.dto.cms.temple.Hoard;
import com.tp.dto.cms.temple.HomePageAdData;
import com.tp.dto.cms.temple.HomePageDiscountData;
import com.tp.dto.cms.temple.HotSellData;
import com.tp.dto.cms.temple.Interest;
import com.tp.dto.cms.temple.Products;
import com.tp.dto.cms.temple.Topic;
import com.tp.dto.cms.temple.TopicDiscountData;
import com.tp.dto.mmp.TopicDetailDTO;
import com.tp.dto.mmp.TopicItemBrandCategoryDTO;
import com.tp.dto.mmp.enums.CmsForcaseType;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.dto.mmp.enums.TopicType;
import com.tp.dto.ord.CartDTO;
import com.tp.dto.ord.CartLineDTO;
import com.tp.enums.common.PlatformEnum;
import com.tp.exception.CmsServiceException;
import com.tp.exception.ItemServiceException;
import com.tp.model.bse.Brand;
import com.tp.model.bse.Category;
import com.tp.model.bse.DistrictInfo;
import com.tp.model.cms.AdvertiseInfo;
import com.tp.model.cms.AnnounceInfo;
import com.tp.model.cms.SingleTepactivity;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.mem.FavoritePromotion;
import com.tp.model.mmp.TopicItem;
import com.tp.model.prd.ItemSku;
import com.tp.query.mmp.CmsTopicSimpleQuery;
import com.tp.query.mmp.TopicItemPageQuery;
import com.tp.result.bse.ChinaRegionInformation;
import com.tp.result.mmp.TopicInfo;
import com.tp.result.mmp.TopicItemInfoResult;
import com.tp.service.bse.IBrandService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.cms.ISingleBusTemService;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.mem.IFavoritePromotionService;
import com.tp.service.mmp.ITopicService;
import com.tp.service.ord.local.ICartLocalService;
import com.tp.service.prd.IItemRemoteService;
import com.tp.service.prd.IItemSkuService;
import com.tp.util.DateUtil;

@Service(value="singleBusTemService")
public class SingleBusTemService implements ISingleBusTemService{

	@Autowired
	private ITopicService topicService;
    
    @Autowired
    private ICategoryService categoryService;
    
    @Autowired
    private IBrandService brandService;
    
	@Autowired
	SingleproTempleDao singleproTempleDao;
	
	@Autowired
	SingleTepnodeDao singleTepnodeDao;
	
	@Autowired
	SingleTepactivityDao singleTepactivityDao;
	
	@Autowired
	AnnounceInfoDao announceInfoDao;
	
	@Autowired
	AdvertiseInfoDao advertiseInfoDao;
	
	@Autowired
	SwitchBussiesConfigDao switchBussiesConfigDao;
	
	@Autowired
	IFavoritePromotionService favoritePromotionService;
	
	@Autowired
	ICartLocalService cartLocalService;
	
	@Autowired
	IPromoterInfoService promoterInfoService;	

	@Autowired
	private IItemSkuService	itemSkuService;

	@Autowired
	private IItemRemoteService itemRemoteService;
		
	/*@Value("${cms.img.server}")
    private String cmsImgPath;*/
	
	@Autowired
	private DfsService dfsService;

	@Value("${upload.tmp.path}")
	private String uploadTempPath;
	
	@Value("${cms_chaimed_adress}")
	private String cmsChaimedAdress;
	
	@Value("${cms_topic_adress}")
	private String cmsTopicAdress;
	
	
	//首页使用
	@Value("${xgfront_address}")
	private String xgfrontAddress;
	

	private final static Log logger = LogFactory.getLog(SingleBusTemService.class);
	
	
	/**
	 * 首页-单品团-PC
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public String singleProduct(ParamSingleBusTemQuery paramSingleBusTemQuery) throws Exception {
		Map<String, Object> templateData = new HashMap<String, Object>();
		List<TempleReturnData> templateList = new ArrayList<TempleReturnData>();
		CmsSingleTempleDTO cmsSingleTempleDTO = new CmsSingleTempleDTO();
		cmsSingleTempleDTO.setPlatformType(TempleConstant.CMS_PLATFORM_PC_TYPE);
		cmsSingleTempleDTO.setDr(0);
		cmsSingleTempleDTO.setStatus("0");
		cmsSingleTempleDTO.setStatus(TempleConstant.USEING_TYPE);
	   	cmsSingleTempleDTO.setType(TempleConstant.CMS_SINGLE_SALE_TEMPLE);
	   	
	   	//查询单品团信息
	   	Map<String,Object> map = new HashMap<String,Object>();
	   	map = getSingleInfo(cmsSingleTempleDTO,0,100,TempleConstant.PROMOTION_PLATFORM_PC,"");
	   	CmsSingleTempleDTO cmode = (CmsSingleTempleDTO) map.get("tempMode");
	   	templateList = (List<TempleReturnData>) map.get("list");
	   	if(templateList == null){
	   		return "";
	   	}
	   	
	   	templateData.put("thispinlist", templateList);
	   	templateData.put("xgfrontAddress", xgfrontAddress);
	   	
	   	/**
	   	 * 从服务器下载文件到本地:
	   	 * 注意：如果正式上面单品团没有出来，那么有可能就是这里文件下载路径有问题，uploadTempPath是生产上面根目录下的：/temp/upload，那个是有权限的，直接改代码：
	   	 * String tempUploadPath = uploadTempPath；即可以，如果没报错就没问题
	   	 */
	   	/*
	   	//通过上传模板日志查询具体信息
	   	ImportTempleLogDO importTempleLogDO = uploadTempleLogDAO.selectById(cmode.getUploadTempleId(),DatasourceKey.slave_cms_data_source_0);
	   	
	   	
	   	String tempUploadPath = this.getClass().getClassLoader().getResource("").getPath()+uploadTempPath;
	   	
	   	File destFile = new File(tempUploadPath);
		if (!destFile.exists()) {
			destFile.mkdirs();
		}
	   	File file = querySingleproTemple(importTempleLogDO.getFileName(),
	   			importTempleLogDO.getSecretKey(), importTempleLogDO.getRealFileName(),tempUploadPath);
	   	*/
	   	//把TopicDetailDTO对象拆分成json字符串
	   	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/index03.flt",new StringWriter());
	   	/*String fileName = importTempleLogDO.getFileName();
	   	String newFilename = fileName.toString().substring(fileName.toString().lastIndexOf("/")+1,fileName.toString().length());
	   	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getFileDirectoryFMCFG(new File(tempUploadPath)), 
				templateData,newFilename,new StringWriter());//文件名以加密的文件名，这样不会出现重复*/
	       return str;
	}

	/**
	 * 首页-单品团-PC与APP公用业务
	 */
	private Map<String,Object> getSingleInfo(CmsSingleTempleDTO cmsSingleTempleDTO,
			int pagestart,int pagesize,int platType,String staticDomain) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		List<TempleReturnData> templateList = new ArrayList<TempleReturnData>();
		/**
	   	  * 1.1注意：首先找到模板表，然后在模板表中找到对应的位置，在对应的位置找到相应的id，并通过id去促销那边取数据
	   	  */
	   	//暂时由运维人员去控制，一次一条，以后会用时间去控制
		List<CmsSingleTempleDTO> singleTemoList = new ArrayList<CmsSingleTempleDTO>();
	   	singleTemoList = singleTepnodeDao.selectDynamic(cmsSingleTempleDTO);
	   	//获取模板id
	   	Long templeId = 0L;
	   	if(singleTemoList.size()>0){
	   		map.put("tempMode", singleTemoList.get(0));
	   		templeId = singleTemoList.get(0).getId();
	   	}else{
	   		return map;
	   	}
	   	
	   	/**
	   	 * 1.2再通过模板id去找每个位置的id,暂时不会把活动一起查询出，因为这块位置也要做成灵活的，现在是跟一下的模板位置写死的
	   	 * 注意：模板上面的集合是thispin1,thispin2,thispin3...这种延续下去，那么可以把前面的thispin固定住，后面的数字是从表中查询出来，
	   	 * 	这样就可以是动态的显示
	   	 */
	   	cmsSingleTempleDTO.setId(templeId);
	   	cmsSingleTempleDTO.setDr(0);
	   	//单品团是配置的，不需要分页，所以一次性查询出来
	   	if(pagestart == 0){
	   		cmsSingleTempleDTO.setPageSize(100);
	   	}
	   	cmsSingleTempleDTO.setStartPage((pagestart > 1 ? (pagestart - 1) * pagestart : 0));
	   	
	   	List<CmsSingleTempleDTO> singleNode = singleproTempleDao.selectSingleTemples(cmsSingleTempleDTO);
		
	  //通过位置去找活动id
	   	for(int i=0,j=singleNode.size();i<j;i++){
	   		CmsSingleTempleDTO tmp1 = singleNode.get(i);
	   		//获取位置节点id(singleTempleNodeId)
	   		Long nodeId = tmp1.getSingleTempleNodeId();
	   		//根据节点id去查找活动id
	   		SingleTepactivity cmsSingleTepactivityDO = new SingleTepactivity();
	   		cmsSingleTepactivityDO.setSingleTepnodeId(nodeId);
	   		cmsSingleTepactivityDO.setStartdate(new Date());
	   		cmsSingleTepactivityDO.setEnddate(new Date());
	   		List<SingleTepactivity> tmpList2 = singleTepactivityDao.queryByObject(cmsSingleTepactivityDO);
	   		
	   		/**
	   		 * 通过id去促销拿数据
	   		 */
	   		
	   		if(tmpList2!=null && tmpList2.size()>0){
	   			List<Long> ids = new ArrayList<Long>();
	   			StringBuffer sb = new StringBuffer();
	   			for(int t=0,y=tmpList2.size();t<y;t++){
	   				ids.add(tmpList2.get(t).getActivityId());
	   				sb.append(tmpList2.get(t).getActivityId()+";");
	   			}
	   			
	   			//TopicDetailDTO topicDetailDTO = this.getTopicDetailById(tmpList2.get(0).getActivityId());
	   			
	   			//注意下面传递ids的促销接口，是查询生效的正在进行中的单品团，会过滤掉一部分数据
	   			TopicDetailDTO topicDetailDTO = topicService.getSingleTopicDetailByIdsForCms(ids);
	   			//找出状态为进行中以及审批通过的活动
	           	if(topicDetailDTO != null && topicDetailDTO.getTopic() != null ){
	           		com.tp.model.mmp.Topic topicDO = topicDetailDTO.getTopic();
		           	/*//找出状态为进行中以及审批通过的活动
		           	if(TopicStatus.PASSED.ordinal()==topicDO.getStatus() 
	           			&& ProgressStatus.DOING.ordinal()==topicDO.getProgress() ){*/
	           		List<TopicItem> promotionItemList = topicDetailDTO.getPromotionItemList();
		       		
		       		TempleReturnData obj = new TempleReturnData();
		       		obj.setDescription(topicDO.getTopicPoint());//专题卖点
		       		obj.setIntro(topicDO.getIntro());//专题描述PC
		       		obj.setIntroMobile(topicDO.getIntroMobile());//专题描述APP
		       		obj.setRemark(topicDO.getRemark());//专题备注
		       		obj.setDiscount(topicDO.getDiscount());//折扣
		       		if(platType == TempleConstant.PROMOTION_PLATFORM_PC){
		       			//obj.setImg(dfsDomainUtil.getSnapshotUrl(topicDO.getImage(),278));
		       			/*obj.setImg(switchBussiesConfigDao.getPictureSrc_PC(
		       					topicDO.getImage(),PictureSizeConstant.INDEX_SINGLE_DAILY_STORE));*/
		       			obj.setImg(switchBussiesConfigDao.getPictureSrc_PC(
		       					topicDO.getPcImage(),PictureSizeConstant.INDEX_SINGLE_DAILY_STORE));//活动图片(需要配置前缀)，前台展示，需要压缩
		       		}else if(platType == TempleConstant.PROMOTION_PLATFORM_HAITAO){
		       			//obj.setImg(dfsDomainUtil.getSnapshotUrl(topicDO.getImageHitao(),576));//海淘图片
		       			/*obj.setImg(switchBussiesConfigDao.getPictureSrc_PC(
		       					topicDO.getImageHitao(),PictureSizeConstant.HAITAO_ACTIVITY_IMAGE));*/
		       			obj.setImg(switchBussiesConfigDao.getPictureSrc_PC(
		       					topicDO.getHaitaoImage(),PictureSizeConstant.HAITAO_ACTIVITY_IMAGE));
		       		}else{
		       			obj.setImg(topicDO.getImageMobile());
		       			obj.setMobileImage(topicDO.getMobileImage());//活动图片(需要配置前缀)
		       		}
		       		
		       		TopicItem promotionItemDO = new TopicItem();
		       		if(promotionItemList != null&&promotionItemList.size()>0){
		       			promotionItemDO = promotionItemList.get(0);
		       			
		       			if(platType == TempleConstant.PROMOTION_PLATFORM_PC){
		       				//obj.setGoodimg(dfsDomainUtil.getFileFullUrl(promotionItemDO.getTopicImage()));//商品图片(需要配置前缀)
		       				obj.setGoodimg(promotionItemDO.getTopicImage());//商品图片(此处公共方法不做拼接，在后面自己获取数据拼接)
			       		}else{
			       			obj.setGoodimg(promotionItemDO.getTopicImage());//商品图片(需要配置前缀)
			       		}
		       			
		    	       	obj.setName(promotionItemDO.getName());//商品名称
		    	       	obj.setPrice(promotionItemDO.getTopicPrice());//商品活动价
		    	       	obj.setOlderprice(promotionItemDO.getSalePrice());//商品原价
		    	       	
		    	       	//折扣：通过计算的出来的
		    	       	obj.setDiscountPrice(getPriceDiscount(promotionItemDO.getTopicPrice(), promotionItemDO.getSalePrice()));
		    	       	if(promotionItemDO.getSaledAmount() != null){
		    	       		obj.setBuy(promotionItemDO.getSaledAmount().toString());//商品的销售量，即多少人已海淘量
		    	       	}
		    	       	obj.setLink(splitJoinLinkAdress(topicDO.getId(),promotionItemDO.getSku()));//商品的链接
		    	       	obj.setProductId(promotionItemDO.getId());//商品id
		    	       	obj.setSku(promotionItemDO.getSku());//sku编码
		    	       	obj.setCountry(promotionItemDO.getCountryName());//设置国家
		    	       	obj.setCountryImg(staticDomain+CmsTempletUtil.getCountryImg(promotionItemDO.getCountryName()));//设置国家图片地址
		       		}
			       	obj.setPositionSize(Integer.valueOf(tmp1.getPositionSize().toString()));//位置大小，即1 2 3 4
			       	obj.setPositionSort(tmp1.getPositionSort());//位置排序，即位置顺序
			       	
			       	//obj.setActityId(tmpList2.get(0).getActivityId());//活动id
			       	obj.setActityId(topicDO.getId());//活动id
			       	obj.setActityName(topicDO.getName());//活动名称
			       	obj.setStartTime(topicDO.getStartTime());//活动开始时间
			       	obj.setEndTime(topicDO.getEndTime());//活动结束时间
			       	
			       	//计算是否最新单品
			       	Date startDate = topicDO.getStartTime();
			       	Long l = DateUtil.differDateHours(startDate,new Date());
			       	if(l>24){
			       		obj.setIsLastSinglePro(1);//是否最新单品，0表示是，1表示否
			       	}else{
			       		obj.setIsLastSinglePro(0);//是否最新单品，0表示是，1表示否
			       	}
			       	
			        //求出-倒计时
		   	    	Date endTime = topicDO.getEndTime();
		   	    	//此处需要把活动结束时间返回给前端，然后前端要实时计算，每秒种的刷时间
		   	    	if(endTime != null){
		   	    		obj.setDeadline(DateUtil.getSurplusDate(endTime));
		   	    		obj.setDeadlineString(DateUtil.getSurplusDateString(endTime));
		   	    	}else{
		   	    		obj.setDeadline("0,0,0,0");
		   	    		obj.setDeadlineString("00天00时00分00秒");
		   	    	}
			       	
		   	    	//设置是否长期活动
		   	    	obj.setLastType(topicDO.getLastingType());
		   	    	
		   	    	//设置品牌团和品牌名称
    	       		obj.setBrandPinId(topicDO.getBrandId());
	    	       	obj.setBrandPinName(topicDO.getBrandName());
	    	       	
	    	       	//类型
	    	       	if(topicDO.getType() != null){
	    	       		obj.setType(topicDO.getType().toString());
	    	       	}
	    	       		
	    	        //注意：上面设置了链接，是单品团链接直接跳到商品去，但是如果是非单品团是跳到品牌团页面去
	    	       	if(TopicType.SINGLE.ordinal() != topicDO.getType()){
	    	       		obj.setLink(splitJoinTopicAdress(topicDO.getId()));
	    	       	}
		   	    	
			       	templateList.add(obj);
	           	}else{
	           		logger.info("从promotion中获取报错，id值为："+sb.toString());
	           	}
		   	}
	   	}
	   	map.put("list", templateList);
	   	return map;
	}
	
	/**
	 * 首页-单品团-APP
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<AppSingleCotAllInfoDTO> singleProductAPP(int pagestart,int pagesize) throws Exception {
		CmsSingleTempleDTO cmsSingleTempleDTO = new CmsSingleTempleDTO();
		cmsSingleTempleDTO.setPlatformType(TempleConstant.CMS_PLATFORM_APP_TYPE);
		cmsSingleTempleDTO.setDr(0);
		cmsSingleTempleDTO.setStatus("0");
	   	cmsSingleTempleDTO.setType(TempleConstant.APP_SINGLE_SALE_TEMPLE);
	   	List<TempleReturnData> templateList = new ArrayList<TempleReturnData>();
	    //查询单品团信息
	   	Map<String,Object> map = new HashMap<String,Object>();
	   	map = getSingleInfo(cmsSingleTempleDTO,pagestart,pagesize,TempleConstant.PROMOTION_PLATFORM_APP,"");
	   	templateList = (List<TempleReturnData>) map.get("list");
	   	if(templateList == null){
	   		return getNullPage(pagestart, pagesize);
	   	}
	   	
	   	List<AppSingleCotAllInfoDTO> lst = new ArrayList<AppSingleCotAllInfoDTO>();
    	for(int i=0,j=templateList.size();i<j;i++){
    		AppSingleCotAllInfoDTO appSingleCotAllInfoDTO = new AppSingleCotAllInfoDTO();
    		TempleReturnData amode = templateList.get(i);
    		
    		//卖点
    		appSingleCotAllInfoDTO.setTopicPoint(amode.getDescription());
    		//优惠后的价格
    		appSingleCotAllInfoDTO.setPrice(amode.getPrice());
    		//原价
    		appSingleCotAllInfoDTO.setOldprice(amode.getOlderprice());
    		/*//已海淘数目
    		appSingleCotAllInfoDTO.setUnk(amode.getBuy());*/
    		//商品名称
    		appSingleCotAllInfoDTO.setGoodsName(amode.getName());
    		//活动详情detail
    		appSingleCotAllInfoDTO.setDetail(amode.getIntroMobile());
    		//详情图片(取得是活动图片)
    		List<String> lstStr = new ArrayList<String>();
    		lstStr.add(amode.getImg());
    		//lstStr.add(amode.getMobileImage());
    		appSingleCotAllInfoDTO.setImageurl(lstStr);
    		
    		//专场id(specialid)
    		appSingleCotAllInfoDTO.setSpecialid(amode.getActityId());
    		//专场名称
    		appSingleCotAllInfoDTO.setSpecialName(amode.getActityName());
    		//商品id(productid)
    		appSingleCotAllInfoDTO.setProductid(amode.getProductId());
    		//sku编码
    		appSingleCotAllInfoDTO.setSku(amode.getSku());
    		//活动开始时间
    		if(amode.getStartTime() != null){
    			appSingleCotAllInfoDTO.setStartTime(amode.getStartTime().getTime());
    		}
    		//活动结束时间
    		if(amode.getEndTime() != null){
    			appSingleCotAllInfoDTO.setEndTime(amode.getEndTime().getTime());
    		}
    		lst.add(appSingleCotAllInfoDTO);
    	}
    	
    	PageInfo<AppSingleCotAllInfoDTO>  page = new PageInfo<AppSingleCotAllInfoDTO>();
    	page.setRecords(lst.size());//首页-单品团-APP是一次性全部发送过去的，所以现在是直接设置
    	page.setRows(lst);
    	page.setPage(pagestart);
    	page.setSize(pagesize);
		return page;
	}

	private PageInfo<AppSingleCotAllInfoDTO> getNullPage(int pagestart, int pagesize) {
		PageInfo<AppSingleCotAllInfoDTO>  page = new PageInfo<AppSingleCotAllInfoDTO>();
		page.setRecords(0);
		page.setRows(null);
		page.setPage(pagestart);
		page.setSize(pagesize);
		return page;
	}
	
	/**
	 * 秒杀单品团(APP端) ：在cms中进行配置，读取都是和app单品团一致
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<AppSingleCotAllInfoDTO> querySeckillSingleInfo(int pagestart,int pagesize)
			throws Exception {
		CmsSingleTempleDTO cmsSingleTempleDTO = new CmsSingleTempleDTO();
		cmsSingleTempleDTO.setPlatformType(TempleConstant.CMS_PLATFORM_APP_TYPE);
		cmsSingleTempleDTO.setDr(0);
		cmsSingleTempleDTO.setStatus("0");
	   	cmsSingleTempleDTO.setType(TempleConstant.APP_SECKILL_SINGLE_TEMPLE);
	   	List<TempleReturnData> templateList = new ArrayList<TempleReturnData>();
	    //查询单品团信息
	   	Map<String,Object> map = new HashMap<String,Object>();
	   	map = getSingleInfo(cmsSingleTempleDTO,pagestart,pagesize,TempleConstant.PROMOTION_PLATFORM_APP,"");
	   	templateList = (List<TempleReturnData>) map.get("list");
	   	if(templateList == null){
	   		return getNullPage(pagestart, pagesize);
	   	}
	   	
	   	List<AppSingleCotAllInfoDTO> lst = new ArrayList<AppSingleCotAllInfoDTO>();
		for(int i=0,j=templateList.size();i<j;i++){
			AppSingleCotAllInfoDTO appSingleCotAllInfoDTO = new AppSingleCotAllInfoDTO();
			TempleReturnData amode = templateList.get(i);
			
			//卖点
    		appSingleCotAllInfoDTO.setTopicPoint(amode.getDescription());
			//优惠后的价格
			appSingleCotAllInfoDTO.setPrice(amode.getPrice());
			//原价
			appSingleCotAllInfoDTO.setOldprice(amode.getOlderprice());
			//商品名称
			appSingleCotAllInfoDTO.setGoodsName(amode.getName());
			//活动详情detail
			appSingleCotAllInfoDTO.setDetail(amode.getIntroMobile());
			//详情图片(取得是活动图片)
			List<String> lstStr = new ArrayList<String>();
			lstStr.add(amode.getImg());
			//lstStr.add(amode.getMobileImage());
			appSingleCotAllInfoDTO.setImageurl(lstStr);
			
			//专场id(specialid)
			appSingleCotAllInfoDTO.setSpecialid(amode.getActityId());
			//专场名称
    		appSingleCotAllInfoDTO.setSpecialName(amode.getActityName());
			//商品id(productid)
			appSingleCotAllInfoDTO.setProductid(amode.getProductId());
			//sku编码
			appSingleCotAllInfoDTO.setSku(amode.getSku());
			//活动开始时间
			if(amode.getStartTime() != null){
				appSingleCotAllInfoDTO.setStartTime(amode.getStartTime().getTime());
			}
			//活动结束时间
			if(amode.getEndTime() != null){
				appSingleCotAllInfoDTO.setEndTime(amode.getEndTime().getTime());
			}
			lst.add(appSingleCotAllInfoDTO);
		}
		
		PageInfo<AppSingleCotAllInfoDTO>  page = new PageInfo<AppSingleCotAllInfoDTO>();
    	page.setRecords(lst.size());//秒杀单品团(APP端)是一次性全部发送过去的，所以现在是直接设置
    	page.setRows(lst);
    	page.setPage(pagestart);
    	page.setSize(pagesize);
		return page;
	}
	
	/**
	 * WAP-今日精选(APP端) ：在cms中进行配置，读取都是和app单品团一致
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * @author szy
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> queryWAPChosenSingleInfo(int pagestart,int pagesize)
			throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		CmsSingleTempleDTO cmsSingleTempleDTO = new CmsSingleTempleDTO();
		cmsSingleTempleDTO.setPlatformType(TempleConstant.CMS_PLATFORM_APP_TYPE);
		cmsSingleTempleDTO.setDr(0);
		cmsSingleTempleDTO.setStatus("0");
	   	cmsSingleTempleDTO.setType(TempleConstant.APP_WAP_CHOSEN_SINGLE_TEMPLE);
	   	//活动专场集合
	   	List<TempleReturnData> activTemplateList = new ArrayList<TempleReturnData>();
	   	//单品团商品集合
	   	List<TempleReturnData> templateList = new ArrayList<TempleReturnData>();
	   	
	   	//查询今日精选的活动list
	   	cmsSingleTempleDTO.setType(TempleConstant.APP_WAP_CHOSEN_ACTIVITY_TEMPLE);
	   	Map<String,Object> map1 = new HashMap<String,Object>();
	   	map1 = getSingleInfo(cmsSingleTempleDTO,0,100,TempleConstant.PROMOTION_PLATFORM_APP,"");//今日精选的活动需要全部查询出来
	   	activTemplateList = (List<TempleReturnData>) map1.get("list");
	   	
	    //查询今日精选的单品团信息list
	   	cmsSingleTempleDTO.setType(TempleConstant.APP_WAP_CHOSEN_SINGLE_TEMPLE);
	   	Map<String,Object> map2 = new HashMap<String,Object>();
	   	map2 = getSingleInfo(cmsSingleTempleDTO,pagestart,pagesize,TempleConstant.PROMOTION_PLATFORM_APP,"");
	   	templateList = (List<TempleReturnData>) map2.get("list");
	   	
	   	/** 组装活动专场团列表信息 **/
	   	List<AppSingleCotAllInfoDTO> acvLst = new ArrayList<AppSingleCotAllInfoDTO>();
	   	if(activTemplateList != null && activTemplateList.size()>0){
			for(int i=0,j=activTemplateList.size();i<j;i++){
				AppSingleCotAllInfoDTO appSingleCotAllInfoDTO = new AppSingleCotAllInfoDTO();
				TempleReturnData amode = activTemplateList.get(i);
				
				//专场活动图片地址
				List<String> lstStr = new ArrayList<String>();
				lstStr.add(amode.getImg());
				//lstStr.add(amode.getMobileImage());
				appSingleCotAllInfoDTO.setImageurl(lstStr);
				//专场id(specialid)
				appSingleCotAllInfoDTO.setSpecialid(amode.getActityId());
				//专场名称
	    		appSingleCotAllInfoDTO.setSpecialName(amode.getActityName());
	    		//专场描述
	    		appSingleCotAllInfoDTO.setDetail(amode.getIntroMobile());
				//折扣
				appSingleCotAllInfoDTO.setDiscount(amode.getDiscount());
				//活动开始时间
				if(amode.getStartTime() != null){
					appSingleCotAllInfoDTO.setStartTime(amode.getStartTime().getTime());
				}
				//活动结束时间
				if(amode.getEndTime() != null){
					appSingleCotAllInfoDTO.setEndTime(amode.getEndTime().getTime());
				}
				acvLst.add(appSingleCotAllInfoDTO);
			}
	   	}
		
	   	/** 组装单品团列表信息 **/
	   	List<AppSingleCotAllInfoDTO> lst = new ArrayList<AppSingleCotAllInfoDTO>();
	   	if(templateList != null && templateList.size()>0){
			for(int i=0,j=templateList.size();i<j;i++){
				AppSingleCotAllInfoDTO appSingleCotAllInfoDTO = new AppSingleCotAllInfoDTO();
				TempleReturnData amode = templateList.get(i);
				
				//优惠后的价格
				appSingleCotAllInfoDTO.setPrice(amode.getPrice());
				//原价
				appSingleCotAllInfoDTO.setOldprice(amode.getOlderprice());
				//商品名称
				appSingleCotAllInfoDTO.setGoodsName(amode.getName());
				//活动详情detail
				appSingleCotAllInfoDTO.setDetail(amode.getIntroMobile());
				//活动图片
				List<String> lstStr = new ArrayList<String>();
				//lstStr.add(amode.getGoodimg());
				lstStr.add(amode.getImg());
				//lstStr.add(amode.getMobileImage());
				appSingleCotAllInfoDTO.setImageurl(lstStr);
				
				//专场id(specialid)
				appSingleCotAllInfoDTO.setSpecialid(amode.getActityId());
				//专场名称
	    		appSingleCotAllInfoDTO.setSpecialName(amode.getActityName());
				//商品id(productid)
				appSingleCotAllInfoDTO.setProductid(amode.getProductId());
				//sku编码
				appSingleCotAllInfoDTO.setSku(amode.getSku());
				//活动开始时间
				if(amode.getStartTime() != null){
					appSingleCotAllInfoDTO.setStartTime(amode.getStartTime().getTime());
				}
				//活动结束时间
				if(amode.getEndTime() != null){
					appSingleCotAllInfoDTO.setEndTime(amode.getEndTime().getTime());
				}
				lst.add(appSingleCotAllInfoDTO);
			}
	   	}
		
		//查询今日精选的主图数据(图片)
		List<AdvertiseInfo> cmsAdvertiseInfoDOList = queryAdvertBase(TempleConstant.APP_WAP_CHOSEN_PICTURE);
		/*CmsAdvertiseInfoDO cmsAdvertiseInfoDO = new CmsAdvertiseInfoDO();
    	cmsAdvertiseInfoDO.setType(TempleConstant.APP_WAP_CHOSEN_PICTURE);
    	//查询添加当前日期
    	cmsAdvertiseInfoDO.setStartdate(new Date());
    	cmsAdvertiseInfoDO.setEnddate(new Date());
    	cmsAdvertiseInfoDO.setStatus("0");
    	cmsAdvertiseInfoDO.setPlatformType(TempleConstant.CMS_PLATFORM_APP_TYPE);
    	List<CmsAdvertiseInfoDO>  cmsAdvertiseInfoDOList = advertiseInfoDao.selectDynamic(cmsAdvertiseInfoDO);*/
    	AdvertiseInfo cd = new AdvertiseInfo();
    	if(cmsAdvertiseInfoDOList != null && cmsAdvertiseInfoDOList.size()>0){
    		cd = cmsAdvertiseInfoDOList.get(0);
    		
    	}
    	map.put("pictureDO", cd);
    	map.put("acvitTempList", acvLst);
    	map.put("singleTempList", lst);
		return map;
	}


	/**
	 * 首页-今日特卖管理  即活动信息
	 * 注意：每次鼠标滑动，会调此方法，并加载3个数据过去，直到没有数据加载
	 * 		需要有标识去记录页数，前台有记录，每次结束后会自动加1，然后从数据库中查询
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * @author szy
	 */
	@Override
	public String singleIndexDiscountInfo(ParamSingleBusTemQuery paramSingleBusTemQuery) throws Exception {
		Integer pagestart = paramSingleBusTemQuery.getPagestart();
		Integer pageSize = paramSingleBusTemQuery.getPagesize();		
    	if(pagestart==null || pagestart == 0 || "".equals(pagestart) ){
    		pagestart = Integer.parseInt(TempleConstant.CMS_START_PAGE);
    	}
    	//今日特卖如果没有传值，暂定为3条
    	if(pageSize == null || pageSize == 0 || "".equals(pageSize) ){
    		pageSize = 3;
    	}
		//此参数是前台传入的
    	List<HomePageDiscountData> homePageDiscountList = new  ArrayList<HomePageDiscountData>();
    	
    	Map<String, Object> templateData = new HashMap<String, Object>();
    	
    	CmsTopicSimpleQuery paramCmsTopicSimpleQuery = new CmsTopicSimpleQuery();
    	paramCmsTopicSimpleQuery.setPageId(pagestart);
    	paramCmsTopicSimpleQuery.setPageSize(pageSize);
    	paramCmsTopicSimpleQuery.setPlatformType(TempleConstant.PROMOTION_PLATFORM_PC);
    	PageInfo<TopicDetailDTO> pagelist = topicService.getTodayTopic(paramCmsTopicSimpleQuery);
    	List<TopicDetailDTO> topicDetailDTOList = pagelist.getRows();
    	for(int i=0,j=topicDetailDTOList.size();i<j;i++){
    		TopicDetailDTO topicDetailDTO = new TopicDetailDTO();
    		topicDetailDTO = topicDetailDTOList.get(i);
    		HomePageDiscountData discoun1 = new HomePageDiscountData();
    		
        	com.tp.model.mmp.Topic topicDO = topicDetailDTO.getTopic();
   	    	//discoun1.setLeftImage(dfsDomainUtil.getFileFullUrl(topicDO.getImage()));//活动图片(需要配置前缀)
        	if(topicDO.getType() != null && 
        			topicDO.getType().intValue() == TopicType.THEME.ordinal()){
        		//如果是主题团，则直接取原图，755长度
        		/*discoun1.setLeftImage(switchBussiesConfigDao.getFullPictureSrc_PC(
        				topicDO.getImage()));*/
        		discoun1.setLeftImage(switchBussiesConfigDao.getFullPictureSrc_PC(
        				topicDO.getPcImage()));//活动图片(需要配置前缀)
        	}else{
        		//如果是品牌团，则需要压缩，取520长度
        		/*discoun1.setLeftImage(switchBussiesConfigDao.getPictureSrc_PC(
        				topicDO.getImage(),PictureSizeConstant.INDEX_DISCOUNT_BRANCH_LIST));*/
        		//从基础库中查询品牌logo
    		    Brand brandDO = brandService.queryById(topicDO.getBrandId());
    		    if(brandDO != null){
    		    	discoun1.setLeftImage(switchBussiesConfigDao.getFullPictureSrc_PC(
            				brandDO.getLogo()));//logo
    		    }
        		
        		discoun1.setRightImage(switchBussiesConfigDao.getPictureSrc_PC(
        				topicDO.getPcImage(),PictureSizeConstant.INDEX_DISCOUNT_BRANCH_LIST));//活动图片(需要配置前缀)
        	}
        	
   	    	//discoun1.setBrand(promotionItemDO.getName());//商品名称
        	discoun1.setTopicPoint(topicDO.getTopicPoint());
        	discoun1.setType(topicDO.getType());
   	    	discoun1.setBrand(topicDO.getName());//活动名称
   	    	discoun1.setBrandId(topicDO.getId());//活动ID
   	    	discoun1.setBrandPinName(topicDO.getBrandName());//品牌名称
   	    	discoun1.setBrandPinId(topicDO.getBrandId());//品牌ID
   	    	discoun1.setRate(topicDO.getDiscount());//活动折扣
   	    	discoun1.setLastType(topicDO.getLastingType());//长期活动
   	    	//获取活动的结束时间
   	    	Date endTime = topicDO.getEndTime();
   	    	//此处需要把活动结束时间返回给前端，然后前端要实时计算，每秒种的刷时间
   	    	if(endTime != null){
   	    		discoun1.setDeadline(DateUtil.getSurplusDate(endTime));
   	    		discoun1.setDeadlineString(DateUtil.getSurplusDateString(endTime));
   	    	}else{
   	    		discoun1.setDeadline("0,0,0,0");
   	    		discoun1.setDeadlineString("00天00时00分00秒");
   	    	}
   	    	
   	    	//获取活动的开始时间
   	    	Date startTime = topicDO.getStartTime();
   	    	if(DateUtil.formatDate(new Date(),"yyyy-MM-dd").equals(
   	    			DateUtil.formatDate(startTime,"yyyy-MM-dd")) ){
   	    		//今日上线
   	    		discoun1.setNewDayType(1);
   	    	}else if(DateUtil.formatDate(new Date(),"yyyy-MM-dd").equals(
   	    			DateUtil.formatDate(endTime,"yyyy-MM-dd"))){
   	    		//今日下线
   	    		discoun1.setNewDayType(2);
   	    	}
   	    	
   	    	//专题的链接地址
   	    	discoun1.setLink(splitJoinTopicAdress(topicDO.getId()));
   	    	homePageDiscountList.add(discoun1);
    	}
    	
    	/** 设置是否已收藏以及是否需要显示收藏 **/
	   	/*//是否显示收藏
		private boolean collectView;
		//是否已收藏
		private boolean collectBy;*/
    	
    	templateData.put("discounList", homePageDiscountList);
    	
    	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/discount.flt",new StringWriter());
	    return str;
	}
	
	/**
	 * 首页-今日必海淘管理
	 * 描述：把今天所要卖的全部加载出来,但是首页加载的时候会有四个，如果大于四的话就要另起一行重新排序
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * @author szy
	 */
	@Override
	public String singleloadHoardHtml(ParamSingleBusTemQuery paramSingleBusTemQuery)
			throws Exception {
		Map<String, List<Hoard>> templateData = new HashMap<String, List<Hoard>>();
    	List<Hoard> homePageDiscountList = new  ArrayList<Hoard>();
    	
    	Integer pagesize = paramSingleBusTemQuery.getPagesize();
    	Integer pagestart = paramSingleBusTemQuery.getPagestart();
    	if(pagesize==null || pagesize == 0 || "".equals(pagesize)  ){
    		pagesize = 12;//每次加载12条
    	}
    	if(pagestart==null || pagestart == 0 || "".equals(pagestart)  ){
    		pagestart = Integer.parseInt(TempleConstant.CMS_START_PAGE);
    	}
    	/*//只传当前时间和今天的结束时间作为参数，一次性全部查询出来
    	SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
		String str1 = sdf2.format(new Date())+" "+"23:59:59";
		String str2 = sdf2.format(new Date())+" "+"00:00:00";
		Date startTime = sdf1.parse(str1);
    	Date endTime = sdf1.parse(str2);
    	CmsTopicQuery cmsTopicQuery = new CmsTopicQuery();
    	cmsTopicQuery.setStartTime(startTime);
    	cmsTopicQuery.setEndTime(endTime);
    	cmsTopicQuery.setPageId(pagestart);
    	cmsTopicQuery.setPageSize(pagesize);
    	List<TopicDetailDTO> topicDetailDTOList = topicService.getCmsTopicList(cmsTopicQuery);*/
    	
    	CmsTopicSimpleQuery paramCmsTopicSimpleQuery = new CmsTopicSimpleQuery();
    	paramCmsTopicSimpleQuery.setPageId(pagestart);
    	paramCmsTopicSimpleQuery.setPageSize(pagesize);
    	paramCmsTopicSimpleQuery.setPlatformType(TempleConstant.PROMOTION_PLATFORM_PC);
    	
    	PageInfo<TopicDetailDTO> pg = topicService.getTodaySingleTopic(paramCmsTopicSimpleQuery);
    	List<TopicDetailDTO> topicDetailDTOList = pg.getRows();
    	for(int i=0,j=topicDetailDTOList.size();i<j;i++){
    		TopicDetailDTO topicDetailDTO = topicDetailDTOList.get(i);
    		com.tp.model.mmp.Topic topicDO = topicDetailDTO.getTopic();
    		
        	Hoard hoard = new Hoard();
        	hoard.setSrclink("/detail");
        	hoard.setMoney("&yen;");
        	
        	if(topicDetailDTO.getPromotionItemList() != null){
        		List<TopicItem> promotionItemList = topicDetailDTO.getPromotionItemList();
        		TopicItem topicItemDO = new TopicItem();
        		if(promotionItemList != null && promotionItemList.size()>0){
        			topicItemDO = promotionItemList.get(0);
        		}
        		//图片大小，如：100*100 200*200等
        		if(null == topicItemDO.getPictureSize()){
        			hoard.setPictureSize("1");
        			
        		}else{
        			int x = 1;
        			try{
        				x = topicItemDO.getPictureSize().intValue();
        			}catch(Exception e){
        				e.printStackTrace();
        			}
        			hoard.setPictureSize(String.valueOf(x));
        		}
        		//图片路径
        		//hoard.setImgsrc(dfsDomainUtil.getFileFullUrl(topicDO.getImage()));
        		//hoard.setImgsrc(dfsDomainUtil.getSnapshotUrl(topicDO.getImage(),278));//活动图片，需要压缩
        		/*hoard.setImgsrc(switchBussiesConfigDao.getPictureSrc_PC(
        				topicDO.getImage(),PictureSizeConstant.INDEX_SINGLE_DAILY_STORE));*/
        		hoard.setImgsrc(switchBussiesConfigDao.getPictureSrc_PC(
        				topicDO.getPcImage(),PictureSizeConstant.INDEX_SINGLE_DAILY_STORE));//活动图片，需要压缩
        		
        		
        		//活动名称，显示名称全部改成活动名称
            	hoard.setName(topicDO.getName());
        		//当前价格
        		//hoard.setNowValue(topicItemDO.getTopicPrice()==null?"":topicItemDO.getTopicPrice().toString());
            	hoard.setNowValue(topicItemDO.getTopicPrice());
        		//原来价格
            	//hoard.setLastValue(topicItemDO.getSalePrice()+"");
            	hoard.setLastValue(topicItemDO.getSalePrice());
            	
            	hoard.setDiscountPrice(getPriceDiscount(topicItemDO.getTopicPrice(), topicItemDO.getSalePrice()));
            	//已购买数量
            	hoard.setNowHoard(topicItemDO.getSaledAmount()+"人已海淘");
            	//商品sku编码
            	hoard.setSku(topicItemDO.getSku());
            	//描述，即今日卖点
            	hoard.setDescription(topicDO.getTopicPoint());
            	/*//活动名称
            	hoard.setActivityName(topicDO.getName());*/
            	//活动ID
            	hoard.setActivityId(topicDO.getId());
            	//长期活动
            	hoard.setLastType(topicDO.getLastingType());
            	/*//品牌名称
            	hoard.setBrandName(topicDO.getBrandName());
            	//品牌ID
            	hoard.setBrandId(topicDO.getBrandId());*/
            	if(topicDO!=null){
            		//商品链接
                	hoard.setNamelink(splitJoinLinkAdress(topicDO.getId(),topicItemDO.getSku()));
                	//“去看看”上面的商品链接
                	hoard.setSeelink(splitJoinLinkAdress(topicDO.getId(),topicItemDO.getSku()));
                	
                	//计算是否最新单品
        	       	Date startDate = topicDO.getStartTime();
        	       	Long l = DateUtil.differDateHours(startDate,new Date());
        	       	if(l>24){
        	       		hoard.setIsLastSinglePro(1);//是否最新单品，0表示是，1表示否
        	       	}else{
        	       		hoard.setIsLastSinglePro(0);//是否最新单品，0表示是，1表示否
        	       	}
        	       	
                	//求出-倒计时
           	    	Date endDeadTime = topicDO.getEndTime();
           	    	//此处需要把活动结束时间返回给前端，然后前端要实时计算，每秒种的刷时间
           	    	if(endDeadTime != null){
           	    		hoard.setDeadline(DateUtil.getSurplusDate(endDeadTime));
           	    		hoard.setDeadlineString(DateUtil.getSurplusDateString(endDeadTime));
           	    	}else{
           	    		hoard.setDeadline("0,0,0,0");
           	    		hoard.setDeadlineString("00天00时00分00秒");
           	    	}
           	    	
            	}
        	}else{
        		hoard.setNowValue(0.00);
            	hoard.setLastValue(0.00);
            	hoard.setNowHoard("0人已海淘");
            	hoard.setDeadline("0,0,0,0");
        	}
        	
        	hoard.setSee("去看看");
        	hoard.setPictureSize("1");
        	
        	homePageDiscountList.add(hoard);
    	}
    	
    	templateData.put("homePageDiscountList", homePageDiscountList);
    	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/hoard.flt",new StringWriter());
	    return str;
	}
	
	
	/**
	 * 最后疯抢页面-今日特卖管理(PC)
	 * 注意：后台没有模板配置，直接取促销数据
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * @author szy
	 */
	@Override
	public String singleRushedDiscountHtml(ParamSingleBusTemQuery paramSingleBusTemQuery) throws Exception {
		Map<String, Object> templateData = new HashMap<String, Object>();
    	List<HomePageDiscountData> homePageDiscountList = new  ArrayList<HomePageDiscountData>();
    	
    	Integer pageSize = paramSingleBusTemQuery.getPagesize();
    	Integer pagestart = paramSingleBusTemQuery.getPagestart();
    	if(pagestart==null || pagestart == 0 || "".equals(pagestart) ){
    		pagestart = Integer.parseInt(TempleConstant.CMS_START_PAGE);
    	}
    	//最后疯抢的今日特卖如果没有传值，暂定为3条
    	if( pageSize==null || pageSize == 0 || "".equals(pageSize) ){
    		pageSize = 3;
    	}
    	
    	homePageDiscountList = getTodayLastComm(pagestart, pageSize,TempleConstant.PROMOTION_PLATFORM_PC, homePageDiscountList);
    	
    	/** hqb:设置是否已收藏以及是否需要显示收藏 **/
	   	/*//是否显示收藏
		private boolean collectView;
		//是否已收藏
		private boolean collectBy;*/
    	
    	templateData.put("discounList", homePageDiscountList);
    	
    	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/discount.flt",new StringWriter());
	    return str;
	}

	private List<HomePageDiscountData> getTodayLastComm(int pagestart, int pageSize,int platform,
			List<HomePageDiscountData> homePageDiscountList) throws Exception {
		
		CmsTopicSimpleQuery paramCmsTopicSimpleQuery = new CmsTopicSimpleQuery();
    	paramCmsTopicSimpleQuery.setPageId(pagestart);
    	paramCmsTopicSimpleQuery.setPageSize(pageSize);
    	paramCmsTopicSimpleQuery.setPlatformType(platform);
    	
		PageInfo<TopicDetailDTO> pagelist = topicService.getTodayLastRash(paramCmsTopicSimpleQuery);//1是起始位置，3是大小
    	List<TopicDetailDTO> topicDetailDTOList = pagelist.getRows();
    	for(int i=0,j=topicDetailDTOList.size();i<j;i++){
    		TopicDetailDTO topicDetailDTO = new TopicDetailDTO();
    		topicDetailDTO = topicDetailDTOList.get(i);
    		HomePageDiscountData discoun1 = new HomePageDiscountData();
    		
    		com.tp.model.mmp.Topic topicDO = topicDetailDTO.getTopic();
    		//专题ID
    		discoun1.setBrandId(topicDO.getId());
    		//图片地址
    		if(platform == TempleConstant.PROMOTION_PLATFORM_PC){
    			//discoun1.setLeftImage(dfsDomainUtil.getFileFullUrl(topicDO.getImage()));
    			/*discoun1.setLeftImage(switchBussiesConfigDao.getFullPictureSrc_PC(topicDO.getImage()));*/
    			discoun1.setLeftImage(switchBussiesConfigDao.getFullPictureSrc_PC(topicDO.getPcImage()));
    		}else{
    			discoun1.setLeftImage(topicDO.getImageMobile());
    			discoun1.setMobileImage(topicDO.getMobileImage());
    		}
        	
        	//专题链接
    		discoun1.setLink(splitJoinTopicAdress(topicDO.getId()));
    		//专题名称
        	discoun1.setBrand(topicDO.getName());//专题名称
        	//专题折扣
        	discoun1.setRate(topicDO.getDiscount());
        	//长期活动
        	discoun1.setLastType(topicDO.getLastingType());
        	//品牌名称
        	discoun1.setBrandPinName(topicDO.getBrandName());
        	//品牌ID
        	discoun1.setBrandPinId(topicDO.getBrandId());
        	
        	//获取活动的结束时间
   	    	Date endTime = topicDO.getEndTime();
   	    	//此处需要把活动结束时间返回给前端，然后前端要实时计算，每秒种的刷时间
   	    	discoun1.setDeadline(DateUtil.getSurplusDate(endTime));
   	    	discoun1.setDeadlineString(DateUtil.getSurplusDateString(endTime));
   	    	
   	    	//下面的开始和结束时间是传给app的
   	    	discoun1.setStartTime(topicDO.getStartTime());
   	    	discoun1.setEndTime(topicDO.getEndTime());
        	
        	homePageDiscountList.add(discoun1);
    	}
    	return homePageDiscountList;
	}
	
	/**
	 * 最后疯抢页面-今日特卖管理(APP)
	 * 注意：后台没有模板配置，直接取促销数据
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * @author szy
	 */
	@Override
	public PageInfo<AppSingleInfoDTO> singleRushedDiscountHtmlAPP(int pageStart, int pageSize,PlatformEnum platformType)
			throws Exception {
		if(pageStart == 0 || "".equals(pageStart)){
			pageStart = Integer.parseInt(TempleConstant.CMS_START_PAGE);
		}
		if(pageSize == 0 || "".equals(pageSize)){
			pageSize = Integer.parseInt(TempleConstant.CMS_PAGE_SIZE);
		}
		
    	List<HomePageDiscountData> homePageDiscountList = new  ArrayList<HomePageDiscountData>();
    	
    	homePageDiscountList = getTodayLastComm(pageStart, pageSize,TempleConstant.PROMOTION_PLATFORM_APP, homePageDiscountList);
    	
    	List<AppSingleInfoDTO> lst = new ArrayList<AppSingleInfoDTO>();
    	for(int i=0,j=homePageDiscountList.size();i<j;i++){
    		AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    		HomePageDiscountData amode = homePageDiscountList.get(i);
    		
    		appSingleInfoDTO.setDiscount(amode.getRate());
    		//剩余时间
    		appSingleInfoDTO.setSurplusTime(amode.getDeadline());
    		appSingleInfoDTO.setImageurl(amode.getLeftImage());
    		appSingleInfoDTO.setMobileImage(amode.getMobileImage());
    		appSingleInfoDTO.setName(amode.getBrand());
    		appSingleInfoDTO.setSpecialid(amode.getBrandId());
    		
    		if(amode.getStartTime() != null){
    			appSingleInfoDTO.setStartTime(amode.getStartTime().getTime());
    		}
    		if(amode.getEndTime() != null){
    			appSingleInfoDTO.setEndTime(amode.getEndTime().getTime());
    		}
    		
    		lst.add(appSingleInfoDTO);
    	}
    	
    	PageInfo<AppSingleInfoDTO>  page = new PageInfo<AppSingleInfoDTO>();
    	page.setRecords(lst.size());
    	page.setRows(lst);
    	page.setPage(pageStart);
    	page.setSize(pageSize);
    	
	    return page;
	}
	
	/**
	 * 明日预告管理(PC)-非首页
	 * 注意：后台没有模板配置，直接取促销数据
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * @author szy
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String,String> singleAdvanceHtml(ParamSingleBusTemQuery paramSingleBusTemQuery)
			throws Exception {
		Map<String, Object> templateData = new HashMap<String, Object>();
		
		Integer pageSize = paramSingleBusTemQuery.getPagesize();
    	Integer pagestart = paramSingleBusTemQuery.getPagestart();
		if(pageSize==null || pageSize == 0 || "".equals(pageSize) ){
			pageSize = 8;
    	}//每次加载16条
    	if(pagestart==null || pagestart == 0 || "".equals(pagestart) ){
    		pagestart = Integer.parseInt(TempleConstant.CMS_START_PAGE);
    	}
//    	List<AdvanceData> advanceList = new ArrayList<AdvanceData>();
    	
		templateData = getSingleAdvancehtml(pagestart, pageSize, paramSingleBusTemQuery.getDateCounts(),
				TempleConstant.PROMOTION_PLATFORM_PC, templateData,paramSingleBusTemQuery.getUserId());

    	//templateData.put("advanceList", advanceList);
		if(((List<AdvancePicData>)templateData.get("advanceList")).size()<1){
			templateData = getSingleAdvancehtml(1, pageSize, paramSingleBusTemQuery.getDateCounts()+1,
					TempleConstant.PROMOTION_PLATFORM_PC, templateData,paramSingleBusTemQuery.getUserId());
			if(((List<AdvancePicData>)templateData.get("advanceList")).size()<1){
				templateData.put("advanceData", null);
			}
		}else{
			if(pagestart != 1){
				templateData.put("advanceData", null);
			}
		}
		templateData.put("tele", paramSingleBusTemQuery.getTele());
    	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/advance.flt",new StringWriter());
    	Map<String,String> advence = new  HashMap<String,String>();
    	advence.put("html", str);
    	advence.put("datecount", templateData.get("dateCounts").toString());
	    return advence;
	}
	

	private Map<String, Object> getSingleAdvancehtml(int pageStart, int pageSize,int dateCounts,int platform,
			Map<String, Object> advanceList,long userId) throws Exception {
		/** 首先需要查询用户所关注的活动集合 **/
		List<FavoritePromotion> FavoriteList = favoritePromotionService.getOnSalePromotionsByUid(userId);
		List<Long> ids = new ArrayList<Long>();
		for(int i=0,j=FavoriteList.size();i<j;i++){
			ids.add(FavoriteList.get(i).getPromotionId());
		}
		
		List<AdvancePicData> lst = new ArrayList<AdvancePicData>();
		
		AdvanceData advanceData = new AdvanceData();
    	
    	Date d = DateUtil.getDaysCounts(new Date(), +dateCounts);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String tomorrow = sdf.format(d);//tomorrow是日期格式的字符串，如2015-12-25
		String dateString = toparseString(tomorrow);
		
    	advanceData.setDate(dateString);
    	advanceData.setMessage("早上10:00开售");
    	
    	CmsTopicSimpleQuery paramCmsTopicSimpleQuery = new CmsTopicSimpleQuery();
    	paramCmsTopicSimpleQuery.setPageId(pageStart);
    	paramCmsTopicSimpleQuery.setPageSize(pageSize);
    	paramCmsTopicSimpleQuery.setData(tomorrow);
    	paramCmsTopicSimpleQuery.setPlatformType(platform);
    	
    	PageInfo<TopicDetailDTO> pagelist = topicService.getTomorrowForecast(paramCmsTopicSimpleQuery);//1是起始位置，3是大小
    	List<TopicDetailDTO> topicDetailDTOList = pagelist.getRows();
    	for(int i=0,j=topicDetailDTOList.size();i<j;i++){
    		TopicDetailDTO topicDetailDTO = new TopicDetailDTO();
    		topicDetailDTO = topicDetailDTOList.get(i);
    		
   	    	com.tp.model.mmp.Topic topicDO = topicDetailDTO.getTopic();
   	    	
   	    	AdvancePicData a1 = new AdvancePicData();
        	
   	    	if(topicDetailDTO.getPromotionItemList() != null 
        			&& topicDetailDTO.getPromotionItemList().size()>0){
        		TopicItem topicItemDO = topicDetailDTO.getPromotionItemList().get(0);
        		//图片链接
        		a1.setLink(splitJoinLinkAdress(topicDO.getId(),topicItemDO.getSku()));
        		//图片路径
        		if(platform == TempleConstant.PROMOTION_PLATFORM_PC){
        			//a1.setImageSrc(dfsDomainUtil.getSnapshotUrl(topicDO.getImageNew(),375));//要不要改成378
        			/*a1.setImageSrc(switchBussiesConfigDao.getPictureSrc_PC(
        					topicDO.getImageNew(),PictureSizeConstant.INDEX_TRAILER_ADVANCE));*/
        			a1.setImageSrc(switchBussiesConfigDao.getPictureSrc_PC(
        					topicDO.getPcInterestImage(),PictureSizeConstant.INDEX_TRAILER_ADVANCE));
        		}else{
        			a1.setImageSrc(topicDO.getImageMobile());
        			a1.setMobileImage(topicDO.getMobileImage());
        		}
        		
            	//专场名称
            	//a1.setName(topicItemDO.getName());
        		a1.setName(topicDO.getName());
            	//专场id
            	a1.setActivityId(topicDO.getId()); 
            	//专场开始时间
            	a1.setStartdate(topicDO.getStartTime()); 
            	//专场结束时间
            	a1.setEnddate(topicDO.getEndTime()); 
            	//折扣
            	a1.setRate(topicDO.getDiscount()); 
            	//长期活动
            	a1.setLastType(topicDO.getLastingType()); 
            	
            	//sku
            	a1.setSkuCode(topicItemDO.getSku());
            	
            	//设置是否是已关注的活动
            	final long topicId = topicDO.getId();
            	boolean bool =   CollectionUtils.exists(ids, new Predicate() { 
            		 public boolean evaluate(Object object) { 
                         return topicId == (long)object; 
                     } 
                });
            	a1.setAttentionStatus(bool);
            	
   	    	}
//        	advanceData.getAdvancePic().add(a1);
   	    	if(platform == TempleConstant.PROMOTION_PLATFORM_PC){
   	    		lst.add(a1);
    		}else{
    			if(topicDO.getType() != TopicType.SINGLE.ordinal()){
    				lst.add(a1);
    			}
    		}
   	    	
    	}
    	/*if(advanceData.getAdvancePic().size()>0){
    		advanceList.add(advanceData);
    	}*/
    	
    	advanceList.put("advanceData", advanceData);
    	advanceList.put("advanceList", lst);
    	advanceList.put("dateCounts", dateCounts);
    	advanceList.put("totalCount", pagelist.getTotal());
    	
    	return advanceList;
	}

	/**
	 * 明日预告管理(APP)
	 * 注意：后台没有模板配置，直接取促销数据
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * @author szy
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<AppSingleInfoDTO> singleAdvanceHtmlAPP(int pagestart, int pageSize,int dateCounts,PlatformEnum platformType,long userId)
			throws Exception {
		
		if(pagestart == 0 || "".equals(pagestart)){
			pagestart = Integer.parseInt(TempleConstant.CMS_START_PAGE);
		}
		if(pageSize == 0 || "".equals(pageSize)){
			pageSize = Integer.parseInt(TempleConstant.CMS_PAGE_SIZE);
		}
		
		List<AdvancePicData> advanceList = new ArrayList<AdvancePicData>();
		Map<String, Object> templateData = new HashMap<String, Object>();
		templateData = getSingleAdvancehtml(pagestart, pageSize, dateCounts,TempleConstant.PROMOTION_PLATFORM_APP, templateData,userId);
		
		advanceList = (List<AdvancePicData>) templateData.get("advanceList");
		
		if(advanceList.size()<1){
			templateData = getSingleAdvancehtml(1, pageSize, dateCounts+1,TempleConstant.PROMOTION_PLATFORM_APP, templateData,userId);
			if(advanceList.size()<1){
				templateData.put("advanceData", null);
			}
		}else{
			if(pagestart != 1){
				templateData.put("advanceData", null);
			}
		}
		
    	List<AppSingleInfoDTO> lst = new ArrayList<AppSingleInfoDTO>();
    	for(int i=0,j=advanceList.size();i<j;i++){
    		AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
    		AdvancePicData amode = advanceList.get(i);
    		
    		appSingleInfoDTO.setDiscount(amode.getRate());
    		if(amode.getStartdate() != null){
    			appSingleInfoDTO.setStartTime(amode.getStartdate().getTime());
    		}
    		if(amode.getEnddate() != null){
    			appSingleInfoDTO.setEndTime(amode.getEnddate().getTime());
    		}
    		appSingleInfoDTO.setImageurl(amode.getImageSrc());
    		appSingleInfoDTO.setMobileImage(amode.getMobileImage());
    		appSingleInfoDTO.setName(amode.getName());
    		appSingleInfoDTO.setSpecialid(amode.getActivityId());
    		appSingleInfoDTO.setAttentionStatus(amode.getAttentionStatus());
    		
    		lst.add(appSingleInfoDTO);
    	}
    	
    	PageInfo<AppSingleInfoDTO>  page = new PageInfo<AppSingleInfoDTO>();
    	
    	page.setRecords(Integer.parseInt(templateData.get("totalCount").toString()));
    	page.setRows(lst);
    	page.setPage(pagestart);
    	page.setSize(pageSize);
    	
	    return page;
	    
	}
	
	
	/**
     * 首页-公告资讯的加载
     * @return
     * @throws Exception
     */
	@Override
	public String loadIndexAnnouncementHtml() throws Exception {
		Map<String, Object> templateData = new HashMap<String, Object>();
    	String fix1 = "公告资讯";
    	String fix2 = "市场合作";
    	
    	templateData.put("fix1", fix1);
    	templateData.put("fix2", fix2);
    	
    	/*//从后台查询出首页公告资讯信息,类型定位1
    	AnnounceInfo cmsAnnounceInfoDO = new AnnounceInfo();
    	cmsAnnounceInfoDO.setStatus(0);
    	cmsAnnounceInfoDO.setType(TempleConstant.CMS_FIX_ADVICE_INDEX_INFO);*/
    	
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("status", 0);
    	paramMap.put("type", TempleConstant.CMS_FIX_ADVICE_INDEX_INFO);
    	paramMap.put("start", 0);
    	//paramMap.put("start", (pageId > 1 ? (pageId - 1) * pageSize : 0));
    	paramMap.put("pageSize", 6);
    	List<CmsAnnounceInfoDTO> cmsAnnounceInfoDOList = announceInfoDao.selectAnnouncePageQuery(paramMap);
    	
    	if(cmsAnnounceInfoDOList != null && cmsAnnounceInfoDOList.size()>0){
        	for(int i=0,j=cmsAnnounceInfoDOList.size();i<j;i++){
        		CmsAnnounceInfoDTO c = cmsAnnounceInfoDOList.get(i);
        		if(c.getLink() != null && !"".equals(c.getLink())){
        			if(!c.getLink().contains("http://") && !c.getLink().contains("https://")){
        				cmsAnnounceInfoDOList.get(i).setLink("http://"+c.getLink());
            		}
        		}
        	}
    	}
    	
    	templateData.put("fix1_info", cmsAnnounceInfoDOList);
    	
    	/*//从后台查询出首页市场资讯信息,类型定位2
    	cmsAnnounceInfoDO.setType(TempleConstant.CMS_FIX_MAKERT_INDEX_INFO);*/
    	paramMap.put("type", TempleConstant.CMS_FIX_MAKERT_INDEX_INFO);
    	List<CmsAnnounceInfoDTO> cmsAnnounceInfoDOList2 = announceInfoDao.selectAnnouncePageQuery(paramMap);
    	
    	if(cmsAnnounceInfoDOList2 != null && cmsAnnounceInfoDOList2.size()>0){
        	for(int i=0,j=cmsAnnounceInfoDOList2.size();i<j;i++){
        		CmsAnnounceInfoDTO c = cmsAnnounceInfoDOList2.get(i);
        		if(c.getLink() != null && !"".equals(c.getLink())){
        			if(!c.getLink().contains("http://") && !c.getLink().contains("https://")){
        				cmsAnnounceInfoDOList2.get(i).setLink("http://"+c.getLink());
            		}
        		}
        	}
    	}
    	
    	templateData.put("fix2_info", cmsAnnounceInfoDOList2);
    	
    	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/announcement.flt",new StringWriter());
	    return str;
	}


	/**
     * 最后疯抢-公告资讯的加载
     * @return
     * @throws Exception
     */
	@Override
	public String loadRushedAnnouncementHtml() throws Exception {
		Map<String, Object> templateData = new HashMap<String, Object>();
    	String fix1 = "公告资讯";
    	String fix2 = "市场合作";
    	templateData.put("fix1", fix1);
    	templateData.put("fix2", fix2);
    	
    	//从后台查询出公告资讯信息
    	AnnounceInfo cmsAnnounceInfoDO = new AnnounceInfo();
    	cmsAnnounceInfoDO.setStatus(0);
    	cmsAnnounceInfoDO.setType(TempleConstant.CMS_FIX_ADVICE_SALE_INFO);
    	List<AnnounceInfo> cmsAnnounceInfoDOList = announceInfoDao.selectDynamic(cmsAnnounceInfoDO);
    	
    	if(cmsAnnounceInfoDOList != null && cmsAnnounceInfoDOList.size()>0){
        	for(int i=0,j=cmsAnnounceInfoDOList.size();i<j;i++){
        		AnnounceInfo c = cmsAnnounceInfoDOList.get(i);
        		if(c.getLink() != null && !"".equals(c.getLink())){
        			if(!c.getLink().contains("http://") && !c.getLink().contains("https://")){
        				cmsAnnounceInfoDOList.get(i).setLink("http://"+c.getLink());
            		}
        		}
        	}
    	}
    	
    	templateData.put("fix1_info", cmsAnnounceInfoDOList);
    	
    	//从后台查询出首页市场资讯信息,类型定位2
    	cmsAnnounceInfoDO.setType(TempleConstant.CMS_FIX_MAKERT_SALE_INFO);
    	List<AnnounceInfo> cmsAnnounceInfoDOList2 = announceInfoDao.selectDynamic(cmsAnnounceInfoDO);
    	
    	if(cmsAnnounceInfoDOList2 != null && cmsAnnounceInfoDOList2.size()>0){
        	for(int i=0,j=cmsAnnounceInfoDOList2.size();i<j;i++){
        		AnnounceInfo c = cmsAnnounceInfoDOList2.get(i);
        		if(c.getLink() != null && !"".equals(c.getLink())){
        			if(!c.getLink().contains("http://") && !c.getLink().contains("https://")){
        				cmsAnnounceInfoDOList2.get(i).setLink("http://"+c.getLink());
            		}
        		}
        	}
    	}
    	
    	templateData.put("fix2_info", cmsAnnounceInfoDOList2);
    	
    	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/announcement.flt",new StringWriter());
	    return str;
	}
	
	/**
     * 品牌专题-过滤条件的加载：分类和品牌加载
     * @return
     * @throws Exception
     */
	@Override
	public String loadFilterclassInfoHtml(long topicId) throws Exception {
		Map<String, Object> templateData = new HashMap<String, Object>();
		
		templateData = getBaseBrandsInfo(topicId, templateData,null,null);
    	
    	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/filter.flt",new StringWriter());
	    return str;
	}

	private Map<String, Object> getBaseBrandsInfo(long topicId,
			Map<String, Object> templateData,List<Long> brandids,List<Long> kindIds) throws Exception {
		TopicItemBrandCategoryDTO t = new TopicItemBrandCategoryDTO();
		if(topicId > 0 && !"".equals(topicId)){
			t = topicService.queryTopicItemBrandAndCategoryList(topicId);
			//获取分类id集合
	    	 List<Long> categoryIdList = t.getCategoryIdList();
	    	//获取品牌id集合
	    	 List<Long> brandIdList = t.getBrandIdList();
	    	 
	    	//通过分类和品牌的id去基础库里面查询id对应的名称
	    	kindIds = new ArrayList<Long>();
	    	for(int i=0;i<categoryIdList.size();i++){
	    		kindIds.add(categoryIdList.get(i));
	    	}
	    	
	    	brandids = new ArrayList<Long>();
	    	for(int j=0;j<brandIdList.size();j++){
	    		brandids.add(brandIdList.get(j));
	    	}
		}
    	//调用base接口，取获取分类值，里面的int类型参数为2，表示全部查询
    	List<Category> kinds = categoryService.selectByIdsAndStatus(kindIds, 2);
    	
    	//调用base接口，取获取品牌值，里面的int类型参数为2，表示全部查询
    	List<Brand> brands = brandService.selectListBrand(brandids,2);
    	
    	
    	//收藏详细信息
    	TopicDetailDTO  topicDetailDTO = topicService.getTopicDetailById(null, Long.valueOf(topicId));
    	
    	templateData.put("topId", topicId);
    	
    	templateData.put("topicName", topicDetailDTO.getTopic().getName());
		
    	templateData.put("brandId", topicDetailDTO==null?"":topicDetailDTO.getTopic().getBrandId());
    	
    	//主题团状态
    	templateData.put("topicStatus", topicDetailDTO==null?"":topicDetailDTO.getTopic().getStatus());
		
		if(topicDetailDTO.getTopic().getLastingType() == TopicConstant.TOPIC_DURATIONTYPE_LONG){
			templateData.put("lastthing", "1");
		}else{
			templateData.put("lastthing", "0");
		}
		if(topicDetailDTO!=null&&!"0,0,0,0".equals(DateUtil.getSurplusDate(topicDetailDTO.getTopic().getStartTime()))){
			templateData.put("statu", "will");
			templateData.put("dateline",DateUtil.getSurplusDate(topicDetailDTO.getTopic().getStartTime()));
			templateData.put("datelineString",DateUtil.getSurplusDateString(topicDetailDTO.getTopic().getStartTime()));
			
		}else{
			templateData.put("statu", "hava");
			templateData.put("dateline", topicDetailDTO==null?"0,0,0,0":DateUtil.getSurplusDate(topicDetailDTO.getTopic().getEndTime()));
			templateData.put("datelineString",topicDetailDTO==null?"00天00小时00分00秒":DateUtil.getSurplusDateString(topicDetailDTO.getTopic().getEndTime()));
		}
		
    	
    	
    	templateData.put("kinds", kinds);
    	templateData.put("brands", brands);
    	templateData.put("hoard", "仅显示有货库存");
    	templateData.put("price", "价格");
    	
    	
    	return templateData;
	}

	/**
     * 品牌专题-过滤条件的加载：分类和品牌加载(APP)
     * @return
     * @throws Exception
     */
	@Override
	public Map<String, Object> loadFilterclassInfoHtmlApp(List<Long> brandids,List<Long> kinds) throws Exception {
		Map<String, Object> templateData = new HashMap<String, Object>();
    	templateData = getBaseBrandsInfo(0, templateData,brandids,kinds);
	    return templateData;
	}
	
	/**
     * 品牌专题-通过过滤条件去查品牌明细
     * @param 
     * @return
     * @throws Exception
     */
	@Override
	public String loadTopiInfocHtml(TopicItemPageQuery query) throws Exception {
		Map<String, Object> templateData = new HashMap<String, Object>();
		query.setPageSize(100);
		if(query.getPageSize() == null || query.getPageSize()==0){
			//暂时设置页数为8
	    	query.setPageSize(100);
		}
		if(query.getPageId() == null || query.getPageId()==0){
			query.setPageId(1);
		}
		
		com.tp.model.mmp.Topic topicDO = topicService.queryById(query.getTopicId());
		String str = "";
		if(topicDO.getStatus() == 5){
			
			//templateData.put("pic", dfsDomainUtil.getSnapshotUrl(topicDO.getImageInterested(),200));
			/*templateData.put("pic", switchBussiesConfigDao.getPictureSrc_PC(
					topicDO.getImageInterested(),200));*/
			templateData.put("pic", switchBussiesConfigDao.getPictureSrc_PC(
					topicDO.getPcInterestImage(),200));
			
			templateData.put("name", topicDO.getName());
	    	str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
					templateData,"/products_over.flt",new StringWriter());
		}else{
			Topic topic = getTopicFilterInfo(query,TempleConstant.PROMOTION_PLATFORM_PC);
	    	templateData.put("topic", topic);
	    	str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
					templateData,"/products.flt",new StringWriter());
		}
		
	    return str;
	}

	private Topic getTopicFilterInfo(TopicItemPageQuery query,int t) throws Exception {
		
		PageInfo<TopicItemInfoResult> topicItemInfoPage = topicService.queryTopicPageItemByCondition(query);
    	List<TopicItemInfoResult> topicDetailDTOList = topicItemInfoPage.getRows();
    	if(topicDetailDTOList == null || topicDetailDTOList.size()==0){
			//如果活动不存在，抛出异常
			logger.info("获取专题活动有错误，该专题活动id不存在");
//			throw new NullPointerException();
			return new Topic();
		}
    	
    	Topic topic = new Topic();
    	//设置总页码 Math.ceil(1/Double.parseDouble(8+""))
    	//topic.setAllPages((int)Math.ceil(topicItemInfoPage.getTotal()/Double.parseDouble(query.getPageSize()+"")));
    	topic.setAllPages(topicItemInfoPage.getTotal());
    	//设置当前页码
    	topic.setCurrentPages(query.getPageId());
    	/*//设置专题名称
    	if(TopicItemInfoPage.getList() != null){
    		topic.setName(((TopicItemInfoResult)TopicItemInfoPage.getList()).getTopicName());
    	}*/

    	// for dss
    	PromoterInfo promoter = null;
    	if( query.getPromoterId() != null){
    		promoter =  promoterInfoService.queryById(query.getPromoterId());    		
    	}

    	List<String> skuCodeList = new ArrayList<>();

    	for(int i=0;i<topicDetailDTOList.size();i++){
    		TopicItemInfoResult topicItemInfoResult = topicDetailDTOList.get(i);
			skuCodeList.add(topicItemInfoResult.getSku());
//    		if(TopicItemInfoResult.getTopicStatus() != TopicStatus.PASSED.ordinal()){
//    			//如果活动状态不是审核通过的，抛出异常
//    			logger.info("获取专题状态有错误，不是编辑中的活动");
//    			throw new NullPointerException();
//    		}
    		
    		Products productNormal = new Products();
    		productNormal.setTopicid(String.valueOf(query.getTopicId()));
        	productNormal.setSrclink("/detail");
        	
        	//图片地址：注意此处需要改尺寸，dfs工具代码为：getSnapshotUrl
        	if(t == TempleConstant.PROMOTION_PLATFORM_PC){
        		//productNormal.setImgsrc(dfsDomainUtil.getSnapshotUrl(topicItemInfoResult.getTopicImage(),275));
        		productNormal.setImgsrc(switchBussiesConfigDao.getPictureSrc_PC(
        				topicItemInfoResult.getTopicImage(),PictureSizeConstant.INDEX_ITEM_PICTURE));
        	}else{
        		productNormal.setImgsrc(topicItemInfoResult.getTopicImage());
        	}
        	
        	//图片链接:注意此处需要改尺寸
        	productNormal.setNamelink(splitJoinLinkAdress(topicItemInfoResult.getTopicId(),topicItemInfoResult.getSku()));
        	//品牌名称
        	productNormal.setName(topicItemInfoResult.getItemName());
        	//productNormal.setName(TopicItemInfoResult.getTopicName());
        	
        	//字符，特定设置
        	productNormal.setMoney("&yen;");
        	//活动价格
        	productNormal.setNowValue(topicItemInfoResult.getTopicPrice());
        	//原价格
        	productNormal.setLastValue(topicItemInfoResult.getSalePrice());
        	
        	//折扣价格
        	productNormal.setDiscountPrice(getPriceDiscount(topicItemInfoResult.getTopicPrice(), topicItemInfoResult.getSalePrice()));
        	
        	//已卖出数量
        	productNormal.setNowHoard(topicItemInfoResult.getSaledAmount()+"人已海淘");
        	//品牌链接
        	productNormal.setSeelink(splitJoinLinkAdress(topicItemInfoResult.getTopicId(),topicItemInfoResult.getSku()));
        	//开始时间
        	productNormal.setStartDate(topicItemInfoResult.getStartTime());
        	//结束时间
        	productNormal.setEndDate(topicItemInfoResult.getEndTime());
        	//sku
        	productNormal.setSku(topicItemInfoResult.getSku());
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
        	
        	// for dss
        	if(promoter != null){
            	ItemSku qrysku = new ItemSku();
            	qrysku.setSku( topicItemInfoResult.getSku() );
            	List<ItemSku> skuList = itemSkuService.queryByObject(qrysku);
            	if(skuList != null && !skuList.isEmpty() ){            	
            		Double commisionRate = skuList.get(0).getCommisionRate()==null ? 0.0d : skuList.get(0).getCommisionRate();
    				Double commision = promoterInfoService.getCurrentCommision2(promoter,  topicItemInfoResult.getTopicPrice(), commisionRate );
    				productNormal.setCommision(commision);
            	}        		
        	}

        	topic.getProductsList().add(productNormal);
    	}
    	//销售数量
		Map<String,Integer> salesCountMap =itemRemoteService.getSalesCountBySkuList(skuCodeList);
		for(Products products: topic.getProductsList()){
				products.setSalesCount(salesCountMap.get(products.getSku()));
		}

		return topic;
	}
	
	
	
	private Topic getTopicFilterInfoForDss(Long promoterid, TopicItemPageQuery query,int t) throws Exception {
		PageInfo<TopicItemInfoResult> topicItemInfoPage = topicService.queryTopicPageItemByCondition(query);
    	List<TopicItemInfoResult> topicDetailDTOList = topicItemInfoPage.getRows();
    	if(topicDetailDTOList == null || topicDetailDTOList.size()==0){
			//如果活动不存在，抛出异常
			logger.info("获取专题活动有错误，该专题活动id不存在");
//			throw new NullPointerException();
			return new Topic();
		}
    	
    	Topic topic = new Topic();
    	//设置总页码 Math.ceil(1/Double.parseDouble(8+""))
    	//topic.setAllPages((int)Math.ceil(topicItemInfoPage.getTotal()/Double.parseDouble(query.getPageSize()+"")));
    	topic.setAllPages(topicItemInfoPage.getTotal());
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
    		productNormal.setTopicid(String.valueOf(query.getTopicId()));
        	productNormal.setSrclink("/detail");
        	
        	//图片地址：注意此处需要改尺寸，dfs工具代码为：getSnapshotUrl
        	if(t == TempleConstant.PROMOTION_PLATFORM_PC){
        		//productNormal.setImgsrc(dfsDomainUtil.getSnapshotUrl(topicItemInfoResult.getTopicImage(),275));
        		productNormal.setImgsrc(switchBussiesConfigDao.getPictureSrc_PC(
        				topicItemInfoResult.getTopicImage(),PictureSizeConstant.INDEX_ITEM_PICTURE));
        	}else{
        		productNormal.setImgsrc(topicItemInfoResult.getTopicImage());
        	}
        	
        	//图片链接:注意此处需要改尺寸
        	productNormal.setNamelink(splitJoinLinkAdress(topicItemInfoResult.getTopicId(),topicItemInfoResult.getSku()));
        	//品牌名称
        	productNormal.setName(topicItemInfoResult.getItemName());
        	//productNormal.setName(TopicItemInfoResult.getTopicName());
        	
        	//字符，特定设置
        	productNormal.setMoney("&yen;");
        	//活动价格
        	productNormal.setNowValue(topicItemInfoResult.getTopicPrice());
        	//原价格
        	productNormal.setLastValue(topicItemInfoResult.getSalePrice());
        	
        	//折扣价格
        	productNormal.setDiscountPrice(getPriceDiscount(topicItemInfoResult.getTopicPrice(), topicItemInfoResult.getSalePrice()));
        	
        	//已卖出数量
        	productNormal.setNowHoard(topicItemInfoResult.getSaledAmount()+"人已海淘");
        	//品牌链接
        	productNormal.setSeelink(splitJoinLinkAdress(topicItemInfoResult.getTopicId(),topicItemInfoResult.getSku()));
        	//开始时间
        	productNormal.setStartDate(topicItemInfoResult.getStartTime());
        	//结束时间
        	productNormal.setEndDate(topicItemInfoResult.getEndTime());
        	//sku
        	productNormal.setSku(topicItemInfoResult.getSku());
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
        	
			// 佣金计算								
			Double commision = promoterInfoService.getCurrentCommision(promoterid, topicItemInfoResult.getSku(), topicItemInfoResult.getTopicPrice() );
			productNormal.setCommision(commision);
        	
        	topic.getProductsList().add(productNormal);
    	}
		return topic;
	}
	
	/**
     * 品牌专题-通过过滤条件去查品牌明细(APP)-即专场商品列表
     * @param 
     * @return
     * @throws Exception
     */
	@Override
	public Topic loadTopiInfocHtmlApp(AppTopItemPageQuery query)
			throws Exception {
		//首先查询条件的转化		
		TopicItemPageQuery topicQuery = new TopicItemPageQuery();
		topicQuery.setTopicId(query.getSpecialid());
		topicQuery.setBrandId(query.getBrandid());
		topicQuery.setCategoryId(query.getClassifyid());
		if("1".equals(query.getIshave())){
			topicQuery.setStock(true);
		}else{
			topicQuery.setStock(false);
		}
		if("1".equals(query.getIsascending())){
			topicQuery.setPriceOrder("asc");
		}else if("2".equals(query.getIsascending())){
			topicQuery.setPriceOrder("desc");
		}
		topicQuery.setPageId(query.getCurpage());
		topicQuery.setPageSize(query.getPageSize());
		
		if(query.getPageSize() == null || query.getPageSize()==0){
			//暂时设置页数为8
			topicQuery.setPageSize(8);
		}
		if(query.getCurpage() == null || query.getCurpage()==0){
			topicQuery.setPageId(0);
		}
		// by zhs for dss
		if( query.getPromoterId() != null){
			topicQuery.setPromoterId(query.getPromoterId());
		}		
		Topic topic = getTopicFilterInfo(topicQuery,TempleConstant.PROMOTION_PLATFORM_APP);
		
		return topic;
	}
	
	
	
	/**
     * 品牌专题-通过过滤条件去查品牌明细(APP)-即专场商品列表 for Dss
     * @param 
     * @return
     * @throws Exception
     */
	@Override
	public Topic loadTopiInfocHtmlAppForDss(AppTopItemPageQuery query)
			throws Exception {
		//首先查询条件的转化		
		TopicItemPageQuery topicQuery = new TopicItemPageQuery();
		topicQuery.setTopicId(query.getSpecialid());
		topicQuery.setBrandId(query.getBrandid());
		topicQuery.setCategoryId(query.getClassifyid());
		if("1".equals(query.getIshave())){
			topicQuery.setStock(true);
		}else{
			topicQuery.setStock(false);
		}
		if("1".equals(query.getIsascending())){
			topicQuery.setPriceOrder("asc");
		}else if("2".equals(query.getIsascending())){
			topicQuery.setPriceOrder("desc");
		}
		topicQuery.setPageId(query.getCurpage());
		topicQuery.setPageSize(query.getPageSize());
		
		if(query.getPageSize() == null || query.getPageSize()==0){
			//暂时设置页数为8
			topicQuery.setPageSize(8);
		}
		if(query.getCurpage() == null || query.getCurpage()==0){
			topicQuery.setPageId(0);
		}		
		Topic topic = getTopicFilterInfoForDss(query.getPromoterId(), topicQuery,TempleConstant.PROMOTION_PLATFORM_APP);
		
		return topic;
	}
	
	
	
	
	
	
	
	/**
     * 专场详情-通过专场id查询专场详情(APP)
     * @param 
     * @des 
     * @return
     * @throws Exception
     */
	@Override
	public TopicItemBrandCategoryDTO loadTopiInHtmlApp(long topicId) throws Exception {
		TopicItemBrandCategoryDTO t = topicService.queryTopicItemBrandAndCategoryList(topicId);
		return t;
	}
	
	
	/**
     * 品牌专题最下方的-品牌兴趣加载
     * @return
     * @throws Exception
     */
	@Override
	public String loadInterestHtml(long topicId, int size) throws Exception {
		Map<String, Object> templateData = new HashMap<String, Object>();
    	
    	List<TopicInfo> l = topicService.queryTopicRelate(topicId, size);
    	
    	List<Interest> intList =  new ArrayList<Interest>();
    	for(int i=0;i<l.size();i++){
    		TopicInfo topicInfo = l.get(i);
    		
    		Interest inter = new Interest();
    		//描述
        	inter.setDetail(topicInfo.getDiscount());
        	//图片链接
        	if(1 != topicInfo.getTopicType()){
        		inter.setImgLink(splitJoinTopicAdress(topicInfo.getTopicId())); 
        	}else{
        		inter.setImgLink(splitJoinLinkAdress(topicInfo.getTopicId(),topicInfo.getItemSku())); 
        	}
        	
        	//图片路径
        	//inter.setImgsrc(dfsDomainUtil.getSnapshotUrl(topicInfo.getImageInterested(),375));
        	/*inter.setImgsrc(switchBussiesConfigDao.getPictureSrc_PC(
        			topicInfo.getImageInterested(),PictureSizeConstant.INDEX_INTEREST_PICTURE));*/
        	inter.setImgsrc(switchBussiesConfigDao.getPictureSrc_PC(
        			topicInfo.getPcInterestImage(),PictureSizeConstant.INDEX_INTEREST_PICTURE));
        	
        	//名称
        	inter.setName(topicInfo.getName());
        	intList.add(inter);
    	}
    	
    	templateData.put("intList", intList);
    	String advance =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/interest.flt",new StringWriter());
	    return advance;
	}
	
	/**
     * 品牌Banner加载，即品牌专题最上面的一张图的加载，后来确认是从促销那边拿的一段html代码
     * @return
     * @throws Exception
     */
	@Override
	public String loadBannerHtml(Long topicId) throws Exception {
		TopicDetailDTO topicDetailDTO = topicService.getTopicDetailById(null, topicId);
    	//返回促销的描述的html代码
    	return topicDetailDTO.getTopic().getIntro();
	}
	
	/**
     * 从促销那边获取数据,促销的一个接口，通过id值查询专题信息以及专题下的商品信息
     * @param tid
     * @return
     * @throws Exception
     */
    public TopicDetailDTO getTopicDetailById(long tid) throws Exception {
    	TopicDetailDTO topicDetailDTO = topicService.getTopicDetailById(null, tid);
    	return topicDetailDTO;
    }
	
	/**
     * 把2015-01-10时间格式的字符串 转化为 1月10日 格式
     * @param tomorrow
     * @return
     */
    public static String toparseString(String tomorrow){
		String tomorrowStr = tomorrow.substring(5,tomorrow.length());
		String s = "";
		if("0".equals(tomorrowStr.substring(0, 1))){
			s = tomorrowStr.substring(1, tomorrowStr.length()).replaceAll("-", "月");
		}else{
			s = tomorrowStr.substring(0, tomorrowStr.length()).replaceAll("-", "月");
		}
		return s+"日";
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
    
    
    /**
     * 建立活动topic的链接地址:活动地址
     * @param activityId 活动id
     * @param skuId	skuid
     * @return
     */
    public String splitJoinTopicAdress(Long activityId){
    	return cmsTopicAdress.replaceAll("parames", activityId.toString());
    }
    
    /**
     * 通过国家的名称去匹配国家的img地址
     * @param countryName 
     * @param countryImg
     * @return
     */
    /*public static String getCountryImg(String countryName){
    	String str = "";
    	if(countryName != null){
    		if("日本".equals(countryName)){
    			str = TempleConstant.CMS_IMG_JAPAN;
    		}else if("英国".equals(countryName)){
    			str = TempleConstant.CMS_IMG_ENGLAND;
    		}else if("德国".equals(countryName)){
    			str = TempleConstant.CMS_IMG_GERMANY;
    		}else if("美国".equals(countryName)){
    			str = TempleConstant.CMS_IMG_AMERICA;
    		}else if("韩国".equals(countryName)){
    			str = TempleConstant.CMS_IMG_KOREA;
    		}else if("澳大利亚".equals(countryName)){
    			str = TempleConstant.CMS_IMG_AUSTRALIA;
    		}
    	}
    	return str;
    }*/

    /**
     * 通过日期，判断是否明天，后天，或者星期
     * @param countryName 
     * @param countryImg
     * @return
     */
    /*public static String getWeekName(String dateStr,Integer dateNum){
    	String str = "";
    	
    	//先判断是否为明天和后天
    	Date tomorDate = DateUtil.getDaysCounts(new Date(),1);
    	if(dateStr.equals(DateUtil.formatDateStr(tomorDate,"yyyy-MM-dd"))){
    		return "明天";
    	}
    	Date hoiuDate = DateUtil.getDaysCounts(new Date(),2);
    	if(dateStr.equals(DateUtil.formatDateStr(hoiuDate,"yyyy-MM-dd"))){
    		return "后天";
    	}
    	
    	//如果不是明天为后天就直接显示星期几
    	if(dateNum == 1){
    		str="星期天";
    	}else if(dateNum == 2){
    		str="星期一";
    	}else if(dateNum == 3){
    		str="星期二";
    	}else if(dateNum == 4){
    		str="星期三";
    	}else if(dateNum == 5){
    		str="星期四";
    	}else if(dateNum == 6){
    		str="星期五";
    	}else if(dateNum == 7){
    		str="星期六";
    	}
    	return str;
    }*/
    
    /**
     * 把日期格式2015-04-17转化为 04/19
     * @return
     */
    /*public static String getDateStr(String dateStr){
    	if(dateStr != null){
    		return dateStr.substring(5, 10).replaceAll("-", "/");
    	}else{
    		return "";
    	}
    }*/
    
    /**
	 * 调用查询图片信息，参数为传入图片类型
	 * @param type
	 * @return
	 * @throws DAOException
	 */
	private List<AdvertiseInfo> queryAdvertBase(String type) throws Exception {
		AdvertiseInfo cmsAdvertiseInfoDO = new AdvertiseInfo();
		cmsAdvertiseInfoDO.setIdent(type);
    	//查询添加当前日期
    	cmsAdvertiseInfoDO.setStartdate(new Date());
    	cmsAdvertiseInfoDO.setEnddate(new Date());
    	cmsAdvertiseInfoDO.setStatus("0");
    	List<AdvertiseInfo>  cmsAdvertiseInfoDOList = advertiseInfoDao.selectDynamic(cmsAdvertiseInfoDO);
		return cmsAdvertiseInfoDOList;
	}

	/**
	 * 上传文件：上传文件前需要判断当前目录下面是否存在该文件
	 * @param fileName 上传文件服务器的文件名，文件名有加密
	 * @param secretKey 密钥
	 * @param realFileName 实际的文件名
	 * @return
	 * @throws Exception
	 */
	public File querySingleproTemple(String fileName, String secretKey,
			String realFileName,String tempUploadPath) throws Exception {
		String newFilename = fileName.toString().substring(fileName.toString().lastIndexOf("/")+1,fileName.toString().length());
		/*File file = new File(uploadTempPath + realFileName);*/
		//下载到本地的文件名也是加密过的名字，这样就不会出现重复或者因为存在同一个文件名就不会再次下载，导致模板错误
		File file = new File(tempUploadPath + newFilename);
		//判断本地是否存在该文件，如果存在就不需要从服务器上面再次下载
		if(file.exists())    
		{    
		    return file;  
		}    
		// 密钥
		byte[] bis = dfsService.getFileBytes(fileName);
		if (null == bis) {
			throw new ItemServiceException("静态服务器上没有找到附件：附件名为：" + realFileName
					+ " 服务器上的名称为： " + fileName);
		}
		AesCipherService aes = new AesCipherService();
		FileOutputStream fileOutputStream = null;
		try {
			byte[] keys = Hex.decodeHex(secretKey.toCharArray());
			ByteSource byteSource = aes.decrypt(bis, keys);
			fileOutputStream = new FileOutputStream(file);
			IOUtils.write(byteSource.getBytes(), fileOutputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error("下载模板，文件路径找不到",e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("下载模板，流报错",e);
		} catch (DecoderException e) {
			e.printStackTrace();
			logger.error("下载模板，转码报错",e);
		}finally{
			try {
				if(fileOutputStream != null){
					fileOutputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("关闭流报错",e);
			}
			IOUtils.closeQuietly(fileOutputStream);
		}
		return file;
	}

	/**
     * 海淘首页：顶上的自定义编辑区的html代码片
     * @return
     * @throws Exception
     */
	@Override
	public String haiTaoloadDefinedBannerHtml() throws Exception {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("status", 0);
    	paramMap.put("type", TempleConstant.HAITAO_INDEX_DEFINED_INFO);
    	paramMap.put("start", 0);
    	paramMap.put("pageSize", 100);
    	List<CmsAnnounceInfoDTO> cmsAnnounceInfoDOList = announceInfoDao.selectAnnouncePageQuery(paramMap);
    	
    	if(cmsAnnounceInfoDOList != null && cmsAnnounceInfoDOList.size()>0){
    		CmsAnnounceInfoDTO cd = cmsAnnounceInfoDOList.get(0);
    		return cd.getContent();
    	}
    	
	    return "";
	}

	/**
	 * 海淘首页：今日精选，是在cms后台配置出来
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * @author szy
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String loadHaitaoDiscount(HiTaoParamSingleBusQuery params) throws Exception {
		Map<String, Object> templateData = new HashMap<String, Object>();
		List<TempleReturnData> templateList = new ArrayList<TempleReturnData>();
		CmsSingleTempleDTO cmsSingleTempleDTO = new CmsSingleTempleDTO();
		cmsSingleTempleDTO.setPlatformType(TempleConstant.CMS_PLATFORM_PC_TYPE);
		cmsSingleTempleDTO.setDr(0);
		cmsSingleTempleDTO.setStatus("0");
		cmsSingleTempleDTO.setStatus(TempleConstant.USEING_TYPE);
	   	cmsSingleTempleDTO.setType(TempleConstant.HAITAO_RUSH_SALE_TEMPLE);
	   	
	   	//查询单品团信息
	   	Map<String,Object> map = new HashMap<String,Object>();
	   	map = getSingleInfo(cmsSingleTempleDTO,0,100,TempleConstant.PROMOTION_PLATFORM_HAITAO,params.getStaticDomain());
	   	//CmsSingleTempleDTO cmode = (CmsSingleTempleDTO) map.get("tempMode");
	   	templateList = (List<TempleReturnData>) map.get("list");
	   	if(templateList == null){
	   		return "";
	   	}
	   	
	   	templateData.put("discounList", templateList);
	   	
	   	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/haitaodiscount.flt",new StringWriter());
	       return str;
	}

	/**
	 * 首页自定义编辑区域
	 */
	@Override
	public String loadIndexDefinedHtml() throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("status", 0);
    	paramMap.put("type", TempleConstant.CMS_INDEX_DEFINED_INFO);
    	paramMap.put("start", 0);
    	paramMap.put("pageSize", 100);
    	List<CmsAnnounceInfoDTO> cmsAnnounceInfoDOList = announceInfoDao.selectAnnouncePageQuery(paramMap);
    	
    	if(cmsAnnounceInfoDOList != null && cmsAnnounceInfoDOList.size()>0){
    		CmsAnnounceInfoDTO cd = cmsAnnounceInfoDOList.get(0);
    		return cd.getContent();
    	}
    	
	    return "";
	}

	/**
	 * 西客商城首页：热销榜单（PC，与首页单品团配置一致）
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * @author szy
	 */
	@Override
	public String loadIndexHotSell(ParamSingleBusTemQuery params)
			throws Exception {
		Map<String, Object> templateData = new HashMap<String, Object>();
		List<TempleReturnData> templateList = new ArrayList<TempleReturnData>();
		CmsSingleTempleDTO cmsSingleTempleDTO = new CmsSingleTempleDTO();
		cmsSingleTempleDTO.setPlatformType(TempleConstant.CMS_PLATFORM_PC_TYPE);
		cmsSingleTempleDTO.setDr(0);
		cmsSingleTempleDTO.setStatus("0");
		cmsSingleTempleDTO.setStatus(TempleConstant.USEING_TYPE);
	   	cmsSingleTempleDTO.setType(TempleConstant.CMS_INDEX_HOT_SELL);
//		CMS_SINGLE_SALE_TEMPLE
//		cmsSingleTempleDTO.setType(TempleConstant.CMS_SINGLE_SALE_TEMPLE);
	   	
	   	//查询单品团信息
	   	Map<String,Object> map = new HashMap<String,Object>();
	   	map = getSingleInfo(cmsSingleTempleDTO,0,100,TempleConstant.PROMOTION_PLATFORM_PC,"");
	   	CmsSingleTempleDTO cmode = (CmsSingleTempleDTO) map.get("tempMode");
	   	templateList = (List<TempleReturnData>) map.get("list");
	   	if(templateList == null || templateList.size()<0){
	   		return "";
	   	}
	   	
	   	List<HotSellData> HotSellDataList = new ArrayList<HotSellData>();
	   	for(int i=0,j=templateList.size();i<j;i++){
	   		TempleReturnData templeReturnData = templateList.get(i);
	   		HotSellData hotSellData = new HotSellData();
	   		
	   		hotSellData.setActityName(templeReturnData.getActityName());
	   		hotSellData.setDiscount(templeReturnData.getDiscount());//直接后台获得
	   		hotSellData.setDiscountPrice(templeReturnData.getDiscountPrice());//计算得出
	   		
	   		//hotSellData.setGoodimg(dfsDomainUtil.getSnapshotUrl(templeReturnData.getGoodimg(),100));
	   		hotSellData.setGoodimg(switchBussiesConfigDao.getFullPictureSrc_PC(
	   				templeReturnData.getGoodimg()));
	   		
	   		hotSellData.setLink(templeReturnData.getLink());
	   		hotSellData.setOlderprice(templeReturnData.getOlderprice());
	   		hotSellData.setPrice(templeReturnData.getPrice());
	   		
	   		HotSellDataList.add(hotSellData);
	   	}
	   	
	   	templateData.put("thispinlist", HotSellDataList);
	   	
	   	//把TopicDetailDTO对象拆分成json字符串
	   	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/hotsell.flt",new StringWriter());
	    return str;
	}

	/**
	 * 西客商城首页：品牌精选
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * @author szy
	 * @desc 1.先取时间,星期(1=星期天，2=星期一, ……7=星期六)
				select date_format(y.startdate, '%Y-%m-%d'),DAYOFWEEK(y.startdate) from cms_single_tepactivity y 
				where date_format(y.startdate, '%Y%m%d')>date_format(NOW(), '%Y%m%d')
				order by date_format(y.startdate, '%Y%m%d') limit 3;
			 2.第二次查询，需要查询三次，每次查询要把开始时间为上面查询到的时间通过参数传递进行查，每一个日期对应一个活动id的list集合
			 3.再把活动id的集合传入到base库中去查询，调用"com.meitun.base.service.BrandService"接口的"selectListBrandDO"方法
	 */
	@Override
	public String loadIndexTopicDiscount(ParamSingleBusTemQuery params)
			throws Exception {
		//先查询日期
		Map<String, Object> templateData = new HashMap<String, Object>();
		List<TopicDiscountData> templateList = new ArrayList<TopicDiscountData>();
		CmsSingleTempleDTO cmsSingleTempleDTO = new CmsSingleTempleDTO();
		cmsSingleTempleDTO.setPlatformType(TempleConstant.CMS_PLATFORM_PC_TYPE);
		cmsSingleTempleDTO.setStatus(TempleConstant.USEING_TYPE);
	   	cmsSingleTempleDTO.setType(TempleConstant.CMS_INDEX_TOPIC_DISCOUNT);
		List<CmsSingleTempleDTO> lst = singleproTempleDao.selectDateCountQuery(cmsSingleTempleDTO);
		
		//根据日期查询活动id
		List<TopicDiscountData> actIdsList = new ArrayList<TopicDiscountData>();
		if(lst != null && lst.size()>0){
			for(CmsSingleTempleDTO mode:lst){
				CmsSingleTempleDTO mdd = new CmsSingleTempleDTO();
				mdd.setPlatformType(TempleConstant.CMS_PLATFORM_PC_TYPE);
				mdd.setStatus(TempleConstant.USEING_TYPE);
				mdd.setType(TempleConstant.CMS_INDEX_TOPIC_DISCOUNT);
				mdd.setStartDateStr(mode.getStartDateStr());
//				mdd.setEndDateStr(mode.getStartDateStr());
				List<CmsSingleTempleDTO> mddlst = singleproTempleDao.selectDateActivityIdQuery(mdd);
			   	
				if(mddlst != null && mddlst.size()>0){
					TopicDiscountData modeTopicInfo = new TopicDiscountData();
					modeTopicInfo.setToipcSdate(CmsTempletUtil.getDateStr(mode.getStartDateStr()));
					modeTopicInfo.setWeekName(CmsTempletUtil.getWeekName(mode.getStartDateStr(),mddlst.get(0).getTotalCount()));
					modeTopicInfo.setSingleTempletList(mddlst);
					
					actIdsList.add(modeTopicInfo);
				}
			}
		}else{
			return "";
		}
		
		//根据id去查找品牌
		//Map<Long,Long> brandsMap = new HashMap<Long,Long>();
		for (TopicDiscountData topicDiscountData:actIdsList) {
			List<Long> ldList = new ArrayList<Long>();
			for(CmsSingleTempleDTO domode:topicDiscountData.getSingleTempletList()){
				ldList.add(domode.getActivityId());
			}
			
			//先从促销那边取数据，传入id列表，查到具体bean
			List<TopicDetailDTO> topicDetailDTO = topicService.getTopicDetailByIdsForCms(null, ldList);
			List<Long> ldBrandList = new ArrayList<Long>();
           	if(topicDetailDTO != null && topicDetailDTO.size()>0 ){
           		//循环促销数据，找到id值，组装成ids列表去品牌那边查询数据
           		for(TopicDetailDTO dtomd:topicDetailDTO){
           			
           			com.tp.model.mmp.Topic topicDO = dtomd.getTopic();
           			
           			if(topicDO != null){
               			ldBrandList.add(topicDO.getBrandId());
           			}
           		}
           		
           		//从基础库中查询品牌logo
    		    List<Brand> brandDOList = brandService.selectListBrand(ldBrandList, 2);
    		    
    		    List<HomePageAdData> homePageAdDataList = new ArrayList<HomePageAdData>();
           		
    		    //对应每个活动主键，找到每个品牌图片logo地址
    		    for(TopicDetailDTO dtomd:topicDetailDTO){
    		    	com.tp.model.mmp.Topic topicDO = dtomd.getTopic();
    		    	if(TopicStatus.PASSED.ordinal() == topicDO.getStatus().intValue()){
    		    		HomePageAdData homePageAdData = new HomePageAdData();
        		    	homePageAdData.setLink(splitJoinTopicAdress(topicDO.getId()));
        		    	
        		    	for(Brand brandDO:brandDOList){
        		    		if(topicDO.getBrandId() == brandDO.getId()){
        		    			//homePageAdData.setImageSrc(dfsDomainUtil.getSnapshotUrl(brandDO.getLogo(),123));
        		    			homePageAdData.setImageSrc(switchBussiesConfigDao.getPictureSrc_PC(
        		    					brandDO.getLogo(),123));
        		    		}
        		    	}
        		    	homePageAdDataList.add(homePageAdData);
    		    	}
    			}
    		    topicDiscountData.setHomePageAdDataList(homePageAdDataList);
           	}
		    
		    /*for(BrandDO brandDO:brandDOList){
		    	HomePageAdData homePageAdData = new HomePageAdData();
		    	//图片logo
		    	homePageAdData.setImageSrc(dfsDomainUtil.getSnapshotUrl(brandDO.getLogo(),123));
		    	//hotSellData.setGoodimg(dfsDomainUtil.getSnapshotUrl(templeReturnData.getGoodimg(),100));
		    	
		    	//品牌链接
		    	//homePageAdData.setLink(splitJoinTopicAdress(brandsMap.get(brandDO.getId())));
		    	homePageAdData.setLink(splitJoinTopicAdress(brandsMap.get(brandDO.getId())));
		    	
		    	homePageAdDataList.add(homePageAdData);
		    }*/
		    
		    templateList.add(topicDiscountData);
		}
		
		templateData.put("thispinlist", templateList);
	   	
	   	//把TopicDetailDTO对象拆分成json字符串
	   	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/topicdiscount.flt",new StringWriter());
	    return str;
	}

	/**
	 * 明日预告-首页：没有日期包装，一次性把今天以后的活动全部显示出来，加上分页（PC）
	 * @return html字符串的代码
	 * @throws CmsServiceException
	 * @author szy
	 */
	public String loadIndextrailerHtml(ParamSingleBusTemQuery params) throws Exception{
		Map<String, Object> templateData = new HashMap<String, Object>();
		List<AdvancePicData> templateList = new ArrayList<AdvancePicData>();
		
		CmsTopicSimpleQuery paramCmsTopicSimpleQuery = new CmsTopicSimpleQuery();
		if(params != null){
			paramCmsTopicSimpleQuery.setPageId(params.getPagestart());
	    	paramCmsTopicSimpleQuery.setPageSize(params.getPagesize());
		}else{
			paramCmsTopicSimpleQuery.setPageId(0);
	    	paramCmsTopicSimpleQuery.setPageSize(16);
		}
    	paramCmsTopicSimpleQuery.setPlatformType(TempleConstant.PROMOTION_PLATFORM_PC);
    	paramCmsTopicSimpleQuery.setForcaseType(CmsForcaseType.BY_COUNT);
    	
    	Date d = DateUtil.getDateAfterDays(1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String tomorrow = sdf.format(d);//tomorrow是日期格式的字符串，如2015-12-25
		
		paramCmsTopicSimpleQuery.setData(tomorrow);
    	
    	PageInfo<TopicDetailDTO> pagelist = topicService.getTomorrowForecast(paramCmsTopicSimpleQuery);//1是起始位置，3是大小
    	List<TopicDetailDTO> topicDetailDTOList = pagelist.getRows();
    	for(int i=0,j=topicDetailDTOList.size();i<j;i++){
    		TopicDetailDTO topicDetailDTO = new TopicDetailDTO();
    		topicDetailDTO = topicDetailDTOList.get(i);
    		
   	    	com.tp.model.mmp.Topic topicDO = topicDetailDTO.getTopic();
   	    	
   	    	AdvancePicData a1 = new AdvancePicData();
        	
   	    	if(topicDetailDTO.getPromotionItemList() != null 
        			&& topicDetailDTO.getPromotionItemList().size()>0){
        		TopicItem topicItemDO = topicDetailDTO.getPromotionItemList().get(0);
        		//图片链接
        		if(TopicType.SINGLE.ordinal() != topicDO.getType()){
        			a1.setLink(splitJoinTopicAdress(topicDO.getId())); 
            	}else{
            		a1.setLink(splitJoinLinkAdress(topicDO.getId(),topicItemDO.getSku())); 
            	}
        		
        		//图片路径
    			//a1.setImageSrc(dfsDomainUtil.getSnapshotUrl(topicDO.getImageNew(),378));
        		/*a1.setImageSrc(switchBussiesConfigDao.getPictureSrc_PC(
        				topicDO.getImageNew(),PictureSizeConstant.INDEX_TRAILER_ADVANCE));*/
        		a1.setImageSrc(switchBussiesConfigDao.getPictureSrc_PC(
        				topicDO.getPcInterestImage(),PictureSizeConstant.INDEX_TRAILER_ADVANCE));
        		
            	//专场名称
            	//a1.setName(topicItemDO.getName());
        		a1.setName(topicDO.getName());
            	//专场id
            	a1.setActivityId(topicDO.getId()); 
            	//专场开始时间
            	a1.setStartdate(topicDO.getStartTime()); 
            	//专场结束时间
            	a1.setEnddate(topicDO.getEndTime()); 
            	//折扣
            	a1.setRate(topicDO.getDiscount()); 
            	//长期活动
            	a1.setLastType(topicDO.getLastingType()); 
            	
            	//sku
            	a1.setSkuCode(topicItemDO.getSku());
            	
            	
            	templateList.add(a1);
   	    	}
    	}
    	
    	templateData.put("advanceList", templateList);
    	templateData.put("page", params.getPagestart().toString());
    	
    	
	  	String str =FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/indextrailer.flt",new StringWriter());
		return str;
	}
	
	/**
	 * 明日预告-首页：没有日期包装，一次性把今天以后的活动全部显示出来，加上分页（APP）
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Override
	public PageInfo<AppSingleInfoDTO> loadAPPIndextrailerHtml(ParamSingleBusTemQuery params)
			throws Exception {
		List<AppSingleInfoDTO> templateList = new ArrayList<AppSingleInfoDTO>();
		
		CmsTopicSimpleQuery paramCmsTopicSimpleQuery = new CmsTopicSimpleQuery();
		if(params != null){
			paramCmsTopicSimpleQuery.setPageId(params.getPagestart());
	    	paramCmsTopicSimpleQuery.setPageSize(params.getPagesize());
		}else{
			paramCmsTopicSimpleQuery.setPageId(0);
	    	paramCmsTopicSimpleQuery.setPageSize(16);
		}
    	paramCmsTopicSimpleQuery.setPlatformType(TempleConstant.PROMOTION_PLATFORM_APP);
    	paramCmsTopicSimpleQuery.setForcaseType(CmsForcaseType.BY_COUNT);
    	
    	Date d = com.tp.util.DateUtil.getDateAfterDays(1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String tomorrow = sdf.format(d);//tomorrow是日期格式的字符串，如2015-12-25
		
		paramCmsTopicSimpleQuery.setData(tomorrow);
    	
    	PageInfo<TopicDetailDTO> pagelist = topicService.getTomorrowForecast(paramCmsTopicSimpleQuery);//1是起始位置，3是大小
    	List<TopicDetailDTO> topicDetailDTOList = pagelist.getRows();
    	for(int i=0,j=topicDetailDTOList.size();i<j;i++){
    		TopicDetailDTO topicDetailDTO = new TopicDetailDTO();
    		topicDetailDTO = topicDetailDTOList.get(i);
    		
   	    	com.tp.model.mmp.Topic topicDO = topicDetailDTO.getTopic();
   	    	
   	    	AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
        	
   	    	if(topicDetailDTO.getPromotionItemList() != null 
        			&& topicDetailDTO.getPromotionItemList().size()>0){
        		//TopicItemDO topicItemDO = topicDetailDTO.getPromotionItemList().get(0);
        		
        		//折扣
        		appSingleInfoDTO.setDiscount(topicDO.getDiscount()); 
        		//开始时间
        		if(topicDO.getStartTime() != null){
        			appSingleInfoDTO.setStartTime(topicDO.getStartTime().getTime());
        		}
        		//结束时间
        		if(topicDO.getEndTime() != null){
        			appSingleInfoDTO.setEndTime(topicDO.getEndTime().getTime());
        		}
        		//图片路径
        		appSingleInfoDTO.setImageurl(topicDO.getImageMobile());
        		appSingleInfoDTO.setMobileImage(topicDO.getMobileImage());
        		//专场名称
        		appSingleInfoDTO.setName(topicDO.getName());
        		//活动id
        		appSingleInfoDTO.setSpecialid(topicDO.getId());
        		//是否已关注
//        		appSingleInfoDTO.setAttentionStatus(amode.getAttentionStatus());
        		
            	templateList.add(appSingleInfoDTO);
   	    	}
    	}
    	
    	PageInfo<AppSingleInfoDTO>  page = new PageInfo<AppSingleInfoDTO>();
    	
    	page.setRecords(templateList.size());
    	page.setRows(templateList);
    	page.setPage(paramCmsTopicSimpleQuery.getPageId());
    	page.setSize(paramCmsTopicSimpleQuery.getPageSize());
    	
	    return page;
	}

	/**
	 * 取地址信息-首页新版本
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> loadAddrMessageHtml(List<DistrictInfo> addr,List<ChinaRegionInformation> addrFloat) throws Exception{
		Map<String, Object> templateData = new HashMap<String, Object>();
    	templateData.put("addr", addr);
    	Map<String, Object> templateDataFloat = new HashMap<String, Object>();
    	templateDataFloat.put("addr", addrFloat);
    	String advance = FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/map.flt",new StringWriter());
    	String advanceFloat = FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
    			templateDataFloat,"/mapfloat.flt",new StringWriter());
    	Map<String,Object> pageMap = new HashMap<String,Object>();
    	pageMap.put("addr", advance);
    	pageMap.put("addrFloat", advanceFloat);
    	return pageMap;
	}
	
    public Map<String,Object> loadShoppingHtml(Long uid,String salesorderDomain,String addr,String itemurl, String countdown,String countdownDatte) throws Exception{
    	Map<String, Object> templateData = new HashMap<String, Object>();
    	CartDTO shopping = new CartDTO();
    	if(uid != null){
	    	
    		shopping =  cartLocalService.findMemberCart(uid,1); //1为西客商城 2为海淘
    		
    		//合并海淘和西客商城的购物车合并
    		CartDTO shoppingHaitao =  cartLocalService.findMemberCart(uid,2);
    		if(null != shoppingHaitao.getSeaMap()){
    			shoppingHaitao.setLineList(new ArrayList<CartLineDTO>());
    			for(String key : shoppingHaitao.getSeaMap().keySet()){
    				shoppingHaitao.getLineList().addAll(shoppingHaitao.getSeaMap().get(key));
    			}
    		}
    		if(null == shopping.getLineList() && shoppingHaitao.getLineList() != null){
    			shopping = shoppingHaitao;
    		}else if(null != shoppingHaitao.getLineList() && null != shopping.getLineList()){
    			int hCount = shoppingHaitao.getQuantityAll() == null ? 0 : shoppingHaitao.getQuantityAll();
    			int mCount = shopping.getQuantityAll() == null ? 0 : shopping.getQuantityAll();
    			shopping.setQuantityAll(hCount + mCount);
    			
    			double  hRealSumAll = shoppingHaitao.getRealSumAll() == null ? 0 : shoppingHaitao.getRealSumAll();
    			double  mRealSumAll = shopping.getRealSumAll() == null ? 0 : shopping.getRealSumAll();
    			shopping.setRealFee(hRealSumAll + mRealSumAll);
    			
    			if(!shoppingHaitao.getLineList().isEmpty()){
    				shopping.getLineList().addAll(shoppingHaitao.getLineList());
    			}
    		}
    		
    	}
    	if(shopping != null&& shopping.getLineList()!= null&&shopping.getLineList().size()>0){
	    	for(CartLineDTO clt:shopping.getLineList()){
	    		//clt.setItemPic(dfsDomainUtil.getSnapshotUrl(clt.getItemPic(), 60));
	    		clt.setItemPic(switchBussiesConfigDao.getPictureSrc_PC(clt.getItemPic(), 60));
	    	}
    	}
    	templateData.put("shopping", shopping);
    	templateData.put("salesorderDomain",salesorderDomain);
    	templateData.put("addr",addr);
    	templateData.put("uid",uid);
    	templateData.put("itemurl",itemurl);
    	templateData.put("countdown",countdown);
    	templateData.put("countdownDatte",countdownDatte);
    	String advance = FreeMarkerUtils.processTemplateString(FreeMarkerUtils.getDirectoryFMCFG(TempleConstant.CMS_TEMPLE_PATH), 
				templateData,"/Shopping.flt",new StringWriter());

    	Map<String, Object> shoppingMap = new HashMap<String, Object>();
    	shoppingMap.put("shopping", advance);
    	shoppingMap.put("shoppingsize", shopping.getQuantityAll()==null?"0":shopping.getQuantityAll());
    	return shoppingMap;
    }
}
