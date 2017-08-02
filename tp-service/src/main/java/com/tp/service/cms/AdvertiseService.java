package com.tp.service.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.cms.CmsRedisKeyConstant;
import com.tp.common.vo.cms.TempleConstant;
import com.tp.dao.cms.AdvertiseInfoDao;
import com.tp.dao.cms.AdvertiseTypeDao;
import com.tp.model.cms.AdvertiseInfo;
import com.tp.model.cms.AdvertiseType;
import com.tp.mq.RabbitMqProducer;
import com.tp.mq.exception.MqClientException;
import com.tp.service.BaseService;
import com.tp.service.cms.IAdvertiseService;


/**
* 广告管理 Service
* @author szy
*/
@Service
public class AdvertiseService extends BaseService<AdvertiseInfo> implements IAdvertiseService{

	@Autowired
	RabbitMqProducer rabbitMqProducer;
	
	@Autowired
	AdvertiseInfoDao advertiseInfoDao;
	
	@Autowired
	AdvertiseTypeDao advertiseTypeDao;

	@Override
	public List<AdvertiseInfo> queryAdvertiseInfo(AdvertiseInfo cmsAdvertiseInfoDO) throws Exception {
		List<AdvertiseInfo> list = advertiseInfoDao.selectDynamic(cmsAdvertiseInfoDO);
		return list;
	}

	@Override
	public int deleteAdvertiseByIds(List<Long> ids) throws Exception {
		int count = advertiseInfoDao.deleteByIds(ids);
		return count;
	}

	@Override
	public AdvertiseInfo addAdvertiseByIds(AdvertiseInfo cmsAdvertiseInfoDO)
			throws Exception {
		cmsAdvertiseInfoDO = addAdvertiseDetailByIds(cmsAdvertiseInfoDO);
		//推送给mq
		send2CmsMessage(CmsRedisKeyConstant.CMS_INDEX_ADVERT_ALL);
		return cmsAdvertiseInfoDO;
	}

	private AdvertiseInfo addAdvertiseDetailByIds(AdvertiseInfo cmsAdvertiseInfoDO){
		advertiseInfoDao.insert(cmsAdvertiseInfoDO);
		return cmsAdvertiseInfoDO;
	}

	@Override
	public int updateAdvertiseByIds(AdvertiseInfo cmsAdvertiseInfoDO) throws Exception {
		int count = updateAdvertDetailByIds(cmsAdvertiseInfoDO);
		//推送给mq
		send2CmsMessage(CmsRedisKeyConstant.CMS_INDEX_ADVERT_ALL);
		return count;
	}

	private int updateAdvertDetailByIds(AdvertiseInfo cmsAdvertiseInfoDO) {
		int count = advertiseInfoDao.updateDynamic(cmsAdvertiseInfoDO);
		return count;
	}

	@Override
	public AdvertiseInfo selectById(Long id) throws Exception {
		AdvertiseInfo cmsAdvertiseInfoDO = advertiseInfoDao.selectById(id);
		return cmsAdvertiseInfoDO;
	}

	@Override
	public Map<String, Object> selectAdvertPageQuery(
			Map<String, Object> paramMap, AdvertiseInfo cmsAdvertiseInfoDO)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("counts", advertiseInfoDao.selectCountDynamic(cmsAdvertiseInfoDO));
		map.put("list", advertiseInfoDao.selectAdvertPageQuery(paramMap));
		return map;
	}

	@Override
	public int openAdvertiseByIds(List<Long> ids) throws Exception {
		int count = openAdvertDetailByIds(ids);
		//推送给mq
		send2CmsMessage(CmsRedisKeyConstant.CMS_INDEX_ADVERT_ALL);
		return count;
	}

	private int openAdvertDetailByIds(List<Long> ids) {
		int count = advertiseInfoDao.openByIds(ids);
		return count;
	}

	@Override
	public int noOpenAdvertiseByIds(List<Long> ids) throws Exception {
		int count = noOpenAdvertDetailByIds(ids);
		//推送给mq
		send2CmsMessage(CmsRedisKeyConstant.CMS_INDEX_ADVERT_ALL);
		return count;
	}

	private int noOpenAdvertDetailByIds(List<Long> ids) {
		int count = advertiseInfoDao.noOpenByIds(ids);
		return count;
	}

	/**
	 * 查询图片类型
	 */
	@Override
	public List<AdvertiseType> queryAdvertType(AdvertiseType cmsAdvertTypeDO)
			throws Exception {
		return advertiseTypeDao.selectDynamic(cmsAdvertTypeDO);
	}

	@Override
	public AdvertiseType selectAdvertTypeById(Long id) throws Exception {
		return advertiseTypeDao.selectById(id);
	}

	@Override
	public int addAdvertType(AdvertiseType cmsAdvertTypeDO) throws Exception {
		//保存之前判断下是否已存在接口标识
		Long i =advertiseTypeDao.selectIsIdentExist(cmsAdvertTypeDO);
		if(i > 0)
			return -1;
		
		advertiseTypeDao.insert(cmsAdvertTypeDO);
		return 1;
	}

	@Override
	public int updateAdvertType(AdvertiseType cmsAdvertTypeDO)
			throws Exception {
		//保存之前判断下是否已存在接口标识
		Long i = advertiseTypeDao.selectIsIdentExist(cmsAdvertTypeDO);
		if(i>0){
			return -1;
		}
		return advertiseTypeDao.updateDynamic(cmsAdvertTypeDO);
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

	@Override
	public BaseDao<AdvertiseInfo> getDao() {
		return advertiseInfoDao;
	}
	
}
