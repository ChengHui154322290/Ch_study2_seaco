package com.tp.service.mmp;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.CouponDto;
import com.tp.exception.ServiceException;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.CouponAudit;
import com.tp.model.mmp.Topic;
import com.tp.service.IBaseService;

import java.util.List;

/**
  * @author szy
  * 优惠券信息表接口
  */
public interface ICouponService extends IBaseService<Coupon>{


    /***
     * 获取优惠券信息
     * @param id
     * @return
     * @throws ServiceException
     */
    CouponDto getCouponInfosById(Long id)throws ServiceException;

    /**
     * 驳回优惠券审批
     * @param couponId
     * @param userId
     * @param userName
     * @param remark
     * @return
     */
    public ResultInfo refuseCoupon(Long couponId, Long userId, String userName, String remark) throws ServiceException ;

    /**
     * 审核通过优惠券
     * @param couponId
     * @param userId
     * @param userName
     * @param remark
     * @return
     * @throws ServiceException
     */
    public ResultInfo approveCoupon(Long couponId, Long userId, String userName) throws ServiceException ;

    /**
     * 取消优惠券
     * @param couponId
     * @param userId
     * @param userName
     * @return
     * @throws ServiceException
     */
    public ResultInfo cancelCoupon(Long couponId, Long userId, String userName) throws ServiceException ;

    /**
     * 查询审核记录
     * @param id
     * @return
     * @throws ServiceException
     */
    List<CouponAudit> queryCouponAudit(Long id) throws ServiceException ;


    PageInfo<Coupon> queryPageByObjectWithLike(Coupon coupon,PageInfo<Coupon> pageInfo);
    
    /**
     * 根据券ID列表查询券详细信息
     * @param idList
     * @return
     */
    public List<Coupon> queryCouponByCouponIdList(List<Long> idList);

    
    public String queryOfflineCouponCode(final String couponCodeKey);
    
    /**
     * 激活，失效
     * @param coupon
     * @return
     */
	Boolean activeCoupon(Coupon coupon);
}
