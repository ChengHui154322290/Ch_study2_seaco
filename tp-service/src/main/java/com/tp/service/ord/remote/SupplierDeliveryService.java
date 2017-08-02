package com.tp.service.ord.remote;



import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ptm.ResultMessage;
import com.tp.common.util.ptm.ValidateUtils;
import com.tp.dto.OrderOperatorErrorDTO;
import com.tp.dto.stg.ResultOrderDeliverDTO;
import com.tp.exception.ErrorCodes;
import com.tp.exception.OrderServiceException;
import com.tp.model.ord.OrderDelivery;
import com.tp.service.ord.remote.ISalesOrderRemoteService;
import com.tp.service.ord.remote.ISupplierDeliveryService;

@Service("supplierDeliveryService")
public class SupplierDeliveryService implements ISupplierDeliveryService {

    private static Logger LOGGER = LoggerFactory.getLogger(SupplierDeliveryService.class);

    @Autowired
    private ISalesOrderRemoteService salesOrderRemoteService;

    @Override
    public ResultOrderDeliverDTO batchDelivery(final List<OrderDelivery> deliverDTOs) {
        ResultOrderDeliverDTO resultOrderDeliverDTO = new ResultOrderDeliverDTO();

        Integer errorSize = 0;
        List<OrderDelivery> errorDataList = new ArrayList<OrderDelivery>();
        List<OrderOperatorErrorDTO> errorOperatorList = new ArrayList<OrderOperatorErrorDTO>();

        for (OrderDelivery orderDeliverDTO : deliverDTOs) {
            boolean hasError = false;
            String errorMsg = "";
            int errCode = ErrorCodes.SYSTEM_ERROR;

            try {
                ResultMessage message = ValidateUtils.validate(orderDeliverDTO);
                if (message.getResult() == ResultMessage.FAIL) {
                    hasError = true;
                    errCode = ErrorCodes.VERIFY_ARGUMENT_INFO_ERROR;
                    errorMsg = message.getMessage();
                }

                salesOrderRemoteService.operateOrderForDeliver(orderDeliverDTO);

            } catch (OrderServiceException e) {
                LOGGER.error("nofity salesorder system error > {} ", e.getMessage());
                hasError = true;
                errCode = e.getErrorCode();
                String msg = e.getMessage();
                if (StringUtils.isNotBlank(msg) && msg.equals("NOTIFY-ORDER")) {
                    errorMsg = "服务器异常，请稍后再试 【NOTIFY-ORDER】";
                }
            } catch (Exception e) {
                LOGGER.error("batch output order occur error code = {} error = {}", orderDeliverDTO.getOrderCode(),
                    e.getMessage());
                hasError = true;
                errCode = ErrorCodes.SYSTEM_ERROR;
                errorMsg = "服务器异常，请稍后再试";
            }

            if (hasError) {
                errorSize++;
                errorDataList.add(orderDeliverDTO);

                errorOperatorList.add(new OrderOperatorErrorDTO(orderDeliverDTO.getOrderCode(), errCode, errorMsg));
            }
        }

        resultOrderDeliverDTO.setErrorDataList(errorDataList);
        resultOrderDeliverDTO.setErrorSize(errorSize);
        resultOrderDeliverDTO.setOrderOperatorErrorList(errorOperatorList);

        return resultOrderDeliverDTO;
    }
}
