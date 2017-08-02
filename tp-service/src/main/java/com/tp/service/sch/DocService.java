package com.tp.service.sch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.opensearch.CloudsearchDoc;
import com.aliyun.opensearch.CloudsearchSearch;
import com.tp.dao.sch.SearchDao;
import com.tp.dao.sch.SearchShopDao;
import com.tp.dao.sch.TimestampDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.sch.enums.SearchRecordStatus;
import com.tp.dto.sch.enums.SearchTimestampCode;
import com.tp.model.sch.Search;
import com.tp.model.sch.SearchShop;
import com.tp.model.sch.result.ItemResult;
import com.tp.model.sch.result.ItemSearchResult;
import com.tp.service.sch.IDocService;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 搜索文档相关服务
 * Created by ldr on 2016/2/17.
 */
@Service
public class DocService implements IDocService {

    private static final int separate = 200;

    private static final List<String> NUMBER_ARRAY_FIELDS = Arrays.asList("platform","specIds");
    private static final List<String> STRING_ARRAY_FIELDS = Arrays.asList("specDetails","shopTag");

    @Autowired
    private SearchDao searchDao;

    @Autowired
    private TimestampDao timestampDao;

    @Autowired
    private SearchShopDao searchShopDao;

    @Autowired
    private CloudSearchService cloudSearchService;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void push() throws IllegalAccessException, InterruptedException, IOException {
        updateItemDoc();
        updateShopDoc();
    }

    @Override
    public ResultInfo pushAllItemDoc() throws IOException, IllegalAccessException {

        List<Search> searchList = searchDao.getAll();

        CloudsearchDoc doc = cloudSearchService.getCloudsearchDoc();

        for (Search search : searchList) {
            Map<String, Object> mapDoc = getObjectMap(search);
            doc.add(mapDoc);
        }

        String result = doc.push("main");
        LOGGER.debug("SEARCH_DOC_PUSH,RESULT:" + result);

        return new ResultInfo();
    }

    Map<String, Object> getObjectMap(Object search) throws IllegalAccessException {
        Map<String, Object> mapDoc = new HashedMap();
        Field[] fields = search.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals("serialVersionUID")) continue;
            field.setAccessible(true);
            String fieldName = field.getName();
            String name = processFieldName(fieldName);

