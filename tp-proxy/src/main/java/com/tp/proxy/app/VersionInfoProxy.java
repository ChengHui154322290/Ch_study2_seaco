package com.tp.proxy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.app.VersionConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.exception.AppServiceException;
import com.tp.model.app.VersionInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.IVersionInfoService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class VersionInfoProxy extends BaseProxy<VersionInfo>{

	@Autowired
	private IVersionInfoService versionService;

	@Override
	public IBaseService<VersionInfo> getService() {
		return versionService;
	}
	
	public ResultInfo<?> save(VersionInfo versionInfo){
		VersionInfo v = new VersionInfo();
		v.setPlatform(versionInfo.getPlatform());
		v.setVersion(versionInfo.getVersion());
		//如果是最新，则需要把该平台下其他版本设置为非最新
		if(null != versionInfo.getIsNew() && versionInfo.getIsNew() == 1){
			/*Integer upNew = versionService.updateIsNewByPlatform(versionInfo.getPlatform());
			if(upNew < 1) throw new AppServiceException("更新最新错误");*/
			versionService.updateIsNewByPlatform(versionInfo.getPlatform());
		}
		if(versionInfo.getId()==null){
			versionInfo.validate(versionInfo);
			//验证同一平台的版本号不能有重复
			ResultInfo<VersionInfo> r = queryUniqueByObject(v);
			if(null != r.getData())throw new AppServiceException("同一平台不允许重复版本号");
			versionInfo.setCreateTime(versionInfo.getModifyTime());
			versionInfo.setCreateUser(versionInfo.getModifyUser());
			versionInfo.setStatus(VersionConstant.VERSION_STATUS.NO.getCode());
			versionInfo.setIsDel(0);
			return insert(versionInfo);
		}else{
			return updateNotNullById(versionInfo);
		}
	}
}
