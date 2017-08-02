/**
 * 
 */
package com.tp.service.wms.thirdparty.sto;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.service.wms.thirdparty.sto.ws.ImportOrderByJsonString;
import com.tp.service.wms.thirdparty.sto.ws.ImportOrderByJsonStringE;
import com.tp.service.wms.thirdparty.sto.ws.ImportOrderByJsonStringResponse;
import com.tp.service.wms.thirdparty.sto.ws.ImportOrderByJsonStringResponseE;
import com.tp.service.wms.thirdparty.sto.ws.ImportOrderServiceService;
import com.tp.service.wms.thirdparty.sto.ws.ImportOrderServiceServiceStub;

/**
 * @author Administrator
 * 申通WMS仓库对接
 */
@Service
public class STOSoapClient {

	@Value("#{meta['wms.zt.importurl']}")
	private String url;
	
	private ImportOrderServiceService importOrderServiceService = null;
	
	private static final String requestData = "eyJvcmRlckxpc3QiOlt7ImFsbFByaWNlIjoiMC4wIiwiYnV5ZXJJZE51bWJlciI6IjAwMDAwMDAwMDAwMDAwMDAwMCIsImJ1eWVyTmFtZSI6IjAwMCIsImNhcnJpZXIiOiJTVE8iLCJmZWVBbW91bnQiOiIwLjAiLCJpbnN1cmVBbW91bnQiOiIwLjAiLCJpdGVtQ291bnQiOjEsIml0ZW1OYW1lIjoi5paw6KW/5YWwS2FyaWNhcmXlj6/nkZ7lurfph5Hoo4Ux5q61576K5aW257KJOTAwZyDkuKTnvZDoo4UiLCJpdGVtU2t1IjoiMTAxIiwiaXRlbVdlaWdodCI6IjAiLCJtYWlsTm8iOiIyMjAwMDAwMTcyOTMiLCJtZXJjaGFudE51bSI6IlhHR0oiLCJyZWNlaXZlQ2l0eSI6IumTnOW3neW4giIsInJlY2VpdmVDb3VudHkiOiLnjovnm4rljLrnuqLml5fooZfpgZPlip7kuovlpIQiLCJyZWNlaXZlTWFuIjoibWl4aW5nZmVuZyIsInJlY2VpdmVNYW5BZGRyZXNzIjoiZGFzZGFzZGFzZGFzYXNkYXNkYXMiLCJyZWNlaXZlTWFuUGhvbmUiOiIxMzMzMzMzMzMzMyIsInJlY2VpdmVQcm92aW5jZSI6IumZleilvyIsInNlbmRXYXJlaG91c2UiOiLopb/ni5flm73pmYUiLCJ0eExvZ2lzdGljSUQiOiIxMTAwMDgwMTkwMDAwMDAyIiwidW5pdFByaWNlIjoiMC4wIn1dLCJwYXNzd29yZCI6IjEyMyIsInVzZXJOYW1lIjoidGVzdCJ9";
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ImportOrderServiceService getClient() throws AxisFault{
		if (importOrderServiceService == null) {
			importOrderServiceService = new ImportOrderServiceServiceStub(url);
		}
		return importOrderServiceService;
	}
	//出库订单
	public String importOrder(String orderData) throws RemoteException{
		ImportOrderByJsonString string = new ImportOrderByJsonString();
		string.setArg0(orderData);
		ImportOrderByJsonStringE request = new ImportOrderByJsonStringE();
		request.setImportOrderByJsonString(string);
		ImportOrderByJsonStringResponseE response = getClient().importOrderByJsonString(request);
		ImportOrderByJsonStringResponse resStr = response.getImportOrderByJsonStringResponse();
		return resStr.get_return();
	}
	
	public static void main(String[] args) throws RemoteException{
		
		STOSoapClient soapClient = new STOSoapClient();
		
		soapClient.importOrder(requestData);
	}
	
}
