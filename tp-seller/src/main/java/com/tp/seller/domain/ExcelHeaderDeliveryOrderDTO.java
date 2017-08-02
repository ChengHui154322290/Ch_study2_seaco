package com.tp.seller.domain;

import java.util.List;
import java.util.Map;

import com.tp.seller.constant.ExcelHeaderOrderEnum;

/**
 * 发货订单导出模板
 */
public class ExcelHeaderDeliveryOrderDTO extends SellerExcelHeader {

    public ExcelHeaderDeliveryOrderDTO() {
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

    @Override
    public Map<String, Integer> getHeaderIndex() {
        return headerIndex;
    }

}
