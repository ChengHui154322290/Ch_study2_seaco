package com.tp.seller.constant;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Collator;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 公共util
 *
 * @author yfxie
 */
public final class CommonUtils {

    private static final String UNKNOWN = "unknown";

    /**
     * <code>LENGTH_15</code> - {description}.
     */
    private static final int LENGTH_15 = 15;

    /**
     * <code>DATE_FORMATE</code> - {显示XXXX-XX-XX XX:00:00 精确到小时}.
     */
    private static final String DATE_FORMATE = "yyyy-MM-dd HH:00:00";

    /**
     * <code>INT_16</code> - {description}.
     */
    private static final int INT_16 = 16;

    /**
     * <code>INT_256</code> - {description}.
     */
    private static final int INT_256 = 256;

    /**
     * <code>LOGGER</code> - {description}.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

    private CommonUtils() {

    }

    /**
     * {method description}.
     *
     * @param str String
     * @return String
     */
    public static String toMD5(final String str) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(str)) {
            return org.apache.commons.lang3.StringUtils.EMPTY;
        }
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            final byte[] byteDigest = md.digest();
            int i;
            final StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0) {
                    i += INT_256;
                }
                if (i < INT_16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            // 32位加密
            return buf.toString();
            // 16位的加密
            // return buf.toString().substring(8, 24);
        } catch (final NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * {生成(带前缀的)15位随机数}.
     *
     * @param prefix prefix
     * @return String
     */
    public static String getRadomId(final String prefix) {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(prefix);
        buffer.append(StringUtils.upperCase(RandomStringUtils.randomAlphanumeric(LENGTH_15)));
        return buffer.toString();
    }

    /**
     * {获取客户端ip地址}.
     *
     * @param request HttpServletRequest
     * @return String
     */
    public static String getIpAddr(final HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    /**
     * {获取本机ip地址}.
     *
     * @return String
     */
    public static String getIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (final UnknownHostException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * {精确到小时}.
     *
     * @return String 当前时间
     */
    public static String getCurrentTime() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMATE);
        return simpleDateFormat.format(new Date());
    }

    /**
     * {字符编码转换}.
     *
     * @param str String
     * @return String
     */
    public static String encoding(final String str) {
        try {
            return new String(str.getBytes("ISO8859-1"), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * {字符串转换,null - > ""}.
     *
     * @param str String
     * @return String
     */
    public static String nullToEmpty(final String str) {
        return (str == null) ? StringUtils.EMPTY : str;
    }

    /**
     * 按中文排序.
     *
     * @param list 待排序集合
     */
    public static void sortByChina(final List<String> list) {
        final Comparator<Object> cmp = Collator.getInstance(Locale.CHINA);
        Collections.sort(list, cmp);
    }

    /**
     * <pre>
     * 获取int类型数据
     * </pre>
     *
     * @param inStr
     * @return
     */
    public static Integer getIntVal(final String inStr) {
        Integer intVal = null;
        try {
            intVal = Integer.parseInt(inStr);
        } catch (final Exception e) {
            return null;
        }
        return intVal;
    }

    /**
     * <pre>
     * 获取bigDecimal类型
     * </pre>
     *
     * @param inStr
     * @return
     */
    public static BigDecimal getBigDecimalVal(final String inStr) {
        BigDecimal retVal = null;
        try {
            retVal = new BigDecimal(inStr);
        } catch (final Exception e) {
            return null;
        }
        return retVal;
    }

    /**
     * <pre>
     * 获取Long类型数据
     * </pre>
     *
     * @param inStr
     * @return
     */
    public static Long getLongVal(final String inStr) {
        Long intVal = null;
        try {
            intVal = Long.parseLong(inStr);
        } catch (final Exception e) {
            return null;
        }
        return intVal;
    }

    /**
     * 获取文件格式 （后缀）
     *
     * @return
     */
    public static String getFileFormat(final String fileName) {
        if (fileName.indexOf(".") > 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    /**
     * <pre>
     * 获取下个等级
     * </pre>
     *
     * @return
     */
    public static synchronized int getNextAuditLevel(final int currentLevel) {
        return currentLevel + 1;
    }

    /**
     * <pre>
     * 获取当前用户的userId
     * </pre>
     *
     * @param request
     * @return
     */
    public static Long currentUserId(final HttpServletRequest request) {
        return -1L;
    }

    /**
     * <pre>
     * 获取当前用户的roleId
     * </pre>
     *
     * @param request
     * @return
     */
    public static Long currentRoleId(final HttpServletRequest request) {
        return -1L;
    }

    /**
     * 获取时间格式字符串
     *
     * @param format
     * @return
     */
    public static String formatDate(final Date date, final String format) {
        String retStr = null;
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat(format);
            retStr = sdf.format(date);
        } catch (final Exception e) {
            return null;
        }
        return retStr;
    }

    /**
     * {返回最小位数}.
     *
     * @param number
     * @param newValue
     * @return
     */
    public static String getMinIntegerDigits(final Long number, final int newValue) {
        if (number == null) {
            return StringUtils.EMPTY;
        }
        final NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumIntegerDigits(newValue);
        formatter.setGroupingUsed(false);
        return formatter.format(number);
    }

}
