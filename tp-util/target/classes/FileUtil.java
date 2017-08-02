package com.jtx.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;

public class FileUtil {

	private static Logger log = Logger.getLogger(FileUtil.class);

	private static final int LOCAL_FILE_NUM = 5;

	public static boolean writeFile(String filePath, String content) {
		FileOutputStream outputStream = null;
		FileLock fileLock = null;
		FileChannel channel = null;
		try {

			String folderPath = filePath.substring(0, filePath.lastIndexOf("/"));
			File file = new File(folderPath);
			mkDir(file);
			File configFile = new File(filePath);
			outputStream = new FileOutputStream(configFile);
			channel = outputStream.getChannel();
			fileLock = channel.lock();
			outputStream.write(content.getBytes());
			outputStream.flush();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (fileLock != null) {
				try {
					fileLock.release();
					fileLock = null;
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
			if (channel != null) {
				try {
					channel.close();
					channel = null;
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		writeFileTrimLine(filePath, filePath);
		return true;
	}

	public static void mkDir(File file) {
		if (file.getParentFile().exists()) {
			file.mkdir();
		} else {
			mkDir(file.getParentFile());
			file.mkdir();
		}
	}

	public static void copyFile(String sourceFilePath, String retFilePath) {
		File sourceFile = new File(sourceFilePath);
		File retFile = new File(retFilePath);
		try {
			FileCopyUtils.copy(sourceFile, retFile);
		} catch (IOException e) {

		} catch (Exception e) {

		}
	}

	private static void sortStringList(List<String> stringList) {
		Collections.sort(stringList, new Comparator<String>() {
			public int compare(String o1, String o2) {
				String str1 = (String) o1;
				String str2 = (String) o2;
				return str1.compareTo(str2);
			}
		});
	}

	private static List<String> listFiles(String path) {
		List<String> list = new ArrayList<String>();
		File directory = new File(path);
		File[] directoryFiles = directory.listFiles();
		if (!ArrayUtils.isEmpty(directoryFiles)) {
			for (File file : directoryFiles) {
				if (file.isDirectory()) {
					List<String> l = listFiles(path + file.getName() + "/");
					list.addAll(l);
				} else {
					if (file.getName().contains(".properties")) {
						list.add(path + file.getName());
					}
				}
			}
		}
		return list;
	}

	private static Map<String, List<String>> getLocalConfigFilesMaps(String path) {
		List<String> list = listFiles(path);
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		if (!list.isEmpty()) {
			for (String str : list) {
				String substring = str.substring(path.length());
				String[] strArr = substring.split("_");
				String key = strArr[0];
				List<String> values = map.get(key);
				if (null == values || values.isEmpty()) {
					values = new ArrayList<String>();
				}
				values.add(str);
				map.put(key, values);
			}
		}
		return map;
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static Map<String, String> getLocalAllConfigFileMap(String path) {
		Map<String, List<String>> map = getLocalConfigFilesMaps(path);
		Map<String, String> returnMap = new HashMap<String, String>();
		if (!map.isEmpty()) {
			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
				List<String> values = entry.getValue();
				sortStringList(values);
				int index = values.size() - 1;
				String filePath = values.get(index);
				returnMap.put(entry.getKey(), filePath);
			}
		}
		return returnMap;
	}

	public static boolean setBackFileList(String backFolder) {
		
		File backFileFolder = new File(backFolder);
		File[] fileArray = backFileFolder.listFiles();
		List<String> allFiles = new ArrayList<String>();
		
		if (!ArrayUtils.isEmpty(fileArray)) {
			for (File file : fileArray) {
				if (file.isFile()) {
					allFiles.add(file.getName());
				}
			}
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			for (String str : allFiles) {
				if (str.contains("_")) {
					String[] arr = str.split("_");
					if (arr.length > 0) {
						String key = arr[0];
						if (map.containsKey(key)) {
							map.get(key).add(str);
						} else {
							List<String> fileNameList = new ArrayList<String>();
							fileNameList.add(str);
							map.put(key, fileNameList);
						}
					}
				}
			}

			if (!map.isEmpty()) {
				for (Map.Entry<String, List<String>> entry : map.entrySet()) {
					List<String> fileList = entry.getValue();
					sortStringList(fileList);
					int size = fileList.size();
					if (size > LOCAL_FILE_NUM) {
						List<String> removeList = fileList.subList(0, size - LOCAL_FILE_NUM);
						for (String str : removeList) {
							File f = new File(backFolder + str);
							if (f.exists()) {
								f.delete();
							}
						}
						fileList.removeAll(removeList);
					}
				}
			}
		}
		
		return true;
	}

	public static String getFolder(String fullTargetFilePath) {
		return fullTargetFilePath.substring(0, fullTargetFilePath.lastIndexOf("/") + 1);
	}

	public static String genrateBackFileName(String filePath) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String format = sdf.format(new Date());
		return filePath + "_" + format;
	}

	public static List<String> readFileByLines(String fileName) {
		List<String> set = new ArrayList<String>();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			while ((tempString = reader.readLine()) != null) {
				if (null != tempString && !"".equals(tempString) && !tempString.startsWith("#")) {
					set.add(tempString.trim());
				}
				line++;
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {

				}
			}
		}
		return set;
	}

	public static void writeFileTrimLine(String source_file_path, String ret_file_path) {
		List<String> list = readFileByLines(source_file_path);
		Iterator<String> iterator = list.iterator();
		File file = new File(ret_file_path);
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			fw = new FileWriter(file);
			writer = new BufferedWriter(fw);
			while (iterator.hasNext()) {
				writer.write(iterator.next());
				writer.newLine();
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {

			}
		}
	}

	public static void main(String args[]) {
		// String source_file_path =
		// "D:\\configserver\\configserver-web\\src\\main\\resources\\config\\config.properties";
		// String ret_file_path =
		// "D:\\configserver\\configserver-web\\src\\main\\resources\\config\\config.txt";
		// writeFileTrimLine(source_file_path, source_file_path);
		//setBackFileList("D:/abc/config/");
	}

}
