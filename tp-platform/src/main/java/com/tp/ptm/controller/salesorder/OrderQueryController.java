package com.tp.ptm.controller.salesorder;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.ptm.ErrorCodes.SystemError;
import com.tp.dto.ptm.ReturnData;
import com.tp.dto.ptm.salesorder.Order4PlatformDTO;
import com.tp.dto.ptm.salesorder.SubOrder4PlatformQO;
import com.tp.exception.PlatformServiceException;
import com.tp.ptm.annotation.Authority;
import com.tp.ptm.ao.salesorder.OrderQueryAO;
import com.tp.ptm.controller.BaseController;
import com.tp.ptm.support.FrequencyCounter;
import com.tp.ptm.support.FrequencyCounter.BusinessType;

/**
 * {订单查询} <br>
 * Create on : 2015年11月14日 下午2:01:25<br>
 * 
 * @author Administrator<br>
 * @version platform-front v0.0.1
 */
@Controller
public class OrderQueryController extends BaseController {
	private Logger log = LoggerFactory.getLogger(OrderQueryController.class);
    @Value("#{meta['order.query.seconds']}")
    private int seconds;

    @Value("#{meta['order.query.times']}")
    private int times;

    @Autowired
    private OrderQueryAO orderQueryAO;

    @Autowired
    private FrequencyCounter frequencyCounter;

    @RequestMapping(value = "/order/orderInPage", method = RequestMethod.POST)
    @ResponseBody
    @Authority
    public ReturnData findOrderInPage(@RequestParam String appkey, @RequestBody SubOrder4PlatformQO qo) {
        if (!frequencyCounter.overload(appkey, BusinessType.QUERY_ORDER, seconds, times)) {
            // 访问频率超过限制
            return new ReturnData(false, SystemError.ACCESS_OVERLOAD.code, SystemError.ACCESS_OVERLOAD.cnName);
        }

        PageInfo<Order4PlatformDTO> page;
		try {
			page = orderQueryAO.findOrderInPage(appkey, qo);
		} catch (PlatformServiceException e) {
			log.error(e.getMessage(), e);
			return new ReturnData(false, e.getErrorCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ReturnData(false, SystemError.SYSTEM_ERROR.code, SystemError.SYSTEM_ERROR.cnName);
		}
        return new ReturnData(true, page);
    }
}
