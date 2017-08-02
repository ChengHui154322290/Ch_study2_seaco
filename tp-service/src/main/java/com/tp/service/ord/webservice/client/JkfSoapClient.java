package com.tp.service.ord.webservice.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.service.ord.webservice.client.JKF.ReceiveEncryptDeclareService;
import com.tp.service.ord.webservice.client.JKF.ReceiveEncryptDeclareServiceImplService;
import com.tp.service.ord.webservice.client.JKF.ReceiveEncryptDeclareServiceImplServiceLocator;
import com.tp.service.ord.webservice.client.JKF.ReceivedDeclareService;
import com.tp.service.ord.webservice.client.JKF.ReceivedDeclareServiceImplService;
import com.tp.service.ord.webservice.client.JKF.ReceivedDeclareServiceImplServiceLocator;

/**
 *	电子口岸申报数据接口 
 */
@Service
public class JkfSoapClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(JkfSoapClient.class);
	
	@Value("#{meta['JKF.declare.url']}")
	private String url;

	@Value("#{meta['JKF.declare.encrypturl']}")
	private String encryptUrl;
	
	private ReceivedDeclareService jkfClient = null;
	
	private ReceiveEncryptDeclareService jkfEncryptClient = null;
		
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEncryptUrl() {
		return encryptUrl;
	}

	public void setEncryptUrl(String encryptUrl) {
		this.encryptUrl = encryptUrl;
	}

	public ReceivedDeclareService getJKFClient() throws ServiceException, MalformedURLException {
		if (jkfClient == null) {
			ReceivedDeclareServiceImplService service = 
					new ReceivedDeclareServiceImplServiceLocator();
			URL endpointURL = new URL(url);
			jkfClient = service.getReceivedDeclareServiceImplPort(endpointURL);
			return jkfClient;
		}
		return jkfClient;
	}
	
	public ReceiveEncryptDeclareService getEncryptJKFClient() throws MalformedURLException, ServiceException{
		if (jkfEncryptClient == null) {
			ReceiveEncryptDeclareServiceImplService service = 
					new ReceiveEncryptDeclareServiceImplServiceLocator();
			URL endpointURL = new URL(encryptUrl);
			jkfEncryptClient = service.getReceiveEncryptDeclareServiceImplPort(endpointURL);
			return jkfEncryptClient;
		}
		return jkfEncryptClient;
	}
	
	//数据申报
	public String declareData(String data, String businessType, String sourceType) throws RemoteException, MalformedURLException, ServiceException{
		LOGGER.info("电子口岸申报数据：类型:{}, 数据:{}", businessType, data);
		return getJKFClient().checkReceived(data, businessType, sourceType);
	}
	
	//加密申报
	public String declareData(String data, String businessType, String dataDigest, String companyCode) throws RemoteException, MalformedURLException, ServiceException {
		LOGGER.info("电子口岸申报数据：类型:{}, 数据:{}, 发送方企业代码:{}", businessType, data, companyCode);
		return getEncryptJKFClient().receive(data, businessType, dataDigest, companyCode);
	}
	
}
