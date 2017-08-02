package com.tp.test.wx;

import com.alibaba.fastjson.JSON;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.SubOrder;
import com.tp.service.ord.IOrderConsigneeService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.pay.IPayPlatformService;
import com.tp.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by ldr on 8/9/2016.
 */
public class WeixinPushDeclareOrderServiceTest extends BaseTest {

    @Autowired
    private IPayPlatformService weixinPayPlatformService;

    @Autowired
    ISubOrderService subOrderService;

    @Autowired
    IOrderConsigneeService orderConsigneeService;


    @Autowired
    Map<String, IPayPlatformService> payPlatformServices;

    @Test
    public void testDeclareOrder(){

        SubOrder subOrder = subOrderService.queryById(11608);

        OrderConsignee consignee = orderConsigneeService.selectOneByOrderCode(subOrder.getParentOrderCode());
//400180200120160823204670648200 11320
        //400180200120160823204670648201 11321
        ResultInfo resultInfo  =  payPlatformServices.get("weixinExternalPayPlatformService").declareOrder(subOrder,consignee, null);

        System.out.println(JSON.toJSONString(resultInfo));
    }

    @Test
    public void testDeclareQuery(){
        SubOrder subOrder = subOrderService.queryById(11322);
        ResultInfo resultInfo =   payPlatformServices.get("weixinExternalPayPlatformService").declareQuery(subOrder, ClearanceChannelsEnum.HANGZHOU);
        System.out.println(JSON.toJSONString(resultInfo));
    }
}
