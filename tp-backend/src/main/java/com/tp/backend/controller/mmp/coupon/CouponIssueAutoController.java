/**
 *
 */
package com.tp.backend.controller.mmp.coupon;

import com.alibaba.fastjson.JSON;
import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.CouponDto;
import com.tp.dto.mmp.enums.CouponSendType;
import com.tp.dto.mmp.enums.CouponStatus;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.CouponSend;
import com.tp.model.mmp.CouponSendAudit;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.mmp.CouponProxy;
import com.tp.proxy.mmp.CouponUserProxy;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Controller
@RequestMapping(value = "/coupon/auto")
public class CouponIssueAutoController extends AbstractBaseController {

    Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private CouponUserProxy couponUserProxy;

    @Autowired
    private CouponProxy couponProxy;

    @RequestMapping(value = "/sendListAuto")
    public String autoSendList(Model model) {

        return "coupon/coupon_send_list_auto";
    }

    @RequestMapping(value = "/sendList")
    public String snedList(Model model, CouponSend cdo,
                           @RequestParam(value = "page", defaultValue = "1") Integer page,
                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            if (cdo == null) {
                cdo = new CouponSend();
            }
            ResultInfo<PageInfo<CouponSend>> resultInfo = couponProxy.queryCouponSendList(cdo, page, size);
            model.addAttribute("queryAllCouponSendByPage", resultInfo.getData());
            model.addAttribute("CouponSend", cdo);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return "coupon/coupon_send_list_auto";
    }

    @RequestMapping(value = "/issueForm")
    public String showIssueForm(Model model) {

        return "coupon/couponIssue_auto";
    }

    @RequestMapping(value = "/showCouponSearch")
    public String showCouponSearch(@RequestParam(value = "selectRow") Integer selectRow, Model model) {

        model.addAttribute("selectRow", selectRow);
        return "coupon/couponSearch";
    }

