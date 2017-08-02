package com.tp.backend.controller.qrcode;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.backend.util.PdfUtil;
import com.tp.dto.common.ResultInfo;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicItem;
import com.tp.proxy.mmp.TopicItemProxy;
import com.tp.proxy.mmp.TopicProxy;
import com.tp.util.ErWeiMaUtil;
import com.tp.util.JsonUtil;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by ldr on 9/9/2016.
 */
@Controller
@RequestMapping("/qr")
public class QRController extends AbstractBaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${backend.domain}")
    private String backendDomain;

    @Value("${m.domain}")
    private String mDomain;

    @Autowired
    private TopicProxy topicProxy;

    @Autowired
    private TopicItemProxy topicItemProxy;


    @RequestMapping("/qrHome")
    public String qrHome(Model model) {
        Topic topicQuery = new Topic();
        topicQuery.setStatus(3);
        topicQuery.setDeletion(0);
        ResultInfo<List<Topic>> result = topicProxy.queryByObject(topicQuery);
        if (!result.isSuccess()) {
            logger.error("QR_HOME.LOAD_ALL_TOPIC_ERROR.result=" + JsonUtil.convertObjToStr(result));
        }

        if (!CollectionUtils.isEmpty(result.getData())) {
            List<Topic> topicList = result.getData();
            Collections.sort(topicList, new Comparator<Topic>() {
                @Override
                public int compare(Topic o1, Topic o2) {
                    return o2.getId().compareTo(o1.getId());
                }
            });
            for (Topic topic : result.getData()) {
                String t = topic.getId() + " - " + topic.getName();
                topic.setName(t);
            }

        }
        model.addAttribute("topics", result.getData());
        return "qr/qrHome";
    }

    @RequestMapping("/qrList")
    public String qrList(Model model, HttpServletRequest request) {

        String tid = request.getParameter("tid");
        String v = request.getParameter("v");
        String s = request.getParameter("s");
        if (!NumberUtils.isNumber(tid)) {
            logger.error("QR_LIST.PARAM_ERROR.TID=" + String.valueOf(tid));
        }
        int version = 8;
        int size = 8;
        if (NumberUtils.isNumber(v)) {
            version = Integer.parseInt(v);
        }
        if (NumberUtils.isNumber(s)) {
            size = Integer.parseInt(s);
        }

        Long topicId = Long.parseLong(tid);
        TopicItem topicItemQuery = new TopicItem();
        topicItemQuery.setTopicId(topicId);
        topicItemQuery.setDeletion(0);

        ResultInfo<List<TopicItem>> result = topicItemProxy.queryByObject(topicItemQuery);
        if (!result.isSuccess()) {
            logger.error("QR_LIST.LOAD_QR_LIST_ERROR.RESULT=" + JsonUtil.convertObjToStr(request));
        }
        Collections.sort(result.getData(), new Comparator<TopicItem>() {
            @Override
            public int compare(TopicItem o1, TopicItem o2) {
                return o1.getSortIndex().compareTo(o2.getSortIndex());
            }
        });
        model.addAttribute("v", version);
        model.addAttribute("s", size);
        model.addAttribute("md", mDomain);
        model.addAttribute("bd", backendDomain);
        model.addAttribute("items", result.getData());

        return "qr/qrList";
    }


    @RequestMapping("/{s}/{v}")
    public void qr(
           @PathVariable(value = "s") final String size,
            @PathVariable(value = "v") final String version,
            HttpServletRequest request, HttpServletResponse response) {
        String url = request.getParameter("url");
        int s = size == null ? 8 : NumberUtils.isNumber(size) ? Integer.parseInt(size) : 7;
        int v = version == null ? 8 : NumberUtils.isNumber(version) ? Integer.parseInt(version) : 7;
        try {
            ErWeiMaUtil.encoderQRCode(url, response.getOutputStream(), "png", v, s);
        } catch (IOException e) {
            logger.error("GEN_QR_CODE_ERROR.PARAM:url={},version={}", url, v);
            logger.error("GEN_QR_CODE_ERROR.", e);
        }
    }

    @RequestMapping("/qrPDF")
    public void qrPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tid = request.getParameter("tid");
        String v = request.getParameter("v");
        String s = request.getParameter("s");
        if (!NumberUtils.isNumber(tid)) {
            logger.error("QR_LIST.PARAM_ERROR.TID=" + String.valueOf(tid));
        }
        int version = 8;
        int size = 8;
        if (NumberUtils.isNumber(v)) {
            version = Integer.parseInt(v);
        }
        if (NumberUtils.isNumber(s)) {
            size = Integer.parseInt(s);
        }

        Long topicId = Long.parseLong(tid);
        TopicItem topicItemQuery = new TopicItem();
        topicItemQuery.setTopicId(topicId);
        topicItemQuery.setDeletion(0);

        ResultInfo<List<TopicItem>> result = topicItemProxy.queryByObject(topicItemQuery);
        if (!result.isSuccess()) {
            logger.error("QR_LIST.LOAD_QR_LIST_ERROR.RESULT=" + JsonUtil.convertObjToStr(request));
        }
        Collections.sort(result.getData(), new Comparator<TopicItem>() {
            @Override
            public int compare(TopicItem o1, TopicItem o2) {
                return o1.getSortIndex().compareTo(o2.getSortIndex());
            }
        });


        Map<String,Object> param = new HashMap<>();
        param.put("v", version);
        param.put("s", size);
        param.put("md", mDomain);
        param.put("bd", backendDomain);
        param.put("items", result.getData());
        String context = PdfUtil.generateFileUseRelativePath("template","qr-list-template.ftl",param);
        System.out.println(context);
        response.setContentType("application/x-download");
        response.setHeader("Content-disposition", "attachment; filename=qr-code-list_tid_"+tid+".pdf");
        PdfUtil.createPdfFileUseRelativePathToOutputStream(context,response.getOutputStream());
    }



}
