package com.tp.m.helper;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.m.query.seagoorpay.annotation.Verify;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by ldr on 2016/11/19.
 */
public class SeagoorPayHelper {

    private static Logger logger = LoggerFactory.getLogger(SeagoorPayHelper.class);

    public static String genSeagoorPayCode(Long id) {
        StringBuilder stringBuilder = new StringBuilder(18);
        stringBuilder.append(9);
        String idStr = id.toString();
        if (idStr.length() == 1) {
            stringBuilder.append(0);
        } else {
            stringBuilder.append(idStr.charAt(idStr.length() - 2));
        }

        Long a = (id & 1953);
        Random random = new Random();
        int sh = random.nextInt(10);
        a = a << sh;
        String aStr = a.toString();
        int len = aStr.length();
        stringBuilder.append(sh);
        for (int i = 0; i < len; i++) {
            stringBuilder.append(random.nextInt(10));
            stringBuilder.append(aStr.charAt(i));
        }
        while (stringBuilder.length() < 18) {
            stringBuilder.append(random.nextInt(10));
        }
        if (stringBuilder.length() > 18) {
            return stringBuilder.substring(0, 18);
        } else {
            return stringBuilder.toString();
        }

    }

    public static String sign(Object o, String key) {
        if (o == null) throw new NullPointerException("object is Null");
        if (key == null) throw new NullPointerException("key is Null");
        Map<String, String> map = new TreeMap<>();
        List<Field> fields = getFieldsRecursive(o.getClass());
        for (Field field : fields) {
            if (field.getName().equals("sign") || field.getName().equals("serialVersionUID")) continue;
            if(field.getType() == List.class) continue;
            field.setAccessible(true);
            Object fidelVal = null;
            try {
                fidelVal = field.get(o);
            } catch (IllegalAccessException e) {
                logger.error("GEN_SIGN_ERROR_", e);
            }
            if (fidelVal == null) continue;
            if (field.getType() == String.class && fidelVal.toString().trim().equals("")) {
                continue;
            }
            map.put(field.getName(), String.valueOf(fidelVal));
        }
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        if (builder.length() > 0) {
            builder.append("&");
        }
        builder.append("key=").append(key);
        System.out.println(builder.toString());
        String sign = DigestUtils.md5Hex(builder.toString()).toUpperCase();
        return sign;
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

    public static String getRandStr() {
        return String.valueOf(Math.random());
    }

    public static String checkParam(Object o) {
        if (o == null) return "PARAM_OBJECT_IS_NULL";
        List<Field> fields = getFieldsRecursive(o.getClass());
        Map<String, List<Field>> combines = new HashMap<>(2);
        for (Field field : fields) {
            if (field.getAnnotation(Verify.class) == null) continue;
            Verify verify = field.getAnnotation(Verify.class);
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(o);
            } catch (IllegalAccessException e) {
                logger.error("CHECK_PARAM_ERROR_", e);
            }
            if (!verify.nullable()) {
                if (value == null) {
                    return field.getName().toUpperCase() + "_IS_NULL";
                } else if (field.getType() == String.class) {
                    if (String.valueOf(value).trim().equals("")) {
                        return field.getName().toUpperCase() + "_IS_EMPTY";
                    }
                }
            }
            if (verify.maxLength() > 0) {
                if (value != null &&(String.valueOf(value).length() > verify.maxLength())) {
                    return field.getName().toUpperCase() + "_MAX_LENGTH_IS_" + verify.maxLength();
                }
            }

            if(verify.isInt()){
                System.out.println(field.getName());
                System.out.println(value);
                if(Integer.parseInt(value.toString())<=0){
                    return field.getName().toUpperCase() + "_IS_0";
                }
            }


            if(!verify.combine().equals("")){
                List<Field> fieldList = combines.get(verify.combine());
                if(fieldList == null){
                    fieldList = new ArrayList<>(4);
                    fieldList.add(field);
                    combines.put(verify.combine(),fieldList);
                }else {
                    fieldList.add(field);
                }
            }
        }

        if(!combines.isEmpty()){
            for (Map.Entry<String,List<Field>> entry :combines.entrySet()){
                List<Field> fieldList = entry.getValue();
                int nullFields = 0;
                StringBuilder builder = new StringBuilder();
                for(Field field: fieldList){
                    field.setAccessible(true);
                    builder.append(field.getName().toUpperCase()).append("&");
                    try {
                        if(field.get(o) == null || String.valueOf(field.get(o)).equals("")){
                            nullFields ++;
                        }
                    } catch (IllegalAccessException e) {
                        logger.error("CHECK_PARAM_ERROR_", e);
                    }
                }
                if(nullFields == fieldList.size()){
                    return builder.substring(0,builder.length()-1)+"_CAN_NOT_BE_ALL_NULL";
                }
            }
        }
        return null;
    }
}
