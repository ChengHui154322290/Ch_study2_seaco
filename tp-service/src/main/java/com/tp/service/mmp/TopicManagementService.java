/**
 *
 */
package com.tp.service.mmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.tp.common.util.mmp.BeanUtil;
import com.tp.common.vo.Constant.DEFAULTED;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.mmp.AreaConstant;
import com.tp.common.vo.mmp.ProcessingErrorMessage;
import com.tp.common.vo.mmp.TopicAuditLogConstant;
import com.tp.common.vo.mmp.TopicConstant;
import com.tp.common.vo.mmp.TopicMqConstants;
import com.tp.dao.mmp.PolicyChangeDao;
import com.tp.dao.mmp.PolicyInfoDao;
import com.tp.dao.mmp.RelateChangeDao;
import com.tp.dao.mmp.RelateDao;
import com.tp.dao.mmp.TopicAuditLogDao;
import com.tp.dao.mmp.TopicChangeAuditLogDao;
import com.tp.dao.mmp.TopicChangeDao;
import com.tp.dao.mmp.TopicCouponChangeDao;
import com.tp.dao.mmp.TopicCouponDao;
import com.tp.dao.mmp.TopicDao;
import com.tp.dao.mmp.TopicItemChangeDao;
import com.tp.dao.mmp.TopicItemDao;
import com.tp.dao.mmp.TopicPromoterChangeDao;
import com.tp.dao.mmp.TopicPromoterDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.TopicInventoryExchangeDTO;
import com.tp.dto.mmp.TopicItemInfoDTO;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.dto.mmp.enums.InnerBizType;
import com.tp.dto.mmp.enums.InputSource;
import com.tp.dto.mmp.enums.InventoryOperType;
import com.tp.dto.mmp.enums.LockStatus;
import com.tp.dto.mmp.enums.OperStatus;
import com.tp.dto.mmp.enums.SalesPartten;
import com.tp.dto.mmp.enums.StockStatus;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.dto.mmp.enums.TopicType;
import com.tp.dto.stg.InventoryDto;
import com.tp.dto.stg.query.InventoryDtoQuery;
import com.tp.dto.stg.query.InventoryQuery;
import com.tp.enums.common.PlatformEnum;
import com.tp.exception.ServiceException;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.mmp.PolicyChange;
import com.tp.model.mmp.PolicyInfo;
import com.tp.model.mmp.Relate;
import com.tp.model.mmp.RelateChange;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicAuditLog;
import com.tp.model.mmp.TopicChange;
import com.tp.model.mmp.TopicChangeAuditLog;
import com.tp.model.mmp.TopicCoupon;
import com.tp.model.mmp.TopicCouponChange;
import com.tp.model.mmp.TopicInventoryAccBook;
import com.tp.model.mmp.TopicItem;
import com.tp.model.mmp.TopicItemChange;
import com.tp.model.mmp.TopicPromoter;
import com.tp.model.mmp.TopicPromoterChange;
import com.tp.result.mem.app.ResultMessage;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.mmp.ITopicManagementService;
import com.tp.service.mmp.ITopicOperateLogService;
import com.tp.service.mmp.ITopicRedisService;
import com.tp.service.mmp.mq.MQUtils;
import com.tp.service.stg.IInventoryOperService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.util.StringUtil;

import net.sf.json.JSONArray;

/**
 *
 */
@Service
public class TopicManagementService implements ITopicManagementService {

    private final String MOBILE_SYMBOL = "<img width=\"100%\"";

    private ResultMessage SUCCESS = new ResultMessage(ResultMessage.SUCCESS, "success");

    @SuppressWarnings("unused")
    private ResultMessage FAIL = new ResultMessage(ResultMessage.FAIL, "fail");

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private TopicDao topicDAO;

    @Autowired
    private TopicChangeDao topicChangeDAO;

    @Autowired
    private TopicItemDao topicItemDAO;

    @Autowired
    private TopicCouponDao topicCouponDAO;

    @Autowired
    private TopicCouponChangeDao couponChangeDAO;

    @Autowired
    private TopicItemChangeDao topicItemChangeDAO;

    @Autowired
    private PolicyInfoDao policyDAO;

    @Autowired
    private PolicyChangeDao policyChangeDAO;

    @Autowired
    private RelateDao relateDAO;

    @Autowired
    private RelateChangeDao relateChangeDAO;

    @Autowired
    private TopicAuditLogDao topicAuditLogDAO;
    
    @Autowired
    private TopicPromoterDao topicPromoterDao;
    
    @Autowired
    private TopicPromoterChangeDao topicPromoterChangeDao;

    @Autowired
    private TopicChangeAuditLogDao topicChangeAuditLogDAO;

    @Autowired
    private IInventoryQueryService inventoryQueryService;

    @Autowired
    private IInventoryOperService inventoryOperService;

    @Autowired
    private ITopicRedisService topicRedisService;
    @Autowired
    private IPromoterInfoService promoterInfoService;

    @Autowired
    private MQUtils mqUtils;

