package com.tp.service.sup;

import java.util.List;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.ExcelSkuDto;
import com.tp.dto.prd.SkuImportDto;
import com.tp.exception.ItemServiceException;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.QuotationInfo;
import com.tp.service.IBaseService;
/**
  * @author zhs
  * 报价单导入接口
  */
public interface IQuotationImportService {

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
	List<SkuImportDto> importSku(List<SkuImportDto> list,List<ExcelSkuDto> validFailIndexList,String createUser);
	
}
