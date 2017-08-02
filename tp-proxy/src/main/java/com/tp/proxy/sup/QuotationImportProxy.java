package com.tp.proxy.sup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.common.vo.supplier.CommonUtil;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.SkuImportDto;
import com.tp.dto.prd.enums.ItemSaleTypeEnum;
import com.tp.dto.prd.excel.ExcelBaseDTO;
import com.tp.dto.sup.excel.ExcelQuotationInfoDTO;
import com.tp.dto.sup.excel.ExcelQuotationProductDTO;
import com.tp.exception.ExcelContentInvalidException;
import com.tp.exception.ExcelParseException;
import com.tp.exception.ExcelRegexpValidFailedException;
import com.tp.exception.ItemServiceException;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemSku;
import com.tp.model.sup.Contract;
import com.tp.model.sup.QuotationInfo;
import com.tp.model.sup.QuotationProduct;
import com.tp.model.sup.SupplierInfo;
import com.tp.proxy.prd.ItemImportProxy;
import com.tp.proxy.usr.UserHandler;
import com.tp.query.prd.ItemQuery;
import com.tp.query.prd.SkuInfoQuery;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.result.prd.SkuInfoResult;
import com.tp.service.prd.IItemDetailService;
import com.tp.service.prd.IItemInfoService;
import com.tp.service.prd.IItemService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.sup.IContractService;
import com.tp.service.sup.IQuotationImportService;
import com.tp.service.sup.IQuotationInfoService;
import com.tp.service.sup.IQuotationProductService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.util.ExcelUtil;
import com.tp.util.StringUtil;
import com.tp.util.ThreadUtil;
/**
 * 报价单导入代理层
 * @author zhs
 *
 */
@Service
public class QuotationImportProxy {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(QuotationImportProxy.class);
	
	
	/** 返回消息key */
	public static final String SUCCESS_KEY = "success";

	/** 返回消息key */
	public static final String MESSAGE_KEY = "message";

	/** 文件大小参数key */
	public static final String FILE_SIZE_KEY = "file_size_key";

	/** 文件参数key */
	public static final String UPLOADED_FILE_KEY = "uploaded_file_key";

	/** 上传excel文件名 */
	private String realFileName = "";
	
	/** excel 中行数 */
	private Integer sumCount = 0;

	private final static String RUN_WORK_KEY = "backend-importQuotation";

	
	private final static String QUOINFO_FAILMAP_KEY = "quotationinfo-failmap";
		
	private final static String QUOINFO_SUM_COUNT_KEY = "quotationinfo-sumcount";

	private final static String QUOINFO_SUCCESS_COUNT_KEY = "quotationinfo-successcount";
	
	private final static String IMPORT_QUOTATION_EXCEL_PATH="template/import_quotation.xls";

	// quotation 信息
	private Map <Long, ExcelQuotationInfoDTO> mvalidFailMap_Quo = new HashMap<Long, ExcelQuotationInfoDTO>();

	// quotation product信息
	private Map <Long, ExcelQuotationProductDTO> mvalidFailMap_QuoPrd = new HashMap<Long, ExcelQuotationProductDTO>();
	
	private Integer miSuccessImportCount = 0;
	
	private Integer mSumCount = 0;
		
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Autowired
	private IQuotationImportService quotationImportService;

    @Autowired
    private QuotationInfoProxy quotationInfoProxy;
    
    @Autowired
    private SupplierItemProxy supplierItemProxy;
    
    @Autowired
    private ISupplierInfoService supplierInfoService;

    @Autowired
    private IContractService contractService;
    
    @Autowired
    private IItemService itemService;

	@Autowired
	private IItemSkuService itemSkuService;
	
	@Autowired
	private IItemDetailService itemDetailService;
	
	@Autowired
	private IItemInfoService itemInfoService;
	
	@Autowired
	IQuotationInfoService quotationInfoService;
	
	@Autowired
	IQuotationProductService quotationProductService;
	
	public Integer getSuccessImportCount() {
		return miSuccessImportCount;
	}
	
	public Integer getSumCount() {
		return mSumCount;
	}
    
    public List < ExcelQuotationInfoDTO> getFailList_ExcelQuotationInfoDTO(){
    	return new ArrayList<ExcelQuotationInfoDTO>( mvalidFailMap_Quo.values() );
    }
    
    public List < ExcelQuotationProductDTO> getFailList_ExcelQuotationProductDTO() {
		return new ArrayList<ExcelQuotationProductDTO>(mvalidFailMap_QuoPrd.values() );
	}
    
