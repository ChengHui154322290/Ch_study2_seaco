/**
 * 
 */
package com.tp.service.cms.mq;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.cms.CmsRedisKeyConstant;
import com.tp.dto.cms.app.AppAdvertiseInfoDTO;
import com.tp.dto.cms.app.AppIndexAdvertReturnData;
import com.tp.dto.cms.temple.HomePageAdData;
import com.tp.mq.MqMessageCallBack;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.cms.CmsRedisDAO;
import com.tp.service.cms.ISingleBusTemService;
import com.tp.service.cms.ISinglePictureTemService;
import com.tp.service.cms.app.IAdvertiseAppService;

/**
 *
 */
@Service("cmsAdvertUpdateListenerCallback")
public class CmsAdvertUpdateListenerCallback implements MqMessageCallBack {

	@Autowired
	ISinglePictureTemService singlePictureTemService;
	
	@Autowired
	ISingleBusTemService singleBusTemService;
	
	@Autowired
	CmsRedisDAO cmsRedisDAO;
	
	@Autowired
	IAdvertiseAppService advertiseAppService;
	
	@Autowired
	private JedisCacheUtil jedisCacheUtil;

	private Log logger = LogFactory.getLog(this.getClass());
	
	private static final String CHECK_REDIS_COLLECT = "checkRedisCollect";

	/*
	 * (non-Javadoc)
	 * 
	 */
	@Override
	public boolean execute(Object o) {
		boolean lock = jedisCacheUtil.lock(CHECK_REDIS_COLLECT);// 获得锁
		logger.info("start mq excute.......");
		String key = (String) o;
		logger.info("mq cmsAdvert key:" + key);
		if (lock) {
			try {
				switch(key) 
				{
					/** 图片所有更新，APP暂时放到全部更新里面，以后有的话可以单个更新 **/
					case CmsRedisKeyConstant.CMS_INDEX_ADVERT_ALL: 
						updateAdvertInfoAll();
					/** 资讯所有更新 **/
					case CmsRedisKeyConstant.CMS_INDEX_ANNOUNCE_ALL: 
						updateAnnounceInfoAll();
					/** 首页-最上面-轮播图 **/
					case CmsRedisKeyConstant.CMS_ADVERT_CAROUSEL: 
						loadHomePageAdinfo();
					/** 读取首页刚进去-弹出层 **/
					case CmsRedisKeyConstant.CMS_ADVERT_INDEX_POPLAYER: 
						loadIndexPicLinkHtml();
					/** 首页-右边-最优惠的加载 **/
					case CmsRedisKeyConstant.CMS_ADVERT_INDEX_PREFERENT:
						loadPreferential();
					/** 最后疯抢-右边-最优惠加载 **/
					case CmsRedisKeyConstant.CMS_ADVERT_LASTRUSHPRE: 
						loadRushedPreferentialHtml();
					/** 用户管理-用户登录-图片(一张) **/
					case CmsRedisKeyConstant.CMS_ADVERT_USER_LOGINPIC: 
						loadUserIndexLogin();
					/** 首页公告资讯 **/
					case CmsRedisKeyConstant.CMS_INDEX_ANNOUNCE: 
						loadIndexAnnounce();
					/** 海淘自定义区域 **/
					case CmsRedisKeyConstant.CMS_INDEX_HAITAO_ANNOUNCE: 
						haitaoLoadDefindBanner();
					default :
						logger.info("结束："+key);
				}
			}catch (Exception e) {
				logger.error("设值key="+key+"的缓存出错",e);
				closeRedisLock(CHECK_REDIS_COLLECT);
				return false;
			} finally {
				if (lock) {
					closeRedisLock(CHECK_REDIS_COLLECT);
				}
			}
		}
		
			
		return true;
	}
	
	/**
	 * 释放锁
	 * @param strLock
	 */
	public void closeRedisLock(String strLock) {
		jedisCacheUtil.unLock(strLock);// 释放锁
		logger.info("release lock for init redis......" + new Date());
	}
	
	/**
	 * 
	 * 全部更新公告资讯操作
	 * @throws Exception 
	 * 
	 */
	private void updateAnnounceInfoAll() throws Exception{
		loadIndexAnnounce();
		haitaoLoadDefindBanner();
	}

	private void haitaoLoadDefindBanner() {
		String str;
		//海淘自定义区域
		try {
			str = singleBusTemService.haiTaoloadDefinedBannerHtml();
			cmsRedisDAO.insertString(CmsRedisKeyConstant.CMS_INDEX_HAITAO_ANNOUNCE, str);
		} catch (Exception e) {
			logger.error("公告资讯->全部更新->海淘自定义区域的缓存更新报错",e);
		}
	}

