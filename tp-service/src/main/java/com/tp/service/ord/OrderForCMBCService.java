package com.tp.service.ord;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.tp.dto.cmbc.MemberCMBCDto;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;
import com.tp.service.ord.IOrderForCMBCService;
import com.tp.service.ord.IOrderItemService;
import com.tp.service.ord.ISubOrderService;
import com.tp.util.DateUtil;
import com.tp.util.MD5Util;


@Service
public class OrderForCMBCService implements IOrderForCMBCService {
	
	private static final Logger log = LoggerFactory.getLogger(OrderForCMBCService.class);

	private static final String Md5key = "A909ebE4120B044121418D1e9008";

	@Autowired
	private ISubOrderService subOrderService;
	
	@Autowired
	private IOrderItemService orderItemService;
	

	@Value("${cmbc_registermember_url}")
	private String cmbc_registermember_url; 
	
	@Value("${cmbc_order_url}")
	private String cmbc_order_url; 
	
	
	@Override
	public List<SubOrder> getSubOrderByTime( Map<String, Object> params){			
		
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
		return suborderList;
	}
	
	@Override
	public List<SubOrder> pushSubOrderToCMBC(  List<SubOrder> suborderList){			
		List<SubOrder> failedlist = new ArrayList<SubOrder>();
		try{
			if( !suborderList.isEmpty() ){
				for(SubOrder sub : suborderList){
					String utf8str = createXml(sub);
					if(utf8str.isEmpty()){
						return failedlist;
					}					
					String   sign = getMD5( (Md5key + utf8str).getBytes("UTF-8") );
					if( !pushtoSerivce(cmbc_order_url+sign, utf8str) ){
						failedlist.add(sub);
					}
				}				
			}			
		}catch(Exception e){
			log.info(e.getMessage());
		}
		return failedlist;
	}
	
	
	
