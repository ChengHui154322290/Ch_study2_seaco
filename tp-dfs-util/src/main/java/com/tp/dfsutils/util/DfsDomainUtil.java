package com.tp.dfsutils.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author longhaisheng
 *
 */
public class DfsDomainUtil {

	private Map<String, String> domainMap = new HashMap<String, String>();

	private List<String> startGroups = new ArrayList<String>();

	/**
	 * 根据图片名称取得带域名的URL
	 * 
	 * @param url
	 *            图片url:如
	 *            group1/M00/00/0B/rBABm1S7nbqAMcK8AE6PY3fCE94253_640.jpg
	 *         0B/rBABm1S7nbqAMcK8AE6PY3fCE94253_640.jpg
	 */
	public String getFileFullUrl(String url) {
		if (null != url && !"".equals(url.trim()) && !startGroups.isEmpty()) {
			for (String groupName : startGroups) {
				if (null != groupName && !"".equals(groupName.trim()) && url.startsWith(groupName)) {
					String domainUrls = domainMap.get(groupName);
					String httpStr = getRandomString(domainUrls).trim();
					if (null != httpStr && !"".equals(httpStr) && httpStr.startsWith("http://")) {
						if (httpStr.endsWith("/")) {
							return httpStr + url;
						} else {
							return httpStr + "/" + url;
						}
					} else {
						if (httpStr.endsWith("/")) {
							return "http://" + httpStr + url;
						} else {
							return "http://" + httpStr + "/" + url;
						}
					}
				}
			}
		}
		return url;
	}

	/**
	 * 根据图片名称取得带域名的URL
	 * 
	 * @param url
	 *            图片url:如
	 *            group1/M00/00/0B/rBABm1S7nbqAMcK8AE6PY3fCE94253_640.jpg
	 * @return  支持https的返回，如果图片域名配置不以http://或https://开头的话 将返回       //img03.51seaco.com/group1/M00/00/
	 *         0B/rBABm1S7nbqAMcK8AE6PY3fCE94253_640.jpg 这样的路径
	 *         
	 */
	public String getFileFullUrlNew(String url) {
		if (null != url && !"".equals(url.trim()) && !startGroups.isEmpty()) {
			for (String groupName : startGroups) {
				if (null != groupName && !"".equals(groupName.trim()) && url.startsWith(groupName)) {
					String domainUrls = domainMap.get(groupName);
					String httpStr = getRandomString(domainUrls).trim();
					if (null != httpStr && !"".equals(httpStr) && (httpStr.startsWith("http://") || httpStr.startsWith("https://"))) {
						if (httpStr.endsWith("/")) {
							return httpStr + url;
						} else {
							return httpStr + "/" + url;
						}
					} else {
						if (httpStr.endsWith("/")) {
							return "//" + httpStr + url;
						} else {
							return "//" + httpStr + "/" + url;
						}
					}
				}
			}
		}
		return url;
	}

	private String getFileSnapshotUrl(String url, int width) {
		if (null != url && !"".equals(url.trim()) && url.contains(".") && width > 0) {
			String[] urls = url.split("\\.");
			String prefix = urls[0];
			String suffix = urls[1];
			return prefix + "_" + width + "." + suffix;
		}
		return url;
	}

	/**
	 * 根据图片名称 和宽度 取得 带缩略图 域名的URL
	 * 
	 * @param url
	 *            图片url:如 group1/M00/00/0B/a.jpg
	 * @param width
	 *            宽度 640
	 */
	public String getSnapshotUrl(String url, int width) {
		return getFileFullUrl(getFileSnapshotUrl(url, width));

	}

	private final static String getRandomString(String str) {
		if (null != str && !"".equals(str.trim()) && str.contains(",")) {
			String[] strArr = str.split(",");
			int len = strArr.length;
			int rand = (int) (Math.random() * len);
			return strArr[rand];
		}
		return str;
	}

	public Map<String, String> getDomainMap() {
		return domainMap;
	}

	public void setDomainMap(Map<String, String> domainMap) {
		this.domainMap = domainMap;
	}

	public List<String> getStartGroups() {
		return startGroups;
	}

	public void setStartGroups(List<String> startGroups) {
		this.startGroups = startGroups;
	}

	public static void main(String[] args) {
		DfsDomainUtil dfsDomainUtil = new DfsDomainUtil();

		Map<String, String> domainMap = getMap();
		List<String> startGroups = getList();

		dfsDomainUtil.setDomainMap(domainMap);
		dfsDomainUtil.setStartGroups(startGroups);
		System.out.println(dfsDomainUtil.getFileFullUrl("group1/M00/00/0B/rBABm1S7nbqAMcK8AE6PY3fCE94253_640.jpg"));
		System.out.println(dfsDomainUtil.getFileFullUrl("group2/M00/00/01/rBABnFS8noOAEfdqAE6PY3fCE94632.jpg"));

		System.out.println(dfsDomainUtil.getSnapshotUrl("group1/M00/00/a.jpg", 40));

	}

	static List<String> getList() {
		List<String> startGroups = new ArrayList<String>();
		startGroups.add("group1");
		startGroups.add("group2");
		return startGroups;
	}

	static Map<String, String> getMap() {
		Map<String, String> domainMap = new HashMap<String, String>();
		return domainMap;
	}

}
