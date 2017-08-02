package com.tp.ptm.ao.item;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadfontUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(LoadfontUtils.class);
	/** 微软雅黑 */
	public static final String FONT_MSYH="MSYH";
	private static final String RESOURCE_MSYH = "/fonts/MSYH.TTF";
	/** 微软雅黑粗体 */
	public static final String FONT_MSYHBD="MSYHBD";
	private static final String RESOURCE_MSYHBD = "/fonts/MSYHBD.TTF";
	/** 仿宋 */
	public static final String FONT_SIMFANG="SIMFANG";
	private static final String RESOURCE_SIMFANG = "/fonts/SIMFANG.TTF";
	/** 黑体 */
	public static final String FONT_SIMHEI="SIMHEI";
	private static final String RESOURCE_SIMHEI = "/fonts/SIMHEI.TTF";
	/** 新宋体 */
	public static final String FONT_SIMSUN="SIMSUN";
	private static final String RESOURCE_SIMSUN = "/fonts/SIMSUN.TTC";
	/**华文宋体 */
	public static final String FONT_STSONG="STSONG";
	private static final String RESOURCE_STSONG = "/fonts/STSONG.TTF";
	/**华文细黑 */
	public static final String FONT_STXIHEI="STXIHEI";
	private static final String RESOURCE_STXIHEI = "/fonts/STXIHEI.TTF";

	private static Map<String, Font> fontMap;
	
	private static Map<String, String> fontResources;
	
	static{
		fontResources = new HashMap<String, String>();
		fontResources.put(FONT_MSYH, RESOURCE_MSYH);
		fontResources.put(FONT_MSYHBD, RESOURCE_MSYHBD);
		fontResources.put(FONT_SIMFANG, RESOURCE_SIMFANG);
		fontResources.put(FONT_SIMHEI, RESOURCE_SIMHEI);
		fontResources.put(FONT_SIMSUN, RESOURCE_SIMSUN);
		fontResources.put(FONT_STSONG, RESOURCE_STSONG);
		fontResources.put(FONT_STXIHEI, RESOURCE_STXIHEI);
	}
	
	private LoadfontUtils (){}
	
	/**
	 * 获得字体,默认为微软雅黑
	 * 
	 * @return
	 */
	public static Font loadFontByName(String name) // 第一个参数是外部字体名，第二个是字体大小
	{
		if(StringUtils.isBlank(name)){
			name = FONT_MSYH;
		}
		if(fontMap==null){
			fontMap = new HashMap<String, Font>();
		}
		Font font = fontMap.get(name);
		if(null!=font){
			return font;
		}
		FileInputStream fangsongFis = null;
		try {
			String fontResource = fontResources.get(name);
			if(StringUtils.isBlank(fontResource)){
				fontResource = RESOURCE_MSYH;
			}
			URL url = LoadfontUtils.class.getResource(fontResource);
			File file = new File(url.getFile());
			fangsongFis = new FileInputStream(file);
			font = Font.createFont(Font.TRUETYPE_FONT, fangsongFis);
			fangsongFis.close();
			fontMap.put(name, font);
			return font;
		} catch (Exception e){
			logger.error(e.getMessage(), e);
			// e.printStackTrace();
			return new Font("宋体", Font.PLAIN, 14);
		} finally {
			try {
				if (null != fangsongFis) {
					fangsongFis.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
