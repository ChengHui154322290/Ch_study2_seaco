package com.tp.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.usr.Favrite;
import com.tp.model.usr.SysMenu;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.usr.FavriteProxy;
import com.tp.result.mem.app.ResultMessage;
import com.tp.service.usr.ISysMenuService;

@Controller
@RequestMapping("/favrite")
public class FavriteController {

	@Autowired
	private FavriteProxy favriteProxy;
	
	@Autowired
	private ISysMenuService sysMenuService;
	
	
	@RequestMapping("list")
	public String index(Model model){
		UserInfo user = (UserInfo)SecurityUtils.getSubject().getPrincipal();
		if(null==user){
			//未登陆
			return "redirect:/login";
		}
		List<Favrite> favriteDOs = favriteProxy.selectFavriteByUserId(user.getId());
		if(CollectionUtils.isNotEmpty(favriteDOs)){
			List<Long> menuIds = new ArrayList<Long>();
			for (Favrite favriteDO : favriteDOs) {
				menuIds.add(favriteDO.getMenuId());
			}
			List<SysMenu> menuDOs = sysMenuService.findListByIds(menuIds);
			model.addAttribute("menuDOs", menuDOs);
		}
		return "favrite/index";
	}
	
	@RequestMapping("checkedFavrite")
	@ResponseBody
	public ResultInfo<Boolean> checkedFavrite(Long menuId){
		UserInfo user = (UserInfo)SecurityUtils.getSubject().getPrincipal();
		if(null==user){
			return new ResultInfo<Boolean>(new FailInfo(1,"您尚未登录"));
		}
		if(null==menuId){
			return new ResultInfo<Boolean>(new FailInfo(2,"功能菜单不存在"));
		}
		SysMenu menuDO = sysMenuService.queryById(menuId);
		
		if(null==menuDO){
			return new ResultInfo<Boolean>(new FailInfo(2,"功能菜单不存在"));
		}
		Favrite favriteDO = favriteProxy.selectByUserIdAndMeunId(user.getId(),menuId);
		if(null==favriteDO){
			return new ResultInfo<Boolean>(new FailInfo(404,"未加入常用菜单"));
		}
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping("addFavrite")
	@ResponseBody
	public ResultInfo<Boolean> addFavrite(Long menuId){
		UserInfo user = (UserInfo)SecurityUtils.getSubject().getPrincipal();
		if(null==user){
			return new ResultInfo<Boolean>(new FailInfo(1,"您尚未登录"));
		}
		if(null==menuId){
			return new ResultInfo<Boolean>(new FailInfo(2,"功能菜单不存在"));
		}
		SysMenu menuDO = sysMenuService.queryById(menuId);
		
		if(null==menuDO){
			return new ResultInfo<Boolean>(new FailInfo(2,"功能菜单不存在"));
		}
		return favriteProxy.saveFavrite(user.getId(),menuId);
	}
	
	@RequestMapping("removeFavrite")
	@ResponseBody
	public ResultMessage removeFavrite(Long menuId){
		UserInfo user = (UserInfo)SecurityUtils.getSubject().getPrincipal();
		ResultMessage message = null;
		if(null==user){
			//未登陆
			return message = new ResultMessage(ResultMessage.FAIL, "您尚未登录","NOLOGIN");
		}
		if(null==menuId){
			return message = new ResultMessage(ResultMessage.FAIL, "功能菜单不存在","NOMENU");
		}
		
		SysMenu menuDO = sysMenuService.queryById(menuId);
		
		if(null==menuDO){
			return message = new ResultMessage(ResultMessage.FAIL, "功能菜单不存在","NOMENU");
		}
		
		favriteProxy.removeFavrite(user.getId(),menuId);
		message = new ResultMessage(ResultMessage.SUCCESS, "操作成功");
		return  message;
	}
}
