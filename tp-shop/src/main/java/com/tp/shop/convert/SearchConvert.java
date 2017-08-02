package com.tp.shop.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.tp.common.util.ImageUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.dto.sch.Nav;
import com.tp.dto.sch.NavBrandDTO;
import com.tp.dto.sch.NavBrandSimple;
import com.tp.dto.sch.NavCategoryDTO;
import com.tp.m.base.PageForSearch;
import com.tp.m.enums.ImgEnum;
import com.tp.m.enums.ProductEnum;
import com.tp.m.query.search.QuerySearch;
import com.tp.m.util.NumberUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.search.NavBrandVO;
import com.tp.m.vo.search.NavCategoryVO;
import com.tp.m.vo.search.NavigationVO;
import com.tp.m.vo.search.SearchConditionVO;
import com.tp.m.vo.search.SearchItemVO;
import com.tp.m.vo.search.SearchShopVO;
import com.tp.model.sch.Element;
import com.tp.model.sch.result.Aggregate;
import com.tp.model.sch.result.ItemResult;
import com.tp.model.sch.result.ShopResult;
import com.tp.query.sch.SearchQuery;
import com.tp.shop.helper.ImgHelper;
import com.tp.shop.helper.RequestHelper;

/**
 * 搜索封装类
 * @author zhuss
 * @2016年3月2日 上午11:56:52
 */
public class SearchConvert {
	
	/**
	 * 封装搜索查询条件
	 * @return
	 */
	public static SearchQuery convertSearchParam(QuerySearch search){
		SearchQuery query = new SearchQuery();
		query.setNavBrandId(StringUtil.getLongByStr(search.getBrandid()));
		query.setNavCategoryId(StringUtil.getLongByStr(search.getCategoryid()));
		query.setKey(search.getKey());
		query.setPlatform(RequestHelper.getPlatformByName(search.getApptype()));
		query.setSort(StringUtil.getIntegerByStr(search.getSort()));
		query.setStartPage(StringUtil.getCurpageDefault(search.getCurpage()));
		query.setCountryId(StringUtil.getLongByStr(search.getCountry_id()));
		query.setBrandId(StringUtil.getLongByStr(search.getBrand_id()));
		query.setHasInventoryOnly(StringUtil.getIntegerByStr(search.getIsinstock()));
		query.setSalesPattern(StringUtil.getIntegerByStr(search.getSalespattern()));
		return query;
	}

	/**
	 * 封装搜索结果数据
	 *
	 * @return
	 */
	public static PageForSearch<SearchItemVO, List<SearchShopVO>> convertSearchItem(PageInfo<ItemResult> itemsPage, ShopResult shopResult,Long topicId) {
		PageForSearch<SearchItemVO, List<SearchShopVO>> pages = new PageForSearch<>();
		if (null != itemsPage) {
			List<ItemResult> items = itemsPage.getRows();
			if (CollectionUtils.isNotEmpty(items)) {
				List<SearchItemVO> l = new ArrayList<>();
				for (ItemResult item : items) {
					boolean isHave = item.getInventory() == null || item.getInventory() == 0 ? false : true;
					ProductEnum.Status statusenmu = ProductConvert.getStatusByPrdSta(StringUtil.getStrByObj(item.getItem_status()), isHave);
					SearchItemVO vo = new SearchItemVO();
					vo.setChannel(item.getChannel_name());
					vo.setChannelid(StringUtil.getStrByObj(item.getChannel_id()));
					vo.setCountryname(item.getCountry_name());
					vo.setImgurl(ImgHelper.getImgUrl(item.getItem_img(), ImgEnum.Width.WIDTH_360));
					vo.setName(item.getItem_name());
					vo.setOldprice(NumberUtil.sfwr(item.getSale_price().doubleValue()));
					vo.setPrice(NumberUtil.sfwr(item.getTopic_price().doubleValue()));
					vo.setSku(item.getSku());
					vo.setStatus(statusenmu.code);
					vo.setStatusdesc(statusenmu.desc);
					vo.setTid(StringUtil.getStrByObj(item.getTopic_id()));
					vo.setSalescount(item.getSales_count()==null?"" : item.getSales_count()<=0 ? "" :"已售"+item.getSales_count());
					vo.setSalespattern(StringUtil.getStrByObj(item.getSales_pattern()));
					vo.setShopname(StringUtil.getStrByObj(item.getShop_name()));
					l.add(vo);
				}
				pages.setFieldTCount(l, itemsPage.getPage(), itemsPage.getRecords());
			}
			pages.setCurpage(itemsPage.getPage());
		}
		if (shopResult != null && topicId !=null) {
			SearchShopVO searchShopVO = new SearchShopVO();
			searchShopVO.setShopid(shopResult.getShop_id().toString());
			searchShopVO.setShopname(shopResult.getShop_name());
			searchShopVO.setShopbanner(ImgHelper.getImgUrl(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item,shopResult.getShop_banner()), ImgEnum.Width.WIDTH_750));
			searchShopVO.setShoplogo(ImgHelper.getImgUrl(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item,shopResult.getShop_logo()), ImgEnum.Width.WIDTH_80));
			searchShopVO.setShopintro(shopResult.getShop_intro());
			searchShopVO.setSupplierid(shopResult.getSupplier_id());
			searchShopVO.setTid(String.valueOf(topicId));

