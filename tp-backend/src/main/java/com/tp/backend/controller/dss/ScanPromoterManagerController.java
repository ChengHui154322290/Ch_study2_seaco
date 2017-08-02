package com.tp.backend.controller.dss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.dss.PromoterInfo;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.util.StringUtil;

/**
 * 扫码推广管理
 * @author szy
 *
 */
@Controller
@RequestMapping("/dss/scan/")
public class ScanPromoterManagerController extends AbstractBaseController {

	@Autowired
	private PromoterInfoProxy promoterInfoProxy;
	
	@RequestMapping(value="list",method=RequestMethod.GET)
	public void list(Model model){
		model.addAttribute("inviteCodeUsedList", DssConstant.PROMOTER_SCAN_USE.values());
	}
	
	@RequestMapping(value="list",method=RequestMethod.POST)
	@ResponseBody
	public PageInfo<PromoterInfo> list(Model model,PromoterInfo promoterInfo){
		if(StringUtil.isBlank(promoterInfo.getPromoterName())){
			promoterInfo.setPromoterName(null);
		}
		promoterInfo.setPromoterType(DssConstant.PROMOTER_TYPE.SCANATTENTION.code);
		ResultInfo<PageInfo<PromoterInfo>> result = promoterInfoProxy.queryPageByInviteCode(promoterInfo, new PageInfo<PromoterInfo>(promoterInfo.getStartPage(),promoterInfo.getPageSize()));
		return result.getData();
	}
	
	@RequestMapping(value="save",method=RequestMethod.GET)
	public void save(Model model){
		model.addAttribute("promoterLevelList", DssConstant.PROMOTER_LEVEL.values());
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<PromoterInfo> save(Model model,PromoterInfo promoterInfo){
		promoterInfo.setCreateUser(super.getUserName());
		promoterInfo.setUpdateUser(super.getUserName());
		ResultInfo<PromoterInfo> result = promoterInfoProxy.insertScan(promoterInfo);
		return result;
	}
}
