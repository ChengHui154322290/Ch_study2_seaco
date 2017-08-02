package com.tp.service.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.prd.ItemSkuArtDao;
import com.tp.model.prd.ItemSkuArt;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemSkuArtService;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemSkuArtService extends BaseService<ItemSkuArt> implements IItemSkuArtService {

	@Autowired
	private ItemSkuArtDao itemSkuArtDao;
	
	@Override
	public BaseDao<ItemSkuArt> getDao() {
		return itemSkuArtDao;
	}

	@Override
	public List<ItemSkuArt> queryBySkusAndChannel(List<String> skus, Long channelId) {
		if(CollectionUtils.isEmpty(skus)) return Collections.emptyList();
		Map<String,Object> param = new HashMap<>();
		param.put("list",skus);
		param.put("channelId",channelId);

		return itemSkuArtDao.queryBySkusAndChannel(param);

	}

	@Override
	public List<ItemSkuArt> queryByArticleNumbersAndChannel(List<String> articleNumbers, Long channelId) {
		if(CollectionUtils.isEmpty(articleNumbers)) return Collections.emptyList();
		Map<String,Object> param = new HashMap<>();
		param.put("list",articleNumbers);
		param.put("channelId",channelId);

		return itemSkuArtDao.queryByArticleNumbersAndChannel(param);

	}

}
