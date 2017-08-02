package com.tp.seller.ao.base;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.seller.constant.CommonUtils;

public class SellerBaseAO {

    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final Logger LOGGER = LoggerFactory.getLogger(SellerBaseAO.class);

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
        if (null == val || "".equals(val.trim())) {
            return null;
        }
        try {
            retInt = Integer.parseInt(val);
        } catch (final NumberFormatException e) {
            LOGGER.error("parseInt error, val:{}", val);
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
        if (null != vals && vals.length > 0) {
            final Integer[] retVal = new Integer[vals.length];
            for (int i = 0; i < vals.length; i++) {
                final String rateStr = vals[i];
                retVal[i] = CommonUtils.getIntVal(rateStr);
            }
            return retVal;
        }
        return null;
    }

    public BigDecimal[] getRateInfos(final HttpServletRequest request, final String name) {
        final String[] vals = getStringValues(request, name);
        BigDecimal[] retVal = null;
        if (null != vals && vals.length > 0) {
            retVal = new BigDecimal[vals.length];
            for (int i = 0; i < vals.length; i++) {
                String rateStr = vals[i];
                rateStr = rateStr.replace("%", "");
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
    public BigDecimal getRateInfo(final HttpServletRequest request, final String name) {
        String rateStr = getStringValue(request, name);
        if (StringUtils.isEmpty(rateStr)) {
            return null;
        }
        BigDecimal retVal = null;
        try {
            rateStr = rateStr.replace("%", "");
            retVal = new BigDecimal(rateStr);
        } catch (final NumberFormatException e) {
            LOGGER.error("parse BigDecimal error, rateStr:{}", rateStr);
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
        } catch (final NumberFormatException e) {
            LOGGER.error("parse BigDecimal error, inStr:{}", inStr);
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
        BigDecimal[] retVal = null;
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
        final String[] vals = request.getParameterValues(name);
        String[] valRet = null;
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
        final BigDecimal retVal = CommonUtils.getBigDecimalVal(val);
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
        final String[] vals = request.getParameterValues(name);
        BigDecimal[] valRet = null;
        if (null != vals && vals.length > 0) {
            valRet = new BigDecimal[vals.length];
            for (int i = 0; i < vals.length; i++) {
                valRet[i] = CommonUtils.getBigDecimalVal(vals[i]);
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
        final String[] vals = getStringValues(request, name);
        Long[] valLongs = null;
        if (null != vals && vals.length > 0) {
            valLongs = new Long[vals.length];
            for (int i = 0; i < vals.length; i++) {
                valLongs[i] = CommonUtils.getLongVal(vals[i]);
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
        } catch (final NumberFormatException e) {
            LOGGER.error("parseLong error, val:{}", val);
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
            LOGGER.error("date parse error, dateVal:{}", dateVal);
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
        }
        return false;
    }

}
