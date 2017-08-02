package com.tp.service.usr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.usr.SysMenuDao;
import com.tp.model.usr.SysMenu;
import com.tp.service.BaseService;
import com.tp.service.usr.ISysMenuService;

@Service
public class SysMenuService extends BaseService<SysMenu> implements ISysMenuService {

	@Autowired
	private SysMenuDao sysMenuDao;
	
	@Override
	public BaseDao<SysMenu> getDao() {
		return sysMenuDao;
	}
	@Override
	public SysMenu save(SysMenu sysMenu){
		sysMenu.setUpdateTime(new Date());
		sysMenuDao.insert(sysMenu);
		return sysMenu;
	}
	
	@Override
	public List<SysMenu> findListByParentIds(List<SysMenu> list){
		return this.sysMenuDao.findListByParentIds(list);
	}
	
	@Override
	public List<SysMenu> findListByIds(List<Long> list){
		return this.sysMenuDao.findListByIds(list);
	}
	
	
	@Override
	public List<SysMenu> findParentMenu(){
		return this.sysMenuDao.findParentMenu();
	}
	
	@Override
	public List<SysMenu> selectByIds(List<Long> ids){
		if(CollectionUtils.isEmpty(ids)){
			return new ArrayList<SysMenu>();
		}
		return sysMenuDao.selectByIds(ids);
	}
	@Override
	public List<SysMenu> queryByParamForUrlIsNull(SysMenu sysMenu) {
		return sysMenuDao.selectDynamicForUrlIsNull(sysMenu);
	}
}
