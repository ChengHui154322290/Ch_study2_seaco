package com.tp.backend.controller.permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.usr.SysMenu;
import com.tp.proxy.usr.SysMenuProxy;

@Controller
@RequestMapping("/kaifa")
public class KaifaController extends AbstractBaseController{

	@Autowired
	private SysMenuProxy sysMenuProxy;
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request){
		return "kaifa/index";
	}
	
	@RequestMapping("/sysMenuManage")
	public String sysMenuManage(Model model){
		List<SysMenu> parentList = sysMenuProxy.queryByParam(new HashMap<String,Object>()).getData();
		model.addAttribute("parentList", parentList);
		return "kaifa/sysMenuManage";
	}
	
	@RequestMapping("/getSysmenuList")
	@ResponseBody
	public ResultInfo<List<SysMenu>> getSysmenuList(Long sysMenuId){
		if(null==sysMenuId){
			return new ResultInfo<>(new FailInfo("请选择父菜单"));
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentId", sysMenuId);
		return sysMenuProxy.queryByParam(params);
	}
	
	@RequestMapping("/addSysMenu")
	@ResponseBody
	public ResultInfo<SysMenu> addSysMenu(SysMenu sysMenuDO){
		return sysMenuProxy.insert(sysMenuDO);
	}
	
}
