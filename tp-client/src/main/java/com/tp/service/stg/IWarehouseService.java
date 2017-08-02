package com.tp.service.stg;

import java.util.List;
import java.util.Map;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.WarehouseAreaDto;
import com.tp.model.stg.Warehouse;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 仓库信息表接口
  */
public interface IWarehouseService extends IBaseService<Warehouse>{
	/**
	 * 插入  仓库信息
	 * @param warehouseDO
	 * @return 主键
	 * @throws ServiceException
	 * @author szy
	 */
	Warehouse insert(Warehouse warehouse);

	/**
	 * 根据WarehouseDO对象更新 仓库信息
	 * @param warehouseDO
	 * @param isAllField 是否更新所有字段(false 动态更新字段，true 更新所有字段,传null或false将动态更新，建议动态更新)
	 * @return 更新行数
	 * @throws ServiceException
	 * @author szy
	 */
	int update(Warehouse warehouseDO,boolean isAllField);

	/**
	 * 根据ID删除 仓库信息
	 * @param id
	 * @return 物理删除行
	 * @throws ServiceException
	 * @author szy
	 */
	int deleteById(Long id);


	/**
	 * 根据ID查询 一个 仓库信息
	 * @param id
	 * @return Warehouse
	 * @throws ServiceException
	 * @author szy
	 */
	Warehouse queryById(Number id);
	

	/**
	 * 仓库配送区域校验
	 * @param warehouseAreaDto
	 * @return Map<Long,ResultMessage>
	 * @throws StorageServiceException
	 * @author szy
	 */
	Map<Long,ResultInfo<String>> checkWarehouseArea(WarehouseAreaDto warehouseAreaDto);

	/**
	 * 根据仓库编号获得仓库信息
	 * @param defaultWarehouseCode
	 * @return
	 */
	Warehouse selectByCode(String code); 
	/**
	 * 批量查询仓库信息
	 * @param ids
	 * @return
	 * @throws ServiceException
	 */
	List<Warehouse> queryByIds(List<Long> ids);

	List<Warehouse> queryByCodes(List<String> codes);
}
