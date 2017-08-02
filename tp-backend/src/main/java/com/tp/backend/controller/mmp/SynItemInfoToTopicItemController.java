package com.tp.backend.controller.mmp;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.dto.common.ResultInfo;
import com.tp.proxy.mmp.TopicItemSynProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ldr on 2016/11/19.
 */
@Controller
@RequestMapping("/promotion")
public class SynItemInfoToTopicItemController extends AbstractBaseController{

    @Autowired
    private TopicItemSynProxy topicItemSynProxy;

    @ResponseBody
    @RequestMapping("/syn")
    public ResultInfo syn(){
        return  topicItemSynProxy.syn(null,null);
    }
}
