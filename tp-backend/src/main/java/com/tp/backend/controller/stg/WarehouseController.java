package com.tp.backend.controller.stg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.StorageConstant.StorageType;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.common.vo.customs.CustomsConstant;
import com.tp.common.vo.customs.CustomsConstant.TransF;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.ClearanceChannels;
import com.tp.model.bse.CustomsDistrictInfo;
import com.tp.model.bse.CustomsEntryport;
import com.tp.model.bse.DistrictInfo;
import com.tp.model.bse.ExpressInfo;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.SupplierInfo;
import com.tp.proxy.bse.CustomsDistrictInfoProxy;
import com.tp.proxy.bse.CustomsEntryportProxy;
import com.tp.proxy.bse.DistrictInfoProxy;
import com.tp.proxy.bse.ExpressInfoProxy;
import com.tp.proxy.stg.WarehouseProxy;
import com.tp.proxy.stg.WarehouseValidateProxy;
import com.tp.proxy.stg.WarehouseProxy.WarehouseConvert;
import com.tp.result.bse.AreaTree;

import net.sf.json.JSONArray;

/**
 * 仓库管理
 * 
 * @author 付磊 2015年1月8日 下午8:36:35
 *
 */
@Controller
@RequestMapping("/storage/warehouse")
public class WarehouseController extends AbstractBaseController{

	private static final Log LOG = LogFactory.getLog(WarehouseController.class);//输出日志

	@Autowired
	private WarehouseProxy wareHouseProxy;

	@Autowired
	private WarehouseValidateProxy wareHouseValidateProxy;

	@Autowired
	private DistrictInfoProxy districtInfoProxy;

	@Autowired
	private CustomsEntryportProxy customsEntryportProxy;
	
	@Autowired
	private ExpressInfoProxy expressInfoProxy;
	
	@Autowired
	private CustomsDistrictInfoProxy customsDistrictInfoProxy;
	
	/**
	 * 
	 * <pre>
	 * 仓库信息列表 | 条件查询
	 * </pre>
	 *
	 * @param forbiddenWords
	 */
	@RequestMapping("/list")
	public String list(Warehouse warehouse, Integer page, Integer size, Model model) {
		Integer startPage = page == null ? 1 : page;
		Integer pageSize = size == null ? 10 : size;
		PageInfo<WarehouseConvert> pageInfo = wareHouseProxy.getWareHouseList(warehouse, startPage, pageSize);
		model.addAttribute("page", pageInfo);
		model.addAttribute("warehouse", warehouse);
		return "storage/warehouse/list";
	}

	@RequestMapping("/warehouselist")
	@ResponseBody
	public ResultInfo<List<Warehouse>> warehouselist(Warehouse warehouse){
		return wareHouseProxy.queryByObject(warehouse);
	}
	/**
	 * 
	 * <pre>
	 * 添加仓库信息
	 * </pre>
	 *
	 * @param forbiddenWords
	 */
	@RequestMapping("/add")
	public String toSaveOrUpdate(Warehouse wareHouseDO, Model model) {
		if (null == wareHouseDO) {
			LOG.info("数据不能为空");
		}
		Map<String, List<DistrictInfo>> mapProvince = new HashMap<String, List<DistrictInfo>>();// 配送区域区-省
		List<DistrictInfo> agencyAdd = wareHouseProxy.getDistrictInfo();// 机构

		mapProvince = wareHouseProxy.getProvince();
		model.addAttribute("agencyAdd", agencyAdd);
		model.addAttribute("mapProvince", mapProvince);
		//仓库类型
		model.addAttribute("storageTypes", StorageType.values());
		
		//保税区仓库参数
		ResultInfo<List<CustomsEntryport>> entryPortResultInfo = customsEntryportProxy.queryByObject(new CustomsEntryport());
		ResultInfo<List<ExpressInfo>> expressInfoResultInfo = expressInfoProxy.queryByObject(new ExpressInfo());	
		ResultInfo<List<CustomsDistrictInfo>> customsDistrictResultInfo = customsDistrictInfoProxy.queryByObject(new CustomsDistrictInfo());
		model.addAttribute("seaportList", entryPortResultInfo.getData());
		model.addAttribute("customsFieldList", CustomsConstant.CustomsField.values());
		model.addAttribute("expressList", expressInfoResultInfo.getData());
		model.addAttribute("declareTypeList", CustomsConstant.DeclareCompanyType.values());
		model.addAttribute("senderCountryList", customsDistrictResultInfo.getData());
		model.addAttribute("postModeList", CustomsConstant.DeliveryType.values());
		
		model.addAttribute("trafModeList", TransF.values());
		model.addAttribute("tradeCountryList", customsDistrictResultInfo.getData());
		//主仓库
		Warehouse w = new Warehouse();
		w.setMainType(1);
		ResultInfo<List<Warehouse>> warehouseResult = wareHouseProxy.queryByObject(w);
		model.addAttribute("warehouseList", warehouseResult.getData());
		return "storage/warehouse/add";
	}


