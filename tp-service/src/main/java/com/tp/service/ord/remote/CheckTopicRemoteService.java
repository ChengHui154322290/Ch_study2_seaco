package com.tp.service.ord.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.ord.OrderErrorCodes;
import com.tp.common.vo.ord.ParamValidator;
import com.tp.common.vo.ord.TopicLimitItemConstant.TopicLimitType;
import com.tp.dao.ord.OrderItemDao;
import com.tp.dao.ord.SubOrderDao;
import com.tp.dao.ord.TopicLimitItemDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.remote.TopicPolicyDTO;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;
import com.tp.model.ord.TopicLimitItem;
import com.tp.service.ord.remote.ICheckTopicRemoteService;
import com.tp.util.BeanUtil;

/**
 * 促销限购检查
 * 
 * @author szy
 *
 */
@Service("checkTopicRemoteService")
public class CheckTopicRemoteService implements ICheckTopicRemoteService {
	private static final Logger log = LoggerFactory.getLogger(CheckTopicRemoteService.class);

	@Autowired
	private OrderItemDao  orderItemDao;
	@Autowired
	private SubOrderDao  subOrderDao;
	@Autowired
	private TopicLimitItemDao  topicLimitItemDao;	
	

	@Override
	public ResultInfo<Boolean> checkTopicPolicy(TopicPolicyDTO topicPolicyDTO) {
		// TO Auto-generated method stub

		ParamValidator validator = new ParamValidator("促销限购校验");
		validator.notNull(topicPolicyDTO, "促销限购信息");
		validator.notNull(topicPolicyDTO.getTopicId(), "促销id");
		validator.notNull(topicPolicyDTO.getSkuCode(), "商品skucode");
		validator.notNull(topicPolicyDTO.getQuantity(), "本次购买数量");
		validator.notNull(topicPolicyDTO.getTopicSum(), "限购总数");

		if (null == topicPolicyDTO.getUserId() && StringUtils.isBlank(topicPolicyDTO.getIp())
				&& StringUtils.isBlank(topicPolicyDTO.getMobile())) {
			return new ResultInfo<>(Boolean.TRUE);
		}

		Map<String,Object> orderLine = new HashMap<String,Object>();
		orderLine.put("topicId",topicPolicyDTO.getTopicId());
		orderLine.put("skuCode",topicPolicyDTO.getSkuCode());

		List<OrderItem> orderLines = new ArrayList<OrderItem>();

		if (null != topicPolicyDTO.getUserId()) {// 用户限购
			orderLine.put("memberId",topicPolicyDTO.getUserId());
			// by topId,skuCode,userId search
			orderLines = orderItemDao.queryByParam(orderLine);
			if (CollectionUtils.isNotEmpty(orderLines)) {
				Integer sum = getQuantityBought(orderLines);
				if (topicPolicyDTO.getTopicSum() - topicPolicyDTO.getQuantity() - sum < 0) {
					if (topicPolicyDTO.getTopicSum() - sum <= 0) {
						return new ResultInfo<Boolean>(new FailInfo("用户达到限购",OrderErrorCodes.PROMOTION_USERID_LIMIT_POLICY));
					} else {
						return new ResultInfo<Boolean>(new FailInfo("用户达到限购",OrderErrorCodes.PROMOTION_USERID_LIMIT_POLICY));
					}
				}
			}
		}

		// IP 限购
		if (StringUtils.isNotBlank(topicPolicyDTO.getIp())) {
			orderLine.remove("memberId");
			orderLine.put("ip",topicPolicyDTO.getIp());
			// by topId,skuCode,ip search
			List<OrderItem> orderLineByIps = orderItemDao.queryByParam(orderLine);
			if (CollectionUtils.isNotEmpty(orderLineByIps)) {
				Integer sum = getQuantityBought(orderLineByIps);
				if (topicPolicyDTO.getTopicSum() - topicPolicyDTO.getQuantity() - sum < 0) {
					if (topicPolicyDTO.getTopicSum() - sum <= 0) {
						return new ResultInfo<Boolean>(new FailInfo("用户达到限购",OrderErrorCodes.PROMOTION_USERID_LIMIT_POLICY));
					} else {
						return new ResultInfo<Boolean>(new FailInfo("用户达到限购",OrderErrorCodes.PROMOTION_USERID_LIMIT_POLICY));
					}
				}
			}
		}
		//收货人手机限制 限购
		if ( StringUtils.isNotBlank(topicPolicyDTO.getMobile())) {
			TopicLimitItem topicLimitItem = new TopicLimitItem();
			topicLimitItem.setTopicId(topicPolicyDTO.getTopicId());
			topicLimitItem.setSkuCode(topicPolicyDTO.getSkuCode());
			topicLimitItem.setLimitValue(topicPolicyDTO.getMobile());
			topicLimitItem.setLimitType(TopicLimitType.MOBILE.code);
			List<TopicLimitItem>topicLimitItems  =	topicLimitItemDao.queryByObject(topicLimitItem);
			if(CollectionUtils.isNotEmpty(topicLimitItems)){
				Integer sum = topicLimitItems.get(0).getBuyedQuantity();
				if (topicPolicyDTO.getTopicSum() - topicPolicyDTO.getQuantity() - sum < 0) {
					if (topicPolicyDTO.getTopicSum() - sum <= 0) {
						return new ResultInfo<Boolean>(new FailInfo("用户达到限购",OrderErrorCodes.PROMOTION_USERID_LIMIT_POLICY));
					} else {
						return new ResultInfo<Boolean>(new FailInfo("用户达到限购",OrderErrorCodes.PROMOTION_USERID_LIMIT_POLICY));
					}
				}
			}
		}
		
		
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}

