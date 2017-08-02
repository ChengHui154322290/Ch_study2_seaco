package com.tp.m.controller.skin;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.dto.common.ResultInfo;
import com.tp.m.ao.dss.DSSUserAO;
import com.tp.m.ao.skin.SkinInfoAO;
import com.tp.m.ao.swt.SwitchAO;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.skin.QuerySkinInfo;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.product.ProductDetailVO;
import com.tp.m.vo.skin.SkinInfoVO;
import com.tp.m.vo.swt.SwitchVO;
import com.tp.model.dss.PromoterInfo;


@Controller
@RequestMapping("/skin")
public class SkinInfoController {
    private static final Logger log = LoggerFactory.getLogger(SkinInfoController.class);
    @Autowired
    private SkinInfoAO skinInfoAO;

    /**
     * 皮肤详情
     *
     * @param topic
     * @return
     */
    @RequestMapping(value = "/icon", method = RequestMethod.GET)
    @ResponseBody
    public String icon(QuerySkinInfo skinInfo) {
        try {

            AssertUtil.notNull(skinInfo, MResultInfo.PARAM_ERROR);
            //	if(skinInfo.getSkinid()!=null){
            MResultVO<SkinInfoVO> result = skinInfoAO.getSkinDetail(skinInfo);

            return JsonUtil.convertObjToStr(result);
            //   }

        } catch (MobileException me) {
            log.error("[API接口 - 皮肤更换  MobileException] = {}", me);
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.PARAM_ERROR));
        } catch (Exception e) {
            log.error("[API接口 - 皮肤更换,EXCEPTION] = {}", e);
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.FAILED));
        }


    }


}
