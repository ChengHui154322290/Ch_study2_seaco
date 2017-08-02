package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemPictures;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemPicturesService;
/**
 * 商品图片信息表代理层
 * @author szy
 *
 */
@Service
public class ItemPicturesProxy extends BaseProxy<ItemPictures>{

	@Autowired
	private IItemPicturesService itemPicturesService;

	@Override
	public IBaseService<ItemPictures> getService() {
		return itemPicturesService;
	}
	
	
	
	
}
