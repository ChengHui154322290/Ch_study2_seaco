/**
 * 
 */
package com.tp.service.wms.thirdparty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.stg.WarehouseConstant.WMSCode;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.wms.SendOrderInfo;
import com.tp.service.wms.thirdparty.ISendOrderService;

/**
 * @author Administrator
 *
 */
@Service
public class STOSendOrderService implements ISendOrderService{

	private static final Logger logger = LoggerFactory.getLogger(STOSendOrderService.class);
	
	@Override
	public ResultInfo<Object> send(SendOrderInfo info) {
		logger.info("[STO_SEND_ORDER_TO_WMS]推送入库计划单至仓库成功");
		return new ResultInfo<>();
	}

	@Override
	public boolean check(SendOrderInfo info) {
        if (info != null && info.getWarehouse() != null && 
        		StorageConstant.StorageType.COMMON_SEA.getValue().equals(info.getWarehouse().getType()) &&
        		WMSCode.STO_HZ.code.equals(info.getWarehouse().getWmsCode()))
            return true;
        return false;
	}
}
