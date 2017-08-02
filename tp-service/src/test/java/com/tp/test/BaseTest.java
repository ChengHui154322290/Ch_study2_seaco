package com.tp.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tp.common.util.mmp.BeanUtil;

/**
 * Created by ldr on 2015/12/30.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:beans.xml"})
public class BaseTest {

    public <T> void processNullField(T t) {
        BeanUtil.processNullField(t);
    }
}
