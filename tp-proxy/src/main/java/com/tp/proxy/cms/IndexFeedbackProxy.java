package com.tp.proxy.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tp.common.vo.cms.TempleConstant;
import com.tp.dto.cms.CmsIndexFeedbackDTO;
import com.tp.model.cms.IndexFeedback;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cms.IIndexFeedbackService;
import com.tp.util.DateUtil;
/**
 * 页面反馈信息表代理层
 * @author szy
 *
 */
@Service
public class IndexFeedbackProxy extends BaseProxy<IndexFeedback>{

	@Autowired
	private IIndexFeedbackService indexFeedbackService;

	@Override
	public IBaseService<IndexFeedback> getService() {
		return indexFeedbackService;
	}

	public IndexFeedback queryFeedbackByID(long parseLong) {
		return indexFeedbackService.queryById(parseLong);
	}

	public JSONArray queryFeedbackList(String params, IndexFeedback cmsIndexFeedbackDO) {
		JSONObject jSONObject = new JSONObject();
		if(params != null){
			Object obj = JSONValue.parse(params);
			jSONObject = (JSONObject)obj;
			if(!StringUtils.isEmpty(jSONObject.get("userId"))){
				cmsIndexFeedbackDO.setUserId(Long.parseLong(jSONObject.get("userId").toString()));
			}
			if(!StringUtils.isEmpty(jSONObject.get("userName"))){
				cmsIndexFeedbackDO.setUserName(jSONObject.get("userName").toString());
			}
		}
		
		int pageId = Integer.parseInt(TempleConstant.CMS_START_PAGE);
    	int pageSize = Integer.parseInt(TempleConstant.CMS_PAGE_SIZE);
    	if(jSONObject.get("pageId") != null && !"".equals(jSONObject.get("pageId"))){
    		pageId = Integer.parseInt(jSONObject.get("pageId").toString());
    	}
		
    	Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("userId", jSONObject.get("userId"));
		paramMap.put("userName", jSONObject.get("userName"));
		paramMap.put("start", (pageId > 1 ? (pageId - 1) * pageSize : 0));
		paramMap.put("pageSize", pageSize);
		Map<String, Object> map = null;
		try {
			map = indexFeedbackService.selectFeedbackPageQuery(paramMap,cmsIndexFeedbackDO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<CmsIndexFeedbackDTO> list = (List<CmsIndexFeedbackDTO>) map.get("list");
		Long counts = (Long) map.get("counts");
		
		int countnum = (int)Math.ceil(counts/Double.parseDouble(pageSize+""));
    	JSONArray mapList = new JSONArray();
    	for(int i=0;i<list.size();i++){
    		CmsIndexFeedbackDTO obj = list.get(i);
    		
    		if(obj.getFeedbackDate() != null){
    			obj.setFeedbackDateStr(DateUtil.formatDateTime(obj.getFeedbackDate()));
    		}
    		
    		String countStr = obj.getFeedbackInfo();
    		if(countStr != null && countStr.length()>50){
    			StringBuffer sb = new StringBuffer();
    			sb.append(countStr.substring(0, 50));
    			sb.append("......");
    			obj.setFeedbackInfo(sb.toString());
    		}
    		
    		obj.setPageNo(pageId);
    		obj.setPageSize(pageSize);
    		obj.setTotalCount(countnum);
    		obj.setTotalCountNum(counts.intValue());
    		mapList.add(obj);
    	}
		return mapList;
	}

	public IndexFeedback queryFeedbackByID(Long id) throws Exception{
		return indexFeedbackService.queryById(id);
	}
	
	public int delFeedbackByIds(List<Long> ids) throws Exception{
		return indexFeedbackService.deleteByIds(ids);
	}
}