    @Autowired
    private ITopicOperateLogService topicOperateLogService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo saveTopic(Topic topic, List<TopicItem> topicItems,
                                Long[] removeTopicItemIds, Long[] relateTopicIds,
                                Long[] removeRelateTopicIds, List<TopicCoupon> topicCoupons,
                                Long[] removeTopicCouponIds, PolicyInfo policy, Long userId,
                                String userName,Integer reserveInventoryFlag) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.info("[saveTopic]start save topic.....");
        }
        if (null == topic || null == topicItems || null == policy) {
            logger.error("[saveTopic]topic is invalid");
            return new ResultInfo<Object>(new FailInfo(ProcessingErrorMessage.VALID_TOPIC_INFO));
        }
        if(TopicType.SUPPLIER_SHOP.ordinal() == topic.getType()){
            topic.setBrandId(null);
            topic.setBrandName(null);
        }else {
            topic.setSupplierId(null);
        }
        topic.setReserveInventoryFlag(reserveInventoryFlag);
        for (TopicItem topicItem : topicItems) {
			topicItem.setReserveInventoryFlag(reserveInventoryFlag);
			if("0".equals(reserveInventoryFlag)){
				topicItem.setLimitTotal(0);
			}
		}

        if (logger.isDebugEnabled()) {
            logger.info("[saveTopic]save waitting lock topic items.....");
        }
        // 生成锁库存列表
        List<TopicInventoryExchangeDTO> lockTopicItems = new ArrayList<TopicInventoryExchangeDTO>();
        
        for (TopicItem TopicItem : topicItems) {
            if (TopicItem == null) {
                continue;
            }
            if (logger.isDebugEnabled()) {
                logger.info("[saveTopic]print spu info....."
                        + TopicItem.getSpu());
            }
            if (null == TopicItem.getId() || 0 == TopicItem.getId()) {
                TopicInventoryExchangeDTO lockItem = new TopicInventoryExchangeDTO();

                lockItem.setAmount(TopicItem.getLimitTotal());
                lockItem.setSku(TopicItem.getSku());
                lockItem.setStatus(OperStatus.NEW.ordinal());
                lockItem.setSupplierId(TopicItem.getSupplierId());
                lockItem.setWarehouseId(TopicItem.getStockLocationId());
                lockItem.setTopicId(TopicItem.getTopicId());
                lockItem.setTopicItemId(TopicItem.getId());
                lockItem.setOperatorId(userId);
                lockItem.setOperatorName(userName);
                lockItem.setOperType(InventoryOperType.NEW);
                lockItem.setBizType(InnerBizType.TOPIC);
                lockTopicItems.add(lockItem);

            }
        }
        if (logger.isDebugEnabled()) {
            logger.info("[saveTopic]save waitting lock topic items end.....");
            // 只检查待锁库存的活动商品
            logger.info("[saveTopic]check storage");
        }
       
        if (logger.isDebugEnabled()) {
            logger.info("[saveTopic]check storage end");
        }

        List<TopicItem> removeTopicItems = new ArrayList<>();
        List<Long> removeItemIds = null;
        if (null != removeTopicItemIds && removeTopicItemIds.length > 0) {
            if (logger.isDebugEnabled()) {
                logger.info("[saveTopic]get remove topic items.....");
            }
            removeItemIds = Arrays.asList(removeTopicItemIds);
            removeTopicItems = new ArrayList<>();
            // 删除 专题活动商品
            if (null != removeTopicItemIds) {
                if (null != removeTopicItemIds && removeTopicItemIds.length > 0) {
                    for (Long topicItemId : removeTopicItemIds) {
                        TopicItem topicItem = new TopicItem();
                        topicItem.setId(topicItemId);
                        removeTopicItems.add(topicItem);
                    }
                }
            }

            if (logger.isDebugEnabled()) {
                logger.info("[saveTopic]get remove topic items end.....");
            }
        }
        if (logger.isDebugEnabled()) {
            logger.info("[saveTopic]save topic and about info.....");
        }
        // 保存数据
       ResultMessage result = this.saveTopicInfo(topic, topicItems, removeTopicItems,
                relateTopicIds, removeRelateTopicIds, topicCoupons,
                removeTopicCouponIds, policy, userId, userName);
        if (logger.isDebugEnabled()) {
            logger.info("[saveTopic]save topic and about info end.....");
        }
        if (null != result && ResultMessage.FAIL == result.getResult()) {
            logger.error(result.getMessage());
            throw new ServiceException(
                    ProcessingErrorMessage.SAVE_TOPIC_FAILD);
        }

        return new ResultInfo();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo<Boolean> submitTopic(Topic topic,
                                  List<TopicItem> topicItems, Long[] removeTopicItemIds,
                                  Long[] relateTopicIds, Long[] removeRelateTopicIds,
                                  List<TopicCoupon> topicCoupons, Long[] removeCouponIds,
                                  PolicyInfo policy, Long userId, String userName,Integer reserveInventoryFlag) throws Exception {
        topic.setStatus(TopicConstant.TOPIC_STATUS_AUDITING);
        if (null == topic || null == topicItems || null == policy) {
            return new ResultInfo<>(new FailInfo(ProcessingErrorMessage.VALID_TOPIC_INFO));
        }
        if(TopicType.SUPPLIER_SHOP.ordinal() == topic.getType()){
            topic.setBrandId(null);
            topic.setBrandName(null);
        }else {
            topic.setSupplierId(null);
        }
        //专场是否预占库存：1预占库存0非预占库存
        topic.setReserveInventoryFlag(reserveInventoryFlag);
        for (TopicItem topicItem : topicItems) {
			topicItem.setReserveInventoryFlag(reserveInventoryFlag);
			if(DEFAULTED.NO.equals(reserveInventoryFlag)){
				topicItem.setLimitTotal(0);
			}
		}
        // 生成锁库存列表
        List<TopicInventoryExchangeDTO> lockTopicItems = new ArrayList<TopicInventoryExchangeDTO>();      
        for (TopicItem TopicItem : topicItems) {
            if (TopicItem == null) continue;
            
            TopicInventoryExchangeDTO lockItem = new TopicInventoryExchangeDTO();
            lockItem.setAmount(TopicItem.getLimitTotal());
            lockItem.setSku(TopicItem.getSku());
            lockItem.setStatus(OperStatus.NEW.ordinal());
            lockItem.setSupplierId(TopicItem.getSupplierId());
            lockItem.setWarehouseId(TopicItem.getStockLocationId());
            lockItem.setTopicId(TopicItem.getTopicId());
            lockItem.setTopicItemId(TopicItem.getId());
            lockItem.setOperatorId(userId);
            lockItem.setOperatorName(userName);
            lockItem.setOperType(InventoryOperType.NEW);
            lockItem.setBizType(InnerBizType.TOPIC);
            lockItem.setTopicInventoryFlag(TopicItem.getReserveInventoryFlag());
            lockTopicItems.add(lockItem);    
        }
        
        if(DEFAULTED.YES.equals(reserveInventoryFlag)){
        	ResultMessage result = checkAvailableStock(lockTopicItems);
        	if (null == result || ResultMessage.FAIL == result.getResult()) {
        		logger.error(result.getMessage());
    			if (StringUtils.isBlank(result.getMessage())) {
    				return new ResultInfo(new FailInfo(ProcessingErrorMessage.CHECK_STORAGE_INVENTORY_FAILD));
    			} else {
    				return new ResultInfo(new FailInfo(result.getMessage()));
    			}
        	}	
        }
       
        List<TopicItem> removeTopicItems = new ArrayList<>();
        List<Long> removeItemIds = null;
        if (null != removeTopicItemIds && removeTopicItemIds.length > 0) { //预占库存的商品需要还库存
            removeItemIds = Arrays.asList(removeTopicItemIds);
            removeTopicItems = new ArrayList<>();
            // 删除 专题活动商品
            if (null != removeTopicItemIds) {
                if (null != removeTopicItemIds && removeTopicItemIds.length > 0) {
                    for (Long topicItemId : removeTopicItemIds) {
                        TopicItem topicItem = new TopicItem();
                        topicItem.setId(topicItemId);
                        removeTopicItems.add(topicItem);
                    }
                }
            }
            if(DEFAULTED.YES.equals(reserveInventoryFlag)){
            	// 删除商品 还库存
                ResultInfo resultInfo = removeTopicItems(removeItemIds, true, userId, userName);
                if (!resultInfo.isSuccess()) {
                    return new ResultInfo(new FailInfo(resultInfo.getMsg()==null ?"还库存失败":resultInfo.getMsg().getMessage()));
                }
            }
            
        }
        // 保存数据
        ResultMessage result = this.saveTopicInfo(topic, topicItems, removeTopicItems,
                relateTopicIds, removeRelateTopicIds, topicCoupons,
                removeCouponIds, policy, userId, userName);
        if (null != result && ResultMessage.FAIL == result.getResult()) {
            logger.error(result.getMessage());
            throw new ServiceException(ProcessingErrorMessage.SAVE_TOPIC_FAILD);
        }        
		// 锁库存
		ResultInfo resultInfo = requestStorageAmount(lockTopicItems);
		if (!resultInfo.isSuccess()) {
			throw new ServiceException(resultInfo.getMsg()==null? "申请库存失败":resultInfo.getMsg().getMessage());
		}
        return new ResultInfo();
    }

    @Override
    public ResultInfo createTopic(Topic topic) {
        try {
            if (topic == null) {
                throw new ServiceException(
                        ProcessingErrorMessage.VALID_TOPIC_INFO);
            }
            if (logger.isDebugEnabled()) {
                logger.info("[createTopic]create topic.....");
            }
            Integer sortIndex = topicDAO.getMaxTopicSortIndex();
            if (null == sortIndex) {
                sortIndex = 0;
            }
            sortIndex += 10;
            Date now = new Date();
            topic.setCreateTime(now);
            topic.setUpdateTime(now);
            topic.setDeletion(DeletionStatus.NORMAL.ordinal());
            topic.setSortIndex(sortIndex);
            topic.setAreaStr(String.valueOf(AreaConstant.AREA_ALL));
            topic.setPlatformStr(String.valueOf(PlatformEnum.ALL.getCode()));
            topic.setLastingType(TopicConstant.TOPIC_DURATIONTYPE_FIX);
            topic.setProgress(TopicConstant.TOPIC_PROCESS_NOTSTART);
            topic.setStatus(TopicConstant.TOPIC_STATUS_EDITING);
            topic.setSalesPartten(SalesPartten.NORMAL.getValue());

            BeanUtil.processNullStringField(topic);
            topic.setPcIndex(0L);
            topic.setWapIndex(0L);
            topic.setAndroidIndex(0L);
            topic.setIosIndex(0L);
            topic.setWxIndex(0L);
            topic.setFreightTemplet(-1);
            topic.setIsSupportSupplierInfo(-1);
            topicDAO.insert(topic);
            if (logger.isDebugEnabled()) {
                logger.info("[createTopic]create topic end.....");
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(
                    ProcessingErrorMessage.SAVE_TOPIC_FAILD);
        }
        return new ResultInfo();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo copyTopic(Long topicId, Long userId, String userName) throws Exception {
        ResultMessage result = SUCCESS;
        try {
            if (topicId == null) {
                throw new ServiceException(
                        ProcessingErrorMessage.VALID_TOPIC_ID);
            }
            if (logger.isDebugEnabled()) {
                logger.info("[copyTopic]copy topic.....");
            }
            Long oldTid = 0L;
            Topic topic = topicDAO.queryById(topicId);
            if (topic == null) {
                throw new ServiceException(
                        ProcessingErrorMessage.VALID_TOPIC_INFO);
            }
            // 主题相关信息
            oldTid = topic.getId();
            if (logger.isDebugEnabled()) {
                logger.info("[copyTopic]copy topic info.....");
            }
            Topic copyTopic = copyTopic(topic, userId, userName);
            if (copyTopic == null) {
                throw new ServiceException(
                        ProcessingErrorMessage.VALID_TOPIC_INFO);
            }
            // BeanUtils.copyProperties(topic, copyTopic);
            if (logger.isDebugEnabled()) {
                logger.info("[copyTopic]save copy topic info.....");
            }
            topicDAO.insert(copyTopic);
            Long tid = copyTopic.getId();
            // 保存新增后的ID
            copyTopic.setId(tid);
            // 限购政策
            PolicyInfo copyPolicy = copyPolicyDO(topic);
            // 专题活动商品清单
            List<TopicItem> copyTopicItems = copyTopicItemDOs(oldTid, tid,
                    userId, userName);
            if (null == copyTopicItems) {
                logger.error("[copyTopic]copy topic items is empty");
                return new ResultInfo(new FailInfo(ResultMessage.FAIL,
                        ProcessingErrorMessage.COPY_TOPIC_ITEM_FAILD));
            }
            List<TopicInventoryExchangeDTO> exchangeList = new ArrayList<TopicInventoryExchangeDTO>();
            for (TopicItem topicItem : copyTopicItems) {
                TopicInventoryExchangeDTO exchangeDTO = new TopicInventoryExchangeDTO();
                exchangeDTO.setAmount(topicItem.getLimitTotal());
                exchangeDTO.setSku(topicItem.getSku());
                exchangeDTO.setWarehouseId(topicItem.getStockLocationId());
                if (topicItem.getId() == null) {
                    exchangeDTO.setTopicItemId(0L);
                } else {
                    exchangeDTO.setTopicItemId(topicItem.getId());
                }
                exchangeDTO.setTopicId(topicItem.getTopicId());
                exchangeDTO.setSupplierId(topicItem.getSupplierId());
                exchangeDTO.setOperatorId(userId);
                exchangeDTO.setOperatorName(userName);
                exchangeDTO.setOperType(InventoryOperType.NEW);
                exchangeDTO.setBizType(InnerBizType.TOPIC);
                exchangeDTO.setTopicInventoryFlag(topic.getReserveInventoryFlag());
                if (topicItem.getId() == null || topicItem.getId() == 0) {
                    exchangeDTO.setStatus(OperStatus.NEW.ordinal());
                } else {
                    exchangeDTO.setStatus(OperStatus.MODIFY.ordinal());
                }
                exchangeList.add(exchangeDTO);
            }
            // 关联活动
            List<Long> relates = this.copyRelateDOs(oldTid);
            if (null == relates) {
                logger.error("[copyTopic]relate info is empty");
                return new ResultInfo(new FailInfo(ResultMessage.FAIL,
                        ProcessingErrorMessage.COPY_TOPIC_RELATE_FAILD));
            }
            // 活动优惠券
            List<TopicCoupon> topicCoupons = this.copyCouponDOs(oldTid);
            if (null == topicCoupons) {
                logger.error("[copyTopic]coupon info is empty");
                return new ResultInfo(new FailInfo(ResultMessage.FAIL,
                        ProcessingErrorMessage.COPY_TOPIC_COUPON_FAILD));
            }
//            ResultInfo resultInfo = requestStorageAmount(exchangeList);
//            if (!resultInfo.isSuccess()) {
//                logger.error(result.getMessage());
//
//                throw new ServiceException(
//                        ProcessingErrorMessage.INCREASE_ITEM_STORAGE_FAILD);
//            }
            
            List<TopicPromoter> topicPromoterList = topicPromoterDao.queryListByTopicId(topicId);
            if(!CollectionUtils.isEmpty(topicPromoterList)){
            	List<Long> promoterIdList = new ArrayList<Long>();
            	for(TopicPromoter topicPromoter:topicPromoterList){
            		promoterIdList.add(topicPromoter.getPromoterId());
            	}
            	topic.setPromoterIdList(promoterIdList);
            }
            // 保存复制的信息
            result = this.saveTopicInfo(copyTopic, copyTopicItems, null,
                    relates.toArray(new Long[relates.size()]), null,
                    topicCoupons, null, copyPolicy, userId, userName);
            if (logger.isDebugEnabled()) {
                logger.info("[saveTopic]copy topic end.....");
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            return new ResultInfo(new FailInfo(ResultMessage.FAIL,
                    ProcessingErrorMessage.COPY_TOPIC_FAILD));
        }
        return new ResultInfo();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo cancelTopic(Long topicId, Long userId, String userName) throws Exception {
        if (null == topicId || 0 == topicId) {
            throw new ServiceException(
                    ProcessingErrorMessage.VALID_TOPIC_ID);
        }
        try {
            // 更新专题活动状态
            Topic topic = updateTopicStatus(topicId,
                    TopicConstant.TOPIC_STATUS_CANCELED);
            // 新增执行动作记录
            this.saveAuditLog(topicId, topic.getStatus(), userId, userName);
//            TopicItem topicItem = new TopicItem();
//            topicItem.setTopicId(topicId);
//            topicItem.setDeletion(DeletionStatus.NORMAL.ordinal());
//            List<TopicItem> topicItems = topicItemDAO.queryByObject(topicItem);
//            if (null != topicItems && topicItems.size() > 0) {
//                // 退还库存
//                ResultMessage result = terminateTopicById(topicId, false,
//                        userId, userName);
//                if (ResultMessage.FAIL == result.getResult()) {
//                    logger.error(result.getMessage());
//                    throw new ServiceException(result.getMessage());
//                }
//            }
            return new ResultInfo();
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(
                    ProcessingErrorMessage.CANCEL_TOPIC_FAILD);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo terminateTopic(Long topicId, Long userId, String userName) throws Exception {
        if (null == topicId || 0 == topicId) {
            throw new ServiceException(ProcessingErrorMessage.VALID_TOPIC_ID);
        }
        try {
            // 更新专题活动状态
            Topic topic = topicDAO.queryById(topicId);
            if (topic == null) {
                throw new ServiceException(ProcessingErrorMessage.TERMINATE_TOPIC_FAILD);
            }
            topic.setStatus(TopicConstant.TOPIC_STATUS_TERMINATION);
            topic.setProgress(TopicConstant.TOPIC_PROCESS_ENDING);
            topic.setUpdateTime(new Date());
            topicDAO.updateNotNullById(topic);
            // 新增执行动作记录
            this.saveAuditLog(topicId, topic.getStatus(), userId, userName);
            // 退还库存
            TopicItem topicItem = new TopicItem();
            topicItem.setTopicId(topicId);
            topicItem.setDeletion(DeletionStatus.NORMAL.ordinal());
            List<TopicItem> topicItems = topicItemDAO
                    .queryByObject(topicItem);
            if (null != topicItems && topicItems.size() > 0) {
                // 退还库存
                ResultMessage result = terminateTopicById(topicId, false,
                        userId, userName);
                if (ResultMessage.FAIL == result.getResult()) {
                    logger.error(result.getMessage());
                    throw new ServiceException(result.getMessage());
                }
            }
            return new ResultInfo();
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(
                    ProcessingErrorMessage.TERMINATE_TOPIC_FAILD);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo approveTopic(Long topicId, Long userId, String userName) {
        if (null == topicId || 0 == topicId) {
            throw new ServiceException(
                    ProcessingErrorMessage.VALID_TOPIC_ID);
        }
        try {
            Topic topic = topicDAO.queryById(topicId);
            if (topic == null) {
                throw new ServiceException(
                        ProcessingErrorMessage.APPROVE_TOPIC_FAILD);
            }
            if (null != topic.getStartTime() && null != topic.getEndTime()) {
                Date now = new Date();
                if (now.after(topic.getStartTime())
                        && now.before(topic.getEndTime())) {
                    topic.setProgress(TopicConstant.TOPIC_PROCESS_PROCESSING);
                }
                if (now.after(topic.getEndTime())) {
                    throw new ServiceException(
                            ProcessingErrorMessage.END_TIME_LT_START_TIME);
                }
            }
            topic.setStatus(TopicConstant.TOPIC_STATUS_AUDITED);
            topic.setUpdateTime(new Date());
            topicDAO.updateNotNullById(topic);
            // 新增执行动作记录
            this.saveAuditLog(topicId, topic.getStatus(), userId, userName);
            // TODO:调整缓存处理
            ResultInfo result = topicRedisService.insertNewPromotion(topicId, TopicConstant.TOPIC_STATUS_AUDITED);
            if (!result.isSuccess()) {
                logger.error(result.getMsg().getMessage());
                throw new ServiceException(ProcessingErrorMessage.APPROVE_TOPIC_FAILD);
            }
            return new ResultInfo();
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(
                    ProcessingErrorMessage.APPROVE_TOPIC_FAILD);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo refuseTopic(Long topicId, Long userId, String userName) {
        if (null == topicId || 0 == topicId) {
            throw new ServiceException(
                    ProcessingErrorMessage.VALID_TOPIC_ID);
        }
        try {
            // 更新专题活动状态
            Topic topic = updateTopicStatus(topicId,
                    TopicConstant.TOPIC_STATUS_REFUSED);
            // 新增执行动作记录
            this.saveAuditLog(topicId, topic.getStatus(), userId, userName);
         // 退还库存
            TopicItem topicItem = new TopicItem();
            topicItem.setTopicId(topicId);
            topicItem.setDeletion(DeletionStatus.NORMAL.ordinal());
            List<TopicItem> topicItems = topicItemDAO
                    .queryByObject(topicItem);
            if (null != topicItems && topicItems.size() > 0) {
                // 退还库存
                ResultMessage result = terminateTopicById(topicId, false,
                        userId, userName);
                if (ResultMessage.FAIL == result.getResult()) {
                    logger.error(result.getMessage());
                    throw new ServiceException(result.getMessage());
                }
            }
            return new ResultInfo();
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(ProcessingErrorMessage.REFUSE_TOPIC_FAILD);
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            throw new ServiceException(ProcessingErrorMessage.REFUSE_TOPIC_FAILD);
		}
    }

    @Override
    public Integer getMaxTopicInfoSortIndex() {
        Integer sortIndex = topicDAO.getMaxTopicSortIndex();
        if (null == sortIndex) {
            return 0;
        }
        return sortIndex;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo requestAddStock(Long topicItemId, int amount,
                                      boolean isTopic, Long userId, String userName) throws Exception {
        try {
            TopicItem itemDO = topicItemDAO.queryById(topicItemId);
            if (null == itemDO || 0 == itemDO.getId()) {
                throw new ServiceException(
                        ProcessingErrorMessage.REQUEST_TOPIC_ITEM_FAILD);
            }
            Integer limitTotal = itemDO.getLimitTotal();
            if (null == limitTotal) {
                limitTotal = 0;
            }
            itemDO.setLimitTotal(amount + limitTotal);
            itemDO.setUpdateTime(new Date());
            // 更新库存信息
            topicItemDAO.updateNotNullById(itemDO);
            List<TopicInventoryExchangeDTO> exchanges = new ArrayList<TopicInventoryExchangeDTO>();
            TopicInventoryExchangeDTO exchange = new TopicInventoryExchangeDTO();
            exchange.setTopicId(itemDO.getTopicId());
            exchange.setTopicItemId(itemDO.getId());
            exchange.setSku(itemDO.getSku());
            exchange.setStatus(OperStatus.MODIFY.ordinal());
            exchange.setSupplierId(itemDO.getSupplierId());
            exchange.setAmount(amount);
            exchange.setWarehouseId(itemDO.getStockLocationId());
            if (isTopic) {
                exchange.setBizType(InnerBizType.TOPIC);
            } else {
                exchange.setBizType(InnerBizType.TOPIC_CHANGE);
            }
            exchange.setOperType(InventoryOperType.EDIT);
            exchange.setTopicInventoryFlag(itemDO.getReserveInventoryFlag());//是否预占库存0否1是
            exchange.setOperatorId(userId);
            exchange.setOperatorName(userName);
            exchanges.add(exchange);
            ResultInfo rm = this.requestStorageAmount(exchanges);
            // 单个商品申请库存，失败不用回滚
            if (!rm.isSuccess()) {
                logger.error(rm.getMsg().getMessage());
                logger.error("[requestAddStock]request add new stock error!");
                throw new ServiceException(rm.getMsg().getMessage());
            }
            return rm;
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(
                    ProcessingErrorMessage.INCREASE_ITEM_STORAGE_FAILD);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo backStock(Long topicItemId, int amount, Long userId,
                                String userName) throws Exception {
        try {
            TopicItem itemDO = topicItemDAO.queryById(topicItemId);
            if (null == itemDO || 0 == itemDO.getId()) {
                throw new ServiceException(
                        ProcessingErrorMessage.REQUEST_TOPIC_ITEM_FAILD);
            }
            Integer limitTotal = itemDO.getLimitTotal();
            if (null == limitTotal) {
                limitTotal = 0;
            }
            // 确保传入参数为负数
            amount = Math.abs(amount) * -1;
            itemDO.setLimitTotal(amount + limitTotal);
            itemDO.setUpdateTime(new Date());
            // 更新库存信息
            topicItemDAO.updateNotNullById(itemDO);
            List<TopicInventoryExchangeDTO> exchanges = new ArrayList<TopicInventoryExchangeDTO>();
            TopicInventoryExchangeDTO exchange = new TopicInventoryExchangeDTO();
            exchange.setTopicId(itemDO.getTopicId());
            exchange.setTopicItemId(itemDO.getId());
            exchange.setSku(itemDO.getSku());
            exchange.setStatus(OperStatus.MODIFY.ordinal());
            exchange.setSupplierId(itemDO.getSupplierId());
            exchange.setAmount(amount);
            exchange.setWarehouseId(itemDO.getStockLocationId());
            exchange.setBizType(InnerBizType.TOPIC);
            exchange.setOperType(InventoryOperType.EDIT);
            exchange.setOperatorId(userId);
            exchange.setOperatorName(userName);
            exchange.setTopicInventoryFlag(itemDO.getReserveInventoryFlag());
            exchanges.add(exchange);
            ResultInfo rm = this.requestStorageAmount(exchanges);
            // 单个商品申请库存，失败不用回滚
            if (!rm.isSuccess()) {
                logger.error(rm.getMsg().getMessage());
                logger.error("[backStock]payback stock error!");
                throw new ServiceException(rm.getMsg().getMessage());
            }
            return new ResultInfo();
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(
                    ProcessingErrorMessage.INCREASE_ITEM_STORAGE_FAILD);
        }
    }


    /**
     * 删除活动商品，并退还库存
     *
     * @param topic
     * @param result
     * @param removeItemIds
     * @return
     */
    private ResultInfo removeTopicItems(List<Long> removeItemIds,
                                        boolean isTopic, Long userId, String userName) {
        try {
            ResultInfo resultInfo = new ResultInfo();
            if (removeItemIds != null && removeItemIds.size() > 0) {
                List<TopicItem> removeItems = topicItemDAO.getTopicItemByIds(
                        removeItemIds);
                List<TopicInventoryExchangeDTO> exchangeDtos = new ArrayList<TopicInventoryExchangeDTO>();
                for (TopicItem TopicItem : removeItems) {
//                    int remainAmount = inventoryQueryService.selectInvetory(
//                            StorageConstant.App.PROMOTION,
//                            String.valueOf(TopicItem.getTopicId()),
//                            TopicItem.getSku());
                    Integer remainAmount = inventoryQueryService.querySalableInventory(App.PROMOTION, String.valueOf(TopicItem.getTopicId()), TopicItem.getSku(),
                			TopicItem.getStockLocationId(), DEFAULTED.YES.equals(TopicItem.getReserveInventoryFlag()));
                    TopicInventoryExchangeDTO exchange = new TopicInventoryExchangeDTO();
                    exchange.setAmount(remainAmount);
                    if (isTopic) {
                        exchange.setBizType(InnerBizType.TOPIC);
                    } else {
                        exchange.setBizType(InnerBizType.TOPIC_CHANGE);
                    }
                    exchange.setOperType(InventoryOperType.DELETE);
                    exchange.setOperatorId(userId);
                    exchange.setOperatorName(userName);
                    exchange.setSku(TopicItem.getSku());
                    exchange.setStatus(OperStatus.DELETE.ordinal());
                    exchange.setSupplierId(TopicItem.getSupplierId());
                    exchange.setTopicId(TopicItem.getTopicId());
                    exchange.setTopicItemId(TopicItem.getId());
                    exchange.setWarehouseId(TopicItem.getStockLocationId());
                    exchange.setTopicInventoryFlag(TopicItem.getReserveInventoryFlag()); //是否预占库存：0否1是
                    exchangeDtos.add(exchange);
                }
                resultInfo = this.requestStorageAmount(exchangeDtos);
            }
            return resultInfo;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultInfo(new FailInfo(ResultMessage.FAIL, "删除商品或退还库存时失败"));
        }
    }

    /**
     * 保存专场活动信息
     *
     * @param topic                待保存专场活动
     * @param topicItems           待保存专场活动商品
     * @param removeTopicItems     待删除专场活动商品(如果没有，传入Null)
     * @param relateTopicIds       待保存关联活动Id
     * @param removeRelateTopicIds 待删除关联活动Id(如果没有，传入Null)
     * @param policy               待保存限购策略信息
     * @return
     */
    private ResultMessage saveTopicInfo(Topic topic,
                                        List<TopicItem> topicItems, List<TopicItem> removeTopicItems,
                                        Long[] relateTopicIds, Long[] removeRelateTopicIds,
                                        List<TopicCoupon> topicCoupons, Long[] removeCouponIds,
                                        PolicyInfo policy, Long userId, String userName) {
        if (logger.isDebugEnabled()) {
            logger.info("[saveTopic]save topic start.....");
        }
        if (null == topic) {
            logger.error("[saveTopicInfo]topic is invalid");
            return new ResultMessage(ResultMessage.FAIL,
                    ProcessingErrorMessage.VALID_TOPIC_INFO);
        }
        Long topicId = topic.getId();
        try {
            // 标记删除 item
            this.removeTopicItemDOs(removeTopicItems);
            // 标记删除 relates
            this.removeTopicRelateDOs(removeRelateTopicIds);
            // 删除 优惠券
            this.removeTopicCouponDOs(removeCouponIds);
            // 提交策略(拆分更新和新增)
            Long policyId = this.saveTopicPolicDO(policy);
            this.saveTopicDO(topic, topicItems, topicCoupons, policyId, userName);
            // 保存relates
            this.saveTopicRelateDOs(relateTopicIds, topicId);
            //保存分销平台列表
            this.saveTopicPromoterList(topicId, topic.getPromoterIdList(), userName);
            if (null != topic && null != topic.getStatus()
                    && TopicConstant.TOPIC_STATUS_EDITING != topic.getStatus()) {
                // 新增执行动作记录
                this.saveAuditLog(topic.getId(), topic.getStatus(), userId,
                        userName);
            }
            if (logger.isDebugEnabled()) {
                logger.info("[saveTopicInfo]save topic end.....");
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            return new ResultMessage(ResultMessage.FAIL,
                    ProcessingErrorMessage.SAVE_TOPIC_FAILD);
        }
        return SUCCESS;
    }

    /**
     * 保存审批记录
     *
     * @param topicId  活动Id
     * @param status   活动状态
     * @param userId   当前编辑人员Id
     * @param userName 当前编辑人员名
     * @throws ServiceException
     */
    private void saveAuditLog(Long topicId, Integer status, Long userId,
                              String userName) {

        TopicAuditLog auditLog = new TopicAuditLog();
        auditLog.setTopicId(topicId);
        auditLog.setAuditId(userId);
        auditLog.setAuditName(userName);
        auditLog.setCreateTime(new Date());
        auditLog.setCreateId(userId);
        if (TopicConstant.TOPIC_STATUS_AUDITING == status) {
            auditLog.setAuditOperation(TopicAuditLogConstant.STATUS_AUDITING_VALUE);
        } else if (TopicConstant.TOPIC_STATUS_AUDITED == status) {
            auditLog.setAuditOperation(TopicAuditLogConstant.STATUS_AUDITED_VALUE);
        } else if (TopicConstant.TOPIC_STATUS_CANCELED == status) {
            auditLog.setAuditOperation(TopicAuditLogConstant.STATUS_CANCELED_VALUE);
        } else if (TopicConstant.TOPIC_STATUS_REFUSED == status) {
            auditLog.setAuditOperation(TopicAuditLogConstant.STATUS_REFUSED_VALUE);
        } else if (TopicConstant.TOPIC_STATUS_TERMINATION == status) {
            auditLog.setAuditOperation(TopicAuditLogConstant.STATUS_TERMINATION_VALUE);
        } else {
            return;
        }
        BeanUtil.processNullField(auditLog);
        topicAuditLogDAO.insert(auditLog);
    }

    /**
     * @param removeRelateTopicIds
     * @throws ServiceException
     */
    private void removeTopicRelateDOs(Long[] removeRelateTopicIds)
            throws ServiceException {
        if (null != removeRelateTopicIds) {
            for (Long rRelateId : removeRelateTopicIds) {
                Relate Relate = new Relate();
                Relate.setId(rRelateId);
                Relate.setDeletion(DeletionStatus.DELETED.ordinal());
                relateDAO.updateNotNullById(Relate);
            }
        }
    }

    private void removeTopicCouponDOs(Long[] removeTopicCouponIds)
            throws ServiceException {
        if (null != removeTopicCouponIds) {
            for (Long couponId : removeTopicCouponIds) {
                topicCouponDAO.deleteById(couponId);
            }
        }
    }

    /**
     * @param removeTopicItems
     * @throws ServiceException
     */
    private void removeTopicItemDOs(List<TopicItem> removeTopicItems)
            throws ServiceException {
        if (null != removeTopicItems) {
            for (TopicItem dTopicItem : removeTopicItems) {
                dTopicItem.setUpdateTime(new Date());
                dTopicItem.setDeletion(DeletionStatus.DELETED.ordinal());
                topicItemDAO.updateNotNullById(dTopicItem);
            }
        }
    }

    /**
     * @param relateTopicIds
     * @param topicId
     * @throws ServiceException
     */
    private void saveTopicRelateDOs(Long[] relateTopicIds, Long topicId)
            throws ServiceException {
        if (null != relateTopicIds) {
            for (Long rTopicId : relateTopicIds) {
                if (rTopicId == null) continue;
                Relate Relate = new Relate();
                Relate.setFirstTopicId(topicId);
                Relate.setSecondTopicId(rTopicId);
                List<Relate> relates = relateDAO.queryByObject(Relate);
                if (null != relates && relates.size() > 0) {
                    throw new ServiceException(
                            String.format(
                                    ProcessingErrorMessage.EXIST_RELATE_TOPIC,
                                    rTopicId));
                }
                Relate.setCreateTime(new Date());
                Relate.setDeletion(DeletionStatus.NORMAL.ordinal());
                relateDAO.insert(Relate);
            }
        }
    }

    /**
     * @param topic
     * @param topicItems
     * @param policyId
     * @throws ServiceException
     */
    private void saveTopicDO(Topic topic, List<TopicItem> topicItems,
                             List<TopicCoupon> topicCoupons, Long policyId, String userName)
            throws ServiceException {
        // 保存专题活动
        topic.setLimitPolicyId(policyId);
        topic.setUpdateTime(new Date());
        // 专为移动端打标
        if (topic.getIntroMobile() != null
                && topic.getIntroMobile().indexOf("<img") > -1) {
            String introMobile = topic.getIntroMobile().replaceAll(
                    MOBILE_SYMBOL, "<img");
            introMobile = introMobile.replaceAll("<img", MOBILE_SYMBOL);
            topic.setIntroMobile(introMobile);
        }
        // 根据活动类型和销售类型调整如何保留图片
        if (topic.getSalesPartten() == SalesPartten.NORMAL.getValue()) {
            topic.setHaitaoImage(" ");
            topic.setMallImage(" ");
        } else if (topic.getSalesPartten() == SalesPartten.FLAGSHIP_STORE
                .getValue()) {
            topic.setHaitaoImage(" ");
            topic.setMallImage(" ");
            topic.setPcImage(" ");
            topic.setPcInterestImage(" ");
            topic.setMobileImage(" ");
        } else if (topic.getSalesPartten() == SalesPartten.XG_STORE.getValue()) {
            topic.setHaitaoImage(" ");
            topic.setPcImage(" ");
            topic.setPcInterestImage(" ");
            topic.setMobileImage(" ");
        } else if (topic.getSalesPartten() == SalesPartten.YTP.getValue()) {
            topic.setMallImage(" ");
            topic.setPcImage(" ");
            topic.setPcInterestImage(" ");
            topic.setMobileImage(" ");
        }

        topicDAO.updateNotNullById(topic);

        // 保存活动商品
        if (null != topicItems) {
            for (TopicItem topicItem : topicItems) {
                if (logger.isDebugEnabled()) {
                    logger.info("[saveTopicDO]print spu info....."
                            + topicItem.getSpu());
                }
                if (null == topicItem.getId() || 0 == topicItem.getId()) {
                    // 默认有库存
                    topicItem.setStock(StockStatus.HAS_STOCK.ordinal());
                    // 新增
                    topicItem.setCreateTime(new Date());
                    topicItem.setCreateUser(userName);
                    topicItem.setUpdateTime(new Date());
                    topicItem.setUpdateUser(userName);
                    topicItem.setDeletion(DeletionStatus.NORMAL.ordinal());

                    topicItem.setItemStatus(-1);
                    topicItem.setItemColor("");
                    topicItem.setItemSize("");
                    topicItem.setDetailId(-1L);
                    topicItem.setLargeCateoryId(-1L);
                    topicItem.setMiddleCategoryId(-1L);
                    topicItem.setPrdid("");
                    topicItem.setListingTime(new Date());
                    topicItem.setApplyAgeId(-1L);

                    topicItemDAO.insert(topicItem);
                } else {
                    // 修改
                    topicItem.setUpdateTime(new Date());
                    topicItemDAO.updateNotNullById(topicItem);
                }
            }
        }
        // 保存活动优惠券
        if (null != topicCoupons) {
            for (TopicCoupon topicCoupon : topicCoupons) {
                if (null == topicCoupon.getId() || 0 == topicCoupon.getId()) {
                    topicCouponDAO.insert(topicCoupon);
                } else {
                    topicCouponDAO.updateNotNullById(topicCoupon);
                }
            }
        }
    }

    /**
     * 保存专题活动策略
     *
     * @param policy
     * @return
     * @throws ServiceException
     */
    private Long saveTopicPolicDO(PolicyInfo policy) throws ServiceException {
        Long policyId = 0L;
        if (null != policy) {
            if (null == policy.getId()) {
                policy.setCreateTime(new Date());
                policyDAO.insert(policy);
                Long policyIdValue = policy.getId();
                if (null != policyIdValue) {
                    policyId = policyIdValue;
                }
            } else {
                policyId = policy.getId();
                policy.setUpdateTime(new Date());
                this.policyDAO.updateNotNullById(policy);
            }
        }
        return policyId;
    }

    /**
     * @param oldTid
     * @return
     */
    private List<Long> copyRelateDOs(Long oldTid) {
        List<Long> relates = new ArrayList<Long>();
        try {
            Relate selectRelate = new Relate();
            selectRelate.setFirstTopicId(oldTid);
            selectRelate.setDeletion(DeletionStatus.NORMAL.ordinal());
            List<Relate> selectRelates = relateDAO
                    .queryByObject(selectRelate);
            if (selectRelates != null && selectRelates.size() > 0) {
                for (Relate relate : selectRelates) {
                    if (relate != null) {
                        relates.add(relate.getSecondTopicId());
                    }
                }
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(
                    ProcessingErrorMessage.COPY_TOPIC_FAILD);
        }
        return relates;
    }

    /**
     * @param oldTid
     * @return
     */
    private List<TopicCoupon> copyCouponDOs(Long oldTid) {
        List<TopicCoupon> coupons = new ArrayList<TopicCoupon>();
        try {
            TopicCoupon selectCoupon = new TopicCoupon();
            selectCoupon.setTopicId(oldTid);
            List<TopicCoupon> selectCoupons = topicCouponDAO
                    .queryByObject(selectCoupon);
            int sortIndex = 10;
            if (selectCoupons != null && selectCoupons.size() > 0) {
                for (TopicCoupon coupon : selectCoupons) {
                    if (coupon != null) {
                        coupon.setSortIndex(sortIndex);
                        coupons.add(this.copyCoupon(coupon));
                        sortIndex += 10;
                    }
                }
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(
                    ProcessingErrorMessage.COPY_TOPIC_FAILD);
        }
        return coupons;
    }

    /**
     * 拷贝活动商品对象
     *
     * @param oldTid
     * @param tid
     * @return
     */
    private List<TopicItem> copyTopicItemDOs(Long oldTid, Long tid,
                                             Long userId, String userName) {
        List<TopicItem> saveItems = null;
        try {
            TopicItem selectItem = new TopicItem();
            selectItem.setDeletion(DeletionStatus.NORMAL.ordinal());
            selectItem.setTopicId(oldTid);
            List<TopicItem> items = topicItemDAO.queryByObject(selectItem);
            // int sortIndex = 10;
            saveItems = new ArrayList<TopicItem>();
            for (TopicItem topicItem : items) {
                TopicItem copyTopicItem = this.copyTopicItem(topicItem, tid,
                        userId);
                // BeanUtils.copyProperties(topicItem, copyTopicItem);
                // 采用原有排序规则
                // if (copyTopicItem != null) {
                // copyTopicItem.setSortIndex(sortIndex);
                saveItems.add(copyTopicItem);
                // sortIndex += 10;
                // }
            }
        } catch (BeansException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(
                    ProcessingErrorMessage.COPY_TOPIC_FAILD);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(
                    ProcessingErrorMessage.COPY_TOPIC_FAILD);
        }
        return saveItems;
    }

    /**
     * 拷贝限购政策
     *
     * @param topic
     * @return
     */
    private PolicyInfo copyPolicyDO(Topic topic) {
        PolicyInfo copyPolicy = null;
        try {
            PolicyInfo policy = policyDAO.queryById(topic.getLimitPolicyId());
            copyPolicy = copyPolicy(policy);
            // BeanUtils.copyProperties(policy, copyPolicy);
        } catch (BeansException e) {
            logger.error(e.getMessage(), e);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }
        return copyPolicy;
    }

    /**
     * 检查传入活动商品是否有可用库存
     *
     * @param exchangeItems 活动商品
     * @return
     */
    public ResultMessage checkAvailableStock(
            List<TopicInventoryExchangeDTO> exchangeItems) {
        if (null == exchangeItems || exchangeItems.size() == 0) {
            return SUCCESS;
        }
        List<InventoryQuery> inventoryQueries = new ArrayList<InventoryQuery>();
        for (TopicInventoryExchangeDTO exchangeItem : exchangeItems) {
            if (exchangeItem.getAmount() < 1) {
                continue;
            }
            InventoryQuery inventoryQuery = new InventoryQuery();
            inventoryQuery.setSku(exchangeItem.getSku());
            inventoryQuery.setWarehouseId(exchangeItem.getWarehouseId());
            inventoryQuery.setSpId(exchangeItem.getSupplierId());
            inventoryQueries.add(inventoryQuery);   
        }
        if (exchangeItems.size() > 0 && inventoryQueries.size() == 0) {
            return SUCCESS;
        }
        List<InventoryDto> inveDtos = inventoryQueryService.queryAvailableInventory(inventoryQueries);
        if (CollectionUtils.isEmpty(inveDtos)) {
            return new ResultMessage(ResultMessage.FAIL,
                    ProcessingErrorMessage.CHECK_STORAGE_INVENTORY_FAILD);
        }
        // 组装库存信息
        Map<String, InventoryDto> inveMap = new HashMap<String, InventoryDto>();
        for (InventoryDto inventoryDto : inveDtos) {
            if (null == inventoryDto) {
                return new ResultMessage(ResultMessage.FAIL,
                        ProcessingErrorMessage.CHECK_STORAGE_INVENTORY_FAILD);
            }
            String sku = inventoryDto.getSku();
            Long warehouseId = inventoryDto.getWarehouseId();
            if (null == warehouseId) {
                return new ResultMessage(ResultMessage.FAIL, String.format(
                        ProcessingErrorMessage.HAVENT_STORAGE_INVENTORY, sku));
            }
            if (inveMap.containsKey(sku + "_" + String.valueOf(warehouseId))) {
                return new ResultMessage(
                        ResultMessage.FAIL,
                        String.format(
                                ProcessingErrorMessage.EXIST_MORE_THAN_ONE_STORAGE_INVENTORY,
                                sku));
            }
            inveMap.put(sku + "_" + String.valueOf(warehouseId), inventoryDto);
        }
        if (null == inveMap || inveMap.size() == 0) {
            return new ResultMessage(ResultMessage.FAIL,
                    ProcessingErrorMessage.CHECK_STORAGE_INVENTORY_FAILD);
        }
        // 遍历活动商品和库存信息，检查是否有库存
        for (TopicInventoryExchangeDTO exchangeItem : exchangeItems) {
            String key = exchangeItem.getSku() + "_" + exchangeItem.getWarehouseId();
            if (!inveMap.containsKey(key)) {
                return new ResultMessage(ResultMessage.FAIL, String.format(
                        ProcessingErrorMessage.HAVENT_STORAGE_INVENTORY,
                        exchangeItem.getSku()));
            }
            InventoryDto inventory = inveMap.get(key);
            if (null != exchangeItem && null != inventory) {
                if (inventory.getInventory() < exchangeItem.getAmount()) {
                    return new ResultMessage(ResultMessage.FAIL, String.format(
                            ProcessingErrorMessage.NO_ENOUGH_STORAGE_INVENTORY,
                            exchangeItem.getSku()));
                }
            } else {
                return new ResultMessage(ResultMessage.FAIL,
                        ProcessingErrorMessage.CHECK_STORAGE_INVENTORY_FAILD);
            }
        }
        return SUCCESS;
    }

    /**
     * 更新专场活动状态
     *
     * @param topicId 活动Id
     * @return
     * @throws ServiceException
     */
    private Topic updateTopicStatus(Long topicId, Integer status)
            throws ServiceException {
        Topic topic = new Topic();
        topic.setId(topicId);
        topic.setStatus(status);
        topic.setUpdateTime(new Date());
        topicDAO.updateNotNullById(topic);
        return topic;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo cancelTopicChange(Long topicChangeId, Long userId,
                                        String userName) throws Exception {
        // TODO:逻辑优化
        try {
            TopicChange changeDO = topicChangeDAO.queryById(topicChangeId);
            if (changeDO == null) {
                logger.error("[cancelTopicChange]change order is null.............");
                throw new ServiceException(ProcessingErrorMessage.INVALID_TOPIC_CHANGE_ID);
            }
            // 处理正常的回滚
            TopicItemChange selectItemDO = new TopicItemChange();
            selectItemDO.setTopicChangeId(topicChangeId);
            selectItemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
            List<TopicItemChange> normalItemChanges = topicItemChangeDAO
                    .queryByObject(selectItemDO);
            // 处理新增删除回滚
            TopicItemChange newDeleteItemDO = new TopicItemChange();
            newDeleteItemDO.setTopicChangeId(topicChangeId);
            newDeleteItemDO.setOperStatus(OperStatus.NEW_DELETE.ordinal());
            List<TopicItemChange> newDeleteItemChanges = topicItemChangeDAO
                    .queryByObject(newDeleteItemDO);
            List<TopicItemChange> itemChanges = new ArrayList<TopicItemChange>();
            for (TopicItemChange TopicItemChange : newDeleteItemChanges) {
                itemChanges.add(TopicItemChange);
            }
            for (TopicItemChange TopicItemChange : normalItemChanges) {
                itemChanges.add(TopicItemChange);
            }
            List<Long> removeItemIds = new ArrayList<Long>();
            List<TopicInventoryExchangeDTO> exchangesDTOs = new ArrayList<TopicInventoryExchangeDTO>();
            for (TopicItemChange TopicItemChange : itemChanges) {
                TopicItem updateItem = new TopicItem();
                if (TopicItemChange.getOperStatus() == OperStatus.NEW
                        .ordinal()
                        || TopicItemChange.getOperStatus() == OperStatus.NEW_DELETE
                        .ordinal()) {
                    removeItemIds.add(TopicItemChange.getChangeTopicItemId());
                    // 删除新增的活动商品
                    updateItem.setDeletion(DeletionStatus.DELETED.ordinal());
                } else if (TopicItemChange.getOperStatus() == OperStatus.MODIFY
                        .ordinal()) {
                    TopicInventoryExchangeDTO exchange = new TopicInventoryExchangeDTO();
                    Integer paybackLimit = TopicItemChange
                            .getSourceLimitTotal()
                            - TopicItemChange.getLimitTotal();
                    exchange.setTopicItemId(TopicItemChange
                            .getChangeTopicItemId());
                    exchange.setAmount(paybackLimit);
                    exchange.setSku(TopicItemChange.getSku());
                    exchange.setStatus(OperStatus.MODIFY.ordinal());
                    exchange.setSupplierId(TopicItemChange.getSupplierId());
                    exchange.setTopicId(changeDO.getChangeTopicId());
                    exchange.setWarehouseId(TopicItemChange
                            .getStockLocationId());
                    exchange.setOperatorId(userId);
                    exchange.setOperatorName(userName);
                    exchange.setBizType(InnerBizType.TOPIC_CHANGE);
                    exchange.setOperType(InventoryOperType.ROLL_BACK);
                    exchange.setTopicChangeId(topicChangeId);
                    exchange.setTopicItemChangeId(TopicItemChange.getId());
                    /** 活动是否预占库存：0否1是 */
                    exchange.setTopicInventoryFlag(changeDO.getReserveInventoryFlag());
                    exchangesDTOs.add(exchange);
                    updateItem.setLimitTotal(TopicItemChange
                            .getSourceLimitTotal());
                }
                updateItem.setId(TopicItemChange.getChangeTopicItemId());
                updateItem.setUpdateTime(new Date());
                topicItemDAO.updateNotNullById(updateItem);
            }
            // 新增商品返还库存
            if (removeItemIds != null && removeItemIds.size() > 0) {
                ResultInfo result = removeTopicItems(removeItemIds, false,
                        userId, userName);
                if (!result.isSuccess()) {
                    throw new ServiceException(
                            ProcessingErrorMessage.ROLLBACK_STORAGE_INVENTORY_FAILD);
                }
            }
            // 标记活动商品表锁定商品为解锁
            TopicItem lockDO = new TopicItem();
            lockDO.setLockStatus(LockStatus.LOCK.ordinal());
            lockDO.setTopicId(changeDO.getChangeTopicId());
            lockDO.setDeletion(DeletionStatus.NORMAL.ordinal());
            List<TopicItem> lockDOs = topicItemDAO.queryByObject(lockDO);
            for (TopicItem TopicItem : lockDOs) {
                TopicItem updateDO = new TopicItem();
                updateDO.setId(TopicItem.getId());
                updateDO.setLockStatus(LockStatus.UNLOCK.ordinal());
                updateDO.setUpdateTime(new Date());
                topicItemDAO.updateNotNullById(updateDO);
            }
            // 修改变更单状态
            changeDO.setStatus(TopicStatus.CANCELED.ordinal());
            changeDO.setUpdateTime(new Date());
            changeDO.setUpdateUser(userName);
            topicChangeDAO.updateNotNullById(changeDO);
            this.saveChangeAuditLog(topicChangeId,
                    TopicStatus.CANCELED.ordinal(), userId, userName);
            // 修改商品退还库存
            if (exchangesDTOs != null && exchangesDTOs.size() > 0) {
                this.payBackExistItemInventory(exchangesDTOs);
            }
//             ResultInfo rm = topicRedisService.insertNewPromotion(changeDO.getChangeTopicId(), -1);
//             if (!rm.isSuccess()) {
//             logger.error("[cancelTopicChange]sync topic info to redis error........");
//             throw new ServiceException(ProcessingErrorMessage.SYNC_TOPIC_INFO_TO_REDIS_ERROR);
//             }
            this.saveChangeAuditLog(topicChangeId,
                    TopicStatus.CANCELED.ordinal(), userId, userName);
            return new ResultInfo();
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(
                    ProcessingErrorMessage.COMMON_ERROR);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo approveTopicChange(Long topicChangeId, Long userId,
                                         String userName) throws ServiceException {
        ResultMessage rm = SUCCESS;

        TopicChange topicChange = topicChangeDAO.queryById(topicChangeId);
        if (topicChange.getStatus() != TopicStatus.AUDITING.ordinal()) {
            logger.error("[approveTopicChange]status of approve change order is not auditing....");
            throw new ServiceException(ProcessingErrorMessage.APPROVE_TOPIC_CHANGE_STATUS_ERROR);
        }
        Long topicId = topicChange.getChangeTopicId();
        rm = approveTopicChangeWithTransactional(topicChange, userId, userName);
        if (ResultMessage.SUCCESS == rm.getResult()) {
            {
                ResultInfo resultInfo = topicRedisService.insertNewPromotion(topicId, -1);
                if (!resultInfo.isSuccess()) {
                    logger.error("[approveTopicChange]sync topic info to redis error........");
                    throw new ServiceException(
                            ProcessingErrorMessage.SYNC_TOPIC_INFO_TO_REDIS_ERROR);
                } else {
                    logger.info("[approveTopicChange]send mq msg for cms start............");
                    boolean sendSuccess = mqUtils.sendMqP2pMessage(TopicMqConstants.MQ_FOR_CMS_QUERRY_ID, topicId);
                    logger.info("[approveTopicChange]send mq msg for cms end............");
                    if (!sendSuccess) {
                        logger.error("[approveTopicChange]send mq msg for cms error............");
                    }
                }
            }
            return new ResultInfo();
        } else {
            return new ResultInfo(new FailInfo(rm.getMessage(), ResultMessage.FAIL));
        }

    }

    private ResultMessage approveTopicChangeWithTransactional(TopicChange topicChange, Long userId, String userName) {

        Long topicId = topicChange.getChangeTopicId();
        Long topicChangeId = topicChange.getId();
        Topic topic = topicDAO.queryById(topicId);
        if (topic == null || topic.getStatus() != TopicStatus.PASSED.ordinal() || topic.getDeletion() == DeletionStatus.DELETED.ordinal()) {
            logger.error("[approveTopicChange]original topic info is error(deleted,unaudited,info is null)....");
            return new ResultMessage(ResultMessage.FAIL, ProcessingErrorMessage.MERGE_TOPIC_STATUS_ERROR);
        }
        /**
         * 审批通过后，处理以下事情 1. 删除活动商品退库 2. 合并4个数据对象
         */
        // 合并Item
        TopicItemChange itemChange = new TopicItemChange();
        itemChange.setTopicChangeId(topicChangeId);
        List<TopicItemChange> changeDOs = topicItemChangeDAO.queryByObject(itemChange);
        List<Long> removeItemIds = new ArrayList<Long>();
        Map<TopicItem,TopicItemChange> meta = new HashMap<>();
        for (TopicItemChange topicItemChange : changeDOs) {
            Long topicItemId = topicItemChange.getChangeTopicItemId();
            TopicItem itemDO = topicItemDAO.queryById(topicItemId);

            if (itemDO == null) {
                logger.error("[approveTopicChange]topic item change order,topic item info invalid....."
                        + topicItemChange.getId());
                throw new ServiceException(ProcessingErrorMessage.INVALID_TOPIC_ITEM_INFO);
            }

            TopicItem topicItemOLD = cloneBean(itemDO,TopicItem.class);

            itemDO.setSortIndex(topicItemChange.getSortIndex());
            itemDO.setLimitAmount(topicItemChange.getLimitAmount());
            itemDO.setLimitTotal(topicItemChange.getLimitTotal());
            itemDO.setTopicPrice(topicItemChange.getTopicPrice());
            itemDO.setSalePrice(topicItemChange.getSalePrice());
            itemDO.setTopicImage(topicItemChange.getTopicImage());
            itemDO.setName(topicItemChange.getName());
            itemDO.setLockStatus(LockStatus.UNLOCK.ordinal());
            itemDO.setPurchaseMethod(topicItemChange.getPurchaseMethod());
            if (topicItemChange.getOperStatus() == OperStatus.DELETE.ordinal()
                    || topicItemChange.getOperStatus() == OperStatus.NEW_DELETE.ordinal()) {
                removeItemIds.add(topicItemId);
                itemDO.setDeletion(DeletionStatus.DELETED.ordinal());
            }
            itemDO.setUpdateTime(new Date());
            topicItemDAO.updateNotNullById(itemDO);
            meta.put(topicItemOLD,topicItemChange);
        }
        // 合并Policy
        Long policyId = topic.getLimitPolicyId();
        PolicyInfo policy = policyDAO.queryById(policyId);
        if (policy == null) {
            logger.error("[approveTopicChange]original policy info is error(info is null)....");
            throw new ServiceException(ProcessingErrorMessage.POLICY_INFO_IS_INVALID);
        }
        Long changePolicyId = topicChange.getLimitPolicyId();
        PolicyChange pChange = policyChangeDAO.queryById(changePolicyId);
        if (pChange == null) {
            logger.error("[approveTopicChange]policy change order is error(info is null)....");
            throw new ServiceException(ProcessingErrorMessage.POLICY_CHANGE_INFO_IS_INVALID);
        }
        PolicyInfo policyOLD = cloneBean(policy,PolicyInfo.class);
        this.MergePolicyInfo(policyId, policy, pChange);
        policyDAO.updateNotNullById(policy);
        // 合并关联活动
        this.MergeTopicRelateInfo(topicId, topicChangeId);
        // 合并优惠券信息
        this.MergeTopicCouponInfo(topicId, topicChangeId);
        // 合并分销渠道
        mergeTopicPromoterChangeList(topicChangeId,topicId);
        // 合并活动信息
        Topic topicOLD = cloneBean(topic,Topic.class);
        this.MergeTopicInfo(userId, userName, topicChange, topic);
        // 修改变更单状态
        topicChange.setStatus(TopicStatus.PASSED.ordinal());
        topicChange.setUpdateTime(new Date());
        topicChange.setUpdateUser(userName);
        topicChangeDAO.updateNotNullById(topicChange);
        this.saveChangeAuditLog(topicChangeId, TopicStatus.PASSED.ordinal(), userId, userName);
        // 删除商品 还库存
        ResultInfo result = removeTopicItems(removeItemIds, false, userId, userName);
        if (!result.isSuccess()) {
            logger.error("还库存失败:result" + JSON.toJSONString(result));
            if (result.getMsg() != null && StringUtils.isNotBlank(result.getMsg().getMessage())) {
                throw new ServiceException(result.getMsg().getMessage());
            }
            throw new ServiceException(ProcessingErrorMessage.PAYBACK_STORAGE_INVENTORY_FAILD);
        }
         this.saveChangeAuditLog(topicChangeId,
        TopicStatus.PASSED.ordinal(), userId, userName);

        topicOperateLogService.saveTopicOperateDetailLog(meta,topic, topicOLD,policy,policyOLD,userId,userName);
        return SUCCESS;

    }


    private <T>T cloneBean(Object o,Class<T> clazz) {
        try {
            return (T)BeanUtils.cloneBean(o);
        } catch (Exception e) {
         logger.error("CLONE_BEAN_ERROR:",e);
            return null;
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo refuseTopicChange(Long topicChangeId, Long userId,
                                        String userName) throws ServiceException {
        try {
            TopicChange topicChange = topicChangeDAO.queryById(
                    topicChangeId);
            if (topicChange.getStatus() != TopicStatus.AUDITING.ordinal()) {
                logger.error("[refuseTopicChange]status of refuse change order is not auditing....");
                return new ResultInfo(new FailInfo(ResultMessage.FAIL,
                        ProcessingErrorMessage.REFUSE_TOPIC_CHANGE_STATUS_ERROR));
            }
            topicChange.setStatus(TopicStatus.EDITING.ordinal());
            topicChange.setUpdateTime(new Date());
            topicChange.setUpdateUser(userName);
            topicChangeDAO.updateNotNullById(topicChange);
            this.saveChangeAuditLog(topicChangeId,
                    TopicStatus.REFUSED.ordinal(), userId, userName);
            return new ResultInfo();
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            return new ResultInfo(new FailInfo(ResultMessage.FAIL,
                    ProcessingErrorMessage.REFUSE_TOPIC_CHANGE_FAILD));
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo saveTopicChange(TopicChange topicChange,
                                      List<TopicItemChange> topicItemChanges,
                                      Long[] removeTopicItemChangeIds, Long[] relateTopicIds,
                                      Long[] removeRelateTopicIds,
                                      List<TopicCouponChange> topicCoupons,
                                      Long[] removeTopicCouponIds, PolicyChange policy, Long userId,
                                      String userName) throws Exception {
        if (null == topicChange || null == topicItemChanges || null == policy) {
            return new ResultInfo(new FailInfo(ResultMessage.FAIL,ProcessingErrorMessage.INVALID_TOPIC_CHANGE_INFO));
        }
        // 生成锁库存列表
        ArrayList<Long> removeTopicItemIdsTemp = new ArrayList<Long>();
        for (Long removeId : removeTopicItemChangeIds) {
            removeTopicItemIdsTemp.add(removeId);
        }
        // 合并活动商品列表与被删除活动商品(界面删除和数据库中保存为已删除的活动商品)
        ArrayList<TopicInventoryExchangeDTO> changeInventoryDtos = this.mergeTopicItemChange(topicItemChanges, removeTopicItemIdsTemp,
                        topicChange.getChangeTopicId(), topicChange.getId(),
                        userId, userName,topicChange.getReserveInventoryFlag());
        //ArrayList<TopicInventoryExchangeDTO> changeInventoryDtos = new ArrayList<TopicInventoryExchangeDTO>();
        if (removeTopicItemIdsTemp != null && removeTopicItemIdsTemp.size() > 0) {
            removeTopicItemIdsTemp.toArray(removeTopicItemChangeIds);
        } else {
            removeTopicItemChangeIds = null;
        }

        // 加工活动商品信息
        for (TopicItemChange TopicItemChange : topicItemChanges) {
            if (TopicItemChange == null) {
                continue;
            }
            if (TopicItemChange.getOperStatus() == null) {
                TopicItemChange.setOperStatus(OperStatus.NEW.ordinal());
            }
            if (null == TopicItemChange.getId()|| 0 == TopicItemChange.getId()) {
                TopicInventoryExchangeDTO lockItem = new TopicInventoryExchangeDTO();
                lockItem.setStatus(OperStatus.NEW.ordinal());
                lockItem.setAmount(TopicItemChange.getLimitTotal());
                lockItem.setSku(TopicItemChange.getSku());
                lockItem.setSupplierId(TopicItemChange.getSupplierId());
                lockItem.setTopicItemId(TopicItemChange.getChangeTopicItemId());
                lockItem.setTopicId(topicChange.getChangeTopicId());
                lockItem.setWarehouseId(TopicItemChange.getStockLocationId());
                lockItem.setBizType(InnerBizType.TOPIC_CHANGE);
                lockItem.setOperatorId(userId);
                lockItem.setOperatorName(userName);
                lockItem.setOperType(InventoryOperType.NEW);
                lockItem.setTopicInventoryFlag(topicChange.getReserveInventoryFlag());//是否预占库存：0否1是
                changeInventoryDtos.add(lockItem);
            }
        }

        if(DEFAULTED.YES.equals(topicChange.getReserveInventoryFlag())){
        	ResultMessage result = checkAvailableStock(changeInventoryDtos);
            if (null == result || ResultMessage.FAIL == result.getResult()) {
                if (StringUtils.isBlank(result.getMessage())) {
                    return new ResultInfo(new FailInfo(ResultMessage.FAIL,
                            ProcessingErrorMessage.CHECK_STORAGE_INVENTORY_FAILD));
                } else {
                    return new ResultInfo(new FailInfo(ResultMessage.FAIL,
                            result.getMessage()));
                }
            }	
        }  
        List<TopicItemChange> removeTopicItems = new ArrayList<TopicItemChange>();
        if (null != removeTopicItemChangeIds&& removeTopicItemChangeIds.length > 0) {
            removeTopicItems = new ArrayList<TopicItemChange>();
            // 删除 专题活动商品
            if (null != removeTopicItemChangeIds) {
                if (null != removeTopicItemChangeIds
                        && removeTopicItemChangeIds.length > 0) {
                    for (Long topicItemId : removeTopicItemChangeIds) {
                        TopicItemChange topicItem = new TopicItemChange();
                        topicItem.setId(topicItemId);
                        removeTopicItems.add(topicItem);
                    }
                }
            }
        }

        // 保存数据
        this.saveTopicChangeInfo(topicChange, topicItemChanges,
                removeTopicItems, relateTopicIds, removeRelateTopicIds,
                topicCoupons, removeTopicCouponIds, policy, userId, userName);
        // 锁库存
        ResultInfo resultInfo = this.requestStorageAmount(changeInventoryDtos);
        if (!resultInfo.isSuccess()  ) {
        	throw new ServiceException(resultInfo.getMsg()==null? "申请库存失败":resultInfo.getMsg().getMessage());
        }
        return new ResultInfo();
    }

    @Override
    @Transactional
    public ResultInfo submitTopicChange(TopicChange topicChange,
                                        List<TopicItemChange> topicItemChanges,
                                        Long[] removeTopicItemChangeIds, Long[] relateTopicIds,
                                        Long[] removeRelateTopicIds,
                                        List<TopicCouponChange> topicCoupons,
                                        Long[] removeTopicCouponIds, PolicyChange policy, Long userId,
                                        String userName) throws Exception {
    	logger.info("[submitTopicChange]submit topic change order.....");
        topicChange.setStatus(TopicConstant.TOPIC_STATUS_AUDITING);
        return saveTopicChange(topicChange, topicItemChanges, removeTopicItemChangeIds, relateTopicIds, removeRelateTopicIds, topicCoupons, removeTopicCouponIds, policy, userId, userName);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Long generateTopicChange(TopicChange topicChange,
                                    List<TopicItemChange> topicItemChanges,
                                    List<TopicCouponChange> topicCouponChanges,
                                    List<Long> relateTopicIds, 
                                    PolicyChange policy, 
                                    List<TopicPromoterChange> topicPromoterChangeList,
                                    Long userId,
                                    String userName) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.info("[generateTopicChange]start generate topic change order.....");
        }

            if (topicChange.getChangeTopicId() == null) {
                logger.error("[generateTopicChange]topic id is null");
                throw new ServiceException(
                        ProcessingErrorMessage.VALID_TOPIC_ID);
            }
            TopicChange selectDO = new TopicChange();
            selectDO.setChangeTopicId(topicChange.getChangeTopicId());
            Integer unprocessingCount = topicChangeDAO.getUnprocessingChangeOrderCount(topicChange.getChangeTopicId());
            if (unprocessingCount > 0) {
                logger.error("[generateTopicChange]topic change is null or has unaudited or uncancled change order,topic id:"
                        + topicChange.getChangeTopicId());
                return null;
            }
            // 生成限购策略
            policyChangeDAO.insert(policy);
            Long policyId = policy.getId();
            // 保存活动变更单
            topicChange.setLimitPolicyId(policyId);

            topicChangeDAO.insert(topicChange);
            Long topicChangeId = topicChange.getId();
            // 保存活动商品变更单
            for (TopicItemChange topicItemChange : topicItemChanges) {
                topicItemChange.setTopicChangeId(topicChangeId);
                topicItemChangeDAO.insert(topicItemChange);
            }
            // 生成优惠券
            for (TopicCouponChange couponChange : topicCouponChanges) {
                couponChange.setId(null);
                couponChange.setTopicChangeId(topicChangeId);
                couponChangeDAO.insert(couponChange);
            }
            // 生成关联活动
            if (relateTopicIds != null && relateTopicIds.size() > 0) {
                for (Long topicId : relateTopicIds) {
                    RelateChange changeDo = new RelateChange();
                    changeDo.setDeletion(DeletionStatus.NORMAL.ordinal());
                    changeDo.setTopicChangeId(topicChangeId);
                    changeDo.setFirstTopicId(topicChange.getChangeTopicId());
                    changeDo.setSecondTopicId(topicId);
                    changeDo.setCreateTime(new Date());
                    changeDo.setUpdateTime(new Date());
                    BeanUtil.processNullField(changeDo);
                    relateChangeDAO.insert(changeDo);
                }
            }
            
            if(!CollectionUtils.isEmpty(topicPromoterChangeList)){
            	for(TopicPromoterChange topicPromoterChange:topicPromoterChangeList){
            		topicPromoterChange.setTopicChangeId(topicChangeId);
            	}
            	topicPromoterChangeDao.batchInsert(topicPromoterChangeList);
            }
            if (logger.isDebugEnabled()) {
                logger.info("[generateTopicChange]end generate topic change order..........................");
            }
            return topicChangeId;

    }

    private ResultMessage saveTopicChangeInfo(TopicChange topicChange,
                                              List<TopicItemChange> topicItemChanges,
                                              List<TopicItemChange> removeTopicItemChanges,
                                              Long[] relateTopicChangeIds, Long[] removeRelateTopicChangeIds,
                                              List<TopicCouponChange> topicCoupons,
                                              Long[] removeCouponChangeIds, PolicyChange policy, Long userId,
                                              String userName) {
        if (logger.isDebugEnabled()) {
            logger.info("[saveTopicChangeInfo]save topic start.....");
        }
        if (null == topicChange) {
            return new ResultMessage(ResultMessage.FAIL,
                    ProcessingErrorMessage.VALID_TOPIC_INFO);
        }
        Long topicChangeId = topicChange.getId();
        Long changeTopicId = topicChange.getChangeTopicId();
        try {
            // 标记删除 item
            this.removeTopicItemChangeDOs(removeTopicItemChanges);
            // 标记删除 relates
            this.removeTopicRelateChangeDOs(removeRelateTopicChangeIds);
            // 删除 优惠券
            this.removeTopicCouponChangeDOs(removeCouponChangeIds);
            // 提交策略(拆分更新和新增)
            Long policyId = this.saveTopicPolicChangeDO(policy);
            this.saveTopicChangeDO(topicChange, topicItemChanges, policyId,
                    userId, userName);
            // 保存relates
            this.saveTopicRelateChangeDOs(relateTopicChangeIds, topicChangeId,
                    changeTopicId);
            // 保存优惠券
            this.saveTopicCouponChangeDOS(topicCoupons);
            
            saveTopicPromoterChangeList(topicChange.getId(),topicChange.getChangeTopicId(),topicChange.getPromoterIdList(),userName);
            if (null != topicChange
                    && null != topicChange.getStatus()
                    && TopicConstant.TOPIC_STATUS_EDITING != topicChange
                    .getStatus()) {
                // 新增执行动作记录
                this.saveChangeAuditLog(topicChange.getId(),
                        topicChange.getStatus(), userId, userName);
            }
            if (logger.isDebugEnabled()) {
                logger.info("[saveTopicChangeInfo]save topic end.....");
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            return new ResultMessage(ResultMessage.FAIL,
                    ProcessingErrorMessage.SAVE_TOPIC_FAILD);
        }
        return SUCCESS;
    }

    /**
     * 删除标记为删除的优惠券
     *
     * @param removeTopicCouponIds
     * @throws ServiceException
     */
    private void removeTopicCouponChangeDOs(Long[] removeTopicCouponIds)
            throws ServiceException {
        if (null != removeTopicCouponIds) {
            for (Long couponId : removeTopicCouponIds) {
                couponChangeDAO.deleteById(couponId);
            }
        }
    }

    /**
     * 变更单保存审批记录
     *
     * @param topicChangeId 活动变更单Id
     * @param status        活动状态
     * @param userId        当前编辑人员Id
     * @param userName      当前编辑人员名
     * @throws ServiceException
     */
    private void saveChangeAuditLog(Long topicChangeId, Integer status,
                                    Long userId, String userName) throws ServiceException {

        TopicChangeAuditLog auditLog = new TopicChangeAuditLog();
        auditLog.setTopicChangeId(topicChangeId);
        auditLog.setAuditId(userId);
        auditLog.setAuditName(userName);
        auditLog.setCreateTime(new Date());
        auditLog.setCreateId(userId);
        if (TopicConstant.TOPIC_STATUS_AUDITING == status) {
            auditLog.setAuditOperation(TopicAuditLogConstant.STATUS_AUDITING_VALUE);
        } else if (TopicConstant.TOPIC_STATUS_AUDITED == status) {
            auditLog.setAuditOperation(TopicAuditLogConstant.STATUS_AUDITED_VALUE);
        } else if (TopicConstant.TOPIC_STATUS_CANCELED == status) {
            auditLog.setAuditOperation(TopicAuditLogConstant.STATUS_CANCELED_VALUE);
        } else if (TopicConstant.TOPIC_STATUS_REFUSED == status) {
            auditLog.setAuditOperation(TopicAuditLogConstant.STATUS_REFUSED_VALUE);
        } else {
            return;
        }
        BeanUtil.processNullField(auditLog);
        topicChangeAuditLogDAO.insert(auditLog);
    }

    /**
     * 保存变更单中的商品信息
     *
     * @param removeTopicItems
     * @throws ServiceException
     */
    private void removeTopicItemChangeDOs(
            List<TopicItemChange> removeTopicItemChanges) throws ServiceException {
        if (null != removeTopicItemChanges) {
            for (TopicItemChange dTopicItemChange : removeTopicItemChanges) {
                TopicItemChange deleteDO = topicItemChangeDAO.queryById(
                        dTopicItemChange.getId());
                if (deleteDO == null) {
                    continue;
                }
                dTopicItemChange.setUpdateTime(new Date());
                if (deleteDO.getOperStatus() != null
                        && deleteDO.getOperStatus() == OperStatus.NEW.ordinal()) {
                    dTopicItemChange.setOperStatus(OperStatus.NEW_DELETE
                            .ordinal());
                } else {
                    dTopicItemChange.setOperStatus(OperStatus.DELETE.ordinal());
                }
                dTopicItemChange.setDeletion(DeletionStatus.DELETED.ordinal());
                topicItemChangeDAO.updateNotNullById(dTopicItemChange);
            }
        }
    }

    /**
     * 保存变更单关联活动
     *
     * @throws ServiceException
     */
    private void removeTopicRelateChangeDOs(Long[] removeRelateTopicChangeIds)
            throws ServiceException {
        if (null != removeRelateTopicChangeIds) {
            for (Long rRelateId : removeRelateTopicChangeIds) {
                RelateChange RelateChange = new RelateChange();
                RelateChange.setId(rRelateId);
                RelateChange.setDeletion(DeletionStatus.DELETED.ordinal());
                this.relateChangeDAO.updateNotNullById(RelateChange);
            }
        }
    }

    /**
     * 保存活动变更单策略
     *
     * @param policy
     * @return
     * @throws ServiceException
     */
    private Long saveTopicPolicChangeDO(PolicyChange policy)
            throws ServiceException {
        Long policyId = 0L;
        if (null != policy) {
            policyId = policy.getId();
            policy.setUpdateTime(new Date());
            this.policyChangeDAO.updateNotNullById(policy);
        }
        return policyId;
    }

    /**
     * @throws ServiceException
     */
    private void saveTopicRelateChangeDOs(Long[] relateTopicChangeIds,
                                          Long topicChangeId, Long changeTopicId) throws ServiceException {
        if (null != relateTopicChangeIds) {
            for (Long rTopicId : relateTopicChangeIds) {
                if (rTopicId == null) continue;
                RelateChange RelateChange = new RelateChange();
                RelateChange.setTopicChangeId(topicChangeId);
                RelateChange.setFirstTopicId(changeTopicId);
                RelateChange.setSecondTopicId(rTopicId);
                RelateChange.setDeletion(DeletionStatus.NORMAL.ordinal());
                List<RelateChange> relates = relateChangeDAO
                        .queryByObject(RelateChange);
                if (null != relates && relates.size() > 0) {
                    throw new ServiceException(
                            String.format(
                                    ProcessingErrorMessage.EXIST_RELATE_TOPIC,
                                    rTopicId));
                }
                RelateChange.setCreateTime(new Date());
                RelateChange.setDeletion(DeletionStatus.NORMAL.ordinal());
                relateChangeDAO.insert(RelateChange);
            }
        }
    }

    /**
     * 保存活动优惠券变更单
     *
     * @param topicCoupons
     * @throws ServiceException
     */
    private void saveTopicCouponChangeDOS(List<TopicCouponChange> topicCoupons)
            throws ServiceException {
        // 保存优惠券
        if (topicCoupons != null && topicCoupons.size() > 0) {
            for (TopicCouponChange topicCouponChange : topicCoupons) {
                if (topicCouponChange == null || topicCouponChange.getCouponId() == null) continue;
                if (topicCouponChange.getId() != null
                        && topicCouponChange.getId() != 0) {
                    couponChangeDAO.updateNotNullById(topicCouponChange);
                } else {
                    couponChangeDAO.insert(topicCouponChange);
                }
            }
        }
    }

    /**
     * 保存活动变更单信息
     *
     * @param topicChange
     * @param topicItemChanges
     * @param policyId
     * @throws ServiceException
     */
    private void saveTopicChangeDO(TopicChange topicChange,
                                   List<TopicItemChange> topicItemChanges, Long policyId,
                                   Long userId, String userName) throws ServiceException {
        try {
            Long topicId = topicChange.getChangeTopicId();
            // 保存活动商品
            if (null != topicItemChanges) {
                Integer sortIndex = topicItemDAO.getMaxTopicItemSortIndex(
                        topicId);
                for (TopicItemChange topicItemChange : topicItemChanges) {
                    if (logger.isDebugEnabled()) {
                        logger.info("[saveTopicChangeDO]start save item change order ....."
                                + topicItemChange.getSpu());
                    }
                    if (null == topicItemChange.getId()
                            || 0 == topicItemChange.getId()) {
                        if (sortIndex == null) {
                            sortIndex = 0;
                        }
                        sortIndex += 10;
                        TopicItemInfoDTO itemConvert = new TopicItemInfoDTO();
                        itemConvert.setTopicItemChange(topicItemChange);
                        TopicItem itemDO = itemConvert.getTopicItem();
                        itemDO.setSortIndex(sortIndex);
                        itemDO.setTopicId(topicId);
                        itemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
                        itemDO.setCreateTime(new Date());
                        itemDO.setCreateUser(userName);
                        itemDO.setUpdateTime(new Date());
                        itemDO.setUpdateUser(userName);
                        itemDO.setCreateUser(userName);
                        itemDO.setLockStatus(LockStatus.LOCK.ordinal());
                        itemDO.setStock(StockStatus.HAS_STOCK.ordinal());
                        itemDO.setItemColor("");
                        itemDO.setItemSize("");
                        itemDO.setPrdid("");
                        itemDO.setLargeCateoryId(-1L);
                        itemDO.setMiddleCategoryId(-1L);
                        itemDO.setListingTime(new Date());
                        itemDO.setDetailId(-1L);
                        itemDO.setItemStatus(-1);
                        /** 活动是否预占库存：0否1是 */
                        itemDO.setReserveInventoryFlag(topicChange.getReserveInventoryFlag());
                        topicItemDAO.insert(itemDO);
                        Long itemId = itemDO.getId();
                        topicItemChange.setChangeTopicItemId(itemId);
                        topicItemChange.setSourceLimitTotal(itemDO
                                .getLimitTotal());
                        // 默认有库存
                        topicItemChange.setStock(StockStatus.HAS_STOCK
                                .ordinal());
                        // 新增
                        topicItemChange.setCreateTime(new Date());
                        topicItemChange.setCreateUser(userName);
                        topicItemChange.setUpdateTime(new Date());
                        topicItemChange.setUpdateUser(userName);
                        topicItemChange.setDeletion(DeletionStatus.NORMAL.ordinal());
                        topicItemChange.setItemColor(itemDO.getItemColor());
                        topicItemChange.setItemStatus(itemDO.getItemStatus());
                        topicItemChange.setDetailId(itemDO.getDetailId());
                        topicItemChange.setListingTime(itemDO.getListingTime());
                        topicItemChange.setPrdid(itemDO.getPrdid());
                        topicItemChange.setLargeCateoryId(itemDO.getLargeCateoryId());
                        topicItemChange.setMiddleCategoryId(itemDO.getMiddleCategoryId());
                        topicItemChange.setItemSize(itemDO.getItemSize());

                        topicItemChangeDAO.insert(topicItemChange);
                    } else {
                        // 修改
                        topicItemChange.setUpdateTime(new Date());
                        topicItemChangeDAO.updateNotNullById(topicItemChange);
                    }
                }
            }

            // 保存专题活动
            topicChange.setLimitPolicyId(policyId);
            topicChange.setUpdateTime(new Date());
            // 专为移动端打标
            if (topicChange.getIntroMobile() != null
                    && topicChange.getIntroMobile().indexOf("<img") > -1) {
                String introMobile = topicChange.getIntroMobile().replaceAll(
                        MOBILE_SYMBOL, "<img");
                introMobile = introMobile.replaceAll("<img", MOBILE_SYMBOL);
                topicChange.setIntroMobile(introMobile);
            }
            // 根据活动类型和销售类型调整如何保留图片
            if (topicChange.getSalesPartten() == SalesPartten.NORMAL.getValue()) {
                topicChange.setHaitaoImage(" ");
                topicChange.setMallImage(" ");
            } else if (topicChange.getSalesPartten() == SalesPartten.FLAGSHIP_STORE
                    .getValue()) {
                topicChange.setHaitaoImage(" ");
                topicChange.setMallImage(" ");
                topicChange.setPcImage(" ");
                topicChange.setPcInterestImage(" ");
                topicChange.setMobileImage(" ");
            } else if (topicChange.getSalesPartten() == SalesPartten.XG_STORE
                    .getValue()) {
                topicChange.setHaitaoImage(" ");
                topicChange.setPcImage(" ");
                topicChange.setPcInterestImage(" ");
                topicChange.setMobileImage(" ");
            } else if (topicChange.getSalesPartten() == SalesPartten.YTP
                    .getValue()) {
                topicChange.setMallImage(" ");
                topicChange.setPcImage(" ");
                topicChange.setPcInterestImage(" ");
                topicChange.setMobileImage(" ");
            }
            topicChangeDAO.updateNotNullById(topicChange);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(
                    ProcessingErrorMessage.SAVE_TOPIC_CHANGE_FAILD);

        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo requestAddStockForChangeOrder(Long topicItemChangeId,
                                                    int amount, Long userId, String userName)
            throws Exception {
        try {
            TopicItemChange itemChange = topicItemChangeDAO.queryById(
                    topicItemChangeId);
            if (null == itemChange || 0 == itemChange.getId()
                    || itemChange.getTopicChangeId() == null) {
                throw new ServiceException(
                        ProcessingErrorMessage.REQUEST_TOPIC_ITEM_FAILD);
            }
            // 获取变更单中活动的序号
            TopicChange topicChange = topicChangeDAO.queryById(
                    itemChange.getTopicChangeId());
            if (topicChange == null || topicChange.getChangeTopicId() == null) {
                throw new ServiceException(
                        ProcessingErrorMessage.REQUEST_TOPIC_ITEM_FAILD);
            }
            Integer limitTotal = itemChange.getLimitTotal();
            if (null == limitTotal) {
                limitTotal = 0;
            }
            itemChange.setLimitTotal(amount + limitTotal);
            itemChange.setUpdateTime(new Date());
            // 更新库存信息
            topicItemChangeDAO.updateNotNullById(itemChange);
            List<TopicInventoryExchangeDTO> exchanges = new ArrayList<TopicInventoryExchangeDTO>();
            TopicInventoryExchangeDTO exchange = new TopicInventoryExchangeDTO();
            exchange.setTopicId(topicChange.getChangeTopicId());
            exchange.setTopicItemId(itemChange.getChangeTopicItemId());
            exchange.setSku(itemChange.getSku());
            exchange.setStatus(OperStatus.MODIFY.ordinal());
            exchange.setSupplierId(itemChange.getSupplierId());
            exchange.setAmount(amount);
            exchange.setWarehouseId(itemChange.getStockLocationId());
            exchange.setBizType(InnerBizType.TOPIC_CHANGE);
            exchange.setOperatorId(userId);
            exchange.setOperatorName(userName);
            exchange.setOperType(InventoryOperType.EDIT);
            exchange.setTopicInventoryFlag(topicChange.getReserveInventoryFlag());//是否预占库存0否1是
            exchanges.add(exchange);
            ResultInfo rm = this.requestStorageAmount(exchanges);
            // 单个商品申请库存，不需要回滚
            if (!rm.isSuccess()) {
                logger.error(rm.getMsg().getMessage());
                logger.error("[requestAddStockForChangeOrder]request add new stock error!");
                throw new ServiceException(rm.getMsg().getMessage());
            }
            return rm;
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(
                    ProcessingErrorMessage.INCREASE_ITEM_STORAGE_FAILD);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo backStockForChangeOrder(Long topicItemChangeId,
                                              int amount, Long userId, String userName)
            throws Exception {
        try {
            TopicItemChange itemChange = topicItemChangeDAO.queryById(
                    topicItemChangeId);
            if (null == itemChange || 0 == itemChange.getId()
                    || itemChange.getTopicChangeId() == null) {
                throw new ServiceException(
                        ProcessingErrorMessage.REQUEST_TOPIC_ITEM_FAILD);
            }
            // 获取变更单中活动的序号
            TopicChange topicChange = topicChangeDAO.queryById(
                    itemChange.getTopicChangeId());
            if (topicChange == null || topicChange.getChangeTopicId() == null) {
                throw new ServiceException(
                        ProcessingErrorMessage.REQUEST_TOPIC_ITEM_FAILD);
            }
            Integer limitTotal = itemChange.getLimitTotal();
            if (null == limitTotal) {
                limitTotal = 0;
            }
            itemChange.setLimitTotal(amount + limitTotal);
            itemChange.setUpdateTime(new Date());
            // 更新库存信息
            topicItemChangeDAO.updateNotNullById(itemChange);
            List<TopicInventoryExchangeDTO> exchanges = new ArrayList<TopicInventoryExchangeDTO>();
            TopicInventoryExchangeDTO exchange = new TopicInventoryExchangeDTO();
            exchange.setTopicId(topicChange.getChangeTopicId());
            exchange.setTopicItemId(itemChange.getChangeTopicItemId());
            exchange.setSku(itemChange.getSku());
            exchange.setStatus(OperStatus.MODIFY.ordinal());
            exchange.setSupplierId(itemChange.getSupplierId());
            exchange.setAmount(amount);
            exchange.setWarehouseId(itemChange.getStockLocationId());
            exchange.setOperatorId(userId);
            exchange.setOperatorName(userName);
            exchange.setOperType(InventoryOperType.EDIT);
            exchange.setBizType(InnerBizType.TOPIC_CHANGE);
            exchanges.add(exchange);
            exchange.setTopicInventoryFlag(topicChange.getReserveInventoryFlag());//是否预占库存:0否1是
            ResultInfo rm = this.requestStorageAmount(exchanges);
            // 单个商品申请库存，不需要回滚
            if (!rm.isSuccess()) {
                logger.error(rm.getMsg().getMessage());
                logger.error("[backStockForChangeOrder]payback stock error!");
                throw new ServiceException(rm.getMsg().getMessage());
            }
            return rm;
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(
                    ProcessingErrorMessage.INCREASE_ITEM_STORAGE_FAILD);
        }
    }

    /**
     * 退还变更单申请的库存
     *
     * @param exchangesDTOs
     */
    private void payBackExistItemInventory(
            List<TopicInventoryExchangeDTO> exchangesDTOs) throws Exception {
        for (TopicInventoryExchangeDTO exchange : exchangesDTOs) {
            if (exchange.getAmount() > 0) {
                this.requestAddStockForChangeOrder(
                        exchange.getTopicItemChangeId(), exchange.getAmount(),
                        exchange.getOperatorId(), exchange.getOperatorName());
            } else if (exchange.getAmount() < 0) {
                this.backStockForChangeOrder(exchange.getTopicItemChangeId(),
                        exchange.getAmount(), exchange.getOperatorId(),
                        exchange.getOperatorName());
            }
        }
    }

    private ResultMessage terminateTopicById(Long tid, boolean isCopy,
                                             Long userId, String userName) throws Exception {
        try {
            TopicItem selectTopicItem = new TopicItem();
            selectTopicItem.setTopicId(tid);
            selectTopicItem.setDeletion(DeletionStatus.NORMAL.ordinal());
            List<TopicItem> items = topicItemDAO
                    .queryByObject(selectTopicItem);
            List<TopicInventoryExchangeDTO> exchanges = new ArrayList<TopicInventoryExchangeDTO>();
            for (TopicItem TopicItem : items) {
                TopicInventoryExchangeDTO exchangeDTO = new TopicInventoryExchangeDTO();
                exchangeDTO.setTopicId(TopicItem.getTopicId());
                exchangeDTO.setTopicItemId(TopicItem.getId());
                exchangeDTO.setBizType(InnerBizType.TOPIC);
                exchangeDTO.setOperatorId(userId);
                exchangeDTO.setOperatorName(userName);
                exchangeDTO.setSku(TopicItem.getSku());
                exchangeDTO.setStatus(OperStatus.TERMINATE.ordinal());
                exchangeDTO.setWarehouseId(TopicItem.getStockLocationId());
                exchangeDTO.setSupplierId(TopicItem.getSupplierId());
                if (isCopy) {
                    exchangeDTO.setOperType(InventoryOperType.ROLL_BACK_TERM);
                } else {
                    exchangeDTO.setOperType(InventoryOperType.TERMINATE);
                }
//                int remainAmount = inventoryQueryService.selectInvetory(
//                        StorageConstant.App.PROMOTION,
//                        String.valueOf(exchangeDTO.getTopicId()),
//                        exchangeDTO.getSku());
                Integer remainAmount = inventoryQueryService.querySalableInventory(App.PROMOTION, String.valueOf(TopicItem.getTopicId()), TopicItem.getSku(),
            			TopicItem.getStockLocationId(), DEFAULTED.YES.equals(TopicItem.getReserveInventoryFlag()));
                exchangeDTO.setAmount(remainAmount);
                exchanges.add(exchangeDTO);
                if(DEFAULTED.YES.equals(TopicItem.getReserveInventoryFlag())){
                	terminateTopicStorageAmount(tid, exchanges);
                }
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            return new ResultMessage(ResultMessage.FAIL,
                    ProcessingErrorMessage.COMMON_ERROR);
        }
        return SUCCESS;
    }

    @SuppressWarnings("unused")
    private String getTopicItemKey(TopicItem topicItem) {
        // TODO:替换为唯一Key
        if (topicItem == null) {
            return "";
        }
        return topicItem.getSku() + "-" + topicItem.getStockLocationId();
    }

    private String getTopicItemChangeKey(TopicItemChange topicItemChange) {
        // TODO:替换为唯一Key
        if (topicItemChange == null) {
            return "";
        }
        return topicItemChange.getSku() + "-"
                + topicItemChange.getStockLocationId();
    }

    // #region 库存操作

    /**
     * 锁库存 app = App.PROMOTION bizId = topicId spid - 0 = xg 必须有仓库
     *
     * @param exchangeItems
     * @return
     */
    public ResultInfo requestStorageAmount(List<TopicInventoryExchangeDTO> exchangeItems) throws Exception {

    	if(exchangeItems == null || CollectionUtils.isEmpty(exchangeItems)){
    		return new ResultInfo<>();
    	}
        Map<String, ResultInfo<Boolean>> resultMap = new HashMap<>();
        List<TopicInventoryExchangeDTO> rollbackExchanges = new ArrayList<TopicInventoryExchangeDTO>();
        try {        
        	for (TopicInventoryExchangeDTO inventoryExchange : exchangeItems) {
                String topicId = inventoryExchange.getTopicId().toString();
                String sku = inventoryExchange.getSku();
                int amount = inventoryExchange.getAmount();
                Long spId = inventoryExchange.getSupplierId();
                Long warehouseId = inventoryExchange.getWarehouseId();
                Integer status = inventoryExchange.getStatus();
                App app = App.PROMOTION;
                boolean isPreOccupy = DEFAULTED.YES.equals(inventoryExchange.getTopicInventoryFlag());
                if (status == OperStatus.NEW.ordinal()) { //新增
                	ResultInfo<Boolean> resultInfo = null;
                    if (null != warehouseId) {
                        // TODO：重构时，换成sku集合
                        resultInfo = inventoryOperService.requestInventory(app, topicId, sku, amount, spId, warehouseId, isPreOccupy);
                    } else {
                        resultInfo = new ResultInfo(new FailInfo(ResultMessage.FAIL, String.format(ProcessingErrorMessage.HAVENT_STORAGE_INVENTORY, sku)));
                    }
                    resultMap.put(topicId + "_" + sku, resultInfo);
                } else if (status == OperStatus.RECOVER.ordinal() || status == OperStatus.MODIFY.ordinal()) { //修改
                	ResultInfo<Boolean> resultInfo = null;
                    if (amount > 0) {
                        resultInfo = inventoryOperService.requestInventory(app, topicId, sku, amount, spId, warehouseId, isPreOccupy);
                    } else if (amount < 0 && isPreOccupy) {
                        resultInfo = inventoryOperService.backRequestSpecInventoryBySku(app, topicId, sku, Math.abs(amount));
                    }
                    resultMap.put(topicId + "_" + sku, resultInfo);
                } else if (status == OperStatus.DELETE.ordinal() || status == OperStatus.NEW_DELETE.ordinal()) { //删除
                	ResultInfo<Boolean> resultInfo = null;
                    if(warehouseId!=null && StringUtils.isNoneBlank(sku) && isPreOccupy){
                        resultInfo = inventoryOperService.backRequestInventoryBySkuAndWarehouse(app, topicId, sku,warehouseId);
                    }else if (StringUtils.isBlank(sku) && isPreOccupy) {
                        resultInfo = inventoryOperService.backRequestInventory(app, topicId);
                    } else if(isPreOccupy){
                        resultInfo = inventoryOperService.backRequestInventoryBySku(app, topicId, sku);
                    }
                    resultMap.put(topicId + "_" + sku, resultInfo);
                }
                // 生成回滚库存申请列表，以备部分库存申请失败，全体回滚 
                TopicInventoryExchangeDTO rollbackExchange = new TopicInventoryExchangeDTO();
                com.tp.util.BeanUtil.copyProperties(rollbackExchange, inventoryExchange);
                rollbackExchange.setOperType(InventoryOperType.ROLL_BACK);
                rollbackExchanges.add(rollbackExchange);
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            this.paybackStorageAmount(rollbackExchanges);
            return new ResultInfo(new FailInfo(ResultMessage.FAIL,
                    ProcessingErrorMessage.DISTRIB_STORAGE_INVENTORY_FAILD));
        }
        StringBuffer sbBuffer = new StringBuffer();
        Iterator<Map.Entry<String, ResultInfo<Boolean>>> iterator = resultMap.entrySet().iterator();
        while(iterator.hasNext()){
        	Map.Entry<String, ResultInfo<Boolean>> entry = iterator.next();
        	if(entry.getValue() != null && !entry.getValue().isSuccess()){
        		sbBuffer.append(entry.getValue().getMsg().getDetailMessage()).append(";");
        	}
        }
        if(StringUtil.isNotEmpty(sbBuffer.toString())){
        	return new ResultInfo<>(new FailInfo(sbBuffer.toString()));
        }
        return new ResultInfo<>();
//    
//        ResultInfo resultInfo = new ResultInfo();
//        List<TopicInventoryExchangeDTO> rollbackExchanges = new ArrayList<TopicInventoryExchangeDTO>();
//        try {
//            if (null != exchangeItems && exchangeItems.size() > 0) {
//
//                for (TopicInventoryExchangeDTO inventoryExchange : exchangeItems) {
//                    Long topicId = inventoryExchange.getTopicId();
//                    String sku = inventoryExchange.getSku();
//                    int amount = inventoryExchange.getAmount();
//                    Long spId = inventoryExchange.getSupplierId();
//                    Long warehouseId = inventoryExchange.getWarehouseId();
//                    if (inventoryExchange.getStatus() == OperStatus.NEW
//                            .ordinal()) {
//                        if (null != warehouseId) {
//                            // TODO：重构时，换成sku集合
//                            resultInfo = inventoryOperService.requestInventory(StorageConstant.App.PROMOTION, String.valueOf(topicId), sku, amount, spId, warehouseId);
//                        } else {
//                            resultInfo = new ResultInfo(new FailInfo(ResultMessage.FAIL, String.format(ProcessingErrorMessage.HAVENT_STORAGE_INVENTORY, sku)));
//                        }
//                    } else if (inventoryExchange.getStatus() == OperStatus.RECOVER.ordinal() || inventoryExchange.getStatus() == OperStatus.MODIFY.ordinal()) {
//                        if (amount > 0) {
//                            resultInfo = inventoryOperService.requestInventory(StorageConstant.App.PROMOTION, String.valueOf(topicId), sku, amount, spId, warehouseId);
//                        } else if (amount < 0) {
//                            resultInfo = inventoryOperService.backRequestSpecInventoryBySku(StorageConstant.App.PROMOTION, String.valueOf(topicId), sku, Math.abs(amount));
//                        }
//                    } else if (inventoryExchange.getStatus() == OperStatus.DELETE.ordinal() || inventoryExchange.getStatus() == OperStatus.NEW_DELETE.ordinal()) {
//                        if(warehouseId!=null && StringUtils.isNoneBlank(sku)){
//                            resultInfo = inventoryOperService.backRequestInventoryBySkuAndWarehouse(StorageConstant.App.PROMOTION, String.valueOf(topicId), sku,warehouseId);
//                        }else if (StringUtils.isBlank(sku)) {
//                            resultInfo = inventoryOperService.backRequestInventory(StorageConstant.App.PROMOTION, String.valueOf(topicId));
//                        } else {
//                            resultInfo = inventoryOperService.backRequestInventoryBySku(StorageConstant.App.PROMOTION, String.valueOf(topicId), sku);
//                        }
//                    }
//                    // 生成回滚库存申请列表，以备部分库存申请失败，全体回滚
//                    TopicInventoryExchangeDTO rollbackExchange = new TopicInventoryExchangeDTO();
//                    rollbackExchange.setAmount(inventoryExchange.getAmount());
//                    rollbackExchange.setBizType(inventoryExchange.getBizType());
//                    rollbackExchange.setOperatorId(inventoryExchange
//                            .getOperatorId());
//                    rollbackExchange.setOperatorName(inventoryExchange
//                            .getOperatorName());
//                    rollbackExchange.setOperType(InventoryOperType.ROLL_BACK);
//                    rollbackExchange.setSku(inventoryExchange.getSku());
//                    rollbackExchange.setStatus(inventoryExchange.getStatus());
//                    rollbackExchange.setSupplierId(inventoryExchange
//                            .getSupplierId());
//                    rollbackExchange.setTopicChangeId(inventoryExchange
//                            .getTopicChangeId());
//                    rollbackExchange.setTopicId(inventoryExchange.getTopicId());
//                    rollbackExchange.setTopicItemChangeId(inventoryExchange
//                            .getTopicItemChangeId());
//                    rollbackExchange.setTopicItemId(inventoryExchange
//                            .getTopicItemId());
//                    rollbackExchange.setWarehouseId(inventoryExchange
//                            .getWarehouseId());
//                    rollbackExchanges.add(rollbackExchange);
//                }
//            }
//        } catch (ServiceException e) {
//            logger.error(e.getMessage(), e);
//            this.paybackStorageAmount(rollbackExchanges);
//            return new ResultInfo(new FailInfo(ResultMessage.FAIL,
//                    ProcessingErrorMessage.DISTRIB_STORAGE_INVENTORY_FAILD));
//        }
//        return resultInfo;
    }

    /**
     * 还库存
     *
     * @return
     */
    private ResultInfo paybackStorageAmount(
            List<TopicInventoryExchangeDTO> paybackExchanges) throws Exception {
        TopicInventoryExchangeDTO currentExchange = null;
        boolean sendComplete = false;
        try {
            for (TopicInventoryExchangeDTO exchange : paybackExchanges) {
            	boolean isPreOccupy = DEFAULTED.YES.equals(exchange.getTopicInventoryFlag());
            	App app = App.PROMOTION;
            	String topicId = exchange.getTopicId().toString();
            	sendComplete = false;
            	//非预占库存不需要归还
            	if(false == isPreOccupy) continue;
            	
                ResultInfo<Boolean> resultInfo = inventoryOperService.backRequestInventoryBySku(app,topicId,exchange.getSku());
                if (resultInfo != null) {
                    this.sendPaybackInventoryLog(exchange,
                            resultInfo.isSuccess(),
                            resultInfo.getMsg().getCode().toString(),
                            resultInfo.getMsg().getMessage());
                    sendComplete = true;
                    currentExchange = exchange;
                    if (!resultInfo.isSuccess()) {
                        logger.error(resultInfo.getMsg().getMessage());
                        return resultInfo;
                    }
                }
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            if (!sendComplete && currentExchange != null) {
                this.sendPaybackInventoryLog(currentExchange,
                        false,
                        ProcessingErrorMessage.REMOTE_ERROR_CODE,
                        String.format(ProcessingErrorMessage.REMOTE_ERROR_MSG,
                                "storage-remote"));
            }
            return new ResultInfo(new FailInfo(ResultMessage.FAIL,
                    ProcessingErrorMessage.ROLLBACK_STORAGE_INVENTORY_FAILD));
        }
        return new ResultInfo();
    }

    /**
     * 根据TopicId还库存
     *
     * @param paybackSKUs 商品sku
     * @param topicId     专场活动Id
     * @return
     */
    private ResultMessage terminateTopicStorageAmount(Long topicId,
                                                      List<TopicInventoryExchangeDTO> exchangeDtos) throws Exception {
        try {
            ResultInfo resultInfo = inventoryOperService
                    .backRequestInventory(StorageConstant.App.PROMOTION,
                            String.valueOf(topicId));
            if (resultInfo != null) {
                for (TopicInventoryExchangeDTO exchange : exchangeDtos) {
                    sendTerminateInventoryLog(exchange,
                            resultInfo.isSuccess(),
                            "",
                            "");
                }
                if (!resultInfo.isSuccess()) {
                    logger.error(resultInfo.getMsg().getMessage());
                    return new ResultMessage(ResultMessage.FAIL,
                            resultInfo.getMsg().getMessage());
                }
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            for (TopicInventoryExchangeDTO exchange : exchangeDtos) {
                sendTerminateInventoryLog(exchange,
                        false,
                        ProcessingErrorMessage.REMOTE_ERROR_CODE,
                        String.format(ProcessingErrorMessage.REMOTE_ERROR_MSG,
                                "storage-remote"));
            }
            return new ResultMessage(ResultMessage.FAIL,
                    ProcessingErrorMessage.ROLLBACK_STORAGE_INVENTORY_FAILD);
        }
        return SUCCESS;
    }

    /**
     * 提交库存操作记录
     *
     * @param exchange
     * @param result
     */
    private void sendInventoryLog(TopicInventoryExchangeDTO exchange,
                                  boolean result, String errorCode, String message) {
        try {
            TopicInventoryAccBook accBookDO = getInventoryLog(exchange,
                    result, errorCode, message);
            mqUtils.sendMqP2pMessage(TopicMqConstants.MQ_FOR_INVENTORY_LOG_QUERRY_ID, accBookDO);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 提交回退库存操作记录
     *
     * @param exchange
     * @param result
     */
    private void sendPaybackInventoryLog(TopicInventoryExchangeDTO exchange,
                                         boolean result, String errorCode, String message) {
        try {
            TopicInventoryAccBook accBookDO = getInventoryLog(exchange,
                    result, errorCode, message);
            accBookDO.setOperType(InventoryOperType.ROLL_BACK.getKey());
            mqUtils.sendMqP2pMessage(TopicMqConstants.MQ_FOR_INVENTORY_LOG_QUERRY_ID, accBookDO);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 提交终止库存操作记录
     *
     * @param exchange
     * @param result
     */
    private void sendTerminateInventoryLog(TopicInventoryExchangeDTO exchange,
                                           boolean result, String errorCode, String message) {
        try {
            TopicInventoryAccBook accBookDO = getInventoryLog(exchange,
                    result, errorCode, message);
            mqUtils.sendMqP2pMessage(TopicMqConstants.MQ_FOR_INVENTORY_LOG_QUERRY_ID, accBookDO);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private TopicInventoryAccBook getInventoryLog(
            TopicInventoryExchangeDTO exchange, boolean result, String errorCode,
            String message) {
        if (exchange != null) {
            TopicInventoryAccBook accBookDO = new TopicInventoryAccBook();
            accBookDO.setBizId(exchange.getTopicId());
            accBookDO.setItemId(exchange.getTopicItemId());
            accBookDO.setBizType(exchange.getBizType().getKey());
            accBookDO.setOperType(exchange.getOperType().getKey());
            accBookDO.setWarehouseId(exchange.getWarehouseId());
            accBookDO.setSku(exchange.getSku());
            accBookDO.setSpId(exchange.getSupplierId());
            accBookDO.setOperAmount(exchange.getAmount());
            accBookDO.setOperId(exchange.getOperatorId());
            accBookDO.setOperator(exchange.getOperatorName());
            accBookDO.setOperResult(result ? 1 : 2);
            accBookDO.setOperId(exchange.getOperatorId());
            accBookDO.setOperator(exchange.getOperatorName());
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
        return null;
    }

    // #end region

    // #region 合并信息操作

    /**
     * 合并变更单关联活动
     *
     * @param topicId
     * @param topicChangeId
     * @throws ServiceException
     */
    private void MergeTopicRelateInfo(Long topicId, Long topicChangeId) {
        Relate selectRelate = new Relate();
        selectRelate.setFirstTopicId(topicId);
        selectRelate.setDeletion(DeletionStatus.NORMAL.ordinal());
        List<Relate> relateDos = relateDAO.queryByObject(selectRelate);
        for (Relate Relate : relateDos) {
            Relate.setDeletion(DeletionStatus.DELETED.ordinal());
            relateDAO.updateNotNullById(Relate);
        }
        RelateChange selectRelateChange = new RelateChange();
        selectRelateChange.setTopicChangeId(topicChangeId);
        selectRelateChange.setDeletion(DeletionStatus.NORMAL.ordinal());
        List<RelateChange> relateChangeDos = relateChangeDAO.queryByObject(selectRelateChange);
        for (RelateChange RelateChange : relateChangeDos) {
            Relate updateRelate = new Relate();
            updateRelate.setDeletion(DeletionStatus.NORMAL.ordinal());
            updateRelate.setCreateTime(new Date());
            updateRelate.setFirstTopicId(topicId);
            updateRelate.setSecondTopicId(RelateChange.getSecondTopicId());
            relateDAO.insert(updateRelate);
        }
    }

    /**
     * 合并变更单优惠券信息至活动优惠券信息中
     *
     * @param topicId
     * @param topicChangeId
     * @throws ServiceException
     */
    private void MergeTopicCouponInfo(Long topicId, Long topicChangeId) {
        TopicCoupon selectCoupon = new TopicCoupon();
        selectCoupon.setTopicId(topicId);
        List<TopicCoupon> coupons = topicCouponDAO.queryByObject(selectCoupon);
        for (TopicCoupon TopicCoupon : coupons) {
            if (TopicCoupon != null) {
                topicCouponDAO.deleteById(TopicCoupon.getId());
            }
        }
        TopicCouponChange selectCoupChange = new TopicCouponChange();
        selectCoupChange.setTopicChangeId(topicChangeId);
        List<TopicCouponChange> newCoupons = couponChangeDAO.queryByObject(selectCoupChange);
        for (TopicCouponChange couponChange : newCoupons) {
            TopicCoupon couponDO = new TopicCoupon();
            couponDO.setCouponId(couponChange.getCouponId());
            couponDO.setCouponImage(couponChange.getCouponImage());
            couponDO.setCouponSize(couponChange.getCouponSize());
            couponDO.setSortIndex(couponChange.getSortIndex());
            couponDO.setTopicId(topicId);
            topicCouponDAO.insert(couponDO);
        }
    }

    /**
     * 合并变更单限购策略至活动限购策略上
     *
     * @param policyId
     * @param policy
     * @param pChange
     */
    private void MergePolicyInfo(Long policyId, PolicyInfo policy,
                                 PolicyChange pChange) {
        policy.setId(policyId);
        policy.setByIp(pChange.getByIp());
        policy.setByMobile(pChange.getByMobile());
        policy.setByRegisterTime(pChange.getByRegisterTime());
        policy.setByUid(pChange.getByUid());
        policy.setEarlyThanTime(pChange.getEarlyThanTime());
        policy.setLateThanTime(pChange.getLateThanTime());
        policy.setUpdateTime(new Date());
    }

    /**
     * 合并活动信息至活动专题上
     *
     * @param userId
     * @param userName
     * @param topicChange
     * @param topic
     * @throws ServiceException
     */
    private void MergeTopicInfo(Long userId, String userName, TopicChange topicChange, Topic topic) throws ServiceException {
        if (StringUtils.isBlank(topicChange.getAreaStr())) {
            topic.setAreaStr(" ");
        } else {
            topic.setAreaStr(topicChange.getAreaStr());
        }
        if (StringUtils.isBlank(topicChange.getDiscount())) {
            topic.setDiscount(" ");
        } else {
            topic.setDiscount(topicChange.getDiscount());
        }
        topic.setEndTime(topicChange.getEndTime());
        topic.setFreightTemplet(topicChange.getFreightTemplet());
        if (StringUtils.isBlank(topicChange.getImage())) {
            topic.setImage(" ");
        } else {
            topic.setImage(topicChange.getImage());
        }
        if (StringUtils.isBlank(topicChange.getImageMobile())) {
            topic.setImageMobile(" ");
        } else {
            topic.setImageMobile(topicChange.getImageMobile());
        }
        if (StringUtils.isBlank(topicChange.getImageNew())) {
            topic.setImageNew(" ");
        } else {
            topic.setImageNew(topicChange.getImageNew());
        }
        if (StringUtils.isBlank(topicChange.getImageNew())) {
            topic.setImageInterested(" ");
        } else {
            topic.setImageInterested(topicChange.getImageInterested());
        }
        if (StringUtils.isBlank(topicChange.getImageHitao())) {
            topic.setImageHitao(" ");
        } else {
            topic.setImageHitao(topicChange.getImageHitao());
        }
        if (StringUtils.isBlank(topicChange.getPcImage())) {
            topic.setPcImage(" ");
        } else {
            topic.setPcImage(topicChange.getPcImage());
        }
        if (StringUtils.isBlank(topicChange.getPcInterestImage())) {
            topic.setPcInterestImage(" ");
        } else {
            topic.setPcInterestImage(topicChange.getPcInterestImage());
        }
        if (StringUtils.isBlank(topicChange.getMobileImage())) {
            topic.setMobileImage(" ");
        } else {
            topic.setMobileImage(topicChange.getMobileImage());
        }
        if (StringUtils.isBlank(topicChange.getMallImage())) {
            topic.setMallImage(" ");
        } else {
            topic.setMallImage(topicChange.getMallImage());
        }
        if (StringUtils.isBlank(topicChange.getHaitaoImage())) {
            topic.setHaitaoImage(" ");
        } else {
            topic.setHaitaoImage(topicChange.getHaitaoImage());
        }
        if (StringUtils.isBlank(topicChange.getIntro())) {
            topic.setIntro(" ");
        } else {
            topic.setIntro(topicChange.getIntro());
        }
        if (StringUtils.isBlank(topicChange.getIntroMobile())) {
            topic.setIntroMobile(" ");
        } else {
            topic.setIntroMobile(topicChange.getIntroMobile());
        }
        topic.setIsSupportSupplier(topicChange.getIsSupportSupplier());
        topic.setIsSupportSupplierInfo(topicChange.getIsSupportSupplierInfo());
        topic.setLastingType(topicChange.getLastingType());
        topic.setUpdateTime(new Date());
        topic.setUpdateUser(userName);
        topic.setName(topicChange.getName());
        topic.setNumber(topicChange.getNumber());
        topic.setPlatformStr(topicChange.getPlatformStr());
        topic.setSortIndex(topicChange.getSortIndex());
        topic.setStartTime(topicChange.getStartTime());
        topic.setCanUseXgMoney(topicChange.getCanUseXgMoney());
        if (StringUtils.isBlank(topicChange.getTopicPoint())) {
            topic.setTopicPoint(" ");
        } else {
            topic.setTopicPoint(topicChange.getTopicPoint());
        }
        if (StringUtils.isBlank(topicChange.getRemark())) {
            topic.setRemark(" ");
        } else {
            topic.setRemark(topicChange.getRemark());
        }
        topic.setSalesPartten(topicChange.getSalesPartten());
        topicDAO.updateNotNullById(topic);
    }

    /**
     * 合并活动商品列表与被删除活动商品(界面删除和数据库中保存为已删除的活动商品)
     *
     * @param topicItemChanges
     * @param removeTopicItemChangeIds
     * @param topicId
     * @param topicChangeId
     * @return
     */
    private ArrayList<TopicInventoryExchangeDTO> mergeTopicItemChange(
            List<TopicItemChange> topicItemChanges,
            ArrayList<Long> removeTopicItemChangeIds, Long topicId,
            Long topicChangeId, Long userId, String userName, Integer reserveInventoryFlag) {
        ArrayList<TopicInventoryExchangeDTO> exchangeDtos = new ArrayList<TopicInventoryExchangeDTO>();
        try {
            List<TopicItemChange> newItemChanges = new ArrayList<TopicItemChange>();
            for (TopicItemChange TopicItemChange : topicItemChanges) {
                if (TopicItemChange.getId() == null
                        || TopicItemChange.getId() == 0L) {
                    newItemChanges.add(TopicItemChange);
                }
            }
            if (newItemChanges.size() == 0) {
                return new ArrayList<TopicInventoryExchangeDTO>();
            }
            // 检查新增活动商品是否之前被删除过
            List<TopicItemChange> itemChanges = Collections.emptyList();
            if (removeTopicItemChangeIds != null
                    && removeTopicItemChangeIds.size() > 0) {
                itemChanges = topicItemChangeDAO.getTopicItemChangeByIds(
                        removeTopicItemChangeIds);
            }
            TopicItemChange selectDO = new TopicItemChange();
            selectDO.setTopicChangeId(topicChangeId);
            selectDO.setDeletion(DeletionStatus.DELETED.ordinal());
            List<TopicItemChange> deleteitemChanges = topicItemChangeDAO.queryByObject(selectDO);
            Map<String, TopicItemChange> deleteItemChangeMap = new HashMap<String, TopicItemChange>();
            List<TopicItemChange> mergeDoList = new ArrayList<TopicItemChange>();
            for (TopicItemChange TopicItemChange : itemChanges) {
                String key = this.getTopicItemChangeKey(TopicItemChange);
                deleteItemChangeMap.put(key, TopicItemChange);
            }
            for (TopicItemChange TopicItemChange : deleteitemChanges) {
                String key = this.getTopicItemChangeKey(TopicItemChange);
                deleteItemChangeMap.put(key, TopicItemChange);
            }
            if (deleteItemChangeMap.size() == 0) {
                return new ArrayList<TopicInventoryExchangeDTO>();
            }
            // 合并新增的商品，以前删除过情况
            for (TopicItemChange topicItemChange : newItemChanges) {
                String key = this.getTopicItemChangeKey(topicItemChange);
                if (!deleteItemChangeMap.containsKey(key)) {
                    continue;
                }
                TopicItemChange mergeTopicItem = deleteItemChangeMap.get(key);
                mergeTopicItem.setDeletion(DeletionStatus.NORMAL.ordinal());
                mergeTopicItem.setTopicPrice(topicItemChange.getTopicPrice());
                mergeTopicItem.setSalePrice(topicItemChange.getSalePrice());
                mergeTopicItem.setName(topicItemChange.getName());
                mergeTopicItem.setLimitAmount(topicItemChange.getLimitAmount());
                mergeTopicItem.setOperStatus(OperStatus.MODIFY.ordinal());
                mergeTopicItem.setBondedArea(topicItemChange.getBondedArea());
                mergeTopicItem.setWhType(topicItemChange.getWhType());
                mergeTopicItem.setCountryId(topicItemChange.getCountryId());
                mergeTopicItem.setCountryName(topicItemChange.getCountryName());
                mergeTopicItem.setPurchaseMethod(topicItemChange.getPurchaseMethod());
                mergeTopicItem.setSortIndex(topicItemChange.getSortIndex());
                if (mergeTopicItem.getLimitTotal() != topicItemChange.getLimitTotal()) {
                    Integer diffAmount = topicItemChange.getLimitTotal()- mergeTopicItem.getLimitTotal();
                    TopicInventoryExchangeDTO exchangeDto = new TopicInventoryExchangeDTO();
                    exchangeDto.setAmount(diffAmount);
                    exchangeDto.setSku(mergeTopicItem.getSku());
                    exchangeDto.setSupplierId(mergeTopicItem.getSupplierId());
                    exchangeDto.setTopicId(topicId);
                    exchangeDto.setTopicItemId(mergeTopicItem.getChangeTopicItemId());
                    exchangeDto.setWarehouseId(mergeTopicItem.getStockLocationId());
                    exchangeDto.setStatus(OperStatus.RECOVER.ordinal());
                    exchangeDto.setBizType(InnerBizType.TOPIC_CHANGE);
                    exchangeDto.setOperType(InventoryOperType.EDIT);
                    exchangeDto.setTopicInventoryFlag(reserveInventoryFlag);//活动是否预占库存：0否1是
                    exchangeDto.setOperatorId(userId);
                    exchangeDto.setOperatorName(userName);
                    exchangeDtos.add(exchangeDto);
                    mergeTopicItem.setLimitTotal(topicItemChange.getLimitTotal());
                }
                mergeDoList.add(topicItemChange);
                topicItemChangeDAO.updateNotNullById(mergeTopicItem);
                mergeTopicItem.setLimitTotal(topicItemChange.getLimitTotal());
                // 更新原来表中限购总数
                TopicItem updateItem = new TopicItem();
                updateItem.setId(mergeTopicItem.getChangeTopicItemId());
                updateItem.setLimitTotal(topicItemChange.getLimitTotal());
                topicItemDAO.updateNotNullById(updateItem);
            }
            // 移除合并后的活动商品信息
            for (TopicItemChange TopicItemChange : mergeDoList) {
                if (topicItemChanges.contains(TopicItemChange)) {
                    topicItemChanges.remove(TopicItemChange);
                }
                String key = this.getTopicItemChangeKey(TopicItemChange);
                if (deleteItemChangeMap.containsKey(key)) {
                    TopicItemChange deleteItem = deleteItemChangeMap.get(key);
                    if (removeTopicItemChangeIds.contains(deleteItem.getId())) {
                        removeTopicItemChangeIds.remove(deleteItem.getId());
                    }
                }
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(
                    ProcessingErrorMessage.COMMON_ERROR);
        }

        return exchangeDtos;
    }

    // #end region 合并信息操作

    // #region 执行数据拷贝

    /**
     * 拷贝专题活动相关信息
     *
     * @param topic
     * @return
     */
    private Topic copyTopic(Topic topic, Long userId, String userName) {
        Topic copyTopic = new Topic();
        copyTopic.setStatus(TopicConstant.TOPIC_STATUS_EDITING);
        copyTopic.setProgress(TopicConstant.TOPIC_PROCESS_NOTSTART);
        copyTopic.setId(null);
        copyTopic.setCreateUser(userName);
        copyTopic.setCreateTime(new Date());
        copyTopic.setUpdateTime(new Date());
        copyTopic.setUpdateUser(userName);
        copyTopic.setDeletion(DeletionStatus.NORMAL.ordinal());
        if (topic == null) {
            return copyTopic;
        }
        copyTopic.setAreaStr(topic.getAreaStr());
        copyTopic.setPlatformStr(topic.getPlatformStr());
        copyTopic.setBrandId(topic.getBrandId());
        copyTopic.setBrandName(topic.getBrandName());
        copyTopic.setDiscount(topic.getDiscount());
        if (topic.getEndTime() != null) {
            copyTopic.setEndTime(topic.getEndTime());
        }
        copyTopic.setFreightTemplet(topic.getFreightTemplet());
        copyTopic.setImage(topic.getImage());
        copyTopic.setImageMobile(topic.getImageMobile());
        copyTopic.setImageNew(topic.getImageNew());
        copyTopic.setImageInterested(topic.getImageInterested());
        copyTopic.setImageHitao(topic.getImageHitao());
        copyTopic.setPcImage(topic.getPcImage());
        copyTopic.setPcInterestImage(topic.getPcInterestImage());
        copyTopic.setMobileImage(topic.getMobileImage());
        copyTopic.setMallImage(topic.getMallImage());
        copyTopic.setHaitaoImage(topic.getHaitaoImage());
        copyTopic.setIntro(topic.getIntro());
        copyTopic.setIntroMobile(topic.getIntroMobile());
        copyTopic.setIsSupportSupplier(topic.getIsSupportSupplier());
        copyTopic.setIsSupportSupplierInfo(topic.getIsSupportSupplierInfo());
        copyTopic.setLastingType(topic.getLastingType());
        copyTopic.setName(topic.getName());
        copyTopic.setNumber(topic.getNumber());
        copyTopic.setTopicPoint(topic.getTopicPoint());
        copyTopic.setRemark(topic.getRemark());
        copyTopic.setSupplierId(topic.getSupplierId());
        copyTopic.setSupplierName(topic.getSupplierName());
        
        /** 活动是否预占库存：0否1是 */
        copyTopic.setReserveInventoryFlag(topic.getReserveInventoryFlag());
        if (topic.getStartTime() != null) {
            copyTopic.setStartTime(topic.getStartTime());
        }
        copyTopic.setType(topic.getType());
        copyTopic.setSalesPartten(topic.getSalesPartten());
        Integer topicSortIndex = topicDAO.getMaxTopicSortIndex();
        if (null == topicSortIndex) {
            topicSortIndex = 0;
        }
        topicSortIndex += 10;
        copyTopic.setSortIndex(topicSortIndex);

        copyTopic.setPcIndex(topic.getPcIndex());
        copyTopic.setWxIndex(topic.getWapIndex());
        copyTopic.setAndroidIndex(topic.getAndroidIndex());
        copyTopic.setWapIndex(topic.getWapIndex());
        copyTopic.setIosIndex(topic.getIosIndex());

        return copyTopic;
    }

    private TopicItem copyTopicItem(TopicItem topicItem, Long topicId,
                                    Long userId) {
        TopicItem copyTopicItem = new TopicItem();
        copyTopicItem.setTopicId(topicId);
        copyTopicItem.setCreateUser(String.valueOf(userId));
        copyTopicItem.setCreateTime(new Date());
        copyTopicItem.setDeletion(DeletionStatus.NORMAL.ordinal());
        copyTopicItem.setInputSource(InputSource.COPY.ordinal());
        copyTopicItem.setLockStatus(LockStatus.UNLOCK.ordinal());
        copyTopicItem.setId(null);
        if (topicItem == null) {
            return copyTopicItem;
        }
        copyTopicItem.setBarCode(topicItem.getBarCode());
        copyTopicItem.setBrandId(topicItem.getBrandId());
        copyTopicItem.setCategoryId(topicItem.getCategoryId());
        // 去除ItemColor和ItemSize
        copyTopicItem.setItemId(topicItem.getItemId());
        copyTopicItem.setItemSpec(topicItem.getItemSpec());
        copyTopicItem.setLimitAmount(topicItem.getLimitAmount());
        copyTopicItem.setLimitTotal(topicItem.getLimitTotal());
        copyTopicItem.setName(topicItem.getName());
        copyTopicItem.setPictureSize(topicItem.getPictureSize());
        copyTopicItem.setRemark(topicItem.getRemark());
        copyTopicItem.setSaledAmount(topicItem.getSaledAmount());
        copyTopicItem.setSalePrice(topicItem.getSalePrice());
        copyTopicItem.setSku(topicItem.getSku());
        copyTopicItem.setSpu(topicItem.getSpu());
        copyTopicItem.setStock(topicItem.getStock());
        copyTopicItem.setStockAmount(topicItem.getStockAmount());
        copyTopicItem.setStockLocation(topicItem.getStockLocation());
        copyTopicItem.setStockLocationId(topicItem.getStockLocationId());
        copyTopicItem.setSupplierId(topicItem.getSupplierId());
        copyTopicItem.setSupplierName(topicItem.getSupplierName());
        copyTopicItem.setTopicImage(topicItem.getTopicImage());
        copyTopicItem.setTopicPrice(topicItem.getTopicPrice());
        copyTopicItem.setBondedArea(topicItem.getBondedArea());
        copyTopicItem.setWhType(topicItem.getWhType());
        copyTopicItem.setCountryId(topicItem.getCountryId());
        copyTopicItem.setCountryName(topicItem.getCountryName());
        copyTopicItem.setSortIndex(topicItem.getSortIndex());
        copyTopicItem.setPurchaseMethod(topicItem.getPurchaseMethod());
        /** 活动是否预占库存：0否1是 */
        copyTopicItem.setReserveInventoryFlag(topicItem.getReserveInventoryFlag());
        return copyTopicItem;
    }

    /**
     * 拷贝限购政策
     *
     * @param policy
     * @return
     */
    private PolicyInfo copyPolicy(PolicyInfo policy) {
        if (policy == null) {
            return null;
        }
        PolicyInfo copyPolicy = new PolicyInfo();
        copyPolicy.setByIp(policy.getByIp());
        copyPolicy.setByMobile(policy.getByMobile());
        copyPolicy.setByRegisterTime(policy.getByRegisterTime());
        copyPolicy.setByTopic(policy.getByTopic());
        copyPolicy.setByUid(policy.getByUid());
        copyPolicy.setEarlyThanTime(policy.getEarlyThanTime());
        copyPolicy.setLateThanTime(policy.getLateThanTime());
        copyPolicy.setCreateTime(new Date());
        copyPolicy.setId(null);
        return copyPolicy;
    }

    /**
     * 拷贝活动指定优惠券
     *
     * @return
     */
    private TopicCoupon copyCoupon(TopicCoupon coupon) {
        TopicCoupon copyCoupon = new TopicCoupon();
        if (coupon == null) {
            return null;
        }
        copyCoupon.setCouponId(coupon.getCouponId());
        copyCoupon.setCouponImage(coupon.getCouponImage());
        copyCoupon.setCouponSize(coupon.getCouponSize());
        copyCoupon.setSortIndex(coupon.getSortIndex());
        copyCoupon.setTopicId(coupon.getTopicId());
        return copyCoupon;
    }
    // #end region

    private void saveTopicPromoterList(Long topicId,List<Long> promoterIdList,String userName){
    	Map<String,Object> params = new HashMap<String,Object>();
		params.put("topicId", topicId);
		topicPromoterDao.deleteByParam(params);
    	if(!CollectionUtils.isEmpty(promoterIdList)){
    		params.clear();
    		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " promoter_id in ("+StringUtil.join(promoterIdList, SPLIT_SIGN.COMMA)+")");
    		List<PromoterInfo> promoterInfoList = promoterInfoService.queryByParam(params);
    		if(!CollectionUtils.isEmpty(promoterInfoList)){
    			List<TopicPromoter> topicPromoterList = new ArrayList<TopicPromoter>();
    			for(PromoterInfo promoterInfo:promoterInfoList){
    				TopicPromoter topicPromoter = new TopicPromoter();
    				topicPromoter.setPromoterId(promoterInfo.getPromoterId());
    				topicPromoter.setTopicId(topicId);
    				topicPromoter.setPromoterName(promoterInfo.getPromoterName());
    				topicPromoter.setChannelCode(promoterInfo.getChannelCode());
    				topicPromoter.setCreateUser(userName);
    				topicPromoter.setCreateTime(new Date());
    				topicPromoterList.add(topicPromoter);
    			}
				topicPromoterDao.batchInsert(topicPromoterList);
    		}
    	}
    }
    
    private void saveTopicPromoterChangeList(Long topicChangeId,Long topicId,List<Long> promoterIdList,String userName){
    	Map<String,Object> params = new HashMap<String,Object>();
		params.put("topicId", topicId);
		topicPromoterChangeDao.deleteByParam(params);
    	if(!CollectionUtils.isEmpty(promoterIdList)){
    		params.clear();
    		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " promoter_id in ("+StringUtil.join(promoterIdList, SPLIT_SIGN.COMMA)+")");
    		List<PromoterInfo> promoterInfoList = promoterInfoService.queryByParam(params);
    		if(!CollectionUtils.isEmpty(promoterInfoList)){
    			List<TopicPromoterChange> topicPromoterChangeList = new ArrayList<TopicPromoterChange>();
    			for(PromoterInfo promoterInfo:promoterInfoList){
    				TopicPromoterChange topicPromoterChange = new TopicPromoterChange();
    				topicPromoterChange.setPromoterId(promoterInfo.getPromoterId());
    				topicPromoterChange.setTopicChangeId(topicChangeId);
    				topicPromoterChange.setTopicId(topicId);
    				topicPromoterChange.setPromoterName(promoterInfo.getPromoterName());
    				topicPromoterChange.setChannelCode(promoterInfo.getChannelCode());
    				topicPromoterChange.setCreateUser(userName);
    				topicPromoterChange.setCreateTime(new Date());
    				topicPromoterChangeList.add(topicPromoterChange);
    			}
    			topicPromoterChangeDao.batchInsert(topicPromoterChangeList);
    		}
    	}
    }
    
    private void mergeTopicPromoterChangeList(Long topicChangeId,Long topicId){
    	Map<String,Object> params = new HashMap<String,Object>();
		params.put("topicId", topicId);
		topicPromoterDao.deleteByParam(params);
		List<TopicPromoterChange> topicPromoterChangeList = topicPromoterChangeDao.queryListByTopicChangeId(topicChangeId);
    	if(!CollectionUtils.isEmpty(topicPromoterChangeList)){
    		List<TopicPromoter> topicPromoterList = new ArrayList<TopicPromoter>();
			for(TopicPromoterChange topicPromoterChange:topicPromoterChangeList){
				TopicPromoter topicPromoter = new TopicPromoter();
				topicPromoter.setPromoterId(topicPromoterChange.getPromoterId());
				topicPromoter.setTopicId(topicId);
				topicPromoter.setPromoterName(topicPromoterChange.getPromoterName());
				topicPromoter.setChannelCode(topicPromoterChange.getChannelCode());
				topicPromoter.setCreateUser(topicPromoterChange.getCreateUser());
				topicPromoter.setCreateTime(new Date());
				topicPromoterList.add(topicPromoter);
			}
			topicPromoterDao.batchInsert(topicPromoterList);
    	}
    }
}
