package com.tp.service.pay.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ldr on 8/24/2016.
 */
public class WeixinPayUtil {

    private static Logger logger = LoggerFactory.getLogger(WeixinPayUtil.class);

    public static <T> T mapToBean(Map<String, ?> map, Class<T> clazz) {

        if (map == null || clazz == null) return null;
        try {
            T t = clazz.newInstance();
            List<Field> fields = getFieldsRecursive(clazz);
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                String key = entry.getKey();
                for (Field field : fields) {
                    if (field.getName().equals(key)) {
                        field.setAccessible(true);
                        if (field.getType().getName().equals("int")) {
                            field.set(t, Integer.parseInt(entry.getValue() == null ? "0" : entry.getValue().toString()));
                        } else {
                            field.set(t, entry.getValue());
                        }
                        break;
                    }
                }
            }
            return t;
        } catch (InstantiationException e) {
            logger.error("MAP_TO_BEAN_ERROR:MAP={},CLASS={}", map, clazz);
            logger.error("MAP_TO_BEAN_ERROR:", e);
        } catch (IllegalAccessException e) {
            logger.error("MAP_TO_BEAN_ERROR:MAP={},CLASS={}", map, clazz);
            logger.error("MAP_TO_BEAN_ERROR:", e);
        }
        return null;
    }

    public static List<Field> getFieldsRecursive(Class t) {
        if (t == null) return Collections.emptyList();
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(getFields(t)));
        Class c = t;
        while (c.getSuperclass() != null) {
            c = c.getSuperclass();
            fields.addAll(Arrays.asList(getFields(c)));
        }
        return fields;
    }

    public static Field[] getFields(Class t) {
        if (t == null) {
            return new Field[]{};
        }
        Field[] fields = t.getDeclaredFields();
        if (fields == null) return new Field[]{};
        return fields;

    }

    public static Map<String, String> xmlToMap(String xml) {
        Map<String, String> map = new HashMap<>();
        Pattern p = Pattern.compile("<(\\w+)><!\\[CDATA\\[([^]]*)\\]\\]></");
        Matcher m = p.matcher(xml);
        while (m.find()) {
            map.put(m.group(1), m.group(2));
        }
        p = Pattern.compile("<(\\w+)>([^<]*)</");
        m = p.matcher(xml);
        while (m.find()) {
            map.put(m.group(1), m.group(2));
        }

        return map;
    }

}
