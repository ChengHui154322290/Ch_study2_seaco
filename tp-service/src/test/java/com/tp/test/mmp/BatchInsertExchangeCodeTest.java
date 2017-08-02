package com.tp.test.mmp;

import com.tp.dao.mmp.ExchangeCouponChannelCodeDao;
import com.tp.model.mmp.ExchangeCouponChannelCode;
import com.tp.test.BaseTest;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by ldr on 2016/1/12.
 */
public class BatchInsertExchangeCodeTest extends BaseTest {

    @Autowired
    ExchangeCouponChannelCodeDao exchangeCouponChannelCodeDao;

    @Test
    public void testB() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        List<ExchangeCouponChannelCode> list = new ArrayList<>();
        ExchangeCouponChannelCode exchangeCouponChannelCode = new ExchangeCouponChannelCode();
        exchangeCouponChannelCode.setCouponId(111111L);
        exchangeCouponChannelCode.setVersionCode("1111111111");
        exchangeCouponChannelCode.setCreateUser("");
        exchangeCouponChannelCode.setStatus(1);
        exchangeCouponChannelCode.setActId(22L);
        exchangeCouponChannelCode.setCreateTime(new Date());
        exchangeCouponChannelCode.setExchangeCode("----------");
        exchangeCouponChannelCode.setUpdateTime(new Date());
        exchangeCouponChannelCode.setUpdateUser("");
        list.add(exchangeCouponChannelCode);
        list.add(((ExchangeCouponChannelCode)BeanUtils.cloneBean(exchangeCouponChannelCode)));
        list.add((ExchangeCouponChannelCode)BeanUtils.cloneBean(exchangeCouponChannelCode));
        list.add((ExchangeCouponChannelCode)BeanUtils.cloneBean(exchangeCouponChannelCode));

        Map<String,Object> param = new HashMap<String,Object>();
        param.put("list",list);

        exchangeCouponChannelCodeDao.batchInsert(list);
    }

}
