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
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.DistrictInfo;
import com.tp.model.bse.NationalIcon;
import com.tp.proxy.bse.DistrictInfoProxy;
import com.tp.proxy.bse.NationalIconProxy;

@Controller
@RequestMapping("/basedata/nationalIcon")
public class NationalIconController extends AbstractBaseController {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(NationalIconController.class);

	@Autowired
	private NationalIconProxy nationalIconProxy;
	@Autowired
	private DistrictInfoProxy districtInfoProxy;
	/**
	 * 产地信息列表查询
	 * @param model
	 * @param brandDO
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	public void list(Model model,NationalIcon nationalIcon,Integer sortNo,String nameEn,Integer page,Integer size){
		nationalIcon.setSortNo(sortNo);
		Map<String, String> continentInformations = districtInfoProxy.inintContinentInformation();
		Map<String, String> allCountryAndAllProvince = districtInfoProxy.getAllCountryAndAllProvince();
	    if(StringUtils.isBlank(nameEn)){
	    	nationalIcon.setNameEn(null);
	    }
	    PageInfo<NationalIcon> pageInfo = new PageInfo<NationalIcon>(page,size);
		ResultInfo<PageInfo<NationalIcon>> pageInfoResult = nationalIconProxy.queryPageByObject(nationalIcon, pageInfo);
		if(pageInfoResult.success && CollectionUtils.isNotEmpty(pageInfoResult.getData().getRows())){
			for(NationalIcon national:pageInfoResult.getData().getRows()){
				national.setPicPath(ImageDownUtil.getThumbnail(Constant.IMAGE_URL_TYPE.basedata.url, national.getPicPath(),IMAGE_SIZE.model40_40));
			}
		}
		model.addAttribute("allCountryAndAllProvince", allCountryAndAllProvince);
		model.addAttribute("nationalIcon", nationalIcon);
		model.addAttribute("queryAllNationalIconByPage", pageInfoResult.getData());
		model.addAttribute("continentInformations", continentInformations);
	}

	/**
	 * 产地信息编辑
	 * @param model
	 * @param brandDO
	 * @throws Exception
	 */
	@RequestMapping(value = "/edit")
	public void edit(@RequestParam(value = "id") Long id, Model model)throws Exception {
		if (null==id) {
			throw new Exception("id为空,异常");
		}
		ResultInfo<NationalIcon> nationalIconResultInfo = nationalIconProxy.queryById(id);
		if(nationalIconResultInfo.success){
			nationalIconResultInfo.getData().setPicPath(ImageDownUtil.getThumbnail(Constant.IMAGE_URL_TYPE.basedata.url, nationalIconResultInfo.getData().getPicPath(),IMAGE_SIZE.model100_100));
		}
		// 返回地区信息
		Map<String, String> allCountryAndAllProvince = districtInfoProxy.getAllCountryAndAllProvince();
		model.addAttribute("allCountryAndAllProvince", allCountryAndAllProvince);
		model.addAttribute("nationalIcon", nationalIconResultInfo.getData());
		model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.basedata.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.basedata.name());
	}

	
	/**
	 *  转到增加产地信息的页面
	 */
	@RequestMapping(value = "/add")
	public void addNationalIcon(Model model) {
		Map<String, String> allCountryAndAllProvince = districtInfoProxy.getAllCountryAndAllProvince();
		model.addAttribute("allCountryAndAllProvince", allCountryAndAllProvince);
		model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.basedata.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.basedata.name());
	}

	/**
	 * 
	 * <pre>
	 * 产地信息增加发的提交
	 * </pre>
	 *
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/addNationalIconSubmit")
	@ResponseBody
	public ResultInfo<?> addNationalIconSubmit(NationalIcon nationalIcon) throws Exception {
		if (null==nationalIcon) {
			LOGGER.info("数据不能为空");
			throw new Exception("数据不能为空");
		}
		return nationalIconProxy.addNationalIcon(nationalIcon);
	}

	/**
	 * 
	 * <pre>
	 * 更新产地信息
	 * </pre>
	 *
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> updateNationalIcon(NationalIcon nationalIcon) throws Exception {
		if (null==nationalIcon) {
			LOGGER.info("数据不能为空");
			throw new Exception("数据不能为空");
		}
		return nationalIconProxy.updateNationalIcon(nationalIcon, false);
	}

	/**
	 * 
	 * <pre>
	 *  根据国家名动态查询信息
	 * </pre>
	 *
	 * @param name
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/queryCountry")
	public @ResponseBody List<DistrictInfo> queryCountryInformationByName(String term) throws Exception {
		if(StringUtils.isBlank(term)){
			return null;
		}
		LOGGER.info(term);
		return districtInfoProxy.getDistrictInfosByCountryName(term.trim());
	}
	
	
	
	@RequestMapping(value="/uploadBrandFiles",method=RequestMethod.POST,produces="text/json")
	@ResponseBody
	public String uploadMultiFiles(HttpServletRequest request) {
		return null;
	}
}
