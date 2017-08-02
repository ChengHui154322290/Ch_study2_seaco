/**
 * 
 */
package com.tp.service.ord;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.Constant.LOG_AUTHOR_TYPE;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.FastConstant.USER_TYPE;
import com.tp.common.vo.ord.OrderStatusLogConstant;
import com.tp.model.dss.FastUserInfo;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.SubOrder;
import com.tp.service.dss.IFastUserInfoService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.ord.IOrderInfoService;
import com.tp.service.ord.IOrderStatusLogService;
import com.tp.service.ord.ISubOrderService;

/**
 * 订单编号用户校验
 * 
 * @author szy
 * 
 */
@Service("orderCodeUserVerification")
public class OrderCodeUserVerification {
    private static final Logger logger = LoggerFactory.getLogger(OrderCodeUserVerification.class);

    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private ISubOrderService subOrderService;
    @Autowired
    private IMemberInfoService memberInfoService;
    @Autowired
    private IFastUserInfoService fastUserInfoService;
    @Autowired
    private IOrderStatusLogService orderStatusLogService;

    /**
     * 
     * <pre>
     * 验证单号和用户是否匹配
     * </pre>
     * 
     * @param memberId
     *            会员ID
     * @param code
     *            单号：父订单、子订单、退货单
     * @return
     */
    public Boolean verifyCodeUser(Long memberId, Long code) {
        if (memberId == null) {
        	return Boolean.FALSE;
        }
        if (null==code) {
        	return Boolean.FALSE;
        }

        Boolean bool = Boolean.TRUE;
        if (String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.SO_ORDER.code.toString())) {
            List<OrderInfo> list = orderInfoService.selectByCodeAndMemberID(code, memberId);
            if (CollectionUtils.isEmpty(list)) {
                bool = Boolean.FALSE;
            }
        } else if (String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.SO_SUB_ORDER.code.toString())) {
        	SubOrder subOrder = subOrderService.selectOneByCode(code);
            if (subOrder==null) {
                bool = Boolean.FALSE;
            }
            if(subOrder.getMemberId().equals(memberId)){
                return bool;
            }
            if(!OrderConstant.FAST_ORDER_TYPE.equals(subOrder.getType())){
            	return false;
            }
            MemberInfo memberInfo = memberInfoService.queryById(memberId);
        	if(memberInfo==null){
        		return false;
        	}
        	Map<String,Object> params = new HashMap<String,Object>();
        	params.put("mobile", memberInfo.getMobile());
        	params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(),"user_type in ("+USER_TYPE.COURIER.code+","+USER_TYPE.MANAGER.code+")");
        	params.put("enabled",Constant.ENABLED.YES);
        	FastUserInfo fastUserInfo = fastUserInfoService.queryUniqueByParams(params);
        	if(fastUserInfo==null || !fastUserInfo.getFastUserId().equals(subOrder.getFastUserId())){
        		return false;
        	}
        	insertOrderLog(fastUserInfo,subOrder);
        } else if (String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.RETURNED.code.toString())) {
            // 退货单暂时不校验
            bool = Boolean.TRUE;
        } else {
            logger.info("校验单号不和法");
            bool = Boolean.FALSE;
        }

        return bool;
    }
    
    public void insertOrderLog(FastUserInfo fastUserInfo,SubOrder subOrder){
    	OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setName(OrderConstant.ORDER_STATUS.RECEIPT.cnName);
		orderStatusLog.setOrderCode(subOrder.getOrderCode());
		orderStatusLog.setPreStatus(subOrder.getOrderStatus());
		orderStatusLog.setCurrStatus(subOrder.getOrderStatus());
		orderStatusLog.setType(OrderStatusLogConstant.LOG_TYPE.TRACKING.code);
		orderStatusLog.setName("配送员确认送达");
		orderStatusLog.setParentOrderCode(subOrder.getParentOrderCode());
		orderStatusLog.setPreStatus(subOrder.getOrderStatus());
		orderStatusLog.setContent("配送员确认送达【联系信息:"+fastUserInfo.getUserName()+","+fastUserInfo.getMobile()+"】");
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setCreateUserId(fastUserInfo.getFastUserId());
		orderStatusLog.setCreateUserName(fastUserInfo.getUserName());
		orderStatusLog.setCreateUserType(LOG_AUTHOR_TYPE.USER_OPERATER.code);
		try{
			orderStatusLogService.insert(orderStatusLog);
		}catch(Exception e){
			
		}
    }
}
