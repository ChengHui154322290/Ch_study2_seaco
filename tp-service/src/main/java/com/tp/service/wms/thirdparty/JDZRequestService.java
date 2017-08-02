package com.tp.service.wms.thirdparty;

import com.alibaba.fastjson.JSONObject;
import com.tp.exception.ServiceException;
import com.tp.model.wms.jdz.JdzRequestUser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ldr on 2016/6/30.
 */
@Service
public class JDZRequestService {

    @Value("#{meta['wms.jdz.saleOrderurl']}")
    private String serviceUrl;

    @Value("#{meta['wms.jdz.purchaseOrderUrl']}")
    private String purchaseOrderUrl;

    @Value("#{meta['wms.jdz.appKey']}")
    private String jdzAppKey;

    @Value("#{meta['wms.jdz.password']}")
    private String jdzSecret;

    //电商备案编号
    @Value("#{meta['wms.jdz.providerCode']}")
    private String providerCode;

    //账册编号
    @Value("#{meta['wms.jdz.manualNo']}")
    private String manualNo;

    //账册编号
    @Value("#{meta['wms.jdz.goodsOwner']}")
    private String goodsOwner;

    @Value("#{meta['wms.jdz.supplierCode']}")
    private String supplierCode;


    public String getServiceId() {
        JdzRequestUser user = getRequestUser();
        return JSONObject.toJSONString(user);
    }

    public JdzRequestUser getRequestUser() {
        if (jdzAppKey == null) throw new ServiceException("WMS jdzAppKey配置为空");
        if (jdzSecret == null) throw new ServiceException("WMS jdzSecret配置为空");
        JdzRequestUser user = new JdzRequestUser();
        user.setAppkey(jdzAppKey);
        user.setSecret(JdzRequestUser.encryptSecret(jdzSecret));
        return user;
    }

    public Map<String, String> getHeaders(String serviceId) {
        Map<String, String> headers = new HashMap<>(2);
        headers.put("serviceId", serviceId);
        return headers;
    }

    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>(2);
        headers.put("serviceId", this.getServiceId());
        return headers;
    }

    public String getGoodsOwner() {
        return goodsOwner;
    }

    public String getJdzAppKey() {
        return jdzAppKey;
    }

    public String getJdzSecret() {
        return jdzSecret;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public String getManualNo() {
        return manualNo;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public String getPurchaseOrderUrl() {
        return purchaseOrderUrl;
    }
}