            if (field.getType() == Date.class) {
                mapDoc.put(name, ((Date) field.get(search)).getTime());
            } else if ( NUMBER_ARRAY_FIELDS.contains( field.getName())) {
                mapDoc.put(name, JSONArray.toJSON(JSONArray.parseArray(field.get(search).toString(), Long.class)));
            } else if (STRING_ARRAY_FIELDS.contains(field.getName())) {
                mapDoc.put(name, JSONArray.toJSON(JSONArray.parseArray(field.get(search).toString(), String.class)));
            } else {
                mapDoc.put(name, field.get(search));
            }
        }
        return mapDoc;
    }


    public ResultInfo delItemDocTotal() throws IOException, IllegalAccessException, InterruptedException {
        List<Search> searchList = searchDao.getAll();
        CloudsearchDoc doc = cloudSearchService.getCloudsearchDoc();

        int total = searchList.size();

        if (total == 0) {
            return new ResultInfo();
        }

        int times = total / separate;
        if ((total % separate) > 0) {
            ++times;
        }

        LOGGER.debug("SEARCH_DOC_DEL_PUSH_NEED_TIMES:" + times);
        for (int i = 0; i < times; i++) {
            int start = i * separate;
            int end = start + separate;
            if (end > total) {
                end = total;
            }
            List<Search> temp = searchList.subList(start, end);

            for (Search search : temp) {
                doc.remove(getObjectMap(search));
            }
            String result = doc.push("main");
            LOGGER.debug("SEARCH_DOC_DEL_ALL_PUSH,RESULT:" + result);
            JSONObject jsonObject = JSON.parseObject(result);
            if (!StringUtils.equals("OK", String.valueOf(jsonObject.get("status")))) {
                LOGGER.error("SEARCH_DOC_DEL_ALL_PUSH_FAIL.SLEEP_300_MS_AND_RETRY");
                Thread.sleep(300);
                for (Search search : temp) {
                    doc.remove(getObjectMap(search));
                }
                String secRes = doc.push("main");
                LOGGER.debug("SEARCH_DOC_DEL_ALL_PUSH_RETRY,RESULT:" + secRes);

            }
            delServerDoc(doc);
        }
        return new ResultInfo();
    }

    private void delServerDoc(CloudsearchDoc doc) throws IOException, InterruptedException {
        CloudsearchSearch search = cloudSearchService.getCloudsearchSearch();
        search.addFilter("create_time>0");
        search.setHits(100);
        String s = search.search();
        ItemSearchResult searchResult = JSON.parseObject(s, ItemSearchResult.class);
        if (searchResult != null && searchResult.getResult() != null && searchResult.getResult().getItems() != null) {
            LOGGER.info("SEARCH_DOC_DEL_SERVER_DOC_COUNT:" + searchResult.getResult().getItems().size());
            for (ItemResult item : searchResult.getResult().getItems()) {
                Map<String, Object> param = new HashMap<>(4);
                param.put("id", item.getId());
                doc.remove(param);
            }
            String delResult = doc.push("main");
            if (!StringUtils.equals("OK", String.valueOf(JSON.parseObject(delResult).get("status")))) {
                LOGGER.error("SEARCH_DOC_DEL_ALL_PUSH_FAIL.SLEEP_300_MS_AND_RETRY");
                Thread.sleep(300);
                for (ItemResult item : searchResult.getResult().getItems()) {
                    Map<String, Object> param = new HashMap<>(4);
                    param.put("id", item.getId());
                    doc.remove(param);
                }
                delResult = doc.push("main");
                LOGGER.debug("SEARCH_DOC_DEL_ALL_PUSH_RETRY,RESULT:" + delResult);
            }
        }
    }

    public ResultInfo updateItemDocTotal() throws IOException, IllegalAccessException, InterruptedException {
        CloudsearchDoc doc = cloudSearchService.getCloudsearchDoc();

        delItemDoc(doc);

        updateItemDoc(doc, null);

        return new ResultInfo();

    }

    public ResultInfo updateItemDoc() throws IOException, IllegalAccessException, InterruptedException {
        CloudsearchDoc doc = cloudSearchService.getCloudsearchDoc();

        delItemDoc(doc);

        Date timestamp = timestampDao.getTimestamp(SearchTimestampCode.ITEM.name());

        updateItemDoc(doc, timestamp);

        return new ResultInfo();
    }

    private void updateItemDoc(CloudsearchDoc doc, Date timestamp) throws IllegalAccessException, IOException, InterruptedException {
        List<Search> searchList = searchDao.getUpdatedByUpdateTime(timestamp);
        int total = searchList.size();

        if (total == 0) {
            return;
        }

        int times = total / separate;
        if ((total % separate) > 0) {
            ++times;
        }

        LOGGER.info("SEARCH_DOC_UPDATE_PUSH_NEED_TIMES:" + times);
        LOGGER.info("SEARCH_DOC_UPDATE_PUSH_ROWS:" + total);
        for (int i = 0; i < times; i++) {
            int start = i * separate;
            int end = start + separate;
            if (end > total) {
                end = total;
            }
            List<Search> temp = searchList.subList(start, end);
            for (Search search : temp) {
                doc.update(getObjectMap(search));
            }
            LOGGER.info("SEARCH_DOC_UPDATE_PUSH_DATA:" + JSON.toJSONString(temp));
            LOGGER.info("SEARCH_DOC_UPDATE_PUSH_DATA_COUNT:" + temp.size());
            String result = doc.push("main");
            Thread.sleep(200);
            System.out.println("SEARCH_DOC_UPDATE_PUSH:" + result);
            LOGGER.info("SEARCH_DOC_UPDATE_PUSH,TIMES={},RESULT={}", i, result);
            JSONObject first = JSON.parseObject(result);
            if (!StringUtils.equals("OK", String.valueOf(first.get("status")))) {
                LOGGER.error("SEARCH_DOC_UPDATE_PUSH_FAIL.SLEEP_300_MS_AND_RETRY");
                Thread.sleep(300);
                for (Search search : temp) {
                    doc.update(getObjectMap(search));
                }
                String secRes = doc.push("main");
                LOGGER.info("SEARCH_DOC_UPDATE_PUSH_RETRY,TIMES={},RESULT={}", i, secRes);
                JSONObject second = JSON.parseObject(secRes);
                if (!StringUtils.equals("OK", String.valueOf(second.get("status")))) {
                    LOGGER.error("SEARCH_DOC_UPDATE_PUSH_RETRY_FAILED,TIMES={},RESULT={}", i, secRes);
                }

            }
        }
    }

    private void delItemDoc(CloudsearchDoc doc) throws IllegalAccessException, IOException, InterruptedException {
        List<Long> ids = new ArrayList<>();
        List<Search> delList = searchDao.getAllDel();
        if (delList.isEmpty()) {
            return;
        }
        for (Search search : delList) {
            ids.add(search.getId());
            doc.remove(getObjectMap(search));
        }
        String result = doc.push("main");
        LOGGER.info("SEARCH_DOC_DEL_PUSH,RESULT:" + result);
        System.out.println("SEARCH_DOC_DEL_PUSH:" + result);
        JSONObject jsonObject = JSON.parseObject(result);
        boolean pushSuccess = false;
        if (StringUtils.equals(String.valueOf(jsonObject.get("status")), "OK")) {
            LOGGER.info("SEARCH_DOC_DEL_PUSH,SUCCESS,RESULT:" + result);
            pushSuccess = true;
        } else {
            LOGGER.error("SEARCH_DOC_DEL_PUSH_FAIL.SLEEP_300_MS_AND_RETRY.RESULT:" + result);
            Thread.sleep(300);
            for (Search search : delList) {
                doc.remove(getObjectMap(search));
            }
            String resultSec = doc.push("main");
            LOGGER.info("SEARCH_DOC_DEL_PUSH_RETRY,RESULT:" + resultSec);
            JSONObject jsonSec = JSON.parseObject(resultSec);
            if (StringUtils.equals(String.valueOf(jsonSec.get("status")), "OK")) {
                pushSuccess = true;
            } else {
                pushSuccess = false;
            }
        }
        if (pushSuccess) {
            searchDao.deleteByIds(ids);
        }

    }


    private static String processFieldName(String fieldName) {
        List<Integer> position = new ArrayList<>();
        for (int i = 0; i < fieldName.length(); i++) {
            char a = fieldName.charAt(i);
            if (a >= 'A' && a <= 'Z') {
                position.add(i);
            }
        }
        if (position.isEmpty()) return fieldName;
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < position.size(); j++) {
            if (j == 0) {
                int start = 0;
                int end = position.get(0);
                sb.append(fieldName.substring(start, end)).append("_");
            } else {
                int start = position.get(j - 1);
                int end = position.get(j);
                sb.append(fieldName.substring(start, end)).append("_");
            }
        }
        sb.append(fieldName.substring(position.get(position.size() - 1)));
        return sb.toString().toLowerCase();
    }

    @Override
    public ResultInfo updateShopDoc() throws IOException, IllegalAccessException, InterruptedException {
        Date timestamp = timestampDao.getTimestamp(SearchTimestampCode.SHOP.name());
        return updateShopDoc(timestamp, Arrays.asList(SearchRecordStatus.NEW.ordinal(), SearchRecordStatus.TO_UPDATE.ordinal()));
    }

    @Override
    public ResultInfo updateShopDocTotal() throws IOException, IllegalAccessException, InterruptedException {
        return updateShopDoc(null, Arrays.asList(SearchRecordStatus.NEW.ordinal(), SearchRecordStatus.NORMAL.ordinal(), SearchRecordStatus.TO_UPDATE.ordinal()));
    }

    @Override
    public ResultInfo deleteShopDocTotal() throws IOException, IllegalAccessException, InterruptedException {
        List<SearchShop> searchShopListForDel = searchShopDao.queryByUpdateTimeAndRecordStatus(null, Arrays.asList(SearchRecordStatus.NEW.ordinal(), SearchRecordStatus.NORMAL.ordinal(), SearchRecordStatus.TO_UPDATE.ordinal(), SearchRecordStatus.TO_DELETE.ordinal()));
        CloudsearchDoc doc = cloudSearchService.getShopCloudsearchDoc(cloudSearchService.getShopAppName());
        boolean b = pushDeletedDoc(searchShopListForDel, doc, cloudSearchService.SHOP_TABLE);
        return b ? new ResultInfo() : new ResultInfo(new FailInfo("删除文档失败", -1));
    }

    private ResultInfo updateShopDoc(Date timestamp, List<Integer> recodeStatus) throws IllegalAccessException, IOException, InterruptedException {
        List<SearchShop> searchShopListForUpdate = searchShopDao.queryByUpdateTimeAndRecordStatus(timestamp, recodeStatus);
        List<SearchShop> searchShopListForDel = searchShopDao.queryByUpdateTimeAndRecordStatus(timestamp, Arrays.asList(SearchRecordStatus.TO_DELETE.ordinal()));
        CloudsearchDoc doc = cloudSearchService.getShopCloudsearchDoc(cloudSearchService.getShopAppName());
        boolean updateSuccess = true;
        boolean deleteSuccess = true;
        if (!CollectionUtils.isEmpty(searchShopListForUpdate))
            updateSuccess = pushUpdateDoc(searchShopListForUpdate, doc, cloudSearchService.SHOP_TABLE);
        if (!CollectionUtils.isEmpty(searchShopListForDel)) {
            deleteSuccess = pushDeletedDoc(searchShopListForDel, doc, cloudSearchService.SHOP_TABLE);
        }

        if (updateSuccess && !CollectionUtils.isEmpty(searchShopListForUpdate)) {
            List<Long> ids = searchShopListForUpdate.stream().map(SearchShop::getId).collect(Collectors.toList());
            LOGGER.info("SHOP_DOC_UPDATED.COUNT:"+ ids.size());
            LOGGER.info("SHOP_DOC_UPDATED.IDS:"+ ids);
            searchShopDao.updateRecordStatusByIds(ids, SearchRecordStatus.NORMAL.ordinal());
        }
        if (deleteSuccess && !CollectionUtils.isEmpty(searchShopListForDel)) {
            List<Long> ids = searchShopListForDel.stream().map(SearchShop::getId).collect(Collectors.toList());
            LOGGER.info("SHOP_DOC_ DELETED.COUNT:"+ ids.size());
            LOGGER.info("SHOP_DOC_DELETED.IDS:"+ ids);
            searchShopDao.deleteByIds(ids);
        }
        return updateSuccess && deleteSuccess ? new ResultInfo() : new ResultInfo(new FailInfo( "更新数据异常",-1));
    }

    private boolean pushDeletedDoc(List<?> dataForDelete, CloudsearchDoc doc, String tableName) throws IllegalAccessException, IOException, InterruptedException {
        for (Object search : dataForDelete) {
            doc.remove(getObjectMap(search));
        }
        String result = doc.push(tableName);
        LOGGER.info("SEARCH_DOC_DEL_PUSH,RESULT:" + result);

        JSONObject jsonObject = JSON.parseObject(result);
        boolean pushSuccess = true;
        if (StringUtils.equals(String.valueOf(jsonObject.get("status")), "OK")) {
            LOGGER.info("SEARCH_DOC_DEL_PUSH,SUCCESS,RESULT:" + result);
        } else {
            LOGGER.error("SEARCH_DOC_DEL_PUSH_FAIL.SLEEP_300_MS_AND_RETRY.RESULT:" + result);
            Thread.sleep(300);
            for (Object search : dataForDelete) {
                doc.remove(getObjectMap(search));
            }
            String resultSec = doc.push(cloudSearchService.SHOP_TABLE);
            LOGGER.info("SEARCH_DOC_DEL_PUSH_RETRY,RESULT:" + resultSec);
            JSONObject jsonSec = JSON.parseObject(resultSec);
            if (!StringUtils.equals(String.valueOf(jsonSec.get("status")), "OK")) {
                pushSuccess = false;
            }
        }
        return pushSuccess;
    }

    private boolean pushUpdateDoc(List<?> dataForUpdate, CloudsearchDoc doc, String tableName) throws IllegalAccessException, IOException, InterruptedException {
        boolean b = true;
        int total = dataForUpdate.size();
        int times = total / separate;
        if ((total % separate) > 0) {
            ++times;
        }

        for (int i = 0; i < times; i++) {
            int start = i * separate;
            int end = start + separate;
            if (end > total) {
                end = total;
            }
            List<?> temp = dataForUpdate.subList(start, end);
            for (Object search : temp) {
                doc.update(getObjectMap(search));
            }
            LOGGER.info("SEARCH_DOC_UPDATE_PUSH_DATA:" + JSON.toJSONString(temp));
            LOGGER.info("SEARCH_DOC_UPDATE_PUSH_DATA_COUNT:" + temp.size());
            String result = doc.push(tableName);
            Thread.sleep(200);
            System.out.println("SEARCH_DOC_UPDATE_PUSH:" + result);
            LOGGER.info("SEARCH_DOC_UPDATE_PUSH,TIMES={},RESULT={}", i, result);
            JSONObject first = JSON.parseObject(result);
            if (!StringUtils.equals("OK", String.valueOf(first.get("status")))) {
                LOGGER.error("SEARCH_DOC_UPDATE_PUSH_FAIL.SLEEP_300_MS_AND_RETRY");
                Thread.sleep(300);
                for (Object search : temp) {
                    doc.update(getObjectMap(search));
                }
                String secRes = doc.push(tableName);
                LOGGER.info("SEARCH_DOC_UPDATE_PUSH_RETRY,TIMES={},RESULT={}", i, secRes);
                JSONObject second = JSON.parseObject(secRes);
                if (!StringUtils.equals("OK", String.valueOf(second.get("status")))) {
                    LOGGER.error("SEARCH_DOC_UPDATE_PUSH_RETRY_FAILED,TIMES={},RESULT={}", i, secRes);
                    b = false;
                }

            }
        }
        return b;
    }
}
