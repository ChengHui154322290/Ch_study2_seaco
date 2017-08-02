package com.tp.proxy.wms;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tp.common.util.mmp.AssertUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.supplier.CommonUtil;
import com.tp.common.vo.supplier.entry.OrderStatus;
import com.tp.dfsutils.constants.MetaDataKey;
import com.tp.dfsutils.file.TxtFile;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.wms.SendOrderInfo;
import com.tp.dto.wms.excel.StockinImportDetailDto;
import com.tp.exception.ExcelContentInvalidException;
import com.tp.exception.ExcelParseException;
import com.tp.exception.ExcelRegexpValidFailedException;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.PurchaseInfo;
import com.tp.model.sup.PurchaseProduct;
import com.tp.model.sup.PurchaseWarehouse;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.usr.UserInfo;
import com.tp.model.wms.Stockasn;
import com.tp.model.wms.StockasnDetail;
import com.tp.model.wms.StockasnFact;
import com.tp.model.wms.StockinImportLog;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.IBaseService;
import com.tp.service.IUploadService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.sup.IPurchaseInfoService;
import com.tp.service.sup.IPurchaseProductService;
import com.tp.service.sup.IPurchaseWarehouseService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.service.wms.IStockasnDetailService;
import com.tp.service.wms.IStockasnFactService;
import com.tp.service.wms.IStockasnService;
import com.tp.service.wms.IStockinImportLogService;
import com.tp.util.ExcelUtil;

/**
 * 入库订单代理层
 *
 * @author szy
 */
@Service
public class StockasnProxy extends BaseProxy<Stockasn> {

	private static final Logger LOGGER = LoggerFactory.getLogger(StockasnProxy.class);
	/** 返回消息key */
	public static final String SUCCESS_KEY = "success";

	/** 返回消息key */
	public static final String MESSAGE_KEY = "message";

	/** 文件参数key */
	public static final String UPLOADED_FILE_KEY = "uploaded_file_key";

	/** 文件最大值 */
	public static final Long MAX_FILE_SIZE = 20194300L;

	/** 文件上传者的信息 */
	public static final String UPLOAD_CREATOR = "stockin_mode";

	/** 上传excel文件名 */
	private String realFileName = "";

	/** excel 中行数 */
	private Integer sumCount = 0;

	/** 密钥 */
	private String secretKey = "";

	/** 文件服务器上的唯一的文件名 */
	private String uniqueFileName = "";

	@Autowired
	private IUploadService uploadService;

	@Autowired
	private IStockasnService stockasnService;

	@Autowired
	private IPurchaseProductService purchaseProductService;

	@Autowired
	private IPurchaseInfoService purchaseInfoService;

	@Autowired
	private IWarehouseService warehouseService;

	@Autowired
	private ISupplierInfoService supplierInfoService;

	@Autowired
	private IPurchaseWarehouseService purchaseWarehouseService;

	@Autowired
	private IStockinImportLogService stockinImportLogService;

	@Autowired
	private IStockasnDetailService stockasnDetailService;
	
	@Autowired
	private IStockasnFactService stockasnFactService;
	
	@Autowired
	private JedisCacheUtil jedisCacheUtil;

	private final static String RUN_WORK_KEY = "backend-front-importStockin";

	@Override
	public IBaseService<Stockasn> getService() {
		return stockasnService;
	}

