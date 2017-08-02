package com.tp.test.sch;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.tp.model.sch.Search;

/**
 * Created by ldr on 2016/2/17.
 */
public class SimT {

    public static void main(String[] args) {
        Field[] fields = Search.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            processFieldName(fieldName);
        }
    }

    private static String processFieldName(String fieldName) {
        List<Integer> position = new ArrayList<>();
        for (int i = 0; i < fieldName.length(); i++) {
            char a = fieldName.charAt(i);
            if (a >= 'A' && a <= 'Z') {
                position.add(i);
            }
        }
        int jj;
        if (position.isEmpty()) return fieldName;
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < position.size(); j++) {
            if (j == 0) {
                int start = 0;
                int end = position.get(0);
                sb.append(fieldName.substring(start, end)).append("_");
            } else {
                int ssadf;
                int start = position.get(j - 1);
                int end = position.get(j);
                sb.append(fieldName.substring(start, end)).append("_");
            }
        }
        sb.append(fieldName.substring(position.get(position.size() - 1)));
        return sb.toString().toLowerCase();
    }
}
