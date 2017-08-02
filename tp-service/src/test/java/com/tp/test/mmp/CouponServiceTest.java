package com.tp.test.mmp;

import com.tp.model.mmp.Coupon;
import com.tp.service.mmp.ICouponService;
import com.tp.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by ldr on 2016/1/3.
 */
public class CouponServiceTest extends BaseTest {


    @Autowired
    private ICouponService couponService;

    @Test
    public void testCoupon() throws IllegalAccessException {
        Coupon coupon = new Coupon();
        coupon.setCouponName("测试" + new Random().nextInt());
        processNullField(coupon);
        System.out.println();

        couponService.insert(coupon);
        System.out.println(coupon.getId() + "@@@@@@@@ @@@@@@@@@@@@@@@@@@@@@@@@");
    }



}