	public ResultInfo<String> sendOrderInfo(PurchaseWarehouse purchaseWarehouse, UserInfo user) {
		final ResultInfo<String> result = new ResultInfo<>();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				AssertUtil.notNull(purchaseWarehouse, "仓库预约单为空");
				SendOrderInfo info = new SendOrderInfo();
				info.setPurchaseWarehouse(purchaseWarehouse);

				PurchaseInfo purchaseInfo = purchaseInfoService.queryById(purchaseWarehouse.getPurchaseId());
				AssertUtil.notNull(purchaseInfo, "采购订单为空");
				info.setPurchaseInfo(purchaseInfo);

				Map<String, Object> params = new HashMap<>();
				params.put("purchaseId", purchaseWarehouse.getPurchaseId());
				params.put("status", Constant.ENABLED.YES);
				List<PurchaseProduct> purchaseProducts = purchaseProductService.queryByParam(params);
				info.setPurchaseProducts(purchaseProducts);

				SupplierInfo supplierInfo = supplierInfoService.queryById(purchaseWarehouse.getSupplierId());
				AssertUtil.notNull(supplierInfo, "供应商信息为空");
				info.setSupplierInfo(supplierInfo);

				Warehouse warehouse = warehouseService.queryById(purchaseWarehouse.getWarehouseId());
				AssertUtil.notNull(warehouse, "仓库信息为空");
				info.setWarehouse(warehouse);

				info.setUser(user);

				ResultInfo<Object> sendResult = stockasnService.sentWarehouseOrder(info);

				if (sendResult != null && sendResult.isSuccess()) {
					PurchaseWarehouse purchaseWarehoused = new PurchaseWarehouse();
					purchaseWarehoused.setAuditStatus(OrderStatus.SUCCESS.getStatus());
					purchaseWarehoused.setId(purchaseWarehouse.getId());
					purchaseWarehouseService.updateNotNullById(purchaseWarehoused);
				}

				result.setMsg(sendResult.getMsg());
				result.setSuccess(sendResult.isSuccess());

			}
		});

		return result;

	}

	/**
	 * 解析文件、验证数据。。。
	 * 
	 * @param request
	 * @param fileName
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public ResultInfo<Boolean> uploadExcelToServer(HttpServletRequest request, String fileName, String userName,Integer stockasnId,String uploadToken)
			throws Exception {
		String path = request.getSession().getServletContext().getRealPath("upload");
		// 上传附件并统计excel行数...
		if (!(request instanceof MultipartHttpServletRequest)) {
			return new ResultInfo<>(new FailInfo("请上传正确的文件"));
		}	
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		String newName = generateFileName();
		ResultInfo<File> fileResult = getUploadFile(path, fileName, newName, multipartRequest);
		if(!fileResult.isSuccess() && fileResult.getData() == null ){
			return new ResultInfo<>(fileResult.getMsg());
		}
		//
		File file = fileResult.getData();
		uniqueFileName = uploadFile(file);
		FailInfo failInfo = validateExcel(file);
		if (failInfo != null) return new ResultInfo<>(failInfo);
		return sysnImportStockinExcel(file, userName, newName,stockasnId,uploadToken);// 导入
	}

	
	private ResultInfo<File> getUploadFile(String path, String fileName, String newName, MultipartHttpServletRequest multipartRequest) throws IOException{
		MultipartFile multipartFile = multipartRequest.getFile(fileName);
		if (null == multipartFile || multipartFile.isEmpty()) {
			LOGGER.info("找不到文件：" + fileName);
			return new ResultInfo<>(new FailInfo("找不到文件"));
		}

		long fileSize = multipartFile.getSize();
		// 上传的文件名
		realFileName = multipartFile.getOriginalFilename();
		FailInfo failInfo = checkFileSize(fileSize, multipartFile.getOriginalFilename());
		if(failInfo != null) return new ResultInfo<>(failInfo);
		
		
		String format = CommonUtil.getFileFormat(multipartFile.getOriginalFilename());
		File destFile = new File(path);
		if (!destFile.exists()) {
			destFile.mkdirs();
		}

		final File retFile = new File(destFile, newName + "." + format);
		FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), retFile);
		return new ResultInfo<>(retFile);
	}
	
	/**
	 * 校验文件大小
	 *
	 * @param fileSize
	 * @param fileName
	 * @return
	 */
	public static FailInfo checkFileSize(final long fileSize, final String fileName) {
		// excel大小
		if (fileSize > StockasnProxy.MAX_FILE_SIZE.longValue()) {
			return new FailInfo("excel文件大小不能超过20M");
		}
		return null;
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
	 * 校验
	 * 
	 * @param retFile
	 * @param retMap
	 *            : 校验结果
	 */
	private FailInfo validateExcel(File retFile) throws Exception {
		try {
			ExcelUtil el = ExcelUtil.readValidateExcel(retFile, 0, 1);
			el.toEntitys(StockinImportDetailDto.class);
			return null;
		} catch (Exception e) {
			logger.error("校验excel出错", e);
			return new FailInfo("校验excel异常");
		}
	}

	private List<StockinImportDetailDto> readExcelList(File file)
			throws org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		long start = System.currentTimeMillis();
		List<StockinImportDetailDto> entitys = null;
		try {
			ExcelUtil eh = ExcelUtil.readExcel(file, 0);
			sumCount = eh.getDatas().length;
			entitys = eh.toEntitys(StockinImportDetailDto.class);
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

	public ResultInfo<Boolean> sysnImportStockinExcel( File retFile, String userName, String newName,Integer stockasnId,String uploadToken) {
		try {
			// 加锁
			boolean lock = jedisCacheUtil.lock(RUN_WORK_KEY);// 获得锁
			LOGGER.info("=查看锁==backend-front-importStockin=={} ", lock);
			if (!lock) return new ResultInfo<>(new FailInfo("正在导入，请稍后再试！"));
			
			List<StockinImportDetailDto> excelList = readExcelList(retFile);
			FailInfo failInfo = validImportStockinData(excelList, userName, newName,stockasnId,uploadToken);	
			if (failInfo != null){
				logger.error("入库单导入异常：{}", failInfo.getDetailMessage());
				return new ResultInfo<>(failInfo);
			}
			jedisCacheUtil.setCache("token", uploadToken);
			// 保存数据到表里：importLog表 、importDetail表、fact表 、detailFact表
			stockasnFactService.saveImportDetailAndImportLog(newName, userName, excelList,
					Long.valueOf(stockasnId),realFileName,secretKey,uploadToken);
			return new ResultInfo<>(Boolean.TRUE);	
		} catch (Exception e) {
			LOGGER.error("入库单导入异常", e);
		} finally {
			jedisCacheUtil.unLock(RUN_WORK_KEY);// 释放锁
			LOGGER.info("=释放锁==backend-front-importStockin ");
		}
		return new ResultInfo<>(new FailInfo("导入异常"));
	}

	/**
	 * 
	 * <pre>
	 * 校验并保存导入信息
	 * </pre>
	 * 
	 * @param retFile
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private FailInfo validImportStockinData(List<StockinImportDetailDto> excelList, String userName, String newName,Integer stockasnId,String token) throws Exception {		
		if (CollectionUtils.isEmpty(excelList)) {
			return new FailInfo("Excel数据为空");
		}
		
		LOGGER.info("导入的excel模板总共有:{} 行数据" + excelList.size());
		if (excelList.size() > 1000) {
			return new FailInfo("Excel行数不能超过1000行");
		}
		
		String purchaseCode = excelList.get(0).getPurchaseCode();//第一行数据的采购单号
		String warehouseCode = excelList.get(0).getWarehouseCode();//第一行数据的仓库编号
		String warehouseId = excelList.get(0).getWarehouseId();//第一行数据的仓库id
		
		
		//判断token列 是否只有一个值
		for(StockinImportDetailDto stockinImportDetailDto : excelList ){
			if(!stockinImportDetailDto.getPurchaseCode().equals(purchaseCode)){
				return new FailInfo("excel表格中“*采购单号”列存在不同值");
			}
			if(!stockinImportDetailDto.getWarehouseCode().equals(warehouseCode)){
				return new FailInfo("excel表格中“*仓库编号”列存在不同值");
			}
			if(!stockinImportDetailDto.getWarehouseId().equals(warehouseId)){
				return new FailInfo("excel表格中“仓库ID”列存在不同值");
			}
		}
		// 校验token
		if(token.equals(jedisCacheUtil.getCache("token"))){
			return new FailInfo("导入token已存在");
		}
		// 校验log表中的token
		StockinImportLog stockinImportLog = new StockinImportLog();
		stockinImportLog.setUploadToken(token);
		List<StockinImportLog> importLogList = stockinImportLogService.queryByObject(stockinImportLog);
		if (importLogList != null && importLogList.size() != 0) {
			return new FailInfo("导入token已存在");
		}
		// 校验入库单是否有数据
		Stockasn stockasn = stockasnService.queryById(Long.valueOf(stockasnId));
		if(stockasn ==null ){
			return new FailInfo("不存在对应的入库单");
		}
		//校验表格中的采购单号字段
		if(!purchaseCode.equals(stockasn.getOrderCode())){
			return new FailInfo("导入数据中的采购单号有误");
		}
		//校验表格中的仓库id和仓库编号字段
		if(!warehouseCode.equals(stockasn.getWarehouseCode()) || !warehouseId.equals(String.valueOf(stockasn.getWarehouseId()))){
			return new FailInfo("导入数据中的仓库编号或仓库id有误");
		}
		// 校验入库明细是否有数据
		StockasnDetail stockasnDetail = new StockasnDetail();
		stockasnDetail.setStockasnId(Long.valueOf(stockasnId));
		List<StockasnDetail> StockasnDetails = stockasnDetailService.queryByObject(stockasnDetail);
		if(StockasnDetails == null || StockasnDetails.size() == 0){
			return new FailInfo( "不存在对应的入库单明细");
		}
		//校验导入数据中的sku字段
		Map<String,Object> map = new HashMap<String,Object>();
		for(int i=0;i<StockasnDetails.size();i++){
			map.put(String.valueOf(i), StockasnDetails.get(i).getItemSku());
		}
		for(StockinImportDetailDto stockinImportDetailDto : excelList ){
			if(!map.containsValue(stockinImportDetailDto.getSkuCode())){
				return new FailInfo("导入数据中的商品SKU有误");
			}
		}
		// 校验入库单反馈中是否有数据
		StockasnFact stockasnFact = new StockasnFact();
		stockasnFact.setStockasnId(Long.valueOf(stockasnId));
		List<StockasnFact> stockasnFacts = stockasnFactService.queryByObject(stockasnFact);
		if(stockasnFacts !=null && stockasnFacts.size() != 0){
			return new FailInfo("入库单反馈已存在");
		}
		return null;
	}


}
