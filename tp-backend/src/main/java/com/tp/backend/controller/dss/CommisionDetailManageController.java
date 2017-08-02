package com.tp.backend.controller.dss;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.common.ResultInfo;
import com.tp.model.dss.CommisionDetail;
import com.tp.model.mem.MemberInfo;
import com.tp.proxy.dss.CommisionDetailProxy;
import com.tp.proxy.mem.MemberInfoProxy;

/**
 * 佣金明细管理
 * @author szy
 *
 */
@Controller
@RequestMapping("/dss/commisiondetail/")
public class CommisionDetailManageController extends AbstractBaseController {

	@Autowired
	private CommisionDetailProxy commisionDetailProxy;
	@Autowired
	private MemberInfoProxy memberInfoProxy;
	
	@RequestMapping(value="list",method=RequestMethod.GET)
	public void index(Model model,CommisionDetail commisionDetail,Integer fixed){
		model.addAttribute("commisionDetail",commisionDetail);
		model.addAttribute("fixed",fixed);
		model.addAttribute("collectStatusList",DssConstant.COLLECT_STATUS.values());
	}
	
	@RequestMapping(value="list",method=RequestMethod.POST)
	@ResponseBody
	public PageInfo<CommisionDetail> list(Model model, CommisionDetail commisionDetail){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("promoterId", commisionDetail.getPromoterId());
		params.put("orderCode", commisionDetail.getOrderCode());
		params.put("collectStatus", commisionDetail.getCollectStatus());
		params.put("memberId", commisionDetail.getMemberId());
		ResultInfo<PageInfo<CommisionDetail>> result = commisionDetailProxy.queryPageByParamNotEmpty(params,
				new PageInfo<CommisionDetail>(commisionDetail.getStartPage(),commisionDetail.getPageSize()));
		return result.getData();
	}
	
	@RequestMapping(value="querymemberinfobylikememberaccount",method=RequestMethod.POST)
	@ResponseBody
	public List<MemberInfo> queryMemberInfoByLikeMemberAccount(Model model,String memberAccount){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " nick_name like concat('"+memberAccount+"','%') or mobile like concat('"+memberAccount+"','%')");
		params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), 10);
		return memberInfoProxy.queryByParam(params).getData();
	}
}
