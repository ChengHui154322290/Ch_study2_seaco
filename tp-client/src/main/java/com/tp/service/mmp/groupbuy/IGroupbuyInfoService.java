package com.tp.service.mmp.groupbuy;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.groupbuy.GroupbuyListDTO;
import com.tp.model.mmp.GroupbuyInfo;
import com.tp.query.mmp.TopicItemCartQuery;
import com.tp.service.IBaseService;

import java.util.List;

/**
  * @author szy 
  * 接口
  */
public interface IGroupbuyInfoService extends IBaseService<GroupbuyInfo>{

    ResultInfo checkForOrder(Long topicId,String sku,Long groupId,Long memberId);

    PageInfo<GroupbuyListDTO> list(GroupbuyListDTO query);
}
