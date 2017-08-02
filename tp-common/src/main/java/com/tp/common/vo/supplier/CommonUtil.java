package com.tp.common.vo.supplier;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Collator;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * {class_description} <br>
 * Create on : 2014年12月22日 下午3:34:44<br>
 *
 * @author szy
 * @version 0.0.1
 */
public final class CommonUtil {

    public static final String UNKNOWN = "unknown";

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
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * Constructors.
     */
    private CommonUtil() {

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
     * {返回36位随机数}.
     *
     * @return String
     */
    public static String getRadomGUID() {
        return RandomGUIDUtil.generateKey();
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
     * 校验文件大小
     *
     * @param fileSize
     * @param fileName
     * @return
     */
    public static Map<String, Object> checkFileSize(final long fileSize, final String fileName) {
        final Map<String, Object> retMap = new HashMap<String, Object>();
        // 压缩包大小
        if (fileName.toLowerCase().indexOf(".zip") > -1 || fileName.toLowerCase().indexOf(".rar") > -1) {
            if (fileSize > SupplierConstant.MAX_FILE_SIZE.longValue()) {
                retMap.put(SupplierConstant.SUCCESS_KEY, false);
                retMap.put(SupplierConstant.MESSAGE_KEY, "压缩文件大小不能超过20M。");
                return retMap;
            }
        } else if (fileName.toLowerCase().indexOf(".pdf") > -1) {
            // pdf
            if (fileSize > SupplierConstant.MAX_FILE_SIZE.longValue()) {
                retMap.put(SupplierConstant.SUCCESS_KEY, false);
                retMap.put(SupplierConstant.MESSAGE_KEY, "pdf文件大小不能超过20M。");
                return retMap;
            }
        } else {
            // 图片文件大小
            if (fileSize > SupplierConstant.MAX_IMAGE_SIZE.longValue()) {
                retMap.put(SupplierConstant.SUCCESS_KEY, false);
                retMap.put(SupplierConstant.MESSAGE_KEY, "图片文件大小不能超过1M。");
                return retMap;
            }
        }
        retMap.put(SupplierConstant.SUCCESS_KEY, true);
        return retMap;
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
            return new BigDecimal(0);
        }
        return retVal;
    }
    /**
     * <pre>
     * 获取bigDecimal类型
     * </pre>
     *
     * @param inStr
     * @return
     */
    public static Double getDoubleVal(final String inStr) {
    	Double retVal = null;
        try {
            retVal = new Double(inStr);
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
     * list类型转换
     * 
     * @param sourceList
     * @param destClass
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T, E> List<T> listConvert(List<E> sourceList, Class<T> destClass) throws InstantiationException,
        IllegalAccessException {
        List<T> retList = new ArrayList<T>();
        if (null != sourceList && sourceList.size() > 0) {
            for (E source : sourceList) {
                T destObj = destClass.newInstance();
                BeanUtils.copyProperties(source, destObj);
                retList.add(destObj);
            }
            return retList;
        } else {
            return retList;
        }
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