	@Override
	public boolean pushNewMemberToCMBC(  MemberCMBCDto member ){			
		try{
			String utf8str = createXml_Member(member);
			if (utf8str.isEmpty()) {
				return true;
			}
			String sign = getMD5((Md5key + utf8str).getBytes("UTF-8"));
			if (!pushtoSerivce(cmbc_registermember_url + sign, utf8str)) {
				return false;
			}
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return true;
	}

	
    public static String getMD5(byte[] source) {
        String s = null;
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                // >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            s = new String(str); // 换后的结果转换为字符串

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

	 private String createXml(SubOrder suborder) {  
		 try{			 
	            // 定义工厂 API，使应用程序能够从 XML 文档获取生成 DOM 对象树的解析器  
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	            // 定义 API， 使其从 XML 文档获取 DOM 文档实例。使用此类，应用程序员可以从 XML 获取一个 Document  
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            // Document 接口表示整个 HTML 或 XML 文档。从概念上讲，它是文档树的根，并提供对文档数据的基本访问  
	            Document document = builder.newDocument();  
	            document.setXmlStandalone(true);	              
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
	            uuid.appendChild(document.createTextNode( MD5Util.encrypt( suborder.getMemberId().toString() ) ));  
	            head.appendChild(uuid);
	            Element PIN = document.createElement("PIN");  		
	            PIN.appendChild(document.createTextNode( suborder.getUuid() ));  
	            head.appendChild(PIN);
	            Element TPIN = document.createElement("TPIN");  	// 
	            TPIN.appendChild(document.createTextNode( suborder.getTpin() ));  
	            head.appendChild(TPIN);	            
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
	            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");  
	            // 一个节点后换行，你可以设置为true，然后尝试解析看打印结果  
	            // transformer.setOutputProperty(OutputKeys.INDENT, "yes");  
	            // 向文本输出流打印对象的格式化表示形式  
	            // 要保证你的文本输出后格式不乱码，打印对象需指定打印格式，以标记此文本支持的格式  
//	            PrintWriter pw = new PrintWriter(filePath, "utf-8");  
//	            // 充当转换结果的持有者，可以为 XML、纯文本、HTML 或某些其他格式的标记  
//	            StreamResult result = new StreamResult(pw);  
//	            transformer.transform(source, result);  	            
	            StringWriter sw = new StringWriter();  
	            StreamResult xmlResult = new StreamResult(sw);  
	            transformer.transform(source, xmlResult); 	            
//	            log.info( sw.toString() );
	            System.out.println("生成XML文件成功!");  
	            return sw.toString();	            
		 }catch(Exception e){
	            return "";	            			 
		 }
	  }
	
	 
	 
	 private String createXml_Member(MemberCMBCDto member) {  
		 try{			 
	            // 定义工厂 API，使应用程序能够从 XML 文档获取生成 DOM 对象树的解析器  
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	            // 定义 API， 使其从 XML 文档获取 DOM 文档实例。使用此类，应用程序员可以从 XML 获取一个 Document  
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            // Document 接口表示整个 HTML 或 XML 文档。从概念上讲，它是文档树的根，并提供对文档数据的基本访问  
	            Document document = builder.newDocument();  
	            document.setXmlStandalone(true);	              
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
	            uuid.appendChild(document.createTextNode( MD5Util.encrypt( member.getMemberId().toString() ) ));  
	            head.appendChild(uuid);
	            Element PIN = document.createElement("PIN"); 
	            PIN.appendChild(document.createTextNode( member.getUnionVal() ));  
	            head.appendChild(PIN);
	            Element TPIN = document.createElement("TPIN");  	// 
	            TPIN.appendChild(document.createTextNode( member.getTpin() ));  
	            head.appendChild(TPIN);	            
	            Element SendTime = document.createElement("SendTime");  
	            SendTime.appendChild(document.createTextNode( DateUtil.formatDateTime( new Date() ))  );  
	            head.appendChild(SendTime);
	            Element Mobile = document.createElement("Mobile");  
	            Mobile.appendChild(document.createTextNode( member.getMobile() ) );  
	            head.appendChild(Mobile);
	            Element Asyn = document.createElement("Asyn");  
	            Asyn.appendChild(document.createTextNode("false"));  
	            head.appendChild(Asyn);	            
	            Element ReturnUrl = document.createElement("ReturnUrl");  
	            ReturnUrl.appendChild(document.createTextNode(""));  
	            head.appendChild(ReturnUrl);	            
	            TransformerFactory tf = TransformerFactory.newInstance();  
	            // 此抽象类的实例能够将源树转换为结果树  
	            Transformer transformer = tf.newTransformer();  
	            DOMSource source = new DOMSource(document);  
	            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");  
	            // 一个节点后换行，你可以设置为true，然后尝试解析看打印结果  
	            // transformer.setOutputProperty(OutputKeys.INDENT, "yes");  
	            // 向文本输出流打印对象的格式化表示形式  
	            // 要保证你的文本输出后格式不乱码，打印对象需指定打印格式，以标记此文本支持的格式  
//	            PrintWriter pw = new PrintWriter(filePath, "utf-8");  
//	            // 充当转换结果的持有者，可以为 XML、纯文本、HTML 或某些其他格式的标记  
//	            StreamResult result = new StreamResult(pw);  
//	            transformer.transform(source, result);  	            
	            StringWriter sw = new StringWriter();  
	            StreamResult xmlResult = new StreamResult(sw);  
	            transformer.transform(source, xmlResult); 	            
//	            log.info( sw.toString() );
	            System.out.println("生成XML文件成功!");  
	            return sw.toString();	            
		 }catch(Exception e){
	            return "";	            			 
		 }
	  }
    private boolean pushtoSerivce(String url, String xmlstr)
    {	        	    	   
	   try{	    		       
		   HttpClient httpclient = new HttpClient();  
		   PostMethod post  = new PostMethod(url);  
		   try {
			   RequestEntity entity = new StringRequestEntity( xmlstr, "text/xml",  "UTF-8");  
			   post.setRequestEntity(entity);  
			   httpclient.executeMethod(post);   
			   int code = post.getStatusCode();  
			   if (code == HttpStatus.SC_OK)  
			   {	
				  if(  parserXML( post.getResponseBodyAsString() ) ){
					  log.info("[xg-service -民生银行 success 请求url] = {}", url);
					  log.info("[xg-service -民生银行 success 请求报文] = {}", xmlstr);
					  log.info("[xg-service -民生银行 success 响应报文] = {}", post.getResponseBodyAsString() );
					   return true;					  
				  }else{
					  log.info("[xg-service -民生银行 failed 请求url] = {}", url);
					  log.info("[xg-service -民生银行 failed 请求报文] = {}", xmlstr);
					  log.info("[xg-service -民生银行 failed 响应报文] = {}", post.getResponseBodyAsString() );
					  return false;
				  }
			   }
         } catch (Exception ex) {  
             ex.printStackTrace();  
         } finally {  
             post.releaseConnection();  
         }  	           	            
    } catch (Exception e) {  
        e.printStackTrace();  
    }  
	 return false;
}  
	
	

    public boolean parserXML(String strXML){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            StringReader sr = new StringReader(strXML);
            InputSource is = new InputSource(sr);
            Document doc = builder.parse(is);
            Element rootElement = doc.getDocumentElement();
            NodeList ResponseCodelist = rootElement.getElementsByTagName("ResponseCode");
            for (int i = 0; i < ResponseCodelist.getLength(); i++) {
              Node property = ResponseCodelist.item(i);
              String nodeName = property.getNodeName();
              String values = property.getFirstChild().getNodeValue();
              if( Integer.valueOf(values).equals(1) ){
            	  return true;
              }              
            }
                        
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info(e.getMessage());          
        }catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info(e.getMessage());          
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info(e.getMessage());          
        }
        return false;
    }

}
   
