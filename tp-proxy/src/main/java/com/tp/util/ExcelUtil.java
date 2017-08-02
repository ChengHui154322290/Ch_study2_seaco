package com.tp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tp.common.annotation.excel.poi.ExcelEntity;
import com.tp.common.annotation.excel.poi.ExcelProperty;
import com.tp.common.annotation.excel.poi.ExcelRule;
import com.tp.common.annotation.excel.poi.ExcelType;
import com.tp.dto.prd.excel.ExcelBaseDTO;
import com.tp.exception.ExcelContentInvalidException;
import com.tp.exception.ExcelParseException;
import com.tp.exception.ExcelRegexpValidFailedException;
import com.tp.util.StringUtil;

/**
 * 
 * <pre>
 * 		excel工具类：主要负责读写excel模板信息
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class ExcelUtil {

	private final static Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);
	/** 最小列数目 */
	private static final int MIN_ROW_COLUMN_COUNT = 1;
	/** 列索引 */
	private int lastColumnIndex;
	/** 从Excel中读取的标题栏 */
	private String[] headers = null;
	/** 从Excel中读取的数据 */
	private String[][] datas = null;
	
	/** 规则对象缓存 */
	@SuppressWarnings("rawtypes")
	private static Map<String, ExcelRule> rulesCache = new HashMap<String, ExcelRule>();

	@SuppressWarnings("rawtypes")
	private static List<Class<? extends ExcelType>> userDefinedType = new ArrayList<Class<? extends ExcelType>>();

	
    private static ExcelUtil excelUtil = null;  
    
    private ExcelUtil() {  
    }  
   
    public static ExcelUtil getInstance() {  
       if (excelUtil == null) {  
    	   excelUtil = new ExcelUtil();  
       }  
       return excelUtil;  
    }  

	/**
	 * 
	 * <pre>
	 *    注册新字段类型
	 * </pre>
	 *
	 * @param type
	 * @throws ExcelParseException
	 */
	public static void registerNewType(
			@SuppressWarnings("rawtypes") Class<? extends ExcelType> type)
			throws ExcelParseException {
		if (!userDefinedType.contains(type)) {
			userDefinedType.add(type);
		}
	}

	/**
	 * 
	 * <pre>
	 *   转换为实体
	 * </pre>
	 *
	 * @param classType
	 * @param excelDatas
	 * @return
	 * @throws ExcelParseException
	 * @throws ExcelContentInvalidException
	 * @throws ExcelRegexpValidFailedException
	 */
	public <T extends ExcelBaseDTO> List<T> toEntitys(Class<T> classType)
			throws ExcelParseException, ExcelContentInvalidException,
			ExcelRegexpValidFailedException {
		// 如果实体没有@ExcelEntity，则不允许继续操作
		ExcelEntity excelEntity = classType.getAnnotation(ExcelEntity.class);
		if (excelEntity == null) {
			throw new ExcelParseException(0,"转换的实体必须存在@ExcelEntity!");
		}
		// 创建Excel实体字段信息
		List<ExcelEntityField> eefs = getEntityFields(classType);

		// 创建实体对象集
		List<T> entitys = new ArrayList<T>();
		try {
			// 遍历提交的数据行，依次填充到创建的实体对象中
			Long rowIndex = 0l ;
			for (String[] data : datas) {
				rowIndex++;
				T obj = classType.newInstance();
				Object superObj = null;
				Class<?> superClass = classType.getSuperclass();
				if(superClass != ExcelBaseDTO.class){
					superObj = superClass.newInstance();
				}
				ExcelBaseDTO base = (ExcelBaseDTO)obj;
				base.setCreateTime(new Date());
				base.setExcelIndex(rowIndex);
				StringBuilder sBuilder = new StringBuilder();
				// 遍历实体对象的实体字段，通过反射为实体字段赋值
				for (ExcelEntityField eef : eefs) {
					// 如果字段非必须同时字段内容为空，则跳过，不需要进行填充
					// 如果字段非必填，但是内容不为空，还是需要校验填写的
					// 实体数据填充
					try {
						Method method = null;
						if(eef.getIsSuper()==0){
							method = obj.getClass().getDeclaredMethod("set"+ _toCapitalizeCamelCase(eef.getField().getName()),eef.getField().getType());
						}else{
							method = superObj.getClass().getDeclaredMethod("set"+ _toCapitalizeCamelCase(eef.getField().getName()),eef.getField().getType());
						}
						method.invoke(obj,getFieldValue(data[eef.getIndex()], eef));
					} catch (ExcelParseException e) {
						if(e.getType()==2){
							sBuilder.append(e.getMessage());
						}
						if (eef.isRequired()) {
							sBuilder.append("字段"+ eef.getColumnName() + ", 字段值为： "
												 +data[eef.getIndex()]+"出错! 出错原因：" +e.getMessage() +" ;\n");
							//throw new ExcelParseException("字段" + eef.getColumnName() + "出错!", e);
						}
						continue;
					} catch (ExcelContentInvalidException e) {
						if (eef.isRequired()) {
							sBuilder.append("字段"+ eef.getColumnName() + ", 字段值为： "
						                          +data[eef.getIndex()]+ "出错! 出错原因：" +e.getMessage() +" ;\n");
							//throw new ExcelContentInvalidException("字段" + eef.getColumnName() + "出错!", e);
						}
						continue;
					} catch (NullPointerException e) {
						if (eef.isRequired()) {
							sBuilder.append("字段"+ eef.getColumnName()+ ", 字段值为： "+data[eef.getIndex()] 
									         + "出错! 出错原因：" +e.getMessage() +" ;");
							//throw new ExcelParseException("字段" + eef.getColumnName() + "出错!", e);
						}
						continue;
					} catch (Exception e) {
						if (eef.isRequired()) {
							sBuilder.append("字段"+ eef.getColumnName()+ ", 字段值为： "+data[eef.getIndex()]
									           + "出错! 出错原因：" +e.getMessage() +" ;\n");
							//throw new ExcelParseException("字段" + eef.getColumnName() + "出错!", e);
						}
						continue;
					}finally{
						
					}
				}
				if(StringUtil.isBlank(sBuilder.toString())){
					base.setExcelOpStatus(Short.parseShort("1"));
				}else{
					base.setExcelOpStatus(Short.parseShort("2"));
				}
				base.setExcelOpmessage(sBuilder.toString());
				entitys.add(obj);
			}
		}catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		return entitys;
	}

	/**
	 * 
	 * <pre>
	 * 获取Excel实体类中的填充字段
	 * </pre>
	 *
	 * @param classType
	 * @return
	 * @throws ExcelParseException
	 */
	private <T> List<ExcelEntityField> getEntityFields(Class<T> classType)
			throws ExcelParseException {
		List<ExcelEntityField> eefs = new ArrayList<ExcelUtil.ExcelEntityField>();
		// 遍历所有字段
		Field[] allFields = classType.getDeclaredFields();
		Class<?> superClass = classType.getSuperclass();
		if(superClass != ExcelBaseDTO.class){
			Field[] allSuperFields = superClass.getDeclaredFields();
			for (Field field : allSuperFields) {
				ExcelProperty excelProperty = field
						.getAnnotation(ExcelProperty.class);
				// 只对含有@ExcelProperty注解的字段进行赋值
				if (excelProperty == null) {
					continue;
				}
				ExcelEntityField eef = new ExcelEntityField();

				String key = excelProperty.value().trim();// Excel Header名
				boolean required = excelProperty.required(); // 该列是否为必须列
				int index = indexOfHeader(key);
				// 如果字段必须，而索引为-1 ，说明没有这一列，抛错
				if (required && index == -1) {
					throw new ExcelParseException(1,"字段" + key + "必须!\n");
				}
				eef.setField(field);
				eef.setColumnName(key);
				eef.setRequired(required);
				eef.setIndex(indexOfHeader(key));
				eef.setAnnotation(excelProperty);
				eef.setIsSuper(1);
				eefs.add(eef);
			}
			
		}
		for (Field field : allFields) {
			ExcelProperty excelProperty = field
					.getAnnotation(ExcelProperty.class);
			// 只对含有@ExcelProperty注解的字段进行赋值
			if (excelProperty == null) {
				continue;
			}
			ExcelEntityField eef = new ExcelEntityField();

			String key = excelProperty.value().trim();// Excel Header名
			boolean required = excelProperty.required(); // 该列是否为必须列

			int index = indexOfHeader(key);
			// 如果字段必须，而索引为-1 ，说明没有这一列，抛错
			if (required && index == -1) {
				throw new ExcelParseException(1,"字段" + key + "必须!\n");
			}
			
			eef.setField(field);
			eef.setColumnName(key);
			eef.setRequired(required);
			eef.setIndex(indexOfHeader(key));
			eef.setAnnotation(excelProperty);
			eef.setIsSuper(0);
			eefs.add(eef);
		}
		return eefs;
	}

	/**
	 * 
	 * <pre>
	 *  获取字段的值，路由不同的字段类型
	 * </pre>
	 *
	 * @param value
	 * @param eef
	 * @return
	 * @throws ExcelParseException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ExcelContentInvalidException
	 * @throws ExcelRegexpValidFailedException
	 */
	@SuppressWarnings("rawtypes")
	private Object getFieldValue(String value, ExcelEntityField eef)
			throws ExcelParseException, InstantiationException,
			IllegalAccessException, ExcelContentInvalidException,
			ExcelRegexpValidFailedException {
		// 进行规则校验
		ExcelProperty annotation = eef.getAnnotation();
		Class<? extends ExcelRule> rule = annotation.rule();

		// 获取解析后的字段结果
		Object result = null;
		try {
			// 是否提交过来的是空值
			// 如果提交值是空值而且含有默认值的话
			// 则让提交过来的空值为默认值
			if (("".equals(value) || (value == null)) && annotation.hasDefaultValue()) {
				value = annotation.defaultValue();
			}
			result = getFieldValue(value, eef.getField(), annotation.regexp());
			
			int coloumLength = annotation.columnLength();//该列的长度
			if(coloumLength != -1 && value.length()>coloumLength){
				throw new ExcelParseException(2,"字段" + annotation.value()+ "的长度不能超过"+coloumLength+"位 \n");
			}
			
		} catch (ExcelRegexpValidFailedException e) {
			// 捕获正则验证失败异常
			String errMsg = annotation.regexpErrorMessage();
			if ("".equals(errMsg)) {
				errMsg = "列 " + eef.getColumnName() + " 没有通过规则验证!\n";
			}
			throw new ExcelContentInvalidException(errMsg, e);
		} catch (NumberFormatException e) {
			throw new ExcelContentInvalidException("列 " + eef.getColumnName() + " 数据类型错误!\n");
		} catch (NullPointerException e) {
			throw new ExcelContentInvalidException("列 " + eef.getColumnName() + " 不能为空!\n");
		}
		/**
		 * 缓存已经实例化过的规则对象，避免每次都重新 创建新的对象的额外消耗
		 */
		ExcelRule ruleObj = null;
		if (rulesCache.containsKey(rule.getName())) {
			ruleObj = rulesCache.get(rule.getName());
		} else {
			ruleObj = rule.newInstance();
			rulesCache.put(rule.getName(), ruleObj);
		}

		// 进行校验
		ruleObj.check(result, eef.getColumnName(), eef.getField().getName());
		result = ruleObj.filter(result, eef.getColumnName(), eef.getField().getName());
		return result;
	}

	/**
	 * 
	 * <pre>
	 * 解析字段类型
	 * </pre>
	 *
	 * @param value
	 * @param field
	 * @param regexp
	 * @return
	 * @throws ExcelParseException
	 * @throws ExcelContentInvalidException
	 * @throws ExcelRegexpValidFailedException
	 */
	@SuppressWarnings("rawtypes")
	private Object getFieldValue(String value, Field field, String regexp)
			throws ExcelParseException, ExcelContentInvalidException,
			ExcelRegexpValidFailedException {
		Class<?> type = field.getType();
		String typeName = type.getName();
		// 字符串
		if ("java.lang.String".equals(typeName)) {
			if (!"".equals(regexp) && !value.matches(regexp)) {
				throw new ExcelRegexpValidFailedException();
			}
			return value;
		}
		// 长整形
		if ("java.lang.Long".equals(typeName) || "long".equals(typeName)) {
			if (!"".equals(regexp) && !value.matches(regexp)) {
				throw new ExcelRegexpValidFailedException();
			}
			return Long.parseLong(value);
		}
		// 整形
		if ("java.lang.Integer".equals(typeName) || "int".equals(typeName)) {
			if (!"".equals(regexp) && !value.matches(regexp)) {
				throw new ExcelRegexpValidFailedException();
			}
			return Integer.parseInt(value);
		}
		// 短整型
		if ("java.lang.Short".equals(typeName) || "short".equals(typeName)) {
			if (!"".equals(regexp) && !value.matches(regexp)) {
				throw new ExcelRegexpValidFailedException();
			}
			return Short.parseShort(value);
		}
		// Date型
		if ("java.util.Date".equals(typeName)) {
			try {
				return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(value);
			} catch (ParseException e) {
				throw new ExcelParseException(0,"日期类型格式有误!\n");
			}
		}
		// Double型
		if ("java.lang.Double".equals(typeName)||"double".equals(typeName)){
			if (!"".equals(regexp) && !value.matches(regexp)) {
				throw new ExcelRegexpValidFailedException();
			}
			return Double.parseDouble(value);
		}
		// Timestamp
		if ("java.sql.Timestamp".equals(typeName)) {
			try {
				return new Timestamp(
						new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
								.parse(value).getTime());
			} catch (ParseException e) {
				throw new ExcelParseException(0,"时间戳类型格式有误!\n");
			}
		}
		// Char型
		if ("java.lang.Character".equals(typeName) || "char".equals(typeName)) {
			if (value.length() == 1) {
				return value.charAt(0);
			}
		}
		// 用户注册的自定义类型
		for (Class<? extends ExcelType> et : userDefinedType) {
			if (et.getName().equals(typeName)) {
				try {
					ExcelType newInstance = et.newInstance();
					return newInstance.parseValue(value);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		throw new ExcelParseException(0,"不支持的字段类型 " + typeName + " !\n");
	}

	/**
	 * 
	 * <pre>
	 * 读取Excel内容
	 * </pre>
	 *
	 * @param excelFilename
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException 
	 */
	public static ExcelUtil readExcel(String excelFilename)
			throws InvalidFormatException, IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		return readExcel(excelFilename, 0);
	}

	/**
	 * 
	 * <pre>
	 * 读取Excel内容
	 * </pre>
	 *
	 * @param excelFilename
	 * @param sheetIndex
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException 
	 */
	public static ExcelUtil readExcel(String excelFilename, int sheetIndex)
			throws InvalidFormatException, IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		return readExcel(new File(excelFilename), sheetIndex);
	}

	/**
	 * 
	 * <pre>
	 * 读取excel
	 * </pre>
	 *
	 * @param file
	 * @param sheetIndex
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException 
	 */
	public static ExcelUtil readExcel(File file, int sheetIndex)
			throws InvalidFormatException, IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		// 读取Excel工作薄
		InputStream in  = new FileInputStream(file);
		Workbook wb = WorkbookFactory.create(in);
		//关闭文件流
		in.close(); 
		return readExcel(wb, sheetIndex);
	}
	
	/**
	 * 
	 * <pre>
	 * 读取excel
	 * </pre>
	 *
	 * @param file
	 * @param sheetIndex
	 * @param rowCounts
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException 
	 */
	public static ExcelUtil readValidateExcel(File file, int sheetIndex,int rowCounts)
			throws InvalidFormatException, IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		// 读取Excel工作薄
		InputStream in  = new FileInputStream(file);
		Workbook wb = WorkbookFactory.create(in);
		//关闭文件流
		in.close(); 
		return readExcel(wb, sheetIndex,rowCounts);
	}
	
	public static  Workbook writeExcel(File file,int sheetIndex,String[] heads,String [] [] datas,List<Long> excelIndexList)
			throws InvalidFormatException, IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		InputStream in  = new FileInputStream(file);
		Workbook wb = WorkbookFactory.create(in);
		return writeExcel(wb, sheetIndex,heads,datas,excelIndexList);
	}

	/**
	 * 
	 * <pre>
	 * 读取Excel内容
	 * </pre>
	 *
	 * @param wb
	 * @param sheetIndex
	 * @return
	 */
	private static ExcelUtil readExcel(Workbook wb, int sheetIndex) {
		ExcelUtil eh = new ExcelUtil();
		// 遍历Excel Sheet， 依次读取里面的内容
		if (sheetIndex > wb.getNumberOfSheets()) {
			return null;
		}
		Sheet sheet = wb.getSheetAt(sheetIndex);
		// 遍历表格的每一行
		int rowStart = sheet.getFirstRowNum();
		// 最小行数为1行
		int rowEnd = sheet.getLastRowNum();
		// 读取EXCEL标题栏
		eh.parseExcelHeader(sheet.getRow(0));
		// 读取EXCEL数据区域内容
		eh.parseExcelData(sheet, rowStart + 1, rowEnd,wb);
		return eh;
	}
	
	/**
	 * 
	 * <pre>
	 * 读取Excel内容
	 * </pre>
	 *
	 * @param wb
	 * @param sheetIndex
	 * @return
	 */
	private static ExcelUtil readExcel(Workbook wb, int sheetIndex,int rowCount) {
		ExcelUtil eh = new ExcelUtil();
		// 遍历Excel Sheet， 依次读取里面的内容
		if (sheetIndex > wb.getNumberOfSheets()) {
			return null;
		}
		Sheet sheet = wb.getSheetAt(sheetIndex);
		// 遍历表格的每一行
		int rowStart = sheet.getFirstRowNum();
		// 读取EXCEL标题栏
		eh.parseExcelHeader(sheet.getRow(0));
		// 读取EXCEL数据区域内容
		eh.parseExcelData(sheet, rowStart + 1, rowCount,wb);
		return eh;
	}
	
	/**
	 * 
	 * <pre>
	 *  写Excel内容
	 * </pre>
	 *
	 * @param wb
	 * @param sheetIndex
	 * @return
	 */
	private static Workbook writeExcel(Workbook wb, int sheetIndex,String [] heads,String[][] datas,List<Long> excelIndexList) {
		ExcelUtil eh = new ExcelUtil();
		// 遍历Excel Sheet
		if (sheetIndex > wb.getNumberOfSheets()) {
			return null;
		}
		Sheet sheet = wb.getSheetAt(sheetIndex);
		// 遍历表格的每一行
		int rowStart = sheet.getFirstRowNum();
		// 最小行数为1行
		int rowEnd = sheet.getLastRowNum();
		// 写EXCEL标题栏
		eh.writeExcelHeader(sheet.getRow(0),heads);
		// 删除
		removeRows(sheet,excelIndexList);

		// 写EXCEL数据区域内容
		eh.writeExcelData(sheet, rowStart + 1, rowEnd,wb,datas);
		return wb;
	}

	/**
	 * 
	 * <pre>
	 * 解析EXCEL标题栏
	 * </pre>
	 *
	 * @param row
	 */
	private void parseExcelHeader(Row row) {
		lastColumnIndex = Math.max(row.getLastCellNum(), MIN_ROW_COLUMN_COUNT);
		headers = new String[lastColumnIndex];
		// 初始化headers，每一列的标题
		for (int columnIndex = 0; columnIndex < lastColumnIndex; columnIndex++) {
			Cell cell = row.getCell(columnIndex, Row.RETURN_BLANK_AS_NULL);
			headers[columnIndex] = getCellValue(cell).trim();
		}
	}

	/**
	 * 
	 * <pre>
	 * 写入EXCEL数据区域内容
	 * </pre>
	 *
	 * @param sheet
	 * @param rowStart
	 * @param rowEnd
	 */
	private void writeExcelData(Sheet sheet, int rowStart, int rowEnd,Workbook wb, String [] [] datas) {
		
		for(int i = 0 ; i < datas.length ; i ++){
			Row row = sheet.getRow(i+1);
			for(int j =0 ; j < datas[i].length ; j++ ){
				Cell cell = row.createCell((short)(lastColumnIndex+j));
				cell.setCellValue(datas[i][j]);
			}
		}
	}
	
	
	/**
	 * 
	 * <pre>
	 * 	删除
	 * </pre>
	 *
	 * @param sheet
	 * @param list
	 */
	private static void removeRows(Sheet sheet, List<Long> list){
		for(int j = 1 ; j <= sheet.getLastRowNum() ; j++){
			if(!checkExsit(j,list)){
				sheet.removeRow(sheet.getRow(j));
			}
		}
		
		int i = sheet.getLastRowNum();
		Row tempRow;
		while(i > 0){
		 i--;
		 tempRow = sheet.getRow(i);
		 if(tempRow == null){
		  sheet.shiftRows(i+1, sheet.getLastRowNum(), -1);  
		 }
		}
		
	}
	
	/**
	 * 
	 * <pre>
	 * 
	 * </pre>
	 *
	 * @param rowNum
	 * @param list
	 * @return
	 */
	private static boolean checkExsit(int rowNum,List<Long> list){
		for(Long excelIndex: list){
			if(rowNum==excelIndex.intValue()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * <pre>
	 *   在excel模板后面增加几列
	 * </pre>
	 *
	 * @param row
	 * @param heads
	 */
	private void writeExcelHeader(Row row,String [] heads) {
		lastColumnIndex = Math.max(row.getLastCellNum(), MIN_ROW_COLUMN_COUNT);
		headers = new String[lastColumnIndex];
		// 初始化headers，每一列的标题
		for(int i =0 ;i < heads.length ; i ++) {
			Cell cell = row.createCell((short)(lastColumnIndex+i));
			cell.setCellValue(heads[i]);
		}
	}
	
	/**
	 * 
	 * <pre>
	 *  写Excel内容
	 * </pre>
	 *
	 * @param wb
	 * @param sheetIndex
	 * @return
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException 
	 */
	public  static Workbook writeExcelTemplate(File templateFile, int sheetIndex,String[][] data1,
			String [][] data2,String [][] data3,String[][] data4,
			String[][] data5,String[][] data6,String[][] data7,
			String[][] data8,String[][] data9,String[][] data10,String[][] data11,String[][] data12,String[][] data13
			)
			throws InvalidFormatException, IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		
		InputStream in  = new FileInputStream(templateFile);
		Workbook wb = WorkbookFactory.create(in);
		
		ExcelUtil eh = new ExcelUtil();
		// 遍历Excel Sheet， 依次读取里面的内容
		if (sheetIndex > wb.getNumberOfSheets()) {
			return null;
		}
		Sheet sheet = wb.getSheetAt(sheetIndex);
		// 遍历表格的每一行
		int rowStart = sheet.getFirstRowNum();
		// 写EXCEL数据区域内容
		int colNum = 0;
		eh.writeExcelTemplate(sheet, rowStart + 1,colNum, data1);
		colNum += 2;
		eh.writeExcelTemplate(sheet, rowStart + 1,colNum, data2);
		colNum += 2;
		eh.writeExcelTemplate(sheet, rowStart + 1,colNum, data3);
		colNum += 6;
		eh.writeExcelTemplate(sheet, rowStart + 1,colNum, data4);
		colNum += 4;
		eh.writeExcelTemplate(sheet, rowStart + 1,colNum, data5);
		colNum += 2;
		eh.writeExcelTemplate(sheet, rowStart + 1,colNum, data6);
		colNum += 2;
		eh.writeExcelTemplate(sheet, rowStart + 1,colNum, data7);
		
		if(null!=data8&&data8.length>0){
			colNum += 2;
			eh.writeExcelTemplate(sheet, rowStart + 1,colNum, data8);
		}
		if(null!=data9&&data9.length>0){
			colNum += 2;
			eh.writeExcelTemplate(sheet, rowStart + 1,colNum, data9);
		}
		
		if(null!=data10&&data10.length>0){
			colNum += 2;
			eh.writeExcelTemplate(sheet, rowStart + 1,colNum, data10);
		}
		
		if(null!=data11&&data11.length>0){
			colNum += 2;
			eh.writeExcelTemplate(sheet, rowStart + 1,colNum, data11);
		}
		if(null!=data12&&data12.length>0){
			colNum += 2;
			eh.writeExcelTemplate(sheet, rowStart + 1,colNum, data12);
		}
		if(null!=data13&&data13.length>0){
			colNum += 2;
			eh.writeExcelTemplate(sheet, rowStart + 1,colNum, data13);
		}
		return wb;
	}
	
	
	
	/**
	 * 
	 * <pre>
	 *  写Excel内容
	 * </pre>
	 *
	 * @param wb
	 * @param sheetIndex
	 * @return
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException 
	 */
	public  static Workbook writeExcelTemplate_Quotation(File templateFile, int sheetIndex,String[][] data1)
			throws InvalidFormatException, IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {

		InputStream in  = new FileInputStream(templateFile);
		Workbook wb = WorkbookFactory.create(in);
		ExcelUtil eh = new ExcelUtil();
		// 遍历Excel Sheet， 依次读取里面的内容
		if (sheetIndex > wb.getNumberOfSheets()) {
			return null;
		}
		Sheet sheet = wb.getSheetAt(sheetIndex);
		// 遍历表格的每一行
		int rowStart = sheet.getFirstRowNum();
		// 写EXCEL数据区域内容
		int colNum = 0;
		if(null!=data1&&data1.length>0){
			eh.writeExcelTemplate(sheet, rowStart + 1,colNum, data1);			
		}
		return wb;
	}
	
	
	/**
	 * 
	 * <pre>
	 * 写入excel模板
	 * </pre>
	 *
	 * @param rowIndex
	 * @param colIndex
	 * @param datas
	 */
	public void writeExcelTemplate(Sheet sheet,int rowIndex,int colIndex,String [] [] datas){
		if(null!=datas){
			//写入数据到模板中
			for(int i = 0 ;i < datas.length; i++){
				Row row = sheet.getRow(i+1);
				if(null == row ){
					row = sheet.createRow(i + 1);
				}
				for(int j= 0 ; j< datas[i].length; j ++){
					Cell cell = row.createCell((short)(colIndex+j));
					cell.setCellValue(datas[i][j]);
				}
			}
		}
	}

	/**
	 * 
	 * <pre>
	 * 解析EXCEL数据区域内容
	 * </pre>
	 *
	 * @param sheet
	 * @param rowStart
	 * @param rowEnd
	 */
	private void parseExcelData(Sheet sheet, int rowStart, int rowEnd,Workbook wb) {
		datas = new String[rowEnd][lastColumnIndex];
		for (int rowIndex = rowStart; rowIndex <= rowEnd; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			if(row==null){
				continue;
			}
			int rowNumber = rowIndex - rowStart;
			// 读取遍历每一行中的每一列
			for (int columnIndex = 0; columnIndex < lastColumnIndex; columnIndex++) {
				Cell cell = row.getCell(columnIndex, Row.RETURN_BLANK_AS_NULL);
				String value = getCellValue(cell).trim();
				datas[rowNumber][columnIndex] = value;
			}
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 读取每个单元格中的内容
	 * </pre>
	 *
	 * @param cell
	 * @return
	 */
	private String getCellValue(Cell cell) {
		// 如果单元格为空的，则返回空字符串
		if (cell == null) {
			return "";
		}
		String value = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			value = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				value = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(cell
						.getDateCellValue());
			}else {
				DecimalFormat df = new DecimalFormat("0");  
				String temp = cell.getNumericCellValue()+"";
				String formatTemp = df.format(cell.getNumericCellValue());  
				if(!(temp+ "").endsWith(".0")&&temp.indexOf("E")==-1){
					value = cell.getNumericCellValue()+ "";
				}else{
					value = formatTemp;
				}
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			value = cell.getBooleanCellValue() ? "TRUE" : "FALSE";
			break;
		case Cell.CELL_TYPE_FORMULA: //支持函数运算例如：vlookup
			 cell.setCellType(Cell.CELL_TYPE_STRING);
			 value = cell.getStringCellValue();
			break;
		default:
		}
		return value;
	}

	/**
	 * 
	 * <pre>
	 * 转换驼峰命名方式
	 * </pre>
	 *
	 * @param name
	 * @return
	 */
	private String _toCapitalizeCamelCase(String name) {
		if (name == null) {
            return null;
        }
		//name = name.toLowerCase();
        StringBuilder sb = new StringBuilder(name.length());
        boolean upperCase = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);

            if (c == '_') {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        name = sb.toString();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	/**
	 * 
	 * <pre>
	 * 列名在列标题中的索引
	 * </pre>
	 *
	 * @param columnName
	 * @return
	 */
	private int indexOfHeader(String columnName) {
		for (int i = 0; i < headers.length; i++) {
			if (headers[i].equals(columnName)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 
	 * <pre>
	 *  功能说明： 获取表格数据二维表
	 * </pre>
	 *
	 * @return
	*/
	public String [][] getDatas(){
		return datas;
	}
	
	/** 
	 * <pre>
	 *  功能说明： 获取表格标题
	 * </pre>
	 *
	 * @return
	 * 
	*/
	public String [] getHeaders(){
		return headers;
	}

	/**
	 * 
	 * <pre>
	 *   Excel实体字段类（内部类）
	 * </pre>
	 *
	 * @author szy
	 * @version 0.0.1
	 */
	private class ExcelEntityField {
		private String columnName;
		private boolean required;
		private Field field;
		private int index;
		private ExcelProperty annotation;
		private int isSuper ; 

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		public boolean isRequired() {
			return required;
		}

		public void setRequired(boolean required) {
			this.required = required;
		}

		public Field getField() {
			return field;
		}

		public void setField(Field field) {
			this.field = field;
		}

		public ExcelProperty getAnnotation() {
			return annotation;
		}

		public void setAnnotation(ExcelProperty annotation) {
			this.annotation = annotation;
		}

		/**
		 * @return the isSuper
		 */
		public int getIsSuper() {
			return isSuper;
		}

		/**
		 * @param isSuper the isSuper to set
		 */
		public void setIsSuper(int isSuper) {
			this.isSuper = isSuper;
		}
	}
}
