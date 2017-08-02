package com.tp.shop.helper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PaymentConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.model.ord.SubOrder;
import com.tp.model.pay.PaymentInfo;
import com.tp.proxy.ord.SubOrderProxy;

/**
 * Created by ldr on 2016/10/18.
 */
@Service
public class OrderHelper {

    @Autowired
    private SubOrderProxy subOrderProxy;

    Logger logger = LoggerFactory.getLogger(this.getClass());


    public boolean hasCOMMON_SEA (PaymentInfo info){
        if (info.getBizType().equals(PaymentConstant.BIZ_TYPE.MERGEORDER.code)) {
            SubOrder subOrder = new SubOrder();
            subOrder.setParentOrderCode(Long.parseLong(info.getPaymentTradeNo()));
            ResultInfo<List<SubOrder>> resultInfo = subOrderProxy.queryByObject(subOrder);
            if(!resultInfo.isSuccess()){
                logger.error("[PAY_ORDER:GET_PAYMENT_INFO_LIST_ERROR.PAYMENT_TRADE_NO=]"+String.valueOf(info.getPaymentTradeNo()));
                throw  new MobileException(MResultInfo.SYSTEM_ERROR);
            }
            if(!CollectionUtils.isEmpty(resultInfo.getData())){
                for(SubOrder t :resultInfo.getData()){
                    if(t.getType().equals(OrderConstant.OrderType.COMMON_SEA.getCode())){
                        return true;
                    }
                }
            }
        }else {
            if(info.getOrderType() != null && info.getOrderType().intValue()==OrderConstant.OrderType.COMMON_SEA.getCode()){
                return true;
            }
        }
        return  false;
    }
}
