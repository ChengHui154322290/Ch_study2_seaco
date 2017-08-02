package com.tp.service.wx;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.wx.MenuInfoDao;
import com.tp.dto.wx.message.widget.Menu;
import com.tp.model.wx.MenuInfo;
import com.tp.result.wx.MenuResult;
import com.tp.service.BaseService;
import com.tp.service.wx.IMenuInfoService;
import com.tp.service.wx.cache.VoucherCache;
import com.tp.service.wx.manager.MenuManager;

/**
 * 菜单管理实现类
 * @author zhuss
 */
@Service
public class MenuInfoService extends BaseService<MenuInfo> implements IMenuInfoService{

	@Autowired
	private VoucherCache voucherCache;
	
	@Autowired
	private MenuInfoDao menuInfoDao;
	
	@Override
	public BaseDao<MenuInfo> getDao() {
		return menuInfoDao;
	}
	
	@Override
	public boolean createMenu(Menu menu) {
		return MenuManager.createMenu(voucherCache.getAccessTokenCache(), menu);
	}

	@Override
	public MenuResult getMenu() {
		return MenuManager.getMenu(voucherCache.getAccessTokenCache());
	}

	@Override
	public boolean deleteMenu() {
		return MenuManager.deleteMenu(voucherCache.getAccessTokenCache());
	}

	@Override
	public Integer queryCountByPid(Integer pid) {
		return menuInfoDao.queryCountByPid(pid);
	}

	@Override
	public Integer delMenu(Map<String,Object> params) {
		return menuInfoDao.delMenu(params);
	}

}
