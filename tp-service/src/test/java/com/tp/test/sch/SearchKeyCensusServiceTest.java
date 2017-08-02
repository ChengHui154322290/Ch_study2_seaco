package com.tp.test.sch;

import com.tp.query.sch.SearchQuery;
import com.tp.service.sch.ISearchKeyCensusService;
import com.tp.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ldr on 2016/3/4.
 */
public class SearchKeyCensusServiceTest extends BaseTest {

    @Autowired
    private ISearchKeyCensusService searchKeyCensusService;

    @Test
    public void testAdd() throws InterruptedException {
        String key = "sdf33333";
        long b = System.currentTimeMillis();
        SearchQuery query = new SearchQuery();
        //query.setKey("33333");
        //query.setNavCategoryId( 139L);
        query.setNavBrandId(3L);
        searchKeyCensusService.addSearchKeyCensus(query);
        System.out.println("444444444444444444444444444444444444444444444444444444444444444444444444");
        System.out.println(System.currentTimeMillis() - b);

    }
}
