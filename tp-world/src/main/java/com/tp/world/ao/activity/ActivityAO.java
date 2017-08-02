/**
 * 
 */
package com.tp.world.ao.activity;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.mmp.TopicConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.TopicType;
import com.tp.m.base.MResultVO;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicItem;
import com.tp.model.ord.OrderItem;
import com.tp.proxy.mmp.CouponProxy;
import com.tp.proxy.mmp.CouponUserProxy;
import com.tp.proxy.mmp.TopicItemProxy;
import com.tp.proxy.mmp.TopicProxy;
import com.tp.proxy.ord.OrderItemProxy;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.util.BeanUtil;

/**
 * @author ll
 * 活动业务层
 */
@Service
public class ActivityAO {
	private static final Logger log = LoggerFactory.getLogger(ActivityAO.class);
	@Autowired
	private CouponUserProxy couponUserProxy;
	
	@Autowired
	private CouponProxy couponProxy;
	
	@Autowired
	private TopicProxy topicProxy;
	@Autowired 
	TopicItemProxy topicItemProxy;
	
	@Autowired
	private OrderItemProxy orderItemProxy;

	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	
	/**
	 * 向指定手机号发送指定名称的优惠券,每号仅限一张
	 * @param mobile
	 * @param couponName
	 * @return
	 */
	public MResultVO<Object> sendCoupon2UserUnique (String mobile, String couponName) {
		Boolean x = (Boolean) jedisCacheUtil.getCache("ACTIVITY_"+couponName+"_"+mobile);
//		if(jedisCacheUtil.getCache("ACTIVITY_"+couponName+"_"+mobile) != null && (boolean) jedisCacheUtil.getCache("ACTIVITY_"+couponName+"_"+mobile)) {
//			return new MResultVO<>("-1","已领过优惠券");
//		}
		
		Coupon query = new Coupon();
		query.setCouponName(couponName);
		
		ResultInfo<Coupon> coupon = couponProxy.queryUniqueByObject(query);
		
		if(coupon.getData() == null) {
			log.warn("无此优惠券{}" , couponName);
			return new MResultVO<>("-1","无此优惠券");
		}
		
		ResultInfo<Object> sendMobileCouponUnique = couponUserProxy.sendMobileCouponUnique(coupon.getData().getId(), mobile, 1);
		jedisCacheUtil.setCache("ACTIVITY_"+couponName+"_"+mobile, true, 60*30);
		if(sendMobileCouponUnique != null && !sendMobileCouponUnique.success) {
			return new MResultVO<>("-1","领取优惠券失败");
		}
		
		return new MResultVO<>("1","领取优惠券成功", sendMobileCouponUnique.getData());
	}
	
	public MResultVO<String> getTopicLinkByTopicName(String topicName, Integer topicType, Long memberId) {
		Topic query = new Topic();
		query.setProgress(TopicConstant.TOPIC_PROCESS_PROCESSING);
		query.setStatus(TopicConstant.TOPIC_STATUS_AUDITED);
		query.setType(topicType);
		Map<String, Object> params = BeanUtil.beanMap(query);
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "start_time < now() and end_time > now() and name like '"+topicName+"%' ");
		List<Topic> topicList = topicProxy.queryByParam(params).getData();
		
		Topic topic = (Topic) jedisCacheUtil.getCache("ACTIVITY_TOPIC_"+topicType+"_"+topicName);;
		
		if(topic == null) {
			new ResultInfo<Object>(new FailInfo("没有符合的活动"));
		}
		
		if(topicType.equals(TopicType.THEME.ordinal())) {
			if(CollectionUtils.isNotEmpty(topicList))
				return new MResultVO<>("0","m.51seaco.com/hd.htm?tid=" + topicList.get(0).getId());
		}
		else if(topicType.equals(TopicType.SINGLE.ordinal())) {
			
			if(CollectionUtils.isNotEmpty(topicList)) {
				int r = new Random().nextInt(topicList.size());
				topic = topicList.get(r);
				TopicItem itemQuery = new TopicItem();
				itemQuery.setTopicId(topic.getId());
				List<TopicItem> list = topicItemProxy.queryByObject(itemQuery).getData();
				TopicItem item = null;
				if(CollectionUtils.isNotEmpty(list)) {
					item = list.get(0);
				}
				else {
					new ResultInfo<Object>(new FailInfo("活动下没有商品"));
				}
				OrderItem orderItemQuery = new OrderItem();
				orderItemQuery.setMemberId(memberId);
				orderItemQuery.setSkuCode(item.getSku());
				orderItemQuery.setTopicId(topic.getId());
				List<OrderItem> orderItemList = orderItemProxy.queryByObject(orderItemQuery).getData();
				if(item != null && CollectionUtils.isEmpty(orderItemList)) {
					return new MResultVO<>("0","m.51seaco.com/item.htm?tid="+ topic.getId() +"&sku=" + item.getSku());
				}
			}
			
		}
		
		return new MResultVO<>("-1","没有符合条件的活动");
	}
}
