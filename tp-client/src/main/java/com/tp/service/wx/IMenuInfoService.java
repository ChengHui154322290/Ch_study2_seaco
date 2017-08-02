package com.tp.service.wx;

import java.util.Map;

import com.tp.dto.wx.message.widget.Menu;
import com.tp.model.wx.MenuInfo;
import com.tp.result.wx.MenuResult;
import com.tp.service.IBaseService;

/**
 * 菜单管理接口
 * @author zhuss
 */
public interface IMenuInfoService extends IBaseService<MenuInfo>{
	
	public Integer queryCountByPid(Integer pid);
	
	public Integer delMenu(Map<String,Object> params);

	/**
	 * 创建菜单
	 * @return
	 */
	public boolean createMenu(Menu menu);
	
	/**
	 * 获取菜单
	 * @return
	 */
	public MenuResult getMenu();
	
	/**
	 * 删除菜单
	 * @return
	 */
	public boolean deleteMenu();
}
