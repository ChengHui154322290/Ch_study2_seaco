package com.tp.proxy.usr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.usr.SysMenuLimit;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.usr.ISysMenuLimitService;
/**
 * 权限表代理层
 * @author szy
 *
 */
@Service
public class SysMenuLimitProxy extends BaseProxy<SysMenuLimit>{

	@Autowired
	private ISysMenuLimitService sysMenuLimitService;

	@Override
	public IBaseService<SysMenuLimit> getService() {
		return sysMenuLimitService;
	}

	public List<SysMenuLimit> findSysMenuLimitIds(List<Long> ids){
		return this.sysMenuLimitService.selectByIds(ids);
	}
	
	
	public SysMenuLimit findById(Long id){
		return this.sysMenuLimitService.queryById(id);
	}
	
	
	public List<SysMenuLimit> findBySysMenuId(Long sysMenuid){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sysMenuid", sysMenuid);
		return this.sysMenuLimitService.queryByParam(params);
	}
	
}
