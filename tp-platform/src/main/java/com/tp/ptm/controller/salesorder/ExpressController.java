package com.tp.ptm.controller.salesorder;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.ptm.ErrorCodes.SystemError;
import com.tp.dto.ord.remote.ExpressModifyDTO;
import com.tp.dto.ptm.ReturnData;
import com.tp.dto.ptm.salesorder.ExpressLogQueryParamDTO;
import com.tp.ptm.annotation.Authority;
import com.tp.ptm.ao.salesorder.ExpressAO;
import com.tp.ptm.controller.BaseController;
import com.tp.ptm.support.FrequencyCounter;
import com.tp.ptm.support.FrequencyCounter.BusinessType;

/**
 * 快递控制类
 *
 * @author ZhuFuhua
 */
@Controller
@RequestMapping("/express")
public class ExpressController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressController.class);

    private static final Integer DEFULT_DEAL_MODIFY_EXPRESS_SIZE = new Integer(50);

    @Autowired
    private ExpressAO expressAO;

    @Autowired
    private FrequencyCounter frequencyCounter;

    @Value("#{meta['express.modifyExpressNo.seconds']}")
    private int modifyExpressNoSeconds;

    @Value("#{meta['express.modifyExpressNo.times']}")
    private int modifyExpressNoTimes;

    @Value("#{meta['express.batchModifyExpressNo.seconds']}")
    private int batchModifyExpressNoSeconds;

    @Value("#{meta['express.batchModifyExpressNo.times']}")
    private int batchModifyExpressNoTimes;

    @Value("#{meta['express.queryOrderExpressLog.seconds']}")
    private int queryOrderExpressLogSeconds;

    @Value("#{meta['express.queryOrderExpressLog.times']}")
    private int queryOrderExpressLogTimes;

    @Value("#{meta['express.queryExpressCompany.seconds']}")
    private int queryExpressCompanySeconds;

    @Value("#{meta['express.queryExpressCompany.times']}")
    private int queryExpressCompanyTimes;

    /**
     * <pre>
     * 修改快递单号
     * </pre>
     *
     * @param appkey
     * @param request
     * @param response
     */
    @RequestMapping(value = "/modifyExpressNo", method = RequestMethod.POST)
    @Authority
    public void modifyExpressNo(@RequestParam final String appkey, final HttpServletRequest request, final HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        ReturnData rtData = new ReturnData(Boolean.TRUE);
        // 访问频率超过限制
        if (!frequencyCounter.overload(appkey, BusinessType.MODIFY_EXPRESS_NO, modifyExpressNoSeconds, modifyExpressNoTimes)) {
            rtData = new ReturnData(false, SystemError.ACCESS_OVERLOAD.code, SystemError.ACCESS_OVERLOAD.cnName);
        } else {
            String reqDataJson = getRequestContent(request);
            if (StringUtils.isNotBlank(reqDataJson)) {
                ExpressModifyDTO expressModifyDTO = null;
                try {
                    expressModifyDTO = JSONObject.parseObject(reqDataJson, ExpressModifyDTO.class);
                } catch (Exception e) {
                    LOGGER.error("请求JSON参数转换为实体类异常", e);
                    LOGGER.error("json原始参数为:{}", reqDataJson);
                    rtData = new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code, SystemError.PARAM_ERROR.cnName);
                }
                if (expressModifyDTO != null) {
                    rtData = expressAO.modifyExpressNo(appkey, expressModifyDTO);
                }
            } else {
                rtData = new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code, SystemError.PARAM_ERROR.cnName);
            }
        }

        try {
            response.getWriter().print(JSONObject.toJSONString(rtData));
        } catch (IOException e) {
            LOGGER.error("返回响应结果时IO异常", e);
        }
    }

    /**
     * <pre>
     * 批量修改快递单号
     * </pre>
     *
     * @param appkey
     * @param request
     * @param response
     */
    @RequestMapping(value = "/batchModifyExpressNo", method = RequestMethod.POST)
    @Authority
    public void batchModifyExpressNo(@RequestParam final String appkey, final HttpServletRequest request, final HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        ReturnData rtData = new ReturnData(Boolean.TRUE);
        // 访问频率超过限制
        if (!frequencyCounter.overload(appkey, BusinessType.BATCH_MODIFY_EXPRESS_NO, batchModifyExpressNoSeconds, batchModifyExpressNoTimes)) {
            rtData = new ReturnData(false, SystemError.ACCESS_OVERLOAD.code, SystemError.ACCESS_OVERLOAD.cnName);
        } else {
            String reqDataJson = getRequestContent(request);
            if (StringUtils.isNotBlank(reqDataJson)) {
                List<ExpressModifyDTO> expressModifyList = null;
                try {
                    expressModifyList = JSONArray.parseArray(reqDataJson, ExpressModifyDTO.class);
                } catch (Exception e) {
                    LOGGER.error("请求JSON参数转换为实体类异常", e);
                    LOGGER.error("json原始参数为:{}", reqDataJson);
                    rtData = new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code, SystemError.PARAM_ERROR.cnName);
                }
                if (CollectionUtils.isEmpty(expressModifyList) || expressModifyList.size() > DEFULT_DEAL_MODIFY_EXPRESS_SIZE.intValue()) {
                    LOGGER.info("批量处理修改运单号参数为:空或者超过限制");
                    rtData = new ReturnData(Boolean.FALSE, SystemError.PROCESS_SIZE_UNMATCH_LIMIT.code, SystemError.PROCESS_SIZE_UNMATCH_LIMIT.cnName);
                } else {
                    rtData = expressAO.batchModifyExpressNo(appkey, expressModifyList);
                }
            } else {
                rtData = new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code, SystemError.PARAM_ERROR.cnName);
            }
        }
        try {
            response.getWriter().print(JSONObject.toJSONString(rtData));
        } catch (IOException e) {
            LOGGER.error("返回响应结果时IO异常", e);
        }
    }

    /**
     * 快递跟踪查询（查询快递日志信息）
     *
     * @param appkey
     * @param request
     * @param response
     */
    @RequestMapping(value = "/queryOrderExpressLog", method = RequestMethod.POST)
    @Authority
    public void queryOrderExpressLog(@RequestParam final String appkey, final HttpServletRequest request, final HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        ReturnData rtData = new ReturnData(Boolean.TRUE);
        // 访问频率超过限制
        if (!frequencyCounter.overload(appkey, BusinessType.QUERY_ORDER_EXPRESS_LOG, queryOrderExpressLogSeconds, queryOrderExpressLogTimes)) {
            rtData = new ReturnData(false, SystemError.ACCESS_OVERLOAD.code, SystemError.ACCESS_OVERLOAD.cnName);
        } else {
            String reqDataJson = getRequestContent(request);
            if (StringUtils.isNotBlank(reqDataJson)) {
                ExpressLogQueryParamDTO logQuery = null;
                try {
                    logQuery = JSONObject.parseObject(reqDataJson, ExpressLogQueryParamDTO.class);
                } catch (Exception e) {
                    LOGGER.error("请求JSON参数转换为实体类异常", e);
                    LOGGER.error("json原始参数为:{}", reqDataJson);
                    rtData = new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code, SystemError.PARAM_ERROR.cnName);
                }
                if (logQuery == null || logQuery.getCode() == null) {
                    rtData = new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code, SystemError.PARAM_ERROR.cnName);
                } else {
                    rtData = expressAO.queryOrderExpressLog(appkey, logQuery);
                }
            } else {
                rtData = new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code, SystemError.PARAM_ERROR.cnName);
            }
        }
        try {
            response.getWriter().print(JSONObject.toJSONString(rtData));
        } catch (IOException e) {
            LOGGER.error("返回响应结果时IO异常", e);
        }
    }

    /**
     * <pre>
     * 查询快递公司
     * </pre>
     *
     * @param appkey
     * @param request
     * @param response
     */
    @RequestMapping(value = "/queryExpressCompany", method = RequestMethod.POST)
    @Authority
    public void queryExpressCompany(@RequestParam final String appkey, final HttpServletRequest request, final HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        ReturnData rtData = new ReturnData(Boolean.TRUE);
        // 访问频率超过限制
        if (!frequencyCounter.overload(appkey, BusinessType.QUERY_EXPRESS_COMPANY, queryExpressCompanySeconds, queryExpressCompanyTimes)) {
            rtData = new ReturnData(false, SystemError.ACCESS_OVERLOAD.code, SystemError.ACCESS_OVERLOAD.cnName);
        } else {
            String reqDataJson = getRequestContent(request);
            String name = null;
            String code = null;
            if (StringUtils.isNotBlank(reqDataJson)) {
                JSONObject jo = JSONObject.parseObject(reqDataJson);
                name = jo.getString("name");
                code = jo.getString("code");
            }
            rtData = expressAO.queryExpressInfo(appkey, name, code);
        }
        try {
            response.getWriter().print(JSONObject.toJSONString(rtData));
        } catch (IOException e) {
            LOGGER.error("返回响应结果时IO异常", e);
        }
    }

    public static void main(final String[] args) {
        ExpressModifyDTO expressModifyDTO = new ExpressModifyDTO();
        expressModifyDTO.setCompanyName("EMS");
        expressModifyDTO.setCompanyNo("ems");
        expressModifyDTO.setNewExpressNo("1515151515151");
        expressModifyDTO.setOrderNo(1115050515530320L);
        expressModifyDTO.setOriginalExpressNo("20150505143853");
        System.out.println(JSONObject.toJSONString(expressModifyDTO));
    }
}
