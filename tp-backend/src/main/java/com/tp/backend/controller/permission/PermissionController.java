package com.tp.backend.controller.permission;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tp.backend.controller.AbstractBaseController;
import com.tp.model.usr.SysMenu;
import com.tp.proxy.usr.SysMenuProxy;
import com.tp.proxy.usr.UserHandler;

@Controller
public class PermissionController extends AbstractBaseController{
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="loadAdminRights")
	@ResponseBody
	public JSONArray loadAdminRights(HttpServletResponse resp) {
		JSONArray root = new JSONArray();
		List<SysMenu> sm = UserHandler.getUser().getSysMenuList();
		
		if(null != sm && !sm.isEmpty()){
			for (SysMenu sysMenuDO : sm) {
				if (null == sysMenuDO.getParentId()) { // 1级
					JSONObject first = new JSONObject();
					first.put("id", sysMenuDO.getId());
					first.put("text", sysMenuDO.getName());
					first.put("url", sysMenuDO.getUrl());
					sysMenuDO.setLocation(1L);
					first.put("location", sysMenuDO.getLocation());
					this.buildRights(first, sysMenuDO, sm);
					
					root.add(first);
				}
			}
		}
		return root;
	}
	

	/**
	 * 构建
	 * 
	 * @param first
	 * @param permission
	 */
	@SuppressWarnings("unchecked")
	private void buildRights(JSONObject po, SysMenu parent,
			List<SysMenu> permissions) {
		JSONArray children = new JSONArray();
		po.put("children", children);
		for (SysMenu permission : permissions) {
			if (null != permission.getParentId()
					&& permission.getParentId().equals(parent.getId())) {
				JSONObject node = new JSONObject();
				node.put("id", permission.getId());
				node.put("text", permission.getName());
				node.put("url", permission.getUrl());
				node.put("location", null!=parent.getLocation()?parent.getLocation():1);

				this.buildRights(node, permission, permissions);
				children.add(node);

			}
		}
		
	}
	
	class Right{
		public Right(){
			
		}
		public Right(Integer id,String name,String url,Integer location,Integer parent){
			this.id=Long.valueOf(id);
			this.name=name;
			this.url=url;
			this.location=location;
			this.parentId=Long.valueOf(parent);
		}
		Long id;
		String name;
		String url;
		String pcode;
		Integer location;
		Long parentId;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getPcode() {
			return pcode;
		}
		public void setPcode(String pcode) {
			this.pcode = pcode;
		}
		public Integer getLocation() {
			return location;
		}
		public void setLocation(Integer location) {
			this.location = location;
		}
		public Long getParentId() {
			return parentId;
		}
		public void setParentId(Long parentId) {
			this.parentId = parentId;
		}
	}
//	 
//	 /**
//		 * 构建
//		 * 
//		 * @param first
//		 * @param permission
//		 */
//		@SuppressWarnings("unchecked")
//		private void buildRights(JSONObject po, Right parent,
//				List<Right> permissions) {
//			JSONArray children = new JSONArray();
//			po.put("children", children);
//			for (int i = 0; i < permissions.size(); i++) {
//				Right permission = permissions.get(i);
//				if (null != permission.getParentId()
//						&& permission.getParentId().equals(parent.getId())) {
//					JSONObject node = new JSONObject();
//					node.put("id", permission.getId());
//					node.put("pcode", permission.getPcode());
//					node.put("text", permission.getName());
//					node.put("url", permission.getUrl());
//					node.put("location", permission.getLocation());
//
//					this.buildRights(node, permission, permissions);
//					children.add(node);
//
//				}
//			}
//		}
//		
//		class Right{
//			public Right(){
//				
//			}
//			public Right(Integer id,String name,String url,Integer location,Integer parent){
//				this.id=Long.valueOf(id);
//				this.name=name;
//				this.url=url;
//				this.location=location;
//				this.parentId=Long.valueOf(parent);
//			}
//			Long id;
//			String name;
//			String url;
//			String pcode;
//			Integer location;
//			Long parentId;
//			public Long getId() {
//				return id;
//			}
//			public void setId(Long id) {
//				this.id = id;
//			}
//			public String getName() {
//				return name;
//			}
//			public void setName(String name) {
//				this.name = name;
//			}
//			public String getUrl() {
//				return url;
//			}
//			public void setUrl(String url) {
//				this.url = url;
//			}
//			public String getPcode() {
//				return pcode;
//			}
//			public void setPcode(String pcode) {
//				this.pcode = pcode;
//			}
//			public Integer getLocation() {
//				return location;
//			}
//			public void setLocation(Integer location) {
//				this.location = location;
//			}
//			public Long getParentId() {
//				return parentId;
//			}
//			public void setParentId(Long parentId) {
//				this.parentId = parentId;
//			}
//		}
}
