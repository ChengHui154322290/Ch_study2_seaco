package com.tp.dao.dss;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.dto.promoter.PromoterTopicItemDTO;
import com.tp.model.dss.PromoterTopic;
import com.tp.model.mmp.ItemOtherInfo;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicInfo;
import com.tp.model.mmp.TopicItemDss;

public interface PromoterTopicDao extends BaseDao<PromoterTopic> {

	List<TopicInfo> selectTopics( Map<String,Object> params);
	List<TopicInfo> queryTopicListByChannelCode(@Param("promoterId")Long promoterId,@Param("channelCode")String channelCode);
	
	List<PromoterTopicItemDTO> selectTopicItems(PromoterTopic pTopic);
	
	Long countTopicItems(PromoterTopic pTopic);

	
	List<TopicInfo>  selectBrandTopics(Map<String,Object> params);
	
	
	List<Topic> geBrandTopics(@Param("promoterId")Long promoterid,@Param("channelCode")String channelCode);			

	List<TopicItemDss> getBrandTopicItems(@Param("promoterId")Long promoterid,@Param("channelCode")String channelCode);			

	List<TopicItemDss> getOtherTopicItems(@Param("promoterId")Long promoterid,@Param("channelCode")String channelCode);

	List<TopicItemDss> getSingleTopicItem(@Param("promoterId")Long promoterid,@Param("channelCode")String channelCode);

	List<ItemOtherInfo> getItemOtherInfo(List<Long> itemids);			
	
}
