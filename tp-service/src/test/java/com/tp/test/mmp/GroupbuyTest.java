package com.tp.test.mmp;

import com.alibaba.fastjson.JSON;
import com.tp.dto.mmp.groupbuy.GroupbuyDetail;
import com.tp.dto.mmp.groupbuy.GroupbuyGroupDTO;
import com.tp.model.mmp.GroupbuyGroup;
import com.tp.service.mmp.ITopicService;
import com.tp.service.mmp.groupbuy.GroupbuyService;
import com.tp.service.mmp.groupbuy.IGroupbuyGroupService;
import com.tp.service.mmp.groupbuy.IGroupbuySendMsgService;
import com.tp.service.mmp.groupbuy.IGroupbuyService;
import com.tp.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by ldr on 2016/3/16.
 */
public class GroupbuyTest extends BaseTest {


    @Autowired
    private IGroupbuyService groupbuyService;

    @Autowired
    private IGroupbuyGroupService groupbuyGroupService;

    @Autowired
    private ITopicService topicService;

    @Autowired
    private IGroupbuySendMsgService groupbuySendMsgService;

    @Test
    public void testProcess(){
        GroupbuyDetail detail= groupbuyService.getGroupbuyDetail(53L,null,68953L);
        System.out.println("detail:"+detail);
        GroupbuyDetail groupbuyDetail =    groupbuyService.launch(detail.getGroupbuyId(),68953L);
        System.out.println("groupbuyDetail:"+groupbuyDetail);
        GroupbuyDetail groupbuyDetail1 = groupbuyService.join(groupbuyDetail.getGroupbuyId(),groupbuyDetail.getGroupId(),68971L);
        System.out.println("JoinDetail:"+groupbuyDetail1);


    }

    @Test
    public void test30(){
        Long memberId = 68775L;
        for(int i = 30 ;i >0;i--){

            groupbuyService.join(56L,34L,memberId);

            memberId++;
        }

  }

    @Test
    public void testLaunch(){

    GroupbuyDetail groupbuyDetail =    groupbuyService.launch(56L,68954L);
        System.out.println(groupbuyDetail);
        System.out.println(JSON.toJSONString(groupbuyDetail));
    }

    @Test
    public void testGetDetail(){
     GroupbuyDetail detail= groupbuyService.getGroupbuyDetail(53L,null,68953L);
        System.out.println(detail);
        System.out.println(JSON.toJSONString(detail));
    }

    @Test
    public void testGetDetailWithGroupId(){
        GroupbuyDetail detail= groupbuyService.getGroupbuyDetail(53L,28L,68971L);
        System.out.println(detail);
        System.out.println(JSON.toJSONString(detail));
    }

    @Test
    public void testJoin() throws InterruptedException {
        System.out.println( groupbuyService.join(53L,27L,68971L));
        Thread.sleep(3000L);
    }

    @Test
    public void testSms() throws InterruptedException {
        groupbuySendMsgService.sendMsg(24L);

    } @Test
    public void testScan() throws InterruptedException {
        topicService.scanTopicStatus(new Date());

    }

    @Test
    public void testMy(){
        List<GroupbuyGroupDTO> groupbuyGroupList = groupbuyService.myLaunch(68953L);
        List<GroupbuyGroupDTO> groupbuyGroupList2 = groupbuyService.myJoin(68954L);

        for(GroupbuyGroupDTO groupbuyGroup: groupbuyGroupList){
            System.out.println(JSON.toJSONString(groupbuyGroup));
        }


        for(GroupbuyGroup groupbuyGroup: groupbuyGroupList2){
            System.out.println(JSON.toJSONString(groupbuyGroup));
        }

        System.out.println(JSON.toJSONString(groupbuyGroupList));
        System.out.println(JSON.toJSONString(groupbuyGroupList2));

    }

    @Test
    public void update(){
        groupbuyGroupService.updateExpiredGroup();
    }
}
