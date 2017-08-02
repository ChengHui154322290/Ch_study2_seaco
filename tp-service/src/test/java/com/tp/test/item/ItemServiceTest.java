package com.tp.test.item;

import com.alibaba.fastjson.JSON;
import com.tp.dto.prd.mq.PromotionItemMqDto;
import com.tp.service.mmp.ITopicItemService;
import com.tp.service.prd.IItemService;
import com.tp.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ldr on 2016/11/19.
 */
public class ItemServiceTest extends BaseTest {

    @Autowired
    private IItemService itemService;

    @Autowired
    private ITopicItemService topicItemService;

    @Test
    public void testUpdate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,9);
        Date date = calendar.getTime();
        System.out.println(date);

        List<PromotionItemMqDto> dtos = itemService.queryItemByUpdateTime( date);
       int count =  topicItemService.updateTopicItemByUpdatedItemInfoVersion2(dtos);
        System.out.println(count);

        for(PromotionItemMqDto dto :dtos){
            System.out.println(JSON.toJSONString(dto));
        }


    }

}
