package com.tp.dao.prd;

import com.tp.common.dao.BaseDao;
import com.tp.model.prd.ItemSkuArt;

import java.util.List;
import java.util.Map;

public interface ItemSkuArtDao extends BaseDao<ItemSkuArt> {

    List<ItemSkuArt> queryBySkusAndChannel(Map<String,Object> param);

    List<ItemSkuArt> queryByArticleNumbersAndChannel(Map<String,Object> param);

}
