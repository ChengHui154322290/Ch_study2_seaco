package com.tp.proxy.prd;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.prd.ItemConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.DetailDto;
import com.tp.dto.prd.DetailSkuDto;
import com.tp.exception.ItemServiceException;
import com.tp.model.bse.ForbiddenWords;
import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemDesc;
import com.tp.model.prd.ItemDescMobile;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemSkuArt;
import com.tp.service.bse.IForbiddenWordsService;
import com.tp.service.prd.IItemManageService;
import com.tp.util.StringUtil;

/**
 * 
 * <pre>
 * 后台校验商品信息
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 *          Exp $
 */
@Service
public class ItemValidateProxy {
	
	
	@Autowired IItemManageService itemManageService;
	
	@Autowired IForbiddenWordsService forbiddenWordsService;
	
	/**
	 * 
	 * <pre>
	 * 	  校验保存的商品(spu,prdid)的信息
	 * </pre>
	 *
	 * @param info
	 * @param details
	 * @param type 1: 只验证info,2验证全部
	 * @return
	 */
	public ResultInfo<Boolean> validItem(ItemInfo info, ItemDetail[] details,int type) {
		ResultInfo<Boolean> msg = new ResultInfo<Boolean>(Boolean.TRUE);
		// step1 校验 spu
		if(StringUtil.isBlank(info.getMainTitle())) {
			return new ResultInfo<Boolean>(new FailInfo("SPU名称不能为空"));
		}else if (null==info.getSmallId()) {
			// 修改的时候可以不判断分类
			return new ResultInfo<Boolean>(new FailInfo("产品关联的分类不能为空"));
		} else if (null==info.getUnitId()) {
			return new ResultInfo<Boolean>(new FailInfo("产品的单位不能为空"));
		} else if (null==info.getBrandId()) {
			return new ResultInfo<Boolean>(new FailInfo("产品的品牌不能为空"));
		}
		
		List<ForbiddenWords>  list = getForbiddenWords() ;
		
		//违禁词校验
		String checkMsg = "" ; 
		if(StringUtils.isNotEmpty(info.getMainTitle())){
			checkMsg = checkForbiddenWordsField(info.getMainTitle(),"SPU名称",list);
		}
		if(StringUtils.isNotEmpty(info.getRemark())){
			checkMsg += checkForbiddenWordsField(info.getRemark(),"备注",list);
		}
		if(StringUtils.isNotEmpty(checkMsg)){
			return new ResultInfo<Boolean>(new FailInfo(checkMsg));
		}
		
		
		//校验大类,小类,中类,单位,品牌下spu名称唯一的 
		ItemInfo infoDO = itemManageService.checkSpuExsit(info.getSmallId(),info.getBrandId(),info.getUnitId(),info.getMainTitle(),info.getId());
		if(null!=infoDO){
			return new ResultInfo<Boolean>(new FailInfo("SPU名称已经存在了"));
		}
		if(type==1){
			return msg;
		}
		
		
		// step2 校验prdid
		if (details == null || details.length == 0) {
			return new ResultInfo<Boolean>(new FailInfo("产品的prdid纬度信息不能为空"));
		}
		
		String checkprdMsg = "";
		for (ItemDetail detail : details) {
			if(StringUtils.isNotEmpty(detail.getMainTitle())){
				checkprdMsg += checkForbiddenWordsField(detail.getMainTitle(),"商品名称",list);
			}
			if (StringUtil.isBlank(detail.getBarcode())) {
				return new ResultInfo<Boolean>(new FailInfo("产品的条码不能为空"));
			} else if (!StringUtil.isBlank(detail.getBarcode())) {
				boolean res = false;
				try {
					res = itemManageService.checkBarcodeExsit(
							detail.getBarcode(), detail.getId(),null);
					if (!res) {
						return new ResultInfo<Boolean>(new FailInfo("产品的条码在系统中已经存在"));
					}
				} catch (ItemServiceException e) {
					return new ResultInfo<Boolean>(new FailInfo("产品的条码在系统中已经存在"));
				}

			} else if (StringUtil.isBlank(detail.getMainTitle())) {
				return new ResultInfo<Boolean>(new FailInfo("产品对应的prdid纬度的名称不能为空"));
			}
		}
		
		if(StringUtils.isNotEmpty(checkprdMsg)){
			return new ResultInfo<Boolean>(new FailInfo(checkprdMsg));
		}
		
		return msg;
	}

	
	/**
	 * 
	 * <pre>
	 * 	校验prdid修改，以及sku
	 * </pre>
	 * @param info
	 * @param detail
	 * @param skus
	 * @return
	 */
	public ResultInfo<Boolean> validItemDetail(ItemInfo info,DetailDto detailDto) {
		ResultInfo<Boolean> msg = new ResultInfo<Boolean>(Boolean.TRUE);
		//  校验
		// step1 校验 prdid
		// step2校验 sku
		ItemDetail detail = detailDto.getItemDetail();
		if (StringUtil.isBlank(detail.getItemTitle())) {
			return new ResultInfo<Boolean>(new FailInfo("商品名称不能为空"));
		}else if (StringUtil.isBlank(detail.getMainTitle())) {
			return new ResultInfo<Boolean>(new FailInfo("网站显示名称不能为空"));
		}else if (StringUtil.isBlank(detail.getStorageTitle())) {
			return new ResultInfo<Boolean>(new FailInfo("仓库名称不能为空"));
		}else if (StringUtil.isBlank(detail.getSubTitle())) {
			return new ResultInfo<Boolean>(new FailInfo("商品卖点(副标题)不能为空"));
		} else if (null==detail.getBasicPrice()) {
			return new ResultInfo<Boolean>(new FailInfo("市场价不能为空"));
		} else if (null==detail.getWeight()) {
			return new ResultInfo<Boolean>(new FailInfo("毛重不能为空"));
		} else if (null==detail.getItemType()) {
			return new ResultInfo<Boolean>(new FailInfo("商品类型不能为空"));
		} else if (null==detail.getPurRate()) {
			return new ResultInfo<Boolean>(new FailInfo("进项率不能为空"));
		} else if (null==detail.getSaleRate()) {
			return new ResultInfo<Boolean>(new FailInfo("销项税率不能为空"));
		} else if (null==detail.getFreightTemplateId()) {
			return new ResultInfo<Boolean>(new FailInfo("运费模板不能为空"));
		}
		ItemDesc desc = detailDto.getItemDesc();
		if(null==desc || StringUtils.isBlank(desc.getDescription())){
			return new ResultInfo<Boolean>(new FailInfo("PC模板不能为空"));
		}
		
		ItemDescMobile descMobile = detailDto.getItemDescMobile();
		if(null==descMobile || StringUtils.isBlank(descMobile.getDescription())){
			return new ResultInfo<Boolean>(new FailInfo("手机模板不能为空"));
		}
		List<String> picList = detailDto.getPicList();
		if(CollectionUtils.isEmpty(picList)){
			return new ResultInfo<Boolean>(new FailInfo("上传图片不能为空,图片只能传3-10张"));
		}else{
			if(picList.size()>10||picList.size()<3){
				return new ResultInfo<Boolean>(new FailInfo("上传图片数量不对,应上传3-10张图片"));
			}
		}
		
		List<ForbiddenWords>  list = getForbiddenWords() ;
		//违禁词校验
		String checkMsg = "" ; 
		if(StringUtils.isNotEmpty(detail.getItemTitle())){
			checkMsg = checkForbiddenWordsField(detail.getItemTitle(),"商品名称",list);
		}
		if(StringUtils.isNotEmpty(detail.getMainTitle())){
			checkMsg += checkForbiddenWordsField(detail.getMainTitle(),"网站显示名称",list);
		}
		if(StringUtils.isNotEmpty(detail.getStorageTitle())){
			checkMsg += checkForbiddenWordsField(detail.getStorageTitle(),"仓库名称",list);
		}
		if(StringUtils.isNotEmpty(detail.getSubTitle())){
			checkMsg += checkForbiddenWordsField(detail.getSubTitle(),"商品卖点(副标题)",list);
		}
		if(StringUtils.isNotEmpty(detail.getRemark())){
			checkMsg += checkForbiddenWordsField(detail.getRemark(),"备注",list);
		}
		if(StringUtils.isNotEmpty(detail.getSpecifications())){
			checkMsg += checkForbiddenWordsField(detail.getSpecifications(),"规格",list);
		}
		if(StringUtils.isNotEmpty(detail.getCartonSpec())){
			checkMsg += checkForbiddenWordsField(detail.getCartonSpec(),"箱规",list);
		}
		
		List<ItemAttribute> attrList = detailDto.getAttrList();
		
		if(CollectionUtils.isNotEmpty(attrList)){
			for(ItemAttribute attribute : attrList){
				if(null!=attribute && StringUtils.isNotEmpty(attribute.getAttrKey())){
					checkMsg += checkForbiddenWordsField(attribute.getAttrKey(),"自定义属性名",list);
				}
				if(null!=attribute && StringUtils.isNotEmpty(attribute.getAttrValue())){
					checkMsg += checkForbiddenWordsField(attribute.getAttrValue(),"自定义属性值",list);
				}
			}
		}
		
		if(null!=desc && StringUtils.isNotEmpty(desc.getDescription())){
			checkMsg += checkForbiddenWordsField(desc.getDescription(),"详情-PC模板",list);
		}
		if(null!=descMobile && StringUtils.isNotEmpty(descMobile.getDescription())){
			checkMsg += checkForbiddenWordsField(descMobile.getDescription(),"详情-手机模板",list);
		}
		if(StringUtils.isNotEmpty(checkMsg)){
			return new ResultInfo<Boolean>(new FailInfo(checkMsg));
		}
		else if (StringUtil.isBlank(detail.getBarcode())) {
			return new ResultInfo<Boolean>(new FailInfo("产品的条码不能为空"));
		} else if (!StringUtil.isBlank(detail.getBarcode())) {
			boolean res = false;
			try {
				res = itemManageService.checkBarcodeExsit(detail.getBarcode(), detail.getId(),null);
				if (!res) {
					return new ResultInfo<Boolean>(new FailInfo("产品的条码在系统中已经存在"));
				}
			} catch (ItemServiceException e) {
				return new ResultInfo<Boolean>(new FailInfo("产品的条码在系统中已经存在"));
			}

		} else if (StringUtil.isBlank(detail.getMainTitle())) {
			return new ResultInfo<Boolean>(new FailInfo("产品对应的prdid纬度的名称不能为空"));
		}
		
		if(null!= info){
			//违禁词校验
			String checkItemInfoMsg = "" ; 
			if(StringUtils.isNotEmpty(info.getMainTitle())){
				checkItemInfoMsg = checkForbiddenWordsField(info.getMainTitle(),"SPU名称",list);
			}
			if(StringUtils.isNotEmpty(info.getRemark())){
				checkItemInfoMsg += checkForbiddenWordsField(info.getRemark(),"备注",list);
			}
			
			if(StringUtils.isNotEmpty(checkItemInfoMsg)){
				return new ResultInfo<Boolean>(new FailInfo(checkItemInfoMsg));
			}
		}
		
		return msg;
	}
	
	
	/**
	 * 
	 * <pre>
	 * 	校验prdid修改，以及sku
	 * </pre>
	 * @param info
	 * @param detail
	 * @param skus
	 * @return
	 */
	public ResultInfo<Boolean> validItemCopy(List<String> picList,String desc,String descMobile) {
		ResultInfo<Boolean> msg = new ResultInfo<Boolean>(Boolean.TRUE);
		
		if(CollectionUtils.isEmpty(picList)){
			return new ResultInfo<Boolean>(new FailInfo("上传图片不能为空,图片只能传3-10张"));
		}else{
			if(picList.size()>10||picList.size()<3){
				return new ResultInfo<Boolean>(new FailInfo("上传图片数量不对,应上传3-10张图片"));
			}
		}
		if (StringUtils.isBlank(desc)) {
			return new ResultInfo<Boolean>(new FailInfo("PC模板不能为空"));
		}else if (StringUtils.isBlank(descMobile)) {
			return new ResultInfo<Boolean>(new FailInfo("手机模板不能为空"));
		}
		List<ForbiddenWords>  list = getForbiddenWords() ;
		//违禁词校验
		String checkItemInfoMsg = "" ; 
		if(StringUtils.isNotBlank(desc)){
			checkItemInfoMsg = checkForbiddenWordsField(desc,"PC模板",list);
		}
		if(StringUtils.isNotBlank(descMobile)){
			checkItemInfoMsg += checkForbiddenWordsField(descMobile,"手机模板",list);
		}
		if(StringUtils.isNotBlank(checkItemInfoMsg)){
			return new ResultInfo<Boolean>(new FailInfo(checkItemInfoMsg));
		}
		return msg;
	}
	