	public Map<String, Object> uploadExcelToServer(HttpServletRequest request,
			String fileName,String waveSign,String userName) throws Exception {
		String path = request.getSession().getServletContext().getRealPath("upload");
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		//上传附件并统计excel行数...
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest.getFile(fileName);
			if (null == multipartFile || multipartFile.isEmpty()) {
				LOGGER.info("找不到文件：" + fileName);
				retMap.put(SUCCESS_KEY, false);
				retMap.put(MESSAGE_KEY, "找不到文件：" + fileName);
				return retMap;
			}
			
			long fileSize = multipartFile.getSize();
			// 上传的文件名
			realFileName = multipartFile.getOriginalFilename();
			Map<String, Object> fileSizeCheckMap = checkFileSize(
					fileSize, multipartFile.getOriginalFilename());
			if (!(Boolean) fileSizeCheckMap.get(SUCCESS_KEY)) {
				retMap.put(SUCCESS_KEY, false);
				retMap.put(MESSAGE_KEY, "excel大小不能超过20M");
				return retMap;
			}
			String newName = generateFileName();
			String format = CommonUtil.getFileFormat(multipartFile
					.getOriginalFilename());
			File destFile = new File(path);
			if (!destFile.exists()) {
				destFile.mkdirs();
			}
			
			final File retFile = new File(destFile, newName + "." + format);
			FileUtils.copyInputStreamToFile(multipartFile.getInputStream(),retFile);
			//uniqueFileName = uploadFile(retFile);
				// 验证报价单基本信息
				validateExcel(retFile,1, 0, retMap);
				if(!(Boolean)retMap.get(SUCCESS_KEY)){
					return retMap;
				}
				// 验证报价单商品信息
				validateExcel(retFile, 2, 1, retMap );
				if(!(Boolean)retMap.get(SUCCESS_KEY)){
					return retMap;
				}

				// 判断 quotation 记录的行数
				List<ExcelQuotationInfoDTO> excelList = readExcelList(retFile);
				if(CollectionUtils.isNotEmpty(excelList)){
					LOGGER.info("导入的excel模板总共有:{} 行数据" +excelList.size());
					if(excelList.size()>1000){
						retMap.put(SUCCESS_KEY, false);
						retMap.put(MESSAGE_KEY, "Excel 报价单行数不能超过1000行");
						return retMap;
					}
				}

				try {
//					sysnImportExcel(retFile,userName);//导入
					validAndSaveData(retFile,userName);
					// 删除上传的附件...
					if (retFile.exists()) {
						retFile.delete();
					}										
					setQuoFailMapCache();
					
				}catch(Exception e){
					LOGGER.info("上传Excel记录日志表出错,出错原因: {}" ,e);
					retMap.put(SUCCESS_KEY, false);
					retMap.put(MESSAGE_KEY, "保存Excel到日志失败,请检查导入的excel模板，或者联系管理员");
					return retMap;
				}
				
				if (null == fileName) {
					retMap.put(SUCCESS_KEY, false);
					retMap.put(MESSAGE_KEY, "文件上传到错误！");
					return retMap;
				}
				retMap.put(SUCCESS_KEY, true);
				retMap.put(FILE_SIZE_KEY, fileSize);
				retMap.put(UPLOADED_FILE_KEY, fileName);
		} else {
			LOGGER.info("request请求类型不对！");
		}
		return retMap;
	}
	
	
private boolean setQuoFailMapCache(){
	try{
//		boolean lock = jedisCacheUtil.lock(QUOINFO_FAILMAP_KEY, 10);// 获得锁
//		if(!lock){
//			LOGGER.info("获取锁 {} 失败", QUOINFO_FAILMAP_KEY );
//			return false;
//		}else {
//			LOGGER.info("获取锁 {} 成功", QUOINFO_FAILMAP_KEY );							
//		}
		// 临时保存导入失败列表 
		if( jedisCacheUtil.setCache(QUOINFO_FAILMAP_KEY, getFailList_ExcelQuotationInfoDTO(), 3600) ){
			LOGGER.info("设置 jedisCache {} 成功", QUOINFO_FAILMAP_KEY );								
		}else {							
			LOGGER.info("设置 jedisCache {} 失败", QUOINFO_FAILMAP_KEY );								
		}		
		// 临时保存总导入
		if( jedisCacheUtil.setCache(QUOINFO_SUM_COUNT_KEY, getSumCount(), 3600) ){
			LOGGER.info("设置 jedisCache {} 成功", QUOINFO_SUM_COUNT_KEY );								
		}else {							
			LOGGER.info("设置 jedisCache {} 失败", QUOINFO_SUM_COUNT_KEY );								
		}
		
		if( jedisCacheUtil.setCache(QUOINFO_SUCCESS_COUNT_KEY, getSuccessImportCount(), 3600) ){
			LOGGER.info("设置 jedisCache {} 成功", QUOINFO_SUCCESS_COUNT_KEY );								
		}else {							
			LOGGER.info("设置 jedisCache {} 失败", QUOINFO_SUCCESS_COUNT_KEY );								
		}
				
	}catch(Exception e){
		LOGGER.info( e.getMessage() );
	}
//	finally {
//		jedisCacheUtil.unLock(QUOINFO_FAILMAP_KEY);// 释放锁
//	}
	
	return true;
}

