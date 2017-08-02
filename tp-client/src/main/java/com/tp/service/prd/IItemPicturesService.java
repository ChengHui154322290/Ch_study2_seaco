package com.tp.service.prd;

import java.util.ArrayList;
import java.util.List;

import com.tp.model.prd.ItemPictures;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 商品图片信息表接口
  */
public interface IItemPicturesService extends IBaseService<ItemPictures>{
	/**
	 * 
	 * updatePictureToQiNiu:(获取未上传的图片). <br/>  
	 *  
	 * @author zhouguofeng    
	 * @sinceJDK 1.8
	 */
	List<ItemPictures>   getUnUploadPictures();

	void replaceItemPics(ItemPictures itemMianPic , List<ItemPictures> itemPicQnPaths, List<ItemPictures> descPicQnPaths);
}
