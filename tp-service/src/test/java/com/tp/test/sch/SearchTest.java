package com.tp.test.sch;

import com.alibaba.fastjson.JSON;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sch.Element;
import com.tp.model.sch.result.Aggregate;
import com.tp.model.sch.result.ItemResult;
import com.tp.query.sch.SearchQuery;
import com.tp.service.sch.ISearchService;
import com.tp.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * Created by ldr on 2016/2/18.
 */
public class SearchTest extends BaseTest {

    @Autowired
    private ISearchService searchService;

    @Test
    public void testSearch() throws Exception {
        SearchQuery query = new SearchQuery();
        query.setKey("奶粉");
       // query.setSort(Sort.PRICE_ASC.getCode());
      // query.setBrandId(2L);
       // query.setNavBrandId(90L);
        //query.setHasInventoryOnly(1);
        query.setPageSize(50);
       // query.setPlatform(PlatformEnum.PC);
        ResultInfo<PageInfo<ItemResult>> result = searchService.search(query);
        ResultInfo<List<Aggregate>> agg = searchService.aggregate(query);
        System.out.println(result.getData());
        System.out.println(result.getData().getRows());
        List<ItemResult> itemList = result.getData().getRows();
        for(ItemResult item: itemList){
            System.out.println(item.getTopic_price()+"          "+item.getSale_price()+"    "+item.getInventory()+"     " +item.getBrand_name()+"       " +item.getPlatform()+"     " + item.getItem_name());
        }
        System.out.println(agg.getData());
        for(Element element: agg.getData().get(0).getElements()){
            System.out.println(element);
        }

    }

    @Test
    public void testNavSearch() throws Exception {
        SearchQuery query = new SearchQuery();
        query.setPageSize(50);
        //query.setSort(Sort.PRICE_ASC.getCode());
      //   query.setBrandId(1L);
      //  query.setHasInventoryOnly(1);
      //   query.setNavBrandId(2L);

        query.setNavCategoryId(163l);
        //query.setCountryId(115L);
        //query.setBrandId(53L);
        ResultInfo<PageInfo<ItemResult>> result = searchService.search(query);
        ResultInfo<List<Aggregate>> agg = searchService.aggregate(query);

        System.out.println("搜索结果"+ JSON.toJSONString(result));
        List<ItemResult> itemList = result.getData().getRows();
        for(ItemResult item: itemList){
            System.out.println(item.getTopic_price()+"  "+item.getInventory()+" " +item.getBrand_name()+"   " +item.getPlatform()+" " + item.getItem_name()+" category:"+item.getL_category_id()+"-"+item.getM_category_id()+"-"+item.getS_category_id());
        }
        printAggregate(agg);
    }

    @Test
    public void testAggregate() throws IOException {
        SearchQuery query = new SearchQuery();
        query.setPageSize(50);
        //query.setSort(Sort.PRICE_ASC.getCode());
        //   query.setBrandId(1L);
        //  query.setHasInventoryOnly(1);
        //query.setNavBrandId(3999L);

         query.setNavCategoryId(162L);
        //query.setKey("naifen");
        ResultInfo<List<Aggregate>> agg = searchService.aggregate(query);

        printAggregate(agg);


    }

    private void printAggregate(ResultInfo<List<Aggregate>> agg) {
        System.out.println("统计结果"+ JSON.toJSONString(agg));
        for(Aggregate a: agg.getData()){
            System.out.println(a.getName()+"    "+a.getCode());
            for(Element element: a.getElements()){
                System.out.println(element.getKey()+"   "+element.getValue());
            }
        }
    }

    @Test
    public void testLN() throws Exception {
        for(int i = 0;i<100;i++ ){
            long b = System.currentTimeMillis();
            testNavSearch();
            System.out.println("TTTTTTTTTTTTTTT:" + (System.currentTimeMillis() - b));
            System.out.println("end");
            Thread.sleep(200);
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        }
    }


}
