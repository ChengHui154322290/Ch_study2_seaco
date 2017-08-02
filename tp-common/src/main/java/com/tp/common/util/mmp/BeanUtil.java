package com.tp.common.util.mmp;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by ldr on 2016/1/4.
 */
public class BeanUtil {

    public static <T> void processNullField(T t) {

        if (t == null) {
            return;
        }

        Field[] fields = t.getClass().getDeclaredFields();
        int i = -1;
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object o = field.get(t);

                if (o == null) {
                    if (field.getName().equalsIgnoreCase("id")) {
                        continue;
                    }
                    if (field.getType() == String.class) {
                        field.set(t, "");
                    } else if (field.getType() == Integer.class) {
                        field.set(t, i);
                    } else if (field.getType() == Long.class) {
                        field.set(t, Long.valueOf(i));
                    } else if (field.getType() == Date.class) {
                        field.set(t, new Date());
                    } else if (field.getType() == Double.class) {
                        field.set(t, Double.valueOf(i));
                    }
                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T> void processNullStringField(T t) {
        if (t == null) return;
        Field[] fields = t.getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object o = field.get(t);

                if (o == null) {
                    if (field.getName().equalsIgnoreCase("id")) {
                        continue;
                    }
                    if (field.getType() == String.class) {
                        field.set(t, "");
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
