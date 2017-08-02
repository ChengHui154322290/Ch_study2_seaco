package com.tp.service.prd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.dao.prd.ItemPicturesDao;
import com.tp.model.prd.ItemDesc;
import com.tp.model.prd.ItemDescMobile;
import com.tp.model.prd.ItemPictures;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemPicturesService;
import com.tp.util.BeanUtil;

@Service
public class ItemPicturesService extends BaseService<ItemPictures> implements IItemPicturesService {

	@Autowired
	private ItemPicturesDao itemPicturesDao;
	
	@Autowired
	private IItemDescService itemDescService;
	
	@Autowired
	private IItemDescMobileService itemDescMobileService;
	
	@Override
	public BaseDao<ItemPictures> getDao() {
		return itemPicturesDao;
	}

	@Override
	public List<ItemPictures> getUnUploadPictures() {
		return itemPicturesDao.queryUnUploadToQiNiu();
	}

	@Transactional
	@Override
	public void replaceItemPics(ItemPictures itemMianPic , List<ItemPictures> itemPicQnPaths, List<ItemPictures> descPicQnPaths) {
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("item_id", itemMianPic.getItemId() );
		params.put("detail_id", itemMianPic.getDetailId() );
		List<ItemPictures> itemPicturesList = itemPicturesDao.queryByParam(params);
		//根据itemID和detailId查询，存在则删除
		if(null != itemPicturesList && itemPicturesList.size()>0 ){ 
			for(ItemPictures itemPictures : itemPicturesList){
				itemPicturesDao.deleteByObject(itemPictures);
			}
		}
		//新增主图
		itemPicturesDao.insert(itemMianPic);
		//新增商品图片
		itemPicturesDao.batchInsert(itemPicQnPaths);
		//更新商品详情描述
		String itemDesc = "";
		for(ItemPictures itemPictures:descPicQnPaths){
			itemDesc += "<div>"
						+"<img src=\"http://item.qn.seagoor.com/"+itemPictures.getPicture()+" width=\"100%\" alt=\"\" />"
						+"</div>";
		}
		itemDesc += "<div>"
				+"<img src=\"http://item.qn.seagoor.com/"+itemMianPic.getPicture()+" width=\"100%\" alt=\"\" />"
				+"</div>";
		for(ItemPictures itemPictures:itemPicQnPaths){
			itemDesc += "<div>"
					+"<img src=\"http://item.qn.seagoor.com/"+itemPictures.getPicture()+" width=\"100%\" alt=\"\" />"
					+"</div>";
		}
		Map<String, Object> paras = new HashMap<String,Object>();
		paras.put("item_id", itemMianPic.getItemId());
		paras.put("detail_id", itemMianPic.getDetailId());
		ItemDesc desc = itemDescService.queryUniqueByParams(paras);
		desc.setDescription(itemDesc);
		itemDescService.updateById(desc);
		ItemDescMobile descMobile = new ItemDescMobile();
		BeanUtils.copyProperties(desc , descMobile);
		itemDescMobileService.updateById(descMobile);
		
	}
	
}
