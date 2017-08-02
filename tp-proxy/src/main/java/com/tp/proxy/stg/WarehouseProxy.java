package com.tp.proxy.stg;

import java.beans.Beans;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.text.html.HTMLDocument.HTMLReader.ParagraphAction;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mchange.v2.beans.BeansUtils;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.StorageConstant.StorageType;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.model.bse.ClearanceChannels;
import com.tp.model.bse.DistrictInfo;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.SupplierInfo;
import com.tp.proxy.BaseProxy;
import com.tp.result.sup.SupplierResult;
import com.tp.service.IBaseService;
import com.tp.service.bse.IClearanceChannelsService;
import com.tp.service.bse.IDistrictInfoService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.sup.IPurchasingManagementService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.util.BeanUtil;
import com.tp.util.StringUtil;

import net.sf.json.util.JSONStringer;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
/**
 * 仓库信息表代理层
 * @author szy
 *
 */
@Service
public class WarehouseProxy extends BaseProxy<Warehouse>{

	@Autowired
	private IWarehouseService warehouseService;

	@Autowired
	private IDistrictInfoService districtInfoService;

	@Autowired
	private IPurchasingManagementService purchasingManagementService;

	@Autowired
	private ISupplierInfoService supplierInfoService;
	
	@Autowired
	private IClearanceChannelsService clearanceChannelsService;

	private static Logger logger = LoggerFactory.getLogger(WarehouseProxy.class);

	@Override
	public IBaseService<Warehouse> getService() {
		return warehouseService;
	}
	
	public List<Warehouse> getWarehouseByIds(List<Long> ids){
		return warehouseService.queryByIds(ids);
	}
	
	
	/**
	 * 
	 * <pre>
	 * 生成仓库编码，生成规则：根据选中的供应商，将其名称的拼音作为编码，使用pinyin4j插件完成
	 * </pre>
	 *
	 * @param spname
	 *            供应商名称
	 * @return 生成的仓库编码
	 */
	public String convertCode(String spname) {
		if (null == spname || "".equals(spname)) {
			return null;
		}
		char[] c = spname.toCharArray();
		StringBuffer strRes = new StringBuffer();
		for (Character ch : c) {
			if (null == ch.toString() || "".equals(ch.toString())) {
				continue;
			}
//			if (ch.equals('淘')) {
//				strRes.append('t');
//				continue;
//			}
			if ('（' == ch || '）' == ch || '(' == ch || ')' == ch || ' ' == ch
					|| '-' == ch) {
				continue;
			}
			if (this.isHanZi(ch)) {
				HanyuPinyinOutputFormat formatPinYin = new HanyuPinyinOutputFormat();
				formatPinYin.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
				formatPinYin.setCaseType(HanyuPinyinCaseType.LOWERCASE);
				formatPinYin
						.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
				String[] strPinYinTemp = null;
				try {
					strPinYinTemp = PinyinHelper.toHanyuPinyinStringArray(ch,
							formatPinYin);
					strRes.append(strPinYinTemp[0].substring(0, 1));
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					logger.error("汉字转拼音格式化输出失败", e);
				}
			} else {
				strRes.append(ch);
			}
		}

		return this.isExist(strRes.toString());
	}

	/**
	 * 
	 * <pre>
	 * 校验仓库编码是否重复
	 * </pre>
	 *
	 * @param str
	 * @return String,如果没有重复，则返回该字符，否则在后面加 0~10随机数 直至不重复
	 */
	public String isExist(String str) {
		Map<String, Object> params = new HashMap<>();
		params.put("code", str);
		List<Warehouse> resValidate = warehouseService.queryByParamNotEmpty(params);
		if (resValidate.size() <= 0) {
			return str;
		} else {
			return isExist(str + new Random().nextInt(10));
		}
	}

