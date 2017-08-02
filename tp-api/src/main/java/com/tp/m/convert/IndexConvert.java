package com.tp.m.convert;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.app.AppAdvertiseInfoDTO;
import com.tp.dto.cms.app.AppIndexAdvertReturnData;
import com.tp.dto.cms.app.AppSingleAllInfoDTO;
import com.tp.dto.cms.app.AppSingleInfoDTO;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.m.base.Page;
import com.tp.m.enums.ImgEnum;
import com.tp.m.helper.ImgHelper;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.home.BannerVO;
import com.tp.m.vo.home.ContentVO;
import com.tp.m.vo.home.LabVO;
import com.tp.m.vo.topic.TopicVO;

import org.apache.commons.lang.StringUtils;

public class IndexConvert {

	/**
	 * 封装广告位
	 * @param
	 * @return
	 */
	public static List<BannerVO> convertBanners(AppIndexAdvertReturnData ad,boolean isApp){
		List<BannerVO> bannerlist = new ArrayList<>();
		if(null != ad){
			List<AppAdvertiseInfoDTO<Object>> urls = ad.getUrls();
			if(CollectionUtils.isNotEmpty(urls)){
				for (AppAdvertiseInfoDTO<Object> queryAdvertPulseInfo : urls) {
					//Type为空，跳出广告数据组装
					if(StringUtil.isBlank(queryAdvertPulseInfo.getType()) || queryAdvertPulseInfo.getInfo()==null){
						continue;
					}
					//app 屏蔽团购连接
					if(isApp && StringUtils.equals(queryAdvertPulseInfo.getLinkurl(),"tuanlist.html")) continue;

					AppSingleInfoDTO as=(AppSingleInfoDTO) queryAdvertPulseInfo.getInfo();
					BannerVO t = new BannerVO();
					t.setType(queryAdvertPulseInfo.getType());
					t.setImageurl(ImgHelper.getImgUrl(queryAdvertPulseInfo.getImageurl(), ImgEnum.Width.WIDTH_750));
					t.setContent(new ContentVO(as.getText(),as.getSku(),StringUtil.getStrByObj(as.getSpecialid())));
					bannerlist.add(t);
				}
			}
		}
		return bannerlist;
	}
	
	/**
	 * 封装功能标签
	 * @return
	 */
	public static List<LabVO> convertLabs(AppIndexAdvertReturnData result){
		List<LabVO> resu = new ArrayList<>();
		if(null != result){
			List<AppAdvertiseInfoDTO<Object>> tables = result.getTables();
			if(CollectionUtils.isNotEmpty(tables)){
				for(AppAdvertiseInfoDTO<Object> tag :tables){
					LabVO bv = new LabVO();
					bv.setImageurl(tag.getImageurl());
					bv.setType(tag.getType());
					bv.setTitle(tag.getName());
					bv.setLinkurl(tag.getLinkurl());
					AppSingleInfoDTO as=(AppSingleInfoDTO) tag.getInfo();
					if(null != as){
						ContentVO content = new ContentVO();
						content.setSku(as.getSku());
						content.setText(as.getText());
						content.setTid(StringUtil.getStrByObj(as.getSpecialid()));
						bv.setContent(content);
					}
					resu.add(bv);
				}
			}
		}
		return resu;
	}
	
	/**
	 * 今日上新
	 * @param singleinfo
	 * @return
	 */
	public static Page<TopicVO> convertIndexTopic(PageInfo<AppSingleInfoDTO> singleinfo,boolean newVersion){
		Page<TopicVO> pages = new Page<>();
		List<AppSingleInfoDTO> infos = singleinfo.getRows();
		List<TopicVO> tlist = new ArrayList<>();
		if(null != infos){
			if(CollectionUtils.isNotEmpty(infos)){
				for(AppSingleInfoDTO single : infos){
					//必须专题要经过审核的
					if(single.getStatus() != TopicStatus.PASSED.ordinal()){
						continue;
					}
					tlist.add(TopicConvert.convertTopic(single,newVersion));
				}
				pages.setFieldTCount(tlist,singleinfo.getPage(), singleinfo.getRecords());
			}
			pages.setCurpage(singleinfo.getPage());
		}
		return pages;
	}
	
	/**
	 * 专题团和单品团合并
	 * @param singleinfo
	 * @return
	 */
	public static Page<TopicVO> convertTopicAndSingle( PageInfo<AppSingleAllInfoDTO> singleinfo,String shareUrl,boolean isNew){
		Page<TopicVO> pages = new Page<>();
		List<AppSingleAllInfoDTO> infos = singleinfo.getRows();
		List<TopicVO> tlist = new ArrayList<>();
		if(null != infos){
			if(CollectionUtils.isNotEmpty(infos)){
				for(AppSingleAllInfoDTO single : infos){
					//必须专题要经过审核的
					if(single.getStatus() != TopicStatus.PASSED.ordinal()){
						continue;
					}
					String sUrl = null;
					if(single.isSingle())sUrl = shareUrl.replace("TID", single.getSpecialid().toString()).replace("SKU", single.getSku()==null? "":single.getSku());
										
					tlist.add(TopicConvert.convertTopicAndSingle(single,sUrl,isNew));
				}
				pages.setFieldTCount(tlist,singleinfo.getPage(), singleinfo.getRecords());
			}
			pages.setCurpage(singleinfo.getPage());
		}
		return pages;
	}
	
	
	/**
	 * 专题团和单品团合并 For Dss
	 * @param singleinfo
	 * @return
	 */
	public static Page<TopicVO> convertTopicAndSingleForDss(Long promoterId, PageInfo<AppSingleAllInfoDTO> singleinfo,String shareUrl ,boolean newVersion){
		Page<TopicVO> pages = new Page<>();
		List<AppSingleAllInfoDTO> infos = singleinfo.getRows();
		List<TopicVO> tlist = new ArrayList<>();
		if(null != infos){
			if(CollectionUtils.isNotEmpty(infos)){
				for(AppSingleAllInfoDTO single : infos){
					//必须专题要经过审核的
					if(single.getStatus() != TopicStatus.PASSED.ordinal()){
						continue;
					}
					String sUrl = null;
					if(single.isSingle()) sUrl = shareUrl.replace("TID", single.getSpecialid().toString()).replace("SKU", single.getSku());
						
					tlist.add(TopicConvert.convertTopicAndSingleForDSS(single,sUrl,newVersion));
				}
				pages.setFieldTCount(tlist,singleinfo.getPage(), singleinfo.getRecords());
			}
			pages.setCurpage(singleinfo.getPage());
		}
		return pages;
	}
	
	
}
