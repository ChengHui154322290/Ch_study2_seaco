package com.tp.test;

import com.tp.result.pay.wexin.DeclareQueryResult;
import com.tp.service.pay.util.WeixinPayUtil;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by ldr on 8/9/2016.
 */
public class MyTEst {

    public static void main(String[] argss) throws InstantiationException, IllegalAccessException {


        Map<String, String> map = new TreeMap<>();
        map.put("abc", "abc");
        map.put("gdc", "gdc");
        map.put("bcd", "bcd");
        map.put("xxx", "xxx");
        map.put("fff", "fff");
        map.put("aaa", "aaa");
        map.put("lll", "lll");
        map.put("desc", "desc");
        for (Map.Entry e : map.entrySet()) {
            //  System.out.println(e.getKey()+"     "+e.getValue());
        }


//        Map<String, Object> my = new HashMap<>();
//        my.put("return_code", "SUCCESS");
//        my.put("mch_id", "mmmmmmmmmmmm");
//        my.put("state", "SUBMITTED");
//        DeclareOrderResult result = WeixinPayUtil.mapToBean(my, DeclareOrderResult.class);
//        System.out.println(result);
//
        String xml = "<return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "<return_msg><![CDATA[成功]]></return_msg>\n" +
                "<sign><![CDATA[E6C36335DC279D0A39D1F5F3B3F9C63F]]></sign>\n" +
                "<appid><![CDATA[wxfa4bf063b7848eff]]></appid>\n" +
                "<mch_id><![CDATA[1356867702]]></mch_id>\n" +
                "<result_code><![CDATA[SUCCESS]]></result_code>\n" +
                "<err_code><![CDATA[0]]></err_code>\n" +
                "<err_code_des><![CDATA[OK]]></err_code_des>\n" +
                "<transaction_id><![CDATA[4001802001201608232045602270]]></transaction_id>\n" +
                "<count>1</count>\n" +
                "<sub_order_no_0><![CDATA[1100082390000023]]></sub_order_no_0>\n" +
                "<sub_order_id_0><![CDATA[400180200120160823204560227000]]></sub_order_id_0>\n" +
                "<mch_customs_no_0><![CDATA[3301960H93]]></mch_customs_no_0>\n" +
                "<customs_0><![CDATA[HANGZHOU]]></customs_0>\n" +
                "<cert_type_0><![CDATA[ID]]></cert_type_0>\n" +
                "<cert_id_0><![CDATA[411321198712280015]]></cert_id_0>\n" +
                "<name_0><![CDATA[李后梁]]></name_0>\n" +
                "<fee_type_0><![CDATA[CNY]]></fee_type_0>\n" +
                "<order_fee_0>11</order_fee_0>\n" +
                "<duty_0>0</duty_0>\n" +
                "<transport_fee_0>0</transport_fee_0>\n" +
                "<product_fee_0>11</product_fee_0>\n" +
                "<state_0><![CDATA[SUCCESS]]></state_0>\n" +
                "<explanation_0><![CDATA[处理成功]]></explanation_0>\n" +
                "<modify_time_0><![CDATA[20160824170007]]></modify_time_0>\n" +
                "</xml>";

        long b = System.currentTimeMillis();
        for(int i =0 ;i<100;i++){
            System.out.println(WeixinPayUtil.mapToBean(WeixinPayUtil.xmlToMap(xml), DeclareQueryResult.class));
        }
        long e = System.currentTimeMillis() - b;
        System.out.println(e/100);

        System.out.println("9000"+new Random().nextInt(9)+new Random().nextInt(9)+new Random().nextInt(9)+new Random().nextInt(9)+new Random().nextInt(9)+new Random().nextInt(9));
        System.out.println(UUID.randomUUID().toString().replaceAll("-",""));




    }



    public static <T> T mapToBean(Map<String, ?> map, Class<T> tClass) throws IllegalAccessException, InstantiationException {
        if(map == null || tClass == null) return null;
        T t = tClass.newInstance();
        List<Field> fields = getAllField(tClass);
        System.out.println(t.getClass().getName());
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = entry.getKey();
            for (Field field : fields) {
                if (field.getName().equals(key)) {
                    field.setAccessible(true);
                    field.set(t, entry.getValue());
                }
            }
        }
        return t;

    }


    public static List<Field> getAllField(Class t) {
        if (t == null) return Collections.emptyList();
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(getField(t)));
        Class c = t;
        while (c.getSuperclass() != null) {
            c = c.getSuperclass();
            fields.addAll(Arrays.asList(getField(c)));
        }
        return fields;
    }


    public static Field[] getField(Class t) {
        if (t == null) {
            return new Field[]{};
        }
        Field[] fields = t.getDeclaredFields();
        if (fields == null) return new Field[]{};
        return fields;

    }


}
