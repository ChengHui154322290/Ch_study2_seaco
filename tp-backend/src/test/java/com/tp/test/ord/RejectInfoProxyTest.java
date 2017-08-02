package com.tp.test.ord;

import com.alibaba.fastjson.JSON;
import com.tp.common.annotation.Id;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.ord.RejectConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.*;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.dto.ord.remote.RejectDetailDTO;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.RejectItem;
import com.tp.proxy.mmp.facade.CouponFacadeProxy;
import com.tp.proxy.ord.RejectInfoProxy;
import com.tp.query.mmp.CouponOrderQuery;
import com.tp.query.mmp.MyCouponQuery;
import com.tp.test.BaseTest;

import junit.framework.Assert;

import org.apache.xmlbeans.impl.jam.mutable.MMember;
import org.junit.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.StylesDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.crypto.Data;

/**
 * Created by ldr on 2016/1/11.
 */
public class RejectInfoProxyTest extends BaseTest {

    @Autowired
    private RejectInfoProxy rejectInfoProxy;

    @Test
    public void testReject() {
    	   
    	// 68963L
    	// 68479L
    	Long userId = 68963L ;
    	int pageNo = 1;
    	int pageSize =  10;
    	ResultInfo<PageInfo<RejectInfo>> pageRejectinfo = rejectInfoProxy.queryMobilePageListByRejectQuery(userId, pageNo, pageSize,null,null);
    
    	if (pageRejectinfo.getData().getTotal() == 0 ) {
			System.out.println("空");
		}else{
			System.out.println("非空");
		}    
    }

    
    @Test
    public void testShowRejectHistoryForMemberId() {
    	RejectItem item = new RejectItem();
    	item.setOrderCode(1116022400000294L);
    	item.setOrderItemId(12798L);
    	Long userId = 68963L;
    	ResultInfo<RejectDetailDTO > rejectDetail = rejectInfoProxy.showRejectHistoryForMemberId( item, userId);
    	System.out.println("-----");   
    }
    

    
    
    @Test
    public void testApplyReturnGoods() {
    	 	
    	MemberInfo meminfo = new MemberInfo();
    	meminfo.setId(68963L);
    	meminfo.setMobile("15800381241");
    	meminfo.setEmail("");
    	meminfo.setNickName("15800381241");
    	meminfo.setPassword("ef468899870bbd143df745e9f45645d2");
    	meminfo.setSalt("734e625be52637435f196075a3810c7278321577");
    	//meminfo.setSex(sex);
    	//meminfo.setIsMobileVerified(isMobileVerified);
    	meminfo.setCreateTime( new Date() );
    	//meminfo.setIp(ip);
    	meminfo.setModifyTime(new Date() );
    	meminfo.setPlatForm(0);
    	meminfo.setState(true);
    	meminfo.setSource(1);
    	
    	
//    	  `reject_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '退货单ID',
//    	  `reject_code` bigint(20) NOT NULL COMMENT '退货单编号(24+yymmdd+（退货单ID，不足8位前补0）)',
//    	  `refund_code` bigint(16) DEFAULT NULL COMMENT '退款单编号',
//    	  `offset_code` bigint(16) DEFAULT NULL COMMENT '补偿单编号',
//    	  `express_no` varchar(64) DEFAULT NULL COMMENT '退回货物快递单号',
//    	  `company_name` varchar(32) DEFAULT NULL COMMENT '物流公司名称',
//    	  `company_code` varchar(32) DEFAULT NULL COMMENT '物流公司编号',
//    	  `post_kuaidi100` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否成功推送给快递100平台,0:没有成功，1:成功',
//    	  `post_kuaidi100_times` tinyint(1) NOT NULL DEFAULT '0' COMMENT '推送快递100次数',
    	RejectInfo rejectInfo = new RejectInfo();
    	rejectInfo.setRejectType(1);
    	rejectInfo.setOrderCode(1116022400000294L);
//    	rejectInfo.setOrderCode(1116022300000291L);    	
    	rejectInfo.setRefundAmount(0.01);
    	rejectInfo.setOffsetAmount(0.0);
    	rejectInfo.setRejectStatus(0);
    	rejectInfo.setAuditStatus(1);
    	rejectInfo.setRejectReason("2");
    	rejectInfo.setLinkMobile("15800381241");
    	rejectInfo.setLinkMan("张虹生");
    	rejectInfo.setBuyerImgUrl("1.jpg");
    	rejectInfo.setSellerImgUrl("2.jpg");
    	rejectInfo.setBuyerRemarks("没啥可说的1111111");
    	rejectInfo.setSellerRemarks("不退1111111");
    	rejectInfo.setRemarks("卵备注");
    	rejectInfo.setCustomCode("0102020009");
    	rejectInfo.setCreateTime(new Date());
    	rejectInfo.setCreateUser("张虹生");
    	rejectInfo.setUpdateTime(new Date());
    	rejectInfo.setUpdateUser("张虹生");
    	rejectInfo.setSupplierName("FS库存测试7777777");
    	rejectInfo.setSupplierId(838L);
    	rejectInfo.setUserId(68963L);
    	rejectInfo.setReturnAddress("还想要寄回地址");
//    	rejectInfo.setReturnContact("某某人");
//    	rejectInfo.setReturnMobile("15800362365");
     	
    	
//    	  `reject_item_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '退货单商品子项ID',
//    	  `reject_code` bigint(20) NOT NULL,
    	RejectItem rejectItem = new RejectItem();  
    	rejectItem.setOrderCode(1116022400000294L);
    	rejectItem.setOrderItemId(12798L);
    	rejectItem.setItemRefundQuantity(1);
    	rejectItem.setRemarks("哪有这么多备注啊");
    	rejectItem.setItemSkuCode("01020200090101");
    	rejectItem.setItemName("hellokitty");
    	rejectItem.setItemStatus(1);
    
    	try{
    		String reson = RejectConstant.REJECT_REASON.getCnName("2");
    		System.out.println(reson);
        	rejectInfoProxy.applyReturnGoods(meminfo, rejectInfo, rejectItem) ;    		
    	}catch(Exception e){
    		System.out.println(e.getMessage());
    	}
    }
    
    

