package com.tp.m.query.seagoorpay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装分页和排序参数及分页查询结果.
 */
public class PageForSeagoorPay<T> extends SeagoorPayBaseVO implements Serializable {

    private static final long serialVersionUID = 2832561155483478118L;

    private int cur_page = 1;//当前页

    private int page_size = 10;//每页数量

    private int total_count = -1;//总数量

    private int total_page_count = 0; //总页数

    private List<T> list = new ArrayList<>();


    public int getCur_page() {
        return cur_page;
    }

    public void setCur_page(int cur_page) {
        this.cur_page = cur_page;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getTotal_count() {


        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getTotal_page_count() {
        if (total_count % page_size > 0)
            return total_count / page_size +1;
        return total_count / page_size;
    }

    public void setTotal_page_count(int total_page_count) {
        this.total_page_count = total_page_count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}

