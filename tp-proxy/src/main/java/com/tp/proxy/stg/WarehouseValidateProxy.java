package com.tp.proxy.stg;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.StorageConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.map.LngLatRequest;
import com.tp.dto.map.LngLatResult;
import com.tp.model.stg.Inventory;
import com.tp.model.stg.Warehouse;
import com.tp.service.map.IMapAPIService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.service.stg.IWarehouseService;
import com.tp.util.StringUtil;

@Service
public class WarehouseValidateProxy {
	@Autowired
	public IWarehouseService warehouseService;
	
	@Autowired
	public IInventoryQueryService inventoryQueryService;
	@Autowired
	private IMapAPIService mapAPIService;
	
	public ResultInfo<String> warehouseValidate(Warehouse wareHouseObj) {
		if (StringUtil.isNullOrEmpty(wareHouseObj.getSpName())) {
			return new ResultInfo<String>(new FailInfo("商家名称不能为空!"));
		} else if (StringUtil.isNullOrEmpty(wareHouseObj.getName())) {
			return new ResultInfo<String>(new FailInfo("仓库名称不能为空!"));
		} else if (StringUtil.isNullOrEmpty(wareHouseObj.getAddress())) {
			return new ResultInfo<String>(new FailInfo("仓库地址不能为空!"));
		} else if (StringUtil.isNullOrEmpty(wareHouseObj.getLinkman())) {
			return new ResultInfo<String>(new FailInfo("联系人不能为空!"));
		} else if (StringUtil.isNullOrEmpty(wareHouseObj.getZipCode())) {
			return new ResultInfo<String>(new FailInfo("邮编不能为空!"));
		} else if (StringUtil.isNullOrEmpty(wareHouseObj.getPhone())) {
			return new ResultInfo<String>(new FailInfo("联系电话不能为空!"));
		}
		if (StringUtil.isNullOrEmpty(wareHouseObj.getSpId())) {
			return new ResultInfo<String>(new FailInfo("供应商编号不能为空！"));
		}
		if (StringUtil.isNullOrEmpty(wareHouseObj.getDeliverAddr())) {
			return new ResultInfo<String>(new FailInfo("配送区域不能为空！"));
		}
		
		if ("1".equals(wareHouseObj.getPutCleanOrder())) { //推送
			if (wareHouseObj.getImportType() == null || wareHouseObj.getImportType() == -1) {
				return new ResultInfo<>(new FailInfo("进口类型不能为空"));
			}
			//保税
			if (wareHouseObj.getImportType() == 1){
				if (StringUtil.isEmpty(wareHouseObj.getWmsName())) {
					return new ResultInfo<>(new FailInfo("对接wms仓库名称不能为空"));
				}			
				if (StringUtil.isEmpty(wareHouseObj.getWmsCode())) {
					return new ResultInfo<>(new FailInfo("对接wms仓库编码不能为空"));
				}
				if (StringUtil.isEmpty(wareHouseObj.getGoodsOwner())) {
					return new ResultInfo<>(new FailInfo("货主名不能为空"));
				}
				if (StringUtil.isEmpty(wareHouseObj.getAccountBookNo())) {
					return new ResultInfo<>(new FailInfo("账册编号不能为空"));
				}
				if (StringUtil.isEmpty(wareHouseObj.getStorageCode())) {
					return new ResultInfo<>(new FailInfo("仓储企业代码不能为空"));
				}
				if (StringUtil.isEmpty(wareHouseObj.getStorageName())) {
					return new ResultInfo<>(new FailInfo("仓储企业名称不能为空"));
				}
			}
			
			if (StringUtil.isEmpty(wareHouseObj.getIoSeaport())) {
				return new ResultInfo<>(new FailInfo("进出口岸代码不能为空"));
			}
			if (StringUtil.isEmpty(wareHouseObj.getDeclSeaport())) {
				return new ResultInfo<>(new FailInfo("申报口岸代码不能为空"));
			}
			if (StringUtil.isEmpty(wareHouseObj.getCustomsField())) {
				return new ResultInfo<>(new FailInfo("码头货场不能为空"));
			}
			if (StringUtil.isEmpty(wareHouseObj.getLogistics())) {
				return new ResultInfo<>(new FailInfo("快递企业不能为空"));
			}
			if (StringUtil.isEmpty(wareHouseObj.getLogisticsCode())) {
				return new ResultInfo<>(new FailInfo("快递企业报关代码不能为空"));
			}
			if (StringUtil.isEmpty(wareHouseObj.getLogisticsName())) {
				return new ResultInfo<>(new FailInfo("快递企业报关名称不能为空"));
			}			
			if (StringUtil.isEmpty(wareHouseObj.getDeclareType())) {
				return new ResultInfo<>(new FailInfo("报关类型不能为空"));
			}
			if (StringUtil.isEmpty(wareHouseObj.getDeclareCompanyCode())) {
				return new ResultInfo<>(new FailInfo("报关企业编号不能为空"));
			}
			if (StringUtil.isEmpty(wareHouseObj.getDeclareCompanyName())) {
				return new ResultInfo<>(new FailInfo("报关企业名称不能为空"));
			}
			if (StringUtil.isEmpty(wareHouseObj.getTrafMode())){
				return new ResultInfo<>(new FailInfo("运输方式不能为空"));
			}
			if (StringUtil.isEmpty(wareHouseObj.getTradeCountry())){
				return new ResultInfo<>(new FailInfo("贸易国（起运地）不能为空"));
			}
			
			//同一供应商的不同仓库不允许存在相同的WMSCode
			if (StringUtil.isNotEmpty(wareHouseObj.getWmsCode())){
				Warehouse queryWarehouse = new Warehouse();
				queryWarehouse.setSpId(wareHouseObj.getSpId());
				queryWarehouse.setWmsCode(wareHouseObj.getWmsCode());
				List<Warehouse> queryResultList = warehouseService.queryByObject(queryWarehouse);
				if (CollectionUtils.isNotEmpty(queryResultList)) {
					if (wareHouseObj.getId() == null) {
						return new ResultInfo<>(new FailInfo("同一供应商已存在WMSCode,不允许重复"));
					}
					for (Warehouse wh : queryResultList) {
						if (!wh.getId().equals(wareHouseObj.getId())) {
							return new ResultInfo<>(new FailInfo("同一供应商已存在WMSCode,不允许重复"));
						}
					}
				}	
			}
		}
		if(StorageConstant.StorageType.FAST.value.equals(wareHouseObj.getType())){
			if(!wareHouseObj.getAddress().matches("^.+市.+$")){
				return new ResultInfo<>(new FailInfo("速购仓库地址请填写城市"));
			}
			LngLatRequest request = new LngLatRequest();
			request.setCity(wareHouseObj.getAddress().split("市")[0]);
			request.setAddress(wareHouseObj.getAddress());
			LngLatResult result = mapAPIService.geocode(request);
			if(result==null){
				return new ResultInfo<>(new FailInfo("速购仓库地址请填写具体地址"));
			}
			if(result.getStatus()!=1 && result.getGeocodes().size()==0){
				return new ResultInfo<>(new FailInfo("根据速购仓库地址获取经纬度出错,请稍后重试")); 
			}
			wareHouseObj.setLngLat(result.getGeocodes().get(0).getLocation());
		}
		return new ResultInfo<String>();
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public ResultInfo<String> deleteWarehouseValidate(Long id) {
		if(null == id)
			return new ResultInfo<String>(new FailInfo("仓库id不能为空!"));
		// valid warehouse stock 
		List<Inventory> list = inventoryQueryService.queryInventoryByWarehouseId(id);
		if(!CollectionUtils.isEmpty(list))
			return new ResultInfo<String>(new FailInfo("仓库已经被关联了库存,不能被删除."));
		return new ResultInfo<String>();		
	}
}
