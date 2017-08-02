package com.tp.seller.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel sheet模板
 * 
 * @author yfxie
 */
public class ExcelSheetStyle implements Serializable {

    private static final long serialVersionUID = -486034987626512220L;

    /**
     * 标题样式
     */
    private ExcelCellStyle titleStyle;

    /** 数据列的类型 */
    private Map<String, ExcelCellStyle> columnStyle = new HashMap<String, ExcelCellStyle>();;

    /** 头部样式 */
    private Map<String, ExcelCellStyle> headerStyle = new HashMap<String, ExcelCellStyle>();

    /**
     * 标题行高度
     */
    private Integer titleHeight;

    /**
     * 标题区域
     */
    private String titleRegion;

    /**
     * 标题
     */
    private String titleValue;

    /**
     * 默认的行的高度
     */
    private Integer defaultLineHeight;

    /**
     * 开始行
     */
    private Integer startLine = 0;

    /**
     * 开始列
     */
    private Integer startColumn = 0;

    /**
     * 列的宽度
     */
    private Map<String, Integer> columnWidth = new HashMap<String, Integer>();

    /** 自动设置宽度的列 */
    private List<String> autoWidthColumn = new ArrayList<String>();

    public Map<String, ExcelCellStyle> getColumnStyle() {
        return columnStyle;
    }

    public void setColumnStyle(final Map<String, ExcelCellStyle> columnStyle) {
        this.columnStyle = columnStyle;
    }

    public Map<String, ExcelCellStyle> getHeaderStyle() {
        return headerStyle;
    }

    public void setHeaderStyle(final Map<String, ExcelCellStyle> headerStyle) {
        this.headerStyle = headerStyle;
    }

    public List<String> getAutoWidthColumn() {
        return autoWidthColumn;
    }

    public void setAutoWidthColumn(final List<String> autoWidthColumn) {
        this.autoWidthColumn = autoWidthColumn;
    }

    public Map<String, Integer> getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(final Map<String, Integer> columnWidth) {
        this.columnWidth = columnWidth;
    }

    public ExcelCellStyle getTitleStyle() {
        return titleStyle;
    }

    public void setTitleStyle(final ExcelCellStyle titleStyle) {
        this.titleStyle = titleStyle;
    }

    public Integer getStartLine() {
        return startLine;
    }

    public void setStartLine(final Integer startLine) {
        this.startLine = startLine;
    }

    public Integer getStartColumn() {
        return startColumn;
    }

    public void setStartColumn(final Integer startColumn) {
        this.startColumn = startColumn;
    }

    public Integer getTitleHeight() {
        return titleHeight;
    }

    public void setTitleHeight(final Integer titleHeight) {
        this.titleHeight = titleHeight;
    }

    public String getTitleRegion() {
        return titleRegion;
    }

    public void setTitleRegion(final String titleRegion) {
        this.titleRegion = titleRegion;
    }

    public String getTitleValue() {
        return titleValue;
    }

    public void setTitleValue(final String titleValue) {
        this.titleValue = titleValue;
    }

    public Integer getDefaultLineHeight() {
        return defaultLineHeight;
    }

    public void setDefaultLineHeight(final Integer defaultLineHeight) {
        this.defaultLineHeight = defaultLineHeight;
    }

}
