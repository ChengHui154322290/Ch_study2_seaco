/**
 *
 */
package com.tp.backend.controller.mmp;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.mmp.Topic;
import com.tp.proxy.mmp.TopicRelateProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 */
@Controller
public class TopicRelateController extends AbstractBaseController {


    @Autowired
    private TopicRelateProxy topicRelateProxy;

    @RequestMapping(value = "/topicRelate/add", method = RequestMethod.GET)
    public String loadTopicRelates(ModelMap model, HttpServletRequest request) {

        return "promotion/subpages/topicRelation";
    }

    @RequestMapping(value = "/topicRelate/create", method = RequestMethod.POST)
    public String confirmTopicRelate(@RequestParam("topicId") String topicId,@RequestParam("relateTId") String relateTId, ModelMap model, HttpServletRequest request) {

        // TODO:保存至新增REDIS
        return "promotion/subpages/topicRelation";
    }

    @RequestMapping(value = "/topicRelate/{topicId}/selection/{rowIndex}", method = RequestMethod.GET)
    public String selectionTopicRelate(@PathVariable("topicId") String topicId,
                                       @PathVariable("rowIndex") String rowIndex, ModelMap model,
                                       HttpServletRequest request) {

        model.addAttribute("topicId", topicId);
        model.addAttribute("rowIndex", rowIndex);
        return "promotion/topicRelationSearch";
    }

    @RequestMapping(value = "/topicRelate/search", method = RequestMethod.GET)
    public String searchTopicRelate(@RequestParam("topicId") Long topicId,
                                    @RequestParam("number") String number,
                                    @RequestParam("name") String name,
                                    @RequestParam("startPage") Integer startPage,
                                    @RequestParam("pageSize") Integer pageSize, ModelMap model,
                                    HttpServletRequest request) {

        PageInfo<Topic> topicPage = this.topicRelateProxy.getTopicInfoWithoutSpecialId(topicId, number, name, startPage,
                pageSize);
        model.addAttribute("topics", topicPage.getRows());
        model.addAttribute("topicCount", topicPage.getRecords());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currPage", topicPage.getPage());
        model.addAttribute("totalPage", topicPage.getTotal());
        return "promotion/subpages/topicRelationSearchList";
    }

    @RequestMapping(value = "/topicRelate/searchSingle", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo<Topic> searchTopicRelate(@RequestParam("topicId") Long topicId, ModelMap model, HttpServletRequest request) {
        try {
            if (0L == topicId) {
                return new ResultInfo<>(new FailInfo("专场id为空"));
            }
            Topic topic = this.topicRelateProxy.getAuditedTopicById(topicId);
            if (null == topic) {
                return new ResultInfo<>(new FailInfo("没有找到指定主题，请确认主题已通过审批!"));
            }
            return new ResultInfo<>(topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultInfo<>();
    }
}
