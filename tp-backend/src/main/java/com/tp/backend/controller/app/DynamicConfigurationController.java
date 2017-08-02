package com.tp.backend.controller.app;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.DAOConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.app.DynamicConfiguration;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.app.DynamicConfigurationProxy;
import com.tp.redis.util.JedisCacheUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * APP首页动态配置
 * Created by ldr on 2016/12/8.
 */

@Controller
@RequestMapping("/app")
public class DynamicConfigurationController extends AbstractBaseController {

    private static final String dc_cache_key = "DC_CACHE_KEY_";


    @Autowired
    private DynamicConfigurationProxy dynamicConfigurationProxy;

    @Autowired
    private JedisCacheUtil util;

    @RequestMapping("/dc")
    public String dc(HttpServletRequest request, Model model) {

        Map<String, Object> params = new HashMap<>();
        List<DAOConstant.WHERE_ENTRY> whereList = new ArrayList<DAOConstant.WHERE_ENTRY>();

        whereList.add(new DAOConstant.WHERE_ENTRY("status", DAOConstant.MYBATIS_SPECIAL_STRING.INLIST, Arrays.asList(0, 1)));
        params.put(DAOConstant.MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
        params.put(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " id desc");
        ResultInfo<List<DynamicConfiguration>> result = dynamicConfigurationProxy.queryByParam(params);
        model.addAttribute("result", result);
        return "app/dc/dc";
    }

    @RequestMapping("/dcDetail")
    public String dcDetail(HttpServletRequest request, Model model, Long id) {

        if (id != null) {
            ResultInfo<DynamicConfiguration> resultInfo = dynamicConfigurationProxy.queryById(id);
            if (resultInfo.isSuccess() && resultInfo.getData() != null) {
                model.addAttribute("detail", resultInfo.getData());
            }
        }
        return "app/dc/dcDetail";
    }

    @ResponseBody
    @RequestMapping("/addDetail")
    public ResultInfo addDetail(HttpServletRequest request, Model model, Long id, String name, String content,String versionFrom,String versionTo) {
        util.deleteCacheKey(dc_cache_key);

        if (StringUtils.isBlank(name)) {
            return new ResultInfo<>(new FailInfo("请填写配置名称"));
        }
        if (StringUtils.isBlank(content)) {
            return new ResultInfo<>(new FailInfo("请填写配置内容"));
        }
        if (StringUtils.isBlank(versionFrom)) {
            return new ResultInfo<>(new FailInfo("请填写起始版本"));
        }
        if (StringUtils.isBlank(versionTo)) {
            return new ResultInfo<>(new FailInfo("请填写结束版本"));
        }
        if(!NumberUtils.isNumber(versionFrom)){
            return new ResultInfo<>(new FailInfo("起始版本只能为数字"));
        }
        if(!NumberUtils.isNumber(versionTo)){
            return new ResultInfo<>(new FailInfo("结束版本只能为数字"));
        }

        Date cur = new Date();
        UserInfo user = getUserInfo();
        DynamicConfiguration configuration = new DynamicConfiguration();
        configuration.setContent(content.trim());
        configuration.setName(name);
        configuration.setVersionFrom(Integer.parseInt(versionFrom));
        configuration.setVersionTo(Integer.parseInt(versionTo));
        configuration.setUpdateUser(user.getUserName());
        configuration.setUpdateTime(cur);
        if (id != null) {
            configuration.setId(id);
            return dynamicConfigurationProxy.updateNotNullById(configuration);
        } else {
            configuration.setStatus(0);
            configuration.setCreateTime(cur);
            configuration.setCreateUser(user.getUserName());
            return dynamicConfigurationProxy.insert(configuration);
        }

    }


    @ResponseBody
    @RequestMapping("/operate")
    public ResultInfo operate(HttpServletRequest request, Model model, Long id, Integer status) {
        util.deleteCacheKey(dc_cache_key);

        Date cur = new Date();
        UserInfo user = getUserInfo();
        DynamicConfiguration configuration = new DynamicConfiguration();
        configuration.setId(id);
        configuration.setStatus(status);
        configuration.setUpdateUser(user.getUserName());
        configuration.setUpdateTime(cur);

//        if(status == 1){
//            DynamicConfiguration query = new DynamicConfiguration();
//            query.setStatus(1);
//            ResultInfo<List<DynamicConfiguration>> resultInfo = dynamicConfigurationProxy.queryByObject(query);
//            if(resultInfo.isSuccess() && !CollectionUtils.isEmpty(resultInfo.getData())){
//                for(DynamicConfiguration t: resultInfo.getData()){
//                    t.setUpdateTime(cur);
//                    t.setUpdateUser(user.getUserName());
//                    t.setStatus(0);
//                    dynamicConfigurationProxy.updateNotNullById(t);
//                }
//            }
//        }

        return dynamicConfigurationProxy.updateNotNullById(configuration);
    }
}


