package com.tp.service.mmp;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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

import com.alibaba.fastjson.JSON;
import com.tp.common.dao.BaseDao;
import com.tp.common.util.mmp.AssertUtil;
import com.tp.common.util.mmp.DateTimeFormatUtil;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.mmp.CouponDao;
import com.tp.dao.mmp.CouponRangeDao;
import com.tp.dao.mmp.CouponSendDao;
import com.tp.dao.mmp.CouponUserDao;
import com.tp.dao.mmp.TopicDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.CouponOrderDTO;
import com.tp.dto.mmp.CouponReceiveDTO;
import com.tp.dto.mmp.CouponUserInfoDTO;
import com.tp.dto.mmp.MyCouponBasicDTO;
import com.tp.dto.mmp.MyCouponDTO;
import com.tp.dto.mmp.MyCouponPageDTO;
import com.tp.dto.mmp.OrderCouponDTO;
import com.tp.dto.mmp.enums.CouponOfferType;
import com.tp.dto.mmp.enums.CouponSendStatus;
import com.tp.dto.mmp.enums.CouponSendType;
import com.tp.dto.mmp.enums.CouponStatus;
import com.tp.dto.mmp.enums.CouponType;
import com.tp.dto.mmp.enums.CouponUserSource;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.dto.mmp.enums.ErrorCodeType;
import com.tp.enums.common.PlatformEnum;
import com.tp.exception.ServiceException;
import com.tp.model.mem.MemberInfo;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.CouponRange;
import com.tp.model.mmp.CouponSend;
import com.tp.model.mmp.CouponUser;
import com.tp.model.mmp.Topic;
import com.tp.query.mmp.CouponInfoQuery;
import com.tp.query.mmp.CouponOrderQuery;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.result.mem.app.ResultMessage;
import com.tp.service.BaseService;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mem.ISendSmsService;
import com.tp.service.mmp.ICouponUserService;
import com.tp.util.BeanUtil;
import com.tp.util.StringUtil;
import com.tp.util.VerifyUtil;

@Service
public class CouponUserService extends BaseService<CouponUser> implements ICouponUserService {

    @Autowired
    private CouponUserDao couponUserDao;

    @Autowired
    private CouponSendDao couponSendDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private IMemberInfoService memberInfoService;
    @Autowired
    private TopicDao topicDao;
    @Autowired
    private ISendSmsService sendSmsService;

    @Autowired
    private JedisCacheUtil jedisCacheUtil;

    @Autowired
    private CouponRangeDao couponRangeDao;
    
    @Autowired
    private IPromoterInfoService promoterInfoService;

    @Override
    public BaseDao<CouponUser> getDao() {
        return couponUserDao;
    }


    @Override
    public ResultInfo newUserCoupon(String login) {
        try {
            List<CouponSend> sendList = new ArrayList<CouponSend>();
            CouponSend CouponSend = new CouponSend();
            CouponSend.setType(CouponSendType.AUTO_NEWUSER.ordinal());
            CouponSend.setStatus(CouponSendStatus.PASSED.ordinal());
            sendList = couponSendDao.queryByObject(CouponSend);
            if (sendList != null && sendList.size() > 0) {
                Date now = new Date();
                for (CouponSend couponSend : sendList) {
                    if (couponSend.getEndTime().after(now) && couponSend.getStartTime().before(now)) {
                        try {

                            //控制非全员自动发券
                            if (couponSend.getToAll() != null && couponSend.getToAll() == 0) {
                                boolean needSend = false;
                                if (StringUtils.isBlank(couponSend.getToUserIds())) {
                                    logger.error("TARGET_USER_COUPON_SEND_AUTO_ERROR:COUPON_USER_IDS_IS_EMPTY:" + JSON.toJSONString(couponSend));
                                    continue;
                                }
                                String[] users = couponSend.getToUserIds().split(",");
                                for (String userCode : users) {
                                    if (StringUtils.equals(login, userCode)) {
                                        needSend = true;
                                        break;
                                    }
                                }
                                if (!needSend) continue;
                                logger.info("NOT_ALL_COUPON_SEND_AUTO:loginId" + login);
                            }

                            String couponIdStr = couponSend.getCouponIds();
                            String msgContent = couponSend.getMsgContent();
                            Integer msgSend = couponSend.getSendMsg();
                            String[] coupIdsArry = couponIdStr.split(",");
                            List<Long> coupIds = new ArrayList<Long>();
                            for (String cid : coupIdsArry) {
                                coupIds.add(Long.valueOf(cid));
                            }
                            List<String> uids = new ArrayList<String>();
                            uids.add(login);
                            List<String> res = sendCouponToUser(coupIds, 0l, "注册自动发放", uids, msgSend, msgContent);
                            if (res != null && res.size() > 0)
                                logger.info("自动领券出错" + login + res.toString());
                        } catch (Exception e) {
                            logger.info("自动领券出错" + login, e);

                        }
                    }
                }
            }
            //logger.info("自动领券成功" + login);
            return new ResultInfo<>();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultInfo(new FailInfo(ResultMessage.FAIL, e.getMessage()));
        }

    }
    
    @Override
    public ResultInfo sendPromoteeCoupon(Long couponId, String mobile, int number) {
    	
    	Coupon coupon = couponDao.queryById(couponId);
    	
    	return sendCouponToMobile(coupon.getId(), mobile, number);
    }
    
