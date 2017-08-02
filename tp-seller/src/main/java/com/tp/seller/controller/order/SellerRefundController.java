package com.tp.seller.controller.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.ord.RejectConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.RejectLog;
import com.tp.model.ord.SubOrder;
import com.tp.query.ord.RejectQuery;
import com.tp.result.ord.RejectAuditDTO;
import com.tp.result.ord.SubOrderExpressInfoDTO;
import com.tp.seller.ao.order.SellerRefundAO;
import com.tp.seller.controller.base.BaseController;
import com.tp.seller.util.SessionUtils;

/**
 * 商家退货
 *
 * @author yfxie
 */
@Controller
@RequestMapping("/seller/refund/")
public class SellerRefundController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SellerRefundController.class);

    @Autowired
    private SellerRefundAO sellerRefundAO;

    /**
     * 退货单列表
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/refundList", method = RequestMethod.GET)
    public String toproductreturn(final ModelMap model, final HttpServletRequest request, final RejectQuery rejectQuery) {
        model.addAttribute("rejectStatusList", RejectConstant.REJECT_STATUS.values());
        model.addAttribute("auditStatusList", RejectConstant.REJECT_AUDIT_STATUS.values());
        model.addAttribute("query", rejectQuery == null ? new RejectQuery() : rejectQuery);
        return "seller/refund/refund_list";
    }

    /**
     * 退货单列表查询 只刷新列表
     */
    @RequestMapping(value = "/refundQuery", method = RequestMethod.POST)
    public String list(final ModelMap model, final RejectQuery rejectQuery, final HttpServletRequest request) {
        PageInfo<RejectInfo> sellerRefundPageInfo = null;
        try {
            sellerRefundPageInfo = sellerRefundAO.queryRefundByCondition(rejectQuery, request);

        } catch (final Exception e) {
            LOGGER.error("获取退货单列表异常", e);
        }
        final boolean isHaitao = SessionUtils.checkIsHaitao(request);
        if (isHaitao) {
            model.addAttribute("isHaitao", isHaitao);
        }
        model.addAttribute("rejectPage", sellerRefundPageInfo);
        return "seller/refund/subpage/subPagelist";
    }

    /**
     * 查看详情
     *
     * @param model
     * @param rejectId
     */
    @RequestMapping(value = { "show", "auditshow" })
    public void show(final Model model, final Long rejectId, final HttpServletRequest request) {
        if (null == rejectId) {
            model.addAttribute("errorMessage", "没有传入参数");
            return;
        }
        final RejectInfo queryRejectItem = sellerRefundAO.queryRejectItem(rejectId);
        if (null == queryRejectItem || CollectionUtils.isEmpty(queryRejectItem.getRejectItemList())) {
            model.addAttribute("errorMessage", "根据传入参数没有查询到退货信息");
            return;
        }

        SubOrder order = null;
        List<RejectLog> logList = null;
        try {
            order = sellerRefundAO.queryOrder(queryRejectItem.getOrderCode());
            logList = sellerRefundAO.queryRejectLog(queryRejectItem.getRejectCode());
        } catch (final Exception e) {
            LOGGER.error("rejectId=" + rejectId + "显示退货单信息出现异常", e);
        }
        // 收货人信息
        model.addAttribute("rejectItem", queryRejectItem.getRejectItemList().get(0));
        model.addAttribute("rejectInfo", queryRejectItem);
        model.addAttribute("order", order);
        final boolean isHaitao = SessionUtils.checkIsHaitao(request);
        if (isHaitao) {
            model.addAttribute("isHaitao", isHaitao);
        }

        if (CollectionUtils.isNotEmpty(logList)) {
            model.addAttribute("logList", logList.get(0));
        }
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.supplier.name());
        model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.supplier.url);
        // return "/seller/refund/refundaudit";
    }

    /**
     * 退货审核
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/refundAudit")
    @ResponseBody
    public ResultInfo<Boolean> toproductreturnaudit(final ModelMap model, final RejectAuditDTO rejectAudit, final HttpServletRequest request) {
        final ModelMap map = new ModelMap();
        if (null == rejectAudit) {
        	return new ResultInfo<>(new FailInfo("审核信息为空"));
        }
        if (StringUtils.isBlank(rejectAudit.getRejectNo())) {
        	return new ResultInfo<>(new FailInfo("退货编号为空"));
        }
        if (null == rejectAudit.getRejectId()) {
        	return new ResultInfo<>(new FailInfo("退货号为空"));
        }
        if (null == rejectAudit.getRejectItemId()) {
        	return new ResultInfo<>(new FailInfo("退货商品为空"));
        }
        if (null == rejectAudit.getSuccess()) {
        	return new ResultInfo<>(new FailInfo("没有审核状态"));
        }
        rejectAudit.setCreateUser(SessionUtils.getUserName(request));
        try {
            return sellerRefundAO.auditReject(rejectAudit);
        } catch (final Exception e) {
            LOGGER.error("商家审核出错： ", e.getMessage());
            LOGGER.error(e.getMessage(), e);
            return new ResultInfo<Boolean>(new FailInfo(e));
        }
    }

    /**
     * 查看快递信息
     * 
     * @param expressNo
     */
    @RequestMapping("refundDelivery")
    public void showDelivery(final Model model, final Long rejectNo, final String expressNo) {
        if (null == expressNo) {
            model.addAttribute("errorMessage", "expressNo参数为空");
            return;
        }
        if (null == rejectNo) {
            model.addAttribute("errorMessage", "rejectNo参数为空");
            return;
        }
        List<SubOrderExpressInfoDTO> queryExpressInfo = null;
        try {
            queryExpressInfo = sellerRefundAO.queryExpressInfo(rejectNo, expressNo);
        } catch (final Exception e) {
            LOGGER.error("rejectNo=" + rejectNo + "," + "expressNo=" + expressNo + "查询物流信息出现异常", e);
        }
        model.addAttribute("queryExpressInfo", queryExpressInfo);
    }

}