	/**
	 * 
	 * <pre>
	 * 校验是否是汉字
	 * </pre>
	 *
	 * @param DistrictInfo
	 * @return boolean,返回true则是汉字
	 */
	public boolean isHanZi(Character ch) {
		String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(ch);
		if (null == pinyinArray) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * <pre>
	 * 获取配送区域信息
	 * </pre>
	 *
	 * @param districtInfo
	 * @return List<DistrictInfo>
	 */
	public List<DistrictInfo> getInfo(DistrictInfo districtInfo) {
		return districtInfoService.queryByObject(districtInfo);
	}

	/**
	 * 
	 * <pre>
	 * 初始化供应商类型
	 * </pre>
	 *
	 * @return Map<String, String>
	 */
	public Map<String, String> initSupplierType() {
		// 商家类型
		Map<String, String> supplierTypes = new HashMap<String, String>();
		SupplierType[] values = SupplierType.values();
		for (SupplierType sType : values) {
			supplierTypes.put(sType.getValue(), sType.getName());
		}
		return supplierTypes;
	}

	/**
	 * 
	 * <pre>
	 * 获取供应商列表
	 * </pre>
	 *
	 * @param Long
	 *            supplierId
	 * @param String
	 *            supplierName
	 * @param String
	 *            supplierType
	 * @param int startPage
	 * @param int pageSize
	 * @return Page<SupplierInfo>
	 */
	public PageInfo<SupplierInfo> getSupplierListWidthCondition(Long supplierId,
			String supplierType, String supplierName, Integer startPage,
			Integer pageSize) {
		List<SupplierType> supplierTypeList = new ArrayList<SupplierType>();

		supplierTypeList.add(SupplierType.ASSOCIATE);

		SupplierInfo supplierObj = supplierInfoService.queryById(0L);// 查出id=0的记录

		SupplierResult result = null;

		if (1 == startPage) {
			result = purchasingManagementService.getSupplierListWithCondition(
					supplierId, supplierTypeList, supplierName, startPage,
					pageSize);
			result.getSupplierInfoList().add(0, supplierObj);
		} else {
			result = purchasingManagementService.getSupplierListWithCondition(
					supplierId, supplierTypeList, supplierName, startPage,
					pageSize);
		}

		PageInfo<SupplierInfo> page = new PageInfo<SupplierInfo>();
		page.setPage(result.getStartPage());
		page.setSize(result.getPageSize());
		page.setRows(result.getSupplierInfoList());
		page.setRecords(result.getTotalCount().intValue());
		return page;
	}

	/**
	 * 
	 * <pre>
	 * Page<Warhouse>转换成Page<WarehouseConvert>
	 * </pre>
	 *
	 * @param page
	 * @return Page<WarehouseConvert>
	 */
	public PageInfo<WarehouseConvert> convertToName(PageInfo<Warehouse> page) {// 机构id装换成机构name
		PageInfo<WarehouseConvert> pageRes = new PageInfo<WarehouseConvert>();
		List<Warehouse> listsour = page.getRows();
		List<WarehouseConvert> list = new ArrayList<WarehouseConvert>();
		for (int i = 0; i < listsour.size(); i++) {
			WarehouseConvert whc = new WarehouseConvert();
			whc = this.convertCW(listsour.get(i));
			String whcdisname = this
					.convertDisInfoToName(whc.getDistrictName());
			whc.setDistrictName(whcdisname);
			list.add(whc);
		}
		pageRes.setRows(list);
		pageRes.setPage(page.getPage());
		pageRes.setSize(page.getSize());
		pageRes.setTotal(page.getTotal());
		return pageRes;
	}

	/**
	 * 
	 * <pre>
	 * 根据机构id查找对应的名称
	 * </pre>
	 *
	 * @param disNameId
	 *            机构id
	 * @return 机构名称
	 */
	public String convertDisInfoToName(String disNameId) {
		Long disId = Long.parseLong(disNameId.trim());
		String disNameRes = null;
		List<DistrictInfo> listInfo = this.getDistrictInfo();
		for (int i = 0; i < listInfo.size(); i++) {
			Long did = listInfo.get(i).getId();
			if (disId.equals(did)) {
				disNameRes = listInfo.get(i).getName();
			}
		}
		return disNameRes;
	}

	/**
	 * 
	 * <pre>
	 * 按照名称查找仓库信息列表
	 * </pre>
	 *
	 * @param spName
	 * @param startPage
	 * @param pageSize
	 * @return Page<WarehouseConvert>
	 */
	public PageInfo<WarehouseConvert> getWareHouseList(Warehouse warehouse,
			Integer startPage, Integer pageSize) {
		PageInfo<Warehouse> page = warehouseService.queryPageByObject(warehouse, new PageInfo<Warehouse>(startPage, pageSize));
		return this.convertToName(page);
	}

	/**
	 * 
	 * <pre>
	 * 新增仓库信息
	 * </pre>
	 *
	 * @param Warehouse
	 */
	public void addWareHouse(Warehouse warehouseObj) {

		// 设置创建时间和修改时间
		Date date = new Date();
		warehouseObj.setCreateTime(date);
		warehouseObj.setModifyTime(date);

		warehouseService.insert(warehouseObj);
	}

	/**
	 * 
	 * <pre>
	 * 更新仓库信息
	 * </pre>
	 *
	 * @param Warehouse
	 */
	public void updateWareHouseDO(Warehouse warehouseObj) {
		Date modifyDate = new Date();
		warehouseObj.setModifyTime(modifyDate);// 设置最终修改时间
		warehouseService.update(warehouseObj, false);
	}

	/**
	 * 
	 * <pre>
	 * 根据type获取所有机构信息
	 * </pre>
	 *
	 * @return List<DistrictInfo>
	 */
	public List<DistrictInfo> getDistrictInfo() {
		DistrictInfo districtInfo = new DistrictInfo();
		int type = 3;
		districtInfo.setType(type);
		List<DistrictInfo> list = this.getInfo(districtInfo);
		return list;
	}

	/**
	 * 
	 * <pre>
	 * 根据WarehouseDO的id获取信息
	 * </pre>
	 *
	 * @param Long
	 * @return Warehouse
	 */
	public Warehouse getById(Long id) {
		return warehouseService.queryById(id);
	}

	/**
	 * 
	 * <pre>
	 * 获取地区省信息
	 * </pre>
	 *
	 * @return Map<String, List<DistrictInfo>>
	 */
	public Map<String, List<DistrictInfo>> getProvince() {
		DistrictInfo districtInfo = new DistrictInfo();
		Map<Long, String> map = new HashMap<Long, String>();
//		map.put(248L, "西北");
//		map.put(249L, "华中");
//		map.put(250L, "华东");
//		map.put(252l, "东北");
//		map.put(253L, "华南");
//		map.put(254L, "西南");
//		map.put(255L, "华北");
		List<DistrictInfo> list=this.getDistrictInfo();
		for (DistrictInfo difDO : list) {
			map.put(difDO.getId(), difDO.getName());
		}

		districtInfo.setType(4);
		List<DistrictInfo> temList = districtInfoService.queryByObject(districtInfo);

		Map<String, List<DistrictInfo>> result = new HashMap<String, List<DistrictInfo>>();

		for (DistrictInfo districtInfoObj : temList) {
			String key = map.get(districtInfoObj.getParentId().longValue());
			if (result.containsKey(key)) {
				result.get(key).add(districtInfoObj);
			} else {
				List<DistrictInfo> items = new ArrayList<DistrictInfo>();
				items.add(districtInfoObj);
				result.put(key, items);
			}
		}
		return result;
	}
	
	
	/**
	 * 
	 * <pre>
	 * 初始化保税区类型
	 * </pre>
	 *
	 * @return
	 */
	public List<ClearanceChannels> initClearanceChannelsList(Integer type) {
		//BONDEDAREA  ("保税区",4),
		//OVERSEASMAIL ("境外直发",5);)
		List<ClearanceChannels>  list = new ArrayList<ClearanceChannels>();
		if(type.equals(StorageType.BONDEDAREA.getValue())){
			list = clearanceChannelsService.getAllClearanceChannelsByStatus(1); 
		}else if(type.equals(StorageType.OVERSEASMAIL.getValue())){
			list = clearanceChannelsService.getAllClearanceChannelsByStatus(2); 
		}else if(type.equals(StorageType.COMMON_SEA.getValue())){
			list = clearanceChannelsService.getAllClearanceChannelsByStatus(2);//TODO 状态的意义
		}
		
		return list;
	}
	
	/**
	 * 删除仓库
	 * @param id
	 */
	public void deleteWarehouse(Long id){
		//删除
		warehouseService.deleteById(id);
	}

	/**
	 * 返回出错或成功的信息。
	 * 
	 * @param writer
	 * @param resultMsg
	 * @param successFail
	 */
	public void writeResultMessage(PrintWriter writer, String resultMsg,
			int successFail) {
		JSONStringer stringer = new JSONStringer();
		stringer.object();
		stringer.key("result");
		stringer.value(successFail);
		stringer.key("message");
		stringer.value(resultMsg);
		stringer.endObject();
		writer.print(stringer.toString());
	}


	/**
	 * 保存结果操作消息
	 * 
	 * @param session
	 * @param msg
	 * @param successFail
	 */
//	public void saveResultMessage(HttpSession session, String msg,
//			int successFail) {
//		ResultMessage resultMsg = new ResultMessage(successFail, msg);
//		session.setAttribute(BaseFormController.Message, resultMsg);
//	}

	/**
	 * Warehouse转换成WarehouseConvert
	 * 
	 * @param warehouseObj
	 * @return WarehouseConvert
	 */
	public WarehouseConvert convertCW(Warehouse warehouseObj) {
		WarehouseConvert warehouseConvert = new WarehouseConvert();
		warehouseConvert.setId(warehouseObj.getId());
		warehouseConvert.setAddress(warehouseObj.getAddress());
		warehouseConvert.setCode(warehouseObj.getCode());
		warehouseConvert.setDeliverAddr(warehouseObj.getDeliverAddr());
		warehouseConvert
				.setDistrictName(warehouseObj.getDistrictId().toString());
		warehouseConvert.setLinkman(warehouseObj.getLinkman());
		warehouseConvert.setName(warehouseObj.getName());
		warehouseConvert.setPhone(warehouseObj.getPhone());
		warehouseConvert.setSpName(warehouseObj.getSpName());
		warehouseConvert.setSpId(warehouseObj.getSpId());
		warehouseConvert.setZipCode(warehouseObj.getZipCode());

		return warehouseConvert;
	}

	public class WarehouseConvert {
		/** 主键 */
		private Long id;

		/** 所在地id */
		private String districtName;

		/** 详细地址 */
		private String address;

		/** 仓库名称 */
		private String name;

		/** 仓库编号 */
		private String code;

		/** 供应商名称 */
		private String spName;

		/** 供应商id 0 表示自营仓库 */
		private Long spId;

		/** 邮编 */
		private String zipCode;

		/** 联系人 */
		private String linkman;

		/** 电话 */
		private String phone;

		/** 配送地区，多个地区id用逗号隔开，为0表示全国 */
		private String deliverAddr;

		/** 记录创建时间 */
		private Date createTime;

		/** 最后修改时间 */
		private Date modifyTime;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getDistrictName() {
			return districtName;
		}

		public void setDistrictName(String districtName) {
			this.districtName = districtName;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getSpName() {
			return spName;
		}

		public void setSpName(String spName) {
			this.spName = spName;
		}

		public Long getSpId() {
			return spId;
		}

		public void setSpId(Long spId) {
			this.spId = spId;
		}

		public String getZipCode() {
			return zipCode;
		}

		public void setZipCode(String zipCode) {
			this.zipCode = zipCode;
		}

		public String getLinkman() {
			return linkman;
		}

		public void setLinkman(String linkman) {
			this.linkman = linkman;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getDeliverAddr() {
			return deliverAddr;
		}

		public void setDeliverAddr(String deliverAddr) {
			this.deliverAddr = deliverAddr;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}
	}
}