	/**
	 * 
	 * 验证商品prd信息
	 * @return msg
	 */
	public ResultInfo<Boolean> validItemPrd(ItemInfo info, ItemDetail[] details,int type) {
		ResultInfo<Boolean> msg = new ResultInfo<Boolean>(Boolean.TRUE);
		// step1 校验 spu
		if(StringUtil.isBlank(info.getMainTitle())) {
			return new ResultInfo<Boolean>(new FailInfo("SPU名称不能为空"));
		}else if (null==(info.getSmallId())) {
			return new ResultInfo<Boolean>(new FailInfo("产品关联的分类不能为空"));
			// 修改的时候可以不判断分类
		} else if (null==(info.getUnitId())) {
			return new ResultInfo<Boolean>(new FailInfo("产品的单位不能为空"));
		} else if (null==(info.getBrandId())) {
			return new ResultInfo<Boolean>(new FailInfo("产品的品牌不能为空"));
		}
		
		List<ForbiddenWords>  list = getForbiddenWords() ;
		
		//违禁词校验
		String checkMsg = "" ; 
		if(StringUtils.isNotEmpty(info.getMainTitle())){
			checkMsg = checkForbiddenWordsField(info.getMainTitle(),"SPU名称",list);
		}
		if(StringUtils.isNotEmpty(info.getRemark())){
			checkMsg += checkForbiddenWordsField(info.getRemark(),"备注",list);
		}
		if(StringUtils.isNotEmpty(checkMsg)){
			return new ResultInfo<Boolean>(new FailInfo(checkMsg));
		}
		//校验大类,小类,中类,单位,品牌下spu名称唯一的 
		ItemInfo infoDO = itemManageService.checkSpuExsit(info.getSmallId(),info.getBrandId(),info.getUnitId(),info.getMainTitle(),info.getId());
		if(null!=infoDO){
			return new ResultInfo<Boolean>(new FailInfo("SPU名称已经存在了"));
		}
		if(type==1){
			return msg;
		}
		// step2 校验prdid
		if (details == null || details.length == 0) {
			return new ResultInfo<Boolean>(new FailInfo("产品的prdid纬度信息不能为空"));
		}
		
		String checkprdMsg = "";
		for (ItemDetail detail : details) {
			if(StringUtils.isNotEmpty(detail.getMainTitle())){
				checkprdMsg += checkForbiddenWordsField(detail.getMainTitle(),"商品名称",list);
			}
			
			if(detail.getId()==null){
				Map<Long,Long> specInfoMap = new HashMap <Long,Long>();
				//校验规格不能重复
				String specGroupIds = detail.getSpecGroupIds();
				if(StringUtils.isNotBlank(specGroupIds)){
					String [] str = specGroupIds.split(ItemConstant.DEFAULT_SEPARATOR);
					for(String s : str){
						String [] ids = s.split(ItemConstant.DEFAULT_JOIN);
						specInfoMap.put(Long.parseLong(ids[0]), Long.parseLong(ids[1]));
					}
				}
				
				int flag = itemManageService.checkPrdidSpec(info.getId(), specInfoMap);
				if(flag!=1){
					return new ResultInfo<Boolean>(new FailInfo("产品的prdid规格不匹配"));
				}
			}
			
			if (StringUtil.isBlank(detail.getBarcode())) {
				return new ResultInfo<Boolean>(new FailInfo("产品的条码不能为空"));
			} else if (!StringUtil.isBlank(detail.getBarcode())) {
				boolean res = false;
				try {
					if(null == detail.getId()){
						res = itemManageService.checkBarcodeExsit(
								detail.getBarcode(),null,null);
					}else{
						res = itemManageService.checkBarcodeExsit(
								detail.getBarcode(), detail.getId(),null);
					}
					if (!res) {
						return new ResultInfo<Boolean>(new FailInfo("产品的条码在系统中已经存在"));
					}
				} catch (ItemServiceException e) {
					return new ResultInfo<Boolean>(new FailInfo("产品的条码在系统中已经存在"));
				}

			} else if (StringUtil.isBlank(detail.getMainTitle())) {
				return new ResultInfo<Boolean>(new FailInfo("产品对应的prdid纬度的名称不能为空"));
			}
		}
		
		if(StringUtils.isNotEmpty(checkprdMsg)){
			return new ResultInfo<Boolean>(new FailInfo(checkprdMsg));
		}
		return msg;
	}
	
	
	/***
	 * 商品上架时 验证是否输入了手机端描述
	 * @param info
	 * @param detailDto
	 * @return
	 */
	public ResultInfo<Boolean> validItemMobileItemDescWhenPutOn(DetailDto detailDto) {
		ResultInfo<Boolean> msg = new ResultInfo<Boolean>(Boolean.TRUE);
		if(detailDto.getItemDescMobile() !=null){
			//手机端描述是空
			if(StringUtils.isBlank(detailDto.getItemDescMobile().getDescription())){
				List<DetailSkuDto> skuList = detailDto.getDetailSkuList();
				if(CollectionUtils.isNotEmpty(skuList)){
					for (DetailSkuDto detailSkuDto : skuList) {
						if(detailSkuDto.getStatus() == 1){
							return new ResultInfo<Boolean>(new FailInfo("上架商品前，必须维护手机模板"));
						}
					}
				}
			}
			
		}
		return msg;
	}
	
	
	
