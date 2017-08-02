package com.tp.test.mmp;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.app.query.AppTopItemPageQuery;
import com.tp.dto.mmp.TopicDetailDTO;
import com.tp.dto.mmp.TopicItemBrandCategoryDTO;
import com.tp.enums.common.PlatformEnum;
import com.tp.query.mmp.CmsTopicSimpleQuery;
import com.tp.query.mmp.TopicItemPageQuery;
import com.tp.result.mmp.TopicItemInfoResult;
import com.tp.service.cms.ISingleBusTemService;
import com.tp.service.cms.app.IHaitaoAppService;
import com.tp.service.mmp.ITopicService;
import com.tp.service.prd.IItemRemoteService;
import com.tp.service.prd.IItemService;
import com.tp.test.BaseTest;

/**
 * Created by ldr on 2016/1/9.
 */
public class TopicServiceTest extends BaseTest {

    @Autowired
    private ITopicService topicService;
    @Autowired
    IHaitaoAppService haitaoAppService;
    @Autowired
    ISingleBusTemService singleBusTemService;
    @Autowired
    IItemRemoteService itemRemoteService;
    @Autowired
    IItemService itemService;

    @Test
    public void testGet() throws Exception {
        CmsTopicSimpleQuery query = new CmsTopicSimpleQuery();
        query.setPlatformType(PlatformEnum.ANDROID.getCode());
        query.setPageSize(20);
      PageInfo<TopicDetailDTO> page  = topicService.getTodayAllTopic(query);
        List<TopicDetailDTO> list = page.getRows();
        for(TopicDetailDTO detailDTO : list){
            System.out.println(detailDTO.getTopic().getId()+"   "+detailDTO.getTopic().getName()+"     "+ detailDTO.getTopic().getPlatformStr()+" "+  JSON.toJSONString(detailDTO));
        }
        System.out.println(JSON.toJSONString(page));

    }

    @Test
    public void testGetSalesCount(){
    	System.out.println(itemRemoteService.getSalesCountBySku("01010102700101"));
    }
    @Test
    public void testGetTopicItems(){
    	System.out.println(new Gson().toJson(itemService.queryItemSkuTopicInfoForAPPHaiTao("01010102680206","165311")));
    }
    
    @Test
    public void testGetTopicItemList(){
    	AppTopItemPageQuery query = new AppTopItemPageQuery();
    	query.setSpecialid(165311L);
    	query.setIsascending("1");
    	query.setCurpage(1);
    	query.setPageSize(20);
    	Gson gson = new Gson();
    	try {
			System.out.println(gson.toJson(singleBusTemService.loadTopiInfocHtmlApp( query)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 海淘专场列表
     * @throws Exception
     */
    @Test
    public void getHTList() throws Exception {
        List<Long> ids = Arrays.asList(165219L, 165218L, 165217L, 165216L);
        List<TopicDetailDTO> list = topicService.getTopicDetailByIdsForCms(null, ids);
        for(TopicDetailDTO topicDetailDTO:list){
            System.out.println(JSON.toJSONString(topicDetailDTO));
        }
    }

    @Test
    public void getTopicList(){
    	Gson gson = new Gson();
    	System.out.println(gson.toJson(haitaoAppService.htTopicAllList(null, 1, 20,new String[0])));
//    	System.out.println(gson.toJson(haitaoAppService.singleTemplet(1, 50)));
    }
    @Test
    public void getFunLab(){
    	Gson gson = new Gson();
    	System.out.println(gson.toJson(haitaoAppService.funLab()));
//    	System.out.println(gson.toJson(haitaoAppService.singleTemplet(1, 50)));
    }
    @Test
    public void getHTDetail() throws Exception {
        Long id = 165218L ;
        TopicItemBrandCategoryDTO result = topicService.queryTopicItemBrandAndCategoryList(id);
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void getHTItems() throws Exception {
        TopicItemPageQuery query = new TopicItemPageQuery();
        query.setPageId(1);
        query.setPageSize(10);
        query.setTopicId(165219L);
        PageInfo<TopicItemInfoResult>  pageInfo = topicService.queryTopicPageItemByCondition(query);
        for(TopicItemInfoResult t: pageInfo.getRows()){
            System.out.println(JSON.toJSONString(t));
        }
        System.out.println(JSON.toJSONString(pageInfo));
    }




}
