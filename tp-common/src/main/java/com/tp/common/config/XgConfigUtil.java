package com.tp.common.config;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.PriorityOrdered;

public class XgConfigUtil  implements BeanFactoryPostProcessor, PriorityOrdered{
	
	private static final Logger log = LoggerFactory.getLogger(XgConfigUtil.class);
	private static final String PROPERTIES_ROOT_DIR = "/usr/local/develop/properties/";
	private String appConfigDir;
	
	public void init() {
		log.info("{} 获取配置文件",appConfigDir);
		File file = new File(PROPERTIES_ROOT_DIR+appConfigDir+"_tomcat");
		if(file.exists() && file.isDirectory()){
			log.info("{} exists!",file.getAbsolutePath());
			String path = getAppClassesPath();
			File[] configs = file.listFiles();
			if(configs!=null && configs.length>0){
				for(File configFile:configs){
					try {
						log.info("要覆盖的文件是{}",path+configFile.getName());
						FileUtils.copyFile(configFile, new File(path+configFile.getName()));
					} catch (IOException e) {
						log.error("{} copyFile to {} error! info exception:",configFile,path+configFile.getName(),e);
					}
				}
			}
		}
	}
	
	private String getAppClassesPath() {
		String osName = System.getProperty("os.name");
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		log.info("加载器文件路径是{}",path);
		if (osName.toLowerCase().startsWith("windows")) {
			path = path.substring(1);
		}
		return path;
		/**
		String[] arrPath = path.split("/");
		int index = 0;
		for (String str : arrPath) {
			if (str.startsWith("webapps")) {
				index = path.indexOf(str);
			}
		}
		return path.substring(0, index);
		*/
	}
	
	public int getOrder() {
		return 0;
	}

	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}

	public String getAppConfigDir() {
		return appConfigDir;
	}

	public void setAppConfigDir(String appConfigDir) {
		this.appConfigDir = appConfigDir;
	}

}
