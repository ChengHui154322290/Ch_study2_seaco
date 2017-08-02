package com.tp.service.ord.mq;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PaymentConstant;
import com.tp.common.vo.PaymentConstant.PAYMENT_STATUS;
import com.tp.common.vo.ord.ParamValidator;
import com.tp.common.vo.ord.LogTypeConstant.LOG_LEVEL;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.SubOrder;
import com.tp.model.pay.PaymentInfo;
import com.tp.mq.MqMessageCallBack;
import com.tp.service.ord.IOrderItemService;
import com.tp.service.ord.IOrderService;
import com.tp.service.ord.IOrderStatusLogService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.prd.IItemRemoteService;

/**
 * 支付成功回调服务
 * 
 * @author szy
 * @version 0.0.1
 */
@Service
public class SalesOrderPaidCallback implements MqMessageCallBack {

	private static final Logger log = LoggerFactory.getLogger(SalesOrderPaidCallback.class);

	private static final String LOG_PREFIX = "支付成功回调：";
	
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderStatusLogService orderStatusLogService;
	@Autowired
	private ISubOrderService subOrderService;
	@Autowired
	private IOrderItemService orderItemService;
	@Autowired
	private IItemRemoteService itemRemoteService;

	@Override
	public boolean execute(Object o) {
		boolean isValid = validateParam(o); // 入参校验
		log.info("支付成功mq回调：", o);
		if (isValid) {
			PaymentInfo pay = (PaymentInfo) o;
			if (log.isInfoEnabled()) {
				log.info("订单[{}]支付成功回调，支付状态[{}]，支付金额[{}]", pay.getBizCode(), pay.getStatus(), pay.getAmount());
			}

			try {
				orderService.operateAfterSuccessPay(pay);
				itemSaleCount(pay.getBizCode());
			} catch (Exception e) {
				log.error("支付成功回调错误：订单号[{}], 订单金额[{}]", pay.getBizCode(), pay.getAmount(), e);
				log(pay, e.getMessage());
			}
		}

		return true;
	}

	// 记录错误日志入库
	private void log(PaymentInfo pay, String msg) {
		if (null != pay && null!=pay.getBizCode()) {
			OrderStatusLog orderLog = new OrderStatusLog();
			orderLog.setContent(LOG_PREFIX + msg);
			orderLog.setCreateTime(new Date());
			if (PaymentConstant.BIZ_TYPE.ORDER.code.equals(pay.getBizType())) { // 父单
				orderLog.setParentOrderCode(pay.getBizCode());
			} else { // 子单
				orderLog.setOrderCode(pay.getBizCode());
			}
			orderLog.setType(LOG_LEVEL.ERROR.code);
			orderStatusLogService.insert(orderLog);
		}
	}
	// 销售量累计
    public void itemSaleCount(final Long bizCode) {
        try {
            List<OrderItem> lineList = null;
           
            SubOrder sub = subOrderService.selectOneByCode(bizCode);
            if (sub != null) {
                lineList = orderItemService.selectListBySubId(sub.getId());
            }

            if (CollectionUtils.isNotEmpty(lineList)) {
                Map<String, Integer> qMap = new HashMap<String, Integer>();
                for (OrderItem line : lineList) {
                    qMap.put(line.getSkuCode(), line.getQuantity());
                }
                itemRemoteService.updateItemSalesCount(qMap);
            }
        } catch (Exception e) {
            log.error("order payment - order[{}] item count fails", bizCode, e);
        }
    }
	// 入参校验
	private boolean validateParam(Object obj) {
		if (!(obj instanceof PaymentInfo)) { // 类型不匹配
			log.error("支付成功回调错误：入参错误，类型不匹配，应该为[{}]，而实际获得[{}]", PaymentInfo.class.getName(), obj.getClass().getName());
			return false;
		}

		ParamValidator pv = new ParamValidator("支付成功回调");
		PaymentInfo pay = (PaymentInfo) obj;
		try {
			pv.notNull(obj, "支付信息");
			pv.notNull(pay.getBizCode(), "订单编号");
			pv.notNull(pay.getStatus(), "支付状态");
			pv.notNull(pay.getAmount(), "支付金额");
		} catch (Exception e) {
			log.error("入参校验未通过{}", e);
			return false;
		}

		if (!PaymentConstant.PAYMENT_STATUS.PAYED.code.equals(pay.getStatus())) {
			log.error("支付成功回调错误：入参错误，支付状态应该是“已支付”，而实际获得的是“{}”", PAYMENT_STATUS.getCnName(pay.getStatus()));
			return false;
		}

		return true;
	}

}
