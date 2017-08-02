package com.tp.common.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PageInfo<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2626585127466370060L;
	
	/**当前页面 */
	private Integer page = DAOConstant.DEFUALT_PAGE;
	/**每页几行*/
	private Integer size = DAOConstant.DEFUALT_SIZE;
	/**页总数*/
	private Integer total=0;
	/**行总数*/
	private Integer records;
	
	/**数据集合*/
	private List<T> rows = new ArrayList<T>();

	private List<Integer> steps = new ArrayList<Integer>();
	
	private int step = 5;
	
	/**订单总金额*/
	private Double totalMoney=0d;
	public PageInfo(){
		
	}
	
	public PageInfo(Integer page,Integer size){
		setPage(page);
		setSize(size);
	}
	public Integer getStart() {
		if (page < 0 || page < 0) {
			return 0;
		} else {
			return ((page - 1) * size);
		}
	}
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		if(page!=null)
		this.page = page;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getRecords() {
		return records;
	}

	public void setRecords(Integer records) {
		this.records = records;
		if(records!=null && records>0 && size!=null && size>0){
			this.total = records/size;
			if(records%size>0){
				this.total = total+1;
			}
		}else{
			this.total = 0;
		}
		int begin = 1,end =total;
		if((page-step)>0){
			begin = page-step;
		}
		for(int i=begin;i<=page;i++){
			steps.add(i);
		}
		if(total>page){
			if((page+step)<total){
				end = page+step;
			}
			for(int i=page+1;i<=end;i++){
				steps.add(i);
			}
		}
		
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		if(size!=null)
		this.size = size;
	}

	public List<Integer> getSteps() {
		return steps;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public Double getTotalMoney() {
		return 	Math.round(totalMoney*100)/100.0;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}
	
}