	/***
	 * 海淘商品 sku上架状态校验是否有海关信息
	 * @param info
	 * @param detailDto
	 * @return
	 */
	public ResultInfo<Boolean> validItemSku(List<DetailSkuDto> skuList) {
		ResultInfo<Boolean> msg = new ResultInfo<Boolean>(Boolean.TRUE);
		//海淘商品校验 如果sku是上架状态，那么必须有海关货号信息
		for (DetailSkuDto detailSkuDto : skuList) {
			// 如果sku有id 说明只能更新SKU的价格，商品号 ，上下架
			if(detailSkuDto.getStatus()==1){
				if(detailSkuDto.getId() == null){
					return new ResultInfo<Boolean>(new FailInfo("海淘商品sku在上架前必须维护海关信息，请先不上架保存，在维护海关信息，在做上架操作."));
				}else{
					ItemSkuArt check = new ItemSkuArt();
					check.setSkuId(detailSkuDto.getId());
					try {
						ItemSkuArt exist=	itemManageService.checkSkuArtWithSkuId(check);
						if(exist == null){
							return new ResultInfo<Boolean>(new FailInfo("海淘商品上架前，请先维护海关信息"));
						}
					} catch (Exception e) {
						new Throwable(e.getMessage());
					}
				}
			}
		}
		return msg;
	}
		
	
	/**
	 * 
	 * <pre>
	 *    获取违禁词列表
	 * </pre>
	 *
	 * @return
	 */
	public List<ForbiddenWords> getForbiddenWords(){
		List<ForbiddenWords> list  = forbiddenWordsService.getAllEffectiveForbiddenWords();
		return list;
	}
	
