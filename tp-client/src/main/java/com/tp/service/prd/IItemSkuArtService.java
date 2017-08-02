package com.tp.service.prd;

import com.tp.model.prd.ItemSkuArt;
import com.tp.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
  * @author szy
  * 商品通关信息表接口
  */
public interface IItemSkuArtService extends IBaseService<ItemSkuArt>{

    List<ItemSkuArt> queryBySkusAndChannel(List<String> skus, Long channelId);

    List<ItemSkuArt> queryByArticleNumbersAndChannel(List<String> artNumbers, Long channelId);

}
