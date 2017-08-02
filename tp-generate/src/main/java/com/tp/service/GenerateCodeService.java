package com.tp.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.dao.ColumnDao;
import com.tp.dao.TableDao;
import com.tp.generate.Constant;
import com.tp.model.Column;
import com.tp.model.Model;
import com.tp.model.Property;
import com.tp.model.Table;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class GenerateCodeService {
	
	@Value("#{settings['packageFixed']}")
	private String packageFixed;
	@Value("#{settings['notInTable']}")
	private String notInTable;
	@Value("#{settings['inTable']}")
	private String inTable;
	@Value("#{settings['schema']}")
	private String schema;
	@Value("#{settings['templatesPath']}")
	private String templatesPath;
	@Resource
	private Map<String,String> codePathMap;
	@Autowired
	private TableDao tableDao;
	@Autowired
	private ColumnDao columnDao;
	
	public void createCode(){
		List<Model> list = queryModelList();
		if(CollectionUtils.isNotEmpty(list)){
			for(Model model:list){
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("datetime", model.getDatetime());
				params.put("hasDate", model.getHasDate());
				params.put("modelFixed", model.getModelFixed());
				params.put("modelMark", model.getModelMark());
				params.put("modelName", model.getModelName());
				params.put("modelNameMin", model.getModelNameMin());
				params.put("properties", model.getProperties());
				//
				String[] templateFiles = new String[]{"Model.java","ModelDao.java","IModelService.java","ModelService.java","Model.xml","ModelProxy.java"};
				for(int i=0;i<templateFiles.length;i++){
					String filePath = codePathMap.get(templateFiles[i])+model.getModelFixed();
					File file = new File(filePath);
					if (!file.exists()) {
						try {
							file.mkdirs();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					String htmlFile=filePath+"\\"+templateFiles[i].replaceFirst("Model", model.getModelName());
					generateFile(templatesPath, templateFiles[i]+".ftl", htmlFile, params);
				}
			}
		}
	}
	
	/**
	 * 	获取实体bean信息
	 * @return
	 */
	public List<Model> queryModelList(){
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNoneBlank(notInTable)){
			params.put("notInTable", Arrays.asList(notInTable.split(",")));
		}
		if(StringUtils.isNoneBlank(inTable)){
			params.put("inTable", Arrays.asList(inTable.split(",")));
		}
		params.put("schema", schema);
		List<Table> tables = tableDao.queryListBySchema(params);
		List<Column> columns = columnDao.queryListByParams(params);
		Map<String,List<Column>> columnMap = new HashMap<String,List<Column>>();
		if(CollectionUtils.isEmpty(columns)){
			throw new RuntimeException("没有查询到相关数据"+params);
		}
		for(Column column:columns){
			List<Column> columnList = columnMap.get(column.getTableName());
			if(columnList==null){
				columnList = new ArrayList<Column>();
			}
			columnList.add(column);
			columnMap.put(column.getTableName(), columnList);
		}
		if(CollectionUtils.isNotEmpty(tables)){
			List<Model> modelList = new ArrayList<Model>();
			for(Table table:tables){
				Model model = new Model();
				String name = table.getTableName();
				String comment = table.getTableComment();
				model.setModelFixed(name.toLowerCase().split("_")[0]);
				model.setModelMark(comment);
				String[] names = name.split("_");
				StringBuffer clazz=new StringBuffer();
				for(int i=1;i<names.length;i++){
					clazz.append(names[i].substring(0,1).toUpperCase())
					     .append(names[i].substring(1).toLowerCase());
				}
				if(clazz.length()==0){
					continue;
				}
				model.setModelName(clazz.toString());
				model.setModelNameMin(clazz.substring(0,1).toLowerCase()+clazz.substring(1));
				List<Column> columnList = columnMap.get(name);
				if(CollectionUtils.isEmpty(columnList)){
					throw new RuntimeException("没有查询到相关数据"+name);
				}
				for(Column column:columnList){
					Property property = new Property();
					property.setColumn(column.getColumnName());
					property.setMark(column.getColumnComment()+" 数据类型"+column.getColumnType());
					String[] cls = column.getColumnName().split("_");
					StringBuffer objName=new StringBuffer();
					for(int i=0;i<cls.length;i++){
						objName.append(cls[i].substring(0,1).toUpperCase())
						     .append(cls[i].substring(1).toLowerCase());
					}
					property.setMethodName(objName.toString());
					property.setName(property.getMethodName().substring(0,1).toLowerCase()+property.getMethodName().substring(1));
					property.setType(Constant.COLUMN_PROPERTY_TYPE_MAP.get(column.getDataType()));
					property.setPrimary("PRI".equalsIgnoreCase(column.getColumnKey()));
					model.getProperties().add(property);
				}
				modelList.add(model);
			}
			return modelList;
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static void generateFile(String templatePath, String templateName, String fileName, Map<?, ?> root) {
		try {
			Configuration config = new Configuration();
			config.setDirectoryForTemplateLoading(new File(templatePath));
			config.setObjectWrapper(new DefaultObjectWrapper());
			Template template = config.getTemplate(templateName, "UTF-8");
			FileOutputStream fos = new FileOutputStream(fileName);
			Writer out = new OutputStreamWriter(fos, "UTF-8");
			template.process(root, out);
			out.flush();
			out.close();
		} catch (IOException e) {
		} catch (TemplateException e) {
		}
	}
	
	public static void createJaveSourceFile(String path, String fileName, String writeString) throws IOException {
		String allPath = path + File.separator + fileName;
		File file = new File(allPath);
		if (!file.exists()) {
			file.createNewFile();
		}
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		out.write(writeString.getBytes());
		out.flush();
		out.close();
	}
}
