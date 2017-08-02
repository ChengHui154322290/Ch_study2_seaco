package com.tp.seller.domain;

import java.util.List;
import java.util.Map;

import com.tp.seller.constant.ExcelHeaderOrderEnum;

/**
 * excel
 * 
 * @author yfxie
 */
public class ExcelHeaderOrderDTO extends SellerExcelHeader {

    public ExcelHeaderOrderDTO() {
        excelKeyValue.clear();
        headerIndex.clear();
        ExcelHeaderOrderEnum[] orders = ExcelHeaderOrderEnum.values();
        for (int i = 0; i < orders.length; i++) {
            ExcelHeaderOrderEnum order = orders[i];
            ExcelKeyValue excelKey = new ExcelKeyValue();
            excelKey.setKey(order.getKeyName());
            excelKey.setValue(order.getKeyName());
            excelKeyValue.add(excelKey);
            headerIndex.put(order.getKeyName(), new Integer(i));
        }
    }

    @Override
    public List<ExcelKeyValue> getExcelKeyValues() {
        return excelKeyValue;
    }

    /**
     * 获取头部的索引
     * 
     * @return
     */
    @Override
    public Map<String, Integer> getHeaderIndex() {
        return headerIndex;
    }

}
