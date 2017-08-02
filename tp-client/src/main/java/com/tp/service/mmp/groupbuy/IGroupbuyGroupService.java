package com.tp.service.mmp.groupbuy;

import com.tp.common.vo.PageInfo;
import com.tp.model.mmp.GroupbuyGroup;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 阶梯团信息表接口
  */
public interface IGroupbuyGroupService extends IBaseService<GroupbuyGroup>{

    Integer updateExpiredGroup();

    PageInfo<GroupbuyGroup> query(GroupbuyGroup groupbuyGroup);

}
