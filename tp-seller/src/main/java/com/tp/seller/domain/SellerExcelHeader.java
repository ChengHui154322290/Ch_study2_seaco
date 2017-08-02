package com.tp.seller.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel头部定义
 * 
 * @author yfxie
 */
public abstract class SellerExcelHeader {

    public final Map<String, Integer> headerIndex = new HashMap<String, Integer>();
    public final List<ExcelKeyValue> excelKeyValue = new ArrayList<ExcelKeyValue>();

    public abstract List<ExcelKeyValue> getExcelKeyValues();

    public abstract Map<String, Integer> getHeaderIndex();

    /**
     * 初始化头部信息
     * 
     * @param header
     */
    public void initHeader(final List<String> header) {
        headerIndex.clear();
        excelKeyValue.clear();
        if (null != header && header.size() > 0) {
            for (int i = 0; i < header.size(); i++) {
                ExcelKeyValue excelKey = new ExcelKeyValue();
                excelKey.setKey(header.get(i));
                excelKey.setValue(header.get(i));
                excelKeyValue.add(excelKey);
                headerIndex.put(header.get(i), new Integer(i));
            }
        }
    }

}
