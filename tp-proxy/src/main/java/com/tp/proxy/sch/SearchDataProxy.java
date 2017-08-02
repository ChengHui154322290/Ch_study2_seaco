package com.tp.proxy.sch;

import com.tp.dto.common.ResultInfo;
import com.tp.model.sch.Search;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.IBaseService;
import com.tp.service.sch.IDocService;
import com.tp.service.sch.ISearchDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 代理层
 * @author szy
 *
 */
@Service
public class SearchDataProxy extends BaseProxy<Search>{

	@Autowired
	private ISearchDataService searchDataService;

	@Autowired
	private IDocService docService;

	@Override
	public IBaseService<Search> getService() {
		return searchDataService;
	}


	public ResultInfo updateSearchData(){
		ResultInfo result  = new ResultInfo();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				searchDataService.processItemData();
			}
		});
		return result;
	}

	public ResultInfo updateDoc(){
		ResultInfo result  = new ResultInfo();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				docService.updateItemDocTotal();
			}
		});
		return result;
	}

	public ResultInfo updateDataAndDocTotal(){
		ResultInfo result  = new ResultInfo();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				docService.delItemDocTotal();
				searchDataService.processItemDataTotal();
				docService.updateItemDocTotal();
			}
		});
		return result;
	}

	public ResultInfo updateDataAndDoc(){
		ResultInfo result  = new ResultInfo();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				searchDataService.processItemData();
				docService.updateItemDocTotal();
			}
		});
		return result;
	}

	public ResultInfo update(){
		ResultInfo result  = new ResultInfo();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				searchDataService.processItemData();
				docService.updateItemDoc();
			}
		});
		return result;
	}

	public ResultInfo clearDoc(){
		ResultInfo result  = new ResultInfo();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				docService.delItemDocTotal();
			}
		});
		return result;
	}

	public ResultInfo updateSHOPDoc(){
		ResultInfo resultInfo = new ResultInfo();
		this.execute(resultInfo, new Callback() {
			@Override
			public void process() throws Exception {
				searchDataService.processShopData();
				docService.updateShopDoc();
			}
		});
		return resultInfo;
	}

	public ResultInfo updateSHOPDocTOTAL(){
		ResultInfo resultInfo = new ResultInfo();
		this.execute(resultInfo, new Callback() {
			@Override
			public void process() throws Exception {
				docService.deleteShopDocTotal();
				searchDataService.processShopDataTotal();
				docService.updateShopDocTotal();
			}
		});
		return resultInfo;
	}



}
