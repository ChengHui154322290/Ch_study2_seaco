package com.tp.service.cms.redis;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tp.common.vo.cms.CmsRedisKeyConstant;
import com.tp.dto.cms.temple.HomePageAdData;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.cms.CmsRedisDAO;
import com.tp.service.cms.ISingleBusTemService;
import com.tp.service.cms.ISinglePictureTemService;
import com.tp.service.cms.redis.ICmsRedisService;

/**
 *
 */
@Service
public class CmsRedisService implements ICmsRedisService{

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Autowired
	CmsRedisDAO cmsRedisDAO;
	
	@Autowired
	ISinglePictureTemService singlePictureTemService;
	
	@Autowired
	ISingleBusTemService singleBusTemService;
	
	private static final String RUN_REDIS_COLLECT = "runCmsRedisAdvertCollect";
	
	private static final String CHECK_REDIS_COLLECT = "CheckCmsRedisAdvertCollect";

	/**
	 * 此方法是把前台首页需要的图片以及公告资讯等信息拼装成html字符串模板，直接存放到缓存中供前台取
	 * 注意：singlePictureTemService是取html代码块，只能是topicRedisService调singlePictureTemService去取数据，不能出现相互调用情况
	 */
	@Override
	public void initRedis() {
		boolean lock = jedisCacheUtil.lock(RUN_REDIS_COLLECT);// 获得锁
		logger.info(String.format(
				"lock for init redis value:[%s]......" + new Date(), lock));
		if (lock) {
			try {
				logger.info("start init cms redis......" + new Date());
				
				//先清除掉redis的key值
				cmsRedisDAO.releaseKeyList();
				//往缓存中设值
				setCache();
				
				logger.info("end init cms redis......" + new Date());
			} catch (Exception e) {
				if (lock) {
					closeRedis();
				}
			} finally {
				if (lock) {
					closeRedis();
				}
			}
		}
	}

	/** 
	 * 调用singlePictureTemService接口取数据，
	 * 注意只能调singlePictureTemService接口，不能出现相互调用情况 
	**/
	private void setCache() {
		try {
			//首页-轮播图加载
			String carouselStr = singlePictureTemService.loadHomePageAdInfo();
			cmsRedisDAO.insertString(CmsRedisKeyConstant.CMS_ADVERT_CAROUSEL,carouselStr);
		} catch (Exception e) {
			logger.error("设值缓存(首页轮播图)出错",e);
		}
		
		try {
			//首页-右边-最优惠的加载
			String indexPreferentStr = singlePictureTemService.loadPreferentialHtml();
			cmsRedisDAO.insertString(CmsRedisKeyConstant.CMS_ADVERT_INDEX_PREFERENT,indexPreferentStr);
		} catch (Exception e) {
			logger.error("设值缓存(首页-右边-最优惠的加载)出错",e);
		}
		
		try {
			//最后疯抢-右边-最优惠加载
			String lastRushPreferentStr = singlePictureTemService.loadRushedPreferentialHtml();
			cmsRedisDAO.insertString(CmsRedisKeyConstant.CMS_ADVERT_LASTRUSHPRE,lastRushPreferentStr);
		} catch (Exception e) {
			logger.error("设值缓存(最后疯抢-右边-最优惠加载)出错",e);
		}
		
		try {
			//读取首页刚进去-弹出层
			String indexPopLayerStr = singlePictureTemService.loadIndexPicLinkHtml();
			cmsRedisDAO.insertString(CmsRedisKeyConstant.CMS_ADVERT_INDEX_POPLAYER,indexPopLayerStr);
		} catch (Exception e) {
			logger.error("设值缓存(首页刚进去-弹出层)出错",e);
		}
		
		try {
			//用户管理-用户登录-图片(一张)
			HomePageAdData userLoginPicStr = singlePictureTemService.loadUserIndexLogin();
			cmsRedisDAO.insertObject(CmsRedisKeyConstant.CMS_ADVERT_USER_LOGINPIC,userLoginPicStr);
		} catch (Exception e) {
			logger.error("设值缓存(用户管理-用户登录-图片)出错",e);
		}
		
		try {
			//首页-公告资讯的加载
			String indexAnnounceHtml = singleBusTemService.loadIndexAnnouncementHtml();
			cmsRedisDAO.insertString(CmsRedisKeyConstant.CMS_INDEX_ANNOUNCE,indexAnnounceHtml);
		} catch (Exception e) {
			logger.error("设值缓存(首页-公告资讯)出错",e);
		}
		
	}


	@Override
	public void closeRedis() {
		jedisCacheUtil.unLock(RUN_REDIS_COLLECT);// 释放锁
		logger.info("release lock for init redis......" + new Date());
	}


	@Override
	public void checkAdvanceRedis() {
		boolean lock = jedisCacheUtil.lock(CHECK_REDIS_COLLECT);// 获得锁
		logger.info(String.format(
				"lock for init redis value:[%s]......" + new Date(), lock));
		if (lock) {
			try {
				logger.info("start init cms redis......" + new Date());
				
				//取缓存值，判断是否有值，有值则直接退出，没有则全量更新，暂时定：轮播图和登录图片以及首页公告信息为主
				String carouselStr = (String) cmsRedisDAO.getValueString(CmsRedisKeyConstant.CMS_ADVERT_CAROUSEL);
				String announceStr = (String) cmsRedisDAO.getValueString(CmsRedisKeyConstant.CMS_INDEX_ANNOUNCE);
				if(StringUtils.isEmpty(carouselStr) || StringUtils.isEmpty(announceStr)){
					setCache();
				}
				
				logger.info("end init cms redis......" + new Date());
			} catch (Exception e) {
				if (lock) {
					closeRedis();
				}
			} finally {
				if (lock) {
					closeRedis();
				}
			}
		}
	}


	@Override
	public void closeCheckAdvanceRedis() {
		jedisCacheUtil.unLock(CHECK_REDIS_COLLECT);// 释放锁
		logger.info("release lock for init redis......" + new Date());
	}

}
