package com.tp.backend.controller.wx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.wx.MenuConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.wx.MenuInfoDto;
import com.tp.exception.AppServiceException;
import com.tp.model.wx.MenuInfo;
import com.tp.proxy.wx.MenuInfoProxy;

/**
 * 微信菜单管理
 * @author zhuss
 */
@Controller
@RequestMapping("/wx/menu/")
public class MenuInfoController {

	private  Logger logger = LoggerFactory.getLogger(MenuInfoController.class);
	
	@Autowired
	private MenuInfoProxy menuInfoProxy;
	
	/**
	 * 菜单
	 * @param model
	 * @param menuInfo
	 */
	@RequestMapping("list")
	public void list(Model model){
		model.addAttribute("levelList", MenuConstant.LEVEL.values());
		model.addAttribute("typeList", MenuConstant.TYPE.values());
		ResultInfo<MenuInfo> menuInfo  = new ResultInfo<MenuInfo>(new MenuInfo());
		model.addAttribute("resultInfo", menuInfo);
	}
	
	@RequestMapping("convTree")
	@ResponseBody
	public List<MenuInfoDto> convTree(){
		return menuInfoProxy.convTree();
	}
	
	/**
	 * 跳转消息编辑页
	 * @param model
	 * @param id
	 */
	@RequestMapping(value="save",method=RequestMethod.GET)
	@ResponseBody
	public void save(Model model,Long id){
		ResultInfo<MenuInfo> menuInfo = null;
		if(null==id){
			menuInfo = new ResultInfo<MenuInfo>(new MenuInfo());
		}else{
			menuInfo = menuInfoProxy.queryById(id);
		}
		model.addAttribute("resultInfo", menuInfo);
		model.addAttribute("levelList", MenuConstant.LEVEL.values());
		model.addAttribute("typeList", MenuConstant.TYPE.values());
	}
	
	/**
	 * 保存菜单记录
	 * @param model
	 * @return
	 */
	@RequestMapping(value="save",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> save(MenuInfo menuInfo){
		try{
			return menuInfoProxy.saveMenu(menuInfo);
		}catch(AppServiceException ase){
			logger.error("[微信 - 保存菜单 AppServiceException] ={}",ase.getMessage()); 
			return new ResultInfo<>(new FailInfo(ase.getMessage()));
		}
	}
	
	/**
	 * 删除菜单
	 * @param model
	 * @return
	 */
	@RequestMapping(value="del",method=RequestMethod.GET)
	@ResponseBody
	public ResultInfo<Integer> del(Integer pid,Integer id){
		try{
			Map<String,Object> params = new HashMap<>();
			params.put("pid", pid);
			params.put("id", id);
			return menuInfoProxy.delMenu(params);
		}catch(AppServiceException ase){
			logger.error("[微信 - 删除菜单 AppServiceException] ={}",ase.getMessage()); 
			return new ResultInfo<>(new FailInfo(ase.getMessage()));
		}
	}
	
	/**
	 * 发布菜单到公众号
	 * @param model
	 * @return
	 */
	@RequestMapping(value="push",method=RequestMethod.GET)
	@ResponseBody
	public ResultInfo<Boolean> push(){
		try{
			return menuInfoProxy.pushMenu();
		}catch(AppServiceException ase){
			logger.error("[微信 - 发布菜单 AppServiceException] ={}",ase.getMessage()); 
			return new ResultInfo<>(new FailInfo(ase.getMessage()));
		}
	}
}
