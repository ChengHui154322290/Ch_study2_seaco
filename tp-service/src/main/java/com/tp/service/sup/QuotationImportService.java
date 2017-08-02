package com.tp.service.sup;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.dao.prd.ItemInfoDao;
import com.tp.dao.sup.AuditRecordsDao;
import com.tp.dao.sup.QuotationInfoDao;
import com.tp.dao.sup.QuotationProductDao;
import com.tp.datasource.ContextHolder;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.ExcelImportSucessDto;
import com.tp.dto.prd.ExcelSkuDto;
import com.tp.dto.prd.SkuImportDto;
import com.tp.exception.ItemServiceException;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemImportList;
import com.tp.model.prd.ItemImportLog;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemSku;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.Contract;
import com.tp.model.sup.QuotationInfo;
import com.tp.model.sup.QuotationProduct;
import com.tp.model.sup.SupplierInfo;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemManageService;
import com.tp.service.prd.ItemImportService;
import com.tp.service.sup.IQuotationImportService;
import com.tp.util.CodeCreateUtil;

@Service
public class QuotationImportService implements IQuotationImportService {


	private static final Logger  LOGGER = LoggerFactory.getLogger(QuotationImportService.class);

		
	
	@Override
	public List<SkuImportDto> importSku(List<SkuImportDto> list,List<ExcelSkuDto> validFailIndexList,String createUser) {
		//处理日志监控
		//一批执行的时间
		//单个sku的时间
		Long start = System.currentTimeMillis();

		for(SkuImportDto sku : list){
			Long oneSkustart = System.currentTimeMillis();
			ResultInfo<?> resultInfo = new ResultInfo<>();
			try{
				ExcelImportSucessDto excelImportSucessDto = new ExcelImportSucessDto();
				resultInfo = importOneSku(sku,createUser,excelImportSucessDto);
			}
			catch(Exception e){
				resultInfo.setMsg(new FailInfo(e));
			}
			LOGGER.info("导入一个商品，excelIndex:{},耗时： {} ",sku.getExcelIndex(), (System.currentTimeMillis()-oneSkustart));
		}
		
		LOGGER.info("导入商品，耗时: "+ (System.currentTimeMillis()-start));
		return list;
	}
	
	/**
	 * 
	 * <pre>
	 * 	 导入一个sku信息
	 * </pre>
	 *
	 * @param sku
	 * @param userId
	 * @param excelImportSucessDto
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo<Boolean> importOneSku(SkuImportDto sku,String createUser,ExcelImportSucessDto excelImportSucessDto){ 
		ResultInfo<Boolean> resultInfo = new ResultInfo<Boolean>();

		return resultInfo;
	}
	
	
	
	
}
