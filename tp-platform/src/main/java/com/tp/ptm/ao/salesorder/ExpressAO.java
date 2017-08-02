package com.tp.ptm.ao.salesorder;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.ptm.ErrorCodes;
import com.tp.dto.ord.remote.ExpressInfoDTO;
import com.tp.dto.ord.remote.ExpressModifyDTO;
import com.tp.dto.ptm.ReturnData;
import com.tp.dto.ptm.salesorder.ExpressLogQueryErrorDTO;
import com.tp.dto.ptm.salesorder.ExpressLogQueryParamDTO;
import com.tp.dto.ptm.salesorder.ExpressModifyErrorDTO;
import com.tp.dto.ptm.salesorder.OrderMiniDTO;
import com.tp.result.bse.ExpressCompanyDTO;
import com.tp.service.bse.IExpressInfoService;
import com.tp.service.ord.IOrderDeliveryService;
import com.tp.service.ptm.IPlatformAccountService;
import com.tp.service.ptm.IPlatformAccountService.KeyType;

/**
 * 快递信息AO
 *
 * @author ZhuFuhua
 */
@Service
public class ExpressAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressAO.class);

    @Autowired
    private IPlatformAccountService platformAccountService;

    @Autowired
    private IExpressInfoService expressInfoService;
    @Autowired
    private IOrderDeliveryService orderDeliveryService;

    /**
     * 修改快递单号
     * 
     * @param appKey
     * @param expressModifyDTO
     * @return
     */
    public ReturnData modifyExpressNo(final String appKey, final ExpressModifyDTO expressModifyDTO) {
        // 权限验证
        Map<KeyType, List<OrderMiniDTO>> resultMap = null;
        List<Long> orderNoList = new ArrayList<Long>();
        try {
            orderNoList.add(expressModifyDTO.getOrderNo());
            resultMap = platformAccountService.verifySalesOrderAuthOfAccount(appKey, orderNoList);
        } catch (Exception e) {
            LOGGER.error("验证供应商权限异常", e);
            return new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.SYSTEM_ERROR.code,
                ErrorCodes.SystemError.SYSTEM_ERROR.cnName);
        }

        ReturnData rData = new ReturnData(Boolean.TRUE);
        List<OrderMiniDTO> verifyPass = resultMap.get(KeyType.SUCCESS);

        ExpressModifyErrorDTO expressModifyErrorDTO = new ExpressModifyErrorDTO();
        expressModifyErrorDTO.setOrderNo(expressModifyDTO.getOrderNo());
        expressModifyErrorDTO.setOriginalExpressNo(expressModifyDTO.getOriginalExpressNo());
        expressModifyErrorDTO.setNewExpressNo(expressModifyDTO.getNewExpressNo());

        if (CollectionUtils.isEmpty(verifyPass)) {// 验证通过列表为空，则表示验证未通过，直接返回结果
            expressModifyErrorDTO.setErrorCode(ErrorCodes.AuthError.UNPASS_AUTH.code);
            expressModifyErrorDTO.setErrorMsg(ErrorCodes.AuthError.UNPASS_AUTH.cnName);
            rData = new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.BUSINESS_PROCESS_ERROR.code,
                expressModifyErrorDTO);
        } else {
            try {
                ExpressModifyDTO salesExpressModifyDTO = new ExpressModifyDTO();
                BeanUtils.copyProperties(salesExpressModifyDTO, expressModifyDTO);
                com.tp.dto.mmp.ReturnData modifyResultData = orderDeliveryService.modifyExpressNo(salesExpressModifyDTO);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("修改快递编号信息返回结果为：{}", JSONObject.toJSONString(modifyResultData));
                }
                if (!modifyResultData.isSuccess()) {
                    expressModifyErrorDTO.setErrorCode(modifyResultData.getErrorCode());
                    expressModifyErrorDTO.setErrorMsg(modifyResultData.getData()!=null?modifyResultData.getData().toString():"");
                    rData = new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.BUSINESS_PROCESS_ERROR.code, expressModifyErrorDTO);
                }
            } catch (Exception e) {
                LOGGER.error("修改快递编号接口调用异常", e);
                rData = new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.SYSTEM_ERROR.code,
                    ErrorCodes.SystemError.SYSTEM_ERROR.cnName);
            }
        }

        return rData;
    }

    /**
     * <pre>
     * 批量修改运单号
     * </pre>
     * 
     * @param appKey
     * @param expressModifyList
     * @return
     */
    public ReturnData batchModifyExpressNo(final String appKey, final List<ExpressModifyDTO> expressModifyList) {
        List<Long> orderNoList = new ArrayList<Long>();
        List<ExpressModifyErrorDTO> expressModifyErrorList = new ArrayList<ExpressModifyErrorDTO>();
        // 组织验证权限的参数
        for (ExpressModifyDTO expressModify : expressModifyList) {
            Long orderCode = expressModify.getOrderNo();
            if (orderCode != null) {
                orderNoList.add(expressModify.getOrderNo());
            } else {
                ExpressModifyErrorDTO errorDTO = new ExpressModifyErrorDTO();
                errorDTO.setOrderNo(expressModify.getOrderNo());
                errorDTO.setOriginalExpressNo(expressModify.getOriginalExpressNo());
                errorDTO.setNewExpressNo(expressModify.getNewExpressNo());
                errorDTO.setErrorCode(ErrorCodes.ExpressError.PARAM_ILLEGAL.code);
                errorDTO.setErrorMsg(ErrorCodes.ExpressError.PARAM_ILLEGAL.cnName);
                expressModifyErrorList.add(errorDTO);
            }
        }
        // 权限验证
        Map<KeyType, List<OrderMiniDTO>> resultMap = null;
        try {
            resultMap = platformAccountService.verifySalesOrderAuthOfAccount(appKey, orderNoList);
        } catch (Exception e) {
            LOGGER.error("验证供应商权限异常", e);
            return new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.SYSTEM_ERROR.code,
                ErrorCodes.SystemError.SYSTEM_ERROR.cnName);
        }

        Map<Long, List<ExpressModifyDTO>> expressModifyMap = transExpressModifyListToMap(expressModifyList);
        if (CollectionUtils.isNotEmpty(resultMap.get(KeyType.SUCCESS))) {// 验证通过
            List<ExpressModifyDTO> passExpressModifyList = new ArrayList<ExpressModifyDTO>();
            List<OrderMiniDTO> successVerifyList = resultMap.get(KeyType.SUCCESS);
            for (OrderMiniDTO orderMiniDTO : successVerifyList) {
                passExpressModifyList.addAll(expressModifyMap.get(orderMiniDTO.getCode()));
            }
            List<com.tp.dto.mmp.ReturnData> modifyResultList = null;
            try {
                modifyResultList = orderDeliveryService.batchModifyExpressNo(passExpressModifyList);
            } catch (Exception e) {
                LOGGER.error("批量修改快递编号接口调用异常", e);
                return new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.SYSTEM_ERROR.code,
                    ErrorCodes.SystemError.SYSTEM_ERROR.cnName);
            }
            if (CollectionUtils.isNotEmpty(modifyResultList)) {
                for (com.tp.dto.mmp.ReturnData returnData : modifyResultList) {
                    if (!returnData.isSuccess()) {
                        String[] errorMsg = returnData.getData().toString().split(":");
                        String orderCode = errorMsg[0].split("-")[0];
                        List<ExpressModifyDTO> errorExpressModifyList = expressModifyMap.get(Long.valueOf(orderCode));
                        for (ExpressModifyDTO errorExpressModify : errorExpressModifyList) {
                            if (StringUtils.equals(errorMsg[0].split("-")[1],
                                errorExpressModify.getOriginalExpressNo())) {
                                ExpressModifyErrorDTO errorDTO = new ExpressModifyErrorDTO();
                                errorDTO.setOrderNo(errorExpressModify.getOrderNo());
                                errorDTO.setOriginalExpressNo(errorExpressModify.getOriginalExpressNo());
                                errorDTO.setNewExpressNo(errorExpressModify.getNewExpressNo());
                                errorDTO.setErrorCode(returnData.getErrorCode());
                                errorDTO.setErrorMsg(errorMsg[1]);
                                expressModifyErrorList.add(errorDTO);
                            }
                        }
                    }
                }
            }

        }
        if (CollectionUtils.isNotEmpty(resultMap.get(KeyType.FAILURE))) {
            List<OrderMiniDTO> failureVerifyList = resultMap.get(KeyType.FAILURE);
            for (OrderMiniDTO orderMiniDTO : failureVerifyList) {
                List<ExpressModifyDTO> unpassExpressModifyList = expressModifyMap.get(orderMiniDTO.getCode());
                for (ExpressModifyDTO unpassExpressModify : unpassExpressModifyList) {
                    ExpressModifyErrorDTO errorDTO = new ExpressModifyErrorDTO();
                    errorDTO.setOrderNo(unpassExpressModify.getOrderNo());
                    errorDTO.setOriginalExpressNo(unpassExpressModify.getOriginalExpressNo());
                    errorDTO.setNewExpressNo(unpassExpressModify.getNewExpressNo());
                    errorDTO.setErrorCode(ErrorCodes.AuthError.UNPASS_AUTH.code);
                    errorDTO.setErrorMsg(ErrorCodes.AuthError.UNPASS_AUTH.cnName);
                    expressModifyErrorList.add(errorDTO);
                }
            }
        }
        ReturnData rData = new ReturnData(Boolean.TRUE);
        if (CollectionUtils.isNotEmpty(expressModifyErrorList)) {
            rData = new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.BUSINESS_PROCESS_ERROR.code, expressModifyErrorList);
        }
        return rData;
    }

    /**
     * <pre>
     * 转换修改列表为MAP类型 key：订单编号，value：修改列表
     * </pre>
     * 
     * @param expressModifyList
     * @return
     */
    private Map<Long, List<ExpressModifyDTO>> transExpressModifyListToMap(
        final List<ExpressModifyDTO> expressModifyList) {
        Map<Long, List<ExpressModifyDTO>> map = new HashMap<Long, List<ExpressModifyDTO>>();
        if (CollectionUtils.isNotEmpty(expressModifyList)) {
            List<ExpressModifyDTO> list = null;
            for (ExpressModifyDTO expressModifyDTO : expressModifyList) {
                list = map.get(expressModifyDTO.getOrderNo());
                if (list == null) {
                    list = new ArrayList<ExpressModifyDTO>();
                }
                list.add(expressModifyDTO);
                map.put(expressModifyDTO.getOrderNo(), list);
            }
        }
        return map;
    }

    /**
     * <pre>
     * 查询快递日志记录
     * </pre>
     * 
     * @param appKey
     * @param logQuery
     * @return
     */
    public ReturnData queryOrderExpressLog(final String appKey, final ExpressLogQueryParamDTO logQuery) {
        ReturnData rData = new ReturnData(Boolean.TRUE);
        if (logQuery == null || logQuery.getCode() == null) {
            rData = new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.PARAM_ERROR.code,
                ErrorCodes.SystemError.PARAM_ERROR.cnName);
        } else {
            Map<KeyType, List<OrderMiniDTO>> resultMap = null;
            try {
                List<Long> orderCodeList = new ArrayList<Long>();
                orderCodeList.add(logQuery.getCode());
                resultMap = platformAccountService.verifySalesOrderAuthOfAccount(appKey, orderCodeList);
            } catch (Exception e) {
                LOGGER.error("验证供应商权限异常", e);
                return new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.SYSTEM_ERROR.code,
                    ErrorCodes.SystemError.SYSTEM_ERROR.cnName);
            }
            if (CollectionUtils.isNotEmpty(resultMap.get(KeyType.SUCCESS))) {
                try {
                    List<ExpressInfoDTO> list = expressInfoService.queryExpressLogInfo(logQuery.getCode(), logQuery.getPackageNo());
                    rData = new ReturnData(Boolean.TRUE, null, list);
                } catch (Exception e) {
                    rData = new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.SYSTEM_ERROR.code,
                        ErrorCodes.SystemError.SYSTEM_ERROR.cnName);
                }

            } else {
                ExpressLogQueryErrorDTO logQueryError = new ExpressLogQueryErrorDTO();
                logQueryError.setOrderCode(logQuery.getCode());
                logQueryError.setErrorCode(ErrorCodes.AuthError.UNPASS_AUTH.code);
                logQueryError.setErrorMsg(ErrorCodes.AuthError.UNPASS_AUTH.cnName);
                rData = new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.BUSINESS_PROCESS_ERROR.code,
                    logQueryError);
            }
        }
        return rData;
    }

    /**
     * 查询快递公司列表
     * 
     * @param appKey
     * @param name
     * @param code
     * @return
     */
    public ReturnData queryExpressInfo(final String appKey, final String name, final String code) {

        LOGGER.info("name: {},code: {}", name, code);
        List<ExpressCompanyDTO> list = expressInfoService.selectByNameOrCode(name, code);
        if (CollectionUtils.isEmpty(list)) {
            return new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.BUSINESS_PROCESS_ERROR.code, "查询不到快递公司");
        }

        return new ReturnData(Boolean.TRUE, list);
    }
}
