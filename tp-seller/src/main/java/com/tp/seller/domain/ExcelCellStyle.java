package com.tp.seller.domain;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * excel单元格的样式定义
 * 
 * @author yfxie
 */
public class ExcelCellStyle {

    /**
     * 单元格的样式
     */
    private CellStyle cellStyle;

    /**
     * 数据类型
     */
    private Integer dataType;

    /**
     * excel的类型
     * 
     * @param cellWidth
     * @param cellHeight
     * @param cellStyle
     */
    public ExcelCellStyle(final CellStyle cellStyle, final int dateType) {
        this.cellStyle = cellStyle;
        this.dataType = dateType;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(final CellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(final Integer dataType) {
        this.dataType = dataType;
    }

}
