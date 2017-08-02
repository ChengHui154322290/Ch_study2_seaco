package com.tp.m.convert;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import com.tp.common.util.ImageUtil;
import com.tp.m.enums.ImgEnum;
import com.tp.m.helper.ImgHelper;
import com.tp.m.vo.skin.SkinInfoVO;
import com.tp.model.app.SkinInfo;
import com.tp.util.ErWeiMaUtil;


public class SkinInfoConvert {
	public static  SkinInfoVO convertSkin(SkinInfo skin ,Long oldskinid){
		
		SkinInfoVO vo = new SkinInfoVO();
		if(!oldskinid.equals(skin.getId())){
		vo.setSkinid(skin.getId()+"");
		//String aa=ImageUtil.getCMSImgFullUrl(skin.getIconA());
	//	String aa=ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconA()), ImgEnum.Width.WIDTH_72);
		vo.setIcona(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconA()), ImgEnum.Width.WIDTH_72)));
			System.out.println(ImageUtil.getCMSImgFullUrl(skin.getIconA()));
			System.out.println(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconA()), ImgEnum.Width.WIDTH_72));
			System.out.println(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconA()), ImgEnum.Width.WIDTH_72)));
		vo.setIconb(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconASelected()), ImgEnum.Width.WIDTH_72)));
		vo.setIconc(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconB()), ImgEnum.Width.WIDTH_72)));
		vo.setIcond(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconBSelected()), ImgEnum.Width.WIDTH_72)));
		vo.setIcone(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconC()), ImgEnum.Width.WIDTH_72)));
		vo.setIconf(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconCSelected()), ImgEnum.Width.WIDTH_72)));
		vo.setIcong(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconD()), ImgEnum.Width.WIDTH_72)));
		vo.setIconh(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconDSelected()), ImgEnum.Width.WIDTH_72)));
		vo.setIconi(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getTapBar()), ImgEnum.Width.WIDTH_1125)));
		
		vo.setIconj(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconA()), ImgEnum.Width.WIDTH_48)));
		vo.setIconk(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconASelected()), ImgEnum.Width.WIDTH_48)));
		vo.setIconl(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconB()), ImgEnum.Width.WIDTH_48)));
		vo.setIconm(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconBSelected()), ImgEnum.Width.WIDTH_48)));
		vo.setIconn(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconC()), ImgEnum.Width.WIDTH_48)));
		vo.setIcono(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconCSelected()), ImgEnum.Width.WIDTH_48)));
		vo.setIconp(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconD()), ImgEnum.Width.WIDTH_48)));
		vo.setIconq(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getIconDSelected()), ImgEnum.Width.WIDTH_48)));
		vo.setIconr(ImgHelper.encodeImageDate(ImgHelper.getImgUrl(ImageUtil.getCMSImgFullUrl(skin.getTapBar()), ImgEnum.Width.WIDTH_750)));
		vo.setUnselectedcolor(skin.getUnSelectedColor());
		vo.setSelectedcolor(skin.getSelectedColor());
		vo.setStatus("1");//有新皮肤
		}
		
		if(skin.getId().equals(oldskinid)){
		vo.setSkinid(skin.getId()+"");
		vo.setStatus("1");//有新皮肤
		}
		return vo;
		
	}

}
