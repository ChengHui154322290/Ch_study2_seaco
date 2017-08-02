package com.tp.test.wx;

import com.tp.model.pay.RefundPayinfo;
import com.tp.service.pay.IPayPlatformService;
import com.tp.service.pay.IRefundPayinfoService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ldr on 8/23/2016.
 */
public class WeixinExternalPayTest {

    @Autowired
    private IPayPlatformService weixinExternalPayPlatformService;

    @Autowired
    private IRefundPayinfoService refundPayinfoService;

    public void testRefund(){


    }
}
