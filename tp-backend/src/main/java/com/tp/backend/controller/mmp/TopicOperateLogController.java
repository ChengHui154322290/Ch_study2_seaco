package com.tp.backend.controller.mmp;

import com.tp.dto.common.ResultInfo;
import com.tp.model.mmp.TopicOperateLog;
import com.tp.proxy.mmp.TopicOperateLogProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * Created by ldr on 9/19/2016.
 */
@Controller
@RequestMapping("/mmp")
public class TopicOperateLogController {

    @Autowired
    private TopicOperateLogProxy topicOperateLogProxy;

    @RequestMapping("/topicOperateLog")
    public String showLog(HttpServletRequest request, Model model,Long topicId){

        TopicOperateLog query = new TopicOperateLog();
        query.setTopicId(topicId== null? -1: topicId);
        ResultInfo<List<TopicOperateLog>> result = topicOperateLogProxy.queryByObject(query);
        if(result.isSuccess()&& result.getData()!=null){
            Collections.reverse(result.getData());

        }


        model.addAttribute("result",result);
        return "promotion/topicOperateLog";
    }
}
