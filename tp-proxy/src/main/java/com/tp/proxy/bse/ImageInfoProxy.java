package com.tp.proxy.bse;

import com.tp.common.util.ImageUtil;
import com.tp.common.vo.Constant;
import com.tp.model.bse.ImageInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IImageInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 代理层
 * @author szy
 *
 */
@Service
public class ImageInfoProxy extends BaseProxy<ImageInfo>{

	@Autowired
	private IImageInfoService imageInfoService;

	@Override
	public IBaseService<ImageInfo> getService() {
		return imageInfoService;
	}

	public List<ImageInfo> queryListWithId(Long id){
		List<ImageInfo> list = imageInfoService.queryListWithId(id);
		for(ImageInfo imageInfo: list){
			imageInfo.setImage(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.basedata,imageInfo.getImage()));
		}

		return list;

	}

}
