package com.tp.proxy.ord;

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
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.BseConstant.DictionaryCode;
import com.tp.common.vo.BseConstant.TaxRateEnum;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.common.vo.supplier.CommonUtil;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.dfsutils.constants.MetaDataKey;
import com.tp.dfsutils.file.TxtFile;
import com.tp.dfsutils.service.DfsService;
import com.tp.dto.prd.SkuImportLogDto;
import com.tp.dto.prd.excel.ExcelEditSkuDTO;
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
import com.tp.model.bse.Spec;
import com.tp.model.bse.SpecGroup;
import com.tp.model.bse.TaxRate;
import com.tp.model.mmp.FreightTemplate;
import com.tp.model.ord.OrderImportLog;
import com.tp.model.prd.ItemImportList;
import com.tp.model.prd.ItemImportLog;
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
import com.tp.service.ord.IOrderImportInfoService;
import com.tp.service.prd.IItemManageService;
import com.tp.service.sup.IPurchasingManagementService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.util.ExcelUtil;
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
public class OrderImportProxy {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderImportValidateProxy.class);

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
	private IOrderImportInfoService orderImportInfoService;


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
	private IPurchasingManagementService purchasingManagementService;

	@Autowired
	private IFreightTemplateService freightTemplateService;
	
	@Autowired
	private SupplierUserProxy  supplierUserProxy;
	@Autowired
	private TaxRateProxy taxRateProxy;
	
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	@Autowired
	private IUploadService uploadService;
	
	@Autowired
	private IClearanceChannelsService clearanceChannelsService;
	
	private final static String excelPath = "template/sku-template.xls";
	private final static String hitaoExcelPath="template/hitao-sku-template.xls";
	private final static String RUN_WORK_KEY = "backend-front-importSku";
	private final static String WAVESIGN = "isWave" ;//是否海淘模板

	
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
//				try {
//					final OrderImportLog orderImportLog =orderImportInfoService.saveImportLogDto(orderImportLog);
//					retMap.put("logId", orderImportLog.getId());
//					sysnImportWaveSkuExcel(retFile,orderImportLog.getId(),userName,importFrom);//导入
//				}catch(Exception e){
//					LOGGER.info("上传Excel记录日志表出错,出错原因: {}" +e.getMessage());
//					retMap.put(SUCCESS_KEY, false);
//					retMap.put(MESSAGE_KEY, fileSizeCheckMap.get(MESSAGE_KEY));
//					return retMap;
//				}
				
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
				OrderImportLog skuItemImportLogDto = new OrderImportLog();
				try {
					skuItemImportLogDto =orderImportInfoService.saveImportLogDto(skuItemImportLogDto);
					retMap.put("logId", skuItemImportLogDto.getId());
					sysnImportExcel(retFile,skuItemImportLogDto.getId(),userName,importFrom);//导入
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
		return  orderImportInfoService.queryOrderImport(createUser,status,pageNo, pageSize);
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
								//orderImportInfoService.updateImportLogStatus(logId, 5);//处理超时
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
						//orderImportInfoService.updateImportLogStatus(logId, 2);//正在处理
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
								//orderImportInfoService.updateImportLogStatus(logId, 5);//处理超时
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
						//orderImportInfoService.updateImportLogStatus(logId, 2);//正在处理
						//validWaveSkuAndSaveData(retFile,logId,validSucessMap,validFailMap,userName,userId);
						// 删除上传的附件...
						if (retFile.exists()) {
							retFile.delete();
						}
					}catch(Exception e){
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
		// 校验sku信息(必填的id是否存在...)
		Long start = System.currentTimeMillis();
		validExcelData(validSucessMap,validFailMap);
		LOGGER.info("valid all skus cost: ",System.currentTimeMillis()-start);
		// 转换校验正确的值
		List<ExcelSkuDTO> list = new ArrayList<ExcelSkuDTO>(
				validSucessMap.values());
		//保存
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
			
		}
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
//		SkuImportLogDto skuItemImportLogDto = orderImportInfoService.querySkuImportById(
//				id, status);
		List<ItemImportList> importLists;//;
		return null;
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
   			Long supplierUserId=(Long) request.getSession().getAttribute("USER_ID_KEY");
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
		String savePath =  OrderImportValidateProxy.class.getResource("/").getPath()+excelPath;
		if(waveSign.equals(WAVESIGN)){
			savePath =  OrderImportValidateProxy.class.getResource("/").getPath()+hitaoExcelPath;
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
//	public OrderImportLogDto queryImportLogDto(String createUser,Integer status, Integer pageNo,
//			Integer pageSize) {
//		return  orderImportInfoService(createUser,status,	pageNo, pageSize);
//	}

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
	
}
