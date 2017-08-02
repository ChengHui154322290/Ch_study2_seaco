package com.tp.proxy.prd;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import com.tp.common.vo.Constant;

/**
 * sxssf实现
 * 
 * @author szy
 * @version 0.0.1
 */
@Service
public class XSSFExcelWriter<T> implements ExcelWriter<T>{
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(XSSFExcelWriter.class);
	
	/** 临时文件夹 */
	private String tempFolder;
	/** 模版路径 */
	private String templateLocation;
	/** 跳过的行数 */
	private Integer skipRows = 0;
	/** 位置列表 */
	private List<String> positionList;
	
	// 数据类型
	private Class<T> modelClass;

	@Override
	public void write(List<T> dataList, OutputStream os) throws Exception {
		Assert.notNull(dataList);
		Assert.notNull(os);
		
		XSSFWorkbook wb = createWorkbook();
		Sheet sh = wb.getSheetAt(0);	// 获取第一页
		XSSFCellStyle cellStyle=wb.createCellStyle();     
		cellStyle.setWrapText(true);  
		if (dataList.size() > 0) {
			Map<String, PropertyDescriptor> propMap = getPropertyMap(dataList);
			
			for (int rownum = 0; rownum < dataList.size(); rownum++) {
				Row row = sh.createRow(rownum + skipRows);	// 为标题行让位
				
				for (int cellnum = 0; cellnum < positionList.size(); cellnum++) {
					Cell cell = row.createCell(cellnum);
					cell.setCellStyle(cellStyle); 
					try {
						PropertyDescriptor prop = propMap.get(positionList.get(cellnum));
						if (null == prop)	throw new NotReadablePropertyException(modelClass, positionList.get(cellnum));
						
						Object propVal = propMap.get(positionList.get(cellnum)).getReadMethod().invoke(dataList.get(rownum));
						String cellValue = null;
						if (null != propVal) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							if (propVal instanceof Date) {	// 日期
								cellValue = new SimpleDateFormat(Constant.DATE_TIME_FORMAT.DATE_FORMAT).format(propVal);
							} else {
								cellValue = propVal.toString();
							}
						}
						cell.setCellValue(cellValue);
						   
						
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new Exception("excel写入数据异常", e);
					}
				}
			}
		}
		
		try {
			wb.write(os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 创建临时文件夹
	private File createTempFolder() throws Exception {
		notBlank(tempFolder, "tempFolder");
		
		File folder = new File(tempFolder);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return folder;
	}
	
	// 获取模版
	private File getTemplate() throws Exception {
		notBlank(templateLocation, "templateLocation");
		
		try {
			return ResourceUtils.getFile(templateLocation);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("该excel模版[" + templateLocation + "]不存在");
		}
	}
	
	// 创建临时文件
	private File createTempFile(File folder) throws IOException {
		try {
			File tmp = File.createTempFile("temp", ".xlsx", folder);
			tmp.deleteOnExit();	// jvm退出时自动删除
			return tmp;
		} catch (IOException e) {
			throw new IOException("创建临时文件异常", e);
		}
	}

	// 创建工作薄
	private XSSFWorkbook createWorkbook() throws Exception {
		File folder = createTempFolder();	// 临时文件夹
		File template = getTemplate();	// 模版
		File tmp = createTempFile(folder);	// 临时文件
		
		try {
			FileUtils.copyFile(template, tmp);
			return new XSSFWorkbook(new FileInputStream(tmp));
		} catch (IOException e) {
			throw new IOException("创建工作薄异常", e);
		}
	}
	
	// 获取属性映射
	@SuppressWarnings("unchecked")
	private Map<String, PropertyDescriptor> getPropertyMap(List<T> dataList) {
		modelClass = (Class<T>) dataList.get(0).getClass();
		PropertyDescriptor[] propArr = ReflectUtils.getBeanGetters(modelClass);
		Map<String, PropertyDescriptor> propMap = new HashMap<String, PropertyDescriptor>();
		for (PropertyDescriptor prop : propArr) {
			propMap.put(prop.getName(), prop);
		}
		return propMap;
	}
	
	private void notBlank(String str, String paramName) throws Exception {
		if (StringUtils.isBlank(templateLocation))	throw new Exception("[" + paramName + "]必须配置，不能为空");
	}
	
	public String getTempFolder() {
		return tempFolder;
	}

	public void setTempFolder(String tempFolder) {
		this.tempFolder = tempFolder;
	}

	public String getTemplateLocation() {
		return templateLocation;
	}

	public void setTemplateLocation(String templateLocation) {
		this.templateLocation = templateLocation;
	}

	public Integer getSkipRows() {
		return skipRows;
	}

	public void setSkipRows(Integer skipRows) {
		this.skipRows = skipRows;
	}

	public List<String> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<String> positionList) {
		this.positionList = positionList;
	}
	
}
