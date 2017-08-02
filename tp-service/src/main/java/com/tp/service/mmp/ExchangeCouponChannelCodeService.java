package com.tp.service.mmp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tp.common.dao.BaseDao;
import com.tp.common.util.mmp.AssertUtil;
import com.tp.common.util.mmp.DataUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mmp.ExchangeCodeConstant;
import com.tp.dao.mmp.CouponDao;
import com.tp.dao.mmp.CouponSendDao;
import com.tp.dao.mmp.ExchangeCouponChannelCodeDao;
import com.tp.dao.mmp.ExchangeCouponChannelDao;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.ExchangeCouponCodeDTO;
import com.tp.dto.mmp.enums.CouponType;
import com.tp.dto.mmp.enums.OfferType;
import com.tp.exception.ServiceException;
import com.tp.model.mem.MemberInfo;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.CouponUser;
import com.tp.model.mmp.ExchangeCouponChannel;
import com.tp.model.mmp.ExchangeCouponChannelCode;
import com.tp.redis.util.JedisDBUtil;
import com.tp.service.BaseService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mmp.ICouponUserService;
import com.tp.service.mmp.IExchangeCouponChannelCodeService;
import com.tp.util.DateUtil;

@Service
public class ExchangeCouponChannelCodeService extends BaseService<ExchangeCouponChannelCode> implements IExchangeCouponChannelCodeService {

    @Autowired
    ICouponUserService couponUserService;

    @Autowired
    private ExchangeCouponChannelCodeDao exchangeCouponChannelCodeDao;

    @Autowired
    private ExchangeCouponChannelDao exchangeCouponChannelDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private IMemberInfoService memberInfoService;
    @Autowired
    private JedisDBUtil jedisDBUtil;


    @Override
    public BaseDao<ExchangeCouponChannelCode> getDao() {
        return exchangeCouponChannelCodeDao;
    }


