package com.tp.backend.controller.dss;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.model.dss.RefereeDetail;
import com.tp.proxy.dss.RefereeDetailProxy;

/**
 * 拉新管理
 * @author szy
 *
 */
@Controller
@RequestMapping("/dss/refereedetail/")
public class RefereeDetailManageController extends AbstractBaseController {

	@Autowired
	private RefereeDetailProxy refereeDetailProxy;
	
	@RequestMapping(value="list",method=RequestMethod.GET)
	public void list(Model model,Long promoterId,Integer fixed){
		model.addAttribute("promoterId", promoterId);
		model.addAttribute("fixed", fixed);
	}
	
	@RequestMapping(value="list",method=RequestMethod.POST)
	@ResponseBody
	public PageInfo<RefereeDetail> list(Model model,RefereeDetail refereeDetail){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("promoterId", refereeDetail.getPromoterId());
		params.put("memberId", refereeDetail.getMemberId());
		
		return refereeDetailProxy.queryPageByParamNotEmpty(params, new PageInfo<RefereeDetail>(refereeDetail.getStartPage(),refereeDetail.getPageSize())).getData();
	}
}
