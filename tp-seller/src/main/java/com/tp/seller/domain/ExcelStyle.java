package com.tp.seller.domain;

/**
 * Excel的类型
 * 
 * @author yfxie
 */
public class ExcelStyle {

    public static Integer HSS_TYPE = 1;
    public static Integer XSS_TYPE = 2;
    /**
     * Excel的类型
     */
    private Integer excelType;

    public Integer getExcelType() {
        return excelType;
    }

    public void setExcelType(final Integer excelType) {
        this.excelType = excelType;
    }

}
