package com.tp.world.ao.home;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tp.dto.common.ResultInfo;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.util.JsonUtil;
import com.tp.model.app.DynamicConfiguration;
import com.tp.proxy.app.DynamicConfigurationProxy;
import com.tp.redis.util.JedisCacheUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ldr on 2016/12/9.
 */
@Service
public class DCAO {

    private static final String dc_cache_key = "DC_CACHE_KEY_";

    @Autowired
    private DynamicConfigurationProxy dynamicConfigurationProxy;

    @Autowired
    private JedisCacheUtil util;

    Logger logger = LoggerFactory.getLogger(DCAO.class);

    public MResultVO<JSONObject> getConfig() {

        JSONObject jsObj = (JSONObject) util.getCache(dc_cache_key);
        if (jsObj != null) {
            return new MResultVO<>(MResultInfo.SUCCESS, jsObj);
        }
        DynamicConfiguration dc = new DynamicConfiguration();
        dc.setStatus(1);
        ResultInfo<DynamicConfiguration> resultInfo = dynamicConfigurationProxy.queryUniqueByObject(dc);
        if (!resultInfo.isSuccess()) {
            logger.error("GET_DC_FAILED.RESULT=" + JsonUtil.convertObjToStr(resultInfo));
            return new MResultVO<>(MResultInfo.SUCCESS);
        } else {
            if (resultInfo.getData() != null) {
                JSONObject jsonObject = JSON.parseObject(resultInfo.getData().getContent());
                util.setCache(dc_cache_key, jsonObject, 6000);

                return new MResultVO<>(MResultInfo.SUCCESS, jsonObject);
            } else {
                return new MResultVO<>(MResultInfo.SUCCESS);
            }
        }
    }
}
