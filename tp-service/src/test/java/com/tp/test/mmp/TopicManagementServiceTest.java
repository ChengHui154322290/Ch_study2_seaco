package com.tp.test.mmp;

import com.alibaba.fastjson.JSON;
import com.tp.dao.mmp.TopicItemDao;
import com.tp.dto.common.ResultInfo;
import com.tp.model.mmp.Topic;
import com.tp.service.mmp.ITopicManagementService;
import com.tp.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ldr on 2015/12/31.
 */
public class TopicManagementServiceTest extends BaseTest {

    @Autowired
    private ITopicManagementService topicManagementService;

    @Autowired
    private TopicItemDao topicItemDao;
    @Test
    public void testAdd(){

        Topic topic = new Topic();
        topic.setName("专场test"  + new Random().nextInt());
        processNullField(topic);
        ResultInfo resultInfo = topicManagementService.createTopic(topic );
        System.out.println(JSON.toJSONString(resultInfo));
    }

    @Test
    public void testCopy() throws Exception {
       ResultInfo resultInfo =topicManagementService.copyTopic(1614L,111L,"sfsdf");

        System.out.println(JSON.toJSONString(resultInfo));


    }
    
    @Test
    public void testDao() {
    	List<String> skus = new ArrayList<>();
    	skus.add("1111");
    	System.out.println(topicItemDao.getTopicItemBySkuList(skus));
    }
}
