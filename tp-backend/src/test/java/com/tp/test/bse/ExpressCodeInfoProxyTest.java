package com.tp.test.bse;

import com.alibaba.fastjson.JSON;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.*;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.model.bse.ExpressInfo;
import com.tp.proxy.bse.ExpressCodeInfoProxy;
import com.tp.proxy.mmp.facade.CouponFacadeProxy;
import com.tp.proxy.ord.RejectInfoProxy;
import com.tp.query.mmp.CouponOrderQuery;
import com.tp.query.mmp.MyCouponQuery;
import com.tp.test.BaseTest;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ExpressCodeInfoProxyTest extends BaseTest {

    @Autowired
    private ExpressCodeInfoProxy expressCodeInfoProxy;

    @Test
    public void testExpress() {
    	 ResultInfo<List<ExpressInfo>> listExpress = expressCodeInfoProxy.selectAllExpressCode();
    	
    	 if( listExpress.getData().isEmpty() ){
        	 System.out.println("空");    		 
    	 }else {
        	 System.out.println("有");    		 			
		}
    }

}
