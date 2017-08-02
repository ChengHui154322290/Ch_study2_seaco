package com.tp.seller.ao.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.ord.LogTypeConstant;
import com.tp.common.vo.ord.RejectConstant;
import com.tp.common.vo.ord.OrderErrorCodes.CUSTOMER_SERVICE_ERROR_CODE;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.RejectLog;
import com.tp.model.ord.SubOrder;
import com.tp.query.ord.RejectQuery;
import com.tp.result.ord.RejectAuditDTO;
import com.tp.result.ord.SubOrderExpressInfoDTO;
import com.tp.seller.ao.base.SellerBaseAO;
import com.tp.seller.util.SessionUtils;
import com.tp.service.ord.IRejectInfoService;
import com.tp.service.ord.IRejectItemService;
import com.tp.service.ord.IRejectLogService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.remote.ISalesOrderRemoteService;

@Service
public class SellerRefundAO extends SellerBaseAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(SellerRefundAO.class);

    @Autowired
    private IRejectInfoService rejectInfoService;

    @Autowired
    private IRejectItemService rejectItemService;

    @Autowired
    private ISubOrderService subOrderService;

    @Autowired
    private IRejectLogService rejectLogService;

    @Autowired
    private ISalesOrderRemoteService salesOrderRemoteService;

    public PageInfo<RejectInfo> queryRefundByCondition(final RejectQuery rejectQuery, final HttpServletRequest request) {

        final Long supplierId = SessionUtils.getSupplierId(request);
        if (null == supplierId) {
            LOGGER.info("supplierId is null");
        }

        rejectQuery.setSupplierId(supplierId);
        final Integer start = getIntValue(request, "start");
        rejectQuery.setStartPage(start);

        return rejectInfoService.queryPageListByRejectQuery(rejectQuery);

    }

    public SubOrder queryOrder(final Long orderNo) {
        final SubOrder query = new SubOrder();
        query.setOrderCode(orderNo);
        final List<SubOrder> subOrderDOs = subOrderService.queryByObject(query);
        if (CollectionUtils.isNotEmpty(subOrderDOs)) {
            final SubOrder subOrderDTO = new SubOrder();
            BeanUtils.copyProperties(subOrderDOs.get(0), subOrderDTO);
            return subOrderDTO;
        }
        return null;
    }

    public List<RejectLog> queryRejectLog(final Long rejectNo) {
        final RejectLog rejectLogDO = new RejectLog();
        rejectLogDO.setRejectCode(rejectNo);
        final List<RejectLog> logList = rejectLogService.queryByObject(rejectLogDO);
        return logList;
    }

    public RejectInfo queryRejectItem(final Long rejectId) {
        return rejectInfoService.queryRejectItemByRejectId(rejectId);
    }

    public ResultInfo<Boolean> auditReject(final RejectAuditDTO rejectAudit) {
        final RejectInfo reject = new RejectInfo();
        final Long rejectId = rejectAudit.getRejectId();
        if (null == rejectId) {
        	return new ResultInfo<Boolean>(new FailInfo(CUSTOMER_SERVICE_ERROR_CODE.REJECTID_NULL.value,CUSTOMER_SERVICE_ERROR_CODE.REJECTID_NULL.code));
        }

        final RejectInfo rejectItemInfo = rejectInfoService.queryById(rejectId);
        if (null == rejectItemInfo) {
        	return new ResultInfo<Boolean>(new FailInfo(CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_NULL.value,CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_NULL.code));
        }

        if (!rejectItemInfo.getRejectCode().equals(Long.valueOf(rejectAudit.getRejectNo()))) {
        	return new ResultInfo<Boolean>(new FailInfo(CUSTOMER_SERVICE_ERROR_CODE.REJECTNO_ERROR.value,CUSTOMER_SERVICE_ERROR_CODE.REJECTNO_ERROR.code));
        }

        Integer auditStatus = rejectItemInfo.getAuditStatus();

        // String refundNo = null;

        if (!RejectConstant.REJECT_AUDIT_STATUS.SELLER_AUDITING.code.equals(auditStatus)) {
        	return new ResultInfo<Boolean>(new FailInfo(CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_AUDIT_ERROR.value,CUSTOMER_SERVICE_ERROR_CODE.REJECTINFO_AUDIT_ERROR.code));
        }

        if (rejectAudit.getSuccess()) {
            auditStatus = RejectConstant.REJECT_AUDIT_STATUS.SELLER_AUDITED.code;
        } else {
            auditStatus = RejectConstant.REJECT_AUDIT_STATUS.SELLER_FIAL.code;
        }

        reject.setOrderCode(rejectItemInfo.getOrderCode());
        reject.setRejectCode(Long.valueOf(rejectAudit.getRejectNo()));
        reject.setAuditStatus(auditStatus);
        reject.setSellerRemarks(rejectAudit.getRemark());
        reject.setRejectId(rejectItemInfo.getRejectId());
        reject.setRefundAmount(rejectItemInfo.getRefundAmount());
        reject.setPoints(rejectItemInfo.getPoints());
        reject.setSellerImgUrl(rejectAudit.getAuditImage());

        LOGGER.info("rejectInfoService.updateForSellerAudit: input params {}", ToStringBuilder.reflectionToString(reject));

        rejectInfoService.updateForSellerAudit(reject, LogTypeConstant.LOG_TYPE.USER.code, rejectAudit.getCreateUser());
        return new ResultInfo<Boolean>(Boolean.TRUE);
    }

    public List<SubOrderExpressInfoDTO> queryExpressInfo(final Long rejectNo, final String expressNo) {
        final List<SubOrderExpressInfoDTO> queryExpressLogInfo = salesOrderRemoteService.queryExpressLogInfo(rejectNo, expressNo);
        return queryExpressLogInfo;
    }

}
