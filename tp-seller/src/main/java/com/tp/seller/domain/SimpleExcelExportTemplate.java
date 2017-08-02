package com.tp.seller.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * 简单的excel导出
 */
public class SimpleExcelExportTemplate extends ExcelExportTemplate {

    private List<ExcelKeyValue> excelHeaderValues;

    public SimpleExcelExportTemplate(final SellerExcelHeader excelEnum) {
        List<ExcelKeyValue> excelKeyVals = null;
        if (null != excelEnum && null != (excelKeyVals = excelEnum.getExcelKeyValues())) {
            this.excelHeaderValues = excelKeyVals;
        } else {
            this.excelHeaderValues = new ArrayList<ExcelKeyValue>();
        }
    }

    @Override
    public void initExcelTitle(final Workbook workbook, final ExcelSheetStyle excelSheetStyle) {
        // 默认第一行为标题
        excelSheetStyle.setStartLine(1);
    }

    @Override
    public void initCellStyle(final ExcelSheetStyle excelSheetStyle, final ExcelCellStyle cellValStr,
        final ExcelCellStyle cellValNum) {

        final Map<String, ExcelCellStyle> columnStyleMap = new HashMap<String, ExcelCellStyle>();

        for (int i = 0; i < excelHeaderValues.size(); i++) {
            final ExcelKeyValue keyVal = excelHeaderValues.get(i);
            columnStyleMap.put(keyVal.getKey(), cellValStr);
        }
        excelSheetStyle.setColumnStyle(columnStyleMap);

    }

    @Override
    public void initHeadCellStyle(final ExcelSheetStyle excelSheetStyle, final ExcelCellStyle headCellValStr) {
        final Map<String, ExcelCellStyle> headStyleMap = new HashMap<String, ExcelCellStyle>();
        for (int i = 0; i < excelHeaderValues.size(); i++) {
            final ExcelKeyValue keyVal = excelHeaderValues.get(i);
            headStyleMap.put(keyVal.getKey(), headCellValStr);
        }
        excelSheetStyle.setHeaderStyle(headStyleMap);
    }

    @Override
    public void initExcelColumnWidth(final ExcelSheetStyle excelSheetStyle) {
    }

    @Override
    public List<ExcelKeyValue> getHeaderValues() {
        return excelHeaderValues;
    }

}
