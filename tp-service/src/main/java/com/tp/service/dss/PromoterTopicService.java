package com.tp.service.dss;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.dss.PromoterTopicDao;
import com.tp.dto.promoter.PromoterTopicItemDTO;
import com.tp.model.dss.PromoterTopic;
import com.tp.model.mmp.ItemOtherInfo;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicInfo;
import com.tp.model.mmp.TopicItemDss;
import com.tp.service.BaseService;
import com.tp.service.dss.IPromoterTopicService;

@Service
public class PromoterTopicService extends BaseService<PromoterTopic> implements IPromoterTopicService {

	@Autowired
	private PromoterTopicDao promoterTopicDao;
	
	@Override
	public BaseDao<PromoterTopic> getDao() {
		return promoterTopicDao;
	}
	
	@Override
	public List<TopicInfo> selectTopics( Map<String,Object> params){
		return promoterTopicDao.selectTopics(params);
	}
	@Override
	public List<TopicInfo> queryTopicListByChannelCode(Long promoterId,String channelCode){
		return promoterTopicDao.queryTopicListByChannelCode(promoterId,channelCode);
	}
	@Override
	public List<PromoterTopicItemDTO> selectTopicItems(PromoterTopic pTopic){
		return promoterTopicDao.selectTopicItems(pTopic);
	}
	
	
	@Override
	public Long countTopicItems(PromoterTopic pTopic){
		return promoterTopicDao.countTopicItems(pTopic);
	}
	
	@Override
	public List<TopicInfo>  selectBrandTopics(Map<String,Object> params){
		return promoterTopicDao.selectBrandTopics(params);		
	}
	
	// 首页使用	品牌团
	@Override
	public List<Topic> geBrandTopics(Long promoterid,String channelCode){
		return promoterTopicDao.geBrandTopics(promoterid,channelCode);				
	}
	
	// 首页使用 品牌团商品
	@Override
	public List< TopicItemDss > getBrandTopicItems(Long promoterid,String channelCode){
		return promoterTopicDao.getBrandTopicItems(promoterid,channelCode);
	}

	
	// 首页使用 主题团商品
	@Override
	public List< TopicItemDss > getOtherTopicItems(Long promoterid,String channelCode){
		return promoterTopicDao.getOtherTopicItems(promoterid,channelCode);
	}

	@Override
	public List<TopicItemDss> getSingleTopicItem(Long promoterid,String channelCode) {
		return promoterTopicDao.getSingleTopicItem( promoterid,channelCode);
	}

	@Override
	public List<ItemOtherInfo> getItemOtherInfo(List<Long> itemids){
		return promoterTopicDao.getItemOtherInfo(itemids);
	}

			

	
//	@Override
//	public List<TopicInfo> listTopics( Long promoterid ){
//		List<String> list = new ArrayList<String>();
//		list.add(AppTaiHaoTempletConstant.HAITAO_APP_SINGLETEMP);
//		list.add(AppTaiHaoTempletConstant.HAITAO_APP_TOPICLIST);		
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("templecodelist", list);
//		params.put("elementype", ElementEnum.ACTIVITY.getValue() );
//		params.put("promoterid", promoterid );			
//		List<TopicInfo> listTopic = selectTopics(params) ;				
//		return listTopic;
//	}
//	
//	
//	@Override
//	public List<PromoterTopicItemDTO> listTopicItems(Long promoterid, Long topicid ){		
//		PromoterTopic pTopic = new PromoterTopic();
//		pTopic.setPromoterId(promoterid);
//		pTopic.setStatus(PROMOTERTOPIC_STATUS.OFFSHELF.code);
//		pTopic.setTopicId(topicid);
//		pTopic.setPageSize(null);
//		List<PromoterTopicItemDTO> listTopicItems = selectTopicItems(pTopic);	
//		return listTopicItems;
//	}	
}
