package com.tp.world.ao.swt;

import com.tp.dto.common.ResultInfo;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.vo.swt.SwitchVO;
import com.tp.model.app.SwitchInfo;
import com.tp.proxy.app.SwitchInfoProxy;
import com.tp.redis.util.JedisCacheUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ldr on 2016/12/15.
 */
@Service
public class SwitchAO {


    private static final String switch_cache_key = "SWITCH_CACHE_KEY_";

    @Autowired
    private SwitchInfoProxy switchInfoProxy;

    @Autowired
    private JedisCacheUtil util;

    public MResultVO<List<SwitchVO>>  switchInfo(){

        List<SwitchVO> cache = (List<SwitchVO>)util.getCache(switch_cache_key);
        if(cache != null) return new MResultVO<List<SwitchVO>>(MResultInfo.SUCCESS,cache);

        ResultInfo<List<SwitchInfo>> resultInfo = switchInfoProxy.queryByObject(new SwitchInfo());
        List<SwitchVO> switchVOs = new ArrayList<>();
        if(resultInfo.isSuccess() && resultInfo.getData()!=null){
            for(SwitchInfo switchInfo: resultInfo.getData()){
                switchVOs.add(new SwitchVO(switchInfo.getCode(),String.valueOf(switchInfo.getStatus())));
            }

        }
        util.setCache(switch_cache_key,switchVOs,3600);
        return new MResultVO<List<SwitchVO>>(MResultInfo.SUCCESS,switchVOs);
    }

}
