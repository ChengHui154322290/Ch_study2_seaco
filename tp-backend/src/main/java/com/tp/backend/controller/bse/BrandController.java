package com.tp.backend.controller.bse;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.util.ImageDownUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.Constant.IMAGE_SIZE;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.Brand;
import com.tp.model.bse.DistrictInfo;
import com.tp.proxy.bse.BrandProxy;
import com.tp.proxy.bse.DistrictInfoProxy;

@Controller
@RequestMapping("/basedata/brand")
public class BrandController extends AbstractBaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BrandController.class);

	@Autowired
	private BrandProxy brandProxy;
	@Autowired
	private DistrictInfoProxy districtInfoProxy;

	/**
	 * 
	 * <pre>
	 * 品牌列表查询
	 * </pre>
	 *
	 * @param model
	 * @param brand
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	public void list(Model model, Brand brand, Integer page, Integer size)
			throws Exception {
		PageInfo<Brand> pageInfo = new PageInfo<Brand>(page,size);
		ResultInfo<PageInfo<Brand>> pageInfoResult = brandProxy.queryPageByObject(brand, pageInfo);
		if(pageInfoResult.success && CollectionUtils.isNotEmpty(pageInfoResult.data.getRows())){
			List<Brand> brandList = pageInfoResult.data.getRows();
			for(Brand row:brandList){
				row.setLogo(ImageDownUtil.getThumbnail(Constant.IMAGE_URL_TYPE.basedata.url, row.getLogo(),IMAGE_SIZE.model40_40));
			}
			pageInfoResult.data.setRows(brandList);
		}
		Map<String, String> allCountryAndAllProvince = districtInfoProxy.getAllCountryAndAllProvince();
		model.addAttribute("allCountryAndAllProvince", allCountryAndAllProvince);
		model.addAttribute("brand", brand);
		model.addAttribute("queryAllBrandByPageInfo",pageInfoResult.getData());
	}

	/**
	 * 
	 * <pre>
	 * 品牌编辑
	 * </pre>
	 *
	 * @param model
	 * @param brand
	 * @throws Exception
	 */
	@RequestMapping(value = "/edit")
	public void edit(@RequestParam(value = "id") Long id, Model model)
			throws Exception {
		if (null == id) {
			throw new Exception("id为空,异常");
		}
		ResultInfo<Brand> brandResultInfo = brandProxy.queryById(id);
		if(brandResultInfo.success){
			brandResultInfo.getData().setLogo(ImageDownUtil.getThumbnail(Constant.IMAGE_URL_TYPE.basedata.url, brandResultInfo.getData().getLogo(),IMAGE_SIZE.model100_100));
		}
		model.addAttribute("brand", brandResultInfo.getData());
		Map<String, String> allCountryAndAllProvince = districtInfoProxy.getAllCountryAndAllProvince();
		model.addAttribute("allCountryAndAllProvince", allCountryAndAllProvince);
		model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.basedata.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.basedata.name());
	}

	/**
	 * 
	 * <pre>
	 * 转到增加品牌的页面
	 * </pre>
	 *
	 */
	@RequestMapping(value = "/add")
	public void addBrand(Model model) {
		Map<String, String> allCountryAndAllProvince = districtInfoProxy.getAllCountryAndAllProvince();
		model.addAttribute("allCountryAndAllProvince", allCountryAndAllProvince);
		model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.basedata.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.basedata.name());
	}

	/**
	 * 
	 * <pre>
	 * 品牌增加发的提交
	 * </pre>
	 *
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/addBrandSubmit")
	@ResponseBody
	public ResultInfo<?> addBrandSubmit(Brand brand) throws Exception {
		if (null == brand) {
			LOGGER.info("数据不能为空");
			return new ResultInfo<>(new FailInfo("数据异常"));
		}
		brandProxy.addBrand(brand);
		return new ResultInfo<>(Boolean.TRUE);
	}

	/**
	 * 
	 * <pre>
	 * 更新品牌
	 * </pre>
	 *
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> updateBrand(Brand brand, HttpServletRequest request)
			throws Exception {
		if (null == brand) {
			LOGGER.info("数据不能为空");
			throw new Exception("数据异常");
		}
		return brandProxy.updateNotNullById(brand);
	}

	/**
	 * 
	 * <pre>
	 * 根据国家名动态查询信息
	 * </pre>
	 *
	 * @param name
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/queryCountry")
	public @ResponseBody List<DistrictInfo> queryCountryInformationByName(
			String term) throws Exception {
		if (StringUtils.isBlank(term)) {
			return null;
		}
		LOGGER.info(term);
		return districtInfoProxy.getDistrictInfosByCountryName(term);
	}

	@RequestMapping(value = "/uploadBrandFiles", method = RequestMethod.POST, produces = "text/json")
	@ResponseBody
	public String uploadMultiFiles(HttpServletRequest request) {
		return "";
	}
}
