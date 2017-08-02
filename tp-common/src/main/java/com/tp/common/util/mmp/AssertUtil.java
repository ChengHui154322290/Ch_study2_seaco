package com.tp.common.util.mmp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import com.tp.exception.ServiceException;

import java.util.Collection;

/**
 * Created by ldr on 2016/1/4.
 */
public class AssertUtil {

    public static final int CODE = -1;

    public static void notNull(Object o, String message, Integer code) {
        if (o == null)
            throw new ServiceException(message, code);
    }

    public static void notNull(Object o, String message) {
        notNull(o, message, CODE);
    }

    public static void notEmpty(String str, String message, Integer code) {
        if (StringUtils.isEmpty(str)) {
            throw new ServiceException(message, code);
        }
    }

    public static void notEmpty(String str, String message) {
        notEmpty(str, message,CODE);
    }

    public static void notBlank(String str, String message, Integer code) {
        if (StringUtils.isBlank(str))
            throw new ServiceException(message, code);
    }

    public static void notBlank(String str, String message) {
        notBlank(str, message, CODE);
    }

    public static <E> void notEmpty(Collection<E> collection, String message, Integer code) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(message, code);
        }
    }

    public static <E> void notEmpty(Collection<E> collection, String message) {
        notEmpty(collection, message,CODE);
    }

    public static void isNumber(String o, String message, Integer code) {
        if (!NumberUtils.isNumber(o))
            throw new ServiceException(message, code);
    }

    public static void isNumber(String o, String message) {
        isNumber(o, message,CODE);
    }

    public static void notEmpty(String[] strings, String message, Integer code) {
        if (strings == null || strings.length == 0) {
            throw new ServiceException(message, code);
        }
    }

    public static void notEmpty(String[] strings, String message) {
        notEmpty(strings, message,CODE);
    }


}
