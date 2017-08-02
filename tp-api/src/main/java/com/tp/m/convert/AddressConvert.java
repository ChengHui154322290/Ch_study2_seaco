package com.tp.m.convert;

import java.util.ArrayList;
import java.util.List;

import com.tp.common.util.ImageUtil;
import com.tp.common.vo.Constant;
import com.tp.m.constant.UserConstant;
import com.tp.m.query.user.QueryAddress;
import com.tp.m.util.StringUtil;
import com.tp.m.util.VerifyUtil;
import com.tp.m.vo.user.AddressDetailVO;
import com.tp.model.mem.ConsigneeAddress;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 地址封装类
 * @author zhuss
 * @2016年1月11日 下午8:30:10
 */
public class AddressConvert {

	/**
	 * 封装收货地址列表
	 */
	public static List<AddressDetailVO> convertAddresslist(List<ConsigneeAddress> addresslist){
		List<AddressDetailVO> alist = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(addresslist)){
			for(ConsigneeAddress dto : addresslist){
				AddressDetailVO vo = convertAddress(dto,true);
				if(StringUtil.isNotBlank(vo.getIdentityCard())){
					vo.setIdentityCard(vo.getIdentityCard().replaceAll("^(\\d{4})(.+)(\\d{4}.)$", "$1*********$3"));
				}
				alist.add(vo);
			}
		}
		return alist;
	}
	
	/**
	 * 封装单个收货地址
	 * @param dto
	 * @param isFull:是否需要全参数(用于修改)
	 * @return
	 */
	public static AddressDetailVO convertAddress(ConsigneeAddress dto,boolean isFull){
		AddressDetailVO ra = new AddressDetailVO();
		if(null != dto){
			ra.setName(dto.getName());
			ra.setTel(dto.getMobile());
			ra.setFullinfo(convertFullAddress(dto));
			ra.setAid(StringUtil.getStrByObj(dto.getId()));
			ra.setIdentityCard(dto.getIdentityCard());
			if(isFull){
				ra.setProvinceid(dto.getProvinceId());
				ra.setProvince(dto.getProvince());
				ra.setCityid(dto.getCityId());
				ra.setCity(dto.getCity());
				ra.setDistrictid(dto.getCountyId());
				ra.setDistrict(dto.getCounty());
				ra.setStreetid(dto.getStreetId());
				ra.setStreet(dto.getStreet());
				ra.setInfo(dto.getAddress());
				ra.setIsdefault(dto.getIsDefault()?UserConstant.DEFAULT_ADDRESS:UserConstant.DEFAULT_NO_ADDRESS);
			}
			ra.setFrontimg(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.cmsimg,dto.getFrontImg()));
			ra.setBackimg(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.cmsimg,dto.getBackImg()));
//			ra.setIscertificated(StringUtils.isNotBlank(dto.getIdentityCard())&& StringUtils.isNotBlank(dto.getFrontImg())&& StringUtils.isNotBlank(dto.getBackImg())? "1":"0");
			ra.setIscertificated(StringUtils.isNotBlank(dto.getIdentityCard())? "1":"0");
		}
		return ra;
	}
	
	/**
	 * 封装修改入参对象
	 * @param address
	 * @return
	 */
	public static ConsigneeAddress convertModifyAddress(QueryAddress address){
		ConsigneeAddress con = new ConsigneeAddress();
		if(StringUtil.isNotBlank(address.getAid()))
			con.setId(StringUtil.getLongByStr(address.getAid()));
		con.setUserId(address.getUserid());
		if(StringUtil.isNotBlank(address.getName()))con.setName(VerifyUtil.escapeJSAndEmoji(address.getName().trim()));
		con.setMobile(address.getTel());
		con.setIsDefault(StringUtil.equals(address.getIsdefault(), UserConstant.DEFAULT_ADDRESS)?true:false);
		if(StringUtil.isNotBlank(address.getInfo()))con.setAddress(VerifyUtil.escapeJSAndEmoji(address.getInfo().trim()));
		con.setProvinceId(StringUtil.getLongByStr(address.getProvid()));
		con.setProvince(address.getProvname());
		con.setCityId(StringUtil.getLongByStr(address.getCityid()));
		con.setCity(address.getCityname());
		con.setCountyId(StringUtil.getLongByStr(address.getDistrictid()));
		con.setCounty(address.getDistrictname());
		con.setStreetId(StringUtil.getLongByStr(address.getStreetid()));
		con.setStreet(address.getStreetname());
		con.setIdentityCard(address.getIdentityCard()==null? null :address.getIdentityCard().toUpperCase());
		con.setFrontImg(subImgHost(StringUtil.trim(address.getFrontimg())));
		con.setBackImg(subImgHost(StringUtil.trim(address.getBackimg())));
		return con;
	}
	private static String subImgHost(String img){
		return img == null ? null :img.replace(Constant.IMAGE_URL_TYPE.cmsimg.url,"").replace(Constant.IMAGE_URL_TYPE.cmsimg.sslUrl, "");
	}
	/**
	 * 封装收货地址全路径
	 * @param dto
	 * @return
	 */
	public static String convertFullAddress(ConsigneeAddress dto){
		StringBuffer sb = new StringBuffer();
		if(null != dto){
			sb.append(dto.getProvince());
			sb.append(dto.getCity());
			sb.append(dto.getCounty());
			sb.append(dto.getStreet());
			sb.append(dto.getAddress());
		}
		return sb.toString();
	}
}
