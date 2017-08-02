package com.tp.test.mmp;

import com.alibaba.fastjson.JSON;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.ExchangeCouponCodeDTO;
import com.tp.service.mmp.ExchangeCouponChannelCodeService;
import com.tp.service.mmp.IExchangeCouponChannelCodeService;
import com.tp.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ldr on 2016/1/10.
 */
public class ExchangeCouponChannelCodeServiceTest extends BaseTest {


    @Autowired
    private IExchangeCouponChannelCodeService exchangeCouponChannelCodeService;

    @Test
    public void testDoEx(){
        ExchangeCouponCodeDTO exchangeCouponCodeDTO = new ExchangeCouponCodeDTO();
        exchangeCouponCodeDTO.setExchangeCode("7C948E44FB08B992");
        exchangeCouponCodeDTO.setUserId(68647L);
        ResultInfo result = exchangeCouponChannelCodeService.exchangeCouponsCode(exchangeCouponCodeDTO);
        System.out.println(JSON.toJSONString(result));
    }

}
