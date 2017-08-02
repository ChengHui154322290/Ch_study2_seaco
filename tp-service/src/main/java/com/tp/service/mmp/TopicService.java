package com.tp.service.mmp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tp.common.dao.BaseDao;
import com.tp.common.util.ImageUtil;
import com.tp.common.util.mmp.DateTimeFormatUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.Constant.DEFAULTED;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.mmp.AreaConstant;
import com.tp.common.vo.mmp.ProcessingErrorMessage;
import com.tp.common.vo.mmp.TopicAuditLogConstant;
import com.tp.common.vo.mmp.TopicConstant;
import com.tp.common.vo.mmp.TopicMqConstants;
import com.tp.dao.mmp.CouponDao;
import com.tp.dao.mmp.PolicyInfoDao;
import com.tp.dao.mmp.RelateDao;
import com.tp.dao.mmp.TopicAuditLogDao;
import com.tp.dao.mmp.TopicCouponDao;
import com.tp.dao.mmp.TopicDao;
import com.tp.dao.mmp.TopicItemDao;
import com.tp.dao.mmp.TopicPromoterDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mem.BrandCollection;
import com.tp.dto.mem.ItemCollection;
import com.tp.dto.mmp.RelateDTO;
import com.tp.dto.mmp.SkuTopicDTO;
import com.tp.dto.mmp.TopicCouponDTO;
import com.tp.dto.mmp.TopicDetailDTO;
import com.tp.dto.mmp.TopicItemBrandCategoryDTO;
import com.tp.dto.mmp.TopicItemInfoDTO;
import com.tp.dto.mmp.TopicQueryDTO;
import com.tp.dto.mmp.enums.CmsForcaseType;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.dto.mmp.enums.ErrorCodeType;
import com.tp.dto.mmp.enums.InnerBizType;
import com.tp.dto.mmp.enums.InventoryOperType;
import com.tp.dto.mmp.enums.LockStatus;
import com.tp.dto.mmp.enums.ProgressStatus;
import com.tp.dto.mmp.enums.TopicProcess;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.dto.mmp.enums.TopicType;
import com.tp.dto.ord.CartDTO;
import com.tp.dto.ord.CartLineDTO;
import com.tp.dto.ord.ItemInventoryDTO;
import com.tp.dto.ord.remote.TopicPolicyDTO;
import com.tp.dto.prd.enums.ItemStatusEnum;
import com.tp.enums.common.PlatformEnum;
import com.tp.exception.ServiceException;
import com.tp.model.mem.MemberInfo;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.Platform;
import com.tp.model.mmp.PolicyInfo;
import com.tp.model.mmp.Relate;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicAuditLog;
import com.tp.model.mmp.TopicCoupon;
import com.tp.model.mmp.TopicInventoryAccBook;
import com.tp.model.mmp.TopicItem;
import com.tp.model.mmp.TopicPromoter;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;
import com.tp.query.mmp.CmsTopicQuery;
import com.tp.query.mmp.CmsTopicSimpleQuery;
import com.tp.query.mmp.ItemQuery;
import com.tp.query.mmp.TopicItemCartQuery;
import com.tp.query.mmp.TopicItemInfoQuery;
import com.tp.query.mmp.TopicItemPageQuery;
import com.tp.result.mem.app.ResultMessage;
import com.tp.result.mmp.TopicInfo;
import com.tp.result.mmp.TopicItemInfoResult;
import com.tp.service.BaseService;
import com.tp.service.bse.IClearanceChannelsService;
import com.tp.service.bse.IDistrictInfoService;
import com.tp.service.bse.INationalIconService;
import com.tp.service.dss.IPromoterTopicService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mmp.ITopicRedisService;
import com.tp.service.mmp.ITopicService;
import com.tp.service.mmp.mq.MQUtils;
import com.tp.service.ord.IOrderItemService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.prd.IItemDetailService;
import com.tp.service.stg.IInventoryOperService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.util.StringUtil;

@Service
public class TopicService extends BaseService<Topic> implements ITopicService {

	@Autowired
	private TopicDao topicDao;

	@Autowired
	private TopicItemDao topicItemDao;

	@Autowired
	private RelateDao relateDAO;

	@Autowired
	private PolicyInfoDao policyInfoDao;

	@Autowired
	private CouponDao couponDao;
	
	@Autowired
	private TopicPromoterDao topicPromoterDao;

	@Autowired
	private TopicCouponDao topicCouponDao;

	@Autowired
	private TopicAuditLogDao topicAuditLogDao;

	@Autowired
	private IMemberInfoService memberInfoService;

	@Autowired
	private IInventoryOperService inventoryOperService;

	@Autowired
	private IInventoryQueryService inventoryQueryService;

	@Autowired
	private IOrderItemService orderItemService;

	@Autowired
	private ISubOrderService subOrderService;

	@Autowired
	private ITopicRedisService topicRedisService;

	@Autowired
	private MQUtils mqUtils;

	@Autowired
	IPromoterTopicService promoterTopicService;

	@Autowired
	IItemDetailService itemDetailService;

	@Autowired
	IClearanceChannelsService clearanceChannelsService;

	@Autowired
	IDistrictInfoService districtInfoService;

	@Autowired
	INationalIconService nationalIconService;

	@Override
	public BaseDao<Topic> getDao() {
		return topicDao;
	}

