package com.tp.backend.controller.supplier.ao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tp.backend.controller.SupplierUploadAO;
import com.tp.common.vo.supplier.CommonUtil;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.AuditStatus;

/**
 * <pre>
 * 供应商AO公共类
 * </pre>
 *
 * @author Administrator
 * @version $Id: SupplierBaseAO.java, v 0.1 2014年12月24日 下午6:30:11 Administrator Exp $
 */
public class SupplierBaseAO {

    public final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SupplierUploadAO supplierUploadAO;

    /**
     * 默认时间格式
     */
    public final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * <pre>
     * 获取字符串类型 如果为空的返回null
     * </pre>
     *
     * @param request
     * @param name
     * @return
     */
    public String getStringValue(final HttpServletRequest request, final String name) {
        if (checkIsNull(request, name)) {
            return null;
        }
        final String val = request.getParameter(name);
        if (null == val || "".equals(val.trim())) {
            return null;
        }
        return val;
    }

    /**
     * <pre>
     * 设置空值为null
     * </pre>
     *
     * @return
     */
    public String setBlankNull(final String inputStr) {
        if (null == inputStr || "".equals(inputStr.trim())) {
            return null;
        }
        return inputStr;
    }

    /**
     * <pre>
     * 获取int类型的数据
     * </pre>
     *
     * @param request
     * @param name
     * @return
     */
    public Integer getIntValue(final HttpServletRequest request, final String name) {
        Integer retInt = null;
        if (checkIsNull(request, name)) {
            return null;
        }
        final String val = request.getParameter(name);
        if (null == name || "".equals(name.trim())) {
            return null;
        }
        try {
            retInt = Integer.parseInt(val);
        } catch (final Exception e) {
        }
        return retInt;
    }

    /**
     * 获取Integer类型数组
     *
     * @param request
     * @param name
     * @return
     */
    public Integer[] getIntegerVals(final HttpServletRequest request, final String name) {
        final String[] vals = getStringValues(request, name);
        Integer retVal[] = null;
        if (null != vals && vals.length > 0) {
            retVal = new Integer[vals.length];
            for (int i = 0; i < vals.length; i++) {
                final String rateStr = vals[i];
                retVal[i] = CommonUtil.getIntVal(rateStr);
            }
        }
        return retVal;
    }

    public BigDecimal[] getRateInfos(final HttpServletRequest request, final String name) {
        final String[] vals = getStringValues(request, name);
        BigDecimal retVal[] = null;
        if (null != vals && vals.length > 0) {
            retVal = new BigDecimal[vals.length];
            for (int i = 0; i < vals.length; i++) {
                String rateStr = vals[i];
                if(null != rateStr){
                	rateStr = rateStr.replace("%", "");
                }
                retVal[i] = toBigDecimal(rateStr);
            }
        }
        return retVal;
    }

    /**
     * <pre>
     * 获取税率信息
     * </pre>
     *
     * @param request
     * @param string
     * @return
     */
    public Double getRateInfo(final HttpServletRequest request, final String name) {
        String rateStr = getStringValue(request, name);
        if (StringUtils.isEmpty(rateStr)) {
            return null;
        }
        Double retVal = null;
        try {
            rateStr = rateStr.replace("%", "");
            retVal = new Double(rateStr);
        } catch (final Exception e) {
        }
        return retVal;
    }

    /**
     * <pre>
     * bigDecimal转换
     * </pre>
     *
     * @param inStr
     * @return
     */
    private BigDecimal toBigDecimal(final String inStr) {
        BigDecimal bigRet = null;
        try {
            bigRet = new BigDecimal(inStr);
        } catch (final Exception e) {
        	bigRet = new BigDecimal(0);
        }
        return bigRet;
    }

    /**
     * <pre>
     * 生成bigDecimal类型数组
     * </pre>
     *
     * @param request
     * @param name
     * @return
     */
    public BigDecimal[] getBigDecimals(final HttpServletRequest request, final String name) {
        final String[] vals = getStringValues(request, name);
        BigDecimal retVal[] = null;
        if (null != vals && vals.length > 0) {
            retVal = new BigDecimal[vals.length];
            for (int i = 0; i < vals.length; i++) {
                retVal[i] = toBigDecimal(vals[i]);
            }
        }
        return retVal;
    }

    /**
     * <pre>
     * 获取字符串列表
     * </pre>
     *
     * @param request
     * @param name
     * @return
     */
    public String[] getStringValues(final HttpServletRequest request, final String name) {
        final String vals[] = request.getParameterValues(name);
        String valRet[] = null;
        if (null != vals && vals.length > 0) {
            valRet = new String[vals.length];
            for (int i = 0; i < vals.length; i++) {
                valRet[i] = setBlankNull(vals[i]);
            }
        }
        return valRet;
    }

