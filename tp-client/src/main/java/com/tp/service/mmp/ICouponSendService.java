package com.tp.service.mmp;

import com.tp.dto.common.ResultInfo;
import com.tp.exception.ServiceException;
import com.tp.model.mmp.CouponSend;
import com.tp.model.mmp.CouponSendAudit;
import com.tp.service.IBaseService;

import java.util.List;

/**
  * @author szy
  * 优惠券发放信息表接口
  */
public interface ICouponSendService extends IBaseService<CouponSend> {


    /**
     * 终止优惠券发放审批
     * @param couponSendId
     * @param id
     * @param loginName
     * @return
     * @throws ServiceException
     */
    ResultInfo stopCouponSend(Long couponSendId, Long id, String loginName)  throws ServiceException ;

    /**
     * 驳回优惠券发放审批
     * @param couponSendId
     * @param userId
     * @param userName
     * @param remark
     * @return
     * @throws ServiceException
     */
    public ResultInfo refuseCouponSend(Long couponSendId, Long userId, String userName, String remark) throws ServiceException ;

    /**
     * 审核通过优惠券发放
     * @param couponSendId
     * @param userId
     * @param userName
     * @param res
     * @return
     * @throws ServiceException
     */
    public ResultInfo approveCouponSend(Long couponSendId, Long userId, String userName, List<String> res) throws ServiceException;

    /**
     *  取消优惠券发放
     * @param couponSendId
     * @param userId
     * @param userName
     * @return
     * @throws ServiceException
     */
    public ResultInfo cancelCouponSend(Long couponSendId, Long userId, String userName) throws ServiceException ;

    /**
     * 查询审核记录
     * @param id
     * @return
     * @throws ServiceException
     */
    List<CouponSendAudit> queryCouponSendAudit(Long id) throws ServiceException ;


}
