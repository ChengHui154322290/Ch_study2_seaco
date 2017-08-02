package com.tp.proxy.mem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mem.ErrorCode;
import com.tp.common.vo.mem.MemberInfoConstant;
import com.tp.common.vo.mem.SmsUtil;
import com.tp.common.vo.mem.Time;
import com.tp.common.vo.mem.UserRedisKey;
import com.tp.exception.UserServiceException;
import com.tp.model.mem.FavoritePromotion;
import com.tp.model.mmp.Topic;
import com.tp.proxy.BaseProxy;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.IBaseService;
import com.tp.service.mem.IFavoritePromotionService;
import com.tp.service.mem.ISendSmsService;
import com.tp.service.mmp.ITopicService;
import com.tp.util.StringUtil;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class FavoritePromotionProxy extends BaseProxy<FavoritePromotion>{

	@Autowired
	private IFavoritePromotionService favoritePromotionService;
	@Autowired
	private ITopicService topicService;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	@Autowired
	private ISendSmsService sendSmsService;

	@Override
	public IBaseService<FavoritePromotion> getService() {
		return favoritePromotionService;
	}
	public Long insertFavoritePromotion(FavoritePromotion favoritePromotion) throws UserServiceException{
		try {
			if(null == favoritePromotion.getPromotionId()) throw new UserServiceException(ErrorCode.PROMOTION_ID_IS_NULL.value);
//			if(StringUtil.isNullOrEmpty(favoritePromotionDO.getSkuCode())) throw new Exception(ErrorCode.SKUCODE_IS_NULL.value);
			if(StringUtil.isNullOrEmpty(favoritePromotion.getMobile())) throw new UserServiceException(ErrorCode.PROMOTION_MOBILE_IS_NULL.value);
			Topic topicDO = topicService.queryById(favoritePromotion.getPromotionId());
			if(null == topicDO) throw new UserServiceException(ErrorCode.TOPIC_IS_NOT_EXIST.code,ErrorCode.TOPIC_IS_NOT_EXIST.value);
			if(null == favoritePromotion.getUid()){
				if(null == favoritePromotion.getMobile()) throw new UserServiceException(ErrorCode.MOBILE_IS_NULL.code,ErrorCode.MOBILE_IS_NULL.value);
				logger.info("未登录用户关注活动,不入库,只发短信");
			}else{
				if(!topicService.checkFavouriteTopicStatus(favoritePromotion.getPromotionId())) {
					logger.error(">>>>>[ERROR!]: " + " promotion id is invalid.");
					throw new Exception(ErrorCode.PROMOTION_ID_IS_INVALID.value);
				}		
			
				
				FavoritePromotion fp = new FavoritePromotion();
				fp.setUid(favoritePromotion.getUid());
				fp.setPromotionId(favoritePromotion.getPromotionId());
				List<FavoritePromotion> list = favoritePromotionService.queryByObject(fp);
				if(null != list && !list.isEmpty()) throw new UserServiceException(ErrorCode.PROMOTION_HAS_FAVORITE.value);
				favoritePromotion.setCreateTime(new Date());
				favoritePromotion.setOnSaleTime(topicDO.getStartTime());
				favoritePromotion.setModifyTime(new Date());
				favoritePromotion.setIsDelete(Boolean.FALSE);
				
				logger.debug(">>>>>[begin]: insert favorite promotion: " + favoritePromotion.toString());
				favoritePromotion = favoritePromotionService.insert(favoritePromotion);
			}
			StringBuffer redisKey = new StringBuffer(UserRedisKey.FAVORITEPROMOTION.value).append(":").append(favoritePromotion.getMobile()).append(":").append(favoritePromotion.getPromotionId());
			Object o = jedisCacheUtil.getCache(redisKey.toString());
			if(null != o) throw new UserServiceException(ErrorCode.PROMOTION_HAS_FAVORITE.code,ErrorCode.PROMOTION_HAS_FAVORITE.value);
			
			Date sendTime = new Date(topicDO.getStartTime().getTime()-Time.ONE_MINUTE.value*15*1000);
			if(sendTime.before(new Date()))//已经过了发送时间 则立即发送
				sendSmsService.sendSms(favoritePromotion.getMobile(), SmsUtil.getFavoritePromotionSmsContent1(topicDO.getName()),null);
			else sendSmsService.sendSmsForTime(favoritePromotion.getMobile(), SmsUtil.getFavoritePromotionSmsContent1(topicDO.getName()), sendTime,null);
			
			jedisCacheUtil.setCache(redisKey.toString(), MemberInfoConstant.IsSuccess.Success, 15*Time.ONE_MINUTE.value.intValue());
		} catch (UserServiceException ex) {
			logger.error(">>>>>[ERROR!]: " + ex.getMessage(), ex);
			throw new UserServiceException(ex.getErrorCode(),ex.getMessage());
		} catch (Exception ex) {
			logger.error(">>>>>[ERROR!]: " + ex.getMessage(), ex);
			throw new UserServiceException(ex.getMessage());
		}
		return favoritePromotion.getId();
	}
	public PageInfo<FavoritePromotion> queryPageListByFavoritePromotion(FavoritePromotion favoritePromotionDO) throws UserServiceException{
		try {
			if (favoritePromotionDO != null) {
				PageInfo<FavoritePromotion> pageInfo = new PageInfo<FavoritePromotion>();
				pageInfo.setPage(favoritePromotionDO.getStartPage());
				pageInfo.setSize(favoritePromotionDO.getPageSize());
				return favoritePromotionService.queryPageByObject(favoritePromotionDO, pageInfo);
			}
		} catch (Exception ex) {
			logger.error(">>>>>[ERROR!]: " + ex.getMessage(), ex);
			throw new UserServiceException(ex.getMessage());
		}
		return new PageInfo<FavoritePromotion>();
	}

	public List<Long> selectPromotionIdsByUid(Long uid) throws UserServiceException{
		try {
			return favoritePromotionService.selectPromotionIdsByUid(uid);
		} catch (Exception ex) {
			logger.error(">>>>>[ERROR!]: " + ex.getMessage(), ex);
			throw new UserServiceException(ex.getMessage());
		}
	}

	public List<FavoritePromotion> getOnSalePromotionsByUid(Long uid) throws UserServiceException {
		if(null == uid || uid.longValue() == 0L) {
			logger.error(">>>>>[ERROR!]: Invalid uid.");
			throw new IllegalArgumentException(">>>>>[ERROR!]: Invalid uid.");
		}
		logger.debug(">>>>>[begin]: getOnSalePromotionsByUid, uid: " + uid);
		FavoritePromotion query = new FavoritePromotion();
		query.setUid(uid);
		query.setIsDelete(false);
		List<FavoritePromotion> list = new ArrayList<FavoritePromotion>();
		try {
			list = favoritePromotionService.queryByObject(query);
		} catch (Exception ex) {
			logger.error(">>>>>[ERROR!]: " + ex.getMessage(), ex);
			throw new UserServiceException(">>>>>[ERROR!]: Fail to get on sale promotions");
		}
		list =  filterOnSalePromotions(list);
		logger.debug(">>>>>[end]: getOnSalePromotionsByUid");
		return list;
	}

	private List<FavoritePromotion> filterOnSalePromotions(List<FavoritePromotion> all) {
		List<FavoritePromotion> result = new ArrayList<FavoritePromotion>();
		if(null == all || all.size() == 0)
			return result;
		Date now = new Date();
		for(FavoritePromotion favo : all) {
			if(null != favo.getOnSaleTime() && now.before(favo.getOnSaleTime())) 
				result.add(favo);
		}
		
		return result;
	}
}
