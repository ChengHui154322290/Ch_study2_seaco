package com.tp.shop.ao.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.ResultInfo;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.query.system.QueryAppManage;
import com.tp.m.vo.system.AppManageVO;
import com.tp.model.app.VersionInfo;
import com.tp.proxy.app.VersionInfoProxy;
import com.tp.shop.helper.RequestHelper;
import com.tp.shop.helper.VersionHelper;

/**
 * APP管理业务层
 * @author zhuss
 * @2016年4月8日 下午5:23:42
 */
@Service
public class AppManageAO {
	
	private static Logger log = LoggerFactory.getLogger(AppManageAO.class);

	@Autowired
	private VersionInfoProxy appVersionProxy;
	
	
	public MResultVO<AppManageVO> queryVersionIsNew(QueryAppManage appManage){
		try{
			VersionInfo versionInfo = new VersionInfo();
			versionInfo.setPlatform(RequestHelper.getPlatformByName(appManage.getApptype()).code);
			versionInfo.setStatus(1);
			versionInfo.setIsNew(1);
			ResultInfo<VersionInfo> result = appVersionProxy.queryUniqueByObject(versionInfo);
			if(result.success){
				VersionInfo v = result.getData();
				AppManageVO vo = new AppManageVO();
				if(null != v){
					vo.setIsnew(VersionHelper.compareVersion(v.getVersion(), appManage.getAppversion()));
					vo.setDownurl(v.getDownUrl());
					vo.setContent(v.getRemark());
					return new MResultVO<>(MResultInfo.SUCCESS,vo);
				}
			}
			return new MResultVO<>(MResultInfo.PARAM_ERROR);
		}catch(Exception e){
			log.error("[API接口 - 查询当前平台的版本是否最新 Exception] = {}",e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
}
