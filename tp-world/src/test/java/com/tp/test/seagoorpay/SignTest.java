package com.tp.test.seagoorpay;

import com.tp.m.query.seagoorpay.SeagoorPayOrderVO;
import com.tp.world.helper.SeagoorPayHelper;

import java.util.UUID;

/**
 * Created by ldr on 2016/11/24.
 */
public class SignTest {

    public static void main(String []a){
        SeagoorPayOrderVO order = new SeagoorPayOrderVO();
        order.setMer_trade_code("1000000001");
        order.setIp("");
        order.setMerchant_id("234234");
        order.setItem_tag("");
      //  order.setItem_desc("大杯奶茶");
        order.setItem_detail("");
        order.setAttach("");
       // order.setDevice_info("蜜果奶茶西客商城店");
        order.setPay_code("65271789067456");
        order.setRand_str(SeagoorPayHelper.getRandStr());

        order.setTotal_fee(1000);
        System.out.println(SeagoorPayHelper.sign(order,"33b1cd77e5364a028b7012bd75d51607"));

        System.out.println(UUID.randomUUID().toString().replaceAll("-",""));

        System.out.println(Math.random());
    }

}
