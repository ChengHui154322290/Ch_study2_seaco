package com.tp.service.ord.remote;

import java.util.List;

import com.tp.dto.stg.ResultOrderDeliverDTO;
import com.tp.model.ord.OrderDelivery;


public interface ISupplierDeliveryService {

    ResultOrderDeliverDTO batchDelivery(List<OrderDelivery> deliverDTOs);

}
