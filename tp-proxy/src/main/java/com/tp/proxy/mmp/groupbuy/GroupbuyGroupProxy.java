package com.tp.proxy.mmp.groupbuy;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.groupbuy.Groupbuy;
import com.tp.model.mmp.GroupbuyGroup;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.IBaseService;
import com.tp.service.mmp.groupbuy.IGroupbuyGroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 阶梯团信息表代理层
 * @author szy
 *
 */
@Service
public class GroupbuyGroupProxy extends BaseProxy<GroupbuyGroup>{

	@Autowired
	private IGroupbuyGroupService groupbuyGroupService;

	@Override
	public IBaseService<GroupbuyGroup> getService() {
		return groupbuyGroupService;
	}


	public ResultInfo<PageInfo<GroupbuyGroup>> query(GroupbuyGroup groupbuyGroup){
		final ResultInfo<PageInfo<GroupbuyGroup>> result = new ResultInfo<>();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				PageInfo<GroupbuyGroup> page = groupbuyGroupService.query(groupbuyGroup);
				result.setData(page);
			}
		});
		return result;
	}
}
