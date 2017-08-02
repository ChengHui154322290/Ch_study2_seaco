package com.tp.proxy.cms;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tp.common.util.ImageDownUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.cms.TempleConstant;
import com.tp.dfsutils.util.DfsDomainUtil;
import com.tp.dto.cms.CmsAdvertiseInfoDTO;
import com.tp.model.cms.AdvertiseInfo;
import com.tp.model.cms.AdvertiseType;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cms.IAdvertiseService;
import com.tp.util.DateUtil;
import com.tp.util.StringUtil;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
/**
 * 广告管理表代理层
 * @author szy
 *
 */
@Service
public class AdvertiseProxy extends BaseProxy{

	@Autowired
	private IAdvertiseService advertiseService;
	//@Autowired
	DfsDomainUtil dfsDomainUtil;

	@Override
	public IBaseService getService() {
		return advertiseService;
	}
	
	public Map<String, Object> selectAdvertPageQuery(Map<String, Object> paramMap,AdvertiseInfo cmsAdvertiseInfoDO) throws Exception{
		return advertiseService.selectAdvertPageQuery(paramMap, cmsAdvertiseInfoDO);
	}
	
	public List<AdvertiseInfo> queryAdvertiseInfoList(AdvertiseInfo cmsAdvertiseInfoDO) throws Exception{
		//String parames = JSONValue.toJSONString(cmsAdvertiseInfoDO);
		return advertiseService.queryAdvertiseInfo(cmsAdvertiseInfoDO);
	}
	
	public List<AdvertiseType> queryAdvertTypeList(AdvertiseType cmsAdvertTypeDO) throws Exception{
		return advertiseService.queryAdvertType(cmsAdvertTypeDO);
	}
	
	public int delAdvertiseByIds(List<Long> ids) throws Exception{
		return advertiseService.deleteAdvertiseByIds(ids);
	}
	
	public int addAdvertiseByIds(AdvertiseInfo cmsAdvertiseInfoDO) throws Exception{
		cmsAdvertiseInfoDO = advertiseService.addAdvertiseByIds(cmsAdvertiseInfoDO);
		if(cmsAdvertiseInfoDO.getId() != null) return 1;
		return 0;
	}
	
	public int updateAdvertiseByIds(AdvertiseInfo cmsAdvertiseInfoDO) throws Exception{
		return advertiseService.updateAdvertiseByIds(cmsAdvertiseInfoDO);
	}
	
	public AdvertiseInfo selectById(Long id) throws Exception{
		return advertiseService.selectById(id);
	}
	
	public AdvertiseType selectAdvertTypeById(Long id) throws Exception{
		return advertiseService.selectAdvertTypeById(id);
	}
	
	public int addAdvertType(AdvertiseType cmsAdvertTypeDO) throws Exception{
		return advertiseService.addAdvertType(cmsAdvertTypeDO);
	}
	
