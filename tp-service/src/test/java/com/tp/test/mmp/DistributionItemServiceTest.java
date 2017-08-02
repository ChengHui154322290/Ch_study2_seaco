package com.tp.test.mmp;

import com.alibaba.fastjson.JSON;
import com.tp.common.vo.PageInfo;
import com.tp.dto.mmp.distribution.DistributionItem;
import com.tp.dto.mmp.distribution.DistributionItemQuery;
import com.tp.service.mmp.distribution.DistributionItemService;
import com.tp.service.mmp.distribution.IDistributionItemService;
import com.tp.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ldr on 2016/4/19.
 */
public class DistributionItemServiceTest extends BaseTest {

    @Autowired
    private IDistributionItemService  distributionItemService;

    @Test
    public void testLists(){
        DistributionItemQuery query = new DistributionItemQuery();
        query.setStartPage(0);
        query.setPageSize(10);
        PageInfo<DistributionItem> pageInfo= distributionItemService.getDistributionItems(query);
        System.out.println(JSON.toJSONString(pageInfo));
    }
}
