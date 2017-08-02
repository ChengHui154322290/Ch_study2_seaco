package com.tp.service.prd;

import java.util.List;
import java.util.Map;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.ExcelSkuDto;
import com.tp.dto.prd.ItemSkuModifyDto;
import com.tp.dto.prd.SkuImportDto;
import com.tp.dto.prd.SkuImportLogDto;
import com.tp.exception.ItemServiceException;
import com.tp.model.prd.ItemImportLog;

/**
 * <pre>
 *   商品导入模块的接口
 *   </br>
 *   	1,导入服务
 *      2,导出服务
 *      3,插入日志
 *      4,日志查询
 *   
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public interface IItemImportService {
	
	/**
	 * 
	 * <pre>
	 * 
	 * </pre>
	 *
	 * @param skuImportLogDto
	 * @throws ItemServiceException
	 */
	SkuImportLogDto saveImportLogDto(SkuImportLogDto skuImportLogDto);
	
	
	/**
	 * 
	 * <pre>
	 *   step1 模板导入sku到库中 
	 *   step2 插入日志信息到商品库中
	 * </pre>
	 *
	 * @param list
	 * @param logId
	 * @param validFailIndexList
	 * @param userID
	 * @throws ItemServiceException
	 */
	List<SkuImportDto> importSku(List<SkuImportDto> list,Long logId,List<ExcelSkuDto> validFailIndexList,String createUser,String importFrom);
	
	/**
	 * 
	 * <pre>
	 *  查询excel导入日志信息
	 * </pre>
	 * @param userId
	 * @param status
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws ItemServiceException
	 */
	SkuImportLogDto querySkuImport(String createUser,int status,int pageNo, int pageSize);
	
	/**
	 * 
	 * <pre>
	 *   查询excel导入错误信息
	 * </pre>
	 *
	 * @param logId
	 * @param status
	 * @return
	 * @throws ItemServiceException
	 */
	SkuImportLogDto querySkuImportById(Long logId,Integer status);
	
	/**
	 * 
	 * <pre>
	 *  保存/更新导入商品模板的信息
	 * </pre>
	 *
	 * @param importLogDO
	 * @return
	 * @throws ItemServiceException
	 */
	Long  saveImportLog(ItemImportLog itemImportLog);

	
	/**
	 * 
	 * <pre>
	 * 	更新状态处理
	 * </pre>
	 *
	 * @param logId
	 * @param status
	 * @return
	 * @throws ItemServiceException
	 */
	Integer updateImportLogStatus(Long logId, int status);	
	
	/**
	 * 
	 * <pre>
	 * 	批量更新商品数据
	 * </pre>
	 *
	 * @param logId
	 * @param status
	 * @return
	 * @throws ItemServiceException
	 */
	ResultInfo<Map<String, String>> importModifySku(List<ItemSkuModifyDto> list);	
}