	/**
	 * 
	 * <pre>
	 *  验证违禁词
	 * </pre>
	 *
	 * @param sourceField
	 * @param fieldItemDesc
	 * @param fobiddenWords
	 * @return
	 */
	public  String checkForbiddenWordsField(String sourceField ,String fieldItemDesc,List<ForbiddenWords> fobiddenWords){
		StringBuffer sb = new StringBuffer(fieldItemDesc);
		boolean flag = false;
		sb.append("中有");
		if(CollectionUtils.isNotEmpty(fobiddenWords)){
			if(!StringUtil.isBlank(sourceField)){
				for(ForbiddenWords forbiddenWordsDO :fobiddenWords ){
					String forbiddenWords = forbiddenWordsDO.getWords();
					if(!StringUtil.isBlank(forbiddenWords)){
						int total = checkStrCount(sourceField,forbiddenWords);
						if(total>0){
							flag = true;
							sb.append(": 违禁词[").append(forbiddenWords)
							  .append("],总共出现").append(total).append("次。");
						}
					}
				}
			}
		}
		if(flag){
			return sb.toString();
		}else{
			return "";
		}
	}
	
	/**
	 * 
	 * <pre>
	 *  判断一个字符串出现另一个字符串的次数
	 * </pre>
	 *
	 * @param str
	 * @param checkStr
	 * @return
	 */
	private static  int checkStrCount(String str, String checkStr){
		int total = 0;
		for (String tmp = str; 	tmp!= null
				&&tmp.length()>=checkStr.length();){
		  if(tmp.indexOf(checkStr) == 0){
		    total ++;
		  }
		  tmp = tmp.substring(1);
		}
		return total;
	}
	
}
