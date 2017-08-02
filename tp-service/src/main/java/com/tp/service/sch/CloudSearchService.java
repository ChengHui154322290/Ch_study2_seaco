package com.tp.service.sch;

import com.aliyun.opensearch.CloudsearchClient;
import com.aliyun.opensearch.CloudsearchDoc;
import com.aliyun.opensearch.CloudsearchSearch;
import com.aliyun.opensearch.object.KeyTypeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * 开放搜索服务
 * Created by ldr on 2016/2/18.
 */
@Service
public class CloudSearchService {

    @Value("${search.accesskey}")
    private String accesskey;

    @Value("${search.secret}")
    private String secret;

    @Value("${search.appName}")
    private String appName;

    @Value("${search.shopAppName}")
    private String shopAppName;

    @Value("${search.host}")
    private String host;

    private String format = "json";

    public static final String ITEM_TABLE = "main";

    public static final String SHOP_TABLE ="main";

    public CloudsearchClient getCloudsearchClient() throws UnknownHostException {
        Map<String, Object> opts = new HashMap<>();
        CloudsearchClient client = new CloudsearchClient(accesskey, secret, host, opts, KeyTypeEnum.ALIYUN);
        return client;
    }

    public CloudsearchDoc getCloudsearchDoc() throws UnknownHostException {
        CloudsearchDoc doc = new CloudsearchDoc(appName, getCloudsearchClient());
        return  doc;
    }

    public CloudsearchSearch getCloudsearchSearch() throws UnknownHostException {
        CloudsearchClient client = getCloudsearchClient();
        CloudsearchSearch search = new CloudsearchSearch(client);
        search.addIndex(getAppName());
        search.setFormat(getFormat());
        return search;
    }

    public CloudsearchDoc getShopCloudsearchDoc(String appName) throws UnknownHostException {
        CloudsearchDoc doc = new CloudsearchDoc(appName, getCloudsearchClient());
        return  doc;
    }
    public CloudsearchSearch getCloudsearchSearch(String appName) throws UnknownHostException {
        CloudsearchClient client = getCloudsearchClient();
        CloudsearchSearch search = new CloudsearchSearch(client);
        search.addIndex(appName);
        search.setFormat(getFormat());
        return search;
    }


    public String getAppName() {
        return appName;
    }

    public String getFormat() {
        return format;
    }

    public String getShopAppName() {
        return shopAppName;
    }
}
