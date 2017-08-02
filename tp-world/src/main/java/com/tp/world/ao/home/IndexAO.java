package com.tp.world.ao.home;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.cms.AppTaiHaoTempletConstant;
import com.tp.dto.cms.app.AppIndexAdvertReturnData;
import com.tp.dto.cms.app.AppSingleAllInfoDTO;
import com.tp.dto.cms.app.AppSingleInfoDTO;
import com.tp.dto.common.ResultInfo;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.constant.PageConstant;
import com.tp.m.enums.MResultInfo;
import com.tp.m.query.home.QueryIndex;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.home.BannerVO;
import com.tp.m.vo.home.IndexModuleVO;
import com.tp.m.vo.topic.TopicVO;
import com.tp.model.dss.PromoterInfo;
import com.tp.proxy.cms.HitaoAppProxy;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.world.ao.dss.DSSUserAO;
import com.tp.world.ao.dss.PromoterAO;
import com.tp.world.convert.IndexConvert;
import com.tp.world.helper.PropertiesHelper;
import com.tp.world.helper.RequestHelper;
import com.tp.world.helper.VersionHelper;

/**
 * 首页 业务层
 * @author zhuss
 * @2016年1月2日 下午3:48:38
 */
@Service
public class IndexAO {
	private static final Logger log=LoggerFactory.getLogger(IndexAO.class);
	
	@Autowired
	private HitaoAppProxy hitaoAppProxy;
	

	@Autowired
	private PropertiesHelper propertiesHelper;

	
	@Autowired
	private PromoterAO promoterAO;
	
	@Autowired
	PromoterInfoProxy promoterProxy;
	
	
	@Autowired
	private DSSUserAO dSSUserAO;


	/**
	 * 获取 主页广告位
	 * @return
	 */
	public MResultVO<List<BannerVO>> getBanners(QueryIndex indexQuery){
		try {
			boolean isApp = RequestHelper.isAPP(indexQuery.getApptype());
			AppIndexAdvertReturnData ad = hitaoAppProxy.getHaitaoAppService().carouseAdvert();
			if(null != ad)return new MResultVO<>(MResultInfo.SUCCESS,IndexConvert.convertBanners(ad,isApp));
			return new MResultVO<>(MResultInfo.SUCCESS);
		} catch (Exception e) {
			log.error("[API接口 - 首页广告位 Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}

	/**
	 * 首页-今日上新
	 * @return
	 */
	public MResultVO<Page<TopicVO>> getTodayNew(QueryIndex indexQuery) {
		try {
			PageInfo<AppSingleInfoDTO> singleinfo = hitaoAppProxy.getHaitaoAppService().htTopicList(StringUtil.getCurpageDefault(indexQuery.getCurpage()), PageConstant.DEFAULT_PAGESIZE);
			boolean isAPP = RequestHelper.isAPP(indexQuery.getApptype());
			int version = VersionHelper.getAppVersion4(indexQuery.getAppversion());
			boolean isNew = isAPP?  version>=VersionHelper.VERSION_150? true: false :true;

			if(null != singleinfo)return new MResultVO<>(MResultInfo.SUCCESS,IndexConvert.convertIndexTopic(singleinfo,isNew));
			return new MResultVO<>(MResultInfo.SUCCESS);
		} catch (Exception e) {
			log.error("[API接口 - 首页今日上新  Exception] = {}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	/**
	 * 首页-今日上线(主题团和单品团合并)
	 * @return
	 */
	public MResultVO<Page<TopicVO>> topicAndSingleGroup(QueryIndex indexQuery) {
		try {

			// by zhs for dss
			Long promoterId = null;
			if(indexQuery.getShopmobile() != null){
				ResultInfo<PromoterInfo> reltPromoter= dSSUserAO.getPromoterInfo( indexQuery.getShopmobile() );
				if(reltPromoter.isSuccess() && reltPromoter.getData()!=null){
					promoterId = reltPromoter.getData().getPromoterId();
				}
			}
			
			String shareUrl = propertiesHelper.shareProductUrl.trim();

			boolean isAPP = RequestHelper.isAPP(indexQuery.getApptype());
			int version = VersionHelper.getAppVersion4(indexQuery.getAppversion());
			boolean isNew = isAPP?  version>=VersionHelper.VERSION_150? true: false :true;
			
			PageInfo<AppSingleAllInfoDTO> singleinfo = null;
			if(promoterId != null){
//				singleinfo = hitaoAppProxy.getHaitaoAppService().htTopicAllListForDss(promoterId, StringUtil.getCurpageDefault(indexQuery.getCurpage()), PageConstant.DEFAULT_PAGESIZE_PROMOTER);								
				singleinfo = hitaoAppProxy.getHaitaoAppService().htTopicAllListForDss2(promoterId,null, StringUtil.getCurpageDefault(indexQuery.getCurpage()), PageConstant.DEFAULT_PAGESIZE);								
			}else{
				singleinfo = hitaoAppProxy.getHaitaoAppService().htTopicAllList(null, StringUtil.getCurpageDefault(indexQuery.getCurpage()), PageConstant.DEFAULT_PAGESIZE, AppTaiHaoTempletConstant.HAITAO_APP_DSS_TOPICLIST);
			}
						
			if(null != singleinfo){
				if(promoterId != null){
					return new MResultVO<>(MResultInfo.SUCCESS,IndexConvert.convertTopicAndSingleForDss(promoterId, singleinfo,shareUrl,isNew) );
				}else{
					return new MResultVO<>(MResultInfo.SUCCESS,IndexConvert.convertTopicAndSingle( singleinfo,shareUrl,isNew));
				}
			}
			return new MResultVO<>(MResultInfo.SUCCESS);
		} catch (Exception e) {
			log.error("[API接口 - 首页今日上新  Exception]", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	/**
	 * 获取首页模块
	 * @return
	 */
	public MResultVO<IndexModuleVO> queryIndexModular(QueryIndex indexQuery){
		try{
			IndexModuleVO vo = new IndexModuleVO();
			//广告位模块
			vo.setBanners(IndexConvert.convertBanners(hitaoAppProxy.carouseAdvert(),RequestHelper.isAPP(indexQuery.getApptype())));
			//功能标签模块
			vo.setLabs(IndexConvert.convertLabs(hitaoAppProxy.funLabForWorld()));
			return new MResultVO<>(MResultInfo.SUCCESS,vo);
		}catch(Exception e){
			log.error("[API接口 - 获取首页模块 Exception] = {}",e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
}
