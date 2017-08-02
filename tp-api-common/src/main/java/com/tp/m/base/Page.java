package com.tp.m.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装分页和排序参数及分页查询结果.
 */
public class Page<T>  implements Serializable{

	private static final long serialVersionUID = 2832561155483478118L;
	
	private int curpage = 1;//当前页

	private int pagesize = 10;//每页数量

	private int totalcount = -1;//总数量
	
	private int totalpagecount = 0; //总页数
	
	/**订单总金额*/
	private Double totalMoney;
	

	private List<T> list = new ArrayList<>();

	public Page() {
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}
	public int getTotalpagecount() {
		if(totalpagecount > 0) return totalpagecount;
		int count = totalcount / pagesize;
		if (totalcount % pagesize > 0) {
			count++;
		}
		totalpagecount = count;
		return totalpagecount;
	}
	public void setTotalpagecount(int totalpagecount) {
		this.totalpagecount = totalpagecount;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public int getCurpage() {
		return curpage;
	}
	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}

	public void setFieldTCount(List<T> list,int curpage,int totalcount){
		this.list = list;
		this.curpage = curpage;
		this.totalcount = totalcount;
	}
	
	public void setFieldTPageCount(List<T> list,int curpage,int totalpagecount){
		this.list = list;
		this.curpage = curpage;
		this.totalpagecount = totalpagecount;
	}
	public Double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}
	
}