	@Override
	public TopicDetailDTO getTopicDetailById(Long promoterid, long tid) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.info("[TopicService.getTopicDetailById]getTopicDetailById ---- topicId:" + tid);
		}
		TopicDetailDTO topicDetail = new TopicDetailDTO();
		Topic topic = queryById(tid);
		if (topic == null)
			throw new ServiceException(ProcessingErrorMessage.VALID_TOPIC_INFO);
		topicDetail.setTopic(topic);
		// 获得平台信息
		getPlatformInfo(topicDetail, topic);
		// 获得限购政策
		getPolicy(topicDetail, topic);
		// 获得关联专场
		// relateDAO
		getRelateInfo(tid, topicDetail);
		// 获得优惠券列表
		getCouponInfo(tid, topicDetail);
		// 获得审批记录
		getAuditInfo(tid, topicDetail);
		// 获得商品信息
		List<TopicItem> items = null;
		if (promoterid != null) {
			items = getTopicItemDOsForDss(promoterid, tid, false);
		} else {
			items = getTopicItemDOs(promoterid, tid, false);
		}

		topicDetail.setPromotionItemList(items);
		return topicDetail;
	}

	@Override
	public TopicDetailDTO getTopicDetailWithItemDTOById(long tid) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.info(
					"[TopicService.getTopicDetailWithItemDTOById]getTopicDetailWithItemDTOById ---- topicId:" + tid);
		}
		TopicDetailDTO topicDetail = new TopicDetailDTO();
		Topic Topic = queryById(tid);
		topicDetail.setTopic(Topic);
		// 获得平台信息
		getPlatformInfo(topicDetail, Topic);
		// 获得限购政策
		getPolicy(topicDetail, Topic);
		// 获得关联专场
		// relateDAO
		getRelateInfo(tid, topicDetail);
		// 获得优惠券列表
		getCouponInfo(tid, topicDetail);
		// 获得审批记录
		getAuditInfo(tid, topicDetail);
		// 获得商品信息
		List<TopicItemInfoDTO> items = getTopicItemDTOs(tid);
		List<TopicPromoter> topicPromoterList = topicPromoterDao.queryListByTopicId(tid);
		if(!CollectionUtils.isEmpty(topicPromoterList)){
			List<Long> promoterIdList = new ArrayList<Long>();
			for(TopicPromoter topicPromoter:topicPromoterList){
				promoterIdList.add(topicPromoter.getPromoterId());
			}
			topicDetail.setPromoterIdList(promoterIdList);
		}
		topicDetail.setTopicItemDtoList(items);
		return topicDetail;
	}

	@Override
	public List<TopicDetailDTO> queryTopicDetailList(List<Long> topicIdList) throws Exception {
		if (topicIdList != null && topicIdList.size() > 0) {
			List<TopicDetailDTO> res = new ArrayList<TopicDetailDTO>();
			for (Long topicId : topicIdList) {
				TopicDetailDTO dto = this.getTopicDetailById(null, topicId);
				res.add(dto);
			}
			return res;
		}
		return Collections.emptyList();
	}

	@Override
	public List<TopicItemInfoResult> getTopicItemInfo(TopicItemInfoQuery query) {
		try {
			long topicId = query.getTopicId();
			Topic topic = topicDao.queryById(topicId);
			// String areaStr = query.getAreaStr();
			List<String> skuList = query.getSkuList();
			if (!skuList.isEmpty() && skuList.size() > 0 && topic != null) {
				List<TopicItemInfoResult> resultList = new ArrayList<TopicItemInfoResult>();
				for (String sku : skuList) {
					TopicItem promotionItemDO = new TopicItem();
					promotionItemDO.setSku(sku);
					promotionItemDO.setTopicId(topicId);
					// 加上地区信息 TODO
					promotionItemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
					List<TopicItem> pitemList = topicItemDao.queryByObject(promotionItemDO);
					if (!pitemList.isEmpty() && pitemList.size() > 0) {
						TopicItem pitemDO = pitemList.get(0);
						TopicItemInfoResult result = new TopicItemInfoResult();
						result.setSku(sku);
						result.setItemId(pitemDO.getItemId());
						if (pitemDO.getStockAmount() != null)
							result.setStockAmount(pitemDO.getStockAmount());
						result.setLimitAmount(pitemDO.getLimitAmount());
						result.setTopicPrice(pitemDO.getTopicPrice());
						result.setTopicImage(pitemDO.getTopicImage());
						result.setSalePrice(pitemDO.getSalePrice());
						if (pitemDO.getLockStatus() != null) {
							result.setLockStatus(pitemDO.getLockStatus());
						} else {
							result.setLockStatus(LockStatus.UNLOCK.ordinal());
						}
						result.setTopicId(topicId);
						result.setTopicName(topic.getName());
						result.setTopicStatus(topic.getStatus());
						result.setLastingType(topic.getLastingType());
						result.setStartTime(topic.getStartTime());
						result.setEndTime(topic.getEndTime());
						result.setTopicType(topic.getType());
						result.setStockLocationId(pitemDO.getStockLocationId());
						result.setStockLocationName(pitemDO.getStockLocation());
						result.setPurchaseMethod(pitemDO.getPurchaseMethod());
						result.setItemTags(pitemDO.getItemTags());
						//活动是否预占库存：0否1是(5.4)
						result.setTopicInventoryFlag(topic.getReserveInventoryFlag());
						
						resultList.add(result);
					}
				}
				return resultList;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	@Deprecated
	public ResultInfo reduceStock(List<ItemQuery> itemQueries) {
		try {
			if (itemQueries != null && itemQueries.size() > 0) {
				for (ItemQuery item : itemQueries) {
					long topicId = item.getTopicId();
					int amount = item.getAmount();
					String sku = item.getSku();
					// Topic topic = topicDAO.selectById(topicId);
					TopicItem topicItemT = new TopicItem();
					topicItemT.setSku(sku);
					topicItemT.setTopicId(topicId);
					List<TopicItem> topicItemList = topicItemDao.queryByObject(topicItemT);
					if (!topicItemList.isEmpty() && topicItemList.size() > 0) {
						TopicItem topicItem = topicItemList.get(0);
						int limitAmount = topicItem.getLimitAmount();
						int stockAmount = topicItem.getStockAmount();
						// 先判断限购数量，后判断总库存
						if (limitAmount >= amount) {
							if (stockAmount >= amount) {
								topicItem.setStockAmount(topicItem.getStockAmount() - amount);// 扣除库存
								if (topicItem.getSaledAmount() == null)
									topicItem.setSaledAmount(0);
								topicItem.setSaledAmount(topicItem.getSaledAmount() + amount);// 增加销售数量

								topicItemDao.updateNotNullById(topicItem);
							} else {
								String info = "sku:" + sku + ";";
								return new ResultInfo(new FailInfo(info, ResultMessage.FAIL));
							}
						} else {
							String info = "sku:" + sku + ";";
							return new ResultInfo(new FailInfo(info, ResultMessage.FAIL));
						}
					}
				}
				return new ResultInfo();
			} else
				throw new Exception(ProcessingErrorMessage.QUERY_FAILD);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultInfo(new FailInfo(e.getMessage(), ResultMessage.FAIL));
		}
	}

	@Override
	@Deprecated
	public ResultInfo addStock(List<ItemQuery> itemQueries) {
		try {
			if (itemQueries != null && itemQueries.size() > 0) {
				for (ItemQuery item : itemQueries) {
					long topicId = item.getTopicId();
					int amount = item.getAmount();
					String sku = item.getSku();
					TopicItem topicItemT = new TopicItem();
					topicItemT.setSku(sku);
					topicItemT.setTopicId(topicId);
					List<TopicItem> topicItemList = topicItemDao.queryByObject(topicItemT);
					if (!topicItemList.isEmpty() && topicItemList.size() > 0) {
						TopicItem topicItem = topicItemList.get(0);
						int limitAmount = topicItem.getLimitAmount();
						int stockAmount = topicItem.getStockAmount();
						int limitTotal = topicItem.getLimitTotal();// 本专场的总库存
						// 先判断限购数量，后判断总库存
						if (limitAmount >= amount) {
							if (limitTotal >= (stockAmount + amount)) {
								topicItem.setStockAmount(topicItem.getStockAmount() + amount);// 增加爱库存
								if (topicItem.getSaledAmount() == null)
									topicItem.setSaledAmount(0);
								else
									topicItem.setSaledAmount(topicItem.getSaledAmount() - amount);// 增加销售数量
								topicItemDao.updateNotNullById(topicItem);
							} else {
								String info = "sku:" + sku + ";";
								return new ResultInfo(new FailInfo(info, ResultMessage.FAIL));
							}
						} else {
							String info = "sku:" + sku + ";";
							return new ResultInfo(new FailInfo(info, ResultMessage.FAIL));
						}
					}
				}
				return new ResultInfo();
			} else
				throw new Exception(ProcessingErrorMessage.QUERY_FAILD);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultInfo(new FailInfo(e.getMessage(), ResultMessage.FAIL));
		}
	}

	@Override
	@Deprecated
	public ResultInfo checkStock(List<ItemQuery> itemQueries) {
		try {
			if (itemQueries != null && itemQueries.size() > 0) {
				for (ItemQuery item : itemQueries) {
					long topicId = item.getTopicId();
					int amount = item.getAmount();
					String sku = item.getSku();
					TopicItem TopicItem = new TopicItem();
					TopicItem.setSku(sku);
					TopicItem.setTopicId(topicId);
					List<TopicItem> topicItemList = topicItemDao.queryByObject(TopicItem);
					if (!topicItemList.isEmpty() && topicItemList.size() > 0) {
						TopicItem topicItem = topicItemList.get(0);
						int limitAmount = topicItem.getLimitAmount();
						int stockAmount = topicItem.getStockAmount();
						// 先判断限购数量，后判断总库存
						if (limitAmount >= amount) {
							if (stockAmount >= amount) {
								continue;
							} else {
								String info = "sku:" + sku + ";";
								return new ResultInfo(new FailInfo(info, ResultMessage.FAIL));
							}
						} else {
							String info = "sku:" + sku + ";";
							return new ResultInfo(new FailInfo(info, ResultMessage.FAIL));
						}
					}
				}
				return new ResultInfo();
			} else
				throw new Exception(ProcessingErrorMessage.QUERY_FAILD);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultInfo(new FailInfo(e.getMessage(), ResultMessage.FAIL));
		}

	}

	@Override
	public CartDTO cartValidate(CartDTO cartDTO) {
		try {
			if (cartDTO != null) {
				BigDecimal totalPrice = BigDecimal.ZERO;
				BigDecimal orignalTotalPrice = BigDecimal.ZERO;
				BigDecimal allTotalPrice = BigDecimal.ZERO;
				BigDecimal taxSum = BigDecimal.ZERO;
				int totaluantity = 0;
				int allTotalQuantity = 0;
				List<String> validSkus = cartDTO.getValidateSkuList();
				Integer cartType = cartDTO.getCartType();

				if (cartType == 1) {
					if (logger.isDebugEnabled()) {
						logger.info("[TopicService.cartValidate]普通购物车 normal cartdto");
					}
					List<CartLineDTO> lineList = cartDTO.getLineList();
					if (lineList != null && lineList.size() > 0) {
						for (CartLineDTO cartLine : lineList) {
							String sku = cartLine.getSkuCode();
							long topicId = cartLine.getTopicId();
							int quantity = cartLine.getQuantity();
							if (logger.isDebugEnabled()) {
								logger.info("[TopicService.cartValidate]topic id in cart:" + topicId);
							}
							Topic topic = topicDao.queryById(topicId);
							if (null == topic) {
								logger.error("[TopicService.cartValidate]topic info is invalid....");
								throw new ServiceException("无 法获取活动信息");
							}
							String areaStr = topic.getAreaStr();
							TopicItem promotionItemDO = new TopicItem();
							promotionItemDO.setSku(sku);
							promotionItemDO.setTopicId(topicId);
							promotionItemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
							// TODO: 加上地区信息
							List<TopicItem> pitemList = topicItemDao.queryByObject(promotionItemDO);
							// TODO:如果没有活动商品信息 反馈异常
							if (pitemList != null && pitemList.size() > 0) {
								TopicItem pitemDO = pitemList.get(0);
								TopicItemInfoDTO result = new TopicItemInfoDTO();
								result.setStockAmount(pitemDO.getStockAmount());
								result.setLimitAmount(pitemDO.getLimitAmount());
								result.setTopicPrice(pitemDO.getTopicPrice());
								result.setTopicImage(pitemDO.getTopicImage());
								if (pitemDO.getLockStatus() != null && pitemDO.getLockStatus() == 1)
									result.setLocked(true);// 商品已经被锁定
								else
									result.setLocked(false);
								result.setTopicName(topic.getName());
								result.setTopicId(topicId);
								result.setTopicStatus(topic.getStatus());// 专场专场
								result.setLastingType(topic.getLastingType());// 专场持续类型
								result.setStartTime(topic.getStartTime());
								result.setEndTime(topic.getEndTime());
								// result.setAreaStr(areaStr);
								// result.setPlatformStr(topic.getPlatformStr());
								Date now = new Date();
								if (topic.getStatus() == TopicStatus.PASSED.ordinal() && now.after(topic.getStartTime())
										&& now.before(topic.getEndTime()))
									result.setValidate(true);
								else
									result.setValidate(false);
								//设置活动是否预占库存(5.4)
								result.setTopicInventoryFlag(topic.getReserveInventoryFlag());
								cartLine.setTopicItemInfo(result);
								cartLine.setWarehouseId(pitemDO.getStockLocationId());
								cartLine.setSalesPattern(topic.getSalesPartten());
								if (cartLine.getSelected() && validSkus.contains(sku) && result.getValidate()) {
									Double topicPrice = pitemDO.getTopicPrice();
									totalPrice = totalPrice.add(
											new BigDecimal(topicPrice.toString()).multiply(new BigDecimal(quantity)));

									Double orignalPrice = cartLine.getListPrice();
									orignalTotalPrice = orignalTotalPrice.add(
											new BigDecimal(orignalPrice.toString()).multiply(new BigDecimal(quantity)));
									totaluantity += quantity;
								}
								allTotalPrice = allTotalPrice.add(
										new BigDecimal(pitemDO.getTopicPrice()).multiply(new BigDecimal(quantity)));
								allTotalQuantity += quantity;
							}
						} // end for
						cartDTO.setRealSum(totalPrice.doubleValue());
						cartDTO.setOriginalSum(orignalTotalPrice.doubleValue());
						cartDTO.setTopicRealSum(totalPrice.doubleValue());
						cartDTO.setRealSumAll(allTotalPrice.doubleValue());
						cartDTO.setQuantity(totaluantity);
						cartDTO.setQuantityAll(allTotalQuantity);
					} else
						throw new Exception(ProcessingErrorMessage.CART_EMPTY);
				} else if (cartType == 2) { // 海淘购物车
					if (logger.isDebugEnabled()) {
						logger.info("[TopicService.cartValidate]海淘购物车 hitao cartdto");
					}
					Map<String, List<CartLineDTO>> seaMap = cartDTO.getSeaMap();
					Set<String> keySet = seaMap.keySet();
					for (String key : keySet) {
						List<CartLineDTO> cartLineList = seaMap.get(key);
						BigDecimal keyTotalTax = BigDecimal.ZERO;
						if (cartLineList != null && cartLineList.size() > 0) {
							for (CartLineDTO cartLine : cartLineList) {
								String sku = cartLine.getSkuCode();
								long topicId = cartLine.getTopicId();
								int quantity = cartLine.getQuantity();
								Double tarrifRate = cartLine.getTarrifRate();// 税率
								BigDecimal taxfFee = BigDecimal.ZERO;
								if (logger.isDebugEnabled()) {
									logger.info("[TopicService.cartValidate]topic id in cart:" + topicId);
								}
								Topic topic = topicDao.queryById(topicId);
								if (null == topic) {
									logger.error("[TopicService.cartValidate]topic info is invalid....");
									throw new ServiceException("无 法获取活动信息");
								}

								TopicItem promotionItemDO = new TopicItem();
								promotionItemDO.setSku(sku);
								promotionItemDO.setTopicId(topicId);
								promotionItemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
								List<TopicItem> pitemList = topicItemDao.queryByObject(promotionItemDO);
								if (pitemList != null && pitemList.size() > 0) {
									TopicItem pitemDO = pitemList.get(0);
									TopicItemInfoDTO result = new TopicItemInfoDTO();
									result.setStockAmount(pitemDO.getStockAmount());
									result.setLimitAmount(pitemDO.getLimitAmount());
									result.setTopicPrice(pitemDO.getTopicPrice());
									result.setTopicImage(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item,
											pitemDO.getTopicImage()));
									result.setTopicName(topic.getName());
									result.setTopicId(topicId);
									result.setTopicStatus(topic.getStatus());// 专场专场
									result.setLastingType(topic.getLastingType());// 专场持续类型
									result.setStartTime(topic.getStartTime());
									result.setEndTime(topic.getEndTime());
									if (pitemDO.getLockStatus() != null && pitemDO.getLockStatus() == 1)
										result.setLocked(true);// 商品已经被锁定
									else
										result.setLocked(false);
									Date now = new Date();
									if (topic.getStatus() == TopicStatus.PASSED.ordinal()
											&& now.after(topic.getStartTime()) && now.before(topic.getEndTime()))
										result.setValidate(true);
									else
										result.setValidate(false);
									cartLine.setTopicItemInfo(result);
									cartLine.setSalesPattern(topic.getSalesPartten());
									cartLine.setWarehouseId(pitemDO.getStockLocationId());
									cartLine.setWarehouseName(pitemDO.getStockLocation());
									if (cartLine.getSelected() && validSkus.contains(sku) && result.getValidate()) {
										Double topicPrice = pitemDO.getTopicPrice();
										totalPrice = totalPrice.add(new BigDecimal(topicPrice.toString())
												.multiply(new BigDecimal(quantity)));

										Double orignalPrice = cartLine.getListPrice();
										orignalTotalPrice = orignalTotalPrice
												.add(new BigDecimal(orignalPrice.toString())
														.multiply(new BigDecimal(quantity)));
										totaluantity += quantity;

										taxfFee = new BigDecimal(topicPrice.toString())
												.multiply(new BigDecimal(quantity))
												.multiply(new BigDecimal(tarrifRate.toString()));
										taxfFee = taxfFee.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
										if (logger.isDebugEnabled()) {
											logger.info("[TopicService.cartValidate]haitao tax fee sku:"
													+ pitemDO.getSku() + "..fee:" + taxfFee);
										}
										keyTotalTax = keyTotalTax.add(taxfFee);// 单个供应商的税费
										cartLine.setTaxfFee(taxfFee.doubleValue());// 单行的税费
									}
									allTotalPrice = allTotalPrice.add(
											new BigDecimal(pitemDO.getTopicPrice()).multiply(new BigDecimal(quantity)));
									allTotalQuantity += quantity;
								}

							} // end for cartLieList
							if (keyTotalTax.doubleValue() <= 50) {// 单个供应商的税费总不满50
								// ，税费置为 0
								for (CartLineDTO cartLine : cartLineList) {
									cartLine.setTaxfFee(0d);
								}
								keyTotalTax = BigDecimal.ZERO;
							} else {
								// 总价加上税费
								taxSum = taxSum.add(keyTotalTax);
							}
						} else {
							// cartLineList 为空， 暂时不处理
						}
					}
					cartDTO.setRealSum(totalPrice.doubleValue());
					cartDTO.setOriginalSum(orignalTotalPrice.doubleValue());
					cartDTO.setTopicRealSum(totalPrice.doubleValue());
					cartDTO.setRealSumAll(allTotalPrice.doubleValue());
					cartDTO.setQuantity(totaluantity);
					cartDTO.setTaxSumAll(taxSum.doubleValue());
					if (logger.isDebugEnabled()) {
						logger.info("[TopicService.cartValidate]haitao total tax fee :" + taxSum.doubleValue());
					}
					cartDTO.setQuantityAll(allTotalQuantity);
				} else {
					logger.error("[TopicService.cartValidate]cart type is invalid....");
					throw new Exception(ProcessingErrorMessage.CART_TYPE_ERROR);
				}
			}
			return cartDTO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return cartDTO;
		}

	}

	@Override
	public ResultInfo<Boolean> validateSingleTopicItem(TopicItemCartQuery query) {
		if (logger.isDebugEnabled()) {
			logger.info("[TopicService.validateSingleTopicItem]single topic item in validate");
		}
		try {
			if (query != null) {
				long topicId = query.getTopicId();
				int amount = query.getAmount();
				String sku = query.getSku();
				long areaId = query.getArea();
				int platformId = query.getPlatform();
				Long memberId = query.getMemberId();
				if (logger.isDebugEnabled()) {
					logger.info("[TopicService.validateSingleTopicItem]topicId: +" + topicId);
					logger.info("[TopicService.validateSingleTopicItem]amount: +" + amount);
					logger.info("[TopicService.validateSingleTopicItem]sku: +" + sku);
					logger.info("[TopicService.validateSingleTopicItem]areaId: +" + areaId);
					logger.info("[TopicService.validateSingleTopicItem]platformId: +" + platformId);
				}
				Topic topic = topicDao.queryById(topicId);

				if (topic.getStatus() != TopicStatus.PASSED.ordinal()
						|| topic.getProgress() != ProgressStatus.DOING.ordinal()) {
					String info = "活动已经结束";
					logger.error("[TopicService.validateSingleTopicItem]topic is terminate");
					return new ResultInfo<Boolean>(new FailInfo(info, ErrorCodeType.OVERDUE.ordinal()));
				}
				TopicItem TopicItem = new TopicItem();
				TopicItem.setSku(sku);
				TopicItem.setTopicId(topicId);
				TopicItem.setDeletion(DeletionStatus.NORMAL.ordinal());
				List<TopicItem> topicItemList = topicItemDao.queryByObject(TopicItem);
				if (!topicItemList.isEmpty() && topicItemList.size() > 0) {
					TopicItem topicItem = topicItemList.get(0);
					if (topicItem.getLockStatus() != null && topicItem.getLockStatus() == 1) {
						logger.error("[TopicService.validateSingleTopicItem]topic item is locaked:" + areaId);
						String info = "商品已经被锁定！";
						return new ResultInfo<Boolean>(new FailInfo(info, ErrorCodeType.LOCKED.ordinal()));
					}

					int limitAmount = topicItem.getLimitAmount();
					if (logger.isDebugEnabled()) {
						logger.info("[TopicService.validateSingleTopicItem]limitAmount: +" + limitAmount);
					}
					// 先判断限购数量，后判断总库存
					if (limitAmount >= amount) {
						// 平台、地区、限购政策
						String areaStr = topic.getAreaStr();
						if (areaStr != null && !"".equals(areaStr)
								&& !areaStr.contains(String.valueOf(AreaConstant.AREA_ALL))) {
							String[] arr = areaStr.split(",");
							List<String> areaList = Arrays.asList(arr);
							if (!areaList.contains(areaId + "")) {
								logger.error(
										"[TopicService.validateSingleTopicItem]topic is not current area:" + areaId);
								String info = "专场不适用当前地区！";
								return new ResultInfo<Boolean>(new FailInfo(info, ErrorCodeType.AREA.ordinal()));
							}
						}

						String platformStr = topic.getPlatformStr();
						if (null != platformStr && !"".equals(platformStr)
								&& !platformStr.contains(String.valueOf(PlatformEnum.ALL.getCode()))) {
							String[] arr = platformStr.split(",");
							List<String> pList = Arrays.asList(arr);
							if (!pList.contains(platformId + "")) {
								String info = "专场不适用当前平台！";
								logger.error("[TopicService.validateSingleTopicItem]topic is not current platform:"
										+ platformId);
								return new ResultInfo<Boolean>(new FailInfo(info, ErrorCodeType.PLATFORM.ordinal()));
							}
						}

						// 限购政策
						Long pid = topic.getLimitPolicyId();
						if (pid != null && pid != 0) {
							PolicyInfo PolicyInfo = policyInfoDao.queryById(pid);
							if (PolicyInfo != null) {
								Integer byRregister = PolicyInfo.getByRegisterTime();
								Integer byTopic = PolicyInfo.getByTopic();

								// 如果有注册时间限制
								if (byRregister != null && byRregister == 1) {
									Date earlyThanTime = PolicyInfo.getEarlyThanTime();
									Date lateThanTime = PolicyInfo.getLateThanTime();
									MemberInfo userDO = memberInfoService.queryById(memberId);
									Date registerTime = userDO.getCreateTime();
									if (registerTime != null) {
										if (earlyThanTime != null) {
											if (registerTime.after(earlyThanTime)) {
												logger.error("user register time is invalid");
												String info = "注册时间不符合限购要求";
												return new ResultInfo<Boolean>(
														new FailInfo(info, ErrorCodeType.REGISTER_TIME.ordinal()));
											}
										}
										if (lateThanTime != null) {
											if (registerTime.before(lateThanTime)) {
												String info = "注册时间不符合限购要求";
												logger.error("user register time is invalid");
												return new ResultInfo<Boolean>(
														new FailInfo(info, ErrorCodeType.REGISTER_TIME.ordinal()));
											}
										}
									}
								}
								// 如果有活动购买次数限制
								if (byTopic != null && byTopic == 1) {
									Map<String, Object> params = new HashMap<>();
									params.put("topicId", topic.getId());
									params.put("memberId", query.getMemberId());
									List<OrderItem> itemList = orderItemService.queryByParamNotEmpty(params);
									Set<Long> subOrderIds = new HashSet<>();
									for (OrderItem item : itemList) {
										subOrderIds.add(item.getOrderId());
									}
									List<SubOrder> subOrderList = subOrderService
											.selectListByIdList(new ArrayList<>(subOrderIds));
									for (SubOrder subOrder : subOrderList) {
										if (!OrderConstant.ORDER_STATUS.CANCEL.code.equals(subOrder.getOrderStatus())) {
											logger.error("活动限购只买一次, 已购子订单:{}", subOrder.getOrderCode());
											return new ResultInfo<Boolean>(
													new FailInfo("专场限购每个用户只能买一次", ErrorCodeType.TOPIC.ordinal()));
										}
									}
								}
							}
						}
						return new ResultInfo<Boolean>(Boolean.TRUE);
					} else {
						String info = "sku:" + sku + ";";
						logger.error(info);
						return new ResultInfo<Boolean>(new FailInfo(info + ErrorCodeType.LIMIT_AMOUNT.name(),
								ErrorCodeType.LIMIT_AMOUNT.ordinal()));

					}
				} else {
					String info = "找不到专场对应的商品！sku:" + sku;
					logger.error(String.format("not found sku item,sku:%s", sku));
					return new ResultInfo<Boolean>(new FailInfo(info, ErrorCodeType.OTHER.ordinal()));
				}
			} else {
				logger.error("query condition error");
				return new ResultInfo<Boolean>(new FailInfo("查询条件异常！", ErrorCodeType.GET_TOPIC_BY_ID.ordinal()));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultInfo<Boolean>(new FailInfo(e.getMessage(), ErrorCodeType.OTHER.ordinal()));
		}
	}

	@Override
	public ResultInfo<Boolean> validateTopicItemList(List<TopicItemCartQuery> querys) {
		try {
			if (querys != null && querys.size() > 0) {
				// 保存各专场的topicItem个数 验证同一专场的商品不能被购买多样。
				Map<Long, Integer> topicItemNum = new HashMap<>();
				for (TopicItemCartQuery query : querys) {
					if (query != null) {
						long topicId = query.getTopicId();
						int amount = query.getAmount();
						String sku = query.getSku();
						long areaId = query.getArea();
						int platformId = query.getPlatform();
						long memberId = query.getMemberId();
						// TODO:入参校验
						Topic topic = topicDao.queryById(topicId);

						if (topic.getStatus() != TopicStatus.PASSED.ordinal()
								|| topic.getProgress() != ProgressStatus.DOING.ordinal()) {
							String info = "专场已经结束";
							return new ResultInfo<Boolean>(new FailInfo(info, ErrorCodeType.OVERDUE.ordinal()));
						}
						TopicItem TopicItem = new TopicItem();
						TopicItem.setSku(sku);
						TopicItem.setTopicId(topicId);
						TopicItem.setDeletion(Constant.DISABLED.NO);
						List<TopicItem> topicItemList = topicItemDao.queryByObject(TopicItem);
						if (!topicItemList.isEmpty() && topicItemList.size() > 0) {
							topicItemNum.put(topicId,
									(topicItemNum.get(topicId) == null ? 0 : topicItemNum.get(topicId)) + 1);

							TopicItem topicItem = topicItemList.get(0);
							if (topicItem.getLockStatus() != null && topicItem.getLockStatus() == 1) {
								logger.error("[TopicService.validateSingleTopicItem]topic item is locaked:" + areaId);
								String info = "商品已经被锁定！";
								return new ResultInfo<Boolean>(new FailInfo(info, ErrorCodeType.LOCKED.ordinal()));
							}
							int limitAmount = topicItem.getLimitAmount();
							// 先判断限购数量，后判断总库存
							if (limitAmount >= amount) {
								// 平台、地区、限购政策
								String areaStr = topic.getAreaStr();
								// 如果是所有地区，则不校验
								if (areaStr != null && !"".equals(areaStr)
										&& !areaStr.contains(String.valueOf(AreaConstant.AREA_ALL))) {
									String[] arr = areaStr.split(",");
									List<String> areaList = Arrays.asList(arr);
									if (!areaList.contains(areaId + "")) {
										String info = "【" + topic.getId() + "】专场不适用当前地区";
										return new ResultInfo<Boolean>(
												new FailInfo(info, ErrorCodeType.AREA.ordinal()));
									}
								}

								String platformStr = topic.getPlatformStr();
								// 如果是所有平台，则不校验
								if (null != platformStr && !"".equals(platformStr)
										&& !platformStr.contains(String.valueOf(PlatformEnum.ALL.getCode()))) {
									String[] arr = platformStr.split(",");
									List<String> pList = Arrays.asList(arr);
									if (!pList.contains(platformId + "")) {
										String info = "【" + topic.getId() + "】专场不适用当前平台";
										return new ResultInfo<Boolean>(
												new FailInfo(info, ErrorCodeType.PLATFORM.ordinal()));
									}
								}

								// 限购政策
								Long pid = topic.getLimitPolicyId();
								if (pid != null && pid != 0) {
									PolicyInfo PolicyInfo = policyInfoDao.queryById(pid);
									if (PolicyInfo != null) {
										Integer byRregister = PolicyInfo.getByRegisterTime();
										Integer byTopic = PolicyInfo.getByTopic();
										// 如果有注册时间限制
										if (byRregister != null && byRregister == 1) {
											Date earlyThanTime = PolicyInfo.getEarlyThanTime();
											Date lateThanTime = PolicyInfo.getLateThanTime();
											MemberInfo userDO = memberInfoService.queryById(memberId);
											Date registerTime = userDO.getCreateTime();
											if (registerTime != null) {
												if (earlyThanTime != null) {
													if (registerTime.after(earlyThanTime)) {
														String info = "【" + topic.getId() + "】专场限制注册时间，不符合限购要求";
														return new ResultInfo<Boolean>(new FailInfo(info,
																ErrorCodeType.REGISTER_TIME.ordinal()));
													}
												}
												if (lateThanTime != null) {
													if (registerTime.before(lateThanTime)) {
														String info = "【" + topic.getId() + "】专场限制注册时间，不符合限购要求";
														return new ResultInfo<Boolean>(new FailInfo(info,
																ErrorCodeType.REGISTER_TIME.ordinal()));
													}
												}
											}
										}
										// 如果有活动购买次数限制
										if (byTopic != null && byTopic == 1) {
											if (topicItemNum.get(topic.getId()) > 1) {
												return new ResultInfo<Boolean>(new FailInfo("专场限购每个用户只能购买一种商品",
														ErrorCodeType.TOPIC.ordinal()));
											}

											Map<String, Object> params = new HashMap<>();
											params.put("topicId", topic.getId());
											params.put("memberId", query.getMemberId());
											List<OrderItem> itemList = orderItemService.queryByParamNotEmpty(params);
											Set<Long> subOrderIds = new HashSet<>();
											for (OrderItem item : itemList) {
												subOrderIds.add(item.getOrderId());
											}
											List<SubOrder> subOrderList = subOrderService
													.selectListByIdList(new ArrayList<>(subOrderIds));
											for (SubOrder subOrder : subOrderList) {
												if (!OrderConstant.ORDER_STATUS.CANCEL.code
														.equals(subOrder.getOrderStatus())) {
													logger.error("活动限购只买一次, 已购子订单:{}", subOrder.getOrderCode());
													return new ResultInfo<Boolean>(new FailInfo("专场限购每个用户只能购买一种商品",
															ErrorCodeType.TOPIC.ordinal()));
												}
											}
										}
									}
								}
								continue;
							} else {
								String info = "【" + sku + "】商品库存不足";
								return new ResultInfo<Boolean>(
										new FailInfo(info, ErrorCodeType.LIMIT_AMOUNT.ordinal()));
							}
						} else {
							String info = "专场找不到对应商品【" + sku + "】";
							return new ResultInfo<Boolean>(new FailInfo(info, ErrorCodeType.OTHER.ordinal()));

						}
					} else
						throw new Exception(ProcessingErrorMessage.QUERY_FAILD);
				} // end for
				return new ResultInfo<Boolean>(Boolean.TRUE);
			} else
				throw new Exception(ProcessingErrorMessage.QUERY_FAILD);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultInfo<Boolean>(new FailInfo(e.getMessage(), ErrorCodeType.OTHER.ordinal()));
		}
	}

	@Override
	public List<TopicInfo> queryTopicList(List<Long> topicIdList) {
		if (!topicIdList.isEmpty() && topicIdList.size() > 0) {
			List<Topic> topicList = topicDao.queryTopicInfoList(topicIdList);
			List<TopicInfo> infoList = new ArrayList<TopicInfo>();
			for (Topic Topic : topicList) {
				TopicInfo info = new TopicInfo();
				info.setEndTime(Topic.getEndTime());
				info.setImage(Topic.getImage());
				info.setLastingType(Topic.getLastingType());
				info.setName(Topic.getName());
				info.setStartTime(Topic.getStartTime());
				info.setStatus(Topic.getStatus());
				info.setTopicId(Topic.getId());
				info.setImageNew(Topic.getImageNew());
				info.setTopicPoint(Topic.getTopicPoint());
				info.setImageInterested(Topic.getImageInterested());
				if (TopicStatus.PASSED.ordinal() == Topic.getStatus()) {
					if (new Date().before(Topic.getStartTime())) {// 未开始
						info.setProgress(ProgressStatus.NotStarted.ordinal());
					} else if (new Date().after(Topic.getEndTime())) {// 已结束
						info.setProgress(ProgressStatus.FINISHED.ordinal());
					} else {// 进行中
						info.setProgress(ProgressStatus.DOING.ordinal());
					}
				} else {
					info.setProgress(ProgressStatus.FINISHED.ordinal());
				}
				// 新增加图片
				info.setMobileImage(Topic.getMobileImage());
				info.setMallImage(Topic.getMallImage());
				info.setPcImage(Topic.getPcImage());
				info.setPcInterestImage(Topic.getPcInterestImage());
				info.setHaitaoImage(Topic.getHaitaoImage());
				infoList.add(info);
			}
			return infoList;
		} else
			return null;
	}

	@Override
	public List<Topic> selectDynamicPageQueryWithLike(TopicQueryDTO Topic) {

		try {
			return topicDao.selectDynamicPageQueryWithLike(Topic);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public Long getTopicsWithoutSpecialIdCount(Long specialTopicId, String number, String name) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", name);
		paramMap.put("number", number);
		paramMap.put("id", specialTopicId);
		paramMap.put("status", TopicConstant.TOPIC_STATUS_AUDITED);
		paramMap.put("deletion", DeletionStatus.NORMAL.ordinal());
		return topicDao.getTopicsWithoutSpecialIdCount(paramMap);
	}

	@Override
	public PageInfo<Topic> getTopicsWithoutSpecialId(Long specialTopicId, String number, String name, Integer startPage,
			Integer pageSize) {
		// TODO:按查询条件做数据缓存
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", name);
		paramMap.put("number", number);
		paramMap.put("id", String.valueOf(specialTopicId));
		paramMap.put("start", (startPage > 1 ? (startPage - 1) * pageSize : 0));
		paramMap.put("pageSize", pageSize);
		paramMap.put("status", TopicConstant.TOPIC_STATUS_AUDITED);
		paramMap.put("deletion", DeletionStatus.NORMAL.ordinal());
		Long totalCount = this.getTopicsWithoutSpecialIdCount(specialTopicId, number, name);
		List<Topic> resultList = this.topicDao.getTopicsWithoutSpecialId(paramMap);

		PageInfo<Topic> page = new PageInfo<Topic>();
		page.setPage(startPage);
		page.setSize(pageSize);
		page.setRecords(totalCount.intValue());
		page.setRows(resultList);
		return page;
	}

	@Override
	public Long selectCountDynamicWithLike(TopicQueryDTO topicDO) throws ServiceException {
		try {

			return topicDao.selectCountWithLike(topicDO);
		} catch (Exception e) {
			logger.error("e", e);
			throw new ServiceException(e);
		}
	}

	public PageInfo<Topic> queryPageListByTopicDOWithLike(TopicQueryDTO Topic) {
		if (Topic != null) {
			Long totalCount = this.selectCountDynamicWithLike(Topic);
			List<Topic> resultList = selectDynamicPageQueryWithLike(Topic);

			PageInfo<Topic> page = new PageInfo<Topic>();
			page.setPage(Topic.getStartPage());
			page.setSize(Topic.getPageSize());
			page.setRecords(totalCount.intValue());
			page.setRows(resultList);
			return page;
		}
		return new PageInfo<>();
	}

	@Override
	public PageInfo<Topic> queryPageListByTopicDOAndStartPageSizeWithLike(TopicQueryDTO Topic, int startPage, int pageSize) {
		if (Topic != null && startPage > 0 && pageSize > 0) {
			Topic.setStartPage(startPage);
			Topic.setPageSize(pageSize);
			return this.queryPageListByTopicDOWithLike(Topic);
		}
		return new PageInfo<>();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo saveTopicInfo(Topic topic, PolicyInfo policy, List<TopicItem> topicItems,
			List<TopicItem> removeTopicItems, Long[] relateTopicIds, Long[] removeRelateTopicIds) throws Exception {

		// TODO：保存时校验库存
		if (null == topic) {
			return new ResultInfo(new FailInfo(ProcessingErrorMessage.VALID_TOPIC_INFO, ResultMessage.FAIL));
		}
		Long topicId = topic.getId();

		// 提交策略(拆分跟新和新增)
		Long policyId = 0L;
		if (null != policy) {
			if (null == policy.getId()) {
				policy.setCreateTime(new Date());
				policyInfoDao.insert(policy);
				Long policyIdValue = policy.getId();
				if (null != policyIdValue) {
					policyId = policyIdValue;
				}
			} else {

				// TODO:可拆分是否有变更
				policyId = policy.getId();
				policy.setUpdateTime(new Date());
				this.policyInfoDao.updateNotNullById(policy);
			}
		}

		// 保存专场活动
		topic.setLimitPolicyId(policyId);
		topic.setUpdateTime(new Date());
		topicDao.updateNotNullById(topic);

		// 保存活动商品
		if (null != topicItems && topicItems.size() > 0) {
			for (TopicItem topicItem : topicItems) {
				if (null == topicItem.getId() || 0 == topicItem.getId()) {
					// 新增
					topicItem.setCreateTime(new Date());
					// TODO: 用户Id等权限系统完成
					topicItem.setCreateUser("");
					topicItem.setDeletion(DeletionStatus.NORMAL.ordinal());
					topicItemDao.insert(topicItem);
				} else {
					// 修改
					topicItem.setUpdateTime(new Date());
					topicItemDao.updateNotNullById(topicItem);
				}
			}
		}

		// 保存relates
		if (null != relateTopicIds && relateTopicIds.length > 0) {
			for (Long rTopicId : relateTopicIds) {
				Relate Relate = new Relate();
				Relate.setFirstTopicId(topicId);
				Relate.setSecondTopicId(rTopicId);
				List<Relate> relates = relateDAO.queryByObject(Relate);
				if (null != relates && relates.size() > 0) {
					throw new ServiceException(String.format(ProcessingErrorMessage.EXIST_RELATE_TOPIC, rTopicId));
				}
				Relate.setCreateTime(new Date());
				Relate.setDeletion(DeletionStatus.NORMAL.ordinal());
				relateDAO.insert(Relate);
			}
		}

		// 标记删除 item
		if (null != removeTopicItems && removeTopicItems.size() > 0) {
			for (TopicItem dTopicItem : removeTopicItems) {
				dTopicItem.setUpdateTime(new Date());
				dTopicItem.setDeletion(DeletionStatus.DELETED.ordinal());
				topicItemDao.updateNotNullById(dTopicItem);
			}
		}

		// 标记删除 relates
		if (null != removeRelateTopicIds && removeRelateTopicIds.length > 0) {
			for (Long rRelateId : removeRelateTopicIds) {
				Relate Relate = new Relate();
				Relate.setId(rRelateId);
				Relate.setDeletion(DeletionStatus.DELETED.ordinal());
				relateDAO.updateNotNullById(Relate);
			}
		}

		// 新增执行动作记录
		if (TopicConstant.TOPIC_STATUS_AUDITING == topic.getStatus()) {
			// 新增执行动作记录
			TopicAuditLog auditLog = new TopicAuditLog();
			auditLog.setTopicId(topic.getId());
			auditLog.setAuditName(topic.getCreateUser());
			auditLog.setAuditId(0L);
			// TODO:等待权限系统，获取用户名
			// auditLog.setAuditName(userName);
			auditLog.setAuditOperation(TopicAuditLogConstant.STATUS_AUDITING_VALUE);
			topicAuditLogDao.insert(auditLog);
		}
		return new ResultInfo();
	}

	@Override
	public PageInfo<TopicDetailDTO> getCmsTopicList(CmsTopicQuery query) throws Exception {
		List<TopicDetailDTO> resList = new ArrayList<TopicDetailDTO>();
		Long topicId = query.getTopicId();
		String name = query.getName();
		Date startTime = query.getStartTime();
		Date endTime = query.getEndTime();
		Integer platformType = query.getPlatformType();
		Integer areaId = query.getAreaId();
		Integer salesPartten = query.getSalesPartten();
		if ((topicId == null) && name == null && startTime == null && endTime == null)
			return null;
		Integer topicType = query.getTopicType();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("topicId", topicId);
		paramMap.put("name", name);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		paramMap.put("topicType", topicType);
		paramMap.put("salesPartten", salesPartten);
		int pageId = query.getPageId();
		int pageSize = query.getPageSize();
		paramMap.put("start", (pageId > 1 ? (pageId - 1) * pageSize : 0));
		paramMap.put("pageSize", pageSize);
		paramMap.put("pageNo", pageId);
		if (platformType != null && platformType != PlatformEnum.ALL.getCode())// 全部平台为1
			paramMap.put("platformType", platformType);
		if (areaId != null && areaId != AreaConstant.AREA_ALL)// 全部地区为1
			paramMap.put("areaId", areaId);

		List<Topic> doList = topicDao.queryTopicListCms(paramMap);
		if (doList != null && doList.size() > 0) {
			for (Topic Topic : doList) {
				TopicDetailDTO topicDetail = new TopicDetailDTO();
				topicDetail.setTopic(Topic);
				TopicItem promotionItemDO = new TopicItem();
				promotionItemDO.setTopicId(Topic.getId());
				promotionItemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
				List<TopicItem> promotionItemList = topicItemDao.queryByObject(promotionItemDO);
				if (!promotionItemList.isEmpty() && promotionItemList.size() > 0) {
					List<TopicItem> resItemList = new ArrayList<TopicItem>();
					for (TopicItem item : promotionItemList) {
						resItemList.add(item);
					}
					topicDetail.setPromotionItemList(resItemList);
				} else {
					continue;
				}
				resList.add(topicDetail);
			}
		}
		Integer totalCount = topicDao.countTopicListCms(paramMap);
		PageInfo<TopicDetailDTO> page = new PageInfo<TopicDetailDTO>();
		page.setRows(resList);
		page.setRecords(totalCount);
		page.setPage(pageId);
		page.setSize(pageSize);
		return page;
	}

	@Override
	public Integer getMaxTopicInfoSortIndex() {
		return topicDao.getMaxTopicSortIndex();
	}

	@Override
	public List<TopicItemInfoResult> queryItemRelateTopicItem(long topicId, String sku) {
		Set<Long> relateTidList = new HashSet<Long>();
		try {
			Relate relateF = new Relate();
			relateF.setFirstTopicId(topicId);
			List<Relate> listF = relateDAO.queryByObject(relateF);
			if (listF != null && listF.size() > 0) {
				for (Relate rdo : listF) {
					relateTidList.add(rdo.getSecondTopicId());
				}
			}

			Relate relateS = new Relate();
			relateS.setSecondTopicId(topicId);
			List<Relate> listS = relateDAO.queryByObject(relateS);
			if (listS != null && listS.size() > 0) {
				for (Relate rdo : listS) {
					relateTidList.add(rdo.getFirstTopicId());
				}
			}

			if (relateTidList != null && relateTidList.size() > 0) {
				List<TopicItemInfoResult> resultList = new ArrayList<TopicItemInfoResult>();
				List<Long> secondIdList = new ArrayList<Long>();
				for (Long tid : relateTidList) {
					secondIdList.add(tid);
				}
				List<Topic> secondTopicList = topicDao.queryTopicInList(secondIdList);
				for (Topic topic : secondTopicList) {
					long tid = topic.getId();
					if (null != topic && topic.getStatus() == TopicStatus.PASSED.ordinal()) {
						TopicItem promotionItemDO = new TopicItem();
						promotionItemDO.setTopicId(tid);
						promotionItemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
						List<TopicItem> pitemList = topicItemDao.queryByObject(promotionItemDO);
						if (null != pitemList && !pitemList.isEmpty() && pitemList.size() > 0) {
							List<TopicItem> resItemList = new ArrayList<TopicItem>();
							for (TopicItem item : pitemList) {
								resItemList.add(item);
							}
							if (resItemList.size() > 0) {
								TopicItem pitemDO = pitemList.get(0);
								TopicItemInfoResult result = new TopicItemInfoResult();
								result.setSku(pitemDO.getSku());
								result.setItemId(pitemDO.getItemId());
								result.setStockAmount(pitemDO.getStockAmount());
								result.setLimitAmount(pitemDO.getLimitAmount());
								result.setTopicPrice(pitemDO.getTopicPrice());
								result.setTopicImage(pitemDO.getTopicImage());
								result.setItemName(pitemDO.getName());
								if (pitemDO.getLockStatus() != null) {
									result.setLockStatus(pitemDO.getLockStatus());
								} else {
									result.setLockStatus(LockStatus.UNLOCK.ordinal());
								}
								result.setTopicId(tid);
								result.setTopicName(topic.getName());
								result.setTopicStatus(topic.getStatus());
								result.setLastingType(topic.getLastingType());
								result.setStartTime(topic.getStartTime());
								result.setEndTime(topic.getEndTime());
								result.setTopicType(topic.getType());
								result.setStockLocationId(pitemDO.getStockLocationId());
								result.setStockLocationName(pitemDO.getStockLocation());
								result.setPurchaseMethod(pitemDO.getPurchaseMethod());
								resultList.add(result);
							}
							if (resultList.size() == 6)
								break;
						}
					}
				}
				return resultList;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Collections.emptyList();
		}
		return Collections.emptyList();
	}

	@Override
	public PageInfo<TopicDetailDTO> getTomorrowForecast(CmsTopicSimpleQuery query) throws Exception {
		String date = query.getData();
		Integer pageId = query.getPageId();
		Integer pageSize = query.getPageSize();
		Integer platformType = query.getPlatformType();
		Integer areaId = query.getAreaId();

		List<TopicDetailDTO> resList = new ArrayList<>();
		String sdate = date + " 00:00:00";// 2015-01-10
		String edate = date + " 23:59:59";// 2015-01-10 23:51:59
		Date startTime = DateTimeFormatUtil.parseyyyyMMddHHmmssDate(sdate);
		Date endTime = DateTimeFormatUtil.parseyyyyMMddHHmmssDate(edate);

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("startTime", startTime);
		if (query.getForcaseType() == null || query.getForcaseType() == CmsForcaseType.BY_DATE) {
			paramMap.put("endTime", endTime);
		} else {
			paramMap.put("endTime", null);
		}
		paramMap.put("start", (pageId > 1 ? (pageId - 1) * pageSize : 0));
		paramMap.put("pageSize", pageSize);
		if (platformType != null && platformType != PlatformEnum.ALL.getCode())
			paramMap.put("platformType", platformType);
		if (areaId != null && areaId != AreaConstant.AREA_ALL)
			paramMap.put("areaId", areaId);
		List<Topic> doList = topicDao.getTomorrowForecast(paramMap);
		Integer totalCount = topicDao.countTomorrowForecast(paramMap);
		if (doList != null && doList.size() > 0) {
			if (logger.isDebugEnabled()) {
				logger.info("[TopicService.getTomorrowForecast]getTomorrowForecast list size:" + doList.size());
			}
			for (Topic Topic : doList) {
				TopicDetailDTO topicDetail = new TopicDetailDTO();
				topicDetail.setTopic(Topic);
				TopicItem promotionItemDO = new TopicItem();
				promotionItemDO.setTopicId(Topic.getId());
				promotionItemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
				List<TopicItem> promotionItemList = topicItemDao.queryByObject(promotionItemDO);
				if (null != promotionItemList && !promotionItemList.isEmpty() && promotionItemList.size() > 0) {
					List<TopicItem> resItemList = new ArrayList<TopicItem>();
					for (TopicItem item : promotionItemList) {
						resItemList.add(item);
					}
					topicDetail.setPromotionItemList(resItemList);
				}
				resList.add(topicDetail);
			}
		}
		PageInfo<TopicDetailDTO> page = new PageInfo<TopicDetailDTO>();
		page.setRows(resList);
		page.setPage(pageId);
		page.setSize(pageSize);
		page.setRecords(totalCount);
		return page;
	}

	@Override
	public PageInfo<TopicDetailDTO> getTodayTopic(CmsTopicSimpleQuery query) throws Exception {
		Integer pageId = query.getPageId();
		Integer pageSize = query.getPageSize();
		Integer platformType = query.getPlatformType();
		Integer areaId = query.getAreaId();

		List<TopicDetailDTO> resList = new ArrayList<TopicDetailDTO>();
		Date now = new Date();
		Date startTime = now;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startTime", startTime);
		paramMap.put("start", (pageId > 1 ? (pageId - 1) * pageSize : 0));
		paramMap.put("pageSize", pageSize);
		if (platformType != null && platformType != PlatformEnum.ALL.getCode())
			paramMap.put("platformType", platformType);
		if (areaId != null && areaId != AreaConstant.AREA_ALL)
			paramMap.put("areaId", areaId);
		List<Topic> doList = topicDao.getTodayTopic(paramMap);
		Integer totalCount = topicDao.countTodayTopic(paramMap);
		if (doList != null && doList.size() > 0) {
			if (logger.isDebugEnabled()) {
				logger.info("[TopicService.getTodayTopic]getTodayTopic list size:" + doList.size());
			}
			for (Topic Topic : doList) {
				TopicDetailDTO topicDetail = new TopicDetailDTO();
				topicDetail.setTopic(Topic);
				// CMS确认不需要活动商品信息
				// TopicItem promotionItemDO = new TopicItem();
				// promotionItemDO.setTopicId(Topic.getId());
				// promotionItemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
				// List<TopicItem> promotionItemList = topicItemDao
				// .selectDynamic(promotionItemDO);
				// if (!promotionItemList.isEmpty()
				// && promotionItemList.size() > 0) {
				// List<TopicItem> resItemList = new ArrayList<TopicItem>();
				// for (TopicItem item : promotionItemList) {
				// // if(item.getBarCode()!= null &&
				// // !item.getBarCode().contains("mtqas"))//去除测试数据
				// resItemList.add(item);
				// }
				// topicDetail.setPromotionItemList(resItemList);
				// }
				resList.add(topicDetail);
			}
		}
		PageInfo<TopicDetailDTO> page = new PageInfo<TopicDetailDTO>();
		page.setRows(resList);
		page.setPage(pageId);
		page.setSize(pageSize);
		page.setRecords(totalCount);
		return page;
	}

	@Override
	public PageInfo<TopicDetailDTO> getTodaySingleTopic(CmsTopicSimpleQuery query) throws Exception {
		Integer pageId = query.getPageId();
		Integer pageSize = query.getPageSize();
		Integer platformType = query.getPlatformType();
		Integer areaId = query.getAreaId();
		if (logger.isDebugEnabled()) {
			if (platformType != null) {
				logger.info(
						"[TopicService.getTodaySingleTopic]today single topic parameter platform type:" + platformType);
			}
			if (areaId != null) {
				logger.info("[TopicService.getTodaySingleTopic]today single topic parameter area id:" + areaId);
			}
		}
		List<TopicDetailDTO> resList = new ArrayList<TopicDetailDTO>();
		Date now = new Date();
		Date startTime = now;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startTime", startTime);
		paramMap.put("start", (pageId > 1 ? (pageId - 1) * pageSize : 0));
		paramMap.put("pageSize", pageSize);
		if (platformType != null && platformType != PlatformEnum.ALL.getCode())
			paramMap.put("platformType", platformType);
		if (areaId != null && areaId != AreaConstant.AREA_ALL)
			paramMap.put("areaId", areaId);
		List<Topic> doList = topicDao.getTodaySingleTopic(paramMap);
		Integer totalCount = topicDao.countTodaySingleTopic(paramMap);
		if (doList != null && doList.size() > 0) {
			if (logger.isDebugEnabled()) {
				logger.info("[TopicService.getTodaySingleTopic]getTodaySingleTopic list size:" + doList.size());
			}
			for (Topic Topic : doList) {
				TopicDetailDTO topicDetail = new TopicDetailDTO();
				topicDetail.setTopic(Topic);
				TopicItem promotionItemDO = new TopicItem();
				promotionItemDO.setTopicId(Topic.getId());
				promotionItemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
				List<TopicItem> promotionItemList = topicItemDao.queryByObject(promotionItemDO);
				if (!promotionItemList.isEmpty() && promotionItemList.size() > 0) {
					List<TopicItem> resItemList = new ArrayList<TopicItem>();
					for (TopicItem item : promotionItemList) {
						resItemList.add(item);
					}
					topicDetail.setPromotionItemList(resItemList);
				}
				resList.add(topicDetail);
			}
		}
		PageInfo<TopicDetailDTO> page = new PageInfo<TopicDetailDTO>();
		page.setRows(resList);
		page.setPage(pageId);
		page.setSize(pageSize);
		page.setRecords(totalCount);
		return page;
	}

	@Override
	public PageInfo<TopicDetailDTO> getTodayAllTopic(CmsTopicSimpleQuery query) throws Exception {
		Integer pageId = query.getPageId();
		Integer pageSize = query.getPageSize();
		Integer platformType = query.getPlatformType();
		Integer areaId = query.getAreaId();
		List<TopicDetailDTO> resList = new ArrayList<TopicDetailDTO>();
		Date now = new Date();
		Date startTime = now;
		if (logger.isDebugEnabled()) {
			if (platformType != null) {
				logger.info("[TopicService.getTodayAllTopic]today all topic parameter platform type:" + platformType);
			}
			if (areaId != null) {
				logger.info("[TopicService.getTodayAllTopic]today all topic parameter area id:" + areaId);
			}
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startTime", startTime);
		paramMap.put("start", (pageId > 1 ? (pageId - 1) * pageSize : 0));
		paramMap.put("pageSize", pageSize);
		if (platformType != null && platformType != PlatformEnum.ALL.getCode())
			paramMap.put("platformType", platformType);
		if (areaId != null && areaId != AreaConstant.AREA_ALL)
			paramMap.put("areaId", areaId);
		List<Topic> doList = topicDao.getTodayAllTopic(paramMap);
		Integer totalCount = topicDao.countTodayAllTopic(paramMap);
		if (doList != null && doList.size() > 0) {
			if (logger.isDebugEnabled()) {
				logger.info("[TopicService.getTodayAllTopic]getTodayAllTopic list size:" + doList.size());
			}
			for (Topic Topic : doList) {
				TopicDetailDTO topicDetail = new TopicDetailDTO();
				topicDetail.setTopic(Topic);
				TopicItem promotionItemDO = new TopicItem();
				promotionItemDO.setTopicId(Topic.getId());
				promotionItemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
				List<TopicItem> promotionItemList = topicItemDao.queryByObject(promotionItemDO);
				if (!promotionItemList.isEmpty() && promotionItemList.size() > 0) {
					List<TopicItem> resItemList = new ArrayList<TopicItem>();
					for (TopicItem item : promotionItemList) {
						resItemList.add(item);
					}
					topicDetail.setPromotionItemList(resItemList);
				}
				resList.add(topicDetail);
			}
		}
		PageInfo<TopicDetailDTO> page = new PageInfo<TopicDetailDTO>();
		page.setRows(resList);
		page.setPage(pageId);
		page.setSize(pageSize);
		page.setRecords(totalCount);
		return page;
	}

	@Override
	public PageInfo<TopicDetailDTO> getTodayLastRash(CmsTopicSimpleQuery query) throws Exception {
		Integer pageId = query.getPageId();
		Integer pageSize = query.getPageSize();
		Integer platformType = query.getPlatformType();
		Integer areaId = query.getAreaId();
		if (logger.isDebugEnabled()) {
			if (platformType != null) {
				logger.info(
						"[TopicService.getTodayLastRash]today last rash topic parameter platform type:" + platformType);
			}
			if (areaId != null) {
				logger.info("[TopicService.getTodayLastRash]today last rash topic parameter area id:" + areaId);
			}
		}
		List<TopicDetailDTO> resList = new ArrayList<TopicDetailDTO>();
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_MONTH, 1); // 24小时结束的专场
		Date endTime = calendar.getTime();

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startTime", now);
		paramMap.put("endTime", endTime);
		paramMap.put("start", (pageId > 1 ? (pageId - 1) * pageSize : 0));
		paramMap.put("pageSize", pageSize);
		if (platformType != null && platformType != PlatformEnum.ALL.getCode())
			paramMap.put("platformType", platformType);
		if (areaId != null && areaId != AreaConstant.AREA_ALL)
			paramMap.put("areaId", areaId);
		List<Topic> doList = topicDao.getTodayLastRash(paramMap);
		Integer totalCount = topicDao.countTodayLastRash(paramMap);
		if (doList != null && doList.size() > 0) {
			if (logger.isDebugEnabled()) {
				logger.info("[TopicService.getTodayLastRash]getTodayLastRash list size:" + doList.size());
			}
			for (Topic Topic : doList) {
				TopicDetailDTO topicDetail = new TopicDetailDTO();
				topicDetail.setTopic(Topic);
				TopicItem promotionItemDO = new TopicItem();
				promotionItemDO.setTopicId(Topic.getId());
				promotionItemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
				List<TopicItem> promotionItemList = topicItemDao.queryByObject(promotionItemDO);
				if (null != promotionItemList && !promotionItemList.isEmpty() && promotionItemList.size() > 0) {
					List<TopicItem> resItemList = new ArrayList<TopicItem>();
					for (TopicItem item : promotionItemList) {
						resItemList.add(item);
					}
					topicDetail.setPromotionItemList(resItemList);
				}
				resList.add(topicDetail);
			}
		}
		PageInfo<TopicDetailDTO> page = new PageInfo<TopicDetailDTO>();
		page.setRows(resList);
		page.setPage(pageId);
		page.setSize(pageSize);
		page.setRecords(totalCount);
		return page;
	}

	// useful
	@Override
	public PageInfo<TopicItemInfoResult> queryTopicPageItemByCondition(TopicItemPageQuery query) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Integer pageId = query.getPageId();
		Integer pageSize = query.getPageSize();
		String priceOrder = query.getPriceOrder();
		Long topicId = query.getTopicId();
		Long brandId = query.getBrandId();
		Long categoryId = query.getCategoryId();
		Boolean stock = query.getStock();
		Long promoterid = query.getPromoterId();
		if (logger.isDebugEnabled()) {
			if (priceOrder != null) {
				logger.info("[TopicService.queryTopicPageItemByCondition]queryTopicPageItem parameter priceOrder:"
						+ priceOrder);
			}
			if (topicId != null) {
				logger.info(
						"[TopicService.queryTopicPageItemByCondition]queryTopicPageItem parameter topicId:" + topicId);
			}
			if (brandId != null) {
				logger.info(
						"[TopicService.queryTopicPageItemByCondition]queryTopicPageItem parameter brandId:" + brandId);
			}
			if (categoryId != null) {
				logger.info("[TopicService.queryTopicPageItemByCondition]queryTopicPageItem parameter categoryId:"
						+ categoryId);
			}
		}
		if (promoterid != null) {
			paramMap.put("promoterId", promoterid);
		}
		paramMap.put("itemStatus", ItemStatusEnum.ONLINE.getValue());
		paramMap.put("topicId", topicId);
		paramMap.put("brandId", brandId);
		paramMap.put("categoryId", categoryId);
		paramMap.put("start", (pageId > 1 ? (pageId - 1) * pageSize : 0));
		paramMap.put("pageSize", pageSize);
		if (stock != null && stock)
			paramMap.put("stock", stock);
		try {
			if (priceOrder != null && !"".equals(priceOrder)) {
				if (priceOrder.equals("desc")) {
					paramMap.put("priceOrderType", "desc");// 降序
				} else if (priceOrder.equals("asc")) {
					paramMap.put("priceOrderType", "asc");// 升序
				} else if (priceOrder.equals("sortIndex")) {
					paramMap.put("priceOrderType", "sortIndex");// 按照排序号
				} else if (priceOrder.equals("new")) {
					paramMap.put("priceOrderType", "new");// 按最新上线
				} else
					paramMap.put("priceOrderType", "sortIndex");// 升序
			} else
				paramMap.put("priceOrderType", "sortIndex");// 按照排序号

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		List<TopicItem> list = topicItemDao.queryTopicPageItemByCondition(paramMap);

		List<TopicItemInfoResult> resultList = new ArrayList<TopicItemInfoResult>();
		if (list != null && list.size() > 0) {
			Topic topic = topicDao.queryById(topicId);
			if (null != topic) {
				for (TopicItem pitemDO : list) {
					TopicItemInfoResult result = new TopicItemInfoResult();
					result.setHasStock(true);
					result.setSku(pitemDO.getSku());
					result.setItemId(pitemDO.getItemId());
					result.setStockAmount(pitemDO.getStockAmount());
					result.setLimitAmount(pitemDO.getLimitAmount());
					result.setTopicPrice(pitemDO.getTopicPrice());
					result.setTopicImage(pitemDO.getTopicImage());
					result.setItemName(pitemDO.getName());
					result.setSalePrice(pitemDO.getSalePrice());
					result.setSaledAmount(pitemDO.getSaledAmount());
					if (pitemDO.getLockStatus() != null) {
						result.setLockStatus(pitemDO.getLockStatus());
						result.setHasStock(pitemDO.getLockStatus() != LockStatus.LOCK.ordinal());
					} else {
						result.setLockStatus(LockStatus.UNLOCK.ordinal());
					}
					result.setTopicId(topicId);
					result.setTopicName(topic.getName());
					result.setTopicStatus(topic.getStatus());
					result.setLastingType(topic.getLastingType());
					result.setStartTime(topic.getStartTime());
					result.setEndTime(topic.getEndTime());
					result.setTopicType(topic.getType());
					result.setStockLocationId(pitemDO.getStockLocationId());
					result.setStockLocationName(pitemDO.getStockLocation());
					result.setPurchaseMethod(pitemDO.getPurchaseMethod());
					//活动是否预占库存：1是0否（5.5）
                    result.setTopicInventoryFlag(topic.getReserveInventoryFlag());
                    
					// 提供首页库存是否已卖完
					if (logger.isDebugEnabled()) {
						logger.info(
								"[TopicService.queryTopicPageItemByCondition]queryTopicPageItem query inventory remain stock:"
										+ result.isHasStock());
					}
					if (result.isHasStock()) {
						if (logger.isDebugEnabled()) {
							logger.info(
									"[TopicService.queryTopicPageItemByCondition]queryTopicPageItem query inventory topicId:"
											+ result.getTopicId());
							logger.info(
									"[TopicService.queryTopicPageItemByCondition]queryTopicPageItem query inventory sku:"
											+ result.getSku());
						}
						boolean hasStock = false;
						int remainStock = 0;
						if (!StringUtils.isBlank(result.getSku())) {
							remainStock = inventoryQueryService.querySalableInventory(App.PROMOTION, String.valueOf(result.getTopicId()), result.getSku(),
                        			result.getStockLocationId(), DEFAULTED.YES.equals(result.getTopicInventoryFlag()));
//							remainStock = inventoryQueryService.selectInvetory(StorageConstant.App.PROMOTION,
//									String.valueOf(result.getTopicId()), result.getSku());
							hasStock = remainStock > 0;
						} else {
							logger.error(
									"[TopicService.queryTopicPageItemByCondition]queryTopicPageItem query sku is empty!");
						}
						if (logger.isDebugEnabled()) {
							logger.info(
									"[TopicService.queryTopicPageItemByCondition]queryTopicPageItem query inventory remain stock amount:"
											+ remainStock);
							logger.info(
									"[TopicService.queryTopicPageItemByCondition]queryTopicPageItem query inventory remain stock:"
											+ hasStock);
						}
						result.setHasStock(hasStock);
					}
					resultList.add(result);
				}
			}
		}
		processItemImage(resultList);
		PageInfo<TopicItemInfoResult> page = new PageInfo<TopicItemInfoResult>();
		page.setRows(resultList);
		page.setPage(pageId);
		page.setSize(pageSize);
		Long totalCount = topicItemDao.countTopicPageItemByCondition(paramMap);
		page.setRecords(totalCount.intValue());
		return page;
	}

	// useful
	@Override
	public TopicItemBrandCategoryDTO queryTopicItemBrandAndCategoryList(long topicId) throws Exception {
		TopicItem item = new TopicItem();
		item.setTopicId(topicId);
		item.setDeletion(DeletionStatus.NORMAL.ordinal());
		List<TopicItem> list = topicItemDao.queryByObject(item);
		TopicItemBrandCategoryDTO dto = new TopicItemBrandCategoryDTO();
		Topic topic = topicDao.queryById(topicId);
		if (topic != null)
			dto.setTopic(topic);
		if (list != null && list.size() > 0) {
			Set<Long> brandIdSet = new HashSet<Long>();
			Set<Long> categoryIdSet = new HashSet<Long>();

			for (TopicItem itemDO : list) {
				Long brandId = itemDO.getBrandId();
				Long categoryId = itemDO.getCategoryId();
				if (brandId != null)
					brandIdSet.add(brandId);
				if (categoryId != null)
					categoryIdSet.add(categoryId);
			}
			dto.setBrandIdList(new ArrayList<Long>(brandIdSet));
			dto.setCategoryIdList(new ArrayList<Long>(categoryIdSet));
		}
		processImage(dto);
		return dto;
	}

	@Override
	public List<TopicInfo> queryTopicRelate(long topicId, int size) {
		try {
			Set<Long> relateTidList = new HashSet<Long>();
			Relate relateF = new Relate();
			relateF.setFirstTopicId(topicId);
			relateF.setDeletion(DeletionStatus.NORMAL.ordinal());
			List<Relate> listF = relateDAO.queryByObject(relateF);
			if (listF != null && listF.size() > 0) {
				for (Relate rdo : listF) {
					relateTidList.add(rdo.getSecondTopicId());
				}
			}

			Relate relateS = new Relate();
			relateS.setSecondTopicId(topicId);
			relateS.setDeletion(DeletionStatus.NORMAL.ordinal());
			List<Relate> listS = relateDAO.queryByObject(relateS);
			if (listS != null && listS.size() > 0) {
				for (Relate rdo : listS) {
					relateTidList.add(rdo.getFirstTopicId());
				}
			}
			if (!relateTidList.isEmpty() && relateTidList.size() > 0) {
				List<Topic> topicList = topicDao.queryTopicInfoList(new ArrayList<Long>(relateTidList));
				List<TopicInfo> infoList = new ArrayList<TopicInfo>();
				for (Topic Topic : topicList) {
					TopicInfo info = new TopicInfo();
					info.setEndTime(Topic.getEndTime());
					info.setImage(Topic.getImage());
					info.setLastingType(Topic.getLastingType());
					info.setName(Topic.getName());
					info.setStartTime(Topic.getStartTime());
					info.setStatus(Topic.getStatus());
					info.setTopicId(Topic.getId());
					info.setDiscount(Topic.getDiscount());
					info.setTopicType(Topic.getType());
					info.setImageInterested(Topic.getImageInterested());
					info.setTopicPoint(Topic.getTopicPoint());
					// 如果是单品团，则设置单品团的单个商品的sku
					if (Topic.getType() == TopicType.SINGLE.ordinal()) {
						TopicItem TopicItem = new TopicItem();
						TopicItem.setTopicId(Topic.getId());
						List<TopicItem> itemList = topicItemDao.queryByObject(TopicItem);
						if (itemList != null && itemList.size() > 0) {
							info.setItemSku(itemList.get(0).getSku());
						}
					}
					// 新增加图片
					info.setMobileImage(Topic.getMobileImage());
					info.setMallImage(Topic.getMallImage());
					info.setPcImage(Topic.getPcImage());
					info.setPcInterestImage(Topic.getPcInterestImage());
					info.setHaitaoImage(Topic.getHaitaoImage());
					infoList.add(info);
				}
				if (infoList.size() > size)
					infoList = infoList.subList(0, size);
				return infoList;
			} else
				return Collections.emptyList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Collections.emptyList();
		}
	}

	@Override
	public List<TopicItemInfoResult> queryLastestSingleTopic(int size) throws Exception {
		List<Topic> topicList = topicDao.queryLastestSingleTopic(size);
		List<TopicItemInfoResult> resultList = new ArrayList<TopicItemInfoResult>();
		if (topicList != null && topicList.size() > 0) {
			for (Topic topic : topicList) {
				long tid = topic.getId();
				if (topic.getStatus() == TopicStatus.PASSED.ordinal()) {
					TopicItem promotionItemDO = new TopicItem();
					promotionItemDO.setTopicId(tid);
					promotionItemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
					List<TopicItem> pitemList = topicItemDao.queryByObject(promotionItemDO);
					if (!pitemList.isEmpty() && pitemList.size() > 0) {
						TopicItem pitemDO = pitemList.get(0);
						TopicItemInfoResult result = processResult(pitemDO, tid, topic);
						resultList.add(result);
					}
				}
			}
		}
		return resultList;

	}

	@Override
	public SkuTopicDTO queryTopicListBySku(String sku, int days) throws Exception {
		SkuTopicDTO skutopicDTO = new SkuTopicDTO();
		TopicItem tiDO = new TopicItem();
		tiDO.setSku(sku);
		tiDO.setDeletion(DeletionStatus.NORMAL.ordinal());
		List<TopicItem> list = topicItemDao.queryByObject(tiDO);
		List<TopicItem> resItemList = new ArrayList<TopicItem>();
		for (TopicItem item : list) {
			if (item.getBarCode() != null && !item.getBarCode().contains("mtqas"))// 去除测试数据
				resItemList.add(item);
		}
		List<TopicItemInfoResult> onSaleList = new ArrayList<TopicItemInfoResult>();
		List<TopicItemInfoResult> willSaleList = new ArrayList<TopicItemInfoResult>();
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_MONTH, days + 1);
		Date newTime = calendar.getTime();
		if (resItemList != null && resItemList.size() > 0) {
			List<Long> topicIdList = new ArrayList<Long>();
			for (TopicItem topicItem : resItemList) {
				topicIdList.add(topicItem.getTopicId());
			}
			List<Topic> topicList = topicDao.queryTopicInList(topicIdList);
			Map<Long, Topic> topicMap = new HashMap<Long, Topic>();
			for (Topic topic : topicList) {
				topicMap.put(topic.getId(), topic);
			}

			for (TopicItem topicItem : resItemList) {
				long tid = topicItem.getTopicId();
				Topic topic = topicMap.get(tid);
				if (null == topic) {
					continue;
				}
				if (topic.getStatus() != TopicStatus.PASSED.ordinal()) {
					continue;
				} else {
					Date startTime = topic.getStartTime();
					Date endTime = topic.getEndTime();
					if ((now.after(startTime) || now.equals(startTime)) && (now.before(endTime))) {// 已经开始
						TopicItemInfoResult result = processResult(topicItem, tid, topic);
						onSaleList.add(result);
					}
					if (startTime.after(now) && startTime.before(newTime)) {// 即将开始
						TopicItemInfoResult result = processResult(topicItem, tid, topic);
						willSaleList.add(result);
					}
				}
			}
		}

		skutopicDTO.setOnSaleList(onSaleList);
		skutopicDTO.setWillSaleList(willSaleList);
		return skutopicDTO;

	}

	@Override
	@Transactional
	public ResultInfo scanTopicStatus(Date rangeTime) {
		try {
			if (logger.isDebugEnabled()) {
				logger.info("[TopicService.scanTopicStatus]in scan status.....");
			}
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("status", TopicConstant.TOPIC_STATUS_AUDITED);
			paramMap.put("date", rangeTime);
			List<Long> scanAuditedIds = topicDao.getScanTopicIds(paramMap);
			if (null == scanAuditedIds || scanAuditedIds.size() < 1) {
				return new ResultInfo();
			}
			List<Long> changeIds = new ArrayList<>();
			for (Long id : scanAuditedIds) {
				try {
					if (processUpdateStatus(id, rangeTime)) {
						changeIds.add(id);
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					throw new ServiceException("更新任务状态失败!");
				}
			}
			if (logger.isDebugEnabled()) {
				logger.info("[TopicService.scanTopicStatus]start update redis------->");
			}
			if (changeIds != null && changeIds.size() > 0) {
				ResultInfo pResult = topicRedisService.insertNewPromotions(changeIds);
				if (!pResult.isSuccess()) {
					logger.error("save redis of topic info failed");
				}
				if (logger.isDebugEnabled()) {
					logger.info("[TopicService.scanTopicStatus]end update redis------->");
					logger.info("[TopicService.scanTopicStatus]change info size.....--------->" + changeIds.size());
					logger.info("[TopicService.scanTopicStatus]end scan status.....--------------------->"
							+ scanAuditedIds.size());
				}
			}
			return new ResultInfo();
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("还库存失败!");
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private boolean processUpdateStatus(Long tid, Date afterTime) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.info("[TopicService.processUpdateStatus]in @Transactional......................Topic -->" + tid);
		}
		Topic Topic = topicDao.queryById(tid);
		boolean hasChange = false;
		Date startTime = Topic.getStartTime();
		Date endTime = Topic.getEndTime();
		if (TopicStatus.PASSED.ordinal() == Topic.getStatus() && TopicProcess.NOTSTART.ordinal() == Topic.getProgress()
				&& null != startTime && afterTime.after(startTime)) {
			Topic.setProgress(TopicProcess.PROCESSING.ordinal());
			hasChange = true;
		}
		// 时间到，未终止的活动更新
		if (null != endTime && afterTime.after(endTime) && TopicStatus.STOP.ordinal() != Topic.getStatus()
				&& TopicProcess.ENDING.ordinal() != Topic.getProgress()) {
			Topic.setProgress(TopicProcess.ENDING.ordinal());
			Topic.setStatus(TopicStatus.STOP.ordinal());
			hasChange = true;
		}
		if (hasChange) {
			Topic updateTopic = new Topic();
			updateTopic.setId(Topic.getId());
			updateTopic.setStatus(Topic.getStatus());
			updateTopic.setProgress(Topic.getProgress());
			updateTopic.setUpdateTime(new Date());
			updateTopic.setUpdateUser("");
			topicDao.updateNotNullById(updateTopic);

			if (TopicStatus.STOP.ordinal() == Topic.getStatus()) {
				if (logger.isDebugEnabled()) {
					logger.info("[TopicService.processUpdateStatus]start return inventory------->Topic" + tid);
				}
				boolean isPreOccupyInventory = DEFAULTED.YES.equals(Topic.getReserveInventoryFlag());
				if(isPreOccupyInventory){
					ResultInfo result = inventoryOperService.backRequestInventory(StorageConstant.App.PROMOTION,
							String.valueOf(Topic.getId()));
					if (result != null) {
						if (!result.isSuccess()) {
							logger.error(String.format("活动序号[%d]退还库存失败!", Topic.getId()));
							throw new ServiceException(String.format("活动序号[%d]退还库存失败!", Topic.getId()));
						}
					}
					if (logger.isDebugEnabled()) {
						logger.info("[TopicService.processUpdateStatus]end return inventory------->Topic" + tid);
					}
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.info("[TopicService.processUpdateStatus]in @Transactional end.........-->Topic" + tid);
		}
		return hasChange;
	}

	private TopicItemInfoResult processResult(TopicItem topicItem, long tid, Topic topic) {
		TopicItemInfoResult result = new TopicItemInfoResult();
		result.setSku(topicItem.getSku());
		result.setItemId(topicItem.getItemId());
		result.setStockAmount(topicItem.getStockAmount());
		result.setLimitAmount(topicItem.getLimitAmount());
		result.setTopicPrice(topicItem.getTopicPrice());
		result.setTopicImage(topicItem.getTopicImage());
		result.setItemName(topicItem.getName());
		result.setTopicId(tid);
		result.setTopicName(topic.getName());
		result.setTopicStatus(topic.getStatus());
		result.setLastingType(topic.getLastingType());
		result.setStartTime(topic.getStartTime());
		result.setEndTime(topic.getEndTime());
		result.setSalePrice(topicItem.getSalePrice());
		result.setTopicType(topic.getType());
		if (topicItem.getLockStatus() != null) {
			result.setLockStatus(topicItem.getLockStatus());
		} else {
			result.setLockStatus(LockStatus.UNLOCK.ordinal());
		}
		result.setStockLocationId(topicItem.getStockLocationId());
		result.setStockLocationName(topicItem.getStockLocation());
		result.setPurchaseMethod(topicItem.getPurchaseMethod());
		return result;
	}

	/**
	 * 获得审批信息
	 *
	 * @param tid
	 * @param topicDetail
	 * @
	 */
	private void getAuditInfo(long tid, TopicDetailDTO topicDetail) {
		TopicAuditLog audit = new TopicAuditLog();
		audit.setTopicId(tid);
		topicDetail.setAuditLogList(topicAuditLogDao.queryByObject(audit));
	}

	private void getCouponInfo(long tid, TopicDetailDTO topicDetail) {
		List<TopicCouponDTO> couponDtos = new ArrayList<TopicCouponDTO>();
		TopicCoupon selectCoupon = new TopicCoupon();
		selectCoupon.setTopicId(tid);
		List<TopicCoupon> coupons = topicCouponDao.queryByObject(selectCoupon);
		for (TopicCoupon TopicCoupon : coupons) {
			TopicCouponDTO dto = new TopicCouponDTO();
			dto.setId(TopicCoupon.getId());
			dto.setSortIndex(TopicCoupon.getSortIndex());
			dto.setCouponId(TopicCoupon.getCouponId());
			dto.setCouponImage(TopicCoupon.getCouponImage());
			dto.setCouponSize(TopicCoupon.getCouponSize());
			Coupon coupon = couponDao.queryById(TopicCoupon.getCouponId());
			if (null != coupon) {
				dto.setCouponName(coupon.getCouponName());
				dto.setCouponType(coupon.getCouponType());
			}
			couponDtos.add(dto);
		}
		topicDetail.setCouponList(couponDtos);
	}

	/**
	 * 获得关联专场信息
	 *
	 * @param tid
	 * @param topicDetail
	 * @
	 */
	private void getRelateInfo(long tid, TopicDetailDTO topicDetail) {
		Set<Long> relateTidSet = new HashSet<Long>();
		Set<RelateDTO> relateDTOSet = new HashSet<RelateDTO>();
		Relate relateDOF = new Relate();
		relateDOF.setFirstTopicId(tid);
		relateDOF.setDeletion(DeletionStatus.NORMAL.ordinal());
		List<Relate> relatedListF = relateDAO.queryByObject(relateDOF);
		if (!relatedListF.isEmpty() && relatedListF.size() > 0) {
			for (Relate relate : relatedListF) {

				RelateDTO relateDTO = new RelateDTO();
				relateDTO.setRelateDTO(relate);
				// TODO:性能优化
				Topic secondTopic = topicDao.queryById(relate.getSecondTopicId());
				if (null != secondTopic) {
					relateDTO.setSecondTopicName(secondTopic.getName());
					String number = secondTopic.getNumber();
					relateDTO.setSecondTopicNumber(String.valueOf(secondTopic.getNumber()));
					relateTidSet.add(relate.getSecondTopicId());
					relateDTOSet.add(relateDTO);
				}
			}
		}
		Relate relateDOS = new Relate();
		relateDOS.setSecondTopicId(tid);
		relateDOS.setDeletion(DeletionStatus.NORMAL.ordinal());
		List<Relate> relatedListS = relateDAO.queryByObject(relateDOS);
		if (null != relatedListS) {
			if (!relatedListS.isEmpty() && relatedListS.size() > 0) {
				for (Relate Relate : relatedListS) {
					RelateDTO relateDTO = new RelateDTO();
					relateDTO.setRelateDTO(Relate);
				}
			}
			if (relateTidSet.size() > 0) {
				topicDetail.setRelateTidList(new ArrayList<Long>(relateTidSet));
			}
			if (relateDTOSet.size() > 0) {
				topicDetail.setRelateList(new ArrayList<RelateDTO>(relateDTOSet));
			}
		}
	}

	/**
	 * 获得策略信息
	 *
	 * @param topicDetail
	 * @param Topic
	 * @
	 */
	private void getPolicy(TopicDetailDTO topicDetail, Topic Topic) {
		Long policyId = Topic.getLimitPolicyId();
		if (policyId != null && policyId != 0) {
			PolicyInfo PolicyInfo = policyInfoDao.queryById(policyId.longValue());
			topicDetail.setPolicy(PolicyInfo);
		}
	}

	/**
	 * 获得平台信息
	 *
	 * @param topicDetail
	 * @param Topic
	 */
	private void getPlatformInfo(TopicDetailDTO topicDetail, Topic Topic) {
		String platformStr = Topic.getPlatformStr();
		if (platformStr != null && !platformStr.equals("")) {
			List<Platform> platformList = new ArrayList<Platform>();
			List<Integer> platformCodes = new ArrayList<>();
			String[] pids = platformStr.split(",");
			for (String pid : pids) {
				if (StringUtils.isBlank(pid))
					continue;
				PlatformEnum platformEnum = PlatformEnum.getByCode(Integer.parseInt(pid));
				if (platformEnum == null)
					continue;
				Platform platform = new Platform();
				platform.setId(platformEnum.getCode());
				platform.setName(platformEnum.name());
				platformList.add(platform);
				platformCodes.add(platformEnum.getCode());
			}
			topicDetail.setPlatformList(platformList);
			topicDetail.setPlatformCodes(platformCodes);
		}
	}

	/**
	 * 获取活动商品清单
	 *
	 * @param tid
	 * @param topicDetail
	 * @
	 */
	private List<TopicItem> getTopicItemDOs(Long promoterid, long tid, boolean isLock) {
		// TopicItem promotionItemDO = new TopicItem();
		// promotionItemDO.setTopicId(tid);
		// promotionItemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
		// if (isLock) {
		// promotionItemDO.setLockStatus(LockStatus.LOCK.ordinal());
		// }
		// List<TopicItem> promotionItemList =
		// topicItemDao.queryByObject(promotionItemDO);
		// by zhs for dss
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("promoterid", promoterid);
		params.put("topicid", tid);
		params.put("deletion", DeletionStatus.NORMAL.ordinal());
		params.put("itemStatus", ItemStatusEnum.ONLINE.getValue());
		if (isLock) {
			params.put("lockstatus", LockStatus.LOCK.ordinal());
		}
		List<TopicItem> promotionItemList = topicItemDao.getTopicItemFileterByDSS(params);
		return promotionItemList;
	}

	/**
	 * 获取活动商品清单
	 *
	 * @param tid
	 * @param topicDetail
	 * @
	 */
	private List<TopicItem> getTopicItemDOsForDss(Long promoterid, long tid, boolean isLock) {
		// by zhs for dss
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("promoterid", promoterid);
		params.put("topicid", tid);
		params.put("deletion", DeletionStatus.NORMAL.ordinal());
		params.put("itemStatus", ItemStatusEnum.ONLINE.getValue());
		if (isLock) {
			params.put("lockstatus", LockStatus.LOCK.ordinal());
		}
		Topic topic = queryById(tid);
		if (topic != null && topic.getType().equals(TopicConstant.TOPIC_TYPE_BRAND)) { // 品牌团时
			params.put("topictype", topic.getType());
		}
		List<TopicItem> promotionItemList = topicItemDao.getTopicItemFileterByDSS_2(params);
		return promotionItemList;
	}

	/**
	 * 获取活动商品清单(Dto)
	 *
	 * @
	 */
	private List<TopicItemInfoDTO> getTopicItemDTOs(long tid) {
		TopicItem promotionItemDO = new TopicItem();
		promotionItemDO.setTopicId(tid);
		promotionItemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
		List<TopicItem> promotionItemList = topicItemDao.queryByObject(promotionItemDO);

		List<TopicItemInfoDTO> topicItemDtoList = new ArrayList<TopicItemInfoDTO>();
		for (TopicItem item : promotionItemList) {
			if (null != item) {
				TopicItemInfoDTO itemDto = new TopicItemInfoDTO();
				itemDto.setTopicItem(item);
				topicItemDtoList.add(itemDto);
			}
		}
		return topicItemDtoList;
	}

	@Override
	public List<String> checkTopicSkuList(Long topicId, List<String> skuList) throws Exception {
		return topicItemDao.checkTopicSkuList(topicId, skuList);
	}

	@Override
	public Topic queryTunInfo() throws Exception {
		Topic topic = new Topic();
		topic.setType(TopicType.TUN.ordinal());
		List<Topic> topicList = topicDao.queryByObject(topic);
		if (topicList != null && topicList.size() > 0) {
			return topicList.get(0);
		} else
			throw new Exception("获取海淘出错！");
	}

	@Override
	public PageInfo<TopicItemInfoResult> queryTunTopicItem(TopicItemPageQuery query) throws Exception {
		return queryTopicPageItemByCondition(query);
	}

	@Override
	public TopicItemBrandCategoryDTO queryTunBrandAndCategoryList(long topicId) throws Exception {
		return queryTopicItemBrandAndCategoryList(topicId);
	}

	@Override
	public TopicItemBrandCategoryDTO queryTopicItemBrandList(long topicId, List<Long> brandIds) throws Exception {

		// TODO: 与queryTopicItemBrandAndCategoryList方法进行抽取
		List<TopicItem> list = topicItemDao.getTopicItemByTopicIdAndBrands(topicId, brandIds);
		TopicItemBrandCategoryDTO dto = new TopicItemBrandCategoryDTO();
		if (list != null && list.size() > 0) {
			Set<Long> brandIdSet = new HashSet<Long>();
			Set<Long> categoryIdSet = new HashSet<Long>();

			for (TopicItem itemDO : list) {
				Long brandId = itemDO.getBrandId();
				Long categoryId = itemDO.getCategoryId();
				if (brandId != null)
					brandIdSet.add(brandId);
				if (categoryId != null)
					categoryIdSet.add(categoryId);

			}
			dto.setBrandIdList(new ArrayList<Long>(brandIdSet));
			dto.setCategoryIdList(new ArrayList<Long>(categoryIdSet));
		}
		return dto;
	}

	@Override
	public List<Topic> queryTopicInList(List<Long> topicIdList) {
		return topicDao.queryTopicInList(topicIdList);
	}

	@Override
	public Boolean checkFavouriteTopicStatus(Long topicId) throws Exception {
		Topic topic = topicDao.queryById(topicId);
		if (topic != null) {
			int status = topic.getStatus();
			if (status != TopicStatus.PASSED.ordinal()) {
				logger.error("[TopicService.checkFavouriteTopicStatus]topic status is not passed..id:" + topicId);
				throw new Exception("专场不存在！");
			}
			Date startTime = topic.getStartTime();
			Date endTime = topic.getEndTime();
			Date now = new Date();
			if (startTime != null) {
				if (startTime.before(now)) {
					logger.error("[TopicService.checkFavouriteTopicStatus]topic status is processing..id:" + topicId);
					throw new Exception("专场已经开始！");
				}
			}
			if (endTime != null) {
				if (endTime.before(now)) {
					logger.error("[TopicService.checkFavouriteTopicStatus]topic status is ended..id:" + topicId);
					throw new Exception("专场已经结束！");
				}
			}
			return true;
		} else {
			logger.error("[TopicService.checkFavouriteTopicStatus]topic id is invalid..id:" + topicId);
			throw new Exception("专场不存在！");
		}

	}

	@Override
	public Boolean checkCollectTopicStatus(Long topicId) throws Exception {
		Topic topic = topicDao.queryById(topicId);
		if (topic != null) {
			int status = topic.getStatus();
			if (status == TopicStatus.PASSED.ordinal() || status == TopicStatus.STOP.ordinal())
				return true;
			else {
				logger.error("[TopicService.checkCollectTopicStatus]topic id is invalid..id:" + topicId);
				throw new Exception("专场不存在！");
			}
			/*
			 * Date startTime = topic.getStartTime(); Date endTime =
			 * topic.getEndTime(); Date now = new Date(); if(now.before(endTime)
			 * && now.after(startTime)) return true; else throw new
			 * Exception("专场状态异常！");
			 */

		} else {
			logger.error("[TopicService.checkCollectTopicStatus]topic id is invalid..id:" + topicId);
			throw new Exception("专场不存在！");
		}

	}

	@Override
	public List<ItemCollection> queryCollectItemTopicId(List<ItemCollection> itemList) throws Exception {
		if (itemList != null && itemList.size() > 0) {
			List<ItemCollection> resList = new ArrayList<ItemCollection>();
			List<Long> topicIdList = new ArrayList<Long>();
			for (ItemCollection item : itemList) {
				topicIdList.add(item.getTopicId());
			}
			List<Topic> topicList = this.queryTopicInList(topicIdList);
			Map<Long, Topic> topicMap = new HashMap<Long, Topic>();
			for (Topic Topic : topicList) {
				topicMap.put(Topic.getId(), Topic);
			}
			for (ItemCollection item : itemList) {
				String sku = item.getSkuCode();
				Long topicId = item.getTopicId();
				Topic topic = topicMap.get(topicId);
				if (null == topic)
					continue;
				if (topic.getDeletion() == DeletionStatus.NORMAL.ordinal()
						&& topic.getStatus() == TopicStatus.PASSED.ordinal()
						&& topic.getProgress() == ProgressStatus.DOING.ordinal()) {
					resList.add(item);
				} else {
					TopicItem topicItem = new TopicItem();
					topicItem.setSku(sku);
					topicItem.setDeletion(DeletionStatus.NORMAL.ordinal());
					List<TopicItem> topicItemList = topicItemDao.queryByObject(topicItem);
					if (topicItemList != null && topicItemList.size() > 0) {
						List<Topic> topicListIng = new ArrayList<Topic>();
						List<Topic> topicListWill = new ArrayList<Topic>();
						List<Topic> topicListEnd = new ArrayList<Topic>();

						List<Long> topicIds = new ArrayList<Long>();
						for (TopicItem itemDO : topicItemList) {
							topicIds.add(itemDO.getTopicId());
						}
						List<Topic> topicListSub = this.queryTopicInList(topicIds);
						processTopicList(topicListIng, topicListWill, topicListEnd, topicListSub);
						// 获得规则 1：进行中 开始时间最近的 2：即将开始的 ：最早开始的 3：已结束的：最晚结束的
						if (topicListIng != null && topicListIng.size() > 0) {
							Collections.sort(topicListIng, new StartTimeCompareSort());
							Topic tdo = topicListIng.get(0);
							item.setTopicId(tdo.getId());
							resList.add(item);
						} else if (topicListWill != null && topicListWill.size() > 0) {
							Collections.sort(topicListWill, new StartTimeCompareSort());
							Topic tdo = topicListWill.get(topicListWill.size() - 1);
							item.setTopicId(tdo.getId());
							resList.add(item);
						} else if (topicListEnd != null && topicListEnd.size() > 0) {
							Collections.sort(topicListEnd, new EndTimeCompareSort());
							Topic tdo = topicListEnd.get(topicListEnd.size() - 1);
							item.setTopicId(tdo.getId());
							resList.add(item);
						} else {
							resList.add(item);
						}
					}
				} // end if
			} // end for
			return resList;
		} else
			return null;
	}

	@Override
	public List<BrandCollection> queryCollectBrandTopicId(List<BrandCollection> brandList) throws Exception {
		if (brandList != null && brandList.size() > 0) {
			List<BrandCollection> resList = new ArrayList<>();
			List<Long> topicIdList = new ArrayList<>();
			for (BrandCollection brand : brandList) {
				topicIdList.add(brand.getTopicId());
			}
			List<Topic> topicList = this.queryTopicInList(topicIdList);
			Map<Long, Topic> topicMap = new HashMap<>();
			for (Topic Topic : topicList) {
				topicMap.put(Topic.getId(), Topic);
			}

			for (BrandCollection brand : brandList) {
				if (brand == null)
					continue;
				Long brandId = brand.getBrandId();
				Long topicId = brand.getTopicId();
				if (brandId == null || topicId == null) {
					continue;
				}
				Topic defaultTopic = topicMap.get(topicId);
				if (null == defaultTopic)
					continue;
				if (defaultTopic.getDeletion() == DeletionStatus.NORMAL.ordinal()
						&& defaultTopic.getStatus() == TopicStatus.PASSED.ordinal()
						&& defaultTopic.getProgress() == ProgressStatus.DOING.ordinal()) {
					resList.add(brand);
				} else {
					List<Topic> topicListIng = new ArrayList<Topic>();
					List<Topic> topicListWill = new ArrayList<Topic>();
					List<Topic> topicListEnd = new ArrayList<Topic>();

					Topic topic = new Topic();
					topic.setBrandId(brandId);
					topic.setType(TopicType.BRAND.ordinal());
					topic.setDeletion(DeletionStatus.NORMAL.ordinal());
					List<Topic> toicList = topicDao.queryByObject(topic);
					processTopicList(topicListIng, topicListWill, topicListEnd, toicList);

					// 获得规则 1：进行中 开始时间最近的 2：即将开始的 ：最早开始的 3：已结束的：最晚结束的
					if (topicListIng != null && topicListIng.size() > 0) {
						Collections.sort(topicListIng, new StartTimeCompareSort());
						Topic tdo = topicListIng.get(0);
						brand.setTopicId(tdo.getId());
						resList.add(brand);
					} else if (topicListWill != null && topicListWill.size() > 0) {
						Collections.sort(topicListWill, new StartTimeCompareSort());
						Topic tdo = topicListWill.get(topicListWill.size() - 1);
						brand.setTopicId(tdo.getId());
						resList.add(brand);
					} else if (topicListEnd != null && topicListEnd.size() > 0) {
						Collections.sort(topicListEnd, new EndTimeCompareSort());
						Topic tdo = topicListEnd.get(topicListEnd.size() - 1);
						brand.setTopicId(tdo.getId());
						resList.add(brand);
					} else {
						resList.add(brand);
					}
				}
			}
			return resList;
		} else
			return null;
	}

	/**
	 * 按照状态分组
	 *
	 * @param topicListIng
	 * @param topicListWill
	 * @param topicListEnd
	 * @param toicList
	 */
	private void processTopicList(List<Topic> topicListIng, List<Topic> topicListWill, List<Topic> topicListEnd,
			List<Topic> toicList) {
		for (Topic Topic : toicList) {
			if (Topic.getProgress() == ProgressStatus.DOING.ordinal()
					&& Topic.getStatus() == TopicStatus.PASSED.ordinal()
					&& Topic.getType() != TopicType.TUN.ordinal()) {
				topicListIng.add(Topic);
			} else if (Topic.getProgress() == ProgressStatus.NotStarted.ordinal()
					&& Topic.getStatus() == TopicStatus.PASSED.ordinal()
					&& Topic.getType() != TopicType.TUN.ordinal()) {
				topicListWill.add(Topic);

			} else if (Topic.getStatus() == TopicStatus.STOP.ordinal() && Topic.getType() != TopicType.TUN.ordinal()) {
				topicListEnd.add(Topic);
			}
		}
	}

	@Override
	public List<TopicPolicyDTO> queryTopicPolicyInfo(List<TopicItemCartQuery> queryPolicyInfos) throws Exception {
		List<Long> topicIds = new ArrayList<>();
		List<TopicItem> itemDOs = new ArrayList<>();
		// 获取活动Id和活动商品查询列表
		for (TopicItemCartQuery topicItemCartQuery : queryPolicyInfos) {
			if (topicItemCartQuery == null) {
				continue;
			}
			TopicItem itemDo = new TopicItem();
			topicIds.add(topicItemCartQuery.getTopicId());
			itemDo.setTopicId(topicItemCartQuery.getTopicId());
			itemDo.setSku(topicItemCartQuery.getSku());
			itemDOs.add(itemDo);
		}
		// 获得活动商品限购信息列表
		Map<String, Integer> itemLimitResult = new HashMap<String, Integer>();
		try {
			List<TopicItem> items = topicItemDao.getTopicItemByTopicIdAndSku(itemDOs);
			for (TopicItem TopicItem : items) {
				if (TopicItem == null || TopicItem.getTopicId() == null || TopicItem.getSku() == null) {
					continue;
				}
				String key = String.valueOf(TopicItem.getTopicId()) + "-" + TopicItem.getSku();
				if (!itemLimitResult.containsKey(key)) {
					itemLimitResult.put(key, TopicItem.getLimitAmount());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("查询限购信息出错");
		}
		if (itemLimitResult == null || itemLimitResult.size() == 0) {
			logger.error("[TopicService.queryTopicPolicyInfo]topic id is invalid...");
			throw new ServiceException("查询限购信息出错");
		}
		// 获得活动限购策略和活动对应关系列表
		Map<Long, PolicyInfo> topicPolicies = new HashMap<Long, PolicyInfo>();
		try {
			List<Topic> topicInfos = topicDao.queryTopicInList(topicIds);
			for (Topic Topic : topicInfos) {
				if (Topic == null) {
					continue;
				}
				PolicyInfo policy = policyInfoDao.queryById(Topic.getLimitPolicyId());
				if (policy == null) {
					continue;
				}
				if (!topicPolicies.containsKey(Topic.getId())) {
					topicPolicies.put(Topic.getId(), policy);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("查询限购信息出错");
		}
		if (topicPolicies == null || topicPolicies.size() == 0) {
			logger.error("[TopicService.queryTopicPolicyInfo]topic limit policy is invalid...");
			throw new ServiceException("查询限购策略出错");
		}
		// 根据活动商品信息和活动限购策略 ，组装成限购信息对象
		List<TopicPolicyDTO> policyDTOs = new ArrayList<TopicPolicyDTO>();
		for (TopicItemCartQuery queryDto : queryPolicyInfos) {
			if (queryDto == null) {
				continue;
			}
			String key = String.valueOf(queryDto.getTopicId()) + "-" + queryDto.getSku();
			TopicPolicyDTO policyDTO = new TopicPolicyDTO();
			policyDTO.setSkuCode(queryDto.getSku());
			policyDTO.setTopicId(queryDto.getTopicId());
			policyDTO.setQuantity(queryDto.getAmount());
			if (itemLimitResult.containsKey(key) && itemLimitResult.get(key) != null) {
				policyDTO.setTopicSum(itemLimitResult.get(key));
			} else {
				// 异常处理
				policyDTO.setTopicSum(0);
			}
			if (topicPolicies.containsKey(queryDto.getTopicId())) {
				PolicyInfo policy = topicPolicies.get(queryDto.getTopicId());
				if (policy != null) {
					if (policy.getByIp() != null && 1 == policy.getByIp()) {
						policyDTO.setIp(queryDto.getUip());
					}
					if (policy.getByUid() != null && 1 == policy.getByUid()) {
						policyDTO.setUserId(queryDto.getMemberId());
					}
					if (policy.getByMobile() != null && 1 == policy.getByMobile()) {
						policyDTO.setMobile(queryDto.getMobile());// 如果有收货手机号的限制，则加上
					} // 限制收货手机
				}
			}
			policyDTOs.add(policyDTO);
		}
		return policyDTOs;
	}

	@Override
	public TopicDetailDTO getTopicDetailByIdWithLockItem(Long tid) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.info("[TopicService.getTopicDetailByIdWithLockItem]getTopicDetailById ---- topicId:" + tid);
		}
		TopicDetailDTO topicDetail = new TopicDetailDTO();
		Topic Topic = queryById(tid);
		if (Topic == null)
			throw new ServiceException(ProcessingErrorMessage.VALID_TOPIC_INFO);
		topicDetail.setTopic(Topic);
		// 获得平台信息
		getPlatformInfo(topicDetail, Topic);
		// 获得限购政策
		getPolicy(topicDetail, Topic);
		// 获得关联专场
		// relateDAO
		getRelateInfo(tid, topicDetail);
		// 获得优惠券列表
		getCouponInfo(tid, topicDetail);
		// 获得审批记录
		getAuditInfo(tid, topicDetail);
		// 获得商品信息
		List<TopicItem> items = getTopicItemDOs(null, tid, true);
		topicDetail.setPromotionItemList(items);
		//获取分销渠道
		List<TopicPromoter> topicPromoterList = topicPromoterDao.queryListByTopicId(tid);
		topicDetail.setTopicPromoterList(topicPromoterList);
		return topicDetail;
	}

	@Override
	public TopicDetailDTO getSingleTopicDetailByIdsForCms(List<Long> tids) throws Exception {
		if (tids == null) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (logger.isDebugEnabled()) {
			for (Long tid : tids) {
				logger.info("[TopicService.getSingleTopicDetailByIdsForCms]input param........ id:" + tid);
			}
		}
		paramMap.put("ids", tids);
		paramMap.put("status", TopicStatus.PASSED.ordinal());
		paramMap.put("progress", TopicProcess.PROCESSING.ordinal());
		Long topicId = topicDao.getSingleAvailableTopicIdByIds(paramMap);
		if (topicId == null || topicId == 0) {
			logger.error("[TopicService.getTopicDetailByIdsForCms]topic id is invalid");
			return null;
		}
		if (logger.isDebugEnabled()) {
			logger.info("[TopicService.getSingleTopicDetailByIdsForCms]valid id after filter ........ id:" + topicId);
		}
		return this.getTopicDetailById(null, topicId);
	}

	// useful
	@Override
	public List<TopicDetailDTO> getTopicDetailByIdsForCms(Long promoterid, List<Long> tids) throws Exception {
		if (tids == null) {
			return null;
		}
		List<TopicDetailDTO> dtos = new ArrayList<TopicDetailDTO>();
		//	logger.info("[ $$$$$$$$$$^^^^^^start time ]" +  System.currentTimeMillis()   );
		for (Long tid : tids) {
			if (logger.isDebugEnabled()) {
				logger.info("[TopicService.getTopicDetailByIdsForCms]input param........ id:" + tid);
			}
			TopicDetailDTO dto = this.getTopicDetailById(promoterid, tid);
			if (dto == null) {
				logger.error("[TopicService.getTopicDetailByIdsForCms] info is null........ id:" + tid);
				continue;
			}
			dtos.add(dto);
		}
		//logger.info("[ $$$$$$$$$$^^^^^^end time ]" +  System.currentTimeMillis()   );
		processImage(dtos);
		return dtos;
	}

	/**
	 * 处理图片，返回图片的全路径
	 * 
	 * @param topicDetails
	 */
	private void processImage(List<TopicDetailDTO> topicDetails) {
		if (CollectionUtils.isEmpty(topicDetails))
			return;
		for (TopicDetailDTO topicDetail : topicDetails) {
			processImage(topicDetail);
		}
	}

	private void processItemImage(List<TopicItemInfoResult> list) {
		if (CollectionUtils.isEmpty(list))
			return;
		for (TopicItemInfoResult item : list) {
			if (item == null)
				continue;
			item.setTopicImage(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item, item.getTopicImage()));
		}
	}

	private void processImage(TopicDetailDTO topicDetail) {
		if (topicDetail == null)
			return;
		if (topicDetail.getTopic() == null)
			return;
		Topic topic = topicDetail.getTopic();
		processImage(topic);

	}

	private void processImage(TopicItemBrandCategoryDTO topicItemBrandCategoryDTO) {
		if (topicItemBrandCategoryDTO == null)
			return;
		if (topicItemBrandCategoryDTO.getTopic() == null)
			return;
		Topic topic = topicItemBrandCategoryDTO.getTopic();
		processImage(topic);

	}

	private void processImage(Topic topic) {
		topic.setMobileImage(ImageUtil.getCMSImgFullUrl(topic.getMobileImage()));
		topic.setImageMobile(ImageUtil.getCMSImgFullUrl(topic.getImageMobile()));
	}

	@Override
	public List<ItemInventoryDTO> queryItemInventory(List<ItemInventoryDTO> itemInventoryList) {
		if (itemInventoryList != null && itemInventoryList.size() > 0) {
			for (ItemInventoryDTO itemInventoryDTO : itemInventoryList) {
				Long topicId = itemInventoryDTO.getTopicId();
				String sku = itemInventoryDTO.getSkuCode();

				TopicItem itemDO = new TopicItem();
				itemDO.setTopicId(topicId);
				itemDO.setSku(sku);
				itemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
				try {
					List<TopicItem> list = topicItemDao.queryByObject(itemDO);
					if (list != null && list.size() > 0) {
						itemDO = list.get(0);
						if (logger.isDebugEnabled()) {
							logger.info(
									"[TopicService.queryItemInventory]stock location:" + itemDO.getStockLocationId());
							logger.info("[TopicService.queryItemInventory]bonded Area:" + itemDO.getBondedArea());
							logger.info("[TopicService.queryItemInventory]warehouse type:" + itemDO.getWhType());
						}
						itemInventoryDTO.setStorageId(itemDO.getStockLocationId());
						itemInventoryDTO.setBondedArea(itemDO.getBondedArea());
						itemInventoryDTO.setStorageType(itemDO.getWhType());
					} else {

					}
				} catch (Exception e) {
					logger.error("err", e);
				}

			}
			return itemInventoryList;
		} else
			return null;
	}

	/**
	 * 提交库存操作记录
	 *
	 * @param exchange
	 * @param result
	 */
	private void sendInventoryLog(Long tid, int result, String errorCode, String message) {
		try {
			TopicInventoryAccBook accBookDO = getInventoryLog(tid, result, errorCode, message);
			mqUtils.sendMqP2pMessage(TopicMqConstants.MQ_FOR_INVENTORY_LOG_QUERRY_ID, accBookDO);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private TopicInventoryAccBook getInventoryLog(Long tid, int result, String errorCode, String message) {
		TopicInventoryAccBook accBookDO = new TopicInventoryAccBook();
		accBookDO.setBizId(tid);
		accBookDO.setBizType(InnerBizType.TOPIC.ordinal());
		accBookDO.setOperType(InventoryOperType.TERMINATE.ordinal());
		accBookDO.setOperId(0L);
		accBookDO.setOperator("自动");
		accBookDO.setOperResult(result);
		accBookDO.setOperTime(new Date());
		// 预防超长,做截断
		if (!StringUtils.isBlank(errorCode)) {
			String substringValue = errorCode;
			if (errorCode.length() > 130) {
				substringValue = errorCode.substring(0, 130) + "...";
			}
			accBookDO.setFailedCode(substringValue);
		}
		if (!StringUtils.isBlank(message)) {
			String substringValue = message;
			if (message.length() > 130) {
				substringValue = message.substring(0, 130) + "...";
			}
			accBookDO.setFailedMsg(substringValue);
		}
		return accBookDO;
	}

	@Override
	public List<TopicItem> queryTopicItemInfoByTopicIdAndSku(List<TopicItemCartQuery> queryPolicyInfos) {
		List<Long> topicIdList = new ArrayList<Long>();
		Map<String, Object> params = new HashMap<String, Object>();
		List<TopicItem> topicItemList = new ArrayList<TopicItem>();
		for (TopicItemCartQuery cartQuery : queryPolicyInfos) {
			topicIdList.add(cartQuery.getTopicId());
			TopicItem topicItem = new TopicItem();
			topicItem.setTopicId(cartQuery.getTopicId());
			topicItem.setSku(cartQuery.getSku());
			topicItemList.add(topicItem);
		}
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(),
				" id in (" + StringUtil.join(topicIdList, SPLIT_SIGN.COMMA) + ")");
		List<Topic> topicList = topicDao.queryByParam(params);
		topicItemList = topicItemDao.getTopicItemByTopicIdAndSku(topicItemList);
		if (!CollectionUtils.isEmpty(topicItemList) && !CollectionUtils.isEmpty(topicList)) {
			for (TopicItem topicItem : topicItemList) {
				for (Topic topic : topicList) {
					if (topicItem.getTopicId().equals(topic.getId())) {
						topicItem.setTopic(topic);
					}
				}
			}
		}
		return topicItemList;
	}

}

class EndTimeCompareSort implements Comparator<Topic> {

	@Override
	public int compare(Topic o1, Topic o2) {
		if (o1.getEndTime().after(o2.getEndTime()))
			return -1;
		else if (o1.getEndTime().before(o2.getEndTime()))
			return 1;
		else
			return 0;
	}
}

class StartTimeCompareSort implements Comparator<Topic> {

	@Override
	public int compare(Topic o1, Topic o2) {
		if (o1.getStartTime().after(o2.getStartTime()))
			return -1;
		else if (o1.getStartTime().before(o2.getStartTime()))
			return 1;
		else
			return 0;
	}

}