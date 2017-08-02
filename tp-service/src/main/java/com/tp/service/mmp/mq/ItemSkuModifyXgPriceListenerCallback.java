package com.tp.service.mmp.mq;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.mmp.TopicMqConstants;
import com.tp.dto.prd.SkuDto;
import com.tp.model.mmp.TopicItem;
import com.tp.mq.MqMessageCallBack;
import com.tp.mq.RabbitMqProducer;
import com.tp.mq.exception.MqClientException;
import com.tp.service.mmp.ITopicItemService;
import com.tp.service.mmp.ITopicRedisService;
import com.tp.util.StringUtil;

/**
 * 监听SKU上修改促销价格消息
 * @author szy
 *
 */
@Service(value = "itemSkuModifyXgPriceListenerCallback")
public class ItemSkuModifyXgPriceListenerCallback implements MqMessageCallBack {
	
	@Autowired
	private ITopicItemService topicItemService;
	@Autowired
	private ITopicRedisService topicRedisService;
	@Autowired
	private RabbitMqProducer rabbitMqProducer;
	
	@Override
	public boolean execute(Object o) {
		List<SkuDto> skuDtoList = (List<SkuDto>)o;
		List<String> skuList = new ArrayList<String>();
		skuDtoList.forEach(new Consumer<SkuDto>(){
			public void accept(SkuDto skuDto) {
				skuList.add(skuDto.getSku());
			}
		});
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " sku in ('"+StringUtil.join(skuList, "'"+SPLIT_SIGN.COMMA+"'")+"')");
		List<TopicItem> topicItemList = topicItemService.queryByParam(params); 
		Set<Long> topicIdList = new HashSet<Long>();
		for(TopicItem topicItem:topicItemList){
			for(SkuDto skuDto:skuDtoList){
				if(topicItem.getSku().equals(skuDto.getSku())){
					topicItem.setTopicPrice(skuDto.getXgPrice());
					topicItem.setUpdateUser(skuDto.getSendType());
					topicItem.setUpdateTime(new Date());
					topicItemService.updateNotNullById(topicItem);
				}
			}
			topicIdList.add(topicItem.getTopicId());
		}
		topicIdList.forEach(new Consumer<Long>(){
			@Override
			public void accept(Long topicId) {
			    topicRedisService.insertNewPromotion(topicId, -1);
			    try {
					rabbitMqProducer.sendP2PMessage(TopicMqConstants.MQ_FOR_CMS_QUERRY_ID, topicId);
				} catch (MqClientException e) {
				}
			}
		});
		return Boolean.TRUE;
	}

}
