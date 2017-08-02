package com.tp.seller.constant;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * excel常量定义
 * 
 * @author yfxie
 */
public final class ExcelConstant {

    /** 动态邮件模板 */
    public static final String DELIVERY_ORDER_TEMPLATE = "delivery_order_template";

    public static final Map<String, Method> TEMPLATE_DATA_STYLE_MAP = new HashMap<String, Method>();
    static {
        try {
            TEMPLATE_DATA_STYLE_MAP.put(DELIVERY_ORDER_TEMPLATE,
                DeliveryOrder.class.getMethod("initExcelSheetStyle", Workbook.class, ExcelSheetStyle.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ExcelConstant() {
    }
}
