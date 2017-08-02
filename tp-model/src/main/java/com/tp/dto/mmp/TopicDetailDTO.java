package com.tp.dto.mmp;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.tp.common.vo.mmp.AreaConstant;
import com.tp.common.vo.mmp.PlatformConstant;
import com.tp.model.mmp.*;

public class TopicDetailDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 797134947418312749L;

	/** 专题的基本信息 */
	private Topic topic; // 专题的基本信息
	/** 专题所包含的商品 */
	private List<TopicItem> topicItemList; // 专题所包含的商品

	/** 专题所包含的商品DTO */
	private List<TopicItemInfoDTO> topicItemDtoList; // 专题所包含的商品

	private List<AreaDO> areaList; // 专题所包含的地区

	private PolicyInfo policy;// 专题所包含的限购政策

	private List<Platform> platformList; // 专题所使用的平台

	private List<Long> relateTidList;// 关联专题列表

	private List<Integer> platformCodes;//平台code集合

	private List<RelateDTO> relateList;// 关联专题列表

	private List<TopicAuditLog> auditLogList; // 审批记录

	private List<TopicCouponDTO> couponList; // 优惠券列表

	private Boolean areaAll; // 所有区域

	private Boolean areaEastChina; // 华东

	private Boolean areaNorthChina; // 华北

	private Boolean areaCentChina; // 华中

	private Boolean areaSoutChina; // 华东

	private Boolean areaNortheastChina; // 东北

	private Boolean areaNorthwestChina; // 西北

	private Boolean areaSouthwestChina; // 西南

	// private Boolean areaHkMaTw; // 港澳台

	private Boolean platAll; // 所有平台

	private Boolean platPc; // pc平台

	private Boolean platApp; // app平台

	private Boolean platWap; // wap平台

	private Boolean platHapPreg;

	private String lateThanTime;

	private String earlyThanTime;

	private String createTime;

	private String startTime;

	private String endTime;

	private String byRegisterTimeValue;

	private String byUidValue;

	private String byIpValue;

	private String byMobileValue;
	
	private String byTopic;

	/** Pc图片全路径 */
	private String topicPcImageFull;

	/** 手机图片全路径 */
	private String topicMobileImageFull;

	/** 明日上新图片全路径 */
	private String topicNewImageFull;

	/** 可能感兴趣图片全路径 */
	private String topicInterestedImageFull;

	/** 海淘图片全路径 */
	private String topicHitaoImageFull;

	/** PC首页图片全路径 */
	private String pcImageFull;

	/** PC感兴趣图片全路径 */
	private String pcInterestImageFull;

	/** 移动首页图片全路径 */
	private String mobileImageFull;

	/** 商城图片全路径 */
	private String mallImageFull;

	/** 海淘图片全路径 */
	private String haitaoImageFull;

	/** 注册开始日期 */
	private String userRegisterStartTime;

	/** 注册结束日期 */
	private String userRegisterEndTime;

	/** 类型 */
	private String type;

	/** 活动跳转链接 */
	private String skipLink;

	/** 是否能拷贝 */
	private Boolean canCopy;
	
	private List<Long> promoterIdList;
	
	private List<TopicPromoter> topicPromoterList;

	public Topic getTopic() {
		if (this.topic != null) {
			try {
				String[] datePartten = { "yyyy-MM-dd HH:mm",
						"yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm",
						"yyyy/MM/dd HH:mm:ss" };
				if (!StringUtils.isBlank(this.createTime)) {
					topic.setCreateTime(DateUtils.parseDate(createTime,
							datePartten));
				}
				if (!StringUtils.isBlank(this.endTime)) {
					topic.setEndTime(DateUtils.parseDate(endTime, datePartten));
				}
				if (!StringUtils.isBlank(this.startTime)) {
					topic.setStartTime(DateUtils.parseDate(startTime,
							datePartten));
				}
				if (!StringUtils.isBlank(this.type)
						&& NumberUtils.isDigits(this.type)) {
					topic.setType(Integer.valueOf(this.type));
				}
			} catch (ParseException e) {
			}
		}
		return topic;
	}

	public List<Integer> getPlatformCodes() {
		return platformCodes;
	}

	public void setPlatformCodes(List<Integer> platformCodes) {
		this.platformCodes = platformCodes;
	}

	public void setTopic(Topic topic) {
		if (null != topic) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (null != topic.getCreateTime()) {
				this.createTime = sdf.format(topic.getCreateTime());
			}
			if (null != topic.getStartTime()) {
				this.startTime = sdf.format(topic.getStartTime());
			}
			if (null != topic.getEndTime()) {
				// Date today = new Date();
				// this.canCopy = today.compareTo(topic.getEndTime()) < 0;
				this.endTime = sdf.format(topic.getEndTime());
			}
			if (null != topic.getType()) {
				this.type = String.valueOf(topic.getType());
			}
			this.topic = topic;
		}
	}

	public List<TopicItem> getPromotionItemList() {
		return topicItemList;
	}

	public void setPromotionItemList(List<TopicItem> promotionItemList) {
		this.topicItemList = promotionItemList;
	}

	public List<AreaDO> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<AreaDO> areaList) {
		this.areaList = areaList;
	}

	public PolicyInfo getPolicy() {

		if (null != policy) {
			policy.setByIp("on".equalsIgnoreCase(byIpValue) ? 1 : 0);
			policy.setByMobile("on".equalsIgnoreCase(byMobileValue) ? 1 : 0);
			policy.setByUid("on".equalsIgnoreCase(byUidValue) ? 1 : 0);
			policy.setByRegisterTime("on".equalsIgnoreCase(byRegisterTimeValue) ? 1 : 0);
			policy.setByTopic("on".equalsIgnoreCase(byTopic) ? 1 : 0);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				if (!StringUtils.isBlank(this.getLateThanTime())) {
					policy.setLateThanTime(sdf.parse(this.getLateThanTime()));
				}
				if (!StringUtils.isBlank(this.getEarlyThanTime())) {
					policy.setEarlyThanTime(sdf.parse(this.getEarlyThanTime()));
				}
			} catch (ParseException e) {
				// TODO:异常处理
			}
		}
		return policy;
	}

	public PolicyInfo getPolicyForChangeOrder() {
		return policy;
	}

	public void setPolicy(PolicyInfo policy) {
		this.policy = policy;
	}

	public List<Platform> getPlatformList() {
		return platformList;
	}

	public void setPlatformList(List<Platform> platformList) {
		this.platformList = platformList;
	}

	public List<Long> getRelateTidList() {
		return relateTidList;
	}

	public void setRelateTidList(List<Long> relateTidList) {
		this.relateTidList = relateTidList;
	}

	/**
	 * @param topicItemList
	 *            the topicItemList to set
	 */
	public void setTopicItemList(List<TopicItem> topicItemList) {
		this.topicItemList = topicItemList;
	}

	/**
	 * @return the relateList
	 */
	public List<RelateDTO> getRelateList() {
		return relateList;
	}

	/**
	 * @param relateList
	 *            the relateList to set
	 */
	public void setRelateList(List<RelateDTO> relateList) {
		this.relateList = relateList;
	}

	/**
	 * @return the auditLogList
	 */
	public List<TopicAuditLog> getAuditLogList() {
		return auditLogList;
	}

	/**
	 * @param auditLogList
	 *            the auditLogList to set
	 */
	public void setAuditLogList(List<TopicAuditLog> auditLogList) {
		this.auditLogList = auditLogList;
	}

	/**
	 * 
	 * @return
	 */
	public int getAreaAllSelect() {
		if (null != this.topic) {
			return String.valueOf(AreaConstant.AREA_ALL).equals(
					this.topic.getAreaStr()) ? 1 : 0;
		}
		return 0;
	}

	/**
	 * 
	 * @return
	 */
	public int getAreaEastChinaSelect() {
		if (null != this.topic && null != this.topic.getAreaStr()
				&& !StringUtils.isBlank(this.topic.getAreaStr())) {
			return this.topic.getAreaStr().contains(
					String.valueOf(AreaConstant.AREA_EAST_CHINA)) ? 1 : 0;
		}
		return 0;
	}

	/**
	 * 
	 * @return
	 */
	public int getAreaNorthChinaSelect() {
		if (null != this.topic && null != this.topic.getAreaStr()
				&& !StringUtils.isBlank(this.topic.getAreaStr())) {
			return this.topic.getAreaStr().contains(
					String.valueOf(AreaConstant.AREA_NORTH_CHINA)) ? 1 : 0;
		}
		return 0;
	}

	/**
	 * 
	 * @return
	 */
	public int getAreaCentChinaSelect() {
		if (null != this.topic && null != this.topic.getAreaStr()
				&& !StringUtils.isBlank(this.topic.getAreaStr())) {
			return this.topic.getAreaStr().contains(
					String.valueOf(AreaConstant.AREA_CENT_CHINA)) ? 1 : 0;
		}
		return 0;
	}

	/**
	 * 
	 * @return
	 */
	public int getAreaSoutChinaSelect() {
		if (null != this.topic && null != this.topic.getAreaStr()
				&& !StringUtils.isBlank(this.topic.getAreaStr())) {
			return this.topic.getAreaStr().contains(
					String.valueOf(AreaConstant.AREA_SOUT_CHINA)) ? 1 : 0;
		}
		return 0;
	}

	/**
	 * 
	 * @return
	 */
	public int getAreaNortheastChinaSelect() {
		if (null != this.topic && null != this.topic.getAreaStr()
				&& !StringUtils.isBlank(this.topic.getAreaStr())) {
			return this.topic.getAreaStr().contains(
					String.valueOf(AreaConstant.AREA_NORTHEAST_CHINA)) ? 1 : 0;
		}
		return 0;
	}

	/**
	 * 
	 * @return
	 */
	public int getAreaNorthwestChinaSelect() {
		if (null != this.topic && null != this.topic.getAreaStr()
				&& !StringUtils.isBlank(this.topic.getAreaStr())) {
			return this.topic.getAreaStr().contains(
					String.valueOf(AreaConstant.AREA_NORTHWEST_CHINA)) ? 1 : 0;
		}
		return 0;
	}

	/**
	 * 
	 * @return
	 */
	public int getAreaSouthwestChinaSelect() {
		if (null != this.topic && null != this.topic.getAreaStr()
				&& !StringUtils.isBlank(this.topic.getAreaStr())) {
			return this.topic.getAreaStr().contains(
					String.valueOf(AreaConstant.AREA_SOUTHWEST_CHINA)) ? 1 : 0;
		}
		return 0;
	}

	// /**
	// *
	// * @return
	// */
	// public int getAreaHkMaTwSelect() {
	// if (null != this.topic && null != this.topic.getAreaStr()
	// && !StringUtils.isBlank(this.topic.getAreaStr())) {
	// return this.topic.getAreaStr().contains(
	// String.valueOf(AreaConstant.AREA_HKMATW)) ? 1 : 0;
	// }
	// return 0;
	// }

	/**
	 * @return the areaAll
	 */
	public Boolean isAreaAll() {
		return areaAll;
	}

	/**
	 * @param areaAll
	 *            the areaAll to set
	 */
	public void setAreaAll(Boolean areaAll) {
		this.areaAll = areaAll;
	}

	/**
	 * @return the areaEastChina
	 */
	public Boolean isAreaEastChina() {
		return areaEastChina;
	}

	/**
	 * @param areaEastChina
	 *            the areaEastChina to set
	 */
	public void setAreaEastChina(Boolean areaEastChina) {
		this.areaEastChina = areaEastChina;
	}

	/**
	 * @return the areaNorthChina
	 */
	public Boolean isAreaNorthChina() {
		return areaNorthChina;
	}

	/**
	 * @param areaNorthChina
	 *            the areaNorthChina to set
	 */
	public void setAreaNorthChina(Boolean areaNorthChina) {
		this.areaNorthChina = areaNorthChina;
	}

	/**
	 * @return the areaCentChina
	 */
	public Boolean isAreaCentChina() {
		return areaCentChina;
	}

	/**
	 * @param areaCentChina
	 *            the areaCentChina to set
	 */
	public void setAreaCentChina(Boolean areaCentChina) {
		this.areaCentChina = areaCentChina;
	}

	/**
	 * @return the areaSoutChina
	 */
	public Boolean isAreaSoutChina() {
		return areaSoutChina;
	}

	/**
	 * @param areaSoutChina
	 *            the areaSoutChina to set
	 */
	public void setAreaSoutChina(Boolean areaSoutChina) {
		this.areaSoutChina = areaSoutChina;
	}

	/**
	 * @return the areaNortheastChina
	 */
	public Boolean isAreaNortheastChina() {
		return areaNortheastChina;
	}

	/**
	 * @param areaNortheastChina
	 *            the areaNortheastChina to set
	 */
	public void setAreaNortheastChina(Boolean areaNortheastChina) {
		this.areaNortheastChina = areaNortheastChina;
	}

	/**
	 * @return the areaNorthwestChina
	 */
	public Boolean isAreaNorthwestChina() {
		return areaNorthwestChina;
	}

	/**
	 * @param areaNorthwestChina
	 *            the areaNorthwestChina to set
	 */
	public void setAreaNorthwestChina(Boolean areaNorthwestChina) {
		this.areaNorthwestChina = areaNorthwestChina;
	}

	/**
	 * @return the areaSouthwestChina
	 */
	public Boolean isAreaSouthwestChina() {
		return areaSouthwestChina;
	}

	/**
	 * @param areaSouthwestChina
	 *            the areaSouthwestChina to set
	 */
	public void setAreaSouthwestChina(Boolean areaSouthwestChina) {
		this.areaSouthwestChina = areaSouthwestChina;
	}

	// /**
	// * @return the areaHkMaTw
	// */
	// public Boolean isAreaHkMaTw() {
	// return areaHkMaTw;
	// }

	// /**
	// * @param areaHkMaTw
	// * the areaHkMaTw to set
	// */
	// public void setAreaHkMaTw(Boolean areaHkMaTw) {
	// this.areaHkMaTw = areaHkMaTw;
	// }

	/**
	 * 
	 * @return
	 */
	public int getPlatAllSelect() {
		if (null != this.topic && null != this.topic.getPlatformStr()
				&& !StringUtils.isBlank(this.topic.getPlatformStr())) {
			return this.topic.getPlatformStr().contains(
					String.valueOf(PlatformConstant.PLATFORM_ALL)) ? 1 : 0;
		}
		return 0;
	}

	/**
	 * 
	 * @return
	 */
	public int getPlatPcSelect() {
		if (null != this.topic && null != this.topic.getPlatformStr()
				&& !StringUtils.isBlank(this.topic.getPlatformStr())) {
			return this.topic.getPlatformStr().contains(
					String.valueOf(PlatformConstant.PLATFORM_PC)) ? 1 : 0;
		}
		return 0;
	}

	/**
	 * 
	 * @return
	 */
	public int getPlatAppSelect() {
		if (null != this.topic && null != this.topic.getPlatformStr()
				&& !StringUtils.isBlank(this.topic.getPlatformStr())) {
			return this.topic.getPlatformStr().contains(
					String.valueOf(PlatformConstant.PLATFORM_APP)) ? 1 : 0;
		}
		return 0;
	}

	/**
	 * 
	 * @return
	 */
	public int getPlatWapSelect() {
		if (null != this.topic && null != this.topic.getPlatformStr()
				&& !StringUtils.isBlank(this.topic.getPlatformStr())) {
			return this.topic.getPlatformStr().contains(
					String.valueOf(PlatformConstant.PLATFORM_WAP)) ? 1 : 0;
		}
		return 0;
	}

	/**
	 * 
	 * @return
	 */
	public int getPlatHapPregSelect() {
		if (null != this.topic && null != this.topic.getPlatformStr()
				&& !StringUtils.isBlank(this.topic.getPlatformStr())) {
			return this.topic.getPlatformStr().contains(
					String.valueOf(PlatformConstant.PLATFORM_HAPPERG)) ? 1 : 0;
		}
		return 0;
	}

	/**
	 * @return the platAll
	 */
	public Boolean isPlatAll() {
		return platAll;
	}

	/**
	 * @param platAll
	 *            the platAll to set
	 */
	public void setPlatAll(Boolean platAll) {
		this.platAll = platAll;
	}

	/**
	 * @return the platPc
	 */
	public Boolean isPlatPc() {
		return platPc;
	}

	/**
	 * @param platPc
	 *            the platPc to set
	 */
	public void setPlatPc(Boolean platPc) {
		this.platPc = platPc;
	}

	/**
	 * @return the platApp
	 */
	public Boolean isPlatApp() {
		return platApp;
	}

	/**
	 * @param platApp
	 *            the platApp to set
	 */
	public void setPlatApp(Boolean platApp) {
		this.platApp = platApp;
	}

	/**
	 * @return the platWap
	 */
	public Boolean isPlatWap() {
		return platWap;
	}

	/**
	 * @param platWap
	 *            the platWap to set
	 */
	public void setPlatWap(Boolean platWap) {
		this.platWap = platWap;
	}

	/**
	 * @return the platHapPreg
	 */
	public Boolean isPlatHapPreg() {
		return platHapPreg;
	}

	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		if (null != topic && null != topic.getCreateTime()) {
			return DateFormatUtils.format(topic.getCreateTime(),
					"yyyy-MM-dd HH:mm:ss");
		}
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		if (null != topic && null != topic.getStartTime()) {
			return DateFormatUtils.format(topic.getStartTime(),
					"yyyy-MM-dd HH:mm:ss");
		}
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		if (null != topic && null != topic.getEndTime()) {
			return DateFormatUtils.format(topic.getEndTime(),
					"yyyy-MM-dd HH:mm:ss");
		}
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @param platHapPreg
	 *            the platHapPreg to set
	 */
	public void setPlatHapPreg(Boolean platHapPreg) {
		this.platHapPreg = platHapPreg;
	}

	public int getByRegisterTime() {
		if (this.policy != null) {
			return this.policy.getByRegisterTime() == null ? 0 : this.policy
					.getByRegisterTime();
		}
		return 0;
	}

	public int getByUid() {
		if (this.policy != null) {
			return this.policy.getByUid() == null ? 0 : this.policy.getByUid();
		}
		return 0;
	}

	public int getByIp() {
		if (this.policy != null) {
			return this.policy.getByIp() == null ? 0 : this.policy.getByIp();
		}
		return 0;
	}

	public int getByMobile() {
		if (null != this.policy) {
			return this.policy.getByMobile() == null ? 0 : this.policy
					.getByMobile();
		}
		return 0;
	}

	public String getUserRegisterStartTime() {
		if (null != this.policy && 1 == this.policy.getByRegisterTime()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (null != this.policy.getLateThanTime()) {
				userRegisterStartTime = sdf.format(this.policy
						.getLateThanTime());
			} else {
				userRegisterStartTime = sdf.format(new Date());
			}
			return userRegisterStartTime;
		}
		return userRegisterStartTime;
	}

	public String getUserRegisterEndTime() {
		if (null != this.policy && 1 == this.policy.getByRegisterTime()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (null != this.policy.getEarlyThanTime()) {
				userRegisterEndTime = sdf
						.format(this.policy.getEarlyThanTime());
			} else {
				userRegisterEndTime = sdf.format(new Date());
			}
			return userRegisterEndTime;
		}
		return userRegisterEndTime;
	}

	/**
	 * @return the lateThanTime
	 */
	public String getLateThanTime() {
		return lateThanTime;
	}

	/**
	 * @param lateThanTime
	 *            the lateThanTime to set
	 */
	public void setLateThanTime(String lateThanTime) {
		this.lateThanTime = lateThanTime;
	}

	/**
	 * @return the earlyThanTime
	 */
	public String getEarlyThanTime() {
		return earlyThanTime;
	}

	/**
	 * @param earlyThanTime
	 *            the earlyThanTime to set
	 */
	public void setEarlyThanTime(String earlyThanTime) {
		this.earlyThanTime = earlyThanTime;
	}

	/**
	 * @return the byRegisterTimeValue
	 */
	public String getByRegisterTimeValue() {
		return byRegisterTimeValue;
	}

	/**
	 * @param byRegisterTimeValue
	 *            the byRegisterTimeValue to set
	 */
	public void setByRegisterTimeValue(String byRegisterTimeValue) {
		this.byRegisterTimeValue = byRegisterTimeValue;
	}

	/**
	 * @return the byUidValue
	 */
	public String getByUidValue() {
		return byUidValue;
	}

	/**
	 * @param byUidValue
	 *            the byUidValue to set
	 */
	public void setByUidValue(String byUidValue) {
		this.byUidValue = byUidValue;
	}

	/**
	 * @return the byIpValue
	 */
	public String getByIpValue() {
		return byIpValue;
	}

	/**
	 * @param byIpValue
	 *            the byIpValue to set
	 */
	public void setByIpValue(String byIpValue) {
		this.byIpValue = byIpValue;
	}

	/**
	 * @return the byMobileValue
	 */
	public String getByMobileValue() {
		return byMobileValue;
	}

	/**
	 * @param byMobileValue
	 *            the byMobileValue to set
	 */
	public void setByMobileValue(String byMobileValue) {
		this.byMobileValue = byMobileValue;
	}

	public String getPlatform() {
		List<String> platformList = new ArrayList<String>(4);
		if (null != this.isPlatAll() && this.isPlatAll()) {
			return String.valueOf(PlatformConstant.PLATFORM_ALL);
		}
		if (null != this.isPlatPc() && this.isPlatPc()) {
			platformList.add(String.valueOf(PlatformConstant.PLATFORM_PC));
		}
		if (null != this.isPlatApp() && this.isPlatApp()) {
			platformList.add(String.valueOf(PlatformConstant.PLATFORM_APP));
		}
		if (null != this.isPlatWap() && this.isPlatWap()) {
			platformList.add(String.valueOf(PlatformConstant.PLATFORM_WAP));
		}
		if (null != this.isPlatHapPreg() && this.isPlatHapPreg()) {
			platformList.add(String.valueOf(PlatformConstant.PLATFORM_HAPPERG));
		}
		if (platformList.size() > 0) {
			return StringUtils.join(platformList, ",");
		}
		return "";
	}

	public String getArea() {
		List<String> areaList = new ArrayList<String>(8);
		if (this.isAreaAll() != null && this.isAreaAll()) {
			return String.valueOf(AreaConstant.AREA_ALL);
		}
		if (null != this.isAreaCentChina() && this.isAreaCentChina()) {
			areaList.add(String.valueOf(AreaConstant.AREA_CENT_CHINA));
		}
		if (null != this.isAreaEastChina() && this.isAreaEastChina()) {
			areaList.add(String.valueOf(AreaConstant.AREA_EAST_CHINA));
		}
		// if (null != this.isAreaHkMaTw() && this.isAreaHkMaTw()) {
		// areaList.add(String.valueOf(AreaConstant.AREA_HKMATW));
		// }
		if (null != this.isAreaNorthChina() && this.isAreaNorthChina()) {
			areaList.add(String.valueOf(AreaConstant.AREA_NORTH_CHINA));
		}
		if (null != this.isAreaNortheastChina() && this.isAreaNortheastChina()) {
			areaList.add(String.valueOf(AreaConstant.AREA_NORTHEAST_CHINA));
		}
		if (null != this.isAreaNorthwestChina() && this.isAreaNorthwestChina()) {
			areaList.add(String.valueOf(AreaConstant.AREA_NORTHWEST_CHINA));
		}
		if (null != this.isAreaSoutChina() && this.isAreaSoutChina()) {
			areaList.add(String.valueOf(AreaConstant.AREA_SOUT_CHINA));
		}
		if (null != this.isAreaSouthwestChina() && this.isAreaSouthwestChina()) {
			areaList.add(String.valueOf(AreaConstant.AREA_SOUTHWEST_CHINA));
		}
		if (areaList.size() > 0) {
			return StringUtils.join(areaList, ",");
		}
		return "";
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the topicPcImageFull
	 */
	public String getTopicPcImageFull() {
		return topicPcImageFull;
	}

	/**
	 * @param topicPcImageFull
	 *            the topicPcImageFull to set
	 */
	public void setTopicPcImageFull(String topicPcImageFull) {
		this.topicPcImageFull = topicPcImageFull;
	}

	/**
	 * @return the topicMobileImageFull
	 */
	public String getTopicMobileImageFull() {
		return topicMobileImageFull;
	}

	/**
	 * @param topicMobileImageFull
	 *            the topicMobileImageFull to set
	 */
	public void setTopicMobileImageFull(String topicMobileImageFull) {
		this.topicMobileImageFull = topicMobileImageFull;
	}

	/**
	 * @return the topicNewImageFull
	 */
	public String getTopicNewImageFull() {
		return topicNewImageFull;
	}

	/**
	 * @param topicNewImageFull
	 *            the topicNewImageFull to set
	 */
	public void setTopicNewImageFull(String topicNewImageFull) {
		this.topicNewImageFull = topicNewImageFull;
	}

	/**
	 * @return the topicItemDtoList
	 */
	public List<TopicItemInfoDTO> getTopicItemDtoList() {
		return topicItemDtoList;
	}

	/**
	 * @param topicItemDtoList
	 *            the topicItemDtoList to set
	 */
	public void setTopicItemDtoList(List<TopicItemInfoDTO> topicItemDtoList) {
		this.topicItemDtoList = topicItemDtoList;
	}

	/**
	 * @return the canCopy
	 */
	public Boolean getCanCopy() {
		return this.canCopy;
	}

	/**
	 * @param canCopy
	 *            the canCopy to set
	 */
	public void setCanCopy(Boolean canCopy) {
		this.canCopy = canCopy;
	}

	/**
	 * @return the skipLink
	 */
	public String getSkipLink() {
		return skipLink;
	}

	/**
	 * @param skipLink
	 *            the skipLink to set
	 */
	public void setSkipLink(String skipLink) {
		this.skipLink = skipLink;
	}

	/**
	 * @return the topicInterestedImageFull
	 */
	public String getTopicInterestedImageFull() {
		return topicInterestedImageFull;
	}

	/**
	 * @param topicInterestedImageFull
	 *            the topicInterestedImageFull to set
	 */
	public void setTopicInterestedImageFull(String topicInterestedImageFull) {
		this.topicInterestedImageFull = topicInterestedImageFull;
	}

	/**
	 * @return the topicHitaoImageFull
	 */
	public String getTopicHitaoImageFull() {
		return topicHitaoImageFull;
	}

	/**
	 * @param topicHitaoImageFull
	 *            the topicHitaoImageFull to set
	 */
	public void setTopicHitaoImageFull(String topicHitaoImageFull) {
		this.topicHitaoImageFull = topicHitaoImageFull;
	}

	/**
	 * @return the pcImageFull
	 */
	public String getPcImageFull() {
		return pcImageFull;
	}

	/**
	 * @param pcImageFull
	 *            the pcImageFull to set
	 */
	public void setPcImageFull(String pcImageFull) {
		this.pcImageFull = pcImageFull;
	}

	/**
	 * @return the pcInterestImageFull
	 */
	public String getPcInterestImageFull() {
		return pcInterestImageFull;
	}

	/**
	 * @param pcInterestImageFull
	 *            the pcInterestImageFull to set
	 */
	public void setPcInterestImageFull(String pcInterestImageFull) {
		this.pcInterestImageFull = pcInterestImageFull;
	}

	/**
	 * @return the mobileImageFull
	 */
	public String getMobileImageFull() {
		return mobileImageFull;
	}

	/**
	 * @param mobileImageFull
	 *            the mobileImageFull to set
	 */
	public void setMobileImageFull(String mobileImageFull) {
		this.mobileImageFull = mobileImageFull;
	}

	/**
	 * @return the mallImageFull
	 */
	public String getMallImageFull() {
		return mallImageFull;
	}

	/**
	 * @param mallImageFull
	 *            the mallImageFull to set
	 */
	public void setMallImageFull(String mallImageFull) {
		this.mallImageFull = mallImageFull;
	}

	/**
	 * @return the haitaoImageFull
	 */
	public String getHaitaoImageFull() {
		return haitaoImageFull;
	}

	/**
	 * @param haitaoImageFull
	 *            the haitaoImageFull to set
	 */
	public void setHaitaoImageFull(String haitaoImageFull) {
		this.haitaoImageFull = haitaoImageFull;
	}

	/**
	 * @return the couponList
	 */
	public List<TopicCouponDTO> getCouponList() {
		return couponList;
	}

	/**
	 * @param couponList
	 *            the couponList to set
	 */
	public void setCouponList(List<TopicCouponDTO> couponList) {
		this.couponList = couponList;
	}

	public int getByTopic() {
		if (this.policy != null) {
			return this.policy.getByTopic() == null ? 0 : this.policy.getByTopic();
		}
		return 0;
	}

	public void setByTopic(String byTopic) {
		this.byTopic = byTopic;
	}

	public List<Long> getPromoterIdList() {
		return promoterIdList;
	}

	public void setPromoterIdList(List<Long> promoterIdList) {
		this.promoterIdList = promoterIdList;
	}

	public List<TopicPromoter> getTopicPromoterList() {
		return topicPromoterList;
	}

	public void setTopicPromoterList(List<TopicPromoter> topicPromoterList) {
		this.topicPromoterList = topicPromoterList;
	}
}
