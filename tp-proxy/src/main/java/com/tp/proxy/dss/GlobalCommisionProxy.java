package com.tp.proxy.dss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.dss.GlobalCommision;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.dss.IGlobalCommisionService;
/**
 * 全局佣金设置表代理层
 * @author szy
 *
 */
@Service
public class GlobalCommisionProxy extends BaseProxy<GlobalCommision>{

	@Autowired
	private IGlobalCommisionService globalCommisionService;

	@Override
	public IBaseService<GlobalCommision> getService() {
		return globalCommisionService;
	}
	
	public ResultInfo<GlobalCommision> queryLastGlobalCommision(){
		try{
			GlobalCommision globalCommision = globalCommisionService.queryLastGlobalCommision();
			if(globalCommision==null){
				globalCommision = new GlobalCommision();
			}
			return new ResultInfo<GlobalCommision>(globalCommision);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger);
			return new ResultInfo<>(failInfo);
		}
	}
}
