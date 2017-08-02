/**
 * 
 */
package com.tp.service.stg;

import java.util.List;
import java.util.Map;

import com.tp.common.vo.StorageConstant.App;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.InventoryApply;
import com.tp.dto.stg.OccupyInventoryDto;
import com.tp.model.stg.Inventory;
import com.tp.model.stg.InventoryManageLog;

/**
 * @author szy
 * 库存操作服务类
 */
public interface IInventoryOperService {

	/**
	 * 请求分配商品库存
	 */
	ResultInfo<Boolean> requestInventory(App app, String bizId, String sku, int inventory, Long spId, Long warehouseId, boolean isPreOccupy);
	/**
	 * 请求分配商品库存
	 */
	ResultInfo<Boolean> requestInventory(InventoryApply apply);
	
	/**
	 * 归还业务活动申请的库存,归还活动下所有商品所有库存
	 * @param app
	 * 		应用
	 * @param bizId
	 * 		业务id
	 * @return
	 * 		<pre>
	 * 		分配失败错误码
	 * 		001-还库失败，该活动未申请库存
	 * 		003-调用错误，参数不正确
	 * 		</pre>
	 */
	public ResultInfo<Boolean> backRequestInventory(App app,String bizId);
	
	/**
	 * 归还业务活动申请的库存，归还指定商品所有库存
	 * @param app
	 * 		应用
	 * @param bizId
	 * 		业务id
	 * @param sku
	 * 		商品的sku
	 * @return
	 * 		<pre>
	 * 		分配失败错误码
	 * 		001-还库失败，该商品未申请库存
	 * 		003-调用错误，参数不正确
	 * 		</pre>
	 */
	public ResultInfo<Boolean> backRequestInventoryBySku(App app,String bizId,String sku);


	ResultInfo<Boolean> backRequestInventoryBySkuAndWarehouse(App app, String bizId, String sku,Long warehouseId);
	/**
	 * 归还业务活动申请的库存，归还指定商品的指定库存
	 * @param app
	 * 		应用
	 * @param bizId
	 * 		业务id
	 * @param sku
	 * 		商品的sku
	 * @param invetory
	 * 		归还库存数量 必须大于0 小于现有活动库存数量
	 * @return
	 * 		<pre>
	 * 		分配失败错误码
	 * 		001-还库失败，该商品未申请库存
	 * 		003-调用错误，参数不正确
	 * 		</pre>
	 */
	public ResultInfo<Boolean> backRequestSpecInventoryBySku(App app,String bizId,String sku,int invetory);
	
	/**
	 * 根据sku、商家id、仓库获得 获得商家的某个商品的库存信息，
	 * @param sku
	 * @param spId
	 * @param warehouseId
	 * @return
	 */
	public List<Inventory> selectInventoryInfo(String sku, Long spId, Long warehouseId);
	
	/**
	 * 增加现货库存
	 * @param sku
	 * 		商品sku
	 * @param spId
	 * 		商家id
	 * @param warehouseId
	 * 		仓库id
	 * @param quantity
	 * 		增加数量
	 * @return
	 */
	public ResultInfo<String> increaseRealInventory(String sku,Long spId,Long warehouseId,Integer quantity);
	
	/**
	 * 减少现货库存
	 * @param sku
	 * 		商品sku
	 * @param spId
	 * 		商家id
	 * @param warehouseId
	 * 		仓库id
	 * @param quantity
	 * 		减少数量
	 * @return
	 */
	public ResultInfo<String> reduceRealInventory(String sku,Long spid,Long warehouseId,Integer quantity);
	
	/**
	 * 下单后占用库存，多次调用将增加该订单占用的库存数量（冻结）
	 * @param occupyInventoryDto
	 * 		app,bizId,sku,orderCode,inventory,isPreOccupy,warehouseId均为必传字段
	 * @return
	 */
	public ResultInfo<String> occupyInventory(OccupyInventoryDto occupyInventoryDto);
	
