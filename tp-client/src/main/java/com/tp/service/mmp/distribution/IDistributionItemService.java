package com.tp.service.mmp.distribution;

import com.tp.common.vo.PageInfo;
import com.tp.dto.mmp.distribution.DistributionItem;
import com.tp.dto.mmp.distribution.DistributionItemQuery;

/**
 * Created by ldr on 2016/4/19.
 */
public interface IDistributionItemService {


     PageInfo<DistributionItem> getDistributionItems(DistributionItemQuery query);

}
