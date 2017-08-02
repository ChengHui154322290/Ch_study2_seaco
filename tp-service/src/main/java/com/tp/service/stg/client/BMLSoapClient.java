package com.tp.service.stg.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tp.dto.stg.BML.ASNsDetail;
import com.tp.dto.stg.BML.ASNsDto;
import com.tp.dto.stg.BML.OrderDetails;
import com.tp.dto.stg.BML.OrdersDto;
import com.tp.dto.stg.BML.OutPutBackShipSkuDto;
import com.tp.dto.stg.BML.OutputBackShipDto;
import com.tp.dto.stg.BML.ResponseDto;
import com.tp.dto.stg.BML.StockDto;
import com.tp.model.stg.vo.feedback.ResponseVO;
import com.tp.service.stg.client.BML.BMLQuery;
import com.tp.service.stg.client.BML.BMLQueryLocator;
import com.tp.service.stg.client.BML.BMLQueryPortType;


/**
 * 标杆物流仓储管理系统接口客户端
 * 
 * @author
 * @Date 2015-1-6 10:50:24
 */
@Service
public class BMLSoapClient {


	private static final Logger log = LoggerFactory.getLogger(BMLSoapClient.class);
	
	private String url;
	
	private String username;
	
	private String password;
	
	public void setUrl(String url) {
		this.url = url;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private BMLQueryPortType queryPort = null;

	public BMLQueryPortType getBMLQueryClient() throws MalformedURLException, ServiceException {
		if (queryPort == null) {
			BMLQuery query = new BMLQueryLocator();
			URL endpointURL = new URL(url);
			queryPort = query.getBMLQueryHttpPort(endpointURL);
		}
		return queryPort;
	}

	/**
	 * 入库单
	 * 
	 * @param xml
	 * @return
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	public String ansToWms(String xml) throws RemoteException, MalformedURLException, ServiceException {
		log.info("（标杆）入库订单：{}",xml);
		return this.getBMLQueryClient().ansToWms(username, password, xml);
	}

	/**
	 * 入库单_分页
	 * 
	 * @param xml
	 * @return
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	public String ansToWmsbyPage(String xml) throws RemoteException, MalformedURLException, ServiceException {
		log.info("（标杆）入库订单分页：{}",xml);
		return this.getBMLQueryClient().ansToWmsbyPage(username, password, xml);
	}

	/**
	 * 出库单
	 * 
	 * @param xml
	 * @return
	 *  <pre>
	 * 			001  密钥检查未通过
	 *			002  解析XML内容失败
	 *			003  没有读取到根节点
	 *			004  没有解析到节点信息
	 *			005  数据重复
	 *			006  必填项为空
	 *			007  SKU不存在
	 *			008  保存失败
	 *			009  订单不存在
	 *</pre>
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	public String soToWms(String xml) throws RemoteException, MalformedURLException, ServiceException {
		log.info("（标杆）出库订单：{}",xml);
		return this.getBMLQueryClient().soToWms(username, password, xml);
	}

	/**
	 * 出库单_分页
	 * 
	 * @param xml
	 * @return
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	public String soToWmsbyPage(String xml) throws RemoteException, MalformedURLException, ServiceException {
		log.info("（标杆）出库订单分页：{}",xml);
		return this.getBMLQueryClient().soToWmsbyPage(username, password, xml);
	}

	/**
	 * 
	 * <pre>
	 * 商品订单状态查询
	 * </pre>
	 *
	 * @param orderCode
	 * @return
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	public ResponseDto searchOrderStatus(String orderCode) throws RemoteException, MalformedURLException, ServiceException {
		Map<Class<?>, String> alias= new HashMap<Class<?>, String>();
		alias.put(ResponseDto.class, "Response");
		String xml = this.getBMLQueryClient().searchOrderStatus(username, password,  orderCode);
		log.info("（标杆）订单状态查询:ordercode>>>{} response>>>{}",orderCode,xml);
		ResponseDto dto = (ResponseDto) BeanToXMLUtils.xmlToBean(xml, alias);
		return dto;
	}
	
	/**
	 * 查询标杆库存
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 * @throws RemoteException 
	 */
	@SuppressWarnings("unchecked")
	public  List<StockDto> searchInventory(String sku) throws RemoteException, MalformedURLException, ServiceException{
		Map<Class<?>, String> alias= new HashMap<Class<?>, String>();
		alias.put(StockDto.class, "Stock");
		alias.put(ArrayList.class, "StockQuery");
		String xml = this.getBMLQueryClient().stockQueryBySku(username,password, sku);
		log.info("（标杆）库存查询：sku>>>{} response>>>{}",sku,xml);
	    return (ArrayList<StockDto>) BeanToXMLUtils.xmlToBean(xml,alias);	
	}
	
	/**
	 * 
	 * <pre>
	 * 根据订单编号查询运货信息
	 * </pre>
	 *
	 * @param orderNo
	 * @return
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<OutputBackShipDto> shipmentInfoQueryByOrderId (String orderNo) throws RemoteException, MalformedURLException, ServiceException{
		Map<Class<?>, String> alias= new HashMap<Class<?>, String>();
		alias.put(OutputBackShipDto.class, "outputBack");
		alias.put(ArrayList.class, "outputBacks");
		alias.put(OutPutBackShipSkuDto.class, "sku");
		String xml = this.getBMLQueryClient().shipmentInfoQueryByOrderId(username, password, orderNo);
		log.info("（标杆）发运信息查询：orderNo>>>{} response>>>{}",orderNo,xml);
		return (List<OutputBackShipDto>) BeanToXMLUtils.xmlToBean(xml, alias);
	}
	
	/**
	 * 
	 * <pre>
	 * 根据采购订单号获取入库信息
	 * </pre>
	 *
	 * @param orderNo
	 * @return
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<ASNsDto> noticeOfArrivalQueryById(String orderNo) throws RemoteException, MalformedURLException, ServiceException{
		Map<Class<?>, String> alias= new HashMap<Class<?>, String>();
		alias.put(ASNsDto.class, "ASNs");
		alias.put(ArrayList.class, "ASNDetails");
		alias.put(ASNsDetail.class, "Detail");
		
		String xml = this.getBMLQueryClient().noticeOfArrivalQueryById(username, password, orderNo);
		log.info("（标杆）入库明细查询，orderNo>>>>{} response>>>{}",orderNo,xml);
		return (List<ASNsDto>) BeanToXMLUtils.xmlToBean(xml, alias);
	}
	

/**
 * 
 * <pre>
 * 根据客户订单号查询出库明细
 * </pre>
 *
 * @param orderNo
 * @return
 * @throws RemoteException
 * @throws MalformedURLException
 * @throws ServiceException
 */
	@SuppressWarnings("unchecked")
	public List<OrdersDto> orderDetailQueryById(String orderNo ) throws RemoteException, MalformedURLException, ServiceException{
		Map<Class<?>, String> alias= new HashMap<Class<?>, String>();
		alias.put(OrdersDto.class, "Orders");
		alias.put(ArrayList.class, "OrderDetails");
		alias.put(OrderDetails.class, "Detail");
		
		Map<Class<?>, String> implicitCollections = new HashMap<Class<?>, String>();
		implicitCollections.put(OrdersDto.class, "list");
		
		String xml = this.getBMLQueryClient().orderDetailQueryById(username, password, orderNo);
		log.info("（标杆）出库明细查询，orderNo>>>>{} response>>>{}",orderNo,xml);
		return (List<OrdersDto>) BeanToXMLUtils.xmlToBean(xml, alias,implicitCollections);
		
	}
	/**
	 * sku同步
	 * @param xml
	 * @return
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	public String singleSkuToWms(String xml) throws RemoteException, MalformedURLException, ServiceException{
		log.info("（标杆）sku同步，content>>>{}",xml);
		return this.getBMLQueryClient().singleSkuToWms(username, password, xml);
	}
	/**
	 * 取消订单
	 * @param orderCode
	 * @return
	 * 		<pre>
	 * 			返回值code说明：
	 *			000 取消成功 
	 * 			002 取消失败，订单已发运或者订单不存在
	 * 			003 取消失败，订单进入仓内作业（此时订单可拦截，可根据客户预设，视为取消成功或者失败）
	 * 			009 取消失败，取消时发生异常
	 * 		</pre>
	 * 
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	public ResponseVO cancelOrderRX(String orderCode) throws RemoteException, MalformedURLException, ServiceException{
		
		String result = this.getBMLQueryClient().cancelOrderRX(username, password, orderCode);
		Map<Class<?>, String> classAlias = new HashMap<Class<?>, String>();
		classAlias.put(ResponseVO.class, "Response");
		ResponseVO responseVO =(ResponseVO) BeanToXMLUtils.xmlToBean(result, classAlias);
		log.info("取消订单：orderCode>>>{} response>>>{}",orderCode,responseVO);
		return responseVO;
	}
	

}
