package com.tp.proxy.mem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mem.PlatformCode;
import com.tp.common.vo.mem.SceneCode;
import com.tp.dto.common.ResultInfo;
import com.tp.exception.ServiceException;
import com.tp.model.mem.PointInfo;
import com.tp.model.mem.PointRuleConfig;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.usr.UserHandler;
import com.tp.service.IBaseService;
import com.tp.service.mem.IPointInfoService;
import com.tp.service.mem.IPointRuleConfigService;
import com.tp.util.DateUtil;
import com.tp.util.StringUtil;
/**
 * 用户积分明细表代理层
 * @author szy
 *
 */
@Service
public class PointInfoProxy extends BaseProxy<PointInfo>{

	@Autowired
	private IPointInfoService pointInfoService;
	@Autowired
	private IPointRuleConfigService pointRuleConfigService;

	@Override
	public IBaseService<PointInfo> getService() {
		return pointInfoService;
	}
	
	public PointRuleConfig insert(String platform, String sceneCode, String point, Integer isExpiry, String createBeginTime, String createEndTime) throws ParseException {
		if(!check(sceneCode, platform, isExpiry)){
			throw new ServiceException("相似记录已经存在", 910);
		}
		PointRuleConfig obj = new PointRuleConfig();
		obj.setPoint(Integer.valueOf(point));
		if(1 == isExpiry) {
				obj.setBeginTime(strToDate(createBeginTime));
				obj.setEndTime(strToDate(createEndTime));
		}
		obj.setDeadLineFlag(isExpiry);
		obj.setCreateUserId(UserHandler.getUser().getId());
		obj.setCreateUserName(UserHandler.getUser().getUserName());
		obj.setCreateTime(new Date());
		if(sceneCode.equals(SceneCode.REGISTER_SCENE.code)) {
			obj.setSceneCode(SceneCode.REGISTER_SCENE.code);
			obj.setRemark("注册送" + point + "积分");
		}
		if(sceneCode.equals(SceneCode.MY_INFO_SCENE.code)) {
			obj.setSceneCode(SceneCode.MY_INFO_SCENE.code);
			obj.setRemark("完善个人信息送" + point + "积分");
		}
		if(sceneCode.equals(SceneCode.MY_BABY_SCENE.code)) {
			obj.setSceneCode(SceneCode.MY_BABY_SCENE.code);
			obj.setRemark("新增宝宝信息送" + point + "积分");
		}
		obj.setPlatForm(Integer.valueOf(platform));
		obj.setIsDelete(false);
		obj.setState(true);
		obj = pointRuleConfigService.insert(obj);
		return obj;
	}
	public PointRuleConfig edit(Long id, String platform, String state, String point, Integer isExpiry, String createBeginTime, String createEndTime) throws ParseException {
		PointRuleConfig config = pointRuleConfigService.queryById(id);
		if(!check(config.getSceneCode(), platform, isExpiry) && config.getDeadLineFlag() == 1){
			throw new ServiceException("相似记录已经存在", 910);
		}
		
		PointRuleConfig obj = new PointRuleConfig();
		obj.setPoint(Integer.valueOf(point));
		obj.setRemark("注册送" + point + "积分");

		if(1 == isExpiry) {
			obj.setBeginTime(strToDate(createBeginTime));
			obj.setEndTime(strToDate(createEndTime));
		}
		if("0".equals(state))
			obj.setState(false);
		else
			obj.setState(true);
		obj.setPlatForm(Integer.valueOf(platform));
		obj.setDeadLineFlag(isExpiry);
		obj.setCreateUserId(UserHandler.getUser().getId());
		obj.setCreateUserName(UserHandler.getUser().getUserName());
		obj.setCreateTime(new Date());
		obj.setIsDelete(false);
		obj.setId(id);
		pointRuleConfigService.updateNotNullById(obj);
		return obj;
	}
	
