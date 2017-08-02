package com.tp.backend.controller.sch;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.DAOConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.dto.sch.enums.SearchBlacklistType;
import com.tp.model.mmp.Topic;
import com.tp.model.prd.ItemSku;
import com.tp.model.sch.SearchBlacklist;
import com.tp.model.sch.SearchOperateLog;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.mmp.TopicProxy;
import com.tp.proxy.prd.ItemSkuProxy;
import com.tp.proxy.sch.SearchBlacklistProxy;
import com.tp.proxy.sch.SearchDataProxy;
import com.tp.proxy.sch.SearchOperateLogProxy;
import com.tp.redis.util.JedisCacheUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ldr on 2016/5/19.
 */
@Controller
@RequestMapping("/search/blacklist")
public class SearchBlacklistController extends AbstractBaseController {

    @Autowired
    private ItemSkuProxy itemSkuProxy;

    @Autowired
    private TopicProxy topicProxy;

    @Autowired
    private SearchBlacklistProxy searchBlacklistProxy;

    @Autowired
    private SearchDataProxy searchDataProxy;

    @Autowired
    private SearchOperateLogProxy searchOperateLogProxy;

    @Autowired
    private JedisCacheUtil jedisCacheUtil;

    private static final String MANUAL_SYN_DATA_KEY = "MANUAL_SYN_DATA_KEY";

    @RequestMapping("/home")
    public String home(HttpServletRequest request, Model model) {
        return "search/blacklist/home";
    }

    @ResponseBody
    @RequestMapping("/query")
    public ResultInfo<String> query(Integer type, String value) {
        if (type == null || StringUtils.isBlank(value))
            return new ResultInfo<>(new FailInfo("参数错误", -1));
        if (type.equals(1)) {
            if (!NumberUtils.isNumber(value)) return new ResultInfo<>(new FailInfo("参数错误", -1));
            Long topicId = Long.parseLong(value);
            ResultInfo<Topic> topicResultInfo = topicProxy.queryById(topicId);
            if (topicResultInfo.isSuccess() && topicResultInfo.getData() != null) {
                return new ResultInfo<>(topicResultInfo.getData().getName());
            } else {
                return new ResultInfo(new FailInfo("促销专场不存在", -1));
            }
        } else if (type.equals(2)) {
            ItemSku skuQuery = new ItemSku();
            skuQuery.setSku(value);
            ResultInfo<List<ItemSku>> skuResult = itemSkuProxy.queryByObject(skuQuery);
            if (skuResult.isSuccess() && skuResult.getData() != null && skuResult.getData().size() > 0) {
                return new ResultInfo<>(skuResult.getData().get(0).getDetailName());
            } else {
                return new ResultInfo(new FailInfo("商品不存在", -1));
            }
        }
        return new ResultInfo<>(new FailInfo("查询结果为空", -1));
    }

    @ResponseBody
    @RequestMapping("/addBlackList")
    public ResultInfo addBlackList(Integer type, String value) {
        if (type == null || StringUtils.isBlank(value))
            return new ResultInfo<>(new FailInfo("参数错误", -1));
        if (type.equals(1)) {
            if (!NumberUtils.isNumber(value.trim())) return new ResultInfo<>(new FailInfo("参数错误", -1));
            Long topicId = Long.parseLong(value.trim());
            ResultInfo<Topic> topicResultInfo = topicProxy.queryById(topicId);
            if (topicResultInfo.isSuccess() && topicResultInfo.getData() != null ) {
                SearchBlacklist searchBlacklist = new SearchBlacklist();
                searchBlacklist.setCreateTime(new Date());
                searchBlacklist.setType(1);
                searchBlacklist.setValue(value.trim());
                searchBlacklist.setName(topicResultInfo.getData().getName());
                searchBlacklist.setCreateUser(getUserName());
                return searchBlacklistProxy.doAdd(searchBlacklist);
            } else {
                return new ResultInfo(new FailInfo("促销专场不存在", -1));
            }
        } else if (type.equals(2)) {
            ItemSku skuQuery = new ItemSku();
            skuQuery.setSku(value);
            ResultInfo<List<ItemSku>> skuResult = itemSkuProxy.queryByObject(skuQuery);
            if (skuResult.isSuccess() && skuResult.getData() != null && skuResult.getData().size() > 0) {
                SearchBlacklist searchBlacklist = new SearchBlacklist();
                searchBlacklist.setCreateTime(new Date());
                searchBlacklist.setType(2);
                searchBlacklist.setValue(value.trim());
                searchBlacklist.setName(skuResult.getData().get(0).getDetailName());
                searchBlacklist.setCreateUser(getUserName());
                return searchBlacklistProxy.doAdd(searchBlacklist);
            } else {
                return new ResultInfo(new FailInfo("商品不存在", -1));
            }
        }
        return new ResultInfo<>(new FailInfo("参数错误", -1));
    }


    @RequestMapping("/searchBlacklistList")
    public String blacklistList(Model model, HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>();
        String target = request.getParameter("target");
        param.put(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "create_time desc");
        param.put("isDeleted", 0);
        ResultInfo<List<SearchBlacklist>> result = searchBlacklistProxy.queryByParam(param);
        model.addAttribute("types", SearchBlacklistType.values());
        model.addAttribute("list", result.getData());
        model.addAttribute("target", target);
        return "/search/blacklist/searchBlacklistList";
    }

    /**
     * 删除黑名单
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteBlacklist")
    public ResultInfo<Integer> deleteBlacklist(Long id) {
        if (id == null) return new ResultInfo<>(new FailInfo("参数错误", -1));
        SearchBlacklist query = new SearchBlacklist();
        query.setId(id);
        query.setIsDeleted(1);
        ResultInfo<Integer> result = searchBlacklistProxy.updateNotNullById(query);
        return result;
    }

    @ResponseBody
    @RequestMapping("/synData")
    public ResultInfo synData() throws InterruptedException {
        long b = System.currentTimeMillis();
        UserInfo userInfo = getUserInfo();
        if (userInfo == null) {
            return new ResultInfo(new FailInfo("请登录", -1));
        }
        Object o = jedisCacheUtil.getCache(MANUAL_SYN_DATA_KEY);
        if (o != null) {
            return new ResultInfo(new FailInfo("已有同步操作"));
        }
        jedisCacheUtil.setCache(MANUAL_SYN_DATA_KEY, 1,60);

        addOperateLog();

        ResultInfo resultInfo = searchDataProxy.update();
        long cost = System.currentTimeMillis() - b;
        long padding = 200 - cost;
        if (padding > 0) {
            Thread.sleep(padding);
        }
        jedisCacheUtil.deleteCacheKey(MANUAL_SYN_DATA_KEY);
        return resultInfo;
    }

    private void addOperateLog() {
        SearchOperateLog log = new SearchOperateLog();
        log.setOperateType(1);
        log.setCreateTime(new Date());
        log.setCreateUser(getUserName());
        log.setContent("同步数据");
        searchOperateLogProxy.insert(log);
    }


}