			pages.setSp(Arrays.asList(searchShopVO));

		}else {
			pages.setSp(Collections.EMPTY_LIST);
		}

		return pages;
	}
	
	/**
	 * 封装搜索结果页面中的筛选条件
	 * @return
	 */
	public static List<SearchConditionVO> convertSearchCondition(List<Aggregate> aggregates){
		List<SearchConditionVO> list = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(aggregates)){
			for(Aggregate agg : aggregates){
				SearchConditionVO vo = new SearchConditionVO();
				vo.setCode(agg.getCode());
				vo.setName(agg.getName());
				List<Element> elements = agg.getElements();
				if(CollectionUtils.isNotEmpty(elements)){
					List<SearchConditionVO> child = new ArrayList<>();
					for(Element element : elements){
						child.add(new SearchConditionVO(element.getKey(),element.getValue()));
					}
					vo.setChild(child);
				}
				list.add(vo);
			}
		}
		return list;
	}

	/**
	 * 封装导航
	 * @return
	 */
	public static NavigationVO convertNavigation(Nav nav){
		NavigationVO vo = new NavigationVO();
		if(null != nav){
			List<NavCategoryDTO> categorieDTOs = nav.getCategories();
			if(CollectionUtils.isNotEmpty(categorieDTOs)){
				List<NavCategoryVO> categories = new ArrayList<>();
				for(NavCategoryDTO dto : categorieDTOs){
					categories.add(convertNavCategory(dto));
				}
				vo.setCategories(categories);
			}
			List<NavBrandDTO> brandDTOs = nav.getBrands();
			if(CollectionUtils.isNotEmpty(brandDTOs)){
				List<NavBrandVO> brands = new ArrayList<>();
				for(NavBrandDTO dto : brandDTOs){
					brands.add(convertNavBrand(dto));
				}
				vo.setBrands(brands);
			}
		}
		return vo;
	}
	
	/**
	 * 封装搜索导航的分类
	 * @param dto
	 * @return
	 */
	public static NavCategoryVO convertNavCategory(NavCategoryDTO dto){
		NavCategoryVO navc = new NavCategoryVO();
		navc.setCategoryid(StringUtil.getStrByObj(dto.getId().toString()));
		navc.setImgurl(ImgHelper.getImgUrl(dto.getPic(), ImgEnum.Width.WIDTH_750));
		navc.setName(dto.getName());
		navc.setIshighlight(StringUtil.getStrByObj(dto.getIsHighlight()));
		List<NavCategoryDTO> list = dto.getChild();
		if(CollectionUtils.isNotEmpty(list)){
			List<NavCategoryVO> child = new ArrayList<>();
			for(NavCategoryDTO d : list){
				child.add(convertNavCategory(d));
			}
			navc.setChild(child);
		}
		return navc;
	}
	
	/**
	 * 封装搜索导航 - 品牌
	 * @param dto
	 * @return
	 */
	public static NavBrandVO convertNavBrand(NavBrandDTO dto){
		NavBrandVO navc = new NavBrandVO();
		navc.setName(dto.getName());
		List<NavBrandSimple> list = dto.getBrands();
		if(CollectionUtils.isNotEmpty(list)){
			List<NavBrandVO> child = new ArrayList<>();
			for(NavBrandSimple d : list){
				child.add(new NavBrandVO(StringUtil.getStrByObj(d.getBrandId()),d.getName(),ImgHelper.getImgUrl(d.getPic(), ImgEnum.Width.WIDTH_180)));
			}
			navc.setChild(child);
		}
		return navc;
	}
}
