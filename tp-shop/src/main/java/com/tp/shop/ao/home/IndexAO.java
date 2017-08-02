package com.tp.shop.ao.home;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.DssConstant;
import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.app.AppIndexAdvertReturnData;
import com.tp.dto.cms.app.AppSingleAllInfoDTO;
import com.tp.dto.common.ResultInfo;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.constant.PageConstant;
import com.tp.m.enums.MResultInfo;
import com.tp.m.query.home.QueryIndex;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.home.BannerVO;
import com.tp.m.vo.home.IndexModuleVO;
import com.tp.m.vo.topic.TopicVO;
import com.tp.model.dss.PromoterInfo;
import com.tp.proxy.cms.HitaoAppProxy;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.shop.ao.dss.DSSUserAO;
import com.tp.shop.ao.dss.PromoterAO;
import com.tp.shop.convert.IndexConvert;
import com.tp.shop.helper.AuthHelper;
import com.tp.shop.helper.PropertiesHelper;
import com.tp.shop.helper.RequestHelper;

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
	
	@Autowired
	private AuthHelper authHelper;


	/**
	 * 获取 主页广告位
	 * @return
	 */
//	public MResultVO<List<BannerVO>> getBanners(QueryIndex indexQuery){
//		try {
//			boolean isApp = RequestHelper.isAPP(indexQuery.getApptype());
//			AppIndexAdvertReturnData ad = hitaoAppProxy.getHaitaoAppService().carouseAdvert();
//			if(null != ad)return new MResultVO<>(MResultInfo.SUCCESS,IndexConvert.convertBanners(ad,isApp));
//			return new MResultVO<>(MResultInfo.SUCCESS);
//		} catch (Exception e) {
//			log.error("[API接口 - 首页广告位 Exception]={}", e);
//			return new MResultVO<>(MResultInfo.CONN_ERROR);
//		}
//	}
	
	public MResultVO<List<BannerVO>> getBanners(QueryIndex indexQuery){
		try {
			AppIndexAdvertReturnData ad = hitaoAppProxy.getHaitaoAppService().carouseDssAdvert();
			if(null != ad)return new MResultVO<>(MResultInfo.SUCCESS,IndexConvert.convertBanners(ad,false));
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
//	public MResultVO<Page<TopicVO>> getTodayNew(QueryIndex indexQuery) {
//		try {
//			PageInfo<AppSingleInfoDTO> singleinfo = hitaoAppProxy.getHaitaoAppService().htTopicList(StringUtil.getCurpageDefault(indexQuery.getCurpage()), PageConstant.DEFAULT_PAGESIZE);
//			if(null != singleinfo)return new MResultVO<>(MResultInfo.SUCCESS,IndexConvert.convertIndexTopic(singleinfo));
//			return new MResultVO<>(MResultInfo.SUCCESS);
//		} catch (Exception e) {
//			log.error("[API接口 - 首页今日上新  Exception] = {}", e);
//			return new MResultVO<>(MResultInfo.CONN_ERROR);
//		}
//	}
	
	/**
	 * 首页-今日上线(主题团和单品团合并)
	 * @return
	 */
	public MResultVO<Page<TopicVO>> topicAndSingleGroup(QueryIndex indexQuery) {
		try {
			Long promoterId = null;
			//店铺关联渠道CODE
			if(StringUtil.isNotBlank(indexQuery.getShopmobile()) && !"hhb".equalsIgnoreCase(indexQuery.getChannelcode())){
				ResultInfo<PromoterInfo> rp= dSSUserAO.getPromoterInfo(indexQuery.getShopmobile());
				if(rp.isSuccess() && rp.getData()!=null){
					promoterId = rp.getData().getPromoterId();
				}
			}else if (StringUtil.isNotBlank(indexQuery.getToken())) { //1. 验证是否是管理员 2. 验证是否是分销员
				try {
				TokenCacheTO tokenCacheTO = authHelper.authToken(indexQuery.getToken());
				if (!promoterAO.getIsManager(indexQuery.getChannelcode(), tokenCacheTO.getTel())) {
						promoterId = promoterAO.authPromoter(indexQuery.getToken(), DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
				}
				} catch (Exception e) {
					log.error("VERIFY_IS_ADMIN_ERROR:",e);
				}
			}
			if (promoterId == null || "hhb".equalsIgnoreCase(indexQuery.getChannelcode()) ) {
				ResultInfo<PromoterInfo> rp= dSSUserAO.getPromoterInfoByChannelCode( indexQuery.getChannelcode() );
				if(rp.isSuccess() && rp.getData()!=null){
					promoterId = rp.getData().getPromoterId();
				}else{
					return new MResultVO<>(MResultInfo.FAILED);
				}
			}	
			String shareUrl = propertiesHelper.shareProductUrl.trim();											
			 PageInfo<AppSingleAllInfoDTO> singleinfo = hitaoAppProxy.getHaitaoAppService().htTopicAllListForDssWithMS(promoterId,indexQuery.getChannelcode(), StringUtil.getCurpageDefault(indexQuery.getCurpage()), PageConstant.DEFAULT_PAGESIZE);
			if(null != singleinfo){
				return new MResultVO<>(MResultInfo.SUCCESS,IndexConvert.convertTopicAndSingleForDss(promoterId, singleinfo,shareUrl) );														
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
			vo.setBanners(IndexConvert.convertBanners(hitaoAppProxy.getHaitaoAppService().carouseDssAdvert(),RequestHelper.isAPP(indexQuery.getApptype())));
			//功能标签模块
			vo.setLabs(IndexConvert.convertLabs(hitaoAppProxy.dssFunLab(indexQuery.getChannelcode())));
			return new MResultVO<>(MResultInfo.SUCCESS,vo);
		}catch(Exception e){
			log.error("[API接口 - 获取首页模块 Exception] = {}",e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
}
