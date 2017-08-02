package com.tp.service.ord;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.ord.OrderCodeType;
import com.tp.common.vo.ord.OrderErrorCodes;
import com.tp.dto.ord.OrderItemCMBC;
import com.tp.dto.ord.kuaidi100.SubscribeResult;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;
import com.tp.query.ord.SubOrderQO;
import com.tp.service.ord.IOrderForCMBCService;
import com.tp.service.ord.IOrderInfoService;
import com.tp.service.ord.IOrderItemService;
import com.tp.service.ord.ISubOrderService;
import com.tp.util.DateUtil;
import com.tp.util.HttpClientUtil;
import com.tp.test.BaseTest;

public class OrderForCMBCTest extends BaseTest{

	private static final Logger log = LoggerFactory.getLogger(OrderForCMBCTest.class);

	@Autowired
	private IOrderInfoService orderInfoService;
	
	@Autowired
	private ISubOrderService subOrderService;

	@Autowired
	private IOrderItemService orderItemService;
	
	
	@Autowired
	private IOrderForCMBCService orderForCMBCService;


	@Test
	public void testSelectOrderItemsForPushCMBC() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startdate", "2016-05-04 19:47:25");
		params.put("enddate",  "2016-05-05 10:58:35");
		List<OrderItemCMBC> listCMBC = subOrderService.selectOrderItemsForPushCMBC(params);
		log.info("");
	}

	
	
    

	@Test
	public void testorderinfotocmbc() {
		String url = "http://wx.fanry.com:5010/expand_cms_CmsWxElectronic_checkOrder?sign=";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startdate", "2016-05-04 19:47:25");
		params.put("enddate",  "2016-05-05 10:58:35");
		params.put("channelcode", OrderConstant.CHANNEL_CODE.cmbc.name() );			
		List<SubOrder> suborderList = orderForCMBCService.getSubOrderByTime(params);
		List<SubOrder> failedList = orderForCMBCService.pushSubOrderToCMBC(suborderList);
	}
	


	@Test
	public void testorderinfo() {
		
		Date queryDate = new Date();				
		log.info(queryDate.toString());

		try{
			
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("startdate", "2016-05-04 19:47:25");
			params.put("enddate",  "2016-05-05 10:58:35");
			List<SubOrder> suborderList = subOrderService.selectSubOrderForPushCMBC(params);			
			List<Long> ordercodeList = new ArrayList<Long>();
			if( !suborderList.isEmpty() ){
				for(SubOrder suborder : suborderList){
					ordercodeList.add(suborder.getId());					
				}
			}
			List<OrderItem> itemList = orderItemService.selectListByOrderIdList(ordercodeList);
			if(!itemList.isEmpty()){
				for(  SubOrder suborder : suborderList ){
					for( OrderItem item: itemList){
						if(suborder.getOrderCode().equals( item.getOrderCode() )){
							suborder.getOrderItemList().add(item);
						}
					}
					
				}				
			}				
			if( !suborderList.isEmpty() ){
				for(SubOrder sub : suborderList){
					String xmlstr = createXml(sub);		
					pushtoSerivce(xmlstr);				
				}				
			}


			
			log.debug("");
		}catch(Exception e){
			
		}
		
		


//		List<Integer> statuslist = new ArrayList<Integer>();
//		statuslist.add(OrderConstant.ORDER_STATUS.DELIVERY.code);
//		statuslist.add(OrderConstant.ORDER_STATUS.RECEIPT.code);
//		statuslist.add(OrderConstant.ORDER_STATUS.FINISH.code);
//		List<OrderInfo> retOrderList = orderInfoService.getOrderNeedPushToCMBC(statuslist);		
//				
//		log.debug("");
		
	}
	
	
	@Test
	public void testpush() {		
		OrderItemCMBC orderitem = new OrderItemCMBC();
		orderitem.setSkuCode("11111");		
//		String xmlstr = createXml(orderitem);		
//		pushtoSerivce(xmlstr);
	}

	
	 private String createXml(SubOrder suborder) {  
		 try{			 
	            // 定义工厂 API，使应用程序能够从 XML 文档获取生成 DOM 对象树的解析器  
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	            // 定义 API， 使其从 XML 文档获取 DOM 文档实例。使用此类，应用程序员可以从 XML 获取一个 Document  
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            // Document 接口表示整个 HTML 或 XML 文档。从概念上讲，它是文档树的根，并提供对文档数据的基本访问  
	            Document document = builder.newDocument();  
	              
	            Element packageList = document.createElement("PackageList");  
	            document.appendChild(packageList);  
	            
	            Element pkg = document.createElement("Package");  
	            packageList.appendChild(pkg);
	            	            	            
	            Element head = document.createElement("Header");  
	            pkg.appendChild(head);  
	            
	            Element reqType = document.createElement("RequestType");  
	            reqType.appendChild(document.createTextNode("02"));  
	            head.appendChild(reqType);

	            Element uuid = document.createElement("UUID");  
	            uuid.appendChild(document.createTextNode("14336376457645768578"));  
	            head.appendChild(uuid);

	            Element SendTime = document.createElement("SendTime");  
	            SendTime.appendChild(document.createTextNode( DateUtil.formatDateTime( new Date() ))  );  
	            head.appendChild(SendTime);

	            Element MSOKSerial = document.createElement("MSOKSerial");  
	            MSOKSerial.appendChild(document.createTextNode( String.valueOf( suborder.getOrderCode()) ) );  
	            head.appendChild(MSOKSerial);
	            
	            Element TotalMoney = document.createElement("TotalMoney");  
	            TotalMoney.appendChild(document.createTextNode( String.valueOf( suborder.getTotal()) ));  
	            head.appendChild(TotalMoney);

	            Element Asyn = document.createElement("Asyn");  
	            Asyn.appendChild(document.createTextNode("false"));  
	            head.appendChild(Asyn);
	            
	            Element ReturnUrl = document.createElement("ReturnUrl");  
	            ReturnUrl.appendChild(document.createTextNode(""));  
	            head.appendChild(ReturnUrl);

	            Element ProductCode = document.createElement("ProductCode");  
	            ProductCode.appendChild(document.createTextNode(  String.valueOf(suborder.getOrderCode()) ));  
	            head.appendChild(ProductCode);

	            Element Request = document.createElement("Request");  
	            pkg.appendChild(Request);
	            Element Order = document.createElement("Order");  
	            Request.appendChild(Order);

//	            Element Pin = document.createElement("Pin");  
//	            Pin.appendChild(document.createTextNode("flysolo"));  	            
//	            Order.appendChild(Pin);
	            Element OrderId = document.createElement("OrderId");  
	            OrderId.appendChild(document.createTextNode( String.valueOf(suborder.getOrderCode()) ));  	            
	            Order.appendChild(OrderId);
	            Element TotalMoney2 = document.createElement("TotalMoney");  
	            TotalMoney2.appendChild(document.createTextNode( String.valueOf( suborder.getTotal()) ));  	            
	            Order.appendChild(TotalMoney2);	            
	            Element Freight = document.createElement("Freight");  
	            Freight.appendChild(document.createTextNode( String.valueOf( suborder.getFreight())));  	            
	            Order.appendChild(Freight);	            
	            if( !suborder.getOrderItemList().isEmpty() ){
	            	for(OrderItem orditem : suborder.getOrderItemList()){
			            Element Item   = document.createElement("Item");  
			            Order.appendChild(Item);
			            Element SkuCode   = document.createElement("SkuCode");  
			            SkuCode  .appendChild(document.createTextNode( orditem.getSkuCode() ));  	            
			            Item.appendChild(SkuCode  );	            		
			            Element SpuCode   = document.createElement("SpuCode");  
			            SpuCode  .appendChild(document.createTextNode( orditem.getSpuCode() ));  	            
			            Item.appendChild(SpuCode  );	            		
			            Element SpuName   = document.createElement("SpuName");  
			            SpuName  .appendChild(document.createTextNode( orditem.getSpuName() ));  	            
			            Item.appendChild(SpuName  );	            		
			            Element Quantity   = document.createElement("Quantity");  
			            Quantity.appendChild(document.createTextNode( String.valueOf(orditem.getQuantity()) ) );  	            
			            Item.appendChild(Quantity  );	            		
			            Element SubTotal   = document.createElement("SubTotal");  
			            SubTotal.appendChild(document.createTextNode( String.valueOf(orditem.getSubTotal()) ) );  	            
			            Item.appendChild(SubTotal  );	            		
			            Element ItemFreight   = document.createElement("Freight");  
			            ItemFreight.appendChild(document.createTextNode( String.valueOf(orditem.getFreight()) ) );  	            
			            Item.appendChild(ItemFreight  );	   			            
	            	}		       		            
	            }
	            TransformerFactory tf = TransformerFactory.newInstance();  
	            // 此抽象类的实例能够将源树转换为结果树  
	            Transformer transformer = tf.newTransformer();  
	            DOMSource source = new DOMSource(document);  
	            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");  
	            // 一个节点后换行，你可以设置为true，然后尝试解析看打印结果  
	             transformer.setOutputProperty(OutputKeys.INDENT, "yes");  
	            // 向文本输出流打印对象的格式化表示形式  
	            // 要保证你的文本输出后格式不乱码，打印对象需指定打印格式，以标记此文本支持的格式  
//	            PrintWriter pw = new PrintWriter(filePath, "utf-8");  
//	            // 充当转换结果的持有者，可以为 XML、纯文本、HTML 或某些其他格式的标记  
//	            StreamResult result = new StreamResult(pw);  
//	            transformer.transform(source, result);  	            
	            StringWriter sw = new StringWriter();  
	            StreamResult xmlResult = new StreamResult(sw);  
	            transformer.transform(source, xmlResult); 	            
	            log.info(sw.toString() );
	            System.out.println("生成XML文件成功!");  
	            return sw.toString();	            
		 }catch(Exception e){
	            return "";	            			 
		 }
	  }
	
	

	       private void pushtoSerivce(String xmlstr)
	        {	        	    	   
	    	   try{	    		   
	        	String url = "http://localhost:8080/supplier/quotationList.htm";  
            
	        	HttpClient httpclient = new HttpClient();  
	        	PostMethod post  = new PostMethod(url);  
	        	String info = null;
	        	try {  
		             RequestEntity entity = new StringRequestEntity( xmlstr, "text/xml",  
		             "iso-8859-1");  
		             post.setRequestEntity(entity);  
		             httpclient.executeMethod(post);   
		             int code = post.getStatusCode();  
		             if (code == HttpStatus.SC_OK)  
		                 info = new String(post.getResponseBodyAsString());  
	                 info = new String(post.getResponseBodyAsString());  
		         } catch (Exception ex) {  
		             ex.printStackTrace();  
		         } finally {  
		             post.releaseConnection();  
		         }  	           	            
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  
	 
	 
	 	
}
