package com.tp.test.pay;

import com.tp.dto.pay.AppPayData;
import com.tp.model.pay.PaymentInfo;
import com.tp.service.pay.IPayPlatformService;
import com.tp.service.pay.IPaymentInfoService;
import com.tp.service.pay.IPaymentService;
import com.tp.service.pay.WeixinPayPlatformService;
import com.tp.util.JsonUtil;
import com.tp.test.BaseTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ldr on 8/17/2016.
 */
public class WeiXinPayTest extends BaseTest {
    @Autowired
    private IPayPlatformService weixinPayPlatformService;
    @Autowired
    private IPaymentService paymentService;
    @Autowired
    private IPaymentInfoService paymentInfoService;


    @Test
    public void testPay(){

        Long paymentId = 788L;
        Long userId = 1L;
        PaymentInfo paymentInfo = paymentInfoService.queryById(paymentId);
        System.out.println(paymentInfo);

       AppPayData data =  weixinPayPlatformService.getAppPayData(paymentId,true,userId.toString());
        System.out.println(JsonUtil.convertObjToStr(data));
    }
}
