package com.tp.proxy.prd;

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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tp.common.vo.BseConstant.DictionaryCode;
import com.tp.common.vo.BseConstant.TaxRateEnum;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.common.vo.supplier.CommonUtil;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.dfsutils.constants.MetaDataKey;
import com.tp.dfsutils.file.TxtFile;
import com.tp.dfsutils.service.DfsService;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.ExcelSkuDto;
import com.tp.dto.prd.ItemDetailImportLogDto;
import com.tp.dto.prd.ItemSkuModifyDto;
import com.tp.dto.prd.SkuImportDto;
import com.tp.dto.prd.SkuImportLogDto;
import com.tp.dto.prd.excel.ExcelEditSkuDTO;
import com.tp.dto.prd.excel.ExcelItemDetailForTransDto;
import com.tp.dto.prd.excel.ExcelListDTO;
import com.tp.dto.prd.excel.ExcelSkuDTO;
import com.tp.dto.prd.excel.ExcelWaveListDTO;
import com.tp.dto.prd.excel.ExcelWaveSkuDTO;
import com.tp.exception.ExcelContentInvalidException;
import com.tp.exception.ExcelParseException;
import com.tp.exception.ExcelRegexpValidFailedException;
import com.tp.exception.ItemServiceException;
import com.tp.model.bse.Brand;
import com.tp.model.bse.Category;
import com.tp.model.bse.ClearanceChannels;
import com.tp.model.bse.DictionaryInfo;
import com.tp.model.bse.DistrictInfo;
import com.tp.model.bse.ForbiddenWords;
import com.tp.model.bse.Spec;
import com.tp.model.bse.SpecGroup;
import com.tp.model.bse.TaxRate;
import com.tp.model.mmp.FreightTemplate;
import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemDetailImport;
import com.tp.model.prd.ItemImportList;
import com.tp.model.prd.ItemImportLog;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemSkuArt;
import com.tp.model.sup.SupplierInfo;
import com.tp.proxy.bse.TaxRateProxy;
import com.tp.proxy.sup.SupplierUserProxy;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.result.bse.SpecGroupResult;
import com.tp.result.sup.SupplierResult;
import com.tp.service.IUploadService;
import com.tp.service.bse.IBrandService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.bse.IClearanceChannelsService;
import com.tp.service.bse.IDictionaryInfoService;
import com.tp.service.bse.ISpecGroupLinkService;
import com.tp.service.bse.ISpecGroupService;
import com.tp.service.bse.ISpecService;
import com.tp.service.mmp.IFreightTemplateService;
import com.tp.service.prd.IItemDetailImportService;
import com.tp.service.prd.IItemDetailService;
import com.tp.service.prd.IItemImportLogService;
import com.tp.service.prd.IItemImportService;
import com.tp.service.prd.IItemManageService;
import com.tp.service.sup.IPurchasingManagementService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.util.BeanUtil;
import com.tp.util.ExcelUtil;
import com.tp.util.StringUtil;
import com.tp.util.ThreadUtil;

/**
 * 
 * <pre>
 * 	 通过excel导入商品信息(spu，prdid，sku)
 * </pre>
 * 
 * @author szy
 * @version 0.0.1
 */