	private void loadIndexAnnounce() {
		String str = "";
		//首页公告资讯
		try {
			str = singleBusTemService.loadIndexAnnouncementHtml();
			cmsRedisDAO.insertString(CmsRedisKeyConstant.CMS_INDEX_ANNOUNCE, str);
		} catch (Exception e) {
			logger.error("公告资讯->全部更新->首页公告资讯的缓存更新报错",e);
		}
	}

	/**
	 * 
	 * 全部更新图片类型操作
	 * @throws Exception 
	 * 
	 */
	private void updateAdvertInfoAll() throws Exception{
		loadHomePageAdinfo();
		
		loadPreferential();
		
		loadRushedPreferentialHtml();
		
		loadIndexPicLinkHtml();
		
		loadUserIndexLogin();
		
		queryIndexAdvertiseInfo();
		
		querySecKillAdvertiseInfo();
		
		queryIndexFunlabInfo();
		
		queryGrainAdvertiseInfo();
		
		queryAdvertPulseInfo();
		
		queryAdvertPayInfo();
		
		queryWapAdvertInfo();
		
		queryWapChosenHeadPic();
	}

	private void queryWapChosenHeadPic() {
		AppAdvertiseInfoDTO<Object> obj = new AppAdvertiseInfoDTO<Object>();
		//Wap-今日精选-首图(APP)
		try {
			obj = advertiseAppService.queryWapChosenHeadPic();
			cmsRedisDAO.insertObject(CmsRedisKeyConstant.CMS_ADVERT_PREFIX+CmsRedisKeyConstant.APP_WAP_CHOSEN_PICTURE, obj);
		} catch (Exception e) {
			logger.error("图片类型->全部更新->Wap-今日精选-首图(APP)的缓存更新报错",e);
		}
	}

	private void queryWapAdvertInfo() {
		AppAdvertiseInfoDTO<Object> obj = new AppAdvertiseInfoDTO<Object>();
		//wap-首页弹框(APP)
		try {
			obj = advertiseAppService.queryWapAdvertInfo();
			cmsRedisDAO.insertObject(CmsRedisKeyConstant.CMS_ADVERT_PREFIX+CmsRedisKeyConstant.APP_WAPINDEX_ADVERT_TYPE, obj);
		} catch (Exception e) {
			logger.error("图片类型->全部更新->wap-首页弹框(APP)的缓存更新报错",e);
		}
	}

	private void queryAdvertPayInfo() {
		AppAdvertiseInfoDTO<Object> obj = new AppAdvertiseInfoDTO<Object>();
		//广告-支付成功(APP)
		try {
			obj = advertiseAppService.queryAdvertPayInfo();
			cmsRedisDAO.insertObject(CmsRedisKeyConstant.CMS_ADVERT_PREFIX+CmsRedisKeyConstant.APP_PAY_ADVERT_TYPE, obj);
		} catch (Exception e) {
			logger.error("图片类型->全部更新->广告-支付成功(APP)的缓存更新报错",e);
		}
	}

	private void queryAdvertPulseInfo() {
		AppAdvertiseInfoDTO<Object> obj = new AppAdvertiseInfoDTO<Object>();
		//广告-启动页面(APP)
		try {
			obj = advertiseAppService.queryAdvertPulseInfo();
			cmsRedisDAO.insertObject(CmsRedisKeyConstant.CMS_ADVERT_PREFIX+CmsRedisKeyConstant.APP_PULSE_ADVERT_TYPE, obj);
		} catch (Exception e) {
			logger.error("图片类型->全部更新->广告-启动页面(APP)的缓存更新报错",e);
		}
	}

	private void queryGrainAdvertiseInfo() {
		AppAdvertiseInfoDTO<Object> obj = new AppAdvertiseInfoDTO<Object>();
		//海淘-广告位信息(APP)
		try {
			obj = advertiseAppService.queryGrainAdvertiseInfo();
			cmsRedisDAO.insertObject(CmsRedisKeyConstant.CMS_ADVERT_PREFIX+CmsRedisKeyConstant.APP_GRAIN_ADVERT_TYPE, obj);
		} catch (Exception e) {
			logger.error("图片类型->全部更新->海淘-广告位信息(APP)的缓存更新报错",e);
		}
	}

