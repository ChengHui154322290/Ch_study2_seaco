package com.tp.backend.controller.bse;

import java.util.Map;

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
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.TaxRate;
import com.tp.proxy.bse.TaxRateProxy;

@Controller
@RequestMapping("/basedata/taxRate")
public class TaxRateController extends AbstractBaseController {

	 private static final Logger LOG = LoggerFactory.getLogger(TaxRateController.class);


	@Autowired
	private TaxRateProxy taxRateProxy;

	/**
	 * 税率列表查询
	 * @param model
	 * @param taxRate
	 */
	@RequestMapping(value = "/list")
	public void list(Model model, TaxRate taxRate,String myRate,String myCode, Integer page, Integer size) throws Exception {
		if (null==taxRate ) {
			taxRate = new TaxRate();
		}
		if(StringUtils.isNotBlank(myRate)){
			try {
				double parseDouble = Double.parseDouble(myRate.trim());
				taxRate.setRate(parseDouble);
			    } catch (Exception e) {
				//不做处理
			}
		}
		if(StringUtils.isNotBlank(myCode)){
			taxRate.setCode(myCode.trim());
		}
		PageInfo<TaxRate> pageInfo = new PageInfo<TaxRate>(page,size);
		ResultInfo<PageInfo<TaxRate>> queryAllTaxRateByPageResultInfo = taxRateProxy.queryPageByObject(taxRate, pageInfo);
		Map<String, String> taxRateTypes = taxRateProxy.initTaxRateType();
		model.addAttribute("queryAllTaxRateByPage", queryAllTaxRateByPageResultInfo.getData());
		model.addAttribute("taxRateTypes", taxRateTypes);
		model.addAttribute("taxRate", taxRate);
	}

	/**
	 * 
	 * <pre>
	 *  税率编辑
	 * </pre>
	 *
	 * @param model
	 * @param taxRate
	 * @throws Exception 
	 */
	@RequestMapping(value = "/edit")
	public void edit(@RequestParam(value = "id") Long id, Model model) throws Exception {
		if (null==id ) {
			throw new Exception("id为空,异常");
		}
		ResultInfo<TaxRate> taxRateResultInfo = taxRateProxy.queryById(id);
		Map<String, String> taxRateTypes = taxRateProxy.initTaxRateType();
		model.addAttribute("taxRate", taxRateResultInfo.getData());
		model.addAttribute("taxRateTypes", taxRateTypes);
	}

	@RequestMapping(value = "/add")
	public void addColor(Model model) {
		Map<String, String> taxRateTypes = taxRateProxy.initTaxRateType();
		model.addAttribute("taxRateTypes", taxRateTypes); 
	}

	/**
	 * 
	 * <pre>
	 *  税率增加
	 * </pre>
	 *
	 * @param
	 */
	@RequestMapping(value = "/addtaxRateSubmit", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> addColorSubmit(TaxRate taxRate) throws Exception {
		if (null==taxRate) {
			LOG.info("数据不能为空");
			throw new Exception("数据不能为空");
		}
		return taxRateProxy.addTaxRate(taxRate);
	}

	/**
	 * 
	 * <pre>
	 * 更新 税率
	 * </pre>
	 *
	 * @param
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> updateColor(TaxRate taxRate) throws Exception {
		if (null==taxRate ) {
			LOG.info("数据不能为空");
			throw new Exception("数据不能为空");
		}
		return taxRateProxy.updateTaxRate(taxRate, false);
	}
}
