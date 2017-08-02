package com.tp.ptm.ao.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.ptm.dtos.ItemDto;
import com.tp.ptm.dtos.ItemHotDto;
import com.tp.redis.util.JedisCacheUtil;

@Service
public class ItemCacheServiceAO {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	private static final String SEPARATE="-";
	
	private static final String CACHE_RECOMMAND="CACHE_RECOMMAND";
	private static final String CACHE_RECOMMAND_ADVFIELD="CACHE_RECOMMAND_ADVFIELD";
	
	private static final String CACHE_HOT="CACHE_HOT";
	
	public ItemHotDto selectCacheHot(String sku,Long topicId){
		String key = buildHotCacheKey(sku, topicId);
		ItemHotDto hotDto = null;
		try {
			hotDto = (ItemHotDto) jedisCacheUtil.getCache(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return hotDto;
	}
	
	public void insertCacheHot(ItemHotDto hotDto){
		String sku = hotDto.getSku();
		Long topicId = hotDto.getTopicId();
		String key = buildHotCacheKey(sku, topicId);
		try {
			jedisCacheUtil.setCache(key, hotDto,60*5);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public ItemDto selectCacheRecommand(String sku){
		String key = buildRecommandCacheKey(sku);
		ItemDto dto=null;
		try {
			dto = (ItemDto) jedisCacheUtil.getCache(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return dto;
	}
	
	public void insertCacheRecommand(ItemDto dto){
		String sku = dto.getSku();
		String key = buildRecommandCacheKey(sku);
		try {
			jedisCacheUtil.setCache(key, dto,60*10);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public String selectCacheRecommandAdvField(String sku){
		String key = buildRecommandAdvFieldCacheKey(sku);
		String advField = null;
		try {
			advField = jedisCacheUtil.getCacheString(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return advField;
	}
	
	public void insertCacheRecommandAdvField(String sku,String advFields){
		String key = buildRecommandAdvFieldCacheKey(sku);
		try {
			jedisCacheUtil.setCacheString(key,advFields,60*60*24*7);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private String buildRecommandAdvFieldCacheKey(String sku) {
		StringBuilder sb = new StringBuilder();
		sb.append(CACHE_RECOMMAND_ADVFIELD);
		sb.append(SEPARATE);
		sb.append(sku);
		return sb.toString();
	}

	private String buildRecommandCacheKey(String sku){
		StringBuilder sb = new StringBuilder();
		sb.append(CACHE_RECOMMAND);
		sb.append(SEPARATE);
		sb.append(sku);
		return sb.toString();
	}
	
	private String buildHotCacheKey(String sku,Long topicId){
		StringBuilder sb = new StringBuilder();
		sb.append(CACHE_HOT);
		sb.append(SEPARATE);
		sb.append(sku);
		sb.append(SEPARATE);
		sb.append(topicId);
		return sb.toString();
	}
	
}
