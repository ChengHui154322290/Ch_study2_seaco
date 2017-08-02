package com.tp.proxy.cms;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.dfsutils.file.ImgFile;
import com.tp.dfsutils.util.DfsDomainUtil;
import com.tp.dto.cms.CmsActivityElementDTO;
import com.tp.dto.mmp.TopicDetailDTO;
import com.tp.model.cms.ActivityElement;
import com.tp.model.cms.ImportLog;
import com.tp.model.cms.SingleTepactivity;
import com.tp.proxy.BaseProxy;
import com.tp.query.mmp.CmsTopicQuery;
import com.tp.service.IBaseService;
import com.tp.service.cms.IActivityElementService;
import com.tp.service.cms.ICommUtilService;
import com.tp.service.cms.ISingleTempleService;
import com.tp.service.mmp.ITopicService;
import com.tp.util.DateUtil;
/**
 * 活动元素表代理层
 * @author szy
 *
 */
@Service
public class ActivityElementProxy extends BaseProxy<ActivityElement>{

	@Autowired
	private IActivityElementService activityElementService;
	@Autowired
	private ISingleTempleService singleTempleService;
	
	
	@Autowired
	private ICommUtilService commUtilService;
	
	@Autowired
	private ITopicService topicService;
	
	@Override
	public IBaseService<ActivityElement> getService() {
		return activityElementService;
	}
	
	/**
	 * 删除活动元素
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public int delActivityByID(Long id) throws Exception{
		return activityElementService.deleteById(id);
	}
	
	/**
	 * 增加活动元素
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public Long insertActivityId(ActivityElement cmsActivityElementDO) throws Exception{
		cmsActivityElementDO = activityElementService.insert(cmsActivityElementDO);
		if(cmsActivityElementDO.getId() == null)
			return 0L;
		return cmsActivityElementDO.getId();
	}
	
	/**
	 * 修改活动元素
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public Integer updateActivity(ActivityElement cmsActivityElementDO) throws Exception{
		return activityElementService.update(cmsActivityElementDO, false);
	}
	
	public List<String> uploadFile(List<String> pictures){
		if(CollectionUtils.isEmpty(pictures)){
			return null;
		}
		List<String> resultPaths = new ArrayList<String>();
		
		for (String path : pictures) {
			File file=new File(path);
			if(!file.exists()){
				continue;
			}
			ImgFile info=new ImgFile();
			info.setFile(file);
			String fileid=commUtilService.uploadFile(info);
			try {
				info.clear();//删除本地缓存文件
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultPaths.add(fileid);
		}
		
		return resultPaths;
	}
	
	/**
	 * 通过模板的查询条件去促销那边拿数据
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public PageInfo<TopicDetailDTO> getCmsTopicList(CmsTopicQuery query) throws Exception{
		return topicService.getCmsTopicList(query);
	}
	
	/**
	 * 保存上传模板日志
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public Long saveImportTempleLog(ImportLog importTempleLogDO) throws Exception{
		return singleTempleService.saveImportTempleLog(importTempleLogDO);
	}
	
	/**
	 * 查询促销的数据
	 * @param query
	 * @param jSONObject
	 * @return
	 * @throws ParseException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public JSONArray queryTopicLst(CmsTopicQuery query, JSONObject jSONObject)
			throws ParseException, Exception {
		if(!StringUtils.isEmpty(jSONObject.get("activityInputId"))){
			query.setTopicId(Integer.parseInt(jSONObject.get("activityInputId").toString()));
		}
		if(!StringUtils.isEmpty(jSONObject.get("activityInputName"))){
			query.setName(jSONObject.get("activityInputName").toString());
		}
		if(!StringUtils.isEmpty(jSONObject.get("activityInputCode"))){
			query.setSku(jSONObject.get("activityInputCode").toString());
		}
		if(!StringUtils.isEmpty(jSONObject.get("activityInputSd"))){
			query.setStartTime(DateUtil.parse(jSONObject.get("activityInputSd").toString(), DateUtil.NEW_FORMAT));
		}
		if(!StringUtils.isEmpty(jSONObject.get("activityInputed"))){
			query.setEndTime(DateUtil.parse(jSONObject.get("activityInputed").toString(), DateUtil.NEW_FORMAT));
		}
		if(!StringUtils.isEmpty(jSONObject.get("platformType"))){
			query.setPlatformType(Integer.valueOf(jSONObject.get("platformType").toString()));
		}
		/*if(jSONObject.get("salesPartten") != null && 
				!"".equals(jSONObject.get("salesPartten"))){
			query.set(Integer.valueOf(jSONObject.get("salesPartten").toString()));
		}*/
		if(!StringUtils.isEmpty(jSONObject.get("pageId"))){
			query.setPageId(Integer.valueOf(jSONObject.get("pageId").toString()));
		}
		if(!StringUtils.isEmpty(jSONObject.get("pageSize"))){
			query.setPageSize(Integer.valueOf(jSONObject.get("pageSize").toString()));
		}
		/*query.setPageId(1);
		query.setPageSize(100);*/
		
