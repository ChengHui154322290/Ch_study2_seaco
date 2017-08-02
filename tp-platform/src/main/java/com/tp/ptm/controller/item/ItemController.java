package com.tp.ptm.controller.item;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.ptm.ao.item.ItemServiceAO;
import com.tp.ptm.dtos.ItemHotDto;
import com.tp.ptm.dtos.ItemHotQuery;

@RequestMapping("/item")
@Controller
public class ItemController {
	
	private Logger logger = LoggerFactory.getLogger(ItemController.class);
	
	@Autowired
	private ItemServiceAO itemServiceAO;
	
	static ObjectMapper mapper = new ObjectMapper();
	
	static final int MAX_HOT_QUERY=50;
	
	
	
	@RequestMapping(value="hot/{sku}/{topicId}")
	@ResponseBody
	public ItemHotDto queryItemInfoForHot(@PathVariable String sku,@PathVariable Long topicId){
		if(StringUtils.isBlank(sku)||null==topicId){
			return null;
		}
		ItemHotDto dto = null;
		try {
			dto = itemServiceAO.selectItemInfoBySku(sku, topicId);
		} catch (Exception e) {
			logger.error("获得热销商品出错【s1】：{}",e);
		}
		return dto;
	}
	
	@RequestMapping(value="hot/list",method=RequestMethod.POST)
	@ResponseBody
	public List<ItemHotDto> batchQueryItemInfoForHot(@RequestBody List<ItemHotQuery> body){
		
		List<ItemHotQuery> hotQueries = null;
		try {
			hotQueries = body;
		} catch (Exception e1) {
			logger.error(e1.getMessage(), e1);
		}
		
		if(null==hotQueries){
			return null;
		}
		List<ItemHotQuery> cleanedQueries = new ArrayList<ItemHotQuery>();
		for (ItemHotQuery itemHotQuery : hotQueries) {
			if(StringUtils.isNotBlank(itemHotQuery.getSku())&&null!=itemHotQuery.getTopicId()){
				cleanedQueries.add(itemHotQuery);
			}
			if(cleanedQueries.size()==MAX_HOT_QUERY){
				break;
			}
		}
		if(CollectionUtils.isEmpty(cleanedQueries)){
			return null;
		}
		List<ItemHotDto> dtos = new ArrayList<ItemHotDto>();
		try {
			for (ItemHotQuery query : cleanedQueries) {
				ItemHotDto dto = itemServiceAO.selectItemInfoBySku(query.getSku(), query.getTopicId());
				if(null!=dto){
					dtos.add(dto);
				}
			}
			
		} catch (Exception e) {
			logger.error("获得热销商品出错【list】：{}",e);
		}
		return dtos;
	}
	
	public static <T> T jacksonToCollection(String src,Class<?> collectionClass, Class<?>... valueType)  
            throws Exception {  
        JavaType javaType= mapper.getTypeFactory().constructParametricType(collectionClass, valueType);   
        return (T)mapper.readValue(src, javaType);  
    }  
	@RequestMapping(value="downloadTemplate")
	public void downloadBaseData(HttpServletRequest request, HttpServletResponse response) {
		itemServiceAO.downloadBaseData(request, response, "isWave");
	}
	
	/**
	 * 获取优质商品
	 * @param body
	 * @return
	 *//*
	@RequestMapping(value="highQuality",method=RequestMethod.POST, headers={"Accept=application/json"},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String itemHighQuality(@RequestBody String body){
//		if(startPage <= 0 ){ startPage = 1;}
		ItemHighQualityQuery highQualityQuery=null;
		if(StringUtils.isBlank(body)){
			highQualityQuery = new ItemHighQualityQuery();
		}else{
			highQualityQuery = JSONArray.parseObject(body, ItemHighQualityQuery.class);
		}
		try{
			List<ItemHighQualityDto> list = itemServiceAO.selectHighQuality(highQualityQuery);
			return JSONObject.toJSONString(list);
		}catch(Exception e){
			logger.error("获取优质商品出错：{}",e);
		}
		return null;
	}*/
	
	
}
