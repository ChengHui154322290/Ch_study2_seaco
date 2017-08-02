package com.tp.util;

import java.io.File;

public class ResourceUtil {
	
	public static File getResourceFile(String resourceName) {
		String path = ResourceUtil.class.getResource("/").getFile();
		String root = null;
		if (path.lastIndexOf("/WEB-INF/")>0) {
			root = path.substring(0, path.lastIndexOf("/WEB-INF/"));
		}else if (path.lastIndexOf("/target/classes/")>0){
			root = path.substring(0, path.lastIndexOf("/target/classes/"));
		}else if (path.lastIndexOf("/build/classes/")>0){
			root = path.substring(0, path.lastIndexOf("/build/classes/"));
		}else if (path.lastIndexOf("/bin/")>0){
			root = path.substring(0, path.lastIndexOf("/bin/"));
		}
		return new File(root + resourceName);
	}
	/**
	 * 
	 * @param resourceName
	 * @return
	 * @author szy
	 */
	public static String getResourceFileName(String resourceName) {
		String path = ResourceUtil.class.getResource("/").getFile();
		String root = null;
		if (path.lastIndexOf("/WEB-INF/")>0) {
			root = path.substring(0, path.lastIndexOf("/WEB-INF/"));
		}else if (path.lastIndexOf("/target/classes/")>0){
			root = path.substring(0, path.lastIndexOf("/target/classes/"));
		}else if (path.lastIndexOf("/build/classes/")>0){
			root = path.substring(0, path.lastIndexOf("/build/classes/"));
		}else if (path.lastIndexOf("/bin/")>0){
			root = path.substring(0, path.lastIndexOf("/bin/"));
		}
		return  root + resourceName;
	}
}