		//标记活动类型:salesPartten
		if(!StringUtils.isEmpty(jSONObject.get("salesPartten"))){
			query.setSalesPartten(Integer.valueOf(jSONObject.get("salesPartten").toString()));
		}
		/*query.setSalesPartten(SalesPartten.FLAGSHIP_STORE.getValue());*/
		
		if(!StringUtils.isEmpty(jSONObject.get("type"))){
			query.setTopicType(Integer.parseInt(jSONObject.get("type").toString()));
		}
		
		PageInfo<TopicDetailDTO> toplistPage = topicService.getCmsTopicList(query);
		
		List<TopicDetailDTO> toplist = toplistPage.getRows();
		
		int countnum = (int)Math.ceil(toplistPage.getTotal()/Double.parseDouble(query.getPageSize()+""));
		
		List<SingleTepactivity> singleTepactivityDOList = new ArrayList<SingleTepactivity>();
		for(int i=0;i<toplist.size();i++){
			TopicDetailDTO topicDetailDTO = toplist.get(i);
			SingleTepactivity singleTepactivityDO = new SingleTepactivity();
			/** 专题id */
			singleTepactivityDO.setId(topicDetailDTO.getTopic().getId());
			/** 专题编号 */
			singleTepactivityDO.setActivityCode(topicDetailDTO.getTopic().getId().toString());
			/** 专题名称 */
			singleTepactivityDO.setActivityName(topicDetailDTO.getTopic().getName());
			if(topicDetailDTO.getPromotionItemList()!=null && topicDetailDTO.getPromotionItemList().size()>0){
				/** SKU编号 */
				singleTepactivityDO.setSkuCode(topicDetailDTO.getPromotionItemList().get(0).getSku());
				/** 商品名称 */
				singleTepactivityDO.setGoodsName(topicDetailDTO.getPromotionItemList().get(0).getName());
				/** 商家 */
				singleTepactivityDO.setSeller(topicDetailDTO.getPromotionItemList().get(0).getSupplierName());
				/** 规格参数 */
				String str = topicDetailDTO.getPromotionItemList().get(0).getItemSpec();
				if(str != null && !"".equals(str)){
					singleTepactivityDO.setStandardParams(str.replaceAll("<br>", ","));
				}
				/** 限购总量 */
				singleTepactivityDO.setLimitTotal(topicDetailDTO.getPromotionItemList().get(0).getLimitTotal());
				/** 限购数量 */
				singleTepactivityDO.setLimitNumber(topicDetailDTO.getPromotionItemList().get(0).getLimitAmount());
				/** 活动价 */
				singleTepactivityDO.setSellingPrice(topicDetailDTO.getPromotionItemList().get(0).getTopicPrice());
			}
			//下面是对时间格式化
			/** 开始时间 */
			if(topicDetailDTO.getTopic().getStartTime()!=null){
				String startdateStr = DateUtil.formatDateTime(topicDetailDTO.getTopic().getStartTime());
				singleTepactivityDO.setStartdate(topicDetailDTO.getTopic().getStartTime());
				singleTepactivityDO.setStartdateStr(startdateStr);
			}
			
			/** 结束时间 */
			if(topicDetailDTO.getTopic().getEndTime()!=null){
				String enddateStr = DateUtil.formatDateTime(topicDetailDTO.getTopic().getEndTime());
				singleTepactivityDO.setEnddate(topicDetailDTO.getTopic().getEndTime());
				singleTepactivityDO.setEnddateStr(enddateStr);
			}
			
			singleTepactivityDO.setStartPage(query.getPageId());
			singleTepactivityDO.setPageSize(query.getPageSize());
			singleTepactivityDO.setTotalCount(countnum);
			singleTepactivityDO.setTotalCountNum(toplistPage.getTotal());
    		
			singleTepactivityDOList.add(singleTepactivityDO);
		}
		
