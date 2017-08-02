package com.tp.dao.prd;

import com.tp.common.dao.BaseDao;
import com.tp.model.prd.ItemPictures;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ItemPicturesDao extends BaseDao<ItemPictures> {

    List<ItemPictures> queryMainPicByItemIds(List<Long> itemIds);
    List<ItemPictures> queryUnUploadToQiNiu();

    Integer batchInsert(@Param("itemPicQnPaths")List<ItemPictures> itemPicQnPaths);
}
