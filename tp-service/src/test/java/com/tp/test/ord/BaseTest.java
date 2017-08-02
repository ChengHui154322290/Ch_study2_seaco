package com.tp.test.ord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;
import com.tp.query.ord.SubOrderQO;
import com.tp.service.ord.IOrderItemService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.directOrder.IDirectOrderService;
import com.tp.service.ord.mq.SalesOrderPaidCallback;
import com.tp.service.ord.remote.ISalesOrderRemoteService;

/**
 * Created by ldr on 2015/12/30.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:beans.xml"})
public class BaseTest {

	@Autowired
	private ISalesOrderRemoteService salesOrderRemoteService;
	@Autowired
	private SalesOrderPaidCallback salesOrderPaidCallback;
	@Autowired
	private IOrderItemService orderItemService;
	@Autowired
	private ISubOrderService subOrderService;
	@Autowired
	private IDirectOrderService directOrderService;
	@Test
	public void test (){
		SubOrderQO query = new SubOrderQO();
		query.setMemberId(68479L);
		query.setPageSize(10);
		query.setStartPage(1);
		salesOrderRemoteService.findOrderList4UserPage(query);
	}
	
	@Test
	public void testAddSalesCount (){
		salesOrderPaidCallback.itemSaleCount(1116031710000043L);
	}
	
	@Test
	public void testQuery() {
		Map<String, Object> params = new HashMap<>();
    	params.put("topicId", 120);
    	List<OrderItem> itemList = orderItemService.queryByParamNotEmpty(params);
    	System.out.println(itemList.size());
    	Set<Long> subOrderIds = new HashSet<>();
    	for(OrderItem item : itemList) {
    		subOrderIds.add(item.getOrderId());
    	}
    	List<SubOrder> subOrderList = subOrderService.selectListByIdList(new ArrayList<>(subOrderIds));
    	System.out.println(subOrderList.size());
	}
	
	@Test
	public void testDirectOrder(){
		directOrderService.searchDirectOrderForJob();
	}
	
}
