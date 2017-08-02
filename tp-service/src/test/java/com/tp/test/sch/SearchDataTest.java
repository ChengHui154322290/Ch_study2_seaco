package com.tp.test.sch;

import com.tp.service.sch.ISearchDataService;
import com.tp.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ldr on 2016/2/17.
 */
public class SearchDataTest extends BaseTest {


    @Autowired
    private ISearchDataService searchDataService;

    @Test
    public void testS(){
            searchDataService.processItemData();
    }

    @Test
    public void testShopData(){
        searchDataService.processShopData();
    }
}
