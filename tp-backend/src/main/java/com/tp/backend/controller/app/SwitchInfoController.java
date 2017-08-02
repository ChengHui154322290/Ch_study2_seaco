package com.tp.backend.controller.app;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.app.SwitchInfo;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.app.SwitchInfoProxy;
import com.tp.redis.util.JedisCacheUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by ldr on 2016/12/15.
 */
@Controller
@RequestMapping("/app/switch")
public class SwitchInfoController extends AbstractBaseController {

    private static final String switch_cache_key = "SWITCH_CACHE_KEY_";

    @Autowired
    private SwitchInfoProxy switchInfoProxy;

    @Autowired
    private JedisCacheUtil util;

    @RequestMapping("/switch")
    public String home(HttpServletRequest request, Model model) {

        ResultInfo<List<SwitchInfo>> resultInfo = switchInfoProxy.queryByObject(new SwitchInfo());

        model.addAttribute("result", resultInfo);

        return "app/switch/switch";
    }

    @RequestMapping("/switchDetail")
    public String switchDetail(HttpServletRequest request, Model model) {
        return "app/switch/switchDetail";
    }

    @ResponseBody
    @RequestMapping("/addSwitch")
    public ResultInfo<SwitchInfo> add( SwitchInfo switchInfo) {
        if(switchInfo == null || switchInfo.getStatus() == null || StringUtils.isBlank(switchInfo.getCode())  || StringUtils.isBlank(switchInfo.getName())){
            return new ResultInfo<>(new FailInfo("参数错误"));
        }

        SwitchInfo switchInfo1 = new SwitchInfo();
        switchInfo1.setCode(switchInfo.getCode());
        ResultInfo<List<SwitchInfo>> listResultInfo = switchInfoProxy.queryByObject(switchInfo1);
        if(listResultInfo.isSuccess() && !CollectionUtils.isEmpty(listResultInfo.getData())){
            return new ResultInfo<>(new FailInfo("已经存在的CODE"));
        }


        util.deleteCacheKey(switch_cache_key);
        UserInfo userInfo = getUserInfo();
        Date cur = new Date();
        switchInfo.setCreateUser(userInfo.getUserName());
        switchInfo.setCreateTime(cur);
        switchInfo.setUpdateUser(userInfo.getUserName());
        switchInfo.setUpdateTime(cur);

        ResultInfo<SwitchInfo> resultInfo = switchInfoProxy.insert(switchInfo);
        return resultInfo;
    }

    @ResponseBody
    @RequestMapping("/operate")
    public ResultInfo<Integer> operate(Long id ,Integer status){
        if(id == null || status == null) return new ResultInfo<>(new FailInfo("参数错误"));

        util.deleteCacheKey(switch_cache_key);
        SwitchInfo switchInfo = new SwitchInfo();
        switchInfo.setId(id);
        switchInfo.setStatus(status);
        return switchInfoProxy.updateNotNullById(switchInfo);

    }

}
