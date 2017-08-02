package com.tp.proxy.mmp.facade;

import com.tp.common.util.mmp.AssertUtil;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.*;
import com.tp.exception.ServiceException;
import com.tp.proxy.mmp.callBack.AbstractProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.query.mmp.CouponOrderQuery;
import com.tp.query.mmp.MyCouponQuery;
import com.tp.service.mmp.ICouponUserService;
import com.tp.service.mmp.IExchangeCouponChannelCodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 优惠券相关proxy
 * Created by ldr on 2016/1/10.
 */
@Service
public class CouponFacadeProxy extends AbstractProxy {

    @Autowired
    private IExchangeCouponChannelCodeService exchangeCouponChannelCodeService;

    @Autowired
    private ICouponUserService couponUserService;

    /**
     * 获取我的优惠券
     * 必须参数： 用户Id，优惠券使用状态
     *
     * @param query#
     * @return
     */
    public ResultInfo<PageInfo<MyCouponDTO>> myCoupon(final MyCouponQuery query) {
        final ResultInfo<PageInfo<MyCouponDTO>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                AssertUtil.notNull(query, "参数错误");
                AssertUtil.notNull(query.getMemberId(), "用户Id为空");
                AssertUtil.notNull(query.getCouponUserStatus(), "优惠券使用状态为空");
                Integer page = query.getStartPage() < 1 ? 1 : query.getStartPage();
                Integer size = query.getPageSize() < 1 ? 10 : query.getPageSize();
                Integer couponType = query.getCouponType() == null ? null : query.getCouponType().ordinal();
                Integer userStatus = query.getCouponUserStatus() == null ? null : query.getCouponUserStatus().ordinal();

                PageInfo<MyCouponDTO> pageInfo = couponUserService.myCoupon(query.getMemberId(), couponType, userStatus, page, size);
                result.setData(pageInfo);

            }
        });
        return result;
    }

    /**
     * 兑换优惠券
     * 必须参数：用户Id，兑换码
     *
     * @param code
     * @return
     */
    public ResultInfo<Boolean> exchangeCouponsCode(ExchangeCouponCodeDTO code) {
    	try{
    		ResultInfo resultInfo = exchangeCouponChannelCodeService.exchangeCouponsCode(code);
    		if(resultInfo.isSuccess()) return new ResultInfo<>(Boolean.TRUE);
    		return new ResultInfo<>(Boolean.FALSE);
    	}catch(ServiceException se){
    		return new ResultInfo<>(new FailInfo(se.getMessage()));
    	}catch(Exception se){
    		return new ResultInfo<>(new FailInfo("兑换失败"));
    	}
      /*  final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void processItemData() throws Exception {
                ResultInfo resultInfo = exchangeCouponChannelCodeService.exchangeCouponsCode(code);
                if (!resultInfo.isSuccess()) {
                    result.setMsg(resultInfo.getMsg());
                }
                result.setSuccess(resultInfo.isSuccess());
            }
        });
        return result;*/
    }

    /**
     * 获取用户优惠券数量
     * 必须参数：用户Id
     *
     * @param query
     * @return
     */
    public ResultInfo<MyCouponBasicDTO> myCouponBasicInfo(final MyCouponQuery query) {
        final ResultInfo<MyCouponBasicDTO> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                AssertUtil.notNull(query, "参数为空");
                AssertUtil.notNull(query.getMemberId(), "用户Id为空");
                MyCouponBasicDTO myCouponBasicDTO = couponUserService.myCouponBasicInfo(query.getMemberId());
                result.setData(myCouponBasicDTO);
            }
        });
        return result;
    }

    /**
     * 订单之前，获得本订单可用的优惠券
     *
     * @param query
     * @return
     */
    public ResultInfo<List<OrderCouponDTO>> queryOrderCouponList(final CouponOrderQuery query) {
    	final ResultInfo<List<OrderCouponDTO>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                List<OrderCouponDTO> orderCouponDTOList = couponUserService.queryOrderCouponList(query);
                result.setData(orderCouponDTOList);
            }
        });
        return result;
    }

    /**
     * 领取优惠券
     * userId和mobile必填一项
     * code必填
     * 重复领取返回错误码 -3
     * @param receiveCoupon
     * @return
     */
    public ResultInfo receiveCoupon(final CouponReceiveDTO receiveCoupon){
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                ResultInfo resultInfo = couponUserService.receiveCoupon(receiveCoupon);
                if(!resultInfo.isSuccess()){
                    result.setMsg(resultInfo.getMsg());
                }
                result.setSuccess(resultInfo.isSuccess());
            }
        });
        return result;
    }


}