	public int updateAdvertType(AdvertiseType cmsAdvertTypeDO) throws Exception{
		return advertiseService.updateAdvertType(cmsAdvertTypeDO);
	}
	/**
	 * 启用
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public int openAdvert(List<Long> ids) throws Exception{
		return advertiseService.openAdvertiseByIds(ids);
	}
	
	/**
	 * 禁用
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public int noOpenAdvert(List<Long> ids) throws Exception{
		return advertiseService.noOpenAdvertiseByIds(ids);
	}
	
	/**
	 * 图片列表查询
	 * @param params
	 * @param cmsAdvertiseInfoDO
	 * @return
	 * @throws ParseException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public JSONArray queryAdvertList(String params, AdvertiseInfo cmsAdvertiseInfoDO) throws ParseException, Exception {
		JSONObject jSONObject = new JSONObject();
		if(params != null){
			Object obj = JSONValue.parse(params);
			jSONObject = (JSONObject)obj;
			if(!StringUtils.isEmpty(jSONObject.get("titleName"))){
				cmsAdvertiseInfoDO.setAdvertname(jSONObject.get("titleName").toString());
			}
			if(!StringUtils.isEmpty(jSONObject.get("position"))){
				cmsAdvertiseInfoDO.setPosition(Integer.valueOf(jSONObject.get("position").toString()));
			}
			if(!StringUtils.isEmpty(jSONObject.get("startdate"))){
				String startdate = jSONObject.get("startdate").toString();
				startdate = startdate.replaceAll("/", "-");
				cmsAdvertiseInfoDO.setStartdate(DateUtil.parse(startdate+":00","yyyy-MM-dd HH:mm"));
			}
			if(!StringUtils.isEmpty(jSONObject.get("enddate") )){
				String enddate = jSONObject.get("enddate").toString();
				enddate = enddate.replaceAll("/", "-");
				cmsAdvertiseInfoDO.setEnddate(DateUtil.parse(enddate+":00","yyyy-MM-dd HH:mm"));
			}
			if(!StringUtils.isEmpty(jSONObject.get("type") )){
				cmsAdvertiseInfoDO.setType(jSONObject.get("type").toString());
			}
			if(!StringUtils.isEmpty(jSONObject.get("status") )){
				cmsAdvertiseInfoDO.setStatus(jSONObject.get("status").toString());
			}
		}
		
		int pageId = Integer.parseInt(TempleConstant.CMS_START_PAGE);
    	int pageSize = Integer.parseInt(TempleConstant.CMS_PAGE_SIZE);
    	if(jSONObject.get("pageId") != null && !"".equals(jSONObject.get("pageId"))){
    		pageId = Integer.parseInt(jSONObject.get("pageId").toString());
    	}
		
    	Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("advertname", jSONObject.get("titleName"));
		paramMap.put("position", jSONObject.get("position"));
		paramMap.put("startdate", jSONObject.get("startdate"));
		paramMap.put("enddate", jSONObject.get("enddate"));
		paramMap.put("type", jSONObject.get("type"));
		paramMap.put("status", jSONObject.get("status"));
		paramMap.put("start", (pageId > 1 ? (pageId - 1) * pageSize : 0));
		paramMap.put("pageSize", pageSize);
		Map<String, Object> map = advertiseService.selectAdvertPageQuery(paramMap,cmsAdvertiseInfoDO);
		List<CmsAdvertiseInfoDTO> list = (List<CmsAdvertiseInfoDTO>) map.get("list");
		Long counts = (Long) map.get("counts");
		
		int countnum = (int)Math.ceil(counts/Double.parseDouble(pageSize+""));
    	JSONArray mapList = new JSONArray();
    	for(int i=0;i<list.size();i++){
    		CmsAdvertiseInfoDTO obj = list.get(i);
    		if(obj.getPath()!=null && !obj.getPath().toString().contains("http")){
    			//注意，如果有直接修改数据库的要过滤掉
//    			obj.setPath(imgServerAdress+obj.getPath());
    			if(StringUtil.isNotBlank(obj.getPath())){
    				obj.setPath(ImageDownUtil.getThumbnail(Constant.IMAGE_URL_TYPE.cmsimg.url, obj.getPath(), Constant.IMAGE_SIZE.model40_40));
    			}
//    			obj.setActPath(dfsDomainUtil.getFileFullUrl(obj.getPath()));
    		}
    		obj.setPageNo(pageId);
    		obj.setPageSize(pageSize);
    		obj.setTotalCount(countnum);
    		obj.setTotalCountNum(counts.intValue());
    		
    		if(obj.getStartdate() != null){
    			obj.setStartdateStr(DateUtil.formatDateTime(obj.getStartdate()));
    		}
    		if(obj.getEnddate() != null){
    			obj.setEnddateStr(DateUtil.formatDateTime(obj.getEnddate()));
    		}
    		
    		mapList.add(obj);
    	}
		return mapList;
	}
}