	/**
	 * 下单后占用库存，多次调用将增加该订单占用的库存数量（冻结）
	 * @param inventoryDtos
	 * @return
	 * 		key 为sku，值为message对象
	 * 		<pre>
	 * 		分配失败错误码
	 * 		001-库存不足
	 * 		002-分配库存失败，服务器异常
	 * 		003-调用错误，参数不正确
	 * 		</pre>
	 */
	public Map<String, ResultInfo<String>> batchOccupyInventory(List<OccupyInventoryDto> inventoryDtos);
	
	
	/**
	 * 取消下单库存占用(解冻）
	 * @param orderCode
	 * 		子订单编号
	 * @return
	 * @throws Exception 
	 */
	public ResultInfo<String> unoccupyInventory(Long orderCode);
	
	/**
	 * 批量取消下单库存占用(解冻）
	 * @param orderCode
	 * 		子订单编号
	 * @return
	 * @throws Exception 
	 */
	public ResultInfo<String> batchUnoccupyInventory(List<Long> orderCodes);
	/**
	 * 增加库存记录，入库
	 * @param sku
	 * @param spId
	 * @param warehouseId
	 * @param inventory
	 */
	public ResultInfo<String> addInventory(String sku, Long spId, Long warehouseId,Integer inventory);
	
	/**
	 * 订单发货
	 * @param orderCode
	 * @return
	 */
	ResultInfo<Boolean> reduceInventoryForOrderDelivery(Long orderCode);
	