@Service
public class ItemImportProxy {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ItemImportProxy.class);

	 /**
     * 文件最大值
     */
    public static final Long MAX_FILE_SIZE = 20194300L;
    
	/** 返回消息key */
	public static final String SUCCESS_KEY = "success";

	/** 返回消息key */
	public static final String MESSAGE_KEY = "message";

	/** 文件大小参数key */
	public static final String FILE_SIZE_KEY = "file_size_key";

	/** 文件参数key */
	public static final String UPLOADED_FILE_KEY = "uploaded_file_key";

	/** 文件上传者的信息 */
	public static final String UPLOAD_CREATOR = "item_mode";
	
	/** 下载来源 */
	public static final  String DOWN_LOAD_FROM="FROM_SUPPLIER_SYS";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_NAME_KEY = "user_name_key";

	/** excel 中行数 */
	private Integer sumCount = 0;

	/** 密钥 */
	private String secretKey = "";

	/** 上传excel文件名 */
	private String realFileName = "";

	/** 文件服务器上的唯一的文件名 */
	private String uniqueFileName = "";

	/** 导出excel追加表头 */
	private String[] heads = { "执行状态", "错误信息提示" };

	@Autowired
	private DfsService dfsService;

	@Value("${upload.tmp.path}")
	private String uploadTempPath;

	@Autowired
	private IItemManageService itemManageService;

	@Autowired
	private IItemImportService itemImportService;


	/** base，promotion supplier 服务来做模板 */
	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private ISpecGroupService specGroupService;

	@Autowired
	private ISpecService specService;

	@Autowired
	private ISpecGroupLinkService specGroupLinkService;
	
	@Autowired
	private IBrandService brandService;

	@Autowired
	private IDictionaryInfoService dictionaryInfoService;

	@Autowired
	private ISupplierInfoService supplierInfoService;
	
	@Autowired
	private IPurchasingManagementService purchasingManagementService;

	@Autowired
	private IFreightTemplateService freightTemplateService;
	
	@Autowired
	ItemValidateProxy itemValidateProxy;
	
	@Autowired
	private ItemProxy itemProxy;
	
	@Autowired
	private SupplierUserProxy  supplierUserProxy;
	@Autowired
	private TaxRateProxy taxRateProxy;
	
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	@Autowired
	private IUploadService uploadService;
	
	@Autowired
	private IItemDetailService itemDetailService;
	
	@Autowired
	private IItemDetailImportService itemDetailImportService;
	
	@Autowired
	private IItemImportLogService itemImportLogService;
	@Autowired
	private IClearanceChannelsService clearanceChannelsService;
	
	private final static String excelPath = "template/sku-template.xls";
	private final static String hitaoExcelPath="template/hitao-sku-template.xls";
	private final static String RUN_WORK_KEY = "backend-front-importSku";
	private final static String WAVESIGN = "isWave" ;//是否海淘模板
	private final static String RUN_WORK_KEY_NEW = "backend-front-importItemDetail";
	
	
	public Map<String, Object> uploadExcelToServer(HttpServletRequest request,
			String fileName,String waveSign,String userName) throws Exception {
		String path = request.getSession().getServletContext().getRealPath("upload");
		Map<String, Object> retMap = new HashMap<String, Object>();
	    String  importFrom=request.getParameter("importFrom");
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
			if(WAVESIGN.equals(waveSign)){//海淘上传
				validateExcel(retFile,2,retMap);
				if(!(Boolean)retMap.get(SUCCESS_KEY)){
					return retMap;
				}
				List<ExcelWaveListDTO> excelWaveList = readExcelWaveList(retFile);
				if(CollectionUtils.isNotEmpty(excelWaveList)){
					LOGGER.info("导入的excel模板总共有:{} 行数据" +excelWaveList.size());
					if(excelWaveList.size()>1000){
						retMap.put(SUCCESS_KEY, false);
						retMap.put(MESSAGE_KEY, "Excel行数不能超过1000行");
						return retMap;
					}
				}
				List<ItemImportList> importList = new ArrayList<ItemImportList>();
				ItemImportLog importLogDO = initItemImportLog(userName);
				for (ExcelWaveListDTO listDTO : excelWaveList) {
					ItemImportList importListDO = new ItemImportList();
					BeanUtils.copyProperties(importListDO, listDTO);
					importListDO.setOrigin(listDTO.getCountryName());
					importListDO.setOriginId(Long.valueOf(listDTO.getCountryId()));
					importListDO.setIncomeTaxTate(listDTO.getTarrifRateName());
					importListDO.setIncomeTaxTateId(Long.valueOf(listDTO.getTarrifRate()));
					importListDO.setCustomsBackup(listDTO.getBondedAreaAndArticleNumber());
					importListDO.setStatus(listDTO.getExcelOpStatus());
					importListDO.setOpMessage(listDTO.getExcelOpmessage());
					importListDO.setExcelIndex(listDTO.getExcelIndex());
					importList.add(importListDO);
				}
				SkuImportLogDto skuItemImportLogDto = new SkuImportLogDto();
				skuItemImportLogDto.setItemImportLog(importLogDO);
				skuItemImportLogDto.setImportList(importList);
				try {
					final SkuImportLogDto skuImportLogDto =itemImportService.saveImportLogDto(skuItemImportLogDto);
					retMap.put("logId", skuImportLogDto.getItemImportLog().getId());
					sysnImportWaveSkuExcel(retFile,skuImportLogDto.getItemImportLog().getId(),userName,importFrom);//导入
				}catch(Exception e){
					LOGGER.info("上传Excel记录日志表出错,出错原因: {}" +e.getMessage());
					retMap.put(SUCCESS_KEY, false);
					retMap.put(MESSAGE_KEY, fileSizeCheckMap.get(MESSAGE_KEY));
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
			}else{
				validateExcel(retFile,1,retMap);
				if(!(Boolean)retMap.get(SUCCESS_KEY)){
					return retMap;
				}
				// 逻辑删除该用户的上次上传记录
				List<ExcelListDTO> excelList = readExcelList(retFile);
				if(CollectionUtils.isNotEmpty(excelList)){
					LOGGER.info("导入的excel模板总共有:{} 行数据" +excelList.size());
					if(excelList.size()>1000){
						retMap.put(SUCCESS_KEY, false);
						retMap.put(MESSAGE_KEY, "Excel行数不能超过1000行");
						return retMap;
					}
				}
				List<ItemImportList> importList = new ArrayList<ItemImportList>();
				ItemImportLog importLogDO = initItemImportLog(userName);
				for (ExcelListDTO listDTO : excelList) {
					ItemImportList importListDO = new ItemImportList();
					BeanUtils.copyProperties(importListDO, listDTO);
					importListDO.setStatus(listDTO.getExcelOpStatus());
					importListDO.setOpMessage(listDTO.getExcelOpmessage());
					importListDO.setExcelIndex(listDTO.getExcelIndex());
					importList.add(importListDO);
				}
				SkuImportLogDto skuItemImportLogDto = new SkuImportLogDto();
				skuItemImportLogDto.setItemImportLog(importLogDO);
				skuItemImportLogDto.setImportList(importList);
				try {
					skuItemImportLogDto =itemImportService.saveImportLogDto(skuItemImportLogDto);
					retMap.put("logId", skuItemImportLogDto.getItemImportLog().getId());
					sysnImportExcel(retFile,skuItemImportLogDto.getItemImportLog().getId(),userName,importFrom);//导入
				}catch(Exception e){
					LOGGER.info("上传Excel记录日志表出错,出错原因: {}" +e.getMessage());
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
			}
		} else {
			LOGGER.info("request请求类型不对！");
		}
		return retMap;
	}
	
	
	public void sysnImportExcel(final File retFile,final Long logId,final String userName,String importFrom){
		try {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					Long start = System.currentTimeMillis();
					//加锁
					boolean lock = jedisCacheUtil.lock(RUN_WORK_KEY);// 获得锁
					LOGGER.info("=查看锁==backend-front-importSku=={} ，logId：{}",lock,logId);
					if(!lock){
						//获得锁的次数
						int count = 0;
						while(true){
							 if(count>300){
								//处理超时，重新上传模板...
								itemImportService.updateImportLogStatus(logId, 5);//处理超时
								return;
							 }
							 lock = jedisCacheUtil.lock(RUN_WORK_KEY);
							 //锁的超时时间
							 jedisCacheUtil.setKeyExpire(RUN_WORK_KEY,300);//5 min
							 if(lock){
								 break;
							 }
							 count++;
							 ThreadUtil.sleep(1000L);
						}
					}else{
						jedisCacheUtil.setKeyExpire(RUN_WORK_KEY,300);//5 min
					}

					Map <Long, ExcelSkuDTO> validSucessMap = new HashMap<Long, ExcelSkuDTO>();
					Map <Long, ExcelSkuDTO> validFailMap = new HashMap<Long, ExcelSkuDTO>();
					try {
						//正在处理
						itemImportService.updateImportLogStatus(logId, 2);//正在处理
						//通用的校验模块，只校验excel的数据类型与长度....
						
						
						
						validAndSaveData(retFile,logId,validSucessMap,validFailMap,userName,importFrom);
						// 删除上传的附件...
						if (retFile.exists()) {
							retFile.delete();
						}
					} catch (IllegalAccessException e) {
						LOGGER.error(e.getMessage(), e);
					} catch (InvocationTargetException e) {
						LOGGER.error(e.getMessage(), e);
					} catch(Exception e){
						LOGGER.error(e.getMessage());
					}finally {
						jedisCacheUtil.unLock(RUN_WORK_KEY);// 释放锁
						LOGGER.info("导入、解析、校验、保存excel耗时:  {}",
								((System.currentTimeMillis() - start)));
					}
				}
			};
			//执行线程....
			ThreadUtil.excAsync(r,false);
		} catch (ItemServiceException e) {
			jedisCacheUtil.unLock(RUN_WORK_KEY);// 释放锁
			LOGGER.error("上传的excel批量插入日志表出错,错误信息如下：{}  ", e.getMessage());
		}finally{
			jedisCacheUtil.unLock(RUN_WORK_KEY);// 释放锁
		}

	
		
	}
	private void sysnImportWaveSkuExcel(final File retFile,final Long logId,final String userName,String userId){
		try {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					Long start = System.currentTimeMillis();
					//加锁
					boolean lock = jedisCacheUtil.lock(RUN_WORK_KEY);// 获得锁
					LOGGER.info("=查看锁==backend-front-importSku=={} ，logId：{}",lock,logId);
					if(!lock){
						//获得锁的次数
						int count = 0;
						while(true){
							 if(count>300){
								 //处理超时，重新上传模板...
								itemImportService.updateImportLogStatus(logId, 5);//处理超时
								return;
							 }
							 lock = jedisCacheUtil.lock(RUN_WORK_KEY);
							 //锁的超时时间
							 jedisCacheUtil.setKeyExpire(RUN_WORK_KEY,300);//5 min
							 if(lock){
								 break;
							 }
							 count++;
							 ThreadUtil.sleep(500L);
						}
					}else{
						jedisCacheUtil.setKeyExpire(RUN_WORK_KEY,300);//5 min
					}

					Map <Long, ExcelWaveSkuDTO> validSucessMap = new HashMap<Long, ExcelWaveSkuDTO>();
					Map <Long, ExcelWaveSkuDTO> validFailMap = new HashMap<Long, ExcelWaveSkuDTO>();
					try {
						//正在处理
						itemImportService.updateImportLogStatus(logId, 2);//正在处理
						validWaveSkuAndSaveData(retFile,logId,validSucessMap,validFailMap,userName,userId);
						// 删除上传的附件...
						if (retFile.exists()) {
							retFile.delete();
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						LOGGER.error(e.getMessage(), e);
					} catch (InvocationTargetException e) {
						e.printStackTrace();
						LOGGER.error(e.getMessage(), e);
					} catch(Exception e){
						e.printStackTrace();
						LOGGER.error(e.getMessage());
					}finally {
						jedisCacheUtil.unLock(RUN_WORK_KEY);// 释放锁
						LOGGER.info("导入、解析、校验、保存excel耗时{}",
								((System.currentTimeMillis() - start) ));
					}
				}
			};
			//执行线程....
			ThreadUtil.excAsync(r,false);
			jedisCacheUtil.unLock(RUN_WORK_KEY);// 释放锁
		} catch (ItemServiceException e) {
			jedisCacheUtil.unLock(RUN_WORK_KEY);// 释放锁
			LOGGER.error("上传的excel批量插入日志表出错,错误信息如下：{}  ", e.getMessage());
		}
		jedisCacheUtil.unLock(RUN_WORK_KEY);// 释放锁
	
		
	}
	
	/**
	 * step1文件上传 step2校验excel文件 step3插入商品库 step4插入日志文件
	 * 
	 * @param request
	 * @param fileName
	 * @param dest
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> importSkuExcel(HttpServletRequest request,
			String fileName,final String userName,String importFrom) throws Exception {
		Map<String, Object> retMap = new HashMap<String, Object>();
		// File retFile = null;
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
			Map<String, Object> fileSizeCheckMap = CommonUtil.checkFileSize(
					fileSize, multipartFile.getOriginalFilename());
			if (!(Boolean) fileSizeCheckMap.get(SUCCESS_KEY)) {
				retMap.put(SUCCESS_KEY, false);
				retMap.put(MESSAGE_KEY, fileSizeCheckMap.get(MESSAGE_KEY));
				return retMap;
			}
			String newName = generateFileName();
			String format = CommonUtil.getFileFormat(multipartFile
					.getOriginalFilename());
			File destFile = new File(uploadTempPath);
			if (!destFile.exists()) {
				destFile.mkdirs();
			}
			final File retFile = new File(destFile, newName + "." + format);
			FileUtils.copyInputStreamToFile(multipartFile.getInputStream(),retFile);
			uniqueFileName = uploadFile(retFile);
			
			// 逻辑删除该用户的上次上传记录
			List<ExcelListDTO> excelList = readExcelList(retFile);
			List<ItemImportList> importList = new ArrayList<ItemImportList>();
			ItemImportLog importLogDO = initItemImportLog(userName);
			for (ExcelListDTO listDTO : excelList) {
				ItemImportList importListDO = new ItemImportList();
				BeanUtils.copyProperties(importListDO, listDTO);
				importListDO.setStatus(listDTO.getExcelOpStatus());
				importListDO.setOpMessage(listDTO.getExcelOpmessage());
				importListDO.setExcelIndex(listDTO.getExcelIndex());
				importList.add(importListDO);
			}
			SkuImportLogDto skuItemImportLogDto = new SkuImportLogDto();
			skuItemImportLogDto.setItemImportLog(importLogDO);
			skuItemImportLogDto.setImportList(importList);
			try {
				final SkuImportLogDto skuImportLogDto =itemImportService.saveImportLogDto(skuItemImportLogDto);
				final Long logId = skuImportLogDto.getItemImportLog().getId();
				retMap.put("logId", logId);
				// 异步原子操作
				//validSucessMap = new HashMap<Long, ExcelSkuDTO>();
				//validFailMap = new HashMap<Long, ExcelSkuDTO>();
				Runnable r = new Runnable() {
					@Override
					public void run() {
						Long start = System.currentTimeMillis();
						try {
							Map<Long,ExcelSkuDTO> validSucessMap = new HashMap<Long, ExcelSkuDTO>();
							Map<Long,ExcelSkuDTO>validFailMap = new HashMap<Long, ExcelSkuDTO>();
							validAndSaveData(retFile,logId,validSucessMap,validFailMap,userName,importFrom);
							// 删除上传的附件...
							if (retFile.exists()) {
								retFile.delete();
							}
						} catch (IllegalAccessException e) {
							LOGGER.error(e.getMessage(), e);
						} catch (InvocationTargetException e) {
							LOGGER.error(e.getMessage(), e);
						} finally {
							LOGGER.info("导入、解析、校验、保存excel耗时:{}",
									((System.currentTimeMillis() - start)));
						}
					}
				};
				Thread t = new Thread(r);
				t.start();
			} catch (ItemServiceException e) {
				LOGGER.error("上传的excel批量插入日志表出错,错误信息如下：{}  ", e.getMessage());
				retMap.put(SUCCESS_KEY, false);
				retMap.put(MESSAGE_KEY, fileSizeCheckMap.get(MESSAGE_KEY));
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

	/**
	 * 
	 * <pre>
	 * 校验并保存sku信息
	 * </pre>
	 * 
	 * @param retFile
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void validAndSaveData(File retFile,Long logId,Map<Long,ExcelSkuDTO> validSucessMap,Map<Long,ExcelSkuDTO> validFailMap,String userName,String importFrom) throws IllegalAccessException,
			InvocationTargetException {
		ItemImportLog importLogDO = new ItemImportLog();
		importLogDO.setId(logId);
		List<ExcelSkuDTO> excelSkuList = readExcel(retFile);
		// 拆分excel
		splitExcelSkuList(excelSkuList,validSucessMap,validFailMap);
		// 校验sku信息(必填的id是否存在...)
		Long start = System.currentTimeMillis();
		validExcelData(validSucessMap,validFailMap);
		LOGGER.info("valid all skus cost: ",System.currentTimeMillis()-start);
		// 转换校验正确的值
		List<ExcelSkuDTO> list = new ArrayList<ExcelSkuDTO>(
				validSucessMap.values());
		importSkuData(list,logId,validSucessMap,validFailMap,userName,importFrom);
	}
	
	/**
	 * 
	 * <pre>
	 * 校验并保存sku信息
	 * </pre>
	 * 
	 * @param retFile
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void validWaveSkuAndSaveData(File retFile,Long logId,Map<Long,ExcelWaveSkuDTO> validSucessMap,Map<Long,ExcelWaveSkuDTO> validFailMap,String userName,String userId) throws  Exception{
		ItemImportLog importLogDO = new ItemImportLog();
		importLogDO.setId(logId);
		List<ExcelWaveSkuDTO> excelSkuList = readExcelWaveSku(retFile);
		// 拆分excel
		splitExcelWaveSkuList(excelSkuList,validSucessMap,validFailMap);
		// 校验sku信息(必填的id是否存在...)
		validWaveExcelData(validSucessMap,validFailMap);
		// 转换校验正确的值
		List<ExcelWaveSkuDTO> list = new ArrayList<ExcelWaveSkuDTO>(
				validSucessMap.values());
		importWaveSkuData(list,logId,validSucessMap,validFailMap,userName,userId);
	}

	
	/**
	 * 校验
	 * @param retFile
	 * @param type :　1-普通商品，2-海淘商品
	 * @param retMap :　校验结果
	 */
	private void validateExcel(File retFile,int type,Map<String, Object> retMap) throws Exception {
		ExcelUtil el = ExcelUtil.readValidateExcel(retFile, 0,1);
		try{
			retMap.put(SUCCESS_KEY, true); 
			switch(type){
			case 1: 
				List<ExcelSkuDTO> entitys = null;
				entitys = el.toEntitys(ExcelSkuDTO.class);
				break;
			case 2:
				List<ExcelWaveSkuDTO> waveEntitys = null;
				waveEntitys = el.toEntitys(ExcelWaveSkuDTO.class);
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
	 */
	private List<ExcelSkuDTO> readExcel(File retFile) {
		long start = System.currentTimeMillis();
		List<ExcelSkuDTO> entitys = null;
		try {
			ExcelUtil el = ExcelUtil.readExcel(retFile, 0);
			entitys = el.toEntitys(ExcelSkuDTO.class);
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
	 * 	读取excel 信息
	 * </pre>
	 * 
	 * @param file
	 * @return
	 */
	private List<ExcelWaveSkuDTO> readExcelWaveSku(File retFile) {
		long start = System.currentTimeMillis();
		List<ExcelWaveSkuDTO> entitys = null;
		try {
			ExcelUtil el = ExcelUtil.readExcel(retFile, 0);
			entitys = el.toEntitys(ExcelWaveSkuDTO.class);
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
	 * 	读取excel 信息
	 * </pre>
	 * 
	 * @param file
	 * @return
	 * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException 
	 */
	private List<ExcelListDTO> readExcelList(File file) throws org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		long start = System.currentTimeMillis();
		List<ExcelListDTO> entitys = null;
		try {
			ExcelUtil eh = ExcelUtil.readExcel(file, 0);
			sumCount = eh.getDatas().length;
			entitys = eh.toEntitys(ExcelListDTO.class);
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
	
	private List<ExcelItemDetailForTransDto> readExcelListNew(File file) throws org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		long start = System.currentTimeMillis();
		List<ExcelItemDetailForTransDto> entitys = null;
		try {
			ExcelUtil eh = ExcelUtil.readExcel(file, 0);
			sumCount = eh.getDatas().length;
			entitys = eh.toEntitys(ExcelItemDetailForTransDto.class);
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
	
	private List<ExcelWaveListDTO> readExcelWaveList(File file) throws org.apache.poi.openxml4j.exceptions.InvalidFormatException{
		long start = System.currentTimeMillis();
		List<ExcelWaveListDTO> entitys = null;
		try {
			ExcelUtil eh = ExcelUtil.readExcel(file, 0);
			sumCount = eh.getDatas().length;
			entitys = eh.toEntitys(ExcelWaveListDTO.class);
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

	
	private List<ExcelEditSkuDTO> readExcelEditSkuList(File file) throws org.apache.poi.openxml4j.exceptions.InvalidFormatException{
		long start = System.currentTimeMillis();
		List<ExcelEditSkuDTO> entitys = null;
		try {
			ExcelUtil eh = ExcelUtil.readExcel(file, 0);
			sumCount = eh.getDatas().length;
			entitys = eh.toEntitys(ExcelEditSkuDTO.class);
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
	
	/**
	 * 
	 * <pre>
	 * 校验excel模板单个校验
	 * </pre>
	 * 
	 * @param entitys
	 */
	private void validExcelData(Map<Long,ExcelSkuDTO> excelSkuMap,Map<Long,ExcelSkuDTO> validFailMap) {
		if (null != excelSkuMap && !excelSkuMap.isEmpty()) {
			Set<Long> key = excelSkuMap.keySet();
			
			List<ForbiddenWords>  forbiddenWordslist = itemValidateProxy.getForbiddenWords() ;
			for (Iterator<Long> it = key.iterator(); it.hasNext();) {
				Long excelIndex = (Long) it.next();
				ExcelSkuDTO sku = excelSkuMap.get(excelIndex);
				// excel第一步验证后，再与数据库进行匹配一下，判断一些id是否存在，barcode是否唯一
				Long start = System.currentTimeMillis();
				ExcelSkuDTO excelSkuDTO = validAndSetOneSku(sku,forbiddenWordslist);
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
	 * 校验excel模板单个校验
	 * </pre>
	 * 
	 * @param entitys
	 */
	private void validWaveExcelData(Map<Long,ExcelWaveSkuDTO> excelSkuMap,Map<Long,ExcelWaveSkuDTO> validFailMap) throws Exception {
		if (null != excelSkuMap && !excelSkuMap.isEmpty()) {
			Set<Long> key = excelSkuMap.keySet();
			
			List<ForbiddenWords>  forbiddenWordslist = itemValidateProxy.getForbiddenWords() ;
			
			List<ClearanceChannels> list = clearanceChannelsService.getAllClearanceChannelsByStatus(2);
			
			for (Iterator<Long> it = key.iterator(); it.hasNext();) {
				Long excelIndex = (Long) it.next();
				ExcelWaveSkuDTO sku = excelSkuMap.get(excelIndex);
				// excel第一步验证后，再与数据库进行匹配一下，判断一些id是否存在，barcode是否唯一
				ExcelWaveSkuDTO excelSkuDTO = validWaveAndSetOneSku(sku,forbiddenWordslist,list);
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
	 *  校验并且获取值
	 * </pre>
	 * 
	 * @param sku
	 * @return
	 */
	private ExcelSkuDTO validAndSetOneSku(ExcelSkuDTO sku,List<ForbiddenWords>  forbiddenWordslist) {
		
		Long start = System.currentTimeMillis();
		Long categoryId = sku.getSmallId();
		List<SpecGroupResult>  specGroupList = specGroupService.getSpecGroupResultByCategoryId(categoryId);
		LOGGER.info("valid base getSpecGroupResultByCategoryId : " + (System.currentTimeMillis()-start));
		Map<Long,ArrayList<Long>> specIds = new HashMap<Long,ArrayList<Long>>();
		if(CollectionUtils.isNotEmpty(specGroupList)){
			for(SpecGroupResult specGroupResult : specGroupList){
				ArrayList<Long> ids = new ArrayList<Long>();
				if(CollectionUtils.isNotEmpty(specGroupResult.getSpecDoList())){
					for(Spec specDO : specGroupResult.getSpecDoList()){
						ids.add(specDO.getId());
					}
				}
				specIds.put(specGroupResult.getSpecGroup().getId(),ids);
			}
		}
		if(StringUtils.isBlank(sku.getBarcode())){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*条形码不能为空。\n");
		}
		Category c3 = validaSkuInfo(sku, forbiddenWordslist);
		//均码
		if((null==sku.getSpec1GroupId())&&(null==sku.getSpec2GroupId())&&(null==sku.getSpec3GroupId())
				&&(null==sku.getSpec1Id())&&(null==sku.getSpec2Id())&&(null==sku.getSpec3Id())){
			LOGGER.info("导入的商品为均码规格");
		}else{
			boolean checkSpec1 = checkSpecInCategory(sku.getSpec1GroupId(),sku.getSpec1Id(),specIds);
			boolean checkSpec2 = checkSpecInCategory(sku.getSpec2GroupId(),sku.getSpec2Id(),specIds);
			boolean checkSpec3 = checkSpecInCategory(sku.getSpec3GroupId(),sku.getSpec3Id(),specIds);
			if(!checkSpec1){
				sku.setExcelOpStatus(2);
				sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组1不在分类中;\n");	
			}
			if(!checkSpec2){
				sku.setExcelOpStatus(2);
				sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组2不在分类中;\n");	
			}
			if(!checkSpec3){
				sku.setExcelOpStatus(2);
				sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组3不在分类中;\n");	
			}
		}
		Map<Long,Long> specMap = new HashMap<Long,Long>();
		if(null!=specIds&& !specIds.isEmpty()){
			if (null != sku.getSpec1GroupId()) {
				SpecGroup specGroupDO = specGroupService.queryById(sku.getSpec1GroupId());
				if (null == specGroupDO ||(null!=specGroupDO&& specGroupDO.getStatus().equals(false))) {		
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组在系统中不存在或失效;\n");
				}
			}
			if (null != sku.getSpec2GroupId()) {
				SpecGroup specGroupDO = specGroupService.queryById(sku.getSpec2GroupId());
				if (null == specGroupDO ||(null!=specGroupDO&& specGroupDO.getStatus().equals(false))) {		
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组在系统中不存在或失效;\n");
				}
			}
			if (null != sku.getSpec3GroupId()) {
				SpecGroup specGroupDO = specGroupService.queryById(sku.getSpec3GroupId());
				if (null == specGroupDO ||(null!=specGroupDO&& specGroupDO.getStatus().equals(false))) {		
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组在系统中不存在或失效;\n");
				}
			}
			

			if (null != sku.getSpec1Id()) {
				Spec specDO = specService.queryById(sku.getSpec1Id());
				if (null == specDO ||(null!=specDO&& specDO.getStatus().equals(false))) {		
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格在系统中不存在或失效;\n");
				}
			}
			if (null != sku.getSpec2Id()) {
				Spec specDO = specService.queryById(sku.getSpec2Id());
				if (null == specDO ||(null!=specDO&& specDO.getStatus().equals(false))) {		
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格在系统中不存在或失效;\n");
				}
			}
			if (null != sku.getSpec3Id()) {
				Spec specDO = specService.queryById(sku.getSpec3Id());
				if (null == specDO ||(null!=specDO&& specDO.getStatus().equals(false))) {
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格在系统中不存在或失效;\n");
				}
			}
			
			
			if(null!=sku.getSpec1GroupId()){
				if(specIds.containsKey(sku.getSpec1GroupId())){
					List<Long> ids = specIds.get(sku.getSpec1GroupId());
					if(!ids.contains(sku.getSpec1Id())){
						sku.setExcelOpStatus(2);
						sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组1与规格1不匹配;\n");
					}
				}
				else{
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组1在系统中不存在;\n");
				}
			}
			if(null!=sku.getSpec2GroupId()){
				if(specIds.containsKey(sku.getSpec2GroupId())){
					List<Long> ids = specIds.get(sku.getSpec2GroupId());
					if(!ids.contains(sku.getSpec2Id())){
						sku.setExcelOpStatus(2);
						sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组2与规格2不匹配;\n");
					}
				}
				else{
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组2在系统中不存在;\n");
				}
			}
			if(null!=sku.getSpec3GroupId()){
				if(specIds.containsKey(sku.getSpec3GroupId())){
					List<Long> ids = specIds.get(sku.getSpec3GroupId());
					if(!ids.contains(sku.getSpec3Id())){
						sku.setExcelOpStatus(2);
						sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组3与规格3不匹配;\n");
					}
				}
				else{
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组3在系统中不存在;\n");
				}
			}
			
			if(null!=sku.getSpec1Id()&&null!=sku.getSpec2Id()
					&&null!=sku.getSpec1GroupId()&&null!=sku.getSpec2GroupId()){
				
				if((sku.getSpec1GroupId().equals(sku.getSpec2GroupId())&&sku.getSpec1Id().equals(sku.getSpec2Id()))){
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格1与规格2重复了;\n");
				}
			}
			
			if(null!=sku.getSpec1Id()&&null!=sku.getSpec3Id()
					&&null!=sku.getSpec1GroupId()&&null!=sku.getSpec3GroupId()){
				if((sku.getSpec1GroupId().equals(sku.getSpec3GroupId())&&sku.getSpec1Id().equals(sku.getSpec3Id()))){
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格1与规格3重复了;\n");
				}
			}
			
			if(null!=sku.getSpec2Id()&&null!=sku.getSpec3Id()
					&&null!=sku.getSpec2GroupId()&&null!=sku.getSpec3GroupId()){
				if((sku.getSpec2GroupId().equals(sku.getSpec3GroupId())&&sku.getSpec2Id().equals(sku.getSpec3Id()))){
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格2与规格3重复了;\n");
				}
			}
			
			if(null!=sku.getSpec1GroupId()&&null!=sku.getSpec1Id()){
				specMap.put(sku.getSpec1GroupId(), sku.getSpec1Id());
			}
			if(null!=sku.getSpec2GroupId()&&null!=sku.getSpec2Id()){
				specMap.put(sku.getSpec2GroupId(), sku.getSpec2Id());
			}
			if(null!=sku.getSpec3GroupId()&&null!=sku.getSpec3Id()){
				specMap.put(sku.getSpec3GroupId(), sku.getSpec3Id());
			}
			
		}else{
			// 校验sku的规格
			if(null!=sku.getSpec1GroupId()&&null!=sku.getSpec1Id()){
				sku.setExcelOpStatus(2);
				LOGGER.error("系统中不存在规格组与规格");
				sku.setExcelOpmessage(sku.getExcelOpmessage() + ". 系统中不存在规格组与规格;\n");
			}
		}
		
		LOGGER.info("valid base : " + (System.currentTimeMillis()-start));
		start = System.currentTimeMillis();
		// step2-促销
		// 运费模板
		FreightTemplate freightTemplateDO = freightTemplateService.queryById(sku
				.getFreightTemplateId().longValue());
		if (null == freightTemplateDO||
			 (null!= freightTemplateDO && (freightTemplateDO.getIsDelete().equals(true)||!freightTemplateDO.getCalculateMode().equals(ItemConstant.SINGLE_FREIGHTTEMPLATE_STATUS)))) {
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "促销系统中找不到运费模板;\n");
		}
		// step3-供应商是否存在
		if(0==sku.getSupplierId().longValue()){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "供应商的id为0是西客商城的编号，不能正常导入，必须指定一个供应商，如果没有，需要在供应商模块里录入并审核;\n");
		}else{
			SupplierInfo supplierDO = supplierInfoService.queryById(sku.getSupplierId());
			if (null == supplierDO ) {
				sku.setExcelOpStatus(2);
				sku.setExcelOpmessage(sku.getExcelOpmessage() + "供应商系统中找不到此供应商;\n");
			}else if(null!= supplierDO && (supplierDO.getAuditStatus().intValue()!= AuditStatus.THROUGH.getStatus().intValue() 
					|| (supplierDO.getSupplierType().equals(SupplierType.MAIN.getValue())||"".equals(supplierDO.getSupplierType())))){
				sku.setExcelOpStatus(2);
				sku.setExcelOpmessage(sku.getExcelOpmessage() + "供应商类型不对;\n");
			}else{
				String supplierType = supplierDO.getSupplierType();
				//自营西客商城
				//  PURCHASE("自营", "Purchase"),
				//    SELL("代销", "sell"),
				//    ASSOCIATE("联营", "Associate"),
				if(StringUtil.isNoneBlank(supplierType)){
					sku.setSupplierName(supplierDO.getName());//设置名称
					if(supplierType.equals(SupplierType.SELL.getValue())){
						sku.setSaleType(0);
					}else if(supplierType.equals(SupplierType.PURCHASE.getValue())){
						sku.setSaleType(0);
					}else if(supplierType.equals(SupplierType.ASSOCIATE.getValue())){
						sku.setSaleType(1);
					}
					
				}
			}
		}
		
		LOGGER.info("valid supplier : " + (System.currentTimeMillis()-start));
		/***
		 * 正常商品 服务商品 二手商品 报废商品
		 ***/
		if (sku.getItemTypeStr().equals("正常商品")) {
			sku.setItemType(1);
		} else if (sku.getItemTypeStr().equals("服务商品")) {
			sku.setItemType(2);
		} else if (sku.getItemTypeStr().equals("二手商品")) {
			sku.setItemType(3);
		} else if (sku.getItemTypeStr().equals("报废商品")) {
			sku.setItemType(4);
		} else {
			sku.setExcelOpStatus(2);
			LOGGER.error("商品类型设置不对");
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "商品类型设置不对;\n");
		}
		/***
		 * 是否有效期管理 是否有效期管理:1 是，2-否 默认1 是否海淘商品 是否海淘商品,1-否，2-是，默认1
		 ***/
		sku.setWavesSign(1);
		/*
		if ("是".equals(sku.getWavesSignStr())) {
			sku.setWavesSign(2);
		} else if ("否".equals(sku.getWavesSignStr())) {
			sku.setWavesSign(1);
		} else {
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "是否海淘商品设置不对;\n");
		}*/
		
		if ("是".equals(sku.getExpSignStr())) {
			sku.setExpSign(1);
		} else if ("否".equals(sku.getExpSignStr())) {
			sku.setExpSign(2);
		} else {
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "是否有效期设置不对;\n");
		}
		start = System.currentTimeMillis();
		if(StringUtils.isNotBlank(sku.getBarcode())&&sku.getExcelOpStatus()!=2){
			try {
				boolean checkBarcode = false;
				ItemInfo infoDO = itemManageService.checkSpuExsit(sku.getSmallId(), sku.getBrandId(), sku.getUnitId(), sku.getSpuName().trim(),null);
				if (null==infoDO) {
					checkBarcode = itemManageService.checkBarcodeExsit(sku.getBarcode(), null,null);
					if(!checkBarcode){
						sku.setExcelOpStatus(2);
						sku.setExcelOpmessage(sku.getExcelOpmessage() + "商品系统中已经存在此条码;\n");
					}
				}else{
					sku.setItemId(infoDO.getId());
					checkBarcode = itemManageService.checkBarcodeExsit(sku.getBarcode(), null,infoDO.getId());
					//prdid规格校验
					int flag = itemManageService.checkPrdidSpec(infoDO.getId(),  specMap);
					
					if(checkBarcode){//不存在 
						if(flag!=1){
							sku.setExcelOpStatus(2);
							sku.setExcelOpmessage(sku.getExcelOpmessage() + "Spu已经存在，规格在系统中不匹配;\n");
						}
					}else{
						if(flag==4){
							checkBarcode = itemManageService.checkBarcodeExsitInSku(null,sku.getBarcode(), null, sku.getSupplierId(), sku.getSaleType());
							if (!checkBarcode) {
								sku.setExcelOpStatus(2);
								sku.setExcelOpmessage(sku.getExcelOpmessage() + "商品系统中已经存在此条码:"
										+sku.getBarcode()+",供应商:"+sku.getSupplierName()+";\n");
							}
						}else{
							sku.setExcelOpStatus(2);
							sku.setExcelOpmessage(sku.getExcelOpmessage() + "商品系统中已经存在此条码在prdid中，但是规格不一样;\n");
						}
					}
				}
			} catch (ItemServiceException e) {
				sku.setExcelOpStatus(2);
				LOGGER.error(e.getMessage(), e);
				sku.setExcelOpmessage(sku.getExcelOpmessage() + e.getMessage());
			}
		}
		
		LOGGER.info("valid item : " + (System.currentTimeMillis()-start));
		//赋值code
		if(null!=c3){
			sku.setCategoryCode(c3.getCode());
		}
		return sku;
	}
	
	/**
	 * 
	 * <pre>
	 *  校验并且获取值
	 * </pre>
	 * 
	 * @param sku
	 * @return
	 */
	private ExcelWaveSkuDTO validWaveAndSetOneSku(ExcelWaveSkuDTO sku,List<ForbiddenWords>  forbiddenWordslist,List<ClearanceChannels> clearanceChannelList) throws Exception {
		
		Long categoryId = sku.getSmallId();
		List<SpecGroupResult>  specGroupList = specGroupService.getSpecGroupResultByCategoryId(categoryId);
		Map<Long,ArrayList<Long>> specIds = new HashMap<Long,ArrayList<Long>>();
		if(CollectionUtils.isNotEmpty(specGroupList)){
			for(SpecGroupResult specGroupResult : specGroupList){
				ArrayList<Long> ids = new ArrayList<Long>();
				if(CollectionUtils.isNotEmpty(specGroupResult.getSpecDoList())){
					for(Spec specDO : specGroupResult.getSpecDoList()){
						ids.add(specDO.getId());
					}
				}
				specIds.put(specGroupResult.getSpecGroup().getId(),ids);
			}
		}
		if(StringUtils.isBlank(sku.getBarcode())){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*条形码不能为空。\n");
		}
		Category c3 = validaSkuInfo(sku, forbiddenWordslist);
		//均码
		if((null==sku.getSpec1GroupId())&&(null==sku.getSpec2GroupId())&&(null==sku.getSpec3GroupId())
				&&(null==sku.getSpec1Id())&&(null==sku.getSpec2Id())&&(null==sku.getSpec3Id())){
			LOGGER.info("导入的商品为均码规格");
		}else{
			boolean checkSpec1 = checkSpecInCategory(sku.getSpec1GroupId(),sku.getSpec1Id(),specIds);
			boolean checkSpec2 = checkSpecInCategory(sku.getSpec2GroupId(),sku.getSpec2Id(),specIds);
			boolean checkSpec3 = checkSpecInCategory(sku.getSpec3GroupId(),sku.getSpec3Id(),specIds);
			if(!checkSpec1){
				sku.setExcelOpStatus(2);
				sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组1不在分类中;\n");	
			}
			if(!checkSpec2){
				sku.setExcelOpStatus(2);
				sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组2不在分类中;\n");	
			}
			if(!checkSpec3){
				sku.setExcelOpStatus(2);
				sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组3不在分类中;\n");	
			}
		}
		Map<Long,Long> specMap = new HashMap<Long,Long>();
		if(null!=specIds&& !specIds.isEmpty()){
			if (null != sku.getSpec1GroupId()) {
				SpecGroup specGroupDO = specGroupService.queryById(sku.getSpec1GroupId());
				if (null == specGroupDO ||(null!=specGroupDO&& specGroupDO.getStatus().equals(false))) {		
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组在系统中不存在或失效;\n");
				}
			}
			if (null != sku.getSpec2GroupId()) {
				SpecGroup specGroupDO = specGroupService.queryById(sku.getSpec2GroupId());
				if (null == specGroupDO ||(null!=specGroupDO&& specGroupDO.getStatus().equals(false))) {		
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组在系统中不存在或失效;\n");
				}
			}
			if (null != sku.getSpec3GroupId()) {
				SpecGroup specGroupDO = specGroupService.queryById(sku.getSpec3GroupId());
				if (null == specGroupDO ||(null!=specGroupDO&& specGroupDO.getStatus().equals(false))) {		
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组在系统中不存在或失效;\n");
				}
			}
			

			if (null != sku.getSpec1Id()) {
				Spec specDO = specService.queryById(sku.getSpec1Id());
				if (null == specDO ||(null!=specDO&& specDO.getStatus().equals(false))) {		
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格在系统中不存在或失效;\n");
				}
			}
			if (null != sku.getSpec2Id()) {
				Spec specDO = specService.queryById(sku.getSpec2Id());
				if (null == specDO ||(null!=specDO&& specDO.getStatus().equals(false))) {		
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格在系统中不存在或失效;\n");
				}
			}
			if (null != sku.getSpec3Id()) {
				Spec specDO = specService.queryById(sku.getSpec3Id());
				if (null == specDO ||(null!=specDO&& specDO.getStatus().equals(false))) {
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格在系统中不存在或失效;\n");
				}
			}
			
			
			if(null!=sku.getSpec1GroupId()){
				if(specIds.containsKey(sku.getSpec1GroupId())){
					List<Long> ids = specIds.get(sku.getSpec1GroupId());
					if(!ids.contains(sku.getSpec1Id())){
						sku.setExcelOpStatus(2);
						sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组1与规格1不匹配;\n");
					}
				}
				else{
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组1在系统中不存在;\n");
				}
			}
			if(null!=sku.getSpec2GroupId()){
				if(specIds.containsKey(sku.getSpec2GroupId())){
					List<Long> ids = specIds.get(sku.getSpec2GroupId());
					if(!ids.contains(sku.getSpec2Id())){
						sku.setExcelOpStatus(2);
						sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组2与规格2不匹配;\n");
					}
				}
				else{
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组2在系统中不存在;\n");
				}
			}
			if(null!=sku.getSpec3GroupId()){
				if(specIds.containsKey(sku.getSpec3GroupId())){
					List<Long> ids = specIds.get(sku.getSpec3GroupId());
					if(!ids.contains(sku.getSpec3Id())){
						sku.setExcelOpStatus(2);
						sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组3与规格3不匹配;\n");
					}
				}
				else{
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格组3在系统中不存在;\n");
				}
			}
			
			if(null!=sku.getSpec1Id()&&null!=sku.getSpec2Id()
					&&null!=sku.getSpec1GroupId()&&null!=sku.getSpec2GroupId()){
				
				if((sku.getSpec1GroupId().equals(sku.getSpec2GroupId())&&sku.getSpec1Id().equals(sku.getSpec2Id()))){
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格1与规格2重复了;\n");
				}
			}
			
			if(null!=sku.getSpec1Id()&&null!=sku.getSpec3Id()
					&&null!=sku.getSpec1GroupId()&&null!=sku.getSpec3GroupId()){
				if((sku.getSpec1GroupId().equals(sku.getSpec3GroupId())&&sku.getSpec1Id().equals(sku.getSpec3Id()))){
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格1与规格3重复了;\n");
				}
			}
			
			if(null!=sku.getSpec2Id()&&null!=sku.getSpec3Id()
					&&null!=sku.getSpec2GroupId()&&null!=sku.getSpec3GroupId()){
				if((sku.getSpec2GroupId().equals(sku.getSpec3GroupId())&&sku.getSpec2Id().equals(sku.getSpec3Id()))){
					sku.setExcelOpStatus(2);
					sku.setExcelOpmessage(sku.getExcelOpmessage() + "规格2与规格3重复了;\n");
				}
			}
			
			if(null!=sku.getSpec1GroupId()&&null!=sku.getSpec1Id()){
				specMap.put(sku.getSpec1GroupId(), sku.getSpec1Id());
			}
			if(null!=sku.getSpec2GroupId()&&null!=sku.getSpec2Id()){
				specMap.put(sku.getSpec2GroupId(), sku.getSpec2Id());
			}
			if(null!=sku.getSpec3GroupId()&&null!=sku.getSpec3Id()){
				specMap.put(sku.getSpec3GroupId(), sku.getSpec3Id());
			}
			
		}else{
			// 校验sku的规格
			if(null!=sku.getSpec1GroupId()&&null!=sku.getSpec1Id()){
				sku.setExcelOpStatus(2);
				LOGGER.error("系统中不存在规格组与规格");
				sku.setExcelOpmessage(sku.getExcelOpmessage() + ". 系统中不存在规格组与规格;\n");
			}
		}

		// step2-促销
		// 运费模板
		FreightTemplate freightTemplateDO = freightTemplateService.queryById(sku
				.getFreightTemplateId().longValue());
		if (null == freightTemplateDO||
			 (null!= freightTemplateDO && (freightTemplateDO.getIsDelete().equals(true)||!freightTemplateDO.getCalculateMode().equals(ItemConstant.SINGLE_FREIGHTTEMPLATE_STATUS)))) {
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "促销系统中找不到运费模板;\n");
		}
		// step3-供应商是否存在
		if(0==sku.getSupplierId().longValue()){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "供应商的id为0是西客商城的编号，不能正常导入，必须指定一个供应商，如果没有，需要在供应商模块里录入并审核;\n");
		}else{
			SupplierInfo supplierDO = supplierInfoService.queryById(sku.getSupplierId());
			if (null == supplierDO ) {
				sku.setExcelOpStatus(2);
				sku.setExcelOpmessage(sku.getExcelOpmessage() + "供应商系统中找不到此供应商;\n");
			}else if(null!= supplierDO && (supplierDO.getAuditStatus().intValue()!= AuditStatus.THROUGH.getStatus().intValue() 
					|| (supplierDO.getSupplierType().equals(SupplierType.MAIN.getValue())||"".equals(supplierDO.getSupplierType())))){
				sku.setExcelOpStatus(2);
				sku.setExcelOpmessage(sku.getExcelOpmessage() + "供应商类型不对;\n");
			}else{
				String supplierType = supplierDO.getSupplierType();
				//自营西客商城
				//  PURCHASE("自营", "Purchase"),
				//    SELL("代销", "sell"),
				//    ASSOCIATE("联营", "Associate"),
				if(StringUtil.isNotBlank(supplierType)){
					sku.setSupplierName(supplierDO.getName());//设置名称
					if(supplierType.equals(SupplierType.SELL.getValue())){
						sku.setSaleType(0);
					}else if(supplierType.equals(SupplierType.PURCHASE.getValue())){
						sku.setSaleType(0);
					}else if(supplierType.equals(SupplierType.ASSOCIATE.getValue())){
						sku.setSaleType(1);
					}
					
				}
			}
		}
		/***
		 * 正常商品 服务商品 二手商品 报废商品
		 ***/
		if (sku.getItemTypeStr().equals("正常商品")) {
			sku.setItemType(1);
		} else if (sku.getItemTypeStr().equals("服务商品")) {
			sku.setItemType(2);
		} else if (sku.getItemTypeStr().equals("二手商品")) {
			sku.setItemType(3);
		} else if (sku.getItemTypeStr().equals("报废商品")) {
			sku.setItemType(4);
		} else {
			sku.setExcelOpStatus(2);
			LOGGER.error("商品类型设置不对");
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "商品类型设置不对;\n");
		}

		sku.setWavesSign(2);

		if ("是".equals(sku.getExpSignStr())) {
			sku.setExpSign(1);
		} else if ("否".equals(sku.getExpSignStr())) {
			sku.setExpSign(2);
		} else {
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "是否有效期设置不对;\n");
		}
		if(StringUtils.isNotBlank(sku.getBarcode())&&sku.getExcelOpStatus()!=2){
			try {
				boolean checkBarcode = false;
				ItemInfo infoDO = itemManageService.checkSpuExsit(sku.getSmallId(), sku.getBrandId(), sku.getUnitId(), sku.getSpuName().trim(),null);
				if (null==infoDO) {
					checkBarcode = itemManageService.checkBarcodeExsit(sku.getBarcode(), null,null);
					if(!checkBarcode){
						sku.setExcelOpStatus(2);
						sku.setExcelOpmessage(sku.getExcelOpmessage() + "商品系统中已经存在此条码;\n");
					}
				}else{
					sku.setItemId(infoDO.getId());
					checkBarcode = itemManageService.checkBarcodeExsit(sku.getBarcode(), null,infoDO.getId());
					//prdid规格校验
					int flag = itemManageService.checkPrdidSpec(infoDO.getId(), specMap);
					
					if(checkBarcode){//不存在 
						if(flag!=1){
							sku.setExcelOpStatus(2);
							sku.setExcelOpmessage(sku.getExcelOpmessage() + "Spu已经存在，规格在系统中不匹配;\n");
						}
					}else{
						if(flag==4){
							checkBarcode = itemManageService.checkBarcodeExsitInSku(null,sku.getBarcode(), null, sku.getSupplierId(), sku.getSaleType());
							if (!checkBarcode) {
								sku.setExcelOpStatus(2);
								sku.setExcelOpmessage(sku.getExcelOpmessage() + "商品系统中已经存在此条码:"
										+sku.getBarcode()+",供应商:"+sku.getSupplierName()+";\n");
							}
						}else{
							sku.setExcelOpStatus(2);
							sku.setExcelOpmessage(sku.getExcelOpmessage() + "商品系统中已经存在此条码在prdid中，但是规格不一样;\n");
						}
					}
				}
			} catch (ItemServiceException e) {
				sku.setExcelOpStatus(2);
				LOGGER.error(e.getMessage(), e);
				sku.setExcelOpmessage(sku.getExcelOpmessage() + e.getMessage());
			}
		}
		//赋值code
		if(null!=c3){
			sku.setCategoryCode(c3.getCode());
		}
		
		getSkuArtInfoList(sku, clearanceChannelList);
		
		return sku;
	}
	
	/**
	 * 
	 */
	private void getSkuArtInfoList(ExcelWaveSkuDTO sku,List<ClearanceChannels> clearanceChannelsList) throws Exception {
		
		
		List<ItemSkuArt> list = new ArrayList<ItemSkuArt>();
		String bondedAreaAndArticleNumber = sku.getBondedAreaAndArticleNumber();
		if((bondedAreaAndArticleNumber.indexOf("=")!=-1)&&(bondedAreaAndArticleNumber.indexOf("&")!=-1)){
			String [] str = bondedAreaAndArticleNumber.split("\\;");
			//上海保税区=商品备案号&HS编号&商品报关名称&商品特征;宁波保税区=
			for(String s : str){
				if(s.indexOf("=")!=-1){
					String [] attrs = s.split("=");
					ItemSkuArt a = new ItemSkuArt();
					if(CollectionUtils.isNotEmpty(clearanceChannelsList)){
						for(ClearanceChannels c : clearanceChannelsList){
							if(c.getName().equals(attrs[0])){
								a.setBondedArea(c.getId());
								continue;
							}
						}
					}
					String [] arts = attrs[1].split("&");
					a.setArticleNumber(arts[0]);
					a.setHsCode(arts[1]);
					a.setItemDeclareName(arts[2]);
					a.setItemFeature(arts[3]);
					list.add(a);
				}
			}
		}
		sku.setSkuArtList(list);
	}
	/**
	 * 
	 * <pre>
	 * 	校验商品基本信息
	 * </pre>
	 *
	 * @param sku
	 * @param forbiddenWordslist
	 * @return Category
	 */
	private Category validaSkuInfo(ExcelSkuDTO sku,
			List<ForbiddenWords> forbiddenWordslist) {
		//违禁词校验
		//*SPU名称	*SKU名称	*副标题	生产厂家  商品规格	箱规	自定义属性组
		StringBuffer checkMsg = new StringBuffer(); 
		if(StringUtils.isNotBlank(sku.getSpuName())){
			checkMsg.append( itemValidateProxy.checkForbiddenWordsField(sku.getSpuName(),"*SPU名称",forbiddenWordslist));
		}
		if(StringUtils.isNotBlank(sku.getSkuName())){
			checkMsg.append( itemValidateProxy.checkForbiddenWordsField(sku.getSkuName(),"*SKU名称",forbiddenWordslist));
		}
		if(StringUtils.isNotBlank(sku.getSkuSubTitle())){
			checkMsg.append( itemValidateProxy.checkForbiddenWordsField(sku.getSkuSubTitle(),"*副标题",forbiddenWordslist));
		}
		if(StringUtils.isNotBlank(sku.getManufacturer())){
			checkMsg.append( itemValidateProxy.checkForbiddenWordsField(sku.getManufacturer(),"生产厂家",forbiddenWordslist));
		}
		if(StringUtils.isNotBlank(sku.getSpecifications())){
			checkMsg.append( itemValidateProxy.checkForbiddenWordsField(sku.getSpecifications(),"商品规格",forbiddenWordslist));
		}
		if(StringUtils.isNotBlank(sku.getCartonSpec())){
			checkMsg.append( itemValidateProxy.checkForbiddenWordsField(sku.getCartonSpec(),"箱规",forbiddenWordslist));
		}
		if(StringUtils.isNotBlank(sku.getAttibuteCus())){
			checkMsg.append( itemValidateProxy.checkForbiddenWordsField(sku.getAttibuteCus(),"自定义属性组",forbiddenWordslist));
		}
		
		
		String checkMsgStr = checkMsg.toString();
		if(StringUtils.isNotBlank(checkMsgStr)){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + checkMsgStr+"\n");
		}
		
		if(null==sku.getSupplierId()){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*商家ID的不能为空;\n");
		}
		
		if(StringUtils.isBlank(sku.getBarcode())){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*条形码不能为空;\n");
		}
		if(StringUtils.isBlank(sku.getSpuName())){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*款号不能为空;\n");
		}
		if(null==sku.getLargId()){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*大类ID不能为空;\n");
		}
		if(null==sku.getMediumId()){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*中类ID不能为空;\n");
		}
		
		if(null==sku.getSmallId()){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*小类ID不能为空;\n");
		}
		if(null==sku.getUnitId()){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "单位ID不能为空;\n");
		}
		if(null==sku.getBrandId()){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*品牌ID不能为空;\n");
		}
		
		
		if(null==sku.getFreightTemplateId()){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*运费ID不能为空;\n");
		}
		
		if(null==sku.getBrandId()){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*品牌ID不能为空;\n");
		}
		
		if(StringUtils.isBlank(sku.getSkuName())){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*SKU名称不能为空;\n");
		}
		if(StringUtils.isBlank(sku.getSkuSubTitle())){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*副标题不能为空;\n");
		}
		if(null == sku.getReturnDays()){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*无理由退货期限（天）不能为空;\n");
		}
		
		if(null == sku.getBasicPrice()){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*市场价（元）不能为空;\n");
		}
		
		if(null == sku.getWeight()){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*毛重（g）不能为空;\n");
		}
		
		if(null == sku.getApplyAgeId()){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*适用年龄ID不能为空;\n");
		}
		//ID
		//*商家ID		*大类ID *中类ID *小类ID *品牌ID *运费ID *规格组-1ID
		// *规格1ID
		 if(null!=sku.getSupplierId()&& (sku.getSupplierId()+"").length()>11){
			 sku.setExcelOpStatus(2);
			 sku.setExcelOpmessage(sku.getExcelOpmessage() + "*商家ID的长度不能大于11;\n");
		 }
		 
		 if(null!=sku.getLargId()&& (sku.getLargId()+"").length()>11){
			 sku.setExcelOpStatus(2);
			 sku.setExcelOpmessage(sku.getExcelOpmessage() + "*大类ID的长度不能大于11;\n");
		 }
		 
		 if(null!=sku.getMediumId()&& (sku.getMediumId()+"").length()>11){
			 sku.setExcelOpStatus(2);
			 sku.setExcelOpmessage(sku.getExcelOpmessage() + "*中类ID的长度不能大于11;\n");
		 }
		 
		 if(null!=sku.getSmallId()&& (sku.getSmallId()+"").length()>11){
			 sku.setExcelOpStatus(2);
			 sku.setExcelOpmessage(sku.getExcelOpmessage() + "*小类ID的长度不能大于11;\n");
		 }
		 
		 if(null!=sku.getBrandId()&& (sku.getBrandId()+"").length()>11){
			 sku.setExcelOpStatus(2);
			 sku.setExcelOpmessage(sku.getExcelOpmessage() + " *品牌ID的长度不能大于11;\n");
		 }
		 
		 if(null!=sku.getFreightTemplateId()&& (sku.getFreightTemplateId()+"").length()>11){
			 sku.setExcelOpStatus(2);
			 sku.setExcelOpmessage(sku.getExcelOpmessage() + "*运费ID的长度不能大于11;\n");
		 }
		 
		 
		 if(null!=sku.getSpec1GroupId()&& (sku.getSpec1GroupId()+"").length()>11){
			 sku.setExcelOpStatus(2);
			 sku.setExcelOpmessage(sku.getExcelOpmessage() + "*规格组1ID的长度不能大于11;\n");
		 }
		 
		 if(null!=sku.getSpec2GroupId()&& (sku.getSpec2GroupId()+"").length()>11){
			 sku.setExcelOpStatus(2);
			 sku.setExcelOpmessage(sku.getExcelOpmessage() + "*规格组2ID的长度不能大于11;\n");
		 }
		 
		 if(null!=sku.getSpec3GroupId()&& (sku.getSpec3GroupId()+"").length()>11){
			 sku.setExcelOpStatus(2);
			 sku.setExcelOpmessage(sku.getExcelOpmessage() + "*规格组3ID的长度不能大于11;\n");
		 }
		 
		 if(null!=sku.getSpec1Id()&& (sku.getSpec1Id()+"").length()>11){
			 sku.setExcelOpStatus(2);
			 sku.setExcelOpmessage(sku.getExcelOpmessage() + "*规格1ID的长度不能大于11;\n");
		 }
		 if(null!=sku.getSpec2Id()&& (sku.getSpec2Id()+"").length()>11){
			 sku.setExcelOpStatus(2);
			 sku.setExcelOpmessage(sku.getExcelOpmessage() + "*规格2ID的长度不能大于11;\n");
		 }
		 if(null!=sku.getSpec3Id()&& (sku.getSpec3Id()+"").length()>11){
			 sku.setExcelOpStatus(2);
			 sku.setExcelOpmessage(sku.getExcelOpmessage() + "*规格3ID的长度不能大于11;\n");
		 }
		 if(null!=sku.getApplyAgeId()&& (sku.getApplyAgeId()+"").length()>11){
			 sku.setExcelOpStatus(2);
			 sku.setExcelOpmessage(sku.getExcelOpmessage() + "*适用年龄ID的长度不能大于11;\n");
		 }
		 
		//长度校验
		//*条形码	*SPU名称 *SKU名称 *副标题 商品规格 	生产厂家	箱规 自定义属性组
		//*无理由退货期限（天）	*市场价（元）	*毛重（g）有效期	
		Integer returnDays = sku.getReturnDays();
		if (null != returnDays && ((returnDays+"").length()>5)){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "无理由退货期限（天）的长度不能大于5;\n");
		}
		
		Double basicPrice = sku.getBasicPrice();
		if(null != basicPrice && sku.getBasicPrice()>1000000){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*市场价（元）不得超过百万元;\n");
		}
		
		 Double weight = sku.getWeight();
		 if(null != weight && sku.getWeight()>1000000){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "毛重不得超过百万;\n");
		}
		
		Integer expDays = sku.getExpDays();
		if(null!=expDays && ((expDays+"").length()>5)){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "有效期的长度不能大于5;\n");
		}
		
		if(StringUtils.isNotBlank(sku.getBarcode()) && sku.getBarcode().trim().length()>20){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "条形码的长度不能大于20;\n");
		}
		
		if(StringUtils.isNotBlank(sku.getSpuName()) && sku.getSpuName().trim().length()>60){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*SPU名称的长度不能大于60;\n");
		}
		
		if(StringUtils.isNotBlank(sku.getSkuName()) && sku.getSkuName().trim().length()>60){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*SKU名称的长度不能大于60;\n");
		}
		
		if(StringUtils.isNotBlank(sku.getSkuSubTitle()) && sku.getSkuSubTitle().trim().length()>100){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "*副标题的长度不能大于100;\n");
		}
		
		if(StringUtils.isNotBlank(sku.getManufacturer())&& sku.getManufacturer().trim().length()>100){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "生产厂家的长度不能大于100;\n");
		}
		
		if(StringUtils.isNotBlank(sku.getSpecifications())&& sku.getSpecifications().trim().length()>100){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "商品规格的长度不能大于100;\n");
		}
		
		if(StringUtils.isNotBlank(sku.getCartonSpec())&& sku.getCartonSpec().trim().length()>100){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "箱规的长度不能大于100;\n");
		}

		StringBuffer attrCheckMsg = new StringBuffer();
		List<ItemAttribute> attrList = new ArrayList<ItemAttribute>();
		try {
			attrList = sku.getItemAttributeList();
		} catch (Exception e) {
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "自定义属性组必须符合(xxx=yyy;)的约定形式,x、y都不能为空;\n");
		}
		if(CollectionUtils.isNotEmpty(attrList)){
			for(ItemAttribute attr : attrList){
				if(StringUtils.isNotBlank(attr.getAttrKey())&&attr.getAttrKey().trim().length()>20){
					attrCheckMsg.append("自定义属性名："+attr.getAttrKey()+" 长度不能大于20;");
				}
				if(StringUtils.isNotBlank(attr.getAttrValue())&&attr.getAttrValue().trim().length()>200){
					attrCheckMsg.append("自定义属性值："+attr.getAttrKey()+" 长度不能大于200;");
				}
				
			}
		}
		
		String attrCheckStr = attrCheckMsg.toString();
		if(StringUtils.isNotBlank(attrCheckStr)){
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + attrCheckStr);
		}
		
		Category c1 = categoryService.queryById(sku.getLargId());
		if (null == c1 ||(null!=c1&& c1.getStatus().equals(false))
				||c1.getLevel().intValue()!=ItemConstant.CATEGORY_LARGE_LEVEL) {
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "大类在系统中不存在或失效;\n");
		}
		Category c2 = categoryService.queryById(sku.getMediumId());
		if (null == c2 ||(null!=c1&& c2.getStatus().equals(false))
				||c2.getLevel().intValue()!=ItemConstant.CATEGORY_MEDIUM_LEVEL) {
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "中类在系统中不存在或失效;\n");
		}
		if (null!=c1&&null!=c2&&!c2.getParentId().equals(c1.getId())) {
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "中类与大类对应关系不对;\n");
		}

		Category c3 = categoryService.queryById(sku.getSmallId());
		if (null == c3 ||(null!=c1&& c3.getStatus().equals(false))
				||c3.getLevel().intValue()!=ItemConstant.CATEGORY_SMALL_LEVEL) {
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "小类在系统中不存在或失效;\n");
		}

		if (null!=c3&&null!=c2&&!c3.getParentId().equals(c2.getId())) {
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "小类与中类对应关系不对;\n");
		}
		
		Brand brand = brandService.queryById(sku.getBrandId());
		if (null == brand ||(null!=brand&& brand.getStatus().equals(false))) {
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "品牌在系统中不存在或失效;\n");
		}
		DictionaryInfo dictionaryInfoDO = dictionaryInfoService
				.queryById(sku.getUnitId());
		if (null == dictionaryInfoDO ) {
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "单位在系统中不存在;\n");
		}
		
		DictionaryInfo applyAgeInfoDO = dictionaryInfoService.queryById(sku.getApplyAgeId());
		if (null == applyAgeInfoDO ) {
			sku.setExcelOpStatus(2);
			sku.setExcelOpmessage(sku.getExcelOpmessage() + "适用年龄在系统中不存在;\n");
		}
		
		return c3;
	}

	/**
	 * 
	 * <pre>
	 * 生成importLog对象
	 * </pre>
	 * 
	 * @return
	 */
	private ItemImportLog initItemImportLog(String userName) {
		ItemImportLog importLog = new ItemImportLog();
		importLog.setStatus(1);
		importLog.setDeleteSign(1);
		importLog.setRealFileName(realFileName);
		importLog.setSecretKey(secretKey);
		importLog.setFileName(uniqueFileName);
		importLog.setCreateTime(new Date());
		importLog.setCreateUser(userName);
		importLog.setSumCount(sumCount);
		importLog.setSuccessCount(0);
		importLog.setFailCount(0);
		return importLog;
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
	public List<SkuImportDto> importSkuData(List<ExcelSkuDTO> entitys,Long logId,Map<Long,ExcelSkuDTO> validSucessMap,Map<Long,ExcelSkuDTO> validFailMap,String userName,String importFrom)
			throws IllegalAccessException, InvocationTargetException {
		List<ExcelSkuDto> validFailIndexList = new ArrayList<ExcelSkuDto>();
		Set<Long> key = validFailMap.keySet();
		for (Iterator<Long> it = key.iterator(); it.hasNext();) {
			Long excelIndex = (Long) it.next();
			ExcelSkuDto excelSkuDto = new ExcelSkuDto();
			excelSkuDto.setLogId(logId);
			excelSkuDto.setExcelIndex(excelIndex);
			excelSkuDto.setExcelOpStatus(validFailMap.get(excelIndex)
					.getExcelOpStatus());
			excelSkuDto.setExcelOpmessage(validFailMap.get(excelIndex)
					.getExcelOpmessage());
			validFailIndexList.add(excelSkuDto);
		}
		List<SkuImportDto> list = new ArrayList<SkuImportDto>();
		if (null != entitys) {
			for (ExcelSkuDTO sku : entitys) {
				SkuImportDto skuImportDto = new SkuImportDto();
				BeanUtils.copyProperties(skuImportDto, sku);
				if(null==sku.getSpec1GroupId() ){
					skuImportDto.setSpec1GroupId(null);
				}
				if(null==sku.getSpec2GroupId() ){
					skuImportDto.setSpec2GroupId(null);
				}
				if(null==sku.getSpec3GroupId() ){
					skuImportDto.setSpec3GroupId(null);
				}
				if(null==sku.getSpec1Id() ){
					skuImportDto.setSpec1Id(null);
				}
				if(null==sku.getSpec2Id()){
					skuImportDto.setSpec2Id(null);
				}
				if(null==sku.getSpec3Id()){
					skuImportDto.setSpec3Id(null);
				}
				skuImportDto.setStatus(0);// 默认沒有上架
				skuImportDto.setVolumeHigh(sku.getVolumeHigh());
				skuImportDto.setVolumeLength(sku.getVolumeLength());
				skuImportDto.setVolumeWidth(sku.getVolumeWidth());
				skuImportDto.setWeightNet(sku.getWeightNet());
				skuImportDto.setSaleType(sku.getSaleType());
				// 规格ID 获取规格组信息
				list.add(skuImportDto);
				
			}
		}
		//当前用户
		return itemImportService.importSku(list, logId, validFailIndexList, userName,importFrom);
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
	public List<SkuImportDto> importWaveSkuData(List<ExcelWaveSkuDTO> entitys,Long logId,Map<Long,ExcelWaveSkuDTO> validSucessMap,Map<Long,ExcelWaveSkuDTO> validFailMap,String userName,String userId)
			throws IllegalAccessException, InvocationTargetException {
		List<ExcelSkuDto> validFailIndexList = new ArrayList<ExcelSkuDto>();
		Set<Long> key = validFailMap.keySet();
		for (Iterator<Long> it = key.iterator(); it.hasNext();) {
			Long excelIndex = (Long) it.next();
			ExcelSkuDto excelSkuDto = new ExcelSkuDto();
			excelSkuDto.setLogId(logId);
			excelSkuDto.setExcelIndex(excelIndex);
			excelSkuDto.setExcelOpStatus(validFailMap.get(excelIndex)
					.getExcelOpStatus());
			excelSkuDto.setExcelOpmessage(validFailMap.get(excelIndex)
					.getExcelOpmessage());
			validFailIndexList.add(excelSkuDto);
		}
		List<SkuImportDto> list = new ArrayList<SkuImportDto>();
		if (null != entitys) {
			for (ExcelWaveSkuDTO sku : entitys) {
				SkuImportDto skuImportDto = new SkuImportDto();
				BeanUtils.copyProperties(skuImportDto, sku);
				if(null==sku.getSpec1GroupId() ){
					skuImportDto.setSpec1GroupId(null);
				}
				if(null==sku.getSpec2GroupId() ){
					skuImportDto.setSpec2GroupId(null);
				}
				if(null==sku.getSpec3GroupId() ){
					skuImportDto.setSpec3GroupId(null);
				}
				if(null==sku.getSpec1Id() ){
					skuImportDto.setSpec1Id(null);
				}
				if(null==sku.getSpec2Id()){
					skuImportDto.setSpec2Id(null);
				}
				if(null==sku.getSpec3Id()){
					skuImportDto.setSpec3Id(null);
				}
				skuImportDto.setStatus(0);// 默认沒有上架
				skuImportDto.setVolumeHigh(sku.getVolumeHigh());
				skuImportDto.setVolumeLength(sku.getVolumeLength());
				skuImportDto.setVolumeWidth(sku.getVolumeWidth());
				skuImportDto.setWeightNet(sku.getWeightNet());
				skuImportDto.setSaleType(sku.getSaleType());
				// 规格ID 获取规格组信息
				list.add(skuImportDto);
				
			}
		}
		//当前用户
		return itemImportService.importSku(list, logId, validFailIndexList, userName,userId);
	}

	/**
	 * 
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param skuItemImportLogDto
	 */
	public void saveImportSkuLog(SkuImportLogDto skuItemImportLogDto) {
		itemImportService.saveImportLogDto(skuItemImportLogDto);
	}

	/**
	 * 
	 * <pre>
	 * 		生成导入的log日志，插入数据库..
	 * </pre>
	 * 
	 * @param excelSkuList
	 * @param importSkuList
	 * @param fileName
	 */
	public SkuImportLogDto genItemImportLogList(List<ExcelSkuDTO> excelSkuList,
			List<SkuImportDto> importSkuList, SkuImportLogDto skuItemImportLogDto,Long logId) {
		ItemImportLog importLog = new ItemImportLog();
		importLog.setId(logId);
		List<ItemImportList> importList = new ArrayList<ItemImportList>();
		skuItemImportLogDto.setItemImportLog(importLog);
		skuItemImportLogDto.setImportList(importList);
		// 更新成功的行
		int sucessCount = 0;
		// 失败的行
		int failCount = 0;
		for (ExcelSkuDTO skuDTO : excelSkuList) {
			ItemImportList importOneLog = new ItemImportList();
			if (skuDTO.getExcelOpStatus() == 1) {
				sucessCount++;
			} else {
				failCount++;
			}
			importOneLog.setBarcode(skuDTO.getBarcode());

			importOneLog.setExcelIndex(skuDTO.getExcelIndex());
			importOneLog.setOpMessage(skuDTO.getExcelOpmessage());
			importOneLog.setStatus(Integer.parseInt(skuDTO.getExcelOpStatus() + ""));
			importList.add(importOneLog);
		}
		importLog.setSuccessCount(sucessCount);
		importLog.setFailCount(failCount);
		return skuItemImportLogDto;

	}

	/**
	 * 
	 * <pre>
	 * 拆分导入的excel模板
	 * </pre>
	 * 
	 * @param excelSkuList
	 */
	private void splitExcelSkuList(List<ExcelSkuDTO> excelSkuList,Map<Long,ExcelSkuDTO> validSucessMap,Map<Long,ExcelSkuDTO> validFailMap) {
		for (ExcelSkuDTO sku : excelSkuList) {
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
	 * 拆分导入的excel模板
	 * </pre>
	 * 
	 * @param excelSkuList
	 */
	private void splitExcelWaveSkuList(List<ExcelWaveSkuDTO> excelSkuList,Map<Long,ExcelWaveSkuDTO> validSucessMap,Map<Long,ExcelWaveSkuDTO> validFailMap) {
		for (ExcelWaveSkuDTO sku : excelSkuList) {
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
	 * 查询日志文件
	 * </pre>
	 * 
	 * @param status
	 *            全部成功的状态
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public SkuImportLogDto queryItemImportLogDto(String userName,int status, int pageNo,
			int pageSize) {
		return (SkuImportLogDto) itemImportService.querySkuImport(userName,status,
				pageNo, pageSize);
	}

	/**
	 * 
	 * <pre>
	 * 分页查询
	 * </pre>
	 * 
	 * @param skuItemImportLogDto
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo<ItemImportList> queryItemImportList(SkuImportLogDto skuItemImportLogDto,
			int startPage, int pageSize) {
		PageInfo<ItemImportList> page = new PageInfo<ItemImportList>();
		List<ItemImportList> list = skuItemImportLogDto.getImportList();
		page.setPage(startPage);
		page.setSize(pageSize);
		page.setRecords(skuItemImportLogDto.getTotalCount().intValue());
		page.setRows(list);
		return page;
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
	 * 
	 * <pre>
	 * 文件上传
	 * </pre>
	 * 
	 * @param file
	 * @return
	 */
	public String uploadFile(File file) {
		TxtFile info = new TxtFile();
		String fileId = null;
		info.setFile(file);
		Map<MetaDataKey, String> map = new HashMap<MetaDataKey, String>();
		map.put(MetaDataKey.FILENAME, file.getName());
		map.put(MetaDataKey.CREATOR, UPLOAD_CREATOR);
		info.setMetaData(map);
		info.setEncryptionFlag(true);
		try {
			fileId = uploadService.upload(file);
			secretKey = info.getSecretKey(); // 获取密钥
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("上传文件异常！", e);
		}
		return fileId;
	}

	/**
	 * 
	 * <pre>
	 * 下载导入excel的文件
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @param fileId
	 * @param id
	 * @param status
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException 
	 */
	public List<ItemImportList> exportList(Long id, Integer status)
			throws InvalidFormatException, IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		SkuImportLogDto skuItemImportLogDto = itemImportService.querySkuImportById(
				id, status);
		List<ItemImportList> importLists = skuItemImportLogDto.getImportList();
		return importLists;
	}
	
	/**
	 * 
	 * <pre>
	 *  获取导出模板
	 * </pre>
	 *
	 * @param request
	 * @param response
	 * @param waveSign
	 * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException 
	 */
	@SuppressWarnings("null")
	public void getTemplateDatas(HttpServletRequest request,
			HttpServletResponse response,String waveSign){
		String  downLoadFrom=(String) request.getAttribute("downLoadFrom");//下载模板来源
		// 供应商列表
		List<SupplierInfo> supplierList =  new ArrayList<SupplierInfo>();
		List<SupplierType> supplierTypeList = new ArrayList<SupplierType>();
		supplierTypeList.add(SupplierType.PURCHASE);
		supplierTypeList.add(SupplierType.ASSOCIATE);
		supplierTypeList.add(SupplierType.SELL);
		int batchNums = 1000;
		SupplierResult result = purchasingManagementService.getSuppliersByTypes(supplierTypeList,1, batchNums);
		supplierList.addAll(result.getSupplierInfoList());
		int totalCount = 0; 
		if(null!=result){
			totalCount = result.getTotalCount().intValue();
		}
		int pageNums = totalCount % batchNums > 0 ? (totalCount / batchNums) +1 : (totalCount / batchNums);
		if(pageNums>=2){
			for(int i = 2 ;i <= pageNums;i++){
				SupplierResult result1 =purchasingManagementService.getSuppliersByTypes(supplierTypeList, i, batchNums);
				supplierList.addAll(result1.getSupplierInfoList());
			}
		}
		
		String[][] data1 = null;
		String[][] data2 = null;
		String[][] data3 = null;
		String[][] data4 = null;
		String[][] data5 = null;
		String[][] data6 = null;
		String[][] data7 = null;
		String[][] data8 = null;
		String[][] data9 = null;
		String[][] data10 = null;
		String[][] data11 = null;
		String[][] data12 = null;
		String[][] data13 = null;
		String[][] data14 = null;
       if("FROM_SUPPLIER_SYS".equals(downLoadFrom)){
   			Long supplierUserId=(Long) request.getSession().getAttribute(ItemImportProxy.USER_ID_KEY);
   			SupplierInfo supplierInfo=supplierUserProxy.getSupplierInfoByUserId(supplierUserId);
   			Long supplierId=Long.valueOf(0);
   			String supplierName="";
   			if(supplierInfo!=null){
   				supplierId=supplierInfo.getId();
   				supplierName=supplierInfo.getName();
   			}
	        data1 = new String[1][2];
	        data1[0][0] = supplierName;
			data1[0][1] = supplierId.toString();
       }else{
    	   if (null != supplierList && !supplierList.isEmpty()) {
   			data1 = new String[supplierList.size()][2];
   			int i = 0;
   			for (SupplierInfo s : supplierList) {
   				data1[i][0] = s.getName();
   				data1[i][1] = s.getId().toString();
   				i++;
   			}
   		}
       }
		

		// 品牌
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", Constant.ENABLED.YES);
		List<Brand> brandList = brandService.queryByParam(params);
		if (null != brandList && !brandList.isEmpty()) {
			data2 = new String[brandList.size()][2];
			int i = 0;
			for (Brand s : brandList) {
				data2[i][0] = s.getName();
				data2[i][1] = s.getId().toString();
				i++;
			}
		}
		// 分类
		params.clear();
		params.put("status", Constant.ENABLED.YES);
		List<Category> categoryList = categoryService.queryByParam(params);
		List<Category> categorySmallList = new ArrayList<Category>();
		Map<Long ,Category> cateogryLargeMap = new HashMap<Long,Category>();
		Map<Long ,Category> cateogryMediumMap = new HashMap<Long,Category>();
		for (Category c : categoryList) {
			if (c.getLevel().equals(1)) {
				cateogryLargeMap.put(c.getId(), c);
			} else if (c.getLevel().equals(2)) {
				cateogryMediumMap.put(c.getId(), c);
			} else if (c.getLevel().equals(3)) {
				categorySmallList.add(c);
			}
		}

		//大类	大类ID	中类	中类ID	小类	小类ID
		if (null != categorySmallList && !categorySmallList.isEmpty()) {
			data3 = new String[categorySmallList.size()][6];
			int i = 0 ;
			for(Category smallCategory : categorySmallList){
				
				Set<Long> mediumkey = cateogryMediumMap.keySet();
				String largeName = "";
				Long largeId = 0l ;
				String mediumName = "";
				Long mediumId = 0l ;
				String smallName = smallCategory.getName();
				Long smallId = smallCategory.getId() ;
				for (Iterator<Long> it = mediumkey.iterator(); it.hasNext();) {
					Long id = it.next();
					if(id.equals(smallCategory.getParentId())){
						Category category = cateogryMediumMap.get(id);
						mediumId = id;
						largeId  = category.getParentId();
						mediumName = category.getName();
						break;
					}
				}
				Set<Long> largekey = cateogryLargeMap.keySet();
				for (Iterator<Long> it = largekey.iterator(); it.hasNext();) {
					Long id = it.next();
					if(id.equals(largeId)){
						Category category = cateogryLargeMap.get(id);
						largeName  = category.getName();
						break;
					}
				}
				data3[i][0]=largeName;
				data3[i][1]=largeId.toString();
				data3[i][2]=mediumName;
				data3[i][3]=mediumId.toString();
				data3[i][4]=smallName;
				data3[i][5]=smallId.toString();
				i++;
			}
		}
		

		// 销售属性组
		SpecGroup specGroupDO = new SpecGroup();
		specGroupDO.setStatus(Constant.ENABLED.YES);
		List<SpecGroupResult>  specGroupList = specGroupService.getAllSpecGroupResult();
		
		if (null != specGroupList && !specGroupList.isEmpty()) {
			int specSize = 0; 
			for (SpecGroupResult specGroupResult : specGroupList) {
				if(CollectionUtils.isNotEmpty(specGroupResult.getSpecDoList())){
					 specSize += specGroupResult.getSpecDoList().size();
				}
			}
			
			data4 = new String[specSize][4];
			int i = 0;
			for (SpecGroupResult specGroupResult : specGroupList) {
				SpecGroup specGroup=  specGroupResult.getSpecGroup();
				String specGroupName =  specGroup.getName();
				Long specGroupId = specGroup.getId();
				if(CollectionUtils.isNotEmpty(specGroupResult.getSpecDoList())){
					for(Spec spec: specGroupResult.getSpecDoList()){
						data4[i][0] = specGroupName;
						data4[i][1] = specGroupId.toString();
						data4[i][2] = spec.getSpec();
						data4[i][3] = spec.getId().toString();
						i++;
					}
				}
			}
		}

		// 单位
		params.clear();
		params.put("code", DictionaryCode.c1001.getCode());
		List<DictionaryInfo> unitList = dictionaryInfoService.queryByParam(params);

		if (null != unitList && !unitList.isEmpty()) {
			data5 = new String[unitList.size()][2];
			int i = 0;
			for (DictionaryInfo s : unitList) {
				data5[i][0] = s.getName();
				data5[i][1] = s.getId().toString();
				i++;
			}
		}

		// 运费模板
		List<FreightTemplate> freightTemplateList = new ArrayList<FreightTemplate>();
		try {
			freightTemplateList =freightTemplateService
					.selectByCalculateMode(ItemConstant.SINGLE_FREIGHTTEMPLATE_STATUS);
			if (null != freightTemplateList && !freightTemplateList.isEmpty()) {
				data6 = new String[freightTemplateList.size()][2];
				int i = 0;
				for (FreightTemplate s : freightTemplateList) {
					data6[i][0] = s.getName();
					data6[i][1] = s.getId().toString();
					i++;
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		//适用年龄
		params.clear();
		params.put("code", DictionaryCode.c1003.getCode());
		List<DictionaryInfo> applyAgeList = dictionaryInfoService.queryByParam(params);
		if (CollectionUtils.isNotEmpty(applyAgeList)) {
			data7 = new String[applyAgeList.size()][2];
			int i = 0;
			for (DictionaryInfo s : applyAgeList) {
				data7[i][0] = s.getName();
				data7[i][1] = s.getId().toString();
				i++;
			}
		}
		//国家，海关，税率
		if(waveSign.equals(WAVESIGN)){
			// 行邮税
			params.clear();
			params.put("type", TaxRateEnum.TARRIFRATE.getType());
			params.put("status", Constant.ENABLED.YES);
			List<TaxRate> tarrifList = taxRateProxy.queryByParam(params).getData();
			if(CollectionUtils.isNotEmpty(tarrifList)){
				data8 = new String[tarrifList.size()][2];
				int i = 0;
				for (TaxRate s : tarrifList) {
					data8[i][0] = s.getRate()+"% - "+ s.getRemark();
					data8[i][1] = s.getId().toString();
					i++;
				}
				
			}
			//关税
			params.clear();
			params.put("type", TaxRateEnum.CUSTOMS.getType());
			params.put("status", Constant.ENABLED.YES);
			List<TaxRate> customsList = taxRateProxy.queryByParam(params).getData();
			if(CollectionUtils.isNotEmpty(customsList)){
				data9 = new String[customsList.size()][2];
				int i = 0;
				for (TaxRate s : customsList) {
					data9[i][0] = s.getRate()+"% - "+ s.getRemark();
					data9[i][1] = s.getId().toString();
					i++;
				}
				
			}
			//消费税
			params.clear();
			params.put("type", TaxRateEnum.EXCISE.getType());
			params.put("status", Constant.ENABLED.YES);
			List<TaxRate> exciseList = taxRateProxy.queryByParam(params).getData();
			if(CollectionUtils.isNotEmpty(exciseList)){
				data10 = new String[exciseList.size()][2];
				int i = 0;
				for (TaxRate s : exciseList) {
					data10[i][0] = s.getRate()+"% - "+ s.getRemark();
					data10[i][1] = s.getId().toString();
					i++;
				}
				
			}
			//增值税
			params.clear();
			params.put("type", TaxRateEnum.ADDEDVALUE.getType());
			params.put("status", Constant.ENABLED.YES);
			List<TaxRate> addedvalueList = taxRateProxy.queryByParam(params).getData();
			if(CollectionUtils.isNotEmpty(addedvalueList)){
				data11 = new String[addedvalueList.size()][2];
				int i = 0;
				for (TaxRate s : addedvalueList) {
					data11[i][0] = s.getRate()+"% - "+ s.getRemark();
					data11[i][1] = s.getId().toString();
					i++;
				}
				
			}
			// 国家
			List<DistrictInfo>  countryList = itemProxy.getAllCountryList();
			if(CollectionUtils.isNotEmpty(countryList)){
				data12 = new String[countryList.size()][2];
				int i = 0;
				for (DistrictInfo s : countryList) {
					data12[i][0] = s.getName();
					data12[i][1] = s.getId().toString();
					i++;
				}
			}
			
			List<ClearanceChannels>  list  = clearanceChannelsService.getAllClearanceChannelsByStatus(2); 
			
			if(CollectionUtils.isNotEmpty(list)){
				data13 = new String[list.size()][2];
				int i = 0 ;
				for (ClearanceChannels s : list) {
					data13[i][0] = s.getName();
					data13[i][1] = s.getId().toString();
					i++;
				}
			}
		
		}
		
		

		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", -1);
        response.setContentType("application/x-download");
		response.addHeader("Content-Disposition", "attachment; filename="
				+ "import_sku.xls");
		String savePath =  ItemImportProxy.class.getResource("/").getPath()+excelPath;
		if(waveSign.equals(WAVESIGN)){
			savePath =  ItemImportProxy.class.getResource("/").getPath()+hitaoExcelPath;
		}
		
		File file = new File(savePath);
		byte[] buff;
		ServletOutputStream out = null;
		try {
			Workbook wb  = null ;
			wb = ExcelUtil.writeExcelTemplate(file, 1, data1, data2,data3,data4, data5, data6,data7,data8,data9,data10,data11,data12,data13);
			FileOutputStream fileoutputstream = new FileOutputStream(file);
			wb.write(fileoutputstream);
			out = response.getOutputStream();
			buff = FileUtils.readFileToByteArray(file);
			if (null == buff) {
				throw new FileNotFoundException("服务器没有找到模板!");
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

	/**
	 * 
	 * <pre>
	 * 获取文件服务器上的商品模板
	 * </pre>
	 * 
	 * @param fileName
	 * @param secretKey
	 * @param realFileName
	 */
	public File getSecretExcel(String fileName, String secretKey,
			String realFileName) {
		
		byte[] bis = dfsService.getFileBytes(fileName);
		if (null == bis) {
			LOGGER.error("静态服务器上没有找到附件：附件名为：{},服务器上的名称为{}", realFileName,
					fileName);
			throw new ItemServiceException("静态服务器上没有找到附件：附件名为：" + realFileName
					+ " 服务器上的名称为： " + fileName);
		}
		File file = new File(uploadTempPath + realFileName);
		AesCipherService aes = new AesCipherService();
		try {
			byte[] keys = Hex.decodeHex(secretKey.toCharArray());
			ByteSource byteSource = aes.decrypt(bis, keys);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			IOUtils.write(byteSource.getBytes(), fileOutputStream);
			fileOutputStream.close();
			IOUtils.closeQuietly(fileOutputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		} catch (DecoderException e) {
			LOGGER.error(e.getMessage());
		}finally{
		}
		return file;
	}
	
	
	/**
	 * 
	 * <pre>
	 *  校验规格是否在小类中
	 *  存在为true
	 *  不存在为false
	 * </pre>
	 *
	 * @param specGroupId
	 * @param specId
	 * @param specIds
	 * @return boolean
	 */
	private boolean checkSpecInCategory(Long specGroupId,Long specId,Map<Long,ArrayList<Long>> specGroupIds){
		if(null==specGroupId && null == specId){
			return true;
		}
		if(MapUtils.isNotEmpty(specGroupIds)){
			Set<Long> specGroupIdKey = specGroupIds.keySet();
			for (Iterator<Long> it = specGroupIdKey.iterator(); it.hasNext();) {
				Long groupId  = it.next();
				List<Long> specIdList = specGroupIds.get(groupId);
				if(null!=specGroupId  && specGroupId.equals(groupId)){
					if(null!=specId&&CollectionUtils.isNotEmpty(specIdList)){
						if(specIdList.indexOf(specId)!=-1){
							return true;
						}
					}
				}
			}
		}
		return false;
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
	 * 
	 * <pre>
	 * 查询日志文件
	 * </pre>
	 * 
	 * @param status
	 *            全部成功的状态
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public SkuImportLogDto queryImportLogDto(String createUser,Integer status, Integer pageNo,
			Integer pageSize) {
		return  itemImportService.querySkuImport(createUser,status,	pageNo, pageSize);
	}

	/**
	 * 
	 * <pre>
	 * 分页查询
	 * </pre>
	 * 
	 * @param skuImportLogDto
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo<ItemImportList> queryImportList(SkuImportLogDto skuImportLogDto,
			int startPage, int pageSize) {
		PageInfo<ItemImportList> page = new PageInfo<ItemImportList>();
		List<ItemImportList> list = skuImportLogDto.getImportList();
		page.setPage(startPage);
		page.setSize(pageSize);
		page.setRecords(skuImportLogDto.getTotalCount().intValue());
		page.setRows(list);
		return page;
	}

	/**********************************商品信息修改***********************************************************************************/
	
	/** 上传文件 **/
	public File uploadFile(final HttpServletRequest request, String fileName) throws Exception {
		File file = null;
		if (request instanceof MultipartHttpServletRequest) {
			final MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			final MultipartFile multipartFile = multipartRequest.getFile(fileName);
			final String pathDir = request.getSession().getServletContext().getRealPath("upload");
			final File saveFile = new File(pathDir);
		
			if (!saveFile.exists()) {
				saveFile.mkdirs();
			}

			final String fName = multipartFile.getOriginalFilename();
			final String fPName = pathDir + File.separator + fName;
			file = new File(fPName);

			try {
				multipartFile.transferTo(file);
			} catch (final IllegalStateException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		} else {
			LOGGER.info("request请求类型不对。");
			throw new Exception("文件上传失败");
		}
		return file;
	}
	
	/** 商品信息批量修改 **/
	public ResultInfo<Map<String, Object>> batchModifyItemFromExcel(HttpServletRequest request, String fileName, String userName) throws Exception {			
		final File retFile = uploadFile(request, fileName);
		List<ExcelEditSkuDTO> modifySkuList = readExcelEditSkuList(retFile);
		if(CollectionUtils.isNotEmpty(modifySkuList)){
			LOGGER.info("导入的excel模板总共有:{} 行数据" +modifySkuList.size());
			if(modifySkuList.size() > 500)
				return new ResultInfo<Map<String, Object>>(new FailInfo("总行数大于500"));
			
			List<ItemSkuModifyDto> skuList = new ArrayList<>();
			ResultInfo<Map<String, String>> validResult = getValidateModifySku(modifySkuList, skuList);
			if (!validResult.success) {
				return new ResultInfo<>(new FailInfo("数据异常"));
			}
			Map<String, String> result = validResult.getData();
			try {
				ResultInfo<Map<String, String>> resultInfo = itemImportService.importModifySku(skuList);
				int totalCnt = skuList.size() + result.size();
				int successCnt = 0;
				int failCnt = 0;
				if (null != resultInfo.getData()) {
					resultInfo.getData().putAll(result);
					failCnt = resultInfo.getData().size();
				}else{
					resultInfo.setData(result);
					failCnt = result.size();					
				}
				successCnt = totalCnt - failCnt;
				Map<String, Object> resultMap = new HashMap<>();
				resultMap.put("successCnt", successCnt);
				resultMap.put("failCnt", failCnt);
				resultMap.put("resultTable", resultInfo.getData());
				return new ResultInfo<Map<String, Object>>(resultMap);
			} catch (Exception e) {
				LOGGER.error("修改SKU数据失败:" + e.getMessage());
				return new ResultInfo<>(new FailInfo(e.getMessage()));
			}
		}
		return new ResultInfo<>(new FailInfo("导入失败"));
	}
	private ResultInfo<Map<String, String>> getValidateModifySku(List<ExcelEditSkuDTO> excelSkuList, List<ItemSkuModifyDto> modifySkuList){
		if (null == modifySkuList || null == excelSkuList) {
			return new ResultInfo<>(new FailInfo("数据异常"));
		}
		Map<String, String> result = new HashMap<>();
		List<ForbiddenWords>  forbiddenWordslist = itemValidateProxy.getForbiddenWords();		
		for (ExcelEditSkuDTO dto : excelSkuList) {
			if (dto.getExcelOpStatus() != 1) {
				result.put(dto.getSku(), dto.getExcelOpmessage());
				continue;
			}		
			if (StringUtil.isEmpty(dto.getSku())) {
				LOGGER.error("SKU为空,数据无效");
				continue;
			}
			if (StringUtil.isEmpty(dto.getMainTitle()) && StringUtil.isEmpty(dto.getBasicPrice()) && StringUtil.isEmpty(dto.getDetailBasicPrice())) {
				result.put(dto.getSku(), "更新数据为空");
				continue;
			}
			if (StringUtil.isNotEmpty(dto.getBasicPrice()) && Double.valueOf(dto.getBasicPrice()) <= 0) {
				result.put(dto.getSku(), "市场价非法");
				continue;
			}	
			if (StringUtil.isNotEmpty(dto.getDetailBasicPrice()) && Double.valueOf(dto.getDetailBasicPrice()) <= 0) {
				result.put(dto.getSku(), "商品详情市场价非法");
				continue;
			}	
			if (StringUtil.isNotEmpty(dto.getMainTitle()) && dto.getMainTitle().trim().length() > 60) {
				result.put(dto.getSku(), "网站显示标题长度不能大于60");
				continue;
			}
			if (StringUtil.isNotEmpty(dto.getMainTitle())) {
				String forbiddenCheck = itemValidateProxy.checkForbiddenWordsField(dto.getMainTitle(),"网站显示名",forbiddenWordslist);
				if (StringUtil.isNotEmpty(forbiddenCheck)) {
					result.put(dto.getSku(), forbiddenCheck);
					continue;
				}
			}		
			if (StringUtil.isEmpty(dto.getMainTitle())) {
				dto.setMainTitle(null);
			}
			if (StringUtil.isEmpty(dto.getBasicPrice())) {
				dto.setBasicPrice(null);
			}
			if (StringUtil.isEmpty(dto.getDetailBasicPrice())) {
				dto.setDetailBasicPrice(null);
			}
			ItemSkuModifyDto skuDto = new ItemSkuModifyDto();
			org.springframework.beans.BeanUtils.copyProperties(dto,skuDto);
			modifySkuList.add(skuDto);
		}
		return new ResultInfo<>(result);
	}
	/**
	 * 导入商品详情（翻译）
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public Map<String, Object> uploadDetailExcelToServer(HttpServletRequest request,String fileName,String userName) throws Exception{
		String path = request.getSession().getServletContext().getRealPath("upload");
		Map<String, Object> retMap = new HashMap<String, Object>();
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
			
			List<ExcelItemDetailForTransDto> excelList = readExcelListNew(retFile);
			if(CollectionUtils.isNotEmpty(excelList)){
				LOGGER.info("导入的excel模板总共有:{} 行数据" +excelList.size());
				if(excelList.size()>1000){
					retMap.put(SUCCESS_KEY, false);
					retMap.put(MESSAGE_KEY, "Excel行数不能超过1000行");
					return retMap;
				}
			}
			ItemImportLog importLogDO = initItemImportLog(userName);
			List<ItemDetailImport> importList = new ArrayList<ItemDetailImport>();
			for(ExcelItemDetailForTransDto itemDetailForTransDto:excelList){
				ItemDetailImport itemDetailImport = new ItemDetailImport();
				BeanUtil.copyProperties(itemDetailImport, itemDetailForTransDto);
				itemDetailImport.setCreateTime(new Date());
				itemDetailImport.setCreateUser(userName);
				itemDetailImport.setItemDetailDecs(itemDetailForTransDto.getItemDetailDesc());
				importList.add(itemDetailImport);
			}
			//批量保存log    batchInsert
			ItemDetailImportLogDto itemDetailImportLogDto = new ItemDetailImportLogDto();
			itemDetailImportLogDto.setItemImportLog(importLogDO);
			itemDetailImportLogDto.setImportList(importList);
			/** 建表：导入商品详情日志表 **/
			try {
				itemDetailImportLogDto =itemDetailService.saveImportLogDto(itemDetailImportLogDto);
				retMap.put("logId", itemDetailImportLogDto.getItemImportLog().getId());
				/** 导入数据拼接，封装，保存 *///异步处理
				sysnImportItemDetailFortrans(retFile,itemDetailImportLogDto.getItemImportLog().getId() ,excelList , userName);
			}catch(Exception e){
				LOGGER.info("上传Excel记录日志表出错,出错原因: {}" +e.getMessage());
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
		}
		
		return retMap;
	}
	
	public void sysnImportItemDetailFortrans(final File retFile,final Long logId,List<ExcelItemDetailForTransDto> excelList,final String userName){
		try {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					Long start = System.currentTimeMillis();
					//加锁
					boolean lock = jedisCacheUtil.lock(RUN_WORK_KEY_NEW);// 获得锁
					LOGGER.info("=查看锁==backend-front-importItemDetail=={} ，logId：{}",lock,logId);
					if(!lock){
						//获得锁的次数
						int count = 0;
						while(true){
							 if(count>300){
								//处理超时，重新上传模板...
								itemImportService.updateImportLogStatus(logId, 5);//处理超时
								return;
							 }
							 lock = jedisCacheUtil.lock(RUN_WORK_KEY_NEW);
							 //锁的超时时间
							 jedisCacheUtil.setKeyExpire(RUN_WORK_KEY_NEW,300);//5 min
							 if(lock){
								 break;
							 }
							 count++;
							 ThreadUtil.sleep(1000L);
						}
					}else{
						jedisCacheUtil.setKeyExpire(RUN_WORK_KEY_NEW,300);//5 min
					}

					Map <Long, ExcelItemDetailForTransDto> validSucessMap = new HashMap<Long, ExcelItemDetailForTransDto>();
					Map <Long, ExcelItemDetailForTransDto> validFailMap = new HashMap<Long, ExcelItemDetailForTransDto>();
					try {
						//正在处理
						itemImportService.updateImportLogStatus(logId, 2);//正在处理
//						itemDetailService.saveItemDetailForTrans(excelList);
						//通用的校验模块，只校验excel的数据类型与长度....
						
						validAndSaveDataNew(retFile,logId,validSucessMap,validFailMap,userName);
						// 删除上传的附件...
						if (retFile.exists()) {
							retFile.delete();
						}
					} catch (IllegalAccessException e) {
						LOGGER.error(e.getMessage(), e);
					} catch (InvocationTargetException e) {
						LOGGER.error(e.getMessage(), e);
					} catch(Exception e){
						LOGGER.error(e.getMessage());
					}finally {
						jedisCacheUtil.unLock(RUN_WORK_KEY_NEW);// 释放锁
						LOGGER.info("导入、解析、校验、保存excel耗时:  {}",
								((System.currentTimeMillis() - start)));
					}
				}
			};
			//执行线程....
			ThreadUtil.excAsync(r,false);
		} catch (ItemServiceException e) {
			jedisCacheUtil.unLock(RUN_WORK_KEY_NEW);// 释放锁
			LOGGER.error("上传的excel批量插入日志表出错,错误信息如下：{}  ", e.getMessage());
		}finally{
			jedisCacheUtil.unLock(RUN_WORK_KEY_NEW);// 释放锁
		}
	}
	
	private void validAndSaveDataNew(File retFile,Long logId,Map<Long,ExcelItemDetailForTransDto> validSucessMap,Map<Long,ExcelItemDetailForTransDto> validFailMap,String userName) 
			throws Exception {
		ItemImportLog importLogDO = new ItemImportLog();
		importLogDO.setId(logId);
		List<ExcelItemDetailForTransDto> excelItemDetailList = readExcelListNew(retFile);
		// 拆分excel
		splitExcelList(excelItemDetailList,validSucessMap,validFailMap);
		// 校验sku信息(必填的id是否存在...)
		Long start = System.currentTimeMillis();
		validExcelDataNew(validSucessMap,validFailMap);
		LOGGER.info("valid all itemDetail cost: ",System.currentTimeMillis()-start);
		// 转换校验正确的值
		List<ExcelItemDetailForTransDto> list = new ArrayList<ExcelItemDetailForTransDto>(validSucessMap.values());
//		importSkuData(list,logId,validSucessMap,validFailMap,userName,importFrom);
		if(null!=list && list.size()>0){
			itemDetailService.saveItemDetailForTrans(list);
		}
		List<ExcelItemDetailForTransDto> failList = new ArrayList<ExcelItemDetailForTransDto>(validFailMap.values());
		if(CollectionUtils.isNotEmpty(failList) ){
			for(ExcelItemDetailForTransDto itemDetailForTransDto:failList){
				Map<String, Object> params = new HashMap<String,Object>();
				params.put("log_id", logId);
				params.put("detail_id", itemDetailForTransDto.getDetailId());
				ItemDetailImport itemDetailImport = itemDetailImportService.queryUniqueByParams(params);
				itemDetailImport.setStatus(1);//导入失败-1，成功-0
				itemDetailImport.setOpMessage(itemDetailForTransDto.getExcelOpmessage());
				itemDetailImportService.updateById(itemDetailImport);
			}
		}
		Integer successNum = list.size();
		Integer failNum = failList.size();
		ItemImportLog itemImportLog = itemImportLogService.queryById(logId);
		itemImportLog.setSuccessCount(successNum);
		itemImportLog.setFailCount(failNum);
		itemImportLogService.updateById(itemImportLog);
	}
	/**
	 * 
	 * <pre>
	 * 拆分导入的excel模板
	 * </pre>
	 * 
	 * @param excelSkuList
	 */
	private void splitExcelList(List<ExcelItemDetailForTransDto> excelItemDetailList,Map<Long,ExcelItemDetailForTransDto> validSucessMap,Map<Long,ExcelItemDetailForTransDto> validFailMap) {
		for (ExcelItemDetailForTransDto itemDetail : excelItemDetailList) {
			if (itemDetail.getExcelOpStatus() == Short.parseShort("1")) {
				validSucessMap.put(itemDetail.getExcelIndex(), itemDetail);
			} else {
				validFailMap.put(itemDetail.getExcelIndex(), itemDetail);
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
	private void validExcelDataNew(Map<Long,ExcelItemDetailForTransDto> excelMap,Map<Long,ExcelItemDetailForTransDto> validFailMap) {
		if (null != excelMap && !excelMap.isEmpty()) {
			Set<Long> key = excelMap.keySet();
			
			List<ForbiddenWords>  forbiddenWordslist = itemValidateProxy.getForbiddenWords() ;
			for (Iterator<Long> it = key.iterator(); it.hasNext();) {
				Long excelIndex = (Long) it.next();
				ExcelItemDetailForTransDto itemDetailForTransDto = excelMap.get(excelIndex);
				// excel第一步验证后，再与数据库进行匹配一下，判断一些id是否存在，barcode是否唯一
				Long start = System.currentTimeMillis();
//				ExcelItemDetailForTransDto excelItemDetailForTransDto = validAndSetOneSku(itemDetailForTransDto,forbiddenWordslist);
				ExcelItemDetailForTransDto excelItemDetailForTransDto = validAndSetOneItemDetail(itemDetailForTransDto,forbiddenWordslist);
				LOGGER.info("valid one itemDetail  cost : "+ (System.currentTimeMillis() - start));
				if (excelItemDetailForTransDto.getExcelOpStatus() != Short.parseShort("1")) {
					it.remove();
					validFailMap.put(excelIndex, excelItemDetailForTransDto);
				}
			}
		}
	}
	
	/**
	 * 
	 * <pre>
	 *  校验并且获取值
	 * </pre>
	 * 
	 * @return
	 */
	private ExcelItemDetailForTransDto validAndSetOneItemDetail(ExcelItemDetailForTransDto itemDetailForTransDto,List<ForbiddenWords>  forbiddenWordslist) {
		
		Long start = System.currentTimeMillis();
		if(null == itemDetailForTransDto.getDetailId() || 0L == itemDetailForTransDto.getDetailId() ){
			itemDetailForTransDto.setExcelOpStatus(2);
			itemDetailForTransDto.setExcelOpmessage(itemDetailForTransDto.getExcelOpmessage() + "id不能为空。\n");
			return itemDetailForTransDto;
		}
		if(StringUtil.isBlank(itemDetailForTransDto.getSku())){
			itemDetailForTransDto.setExcelOpStatus(2);
			itemDetailForTransDto.setExcelOpmessage(itemDetailForTransDto.getExcelOpmessage() + "sku不能为空。\n");
			return itemDetailForTransDto;
		}
		if(StringUtil.isBlank(itemDetailForTransDto.getBrandName())){
			itemDetailForTransDto.setExcelOpStatus(2);
			itemDetailForTransDto.setExcelOpmessage(itemDetailForTransDto.getExcelOpmessage() + "品牌名称不能为空。\n");
			return itemDetailForTransDto;
		}
		if(StringUtil.isBlank(itemDetailForTransDto.getBrandStory())){
			itemDetailForTransDto.setExcelOpStatus(2);
			itemDetailForTransDto.setExcelOpmessage(itemDetailForTransDto.getExcelOpmessage() + "品牌故事不能为空。\n");
			return itemDetailForTransDto;
		}
		if(StringUtil.isBlank(itemDetailForTransDto.getItemTitle())){
			itemDetailForTransDto.setExcelOpStatus(2);
			itemDetailForTransDto.setExcelOpmessage(itemDetailForTransDto.getExcelOpmessage() + "商品名称不能为空。\n");
			return itemDetailForTransDto;
		}
		LOGGER.info("valid itemDetai : " + (System.currentTimeMillis()-start));
		
		return itemDetailForTransDto;
	}
}
