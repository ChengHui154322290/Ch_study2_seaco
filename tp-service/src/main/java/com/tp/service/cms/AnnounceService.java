package com.tp.service.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.vo.cms.CmsRedisKeyConstant;
import com.tp.common.vo.cms.TempleConstant;
import com.tp.dao.cms.AnnounceInfoDao;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.AnnounceInfo;
import com.tp.mq.RabbitMqProducer;
import com.tp.mq.exception.MqClientException;
import com.tp.service.cms.IAnnounceService;


/**
* 广告管理 Service
*/
@Service(value="announceService")
public class AnnounceService implements IAnnounceService{

	private final static Log logger = LogFactory.getLog(AnnounceService.class);
	
	@Autowired
	RabbitMqProducer rabbitMqProducer;
	
	@Autowired
	AnnounceInfoDao announceInfoDao;
	
	@Override
	public List<AnnounceInfo> queryAnnounceInfo(AnnounceInfo obj) throws Exception {
		List<AnnounceInfo> list = announceInfoDao.queryByObject(obj);
		return list;
	}

	@Override
	public int deleteAnnounceByIds(List<Long> ids) throws Exception {
		int count = delAnnounceDetailByIds(ids);//注意：需要保存成功后去推数据
		if(count > 0){
			//推送给mq
			send2CmsMessage(CmsRedisKeyConstant.CMS_INDEX_ANNOUNCE_ALL);
		}
		return count;
	}

	private int delAnnounceDetailByIds(List<Long> ids) throws CmsServiceException {
		int count = announceInfoDao.deleteByIds(ids);
		return count;
	}

	@Override
	public int addAnnounceByIds(AnnounceInfo cmsAnnounceInfoDO) throws Exception {
		addAnnounceDetailByIds(cmsAnnounceInfoDO);
		if(cmsAnnounceInfoDO.getId()!=null){
			//推送给mq
			send2CmsMessage(CmsRedisKeyConstant.CMS_INDEX_ANNOUNCE_ALL);
		}
		if(cmsAnnounceInfoDO.getId() == null) return 1;
		return 0;
	}

	private void addAnnounceDetailByIds(AnnounceInfo cmsAnnounceInfoDO) throws CmsServiceException {
		announceInfoDao.insert(cmsAnnounceInfoDO);
	}

	@Transactional
	@Override
	public int updateAnnounceByIds(AnnounceInfo cmsAnnounceInfoDO)
			throws Exception {
		int count = updateAnnounceDetailByIds(cmsAnnounceInfoDO);
		if(count > 0){
			//推送给mq
			send2CmsMessage(CmsRedisKeyConstant.CMS_INDEX_ANNOUNCE_ALL);
		}
		return count;
	}

	private int updateAnnounceDetailByIds(AnnounceInfo cmsAnnounceInfoDO) throws CmsServiceException {
		int count = announceInfoDao.updateNotNullById(cmsAnnounceInfoDO);
		return count;
	}

	@Override
	public AnnounceInfo queryAnnounceInfoByID(Long id) throws Exception {
		AnnounceInfo list = announceInfoDao.queryById(id);
		return list;
	}
	
	@Override
	public Map<String, Object> selectAnnouncePageQuery(
			Map<String, Object> paramMap,AnnounceInfo cmsAnnounceInfoDO) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer counts = announceInfoDao.queryByObjectCount(cmsAnnounceInfoDO);
		map.put("counts", counts);
		map.put("list", announceInfoDao.selectAnnouncePageQuery(paramMap));
		return map;
		
	}
	
	/**
	 * 
	 * <pre>
	 * 	 推送消息给cms
	 * </pre>
	 *
	 * @param key，即一个字符串，是存放缓存的key的字符串
	 * @throws MqClientException 
	 */
	private void send2CmsMessage(String key) throws MqClientException{
		rabbitMqProducer.sendP2PMessage(TempleConstant.CMS_ADVERTISE_PUB_MSG, key);
	}
	
}
