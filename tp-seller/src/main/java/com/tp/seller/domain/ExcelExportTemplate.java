package com.tp.seller.domain;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

import com.tp.seller.util.ExcelUtil;

public abstract class ExcelExportTemplate {

    private static final int DEFAULT_LINE_HEIGHT = 20;

    /**
     * 初始化
     * 
     * @param workbook
     */
    public void initExcelSheetStyle(final Workbook workbook, final ExcelSheetStyle excelSheetStyle) {

        /**
         * 列的类型
         */
        final CellStyle headerCellStyle = ExcelUtil.createCellStyle(workbook, null,
            (byte) IndexedColors.AQUA.getIndex(), ExcelUtil.createFont(workbook, false, null, true));
        headerCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        final CellStyle dataCellStyle = ExcelUtil.createCellStyle(workbook, null, null,
            ExcelUtil.createFont(workbook, false, null, false));
        dataCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        final CellStyle dateStyle = ExcelUtil.createDateCellStyle(workbook, "yyyy-MM-dd HH:mm:ss",
            ExcelUtil.createFont(workbook, false, null, false));

        final ExcelCellStyle cellValStr = new ExcelCellStyle(dataCellStyle, Cell.CELL_TYPE_STRING);
        final ExcelCellStyle cellValNum = new ExcelCellStyle(dateStyle, Cell.CELL_TYPE_NUMERIC);
        final ExcelCellStyle headCellValStr = new ExcelCellStyle(headerCellStyle, Cell.CELL_TYPE_STRING);

        excelSheetStyle.setDefaultLineHeight(DEFAULT_LINE_HEIGHT);

        initExcelColumnWidth(excelSheetStyle);

        initExcelTitle(workbook, excelSheetStyle);

        initHeadCellStyle(excelSheetStyle, headCellValStr);

        initCellStyle(excelSheetStyle, cellValStr, cellValNum);
    }

    /**
     * 获取头部值
     * 
     * @return
     */
    public abstract List<ExcelKeyValue> getHeaderValues();

    /**
     * 必须设置开始行和开始列 示例： CellStyle style = workbook.createCellStyle(); Font titleFont = workbook.createFont();
     * titleFont.setFontHeightInPoints((short) 18); titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
     * style.setAlignment(CellStyle.ALIGN_CENTER); style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
     * style.setFont(titleFont); excelSheetStyle.setTitleStyle(new ExcelCellStyle(style, Cell.CELL_TYPE_STRING));
     * excelSheetStyle.setTitleRegion("$A$1:$L$1"); excelSheetStyle.setTitleHeight(45);
     * excelSheetStyle.setTitleValue("测试标题"); excelSheetStyle.setStartLine(1); excelSheetStyle.setStartColumn(0);
     */
    public abstract void initExcelTitle(Workbook workbook, ExcelSheetStyle excelSheetStyle);

    /**
     * 初始化数据的样式
     * 
     * @param excelSheetStyle
     * @param cellValStr
     * @param cellValNum
     */
    public abstract void initCellStyle(ExcelSheetStyle excelSheetStyle, ExcelCellStyle cellValStr,
        ExcelCellStyle cellValNum);

    /**
     * 初始化excel的第一行
     * 
     * @param excelSheetStyle
     * @param headCellValStr
     */
    public abstract void initHeadCellStyle(ExcelSheetStyle excelSheetStyle, ExcelCellStyle headCellValStr);

    /**
     * @param excelSheetStyle 示例： List<String> autoWidthColumn = new ArrayList<String>();
     *            autoWidthColumn.add(PRODUCT_NAME.getKeyName()); autoWidthColumn.add(ADRESS.getKeyName());
     *            Map<String,Integer> columnWidth = new HashMap<String,Integer>();
     *            columnWidth.put(ADDRESS.getKeyName(), 40); excelSheetStyle.setAutoWidthColumn(autoWidthColumn);
     *            excelSheetStyle.setColumnWidth(columnWidth);
     */
    public abstract void initExcelColumnWidth(ExcelSheetStyle excelSheetStyle);

}
