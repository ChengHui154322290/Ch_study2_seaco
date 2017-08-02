package com.tp.proxy.app;

import com.tp.dto.common.ResultInfo;
import com.tp.model.app.DynamicConfiguration;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.IBaseService;
import com.tp.service.app.IDynamicConfigurationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class DynamicConfigurationProxy extends BaseProxy<DynamicConfiguration>{

	@Autowired
	private IDynamicConfigurationService dynamicConfigurationService;

	@Override
	public IBaseService<DynamicConfiguration> getService() {
		return dynamicConfigurationService;
	}

	public ResultInfo<DynamicConfiguration>  queryByVersion(DynamicConfiguration query){
		ResultInfo<DynamicConfiguration> result = new ResultInfo<>();
		this.execute(result, () -> {
            DynamicConfiguration dynamicConfiguration = dynamicConfigurationService.queryByVersion(query);
            result.setData(dynamicConfiguration);
        });
		return result;
	}
}