	private void queryIndexFunlabInfo() {
		AppIndexAdvertReturnData appIndexAdvertReturnData = new AppIndexAdvertReturnData();
		//首页-功能标签信息(APP)
		try {
			appIndexAdvertReturnData = advertiseAppService.queryIndexFunlabInfo();
			cmsRedisDAO.insertObject(CmsRedisKeyConstant.CMS_ADVERT_PREFIX+CmsRedisKeyConstant.APP_INDEX_FUNCTION_TYPE, appIndexAdvertReturnData);
		} catch (Exception e) {
			logger.error("图片类型->全部更新->首页-功能标签信息(APP)的缓存更新报错",e);
		}
	}

	private void querySecKillAdvertiseInfo() {
		AppAdvertiseInfoDTO<Object> obj = new AppAdvertiseInfoDTO<Object>();
		//秒杀-广告位信息(APP)
		try {
			obj = advertiseAppService.querySecKillAdvertiseInfo();
			cmsRedisDAO.insertObject(CmsRedisKeyConstant.CMS_ADVERT_PREFIX+CmsRedisKeyConstant.APP_SECKILL_ADVERT_TYPE, obj);
		} catch (Exception e) {
			logger.error("图片类型->全部更新->秒杀-广告位信息(APP)的缓存更新报错",e);
		}
	}

	private void queryIndexAdvertiseInfo() {
		//首页-广告位(APP)
		AppIndexAdvertReturnData appIndexAdvertReturnData = new AppIndexAdvertReturnData();
		try {
			appIndexAdvertReturnData = advertiseAppService.queryIndexAdvertiseInfo();
			cmsRedisDAO.insertObject(CmsRedisKeyConstant.CMS_ADVERT_PREFIX+CmsRedisKeyConstant.APP_INDEX_ADVERT_TYPE, appIndexAdvertReturnData);
		} catch (Exception e) {
			logger.error("图片类型->全部更新->首页-广告位(APP)的缓存更新报错",e);
		}
	}

	private void loadUserIndexLogin() {
		HomePageAdData homePageAdData = new HomePageAdData();
		//用户管理-用户登录-图片(一张)
		try {
			homePageAdData = singlePictureTemService.loadUserIndexLogin();
			cmsRedisDAO.insertObject(CmsRedisKeyConstant.CMS_ADVERT_PREFIX+CmsRedisKeyConstant.CMS_ADVERT_USER_LOGINPIC, homePageAdData);
		} catch (Exception e) {
			logger.error("图片类型->全部更新->用户管理-用户登录-图片的缓存更新报错",e);
		}
	}

	private void loadIndexPicLinkHtml() {
		String str = "";
		//读取首页刚进去-弹出层
		try {
			str = singlePictureTemService.loadIndexPicLinkHtml();
			cmsRedisDAO.insertString(CmsRedisKeyConstant.CMS_ADVERT_PREFIX+CmsRedisKeyConstant.CMS_ADVERT_INDEX_POPLAYER, str);
		} catch (Exception e) {
			logger.error("图片类型->全部更新->读取首页刚进去-弹出层的缓存更新报错",e);
		}
	}

	private void loadRushedPreferentialHtml() {
		String str = "";
		//最后疯抢-右边-最优惠加载
		try {
			str = singlePictureTemService.loadRushedPreferentialHtml();
			cmsRedisDAO.insertString(CmsRedisKeyConstant.CMS_ADVERT_PREFIX+CmsRedisKeyConstant.CMS_ADVERT_LASTRUSHPRE, str);
		} catch (Exception e) {
			logger.error("图片类型->全部更新->最后疯抢-右边-最优惠的缓存更新报错",e);
		}
	}

	private void loadPreferential() {
		String str = "";
		//首页-右边-最优惠的加载
		try {
			str = singlePictureTemService.loadPreferentialHtml();
			cmsRedisDAO.insertString(CmsRedisKeyConstant.CMS_ADVERT_PREFIX+CmsRedisKeyConstant.CMS_ADVERT_INDEX_PREFERENT, str);
		} catch (Exception e) {
			logger.error("图片类型->全部更新->首页-右边-最优惠的缓存更新报错",e);
		}
	}

	private void loadHomePageAdinfo() {
		String str = "";
		//首页-最上面-轮播图
		try {
//			String ss = cmsRedisDAO.getValueString(CmsRedisKeyConstant.CMS_ADVERT_PREFIX+CmsRedisKeyConstant.CMS_ADVERT_CAROUSEL);
			str = singlePictureTemService.loadHomePageAdInfo();
			cmsRedisDAO.insertString(CmsRedisKeyConstant.CMS_ADVERT_PREFIX+CmsRedisKeyConstant.CMS_ADVERT_CAROUSEL, str);
		} catch (Exception e) {
			logger.error("图片类型->全部更新->首页-最上面-轮播图的缓存更新报错",e);
		}
	}
	
}
