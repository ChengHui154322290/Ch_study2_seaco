package com.tp.test.sch;

import com.tp.service.sch.DocService;
import com.tp.service.sch.ISearchDataService;
import com.tp.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by ldr on 2016/2/17.
 */
public class DocTest extends BaseTest {

    @Autowired
    DocService docService;


    @Autowired
    private ISearchDataService searchDataService;



    @Test
    public void updateDoc() throws IOException, IllegalAccessException, InterruptedException {
        docService.updateItemDoc();
    }

    @Test
    public void testSearchDataAndUpdateDoc() throws InterruptedException, IOException, IllegalAccessException {

        searchDataService.processItemData();
        Thread.sleep(500);
        docService.updateItemDoc();

    }

    @Test
    public void testTotalUpdate() throws IOException, IllegalAccessException, InterruptedException {
        docService.delItemDocTotal();
        searchDataService.processItemDataTotal();
        docService.updateItemDocTotal();
    }

    @Test
    public void testDelAllDoc() throws IOException, IllegalAccessException, InterruptedException {
        docService.delItemDocTotal();

    }

    @Test
    public void testPushShopDoc() throws IllegalAccessException, InterruptedException, IOException {
        searchDataService.processShopData();
        docService.updateShopDoc();
    }

    @Test
    public void testUpdateSHOPDocTotal() throws IllegalAccessException, InterruptedException, IOException {
        docService.deleteShopDocTotal();
        searchDataService.processShopDataTotal();
        docService.updateShopDocTotal();
    }

}
