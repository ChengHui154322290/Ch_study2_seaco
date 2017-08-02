package com.tp.service.mmp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mmp.ProcessingErrorMessage;
import com.tp.dao.mmp.CouponDao;
import com.tp.dao.mmp.PolicyChangeDao;
import com.tp.dao.mmp.RelateChangeDao;
import com.tp.dao.mmp.TopicChangeAuditLogDao;
import com.tp.dao.mmp.TopicChangeDao;
import com.tp.dao.mmp.TopicCouponChangeDao;
import com.tp.dao.mmp.TopicDao;
import com.tp.dao.mmp.TopicItemChangeDao;
import com.tp.dao.mmp.TopicPromoterChangeDao;
import com.tp.dto.mmp.RelateDTO;
import com.tp.dto.mmp.TopicChangeDetailDTO;
import com.tp.dto.mmp.TopicCouponChangeDTO;
import com.tp.dto.mmp.TopicItemInfoDTO;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.exception.ServiceException;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.PolicyChange;
import com.tp.model.mmp.RelateChange;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicChange;
import com.tp.model.mmp.TopicChangeAuditLog;
import com.tp.model.mmp.TopicCouponChange;
import com.tp.model.mmp.TopicItemChange;
import com.tp.model.mmp.TopicPromoterChange;
import com.tp.service.BaseService;
import com.tp.service.mmp.ITopicChangeService;

@Service
public class TopicChangeService extends BaseService<TopicChange> implements ITopicChangeService {

    @Autowired
    private TopicChangeDao topicChangeDao;

    @Autowired
    private PolicyChangeDao policyChangeDao;

    @Autowired
    private TopicCouponChangeDao topicCouponChangeDao;

    @Autowired
    private RelateChangeDao relateChangeDao;
    
    @Autowired
    private TopicPromoterChangeDao topicPromoterChangeDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private TopicChangeAuditLogDao topicChangeAuditLogDao;

    @Autowired
    private TopicItemChangeDao topicItemChangeDao;

    @Override
    public BaseDao<TopicChange> getDao() {
        return topicChangeDao;
    }


    @Override
    public PageInfo<TopicChange> queryPageListByTopicChangeDOAndStartPageSizeWithLike(
            TopicChange TopicChange, int currentPage, int pageSize) {
        if (TopicChange != null && currentPage > 0 && pageSize > 0) {
            TopicChange.setStartPage(currentPage);
            TopicChange.setPageSize(pageSize);
            return this.queryPageListByTopicChangeDOWithLike(TopicChange);
        }
        return new PageInfo<TopicChange>();
    }

    @Override
    public PageInfo<TopicChange> queryPageListByTopicChangeDOWithLike(
            TopicChange TopicChange) throws ServiceException {
        if (TopicChange != null) {
            Long totalCount = this.selectCountDynamicWithLike(TopicChange);
            List<TopicChange> resultList = this.selectDynamicPageQueryWithLike(TopicChange);

            PageInfo<TopicChange> pageInfo = new PageInfo<>();
            pageInfo.setPage(TopicChange.getStartPage());
            pageInfo.setSize(TopicChange.getPageSize());
            pageInfo.setRecords(totalCount.intValue());
            pageInfo.setRows(resultList);
            return pageInfo;
        }
        return new PageInfo<TopicChange>();
    }

    @Override
    public Long selectCountDynamicWithLike(TopicChange topicChange)
            throws ServiceException {
        return topicChangeDao.selectCountWithLike(topicChange);
    }

    @Override
    public List<TopicChange> selectDynamicPageQueryWithLike(TopicChange topicChange) throws ServiceException {
        return topicChangeDao.selectDynamicPageQueryWithLike(topicChange);
    }

    @Override
    public TopicChangeDetailDTO getTopicChangeDetailById(Long tid) {
        if (logger.isDebugEnabled()) {
            logger.info("getTopicChangeDetailById ---- topicId:" + tid);
        }
        TopicChangeDetailDTO topicChangeDetail = new TopicChangeDetailDTO();
        TopicChange topicChange = queryById(tid);
        if (topicChange == null) {
            logger.error("topic ");
            throw new ServiceException(ProcessingErrorMessage.INVALID_TOPIC_CHANGE_INFO);
        }
        topicChangeDetail.setTopic(topicChange);
        // 获得限购政策
        this.getPolicy(topicChange.getLimitPolicyId(), topicChangeDetail);
        // 获得关联专题
        this.getRelateInfo(tid, topicChangeDetail);
        // 获得优惠券列表
        this.getTopicCoupon(tid, topicChangeDetail);
        // 获得审批记录
        this.getAuditInfo(tid, topicChangeDetail);
        // 获得商品信息
        List<TopicItemInfoDTO> items = this.getTopicItemChangeDTOs(tid);
        //获取分销渠道信息
        List<TopicPromoterChange> topicPromoterChangeList = topicPromoterChangeDao.queryListByTopicChangeId(tid);
        if(CollectionUtils.isNotEmpty(topicPromoterChangeList)){
        	List<Long> promoterIdList = new ArrayList<Long>();
        	for(TopicPromoterChange topicPromoterChange:topicPromoterChangeList){
        		promoterIdList.add(topicPromoterChange.getPromoterId());
        	}
        	topicChangeDetail.setPromoterIdList(promoterIdList);
        }
       
        List<Integer> platforms = new ArrayList<>();
        String ps = topicChange.getPlatformStr();
        if(ps!= null){
            String [] pss = ps.split(",");
            for(String p :pss){
                if(!NumberUtils.isNumber(p)) continue;
                    platforms.add(Integer.parseInt(p));
            }
        }
        topicChangeDetail.setPlatformCodes(platforms);

        topicChangeDetail.setTopicItemDtoList(items);
        return topicChangeDetail;
    }

