package com.tp.dto.usr;

import java.io.Serializable;
import java.util.List;

import com.tp.util.StringUtil;



public class Tree implements Serializable{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	
	private String isFolder;
	
	private String key;
	
	private String isLazy;
	
	private String expand;
	

	public Tree(String title, String isFolder, String key, String isLazy,
			String expand) {
		super();
		this.title = title;
		this.isFolder = isFolder;
		this.key = key;
		this.isLazy = isLazy;
		this.expand = expand;
	}

	
	public String getExpand() {
		return expand;
	}


	public void setExpand(String expand) {
		this.expand = expand;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	
	public String getIsFolder() {
		return isFolder;
	}

	public void setIsFolder(String isFolder) {
		this.isFolder = isFolder;
	}

	public String getIsLazy() {
		return isLazy;
	}

	public void setIsLazy(String isLazy) {
		this.isLazy = isLazy;
	}

	public static String toJson(List<Tree> trees){
		String json = "[";
		if(null != trees&&!trees.isEmpty()){
			for (int i=0;i<trees.size();i++) {
				Tree tree = trees.get(i);
				json += "{"+StringUtil.l("title")+" : "+StringUtil.l(tree.getTitle())+",";
				json += StringUtil.l("isFolder")+" : "+StringUtil.l(tree.getIsFolder())+",";
				json += StringUtil.l("expand")+" : "+StringUtil.l(tree.getExpand())+",";
				json += StringUtil.l("key")+" : "+StringUtil.l(tree.getKey())+",";
				json += StringUtil.l("isLazy")+" :" + StringUtil.l(tree.getIsLazy());
				json +="}";
				if(i+1 != trees.size()) json +=",";
			}
			
		}
		return json+"]";
		
	}
	
	
}
