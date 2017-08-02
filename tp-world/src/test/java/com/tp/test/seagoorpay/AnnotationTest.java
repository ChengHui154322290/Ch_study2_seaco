package com.tp.test.seagoorpay;

import com.tp.m.query.seagoorpay.SeagoorPayRefundQueryVO;
import com.tp.m.query.seagoorpay.SeagoorPayRefundVO;
import com.tp.world.helper.SeagoorPayHelper;

/**
 * Created by ldr on 2016/11/26.
 */
public class AnnotationTest {

    public static void main(String args[]) throws IllegalAccessException {

//        SeagoorPayBaseVO vo =new SeagoorPayBaseVO();
//        vo.setMerchant_id("3333333333333");
//        vo.setSign("");
//
//
//        SeagoorPayOrderVO order = new SeagoorPayOrderVO();
//        order.setMer_trade_code("pay_order"+String.valueOf(Math.random()));
//        order.setIp("32");
//        order.setMerchant_id("234234");
//      //  order.setItem_tag("");
//        order.setItem_desc("大杯奶茶");
//      //  order.setItem_detail("");
//       // order.setAttach("");
//       // order.setDevice_info("2楼1号收银台");
//        order.setPay_code("012678901234567890122345678901");
//        order.setRand_str("r");
//        order.setTotal_fee(200);
//        order.setSign(SeagoorPayHelper.sign(order,"sign"));
//
//       // vo.setRand_str("  eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee ");
//
//        Field [] fields = vo.getClass().getDeclaredFields();
//
//        System.out.println("check_result ="+SeagoorPayHelper.checkParam(order));



        SeagoorPayRefundVO order = new SeagoorPayRefundVO();
      //  order.setMer_trade_code("pay_order0.4123339609382036");
        //order.setPayment_code("6100112509999939");
        order.setIp("33");
        order.setMerchant_id("234234");
        order.setMer_refund_code("refund_"+String.valueOf(Math.random()));
        order.setDevice_info("2楼1号收银台");
        order.setRefund_fee(180);
        order.setRand_str("r");
        order.setTotal_fee(1000);
        order.setOperator_id("ss");
        order.setSign(SeagoorPayHelper.sign(order,"sign"));

        System.out.println(SeagoorPayHelper.checkParam(order));


        SeagoorPayRefundQueryVO vo = new SeagoorPayRefundQueryVO();

        vo.setMerchant_id("33");
        vo.setRand_str("3");
        vo.setSign("33");
      //  vo.setMer_refund_code("d");
      //  vo.setMer_refund_code("2");
       // vo.setPayment_code("d");
        vo.setRefund_code("d");
        System.out.println("SeagoorPayRefundQueryVO"+SeagoorPayHelper.checkParam(vo));


    }
}
