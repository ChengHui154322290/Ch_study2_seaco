package com.tp.proxy.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tp.common.vo.cms.TempleConstant;
import com.tp.dto.cms.CmsAnnounceInfoDTO;
import com.tp.model.bse.ForbiddenWords;
import com.tp.model.cms.AnnounceInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IForbiddenWordsService;
import com.tp.service.cms.IAnnounceElementService;
import com.tp.service.cms.IAnnounceService;
/**
 * 公告元素表代理层
 * @author szy
 *
 */
@Service
public class AnnounceProxy extends BaseProxy{

	@Autowired
	private IAnnounceElementService announceElementService;
	@Autowired
	private IAnnounceService announceService;

	@Override
	public IBaseService getService() {
		return announceElementService;
	}
	
	@Autowired
	IForbiddenWordsService forbiddenWordsService;
	
//	@Autowired
//	RabbitMqProducer rabbitMqProducer;
	
	/**
	 * 公告资讯的查询
	 * @param jSONObject  即前台传值的查询条件
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public JSONArray selectAnnounceInfo(JSONObject jSONObject) throws Exception{
		JSONArray mapList = new JSONArray();
		
		int pageId = Integer.parseInt(TempleConstant.CMS_START_PAGE);
    	int pageSize = Integer.parseInt(TempleConstant.CMS_PAGE_SIZE);
    	if(!StringUtils.isEmpty(jSONObject.get("pageId"))){
    		pageId = Integer.parseInt(jSONObject.get("pageId").toString());
    	}
    	if(!StringUtils.isEmpty(jSONObject.get("pageSize") )){
    		pageSize = Integer.parseInt(jSONObject.get("pageSize").toString());
    	}
    	AnnounceInfo cmsAnnounceInfoDO = new AnnounceInfo();
    	if(!StringUtils.isEmpty(jSONObject.get("titleName"))){
    		cmsAnnounceInfoDO.setTitle(jSONObject.get("titleName").toString());
    	}
    	if(!StringUtils.isEmpty(jSONObject.get("status"))){
    		cmsAnnounceInfoDO.setStatus(Integer.parseInt(jSONObject.get("status").toString()));
    	}
    	if(!StringUtils.isEmpty(jSONObject.get("type"))){
    		cmsAnnounceInfoDO.setType(jSONObject.get("type").toString());
    	}
    	Map<String, Object> paramMap = new HashMap<String,Object>();
    	paramMap.put("type", jSONObject.get("type"));
		paramMap.put("title", jSONObject.get("titleName"));
		paramMap.put("status", jSONObject.get("status"));
		paramMap.put("start", (pageId > 1 ? (pageId - 1) * pageSize : 0));
		paramMap.put("pageSize", pageSize);
		Map<String, Object> map = announceService.selectAnnouncePageQuery(paramMap,cmsAnnounceInfoDO);
		List<CmsAnnounceInfoDTO> list = (List<CmsAnnounceInfoDTO>) map.get("list");
		if(list == null){
			return mapList;
		}
		Integer counts = (Integer) map.get("counts");

		int countnum = (int)Math.ceil(counts/Double.parseDouble(pageSize+""));
    	for(int i=0;i<list.size();i++){
    		CmsAnnounceInfoDTO cmsModel = list.get(i);
    		cmsModel.setPageNo(pageId);
    		cmsModel.setPageSize(pageSize);
    		cmsModel.setTotalCount(countnum);
    		cmsModel.setTotalCountNum(counts.intValue());
    		String countStr = cmsModel.getContent();
    		if(countStr != null && countStr.length()>50){
    			StringBuffer sb = new StringBuffer();
    			sb.append(countStr.substring(0, 50));
    			sb.append("......");
    			cmsModel.setContent(sb.toString());
    		}
    		mapList.add(cmsModel);
    	}
		return mapList;
	}
	
	/**
	 * 公告资讯的保存
	 * @param jSONObject
	 * @return
	 * @throws Exception
	 */
	public int addAnnounceInfo(JSONObject jSONObject) throws Exception {
		AnnounceInfo cmsAnnounceInfoDO = new AnnounceInfo();
		if(!StringUtils.isEmpty(jSONObject.get("sort") )){
			cmsAnnounceInfoDO.setSort(Integer.valueOf(jSONObject.get("sort").toString().trim()));
		}
		if(!StringUtils.isEmpty(jSONObject.get("status") )){
			cmsAnnounceInfoDO.setStatus(Integer.valueOf(jSONObject.get("status").toString().trim()));
		}
		if(!StringUtils.isEmpty(jSONObject.get("title") )){
			cmsAnnounceInfoDO.setTitle(jSONObject.get("title").toString().trim());
		}
		if(!StringUtils.isEmpty(jSONObject.get("type") )){
			cmsAnnounceInfoDO.setType(jSONObject.get("type").toString().trim());
		}
		
		if(!StringUtils.isEmpty(jSONObject.get("content") )){
			cmsAnnounceInfoDO.setContent(jSONObject.get("content").toString().trim());
		}else{
			cmsAnnounceInfoDO.setContent("");
		}
		
		if(!StringUtils.isEmpty(jSONObject.get("link") )){
			cmsAnnounceInfoDO.setLink(jSONObject.get("link").toString().trim());
		}else{
			cmsAnnounceInfoDO.setLink("");
		}
		
		/** 验证标题违禁词  **/
		ForbiddenWords forbiddenWordsDO = new ForbiddenWords();
		forbiddenWordsDO.setStatus(1);
		
		List<ForbiddenWords> list = forbiddenWordsService.queryByObject(forbiddenWordsDO);
		
		for (ForbiddenWords word : list) {
			if(cmsAnnounceInfoDO.getTitle().contains(word.getWords())){
				return -1;
			}
		}
		
		/** 验证内容违禁词  **/
		for (ForbiddenWords word : list) {
			if(cmsAnnounceInfoDO.getContent().contains(word.getWords())){
				return -1;
			}
		}
		
		int counts = 0;
		
		if(StringUtils.isEmpty(jSONObject.get("id"))){
			counts = announceService.addAnnounceByIds(cmsAnnounceInfoDO);
		}else{
			cmsAnnounceInfoDO.setId(Long.valueOf(jSONObject.get("id").toString()));
			counts = announceService.updateAnnounceByIds(cmsAnnounceInfoDO);
		}
		
		return counts;
	}
	
	public Map<String, Object> selectAnnouncePageQuery(Map<String, Object> paramMap,AnnounceInfo cmsAnnounceInfoDO) throws Exception{
		return announceService.selectAnnouncePageQuery(paramMap,cmsAnnounceInfoDO);
	}
	public List<AnnounceInfo> queryAnnounceInfoList(AnnounceInfo cmsAnnounceInfoDO) throws Exception{
		return announceService.queryAnnounceInfo(cmsAnnounceInfoDO);
	}
	public int delAnnounceByIds(List<Long> ids) throws Exception{
		return announceService.deleteAnnounceByIds(ids);
	}
	public int addAnnounceByIds(AnnounceInfo cmsAnnounceInfoDO) throws Exception{
		return announceService.addAnnounceByIds(cmsAnnounceInfoDO);
	}
	public int updateAnnounceByIds(AnnounceInfo cmsAnnounceInfoDO) throws Exception{
		return announceService.updateAnnounceByIds(cmsAnnounceInfoDO);
	}
	public AnnounceInfo queryAnnounceInfoByID(Long id) throws Exception{
		return announceService.queryAnnounceInfoByID(id);
	}
}
