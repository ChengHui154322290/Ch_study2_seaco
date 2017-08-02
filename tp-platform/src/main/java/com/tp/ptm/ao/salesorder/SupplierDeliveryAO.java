package com.tp.ptm.ao.salesorder;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.stg.ResultOrderDeliverDTO;
import com.tp.model.ord.OrderDelivery;
import com.tp.service.ord.remote.ISupplierDeliveryService;

/**
 * {class_description} <br>
 * Create on : 2015年11月30日 下午3:41:35<br>
 *
 * @author Administrator<br>
 * @version platform-front v0.0.1
 */
@Service
public class SupplierDeliveryAO {

    @Autowired
    private ISupplierDeliveryService supplierDeliveryService;

    public ResultOrderDeliverDTO batchDelivery(final List<OrderDelivery> deliverDTOs) {
        return supplierDeliveryService.batchDelivery(deliverDTOs);
    }

}
