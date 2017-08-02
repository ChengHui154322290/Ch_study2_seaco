package com.tp.proxy.usr;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.dto.usr.Tree;
import com.tp.model.usr.SysMenu;
import com.tp.model.usr.SysMenuLimit;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.usr.ISysMenuLimitService;
import com.tp.service.usr.ISysMenuService;
import com.tp.util.StringUtil;
/**
 * 菜单表代理层
 * @author szy
 *
 */
@Service
public class SysMenuProxy extends BaseProxy<SysMenu>{

	@Autowired
	private ISysMenuService sysMenuService;
	@Autowired
	private ISysMenuLimitService sysMenuLimitService;

	@Override
	public IBaseService<SysMenu> getService() {
		return sysMenuService;
	}
	public List<SysMenu> getAll(){
		return this.sysMenuService.queryByParam(new HashMap<String,Object>());
	}
	
	public Map<String,SysMenu> getSysMenuMap(){
		Map<String,SysMenu> map = new HashMap<String, SysMenu>();
		List<SysMenu> list = getAll();
		if(null == list || list.isEmpty()) return null;
		for (SysMenu menu : list) {
			logger.info("put : " + menu.getName() + ".url : " + menu.getUrl());
			
			map.put(null!=menu.getUrl()?menu.getUrl():menu.getId().toString(), menu);
		}
		
		return map;
	}
	
	public List<SysMenu> getTopMenuList(){
		SysMenu sysMenu = new SysMenu();
		sysMenu.setParentId(0L);
		return this.sysMenuService.findParentMenu();
	}
	
	
	public List<SysMenu> getChildMenu(List<SysMenu> list){
		return this.sysMenuService.findListByParentIds(list);
	}
	
	
	public List<Tree> getSysMenuTree(Long parentId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentId", parentId);
		List<SysMenu> list = null;
		if(null == parentId) list =  sysMenuService.findParentMenu();
		else list =  sysMenuService.queryByParam(params);
			 
		if(null==list || list.isEmpty()) return null;
		List<Tree> trees = new ArrayList<Tree>();
		for (SysMenu menu : list) {
			trees.add(new Tree(menu.getName(), StringUtil.isNullOrEmpty(menu.getUrl())?"true":"false", menu.getId()+"", "true","true"));
		}
		return trees;
	}
	
	 public List<SysMenu> getSysMenu(Long parentId){
		SysMenu sysMenu = new SysMenu();
		sysMenu.setParentId(parentId);
		List<SysMenu> list = null;
		if(null == parentId) list =  sysMenuService.findParentMenu();
		else list =  sysMenuService.queryByParamForUrlIsNull(sysMenu);
		return list;
	}
	
	
	public List<Tree> getSysMenuTree(){
		List<SysMenu> list = this.getAll();
		
		if(null==list || list.isEmpty()) return null;
		List<Tree> trees = new ArrayList<Tree>();
		for (SysMenu menu : list) {
			
			trees.add(new Tree(menu.getName(), StringUtil.isNullOrEmpty(menu.getUrl())?"true":"false", menu.getId()+"", "true","true"));
		}
		return trees;
	}
	
	
	public List<SysMenu> findByIds(List<Long> ids){
		return this.sysMenuService.selectByIds(ids);
	}
	
	public String sysMenuTree(Long parentId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentId", parentId);
		List<SysMenu> list = null;
		if(null == parentId) list =  this.sysMenuService.findParentMenu();
		else list =  this.sysMenuService.queryByParam(params);
												
		String json = "";
		if(null == list||list.isEmpty()){
			json += "{"+StringUtil.l("title")+" : "+StringUtil.l("无菜单")+",";
			json += StringUtil.l("key")+" : "+StringUtil.l("0")+",";
			json += StringUtil.l("isFolder")+" : false,";
			json += StringUtil.l("noLink")+" : true,";
			json += StringUtil.l("hideCheckbox")+" : true}";
		}else{
			for (int i=0;i<list.size();i++) {
				SysMenu menu = list.get(i);
				json += "{"+StringUtil.l("title")+" : "+StringUtil.l(menu.getName())+",";
				
				SysMenu s = new SysMenu();
				s.setParentId(menu.getId());
				params.clear();
				params.put("parentId", menu.getId());
				List<SysMenu> childList = this.sysMenuService.queryByParam(params);
				String flag = "false";
				if(null != childList&&!childList.isEmpty()) flag = "true";
				json += StringUtil.l("isFolder")+" : "+flag+",";
				json += StringUtil.l("type")+" : "+StringUtil.l("0")+",";
				json += StringUtil.l("key")+" : "+StringUtil.l(menu.getId().toString())+",";
				json += StringUtil.l("parent")+" : "+StringUtil.l(menu.getParentId()+"")+",";
				json += StringUtil.l("isLazy")+" :"+flag+",";
				json += StringUtil.l("url")+" :"+StringUtil.isNullOrEmpty(menu.getUrl())+",";
				json += StringUtil.l("hashUrl")+" :"+(StringUtil.isNullOrEmpty(menu.getUrl())?"false":"true")+",";
				json += StringUtil.l("expand")+" : false";
				if(null != childList&&!childList.isEmpty()){
					json += ","+StringUtil.l("children")+" : ["+sysMenuTree(menu.getId())+"]";
				}else{
					params.clear();
					params.put("sysMenuId", menu.getId());
					List<SysMenuLimit> limitList = this.sysMenuLimitService.queryByParam(params);
					if(null!=limitList&&!limitList.isEmpty()){
						json += ","+StringUtil.l("children")+" : [";
						for (int j=0;j<limitList.size();j++) {
							
							SysMenuLimit l = limitList.get(j);
							json += "{"+StringUtil.l("title")+" : "+StringUtil.l(l.getDesc())+",";
							json += StringUtil.l("isFolder")+" : false,";
							json += StringUtil.l("parent")+" : "+StringUtil.l(menu.getId()+"")+",";
							json += StringUtil.l("type")+" : "+StringUtil.l("1")+",";
							json += StringUtil.l("key")+" : "+StringUtil.l(l.getId().toString())+",";
							json += StringUtil.l("expand")+" : false}";
							if(j+1 != limitList.size()) json +=",";
						}
						json += "]";
					}
				}
				json +="}";
				if(i+1 != list.size()) json +=",";
			}
		}
		return json;
	}

	
	public void save(SysMenu sysMenu){
		
		UserInfo user = UserHandler.getUser();
		String userName ="system";
		if(null != user) userName = user.getUserName();
		
		sysMenu.setCreateUser(userName);
		sysMenu.setUpdateUser(userName);
		sysMenu.setCreateTime(new Date());
		sysMenu.setUpdateTime(new Date());
		sysMenu.setStatus(Constant.DEFAULTED.YES);
		
		sysMenu = this.sysMenuService.save(sysMenu);
		
		if(!StringUtil.isNullOrEmpty(sysMenu.getUrl()))
			sysMenuLimitService.insert(new SysMenuLimit(sysMenu.getId().longValue(), sysMenu.getUrl(), Constant.DEFAULTED.YES));
		
	}
}