    @Test
    public void testSaveExpressNoForMemberId() {
    	RejectInfo rejectInfo = new RejectInfo();
    	rejectInfo.setRejectId(552L);
    	rejectInfo.setPackageNo("45612311111");
    	rejectInfo.setCompanyName("zhs");
    	rejectInfo.setCompanyCode("zhs111");
    	Long userId =68963L; 
    	
    	try{
        	rejectInfoProxy.saveExpressNoForMemberId(rejectInfo, userId);    		
    	}catch(Exception e){
    		System.out.println(e.getMessage());
    	}
    }

    @Test
    public void testcancelReject() {
    	MemberInfo usr = new MemberInfo();    	
    	ResultInfo<Boolean>  res =  rejectInfoProxy.cancelReject( 556L, usr);    
    	if (res.success) {
			System.out.println("取消成功");
		}
    	else {
			System.out.println("取消失败");
		}
    }

    
    @Test
    public void testUpdateRejectData() {
    	MemberInfo meminfo = new MemberInfo();
    	meminfo.setId(68479L);
    	
    	RejectInfo rejectInfo = new RejectInfo();
    	rejectInfo.setRejectId(558L);
    	rejectInfo.setRefundAmount(0.01);
    	rejectInfo.setOffsetAmount(0.0);
    	rejectInfo.setRejectStatus(0);
    	rejectInfo.setAuditStatus(1);
    	rejectInfo.setRejectReason("想退就退");
    	rejectInfo.setLinkMobile("15800381241");
    	rejectInfo.setLinkMan("张虹生");
    	rejectInfo.setBuyerImgUrl("1.jpg");
    	rejectInfo.setSellerImgUrl("2.jpg");
    	rejectInfo.setBuyerRemarks("没啥可说的-- test2");
    	    	
    	RejectItem rejectItem = new RejectItem();
    	rejectItem.setItemRefundQuantity(2);
    	rejectItem.setItemRefundAmount( 0.02 );
    	
    	ResultInfo<Boolean>  res =  rejectInfoProxy.updateRejectData(meminfo, rejectInfo, rejectItem); 
    	if (res.success) {
			System.out.println("取消成功");
		}
    	else {
			System.out.println("取消失败");
		}
    }

    
}


