package com.tp.test.mmp;

import com.alibaba.fastjson.JSON;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.*;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.proxy.mmp.facade.CouponFacadeProxy;
import com.tp.query.mmp.CouponOrderQuery;
import com.tp.query.mmp.MyCouponQuery;
import com.tp.test.BaseTest;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ldr on 2016/1/11.
 */
public class CouponFacadeProxyTest extends BaseTest {

    @Autowired
    private CouponFacadeProxy couponFacadeProxy;

    @Test
    public void testMyCoupon() {
        MyCouponQuery query = new MyCouponQuery();
        query.setMemberId(68647L);
        query.setCouponUserStatus(CouponUserStatus.NORMAL);
        ResultInfo<PageInfo<MyCouponDTO>> resultInfo = couponFacadeProxy.myCoupon(query);
        for (MyCouponDTO myCouponDTO : resultInfo.getData().getRows()) {
            System.out.println(JSON.toJSONString(myCouponDTO));
        }
        System.out.println(JSON.toJSONString(resultInfo));
        Assert.assertTrue(resultInfo.isSuccess());
    }

    @Test
    public void testExchange() {
        ExchangeCouponCodeDTO code = new ExchangeCouponCodeDTO();
        code.setUserId(68912L);
        code.setExchangeCode("A86D4B6097D6E2C1");
        ResultInfo result = couponFacadeProxy.exchangeCouponsCode(code);
        System.out.println(JSON.toJSONString(result));
        Assert.assertTrue(result.isSuccess());

    }

    @Test
    public void myCouponBasic() {
        MyCouponQuery query = new MyCouponQuery();
        query.setMemberId(68647L);
        ResultInfo<MyCouponBasicDTO> result = couponFacadeProxy.myCouponBasicInfo(query);
        System.out.println(JSON.toJSONString(result));
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void testOrderCoupon() {
        CouponOrderQuery query = new CouponOrderQuery();
        query.setUserId(68647L);
        query.setPlatformType(2);
        query.setItemType(1);
        CouponOrderDTO couponOrderDTO = new CouponOrderDTO();
        couponOrderDTO.setItemType(1);
        couponOrderDTO.setBrandId(104L);
        couponOrderDTO.setSku("26030100180101");
        couponOrderDTO.setFirstCategoryId(586L);
        couponOrderDTO.setSecondCategoryId(586L);
        couponOrderDTO.setThordCategoryId(600L);
        couponOrderDTO.setSupplierId(91L);
        couponOrderDTO.setPrice(10D);
        couponOrderDTO.setQuantity(10);
        query.setCouponOrderDTOList(new ArrayList<>(Arrays.asList(couponOrderDTO)));
        ResultInfo<List<OrderCouponDTO>> result = couponFacadeProxy.queryOrderCouponList(query);
        System.out.println(JSON.toJSONString(result));
        for (OrderCouponDTO dto : result.getData()) {
            System.out.println(JSON.toJSONString(dto));
        }

        Assert.assertTrue(result.isSuccess());
    }

}
