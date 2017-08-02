/**
 * 
 */
package com.tp.scheduler.wms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tp.common.vo.wms.WmsWaybillConstant;
import com.tp.common.vo.wms.WmsConstant.ExpressCompany;
import com.tp.dto.common.ResultInfo;
import com.tp.scheduler.AbstractJobRunnable;
import com.tp.scheduler.JobConstant;
import com.tp.service.wms.logistics.IWaybillApplicationService;

/**
 * @author Administrator
 *
 */
@Component
public class CheckWaybillNoJob extends AbstractJobRunnable{

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckWaybillNoJob.class);
    private static final String CURRENT_JOB_PREFIXED = "seawaybillnocheck";
    
	@Autowired
	private JobConstant jobConstant;
	
	@Autowired
	private IWaybillApplicationService waybillApplicationService;
	
	@Override
	public void execute() {
		LOGGER.info("检查物流公司快递订单号数量begin......");
        String[] companyCodes = jobConstant.getExpressCompanyCode();
        if (companyCodes != null) {
			for (String code : companyCodes) {
				try {
					ExpressCompany company = ExpressCompany.getCompanyByCommonCode(code);
					if (null == company) {
						LOGGER.info("物流公司编号错误：{}", code);
						continue;
					}
					ResultInfo<Integer> countResult = waybillApplicationService.queryUnUsedWaybillNoCount(company);
					if (!countResult.isSuccess()) {
						LOGGER.error("查询未使用运单数量失败：" + countResult.getMsg().getDetailMessage());
						continue;
					}
					Integer count = countResult.getData();
					if (count > WmsWaybillConstant.MIN_WAYBILL_COUNT) {
						LOGGER.info("物流公司-" + company.desc + "存在可用运单" + count + "个");
						continue;
					}
					ResultInfo<Boolean> applyResult = waybillApplicationService.applyWaybills(company, WmsWaybillConstant.SINGLE_APPLY_WAYBILL_COUNT);
					if (!applyResult.isSuccess()) {
						LOGGER.error("物流公司-" + company.desc + "批量申请运单号失败：" + applyResult.getMsg().getDetailMessage());
						continue;
					}
					LOGGER.error("物流公司-" + company.desc + "批量申请运单号成功");
				} catch (Exception e) {
					LOGGER.error("检查物流公司快递订单号数量异常", e);
				}
			}
		}
        LOGGER.info("检查物流公司快递订单号数量end......");
	}

	@Override
	public String getFixed() {
		return CURRENT_JOB_PREFIXED;
	}
}
