package com.tp.proxy.mmp;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.mq.PromotionItemMqDto;
import com.tp.proxy.mmp.callBack.AbstractProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.mmp.ITopicItemService;
import com.tp.service.prd.IItemService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ldr on 2016/11/19.
 */
@Service
public class TopicItemSynProxy extends AbstractProxy {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IItemService itemService;
    @Autowired
    private ITopicItemService topicItemService;

    public ResultInfo syn(Integer auto, final Date date){
        ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                Date update = date;
                if(auto != null && auto ==1){
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.MINUTE,-5);
                    System.out.println(calendar.getTime());
                    update = calendar.getTime();
                }

              List<PromotionItemMqDto> dtos =  itemService.queryItemByUpdateTime(update);
                int count = topicItemService.updateTopicItemByUpdatedItemInfoVersion2(dtos);
                logger.info("SYN_ITEM_INFO_TO_PROMOTION.COUNT="+count);
            }
        });
        return result;
    }


}
