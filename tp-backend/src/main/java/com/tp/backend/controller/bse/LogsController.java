package com.tp.backend.controller.bse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.model.bse.Logs;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.bse.DistrictInfoProxy;
import com.tp.proxy.bse.LogsProxy;
import com.tp.proxy.usr.UserInfoProxy;
import com.tp.util.StringUtil;

@Controller
@RequestMapping("/basedata/logs")
public class LogsController extends AbstractBaseController {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(LogsController.class);

	@Autowired
	private LogsProxy logsProxy;
	@Autowired
	private UserInfoProxy userInfoProxy;
	@Autowired
	private DistrictInfoProxy districtInfoProxy;
	/**
	 * 日志列表查询
	 * @param model
	 * @param brandDO
	 * @throws Exception
	 */
	@RequestMapping(value = "/view")
	public String list(Model model,Long id,String type,Integer page,Integer size){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", id);
		params.put("optionType", type);
		PageInfo<Logs> queryAllLogsByPage =logsProxy.queryPageByParamNotEmpty(params, new PageInfo<Logs>(page,size)).getData();
		if(queryAllLogsByPage!=null){
			List<Logs> logsList = queryAllLogsByPage.getRows();
			if(CollectionUtils.isNotEmpty(logsList)){
				List<Long> ids = new ArrayList<Long>();
				for(Logs log:logsList){
					ids.add(log.getUserId());
				}
				params.clear();
				params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), " user_id in ("+StringUtil.join(ids, Constant.SPLIT_SIGN.COMMA)+")");
				List<UserInfo> userList=userInfoProxy.queryByParam(params).getData();
				model.addAttribute("userList", userList);
			}	
		}
		
		model.addAttribute("id", id);
		model.addAttribute("type", type);
		model.addAttribute("queryAllLogsByPage", queryAllLogsByPage);
		if("Brand".equals(type)){
			Map<String, String> allCountryAndAllProvince = districtInfoProxy.getAllCountryAndAllProvince();
			model.addAttribute("allCountryAndAllProvince", allCountryAndAllProvince);
		}
		return "/basedata/logs/view";
	}
}