	public Boolean check(String sceneCode, String platform, Integer isExpiry) {
		List<PointRuleConfig> list = new ArrayList<PointRuleConfig>();
		PointRuleConfig query = new PointRuleConfig();
		query.setPlatForm(Integer.valueOf(platform));
		query.setDeadLineFlag(isExpiry);
		query.setSceneCode(sceneCode);
		query.setState(true);
		query.setIsDelete(false);
		list = pointRuleConfigService.queryByObject(query);
		if(null == list || list.size() == 0)
			return true;
		else
			return false;
	}
	private Date strToDate(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(dateStr);
		return date;
	}
	
	public PointRuleConfig delete(Long id) throws ParseException {
		PointRuleConfig obj = new PointRuleConfig();
		obj.setIsDelete(true);
		obj.setId(id);
		pointRuleConfigService.updateNotNullById(obj);
		return obj;
	}
	public PageInfo<PointRuleConfig> list(String sceneCode, String point, Integer isExpiry, 
			String createBeginTime, String createEndTime, int pageNo, int pageSize) throws ParseException {
		PointRuleConfig query = new PointRuleConfig();
		if(!StringUtil.isNullOrEmpty(point))
			query.setPoint(Integer.valueOf(point));
		if(null != isExpiry && 5 != isExpiry)
			query.setDeadLineFlag(isExpiry);
		query.setSceneCode(sceneCode);
		if(!StringUtil.isNullOrEmpty(createBeginTime)) {
        	Date date = strToDate(createBeginTime);
        	query.setBeginTime(date);
        }
        if(!StringUtil.isNullOrEmpty(createEndTime)) {
        	Date date = strToDate(createEndTime);
        	query.setEndTime(date);
        }
        query.setIsDelete(false);
//        query.setState(true);
        PageInfo<PointRuleConfig> pageInfo = new PageInfo<PointRuleConfig>();
        pageInfo.setPage(pageNo);
        pageInfo.setSize(pageSize);
		return pointRuleConfigService.queryPageByObject(query, pageInfo);
	}
	
	public JSONObject toJson(PageInfo<PointRuleConfig> pageObj, int rows, int page) throws Exception {
		List<PointRuleConfig> list = new ArrayList<PointRuleConfig>();
		if(null != pageObj && null != pageObj.getRows())
			list = pageObj.getRows();
		else {
			JSONObject res=new JSONObject();
			res.put("rows", new JSONArray());
			res.put("records", 0);
			res.put("total", 0);
			res.put("page", 0);
		}
		JSONArray rowArray = new JSONArray();	
		for (PointRuleConfig pojo : list) {
			JSONObject row = new JSONObject();
			if(null != pojo.getId() && pojo.getId() > 0)
				row.put("id", pojo.getId());
			else
				row.put("id", 0);
			row.put("sceneCode", pojo.getSceneCode());
			row.put("point", pojo.getPoint());
			if(null != pojo.getPlatForm()) {
				if(pojo.getPlatForm().intValue() == PlatformCode.PC.code.intValue())
					row.put("platform", PlatformCode.PC.desc);
				if(pojo.getPlatForm().intValue() == PlatformCode.APP.code.intValue())
					row.put("platform", PlatformCode.APP.desc);
				if(pojo.getPlatForm().intValue() == PlatformCode.WAP.code.intValue())
					row.put("platform", PlatformCode.WAP.desc);
			}
			if(pojo.getDeadLineFlag() == 1) {
				row.put("deadLineFlag", "有");
				row.put("createBeginTime", DateUtil.formatDate(pojo.getBeginTime(), "yyyy-MM-dd"));
				row.put("createEndTime", DateUtil.formatDate(pojo.getEndTime(), "yyyy-MM-dd"));
			} else {
				row.put("deadLineFlag", "无");
				row.put("createBeginTime", "-");
				row.put("createEndTime", "-");
			}
			rowArray.add(row);
		}	
		JSONObject res=new JSONObject();
		res.put("rows", rowArray);
		res.put("records", rowArray.size());
		int totalRecord = pageObj.getTotal();
		int totalPage = totalRecord % rows == 0 ? totalRecord
				/ rows : totalRecord / rows + 1;
		res.put("total", totalPage);
		res.put("page", page);
		return res;
	}
	public PointRuleConfig show(Long id) throws Exception {
		return pointRuleConfigService.queryById(id);
	}
}