    /**
     * 获得策略信息
     */
    private void getPolicy(Long policyId, TopicChangeDetailDTO topicChangeDetail) {
        if (policyId != null && policyId != 0) {
            PolicyChange PolicyChange = policyChangeDao.queryById(policyId.longValue());
            topicChangeDetail.setPolicy(PolicyChange);
        }
    }

    private void getTopicCoupon(Long tcId,
                                TopicChangeDetailDTO topicChangeDetail) {
        if (tcId != null && tcId != 0) {
            List<TopicCouponChangeDTO> changeDtos = new ArrayList<>();
            TopicCouponChange selectCoupon = new TopicCouponChange();
            selectCoupon.setTopicChangeId(tcId);
            List<TopicCouponChange> changeDos = topicCouponChangeDao.queryByObject(selectCoupon);
            for (TopicCouponChange TopicCouponChange : changeDos) {
                TopicCouponChangeDTO changeDto = new TopicCouponChangeDTO();
                changeDto.setId(TopicCouponChange.getId());
                changeDto.setCouponId(TopicCouponChange.getCouponId());
                changeDto.setCouponImage(TopicCouponChange.getCouponImage());
                changeDto.setCouponSize(TopicCouponChange.getCouponSize());
                changeDto.setSortIndex(TopicCouponChange.getSortIndex());
                Coupon coupon = couponDao.queryById(TopicCouponChange.getCouponId());
                if (coupon != null) {
                    changeDto.setCouponName(coupon.getCouponName());
                    changeDto.setCouponType(coupon.getCouponType());
                }
                changeDtos.add(changeDto);
            }
            topicChangeDetail.setCouponList(changeDtos);
        }
    }

    /**
     * 获得关联专场信息
     *
     * @param tid
     * @param topicDetail
     * @
     */
    private void getRelateInfo(Long tid, TopicChangeDetailDTO topicChangeDetail) {
        Set<Long> relateTidSet = new HashSet<Long>();
        Set<RelateDTO> relateDTOSet = new HashSet<RelateDTO>();
        RelateChange relateDOF = new RelateChange();
        relateDOF.setTopicChangeId(tid);
        relateDOF.setDeletion(DeletionStatus.NORMAL.ordinal());
        List<RelateChange> relatedListF = relateChangeDao.queryByObject(relateDOF);
        if (!relatedListF.isEmpty() && relatedListF.size() > 0) {
            for (RelateChange relateDO : relatedListF) {
                RelateDTO relateDTO = new RelateDTO();
                relateDTO.setTopicChangeId(tid);
                relateDTO.setFirstTopicId(relateDO.getFirstTopicId());
                relateDTO.setSecondTopicId(relateDO.getSecondTopicId());
                Topic secondTopic = topicDao.queryById(relateDO.getSecondTopicId());
                if (null != secondTopic) {
                    relateDTO.setSecondTopicName(secondTopic.getName());
                    relateDTO.setSecondTopicNumber(String.valueOf(secondTopic.getNumber()));
                    relateTidSet.add(relateDO.getSecondTopicId());
                    relateDTOSet.add(relateDTO);
                }
            }
            if (relateDTOSet.size() > 0) {
                topicChangeDetail.setRelateList(new ArrayList<RelateDTO>(
                        relateDTOSet));
            }
        }
    }

    /**
     * 获得审批信息
     *
     * @
     */
    private void getAuditInfo(Long tid, TopicChangeDetailDTO topicChangeDetail) {
        TopicChangeAuditLog audit = new TopicChangeAuditLog();
        audit.setTopicChangeId(tid);
        topicChangeDetail.setAuditLogList(topicChangeAuditLogDao.queryByObject(audit));
    }

    /**
     * 获取活动商品清单
     *
     * @
     */
    private List<TopicItemInfoDTO> getTopicItemChangeDTOs(Long tid) {
        TopicItemChange topciItemChangeDO = new TopicItemChange();
        topciItemChangeDO.setTopicChangeId(tid);
        topciItemChangeDO.setDeletion(DeletionStatus.NORMAL.ordinal());
        List<TopicItemChange> topicItemChangeList = topicItemChangeDao.queryByObject(topciItemChangeDO);
        List<TopicItemInfoDTO> topicItemChangeInfoList = new ArrayList<>();
        for (TopicItemChange TopicItemChange : topicItemChangeList) {
            if (TopicItemChange != null) {
                TopicItemInfoDTO infoDto = new TopicItemInfoDTO();
                infoDto.setTopicItemChange(TopicItemChange);
                topicItemChangeInfoList.add(infoDto);
            }
        }
        return topicItemChangeInfoList;
    }


}
