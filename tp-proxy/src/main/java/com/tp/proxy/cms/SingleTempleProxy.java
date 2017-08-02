package com.tp.proxy.cms;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.cms.TempleConstant;
import com.tp.dto.cms.CmsSingleTempleDTO;
import com.tp.dto.cms.CmsSingleTepactivityDTO;
import com.tp.dto.cms.app.query.AppTopItemPageQuery;
import com.tp.dto.cms.query.ParamSingleBusTemQuery;
import com.tp.dto.cms.temple.Products;
import com.tp.dto.cms.temple.Topic;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.TopicDetailDTO;
import com.tp.exception.ServiceException;
import com.tp.model.cms.ImportLog;
import com.tp.model.cms.SingleTepactivity;
import com.tp.proxy.BaseProxy;
import com.tp.query.mmp.CmsTopicQuery;
import com.tp.service.IBaseService;
import com.tp.service.cms.ISingleBusTemService;
import com.tp.service.cms.ISingleTempleService;
import com.tp.service.mmp.ITopicService;
import com.tp.util.DateUtil;
/**
 * 活动元素表代理层
 * @author szy
 *
 */
@Service
public class SingleTempleProxy extends BaseProxy{

	@Autowired
	private ISingleBusTemService singleBusTemService;
	@Autowired
	private ISingleTempleService singleTempleService;
	@Autowired
	private ITopicService topicService;
	
	@Override
	public IBaseService getService() {
		return null;
	}
	public ISingleBusTemService getISingleBusTemService(){
		return singleBusTemService;
	}

