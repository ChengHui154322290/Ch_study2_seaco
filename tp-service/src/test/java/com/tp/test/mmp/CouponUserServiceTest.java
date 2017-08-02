package com.tp.test.mmp;

import com.alibaba.fastjson.JSON;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.CouponReceiveDTO;
import com.tp.dto.mmp.MyCouponDTO;
import com.tp.dto.mmp.enums.CouponSendType;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.service.mmp.ICouponUserService;
import com.tp.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldr on 2016/1/7.
 */
public class CouponUserServiceTest extends BaseTest {

    @Autowired
    private ICouponUserService couponUserService;


    @Test
    public void testAutoIssue(){

      ResultInfo resultInfo = couponUserService.newUserCoupon("15618936792");
        System.out.println(JSON.toJSONString(resultInfo));

    }

    @Test
    public void testMyC() throws Exception {
       PageInfo<MyCouponDTO> result =  couponUserService.myCoupon(68647L,null,0,1,10);
        for(MyCouponDTO myCouponDTO :result.getRows()){
            System.out.println(JSON.toJSONString(myCouponDTO));
        }
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void testChangeStatus() throws Exception {
        List<Long> ids = new ArrayList<>();
        ids.add(2563669L);
        ids.add(2563670L);
        couponUserService.updateCouponUserStatus(ids, CouponUserStatus.NORMAL.ordinal());

    }
    @Test
    public void testShareSendcoupon(){
    	couponUserService.sendAutoCouponBySendType("18521516574", CouponSendType.AUTO_SHARE);
    }

    @Test
    public void testReceive(){

        CouponReceiveDTO couponReceiveDTO = new CouponReceiveDTO();
        couponReceiveDTO.setMobile("15618936792");
        couponReceiveDTO.setCode("374B62BC8020CF1B");
        ResultInfo resultInfo =  couponUserService.receiveCoupon(couponReceiveDTO);
        System.out.println(JSON.toJSONString(resultInfo));
    }

    @Test
    public void testReceiveWithoutReg(){

        CouponReceiveDTO couponReceiveDTO = new CouponReceiveDTO();
        couponReceiveDTO.setMobile("15800917996");
        couponReceiveDTO.setCode("374B62BC8020CF1B");
        ResultInfo resultInfo =  couponUserService.receiveCoupon(couponReceiveDTO);
        System.out.println(JSON.toJSONString(resultInfo));
    }

}
