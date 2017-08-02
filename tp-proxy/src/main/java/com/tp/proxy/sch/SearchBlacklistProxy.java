package com.tp.proxy.sch;

import com.tp.dto.common.ResultInfo;
import com.tp.exception.ServiceException;
import com.tp.model.sch.SearchBlacklist;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.IBaseService;
import com.tp.service.sch.ISearchBlacklistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class SearchBlacklistProxy extends BaseProxy<SearchBlacklist>{

	@Autowired
	private ISearchBlacklistService searchBlacklistService;

	@Override
	public IBaseService<SearchBlacklist> getService() {
		return searchBlacklistService;
	}

	public ResultInfo doAdd(SearchBlacklist bl){
		final ResultInfo result =new ResultInfo();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				SearchBlacklist query = new SearchBlacklist();
				query.setIsDeleted(0);
				query.setValue(bl.getValue());
				int count = searchBlacklistService.queryByObjectCount(query);
				if(count>0) throw new ServiceException("已存在的黑名单");
				searchBlacklistService.insert(bl);
			}
		});
		return result;
	}
}
