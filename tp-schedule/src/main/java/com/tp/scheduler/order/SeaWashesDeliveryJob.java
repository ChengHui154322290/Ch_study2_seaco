package com.tp.scheduler.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.vo.OrderConstant.ClearanceStatus;
import com.tp.common.vo.OrderConstant.PutCustomsStatus;
import com.tp.model.ord.SubOrder;
import com.tp.scheduler.AbstractJobRunnable;
import com.tp.service.ord.remote.ISalesOrderRemoteService;

@Component
public class SeaWashesDeliveryJob extends AbstractJobRunnable {
    private static final Logger LOG = LoggerFactory.getLogger(SeaWashesDeliveryJob.class);
    private static final String CURRENT_JOB_PREFIXED = "seawashesdelivery";

    @Autowired
    private ISalesOrderRemoteService salesOrderRemoteService;
    
    @Override
    @Transactional
    public void execute() {
        LOG.info("海淘保税区订单清关通过后推送到仓库begin......");
        int size = 0;
        do {
            size = 0;
            Map<String, Object> params = new HashMap<>();
            params.put("clearanceStatus", ClearanceStatus.AUDIT_SUCCESS.code);
            List<SubOrder> subOrderList = salesOrderRemoteService.querySubOrderByWaitPutSeaWashes(params);
            if (CollectionUtils.isNotEmpty(subOrderList)) {
                size = subOrderList.size();
                for (SubOrder subOrderDO : subOrderList) {
                	if (subOrderDO.getPutStorage()) {
                		salesOrderRemoteService.putWareHouseShippingBySeaSubOrder(subOrderDO);
					}
                }
            }
        } while (size > 0);
        LOG.info("海淘保税区订单清关通过后推送到仓库end......");
    }

    @Override
    public String getFixed() {
        return CURRENT_JOB_PREFIXED;
    }
}
