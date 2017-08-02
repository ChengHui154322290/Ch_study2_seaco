package com.tp.m.controller.swt;

import com.tp.m.ao.swt.SwitchAO;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.swt.SwitchVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by ldr on 2016/12/15.
 */
@Controller
public class SwitchController {

    private final Logger log = LoggerFactory.getLogger(SwitchController.class);

    @Autowired
    private SwitchAO switchAO;

    @ResponseBody
    @RequestMapping("/swt")
    public String switchInfo(HttpServletRequest request) {
        try {
            MResultVO<List<SwitchVO>> result = switchAO.switchInfo();
            return JsonUtil.convertObjToStr(result);
        } catch (Exception e) {
            log.error("GET_SWITCH_INFO_ERROR,", e);
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SYSTEM_ERROR));
        }

    }

}