    	JSONArray mapList = new JSONArray();
    	for(int i=0;i<singleTepactivityDOList.size();i++){
    		mapList.add(singleTepactivityDOList.get(i));
    	}
		return mapList;
	}
	
	/**
	 * 单击位置编辑，跳转到活动元素列表中
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<CmsActivityElementDTO> addActivityElement(Long id) throws Exception {
		//通过node表的主键id，查询其子表的所有id，再调促销取数据
		/*SingleTepactivity cmsSingleTempleDTO = new SingleTepactivity();
		cmsSingleTempleDTO.setSingleTepnodeId(id);
		List<SingleTepactivity> list = singleTempleService.selectActityIDs(cmsSingleTempleDTO);*/
		
		ActivityElement cmsActivityElementDO = new ActivityElement();
		cmsActivityElementDO.setPositionId(id);
		PageInfo<ActivityElement> pageList = activityElementService.
				queryPageListByCmsActivityElementDOAndStartPageSize(cmsActivityElementDO, 1, 99999);
		
		List<ActivityElement> list = pageList.getRows();
		
		Map<Long, ActivityElement> activityElementMap = new HashMap<Long, ActivityElement>();
		
		//通过id集合，去促销接口中取专题信息集合
		List<Long> ids = new ArrayList<Long>();
		for(int i=0;i<list.size();i++){
			if(list.get(i).getActivityId() != null){
				activityElementMap.put(list.get(i).getActivityId(), list.get(i));
				ids.add(list.get(i).getActivityId());
			}
		}
		List<TopicDetailDTO> toplist = topicService.queryTopicDetailList(ids);
		
		List<CmsActivityElementDTO> singleTepactivityDOList = new ArrayList<CmsActivityElementDTO>();
		for(int i=0;i<toplist.size();i++){
			TopicDetailDTO topicDetailDTO = toplist.get(i);
			CmsActivityElementDTO singleTepactivityDO = new CmsActivityElementDTO();
			/** 位置表id */
			singleTepactivityDO.setPositionId(id);
			/** 专题编号 */
			singleTepactivityDO.setActivityCode(topicDetailDTO.getTopic().getId().toString());
			/** 专题名称 */
			singleTepactivityDO.setActivityName(topicDetailDTO.getTopic().getName());
			
			if(topicDetailDTO.getPromotionItemList()!=null && topicDetailDTO.getPromotionItemList().size()>0){
				/** SKU编号 */
				singleTepactivityDO.setSkuCode(topicDetailDTO.getPromotionItemList().get(0).getSku());
				/** 商品名称 */
				singleTepactivityDO.setGoodsName(topicDetailDTO.getPromotionItemList().get(0).getName());
				/** 商家 */
				singleTepactivityDO.setSeller(topicDetailDTO.getPromotionItemList().get(0).getSupplierId().toString());
				/** 规格参数 */
				singleTepactivityDO.setStandardParams(topicDetailDTO.getPromotionItemList().get(0).getItemSpec());
				/** 限购总量 */
				singleTepactivityDO.setLimitTotal(topicDetailDTO.getPromotionItemList().get(0).getLimitTotal());
				/** 限购数量 */
				singleTepactivityDO.setLimitNumber(topicDetailDTO.getPromotionItemList().get(0).getLimitAmount());
				/** 活动价 */
				singleTepactivityDO.setSellingPrice(topicDetailDTO.getPromotionItemList().get(0).getTopicPrice());
				
			}
			
			/** 专题开始时间(促销数据) */
			singleTepactivityDO.setActStartdate(topicDetailDTO.getTopic().getStartTime());
			/** 专题结束时间 (促销数据)*/
			singleTepactivityDO.setActEnddate(topicDetailDTO.getTopic().getEndTime());
			
			ActivityElement cmode = activityElementMap.get(topicDetailDTO.getTopic().getId());
			/** 开始时间 */
			singleTepactivityDO.setStartdate(cmode.getStartdate());
			/** 结束时间 */
			singleTepactivityDO.setEnddate(cmode.getEnddate());
			/** 展示图片 */
			singleTepactivityDO.setPicture(cmode.getPicture());
			//singleTepactivityDO.setPicture(dfsDomainUtil.getSnapshotUrl(cmode.getPicture(),100));
			singleTepactivityDO.setPictureStr(Constant.IMAGE_URL_TYPE.cmsimg.url+cmode.getPicture());
			/** 当前状态*/
			singleTepactivityDO.setStatus(cmode.getStatus());
			/** 活动元素表id */
			singleTepactivityDO.setId(cmode.getId());
			singleTepactivityDO.setLink(cmode.getLink());
			singleTepactivityDO.setSeq(cmode.getSeq());
			
			/** 
			 * 状态：1.当前日期在范围内的为进行中;
			 * 		2.当前日期不在范围内的，其已经过期的为已过期;
			 * 		3.当前日期不在范围内的，且还未开始的为未开始;
			 */
			if(topicDetailDTO.getTopic().getProgress() == 0){
				singleTepactivityDO.setFlagStr("未开始");
				singleTepactivityDO.setFlagStrNum(10);
			}else if(topicDetailDTO.getTopic().getProgress() == 1){
				singleTepactivityDO.setFlagStr("进行中");
				singleTepactivityDO.setFlagStrNum(1);
			}else{
				singleTepactivityDO.setFlagStr("已结束");
				singleTepactivityDO.setFlagStrNum(20);
			}
			
			singleTepactivityDOList.add(singleTepactivityDO);
		}
		
		//对集合singleTepactivityDOList进行排序，按进行中——未开始——已过期
		Collections.sort(singleTepactivityDOList, new Comparator<CmsActivityElementDTO>(){          
	        public int compare(CmsActivityElementDTO o1, CmsActivityElementDTO o2) {  
	            return (int) (o1.getFlagStrNum()-o2.getFlagStrNum());
	        }  
	    });
		return singleTepactivityDOList;
	}
}