	/**
	 * 
	 * <pre>
	 * 仓库信息保存到数据库
	 * </pre>
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<String> saveWareHouse(Warehouse wareHouseDO) throws Exception {
		wareHouseDO.setPutSign(wareHouseDO.getPutSign() == null ? 0 : wareHouseDO.getPutSign());
		wareHouseDO.setType(wareHouseDO.getType() == null ? 0 : wareHouseDO.getType());
		wareHouseDO.setBondedArea(wareHouseDO.getBondedArea() == null ? 0 : wareHouseDO.getBondedArea());
		ResultInfo<String> msg = wareHouseValidateProxy.warehouseValidate(wareHouseDO);
		if (Boolean.FALSE == msg.isSuccess()) {// 返回结果信息
			 return new ResultInfo<>(new FailInfo(msg.getMsg().getMessage()));
		}else{
			wareHouseProxy.addWareHouse(wareHouseDO);
			return new ResultInfo<>("操作成功");
		}
		
	}

	/**
	 * 
	 * <pre>
	 * 编辑仓库信息
	 * </pre>
	 *
	 * @param forbiddenWords
	 * @return 
	 */
	@RequestMapping(value = { "/edit" })
	// @ResponseBody
	public String doSaveOrUpdate(Model model, HttpServletRequest request) {
		// 获取省信息
		Map<String, List<DistrictInfo>> mapPro = new HashMap<String, List<DistrictInfo>>();
		// 编辑当前记录，根据id获取当前记录的信息,必须有，否则不会进行更新
		String id = request.getParameter("id");
		Long wareHouseId = (long) Integer.parseInt(id);
		Warehouse wareHouseDO = wareHouseProxy.getById(wareHouseId);
		List<ClearanceChannels> clearanceChannelList = wareHouseProxy.initClearanceChannelsList(wareHouseDO.getType());
		//tmp
		if (StorageConstant.StorageType.OVERSEASMAIL.getValue() == wareHouseDO.getType() && 0 == wareHouseDO.getBondedArea()) {
			Long bondArea = 0l;
			for (ClearanceChannels clearanceChannels : clearanceChannelList) {
				if (clearanceChannels.getName().equals(ClearanceChannelsEnum.HWZY.toString())) {
					bondArea = clearanceChannels.getId();
					break;
				}
			}
			wareHouseDO.setBondedArea(bondArea);
		}
		
		List<DistrictInfo> agencyEdit = wareHouseProxy.getDistrictInfo();// 获取机构信息

		model.addAttribute("agencyEdit", agencyEdit);
		model.addAttribute("wareHouseDO", wareHouseDO);
		model.addAttribute("mapPro", mapPro);
		//仓库类型
		model.addAttribute("storageTypes", StorageType.values());
		//仓库保税区
		model.addAttribute("bondedareaTypes", clearanceChannelList);
		
		//保税区仓库参数
		ResultInfo<List<CustomsEntryport>> entryPortResultInfo = customsEntryportProxy.queryByObject(new CustomsEntryport());
		ResultInfo<List<ExpressInfo>> expressInfoResultInfo = expressInfoProxy.queryByObject(new ExpressInfo());	
		ResultInfo<List<CustomsDistrictInfo>> customsDistrictResultInfo = customsDistrictInfoProxy.queryByObject(new CustomsDistrictInfo());
		model.addAttribute("seaportList", entryPortResultInfo.getData());
		model.addAttribute("customsFieldList", CustomsConstant.CustomsField.values());
		model.addAttribute("expressList", expressInfoResultInfo.getData());
		model.addAttribute("declareTypeList", CustomsConstant.DeclareCompanyType.values());
		model.addAttribute("senderCountryList", customsDistrictResultInfo.getData());
		model.addAttribute("postModeList", CustomsConstant.DeliveryType.values());
		
		model.addAttribute("trafModeList", TransF.values());
		model.addAttribute("tradeCountryList", customsDistrictResultInfo.getData());
		
		Map<String, Object> params = new HashMap<>();
		params.put("mainType", 1);
		if(wareHouseDO.getMainType() != null && wareHouseDO.getMainType() > 0){
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "id not in(" + 
						StringUtils.join(new ArrayList<Long>(Arrays.asList(wareHouseDO.getId())), SPLIT_SIGN.COMMA) + ")");	
		}
		ResultInfo<List<Warehouse>> warehouseResult = wareHouseProxy.queryByParam(params);
		model.addAttribute("warehouseList", warehouseResult.getData());
		return "storage/warehouse/edit";
	}

	/**
	 * 
	 * <pre>
	 * 动态更新仓库信息
	 * </pre>
	 *
	 * @param forbiddenWords
	 */
	@RequestMapping(value = { "/update" })
	@ResponseBody
	public ResultInfo<String> updateWareHouse(Warehouse warehouseDO) throws Exception {
		warehouseDO.setType(warehouseDO.getType() == null ? 0 : warehouseDO.getType());
		warehouseDO.setPutSign(warehouseDO.getPutSign() == null ? 0 : warehouseDO.getPutSign());
		warehouseDO.setBondedArea(warehouseDO.getBondedArea() == null ? 0 : warehouseDO.getBondedArea());
		ResultInfo<String> msg = wareHouseValidateProxy.warehouseValidate(warehouseDO);
		if (Boolean.FALSE == msg.isSuccess()) {// 返回结果信息
			return new ResultInfo<>(new FailInfo(msg.getMsg().getMessage()));
		}else{
			wareHouseProxy.updateWareHouseDO(warehouseDO);
			return new ResultInfo<>("操作成功！");
		}
		
	}
	
	/**
	 * 
	 * <pre>
	 * 查询供应商信息
	 * </pre>
	 *
	 * @param model
	 * @param detailId
	 * @param index
	 * @param pageSize
	 * @param supplierName
	 * @param supplierType
	 * @param request
	 * @return item_supplier.htm
	 */
	@RequestMapping(value = "/selectSupplier")
	public String selectSupplier(Model model,Integer page,Integer size,String supplierNameQuery, HttpServletRequest request, Long supplierIdQuery) {
		Integer startPage = page == null ? 1 : page;
		Integer pageSize = size == null ? 10 : size;
		// 初始化供应商类型信息
		PageInfo<SupplierInfo> pageInfo = wareHouseProxy.getSupplierListWidthCondition(
				supplierIdQuery, SupplierType.ASSOCIATE.getValue(), supplierNameQuery, startPage,
				pageSize);
		model.addAttribute("page", pageInfo);
		model.addAttribute("supplierIdQuery", supplierIdQuery);
		model.addAttribute("supplierNameQuery", supplierNameQuery);
		// 初始化供应商类型信息
		Map<String, String> supplierTypeMap = wareHouseProxy.initSupplierType();
		model.addAttribute("supplierTypes", supplierTypeMap);
		return "/storage/warehouse/item_supplier";
	}

	/**
	 * 区域选择四级联动
	 * 
	 * <pre>
	 * 
	 * </pre>
	 *
	 * @param request
	 * @param response
	 * @return JSON
	 */
	@RequestMapping(value = "/getarea")
	@ResponseBody
	public JSONArray getArea(HttpServletRequest request,
			HttpServletResponse response) {// 省市县区街道，四级联动
		String disID = request.getParameter("id");
		DistrictInfo districtInfoDO = new DistrictInfo();
		districtInfoDO.setParentId(Long.parseLong(disID));
		List<DistrictInfo> quyu = wareHouseProxy.getInfo(districtInfoDO);

		JSONArray jsonArray = JSONArray.fromObject(quyu);
		return jsonArray;
	}
	
	/**
	 * 
	 * <pre>
	 * 获取仓库编码
	 * </pre>
	 *
	 * @param request
	 */
	@RequestMapping(value={"/getcode"})
	@ResponseBody
	public String getBuildCode(String code){
		return wareHouseProxy.convertCode(code);
	}
	
	
	@RequestMapping(value = "/getAreas")
	@ResponseBody
	public List<AreaTree> getAreas(HttpServletRequest request,
			HttpServletResponse response) {// 省市县区街道，四级联动
		List<AreaTree> areaTree = districtInfoProxy.selectChinaRegions();
		//JSONObject jSONObject = JSONObject.fromObject(areaTree);
		return areaTree;
	}
	
	@RequestMapping(value = "/getStreets")
	@ResponseBody
	public List<AreaTree> getStreets(HttpServletRequest request,
			HttpServletResponse response,Long id ) {// 省市县区街道，四级联动
		List<AreaTree> areaTree = districtInfoProxy.selectChinaStreets(id);
		//JSONObject jSONObject = JSONObject.fromObject(areaTree);
		return areaTree;
	}
	
	/**
	 * 获取通关渠道
	 * @param catId
	 * @return List<CategoryDO>
	 */
	@RequestMapping(value="bondedarea-list",method=RequestMethod.GET)
	@ResponseBody
	public ResultInfo<List<ClearanceChannels>> getCldCategoryList(Integer typeId){
		if(null==typeId){
			return new ResultInfo<>(new FailInfo("类型错误"));
		}
		List<ClearanceChannels> list = wareHouseProxy.initClearanceChannelsList(typeId);
		return new ResultInfo<>(list);
	}
	
	
	@RequestMapping(value="delete",method=RequestMethod.GET)
	@ResponseBody
	public ResultInfo<String> deleteWarehouse(Long id,HttpServletResponse response)throws Exception{
		ResultInfo<String> msg = wareHouseValidateProxy.deleteWarehouseValidate(id);
		if (Boolean.FALSE == msg.isSuccess()) {// 返回结果信息
			return new ResultInfo<>(new FailInfo(msg.getMsg().getMessage()));
		}else{
			wareHouseProxy.deleteWarehouse(id);
			return new ResultInfo<>("操作成功");
		}
	}
}