	/**
	 * 修改库存数
	 * @param inventory
	 * @return
	 */
	Integer updateInventory(Inventory inventory);
	/**
	 * 批量导入库存
	 * @param inventoryList 库存列表
	 * @param inventoryManageLogList 日志列表
	 * @return
	 */
	int importInventory(List<Inventory> inventoryList,List<InventoryManageLog> inventoryManageLogList);
	/**
	 * 批量修改库存
	 * @param inventoryList 库存列表
	 * @param inventoryManageLogList 日志列表
	 * @return
	 */
	int importUpdateInventory(List<Inventory> inventoryList,List<InventoryManageLog> inventoryManageLogList);
	
//	
//	/**
//	 * 请求分配商品库存，如果是为同一个应用的同一个活动申请同一个商品的库存，等于增加申请库存
//	 * @param app
//	 * 		请求来自应用 必传
//	 * @param bizId
//	 * 		业务id 必传
//	 * @param sku
//	 * 		商品sku 必传
//	 * @param inventory
//	 * 		请求库存数量 必传 大于0
//	 * @param spId
//	 * 		供应商id 0-西客商城自营 必传
//	 * 		<pre>
//	 * 			自营商品先采购入库、再分发销售
//	 * 			商家商品发布活动的同时存入库存信息到仓库系统（虚拟库存），同时插入分发销售记录
//	 * 		</pre>
//	 * @param warehouseId
//	 * 		仓库id，自营商品必须传入仓库id
//	 * 		<pre>
//	 * 			自营商品必须指定仓库
//	 * 			商家商品可不指定存入仓库，若不指定则默认为该商家的第一个仓库，或不关联所在仓库信息
//	 * 		</pre>
//	 * @return
//	 * 		<pre>
//	 * 		分配失败错误码
//	 * 		1-库存不足
//	 * 		2-分配库存失败，服务器异常
//	 * 		3-调用错误，参数不正确
//	 * 		4-分配库存失败，商家没有仓库信息
//	 * 		</pre>
//	 */
//	public ResultInfo<String> requestInventory(App app,String bizId,String sku,int inventory,Long spId,Long warehouseId);
//	
//	/**
//	 * 归还业务活动申请的库存,归还活动下所有商品所有库存
//	 * @param app
//	 * 		应用
//	 * @param bizId
//	 * 		业务id
//	 * @return
//	 * 		<pre>
//	 * 		分配失败错误码
//	 * 		001-还库失败，该活动未申请库存
//	 * 		003-调用错误，参数不正确
//	 * 		</pre>
//	 */
//	public ResultInfo<String> backRequestInventory(App app,String bizId);
//	
//	/**
//	 * 归还业务活动申请的库存，归还指定商品所有库存
//	 * @param app
//	 * 		应用
//	 * @param bizId
//	 * 		业务id
//	 * @param sku
//	 * 		商品的sku
//	 * @return
//	 * 		<pre>
//	 * 		分配失败错误码
//	 * 		001-还库失败，该商品未申请库存
//	 * 		003-调用错误，参数不正确
//	 * 		</pre>
//	 */
//	public ResultInfo<String> backRequestInventoryBySku(App app,String bizId,String sku);
//
//
//	ResultInfo<String> backRequestInventoryBySkuAndWarehouse(App app, String bizId, String sku,Long warehouseId);
//	/**
//	 * 归还业务活动申请的库存，归还指定商品的指定库存
//	 * @param app
//	 * 		应用
//	 * @param bizId
//	 * 		业务id
//	 * @param sku
//	 * 		商品的sku
//	 * @param invetory
//	 * 		归还库存数量 必须大于0 小于现有活动库存数量
//	 * @return
//	 * 		<pre>
//	 * 		分配失败错误码
//	 * 		001-还库失败，该商品未申请库存
//	 * 		003-调用错误，参数不正确
//	 * 		</pre>
//	 */
//	public ResultInfo<String> backRequestSpecInventoryBySku(App app,String bizId,String sku,int invetory);
//	
//	/**
//	 * 根据sku、商家id、仓库获得 获得商家的某个商品的库存信息，
//	 * @param sku
//	 * @param spId
//	 * @param warehouseId
//	 * @return
//	 */
//	public List<Inventory> selectInventoryInfo(String sku, Long spId, Long warehouseId);
//	
//	/**
//	 * 增加现货库存
//	 * @param sku
//	 * 		商品sku
//	 * @param spId
//	 * 		商家id
//	 * @param warehouseId
//	 * 		仓库id
//	 * @param quantity
//	 * 		增加数量
//	 * @return
//	 */
//	public ResultInfo<String> increaseRealInventory(String sku,Long spId,Long warehouseId,Integer quantity);
//	
//	/**
//	 * 减少现货库存
//	 * @param sku
//	 * 		商品sku
//	 * @param spId
//	 * 		商家id
//	 * @param warehouseId
//	 * 		仓库id
//	 * @param quantity
//	 * 		减少数量
//	 * @return
//	 */
//	public ResultInfo<String> reduceRealInventory(String sku,Long spid,Long warehouseId,Integer quantity);
//	
//	/**
//	 * 下单后占用库存，多次调用将增加该订单占用的库存数量（冻结）
//	 * @param app
//	 * 		请求来自应用	必传
//	 * @param bizId
//	 * 		业务id	必传
//	 * @param sku
//	 * 		商品sku	必传
//	 * @param inventory
//	 * 		占用库存数量	必传，大于0
//	 * @param orderCode
//	 * 		订单编号 必传
//	 * @return
//	 * 		<pre>
//	 * 		分配失败错误码
//	 * 		001-库存不足
//	 * 		002-分配库存失败，服务器异常
//	 * 		003-调用错误，参数不正确
//	 * 		</pre>
//	 */
//	public ResultInfo<String> occupyInventory(App app,String bizId,String sku,int inventory,String orderCode);
//	
//	/**
//	 * 下单后占用库存，多次调用将增加该订单占用的库存数量（冻结）
//	 * @param inventoryDtos
//	 * @return
//	 * 		key 为sku，值为message对象
//	 * 		<pre>
//	 * 		分配失败错误码
//	 * 		001-库存不足
//	 * 		002-分配库存失败，服务器异常
//	 * 		003-调用错误，参数不正确
//	 * 		</pre>
//	 */
//	public Map<String, ResultInfo<String>> batchOccupyInventory(List<OccupyInventoryDto> inventoryDtos);
//	
//	
//	/**
//	 * 取消下单库存占用(解冻）
//	 * @param orderCode
//	 * 		子订单编号
//	 * @return
//	 * @throws Exception 
//	 */
//	public ResultInfo<String> unoccupyInventory(Long orderCode);
//	
//	/**
//	 * 批量取消下单库存占用(解冻）
//	 * @param orderCode
//	 * 		子订单编号
//	 * @return
//	 * @throws Exception 
//	 */
//	public ResultInfo<String> batchUnoccupyInventory(List<Long> orderCodes);
//	/**
//	 * 增加库存记录，入库
//	 * @param sku
//	 * @param spId
//	 * @param warehouseId
//	 * @param inventory
//	 */
//	public ResultInfo<String> addInventory(String sku, Long spId, Long warehouseId,Integer inventory);
}
