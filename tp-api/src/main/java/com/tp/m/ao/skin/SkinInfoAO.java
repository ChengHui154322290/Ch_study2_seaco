package com.tp.m.ao.skin;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.TopicType;
import com.tp.m.base.MResultVO;
import com.tp.m.convert.ProductConvert;
import com.tp.m.convert.SkinInfoConvert;
import com.tp.m.enums.MResultInfo;
import com.tp.m.query.skin.QuerySkinInfo;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.product.ProductDetailVO;
import com.tp.m.vo.skin.SkinInfoVO;
import com.tp.m.vo.swt.SwitchVO;
import com.tp.m.vo.topic.TopicDetailVO;
import com.tp.model.app.SkinInfo;
import com.tp.model.app.SwitchInfo;
import com.tp.model.sch.result.ItemResult;
import com.tp.proxy.app.SkinInfoProxy;
import com.tp.proxy.app.SwitchInfoProxy;

import org.springframework.util.CollectionUtils;

@Service
public class SkinInfoAO {
    @Autowired
    private SwitchInfoProxy switchInfoProxy;
    @Autowired
    private SkinInfoProxy skinInfoProxy;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public MResultVO<SkinInfoVO> getSkinDetail(QuerySkinInfo skinInfo) {
        Long skinId;
        Map<String, Object> param = new HashMap<>();
        param.put("code", "skin");
        param.put("status", 1);
        SkinInfoVO vo = new SkinInfoVO();
        ResultInfo<List<SwitchInfo>> switchInfo = switchInfoProxy.queryByParam(param);

        if (switchInfo.success && !CollectionUtils.isEmpty(switchInfo.getData())) {
            if (StringUtil.isBlank(skinInfo.getSkinid())) {
                skinId = -1L;
            } else {
                skinId = StringUtil.getLongByStr(skinInfo.getSkinid());
            }
            ResultInfo<SkinInfo> skins = skinInfoProxy.searchSkinNew(skinId);
            if (skins.success && skins.getData() != null && skins.getData().getId() != null) {
                vo = SkinInfoConvert.convertSkin(skins.getData(), skinId);
                addLengthLog(vo);
            } else {
                vo.setStatus("0");
            }
            return new MResultVO<>(MResultInfo.SUCCESS, vo);
        }
        vo.setStatus("0");
        return new MResultVO<>(MResultInfo.SUCCESS, vo);


    }

    private void addLengthLog(SkinInfoVO vo) {
        try {
            if (vo.getIcona() != null) {
                StringBuilder builder = new StringBuilder();
                builder.append(vo.getIcona().length()).append(vo.getIconb().length()).append(vo.getIconc().length()).append(vo.getIcond().length()).append(vo.getIcone().length())
                        .append(vo.getIconf().length()).append(vo.getIcong().length()).append(vo.getIconh().length())
                        .append(vo.getIconi().length()).append(vo.getIconj().length()).append(vo.getIconk().length())
                        .append(vo.getIconl().length()).append(vo.getIconm().length()).append(vo.getIconn().length())
                        .append(vo.getIcono().length()).append(vo.getIconp().length()).append(vo.getIconq().length()).append(vo.getIconr().length());
                logger.info("SKIN_ICON_STRING_LENGTH" + builder.toString());
            }
        } catch (Exception e) {
            logger.error("SKIN_ICON_STRING_LENGTH_ERROR", e);
        }

    }
}
