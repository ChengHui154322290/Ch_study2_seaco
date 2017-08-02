package com.tp.seller.ao.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.model.bse.ClearanceChannels;
import com.tp.service.bse.IClearanceChannelsService;

/**
 * 卖家平台调用外部接口AO {class_description} <br>
 * Create on : 2015年3月25日 下午7:01:24<br>
 * 
 * @version seller-front v0.0.1
 */
@Service
public class SellerCommonAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaServiceAO.class);

    @Autowired
    private IClearanceChannelsService clearanceChannelsService;

    /**
     * 生成发货方式的选择框 {method description}.
     * 
     * @return
     */
    public String getStringDeliveryWayOptionStr() {
        final ClearanceChannels channnelDO = new ClearanceChannels();
        channnelDO.setStatus(Constant.ENABLED.YES);
        List<ClearanceChannels> channels = null;
        try {
            channels = clearanceChannelsService.queryByObject(channnelDO);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        final List<Map<String, String>> paramsMaps = new ArrayList<Map<String, String>>();
        if (CollectionUtils.isNotEmpty(channels)) {
            for (final ClearanceChannels channel : channels) {
                final Map<String, String> paramsMap = new HashMap<String, String>();
                paramsMap.put("value", channel.getId() + "");
                paramsMap.put("text", channel.getName());
                paramsMaps.add(paramsMap);
            }
        }
        return getOptionStr(paramsMaps, "");
    }

    /**
     * 获取选择的string
     *
     * @param options
     * @return
     */
    private String getOptionStr(final List<Map<String, String>> options, final String defaultVal) {
        final StringBuffer sb = new StringBuffer("");
        if (null == options || options.size() == 0) {
            sb.append("\'").append("\'");
            return sb.toString();
        }
        sb.append("\"");
        for (final Map<String, String> option : options) {
            final String text = option.get("text");
            if (null != defaultVal && defaultVal.equals(text)) {
                sb.append("<option selected=\"selected\" value=\'");
            } else {
                sb.append("<option value=\'");
            }
            sb.append(option.get("value"));
            sb.append("\'>");
            sb.append(text);
            sb.append("</option>");
        }
        sb.append("\"");
        return sb.toString();
    }

}
