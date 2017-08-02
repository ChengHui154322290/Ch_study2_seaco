package com.tp.seller.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.seller.domain.ExcelCellStyle;
import com.tp.seller.domain.ExcelExportTemplate;
import com.tp.seller.domain.ExcelKeyValue;
import com.tp.seller.domain.ExcelSheetStyle;

/**
 * excel操作
 * 
 * @author yfxie
 */
public class ExcelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);
    private static final int DEFAULT_COLUMN_WIDTH = 20;

    /**
     * 导出excel
     * 
     * @param response
     * @param workbook
     */
    public static void export(final HttpServletResponse response, final String fileName, final Workbook workbook) {
        ServletOutputStream fileOut = null;
        try {
            // 导出到文件
            response.setContentType("application/vnd.ms-excel");
            fileOut = response.getOutputStream();
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", -1);
            response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setStatus(HttpServletResponse.SC_OK);
            response.flushBuffer();
            workbook.write(fileOut);
            fileOut.close();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (final IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 生成excel workbook
     * 
     * @param exportTemplate
     * @param dataList
     * @return
     */
    public static Workbook generateWorkbook(final ExcelExportTemplate exportTemplate,
        final List<List<String>> dataList, final String sheetName) {
        final Workbook wb = new HSSFWorkbook();
        final Sheet sheet = wb.createSheet(sheetName);
        sheet.setDefaultColumnWidth(DEFAULT_COLUMN_WIDTH);

        final ExcelSheetStyle excelSheetStyle = new ExcelSheetStyle();
        exportTemplate.initExcelSheetStyle(wb, excelSheetStyle);
        final List<String> autoColumn = excelSheetStyle.getAutoWidthColumn();
        final Map<String, Integer> columnWidth = excelSheetStyle.getColumnWidth();

        int dateLen = 0;

        if (null != dataList && (dateLen = dataList.size()) > 0) {
            final Map<Integer, ExcelCellStyle> dataIndexStyleMap = new HashMap<Integer, ExcelCellStyle>();
            final Map<String, Integer> headerIndexMap = new HashMap<String, Integer>();

            setTitleRegion(sheet, excelSheetStyle);

            setHeaderData(sheet, excelSheetStyle, dataIndexStyleMap, headerIndexMap, exportTemplate.getHeaderValues());

            setBodyData(dataList, sheet, dateLen, dataIndexStyleMap, excelSheetStyle);

            // 设置自动调整大小的列
            if (null != autoColumn && autoColumn.size() > 0) {
                for (final String hVal : autoColumn) {
                    if (null == headerIndexMap.get(hVal)) {
                        continue;
                    }
                    sheet.autoSizeColumn(headerIndexMap.get(hVal));
                }
            }

            if (null != columnWidth) {
                for (final Map.Entry<String, Integer> widthCol : columnWidth.entrySet()) {
                    if (null == headerIndexMap.get(widthCol.getKey())) {
                        continue;
                    }
                    sheet.setColumnWidth(headerIndexMap.get(widthCol.getKey()), widthCol.getValue() * 256);
                }
            }
        }
        return wb;
    }

    /**
     * 设置标题区域
     * 
     * @param sheet
     * @param excelSheetStyle
     */
    private static void setTitleRegion(final Sheet sheet, final ExcelSheetStyle excelSheetStyle) {
        final ExcelCellStyle style = excelSheetStyle.getTitleStyle();
        if (null != style) {
            final Row titleRow = sheet.createRow(0);
            titleRow.setHeightInPoints(excelSheetStyle.getTitleHeight());
            final Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(excelSheetStyle.getTitleValue());
            titleCell.setCellStyle(style.getCellStyle());
            sheet.addMergedRegion(CellRangeAddress.valueOf(excelSheetStyle.getTitleRegion()));
        }
    }

    /**
     * 设置体部数据
     * 
     * @param dataList
     * @param sheet
     * @param dateLen
     * @param dataIndexStyleMap
     */
    private static void setBodyData(final List<List<String>> dataList, final Sheet sheet, final int dateLen,
        final Map<Integer, ExcelCellStyle> dataIndexStyleMap, final ExcelSheetStyle excelSheetStyle) {
        final int startColumn = excelSheetStyle.getStartColumn();
        final int startLine = excelSheetStyle.getStartLine();
        for (int i = 0; i < dateLen; i++) {
            final List<String> oneLine = dataList.get(i);
            if (null == oneLine) {
                continue;
            }
            final Row oneRow = sheet.createRow(i + startLine);
            if (null != excelSheetStyle.getDefaultLineHeight()) {
                oneRow.setHeightInPoints(excelSheetStyle.getDefaultLineHeight());
            }
            for (int celIndex = 0; celIndex < oneLine.size(); celIndex++) {
                final Cell cell = oneRow.createCell(celIndex + startColumn);
                final String val = oneLine.get(celIndex);
                cell.setCellValue(val);
                setCellStyle(cell, dataIndexStyleMap.get(celIndex));
            }
        }
    }

    /**
     * 设置头部数据
     * 
     * @param sheet
     * @param headerMap
     * @param dataMap
     * @param dataIndexStyleMap
     * @param headerIndexMap
     * @param headLine
     */
    private static void setHeaderData(final Sheet sheet, final ExcelSheetStyle excelSheetStyle,
        final Map<Integer, ExcelCellStyle> dataIndexStyleMap, final Map<String, Integer> headerIndexMap,
        final List<ExcelKeyValue> headerValues) {
        final Map<String, ExcelCellStyle> headerMap = excelSheetStyle.getHeaderStyle();
        final Map<String, ExcelCellStyle> dataMap = excelSheetStyle.getColumnStyle();
        final int startColumn = excelSheetStyle.getStartColumn();
        if (null != headerValues && headerValues.size() > 0) {
            final Row oneRow = sheet.createRow(excelSheetStyle.getStartLine() - 1);
            if (null != excelSheetStyle.getDefaultLineHeight()) {
                oneRow.setHeightInPoints(excelSheetStyle.getDefaultLineHeight());
            }
            for (int i = 0; i < headerValues.size(); i++) {
                final Cell cell = oneRow.createCell(i + startColumn);
                final String val = headerValues.get(i).getValue();
                cell.setCellValue(val);
                setCellStyle(cell, headerMap.get(val));
                dataIndexStyleMap.put(i, dataMap.get(val));
                headerIndexMap.put(val, i);
            }
        }
    }

    // /**
    // * 初始化excel样式
    // *
    // * @param wb
    // * @param fileTemplate
    // * @return
    // */
    // private static ExcelSheetStyle initExcelStyle(Workbook wb,String fileTemplate) {
    // ExcelSheetStyle excelSheetStyle = new ExcelSheetStyle();
    // Method initMethod = ExcelConstant.TEMPLATE_DATA_STYLE_MAP.get(fileTemplate);
    // if(null != initMethod){
    // try {
    // initMethod.invoke(initMethod.getClass(),wb,excelSheetStyle);
    // } catch (IllegalAccessException e) {
    // e.printStackTrace();
    // } catch (IllegalArgumentException e) {
    // e.printStackTrace();
    // } catch (InvocationTargetException e) {
    // e.printStackTrace();
    // }
    // }
    // return excelSheetStyle;
    // }

    /**
     * 设置单元格类型
     * 
     * @param cell
     */
    private static void setCellStyle(final Cell cell, final ExcelCellStyle excelStyle) {
        if (null == excelStyle) {
            return;
        }
        final CellStyle cellStyle = excelStyle.getCellStyle();
        cell.setCellStyle(cellStyle);
        cell.setCellType(excelStyle.getDataType());
    }

    /**
     * 创建excel字体
     * 
     * @return
     */
    public static Font createFont(final Workbook workbook, final boolean isItalic, final Byte underLine,
        final boolean isBold) {
        final Font font = workbook.createFont();
        font.setItalic(isItalic);
        if (null != underLine) {
            font.setUnderline(underLine);
        }
        if (isBold) {
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setFontName("Courier New");
        }
        return font;
    }

    /**
     * 创建一个单元格类型
     * 
     * @return
     */
    public static CellStyle createCellStyle(final Workbook workbook, final Byte backgroundColor,
        final Byte foregroundColor, final Font font) {
        final CellStyle cellStyle = workbook.createCellStyle();
        final DataFormat format = workbook.createDataFormat();
        if (null != backgroundColor) {
            cellStyle.setFillBackgroundColor(backgroundColor);
            cellStyle.setFillPattern(CellStyle.BIG_SPOTS);
        }
        if (null != foregroundColor) {
            cellStyle.setFillForegroundColor(foregroundColor);
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        }
        setBorder(cellStyle);
        cellStyle.setFont(font);
        cellStyle.setDataFormat(format.getFormat("@"));
        return cellStyle;
    }

    /**
     * 设置边框
     * 
     * @param cellStyle
     */
    private static void setBorder(final CellStyle cellStyle) {
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
    }

    /**
     * 创建一个单元格类型
     * 
     * @return
     */
    public static CellStyle createDateCellStyle(final Workbook workbook, final String dateFormat, final Font font) {
        final CreationHelper createHelper = workbook.getCreationHelper();
        createHelper.createDataFormat();
        final CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(dateFormat));
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 创建单元格类型
     * 
     * @return
     */
    public static CellStyle createCellStyle(final Workbook workbook, final short align, final short valign,
        final Font font) {
        final CellStyle cellStyle = workbook.createCellStyle();
        final DataFormat format = workbook.createDataFormat();
        cellStyle.setAlignment(align);
        cellStyle.setDataFormat(format.getFormat("@"));
        cellStyle.setVerticalAlignment(valign);
        setBorder(cellStyle);
        cellStyle.setFont(font);
        return cellStyle;
    }

}