    /**
     * <pre>
     * 获取BigDecimal类型
     * </pre>
     *
     * @param request
     * @param name
     * @return
     */
    public BigDecimal getBigDecimalValue(final HttpServletRequest request, final String name) {
        final String val = request.getParameter(name);
        final BigDecimal retVal = CommonUtil.getBigDecimalVal(val);
        return retVal;
    }
    /**
     * <pre>
     * 获取BigDecimal类型
     * </pre>
     *
     * @param request
     * @param name
     * @return
     */
    public Double getDoubleValue(final HttpServletRequest request, final String name) {
        final String val = request.getParameter(name);
        final Double retVal = CommonUtil.getDoubleVal(val);
        return retVal;
    }
    /**
     * <pre>
     * 获取BigDecimal类型
     * </pre>
     *
     * @param request
     * @param name
     * @return
     */
    public BigDecimal[] getBigDecimalValues(final HttpServletRequest request, final String name) {
        final String vals[] = request.getParameterValues(name);
        BigDecimal valRet[] = null;
        if (null != vals && vals.length > 0) {
            valRet = new BigDecimal[vals.length];
            for (int i = 0; i < vals.length; i++) {
                valRet[i] = CommonUtil.getBigDecimalVal(vals[i]);
            }
        }
        return valRet;
    }

    /**
     * <pre>
     *
     * </pre>
     *
     * @param request
     * @param name
     * @return
     */
    public Long[] getLongValues(final HttpServletRequest request, final String name) {
        final String vals[] = getStringValues(request, name);
        Long[] valLongs = null;
        if (null != vals && vals.length > 0) {
            valLongs = new Long[vals.length];
            for (int i = 0; i < vals.length; i++) {
                valLongs[i] = CommonUtil.getLongVal(vals[i]);
            }
        }
        return valLongs;
    }

    /**
     * <pre>
     * 获取long类型的数据
     * </pre>
     *
     * @param request
     * @param name
     * @return
     */
    public Long getLongValue(final HttpServletRequest request, final String name) {
        Long retInt = null;
        if (checkIsNull(request, name)) {
            return null;
        }
        final String val = request.getParameter(name);
        if (null == name || "".equals(name.trim())) {
            return null;
        }
        try {
            retInt = Long.parseLong(val);
        } catch (final Exception e) {
        }
        return retInt;
    }

    /**
     * <pre>
     * 获取时间
     * </pre>
     *
     * @param request
     * @param name
     * @param format
     * @return
     */
    public Date getDate(final HttpServletRequest request, final String name, final String format) {
        Date date = null;
        String dateFormat = DEFAULT_TIME_FORMAT;
        if (checkIsNull(request, name)) {
            return null;
        }
        if (null != format) {
            dateFormat = format;
        }
        final String dateVal = request.getParameter(name);
        if (null == dateVal || "".equals(dateVal.trim())) {
            return null;
        }
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            date = sdf.parse(dateVal);
        } catch (final Exception e) {
        }
        return date;
    }

    /**
     * <pre>
     * 校验是否为空
     * </pre>
     *
     * @param request
     * @param name
     * @return
     */
    private boolean checkIsNull(final HttpServletRequest request, final String name) {
        if (null == name || null == request) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <pre>
     * 校验结果
     * </pre>
     *
     * @param resultMap
     * @return
     */
    public boolean checkResult(final Map<String, Object> resultMap) {
        try {
            return null != resultMap && null != resultMap.get(SupplierConstant.SUCCESS_KEY) && (Boolean) resultMap.get(SupplierConstant.SUCCESS_KEY);
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * <pre>
     * 上传文件信息
     * </pre>
     *
     * @param request
     * @param name
     * @return
     */
    public String uploadFile(final HttpServletRequest request, final String name) {
        String fileId = null;
        try {
            final Map<String, Object> map = supplierUploadAO.uploadFile(request, name);
            if ((Boolean) map.get(SupplierConstant.SUCCESS_KEY)) {
                fileId = (String) map.get(SupplierConstant.UPLOADED_FILE_KEY);
            }
        } catch (final Exception e) {
        }
        return fileId;
    }

    /**
     * <pre>
     * 从返回结果中获取message
     * </pre>
     *
     * @param resultMap
     * @return
     */
    public String getMessage(final Map<String, Object> resultMap) {
        final String result = "";
        if (null != resultMap && null != resultMap.get(SupplierConstant.MESSAGE_KEY)) {
            return (String) resultMap.get(SupplierConstant.MESSAGE_KEY);
        }
        return result;
    }

    /**
     * <pre>
     * 获取返回的result
     * </pre>
     *
     * @return
     */
    public Map<String, Object> setResult(final Map<String, Object> resultMap) {
        final Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put(SupplierConstant.SUCCESS_KEY, false);
        retMap.put(SupplierConstant.MESSAGE_KEY, getMessage(resultMap));
        return retMap;
    }

    /**
     * <pre>
     * 获取传递的code
     * </pre>
     *
     * @param status
     * @return
     */
    public String getAuditCode(final Integer status) {
        final Map<Integer, String> statusCodeMap = new HashMap<Integer, String>();
        statusCodeMap.put(AuditStatus.THROUGH.getStatus(), "pass");
        statusCodeMap.put(AuditStatus.REFUSED.getStatus(), "refuse");
        return statusCodeMap.get(status);
    }

}