	/**
	 * <pre>
	 * 获取该商品已经购买数量
	 * </pre>
	 * 
	 * @param orderLines
	 * @return
	 */
	private Integer getQuantityBought(List<OrderItem> orderLines) {
		Integer sum = 0;
		List<Long> subOrderIdList = new ArrayList<Long>();
		Map<Long, Integer> subOrderIdMapQuantity = new HashMap<Long, Integer>();

		for (OrderItem orderLine : orderLines) {
			subOrderIdList.add(orderLine.getOrderId());
			subOrderIdMapQuantity.put(orderLine.getOrderId(), orderLine.getQuantity());
		}
		List<SubOrder> subOrders = subOrderDao.selectListByIdList(subOrderIdList);
		for (SubOrder subOrder : subOrders) {
			if (!OrderConstant.ORDER_STATUS.CANCEL.code.equals(subOrder.getOrderStatus())) {// 不是取消订单
				sum += subOrderIdMapQuantity.get(subOrder.getId());
			}
		}
		return sum;
	}

	public int getBoughtQuantityForGroup(Long topicId, String sku, Long memberId){
		Map<String,Object> orderLine = new HashMap<String,Object>();
		orderLine.put("topicId",topicId);
		orderLine.put("skuCode",sku);
		orderLine.put("memberId",memberId);
		// by topId,skuCode,userId search
	    List<OrderItem>	orderLines = orderItemDao.queryByParam(orderLine);
		if(org.springframework.util.CollectionUtils.isEmpty(orderLines)) return 0;
		return getQuantityBought(orderLines);

	}

	public int getBoughtCountWithGroupId(Long groupId,Long memberId){

		Map<String,Object> query = new HashMap<>();
		query.put("groupId",groupId);
		query.put("memberId",memberId);
		List<SubOrder> subOrderList = subOrderDao.queryByParam(query);
		if(org.springframework.util.CollectionUtils.isEmpty(subOrderList)) return 0;
		int count = 0;
		for(SubOrder subOrder: subOrderList){
			if(subOrder!=null && !OrderConstant.ORDER_STATUS.CANCEL.code.equals(subOrder.getOrderStatus())){
				count++;
			}
		}
		return count;
	}
}
