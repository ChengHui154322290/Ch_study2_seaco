package com.tp.test.seagoorpay;

import com.alibaba.fastjson.JSON;
import com.tp.m.query.seagoorpay.*;
import com.tp.m.util.JsonUtil;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.test.base.BaseTest;
import com.tp.world.helper.SeagoorPayHelper;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by ldr on 2016/11/23.
 */
public class SeagoorPayControllerTest  extends BaseTest {

    @Autowired
    private JedisCacheUtil jedisCacheUtil;



    @Test
    public void pay() throws Exception {
        SeagoorPayOrderVO order = new SeagoorPayOrderVO();
        order.setMer_trade_code("1000000001");
        //order.setIp("192.168.2.56");
        order.setMerchant_id("234234");
       // order.setItem_desc("奶茶");
       // order.setItem_detail("蜜果奶茶");
       // order.setDevice_info("2楼1号收银台");
        order.setPay_code("65271789067456");
        order.setRand_str("0.8542095736038015");
        order.setTotal_fee(1000);
        System.out.println(SeagoorPayHelper.sign(order,"33b1cd77e5364a028b7012bd75d51607"));
        order.setSign(SeagoorPayHelper.sign(order,"33b1cd77e5364a028b7012bd75d51607"));
        System.out.println(JSON.toJSONString(order));



        String json = JsonUtil.convertObjToStr(order);
        mockMvc.perform(
                (post("/seagoorpay/pay.htm").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(json
                                .getBytes()))).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void paystatusquery() throws Exception {
        QuerySeagoorPayCode order = new QuerySeagoorPayCode();
        order.setCode("600717538780095287");
        order.setToken("70d5b8f53b4e847be17df1a3b3e4684d");


        String json = JsonUtil.convertObjToStr(order);
        mockMvc.perform(
                (post("/seagoorpay/querypaystatus.htm").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(json
                                .getBytes()))).andExpect(status().isOk())
                .andDo(print());
    }




    @Test
    public void refund() throws Exception {
        SeagoorPayRefundVO order = new SeagoorPayRefundVO();
        //order.setMer_trade_code("pay_order0.4123339609382036");
        order.setPayment_code("6100112810000042");
        order.setIp("222");
        order.setMerchant_id("234234");
        order.setMer_refund_code("refund_"+String.valueOf(Math.random()));
        order.setDevice_info("2楼1号收银台");
        order.setRefund_fee(180);
        order.setRand_str("r");
        order.setTotal_fee(1000);
        order.setOperator_id("ss");
        order.setSign(SeagoorPayHelper.sign(order,"sign"));

        String json = JsonUtil.convertObjToStr(order);
        mockMvc.perform(
                (post("/seagoorpay/refund.htm").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(json
                                .getBytes()))).andExpect(status().isOk())
                .andDo(print());
    }



    @Test
    public void querypay() throws Exception {
        SeagoorPayQueryVO order = new SeagoorPayQueryVO();
       // order.setMer_trade_code("pay_order0.4123339609382036");
         order.setMerchant_id("234234");
        order.setPayment_code("6100112509999939");
        order.setRand_str("r");
        order.setSign(SeagoorPayHelper.sign(order,"sign"));
      //  jedisCacheUtil.setCache("12312312123",2L);


        String json = JsonUtil.convertObjToStr(order);
        mockMvc.perform(
                (post("/seagoorpay/querypay.htm").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(json
                                .getBytes()))).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void queryrefund() throws Exception {
        SeagoorPayRefundQueryVO refund = new SeagoorPayRefundQueryVO();
        // order.setMer_trade_code("pay_order0.4123339609382036");
        refund.setMerchant_id("234234");
        refund.setPayment_code("6100112509999939");
     //   refund.setMer_refund_code("refund_0.3804341599258271");
        refund.setRand_str("r");
        refund.setSign(SeagoorPayHelper.sign(refund,"sign"));
        //  jedisCacheUtil.setCache("12312312123",2L);


        String json = JsonUtil.convertObjToStr(refund);
        mockMvc.perform(
                (post("/seagoorpay/queryrefund.htm").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(json
                                .getBytes()))).andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void queryrefundList() throws Exception {
        SeagoorPayRefundQueryListVO refund = new SeagoorPayRefundQueryListVO();
        // order.setMer_trade_code("pay_order0.4123339609382036");
        refund.setMerchant_id("234234");
       // refund.setPayment_code("6100112509999939");
        //   refund.setMer_refund_code("refund_0.3804341599258271");
        refund.setRand_str("r");
        refund.setPage_size(8);
       // refund.setStatus(3);
        refund.setBegin_time("2016-11-23 16:38:41");
       // refund.setEnd_time("2016-11-23 18:38:41");
//        refund.setMer_trade_code("pay_order0.4123339609382036");
//        refund.setMer_refund_code("refund_0.3804341599258271");
        //refund.setPayment_code("6100112509999939");
        refund.setRefund_code("6200112550000038");
        refund.setSign(SeagoorPayHelper.sign(refund,"sign"));
        //  jedisCacheUtil.setCache("12312312123",2L);

        String json = JsonUtil.convertObjToStr(refund);
        mockMvc.perform(
                (post("/seagoorpay/queryrefundlist.htm").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(json
                                .getBytes()))).andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void queryPayList() throws Exception {
        SeagoorPayQueryListVO refund = new SeagoorPayQueryListVO();
        // order.setMer_trade_code("pay_order0.4123339609382036");
        refund.setMerchant_id("234234");

        // refund.setPayment_code("6100112509999939");
        //   refund.setMer_refund_code("refund_0.3804341599258271");
        refund.setRand_str("r");
        refund.setPage_size(8);
        //refund.setMer_trade_code("pay_order0.4123339609382036");
      //  refund.setPayment_code("6100112509999939");
     //   refund.setStatus(2);
        refund.setBegin_time("2016-11-23 17:38:41");
       // refund.setEnd_time("2016-11-23 17:38:42");
        refund.setSign(SeagoorPayHelper.sign(refund,"sign"));
        //  jedisCacheUtil.setCache("12312312123",2L);

        String json = JsonUtil.convertObjToStr(refund);
        mockMvc.perform(
                (post("/seagoorpay/querypaylist.htm").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(json
                                .getBytes()))).andExpect(status().isOk())
                .andDo(print());
    }



}
