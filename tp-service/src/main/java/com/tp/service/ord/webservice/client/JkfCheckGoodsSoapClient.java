package com.tp.service.ord.webservice.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.service.ord.webservice.client.JKF.CheckGoodsDeclService;
import com.tp.service.ord.webservice.client.JKF.CheckGoodsDeclServiceImplService;
import com.tp.service.ord.webservice.client.JKF.CheckGoodsDeclServiceImplServiceLocator;


/**
 * 个人物品申报单状态查询 
 */
@Service
public class JkfCheckGoodsSoapClient {
	
	@Value("#{meta['JKF.checkgoods.url']}")
	private String url;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JkfCheckGoodsSoapClient.class);

	
	private CheckGoodsDeclService jkfCheckGoodsDeclClient = null;

	public CheckGoodsDeclService getJkfCheckGoodsDeclClient() throws MalformedURLException, ServiceException {
		if (jkfCheckGoodsDeclClient == null) {
			CheckGoodsDeclServiceImplService service = new CheckGoodsDeclServiceImplServiceLocator();
			URL endpointURL = new URL(url);
			jkfCheckGoodsDeclClient = service.getCheckGoodsDeclServiceImplPort(endpointURL);
			return jkfCheckGoodsDeclClient;
		}
		return jkfCheckGoodsDeclClient;
	}

	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}	
	
	public String queryGoodsDeclStatus(String wayBillNo) throws RemoteException, MalformedURLException, ServiceException{
		LOGGER.info("查询个人物品申报状态：{}", wayBillNo);
		return getJkfCheckGoodsDeclClient().check(wayBillNo);
	}
}
