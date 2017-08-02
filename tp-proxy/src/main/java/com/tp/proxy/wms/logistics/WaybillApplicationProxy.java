package com.tp.proxy.wms.logistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.wms.WmsConstant;
import com.tp.common.vo.wms.WmsWaybillConstant;
import com.tp.common.vo.wms.WmsConstant.ExpressCompany;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.WaybillApplication;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.wms.logistics.IWaybillApplicationService;
import com.tp.util.StringUtil;
/**
 * 运单号申请表代理层
 * @author szy
 *
 */
@Service
public class WaybillApplicationProxy extends BaseProxy<WaybillApplication>{

	@Autowired
	private IWaybillApplicationService waybillApplicationService;

	@Override
	public IBaseService<WaybillApplication> getService() {
		return waybillApplicationService;
	}
	
	//申请运单
	public ResultInfo<Boolean> apply(WaybillApplication application){
		if (StringUtil.isEmpty(application.getLogisticsCode())) {
			return new ResultInfo<>(new FailInfo("物流公司编号为空"));
		}
		ExpressCompany company = ExpressCompany.getCompanyByCommonCode(application.getLogisticsCode());  //改造
		if (null == company) {
			return new ResultInfo<>(new FailInfo("物流公司不支持批量申请"));
		}
		if(null == application.getAmount() || application.getAmount() <= 0){
			return new ResultInfo<>(new FailInfo("申请数量为空或小于0"));
		}
		return waybillApplicationService.applyWaybills(company, application.getAmount().intValue());
	}
}