public List < ExcelQuotationInfoDTO>  getQuoFailMapCache(){
	try{
		return  (List < ExcelQuotationInfoDTO>)jedisCacheUtil.getCache(QUOINFO_FAILMAP_KEY) ;
	}catch(Exception e){
		LOGGER.info( e.getMessage() );
	}
	return null;
}


public Integer getQuoSumCountCache() {
	return (Integer)jedisCacheUtil.getCache(QUOINFO_SUM_COUNT_KEY);
}

public Integer getQueSuccessCountCache() {
	return (Integer)jedisCacheUtil.getCache(QUOINFO_SUCCESS_COUNT_KEY);
}



	/**
	 * 生成文件名称 uuid
	 * 
	 * @return
	 */
	public static String generateFileName() {
		return UUID.randomUUID().toString();
	}

	 /**
     * 校验文件大小
     *
     * @param fileSize
     * @param fileName
     * @return
     */
    public static Map<String, Object> checkFileSize(final long fileSize, final String fileName) {
        final Map<String, Object> retMap = new HashMap<String, Object>();
        // excel大小
        if (fileSize > SupplierConstant.MAX_FILE_SIZE.longValue()) {
            retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "excel文件大小不能超过20M。");
            return retMap;
        }
        retMap.put(SupplierConstant.SUCCESS_KEY, true);
        return retMap;
    }
	
	/**
	 * 校验
	 * @param retFile
	 * @param type :　1-报价单基本信息，2-报价单商品信息
	 * @param retMap :　校验结果
	 */
	private void validateExcel(File retFile,int type,int sheetIndex, Map<String, Object> retMap) throws Exception {

		ExcelUtil el = ExcelUtil.readValidateExcel(retFile, sheetIndex,1);
		try{
			retMap.put(SUCCESS_KEY, true); 
			switch(type){
			case 1: 
				List<ExcelQuotationInfoDTO> entitys = null;
				entitys = el.toEntitys(ExcelQuotationInfoDTO.class);
				if( entitys == null || entitys.get(0) ==null || entitys.get(0).getExcelOpStatus() !=Short.parseShort("1") ){
					throw new Exception(  entitys.get(0).getExcelOpmessage() );
				}
				break;
			case 2:
				List<ExcelQuotationProductDTO> prdEntitys = null;
				prdEntitys = el.toEntitys(ExcelQuotationProductDTO.class);				
				if( prdEntitys == null || prdEntitys.get(0) ==null || prdEntitys.get(0).getExcelOpStatus() !=Short.parseShort("1") ){
					throw new Exception( prdEntitys.get(0).getExcelOpmessage() );
				}
				break;
			default: break;
		}
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			retMap.put(SUCCESS_KEY, false);
			retMap.put(MESSAGE_KEY, "校验excel出错: " +  e.getMessage());
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 	读取excel 信息
	 * </pre>
	 * 
	 * @param file
	 * @return
	 * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException 
	 */
	private List<ExcelQuotationInfoDTO> readExcelList(File file) throws org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		long start = System.currentTimeMillis();
		List<ExcelQuotationInfoDTO> entitys = null;
		try {
			ExcelUtil eh = ExcelUtil.readExcel(file, 0);
			sumCount = eh.getDatas().length;
			entitys = eh.toEntitys(ExcelQuotationInfoDTO.class);
		} catch (InvalidFormatException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (ExcelParseException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (ExcelContentInvalidException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (ExcelRegexpValidFailedException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
		}
		LOGGER.info("读取模板封装成list对象用了 ：" + (System.currentTimeMillis() - start));
		return entitys;
	}
	
//	public void sysnImportExcel(final File retFile,final String userName){
//		try {
//			Runnable r = new Runnable() {
//				@Override
//				public void run() {
//					Long start = System.currentTimeMillis();
//					//加锁
//					boolean lock = jedisCacheUtil.lock(RUN_WORK_KEY);// 获得锁
////					LOGGER.info("=查看锁==backend-front-importSku=={} ，logId：{}",lock,logId);
//					if(!lock){
//						//获得锁的次数
//						int count = 0;
//						while(true){
//							 if(count>300){
//								//处理超时，重新上传模板...
////								itemImportService.updateImportLogStatus(logId, 5);//处理超时
//								return;
//							 }
//							 lock = jedisCacheUtil.lock(RUN_WORK_KEY);
//							 //锁的超时时间
//							 jedisCacheUtil.setKeyExpire(RUN_WORK_KEY,300);//5 min
//							 if(lock){
//								 break;
//							 }
//							 count++;
//							 ThreadUtil.sleep(1000L);
//						}
//					}else{
//						jedisCacheUtil.setKeyExpire(RUN_WORK_KEY,300);//5 min
//					}
//
//					try {
//						//正在处理
////						itemImportService.updateImportLogStatus(logId, 2);//正在处理
//						//通用的校验模块，只校验excel的数据类型与长度....
//
//						validAndSaveData(retFile,userName);
//						// 删除上传的附件...
//						if (retFile.exists()) {
//							retFile.delete();
//						}
//					} catch (IllegalAccessException e) {
//						LOGGER.error(e.getMessage(), e);
//					} catch (InvocationTargetException e) {
//						LOGGER.error(e.getMessage(), e);
//					} catch(Exception e){
//						LOGGER.error(e.getMessage());
//					}finally {
//						jedisCacheUtil.unLock(RUN_WORK_KEY);// 释放锁
//						LOGGER.info("导入、解析、校验、保存excel耗时:  {}",
//								((System.currentTimeMillis() - start)));
//					}
//				}
//			};
//			//执行线程....
//			ThreadUtil.excAsync(r,false);
//		} catch (ItemServiceException e) {
//			LOGGER.error("上传的excel批量插入日志表出错,错误信息如下：{}  ", e.getMessage());
//		}finally{
//			jedisCacheUtil.unLock(RUN_WORK_KEY);// 释放锁
//		}
//	}
	
	/**
	 * 
	 * <pre>
	 * 校验并保存quotationinfo信息
	 * </pre>
	 * 
	 * @param retFile
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void validAndSaveData(File retFile, String userName) throws IllegalAccessException,
			InvocationTargetException {
		
		miSuccessImportCount = 0;
		// 读取 quotation 基本信息
		mvalidFailMap_Quo.clear();
		Map <Long, ExcelQuotationInfoDTO> validSucessMap_Quo = new HashMap<Long, ExcelQuotationInfoDTO>();
		Map <Long, ExcelQuotationInfoDTO> validFailMap_Quo = new HashMap<Long, ExcelQuotationInfoDTO>();
		List<ExcelQuotationInfoDTO> excelQuoList = readExcel(retFile);
		splitExcelQuotationList(excelQuoList,validSucessMap_Quo,validFailMap_Quo);
		mvalidFailMap_Quo.putAll(validFailMap_Quo);
		mSumCount = excelQuoList.size();

		// 读取 quotation product 信息
		//mvalidFailMap_QuoPrd.clear();
		Map <Long, ExcelQuotationProductDTO> validSucessMap_QuoPrd = new HashMap<Long, ExcelQuotationProductDTO>();
		Map <Long, ExcelQuotationProductDTO> validFailMap_QuoPrd = new HashMap<Long, ExcelQuotationProductDTO>();
		List<ExcelQuotationProductDTO> excelQuoPrdList = readExcel_QuoProduct(retFile);
		splitExcelQuotationList(excelQuoPrdList, validSucessMap_QuoPrd, validFailMap_QuoPrd);
		//mvalidFailMap_QuoPrd.putAll(validFailMap_QuoPrd);

		Map<Long, ExcelQuotationInfoDTO> insertMap = new HashMap<Long, ExcelQuotationInfoDTO>();
		for ( Map.Entry<Long, ExcelQuotationInfoDTO> quoEntry : validSucessMap_Quo.entrySet() ) {  			
			ExcelQuotationInfoDTO quo = quoEntry.getValue();

			Boolean bSuccess = true;

			// 错误 报价单直接跳过
			if (quo.getExcelOpStatus() != 1) {
				bSuccess = false;
				continue;
			}
			
			// 检测是否存在相同的报价单索引
			if( bExistSameQuotationIndex(quo, validSucessMap_Quo) ){
				bSuccess = false;
				continue;
			}
						
			// supplier 不存在			
			SupplierInfo supplierInfo;
			try{
		        supplierInfo = supplierInfoService.queryById(quo.getSupplierId());
		        if (supplierInfo == null) {
					setQuoErrorMsg( quo,  "*商家ID("+quo.getSupplierId()+")不存在;\n");
					bSuccess = false;
				}				
			} catch(Exception e){
				LOGGER.info("上传Excel记录日志表出错,出错原因: {}" +e.getMessage());
				setQuoErrorMsg(quo, e.getMessage());				
				bSuccess = false;
				continue;
			}

	        //  合同 不存在
			try{
				Contract contractDO = contractService.queryById(quo.getContractId() );	       
				if (contractDO == null) {
					setQuoErrorMsg(quo, "合同ID("+quo.getContractId() + ")不存在;\n");
					bSuccess = false;
				}
			} catch(Exception e){
				LOGGER.info("上传Excel记录日志表出错,出错原因: {}" +e.getMessage());
				setQuoErrorMsg(quo, e.getMessage());				
				bSuccess = false;
				continue;
			}
	        	     			
	        Date startDate = quotationInfoProxy.getDate(quo.getStartDate(), "yyyy/MM/dd");
	        if (startDate == null) {
				setQuoErrorMsg(quo, "起始有效日期 无效  日期格式(yyyy/MM/dd);\n");
				bSuccess = false;
			}
	        Date endDate = quotationInfoProxy.getDate(quo.getEndDate(), "yyyy/MM/dd");
	        if (endDate ==  null) {
				setQuoErrorMsg(quo, "终止有效日期 无效  日期格式(yyyy/MM/dd);\n");
				bSuccess = false;	
			}
	        if (endDate.before(startDate)) {				
				setQuoErrorMsg(quo, "终止时间不能早于起始时间;\n");
				bSuccess = false;	
			}
	        // 判断 是否包含错误product
			for(  Map.Entry<Long, ExcelQuotationProductDTO>  failPrdEntry : validFailMap_QuoPrd.entrySet() ){
				ExcelQuotationProductDTO failPrd = failPrdEntry.getValue();
				if (quo.getQuotationInfoIndex().equals(failPrd.getQuotationInfoIndex() ) ) {
					setQuoErrorMsg( quo,  " 包含错误product行(" + failPrd.getExcelIndex() + ") " + failPrd.getExcelOpmessage() );
					bSuccess = false;
				}
			}
	        	        
			// quotation  基本信息包含错误，直接跳过product分析 
			if (bSuccess == false) {
				continue;
			}
			
			for(  Map.Entry<Long, ExcelQuotationProductDTO>  prdEntry : validSucessMap_QuoPrd.entrySet() ){
				ExcelQuotationProductDTO prd = prdEntry.getValue();
				if (quo.getQuotationInfoIndex().equals(prd.getQuotationInfoIndex() ) ) {
					final String skuCode = prd.getPrdSku();
					final Long supplierId = quo.getSupplierId();
			        final SkuInfoQuery queryInfo = new SkuInfoQuery();
			        if(StringUtil.isNotBlank(skuCode)){
			            queryInfo.setSku(skuCode);
			        }
			        queryInfo.setSupplierId(supplierId);
			        if(SupplierType.isXg(supplierInfo.getSupplierType())){
			        	queryInfo.setSaleType(0);
			        } else {
			        	queryInfo.setSaleType(1);
			        }
			        SkuInfoResult skuInfoRet = selectSkuInfo(queryInfo, prd, quo);
			        ///////////////////////////////////
					
					if (skuInfoRet == null) {
						setQuoErrorMsg( quo,  "SKU("+prd.getPrdSku()+")的商品不存在;\n");
						bSuccess = false;
						continue;
					}
					
					Double dBasicPrice = skuInfoRet.getBasicPrice();
					// 建议最低售价不能高于市场价
//					if( prd.getSalePrice() > dBasicPrice ){
//						setQuoErrorMsg( quo, skuToString(prd.getPrdSku()) +"建议最低售价("+prd.getSalePrice()+") 市场价("+dBasicPrice +") 建议最低售价不能大于市场价;\n");
//						bSuccess = false;
//						continue;
//					}
					// 供货价不能高于市场价
//					if (prd.getSupplyPrice() > dBasicPrice) {
//						setQuoErrorMsg( quo, skuToString(prd.getPrdSku()) +"供货价("+prd.getSupplyPrice()+") 市场价("+dBasicPrice+")供货价不能大于市场价;\n");
//						bSuccess = false;
//						continue;
//					}
					// 平台使用费不能大于100
					if (prd.getCommissionPercent() > 100) {
						setQuoErrorMsg( quo, skuToString(prd.getPrdSku())+"平台使用费("+prd.getCommissionPercent() +")平台使用费不能大于100;\n");
						bSuccess = false;
						 continue;
					}	
					// 检测同一报价单是否存在重复sku行
					if ( !quo.getQuotationProductList().isEmpty() ) {
						for(ExcelQuotationProductDTO itPrd2 : quo.getQuotationProductList()){
							if( itPrd2.getQuotationInfoIndex().equals(prd.getQuotationInfoIndex())  
									&&  itPrd2.getPrdSku().equals(prd.getPrdSku()) ){	// 报价单号相同， sku相同，表示重复sku
								setQuoErrorMsg( quo, "*报价单索引(" + quo.getQuotationInfoIndex()+") 出现重复sku(" + prd.getPrdSku() + " )。\n");
								 bSuccess  = false;
								 break;
							}
						}
						if (bSuccess == false) 
							continue;													
					}					
					
					quo.getQuotationProductList().add(prd);
				}			
			}
			
			 if( quo.getQuotationProductList().isEmpty() ){
				setQuoErrorMsg( quo, "*报价单索引(" + quo.getQuotationInfoIndex()+") 没有产品行。\n");
				 bSuccess  = false;
			 }
			 				 			
			if ( bSuccess ) {
				insertMap.put(quo.getExcelIndex(), quo);
			}			
		}
		
		for( Map.Entry<Long, ExcelQuotationInfoDTO> insertEntry : insertMap.entrySet() ){
			ExcelQuotationInfoDTO insertQuo = insertEntry.getValue();
			Map<String, Object> retMap = quotationInfoProxy.genQuotationInfo( insertQuo );			
	    	if (checkResult(retMap)) {	    		    		
	    		QuotationInfo quotationInfo = (QuotationInfo) retMap.get(SupplierConstant.DATA_KEY);
	    		quotationInfo.setCreateUser( UserHandler.getUser().getUserName() );
	    		quotationInfo.setUpdateUser( UserHandler.getUser().getUserName() );	  
	    		
	    		// 判断数据库里否已存在
	    		if (bExistQuatationInDB(quotationInfo)) {
	    			// 已存在此报价单
	    			ResultInfo<QuotationInfo> ret = quotationInfoProxy.addQuotationProductForImport(quotationInfo, insertQuo);
		    		if (!ret.isSuccess()) {
		    			setQuoErrorMsg(insertQuo, ret.getMsg().getDetailMessage() );
		    		}else {
						++miSuccessImportCount;
					}
				}
	    		else {
	    			// 不存在此报价单
		    		ResultInfo<QuotationInfo> ret = quotationInfoProxy.saveQuotationInfo(quotationInfo);
		    		if (!ret.isSuccess()) {
		    			setQuoErrorMsg(insertQuo, ret.getMsg().getDetailMessage() );
					}else {
						++miSuccessImportCount;
					}
		    		
				}
	    	}
		}		
	}
	
	
	private Boolean bExistSameQuotationIndex(ExcelQuotationInfoDTO quo,  Map<Long, ExcelQuotationInfoDTO> validSucessMap_Quo){		
		// 检测是否存在相同的报价单索引
		for(Map.Entry<Long, ExcelQuotationInfoDTO> intQuoEntry2 :  validSucessMap_Quo.entrySet()) {
			ExcelQuotationInfoDTO  quo2 = intQuoEntry2.getValue();
			if( quo.getExcelIndex() != quo2.getExcelIndex()  && quo.getQuotationInfoIndex() == quo2.getQuotationInfoIndex() ){
				setQuoErrorMsg( quo,  "*报价单索引("+quo.getQuotationInfoIndex()+")有重复。\n");
				setQuoErrorMsg( quo2,  "*报价单索引("+quo2.getQuotationInfoIndex()+")有重复。\n");
				return true;
			}
		}
		return false;
	}
	
	
	private Boolean bExistQuatationInDB( QuotationInfo quotationInfo ){
		QuotationInfo queryQua = new QuotationInfo();

		queryQua.setSupplierId(quotationInfo.getSupplierId());	    		
		queryQua.setQuotationName(quotationInfo.getQuotationName() );
		queryQua.setStartDate(quotationInfo.getStartDate());
		queryQua.setEndDate(quotationInfo.getEndDate());
		queryQua.setId(quotationInfo.getId());
		List<QuotationInfo> quaList = quotationInfoService.queryByObject(queryQua);	    		
		if (quaList != null && !quaList.isEmpty() ) {			
			return true;		    				  			    			
		}		
				
		return false;
	}

	
	
	
	public SkuInfoResult selectSkuInfo(SkuInfoQuery sku, ExcelQuotationProductDTO prdDTO, ExcelQuotationInfoDTO quoDTO)
	{		
		ItemSku itemSku = new ItemSku();
		itemSku.setSku(sku.getSku());
		itemSku.setBarcode(sku.getBarcode());
		itemSku.setSaleType(sku.getSaleType());
		if(sku.getSaleType() == ItemSaleTypeEnum.SEAGOOR.getValue().intValue()){
			itemSku.setSpId(ItemConstant.SUPPLIER_ID);
		}else{
			itemSku.setSpId(sku.getSupplierId());
		}
		
		SkuInfoResult skuInfo = new SkuInfoResult();
		try {
			ItemQuery itemquery = new ItemQuery();
			itemquery.setSku( itemSku.getSku() );
			itemquery.setBarcode( itemSku.getBarcode() );
			itemquery.setSaleType(itemSku.getSaleType());
			itemquery.setSupplierId(itemSku.getSpId());
			List<ItemSku> list = itemService.queryByItemQueryNotEmpty(itemquery);
			if (CollectionUtils.isNotEmpty(list)) {
				itemSku = list.get(0);
				skuInfo.setBarcode(itemSku.getBarcode());
				skuInfo.setCategoryCode(itemSku.getCategoryCode());
				skuInfo.setSku(itemSku.getSku());
				skuInfo.setSkuName(itemSku.getDetailName());
				skuInfo.setBrandId(itemSku.getBrandId());
				skuInfo.setStatus(itemSku.getStatus());
				skuInfo.setUnitId(itemSku.getUnitId());
				skuInfo.setBasicPrice(itemSku.getBasicPrice());
				if (null != itemSku.getDetailId()) {
					ItemDetail detail = itemDetailService.selectByIdFromMaster(itemSku.getDetailId() ); //itemDetailDao.queryById(itemSku.getDetailId());
					if (null != detail) {
						skuInfo.setSpecifications(detail.getSpecifications());
						skuInfo.setCartonSpec(detail.getCartonSpec());
						skuInfo.setWavesSign(detail.getWavesSign());// 是否为海淘商品
						// 规格
						List<ItemDetailSpec> itemDetailSpecList = itemService.queryByDetailId( detail.getId() );						
						skuInfo.setItemDetailSpecList(itemDetailSpecList);
					}
				}
				// 类别
				if (null != itemSku.getItemId()) {
					ItemInfo info = itemInfoService.queryById(itemSku.getItemId());
					if (null != info) {
						skuInfo.setLargeId(info.getLargeId());
						skuInfo.setMediumId(info.getMediumId());
						skuInfo.setSmallId(info.getSmallId());
					}
				}
			} else {
				setQuoErrorMsg(quoDTO, "没有找到相应的itemSku, 当sku("+ itemSku.getSku()+") saleType("+itemSku.getSaleType()+") Sp_id("+itemSku.getSpId() + ")\n");	
				return null;
//				LOGGER.error("通过sku,供应商id查询商品信息,传入参数为： sku:{}" ,sku.toString());
//				throw new ItemServiceException("Sku在系统中不存在");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return skuInfo;
	}

	
	private String skuToString( String sku )
	{
		return "SKU("+sku+") ";
	}
	
	public void setQuoErrorMsg(ExcelQuotationInfoDTO quoDTO, String errMsg){	
		quoDTO.setExcelOpStatus(2);
		quoDTO.setExcelOpmessage(quoDTO.getExcelOpmessage() + errMsg );						
		mvalidFailMap_Quo.put(quoDTO.getExcelIndex(), quoDTO);
	}
	
//	private void setProductErrorMsg(ExcelQuotationProductDTO prdDTO, String errMsg){
//		prdDTO.setExcelOpStatus(2);
//		prdDTO.setExcelOpmessage(prdDTO.getExcelOpmessage() + errMsg );
//		mvalidFailMap_QuoPrd.put(prdDTO.getExcelIndex(), prdDTO );
//	}
	
	
	   /**
     * 
     * <pre>
     * 校验结果
     * </pre>
     *
     * @param resultMap
     * @return
     */
    public boolean checkResult(Map<String,Object> resultMap){
        try {
            return null != resultMap && null != resultMap.get(SupplierConstant.SUCCESS_KEY)
                    && (Boolean)resultMap.get(SupplierConstant.SUCCESS_KEY);
        } catch(Exception e){
            return false;
        }
    }
	
	/**
	 * 
	 * <pre>
	 * 调用商品提供的批量插入的服务
	 * </pre>
	 * 
	 * @param entitys
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public List<SkuImportDto> importQuotationData(List<ExcelQuotationInfoDTO> entitys,Map<Long,ExcelQuotationInfoDTO> validSucessMap,Map<Long,ExcelQuotationInfoDTO> validFailMap,String userName)
			throws IllegalAccessException, InvocationTargetException {
		//当前用户
		return new ArrayList<SkuImportDto>();
		//return quotationImportService.importSku(list,  validFailIndexList, userName);
	}
	/**
	 * 
	 * <pre>
	 * 	读取quotation info excel 信息
	 * </pre>
	 * 
	 * @param file
	 * @return
	 */
	private List<ExcelQuotationInfoDTO> readExcel(File retFile) {
		long start = System.currentTimeMillis();
		List<ExcelQuotationInfoDTO> entitys = null;
		try {
			ExcelUtil el = ExcelUtil.readExcel(retFile, 0);
			entitys = el.toEntitys(ExcelQuotationInfoDTO.class);
		} catch (IOException e) {
			LOGGER.error("", e);
		} catch (ExcelParseException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (ExcelContentInvalidException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (ExcelRegexpValidFailedException e) {
			LOGGER.error(e.getMessage(), e);
		} catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}finally {
			LOGGER.info("读取模板封装成list对象用了 ：{}" + (System.currentTimeMillis() - start));
		}
		return entitys;
	}
	
	/**
	 * 
	 * <pre>
	 * 	读取quotation product excel 信息
	 * </pre>
	 * 
	 * @param file
	 * @return
	 */
	private List<ExcelQuotationProductDTO> readExcel_QuoProduct(File retFile) {
		long start = System.currentTimeMillis();
		List<ExcelQuotationProductDTO> entitys = null;
		try {
			ExcelUtil el = ExcelUtil.readExcel(retFile, 1);
			entitys = el.toEntitys(ExcelQuotationProductDTO.class);
		} catch (IOException e) {
			LOGGER.error("", e);
		} catch (ExcelParseException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (ExcelContentInvalidException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (ExcelRegexpValidFailedException e) {
			LOGGER.error(e.getMessage(), e);
		} catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}finally {
			LOGGER.info("读取模板封装成list对象用了 ：{}" + (System.currentTimeMillis() - start));
		}
		return entitys;
	}

	
		
	/**
	 * 
	 * <pre>
	 * 拆分导入的excel模板 quotation info
	 * </pre>
	 * 
	 * @param excelSkuList
	 */
//	private void splitExcelQuotationList(List<ExcelQuotationInfoDTO> excelQuoList,Map<Long,ExcelQuotationInfoDTO> validSucessMap,Map<Long,ExcelQuotationInfoDTO> validFailMap) {
//		for (ExcelQuotationInfoDTO sku : excelQuoList) {
//			if (sku.getExcelOpStatus() == Short.parseShort("1")) {
//				validSucessMap.put(sku.getExcelIndex(), sku);
//			} else {
//				validFailMap.put(sku.getExcelIndex(), sku);
//			}
//		}
//	}

	/**
	 * 
	 * <pre>
	 * 拆分导入的excel模板  quotation product
	 * </pre>
	 * 
	 * @param excelSkuList
	 */
	private  <T extends ExcelBaseDTO> void splitExcelQuotationList(List<T> excelQuoList,Map<Long,T> validSucessMap,Map<Long,T> validFailMap) {
		for (T sku : excelQuoList) {
			if (sku.getExcelOpStatus() == Short.parseShort("1")) {
				validSucessMap.put(sku.getExcelIndex(), sku);
			} else {
				validFailMap.put(sku.getExcelIndex(), sku);
			}
		}
	}

	/**
	 * 
	 * <pre>
	 * 拆分导入的excel模板  quotation product
	 * </pre>
	 * 
	 * @param excelSkuList
	 */
	private  <T extends ExcelBaseDTO> void splitExcelQuotationList(List<T> excelQuoList,List<T> validSucessList,List<T> validFailList) {
		for (T sku : excelQuoList) {
			if (sku.getExcelOpStatus() == Short.parseShort("1")) {
				validSucessList.add( sku);
			} else {
				validFailList.add(sku);
			}
		}
	}

	
	/**
	 * 
	 * <pre>
	 * 校验excel模板单个校验
	 * </pre>
	 * 
	 * @param entitys
	 */
	private void validExcelData(Map<Long,ExcelQuotationInfoDTO> excelQuoMap,Map<Long,ExcelQuotationInfoDTO> validFailMap) {
		if (null != excelQuoMap && !excelQuoMap.isEmpty()) {
			Set<Long> key = excelQuoMap.keySet();
			
//			List<ForbiddenWords>  forbiddenWordslist = itemValidateProxy.getForbiddenWords() ;
			for (Iterator<Long> it = key.iterator(); it.hasNext();) {
				Long excelIndex = (Long) it.next();
				ExcelQuotationInfoDTO sku = excelQuoMap.get(excelIndex);
				// excel第一步验证后，再与数据库进行匹配一下，判断一些id是否存在，barcode是否唯一
				Long start = System.currentTimeMillis();
				ExcelQuotationInfoDTO excelSkuDTO  = new ExcelQuotationInfoDTO();
//				ExcelSkuDTO excelSkuDTO  = validAndSetOneSku(sku,forbiddenWordslist);
				LOGGER.info("valid one sku  cost : "+ (System.currentTimeMillis() - start));
				if (excelSkuDTO.getExcelOpStatus() != Short.parseShort("1")) {
					it.remove();
					validFailMap.put(excelIndex, excelSkuDTO);
				}
			}
		}
	}
	
	
	/**
	 * 
	 * <pre>
	 *  获取导出模板
	 * </pre>
	 *
	 * @param request
	 * @param response
	 * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException 
	 */
	public void getTemplateDatas(HttpServletRequest request, HttpServletResponse response){
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("status", Constant.ENABLED.YES);
		List<Contract> contList = contractService.queryByParam(params);
		
		String[][] data1 = null;
		if( contList != null &&  !contList.isEmpty() ){
			data1 = new String[contList.size()][5];
			int i = 0;
			for(Contract cont : contList){
//				合同ID	供应商ID	供应商名称	合同编号	合同名称
				data1[i][0] = cont.getId().toString();
				data1[i][1] = cont.getSupplierId().toString();
				data1[i][2] = cont.getSupplierName();
				data1[i][3] = cont.getContractCode();
				data1[i][4] = cont.getContractName();
				++i;
			}											
		}
				
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", -1);
        response.setContentType("application/x-xls");
		response.addHeader("Content-Disposition", "attachment; filename="
				+ "import_quotation.xls");
		String savePath =  QuotationImportProxy.class.getResource("/").getPath()+IMPORT_QUOTATION_EXCEL_PATH;
		File file = new File(savePath);
		byte[] buff;
		ServletOutputStream out = null;
		try{
			Workbook wb  = null ;
			wb = ExcelUtil.writeExcelTemplate_Quotation(file, 2, data1);
			FileOutputStream fileoutputstream = new FileOutputStream(file);
			wb.write(fileoutputstream);
			out = response.getOutputStream();
			buff = FileUtils.readFileToByteArray(file);
			if (null == buff) {
				throw new FileNotFoundException("服务器没有找到import_quotation模板!");
			}
			int len = buff.length;
			out.write(buff, 0, len);
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
		} catch (InvalidFormatException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (org.apache.poi.openxml4j.exceptions.InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	
	
	
}