    /**
     * 批量生成兑换码
     */
    @Override
    public List<ExchangeCouponChannelCode> generateCode(ExchangeCouponChannelCode exchangeCouponChannelCode, Integer num) throws Exception {
        Long b = System.currentTimeMillis();
        //通过md的活动id，查询活动信息
        Long actId = exchangeCouponChannelCode.getActId();
        ExchangeCouponChannel exmd = exchangeCouponChannelDao.queryById(actId);
        int actNum = exmd.getNum();

        //首先判断该优惠券的个数
        Coupon coupon = couponDao.queryById(exchangeCouponChannelCode.getCouponId());
        if (coupon == null) {
            logger.error("您输入的优惠券不存在");
            throw new ServiceException("您输入的优惠券不存在");
        }
        if (coupon.getOfferType() != null && coupon.getOfferType() == OfferType.ONLY_SEND.ordinal()) {
            throw new ServiceException("优惠券[" + exchangeCouponChannelCode.getCouponId() + "]仅可用于发放");
        }

        if (coupon.getCouponCount() != null && coupon.getCouponCount().intValue() != -1) {
            ExchangeCouponChannelCode modebak = new ExchangeCouponChannelCode();
            modebak.setCouponId(exchangeCouponChannelCode.getCouponId());
            modebak.setActId(actId);
            int count = exchangeCouponChannelCodeDao.queryByObjectCount(modebak);
            if ((count + num.intValue()) > coupon.getCouponCount().intValue()) {
                logger.error("您此次生成的优惠券超出优惠券设置的张数");
                throw new ServiceException("您此次生成的优惠券超出优惠券设置的张数");

            }
            if ((count + num.intValue()) > actNum) {
                logger.error("您此次生成的优惠券超出兑换码设置的张数");
                throw new ServiceException("您此次生成的优惠券超出优惠券设置的张数");

            }
        }
        Date cur = new Date();
        StringBuilder versionCode = new StringBuilder();
        versionCode.append(DateUtil.formatDate(cur, com.tp.util.DateUtil.LOWER_LONG_FORMAT)).append("-").append(DataUtil.radomCode().substring(0, 8));
        Long couponId = exchangeCouponChannelCode.getCouponId();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("couponId",couponId);
    	Long couponCodeMax = null;
    	String couponSeqKey = "generatecouponcode:seqmax:"+couponId;
        if(jedisDBUtil.lock("generatecouponcode:"+couponId)){
        	if(jedisDBUtil.getDB(couponSeqKey)==null){
        		couponCodeMax = exchangeCouponChannelCodeDao.queryCodeSeqMaxByCouponId(couponId);
	        	jedisDBUtil.setIncr(couponSeqKey, couponCodeMax);
        	}
        }else{
        	  logger.error("系统正在生成优惠券，请稍后。。。");
              throw new ServiceException("系统正在生成优惠券，请稍后。。。");
        }
        
        jedisDBUtil.unLock("generatecouponcode:"+couponId);
        List<ExchangeCouponChannelCode> exchangeCouponChannelCodes = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            ExchangeCouponChannelCode insertMode = new ExchangeCouponChannelCode();
            insertMode.setCreateUser(exchangeCouponChannelCode.getCreateUser());
            insertMode.setVersionCode(versionCode.toString());
            insertMode.setCreateTime(cur);
            insertMode.setCodeSeq(jedisDBUtil.incr(couponSeqKey));
            insertMode.setUpdateUser(exchangeCouponChannelCode.getCreateUser());
            insertMode.setUpdateTime(cur);
            insertMode.setActId(exchangeCouponChannelCode.getActId());
            insertMode.setCouponId(exchangeCouponChannelCode.getCouponId());
            insertMode.setStatus(0);

            String code = "";
            while (true) {
                code = DataUtil.radomCode();
                //先通过code查看是否数据库有重复的
                ExchangeCouponChannelCode mmd = new ExchangeCouponChannelCode();
                mmd.setExchangeCode(code);
                Integer count = exchangeCouponChannelCodeDao.queryByObjectCount(mmd);
                if (count == null || count == 0) {
                    break;
                }
            }
            //生成兑换码
            insertMode.setExchangeCode(code);
            exchangeCouponChannelCodes.add(insertMode);
        }
        System.out.println("GEN TIME COST:"+(System.currentTimeMillis() - b));
       return exchangeCouponChannelCodes;
    }


    @Override
    @Transactional
    public void batchInsert(List<ExchangeCouponChannelCode> list) {
        long l1 = System.currentTimeMillis();
        if(CollectionUtils.isEmpty(list))return;
        int total = list.size();
        int times = total/1000;
        int balance = total% 1000;
        times = balance==0? times : ++times;

        for(int i = 0; i< times;i++){
            int start = i * 1000;
            int end =  (i+1)* 1000;
            end = end>total ? total : end;
            exchangeCouponChannelCodeDao.batchInsert(list.subList(start,end));

        }

        System.out.println("BAT TIME COST:"+(System.currentTimeMillis() - l1));
    }

    /**
     * 用户进行兑换码兑换
     */
    @Override
    @Transactional
    public ResultInfo exchangeCouponsCode(ExchangeCouponCodeDTO codeModel) {
        Long userId = codeModel.getUserId();
        String exchangeCode = codeModel.getExchangeCode();
        if (codeModel == null || userId == null || exchangeCode == null) {
            logger.error("您当前没有优惠券  逛逛西客商城,用户ID：{},mobile:{}", userId, codeModel.getMobile());
            throw new ServiceException("兑换失败,您的输入有误,请确认后重试");
        }
        //通过兑换码查询该兑换码对应的数据，判断是否存在该兑换码，存在兑换码是否到期，是否被兑换
        ExchangeCouponChannelCode mode = new ExchangeCouponChannelCode();
        mode.setExchangeCode(exchangeCode);
        List<ExchangeCouponChannelCode> lst = new ArrayList<ExchangeCouponChannelCode>();

        lst = exchangeCouponChannelCodeDao.queryByObject(mode);

        //兑换码是唯一的
        if (CollectionUtils.isEmpty(lst)) {
            logger.error("您输入兑换码不存在");
            throw new ServiceException("您输入兑换码不存在");

        }
        ExchangeCouponChannelCode md = lst.get(0);
        if (md.getStatus() == ExchangeCodeConstant.STATUS_USE_EXCHANGE) {
            logger.error("您输入兑换码已被使用");
            throw new ServiceException("您输入兑换码已被使用");
        }
        if(md.getStatus()==ExchangeCodeConstant.STATUS_SEALED){
        	 logger.error("您输入兑换码不能使用");
             throw new ServiceException("您输入兑换码不能使用");
        }
        
        //通过md的活动id，查询活动信息
        Long actId = md.getActId();
        ExchangeCouponChannel exmd = exchangeCouponChannelDao.queryById(actId);

			/*兑换码的有效期没有实质的意思
			Long startDate = exmd.getStartdate().getTime();
			Long endDate = exmd.getEnddate().getTime();
			if(new Date().getTime() > endDate || 
					new Date().getTime() < startDate){
				logger.error("该兑换码不在兑换期间");
				return new ResultMessage(ResultMessage.FAIL, "该兑换码不在兑换期间",
						ErrorCodeType.OTHER.ordinal());
			}*/
        if (exmd.getStatus() != ExchangeCodeConstant.STATUS_ACT_EXCHANGE_NOMAL) {
            logger.error("该兑换码的活动已被终止");
            throw new ServiceException("该兑换码的活动已被终止");

        }

        Long couponId = md.getCouponId();
        //判断优惠券是否到期
        Coupon coupon = new Coupon();
        coupon = couponDao.queryById(couponId);
        AssertUtil.notNull(coupon, "优惠券不存在");

        Date couponReleaseEtime = coupon.getCouponReleaseEtime();

        Date eDate = couponReleaseEtime;
        if (eDate.getTime() < new Date().getTime()) {
            logger.error("该兑换码兑换的优惠券已经到期");
            throw new ServiceException("该兑换码兑换的优惠券已经到期");
        }
        if(coupon.getCouponType()==CouponType.NO_CONDITION.ordinal()){
        	if(Constant.ENABLED.NO.equals(coupon.getActiveStatus())){
        		logger.error("该兑换码兑换的优惠券还未激活");
                throw new ServiceException("该兑换码兑换的优惠券未激活");
        	}
        }

        //生成用户对应的优惠券:Long couponId, Long memberId, String mobile, String nickName
        //同一个用户不能领多次同一个优惠券，这个是在下面接口做处理 // 对于领券，现支持一个人使用多个兑换码兑换多次
        ResultInfo<CouponUser> rs = couponUserService.receiveCouponUserForExchange(couponId, userId, codeModel.getMobile(), codeModel.getNickName(),md.getPromoterId());
        if (!rs.isSuccess()) {

            logger.error("生成优惠券报错");
            throw new ServiceException(rs.getMsg().getMessage());

//				/**兑换码状态回滚**/
//				//修改兑换码为未使用
//				mode.setStatus(0);
//				mode.setUserId(null);
//				mode.setUserName("");
//				exchangeCouponChannelCodeDao.updateRollStatus(mode);
//				//把主表的已领用券再加回去
//				ExchangeCouponChannel dmm = new ExchangeCouponChannel();
//				dmm.setId(md.getActId());
//				dmm.setUseNum(-1);
//				exchangeCouponChannelDao.updateUseNumById(dmm);


        }
        else {
        	//修改兑换码为已使用,且记录用户id和用户名
            mode.setStatus(ExchangeCodeConstant.STATUS_USE_EXCHANGE);
            mode.setMemberId(userId);
            mode.setMemberName(codeModel.getNickName());
            mode.setCouponUserId(rs.getData().getId());
            mode.setUpdateTime(new Date());

            int count = exchangeCouponChannelCodeDao.updateStatus(mode);
            if (count < 1) {//如果一开始状态是未领用，但是并发情况下，前面把状态改成已领用，这次再更新状态时候，更新条数为0，则表示并发情况下，不能在领用了
                logger.error("领用优惠券，出现并发情况");
                throw new ServiceException("您输入兑换码已被使用");
            }
            //修改主表的已领用个数
            ExchangeCouponChannel exchangeCouponChannel = new ExchangeCouponChannel();
            exchangeCouponChannel.setId(md.getActId());
            exchangeCouponChannel.setUseNum(1);
            exchangeCouponChannelDao.updateUseNumById(exchangeCouponChannel);
            
            if(md.getPromoterId() != null && md.getPromoterId() != 0) {
            	MemberInfo member = memberInfoService.queryById(userId);
            	if(member != null && member.getPromoterId() == null) {
            		member.setPromoterId(md.getPromoterId());
            		memberInfoService.updateNotNullById(member);
            	}
            }
        }

        return new ResultInfo();
    }

    @Override
    public List<ExchangeCouponChannelCode> queryByTimeAndStatus(Map<String, Object> params) {
        return exchangeCouponChannelCodeDao.queryByTimeAndStatus(params);
    }

    @Override
    public List<Map<String, String>> queryExchangeCountDetails(Long actId) {
        return exchangeCouponChannelCodeDao.queryExchangeCountDetails(actId);
    }


	@Override
	public PageInfo<ExchangeCouponChannelCode> queryExchangeCouponByParam(ExchangeCouponChannelCode query) {
		if(query.getStartPage()==null){
			query.setStartPage(1);
		}
		if(query.getPageSize()==null){
			query.setPageSize(10);
		}
		PageInfo<ExchangeCouponChannelCode> pageInfo = new PageInfo<ExchangeCouponChannelCode>(query.getStartPage(),query.getPageSize());
		pageInfo.setRecords(exchangeCouponChannelCodeDao.queryExchangeCouponByParamCount(query));
		pageInfo.setRows(exchangeCouponChannelCodeDao.queryExchangeCouponByParam(query));
		return pageInfo;
	}
	
	/**
	 * 绑定卡券到推广员
	 * @param params
	 * @return
	 */
	public Integer updatePromoterIdBind(Map<String,Object> params){
		return exchangeCouponChannelCodeDao.updatePromoterIdBind(params);
	}
	
	/**
	 * 绑定卡券到推广员
	 * @param params
	 * @return
	 */
	public Integer updateCouponStatusEnabled(Map<String,Object> params){
		return exchangeCouponChannelCodeDao.updateCouponStatusEnabled(params);
	}
	/**
	 * 卡券作废
	 * @param params
	 * @return
	 */
	public Integer updateCouponStatusCancled(Map<String,Object> params){
		return exchangeCouponChannelCodeDao.cancleCouponStatusEnabled(params);
	}


	@Override
	public List<ExchangeCouponChannelCode> queryExchangeCouponByParams(Map<String, Object> params) {
		   
		return exchangeCouponChannelCodeDao.queryExchangeCouponByParams(params);
	}
}
