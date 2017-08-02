package com.tp.service.dss;

import java.util.List;
import java.util.Map;

import com.tp.dto.promoter.PromoterTopicItemDTO;
import com.tp.model.dss.PromoterTopic;
import com.tp.model.mmp.ItemOtherInfo;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicInfo;
import com.tp.model.mmp.TopicItemDss;
import com.tp.service.IBaseService;
/**
  * @author zhs
  * 分销员主题团上下架接口
  */
public interface IPromoterTopicService extends IBaseService<PromoterTopic>{

	List<TopicInfo> selectTopics( Map<String,Object> params);
	List<TopicInfo> queryTopicListByChannelCode(Long promoterId,String channelCode);
	List<PromoterTopicItemDTO> selectTopicItems(PromoterTopic pTopic);

	Long countTopicItems(PromoterTopic pTopic);

	List<TopicInfo>  selectBrandTopics(Map<String,Object> params);
	
	
	List<Topic> geBrandTopics(Long promoterid,String channelCode);			

	List<TopicItemDss> getBrandTopicItems(Long promoterid,String channelCode);



	List<TopicItemDss> getOtherTopicItems(Long promoterid,String channelCode);

	List<TopicItemDss> getSingleTopicItem(Long promoterid,String channelCode);

	List<ItemOtherInfo> getItemOtherInfo(List<Long> itemids);			


//	List<TopicInfo> listTopics( Long promoterid );
//	List<PromoterTopicItemDTO> listTopicItems(Long promoterid, Long topicid );
	
}