    @RequestMapping(value = "/searchCoupon", method = RequestMethod.POST)
    public String searchCoupon(Coupon coupon, @RequestParam(value = "selectRow") Integer selectRow,
                               @RequestParam(value = "size", defaultValue = "10") Integer size,
                               @RequestParam(value = "page", defaultValue = "1") Integer page, Model model) {

        try {
            PageInfo<Coupon> pageDO = couponProxy.queryValidCoupon(coupon, page, size);
            model.addAttribute("queryAllCouponByPage", pageDO);
            model.addAttribute("selectRow", selectRow);
            model.addAttribute("Coupon", coupon);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "coupon/couponSearch";
    }

    @RequestMapping(value = "/searchCouponName", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo searchCouponName(@RequestParam(value = "couponId") Long couponId) {
        if (null != couponId) {
            Coupon coupon = couponProxy.queryCouponDO(couponId);
            if (null != coupon) {
                if (coupon.getStatus() != CouponStatus.PASSED.ordinal()) {
                    return new ResultInfo(new FailInfo("优惠券状态异常,仅状态为审核通过的才可发放"));
                }
                if (coupon.getCouponReleaseEtime() != null && coupon.getCouponReleaseStime() != null) {
                    try {
                        Date now = new Date();
                        Date stime = (coupon.getCouponReleaseStime());
                        Date etime = (coupon.getCouponReleaseEtime());
                        if (now.after(etime) || now.before(stime)) {
                            return new ResultInfo(new FailInfo("优惠券不在发放时间"));
                        }
                    } catch (Exception e) {
                    }
                }
                return new ResultInfo(coupon.getCouponName());
            }
        }
        return new ResultInfo(new FailInfo("优惠券不存在"));
    }

    @RequestMapping(value = "/searchCouponById", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo searchCouponById(@RequestParam(value = "couponId") Long couponId) {
        if (null != couponId) {
            CouponDto couponDto = couponProxy.getCouponInfosById(couponId);
            if (null != couponDto) {
                return new ResultInfo(couponDto);
            }
        }
        return new ResultInfo(new FailInfo("参数错误"));
    }

    @RequestMapping(value = "/issueCoupon", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo issueCoupon(@RequestParam("batches") String batches,
                                  @RequestParam("msgSend") Integer msgSend,
                                  @RequestParam("msgContent") String msgContent,
                                  @RequestParam("name") String name,
                                  @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                  @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                  @RequestParam(value = "couponsendType", defaultValue = "0") Integer couponsendType,
                                  @RequestParam("couponsendStatus") Integer status,
                                  @RequestParam("users") String users,
                                  @RequestParam("isAll") Integer isAll,
                                  Model model) {
        UserInfo user = getUserInfo();
        if (StringUtils.isBlank(batches)) {
            return new ResultInfo(new FailInfo("批次编号不能为空"));
        }
        return couponProxy.saveCouponSend(batches, users, isAll, user.getId(), user.getLoginName(), msgSend, msgContent, name, status, couponsendType, startTime, endTime);
    }

    /***
     * 终止优惠券
     *
     * @param couponId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/stop_coupon_send")
    @ResponseBody
    public Boolean stopCoupon(Long couponSendId, HttpServletRequest request, HttpServletResponse response) {
        UserInfo user = getUserInfo();
        return couponProxy.stopCouponSend(couponSendId, user).isSuccess();
    }

    /***
     * 驳回审核
     *
     * @param couponId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/refused_coupon_send")
    @ResponseBody
    public Boolean invalidCoupon(Long couponSendId, HttpServletRequest request, HttpServletResponse response) {
        UserInfo user = getUserInfo();
        String reason = request.getParameter("reason");
        return couponProxy.refusedCouponSend(couponSendId, reason, user).isSuccess();
    }

    /**
     * 批准 审核通过
     *
     * @param couponId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/approve_coupon_send")
    @ResponseBody
    public ResultInfo approveCoupon(Long couponSendId, HttpServletRequest request, HttpServletResponse response) {
        UserInfo user = getUserInfo();
        return couponProxy.approveCouponSendAuto(couponSendId, user);
    }

    /**
     * 取消优惠券
     *
     * @param couponId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/cancel_coupon_send")
    @ResponseBody
    public Boolean cancelCoupon(Long couponSendId, HttpServletRequest request, HttpServletResponse response) {
        UserInfo user = getUserInfo();
        return couponProxy.cancelCouponSend(couponSendId, user);
    }

    /***
     * 修改优惠券信息
     *
     * @return
     */
    @RequestMapping(value = "/toEditCouponSend")
    public ModelAndView toEditCouponSend(@RequestParam(value = "id") Long id) throws Exception {
        if (id == null) {
            //ResultUtil.throwExcepion(new ResultInfo(ResultInfo.TYPE_RESULT_FAIL, 909, "id为空,异常"));
        }
        ModelAndView mv = new ModelAndView();
        CouponSend couponSend = couponProxy.queryCouponSendDO(id);
        List<Coupon> couponList = new ArrayList<Coupon>();
        if (couponSend != null) {
            String couponIds = couponSend.getCouponIds();
            String[] cids = couponIds.replace("，", ",").split(",");
            for (String cid : cids) {
                Coupon Coupon = couponProxy.queryCouponDO(Long.parseLong(cid));
                if (Coupon != null)
                    couponList.add(Coupon);
            }
            mv.addObject("couponList", couponList);
        }
        List<CouponSendAudit> auditList = couponProxy.queryCouponSendAudit(id);
        if (auditList != null && auditList.size() > 0)
            mv.addObject("auditList", auditList);
        mv.addObject("couponSend", couponSend);
        mv.addObject("infos", JSON.toJSONString(couponSend));
        mv.setViewName("/coupon/couponIssueEdit_auto");
        mv.addObject("onlyView", false);
        return mv;
    }

    /***
     * 查看优惠券信息
     *
     * @return
     */
    @RequestMapping(value = "/toViewCouponSend")
    public ModelAndView toViewCouponSend(@RequestParam(value = "id") Long id) throws Exception {
        if (id == null) {
            //ResultUtil.throwExcepion(new ResultInfo(ResultInfo.TYPE_RESULT_FAIL, 909, "id为空,异常"));
        }
        ModelAndView mv = new ModelAndView();
        CouponSend couponSend = couponProxy.queryCouponSendDO(id);
        List<Coupon> couponList = new ArrayList<Coupon>();
        if (couponSend != null) {
            String couponIds = couponSend.getCouponIds();
            String[] cids = couponIds.replace("，", ",").split(",");
            for (String cid : cids) {
                Coupon Coupon = couponProxy.queryCouponDO(Long.parseLong(cid));
                if (Coupon != null)
                    couponList.add(Coupon);
            }
            mv.addObject("couponList", couponList);
        }
        List<CouponSendAudit> auditList = couponProxy.queryCouponSendAudit(id);
        if (auditList != null && auditList.size() > 0)
            mv.addObject("auditList", auditList);
        mv.addObject("couponSend", couponSend);
        mv.addObject("infos", JSON.toJSONString(couponSend));
        mv.setViewName("/coupon/couponIssueEdit_auto");
        mv.addObject("onlyView", true);
        return mv;
    }

    @RequestMapping(value = "/issueCouponEdit", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo issueCouponEdit(@RequestParam("batches") String batches,
                                      @RequestParam("msgSend") Integer msgSend,
                                      @RequestParam("msgContent") String msgContent,
                                      @RequestParam("name") String name,
                                      @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                      @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                      @RequestParam(value = "couponsendType", defaultValue = "1") Integer couponsendType,
                                      @RequestParam("couponsendStatus") Integer status,
                                      @RequestParam("id") Long id,
                                      @RequestParam("users") String users,
                                      @RequestParam("isAll") Integer isAll,
                                      Model model) {

        UserInfo user = getUserInfo();
        if (StringUtils.isBlank(batches)) {
            return new ResultInfo(new FailInfo("批次编号不能为空"));
        }
        return couponProxy.updateCouponSend(id, batches, users, isAll, user.getId(), user.getLoginName(), msgSend, msgContent, name, status, couponsendType, startTime, endTime);
    }

}