	public void singleIndexDiscountInfo(ParamSingleBusTemQuery paramSingleBusTemQuery) {
		try {
			singleBusTemService.singleIndexDiscountInfo(paramSingleBusTemQuery);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * APP今日上线
	 * @param memberId
	 * @return
	 */
	public ResultInfo<PageInfo<Products>> loadTopiInfocHtmlApp(AppTopItemPageQuery query) {
		try {
			Topic t = singleBusTemService.loadTopiInfocHtmlApp( query);
			PageInfo<Products> pages = new PageInfo<>();
	    	pages.setPage(t.getCurrentPages());
	    	pages.setTotal(t.getAllPages());
	    	pages.setRows(t.getProductsList());
			return new ResultInfo<>(pages);
		}catch(ServiceException ex){
			logger.error("[APP接口 - 专场商品列表   ServiceException] = "+ex.getMessage());
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Exception ex){
			logger.error("[APP接口 - 专场商品列表   Exception] = {}",ex);
			return new ResultInfo<>(new FailInfo("查询数据失败"));
		}
	}

	/**
	 * 查询模板数据
	 * @param jSONObject
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public JSONArray querysingleTempleLst(JSONObject jSONObject) throws Exception {
		CmsSingleTempleDTO cmsSingleTempleDTO = new CmsSingleTempleDTO();
		if(jSONObject.get("templeName") != null){
			cmsSingleTempleDTO.setTempleName(jSONObject.get("templeName").toString());
		}
		if(jSONObject.get("positionName") != null){
			cmsSingleTempleDTO.setPositionName(jSONObject.get("positionName").toString());
		}
		if(jSONObject.get("status") != null){
			cmsSingleTempleDTO.setStatus(jSONObject.get("status").toString());
		}
		if(jSONObject.get("platformType") != null){
			cmsSingleTempleDTO.setPlatformType(jSONObject.get("platformType").toString());
		}
		if(jSONObject.get("type") != null){
			cmsSingleTempleDTO.setType(jSONObject.get("type").toString());
		}
		
		int pageId = Integer.parseInt(TempleConstant.CMS_START_PAGE);
    	int pageSize = Integer.parseInt(TempleConstant.CMS_PAGE_SIZE);
    	if(jSONObject.get("pageId") != null && !"".equals(jSONObject.get("pageId"))){
    		pageId = Integer.parseInt(jSONObject.get("pageId").toString());
    	}
    	if(jSONObject.get("pageSize") != null && !"".equals(jSONObject.get("pageSize"))){
    		pageSize = Integer.parseInt(jSONObject.get("pageSize").toString());
    	}
    	cmsSingleTempleDTO.setStartPage(pageId);
    	cmsSingleTempleDTO.setPageSize(pageSize);
    	
    	List<CmsSingleTempleDTO> list = singleTempleService.querySingleproTemple(cmsSingleTempleDTO);
    	JSONArray mapList = new JSONArray();
    	for(int i=0;i<list.size();i++){
    		mapList.add(list.get(i));
    	}
		return mapList;
	}

	public int delsingleTempleByIds(List<Long> ids) throws Exception{
		return singleTempleService.delSingleproTempleByIds(ids);
	}
	public List<CmsSingleTempleDTO> querysingleTempleList(CmsSingleTempleDTO cmsSingleTempleDTO) throws Exception{
		return singleTempleService.querySingleproTemple(cmsSingleTempleDTO);
	}
	
	/**
	 * 单品团模板添加活动节点
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<CmsSingleTepactivityDTO> addSingleNode(Long id) throws Exception {
		//通过node表的主键id，查询其子表的所有id，再调促销取数据
		SingleTepactivity cmsSingleTempleDTO = new SingleTepactivity();
		cmsSingleTempleDTO.setSingleTepnodeId(id);
//		List<CmsSingleTempleDTO> list = singleTempleAO.querysingleTempleList(cmsSingleTempleDTO);
		List<SingleTepactivity> list = singleTempleService.selectActityIDs(cmsSingleTempleDTO);
		
		//通过id集合，去促销接口中取专题信息集合
		List<Long> ids = new ArrayList<Long>();
		for(int i=0;i<list.size();i++){
			if(list.get(i).getActivityId() != null){
				ids.add(list.get(i).getActivityId());
			}
		}
		List<TopicDetailDTO> toplist = topicService.queryTopicDetailList(ids);
		
		List<CmsSingleTepactivityDTO> singleTepactivityDOList = new ArrayList<CmsSingleTepactivityDTO>();
		for(int i=0;i<toplist.size();i++){
			TopicDetailDTO topicDetailDTO = toplist.get(i);
			CmsSingleTepactivityDTO singleTepactivityDO = new CmsSingleTepactivityDTO();
			/** 专题id */
			singleTepactivityDO.setId(topicDetailDTO.getTopic().getId());
			/** 模板节点表id */
			singleTepactivityDO.setSingleTepnodeId(id);
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
			
			/** 开始时间 */
			singleTepactivityDO.setStartdate(topicDetailDTO.getTopic().getStartTime());
			/** 结束时间 */
			singleTepactivityDO.setEnddate(topicDetailDTO.getTopic().getEndTime());
			
			/** 
			 * 状态：1.当前日期在范围内的为进行中;
			 * 		2.当前日期不在范围内的，其已经过期的为已过期;
			 * 		3.当前日期不在范围内的，且还未开始的为未开始;
			 */
			/*Date startTime = topicDetailDTO.getTopic().getStartTime();
			Date endTime = topicDetailDTO.getTopic().getEndTime();
			Date currentDate = new Date();
			if(endTime == null){
				endTime = DateUtil.getDaysCounts(currentDate, 365);
			}
			if(startTime == null){
				startTime = DateUtil.getDaysCounts(currentDate, -365);
			}
			if(currentDate.getTime() >= endTime.getTime()){
				singleTepactivityDO.setFlagStr("已过期");
				singleTepactivityDO.setFlagStrNum(20);
			}else if(currentDate.getTime() < startTime.getTime() ){
				singleTepactivityDO.setFlagStr("未开始");
				singleTepactivityDO.setFlagStrNum(10);
			}else {
				singleTepactivityDO.setFlagStr("进行中");
				singleTepactivityDO.setFlagStrNum(1);
			}*/
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
		Collections.sort(singleTepactivityDOList, new Comparator<CmsSingleTepactivityDTO>(){          
	        public int compare(CmsSingleTepactivityDTO o1, CmsSingleTepactivityDTO o2) {  
	            return (int) (o1.getFlagStrNum()-o2.getFlagStrNum());
	        }  
	    });
		return singleTepactivityDOList;
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
	 * 单品团模板的保存
	 * @param params
	 * @param counts
	 * @param cmsSingleTempleDTO
	 * @return
	 * @throws Exception
	 */
	public int insertSingleTemple(String params, int counts,
			CmsSingleTempleDTO cmsSingleTempleDTO) throws Exception {
		
		return singleTempleService.insertSingleTemple(params, counts, cmsSingleTempleDTO);
	}

	public int delActivityByID(CmsSingleTepactivityDTO cmsSingleTepactivityDTO) throws Exception{
		return singleTempleService.delActivityByID(cmsSingleTepactivityDTO);
	}
	
	public int insertActivityId(CmsSingleTepactivityDTO cmsSingleTepactivityDTO) throws Exception{
		return singleTempleService.insertActivityId(cmsSingleTepactivityDTO);
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
		if(jSONObject.get("activityInputId") != null && 
				!"".equals(jSONObject.get("activityInputId"))){
			query.setTopicId(Integer.parseInt(jSONObject.get("activityInputId").toString()));
		}
		if(jSONObject.get("activityInputName") != null && 
				!"".equals(jSONObject.get("activityInputName"))){
			query.setName(jSONObject.get("activityInputName").toString());
		}
		if(jSONObject.get("activityInputCode") != null && 
				!"".equals(jSONObject.get("activityInputCode"))){
			query.setSku(jSONObject.get("activityInputCode").toString());
		}
		if(jSONObject.get("activityInputSd") != null && 
				!"".equals(jSONObject.get("activityInputSd"))){
			query.setStartTime(DateUtil.parse(jSONObject.get("activityInputSd").toString(), DateUtil.NEW_FORMAT));
		}
		if(jSONObject.get("activityInputed") != null && 
				!"".equals(jSONObject.get("activityInputed"))){
			query.setEndTime(DateUtil.parse(jSONObject.get("activityInputed").toString(), DateUtil.NEW_FORMAT));
		}
		if(jSONObject.get("platformType") != null && 
				!"".equals(jSONObject.get("platformType"))){
			query.setPlatformType(Integer.valueOf(jSONObject.get("platformType").toString()));
		}
		
		query.setPageId(1);
		query.setPageSize(100);
		
		//标记活动类型:salesPartten
		if(jSONObject.get("salesPartten") != null && 
				!"".equals(jSONObject.get("salesPartten"))){
			query.setSalesPartten(Integer.valueOf(jSONObject.get("salesPartten").toString()));
		}
		if(jSONObject.get("type") != null && 
				!"".equals(jSONObject.get("type"))){
			query.setTopicType(Integer.parseInt(jSONObject.get("type").toString()));
		}
		
		PageInfo<TopicDetailDTO> toplistPage = topicService.getCmsTopicList(query);
		List<TopicDetailDTO> toplist = toplistPage.getRows();
		
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
			
			singleTepactivityDOList.add(singleTepactivityDO);
		}
		
    	JSONArray mapList = new JSONArray();
    	for(int i=0;i<singleTepactivityDOList.size();i++){
    		mapList.add(singleTepactivityDOList.get(i));
    	}
		return mapList;
	}
}