    @Override
    public ResultInfo<String> sendAutoCouponBySendType(String loginName, CouponSendType couponSendType) {
        try {
            List<CouponSend> sendList = new ArrayList<CouponSend>();
            CouponSend CouponSend = new CouponSend();
            CouponSend.setType(couponSendType.ordinal());
            CouponSend.setStatus(CouponSendStatus.PASSED.ordinal());
            sendList = couponSendDao.queryByObject(CouponSend);
            if (sendList != null && sendList.size() > 0) {
                Date now = new Date();
                for (CouponSend couponSend : sendList) {
                    if (couponSend.getEndTime().after(now) && couponSend.getStartTime().before(now)) {
                        try {

                            //控制非全员自动发券
                            if (couponSend.getToAll() != null && couponSend.getToAll() == 0) {
                                boolean needSend = false;
                                if (StringUtils.isBlank(couponSend.getToUserIds())) {
                                    logger.error("TARGET_USER_COUPON_SEND_AUTO_ERROR:COUPON_USER_IDS_IS_EMPTY:" + JSON.toJSONString(couponSend));
                                    continue;
                                }
                                String[] users = couponSend.getToUserIds().split(",");
                                for (String userCode : users) {
                                    if (StringUtils.equals(loginName, userCode)) {
                                        needSend = true;
                                        break;
                                    }
                                }
                                if (!needSend) continue;
                                logger.info("NOT_ALL_COUPON_SEND_AUTO:loginId" + loginName);
                            }

                            String couponIdStr = couponSend.getCouponIds();
                            String msgContent = couponSend.getMsgContent();
                            Integer msgSend = couponSend.getSendMsg();
                            String[] coupIdsArry = couponIdStr.split(",");
                            List<Long> coupIds = new ArrayList<Long>();
                            for (String cid : coupIdsArry) {
                                coupIds.add(Long.valueOf(cid));
                            }
                            List<String> uids = new ArrayList<String>();
                            uids.add(loginName);
                            List<String> res = sendCouponToUser(coupIds, 0l, "分享自动发放", uids, msgSend, msgContent);
                            if (res != null && res.size() > 0)
                                logger.info("自动领券出错" + loginName + res.toString());
                        } catch (Exception e) {
                            logger.info("自动领券出错" + loginName, e);

                        }
                    }
                }
            }
            //logger.info("自动领券成功" + login);
            return new ResultInfo<>();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultInfo<String>(new FailInfo(ResultMessage.FAIL, e));
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<String> sendCouponToUser(List<Long> coupIds,
                                         long currentUserId, String currentUserName,
                                         List<String> loginNameList, Integer msgSend, String msgContent) throws Exception {
        if (coupIds != null && coupIds.size() > 0) {
            Set<String> successSet = new HashSet<String>();
            List<String> resList = new ArrayList<String>();
            for (Long coupId : coupIds) {
                try {
                    Coupon coupon = couponDao.queryById(coupId);
                    if (coupon == null) {
                        throw new ServiceException("优惠券批次Id出错！批次ID:" + coupId);
                    } else {
                        if (Integer.valueOf(coupon.getStatus()) != CouponStatus.PASSED.ordinal()) {
                            throw new ServiceException("优惠券批次不正常，无法发放！ 批次ID:" + coupId);
                        }
                        Date now = new Date();
                        if (coupon.getCouponReleaseStime() != null) {
                            if (now.before(coupon.getCouponReleaseStime()))
                                throw new ServiceException("还未到优惠券的发放时间！批次ID:"
                                        + coupId);
                        }
                        if (coupon.getCouponReleaseEtime() != null) {
                            if (now.after(coupon.getCouponReleaseEtime()))
                                throw new ServiceException("已过了优惠券的发放时间！ 批次ID:"
                                        + coupId);
                        }
                        // 发放的总数限制
                        Integer couponCount = coupon.getCouponCount();
                        long restCount = 0;
                        if (couponCount != null && couponCount != -1) {
                            // 统计已经发放的数量
                            CouponUser couponUser = new CouponUser();
                            couponUser.setBatchId(coupId);
                            long issueCount = couponUserDao.queryByObjectCount(couponUser);
                            if (couponCount <= issueCount) {
                                throw new ServiceException("优惠券已经发放完，无法继续发放！ 批次ID:"
                                        + coupId);
                            } else {
                                restCount = couponCount - issueCount;
                                if (loginNameList != null
                                        && loginNameList.size() > 0) {
                                    if (loginNameList.size() > restCount) {
                                        throw new ServiceException(
                                                "剩余优惠券数量不够当前数量的用户数！ 批次ID:"
                                                        + coupId);
                                    }
                                }
                            }
                        }

                        if (loginNameList != null && loginNameList.size() > 0) {

                            for (String loginName : loginNameList) {
                                try {
                                    MemberInfo user = memberInfoService.getByMobile(loginName);
                                    if (user == null)
                                        throw new ServiceException("用户不存在！");
                                    long userId = user.getId();
                                    CouponUser couponUser = new CouponUser();
                                    couponUser.setBatchId(coupId);
                                    couponUser.setToUserId(userId);

                                    if (couponUserDao.queryByObjectCount(couponUser) != 0) {
                                        resList.add("批次ID: " + coupId + " 用户ID: "
                                                + loginName + " 不能多次领取！");// 将失败的用户id记录，返回
                                        continue;
                                    }
                                    String number = coupId
                                            + "_"
                                            + userId
                                            + "_"
                                            + DateTimeFormatUtil
                                            .formatDateyyyyMMddHHmmssForTitle(now);
                                    couponUser.setNumber(number);
                                    couponUser.setStatus(CouponUserStatus.NORMAL.ordinal());
                                    couponUser.setCreateTime(now);
                                    couponUser.setUpdateTime(now);
                                    couponUser.setCreateUserId(currentUserId);
                                    couponUser.setCreateUserName(currentUserName);
                                    couponUser.setCouponType(coupon.getCouponType());
                                    couponUser.setToUserLogin(loginName);
                                    couponUser.setToUserMobile(user.getMobile());
                                    couponUser.setBatchNum(0);
                                    couponUser.setRefCode("");
                                    couponUser.setSource(CouponUserSource.ISSUE.getValue());
                                    couponUser.setSourceName(CouponUserSource.ISSUE.getDescription());
                                    couponUser.setCouponUseStime(coupon.getCouponUseStime());
                                    couponUser.setCouponUseEtime(coupon.getCouponUseEtime());
                                    //com.tp.common.util.mmp.BeanUtil.processNullField(couponUser);
                                    couponUserDao.insert(couponUser);
                                    successSet.add(loginName);//成功的插入列表

                                } catch (Exception e) {
                                    resList.add("批次ID: " + coupId + " 用户ID: "
                                            + loginName + "发放失败！用户不存在！ ");// 将失败的用户id记录，返回
                                }
                            }
                        } else {
                            throw new Exception("用户列表为空！");
                        }

                    }
                } catch (Exception e) {
                    resList.add(e.getMessage());// 将失败的用户id记录，返回
                }
            }
            //给成功的人发信息
            if (msgSend == 1) {
                if (successSet != null && successSet.size() > 0) {
                    StringBuffer sb = new StringBuffer();
                    for (String mobile : successSet) {
                        sb.append(",").append(mobile);
                    }
                    String mobiles = sb.toString();
                    mobiles = mobiles.substring(1);
                    try {
                        sendSmsService.sendSms(mobiles, msgContent,null);
                        logger.info("发放短信成功..........." + mobiles);

                    } catch (Exception e) {
                        logger.info("发放短信异常..........." + mobiles);

                    }
                }
            }
            return resList;
        } else {
            throw new ServiceException("优惠券列表为空！");
        }
    }
    
    @Override
    public ResultInfo sendCouponToMobile(Long couponId, String mobile, int number) {
    	AssertUtil.notNull(couponId, "参数错误");
    	AssertUtil.notNull(mobile, "参数错误");
        String lockKey = "promoter_coupon_user_" + couponId + "_" + mobile;//高并发领券 
        boolean concurrent = false;
        try {

            Coupon coupon = new Coupon();
            coupon.setId(couponId);
            List<Coupon> couponList = couponDao.queryByObject(coupon);
            if (null != couponList && couponList.size() > 0) {
                coupon = couponList.get(0);
            } else {
                return new ResultInfo(new FailInfo("优惠券已失效", -1));
            }
            if (null == coupon) {
                return new ResultInfo(new FailInfo("优惠券已失效", -1));
            } else {
                if (coupon.getOfferType() == CouponOfferType.SEND.getCode()) {
                    return new ResultInfo(new FailInfo("优惠券不支持领取", -1));
                }
                if (Integer.valueOf(coupon.getStatus()) != CouponStatus.PASSED.ordinal()) {
                    return new ResultInfo(new FailInfo("优惠券批次不正常，无法领取", -1));
                }
                Date now = new Date();
                if (coupon.getCouponReleaseStime() != null) {
                    if (now.before(coupon.getCouponReleaseStime()))
                        return new ResultInfo(new FailInfo("领券活动尚未开始，请稍后再来", -11));
                }
                if (coupon.getCouponReleaseEtime() != null) {
                    if (now.after(coupon.getCouponReleaseEtime()))
                        return new ResultInfo(new FailInfo("已过了优惠券的发放时间", -12));
                }
                // 发放的总数限制
                Integer couponCount = coupon.getCouponCount();
                long restCount = 0;
                if (couponCount != null && couponCount != -1) {
                    // 统计已经发放的数量
                    CouponUser couponUser = new CouponUser();
                    couponUser.setBatchId(couponId);
                    long issueCount = couponUserDao.queryByObjectCount(couponUser);
                    if (couponCount <= issueCount) {
                        return new ResultInfo(new FailInfo("优惠券已经领取完，无法发放", -13));
                    } else {
                        restCount = couponCount - issueCount;
                        if (restCount < number) {
                            return new ResultInfo(new FailInfo("卡券数量不够发放", -13));
                        }
                    }
                }

                if (null != jedisCacheUtil.getCache(lockKey)) {
                    concurrent = true;
                    return new ResultInfo(new FailInfo("领券异常-CLASH_RECEIVE", -1));
                }
                jedisCacheUtil.setCache(lockKey, "1",10);

                Long memberId = 0l;
                if (StringUtils.isNotBlank(mobile)) {
                    if (!VerifyUtil.verifyTelephone(mobile)) return new ResultInfo(new FailInfo("手机号格式错误", -14));
                    MemberInfo memberInfo = memberInfoService.getByMobile(mobile);
                    if (memberInfo != null)
                        memberId = memberInfo.getId();
                }

                CouponUser couponUser = new CouponUser();
                couponUser.setBatchId(couponId);
                couponUser.setToUserMobile(mobile);
                couponUser.setToUserId(memberId);
                couponUser.setToUserLogin(mobile);
                for(int i=1; i<=number; i++) {
                	addCouponUser(coupon, couponId, now, couponUser, i);
                }
            }
            return new ResultInfo();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultInfo(new FailInfo("系统繁忙,请稍后再试",-1));
        }finally {
            if(!concurrent)
            jedisCacheUtil.deleteCacheKey(lockKey);
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo receiveCouponUser(Long couponId, Long memberId, String mobile, String nickName) {
        try {
            Coupon coupon = couponDao.queryById(couponId);
            if (coupon == null) {
                throw new Exception("优惠券批次Id出错!批次ID:" + couponId);
            } else {
                if (Integer.valueOf(coupon.getStatus()) != CouponStatus.PASSED
                        .ordinal()) {
                    throw new Exception("优惠券批次不正常，无法领取！ 批次ID:" + couponId);
                }
                Date now = new Date();
                if (coupon.getCouponReleaseStime() != null) {
                    if (now.before(coupon.getCouponReleaseStime()))
                        throw new Exception("还未到优惠券的领取时间！");
                }
                if (coupon.getCouponReleaseEtime() != null) {
                    if (now.after(coupon.getCouponReleaseEtime()))
                        throw new Exception("已过了优惠券的领取时间！ ");
                }
                // 发放的总数限制
                Integer couponCount = coupon.getCouponCount();
                long restCount = 0;
                if (couponCount != null && couponCount != -1) {
                    // 统计已经发放的数量
                    CouponUser couponUser = new CouponUser();
                    couponUser.setBatchId(couponId);
                    long issueCount = couponUserDao.queryByObjectCount(couponUser);
                    if (couponCount <= issueCount) {
                        throw new Exception("优惠券已经领取完，无法领取 ");
                    } else {
                        restCount = couponCount - issueCount;
                        if (restCount <= 0) {
                            throw new Exception("优惠券已经领取完，无法领取 ");
                        }
                    }
                }

                MemberInfo user = memberInfoService.getByMobile(mobile);
                if (user == null)
                    throw new Exception("用户不存在！");
                CouponUser couponUser = new CouponUser();
                couponUser.setBatchId(couponId);
                couponUser.setToUserId(memberId);
                String number = couponId
                        + "_"
                        + memberId
                        + "_"
                        + DateTimeFormatUtil
                        .formatDateyyyyMMddHHmmssForTitle(now);
                couponUser.setNumber(number);
                couponUser
                        .setStatus(CouponUserStatus.NORMAL
                                .ordinal());
                couponUser.setCreateTime(now);
                couponUser.setUpdateTime(now);
                couponUser.setSource(CouponUserSource.RECEIVE.getValue());
                couponUser.setCreateUserId(memberId);//TODO 需要区分领取和发放！
                couponUser.setCreateUserName(nickName);
                couponUser.setToUserLogin(mobile);
                couponUser.setToUserMobile(mobile);
                couponUser.setCouponType(coupon.getCouponType());
                couponUserDao.insert(couponUser);
                logger.info("领取优惠券成功:" + user.getMobile());
                return new ResultInfo();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultInfo(new FailInfo(ResultMessage.FAIL, e.getMessage(),
                    ErrorCodeType.OTHER.ordinal()));
        }
    }

    @Override
    public boolean checkCouponEdit(long couponId) {
        CouponUser couponUser = new CouponUser();
        couponUser.setBatchId(couponId);
        try {
            Integer count = couponUserDao.queryByObjectCount(couponUser);
            if (count > 0)
                return false;
            else
                return true;
        } catch (ServiceException e) {
            logger.error("error", e);
            return false;
        }

    }

    @Override
    public PageInfo<MyCouponDTO> myCoupon(long userId, Integer couponType, Integer status, int startPage, int pageSize) throws Exception {

        correlationCoupon(userId);
        // 检查用户的优惠券状态，过期的更新状态
        checkCouponUserStarus(userId, couponType);

        CouponUser couponUser = new CouponUser();
        couponUser.setToUserId(userId);
        couponUser.setCouponType(couponType);
        if (status != null)
            couponUser.setStatus(status);
        couponUser.setPageSize(pageSize);
        couponUser.setStartPage(startPage);

        Map<String, Object> param = BeanUtil.beanMap(couponUser);
        param.put(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " create_time desc");

        PageInfo<CouponUser> couponUserPageInfo = this.queryPageByParam(param, new PageInfo<CouponUser>(startPage, pageSize));
        List<CouponUser> couponUserList = couponUserPageInfo.getRows();

        PageInfo<MyCouponDTO> page = new PageInfo<>(couponUserPageInfo.getPage(), couponUserPageInfo.getSize());
        if (CollectionUtils.isEmpty(couponUserPageInfo.getRows())) {
            return page;
        }

        List<MyCouponDTO> myList = new ArrayList<>();
        for (CouponUser couponUser1 : couponUserList) {
            MyCouponDTO dto = new MyCouponDTO();
            Coupon coupon = couponDao.queryById(couponUser1.getBatchId());
            dto.setCouponId(couponUser1.getBatchId());
            if (couponUser1.getId() != null) {
                dto.setCouponUserId(couponUser1.getId());
            }
            dto.setCouponName(coupon.getCouponName());
            dto.setCouponType(coupon.getCouponType());
            Integer useType = coupon.getCouponUseType();
            if (Integer.valueOf(useType) == 0) {
                if (coupon.getCouponUseEtime() != null)
                    dto.setCouponUseEtime(coupon.getCouponUseEtime());
                if (coupon.getCouponUseStime() != null)
                    dto.setCouponUseStime(coupon.getCouponUseStime());
            } else {
                Integer receiveDay = coupon.getUseReceiveDay();
                Date receiveTime = couponUser1.getCreateTime();
                if (receiveDay != null && receiveTime != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(receiveTime);
                    calendar.add(Calendar.DAY_OF_MONTH, receiveDay);
                    Date newTime = calendar.getTime();
                    dto.setCouponUseStime(receiveTime);
                    dto.setCouponUseEtime(newTime);
                }
            }

            dto.setCouponUseType(coupon.getCouponUseType());
            dto.setFaceValue(coupon.getFaceValue());
            dto.setNeedOverMon(coupon.getNeedOverMon());
            dto.setStatus(couponUser1.getStatus());
            dto.setExchangeXgMoney(coupon.getExchangeXgMoney());
            //
            CouponRange couponRange = new CouponRange();
            couponRange.setCouponId(couponUser1.getBatchId());
            couponRange.setRangeType(0);
            List<CouponRange> rangeList = couponRangeDao.queryByObject(couponRange);
            couponRange.setRangeType(1);
            List<CouponRange> rangeListNoInclude = couponRangeDao.queryByObject(couponRange);
            StringBuffer userCondition = new StringBuffer();
            if (rangeList != null && rangeList.size() > 0) {
                for (CouponRange range : rangeList) {
                    String brandName = range.getBrandName();
                    String sku = range.getCode();
                    String skuName = range.getSkuName();
                    String categoryName = range.getAttributeName();
                    if (categoryName != null && !"".equals(categoryName)) {
                        userCondition.append("分类：" + categoryName + ";<br/>");
                    }
                    if (sku != null && !"".equals(sku) &&"0".equals(range.getType())) {
                        if (skuName != null && !"".equals(skuName)) {
                            userCondition.append("商品：" + skuName + ";<br/>");
                        } else
                            userCondition.append("sku：" + sku + ";<br/>");
                    }
                    if ((range.getCode() != null && !"".equals(range.getCode()))&&"2".equals(range.getType()) ) {
                    	Topic topicInfo=new Topic();
                    	topicInfo.setId(Long.valueOf(range.getCode()));
                    	List<Topic>  couponList=topicDao.queryByObject(topicInfo);
                    	if(couponList.size()>0){
                    		String topicName=couponList.get(0).getName();
                    		userCondition.append("专场名称：" + topicName );
                    	}else{
                    		userCondition.append("专场ID：" + range.getCode() );
                    	}
                       
                    }
                    if (brandName != null && !"".equals(brandName)) {
                        userCondition.append("品牌：" + brandName + ";<br/>");
                    }
                }

            }
            if (rangeListNoInclude != null && rangeListNoInclude.size() > 0) {
                userCondition.append("不包含：<br/>");
                for (CouponRange range : rangeListNoInclude) {
                    String brandName = range.getBrandName();
                    String sku = range.getCode();
                    String skuName = range.getSkuName();
                    String categoryName = range.getAttributeName();
                    if (categoryName != null && !"".equals(categoryName)) {
                        userCondition.append("分类：" + categoryName + ";<br/>");
                    }
                    if (sku != null && !"".equals(sku)) {
                        if (skuName != null && !"".equals(skuName)) {
                            userCondition.append("商品：" + skuName + ";<br/>");
                        } else
                            userCondition.append("sku：" + sku + ";<br/>");
                    }
                    if (brandName != null && !"".equals(brandName)) {
                        userCondition.append("品牌：" + brandName + ";<br/>");
                    }
                }
            }

            if ((rangeList != null && rangeList.size() > 0) || (rangeListNoInclude != null && rangeListNoInclude.size() > 0))
                dto.setUserCondition(userCondition.toString());

            dto.getRangeInclude().addAll(getRangeList(rangeList));
            dto.getRangeNotInclude().addAll(getRangeList(rangeListNoInclude));
            myList.add(dto);
        }

        page.setRows(myList);
        page.setRecords(couponUserPageInfo.getRecords());
        return page;

    }


    private List<String> getRangeList(List<CouponRange> ranges) {
        if (CollectionUtils.isEmpty(ranges)) {
            return Collections.emptyList();
        }
        List<String> rangeDesc = new ArrayList<>();
        for (CouponRange range : ranges) {
            if (StringUtils.isNotBlank(range.getAttributeName())) {
                rangeDesc.add("分类:" + range.getAttributeName());
            }
            if (StringUtils.isNotBlank(range.getBrandName())) {
                rangeDesc.add("品牌:" + range.getBrandName());
            }
            if (StringUtils.isNotBlank(range.getCode())&& "0".equals(range.getType())) {
                rangeDesc.add(StringUtils.isNotBlank(range.getSkuName()) ? ("商品:" + range.getSkuName()) : ("SKU:" + range.getCode()));
            }
            if (StringUtils.isNotBlank(range.getCode())&& "2".equals(range.getType())) {
            	Topic topicInfo=new Topic();
            	topicInfo.setId(Long.valueOf(range.getCode()));
            	List<Topic>  couponList=topicDao.queryByObject(topicInfo);
            	StringBuffer  remark=new StringBuffer();
            	if(couponList.size()>0){
            		String topicName=couponList.get(0).getName();
            		remark.append("专场名称：" + topicName );
            	}else{
            		remark.append("专场ID：" + range.getCode() );
            	}
            	
                rangeDesc.add(remark.toString());
            }
        }
        return rangeDesc;
    }

    /***
     * 检查优惠券的是否已经过期
     *
     * @param userId
     * @param couponType
     * @throws ServiceException
     * @throws Exception
     * @throws ParseException
     */
    private void checkCouponUserStarus(long userId, Integer couponType) throws Exception {
        CouponUser couponUser = new CouponUser();
        couponUser.setToUserId(userId);
        couponUser.setCouponType(couponType);
        couponUser.setStatus(CouponUserStatus.NORMAL.ordinal());
        Integer totalCount = couponUserDao.queryByObjectCount(couponUser);
        if (totalCount > 0) {
            Date now = new Date();
            List<CouponUser> couponUserDOList = couponUserDao.queryPageByParam(BeanUtil.beanMap(couponUser));
            for (CouponUser CouponUser : couponUserDOList) {
                Coupon Coupon = couponDao.queryById(CouponUser.getBatchId());
                Integer useType = Coupon.getCouponUseType();
                Integer couponStatus = Coupon.getStatus();

                if(couponStatus == null || couponStatus == CouponStatus.CANCELED.ordinal() || couponStatus == CouponStatus.STOP.ordinal()){
                    CouponUser.setStatus(CouponUserStatus.INVALID.ordinal());
                    couponUserDao.updateNotNullById(CouponUser);
                }

                if (Integer.valueOf(useType) == 0) {// 固定区段有效
                    if (Coupon.getCouponUseEtime() != null) {
                        if (now.after(Coupon.getCouponUseEtime())) {
                            CouponUser.setStatus(CouponUserStatus.OVERDUE.ordinal());
                            couponUserDao.updateNotNullById(CouponUser);
                        }
                    }
                } else {// 领取几日有效
                    Integer receiveDay = Coupon.getUseReceiveDay();
                    Date receiveTime = CouponUser.getCreateTime();
                    if (receiveDay != null && receiveTime != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(receiveTime);
                        calendar.add(Calendar.DAY_OF_MONTH, receiveDay);
                        Date newTime = calendar.getTime();
                        if (now.after(newTime)) {
                            CouponUser.setStatus(CouponUserStatus.OVERDUE.ordinal());
                            couponUserDao.updateNotNullById(CouponUser);
                        }
                    }
                }
            }

        }

    }

    @Override
    public MyCouponPageDTO myCouponWithCount(long userId, int couponType, int status, int startPage, int pageSize) throws Exception {
        PageInfo<MyCouponDTO> page = myCoupon(userId, couponType, status, startPage, pageSize);
        MyCouponPageDTO dto = new MyCouponPageDTO();
        dto.setPage(page);

        CouponUser couponUser = new CouponUser();
        couponUser.setToUserId(userId);
        couponUser.setCouponType(couponType);
        couponUser.setStatus(CouponUserStatus.NORMAL.ordinal());
        couponUser.setPageSize(pageSize);
        couponUser.setStartPage(startPage);

        Integer normalCount = couponUserDao.queryByObjectCount(couponUser);
        couponUser.setStatus(CouponUserStatus.USED.ordinal());
        Integer usedCount = couponUserDao.queryByObjectCount(couponUser);
        couponUser.setStatus(CouponUserStatus.OVERDUE.ordinal());
        Integer overdueCount = couponUserDao.queryByObjectCount(couponUser);

        dto.setNormalCount(normalCount.intValue());
        dto.setOverdueCount(overdueCount.intValue());
        dto.setUsedCount(usedCount.intValue());
        return dto;

    }

    @Override
    public MyCouponBasicDTO myCouponBasicInfo(long userId) throws Exception {
        correlationCoupon(userId);
        checkCouponUserStarus(userId, null);

        MyCouponBasicDTO myCouponBasicDTO = new MyCouponBasicDTO();
        CouponUser couponUser = new CouponUser();
        couponUser.setToUserId(userId);
        couponUser.setCouponType(CouponType.HAS_CONDITION.ordinal());// 优惠券
        couponUser.setStatus(CouponUserStatus.NORMAL.ordinal());
        // 优惠券拿数量
        Integer normalCount = couponUserDao.queryByObjectCount(couponUser);
        myCouponBasicDTO.setNormalCount(normalCount.intValue());
        Double price = 0d;
        couponUser.setCouponType(CouponType.NO_CONDITION.ordinal());// 红包
        Integer redPacketCount = couponUserDao.queryByObjectCount(couponUser);
        myCouponBasicDTO.setRedPacketCount(redPacketCount.intValue());
//        List<CouponUser> clist = couponUserDao.queryByObject(couponUser);
//        if (clist != null && clist.size() > 0) {
//            for (CouponUser cdo : clist) {
//                long cid = cdo.getBatchId();
//                Coupon Coupon = couponDao.queryById(cid);
//                if (Coupon != null) {
//                    if (Coupon.getFaceValue() != null)
//                        price += Coupon.getFaceValue();
//                }
//            }
//        }
        myCouponBasicDTO.setTotalMoney(price);
        myCouponBasicDTO.setTotalCount((normalCount == null ? 0 : normalCount) + (redPacketCount == null ? 0 : redPacketCount));
        return myCouponBasicDTO;

    }

    @Override
    @Transactional
    public boolean updateCouponUserStatus(List<Long> couponUserIds, int status) throws Exception {

        if (CollectionUtils.isEmpty(couponUserIds)) return true;

        for (Long couponUserId : couponUserIds) {
            CouponUser coupon = couponUserDao.queryById(couponUserId);
            AssertUtil.notNull(coupon, "优惠券不存在");
            if (coupon.getStatus() != null && status == coupon.getStatus()) {
            	logger.info("更新优惠券状态为{}, 优惠券当前状态为：{},不需要更新", status, coupon.getStatus());
            	continue;
            }
            if (status == CouponUserStatus.USED.ordinal()) {
                Map<String, Object> param = new HashMap<>();
                param.put("id", couponUserId);
                param.put("newStatus", status);
                param.put("updateTime", new Date());
                param.put("oldStatus", CouponUserStatus.NORMAL.ordinal());
                int effectCount = couponUserDao.updateStatusById(param);
                if (effectCount == 0) {
                    logger.error("更新优惠券状态为[{}]失败,ID:{}", CouponUserStatus.USED.name(), couponUserId);
                    throw new ServiceException("更新优惠券状态失败");
                }

            } else if (status == CouponUserStatus.NORMAL.ordinal()) {
                Map<String, Object> param = new HashMap<>();
                param.put("id", couponUserId);
                param.put("newStatus", status);
                param.put("updateTime", new Date());
                param.put("oldStatus", CouponUserStatus.USED.ordinal());
                int effectCount = couponUserDao.updateStatusById(param);
                if (effectCount == 0) {
                    logger.error("更新优惠券状态为[{}]失败,ID:{}", CouponUserStatus.NORMAL.name(), couponUserId);
                    throw new ServiceException("更新优惠券状态失败");
                }

            } else {
                coupon.setStatus(status);
                coupon.setUpdateTime(new Date());
                couponUserDao.updateNotNullById(coupon);
            }


        }
        return true;
    }

    /**
     * 判断顺序 1：status是否可用 2： 优惠券的使用类型 0：时间段有效 1：领取有效 是否在可用时间之内
     * 3：如果是优惠券，有最低使用金额，则判断该商品的总价是否满足要求 4：平台是否在优惠券的可用平台之内 :5：是否有限制范围，如果有
     * 5.1：判断brand 5.2：各级category 5.3：sku
     */
    @Override
    public List<OrderCouponDTO> queryOrderCouponList(CouponOrderQuery query) throws Exception {

        AssertUtil.notNull(query, "参数为空");
        AssertUtil.notNull(query.getUserId(), "用户Id为空");
        AssertUtil.notNull(query.getPlatformType(), "平台类型为空");
        AssertUtil.notNull(query.getCouponOrderDTOList(), "商品信息为空");
        correlationCoupon(query.getUserId());
        long userId = query.getUserId();
        Integer platformType = query.getPlatformType();
        Integer itemType = query.getItemType();
        List<CouponOrderDTO> cdtoList = query.getCouponOrderDTOList();
        CouponUser couponUser = new CouponUser();
        couponUser.setToUserId(userId);
        couponUser.setStatus(CouponUserStatus.NORMAL.ordinal());
        List<CouponUser> couponUserDOList = couponUserDao.queryByObject(couponUser);
        if (CollectionUtils.isEmpty(couponUserDOList)) {
            return Collections.emptyList();
        }

        List<OrderCouponDTO> resDtoList = new ArrayList<>();
        for (CouponUser couponUserInfo : couponUserDOList) {

            Boolean canUse = false;// 默认此券不可用
            long couponId = couponUserInfo.getBatchId();
            long couponUserId = couponUserInfo.getId();
            Coupon couponInfo = couponDao.queryById(couponId);
            if (couponInfo.getStatus() != CouponStatus.PASSED.ordinal()) {
                continue;
            }
            // 如果是时间段有效。则判断是否在使用期间内
            Integer useType = couponInfo.getCouponUseType();

            if (useType != null) { // 惠券使用 类型 0：时间段有效 1：领取有效
                if (Integer.valueOf(useType) == 0) {// 时间段有效
                    Date useStime = couponInfo.getCouponUseStime();
                    Date useEtime = couponInfo.getCouponUseEtime();
                    Date now = new Date();
                    if ((now.after(useStime) || now.equals(useStime)) && (now.before(useEtime) || now.equals(useEtime))) {
                        // 在可用时间段以内，继续执行
                    } else {// 不在使用时间段
                        continue;
                    }

                } else {// 领取后几日内有效
                    Integer receiveDay = couponInfo.getUseReceiveDay();
                    Date now = new Date();
                    Date receiveTime = couponUserInfo.getCreateTime();
                    if (receiveDay != null && receiveTime != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(receiveTime);
                        calendar.add(Calendar.DAY_OF_MONTH, receiveDay);
                        Date newTime = calendar.getTime();
                        if (now.before(newTime) || now.equals(newTime)) {
                            // 在可用时间段以内，继续执行
                        } else {// 过了使用日期
                            continue;
                        }
                    } else {
                        // 此优惠券有问题
                    }
                }
            }
            logger.debug("时间段有效.................");
            // 所用平台是否符合要求
            String userPlatform = couponInfo.getUsePlantform();
            if (userPlatform != null && !"".equals(userPlatform)) {
                String[] plarray = userPlatform.split(",");
                List<String> plList = Arrays.asList(plarray);
                if (!plList.contains(PlatformEnum.ALL.getCode() + "")) {
                    if (!plList.contains(platformType.toString() + "")) {
                        continue;// 不在符合条件的平台
                    }
                }
            }

            //是否适用海淘
            String haitaoSign = couponInfo.getHitaoSign();
            if (haitaoSign != null && !"".equals(haitaoSign) && itemType != null) {
                if (!haitaoSign.equals(0 + "") && !haitaoSign.contains(itemType + "")) {
                    continue;
                }
            }

            //发券主体
            List<CouponOrderDTO> cdtoListSource = new ArrayList<CouponOrderDTO>();
            Integer sourceType = couponInfo.getSourceType();
            if (sourceType == 1) {
                //西客券，保持现在逻辑不变
                cdtoListSource = cdtoList;
            } else {
                //商户券，只统计满足要求的商户商品
                Long sourceId = couponInfo.getSourceId();
                for (CouponOrderDTO cdto : cdtoList) {
                    Long supplierId = cdto.getSupplierId();
                    if (supplierId != null && sourceId != null && supplierId.equals(sourceId)) {
                        cdtoListSource.add(cdto);
                    }
                }

            }

            CouponRange rangeDO = new CouponRange();
            rangeDO.setCouponId(couponId);
            rangeDO.setRangeType(0);
            List<CouponRange> rangeList = new ArrayList<>();
            List<CouponRange> rangeListTemp = couponRangeDao.queryByObject(rangeDO);
            if (rangeListTemp != null && rangeListTemp.size() > 0) {
                for (CouponRange range : rangeListTemp) {
                    if (range.getRangeType() == 0) {
                        rangeList.add(range);
                    }
                }
            }

            rangeDO.setRangeType(1);
            List<CouponRange> rangeListNoInclude = couponRangeDao.queryByObject(rangeDO);

            //先处理黑名单,
            Set<Long> brandIdSetNo = new HashSet<Long>();
            Set<Long> cidSetTNo3 = new HashSet<Long>();
            Set<Long> cidSetTNo2 = new HashSet<Long>();
            Set<Long> cidSetTNo1 = new HashSet<Long>();
            Set<String> skuSetNo = new HashSet<String>();
            if (rangeListNoInclude != null && rangeListNoInclude.size() > 0) {
                for (CouponRange rangeNo : rangeListNoInclude) {
                    if (rangeNo.getBrandId() != null && rangeNo.getBrandId() != 0) {
                        brandIdSetNo.add(rangeNo.getBrandId());
                    }
                    if (rangeNo.getCategorySmallId() != null && rangeNo.getCategorySmallId() != 0) {
                        cidSetTNo3.add(rangeNo.getCategorySmallId());
                    }
                    else if (rangeNo.getCategoryMiddleId() != null && rangeNo.getCategoryMiddleId() != 0) {
                    	cidSetTNo2.add(rangeNo.getCategoryMiddleId());
                    }
                    else if (rangeNo.getCategoryId() != null && rangeNo.getCategoryId() != 0) {
                    	cidSetTNo1.add(rangeNo.getCategoryId());
                    }
                    if (rangeNo.getCode() != null && !"".equals(rangeNo.getCode())) {
                        skuSetNo.add(rangeNo.getCode());
                    }
                }
            }

            List<CouponOrderDTO> cdtoListNo = new ArrayList<>();
            for (CouponOrderDTO cdto : cdtoListSource) {
                Long bid = cdto.getBrandId();
                Long cidT3 = cdto.getThordCategoryId();
                Long cidT2 = cdto.getSecondCategoryId();
                Long cidT1 = cdto.getFirstCategoryId();
                String sku = cdto.getSku();
                if (brandIdSetNo != null && brandIdSetNo.size() > 0) {
                    if (brandIdSetNo.contains(bid)) {
                        continue;
                    }
                }
                if (cidSetTNo3 != null && cidSetTNo3.size() > 0) {
                    if (cidSetTNo3.contains(cidT3)) {
                        continue;
                    }
                }
                else if (cidSetTNo2 != null && cidSetTNo2.size() > 0) {
                	if (cidSetTNo2.contains(cidT2)) {
                		continue;
                	}
                }
                else if (cidSetTNo1 != null && cidSetTNo1.size() > 0) {
                	if (cidSetTNo1.contains(cidT1)) {
                		continue;
                	}
                }
                if (skuSetNo != null && skuSetNo.size() > 0) {
                    if (skuSetNo.contains(sku)) {
                        continue;
                    }
                }
                cdtoListNo.add(cdto);
            }

            //白名单处理，符合要求的类型、品牌、sku
            Set<Long> brandIdSet = new HashSet<Long>();
            Set<Long> cidSetT3 = new HashSet<Long>();
            Set<Long> cidSetT2 = new HashSet<Long>();
            Set<Long> cidSetT1 = new HashSet<Long>();
            Set<String> skuSet = new HashSet<String>();
            if (rangeList != null && rangeList.size() > 0) {
                for (CouponRange range : rangeList) {
                    if (range.getBrandId() != null && range.getBrandId() != 0) {
                        brandIdSet.add(range.getBrandId());
                    }
                    if (range.getCategorySmallId() != null && range.getCategorySmallId() != 0) {
                        cidSetT3.add(range.getCategorySmallId());
                    }
                    else if (range.getCategoryMiddleId() != null && range.getCategoryMiddleId() != 0) {
                        cidSetT2.add(range.getCategoryMiddleId());
                    }
                    else if (range.getCategoryId() != null && range.getCategoryId() != 0) {
                        cidSetT1.add(range.getCategoryId());
                    }
                    if (range.getCode() != null && !"".equals(range.getCode())) {
                        skuSet.add(range.getCode());
                    }
                }
            }

            List<CouponOrderDTO> cdtoListRes = new ArrayList<CouponOrderDTO>();
            for (CouponOrderDTO cdto : cdtoListNo) {
                Long bid = cdto.getBrandId();
                Long cidT3 = cdto.getThordCategoryId();
                Long cidT2 = cdto.getSecondCategoryId();
                Long cidT1 = cdto.getFirstCategoryId();
                String sku = cdto.getSku();
                if (brandIdSet.size() == 0 && cidSetT3.size() == 0 && skuSet.size() == 0 && cidSetT2.size() == 0 && cidSetT1.size() == 0) {
                    cdtoListRes = cdtoList;
                } else {
                    if (brandIdSet != null && brandIdSet.size() > 0) {
                        if (brandIdSet.contains(bid)) {
                            cdtoListRes.add(cdto);
                            continue;
                        }
                    }
                    if (cidSetT3 != null && cidSetT3.size() > 0) {
                        if (cidSetT3.contains(cidT3)) {
                            cdtoListRes.add(cdto);
                            continue;
                        }
                    }
                    else if (cidSetT2 != null && cidSetT2.size() > 0) {
                        if (cidSetT2.contains(cidT2)) {
                            cdtoListRes.add(cdto);
                            continue;
                        }
                    }
                    else if (cidSetT1 != null && cidSetT1.size() > 0) {
                        if (cidSetT1.contains(cidT1)) {
                            cdtoListRes.add(cdto);
                            continue;
                        }
                    }
                    if (skuSet != null && skuSet.size() > 0) {
                        if (skuSet.contains(sku)) {
                            cdtoListRes.add(cdto);
                            continue;
                        }
                    }

                }
            }

            //到此为止，cdtoListRes 里为所有符合要求的商品
            if (cdtoListRes.size() > 0) {
                if (couponInfo.getCouponType() == CouponType.NO_CONDITION.ordinal()) {//红包 ，没有满额，直接可用
                    canUse = true;
                } else {         //优惠券，必须满额
                    BigDecimal totalPrice = BigDecimal.ZERO;
                    for (CouponOrderDTO cdto : cdtoListRes) {
                        totalPrice = totalPrice.add(new BigDecimal(cdto.getPrice().toString()).multiply(new BigDecimal(cdto.getQuantity())));
                    }
                    if (totalPrice.doubleValue() >= couponInfo.getNeedOverMon()) {
                        canUse = true;
                    }
                }
            }


            if (canUse) {
                logger.debug(couponId + "can use................");
                addCanUseDTO(resDtoList, couponUserInfo, couponUserId, couponInfo);
            }

        }// end for

        return resDtoList;

    }


    private void addCanUseDTO(List<OrderCouponDTO> dtoList, CouponUser couponUser, long couponUserId, Coupon coupon) throws Exception {
        OrderCouponDTO couponDto = new OrderCouponDTO();
        couponDto.setCouponUserId(couponUserId);
        couponDto.setCouponName(coupon.getCouponName());
        couponDto.setCouponType(coupon.getCouponType());
        couponDto.setFaceValue(coupon.getFaceValue());
        couponDto.setNeedOverMon(coupon.getNeedOverMon());
        couponDto.setSourceId(coupon.getSourceId());
        couponDto.setSourceName(coupon.getSourceName());
        couponDto.setSourceType(coupon.getSourceType());

        if (Integer.valueOf(coupon.getCouponUseType()) == 0) {
            if (coupon.getCouponUseStime() != null)
                couponDto.setCouponUseStime(coupon.getCouponUseStime());
            if (coupon.getCouponUseStime() != null)
                couponDto.setCouponUseEtime(coupon.getCouponUseEtime());
        } else {
            Integer receiveDay = coupon.getUseReceiveDay();
            Date receiveTime = couponUser.getCreateTime();
            if (receiveDay != null && receiveTime != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(receiveTime);
                calendar.add(Calendar.DAY_OF_MONTH, receiveDay);
                Date newTime = calendar.getTime();

                couponDto.setCouponUseStime(receiveTime);
                couponDto.setCouponUseEtime(newTime);
            }
        }

        dtoList.add(couponDto);
    }

    private void checkCartLineTotalPrice(List<CouponOrderDTO> cdtoList,
                                         Coupon Coupon, Set<Long> brandIdSet, Set<Long> cidSetT, Set<String> skuSet) {
        if (Coupon.getCouponType() == CouponType.HAS_CONDITION.ordinal()) {
            logger.debug(" in HAS_CONDITION ...........");
            Map<Long, BigDecimal> brandTotalPriceMap = new HashMap<Long, BigDecimal>();
            Map<Long, BigDecimal> categoryTotalPriceMap = new HashMap<Long, BigDecimal>();
            Map<String, BigDecimal> skuTotalPriceMap = new HashMap<String, BigDecimal>();

            // 取满足总价的商品信息
            for (CouponOrderDTO cdto : cdtoList) {
                BigDecimal totalPrice = BigDecimal.ZERO;
                totalPrice = totalPrice
                        .add(new BigDecimal(cdto.getPrice().toString())
                                .multiply(new BigDecimal(cdto.getQuantity())));

                if (totalPrice.doubleValue() >= Coupon.getNeedOverMon()) {
                    processCouponOrderDTO(brandIdSet, cidSetT, skuSet, cdto);
                } else {
                    // 不满足，不处理 TODO
                    Long brandId = cdto.getBrandId();
                    Long cidT = cdto.getThordCategoryId();
                    String sku = cdto.getSku();
                    //brand
                    BigDecimal brandTotalPrice = brandTotalPriceMap.get(brandId);
                    if (brandTotalPrice != null) {
                        brandTotalPrice = brandTotalPrice.add(totalPrice);
                        if (brandTotalPrice.doubleValue() >= Coupon.getNeedOverMon()) {
                            processCouponOrderDTO(brandIdSet, cidSetT, skuSet, cdto);
                        }
                        brandTotalPriceMap.put(brandId, brandTotalPrice);
                    } else {
                        brandTotalPrice = BigDecimal.ZERO;
                        brandTotalPrice = brandTotalPrice.add(totalPrice);
                        brandTotalPriceMap.put(brandId, brandTotalPrice);
                    }
                    //sku
                    BigDecimal skuTotalPrice = skuTotalPriceMap.get(sku);
                    if (skuTotalPrice != null) {
                        skuTotalPrice = skuTotalPrice.add(totalPrice);
                        if (skuTotalPrice.doubleValue() >= Coupon.getNeedOverMon()) {
                            processCouponOrderDTO(brandIdSet, cidSetT, skuSet, cdto);
                        }
                        skuTotalPriceMap.put(sku, skuTotalPrice);
                    } else {
                        skuTotalPrice = BigDecimal.ZERO;
                        skuTotalPrice = skuTotalPrice.add(totalPrice);
                        skuTotalPriceMap.put(sku, skuTotalPrice);
                    }
                    //category
                    BigDecimal categoryTotalPrice = categoryTotalPriceMap.get(cidT);
                    if (categoryTotalPrice != null) {
                        categoryTotalPrice = categoryTotalPrice.add(totalPrice);
                        if (categoryTotalPrice.doubleValue() >= Coupon.getNeedOverMon()) {
                            processCouponOrderDTO(brandIdSet, cidSetT, skuSet, cdto);
                        }
                        skuTotalPriceMap.put(sku, skuTotalPrice);
                    } else {
                        categoryTotalPrice = BigDecimal.ZERO;
                        categoryTotalPrice = categoryTotalPrice.add(totalPrice);
                        categoryTotalPriceMap.put(cidT, categoryTotalPrice);
                    }

                }
            }


        } else {
            logger.debug(" in noCONDITION ...........");
            // 现金券，无最低消费
            for (CouponOrderDTO cdto : cdtoList) {
                processCouponOrderDTO(brandIdSet, cidSetT, skuSet, cdto);
            }
        }
        logger.debug("skuList size................." + skuSet.size());
    }

    private Boolean checkCouponRange(Boolean canUse, Set<Long> brandIdSet,
                                     Set<Long> cidSetT, Set<String> skuSet, List<CouponRange> rangeList) {
        for (CouponRange range : rangeList) {
            Long brandId = range.getBrandId();
            Long categoryIdT = range.getCategorySmallId();
            String sku = range.getCode();

            if (brandId != null)// 有品牌的限制,判断商品的品牌是否在其中
                if (brandIdSet.contains(brandId)) {
                    canUse = true;
                    continue;
                }
            if (sku != null && !"".equals(sku))
                if (skuSet.contains(sku)) {
                    canUse = true;
                    continue;
                }
            if (categoryIdT != null)
                if (cidSetT.contains(categoryIdT)) {
                    canUse = true;
                    continue;
                }

        }
        return canUse;
    }

    private Boolean checkCouponRangeNoInclude(Boolean canUse, Set<Long> brandIdSet,
                                              Set<Long> cidSetT,
                                              Set<String> skuSet, List<CouponRange> rangeList) {
        for (CouponRange range : rangeList) {
            Long brandId = range.getBrandId();
            Long categoryIdT = range.getCategorySmallId();
            String sku = range.getCode();

            if (brandId != null)// 有品牌的限制,判断商品的品牌是否在其中
                if (brandIdSet.contains(brandId)) {
                    canUse = false;
                    continue;
                }
            if (sku != null && !"".equals(sku))
                if (skuSet.contains(sku)) {
                    canUse = false;
                    continue;
                }
            if (categoryIdT != null)
                if (cidSetT.contains(categoryIdT)) {
                    canUse = false;
                    continue;
                }

        }
        return canUse;
    }

    // 处理
    private void processCouponOrderDTO(Set<Long> brandIdSet, Set<Long> cidSetT, Set<String> skuSet,
                                       CouponOrderDTO cdto) {
        Long bid = cdto.getBrandId();
        Long cidT = cdto.getThordCategoryId();
        String sku = cdto.getSku();
        logger.error("sku................" + sku);
        if (bid != null && bid != 0l) {
            brandIdSet.add(bid);
        }

        if (cidT != null && cidT != 0l) {
            cidSetT.add(cidT);
        }
        if (sku != null && !"".equals(sku)) {
            skuSet.add(sku);
        }
    }

    @Override
    public OrderCouponDTO queryOrderCoupon(long couponUserId) throws Exception {
        try {
            CouponUser CouponUser = couponUserDao.queryById(couponUserId);
            long couponId = CouponUser.getBatchId();
            Coupon Coupon = couponDao.queryById(couponId);
            OrderCouponDTO couponDto = new OrderCouponDTO();
            couponDto.setCouponUserId(couponUserId);
            couponDto.setCouponName(Coupon.getCouponName());
            couponDto.setCouponType(Coupon.getCouponType());
            couponDto.setFaceValue(Coupon.getFaceValue());
            couponDto.setNeedOverMon(Coupon.getNeedOverMon());
            couponDto.setUseRange(Coupon.getUseRange());
            couponDto.setSourceId(Coupon.getSourceId());
            couponDto.setSourceName(Coupon.getSourceName());
            couponDto.setSourceType(Coupon.getSourceType());
            CouponRange CouponRange = new CouponRange();
            CouponRange.setCouponId(couponId);
            List<CouponRange> crList = couponRangeDao
                    .queryByObject(CouponRange);
            couponDto.setCouponRangeList(crList);
            return couponDto;
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public Boolean deleteCouponUser(Long userId, Long couponUserId)
            throws Exception {
        try {
            CouponUser couponUser = couponUserDao.queryById(couponUserId);
            if (couponUser != null) {
                Long toUserId = couponUser.getToUserId();
                if (toUserId != userId) {
                    throw new Exception("没有权限删除优惠券");
                } else {
                    couponUser.setStatus(CouponUserStatus.DELETED.ordinal());
                    couponUserDao.updateNotNullById(couponUser);
                    return true;
                }
            } else {
                throw new Exception("优惠券不存在！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public PageInfo<CouponUserInfoDTO> queryCouponForBackend(CouponInfoQuery query) {
        try {
            Long couponId = query.getCouponId();
            Long couponUserId = query.getCouponUserId();
            String couponName = query.getCouponName();
            String number = query.getNumber();
            Integer status = query.getStatus();
            Integer pageId = query.getPage();
            Integer pageSize = query.getSize();
            String userLogin = query.getUserLogin();
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("couponId", couponId);
            paramMap.put("couponUserId", couponUserId);
            paramMap.put("couponName", couponName);
            paramMap.put("userLogin", userLogin);
            paramMap.put("number", number);
            paramMap.put("status", status);
            paramMap.put("start", (pageId > 1 ? (pageId - 1) * pageSize : 0));
            paramMap.put("pageSize", pageSize);

            List<CouponUserInfoDTO> resultList = new ArrayList<CouponUserInfoDTO>();
            int totalCount = 0;
            resultList = couponDao.queryCouponForBackend(paramMap);

            totalCount = couponDao.countCouponForBackend(paramMap);
            PageInfo<CouponUserInfoDTO> page = new PageInfo<CouponUserInfoDTO>();
            page.setRows(resultList);
            page.setPage(pageId);
            page.setSize(pageSize);
            page.setRecords(totalCount);
            return page;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Boolean invalidCouponUser(Long couponUserId) throws Exception {
        CouponUser couponUser = couponUserDao.queryById(couponUserId);
        if (couponUser != null) {
            couponUser.setStatus(CouponUserStatus.INVALID.ordinal());
            couponUserDao.updateNotNullById(couponUser);
            return true;
        } else
            throw new Exception("优惠券不存在！");
    }

    // TODO:提取重复代码
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderCouponDTO issueSeaGoorFansCouponUser(Long userId, Long fansActId, String fansActName, String refCode, String mobile, String nickName)
            throws Exception {

        return null;
    }

    @Override
    public ResultInfo<CouponUser> receiveCouponUserForExchange(Long couponId, Long memberId, String mobile, String nickName,Long promoterId) {

        Coupon coupon = couponDao.queryById(couponId);
        if (coupon == null) {
            logger.error("RECEIVE_COUPON_ERROR_COUPON_IS_NULL.ID="+String.valueOf(couponId));
            throw new ServiceException("此券已失效");
        } else {
            if (Integer.valueOf(coupon.getStatus()) != CouponStatus.PASSED.ordinal()) {
                logger.error("RECEIVE_COUPON_ERROR_COUPON_STATUS_IS_NOT_PASSED.ID="+String.valueOf(couponId));
                throw new ServiceException("此券已失效");
            }
            Date now = new Date();
            if (coupon.getCouponReleaseStime() != null) {
                if (now.before(coupon.getCouponReleaseStime()))
                    throw new ServiceException("领券尚未开始，请稍后再来");
            }
            if (coupon.getCouponReleaseEtime() != null) {
                if (now.after(coupon.getCouponReleaseEtime())){
                    logger.error("RECEIVE_COUPON_ERROR_COUPON_CUR_AFTER_RELEASE_TIME.ID="+String.valueOf(couponId));
                    throw new ServiceException("此券已失效 ");

                }
            }
            // 发放的总数限制
            Integer couponCount = coupon.getCouponCount();
            long restCount = 0;
            if (couponCount != null && couponCount != -1) {
                // 统计已经发放的数量
                CouponUser couponUser = new CouponUser();
                couponUser.setBatchId(couponId);
                Integer issueCount = couponUserDao.queryByObjectCount(couponUser);
                if (couponCount <= issueCount) {
                    throw new ServiceException("优惠券已经领取完，无法领取");

                } else {
                    restCount = couponCount - issueCount;
                    if (restCount <= 0) {
                        throw new ServiceException("优惠券已经领取完，无法领取");
                    }
                }
            }

            MemberInfo user = memberInfoService.queryById(memberId);
            if (user == null)
                throw new ServiceException("用户不存在！");
            CouponUser couponUser = new CouponUser();
            couponUser.setBatchId(couponId);
            couponUser.setToUserId(memberId);
            couponUser.setPromoterId(promoterId);
            //根据兑换码兑换，一个用户暂时可以领取10张   如果是推广员卡券则不限制张数
            if (promoterId == null && couponUserDao.queryByObjectCount(couponUser) >= 10) {
                throw new ServiceException("一个人最多领10张");

            }
            couponUser = addCouponUserRecord(memberId, user.getMobile(), coupon, couponId, now, user, couponUser);
            return new ResultInfo<CouponUser>(couponUser);
        }
    }


    private CouponUser addCouponUserRecord(Long memberId, String mobile, Coupon coupon, Long couponId, Date now, MemberInfo user, CouponUser couponUser) {
        String number = couponId
                + "_"
                + memberId
                + "_"
                + DateTimeFormatUtil
                .formatDateyyyyMMddHHmmssForTitle(now);
        couponUser.setNumber(number);
        couponUser.setStatus(CouponUserStatus.NORMAL.ordinal());
        couponUser.setCreateTime(now);
        couponUser.setUpdateTime(now);
        couponUser.setSource(CouponUserSource.RECEIVE.getValue());
        couponUser.setSourceName(CouponUserSource.RECEIVE.getDescription());
        couponUser.setCreateUserId(memberId);
        couponUser.setCreateUserName(user.getNickName());
        couponUser.setToUserLogin(user.getEmail() == null ? "" : user.getEmail());
        couponUser.setToUserMobile(user.getMobile());
        couponUser.setCouponType(coupon.getCouponType());
        couponUser.setRefCode("");

        if (Integer.valueOf(coupon.getCouponUseType()) == 0) {
            couponUser.setCouponUseStime(coupon.getCouponUseStime());
            couponUser.setCouponUseEtime(coupon.getCouponUseEtime());
        } else {
            couponUser.setCouponUseStime(now);
            Integer days = coupon.getUseReceiveDay();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DAY_OF_MONTH, days);
            Date newTime = calendar.getTime();
            couponUser.setCouponUseEtime(newTime);
        }
        couponUser.setBatchNum(0);


        couponUserDao.insert(couponUser);
        logger.info("领取优惠券成功:" + user.getMobile());
        return couponUser;
    }


    @Override
    @Transactional
    public ResultInfo receiveCoupon(CouponReceiveDTO receiveCoupon) {
        AssertUtil.notNull(receiveCoupon, "参数错误");
        String code = receiveCoupon.getCode();
        Long memberId = receiveCoupon.getMemberId();
        String mobile = receiveCoupon.getMobile();
        if (StringUtils.isBlank(code)) return new ResultInfo(new FailInfo("优惠券批次为空", -1));
        if (StringUtils.isBlank(mobile) && memberId == null)
            return new ResultInfo(new FailInfo(ResultMessage.FAIL, "参数异常", -1));
        String lockKey = "coupon_user_" + code + "_" + (memberId == null ? mobile : memberId.toString());//高并发领券
        boolean concurrent = false;
        try {

            Coupon coupon = new Coupon();
            coupon.setCode(code);
            List<Coupon> couponList = couponDao.queryByObject(coupon);
            if (null != couponList && couponList.size() > 0) {
                coupon = couponList.get(0);
            } else {
                return new ResultInfo(new FailInfo("优惠券已失效", -1));
            }
            if (null == coupon) {
                return new ResultInfo(new FailInfo("优惠券已失效", -1));
            } else {
                if (coupon.getOfferType() == CouponOfferType.SEND.getCode()) {
                    return new ResultInfo(new FailInfo("优惠券不支持领取", -1));
                }
                Long couponId = coupon.getId();
                if (Integer.valueOf(coupon.getStatus()) != CouponStatus.PASSED.ordinal()) {
                    return new ResultInfo(new FailInfo("优惠券批次不正常，无法领取", -1));
                }
                Date now = new Date();
                if (coupon.getCouponReleaseStime() != null) {
                    if (now.before(coupon.getCouponReleaseStime()))
                        return new ResultInfo(new FailInfo("领券活动尚未开始，请稍后再来", -11));
                }
                if (coupon.getCouponReleaseEtime() != null) {
                    if (now.after(coupon.getCouponReleaseEtime()))
                        return new ResultInfo(new FailInfo("已过了优惠券的领取时间", -12));
                }
                // 发放的总数限制
                Integer couponCount = coupon.getCouponCount();
                long restCount = 0;
                if (couponCount != null && couponCount != -1) {
                    // 统计已经发放的数量
                    CouponUser couponUser = new CouponUser();
                    couponUser.setBatchId(couponId);
                    long issueCount = couponUserDao.queryByObjectCount(couponUser);
                    if (couponCount <= issueCount) {
                        return new ResultInfo(new FailInfo("优惠券已经领取完，无法领取", -13));
                    } else {
                        restCount = couponCount - issueCount;
                        if (restCount <= 0) {
                            return new ResultInfo(new FailInfo("优惠券已经领取完，无法领取", -13));
                        }
                    }
                }

                if (null != jedisCacheUtil.getCache(lockKey)) {
                    concurrent = true;
                    return new ResultInfo(new FailInfo("领券异常-CLASH_RECEIVE", -1));
                }
                jedisCacheUtil.setCache(lockKey, "1",10);

                if (memberId != null) {
                    MemberInfo memberInfo = memberInfoService.queryById(memberId);
                    if (memberInfo == null) {
                        logger.error("GET_MEMBER_INFO_FAIL.MEMBER_ID={}", memberId);
                        return new ResultInfo(new FailInfo("用户不存在", -1));
                    }
                    mobile = memberInfo.getMobile();
                }
                //如果手机号已经注册,则关已联注册用户id
                else if (StringUtils.isNotBlank(mobile)) {
                    if (!VerifyUtil.verifyTelephone(mobile)) return new ResultInfo(new FailInfo("手机号格式错误", -14));
                    MemberInfo memberInfo = memberInfoService.getByMobile(mobile);
                    if (memberInfo != null)
                        memberId = memberInfo.getId();
                }

                CouponUser couponUser = new CouponUser();
                couponUser.setBatchId(couponId);
                couponUser.setToUserMobile(mobile);
                couponUser.setToUserId(memberId);
                if (couponUserDao.queryByObjectCount(couponUser) != 0) {
                    return new ResultInfo(new FailInfo("不可重复领取", -3));
                }
                addCouponUser(coupon, couponId, now, couponUser, 1);
            }
            return new ResultInfo();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultInfo(new FailInfo("系统繁忙,请稍后再试",-1));
        }finally {
            if(!concurrent)
            jedisCacheUtil.deleteCacheKey(lockKey);
        }

    }


    private void addCouponUser(Coupon coupon, Long couponId, Date now, CouponUser couponUser, Integer salt) {
        String number = couponId + "_" + (couponUser.getToUserId() == null ? couponUser.getToUserMobile() : couponUser.getToUserId())+ (salt == null ? "" : salt) + "_" + DateTimeFormatUtil.formatDateyyyyMMddHHmmssForTitle(now);
        couponUser.setNumber(number);
        couponUser.setStatus(CouponUserStatus.NORMAL.ordinal());
        couponUser.setCreateTime(now);
        couponUser.setUpdateTime(now);
        couponUser.setSource(CouponUserSource.RECEIVE.getValue());
        couponUser.setSourceName(CouponUserSource.RECEIVE.getDescription());
        couponUser.setRefCode("");
        couponUser.setBatchNum(0);
        couponUser.setCreateUserName("领取优惠券");
        couponUser.setToUserLogin("");
        couponUser.setCreateUserId(couponUser.getToUserId() == null ? -1 : couponUser.getToUserId());

        if (couponUser.getToUserId() == null) couponUser.setToUserId(-1L);

        couponUser.setCouponType(coupon.getCouponType());
        try {
            if (Integer.valueOf(coupon.getCouponUseType()) == 0) {
                couponUser.setCouponUseStime(coupon.getCouponUseStime());
                couponUser.setCouponUseEtime(coupon.getCouponUseEtime());
            } else {
                couponUser.setCouponUseStime(now);
                Integer days = coupon.getUseReceiveDay();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                calendar.add(Calendar.DAY_OF_MONTH, days);
                Date newTime = calendar.getTime();
                couponUser.setCouponUseEtime(newTime);
            }
        } catch (Exception e) {
            logger.error("设置 优惠券的是使用时间出错！");
        }
        couponUserDao.insert(couponUser);
        logger.info("领取优惠券成功:" + JSON.toJSONString(couponUser));
    }


    /**
     * 关联仅有手机号的优惠券到用户
     *
     * @param userId
     */
    private void correlationCoupon(Long userId) {
        if (userId == null) return;
        MemberInfo memberInfo = memberInfoService.queryById(userId);
        if (memberInfo == null || memberInfo.getMobile() == null || memberInfo.getMobile().isEmpty()) {
            logger.error("GET_MEMBER_INFO_ERROR,USER_NOT_EXIST_OR_USER_MOBILE_IS_EMPTY.USER_ID={}", userId);
            return;
        }

        List<CouponUser> couponUsers = couponUserDao.queryCouponWithoutMemberIdByMobile(memberInfo.getMobile());
        if (CollectionUtils.isEmpty(couponUsers)) return;

        List<Long> ids = new ArrayList<>();
        for (CouponUser couponUser : couponUsers) {
            ids.add(couponUser.getId());
        }
        int count = couponUserDao.updateForCorrelation(ids, memberInfo.getId());
        logger.info("CORRELATION_USER_COUPON.EFFECTIVE_COUNT={},USER_ID={}", count, memberInfo.getId());
    }


	@Override
	public List<OrderCouponDTO> queryCouponUserByIds(List<Long> idList) {
		if(CollectionUtils.isEmpty(idList)){
			return new ArrayList<OrderCouponDTO>();
		}
		Map<String,Object> params = new HashMap<String,Object>();
		List<OrderCouponDTO> orderCouponDTOList = new ArrayList<OrderCouponDTO>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(idList, SPLIT_SIGN.COMMA)+")");
		List<CouponUser> couponUserList = couponUserDao.queryByParam(params);
		if(!CollectionUtils.isEmpty(couponUserList)){
			idList.clear();
			for(CouponUser couponUser:couponUserList){
				OrderCouponDTO couponDto = new OrderCouponDTO();
		        couponDto.setCouponUserId(couponUser.getCreateUserId());
		        couponDto.setCouponId(couponUser.getBatchId());
		        couponDto.setCouponStatus(couponUser.getStatus());
		        orderCouponDTOList.add(couponDto);
				idList.add(couponUser.getBatchId());
			}
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(idList, SPLIT_SIGN.COMMA)+")");
			List<Coupon> couponList = couponDao.queryByParam(params);
			for(OrderCouponDTO orderCouponDTO:orderCouponDTOList){
				for(Coupon coupon:couponList){
					if(orderCouponDTO.getCouponId().equals(coupon.getId())){
						orderCouponDTO.setCouponName(coupon.getCouponName());
						orderCouponDTO.setCouponType(coupon.getCouponType());
						orderCouponDTO.setFaceValue(coupon.getFaceValue());
						orderCouponDTO.setNeedOverMon(coupon.getNeedOverMon());
						orderCouponDTO.setUseRange(coupon.getUseRange());
						orderCouponDTO.setSourceId(coupon.getSourceId());
						orderCouponDTO.setSourceName(coupon.getSourceName());
						orderCouponDTO.setSourceType(coupon.getSourceType());
					}
				}
			}
			List<CouponRange> couponRangeList = couponRangeDao.queryByParam(params);
			for(OrderCouponDTO orderCouponDTO:orderCouponDTOList){
				for(CouponRange couponRange:couponRangeList){
					if(orderCouponDTO.getCouponId().equals(couponRange.getCouponId())){
						List<CouponRange> couponRanges = orderCouponDTO.getCouponRangeList();
						if(null==couponRanges){
							couponRanges = new ArrayList<CouponRange>();
						}
						couponRanges.add(couponRange);
						orderCouponDTO.setCouponRangeList(couponRanges);
					}
				}
			}
		}
		return orderCouponDTOList;
	}

}
